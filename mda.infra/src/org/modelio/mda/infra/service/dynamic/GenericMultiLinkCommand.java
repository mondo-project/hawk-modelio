/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */  
                                    

package org.modelio.mda.infra.service.dynamic;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.ILinkPath;
import org.modelio.api.diagram.dg.IDiagramDG;
import org.modelio.api.diagram.tools.DefaultMultiLinkCommand;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.model.ITransaction;
import org.modelio.api.model.IUmlModel;
import org.modelio.api.modelio.Modelio;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("c7dff8f3-3c32-41a7-a2af-eb49fbe34f11")
class GenericMultiLinkCommand extends DefaultMultiLinkCommand {
    @objid ("06713a74-30fc-48dc-baa2-f1a832982e97")
    private List<GenericScope> sources;

    @objid ("a89995a1-e34e-4a2d-b216-e0a5c8bd36f5")
    private GenericHandler handler;

    @objid ("2b87722b-6ffc-4f6c-8da0-0aa60208c927")
    public GenericMultiLinkCommand(final String name, final ImageDescriptor bitmap, final String tooltip, final GenericHandler handler, final List<GenericScope> sources) {
        super(name, bitmap, tooltip);
        this.handler = handler;
        this.sources = sources;
    }

    @objid ("292c6e69-219b-4e12-a4e7-4c0d1378aafd")
    @Override
    public boolean acceptFirstElement(final IDiagramHandle diagramHandle, final IDiagramGraphic targetNode) {
        ModelElement owner = null;
        
        if (targetNode instanceof IDiagramDG) {
            owner = diagramHandle.getDiagram().getOrigin();
        } else {
            owner = (ModelElement) targetNode.getElement();
        }
        
        for (GenericScope aScope : this.sources) {
            boolean result = true;
        
            String metaclass = aScope.getMetaclass();
            String stereotype = aScope.getStereotype();
        
            Class<? extends MObject> metaclassClass = Metamodel.getJavaInterface(Metamodel.getMClass(metaclass));
            if (metaclass != null) {
                result = result && metaclassClass.isAssignableFrom(owner.getClass());
            }
            if (stereotype != null) {
                result = result && isStereotyped(owner, stereotype);
            }
        
            if (result)
                return true;
        }
        return false;
    }

    @objid ("15b2b58d-a42f-407b-aa4a-5ec3c6b70c8e")
    private boolean isStereotyped(ModelElement elt, String stereotypeName) {
        for (Stereotype stereotype : elt.getExtension()) {
            if (stereotype.getName().equals(stereotypeName)) {
                return true;
            }
        }
        return false;
    }

    @objid ("dd5fd3ed-892e-4399-ac4e-a6e381412613")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic lastNode, List<IDiagramGraphic> otherNodes, List<LinkRouterKind> routerKinds, List<ILinkPath> paths, Rectangle rectangle) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        try (ITransaction tr = session.createTransaction("Create multi link")) {
            if (this.handler != null && this.handler.getMetaclass() != null) {
                MObject newElement =  null;
                IUmlModel modelFactory = session.getModel();
        
        
                if(this.handler.getMetaclass().equals("NaryAssociation")){
                    List<Classifier> ends = new ArrayList<>();
                    ends.add((Classifier) lastNode.getElement());
                    for (IDiagramGraphic node : otherNodes) {
                        ends.add((Classifier) node.getElement());
                    }
                    
                    newElement = modelFactory.createNaryAssociation(ends);
                }else if(this.handler.getMetaclass().equals("NaryConnector")){
                    List<BindableInstance> ends = new ArrayList<>();
                    ends.add((BindableInstance) lastNode.getElement());
                    for (IDiagramGraphic node : otherNodes) {
                        ends.add((BindableInstance) node.getElement());
                    }
                    
                    newElement = modelFactory.createNaryConnector(ends);
                }else if(this.handler.getMetaclass().equals("NaryLink")){
                    List<Instance> ends = new ArrayList<>();
                    ends.add((Instance) lastNode.getElement());
                    for (IDiagramGraphic node : otherNodes) {
                        ends.add((Instance) node.getElement());
                    }
                    
                    newElement = modelFactory.createNaryLink(ends);
                }else{
                    MdaInfra.LOG.error("Invalid metaclass : " + this.handler.getMetaclass());
                }
        
                if (newElement instanceof ModelElement) {
                    if (this.handler.getStereotype() != null) {
                        ((ModelElement) newElement).getExtension().add(session.getMetamodelExtensions().getStereotype(this.handler.getStereotype(), newElement.getMClass()));
                    }
                    ((ModelElement) newElement).setName(getLabel());
                }
        
                int i = 0;
                List<IDiagramGraphic> graph = diagramHandle.unmask(newElement,0,0);
                for (IDiagramGraphic iDiagramGraphic : graph) {
                    if (iDiagramGraphic instanceof IDiagramLink) {
                        IDiagramLink link = (IDiagramLink) iDiagramGraphic;
                        if (i < routerKinds.size()) {
                            link.setRouterKind(routerKinds.get(i));
                        }
                        if (i < paths.size()) {
                            link.setPath(paths.get(i));
                        }
                        
                        i++;
                        
                        diagramHandle.save();
                    }
                    
                }
            }
            tr.commit();
        } catch (Exception e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("25fbfe41-d507-4251-a488-1a6cea4be8ff")
    @Override
    public boolean acceptAdditionalElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> previousNodes, IDiagramGraphic targetNode) {
        ModelElement owner = null;
        
        if (targetNode instanceof IDiagramDG) {
            owner = diagramHandle.getDiagram().getOrigin();
        } else {
            owner = (ModelElement) targetNode.getElement();
        }
        
        for (GenericScope aScope : this.sources) {
            boolean result = true;
        
            String metaclass = aScope.getMetaclass();
            String stereotype = aScope.getStereotype();
        
            Class<? extends MObject> metaclassClass = Metamodel.getJavaInterface(Metamodel.getMClass(metaclass));
            if (metaclass != null) {
                result = result && metaclassClass.isAssignableFrom(owner.getClass());
            }
            if (stereotype != null) {
                result = result && isStereotyped(owner, stereotype);
            }
        
            if (result)
                return true;
        }
        return false;
    }

    @objid ("234773e2-fc2a-4140-bce1-f110f02681e6")
    @Override
    public boolean acceptLastElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> otherNodes, IDiagramGraphic targetNode) {
        ModelElement owner = null;
        
        if (targetNode instanceof IDiagramDG) {
            owner = diagramHandle.getDiagram().getOrigin();
        } else {
            owner = (ModelElement) targetNode.getElement();
        }
        
        for (GenericScope aScope : this.sources) {
            boolean result = true;
        
            String metaclass = aScope.getMetaclass();
            String stereotype = aScope.getStereotype();
        
            Class<? extends MObject> metaclassClass = Metamodel.getJavaInterface(Metamodel.getMClass(metaclass));
            if (metaclass != null) {
                result = result && metaclassClass.isAssignableFrom(owner.getClass());
            }
            if (stereotype != null) {
                result = result && isStereotyped(owner, stereotype);
            }
        
            if (result)
                return true;
        }
        return false;
    }

}

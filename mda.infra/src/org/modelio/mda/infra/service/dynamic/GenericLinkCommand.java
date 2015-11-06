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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.ILinkPath;
import org.modelio.api.diagram.dg.IDiagramDG;
import org.modelio.api.diagram.tools.DefaultLinkCommand;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.model.ITransaction;
import org.modelio.api.model.IUmlModel;
import org.modelio.api.modelio.Modelio;
import org.modelio.gproject.model.api.MTools;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("189d9f4d-c0da-4680-b7ee-06b3e9e2ba4c")
class GenericLinkCommand extends DefaultLinkCommand {
    @objid ("47db719b-4ca9-4c65-a848-9a4e3b7334c1")
    private List<GenericScope> sources;

    @objid ("4c0fbf64-58c9-41b9-a990-3007d7b7ef28")
    private GenericHandler handler;

    @objid ("900400d7-ff24-4e70-8d36-5db7aca700c7")
    private List<GenericScope> targets;

    @objid ("6c553f85-f7df-4af8-95c5-ccb10717d9ce")
    public GenericLinkCommand(final String name, final ImageDescriptor bitmap, final String tooltip, final GenericHandler handler, final List<GenericScope> sources, final List<GenericScope> targets) {
        super(name, bitmap, tooltip);
        this.handler = handler;
        this.sources = sources;
        this.targets = targets;
    }

    @objid ("3389a04b-a143-4ee0-bcf4-cfc1b2302a83")
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

    @objid ("218b8cb9-e504-49bd-83af-2943e713e0b3")
    private boolean isStereotyped(ModelElement elt, String stereotypeName) {
        for (Stereotype stereotype : elt.getExtension()) {
            if (stereotype.getName().equals(stereotypeName)) {
                return true;
            }
        }
        return false;
    }

    @objid ("aafda059-0c7c-47e2-994c-b5bee70564fd")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic originNode, final IDiagramGraphic targetNode, final LinkRouterKind routerType, final ILinkPath path) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        try (ITransaction tr = session.createTransaction("Create link")) {
            ModelElement source = (ModelElement) originNode.getElement();
            ModelElement target = (ModelElement) targetNode.getElement();
        
            if (this.handler != null && this.handler.getMetaclass() != null) {
        
                MObject newElement =  null;
                IUmlModel modelFactory = session.getModel();
        
        
                if(this.handler.getMetaclass().equals("Association")){
                    newElement = modelFactory.createAssociation((Classifier)source,(Classifier)target, getLabel());
                }else if(this.handler.getMetaclass().equals("Connector")){
                    newElement = modelFactory.createConnector((BindableInstance)source,(BindableInstance)target, getLabel());
                }else if(this.handler.getMetaclass().equals("Link")){
                    newElement = modelFactory.createLink((Instance)source,(Instance)target, getLabel());
                }else{
                    newElement = modelFactory.createElement(this.handler.getMetaclass());
                    MTools.getModelTool().setSource(newElement, null, source);
                    MTools.getModelTool().setTarget(newElement, null, target);
                }
        
                if (newElement instanceof ModelElement) {
                    if (this.handler.getStereotype() != null) {
                        ((ModelElement) newElement).getExtension().add(session.getMetamodelExtensions().getStereotype(this.handler.getStereotype(), newElement.getMClass()));
                    }
                    ((ModelElement) newElement).setName(getLabel());
                }
        
                List<IDiagramGraphic> graph = diagramHandle.unmask(newElement,0,0);
                IDiagramLink link = (IDiagramLink) graph.get(0);
                link.setRouterKind(routerType);
                link.setPath(path);
                diagramHandle.save();   
            }
            tr.commit();
        } catch (Exception e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("b3d48044-6bfb-4622-9d3d-e8a23577e824")
    @Override
    public boolean acceptSecondElement(final IDiagramHandle diagramHandle, final IDiagramGraphic originNode, final IDiagramGraphic targetNode) {
        ModelElement owner = null;
        
        if (targetNode instanceof IDiagramDG) {
            owner = diagramHandle.getDiagram().getOrigin();
        } else {
            owner = (ModelElement) targetNode.getElement();
        }
        
        
        for (GenericScope aScope : this.targets) {
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

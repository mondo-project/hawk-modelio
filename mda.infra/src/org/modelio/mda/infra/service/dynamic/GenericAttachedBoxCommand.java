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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.ILinkPath;
import org.modelio.api.diagram.dg.IDiagramDG;
import org.modelio.api.diagram.tools.DefaultAttachedBoxCommand;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.model.ITransaction;
import org.modelio.api.model.IUmlModel;
import org.modelio.api.modelio.Modelio;
import org.modelio.gproject.model.api.MTools;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("df300bf8-87a3-49f5-ab7b-ef9dfdac0fee")
class GenericAttachedBoxCommand extends DefaultAttachedBoxCommand {
    @objid ("9762b3da-7d3e-4ce5-af94-f8631c474e24")
    private List<GenericScope> scopes;

    @objid ("68a17df5-7645-4b47-937a-08a6ce9c5af1")
    private GenericHandler handler;

    @objid ("3196834c-92ad-4135-a40c-ef6bc2a6a697")
    public GenericAttachedBoxCommand(final String name, final ImageDescriptor bitmap, final String tooltip, final GenericHandler handler, final List<GenericScope> scopes) {
        super(name, bitmap, tooltip);
        this.handler = handler;
        this.scopes = scopes;
    }

    @objid ("0ad5b665-d59e-4458-b45e-afeff602a3f3")
    @Override
    public boolean acceptElement(final IDiagramHandle diagramHandle, final IDiagramGraphic targetNode) {
        ModelElement owner = null;
        
        if (targetNode instanceof IDiagramDG) {
            owner = diagramHandle.getDiagram().getOrigin();
        } else {
            owner = (ModelElement) targetNode.getElement();
        }
        
        for (GenericScope aScope : this.scopes) {
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

    @objid ("371030bb-e9e4-43e9-9429-252ce2dfa843")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic graphic, final LinkRouterKind routerType, final ILinkPath path, final Point point) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        try (ITransaction tr = session.createTransaction("GenericBoxCommand")) {
        
            ModelElement parent = null;
        
            if (graphic instanceof IDiagramDG) {
                parent = diagramHandle.getDiagram().getOrigin();
            } else {
                parent = (ModelElement) graphic.getElement();
            }
        
            if (this.handler != null && this.handler.getMetaclass() != null) {
                IUmlModel modelFactory = session.getModel();
        
                MObject newElement = modelFactory.createElement(this.handler.getMetaclass());
                
                MDependency dependency = parent.getMClass().getDependency(this.handler.relation);
                if (dependency == null) {
                    dependency = MTools.getMetaTool().getDefaultCompositionDep(parent, newElement);
                }
                
                if (dependency != null) {
                    // Append new instance of said dependency
                    parent.mGet(dependency).add(newElement);
                }
                
                if (newElement instanceof ModelElement) {
                    if (this.handler.getStereotype() != null) {
                        ((ModelElement) newElement).getExtension().add(session.getMetamodelExtensions().getStereotype(this.handler.getStereotype(), newElement.getMClass()));
                    }
                    ((ModelElement) newElement).setName(getLabel());
                }
        
                diagramHandle.unmask(newElement, point.x,point.y);
                diagramHandle.save();
            }
        
            tr.commit();       
        } catch (Exception e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("20dcf7c2-62f1-49f0-a814-fe16ed422d73")
    @Override
    public void actionPerformedInDiagram(final IDiagramHandle diagramHandle, final Rectangle rect) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        try (ITransaction tr = session.createTransaction("GenericBoxCommand")) {
        
            ModelElement parent = null;
            parent = diagramHandle.getDiagram().getOrigin();
        
            if (this.handler != null && this.handler.getMetaclass() != null) {
                IUmlModel modelFactory = session.getModel();
                MObject newElement = modelFactory.createElement(this.handler.getMetaclass(), parent, this.handler.getRelation());
        
                if (newElement instanceof ModelElement) {
                    if (this.handler.getStereotype() != null) {
                        ((ModelElement) newElement).getExtension().add(session.getMetamodelExtensions().getStereotype(this.handler.getStereotype(), newElement.getMClass()));
                    }
                    ((ModelElement) newElement).setName(getLabel());
                }
            }
        
            List<IDiagramGraphic> graph = diagramHandle.unmask(parent, rect.x,rect.y);
            if(graph.size() > 0)((IDiagramNode)graph.get(0)).setBounds(rect);
            diagramHandle.save();
        
            tr.commit();
        } catch (Exception e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("bf283326-1dd3-4229-834c-31830a69bdf8")
    private boolean isStereotyped(ModelElement elt, String stereotypeName) {
        for (Stereotype stereotype : elt.getExtension()) {
            if (stereotype.getName().equals(stereotypeName)) {
                return true;
            }
        }
        return false;
    }

}

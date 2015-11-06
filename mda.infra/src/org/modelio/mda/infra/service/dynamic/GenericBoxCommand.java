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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.dg.IDiagramDG;
import org.modelio.api.diagram.tools.DefaultBoxCommand;
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

@objid ("cdadbe69-1cce-45c0-a5a5-5c1cc6eac7da")
class GenericBoxCommand extends DefaultBoxCommand {
    @objid ("72cfe6ed-2d03-444b-8bf5-fd61c3f39029")
    private List<GenericScope> scopes;

    @objid ("1a0e8799-3ca1-4b80-a5fe-32ce367761d8")
    private GenericHandler handler;

    @objid ("d39e507a-01cc-470c-a911-bd038d24ebeb")
    public GenericBoxCommand(final String name, final ImageDescriptor bitmap, final String tooltip, final GenericHandler handler, final List<GenericScope> scopes) {
        super(name, bitmap, tooltip);
        this.handler = handler;
        this.scopes = scopes;
    }

    @objid ("754e51be-a602-4c85-829e-f1c8d34bef15")
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

    @objid ("db3e214d-b93f-4cca-b75d-4a5ad6944889")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic graphic, final Rectangle rect) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        
        try (ITransaction tr = session.createTransaction("Create box")) {
        
            ModelElement parent = null;
        
            if (graphic instanceof IDiagramDG) {
                parent = diagramHandle.getDiagram().getOrigin();
            } else {
                parent = (ModelElement) graphic.getElement();
            }
        
            if (this.handler != null && this.handler.getMetaclass() != null) {
                IUmlModel modelFactory = session.getModel();
                MObject newElement = modelFactory.createElement(this.handler.getMetaclass());
                // Get dependency by name.
                MDependency dependency = parent.getMClass().getDependency(this.handler.relation);
                if (dependency == null) {
                    dependency = MTools.getMetaTool().getDefaultCompositionDep(parent, newElement);
                }
        
                if (dependency != null) {
                    // Append new instance of said dependency
                    parent.mGet(dependency).add(newElement);
        
                    if (newElement instanceof ModelElement) {
                        if (this.handler.getStereotype() != null) {
                            ((ModelElement) newElement).getExtension().add(session.getMetamodelExtensions().getStereotype(this.handler.getStereotype(), newElement.getMClass()));
                        }
                        ((ModelElement) newElement).setName(getLabel());
                    }
                    
                    List<IDiagramGraphic> graph = diagramHandle.unmask(newElement, rect.x,rect.y);
                    if(graph.size() > 0)((IDiagramNode)graph.get(0)).setBounds(rect);
                    diagramHandle.save();
                } else {
                    MdaInfra.LOG.error("Metamodel relation on '"+this.handler.getMetaclass()+"' not found.");
                    newElement.delete();
                }
                
            }
        
            tr.commit();
        } catch (Exception e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("c96a5998-cde2-4c18-9a67-b1fe1b5c3a01")
    private boolean isStereotyped(ModelElement elt, String stereotypeName) {
        for (Stereotype stereotype : elt.getExtension()) {
            if (stereotype.getName().equals(stereotypeName)) {
                return true;
            }
        }
        return false;
    }

}

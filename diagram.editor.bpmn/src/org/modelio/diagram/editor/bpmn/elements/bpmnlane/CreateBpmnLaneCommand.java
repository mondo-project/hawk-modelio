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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.GmBpmnLaneSetContainer;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Creation of Lane
 */
@objid ("61177673-55b6-11e2-877f-002564c97630")
public class CreateBpmnLaneCommand extends Command {
    @objid ("71a76aea-55c1-11e2-9337-002564c97630")
    private ModelioCreationContext context;

    @objid ("71a76aeb-55c1-11e2-9337-002564c97630")
    private GmCompositeNode parentNode;

    @objid ("eceb40f3-d0c7-4a23-9ba9-72224ac21952")
    private Object constraint;

    @objid ("6117767b-55b6-11e2-877f-002564c97630")
    public CreateBpmnLaneCommand(final GmCompositeNode parentEditPart, Object constraint, final ModelioCreationContext context) {
        this.parentNode = parentEditPart;
        this.context = context;
        this.constraint = constraint;
    }

    @objid ("6118fce0-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        
        ModelManager modelManager = diagram.getModelManager();
        final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
        BpmnLane newElement = null;
        
        if (this.context.getElementToUnmask() == null) {
        
            BpmnLane target = (BpmnLane) this.parentNode.getRelatedElement();
        
            
            if (target.getChildLaneSet() == null) {
                Rectangle recConstraint = (Rectangle) this.constraint;
                createBpmnLane(diagram, modelManager,(recConstraint.height + 200)/2);
                createBpmnLane(diagram, modelManager,(recConstraint.height + 200)/2);
            }else{
                 createBpmnLane(diagram, modelManager,200);
            }
        
        } else {
            newElement = (BpmnLane) this.context.getElementToUnmask();
            BpmnLane target = (BpmnLane) this.parentNode.getRelatedElement();
            BpmnLaneSet laneset = target.getChildLaneSet();
            GmBpmnLaneSetContainer gmlaneset = null;
            if (laneset == null) {
                laneset = modelFactory.createElement(BpmnLaneSet.class);
                laneset.setParentLane(target);
        
                // Set default name
                laneset.setName(modelManager.getModelServices().getElementNamer().getUniqueName(laneset));
        
                gmlaneset = (GmBpmnLaneSetContainer) diagram.unmask(this.parentNode, laneset, -1);
            } else {
                if (diagram.getAllGMRepresenting(new MRef(laneset)).size() > 0) {
                    gmlaneset = (GmBpmnLaneSetContainer) diagram.getAllGMRepresenting(new MRef(laneset)).get(0);
                } else {
                    gmlaneset = (GmBpmnLaneSetContainer) diagram.unmask(this.parentNode, laneset, -1);
                }
            }
        
            diagram.unmask(gmlaneset, newElement,this.constraint);
        }
    }

    @objid ("6118fce3-55b6-11e2-877f-002564c97630")
    private void createBpmnLane(final GmAbstractDiagram diagram, final ModelManager modelManager, int constraint) {
        final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
          
          BpmnLane newElement;
          newElement = modelFactory.createElement(BpmnLane.class);
          
          BpmnLane target = (BpmnLane) this.parentNode.getRelatedElement();
          
          BpmnLaneSet laneset = target.getChildLaneSet();
          GmBpmnLaneSetContainer gmlaneset = null;
          if (laneset == null) {
              laneset = modelFactory.createElement(BpmnLaneSet.class);
              laneset.setParentLane(target);
              gmlaneset = (GmBpmnLaneSetContainer) diagram.unmask(this.parentNode, laneset, -1);
          
              // Set default name of laneset
              laneset.setName(modelManager.getModelServices().getElementNamer().getUniqueName(laneset));
          } else {
          
              if (diagram.getAllGMRepresenting(new MRef(laneset)).size() > 0) {
                  gmlaneset = (GmBpmnLaneSetContainer) diagram.getAllGMRepresenting(new MRef(laneset)).get(0);
              } else {
                  gmlaneset = (GmBpmnLaneSetContainer) diagram.unmask(this.parentNode, laneset, -1);
              }
          }
          
          newElement.setLaneSet(target.getChildLaneSet());
          
          // Attach the stereotype if needed.
          if (this.context.getStereotype() != null) {
              ((ModelElement) newElement).getExtension().add(this.context.getStereotype());
          }
          
          // Configure element from properties
          final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
          elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, this.context.getProperties());
          
          
          
          // Set default name of lane
          newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
               
          GmNodeModel gmlane =  diagram.unmask(gmlaneset, newElement, this.constraint);
          gmlane.setLayoutData(constraint);
    }

}

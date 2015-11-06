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
                                    

package org.modelio.diagram.editor.bpmn.elements.policies;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.GmBpmnLane;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("621c632e-55b6-11e2-877f-002564c97630")
public class BpmnLaneReparentElementCommand extends Command {
    /**
     * The new parent element (might be different from the element of the new parent node).
     */
    @objid ("621c6339-55b6-11e2-877f-002564c97630")
    private MObject newParentElement;

    /**
     * The new layout data of the reparented child.
     */
    @objid ("621c632f-55b6-11e2-877f-002564c97630")
    private Object newLayoutData;

    /**
     * The new parent of the reparented child.
     */
    @objid ("72a63d49-55c1-11e2-9337-002564c97630")
    private GmCompositeNode newParent;

    /**
     * The child that is being reparented.
     */
    @objid ("72a63d4b-55c1-11e2-9337-002564c97630")
    private GmNodeModel reparentedChild;

    @objid ("621c633d-55b6-11e2-877f-002564c97630")
    public BpmnLaneReparentElementCommand(final MObject newParentElement, final GmCompositeNode newParent, final GmNodeModel reparentedChild, final Object newLayoutData) {
        super();
        this.newParentElement = newParentElement;
        this.newParent = newParent;
        this.reparentedChild = reparentedChild;
        this.newLayoutData = newLayoutData;
    }

    @objid ("621c634d-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        final MObject childElement = this.reparentedChild.getRelatedElement();
        if (this.newParentElement == null || childElement == null)
            return false;
        if (!this.newParentElement.getStatus().isModifiable() ||
            !childElement.getStatus().isModifiable())
            return false;
        if (this.newParentElement instanceof BpmnLaneSet &&
            this.newParentElement.getCompositionOwner() instanceof BpmnProcess) {
            return false;
        }
        return true;
    }

    @objid ("621c6352-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        BpmnLane childElement = (BpmnLane) this.reparentedChild.getRelatedElement();
        MObject newParentElem = this.newParentElement;
        
        // orphan the underlying {@link MObject element} from its previous
        // {@link MObject#getCompositionOwner() composition owner},
        assert (childElement != null) : "cannot reparent: child element is null";
        
        if (newParentElem.equals(childElement.getLaneSet().getCompositionOwner()))
            return;
        
        BpmnLaneSet laneset = childElement.getLaneSet();
        if (laneset != null) {
            laneset.getLane().remove(childElement);
            if (laneset.getLane().size() == 0) {
                laneset.delete();
            }
        }
        
        // orphan the {@link GmNodeModel node} from its previous {@link
        // GmCompositeNode container},
        final GmModel oldParentModel = this.reparentedChild.getParent();
        assert (oldParentModel instanceof GmCompositeNode) : "This command should only be used if both old parent and new parent are instances of GmCompositeNode!";
        final GmCompositeNode oldParent = (GmCompositeNode) oldParentModel;
        oldParent.removeChild(this.reparentedChild);
        
        if (newParentElem instanceof BpmnLane) {
            BpmnLane parentLane = (BpmnLane) newParentElem;
            if (parentLane.getChildLaneSet() != null) {
                newParentElem = parentLane.getChildLaneSet();
            } else {
                final GmAbstractDiagram diagram = this.newParent.getDiagram();
                ModelManager modelManager = diagram.getModelManager();
                final IModelFactory modelFactory = modelManager.getModelFactory(this.newParent.getRelatedElement());
                BpmnLaneSet newElement = modelFactory.createElement(BpmnLaneSet.class);
                parentLane.setChildLaneSet(newElement);
                
                // Set default name
                newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
                
                this.newParent = (GmCompositeNode) diagram.unmask(this.newParent,
                                                                  newElement,
                                                                  this.newLayoutData);
                newParentElem = newElement;
        
            }
        } else if (newParentElem instanceof BpmnProcess) {
            BpmnProcess process = (BpmnProcess) newParentElem;
        
            final GmAbstractDiagram diagram = this.newParent.getDiagram();
            ModelManager modelManager = diagram.getModelManager();
            final IModelFactory modelFactory = modelManager.getModelFactory(this.newParent.getRelatedElement());
            BpmnLaneSet newElement = modelFactory.createElement(BpmnLaneSet.class);
            process.getLaneSet().add(newElement);
            
            // Set default name
            newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
            
            newParentElem = newElement;
            
            // Camculate width of reparented element      
            for(GmNodeModel children : newParent.getChildren()){
                if(children instanceof GmBpmnLane){
                    Rectangle chlayout = (Rectangle)((GmBpmnLane)children).getLayoutData();
                    ((Rectangle)this.newLayoutData).width = chlayout.width;
                    break;
                }
            }
        }
        
        // Some additional initializing steps might be needed.
        if (newParentElem instanceof BpmnLaneSet) {
            childElement.setLaneSet((BpmnLaneSet) newParentElem);
        }
        
        this.reparentedChild.setLayoutData(this.newLayoutData);
        
        if (this.newParent.canContain(this.reparentedChild.getClass())) {
            // and finally attach the {@link GmNodeModel node} to its new {@link
            // GmCompositeNode container}.
            this.newParent.addChild(this.reparentedChild);
        } else {
            // The new parent does not support the node.
            // Ask the diagram to create a new node.
            if (this.newLayoutData instanceof Rectangle) {
                // reset the rectangle dimensions
                final Rectangle r = (Rectangle) this.newLayoutData;
                this.newLayoutData = new Rectangle(r.x, r.y, -1, -1);
            }
        
            this.newParent.getDiagram().unmask(this.newParent,
                                               this.reparentedChild.getRelatedElement(),
                                               this.newLayoutData);
        
            // Delete the now unused child
            this.reparentedChild.delete();
        }
        
        super.execute();
    }

}

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
                                    

package org.modelio.diagram.editor.sequence.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.CombinedFragmentEditPart;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.GmCombinedFragment;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.CombinedFragmentPrimaryNodeEditPart;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.GmCombinedFragmentPrimaryNode;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.GmInteractionOperandContainer;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.GmOperatorLabel;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.InteractionOperandContainerEditPart;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode.OperatorEditPart;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.ExecutionOccurenceSpecificationEditPart;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.GmExecutionOccurenceSpecification;
import org.modelio.diagram.editor.sequence.elements.executionspecification.ExecutionSpecificationEditPart;
import org.modelio.diagram.editor.sequence.elements.executionspecification.GmExecutionSpecification;
import org.modelio.diagram.editor.sequence.elements.gate.GateEditPart;
import org.modelio.diagram.editor.sequence.elements.gate.GatePrimaryNodeEditPart;
import org.modelio.diagram.editor.sequence.elements.gate.GmGate;
import org.modelio.diagram.editor.sequence.elements.gate.GmGatePrimaryNode;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.GmInteractionOperand;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.InteractionOperandEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode.GmGuardLabel;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode.GmInteractionOperandPrimaryNode;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode.GuardEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode.InteractionOperandPrimaryNodeEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionuse.GmInteractionUse;
import org.modelio.diagram.editor.sequence.elements.interactionuse.InteractionUseEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GateOnInteractionUseEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GateOnInteractionUsePrimaryNodeEditPart;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GmGateOnInteractionUse;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GmGateOnInteractionUsePrimaryNode;
import org.modelio.diagram.editor.sequence.elements.interactionuse.primarynode.GmInteractionUseHeader;
import org.modelio.diagram.editor.sequence.elements.interactionuse.primarynode.GmInteractionUsePrimaryNode;
import org.modelio.diagram.editor.sequence.elements.interactionuse.primarynode.InteractionUsePrimaryNodeEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.GmLifeline;
import org.modelio.diagram.editor.sequence.elements.lifeline.LifelineEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.body.GmLifelineBody;
import org.modelio.diagram.editor.sequence.elements.lifeline.body.LifelineBodyEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.GmLifelineHeader;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.GmLifelineHeaderContainer;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.LifelineHeaderContainerEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.LifelineHeaderContainerImageEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.LifelineHeaderContainerSimpleEditPart;
import org.modelio.diagram.editor.sequence.elements.lifeline.header.LifelineHeaderEditPart;
import org.modelio.diagram.editor.sequence.elements.message.GmMessage;
import org.modelio.diagram.editor.sequence.elements.message.MessageEditPart;
import org.modelio.diagram.editor.sequence.elements.message.label.GmMessageHeader;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.GmSequenceDiagram;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.SequenceDiagramEditPart;
import org.modelio.diagram.editor.sequence.elements.sequencediagramview.GmSequenceDiagramView;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariant;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariantBodyText;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.StateInvariantEditPart;
import org.modelio.diagram.elements.common.header.ModelElementHeaderEditPart;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderEditPart;
import org.modelio.diagram.elements.common.text.GmElementTextEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.umlcommon.diagramview.DiagramViewEditPart;

/**
 * The EditPart factory for Sequence diagrams.
 * <p>
 * Implementation of {@link EditPartFactory}.
 */
@objid ("d9853c4a-55b6-11e2-877f-002564c97630")
public final class SequenceDiagramEditPartFactory implements EditPartFactory {
    @objid ("d9853c4c-55b6-11e2-877f-002564c97630")
    private static final SequenceDiagramEditPartFactory INSTANCE = new SequenceDiagramEditPartFactory();

    /**
     * the default factory to use when structured mode is requested.
     */
    @objid ("d9853c4e-55b6-11e2-877f-002564c97630")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    /**
     * the default factory to use when simple mode is requested.
     */
    @objid ("d9853c50-55b6-11e2-877f-002564c97630")
    private SimpleModeEditPartFactory simpleModeEditPartFactory = new SimpleModeEditPartFactory();

    @objid ("d9853c52-55b6-11e2-877f-002564c97630")
    private ImageModeEditPartFactory imageModeEditPartFactory = new ImageModeEditPartFactory();

    @objid ("d9853c53-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart = null;
        
        if (model instanceof GmNodeModel) {
            // For node models, delegates according the representation model.
            GmNodeModel node = (GmNodeModel) model;
            switch (node.getRepresentationMode()) {
                case IMAGE:
                    editPart = this.imageModeEditPartFactory.createEditPart(context, model);
                    break;
                case SIMPLE:
                    editPart = this.simpleModeEditPartFactory.createEditPart(context, model);
                    break;
                case STRUCTURED:
                    editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
            }
        
            if (editPart != null)
                return editPart;
        
            return null;
        }
        // Link models are always in structured mode.
        editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
        
        if (editPart != null)
            return editPart;
        return null;
    }

    /**
     * @return the singleton instance of the edit part factory for Sequence diagram.
     */
    @objid ("d9853c5a-55b6-11e2-877f-002564c97630")
    public static SequenceDiagramEditPartFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Private default constructor
     */
    @objid ("d9853c5f-55b6-11e2-877f-002564c97630")
    private SequenceDiagramEditPartFactory() {
        // Nothing to do
    }

    /**
     * EditPart factory for Sequence graphical models in standard structured mode.
     * <p>
     * This is the default mode so the default factory.
     * 
     * @author chm
     */
    @objid ("d9853c62-55b6-11e2-877f-002564c97630")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("d9853c64-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmSequenceDiagram.class) {
                editPart = new SequenceDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmSequenceDiagramView.class) {
                editPart = new DiagramViewEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmLifeline.class) {
                editPart = new LifelineEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmLifelineHeader.class) {
                editPart = new LifelineHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmLifelineHeaderContainer.class) {
                editPart = new LifelineHeaderContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmLifelineBody.class) {
                editPart = new LifelineBodyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmMessage.class) {
                editPart = new MessageEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmMessageHeader.class) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmExecutionOccurenceSpecification.class) {
                editPart = new ExecutionOccurenceSpecificationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmExecutionSpecification.class) {
                editPart = new ExecutionSpecificationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmInteractionUse.class) {
                editPart = new InteractionUseEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmInteractionUsePrimaryNode.class) {
                editPart = new InteractionUsePrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == org.modelio.diagram.editor.sequence.elements.interactionuse.primarynode.GmOperatorLabel.class) {
                editPart = new OperatorEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmInteractionUseHeader.class) {
                editPart = new ModelElementHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGateOnInteractionUse.class) {
                editPart = new GateOnInteractionUseEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGateOnInteractionUsePrimaryNode.class) {
                editPart = new GateOnInteractionUsePrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGate.class) {
                editPart = new GateEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGatePrimaryNode.class) {
                editPart = new GatePrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmCombinedFragment.class) {
                editPart = new CombinedFragmentEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmCombinedFragmentPrimaryNode.class) {
                editPart = new CombinedFragmentPrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmOperatorLabel.class) {
                editPart = new OperatorEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmInteractionOperandContainer.class) {
                editPart = new InteractionOperandContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmInteractionOperand.class) {
                editPart = new InteractionOperandEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmInteractionOperandPrimaryNode.class) {
                editPart = new InteractionOperandPrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmGuardLabel.class) {
                editPart = new GuardEditPart((GmElementLabel) model);
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmStateInvariant.class) {
                editPart = new StateInvariantEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmStateInvariantBodyText.class) {
                editPart = new GmElementTextEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return null;
        }

    }

    /**
     * EditPart factory for Sequence graphical models in simple mode.
     * <p>
     * This is the default mode so the default factory.
     * 
     * @author chm
     */
    @objid ("d986c2b9-55b6-11e2-877f-002564c97630")
    public class SimpleModeEditPartFactory implements EditPartFactory {
        @objid ("d986c2bb-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(final EditPart context, final Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmGateOnInteractionUse.class) {
                editPart = new GateOnInteractionUseEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGateOnInteractionUsePrimaryNode.class) {
                editPart = new GateOnInteractionUsePrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGate.class) {
                editPart = new GateEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmGatePrimaryNode.class) {
                editPart = new GatePrimaryNodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmLifelineHeaderContainer.class) {
                editPart = new LifelineHeaderContainerSimpleEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return editPart;
        }

    }

    /**
     * EditPart factory for Sequence graphical models in image mode.
     * <p>
     * 
     * @author fpoyer
     */
    @objid ("d986c2c4-55b6-11e2-877f-002564c97630")
    public class ImageModeEditPartFactory implements EditPartFactory {
        @objid ("d986c2c6-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(final EditPart context, final Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmLifelineHeaderContainer.class) {
                editPart = new LifelineHeaderContainerImageEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return editPart;
        }

    }

}

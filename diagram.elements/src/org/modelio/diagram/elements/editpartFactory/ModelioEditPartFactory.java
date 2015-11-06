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
                                    

package org.modelio.diagram.elements.editpartFactory;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.elements.common.freezone.GmBodyFreeZone;
import org.modelio.diagram.elements.common.freezone.GmFreeZone;
import org.modelio.diagram.elements.common.freezone.GmFreeZoneEditPart;
import org.modelio.diagram.elements.common.ghostlink.GhostLinkEditPart;
import org.modelio.diagram.elements.common.ghostnode.GhostNodeEditPart;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.common.group.GmGroupEditPart;
import org.modelio.diagram.elements.common.groupitem.GroupItemEditPart;
import org.modelio.diagram.elements.common.header.GmModelElementHeader;
import org.modelio.diagram.elements.common.header.ModelElementHeaderEditPart;
import org.modelio.diagram.elements.common.image.LabelledImageEditPart;
import org.modelio.diagram.elements.common.image.NonSelectableImageEditPart;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.common.label.base.GmElementLabelEditPart;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderEditPart;
import org.modelio.diagram.elements.common.label.name.GmNameLabel;
import org.modelio.diagram.elements.common.label.name.GmNameSimpleLabel;
import org.modelio.diagram.elements.common.label.name.NameLabelEditPart;
import org.modelio.diagram.elements.common.label.name.NameSimpleLabelEditPart;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;
import org.modelio.diagram.elements.common.simple.NonSelectableSimpleEditPart;
import org.modelio.diagram.elements.common.simple.SimpleEditPart;
import org.modelio.diagram.elements.common.text.GmElementText;
import org.modelio.diagram.elements.common.text.GmElementTextEditPart;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.elements.drawings.core.GmDrawing;
import org.modelio.diagram.elements.drawings.layer.GmDrawingLayer;
import org.modelio.diagram.elements.umlcommon.abstraction.GmAbstraction;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintBodyEditPart;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintBodyLabelEditPart;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPart;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintBody;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintBodyLabel;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintLink;
import org.modelio.diagram.elements.umlcommon.dependency.DependencyEditPart;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependency;
import org.modelio.diagram.elements.umlcommon.diagramheader.DiagramHeaderEditPart;
import org.modelio.diagram.elements.umlcommon.diagramheader.GmDiagramHeader;
import org.modelio.diagram.elements.umlcommon.diagramholder.DiagramHolderEditPart;
import org.modelio.diagram.elements.umlcommon.diagramholder.GmDiagramHolder;
import org.modelio.diagram.elements.umlcommon.diagramholder.GmDiagramHolderLink;
import org.modelio.diagram.elements.umlcommon.elementRealization.ElementRealizationEditPart;
import org.modelio.diagram.elements.umlcommon.elementRealization.GmElementRealization;
import org.modelio.diagram.elements.umlcommon.externdocument.ExternDocumentEditPart;
import org.modelio.diagram.elements.umlcommon.externdocument.GmExternDocument;
import org.modelio.diagram.elements.umlcommon.externdocument.GmExternDocumentLink;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInfoFlowsGroup;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInformationFlowArrow;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInformationFlowLabel;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.InfoFlowsGroupEditPart;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.InformationArrowEditPart;
import org.modelio.diagram.elements.umlcommon.namespaceuse.GmNamespaceUse;
import org.modelio.diagram.elements.umlcommon.namespaceuse.GmNamespaceUseLabel;
import org.modelio.diagram.elements.umlcommon.namespaceuse.NamespaceUseEditPart;
import org.modelio.diagram.elements.umlcommon.note.GmNote;
import org.modelio.diagram.elements.umlcommon.note.GmNoteLink;
import org.modelio.diagram.elements.umlcommon.note.NoteEditPart;
import org.modelio.diagram.elements.umlcommon.usage.GmUsage;
import org.modelio.diagram.elements.umlcommon.usage.GmUsageHeader;

/**
 * The UML standard EditPart factory for Modelio diagrams.
 * <p>
 * This factory is dynamically enriched by diagram plugins, so that it ends by being able to process the complete UML
 * metamodel. The ModelioEditPartFactory actually delegates the createEditPart request to its cascaded factories. No
 * ordering can be assumed in the delegation mechanism.
 */
@objid ("80fc16b6-1dec-11e2-8cad-001ec947c8cc")
public class ModelioEditPartFactory implements EditPartFactory {
    /**
     * the default factory to use when image mode is requested.
     */
    @objid ("80fe790d-1dec-11e2-8cad-001ec947c8cc")
    private ImageModeEditPartFactory imageModeEditPartFactory = new ImageModeEditPartFactory();

    /**
     * the default factory to use when simple mode is requested.
     */
    @objid ("80fe790f-1dec-11e2-8cad-001ec947c8cc")
    private SimpleModeEditPartFactory simpleModeEditPartFactory = new SimpleModeEditPartFactory();

    /**
     * the default factory to use when structured mode is requested.
     */
    @objid ("80fe7911-1dec-11e2-8cad-001ec947c8cc")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    @objid ("80fe7913-1dec-11e2-8cad-001ec947c8cc")
    private static final ModelioEditPartFactory INSTANCE = new ModelioEditPartFactory();

    /**
     * All cascaded factories (if any).
     */
    @objid ("68278379-1e83-11e2-8cad-001ec947c8cc")
    private Map<String, EditPartFactory> cascadedFactories = new HashMap<>();

    @objid ("eaa04cf4-6faa-4d5d-8ba5-1360d762d3e9")
    private DrawingEditPartFactory drawingEditPartFactory = new DrawingEditPartFactory();

    /**
     * @return the instance.
     */
    @objid ("80fe7915-1dec-11e2-8cad-001ec947c8cc")
    public static ModelioEditPartFactory getInstance() {
        return INSTANCE;
    }

    @objid ("80fe791a-1dec-11e2-8cad-001ec947c8cc")
    private ModelioEditPartFactory() {
        // private default constructor
    }

    @objid ("80fe791c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart;
        
        // Priority: Special handling of "dead" elements.
        if (model instanceof GmModel) {
            GmModel gmModel = (GmModel) model;
            if (gmModel.getRelatedElement() == null || gmModel.getRelatedElement().isDeleted() || gmModel.getRelatedElement().isShell()) {
                if (gmModel instanceof GmNodeModel) {
                    editPart = new GhostNodeEditPart();
                    editPart.setModel(gmModel);
                    return editPart;
                } else {
                    editPart = new GhostLinkEditPart();
                    editPart.setModel(gmModel);
                    return editPart;
                }
            }
        }
        
        // First ask cascaded edit part factories
        for (EditPartFactory factory : this.cascadedFactories.values()) {
            editPart = factory.createEditPart(context, model);
            if (editPart != null)
                return editPart;
        }
        
        if (model instanceof GmNodeModel) {
            // For node models, delegates according the representation mode.
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
                break;
            default:
                editPart = null;
                break;
            }
        
            if (editPart != null)
                return editPart;
        
            throw new IllegalArgumentException(model +
                    " is not supported in " +
                    node.getRepresentationMode() +
                    " mode.");
        } 
        
        if (model instanceof GmDrawing || model instanceof GmDrawingLayer) {
            // For drawings, use dedicated factory
            editPart = this.drawingEditPartFactory.createEditPart(context, model);
        
            if (editPart != null)
                return editPart;
        
            throw new IllegalArgumentException(model +
                    " is not supported.");
        }
        
        // Link models are always in structured mode.
        editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
        
        if (editPart != null)
            return editPart;
        
        throw new IllegalArgumentException(model + " link is not supported.");
    }

    /**
     * Register an external edit part factory.
     * <p>
     * External edit part factories are called first when looking for an edit part.
     * @param id id for the edit part factory
     * @param factory the edit part factory.
     */
    @objid ("80fe7926-1dec-11e2-8cad-001ec947c8cc")
    public void registerFactory(String id, EditPartFactory factory) {
        this.cascadedFactories.put(id, factory);
    }

    /**
     * Remove a registered edit part factory.
     * @param id The id used to register the edit part factory.
     */
    @objid ("80fe792d-1dec-11e2-8cad-001ec947c8cc")
    public void unregisterFactory(String id) {
        this.cascadedFactories.remove(id);
    }

    /**
     * EditPart factory for node models in simple mode.
     * 
     * @author cmarin
     */
    @objid ("80fe7931-1dec-11e2-8cad-001ec947c8cc")
    public class SimpleModeEditPartFactory implements EditPartFactory {
        @objid ("80fe7935-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            // Port containers stay a port container in simple mode
            if (model instanceof GmPortContainer) {
                final EditPart editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // Fall back
            if (model instanceof GmNodeModel) {
                if (((GmNodeModel) model).getParent() instanceof GmPortContainer) {
                    final SimpleEditPart editPart = new NonSelectableSimpleEditPart();
                    editPart.setModel(model);
                    return editPart;
                } else {
                    final SimpleEditPart editPart = new SimpleEditPart();
                    editPart.setModel(model);
                    return editPart;
                }
            }
            
            
            // Not handled
            return null;
        }

    }

    /**
     * EditPart factory for node models in standard structured mode.
     * <p>
     * This is the default mode so the default factory.
     * 
     * @author cmarin
     */
    @objid ("80fe7940-1dec-11e2-8cad-001ec947c8cc")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("80fe7944-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            // Information flow
            if (model.getClass() == GmInformationFlowLabel.class) {
                editPart = new GroupItemEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmInfoFlowsGroup.class) {
                editPart = new InfoFlowsGroupEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmInformationFlowArrow.class) {
                editPart = new InformationArrowEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // "standard" elements
            
            if (model.getClass() == GmDependency.class) {
                editPart = new DependencyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmUsage.class) {
                editPart = new DependencyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmAbstraction.class) {
                editPart = new DependencyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmElementRealization.class) {
                editPart = new ElementRealizationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmElementLabel.class) {
                editPart = new GmElementLabelEditPart((GmElementLabel) model);
                return editPart;
            }
            
            if (model.getClass() == GmNameSimpleLabel.class) {
                editPart = new NameSimpleLabelEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNameLabel.class) {
                editPart = new NameLabelEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBodyFreeZone.class) {
                editPart = new GmFreeZoneEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNote.class) {
                editPart = new NoteEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNoteLink.class) {
                editPart = new GmLinkEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmExternDocument.class) {
                editPart = new ExternDocumentEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmExternDocumentLink.class) {
                editPart = new GmLinkEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNamespaceUse.class) {
                editPart = new NamespaceUseEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNamespaceUseLabel.class) {
                editPart = new GmElementLabelEditPart((GmNamespaceUseLabel) model);
                return editPart;
            }
            
            // Constraint
            if (model instanceof GmConstraintBody) {
                editPart = new ConstraintBodyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmConstraintBodyLabel) {
                editPart = new ConstraintBodyLabelEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmConstraintLink) {
                editPart = new ConstraintLinkEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // Related Diagram links
            if (model instanceof GmDiagramHeader) {
                editPart = new DiagramHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model instanceof GmDiagramHolder) {
                editPart = new DiagramHolderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model instanceof GmDiagramHolderLink) {
                editPart = new GmLinkEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmUsageHeader) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
            }
            
            
            // Last chance: Generic fall backs
            // -------------------------------
            if (model instanceof GmElementText) {
                editPart = new GmElementTextEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmGroup) {
                editPart = new GmGroupEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmFreeZone) {
                editPart = new GmFreeZoneEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmModelElementFlatHeader) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model instanceof GmModelElementHeader) {
                editPart = new ModelElementHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // End of last chance generic fall backs
            
            // Not registered : bug
            // --------------------
            throw new IllegalArgumentException(model + " is not registered.");
        }

    }

    /**
     * EditPart factory for node models in stereotype image mode.
     * 
     * @author cmarin
     */
    @objid ("80fe794e-1dec-11e2-8cad-001ec947c8cc")
    public class ImageModeEditPartFactory implements EditPartFactory {
        @objid ("80fe7952-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            // Port containers stay a port container in image mode
            if (model instanceof GmPortContainer) {
                new IllegalStateException("Ports containers should never be in image mode.").printStackTrace();
            
                final EditPart editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model instanceof IImageableNode) {
                if (((GmNodeModel) model).getParent() instanceof GmPortContainer) {
                    final EditPart editPart = new NonSelectableImageEditPart();
                    editPart.setModel(model);
                    return editPart;
                } else {
                    final EditPart editPart = new LabelledImageEditPart();
                    editPart.setModel(model);
                    return editPart;
                }
            }
            return null;
        }

    }

}

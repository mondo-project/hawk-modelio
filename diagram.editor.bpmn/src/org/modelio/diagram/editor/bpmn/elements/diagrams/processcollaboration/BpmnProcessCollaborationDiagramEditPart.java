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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams.processcollaboration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.editor.bpmn.elements.diagrams.BpmnDiagramElementDropEditPolicy;
import org.modelio.diagram.editor.bpmn.elements.diagrams.GmBpmnDiagramStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnDiagramLayoutEditPolicy;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramFigure;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeFinishCreationEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.diagram.elements.core.policies.CreateLinkIntermediateEditPolicy;
import org.modelio.diagram.elements.core.requests.CreateLinkConstants;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.styles.core.IStyle;

/**
 * EditPart (== controller in the GEF model) for BPMN diagram background.
 */
@objid ("61f3303a-55b6-11e2-877f-002564c97630")
public class BpmnProcessCollaborationDiagramEditPart extends AbstractDiagramEditPart {
    @objid ("61f3303e-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        Figure diagramFigure = new BpmnProcessCollaborationDiagramFigure();
        IStyle style = ((GmAbstractObject) this.getModel()).getStyle();
        
        // Set style independent properties
        
        // Set style dependent properties
        refreshFromStyle(diagramFigure, style);
        return diagramFigure;
    }

    @objid ("61f33043-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Policy to add nodes on the diagram
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new BpmnDiagramLayoutEditPolicy());
        //installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramEditLayoutPolicy());
        
        // Policy to create notes
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_END,
                          new LinkedNodeFinishCreationEditPolicy());
        
        // Policy to add bend points to connections being created
        installEditPolicy(CreateLinkConstants.REQ_CONNECTION_ADD_BENDPOINT,
                          new CreateLinkIntermediateEditPolicy());
        
        // Override the default drop handling policy with a specific one that
        // can in some case search for the parent of the dropped element (eg:
        // for pins). 
        installEditPolicy(ModelElementDropRequest.TYPE, new BpmnDiagramElementDropEditPolicy());
        
        // Remove the default DIRECT_EDIT policy: we don't want the diagram
        // background to delegate direct edit requests.
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
    }

    @objid ("61f3304c-55b6-11e2-877f-002564c97630")
    protected Command createAddCommand(final EditPart child, final Object constraint) {
        return null;
    }

    @objid ("61f33054-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        //      super.refreshFromStyle(aFigure,style);
        //      int gridSpacing = style.getInteger(GmBpmnDiagramStyleKeys.GRIDSPACING);
        //      EditPartViewer v = this.getRoot().getViewer();
        //      v.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSpacing, gridSpacing));
        
        AbstractDiagramFigure diagramFigure = (AbstractDiagramFigure) aFigure;
        
        boolean viewGrid = style.getBoolean(GmBpmnDiagramStyleKeys.VIEWGRID);
        boolean snapToGrid = style.getBoolean(GmBpmnDiagramStyleKeys.SNAPTOGRID);
        Color gridColor = style.getColor(GmBpmnDiagramStyleKeys.GRIDCOLOR);
        int gridAlpha = style.getInteger(GmBpmnDiagramStyleKeys.GRIDALPHA);
        int gridSpacing = style.getInteger(GmBpmnDiagramStyleKeys.GRIDSPACING);
        
        Color fillColor = style.getColor(GmBpmnDiagramStyleKeys.FILLCOLOR);
        String fillImage = style.getProperty(GmBpmnDiagramStyleKeys.FILLIMAGE);
        int fillAlpha = style.getInteger(GmBpmnDiagramStyleKeys.FILLALPHA);
        
        // Process the page size property
        // TODO: in the future this parsing might become the responsability of the property view, 
        // ie the property view would propose a 'Dimension' editor returning the proper 'in pixel' dimension value...
        String pageSize = (String) style.getProperty(GmBpmnDiagramStyleKeys.PAGE_SIZE);
        Dimension pixelPageSize = null;
        if (pageSize != null && !pageSize.isEmpty()) {
            PrecisionDimension inchPageSize = this.parsePageSize(pageSize);
            if (inchPageSize != null)
                pixelPageSize = this.convertToPixel(inchPageSize);
        }
        
        //
        EditPartViewer v = this.getRoot().getViewer();
        v.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(viewGrid));
        v.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(snapToGrid));
        v.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSpacing, gridSpacing));
        v.setProperty(AbstractDiagramEditPart.PROPERTY_GRID_COLOR, gridColor);
        v.setProperty(AbstractDiagramEditPart.PROPERTY_GRID_ALPHA, new Integer(gridAlpha));
        v.setProperty(AbstractDiagramEditPart.PROPERTY_FILL_COLOR, fillColor);
        v.setProperty(AbstractDiagramEditPart.PROPERTY_FILL_IMAGE, fillImage);
        v.setProperty(AbstractDiagramEditPart.PROPERTY_FILL_ALPHA, new Integer(fillAlpha));
        diagramFigure.showPageBoundaries(style.getBoolean(GmBpmnDiagramStyleKeys.SHOW_PAGES));
        v.setProperty(AbstractDiagramEditPart.PROPERTY_FILL_TILE_SIZE, pixelPageSize);
        diagramFigure.setPageBoundaries(pixelPageSize);
    }

    @objid ("61f3305d-55b6-11e2-877f-002564c97630")
    private PrecisionDimension convertToPixel(final PrecisionDimension d) {
        org.eclipse.swt.graphics.Point dpi = Display.getCurrent().getDPI();
        return new PrecisionDimension(d.preciseWidth * dpi.x, d.preciseHeight * dpi.y);
    }

    @objid ("61f33063-55b6-11e2-877f-002564c97630")
    private PrecisionDimension parsePageSize(final String value) {
        final float oneInch = 25.4f; // mm
        String s = value.replaceAll(" ", "");
        
        // This might be replaced by a lookup table in the future ?
        if ("A0H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(1189, 841));
        if ("A0V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(841, 1189));
        if ("A1H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(841, 594));
        if ("A1V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(594, 841));
        if ("A2H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(594, 420));
        if ("A2V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(420, 594));
        if ("A3H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(420, 297));
        if ("A3V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(297, 420));
        if ("A4H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(297, 210));
        if ("A4V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(210, 297));
        if ("A5H".equals(s))
            return this.convertMmToInch(new PrecisionDimension(210, 148));
        if ("A5V".equals(s))
            return this.convertMmToInch(new PrecisionDimension(148, 210));
        
        // try to parse
        Pattern whR = Pattern.compile("(\\d+\\.?\\d*.*)(x|X)(\\d+\\.?\\d*.*)", Pattern.CASE_INSENSITIVE);
        
        Matcher whM = whR.matcher(s);
        
        if (whM.matches()) {
            String widthString = whM.group(1);
            String heightString = whM.group(3);
            float width;
            float height;
        
            if (widthString.endsWith("\"")) {
                // inches
        
                width = Float.parseFloat(widthString.replaceAll("[^0-9\\.]", ""));
            } else {
                width = Float.parseFloat(widthString.replaceAll("[^0-9\\.]", "")) / oneInch; // 1 inch = 25,4 mm
            }
            if (heightString.endsWith("\"")) {
                // inches
                height = Float.parseFloat(heightString.replaceAll("[^0-9\\.]", ""));
            } else {
                height = Float.parseFloat(heightString.replaceAll("[^0-9\\.]", "")) / oneInch; // 1 inch = 25,4 mm
            }
            return new PrecisionDimension(width, height);
        }
        return null;
    }

    @objid ("61f33069-55b6-11e2-877f-002564c97630")
    private PrecisionDimension convertMmToInch(final PrecisionDimension d) {
        if (d == null)
            return null;
        final float factor = 25.4f; // one inch is 25,4 mm
        return new PrecisionDimension(d.preciseWidth / factor, d.preciseHeight / factor);
    }

}

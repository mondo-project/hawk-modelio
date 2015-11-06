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
                                    

package org.modelio.diagram.elements.common.abstractdiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.common.linktovoid.LinkToVoidConstants;
import org.modelio.diagram.elements.common.linktovoid.LinkToVoidFinishCreationEditPolicy;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;
import org.modelio.diagram.elements.drawings.layer.DrawingLayerEditPart;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Constraint;

/**
 * Default Edit part for GmDiagram.
 */
@objid ("7e038885-1dec-11e2-8cad-001ec947c8cc")
public class AbstractDiagramEditPart extends GmNodeEditPart {
    /**
     * Grid color property key.
     */
    @objid ("90df2c84-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_GRID_COLOR = "AbstractDiagramEditPart.GridColor";

    /**
     * Grid transparency property key.
     */
    @objid ("90df2c8a-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_GRID_ALPHA = "AbstractDiagramEditPart.GridAlpha";

    /**
     * Diagram fill color property key.
     */
    @objid ("90df2c90-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_FILL_COLOR = "AbstractDiagramEditPart.FillColor";

    /**
     * Fill image property key.
     */
    @objid ("90df2c96-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_FILL_IMAGE = "AbstractDiagramEditPart.FillImage";

    /**
     * Fill image transparency property key.
     */
    @objid ("90df2c9c-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_FILL_ALPHA = "AbstractDiagramEditPart.FillAlpha";

    /**
     * Fill tile size property key.
     */
    @objid ("90df2ca2-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_FILL_TILE_SIZE = "AbstractDiagramEditPart.FillTileSize";

    /**
     * ID of the layer pane where drawing layers are put.
     */
    @objid ("9eeff616-9943-42e1-8193-1a4fa86532d1")
    private static final String LAYER_PANE_DRAWING = "LAYER_PANE_DRAWING";

    /**
     * Default constructor.
     */
    @objid ("7e05eaeb-1dec-11e2-8cad-001ec947c8cc")
    public AbstractDiagramEditPart() {
        // Nothing to do yet.
    }

    @objid ("7e05eaee-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void activate() {
        super.activate();
        // Property change listener already added by super, no need to add another one
        //((GmAbstractDiagram) this.getModel()).addPropertyChangeListener(this);
    }

    /**
     * In order to provide snap to grid facility the AbstractEditPart must be adaptable to a SnapToHelper. Here we adapt
     * to a SnapToGrid only if the style defines SNAPTOGRID = true
     */
    @objid ("7e05eaf1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
        if (type == SnapToHelper.class) {
            boolean snap = this.getModelStyle().getBoolean(GmAbstractDiagramStyleKeys.SNAPTOGRID);
            if (snap)
                return new SnapToGrid(this);
        }
        return super.getAdapter(type);
    }

    @objid ("7e05eaf8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        if (request instanceof ReconnectRequest) {
            final Point p = ((ReconnectRequest) request).getLocation();
            return new XYAnchor(p);
        } else if (request instanceof DropRequest) {
            final Point p = ((DropRequest) request).getLocation();
            return new XYAnchor(p);
        }
        throw new IllegalArgumentException(request + " not handled.");
        
        //DropRequest r = (DropRequest) req;
        //Point p = r.getLocation();
        //System.out.println("getSourceConnectionAnchor at " + p);
        // return new XYAnchor(p);
    }

    @objid ("7e05eb01-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        // Override the default drop policy with a specific one that can also handle links.
        installEditPolicy(ModelElementDropRequest.TYPE, new DiagramElementDropEditPolicy());
        // Remove the default DIRECT_EDIT policy: we don't want the diagram
        // background to delegate direct edit requests.
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
        
        // For lollipops (provided interface, required interface)
        final LinkToVoidFinishCreationEditPolicy linkTovoidEditPolicy = new LinkToVoidFinishCreationEditPolicy();
        installEditPolicy(LinkToVoidConstants.REQ_LINKTOVOID_END, linkTovoidEditPolicy);
        
        // For constraints "body"
        installEditPolicy(Metamodel.getMClass(Constraint.class).getName(), new ConstraintFinalizationEditPolicy());
    }

    /**
     * Refresh the figure from the given style.
     * <p>
     * Often called in {@link #createFigure()} and after a style change.
     * @param aFigure The figure to update, should be {@link #getFigure()}.
     * @param style The style to update from, usually {@link #getModelStyle()}
     */
    @objid ("7e05eb04-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        AbstractDiagramFigure diagramFigure = (AbstractDiagramFigure) aFigure;
        
        boolean viewGrid = style.getBoolean(GmAbstractDiagramStyleKeys.VIEWGRID);
        boolean snapToGrid = style.getBoolean(GmAbstractDiagramStyleKeys.SNAPTOGRID);
        Color gridColor = style.getColor(GmAbstractDiagramStyleKeys.GRIDCOLOR);
        int gridAlpha = style.getInteger(GmAbstractDiagramStyleKeys.GRIDALPHA);
        int gridSpacing = style.getInteger(GmAbstractDiagramStyleKeys.GRIDSPACING);
        
        Color fillColor = style.getColor(GmAbstractDiagramStyleKeys.FILLCOLOR);
        String fillImage = style.getProperty(GmAbstractDiagramStyleKeys.FILLIMAGE);
        int fillAlpha = style.getInteger(GmAbstractDiagramStyleKeys.FILLALPHA);
        
        // Process the page size property
        // TODO: in the future this parsing might become the responsability of the property view, 
        // ie the property view would propose a 'Dimension' editor returning the proper 'in pixel' dimension value...
        String pageSize = (String) style.getProperty(GmAbstractDiagramStyleKeys.PAGE_SIZE);
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
        
        //
        diagramFigure.showPageBoundaries(style.getBoolean(GmAbstractDiagramStyleKeys.SHOW_PAGES));
        
        // 
        v.setProperty(AbstractDiagramEditPart.PROPERTY_FILL_TILE_SIZE, pixelPageSize);
        diagramFigure.setPageBoundaries(pixelPageSize);
    }

    @objid ("7e05eb0c-1dec-11e2-8cad-001ec947c8cc")
    private PrecisionDimension convertMmToInch(PrecisionDimension d) {
        if (d == null)
            return null;
        final float factor = 25.4f; // one inch is 25,4 mm
        return new PrecisionDimension(d.preciseWidth() / factor, d.preciseHeight() / factor);
    }

    /**
     * Convert a dimension from inches to pixel
     * @param d @return
     */
    @objid ("7e05eb15-1dec-11e2-8cad-001ec947c8cc")
    private PrecisionDimension convertToPixel(PrecisionDimension d) {
        org.eclipse.swt.graphics.Point dpi = Display.getCurrent().getDPI();
        return new PrecisionDimension(d.preciseWidth() * dpi.x, d.preciseHeight() * dpi.y);
    }

    @objid ("7e05eb1f-1dec-11e2-8cad-001ec947c8cc")
    private PrecisionDimension parsePageSize(String value) {
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
        
        //System.out.println(whR.toString());
        
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

    @objid ("3cb77c77-2b4d-47c8-834a-ae8b5344a64e")
    @Override
    protected List<Object> getModelChildren() {
        GmAbstractDiagram d = (GmAbstractDiagram) getModel();
        // Add background layer first
        // default children next
        // and finish with foreground layers.
        
        final List<?> modelChildren = super.getModelChildren();
        final Collection<IGmDrawingLayer> drawingLayers = d.getDrawingLayers();
        
        ArrayList<Object> ret = new ArrayList<>(modelChildren.size()+ drawingLayers.size() + 1);
        
        if (d.getBackgroundDrawingLayer()!= null)
            ret.add(d.getBackgroundDrawingLayer());
        
        ret.addAll(modelChildren);
        ret.addAll(drawingLayers);
        return ret;
    }

    /**
     * Redefined to add {@link DrawingLayerEditPart} layers in the drawing layer pane.
     * <p/>
     * Subclasses willing to redefine this method must redefine {@link #doAddChildVisual(EditPart, int)} instead,
     * so that they won't be annoyed by drawing layers.
     */
    @objid ("80a29b4a-33d5-4b9f-813d-bb17f88ff7d5")
    @Override
    protected final void addChildVisual(EditPart childEditPart, int index) {
        if (childEditPart instanceof DrawingLayerEditPart) {
            // Add all drawing layers to the drawing layer pane, except the background layer
            // that stays in this layer.
            if (index != 0) {
                getDrawingLayerPane().add(((DrawingLayerEditPart) childEditPart).getFigure());
            } else {
                super.addChildVisual(childEditPart, index);
            }
        } else {
            doAddChildVisual(childEditPart, index);
        }
    }

    /**
     * Redefined to remove DrawingLayerEditPart layers from the drawing layer pane.
     * <br/>
     * Subclasses must redefine {@link #doRemoveChildVisual(EditPart)}.
     */
    @objid ("78c57828-b40b-489b-b4eb-11e103e5eae4")
    @Override
    protected final void removeChildVisual(EditPart childEditPart) {
        if (childEditPart instanceof DrawingLayerEditPart) {
            final IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
            childFigure.getParent().remove(childFigure);
        } else {
            doRemoveChildVisual(childEditPart);
        }
    }

    /**
     * Get the layer pane where drawing layers are put.
     * @return the drawings layer pane.
     */
    @objid ("2b7373ed-b77c-4cd3-bbae-8887dba3c516")
    protected final LayeredPane getDrawingLayerPane() {
        LayeredPane pane = (LayeredPane) getLayer(LAYER_PANE_DRAWING);
        if (pane == null)
            pane = createDrawingLayerPane();
        return pane;
    }

    /**
     * Creates the layer pane where drawing layers are put.
     * <p>
     * The layer pane is put on top of the {@link LayerConstants#PRINTABLE_LAYERS} layer.
     * @return the drawings layer pane.
     */
    @objid ("a7c40bc5-f7d4-42c6-b604-273fc07d900b")
    protected LayeredPane createDrawingLayerPane() {
        // Ensure the diagram layers already exist
        getFigure();
        
        // Create the drawing layer on top of the diagram layer.
        FreeformLayeredPane drawLayerPane = new FreeformLayeredPane();
        //getViewer().getEditPartRegistry().put(drawLayerPane, LAYER_PANE_DRAWING);
        LayeredPane pane = (LayeredPane) getLayer(LayerConstants.PRINTABLE_LAYERS);
        pane.add(drawLayerPane, LAYER_PANE_DRAWING);
        return drawLayerPane;
    }

    @objid ("e13d5a6e-f507-4d36-834c-e75a3cbe351d")
    protected void doAddChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
    }

    @objid ("18a001e2-2d25-40da-9873-644e28ddd622")
    protected void doRemoveChildVisual(EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
    }

}

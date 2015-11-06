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
                                    

package org.modelio.diagram.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;

/**
 * Extended ScalabableFreeformRootEditPart that adds an additional background layer used to either display a color or an
 * image. This editpart also deals with GridLayer2 additional configuration properties.
 */
@objid ("668fd320-33f7-11e2-95fe-001ec947c8cc")
public class ScalableFreeformRootEditPart2 extends ScalableFreeformRootEditPart {
    @objid ("668fd322-33f7-11e2-95fe-001ec947c8cc")
    private PropertyChangeListener gridAndBackgroundListener;

    @objid ("668fd323-33f7-11e2-95fe-001ec947c8cc")
    private static final Point ORIGIN = new Point(0, 0);

    @objid ("668fd325-33f7-11e2-95fe-001ec947c8cc")
    private Point creationLocationTip = ORIGIN;

    @objid ("668fd326-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected GridLayer createGridLayer() {
        return new GridLayer2();
    }

    @objid ("6692352c-33f7-11e2-95fe-001ec947c8cc")
    public ScalableFreeformRootEditPart2() {
        this.gridAndBackgroundListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
        
                String property = evt.getPropertyName();
                if (property.equals(AbstractDiagramEditPart.PROPERTY_GRID_ALPHA) ||
                    property.equals(AbstractDiagramEditPart.PROPERTY_GRID_COLOR))
                    ScalableFreeformRootEditPart2.this.refreshGridLayer();
                if (property.equals(AbstractDiagramEditPart.PROPERTY_FILL_ALPHA) ||
                    property.equals(AbstractDiagramEditPart.PROPERTY_FILL_COLOR) ||
                    property.equals(AbstractDiagramEditPart.PROPERTY_FILL_TILE_SIZE) ||
                    property.equals(AbstractDiagramEditPart.PROPERTY_FILL_IMAGE))
                    ScalableFreeformRootEditPart2.this.refreshBackground();
            }
        };
    }

    @objid ("6692352e-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected void refreshGridLayer() {
        // super method processes: grid visible, grid spacing, grid origin
        super.refreshGridLayer();
        
        // process additionnal properties
        GridLayer2 grid = (GridLayer2) this.getLayer(GRID_LAYER);
        
        // grid color
        Object gridColor = this.getViewer().getProperty(AbstractDiagramEditPart.PROPERTY_GRID_COLOR);
        if (gridColor != null)
            grid.setGridColor((Color) gridColor);
        
        // grid alpha
        Integer gridAlpha = (Integer) this.getViewer()
                                          .getProperty(AbstractDiagramEditPart.PROPERTY_GRID_ALPHA);
        if (gridAlpha != null)
            grid.setGridAlpha(gridAlpha);
    }

    @objid ("66923531-33f7-11e2-95fe-001ec947c8cc")
    protected void refreshBackground() {
        if (this.getLayer("BACKGROUND_LAYER") != null) {
            BackgroundLayer backgroundLayer = (BackgroundLayer) this.getLayer("BACKGROUND_LAYER");
        
            // fill color
            Object fillColor = this.getViewer().getProperty(AbstractDiagramEditPart.PROPERTY_FILL_COLOR);
            if (fillColor != null)
                backgroundLayer.setBackgroundColor((Color) fillColor);
        
            // fill alpha
            Integer fillAlpha = (Integer) this.getViewer()
                                              .getProperty(AbstractDiagramEditPart.PROPERTY_FILL_ALPHA);
            if (fillAlpha != null)
                backgroundLayer.setAlpha(fillAlpha);
        
            // fill image
            String fillImage = (String) this.getViewer()
                                            .getProperty(AbstractDiagramEditPart.PROPERTY_FILL_IMAGE);
            try {
                if (fillImage != null) {
                    ImageDescriptor id = ImageDescriptor.createFromURL(new URL(fillImage));
                    backgroundLayer.setImage(id);
                } else {
                    backgroundLayer.setImage(null);    
                }
            } catch (MalformedURLException e) {
                // set the image to null (this will also remove the existing image)
                backgroundLayer.setImage(null);
            }
        
            // page boundaries size (in pixels)
            Dimension tileSize = (Dimension) this.getViewer()
                                                 .getProperty(AbstractDiagramEditPart.PROPERTY_FILL_TILE_SIZE);
            backgroundLayer.setTileSize(tileSize);
        
        }
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#register()
     */
    @objid ("66923533-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected void register() {
        super.register();
        if (this.getLayer(GRID_LAYER) != null) {
            this.getViewer().addPropertyChangeListener(this.gridAndBackgroundListener);
            this.refreshBackground();
            this.refreshGridLayer();
        }
    }

    /**
     * @see AbstractEditPart#unregister()
     */
    @objid ("66923537-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected void unregister() {
        this.getViewer().removePropertyChangeListener(this.gridAndBackgroundListener);
        super.unregister();
    }

    /**
     * Creates a layered pane and the layers that should be scaled.
     * @return a new freeform layered pane containing the scalable layers
     */
    @objid ("6692353b-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected ScalableFreeformLayeredPane createScaledLayers() {
        ScalableFreeformLayeredPane layers = super.createScaledLayers();
        layers.add(this.createBackgroundLayer(), "BACKGROUND_LAYER", 0);
        return layers;
    }

    @objid ("66923541-33f7-11e2-95fe-001ec947c8cc")
    protected FreeformLayer createBackgroundLayer() {
        FreeformLayer l = new BackgroundLayer();
        l.setOpaque(true);
        l.setBackgroundColor(ColorConstants.black);
        return l;
    }

    @objid ("66923545-33f7-11e2-95fe-001ec947c8cc")
    public void setCreationLocationTip(Point location) {
        if (this.getFigure().containsPoint(location))
            this.creationLocationTip = location;
        else
            this.creationLocationTip = ORIGIN;
    }

    @objid ("66923548-33f7-11e2-95fe-001ec947c8cc")
    public Point getCreationLocationTip() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.creationLocationTip;
    }

}

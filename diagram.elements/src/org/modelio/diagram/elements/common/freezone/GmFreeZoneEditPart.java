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
                                    

package org.modelio.diagram.elements.common.freezone;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagramStyleKeys;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.styles.core.IStyle;

/**
 * {@link GmFreeZone} edit part.
 */
@objid ("7e3f2368-1dec-11e2-8cad-001ec947c8cc")
public class GmFreeZoneEditPart extends GmNodeEditPart {
    /**
     * c'tor.
     */
    @objid ("7e3f236a-1dec-11e2-8cad-001ec947c8cc")
    public GmFreeZoneEditPart() {
        super();
    }

    @objid ("7e3f236d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // If the visibility of the zone changes, it will be notified by a 
        // PROPERTY_LAYOUTDATA event.
        if (evt.getPropertyName() == IGmObject.PROPERTY_LAYOUTDATA) {
            updateVisibility(getFigure());
        }
        
        super.propertyChange(evt);
    }

    @objid ("7e3f2371-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        updateVisibility(getFigure());
        
        super.addChildVisual(childEditPart, index);
    }

    @objid ("7e3f2378-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        final DefaultFreeZoneLayoutEditPolicy layoutEditPolicy = new DefaultFreeZoneLayoutEditPolicy();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutEditPolicy);
    }

    @objid ("7e3f237b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        Figure groupFigure = new Figure();
        
        // Style independent properties
        groupFigure.setOpaque(false);
        groupFigure.setBackgroundColor(null);
        groupFigure.setBorder(new MarginBorder(3, 2, 3, 2));
        final FreeZoneLayout layout = new FreeZoneLayout();
        groupFigure.setLayoutManager(layout);
        
        // Set style dependent properties
        refreshFromStyle(groupFigure, getModelStyle());
        return groupFigure;
    }

    /**
     * Get the GmFreeZone model.
     * @return the GmFreeZone.
     */
    @objid ("7e3f2382-1dec-11e2-8cad-001ec947c8cc")
    protected GmFreeZone getZoneModel() {
        return (GmFreeZone) getModel();
    }

    @objid ("7e3f2387-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure fig, IStyle style) {
        super.refreshFromStyle(fig, style);
        updateVisibility(fig);
    }

    @objid ("7e3f238e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
        
        updateVisibility(getFigure());
    }

    /**
     * Updates the visibility of the zone's figure.
     * @param aFigure the zone's figure.
     */
    @objid ("7e3f2394-1dec-11e2-8cad-001ec947c8cc")
    private void updateVisibility(IFigure aFigure) {
        final GmFreeZone gmZone = (GmFreeZone) getModel();
        final boolean oldVisible = (aFigure.isVisible());
        final boolean newVisible = gmZone.isVisible();
        
        if (oldVisible == newVisible)
            return;
        
        aFigure.setVisible(newVisible);
    }

    @objid ("7e3f239a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        return false;
    }

    /**
     * In order to provide snap to grid facility the AbstractEditPart must be adaptable to a SnapToHelper. Here we adapt
     * to a SnapToGrid only if the style defines SNAPTOGRID = true
     */
    @objid ("7e4185aa-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class type) {
        if (type == SnapToHelper.class) {
            // Fetch the stylekey value on the style of the diagram (this is a property of the diagram, and relying on cascaded styles is not a good idea).
            boolean snap = ((GmAbstractObject) this.getModel()).getDiagram()
                                                               .getStyle()
                                                               .getBoolean(GmAbstractDiagramStyleKeys.SNAPTOGRID);
            if (snap) {
                return new SnapToGrid(this);
            }
        }
        return super.getAdapter(type);
    }

}

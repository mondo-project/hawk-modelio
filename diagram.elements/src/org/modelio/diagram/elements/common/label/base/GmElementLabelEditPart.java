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
                                    

package org.modelio.diagram.elements.common.label.base;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.FillMode;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * EditPart for {@link GmElementLabel} around connection links or inside GmGroups..
 * <p>
 * Creates a {@link Label} as figure.
 * 
 * @author cmarin
 */
@objid ("7e90337a-1dec-11e2-8cad-001ec947c8cc")
public class GmElementLabelEditPart extends GmNodeEditPart {
    /**
     * Creates the label edit part.
     */
    @objid ("7e90337c-1dec-11e2-8cad-001ec947c8cc")
    public GmElementLabelEditPart() {
        super();
    }

    /**
     * Creates the label edit part.
     * @param grLabel the label model.
     */
    @objid ("7e90337f-1dec-11e2-8cad-001ec947c8cc")
    public GmElementLabelEditPart(GmElementLabel grLabel) {
        super();
        setModel(grLabel);
    }

    @objid ("7e903383-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Label label = (Label) getFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    final Rectangle rect2 = label.getTextBounds().getCopy();
        
                    label.translateToAbsolute(rect);
                    // label.translateToAbsolute(rect2);
        
                    cellEditor.getControl().setBounds(rect.x, rect.y + (rect.height / 2) - (rect2.height / 2),
                            Math.max(rect2.width, rect.width), rect2.height);
                }
            };
        
            DirectEditManager2 manager = new DirectEditManager2(this, TextCellEditor.class, cellEditorLocator) {
        
                @Override
                protected void initCellEditor() {
                    final TextCellEditor textEdit = (TextCellEditor) this.getCellEditor();
                    textEdit.setStyle(SWT.CENTER);
                    textEdit.setValue(((GmModel) getModel()).getEditableText().getText());
        
                    final Text textControl = (Text) textEdit.getControl();
                    textControl.selectAll();
                    textControl.setBackground(ColorConstants.white);
                    textControl.setForeground(ColorConstants.blue);
        
                    super.initCellEditor();
                }
            };
        
            manager.show();
        
        }
        super.performRequest(req);
    }

    /**
     * Added the handling of LABEL property change events: updates the visual and requests a resize to preferred size if available.
     */
    @objid ("7e903389-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            // Take note of the current size.
            final IFigure aFigure = getFigure();
            Dimension currentSize = aFigure.getSize();
            aFigure.translateToAbsolute(currentSize);
        
            refreshVisuals();
        
            // If preferred size if not the same as current size, check if it is
            // possible to resize this figure to its preferred size.
            Dimension updatedPrefSize = aFigure.getPreferredSize().getCopy();
            aFigure.translateToAbsolute(updatedPrefSize);
            Dimension.SINGLETON.width = 0;
            Dimension.SINGLETON.height = 0;
            if (!Dimension.SINGLETON.equals(currentSize) && !updatedPrefSize.equals(currentSize)) {
                ChangeBoundsRequest changeBoundsRequest = new ChangeBoundsRequest(REQ_RESIZE);
                changeBoundsRequest.setEditParts(this);
                changeBoundsRequest.setSizeDelta(new Dimension(updatedPrefSize.width - currentSize.width, updatedPrefSize.height
                        - currentSize.height));
                Command resizeCommand = getCommand(changeBoundsRequest);
                if (resizeCommand != null && resizeCommand.canExecute()) {
                    resizeCommand.execute();
                }
            }
        } else {
            super.propertyChange(evt);
        }
    }

    @objid ("7e92959d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        if (((GmModel) getModel()).getEditableText() != null)
            installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
    }

    /**
     * Creates a {@link Label}.
     */
    @objid ("7e9295a0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        final GmElementLabel model = (GmElementLabel) getModel();
        
        final Label f = new Label(model.getLabel());
        
        // Set style independent properties
        f.setLabelAlignment(PositionConstants.LEFT);
        f.setOpaque(false);
        
        // Set style dependent properties
        refreshFromStyle(f, model.getStyle());
        return f;
    }

    @objid ("7e9295a8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        final GmElementLabel model = (GmElementLabel) getModel();
        Label fig = (Label)aFigure;
        
        StyleKey textColorStyleKey = model.getStyleKey(MetaKey.TEXTCOLOR);
        if (textColorStyleKey != null) {
            fig.setForegroundColor(style.getColor(textColorStyleKey));
        }
        
        StyleKey fontStyleKey = model.getStyleKey(MetaKey.FONT);
        if (fontStyleKey != null) {
            fig.setFont(style.getFont(fontStyleKey));
        }
        
        updateVisibility(aFigure);
    }

    @objid ("7e9295af-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final GmElementLabel model = (GmElementLabel) getModel();
        final Label labelFigure = (Label) getFigure();
        labelFigure.setText(model.getLabel());
        labelFigure.getParent().setConstraint(labelFigure, model.getLayoutData());
    }

    @objid ("7e9295b2-1dec-11e2-8cad-001ec947c8cc")
    protected void updateVisibility(IFigure aFigure) {
        final boolean visible = ((GmElementLabel) getModel()).isVisible();
        if (visible)
            aFigure.setVisible(true);
        else
            aFigure.setVisible(false);
    }

}

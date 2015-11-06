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
                                    

package org.modelio.diagram.elements.common.text;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
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
import org.modelio.diagram.elements.common.edition.MultilineTextCellEditor;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;

/**
 * EditPart for {@link GmElementText} ..
 * <p>
 * Creates a {@link MultilineTextFigure} as figure.
 * 
 * @author cmarin
 */
@objid ("7f2b2c33-1dec-11e2-8cad-001ec947c8cc")
public class GmElementTextEditPart extends GmNodeEditPart {
    /**
     * Creates the edit part.
     */
    @objid ("7f2b2c35-1dec-11e2-8cad-001ec947c8cc")
    public GmElementTextEditPart() {
        super();
    }

    /**
     * Redefined to handle direct edition of the text.
     * @see RequestConstants#REQ_DIRECT_EDIT
     */
    @objid ("7f2b2c38-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final MultilineTextFigure label = (MultilineTextFigure) getFigure();
                    final Rectangle rect = label.getBounds().getCopy();
        
                    label.translateToAbsolute(rect);
                    // label.translateToAbsolute(rect2);
        
                    cellEditor.getControl().setBounds(rect.x, rect.y, rect.width, rect.height);
                }
            };
        
            DirectEditManager2 manager = new DirectEditManager2(this,
                                                                MultilineTextCellEditor.class,
                                                                cellEditorLocator) {
        
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
     * Added the handling of LABEL property change events: updates the visual and requests a resize to preferred size if
     * available.
     */
    @objid ("7f2b2c3f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            // Take note of the current size.
            final IFigure aFigure = getFigure();
            final Dimension currentSize = aFigure.getSize();
            aFigure.translateToAbsolute(currentSize);
        
            refreshVisuals();
        
            // If preferred size is not the same as current size, check if it is
            // possible to resize this figure to its preferred size.
            final Dimension updatedPrefSize = aFigure.getPreferredSize().getCopy();
            aFigure.translateToAbsolute(updatedPrefSize);
            if (!currentSize.isEmpty() && !updatedPrefSize.equals(currentSize)) {
                final ChangeBoundsRequest changeBoundsRequest = new ChangeBoundsRequest(REQ_RESIZE);
                changeBoundsRequest.setEditParts(this);
                changeBoundsRequest.setSizeDelta(updatedPrefSize.getShrinked(currentSize));
        
                final Command resizeCommand = getCommand(changeBoundsRequest);
                if (resizeCommand != null && resizeCommand.canExecute()) {
                    resizeCommand.execute();
                }
            }
        } else {
            super.propertyChange(evt);
        }
    }

    @objid ("7f2b2c44-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        //super.createEditPolicies();
        
        if (((GmModel) getModel()).getEditableText() != null)
            installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
    }

    /**
     * Creates a {@link MultilineTextFigure}.
     */
    @objid ("7f2b2c47-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        final GmElementText model = (GmElementText) getModel();
        
        final MultilineTextFigure f = new MultilineTextFigure(model.getText());
        
        // Set style independent properties
        f.setTextAlignment(PositionConstants.LEFT);
        f.setOpaque(false);
        
        // Set style dependent properties
        refreshFromStyle(f, model.getStyle());
        return f;
    }

    @objid ("7f2b2c4f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        final GmElementText model = (GmElementText) getModel();
        
        aFigure.setForegroundColor(style.getColor(model.getStyleKey(MetaKey.TEXTCOLOR)));
        aFigure.setFont(style.getFont(model.getStyleKey(MetaKey.FONT)));
        
        updateVisibility(aFigure);
    }

    @objid ("7f2b2c56-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final GmElementText model = (GmElementText) getModel();
        final MultilineTextFigure labelFigure = (MultilineTextFigure) getFigure();
        
        labelFigure.setText(model.getText());
        
        final Object layoutData = model.getLayoutData();
        if (layoutData != null)
            labelFigure.getParent().setConstraint(labelFigure, layoutData);
    }

    @objid ("7f2b2c59-1dec-11e2-8cad-001ec947c8cc")
    private void updateVisibility(IFigure aFigure) {
        final boolean visible = ((GmElementText) getModel()).isVisible();
        if (visible)
            aFigure.setVisible(true);
        else
            aFigure.setVisible(false);
    }

    @objid ("7f2b2c5e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        return false;
    }

}

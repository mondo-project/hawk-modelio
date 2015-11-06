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
                                    

package org.modelio.diagram.elements.drawings.text;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.edition.MultilineTextCellEditor;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.drawings.core.HAlign;
import org.modelio.diagram.elements.drawings.core.node.NodeDrawingEditPart;
import org.modelio.diagram.elements.drawings.rectangle.GmRectangleDrawing;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Edit part for {@link GmRectangleDrawing}.
 */
@objid ("1b8943ba-1e24-4f85-b1cc-476a87083c78")
public class TextDrawingEditPart extends NodeDrawingEditPart {
    @objid ("397b4498-0915-4b8c-b515-b81963fd78e2")
    @Override
    protected IFigure createFigure() {
        ResizeableTextFigure f = new ResizeableTextFigure();
        
        // set style independent properties
        f.setMinimumSize(new Dimension(50, 50));
        //f.setContents("test");
        
        // set style dependent properties
        refreshFromStyle(f, getModelStyle());
        
        f.setPreferredSize(50,30);
        f.setOpaque(false);
        
        // return the figure
        return f;
    }

    @objid ("205f29e2-ca5c-4c6c-9413-e870e11dec0d")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        super.refreshFromStyle(aFigure, style);
        
        final GmTextDrawing model = getModel();
        
        StyleKey textColorStyleKey = model.getStyleKey(MetaKey.TEXTCOLOR);
        if (textColorStyleKey != null) {
            aFigure.setForegroundColor(style.getColor(textColorStyleKey));
        }
        
        StyleKey fontStyleKey = model.getStyleKey(MetaKey.FONT);
        if (fontStyleKey != null) {
            aFigure.setFont(style.getFont(fontStyleKey));
        }
        
        // Alignment
        ResizeableTextFigure f = (ResizeableTextFigure) aFigure;
        HAlign align = style.getProperty(GmTextStyleKeys.ALIGNMENT);
        f.setHorizontalAligment(align);
    }

    @objid ("28d8ab32-c70b-4b3d-ad0c-b15a5d8674bc")
    @Override
    protected void refreshVisuals() {
        final ResizeableTextFigure aFigure = getFigure();
        final GmTextDrawing noteModel = getModel();
        
        aFigure.getParent().setConstraint(aFigure, noteModel.getLayoutData());
        
        aFigure.setContents(noteModel.getLabel());
    }

    @objid ("35428fac-573f-4316-9961-d53dcec990d3")
    @Override
    public boolean isResizeable() {
        return true;
    }

    @objid ("39b0514c-db95-415d-87fa-6e0e6598182a")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new TextDrawingDirectEditPolicy());
    }

    @objid ("763f7fa4-9077-45e7-aebb-77ae04fad199")
    @Override
    public ResizeableTextFigure getFigure() {
        return (ResizeableTextFigure) super.getFigure();
    }

    @objid ("f364f6e7-56e0-4fb9-b4d2-ab87beb70abc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (IGmObject.PROPERTY_LABEL.equals(propertyName)) {
            refreshVisuals();
        } else
            super.propertyChange(evt);
    }

    @objid ("9888ced5-b9da-46db-bc3a-83e9a07c89b0")
    private void doPropertyChange(PropertyChangeEvent evt) {
    }

    @objid ("581f316f-cdb8-45dc-b4bc-d7a1ff5d797a")
    @Override
    public GmTextDrawing getModel() {
        return (GmTextDrawing) super.getModel();
    }

    @objid ("0e40132d-e637-42e1-837f-06f4d68d5184")
    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final ResizeableTextFigure label = getFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    final Rectangle rect2 = rect.getResized(label.getPreferredSize(rect.width, -1));
        
                    label.translateToAbsolute(rect);
                    // label.translateToAbsolute(rect2);
        
                    cellEditor.getControl().setBounds(rect.x,
                                                      rect.y ,
                                                      rect.width,
                                                      rect.height);
                }
            };
        
            DirectEditManager2 manager = new DirectEditManager2(this, MultilineTextCellEditor.class, cellEditorLocator) {
        
                @Override
                protected void initCellEditor() {
                    final MultilineTextCellEditor textEdit = (MultilineTextCellEditor) this.getCellEditor();
                    textEdit.setStyle(SWT.CENTER);
                    textEdit.setValue(getModel().getLabel());
        
                    final Text textControl = (Text) textEdit.getControl();
                    textControl.selectAll();
                    textControl.setBackground(ColorConstants.white);
                    textControl.setForeground(ColorConstants.blue);
        
                    super.initCellEditor();
                }
                
              
            };
        
            manager.show();
        
        } else {
            super.performRequest(req);
        }
    }

}

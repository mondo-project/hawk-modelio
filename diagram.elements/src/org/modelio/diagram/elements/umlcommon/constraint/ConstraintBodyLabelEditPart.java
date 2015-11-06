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
                                    

package org.modelio.diagram.elements.umlcommon.constraint;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.edition.MultilineTextCellEditor;
import org.modelio.diagram.elements.common.header.GmModelElementHeader;
import org.modelio.diagram.elements.common.header.ModelElementHeaderEditPart;
import org.modelio.diagram.elements.common.header.WrappedHeaderFigure;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Specialization of the {@link ModelElementHeaderEditPart} providing a {@link MultilineTextCellEditor} instead of a {@link TextCellEditor}.
 */
@objid ("76297c0d-6e41-4105-8244-c1d046bb357f")
public class ConstraintBodyLabelEditPart extends ModelElementHeaderEditPart {
    @objid ("e31b2857-c01f-4e53-98e4-d988461ba78f")
    @Override
    public void performRequest(Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            GmModelElementHeader gm = (GmModelElementHeader) getModel();
            if (gm.getRelatedElement() == null || gm.getRelatedElement().isShell() || gm.getRelatedElement().isDeleted()
                    || !gm.getRelatedElement().getStatus().isModifiable()) {
                return;
            }
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    TextFlow label = getMainLabelFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    final Dimension textSize = TextUtilities.INSTANCE.getTextExtents(label.getText(), cellEditor.getControl()
                            .getFont());
        
                    label.translateToAbsolute(rect);
        
                    cellEditor.getControl().setBounds(rect.x, rect.y + (rect.height / 2) - textSize.height / 2,
                            Math.max(textSize.width, rect.width), textSize.height);
        
                }
        
            };
        
            final IEditableText editableText = ((GmModel) getModel()).getEditableText();
            if (editableText != null) {
                DirectEditManager2 manager = new DirectEditManager2(this, MultilineTextCellEditor.class, cellEditorLocator) {
        
                    @Override
                    protected void initCellEditor() {
        
                        final TextCellEditor textEdit = (TextCellEditor) this.getCellEditor();
                        textEdit.setStyle(SWT.CENTER);
                        textEdit.setValue(editableText.getText());
        
                        final Text textControl = (Text) textEdit.getControl();
                        textControl.selectAll();
                        textControl.setBackground(ColorConstants.white);
                        textControl.setForeground(ColorConstants.blue);
        
                        super.initCellEditor();
                    }
        
                };
        
                manager.show();
            }
        } else {
            super.performRequest(req);
        }
    }

    @objid ("b69b320c-0ee2-4f20-9e05-28db8c86d862")
    @Override
    protected IFigure createFigure() {
        final WrappedHeaderFigure fig = (WrappedHeaderFigure) super.createFigure();
        updateAlignment(fig, ((GmModel)getModel()).getStyle());
        return fig;
    }

    @objid ("18ccb615-545b-4b3d-bd91-348fd1b3260a")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        super.refreshFromStyle(aFigure, style);
        
        final WrappedHeaderFigure fig = (WrappedHeaderFigure) this.figure;
        if (fig != null) {
            updateAlignment(fig, style);
        }
    }

    @objid ("76a5fe82-95e1-48d7-9d7e-04b9ba750d42")
    protected void updateAlignment(final WrappedHeaderFigure fig, IStyle style) {
        // Alignment
        HAlign align = style.getProperty(GmConstraintStyleKeys.ALIGNMENT);
        switch (align) {
        case Left:
            fig.setMainLabelAlignement(PositionConstants.LEFT);
            break;
        case Right:
            fig.setMainLabelAlignement(PositionConstants.RIGHT);
            break;
        default:
        case Center:
            fig.setMainLabelAlignement(PositionConstants.CENTER);
            break;
        
        }
    }

}

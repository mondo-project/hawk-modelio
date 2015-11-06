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
                                    

package org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.common.label.base.GmElementLabelEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;

@objid ("d90b2a52-55b6-11e2-877f-002564c97630")
public class GuardEditPart extends GmElementLabelEditPart {
    @objid ("d90b2a55-55b6-11e2-877f-002564c97630")
    public GuardEditPart(final GmElementLabel label) {
        super(label);
    }

    @objid ("d90b2a5b-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        IFigure fig = new Figure();
        fig.setOpaque(false);
        fig.setLayoutManager(new BorderLayout());
        IFigure label = super.createFigure();
        label.setOpaque(true);
        label.setBackgroundColor(ColorConstants.white);
        fig.add(label, BorderLayout.LEFT, 0);
        IFigure fig2 = new Figure();
        fig2.setOpaque(false);
        fig2.setLayoutManager(new BorderLayout());
        fig.add(fig2, BorderLayout.CENTER, 1);
        return fig;
    }

    @objid ("d90cb0ba-55b6-11e2-877f-002564c97630")
    @Override
    public void performRequest(final Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Label label = (Label) getFigure().getChildren().get(0);
                    final Rectangle rect = label.getBounds().getCopy();
                    final Rectangle rect2 = label.getTextBounds().getCopy();
        
                    label.translateToAbsolute(rect);
                    // label.translateToAbsolute(rect2);
        
                    cellEditor.getControl().setBounds(rect.x,
                                                      rect.y + (rect.height / 2) - (rect2.height / 2),
                                                      Math.max(rect2.width, rect.width),
                                                      rect2.height);
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

    @objid ("d90cb0bf-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        final GmElementLabel model = (GmElementLabel) getModel();
        
        aFigure.setForegroundColor(style.getColor(model.getStyleKey(MetaKey.TEXTCOLOR)));
        aFigure.setFont(style.getFont(model.getStyleKey(MetaKey.FONT)));
    }

    @objid ("d90cb0c8-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        final GmElementLabel model = (GmElementLabel) getModel();
        final IFigure fig = getFigure();
        
        ((Label) fig.getChildren().get(0)).setText(model.getLabel());
        
        fig.getParent().setConstraint(fig, model.getLayoutData());
    }

}

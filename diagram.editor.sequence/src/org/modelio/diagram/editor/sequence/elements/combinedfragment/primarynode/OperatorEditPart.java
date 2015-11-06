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
                                    

package org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
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
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.IBrushOptionsSupport;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.FillMode;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Specialisation of the {@link GmElementLabelEditPart} to provide a specialised figure (with the side decoration).
 * 
 * @author fpoyer
 */
@objid ("d8d12e96-55b6-11e2-877f-002564c97630")
public class OperatorEditPart extends GmElementLabelEditPart {
    @objid ("d8d12e9a-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        GmElementLabel model = (GmElementLabel) getModel();
        
        Figure fig = new GradientFigure();
        fig.setLayoutManager(new BorderLayout());
        fig.setOpaque(true);
        Figure fig2 = new Figure();
        fig2.setLayoutManager(new BorderLayout());
        fig2.setOpaque(false);
        fig.add(fig2, BorderLayout.TOP, 0);
        Figure placeHolder = new Figure();
        placeHolder.setOpaque(false);
        placeHolder.setLayoutManager(new BorderLayout());
        fig.add(placeHolder, BorderLayout.CENTER, 1);
        
        OperatorLabel label = new OperatorLabel(model.getLabel());
        label.setLabelAlignment(PositionConstants.LEFT);
        label.setOpaque(true);
        label.setBorder(new MarginBorder(5, 2, 5, 10));
        fig2.add(label, BorderLayout.LEFT, 0);
        placeHolder = new Figure();
        placeHolder.setOpaque(false);
        placeHolder.setLayoutManager(new BorderLayout());
        fig2.add(placeHolder, BorderLayout.CENTER, 1);
        
        //fig.setBorder(new TLBRBorder(true, true, false, true));
        
        refreshFromStyle(fig, model.getStyle());
        return fig;
    }

    @objid ("d8d2b4fb-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        final GmModel gmModel = (GmModel) getModel();
        // Refresh the main figure
        
        // Set pen properties where applicable
        if (aFigure instanceof IPenOptionsSupport) {
            final IPenOptionsSupport pen = (IPenOptionsSupport) aFigure;
            if (gmModel.getStyleKey(MetaKey.FONT) != null)
                pen.setTextFont(style.getFont(gmModel.getStyleKey(MetaKey.FONT)));
            if (gmModel.getStyleKey(MetaKey.TEXTCOLOR) != null)
                pen.setTextColor(style.getColor(gmModel.getStyleKey(MetaKey.TEXTCOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINECOLOR) != null)
                pen.setLineColor(style.getColor(gmModel.getStyleKey(MetaKey.LINECOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINEWIDTH) != null)
                pen.setLineWidth(style.getInteger(gmModel.getStyleKey(MetaKey.LINEWIDTH)));
            if (gmModel.getStyleKey(MetaKey.LINEPATTERN) != null) {
                LinePattern linePattern = style.getProperty(gmModel.getStyleKey(MetaKey.LINEPATTERN));
                pen.setLinePattern(linePattern);
            }
        }
        
        // Set brush properties where applicable
        if (aFigure instanceof IBrushOptionsSupport) {
            final IBrushOptionsSupport brush = (IBrushOptionsSupport) aFigure;
        
            if (gmModel.getStyleKey(MetaKey.FILLCOLOR) != null)
                brush.setFillColor(style.getColor(gmModel.getStyleKey(MetaKey.FILLCOLOR)));
        
            if (gmModel.getStyleKey(MetaKey.FILLMODE) != null) {
                switch ((FillMode) style.getProperty(gmModel.getStyleKey(MetaKey.FILLMODE))) {
                    case GRADIENT:
                        brush.setUseGradient(true);
                        break;
                    case SOLID:
                        brush.setUseGradient(false);
                        break;
                    case TRANSPARENT:
                        brush.setFillColor(null);
                        break;
                }
            }
        }
        
        // Now also refresh the label specifically: use a darker shade of the fill color for background, and use all pen options.
        
        // Passed figure is 'fig' (see createFigure method) => fetch fig2 in fig and then label in fig2.
        IFigure labelFigure = (IFigure) ((IFigure) aFigure.getChildren().get(0)).getChildren().get(0);
        if (gmModel.getStyleKey(MetaKey.FILLCOLOR) != null) {
            labelFigure.setBackgroundColor(FigureUtilities.darker(style.getColor(gmModel.getStyleKey(MetaKey.FILLCOLOR))));
        }
        // Set pen properties where applicable
        if (labelFigure instanceof IPenOptionsSupport) {
            final IPenOptionsSupport pen = (IPenOptionsSupport) labelFigure;
            if (gmModel.getStyleKey(MetaKey.FONT) != null)
                pen.setTextFont(style.getFont(gmModel.getStyleKey(MetaKey.FONT)));
            if (gmModel.getStyleKey(MetaKey.TEXTCOLOR) != null)
                pen.setTextColor(style.getColor(gmModel.getStyleKey(MetaKey.TEXTCOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINECOLOR) != null)
                pen.setLineColor(style.getColor(gmModel.getStyleKey(MetaKey.LINECOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINEWIDTH) != null)
                pen.setLineWidth(style.getInteger(gmModel.getStyleKey(MetaKey.LINEWIDTH)));
            if (gmModel.getStyleKey(MetaKey.LINEPATTERN) != null) {
                LinePattern linePattern = style.getProperty(gmModel.getStyleKey(MetaKey.LINEPATTERN));
                pen.setLinePattern(linePattern);
            }
        }
    }

    @objid ("d8d2b504-55b6-11e2-877f-002564c97630")
    @Override
    public void performRequest(final Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Label label = (Label) ((IFigure) getFigure().getChildren().get(0)).getChildren()
                                                                                            .get(0);
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

    @objid ("d8d2b509-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        final GmElementLabel model = (GmElementLabel) getModel();
        // Update label.
        ((Label) ((IFigure) getFigure().getChildren().get(0)).getChildren().get(0)).setText(model.getLabel());
        // Update constraint.
        //getFigure().getParent().setConstraint(getFigure(), model.getLayoutData());
    }

    @objid ("d8d2b50c-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

}

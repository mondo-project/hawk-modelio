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
                                    

package org.modelio.diagram.elements.common.label.modelelement;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.common.label.base.GmElementLabelEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ShowStereotypeMode;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Manages a {@link GmModelElementFlatHeader}.
 * <p>
 * The unique editing policy is the text edition of the main label.
 */
@objid ("7e9c1f3b-1dec-11e2-8cad-001ec947c8cc")
public class ModelElementFlatHeaderEditPart extends GmElementLabelEditPart {
    @objid ("7e9c1f3d-1dec-11e2-8cad-001ec947c8cc")
    private static final List<Image> emptyImageList = Collections.emptyList();

    /**
     * If asked for a resize policy, return the default but configured to allow resize only in the horizontal dimension.
     */
    @objid ("7e9c1f41-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public SelectionEditPolicy getPreferredDragRolePolicy(String requestType) {
        if (REQ_RESIZE.equals(requestType)) {
            DefaultNodeResizableEditPolicy policy = new DefaultNodeResizableEditPolicy();
            policy.setResizeDirections(PositionConstants.EAST_WEST);
            return policy;
        }
        // else
        return super.getPreferredDragRolePolicy(requestType);
    }

    @objid ("7e9e815e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Label label = getMainLabelFigure();
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
        
        } else {
            super.performRequest(req);
        }
    }

    @objid ("7e9e8164-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // Create the label figure
        final ModelElementFlatHeaderFigure headerFigure = new ModelElementFlatHeaderFigure();
        
        // Set style independent properties
        headerFigure.setLineWidth(0);
        // TODO determine correct min size to show at least "..."
        headerFigure.setMinimumSize(new Dimension(20, 20));
        
        final GmModelElementFlatHeader gm = (GmModelElementFlatHeader) getModel();
        // Main label
        refreshLabel(headerFigure);
        
        // Tagged values
        refreshTaggedValues(headerFigure);
        
        // Stereotypes
        refreshStereotypes(headerFigure);
        
        // Metaclass icon
        refreshMetaclassIcon(headerFigure, gm);
        
        // Metaclass keyword
        refreshMetaclassKeyword(headerFigure, gm);
        
        // Set style dependent properties
        refreshFromStyle(headerFigure, getModelStyle());
        return headerFigure;
    }

    @objid ("7e9e816b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure fig, IStyle style) {
        // Pen and brush options cannot simply managed by the superclass which manages a "Label" not a "GradientFigure"
        final GmElementLabel model = (GmElementLabel) getModel();
        ModelElementFlatHeaderFigure headerFigure = (ModelElementFlatHeaderFigure)fig;
        
        StyleKey textColorStyleKey = model.getStyleKey(MetaKey.TEXTCOLOR);
        if (textColorStyleKey != null) {
            headerFigure.setTextColor(style.getColor(textColorStyleKey));
        }
        
        StyleKey fontStyleKey = model.getStyleKey(MetaKey.FONT);
        if (fontStyleKey != null) {
            headerFigure.setTextFont(style.getFont(fontStyleKey));
        }
        
        updateVisibility(fig);
        
        
        // Additionally we have to deal with stereotype mode and show/hide for name, stereotypes and tags
        refreshLabel(headerFigure);
        refreshStereotypes(headerFigure);
        refreshTaggedValues(headerFigure);
    }

    /**
     * Refresh the metaclass icon from the given graphic model.
     * @param aFigure The figure to update.
     * @param gm The graphic model to read.
     */
    @objid ("7e9e8172-1dec-11e2-8cad-001ec947c8cc")
    protected static void refreshMetaclassIcon(final ModelElementFlatHeaderFigure aFigure, final GmModelElementFlatHeader gm) {
        if (gm.isShowMetaclassIcon()) {
            aFigure.setLeftIcon(gm.getMetaclassIcon());
        } else {
            aFigure.setLeftIcon(null);
        }
    }

    /**
     * To be called when the stereotype mode changes or when the applied stereotypes change.
     * @param headerFigure The figure to update.
     */
    @objid ("7e9e8179-1dec-11e2-8cad-001ec947c8cc")
    protected final void refreshStereotypes(ModelElementFlatHeaderFigure headerFigure) {
        GmModelElementFlatHeader gm = (GmModelElementFlatHeader) getModel();
        
        StyleKey styleKey = gm.getStyleKey(MetaKey.SHOWSTEREOTYPES);
        if (styleKey == null) {
            return;
        }
        ShowStereotypeMode mode = gm.getStyle().getProperty(styleKey);
        if (mode == null) {
            mode = ShowStereotypeMode.NONE;
        }
        
        switch (mode) {
            case ICON:
                headerFigure.setRightIcons(gm.getStereotypeIcons());
                headerFigure.setStereotypeText("");
                break;
            case TEXT:
                headerFigure.setRightIcons(emptyImageList);
                headerFigure.setStereotypeText(gm.getStereotypesLabel());
                break;
            case TEXTICON:
                headerFigure.setRightIcons(gm.getStereotypeIcons());
                headerFigure.setStereotypeText(gm.getStereotypesLabel());
                break;
            case NONE:
                headerFigure.setRightIcons(emptyImageList);
                headerFigure.setStereotypeText("");
                break;
        }
    }

    @objid ("7e9e817d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final ModelElementFlatHeaderFigure aFigure = (ModelElementFlatHeaderFigure) getFigure();
        final GmModelElementFlatHeader gm = (GmModelElementFlatHeader) getModel();
        
        // Layout data
        aFigure.getParent().setConstraint(aFigure, gm.getLayoutData());
        
        // Main label
        refreshLabel(aFigure);
        
        // Tagged values
        refreshTaggedValues(aFigure);
        
        // Stereotypes
        refreshStereotypes(aFigure);
        
        // Metaclass icon
        refreshMetaclassIcon(aFigure, gm);
        
        // Metaclass keyword
        refreshMetaclassKeyword(aFigure, gm);
    }

    /**
     * Get the main label figure.
     * <p>
     * The main label usually displays the element name.
     * @return the main label figure.
     */
    @objid ("7e9e8180-1dec-11e2-8cad-001ec947c8cc")
    protected final Label getMainLabelFigure() {
        return ((ModelElementFlatHeaderFigure) this.getFigure()).getMainLabelFigure();
    }

    @objid ("7e9e8187-1dec-11e2-8cad-001ec947c8cc")
    private void refreshLabel(ModelElementFlatHeaderFigure headerFigure) {
        GmModelElementFlatHeader gm = (GmModelElementFlatHeader) getModel();
        
        // Ask the gm if the label is shown
        if (gm.isShowLabel()) {
            headerFigure.setMainLabel(gm.getLabel());
        } else {
            headerFigure.setMainLabel("");
        }
    }

    @objid ("7e9e818a-1dec-11e2-8cad-001ec947c8cc")
    private static void refreshMetaclassKeyword(final ModelElementFlatHeaderFigure headerFigure, final GmModelElementFlatHeader gm) {
        if (gm.isShowMetaclassKeyword()) {
            headerFigure.setKeywordText("<<" + gm.getRelatedElement().getMClass().getName() + ">>");
        } else {
            headerFigure.setKeywordText(null);
        }
    }

    /**
     * Refresh the tagged values zone.
     * @param aFigure The figure to update
     */
    @objid ("7e9e8190-1dec-11e2-8cad-001ec947c8cc")
    private void refreshTaggedValues(ModelElementFlatHeaderFigure aFigure) {
        GmModelElementFlatHeader gm = (GmModelElementFlatHeader) getModel();
        
        StyleKey styleKey = gm.getStyleKey(MetaKey.SHOWTAGS);
        if (styleKey == null) {
            return;
        }
        boolean mode = gm.getStyle().getProperty(styleKey);
        if (mode) {
            aFigure.setTagText(gm.getTaggedValuesLabel());
        } else {
            aFigure.setTagText("");
        }
    }

}

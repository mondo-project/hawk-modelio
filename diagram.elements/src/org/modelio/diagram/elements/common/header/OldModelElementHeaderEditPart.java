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
                                    

package org.modelio.diagram.elements.common.header;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ShowStereotypeMode;

/**
 * Manages a GmModelElementHeader. The unique editing policy is the text edition of the main label.
 */
@objid ("7e739756-1dec-11e2-8cad-001ec947c8cc")
public class OldModelElementHeaderEditPart extends GmNodeEditPart {
    @objid ("915b26ce-1e83-11e2-8cad-001ec947c8cc")
    protected static final List<String> emptyLabelList = Collections.emptyList();

    @objid ("242e5285-a41a-487f-9e94-babaf6c5cbf2")
    protected static final List<Image> emptyImageList = Collections.emptyList();

    /**
     * The edit part is selectable only if it is a link label.
     */
    @objid ("7e739760-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        GmNodeModel model = (GmNodeModel) getModel();
        return model.getParentLink() != null;
    }

    @objid ("7e75f967-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        // System.out.println("ModelElementHeaderEditPart performRequest: " +
        // req);
        
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            GmModelElementHeader gm = (GmModelElementHeader) getModel();
            if (gm.getRelatedElement() == null ||
                gm.getRelatedElement().isShell() ||gm.getRelatedElement().isDeleted() ||
                !gm.getRelatedElement().getStatus().isModifiable()) {
                return;
            }
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    Label label = getMainLabelFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    final Rectangle rect2 = label.getTextBounds();
        
                    label.translateToAbsolute(rect);
        
                    cellEditor.getControl().setBounds(rect.x,
                                                      rect.y + (rect.height / 2) - rect2.height / 2,
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
        
        //                    IContextService contextService = (IContextService) PlatformUI.getWorkbench()
        //                                                                                 .getService(IContextService.class);
        //                    contextService.activateContext(IContextService.CONTEXT_ID_DIALOG);
        
                    super.initCellEditor();
                }
        
            };
        
            manager.show();
            // System.out.println("DIRECT Edit request");
        
        } else {
            super.performRequest(req);
        }
    }

    @objid ("7e75f96d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            // Take note of the current size.
            final HeaderFigure aFigure = (HeaderFigure) getFigure();
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
                changeBoundsRequest.setSizeDelta(new Dimension(updatedPrefSize.width - currentSize.width,
                                                               updatedPrefSize.height - currentSize.height));
                Command resizeCommand = getCommand(changeBoundsRequest);
                if (resizeCommand != null && resizeCommand.canExecute()) {
                    resizeCommand.execute();
                }
            }
        
        } else {
            super.propertyChange(evt);
        }
    }

    /**
     * Get the main label figure.
     * <p>
     * The main label usually displays the element name.
     * @return the main label figure.
     */
    @objid ("7e75f971-1dec-11e2-8cad-001ec947c8cc")
    public Label getMainLabelFigure() {
        return ((HeaderFigure) this.getFigure()).getMainLabelFigure();
    }

    @objid ("7e75f978-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
    }

    @objid ("7e75f97b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // Create the class header figure
        final HeaderFigure headerFigure = new HeaderFigure();
        // headerFigure.setSize(300, 100);
        
        // Set style independent properties
        headerFigure.setLineWidth(0);
        final GmModelElementHeader gm = (GmModelElementHeader) getModel();
        
        // Main label
        refreshLabel(headerFigure);
        
        // Tagged values
        refreshTaggedValues(headerFigure);
        
        // Stereotypes
        ShowStereotypeMode mode = getStereotypeMode(gm);
        refreshStereotypes(headerFigure, mode);
        
        // Keyword
        refreshMetaclassKeyword(headerFigure, gm, mode);
        
        // Metaclass icon
        refreshMetaclassIcon(headerFigure, gm, mode);
        
        // Set style dependent properties
        refreshFromStyle(headerFigure, getModelStyle());
        return headerFigure;
    }

    @objid ("7e75f982-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure headerFigure, IStyle style) {
        // Pen and brush options are managed by the superclass
        super.refreshFromStyle(headerFigure, style);
        
        // We have to deal with stereotype mode and show/hide for name, stereotypes and tags
        ShowStereotypeMode mode = getStereotypeMode((GmModelElementHeader) getModel());
        refreshLabel((HeaderFigure) headerFigure);
        refreshStereotypes((HeaderFigure) headerFigure, mode);
        refreshTaggedValues((HeaderFigure) headerFigure);
    }

    /**
     * To be called when the stereotype mode changes or when the applied stereotypes change.
     * @param mode
     * @param headerFigure The figure to update.
     */
    @objid ("7e75f989-1dec-11e2-8cad-001ec947c8cc")
    protected final void refreshStereotypes(HeaderFigure headerFigure, ShowStereotypeMode mode) {
        GmModelElementHeader gm = (GmModelElementHeader) getModel();
        
        switch (mode) {
            case ICON:
                headerFigure.setRightIcons(gm.getStereotypeIcons());
                headerFigure.setTopLabels(emptyLabelList);
                break;
            case TEXT:
                headerFigure.setRightIcons(emptyImageList);
                headerFigure.setTopLabels(gm.getStereotypeLabels());
                break;
            case TEXTICON:
                headerFigure.setRightIcons(gm.getStereotypeIcons());
                headerFigure.setTopLabels(gm.getStereotypeLabels());
                break;
            default:
            case NONE:
                headerFigure.setRightIcons(emptyImageList);
                headerFigure.setTopLabels(emptyLabelList);
                break;
        }
    }

    @objid ("7e75f98d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final HeaderFigure aFigure = (HeaderFigure) getFigure();
        final GmModelElementHeader gm = (GmModelElementHeader) getModel();
        
        ShowStereotypeMode mode = getStereotypeMode(gm);
        
        // Layout data
        final Object layoutData = gm.getLayoutData();
        if (layoutData != null)
            aFigure.getParent().setConstraint(aFigure, layoutData);
        
        // Main label
        refreshLabel(aFigure);
        
        // Tagged values
        refreshTaggedValues(aFigure);
        
        // Stereotypes
        refreshStereotypes(aFigure,mode);
        
        // Metaclass Keyword
        refreshMetaclassKeyword(aFigure, gm, mode);
        
        // Metaclass Icon
        refreshMetaclassIcon(aFigure, gm, mode);
    }

    @objid ("7e75f990-1dec-11e2-8cad-001ec947c8cc")
    private void refreshLabel(HeaderFigure headerFigure) {
        GmModelElementHeader gm = (GmModelElementHeader) getModel();
        
        // Ask the gm if the label is shown
        if (gm.isShowLabel()) {
            headerFigure.setMainLabel(gm.getMainLabel());
        } else {
            headerFigure.setMainLabel("");
        }
    }

    @objid ("7e75f993-1dec-11e2-8cad-001ec947c8cc")
    protected void refreshMetaclassIcon(final HeaderFigure headerFigure, final GmModelElementHeader gm, ShowStereotypeMode mode) {
        ArrayList<Image> icons = new ArrayList<>();
        if (gm.isShowMetaclassIcon() && mode != ShowStereotypeMode.NONE && mode != ShowStereotypeMode.TEXT) {
            icons.add(gm.getMetaclassIcon());
        }
        headerFigure.setLeftIcons(icons);
    }

    @objid ("7e75f999-1dec-11e2-8cad-001ec947c8cc")
    protected void refreshMetaclassKeyword(final HeaderFigure headerFigure, final GmModelElementHeader gm, ShowStereotypeMode mode) {
        if (gm.isShowMetaclassKeyword() && mode != ShowStereotypeMode.NONE && mode != ShowStereotypeMode.ICON ) {
            headerFigure.setKeywordLabel("<<" + gm.getMetaclassKeyword() + ">>");
        } else {
            headerFigure.setKeywordLabel(null);
        }
    }

    /**
     * Refresh the tagged values zone.
     * @param aFigure The figure to update
     */
    @objid ("7e75f99f-1dec-11e2-8cad-001ec947c8cc")
    private void refreshTaggedValues(HeaderFigure aFigure) {
        GmModelElementHeader gm = (GmModelElementHeader) getModel();
        
        boolean mode = gm.getStyle().getProperty(gm.getStyleKey(MetaKey.SHOWTAGS));
        if (mode) {
            aFigure.setBottomLabels(gm.getTaggedValueLabels());
        } else {
            aFigure.setBottomLabels(emptyLabelList);
        }
    }

    @objid ("4282b386-4e94-494e-b042-6b464fb580c5")
    protected ShowStereotypeMode getStereotypeMode(final GmModelElementHeader gm) {
        ShowStereotypeMode mode = gm.getStyle().getProperty(gm.getStyleKey(MetaKey.SHOWSTEREOTYPES));
        if (mode == null) {
            mode = ShowStereotypeMode.NONE;
        }
        return mode;
    }

}

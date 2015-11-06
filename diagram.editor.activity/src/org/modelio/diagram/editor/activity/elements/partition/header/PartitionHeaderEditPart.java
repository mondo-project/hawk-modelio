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
                                    

package org.modelio.diagram.editor.activity.elements.partition.header;

import java.util.ArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.header.OldModelElementHeaderEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.styles.core.StyleKey.ShowStereotypeMode;

/**
 * Specialisation of the ModelElementHeaderEditPart that allows selection while delegating the actual selection feedback
 * to the PartitionEditPart that contains it.
 * 
 * @author fpoyer
 */
@objid ("2b096b29-55b6-11e2-877f-002564c97630")
public class PartitionHeaderEditPart extends OldModelElementHeaderEditPart {
    @objid ("2b09923b-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        final GmPartitionHeader gm = (GmPartitionHeader) getModel();
        if (!gm.isVertical()) {
            // Create an horizontal version of the HeaderFigure
            return createHorizontalHeaderFigure(gm);
        }
        // else
        return super.createFigure();
    }

    /**
     * @return
     */
    @objid ("2b09b94a-55b6-11e2-877f-002564c97630")
    private PartitionHeaderFigure createHorizontalHeaderFigure(GmPartitionHeader gm) {
        final PartitionHeaderFigure headerFigure = new PartitionHeaderFigure();
        // headerFigure.setSize(300, 100);
        
        // Set style independent properties
        headerFigure.setLineWidth(0);
        
        // Main label
        headerFigure.setMainLabel(gm.getMainLabel());
        
        // Stereotypes
        ShowStereotypeMode mode = getStereotypeMode(gm);
        refreshStereotypes(headerFigure, mode);
        
        // Keyword
        if (gm.isShowMetaclassKeyword()) {
            headerFigure.setKeywordLabel("<<" + gm.getRelatedElement().getMClass().getName() + ">>");
        } else {
            headerFigure.setKeywordLabel(null);
        }
        
        // Metaclass icon
        ArrayList<Image> icons = new ArrayList<>();
        if (gm.isShowMetaclassIcon()) {
            icons.add(ElementImageService.getIcon(gm.getRelatedElement()));
        }
        headerFigure.setLeftIcons(icons);
        
        // Set style dependent properties
        refreshFromStyle(headerFigure, getModelStyle());
        return headerFigure;
    }

    @objid ("2b09e059-55b6-11e2-877f-002564c97630")
    @Override
    public void performRequest(Request req) {
        final GmPartitionHeader gm = (GmPartitionHeader) getModel();
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT && !gm.isVertical()) {
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    Label label = getMainLabelFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    rect.setSize(rect.getSize().getTransposed());
                    final Rectangle rect2 = label.getBounds();
                    rect2.setSize(rect2.getSize().getTransposed());
        
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
        } else {
            super.performRequest(req);
        }
    }

}

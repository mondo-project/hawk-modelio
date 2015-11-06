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
                                    

package org.modelio.diagram.elements.umlcommon.note;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.edition.MultilineTextCellEditor;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeEndReconnectEditPolicy;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;

/**
 * Edit part for {@link GmNote}.
 * 
 * @author pvlaemyn
 */
@objid ("818b23fe-1dec-11e2-8cad-001ec947c8cc")
public class NoteEditPart extends GmNodeEditPart {
    @objid ("818b2400-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Figure label = getNoteFigure().getContentFigure();
                    final Rectangle rect = label.getBounds().getCopy();
        
                    label.translateToAbsolute(rect);
        
                    cellEditor.getControl().setBounds(rect.x, rect.y, rect.width, rect.height);
                }
            };
        
            DirectEditManager2 manager = new DirectEditManager2(this,
                                                                MultilineTextCellEditor.class,
                                                                cellEditorLocator) {
        
                @Override
                protected void initCellEditor() {
                    final TextCellEditor textEdit = (TextCellEditor) this.getCellEditor();
                    textEdit.setValue(((GmModel) getModel()).getEditableText().getText());
        
                    final Text textControl = (Text) textEdit.getControl();
                    textControl.selectAll();
                    textControl.setBackground(ColorConstants.white);
                    textControl.setForeground(ColorConstants.blue);
        
                    // Set font
                    textControl.setFont(getNoteFigure().getTextFont());
        
                    super.initCellEditor();
                }
            };
        
            manager.show();
        
        } else {
            super.performRequest(req);
        }
    }

    @objid ("818d861e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            refreshVisuals();
        } else
            super.propertyChange(evt);
    }

    @objid ("818d8622-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy("notelink", new LinkedNodeEndReconnectEditPolicy());
    }

    @objid ("818d8625-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // create the figure
        NoteFigure figure1 = new NoteFigure();
        
        // set style independent properties
        //figure1.setSize(100, 50); 
        figure1.setOpaque(true);
        
        // set style dependent properties
        refreshFromStyle(figure1, getModelStyle());
        
        // return the figure
        return figure1;
    }

    /**
     * Get the note figure.
     * @return The note figure.
     */
    @objid ("818d862c-1dec-11e2-8cad-001ec947c8cc")
    protected final NoteFigure getNoteFigure() {
        return (NoteFigure) getFigure();
    }

    /**
     * Refresh this EditPart's visuals.
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @objid ("818d8631-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final NoteFigure noteFigure = (NoteFigure) getFigure();
        final GmNote noteModel = (GmNote) getModel();
        
        noteFigure.getParent().setConstraint(noteFigure, noteModel.getLayoutData());
        noteFigure.setContents(noteModel.getContents());
        noteFigure.setType(noteModel.getType());
    }

}

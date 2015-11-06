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
                                    

package org.modelio.diagram.elements.umlcommon.externdocument;

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
import org.modelio.api.modelio.Modelio;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeEndReconnectEditPolicy;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;

/**
 * Edit part for {@link GmExternDocument}.
 */
@objid ("814d26ac-1dec-11e2-8cad-001ec947c8cc")
public class ExternDocumentEditPart extends GmNodeEditPart {
    @objid ("814d26ae-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(final Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
        
            if (((GmModel) getModel()).getEditableText() == null)
                return;
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final Figure name = getNoteFigure().getNameFigure();
                    final Rectangle rect = name.getBounds().getCopy();
                    name.translateToAbsolute(rect);
        
                    cellEditor.getControl().setBounds(rect.x, rect.y, rect.width, rect.height);
                }
            };
        
            DirectEditManager2 manager = new DirectEditManager2(this, TextCellEditor.class, cellEditorLocator) {
        
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
        
        } else if (RequestConstants.REQ_OPEN.equals(req.getType())) {
            ExternDocument document = ((GmExternDocument) getModel()).getRepresentedElement();
            Modelio.getInstance().getEditionService().openEditor(document);
        } else {
            super.performRequest(req);
        }
    }

    @objid ("814d26b5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            refreshVisuals();
        } else
            super.propertyChange(evt);
    }

    @objid ("814d26ba-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy("notelink", new LinkedNodeEndReconnectEditPolicy());
    }

    @objid ("814d26bd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // create the figure
        ExternDocumentFigure figure1 = new ExternDocumentFigure();
        
        // set style independent properties
        //figure1.setSize(100, 50); 
        figure1.setOpaque(true);
        
        // set style dependent properties
        refreshFromStyle(figure1, getModelStyle());
        
        // return the figure
        return figure1;
    }

    /**
     * Get the extern document figure.
     * @return The extern document figure.
     */
    @objid ("814f88fb-1dec-11e2-8cad-001ec947c8cc")
    protected final ExternDocumentFigure getNoteFigure() {
        return (ExternDocumentFigure) getFigure();
    }

    /**
     * Refresh this EditPart's visuals.
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @objid ("814f8900-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final ExternDocumentFigure noteFigure = (ExternDocumentFigure) getFigure();
        final GmExternDocument noteModel = (GmExternDocument) getModel();
        
        noteFigure.getParent().setConstraint(noteFigure, noteModel.getLayoutData());
        noteFigure.setName(noteModel.getName());
        noteFigure.setContents(noteModel.getContents());
        noteFigure.setType(noteModel.getType());
        noteFigure.setMimeType(noteModel.getMimeType());
    }

}

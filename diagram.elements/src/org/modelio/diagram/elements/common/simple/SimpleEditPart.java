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
                                    

package org.modelio.diagram.elements.common.simple;

import java.beans.PropertyChangeEvent;
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
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.modelio.diagram.elements.common.edition.DirectEditManager2;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.elements.core.policies.SimpleModeDeferringCreateNodePolicy;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Universal editpart for "simple" mode of any ModelElement It provides a simple rectangle figure with a centered label
 * 
 * 
 * @author pvlaemyn
 */
@objid ("7f240511-1dec-11e2-8cad-001ec947c8cc")
public class SimpleEditPart extends GmNodeEditPart {
    @objid ("7f240513-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This edit part is way too generic to afford being smart: on any property change event, refresh all!
        refresh();
        refreshFromStyle(getFigure(), getModelStyle());
    }

    @objid ("7f240517-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // create the figure
        final SimpleFigure aFigure = new SimpleFigure();
        
        // set style independent properties
        final Dimension d = new Dimension(100, 50);
        aFigure.setPreferredSize(d);
        //        aFigure.setMinimumSize(d);
        
        // set style dependent properties
        refreshFromStyle(aFigure, getModelStyle());
        
        // return the figure
        return aFigure;
    }

    @objid ("7f24051e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (!switchRepresentationMode()) {
            super.refreshFromStyle(aFigure, style);
        }
    }

    @objid ("7f240525-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimpleModeDeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("7f240528-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final GmNodeModel model = (GmNodeModel) this.getModel();
        final SimpleFigure aFigure = (SimpleFigure) this.getFigure();
        
        aFigure.getParent().setConstraint(aFigure, model.getLayoutData());
        
        refreshFigureLabel(model, aFigure);
    }

    /**
     * SimpleEditPart has no model children.
     */
    @objid ("7f24052b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<Object> getModelChildren() {
        // Could also return only the GmModelElementHeader.
        return Collections.emptyList();
    }

    @objid ("7f240533-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            if (((GmModel) getModel()).getEditableText() == null)
                return;
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final SimpleFigure aFigure = (SimpleFigure) getFigure();
                    final Label label = aFigure.getLabelFigure();
                    final Rectangle rect = label.getBounds().getCopy();
                    label.translateToAbsolute(rect);
                    final Rectangle rect2 = label.getTextBounds().getCopy();
                    label.translateToAbsolute(rect2);
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

    /**
     * Refresh the figure label from the model.
     * <p>
     * By defaults:<ul>
     * <li> call {@link GmNodeModel#getEditableText()}
     * <li> fallback to {@link ModelElement#getName()}
     * </ul>
     * May be redefined to improve the label.
     * @param model the node model
     * @param aFigure the figure to refresh.
     */
    @objid ("99f4a628-0520-4406-b282-7b5a3acd0995")
    protected void refreshFigureLabel(final GmNodeModel model, final SimpleFigure aFigure) {
        if (model.getEditableText() != null) {
            aFigure.setLabel(model.getEditableText().getText());
        } else {
            // Ugly fallback: we have to go to the Ob level ..
            final MObject e = model.getRelatedElement();
        
            if (e != null) {
                if (e instanceof ModelElement) {
                    aFigure.setLabel(((ModelElement) e).getName());
                } else {
                    aFigure.setLabel(e.toString());
                }
            }
        }
    }

}

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
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Universal editpart for "simple" mode of potentially any ModelElement It provides a simple rounded rectangle figure
 * with a centered label
 * 
 * 
 * @author pvlaemyn
 */
@objid ("7f1f4061-1dec-11e2-8cad-001ec947c8cc")
public class RoundedSimpleEditPart extends GmNodeEditPart {
    /**
     * @see GmNodeEditPart#propertyChange(java.beans.PropertyChangeEvent)
     */
    @objid ("7f1f4063-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         |  The model change listener method.
         |  This method is called when the model fires a property change event
         |  Typical code consists in testing the PropertyChangeEvent type and
         |  process the proper code.
         |  For IGmObject.PROPERTY_LINK, IGmObject.PROPERTY_CHILDREN,
         |  IGmObject.PROPERTY_LAYOUTDATA and IGmObject.PROPERTY_STYLE event types the super class behaviour
         |  should suffice.
         |  However, if the "GmModel" managed by the edit part fires other types of events,
         |  theses types should be processed here.
         |  Typical code fragment:
         |  if (evt.getPropertyName().equals( "My Special Event Type") {
         |  ...my special processing code...
         |  } else super.propertyChange(evt);
         |
         */
        super.propertyChange(evt);
    }

    /**
     * Creates the Figure to be used as this part's visuals
     * @see org.modelio.diagram.elements.core.editparts.GmNodeEditPart#createFigure()
     */
    @objid ("7f21a2b3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        /*
         | Create (new allocated object) the figure to be used as this part's visuals. 
         | A fake implementation, a RectangleFigure, is coded below. 
         | You are supposed to follow the proposed pattern which clearly distinguishes 
         | between the graphic properties that are controlled by the style and those which are
         | hard-coded
         */
        
        // create the figure
        final RoundedSimpleFigure aFigure = new RoundedSimpleFigure();
        
        // set style independent properties
        aFigure.setSize(100, 50); // TODO: remove/change fake code
        
        // set style dependent properties
        refreshFromStyle(aFigure, getModelStyle());
        
        // return the figure
        return aFigure;
    }

    /**
     * Refresh the figure from the given style.
     * <p>
     * Often called in {@link #createFigure()} and after a style change.
     * @param figure
     * The figure to update, should be {@link #getFigure()}.
     * @param style The style to update from, usually {
     * @link #getModelStyle()}
     */
    @objid ("7f21a2bb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (!switchRepresentationMode()) {
            /*
            | In this method you have to fetch relevant property values from the style 
            | and apply them to the managed figure. Both the figure and the style to use 
            | are passed as parameters, so you are not expected to get them directly from 'this' 
            | This is especially important as the refreshFromStyle method is called in createFigure() 
            | possibly during EditPart initialisation, assuming nothing from 'this' is probably safer 
            | 
            | Typical code: 
            | 
            | MyFigure figure = (MyFigure) aFigure; 
            | figure.setForegroundColor(style.getColor(StyleKey.????.TEXTCOLOR)); 
            | figure.setFont(style.getFont(StyleKey.????.FONT));
            */
            super.refreshFromStyle(aFigure, style);
            //SimpleFigure figure = (SimpleFigure)aFigure;
        }
    }

    /**
     * @see org.modelio.diagram.elements.core.editparts.GmNodeEditPart#createEditPolicies()
     */
    @objid ("7f21a2c3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        /*
         | Installed policies define the editing capabilities of this editpart. 
         | 
         | Be careful when installing policies and follow the rules described below. 
         | and do not forget the motto 'no policy , no cry' 
         | 
         | EditPolicy.DIRECT_EDIT_ROLE 
         |  Install a policy for this role if the edit part provides direct text edition 
         | (ie the figure displays a label that can be edited in the diagram) 
         |
         | Non-graphical roles 
         | EditPolicy.COMPONENT_ROLE 
         |  The fundamental role that most EditParts should have. 
         |  A component is something that is in a parent, and can be deleted from that parent. 
         |  More generally, it is anything that involves only this EditPart (and doesn't involve the view, since it is 
         |  non-graphical).In the Logic example, the LEDEditParts have a specialized EditPolicy in the Component Role 
         |  that knows how to increment the value of the LED object. 
         | 
         | EditPolicy.CONTAINER_ROLE 
         |  The fundamental role that most EditParts with children should have.A container may be involved in 
         |  adds/orphans, and creates/deletes. 
         | 
         | EditPolicy.NODE_ROLE 
         |  The fundamental role that most node EditParts (parts which have connections to them) should have.The node 
         |  role may participate in connection creates, reconnects, and deletions. 
         | 
         | --- Graphical Roles 
         | EditPolicy.PRIMARY_DRAG_ROLE 
         |  Used to allow the user to drag the EditPart.The user may drag it directly by clicking and dragging, or 
         |  perhaps indirectly by clicking on a Handle which that part created.  
         | 
         | EditPolicy.LAYOUT_ROLE 
         |  The Layout role is placed on a container EditPart that has a graphical layout.If the layout has constraints, 
         |  it will handle calculating the proper constraints for the input, or it may have no constraints other than the 
         |  index where children will be placed. 
         | 
         | EditPolicy.GRAPHICAL_NODE_ROLE 
         |  A node supports connections to terminals.When creating and manipulating connections, EditPolicies with this 
         |  role might analyze a Request's data to perform "hit testing" on the graphical view and determine the 
         |  semantics of the connection. 
         | 
         | EditPolicy.SELECTION_FEEDBACK_ROLE 
         |  This role is a feedback only.The SelectionTool will send two types of requests to parts as the mouse enters 
         |  and pauses over objects.EditPolicies implementing this role may alter the EditPart's view in some way, or 
         |  popup hints and labels and the like. 
         */
        
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @objid ("7f21a2c7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final GmNodeModel model = (GmNodeModel) this.getModel();
        final RoundedSimpleFigure aFigure = (RoundedSimpleFigure) this.getFigure();
        
        aFigure.getParent().setConstraint(aFigure, model.getLayoutData());
        
        // Ugly we have to go to the Ob level ..
        final MObject e = model.getRelatedElement();
        if (e instanceof ModelElement) {
            aFigure.setLabel(((ModelElement) e).getName());
        } else {
            aFigure.setLabel(e.toString());
        }
    }

    /**
     * SimpleEditPart has no model children.
     */
    @objid ("7f21a2cb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<Object> getModelChildren() {
        // Could also return only the GmModelElementHeader.
        return Collections.emptyList();
    }

    @objid ("7f21a2d3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
        
            final CellEditorLocator cellEditorLocator = new CellEditorLocator() {
                @Override
                public void relocate(CellEditor cellEditor) {
                    final SimpleFigure aFigure = (SimpleFigure) getFigure();
                    final Label label = aFigure.getLabelFigure();
                    final Rectangle rect = label.getBounds();
                    final Rectangle rect2 = label.getTextBounds();
                    cellEditor.getControl().setBounds(rect.x,
                                                      rect.y + (rect.height / 2) - rect2.height / 2,
                                                      rect.width,
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
        
                }
        
            };
        
            manager.show();
            //System.out.println("DIRECT Edit request");
        
        }
        super.performRequest(req);
    }

}

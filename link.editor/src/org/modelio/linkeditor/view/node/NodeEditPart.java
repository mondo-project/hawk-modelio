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
                                    

package org.modelio.linkeditor.view.node;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.linkeditor.plugin.LinkEditor;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.ui.UIColor;

/**
 * Edit part for the GraphNode model.
 * 
 * @author fpoyer
 */
@objid ("1bb294a3-5e33-11e2-b81d-002564c97630")
public class NodeEditPart extends AbstractGraphicalEditPart implements org.eclipse.gef.NodeEditPart {
    /**
     * Flag set externally telling if the view has the focus.
     */
    @objid ("1bb294a9-5e33-11e2-b81d-002564c97630")
    public static boolean hasFocus = false;

    @objid ("1bb294ab-5e33-11e2-b81d-002564c97630")
    @Override
    protected IFigure createFigure() {
        Figure rectangleFigure = new Figure();
        rectangleFigure.setOpaque(true);
        GridLayout manager = new GridLayout(1, true);
        manager.marginHeight = GraphNode.MARGIN_HEIGHT;
        manager.marginWidth = GraphNode.MARGIN_WIDTH;
        manager.verticalSpacing = GraphNode.VERTICAL_SPACING;
        rectangleFigure.setLayoutManager(manager);
        rectangleFigure.setBorder(new LineBorder(ColorConstants.lightGray, 1, SWT.LINE_SOLID));
        Label label = new Label();
        label.setLabelAlignment(PositionConstants.LEFT);
        label.setToolTip(new Label());
        GridData data = new GridData(PositionConstants.LEFT, PositionConstants.CENTER, true, true);
        rectangleFigure.add(label, data);
        return rectangleFigure;
    }

    @objid ("1bb294b2-5e33-11e2-b81d-002564c97630")
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectionHandlesEditPolicy() {
        
            @Override
            protected List<?> createSelectionHandles() {
                return Collections.emptyList();
            }
        });
    }

    @objid ("1bb294b5-5e33-11e2-b81d-002564c97630")
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        // update figure position and label
        GraphNode model = getModel();
        
        Figure fig = (Figure) getFigure();
        Color swtColor = Display.getDefault().getSystemColor(model.isCentral() ? (hasFocus
                ? SWT.COLOR_LIST_SELECTION : SWT.COLOR_WIDGET_BACKGROUND) : SWT.COLOR_LIST_BACKGROUND);
        fig.setBackgroundColor(swtColor);
        ((LineBorder) fig.getBorder()).setColor(ColorConstants.lightGray);
        
        Label label = (Label) fig.getChildren().get(0);
        Label tooltip = (Label) label.getToolTip();
        
        if (model.getData() instanceof NamespaceUse) {
            NamespaceUse use = (NamespaceUse) model.getData();
            String userName = use.getUser() != null ? use.getUser().getName() : "";
            String usedName = use.getUsed() != null ? use.getUsed().getName() : "";
            label.setText(LinkEditor.I18N.getMessage("Gui.Content.NameSpaceUse.label",
                                                     userName,
                                                     usedName));
            tooltip.setText(LinkEditor.I18N.getMessage("Gui.Content.NameSpaceUse.tooltip",
                                                       userName,
                                                       usedName));
            label.setIcon(ElementImageService.getIcon(model.getData()));
        } else if (model.getData() != null) {
            label.setText(model.getData().getName());
            label.setIcon(ElementImageService.getIcon(model.getData()));
            tooltip.setText(model.getData().getName());
        }
        
        if (model.getData() == null || model.getData().isShell()) {
            label.setForegroundColor(UIColor.SHELL_ELEMENT);
        } else {
            label.setForegroundColor(Display.getDefault().getSystemColor(model.isCentral() ? (hasFocus ? SWT.COLOR_LIST_SELECTION_TEXT : SWT.COLOR_LIST_FOREGROUND) : SWT.COLOR_LIST_FOREGROUND));
        }
    }

    @objid ("1bb294b8-5e33-11e2-b81d-002564c97630")
    @Override
    public List<?> getModelSourceConnections() {
        return getModel().outgoing;
    }

    @objid ("1bb294bf-5e33-11e2-b81d-002564c97630")
    @Override
    public List<?> getModelTargetConnections() {
        return getModel().incoming;
    }

    @objid ("1bb294c6-5e33-11e2-b81d-002564c97630")
    @Override
    public GraphNode getModel() {
        return (GraphNode) super.getModel();
    }

    @objid ("1bb294cb-5e33-11e2-b81d-002564c97630")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final ConnectionEditPart connection) {
        return new ToSideAnchor(getFigure());
    }

    @objid ("1bb294d6-5e33-11e2-b81d-002564c97630")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connection) {
        return new FromSideAnchor(getFigure());
    }

    @objid ("1bb294e1-5e33-11e2-b81d-002564c97630")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final Request request) {
        return new ToSideAnchor(getFigure());
    }

    @objid ("1bb294ec-5e33-11e2-b81d-002564c97630")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final Request request) {
        return new FromSideAnchor(getFigure());
    }

    @objid ("1bb294f7-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class adapter) {
        final Object model = getModel();
        
        if (adapter.isInstance(model)) {
            return model;
        } else if (model instanceof GraphNode) {
            if (adapter.isInstance(((GraphNode) model).data)) {
                return ((GraphNode) model).data;
            }
        }
        return super.getAdapter(adapter);
    }

}

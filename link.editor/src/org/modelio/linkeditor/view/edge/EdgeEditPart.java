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
                                    

package org.modelio.linkeditor.view.edge;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.linkeditor.view.LinkEditorView;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.ui.UIFont;

/**
 * Edit part for the Edge.
 * 
 * @author fpoyer
 */
@objid ("1b9d285e-5e33-11e2-b81d-002564c97630")
public class EdgeEditPart extends AbstractConnectionEditPart {
    /**
     * "<<"
     */
    @objid ("1b9d2862-5e33-11e2-b81d-002564c97630")
    private static final String CLOSING = " \u00BB";

    /**
     * ">>"
     */
    @objid ("1b9d2865-5e33-11e2-b81d-002564c97630")
    private static final String OPENING = "\u00AB ";

    @objid ("2694c9d2-6f1c-4605-8ead-591dcf64142a")
    private static final PointList AGGREG_TIP = new PointList(new int[] { -1, 1, 0, 0, -1, -1, -2, 0 });

    @objid ("a453cc05-31bf-4ad0-92d4-9ba5c2a2aed2")
    private static final PointList NAVIG_AGGREG_TIP = new PointList(new int[] { -1, 1, 0, 0, -1, -1, -2, 0,
            -3, -1, -2, 0, -3, 1, -2, 0 });

    @objid ("c95d026f-58e2-45ea-af1a-761b6e8ba561")
    private static final PointList TRIANGLE_TIP = new PointList(new int[] { -1, 1, 0, 0, -1, -1 });

    @objid ("1b9d2874-5e33-11e2-b81d-002564c97630")
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteEdgeEditPolicy());
    }

    @objid ("1b9d2877-5e33-11e2-b81d-002564c97630")
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        PolylineConnection fig = (PolylineConnection) getFigure();
        ManhattanConnectionRouter router = new ManhattanConnectionRouter();
        fig.setConnectionRouter(router);
        // Add model specific decorations
        Edge edge = (Edge) getModel();
        Object model = edge.data;
        if (model != null) {
            Color swtColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
            fig.setForegroundColor(swtColor);
            if (model instanceof Generalization || model instanceof Operation) {
                decorateGeneralization(fig);
            } else if (model instanceof InterfaceRealization) {
                decorateGeneralization(fig);
                fig.setLineStyle(SWT.LINE_DASH);
            } else if (model instanceof AssociationEnd) {
                decorateAssociation(fig, model);
            } else if (model instanceof Dependency) {
                decorateDependency(fig, model);
            } else if (model instanceof ElementImport) {
                decorateElementImport(fig, model);
            } else if (model instanceof NamespaceUse) {
                decorateNamespaceUse(fig, model);
            }
        } else {
            // link to bus 
            fig.setForegroundColor(ColorConstants.lightGray);
            fig.setLineWidth(2);
        }
    }

    @objid ("1b9d287a-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class adapter) {
        final Object model = getModel();
        
        if (adapter.isInstance(model)) {
            return model;
        } else if (model instanceof Edge) {
            if (adapter.isInstance(((Edge) model).data)) {
                return ((Edge) model).data;
            }
        }
        return super.getAdapter(adapter);
    }

    @objid ("1b9d2881-5e33-11e2-b81d-002564c97630")
    private void decorateElementImport(final PolylineConnection fig, final Object model) {
        fig.setLineStyle(SWT.LINE_DASH);
        fig.setTargetDecoration(new PolylineDecoration());
        Label role = new Label();
        if (((ElementImport) model).getVisibility() == VisibilityMode.PUBLIC) {
            role.setText(EdgeEditPart.OPENING + "import" + EdgeEditPart.CLOSING);
        } else {
            role.setText(EdgeEditPart.OPENING + "access" + EdgeEditPart.CLOSING);
        }
        role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
        ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.MIDDLE);
        constraint.setGap(2);
        constraint.setRelativePosition(PositionConstants.NORTH);
        fig.add(role, 0);
        fig.setConstraint(role, constraint);
    }

    @objid ("1b9d2889-5e33-11e2-b81d-002564c97630")
    private void decorateDependency(final PolylineConnection fig, final Object model) {
        Dependency dependency = (Dependency) model;
        fig.setLineStyle(SWT.LINE_DASH);
        fig.setTargetDecoration(new PolylineDecoration());
        List<Stereotype> stereotypes = dependency.getExtension();
        if (stereotypes != null && !stereotypes.isEmpty()) {
            Label role = new Label();
            if (stereotypes.size() == 1) {
                Stereotype stereotype = stereotypes.get(0);
                String stereoLabel = ModuleI18NService.getLabel(stereotype);
                role.setText(EdgeEditPart.OPENING + stereoLabel + EdgeEditPart.CLOSING);
            } else {
                Label tooltip = new Label();
                for (Stereotype stereotype : dependency.getExtension()) {
                    String stereoLabel = ModuleI18NService.getLabel(stereotype);
                    if (tooltip.getText() == null || tooltip.getText().isEmpty()) {
                        tooltip.setText(EdgeEditPart.OPENING + stereoLabel);
                    } else {
                        tooltip.setText(tooltip.getText() + ", " + stereoLabel);
                    }
                }
                tooltip.setText(tooltip.getText() + EdgeEditPart.CLOSING);
                role.setToolTip(tooltip);
                Stereotype stereotype = stereotypes.get(0);
                String stereoLabel = ModuleI18NService.getLabel(stereotype);
                role.setText(EdgeEditPart.OPENING + stereoLabel + ", ... " + EdgeEditPart.CLOSING);
            }
            role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(),
                                                        UIFont.SMALL_SIZE));
            ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.MIDDLE);
            constraint.setGap(2);
            constraint.setRelativePosition(PositionConstants.NORTH);
            fig.add(role, 0);
            fig.setConstraint(role, constraint);
        }
    }

    @objid ("1b9d2891-5e33-11e2-b81d-002564c97630")
    private void decorateAssociation(final PolylineConnection fig, final Object model) {
        AssociationEnd assocEnd = (AssociationEnd) model;
        decorateAssociationSource(fig, assocEnd);
        decorateAssociationTarget(fig, assocEnd);
    }

    @objid ("1b9d2899-5e33-11e2-b81d-002564c97630")
    private void decorateGeneralization(final PolylineConnection fig) {
        // Arrow toward target
        PolygonDecoration arrow = new PolygonDecoration();
        arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        arrow.setScale(8, 5);
        arrow.setOpaque(true);
        arrow.setFill(true);
        Color swtColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
        arrow.setForegroundColor(swtColor);
        arrow.setBackgroundColor(ColorConstants.white);
        fig.setTargetDecoration(arrow);
    }

    @objid ("1b9f89ae-5e33-11e2-b81d-002564c97630")
    @Override
    public boolean isSelectable() {
        return ((Edge) getModel()).data != null;
    }

    @objid ("1b9f89b3-5e33-11e2-b81d-002564c97630")
    private void decorateAssociationTarget(final PolylineConnection fig, final AssociationEnd assocEnd) {
        RotatableDecoration targetDecoration;
        if (assocEnd.isNavigable()) {
            AssociationEnd opposite = assocEnd.getOpposite();
            if (opposite.getAggregation() == AggregationKind.KINDISAGGREGATION ||
                opposite.getAggregation() == AggregationKind.KINDISCOMPOSITION) {
                targetDecoration = new PolygonDecoration();
                ((PolygonDecoration) targetDecoration).setTemplate(NAVIG_AGGREG_TIP);
                if (opposite.getAggregation() == AggregationKind.KINDISAGGREGATION) {
                    targetDecoration.setBackgroundColor(ColorConstants.white);
                } else {
                    targetDecoration.setBackgroundColor(ColorConstants.black);
                }
            } else {
                targetDecoration = new PolylineDecoration();
                ((PolylineDecoration) targetDecoration).setTemplate(TRIANGLE_TIP);
            }
            fig.setTargetDecoration(targetDecoration);
            Label role = new Label();
            role.setText(assocEnd.getName());
            role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(),
                                                        UIFont.SMALL_SIZE));
            ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.TARGET);
            constraint.setGap(5);
            if (LinkEditorView.getOptions().isLayoutOrientationVertical()) {
                constraint.setRelativePosition(PositionConstants.SOUTH_WEST);
            } else {
                constraint.setRelativePosition(PositionConstants.NORTH_WEST);    
            }
            fig.add(role, 0);
            fig.setConstraint(role, constraint);
            Label card = new Label();
            // correct text
            String cardText = "";
            boolean hasMin = assocEnd.getMultiplicityMin() != null &&
                             !assocEnd.getMultiplicityMin().isEmpty();
            boolean hasMax = assocEnd.getMultiplicityMax() != null &&
                             !assocEnd.getMultiplicityMax().isEmpty();
            if (hasMin && !hasMax) {
                cardText = "[" + assocEnd.getMultiplicityMin() + "]";
            } else if (!hasMin && hasMax) {
                cardText = "[" + assocEnd.getMultiplicityMax() + "]";
            } else if (hasMin && hasMax) {
                cardText = "[" + assocEnd.getMultiplicityMin() + ".." + assocEnd.getMultiplicityMax() + "]";
            }
            card.setText(cardText);
            card.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(),
                                                        UIFont.SMALL_SIZE));
            constraint = new ConnectionLocator(fig, ConnectionLocator.TARGET);
            constraint.setGap(5);
            if (LinkEditorView.getOptions().isLayoutOrientationVertical()) {
                constraint.setRelativePosition(PositionConstants.SOUTH_EAST);
            } else {
                constraint.setRelativePosition(PositionConstants.SOUTH_WEST);    
            }
            fig.add(card, 0);
            fig.setConstraint(card, constraint);
        } else if (assocEnd.getOpposite().getAggregation() == AggregationKind.KINDISAGGREGATION ||
                   assocEnd.getOpposite().getAggregation() == AggregationKind.KINDISCOMPOSITION) {
            targetDecoration = new PolygonDecoration();
            ((PolygonDecoration) targetDecoration).setTemplate(AGGREG_TIP);
            if (assocEnd.getOpposite().getAggregation() == AggregationKind.KINDISAGGREGATION) {
                targetDecoration.setBackgroundColor(ColorConstants.white);
            } else {
                targetDecoration.setBackgroundColor(ColorConstants.black);
            }
            fig.setTargetDecoration(targetDecoration);
        }
    }

    @objid ("1b9f89bd-5e33-11e2-b81d-002564c97630")
    private void decorateAssociationSource(final PolylineConnection fig, final AssociationEnd assocEnd) {
        RotatableDecoration sourceDecoration = new PolygonDecoration();
        AssociationEnd opposite = assocEnd.getOpposite();
        if (opposite.isNavigable()) {
            if (assocEnd.getAggregation() == AggregationKind.KINDISAGGREGATION ||
                assocEnd.getAggregation() == AggregationKind.KINDISCOMPOSITION) {
                sourceDecoration = new PolygonDecoration();
                ((PolygonDecoration) sourceDecoration).setTemplate(NAVIG_AGGREG_TIP);
                if (assocEnd.getAggregation() == AggregationKind.KINDISAGGREGATION) {
                    sourceDecoration.setBackgroundColor(ColorConstants.white);
                } else {
                    sourceDecoration.setBackgroundColor(ColorConstants.black);
                }
            } else {
                sourceDecoration = new PolylineDecoration();
                ((PolylineDecoration) sourceDecoration).setTemplate(TRIANGLE_TIP);
            }
            fig.setSourceDecoration(sourceDecoration);
            Label role = new Label();
            role.setText(opposite.getName());
            role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(),
                                                        UIFont.SMALL_SIZE));
            ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.SOURCE);
            constraint.setGap(5);
            if (LinkEditorView.getOptions().isLayoutOrientationVertical()) {
                constraint.setRelativePosition(PositionConstants.NORTH_WEST);
            } else {
                constraint.setRelativePosition(PositionConstants.NORTH_EAST);
            }
            fig.add(role, 0);
            fig.setConstraint(role, constraint);
            Label card = new Label();
            // correct text
            String cardText = "";
            boolean hasMin = opposite.getMultiplicityMin() != null &&
                             !opposite.getMultiplicityMin().isEmpty();
            boolean hasMax = opposite.getMultiplicityMax() != null &&
                             !opposite.getMultiplicityMax().isEmpty();
            if (hasMin && !hasMax) {
                cardText = "[" + opposite.getMultiplicityMin() + "]";
            } else if (!hasMin && hasMax) {
                cardText = "[" + opposite.getMultiplicityMax() + "]";
            } else if (hasMin && hasMax) {
                cardText = "[" + opposite.getMultiplicityMin() + ".." + opposite.getMultiplicityMax() + "]";
            }
            card.setText(cardText);
            card.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(),
                                                        UIFont.SMALL_SIZE));
            constraint = new ConnectionLocator(fig, ConnectionLocator.SOURCE);
            constraint.setGap(5);
            if (LinkEditorView.getOptions().isLayoutOrientationVertical()) {
                constraint.setRelativePosition(PositionConstants.NORTH_EAST);
            } else {
                constraint.setRelativePosition(PositionConstants.SOUTH_EAST);
            }
            fig.add(card, 0);
            fig.setConstraint(card, constraint);
        } else if (assocEnd.getAggregation() == AggregationKind.KINDISAGGREGATION ||
                   assocEnd.getAggregation() == AggregationKind.KINDISCOMPOSITION) {
            sourceDecoration = new PolygonDecoration();
            ((PolygonDecoration) sourceDecoration).setTemplate(AGGREG_TIP);
            if (assocEnd.getAggregation() == AggregationKind.KINDISAGGREGATION) {
                sourceDecoration.setBackgroundColor(ColorConstants.white);
            } else {
                sourceDecoration.setBackgroundColor(ColorConstants.black);
            }
            fig.setSourceDecoration(sourceDecoration);
        }
    }

    @objid ("1b9f89c7-5e33-11e2-b81d-002564c97630")
    private void decorateNamespaceUse(final PolylineConnection fig, final Object model) {
        fig.setLineStyle(SWT.LINE_DASHDOT);
        fig.setForegroundColor(ColorConstants.lightBlue);
        fig.setLineWidth(2);
        
        PolylineDecoration arrow = new PolylineDecoration();
        arrow.setTemplate(PolylineDecoration.TRIANGLE_TIP);
        arrow.setScale(7, 4);
        arrow.setLineWidth(2);
        arrow.setOpaque(false);
        fig.setTargetDecoration(arrow);
        
        Label role = new Label();
        role.setText(((NamespaceUse)model).getCause().size() + " cause(s)");
        role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
        ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.MIDDLE);
        constraint.setGap(2);
        constraint.setRelativePosition(PositionConstants.NORTH);
        fig.add(role, 0);
        fig.setConstraint(role, constraint);
    }

}

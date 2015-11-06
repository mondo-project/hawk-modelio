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
                                    

package org.modelio.diagram.editor.statik.elements.interfaze;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.editor.statik.elements.innerclass.GmInnerClass;
import org.modelio.diagram.editor.statik.elements.innerclass.GmInnerClassesZone;
import org.modelio.diagram.editor.statik.elements.namespacinglink.GmCompositionLink;
import org.modelio.diagram.editor.statik.elements.namespacinglink.redraw.RedrawCompositionLinkEditPolicy;
import org.modelio.diagram.editor.statik.elements.naryassoc.AcceptNAssocEditPolicy;
import org.modelio.diagram.editor.statik.elements.policies.SmartGeneralizationEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.common.resizablegroup.GmResizableGroup;
import org.modelio.diagram.elements.core.figures.ChildFigureLineSeparator;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;
import org.modelio.diagram.elements.core.figures.borders.ShadowBorder;
import org.modelio.diagram.elements.core.figures.borders.TLBRBorder;
import org.modelio.diagram.elements.core.figures.borders.ZoomableLineBorder;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultElementDropEditPolicy;
import org.modelio.diagram.elements.core.policies.DeferringCreateNodePolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;

/**
 * {@link GmInterfacePrimaryNode} edit part.
 * <p>
 * Creates a {@link GradientFigure} with a toolbar layout, and add separation lines between child figures by setting
 * line borders.
 */
@objid ("357bb748-55b7-11e2-877f-002564c97630")
public class InterfaceEditPart extends GmNodeEditPart {
    @objid ("357bb74c-55b7-11e2-877f-002564c97630")
    private static final int DEFAULT_HEIGHT = 50;

    @objid ("357bb74e-55b7-11e2-877f-002564c97630")
    private static final int DEFAULT_WIDTH = 100;

    @objid ("357bb750-55b7-11e2-877f-002564c97630")
    private static final int MARGIN = 20;

    @objid ("357bb752-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("357bb757-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(ModelElementDropRequest.TYPE, new DefaultElementDropEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new SmartGeneralizationEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        
        installEditPolicy("Constraint creation", new ConstraintLinkEditPolicy(false));
        installEditPolicy("N-ary assoc", new AcceptNAssocEditPolicy(true));
        
        // Add specific policy to handle requests to redraw composition links.
        installEditPolicy("RedrawCompositionLinkEditPolicy", new RedrawCompositionLinkEditPolicy());
    }

    @objid ("357bb75a-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        final GradientFigure classFigure = new GradientFigure();
        
        // Set style independent properties
        classFigure.setOpaque(true);
        classFigure.setPreferredSize(new Dimension(150, 100));
        
        final ToolbarLayoutWithGrab layout = new ToolbarLayoutWithGrab();
        layout.setHorizontal(false);
        layout.setStretchMinorAxis(true);
        
        classFigure.setLayoutManager(layout);
        
        // Set style dependent properties
        refreshFromStyle(classFigure, getModelStyle());
        return classFigure;
    }

    @objid ("357bb75f-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof GradientFigure) {
            GmCompositeNode model = (GmCompositeNode) getModel();
            if (model.getRepresentationMode() != RepresentationMode.STRUCTURED) {
                // reparent all content of the inner zone to port container as satellites and add composition link
                GmInnerClass inner = (GmInnerClass) ((GmResizableGroup) model.getFirstChild("")).getFirstChild("Inner");
                if (inner != null) {
                    for (GmNodeModel gmCompositeNode : inner.getChildren()) {
                        if (gmCompositeNode instanceof GmInnerClassesZone) {
                            for (GmNodeModel child : ((GmInnerClassesZone) gmCompositeNode).getChildren()) {
                                child.setRoleInComposition("body content as satellite");
                                Rectangle constraint = (Rectangle) child.getLayoutData();
                                // Avoid content appearing over the new figure.
                                if (constraint.x() < DEFAULT_WIDTH + MARGIN &&
                                    constraint.y() < DEFAULT_HEIGHT + MARGIN) {
                                    constraint.setY(DEFAULT_HEIGHT + MARGIN);
                                }
                                ((GmInnerClassesZone) gmCompositeNode).removeChild(child);
                                model.getParentNode().addChild(child);
                                GmCompositionLink link = new GmCompositionLink(model.getDiagram(),
                                                                               model.getRepresentedRef());
                                model.addStartingLink(link);
                                child.addEndingLink(link);
                            }
                        }
                    }
                }
            }
        
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
        
                updateSeparations(aFigure);
                updateFigureBorder(aFigure);
            } else {
                GmCompositeNode surroundingPortContainer = model.getParentNode();
                GmCompositeNode ancestor = surroundingPortContainer.getParentNode();
                int index = ancestor.getChildIndex(surroundingPortContainer);
                // This will "delete" the current edit part.
                ancestor.removeChild(surroundingPortContainer);
        
                // This will invoke the ModelioEditPartFactory that will
                // create another edit part.
                ancestor.addChild(surroundingPortContainer, index);
            }
        }
    }

    @objid ("357bb766-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        final IFigure fig = getFigure();
        final GmNodeModel gm = (GmNodeModel) getModel();
        
        fig.getParent().setConstraint(fig, gm.getLayoutData());
    }

    @objid ("357bb769-55b7-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
        
        updateSeparations(getFigure());
        updateFigureBorder(getFigure());
    }

    @objid ("357d3dbc-55b7-11e2-877f-002564c97630")
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
        
        updateSeparations(getFigure());
        updateFigureBorder(getFigure());
    }

    /**
     * Update the separation lines between zones.
     * @param aFigure the composite figure to update.
     */
    @objid ("357d3dc0-55b7-11e2-877f-002564c97630")
    protected void updateSeparations(IFigure aFigure) {
        final GradientFigure stateFig = (GradientFigure) aFigure;
        // Update the zone separation lines
        final TLBRBorder zoneBorder = new TLBRBorder(stateFig.getLineColor(),
                                                     stateFig.getLineWidth(),
                                                     false,
                                                     false,
                                                     true,
                                                     false);
        
        ChildFigureLineSeparator.updateSeparation(stateFig, zoneBorder);
    }

    @objid ("357d3dc4-55b7-11e2-877f-002564c97630")
    private void updateFigureBorder(IFigure aFigure) {
        final GradientFigure classFig = (GradientFigure) aFigure;
        final Border inner = new ZoomableLineBorder(classFig.getLineColor(), classFig.getLineWidth());
        final Border outer = new ShadowBorder(classFig.getLineColor(), classFig.getLineWidth());
        final CompoundBorder b = new CompoundBorder(outer, inner);
        
        classFig.setBorder(b);
    }

}

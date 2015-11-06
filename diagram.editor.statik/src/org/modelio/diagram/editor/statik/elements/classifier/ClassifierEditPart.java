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
                                    

package org.modelio.diagram.editor.statik.elements.classifier;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
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
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;
import org.modelio.diagram.elements.core.figures.borders.ShadowBorder;
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
import org.modelio.metamodel.uml.statik.Classifier;

/**
 * Edit part for any composite node representing a classifier.
 * <p>
 * Creates a {@link GradientFigure} with a toolbar layout, and add separation lines between child figures by setting
 * line borders.
 */
@objid ("34309979-55b7-11e2-877f-002564c97630")
public class ClassifierEditPart extends GmNodeEditPart {
    @objid ("3430997d-55b7-11e2-877f-002564c97630")
     static final int DEFAULT_HEIGHT = 50;

    @objid ("3430997f-55b7-11e2-877f-002564c97630")
     static final int DEFAULT_WIDTH = 100;

    @objid ("34309981-55b7-11e2-877f-002564c97630")
     static final int MARGIN = 20;

    @objid ("34309983-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("34309988-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new SmartGeneralizationEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy("Constraint creation", new ConstraintLinkEditPolicy(false));
        
        GmNodeModel model = (GmNodeModel) getModel();
        // FIXME : Some Gm that don't represent a Classifier use this edit part 
        // only to have the separation line between the zones.
        if (model.getRelatedElement() instanceof Classifier) {
            installEditPolicy("N-ary assoc", new AcceptNAssocEditPolicy(true));
            installEditPolicy(ModelElementDropRequest.TYPE, new ClassifierElementDropEditPolicy());
        } else {
            installEditPolicy(ModelElementDropRequest.TYPE, new DefaultElementDropEditPolicy());
        }
        
        // Add specific policy to handle requests to redraw composition links.
        installEditPolicy("RedrawCompositionLinkEditPolicy", new RedrawCompositionLinkEditPolicy());
    }

    @objid ("3430998b-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // Create the figure
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

    @objid ("34309990-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof GradientFigure) {
            GmCompositeNode model = (GmCompositeNode) getModel();
            if (model.getRepresentationMode() != RepresentationMode.STRUCTURED) {
                // reparent all content of the inner zone to port container as satellites and add composition link
                GmNodeModel firstChild = model.getFirstChild("");
                if (firstChild instanceof GmResizableGroup) {
                    GmInnerClass inner = (GmInnerClass) ((GmResizableGroup) firstChild).getFirstChild("Inner");
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
            }
        
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
        
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

    @objid ("34322019-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        final IFigure fig = getFigure();
        final GmNodeModel gm = (GmNodeModel) getModel();
        
        fig.getParent().setConstraint(fig, gm.getLayoutData());
    }

    @objid ("3432201c-55b7-11e2-877f-002564c97630")
    private void updateFigureBorder(IFigure aFigure) {
        final GradientFigure classFig = (GradientFigure) aFigure;
        final Border inner = new ZoomableLineBorder(classFig.getLineColor(), classFig.getLineWidth());
        final Border outer = new ShadowBorder(classFig.getLineColor(), classFig.getLineWidth());
        final CompoundBorder b = new CompoundBorder(outer, inner);
        
        classFig.setBorder(b);
    }

}

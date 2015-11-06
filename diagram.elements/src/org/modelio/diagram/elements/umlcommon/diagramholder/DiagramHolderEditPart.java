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
                                    

package org.modelio.diagram.elements.umlcommon.diagramholder;

import java.beans.PropertyChangeEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeEndReconnectEditPolicy;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDirectEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Edit part for {@link GmDiagramHolder}.
 */
@objid ("81308a66-1dec-11e2-8cad-001ec947c8cc")
public class DiagramHolderEditPart extends GmNodeEditPart {
    /**
     * @see GmNodeEditPart#propertyChange(java.beans.PropertyChangeEvent)
     */
    @objid ("81308a68-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LABEL)) {
            refreshVisuals();
        } else
            super.propertyChange(evt);
    }

    @objid ("81308a6d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DefaultElementDirectEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutDiagramPolicy());
        installEditPolicy(LinkedNodeEndReconnectEditPolicy.class, new LinkedNodeEndReconnectEditPolicy());
    }

    @objid ("81308a70-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // create the figure
        final DiagramHolderFigure fig = new DiagramHolderFigure();
        
        // set style independent properties
        fig.setPreferredSize(200, 150);
        fig.setOpaque(true);
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return fig;
    }

    /**
     * Get the note figure.
     * @return The note figure.
     */
    @objid ("81308a77-1dec-11e2-8cad-001ec947c8cc")
    protected final DiagramHolderFigure getNoteFigure() {
        return (DiagramHolderFigure) getFigure();
    }

    /**
     * Refresh this EditPart's visuals.
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @objid ("81308a7c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final DiagramHolderFigure noteFigure = (DiagramHolderFigure) getFigure();
        final GmDiagramHolder noteModel = (GmDiagramHolder) getModel();
        
        noteFigure.getParent().setConstraint(noteFigure, noteModel.getLayoutData());
    }

    @objid ("81308a80-1dec-11e2-8cad-001ec947c8cc")
    private void updateFigureBorder(final IFigure aFigure) {
        final DiagramHolderFigure fig = (DiagramHolderFigure) aFigure;
        // final Border inner = new ZoomableLineBorder(fig.getLineColor(), fig.getLineWidth());
        final Border outer = new MarginBorder(DiagramHolderFigure.FOLDSIZE / 2);
        // final CompoundBorder b = new CompoundBorder(outer, inner);
        
        fig.setBorder(outer);
    }

    @objid ("8132ecbf-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        super.refreshFromStyle(aFigure, style);
        
        updateFigureBorder(aFigure);
    }

    /**
     * Open the related diagram on double click.
     */
    @objid ("8132ecc8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(final Request req) {
        if (req.getType().equals(RequestConstants.REQ_OPEN)) {
            final GmDiagramHolder gm = (GmDiagramHolder) getModel();
            final ModelElement relatedDiagram = gm.getRelatedElement().getDependsOn();
        
            IActivationService service = gm.getDiagram().getModelManager().getActivationService();
            service.activateMObject(relatedDiagram);
        }
        super.performRequest(req);
    }

    /**
     * Policy that forwards all to the EditPolicy.PRIMARY_DRAG_ROLE edit policy of another edit part.
     * 
     * @author cmarin
     */
    @objid ("8132ecd0-1dec-11e2-8cad-001ec947c8cc")
    private static final class ForwardDragEditPolicy extends AbstractEditPolicy {
        @objid ("50b85c91-e22c-42c9-a7f2-e338d7e19abc")
        private final EditPart to;

        @objid ("80ce0715-36e1-4acf-b75a-93594072acb2")
        private EditPolicy toPolicy;

        /**
         * Initialize the policy.
         * @param to the edit part to forward requests to.
         */
        @objid ("8132ecdc-1dec-11e2-8cad-001ec947c8cc")
        public ForwardDragEditPolicy(final EditPart to) {
            if (to == null)
                throw new IllegalArgumentException("to is null.");
            
            this.to = to;
        }

        @objid ("8132ece3-1dec-11e2-8cad-001ec947c8cc")
        private EditPolicy getToPolicy() {
            if (this.toPolicy == null)
                this.toPolicy = this.to.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
            return this.toPolicy;
        }

        @objid ("8132ece9-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public EditPart getTargetEditPart(final Request request) {
            EditPolicy p = getToPolicy();
            if (p == null)
                return null;
            else
                return p.getTargetEditPart(request);
        }

        @objid ("8132ecf3-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void showSourceFeedback(final Request request) {
            final EditPolicy p = getToPolicy();
            if (p != null)
                p.showSourceFeedback(request);
        }

        @objid ("8132ecfa-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void showTargetFeedback(final Request request) {
            final EditPolicy p = getToPolicy();
            if (p != null)
                p.showTargetFeedback(request);
        }

        @objid ("8132ed01-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void eraseSourceFeedback(final Request request) {
            final EditPolicy p = getToPolicy();
            if (p != null)
                p.eraseSourceFeedback(request);
        }

        @objid ("8132ed08-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void eraseTargetFeedback(final Request request) {
            final EditPolicy p = getToPolicy();
            if (p != null)
                p.eraseTargetFeedback(request);
        }

        @objid ("8132ed0f-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Command getCommand(final Request request) {
            EditPolicy p = getToPolicy();
            if (p == null)
                return null;
            else
                return p.getCommand(request);
        }

        @objid ("81354f19-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public boolean understandsRequest(final Request req) {
            final EditPolicy p = getToPolicy();
            if (p == null)
                return false;
            else
                return p.understandsRequest(req);
        }

    }

    /**
     * Layout policy for the holder edit part.
     * <ul>
     * <li>Allow creation and drag & drop of diagrams.
     * <li>Trying to move the child part does move the host instead.
     * </ul>
     * 
     * @author cmarin
     */
    @objid ("81354f21-1dec-11e2-8cad-001ec947c8cc")
    private static final class LayoutDiagramPolicy extends LayoutEditPolicy {
        @objid ("81354f26-1dec-11e2-8cad-001ec947c8cc")
        public LayoutDiagramPolicy() {
            super();
        }

        /**
         * Trying to move the child part does move the parent one.
         */
        @objid ("81354f28-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected EditPolicy createChildEditPolicy(final EditPart child) {
            return new ForwardDragEditPolicy(getHost());
        }

        /**
         * Allow creation and drag & drop of diagrams.
         */
        @objid ("81354f34-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected Command getCreateCommand(final CreateRequest request) {
            final ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
            
            final Class<? extends MObject> metaclassToCreate = Metamodel.getJavaInterface(Metamodel.getMClass(ctx.getMetaclass()));
            final MObject hostElement = getHostElement();
            
            if (AbstractDiagram.class.isAssignableFrom(metaclassToCreate)) {
                return new SetHolderContentCommand(hostElement, (GmCompositeNode) this.getHost().getModel(), ctx);
            }
            return null;
        }

        @objid ("81354f40-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected Command getMoveChildrenCommand(final Request request) {
            return null;
        }

        /**
         * Returns the MObject represented by the host's model. Might be <code>null</code>!
         * @return the MObject represented by the host's model.
         */
        @objid ("81354f4b-1dec-11e2-8cad-001ec947c8cc")
        protected MObject getHostElement() {
            if (this.getHost().getModel() instanceof GmModel)
                return ((GmModel) this.getHost().getModel()).getRelatedElement();
            return null;
        }

    }

    /**
     * Command that change the related diagram to another one.
     */
    @objid ("81354f50-1dec-11e2-8cad-001ec947c8cc")
    private static class SetHolderContentCommand extends DefaultCreateElementCommand {
        /**
         * Initialize the command
         * @param relateDependency The &lt;&lt;related>> Dependency
         * @param gmDiagramHolder The diagram holder graphic model
         * @param context The context giving the diagram to relate to.
         */
        @objid ("81354f53-1dec-11e2-8cad-001ec947c8cc")
        public SetHolderContentCommand(final MObject relateDependency, final GmCompositeNode gmDiagramHolder, final ModelioCreationContext context) {
            super(relateDependency, gmDiagramHolder, context, null);
        }

        @objid ("81354f5c-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void execute() {
            final GmAbstractDiagram diagram = this.getParentNode().getDiagram();
            
            Dependency dep = (Dependency) getParentElement();
            AbstractDiagram newElement = (AbstractDiagram) this.getContext().getElementToUnmask();
            
            if (newElement == null) {
                ModelManager modelManager = diagram.getModelManager();
                // Create the Element...
                final IModelFactory modelFactory = modelManager.getModelFactory(getParentElement());
                newElement = (AbstractDiagram) modelFactory.createElement(this.getContext().getMetaclass());
            
                // Set the diagram context to the related element
                dep.getImpacted().getProduct().add(newElement);
            
                // Attach the stereotype if needed.
                if (this.getContext().getStereotype() != null) {
                    newElement.getExtension().add(this.getContext().getStereotype());
                }
                
                // Set default name
                newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
            }
            
            // Set the dependency target
            dep.setDependsOn(newElement);
            
            // The GmDiagramHolder will update itself on transaction commit.
        }

    }

}

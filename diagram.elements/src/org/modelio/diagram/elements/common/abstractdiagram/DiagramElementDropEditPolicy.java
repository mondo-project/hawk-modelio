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
                                    

package org.modelio.diagram.elements.common.abstractdiagram;

import java.util.ArrayDeque;
import java.util.Deque;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.policies.DefaultElementDropEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Adds the handling of dropped links: looks for both ends (unmask them if needed) then unmask the link itself.
 * 
 * @author fpoyer
 */
@objid ("7e0f746a-1dec-11e2-8cad-001ec947c8cc")
public class DiagramElementDropEditPolicy extends DefaultElementDropEditPolicy {
    @objid ("7e0f746c-1dec-11e2-8cad-001ec947c8cc")
    private DropCommandProvider dropCommandProvider;

    /**
     * Makes visibility public so that it can be accessed by visitor.
     */
    @objid ("7e0f746d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void createDropCommandForNode(CompoundCommand command, final Point p, MObject toUnmask) {
        final AbstractDiagramEditPart host = (AbstractDiagramEditPart) getHost();
        
        final GmAbstractDiagram gmDiagram = (GmAbstractDiagram) host.getModel();
        
        Point dropLocation = p;
        // Go through composition stack (upward, up to the ObProject if needed) and while finding elements that are unmaskable, 
        // look if they are already unmasked. If not, unmask them (in reverse order).
        MObject element = toUnmask;
        final Deque<MObject> elementHierarchy = new ArrayDeque<>();
        while (element != null && //< In case we went up to high!
               // the element should not be already unmasked
               getVisibleModelFor(element, gmDiagram) == null &&
               // the element must be unmaskable in this diagram
               gmDiagram.canUnmask(element) &&
               //the element must be unmaskable in this context
               shouldUnmask(element, elementHierarchy)) {
            elementHierarchy.push(element);
            element = getComposition(element);
        }
        
        final CompoundCommand hierarchyUnmaskCommand = new CompoundCommand();
        MObject parent;
        MObject child;
        if (element != null && getVisibleModelFor(element, gmDiagram) != null) {
            // an ancestor was found unmasked in the diagram: use it as "root" of the hierarchy to unmask.
            child = element;
        } else {
            // We didn't find any unmasked ancestor:
            // Make it so that the first element of the hierarchy is unmasked directly in the diagram.
            child = elementHierarchy.pop();
            super.createDropCommandForNode(hierarchyUnmaskCommand, dropLocation, child);
            dropLocation = dropLocation.getCopy();
            dropLocation.x = 0;
            dropLocation.y = 0;
            // It will then be used as "root" of the hierarchy to unmask 
        }
        while (!elementHierarchy.isEmpty()) {
            parent = child;
            child = elementHierarchy.pop();
            hierarchyUnmaskCommand.add(new DeferredUnmaskCommand(parent, child, dropLocation, host));
            dropLocation = dropLocation.getCopy();
            dropLocation.x = 0;
            dropLocation.y = 0;
        }
        command.add(hierarchyUnmaskCommand.unwrap());
    }

    @objid ("7e0f7479-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setHost(final EditPart host) {
        super.setHost(host);
        this.dropCommandProvider = new DropCommandProvider();
    }

    /**
     * For diagram the default feedback provided by the base class is ugly because it has a background Specialize and
     * remove the background. Code is tricky here otherwise we get flickering background: we make the background
     * transparent only when it is created
     */
    @objid ("7e11d6a2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showTargetFeedback(final Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            if (this.highlight == null) {
                super.showTargetFeedback(request);
                this.highlight.setOpaque(false);
                this.highlight.setBackgroundColor(null);
            } else {
                super.showTargetFeedback(request);
            }
        }
    }

    /**
     * Creates a drop command for an element that will be unmasked as a link.
     * @param command the compound in which to add the created command.
     * @param dropLocation the point where the drop happened.
     * @param link the link.
     */
    @objid ("7e11d6aa-1dec-11e2-8cad-001ec947c8cc")
    void createDropCommandForLink(CompoundCommand command, final Point dropLocation, final GmLink link) {
        command.add(new UnmaskLinkCommand(link, (AbstractDiagramEditPart) getHost(), dropLocation));
    }

    @objid ("7e11d6b6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getDropCommand(ModelElementDropRequest request) {
        final CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        final GmModel hostModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = hostModel.getDiagram();
        for (final MObject toUnmask : request.getDroppedElements()) {
            final GmModel previousUnmask = getVisibleModelFor(toUnmask, gmDiagram);
            if (previousUnmask == null) {
                command.add(this.dropCommandProvider.getDropCommandFor(toUnmask, dropLocation));
                // Introduce some offset, so that all elements are not totally on
                // top of each other.
                dropLocation = dropLocation.getTranslated(20, 20);
            } else {
                // Otherwise, just select it.
                createSelectionCommand(command, previousUnmask);
            }
        }
        return command.unwrap();
    }

    @objid ("7e11d6be-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected EditPart getDropTargetEditPart(ModelElementDropRequest request) {
        // If either of the dropped elements cannot be unmasked, return null.
        for (final MObject droppedElement : request.getDroppedElements()) {
            final boolean canUnmaskAsNode = ((GmModel) getHost().getModel()).canUnmask(droppedElement);
            final GmLink gmLink = ((GmAbstractDiagram) getHost().getModel()).unmaskLink(droppedElement);
            final boolean canUnmaskAsLink = (gmLink != null);
            if (gmLink != null) {
                gmLink.delete();
            }
            if (!canUnmaskAsNode && !canUnmaskAsLink) {
                return null;
            }
        }
        // All dropped elements understood: return host!
        return getHost();
    }

    /**
     * <p>
     * Returns whether the element should be unmasked as part of the given hierarchy.
     * </p>
     * <p>
     * Default implementation returns <code>true</code> unless the last element of the current hierarchy is a cms node,
     * a Collaboration or an Instance. Subclasses may override this methods to provide different behaviour.
     * </p>
     * @param element the element for which to decide if it should be unmasked
     * @param hierarchy the hierarchy of elements that will be unmasked. For Performance reasons it is passed by reference and
     * MUST NOT be modified.
     * @return <code>true</code> if this element is to be unmasked, <code>false</code> otherwise.
     */
    @objid ("7e11d6c6-1dec-11e2-8cad-001ec947c8cc")
    protected boolean shouldUnmask(MObject element, Deque<MObject> hierarchy) {
        final MObject lastInHierarchy = hierarchy.peek();
        boolean isCurrentCollaboration = false;
        boolean isCurrentInstance = false;
        boolean isCurrentCmsNode = false;
        boolean isCurrentEnumeration = false;
        boolean isInformationItem = false;
        boolean isRequirement = false;
        boolean isTerm = false;
        boolean isActivity = false;
        boolean isInteraction = false;
        boolean isBpmnProcess = false;
        boolean isBpmnBehavior = false;
        boolean isCommunicationInteraction = false;
        boolean isStateMachine = false;
        boolean isNary = false;
        if (lastInHierarchy != null) {
            isCurrentCmsNode = lastInHierarchy.getMClass().isCmsNode();
            isCurrentInstance = lastInHierarchy instanceof Instance && !(lastInHierarchy instanceof BindableInstance);
            isCurrentCollaboration = lastInHierarchy instanceof Collaboration;
            isCurrentEnumeration = lastInHierarchy instanceof Enumeration;
            isInformationItem = lastInHierarchy instanceof InformationItem;
            isRequirement = lastInHierarchy instanceof AnalystElement;
            isTerm = lastInHierarchy instanceof Term;
            isActivity = lastInHierarchy instanceof Activity;
            isInteraction = lastInHierarchy instanceof Interaction;
            isBpmnProcess = lastInHierarchy instanceof BpmnProcess;
            isBpmnBehavior = lastInHierarchy instanceof BpmnBehavior;
            isCommunicationInteraction = lastInHierarchy instanceof CommunicationInteraction;
            isStateMachine = lastInHierarchy instanceof StateMachine;
            isNary = lastInHierarchy instanceof NaryAssociation || lastInHierarchy instanceof NaryLink;
        }
        return (!isStateMachine &&
                !isCommunicationInteraction &&
                !isActivity &&
                !isInteraction &&
                !isBpmnProcess &&
                !isBpmnBehavior &&
                !isCurrentCmsNode &&
                !isCurrentInstance &&
                !isCurrentCollaboration &&
                !isCurrentEnumeration &&
                !isInformationItem &&
                !isRequirement &&
                !isTerm &&
                !isNary);
    }

    @objid ("7e11d6cf-1dec-11e2-8cad-001ec947c8cc")
    protected static MObject getComposition(final MObject element) {
        return element.getCompositionOwner();
    }

    /**
     * This class will provide the drop command for an element.
     * 
     * @author fpoyer
     */
    @objid ("7e11d6d5-1dec-11e2-8cad-001ec947c8cc")
    private class DropCommandProvider extends DefaultModelVisitor {
        @objid ("4d04d2f7-874f-42eb-a6c7-ef2219cc357a")
        private CompoundCommand command;

        @objid ("5fccbdeb-463a-4f05-b319-64002d3ab03a")
        private Point p;

        /**
         * c'tor.
         */
        @objid ("7e11d6dd-1dec-11e2-8cad-001ec947c8cc")
        public DropCommandProvider() {
        }

        /**
         * Creates and return the drop command for the given element.
         * @param toUnmask the element to unmask.
         * @param location the location point retrieved from the request.
         * @return the drop command.
         */
        @objid ("7e11d6e0-1dec-11e2-8cad-001ec947c8cc")
        public Command getDropCommandFor(final MObject toUnmask, final Point location) {
            this.command = new CompoundCommand();
            this.p = location;
            toUnmask.accept(this);
            return this.command.unwrap();
        }

        @objid ("7e11d6ed-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitCollaborationUse(final CollaborationUse theCollaborationUse) {
            // Do not try to unmask collaboration use as a link, it doesn't work (should it?)
            createDropCommandForNode(this.command, this.p, theCollaborationUse);
            return null;
        }

        @objid ("7e1438f9-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitConstraint(final Constraint theConstraint) {
            this.command.add(new UnmaskConstraintCommand(theConstraint,
                                                         (AbstractDiagramEditPart) getHost(),
                                                         new Rectangle(this.p, new Dimension(-1, -1))));
            return null;
        }

        @objid ("7e143900-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitDependency(final Dependency theDependency) {
            if (theDependency.isStereotyped("ModelerModule", "related_diagram")) {
                this.command.add(new UnmaskLinkedNodeCommand(theDependency,
                                                             (AbstractDiagramEditPart) getHost(),
                                                             new Rectangle(this.p, new Dimension(-1, -1)),
                                                             this.p));
                return null;
            } else {
                return super.visitDependency(theDependency);
            }
        }

        @objid ("7e143906-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitElement(final Element theElement) {
            final GmLink gmLink = ((GmAbstractDiagram) getHost().getModel()).unmaskLink(theElement);
            if (gmLink != null) {
                createDropCommandForLink(this.command, this.p, gmLink);
            } else {
                createDropCommandForNode(this.command, this.p, theElement);
            }
            return null;
        }

        @objid ("7e14390d-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitNote(final Note theNote) {
            this.command.add(new UnmaskNoteCommand(theNote,
                                                   (AbstractDiagramEditPart) getHost(),
                                                   new Rectangle(this.p, new Dimension(-1, -1)),
                                                   this.p));
            return null;
        }

        @objid ("7e143914-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitProvidedInterface(final ProvidedInterface theProvidedInterface) {
            AbstractDiagramEditPart diagramPart = (AbstractDiagramEditPart) getHost();
            GmModel model = (GmModel) diagramPart.getModel();
            this.command.add(new UnmaskLinkToVoidCommand(theProvidedInterface,
                                                         theProvidedInterface.getProviding(),
                                                         model.getRelatedElement(),
                                                         diagramPart,
                                                         this.p));
            return null;
        }

        @objid ("7e14391b-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitRequiredInterface(final RequiredInterface theRequiredInterface) {
            AbstractDiagramEditPart diagramPart = (AbstractDiagramEditPart) getHost();
            GmModel model = (GmModel) diagramPart.getModel();
            this.command.add(new UnmaskLinkToVoidCommand(theRequiredInterface,
                                                         theRequiredInterface.getRequiring(),
                                                         model.getRelatedElement(),
                                                         diagramPart,
                                                         this.p));
            return null;
        }

        @objid ("7e143922-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitExternDocument(final ExternDocument theExternDocument) {
            this.command.add(new UnmaskExternDocumentCommand(theExternDocument,
                                                             (AbstractDiagramEditPart) getHost(),
                                                             new Rectangle(this.p, new Dimension(-1, -1)),
                                                             this.p));
            return null;
        }

    }

}

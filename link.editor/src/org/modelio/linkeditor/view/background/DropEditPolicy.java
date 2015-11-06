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
                                    

package org.modelio.linkeditor.view.background;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.model.api.MTools;
import org.modelio.linkeditor.options.LinkEditorOptions;
import org.modelio.linkeditor.view.LinkEditorView;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.ui.UIFont;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Drop Edit Policy used on the background of the LinkEditor to handle creation
 * by D&D.
 * 
 * @author fpoyer
 */
@objid ("1b91417c-5e33-11e2-b81d-002564c97630")
public class DropEditPolicy extends XYLayoutEditPolicy {
    /**
     * "<<"
     */
    @objid ("1b914180-5e33-11e2-b81d-002564c97630")
    private static final String CLOSING = " \u00BB";

    /**
     * ">>"
     */
    @objid ("1b914183-5e33-11e2-b81d-002564c97630")
    private static final String OPENING = "\u00AB ";

    @objid ("bf01e1a2-c3e7-4f5e-bb35-4f243d835e65")
    private final XYAnchor dummyXYAnchor = new XYAnchor(new Point(10, 10));

    @objid ("ae440281-f7c1-463c-b37f-81d65ed748cb")
    private PolylineConnection connectionFeedback;

    @objid ("5915f8ea-6fee-4e0a-84e1-434f2d433316")
    private static final PointList TRIANGLE_TIP = new PointList(new int[] { -1, 1, 0, 0, -1, -1 });

    @objid ("d4d53937-ea87-4a18-be62-015e845d41f3")
    private final ChopboxAnchor dummyChopBoxAnchor = new ChopboxAnchor(null);

    @objid ("1b914193-5e33-11e2-b81d-002564c97630")
    @Override
    protected Command getCreateCommand(final CreateRequest request) {
        // Extracting the dropped elements
        MObject[] droppedElements = (MObject[]) request.getExtendedData().get(LinkEditorDropTargetListener.DROPPED_ELEMENTS);
        if (droppedElements != null && droppedElements.length > 0 && LinkEditorView.getOptions().isPinned()) {
            // determine whether it is "to" or "from"
            boolean isFrom = this.getSide(request);
            // extracting the type of link to create...
            List<Object> newLinkTypes = DropEditPolicy.getNewLinkType(this.getCenterNode().getData(), droppedElements, isFrom);
            if (newLinkTypes.size() == 0) {
                // No valid types, nothing possible
                return UnexecutableCommand.INSTANCE;
            }
            CompoundCommand command = new CompoundCommand();
            for (MObject element : droppedElements) {
                if (isFrom) {
                    command.add(new CreateLinkCommand(element, this.getCenterNode().getData(), newLinkTypes));
                } else {
                    command.add(new CreateLinkCommand(this.getCenterNode().getData(), element, newLinkTypes));
                }
                // Subsequent commands will use choice made by first command
                // (transmitted through a private static variable).
                newLinkTypes = Collections.emptyList();
            }
            return command.unwrap();
        }
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * Returns the type of new link to be created. If current options give an
     * ambiguous answer, an popup is opened to ask user to resolve it.
     * @return the type of new link to be created.
     */
    @objid ("1b91419e-5e33-11e2-b81d-002564c97630")
    private static List<Object> getNewLinkType(final MObject centerElement, final MObject[] droppedElements, final boolean isFrom) {
        if (droppedElements.length == 0) {
            return Collections.emptyList();
        }
        LinkEditorOptions options = LinkEditorView.getOptions();
        Class<? extends MObject> dropType = null;
        for (MObject droppedElement : droppedElements) {
            if (dropType == null) {
                dropType = droppedElement.getClass();
            } else if (!dropType.equals(droppedElement.getClass())) {
                // Mixed drop types, cancel.
                return Collections.emptyList();
            }
        }
        List<Object> types = new ArrayList<>();
        if (options.isAssociationShown()) {
        
            if ((isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(AssociationEnd.class), droppedElements[0].getMClass(),
                    centerElement.getMClass()))
                    || (!isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(AssociationEnd.class), centerElement.getMClass(),
                            droppedElements[0].getMClass()))) {
                types.add(AssociationEnd.class);
            }
        }
        if (options.isImportShown()) {
            if ((isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(ElementImport.class), droppedElements[0].getMClass(),
                    centerElement.getMClass()))
                    || (!isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(ElementImport.class), centerElement.getMClass(),
                            droppedElements[0].getMClass()))) {
                types.add(ElementImport.class);
            }
        }
        if (options.isInheritanceShown()) {
            if ((isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(Generalization.class), droppedElements[0].getMClass(),
                    centerElement.getMClass()))
                    || (!isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(Generalization.class), centerElement.getMClass(),
                            droppedElements[0].getMClass()))) {
        
                types.add(Generalization.class);
            }
            if ((isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(InterfaceRealization.class), droppedElements[0].getMClass(),
                    centerElement.getMClass()))
                    || (!isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(InterfaceRealization.class), centerElement.getMClass(),
                            droppedElements[0].getMClass()))) {
                types.add(InterfaceRealization.class);
            }
        }
        if (options.isDependencyShown()) {
            // Take stereotype filter into account.
            if (options.isDependencyFiltered() && options.getDependencyFilter() != null) {
                for (Stereotype stereo : options.getDependencyFilter()) {
                    if ((isFrom && MTools.getLinkTool().canLink(stereo, Metamodel.getMClass(Dependency.class), droppedElements[0], centerElement))
                            || (!isFrom && MTools.getLinkTool().canLink(stereo, Metamodel.getMClass(Dependency.class), centerElement, droppedElements[0]))) {
                        types.add(stereo);
                    }
                }
            } else if ((isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(Dependency.class), droppedElements[0].getMClass(),
                    centerElement.getMClass()))
                    || (!isFrom && MTools.getLinkTool().canLink(Metamodel.getMClass(Dependency.class), centerElement.getMClass(),
                            droppedElements[0].getMClass()))) {
                types.add(Dependency.class);
            }
        }
        if (options.isTraceShown()) {
                final Stereotype type = getStereotype(CoreSession.getSession(centerElement).getModel(), "ModelerModule", "trace", Metamodel.getMClass(Dependency.class));
                if (type != null) {
                types.add(type);
                }
        }
        return types;
    }

    @objid ("1b9141b1-5e33-11e2-b81d-002564c97630")
    @Override
    protected EditPolicy createChildEditPolicy(final EditPart child) {
        return null;
    }

    @objid ("1b93a2da-5e33-11e2-b81d-002564c97630")
    @Override
    protected void showLayoutTargetFeedback(final Request request) {
        // Only CreateRequest are handled
        if (!(request instanceof CreateRequest)) {
            return;
        }
        CreateRequest createRequest = (CreateRequest) request;
        GraphNode centerNode = this.getCenterNode();
        GraphicalEditPart centerNodeEditPart = (GraphicalEditPart) this.getHost().getViewer().getEditPartRegistry().get(centerNode);
        IFigure centerNodeFigure = centerNodeEditPart.getFigure();
        this.dummyChopBoxAnchor.setOwner(centerNodeFigure);
        // update the XY anchor position.
        this.dummyXYAnchor.setLocation(createRequest.getLocation());
        
        // determine wether it is "to" or "from"
        boolean isFrom = this.getSide(createRequest);
        
        if (this.connectionFeedback == null) {
            // add the "link" feedback.
            this.connectionFeedback = new PolylineConnection();
            this.connectionFeedback.setVisible(true);
            this.connectionFeedback.setOpaque(true);
            // this.connectionFeedback.setConnectionRouter(((ConnectionLayer)
            // getFeedbackLayer()).getConnectionRouter());
            // extracting the type of link to create...
            // Extracting the dropped elements
            MObject[] droppedElements = (MObject[]) request.getExtendedData().get(LinkEditorDropTargetListener.DROPPED_ELEMENTS);
            List<Object> newLinkTypes = DropEditPolicy.getNewLinkType(this.getCenterNode().getData(), droppedElements, isFrom);
            if (newLinkTypes.size() == 1) {
                Object newLinkType = newLinkTypes.get(0);
                if (newLinkType == AssociationEnd.class) {
                    this.decorateAssociation(this.connectionFeedback);
                } else if (newLinkType == ElementImport.class) {
                    this.decorateElementImport(this.connectionFeedback);
                } else if (newLinkType == Generalization.class || newLinkType == InterfaceRealization.class) {
                    this.decorateGeneralization(this.connectionFeedback);
                } else if (newLinkType == Dependency.class) {
                    this.decorateDependency(this.connectionFeedback, null);
                } else if (newLinkType instanceof Stereotype) {
                    this.decorateDependency(this.connectionFeedback, (Stereotype) newLinkType);
                }
            } else {
                // TODO: decorate undefined type (leading to type selection
                // popup on drop).
            }
            this.getFeedbackLayer().add(this.connectionFeedback);
        }
        if (isFrom) {
            this.connectionFeedback.setSourceAnchor(this.dummyXYAnchor);
            this.connectionFeedback.setTargetAnchor(this.dummyChopBoxAnchor);
        } else {
            this.connectionFeedback.setSourceAnchor(this.dummyChopBoxAnchor);
            this.connectionFeedback.setTargetAnchor(this.dummyXYAnchor);
        }
        this.getFeedbackLayer().remove(this.connectionFeedback);
        this.getFeedbackLayer().add(this.connectionFeedback);
        
        super.showLayoutTargetFeedback(request);
    }

    @objid ("1b93a2e1-5e33-11e2-b81d-002564c97630")
    @Override
    public void deactivate() {
        super.deactivate();
        if (this.connectionFeedback != null) {
            this.getFeedbackLayer().remove(this.connectionFeedback);
            this.connectionFeedback = null;
        }
    }

    @objid ("1b93a2e4-5e33-11e2-b81d-002564c97630")
    private boolean getSide(final CreateRequest request) {
        GraphNode centerNode = this.getCenterNode();
        if (LinkEditorView.getOptions().isLayoutOrientationVertical()) {
            if (request.getLocation().y < (centerNode.y + (centerNode.height / 2))) {
                return false;
            } else {
                return true;
            }
        } else {
            if (request.getLocation().x < (centerNode.x + (centerNode.width / 2))) {
                return true;
            } else {
                return false;
            }
        }
    }

    @objid ("1b93a2eb-5e33-11e2-b81d-002564c97630")
    @Override
    protected void eraseLayoutTargetFeedback(final Request request) {
        if (this.connectionFeedback != null) {
            this.getFeedbackLayer().remove(this.connectionFeedback);
            this.connectionFeedback = null;
        }
        super.eraseLayoutTargetFeedback(request);
    }

    @objid ("1b93a2f2-5e33-11e2-b81d-002564c97630")
    private void decorateElementImport(final PolylineConnection fig) {
        fig.setLineStyle(SWT.LINE_DASH);
        fig.setTargetDecoration(new PolylineDecoration());
        Label role = new Label();
        // default creation is PRIVATE
        role.setText(DropEditPolicy.OPENING + "access" + DropEditPolicy.CLOSING);
        
        role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
        ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.MIDDLE);
        constraint.setGap(2);
        constraint.setRelativePosition(PositionConstants.NORTH);
        fig.add(role, 0);
        fig.setConstraint(role, constraint);
    }

    @objid ("1b93a2f8-5e33-11e2-b81d-002564c97630")
    private void decorateDependency(final PolylineConnection fig, final Stereotype stereo) {
        fig.setLineStyle(SWT.LINE_DASH);
        fig.setTargetDecoration(new PolylineDecoration());
        if (stereo != null) {
            String stereoLabel = ModuleI18NService.getLabel(stereo);
            if (stereoLabel != null && !stereoLabel.isEmpty()) {
                Label role = new Label();
                role.setText(DropEditPolicy.OPENING + stereoLabel + DropEditPolicy.CLOSING);
                role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
                ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.MIDDLE);
                constraint.setGap(2);
                constraint.setRelativePosition(PositionConstants.NORTH);
                fig.add(role, 0);
                fig.setConstraint(role, constraint);
            }
        }
    }

    @objid ("1b93a302-5e33-11e2-b81d-002564c97630")
    private void decorateAssociation(final PolylineConnection fig) {
        RotatableDecoration targetDecoration;
        targetDecoration = new PolylineDecoration();
        ((PolylineDecoration) targetDecoration).setTemplate(DropEditPolicy.TRIANGLE_TIP);
        fig.setTargetDecoration(targetDecoration);
        Label role = new Label();
        // TODO: correct text
        role.setText("associationEnd");
        role.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
        ConnectionLocator constraint = new ConnectionLocator(fig, ConnectionLocator.TARGET);
        constraint.setGap(5);
        constraint.setRelativePosition(PositionConstants.NORTH_WEST);
        fig.add(role, 0);
        fig.setConstraint(role, constraint);
        Label card = new Label();
        // correct text
        card.setText("[0..1]");
        card.setFont(CoreFontRegistry.getScaledFont(Display.getDefault().getSystemFont(), UIFont.SMALL_SIZE));
        constraint = new ConnectionLocator(fig, ConnectionLocator.TARGET);
        constraint.setGap(5);
        constraint.setRelativePosition(PositionConstants.SOUTH_WEST);
        fig.add(card, 0);
        fig.setConstraint(card, constraint);
    }

    @objid ("1b93a308-5e33-11e2-b81d-002564c97630")
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

    /**
     * @return
     */
    @objid ("1b93a30e-5e33-11e2-b81d-002564c97630")
    private GraphNode getCenterNode() {
        GraphNode centerNode = ((BackgroundModel) this.getHost().getModel()).getCenter();
        return centerNode;
    }

    @objid ("a31a487a-e70a-4fa8-ae7c-afb23717ea59")
    public static Stereotype getStereotype(IModel iModel, String moduleName, String stereotypeName, MClass metaclass) {
        Pattern p = Pattern.compile((moduleName == null || moduleName.isEmpty()) ? ".*" : moduleName);
        
        for (Stereotype type : iModel.findByAtt(Stereotype.class, "Name", stereotypeName)) {
            ModuleComponent module = type.getModule();
            if (module != null) {
                MClass steClass = Metamodel.getMClass(type.getBaseClassName());
                if (metaclass.hasBase(steClass) && p.matcher(module.getName()).matches()) {
                    return type;
                }
            }
        }
        return null;
    }

}

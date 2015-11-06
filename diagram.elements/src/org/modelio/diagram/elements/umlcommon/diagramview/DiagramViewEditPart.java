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
                                    

package org.modelio.diagram.elements.umlcommon.diagramview;

import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.ExclusionSearch;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeFinishCreationEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.borders.ZoomableLineBorder;
import org.modelio.diagram.elements.core.link.ConnectionRouterRegistry;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * EditPart for {@link GmAbstractDiagramView}.
 * 
 * @author cmarin
 */
@objid ("81439d36-1dec-11e2-8cad-001ec947c8cc")
public class DiagramViewEditPart extends GmNodeEditPart {
    @objid ("937f83ab-1e83-11e2-8cad-001ec947c8cc")
    private static final String PROP_DGVIEW = "DiagramViewEditPart.DiagramChain";

    /**
     * Viewer of the viewed diagram.
     */
    @objid ("06772ca4-72d8-4901-be6b-cde8c2482b9a")
    private EditPartViewer viewer;

    /**
     * Add the thumbnail only once the getFigure() is attached to the parent.
     */
    @objid ("81439d3e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void addNotify() {
        super.addNotify();
        
        Thumbnail thumbnail = new Thumbnail();
        
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        
        createThumbNail(thumbnail);
        getContentPane().add(thumbnail, data, -1);
    }

    @objid ("81439d42-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void removeNotify() {
        if (this.viewer != null) {
            final RootEditPart rootEditPart = this.viewer.getRootEditPart();
            rootEditPart.deactivate();
            rootEditPart.removeNotify();
            this.viewer = null;
        }
        super.removeNotify();
    }

    @objid ("81439d45-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void addChildVisual(final EditPart childEditPart, final int index) {
        final GridData data = new GridData();
        data.grabExcessHorizontalSpace = false;
        data.grabExcessVerticalSpace = false;
        data.horizontalAlignment = SWT.LEFT;
        data.verticalAlignment = SWT.BEGINNING;
        // data.horizontalIndent = -1;
        
        getContentPane().add(((GraphicalEditPart) childEditPart).getFigure(), data, 0);
        
        updateFigureBorder(getFigure());
    }

    @objid ("81439d4e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Policy to create notes
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_END, new LinkedNodeFinishCreationEditPolicy());
        
        // Remove the default DIRECT_EDIT policy: we don't want the diagram
        // background to delegate direct edit requests.
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
    }

    @objid ("81439d51-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // Create the figure
        final GradientFigure fig = new GradientFigure();
        
        // Set style independent properties
        fig.setOpaque(true);
        fig.setPreferredSize(new Dimension(150, 100));
        
        // classFigure.setLayoutManager(new BorderLayout());
        
        final GridLayout manager = new GridLayout(1, false);
        manager.horizontalSpacing = 0;
        manager.verticalSpacing = 0;
        manager.marginHeight = 0;
        manager.marginWidth = 0;
        fig.setLayoutManager(manager);
        
        // Set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        return fig;
    }

    @objid ("81439d58-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        if (aFigure instanceof GradientFigure) {
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
        
                updateFigureBorder(aFigure);
            }
        }
    }

    @objid ("81439d61-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        final IFigure fig = getFigure();
        final GmNodeModel gm = (GmNodeModel) getModel();
        
        final Object layoutData = gm.getLayoutData();
        if (layoutData != null)
            fig.getParent().setConstraint(fig, layoutData);
    }

    @objid ("81439d64-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void removeChildVisual(final EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
        
        updateFigureBorder(getFigure());
    }

    @objid ("81439d6b-1dec-11e2-8cad-001ec947c8cc")
    private IFigure createThumbNail(final Thumbnail thumbnail) {
        final GmAbstractDiagramView gmView = (GmAbstractDiagramView) getModel();
        
        if (isCycle()) {
            Image image = MetamodelImageService.getIcon(gmView.getRelatedElement().getMClass());
            ImageFigure fig = new ImageFigure(image);
            thumbnail.add(fig);
            thumbnail.setSource(fig);
        } else {
            final GmAbstractDiagram gmViewedDiagram = gmView.getViewedDiagram();
            this.viewer = new InnerEditPartViewer(getViewer());
            this.viewer.setProperty(PROP_DGVIEW, new DiagramViewChain(gmView.getDiagram().getRelatedElement(), getViewer()));
        
            final FreeformGraphicalRootEditPart rootEditPart = new FreeformGraphicalRootEditPart();
            final EditPart diagramEditpart = createChild(gmViewedDiagram);
        
            this.viewer.setRootEditPart(rootEditPart);
        
            // Setup a root figure with its own update manager.
            new RootFig(thumbnail).add(rootEditPart.getFigure());
        
            this.viewer.setContents(diagramEditpart);
        
            // Activate the ready root edit part
            rootEditPart.activate();
            rootEditPart.refresh();
            rootEditPart.getFigure().validate();
            rootEditPart.getFigure().setOpaque(false);
        
            final IFigure srcFigure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
            srcFigure.setBackgroundColor(getFillColor(gmView));
            thumbnail.setSource(srcFigure);
        }
        
        thumbnail.setSize(150, 100);
        return thumbnail;
    }

    /**
     * Get the fill color of the given model.
     * @param gmModel a graphic model
     * @return the fill color.
     */
    @objid ("81439d75-1dec-11e2-8cad-001ec947c8cc")
    private Color getFillColor(final GmNodeModel gmModel) {
        return gmModel.getStyle().getProperty(gmModel.getStyleKeyStrict(MetaKey.FILLCOLOR));
    }

    @objid ("8145ff94-1dec-11e2-8cad-001ec947c8cc")
    private void updateFigureBorder(final IFigure aFigure) {
        final GradientFigure classFig = (GradientFigure) aFigure;
        final Border inner = new ZoomableLineBorder(classFig.getLineColor(), 0);
        
        classFig.setBorder(inner);
    }

    /**
     * Open the related diagram on double click.
     */
    @objid ("8145ff9a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(final Request req) {
        if (req.getType().equals(RequestConstants.REQ_OPEN)) {
            GmAbstractDiagramView gm = (GmAbstractDiagramView) getModel();
            // activate/open editor.
            gm.getDiagram().getModelManager().getActivationService().activateMObject(gm.getRelatedElement());
        }
        super.performRequest(req);
    }

    @objid ("8145ffa2-1dec-11e2-8cad-001ec947c8cc")
    private boolean isCycle() {
        final GmAbstractDiagramView gmView = (GmAbstractDiagramView) getModel();
        
        final MObject viewedDiagram = gmView.getRelatedElement();
        
        MObject currentDiagram = gmView.getDiagram().getRelatedElement();
        DiagramViewChain chain = (DiagramViewChain) getViewer().getProperty(PROP_DGVIEW);
        
        while (chain != null && !viewedDiagram.equals(currentDiagram)) {
            currentDiagram = chain.diagram;
            chain = (DiagramViewChain) chain.parentEditPartViewer.getProperty(PROP_DGVIEW);
        }
        return viewedDiagram.equals(currentDiagram);
    }

    /**
     * Edit part viewer that creates no SWT Control.
     * 
     * @author cmarin
     */
    @objid ("8145ffa6-1dec-11e2-8cad-001ec947c8cc")
    private static final class InnerEditPartViewer extends AbstractEditPartViewer {
        @objid ("8145ffab-1dec-11e2-8cad-001ec947c8cc")
        public InnerEditPartViewer(final EditPartViewer viewer) {
            // Take the same edit domain and edit part factory.
            setEditDomain(viewer.getEditDomain());
            setEditPartFactory(viewer.getEditPartFactory());
            
            // Register the connection router registry.
            setProperty(ConnectionRouterRegistry.ID, viewer.getProperty(ConnectionRouterRegistry.ID));
        }

        @objid ("8145ffb1-1dec-11e2-8cad-001ec947c8cc")
        @SuppressWarnings("rawtypes")
        @Override
        public EditPart findObjectAtExcluding(final Point pt, final Collection exclusionSet, final org.eclipse.gef.EditPartViewer.Conditional condition) {
            class ConditionalTreeSearch extends ExclusionSearch {
                ConditionalTreeSearch(Collection coll) {
                    super(coll);
                }
            
                @Override
                public boolean accept(IFigure aFigure) {
                    EditPart editpart = null;
                    IFigure figure = aFigure;
                    while (editpart == null && figure != null) {
                        editpart = (EditPart) getVisualPartMap().get(figure);
                        figure = figure.getParent();
                    }
                    return editpart != null && (condition == null || condition.evaluate(editpart));
                }
            }
            
            IFigure figure = ((GraphicalEditPart) getRootEditPart()).getFigure().findFigureAt(pt.x, pt.y,
                    new ConditionalTreeSearch(exclusionSet));
            
            EditPart part = null;
            while (part == null && figure != null) {
                part = (EditPart) getVisualPartMap().get(figure);
                figure = figure.getParent();
            }
            if (part == null)
                return getContents();
            return part;
        }

        /**
         * This viewer creates no control, it is embedded inside a Figure.
         */
        @objid ("8145ffc2-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Control createControl(final Composite parent) {
            return null;
        }

    }

    /**
     * Root figure for the viewed diagram.
     */
    @objid ("8145ffca-1dec-11e2-8cad-001ec947c8cc")
    private static class RootFig extends Figure {
        @objid ("f0df32ae-c2e5-42d6-9b23-d670cccccd9c")
        private IFigure thumbnail;

        @objid ("f40c92c7-2e7f-4830-9039-3ae51ac65dac")
        private UpdateManager manager = new DeferredUpdateManager();

        /**
         * Constructor
         * @param thumbnail The thumbnail figure displaying the viewed diagram.
         */
        @objid ("814861ee-1dec-11e2-8cad-001ec947c8cc")
        public RootFig(final IFigure thumbnail) {
            this.thumbnail = thumbnail;
        }

        @objid ("814861f5-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected boolean isValidationRoot() {
            return true;
        }

        @objid ("814861fa-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public UpdateManager getUpdateManager() {
            return this.manager;
        }

        @objid ("81486201-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Color getForegroundColor() {
            return this.thumbnail.getForegroundColor();
        }

        @objid ("81486206-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Color getBackgroundColor() {
            return this.thumbnail.getBackgroundColor();
        }

        @objid ("8148620b-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Font getFont() {
            Font f = this.thumbnail.getFont();
            if (f != null) {
                return f;
            } else {
                return Display.getDefault().getSystemFont();
            }
        }

    }

    @objid ("8148620f-1dec-11e2-8cad-001ec947c8cc")
    private static final class DiagramViewChain {
        @objid ("62644982-722c-4ee9-af43-1f4fef1c6f3f")
         EditPartViewer parentEditPartViewer;

        @objid ("81486211-1dec-11e2-8cad-001ec947c8cc")
         MObject diagram;

        @objid ("81486215-1dec-11e2-8cad-001ec947c8cc")
        public DiagramViewChain(final MObject diagram, final EditPartViewer parentEditPartViewer) {
            this.diagram = diagram;
            this.parentEditPartViewer = parentEditPartViewer;
        }

    }

}

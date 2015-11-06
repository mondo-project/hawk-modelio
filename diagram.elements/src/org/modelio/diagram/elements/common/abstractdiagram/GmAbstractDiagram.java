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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.diagram.elements.core.model.IGmLinkObject;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.drawings.core.IGmDrawing;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;
import org.modelio.diagram.elements.drawings.layer.GmDrawingLayer;
import org.modelio.diagram.elements.plugin.DiagramElements;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.persistence.PersistenceException;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.Style;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vbasic.collections.MultiHashMap;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeSupport;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a diagram and its content.
 * <p>
 * The diagram owns a map of all represented elements mapped by their MRef.<br>
 * The diagram is a model change listener and make impacted GmModel refreshing from the Ob model changes.
 */
@objid ("7e169b53-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmAbstractDiagram extends GmCompositeNode {
    @objid ("7e169b68-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("c21d5b02-6ce1-4f68-b4e4-853f6e86816b")
    private static final String PROP_BG_DRAWING_LAYER = "BgDrawingLayer";

    @objid ("9d3eb649-bff4-4544-a308-b8edc1b5f779")
    private static final String PROP_DRAWING_LAYERS = "DrawingLayers";

    @objid ("7e169b62-1dec-11e2-8cad-001ec947c8cc")
    protected long lastSavedUiDataVersion = -1;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7e169b65-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("7e169b6a-1dec-11e2-8cad-001ec947c8cc")
    private transient boolean visible = false;

    @objid ("fc0a1ce2-102f-4343-90aa-b66c00d92ddf")
    private IGmDrawingLayer backgroundDrawingLayer;

    @objid ("bcbddc8f-2844-491a-a770-6c319533b101")
    private final List<IGmDrawingLayer> drawingLayers = new CopyOnWriteArrayList<>();

    @objid ("898a69f8-6243-4aec-b266-6e68ca4ee393")
    private Map<String, IGmDrawing> drawingMap = new HashMap<>();

    @objid ("7e169b5d-1dec-11e2-8cad-001ec947c8cc")
    private IGmLinkFactory gmLinkCreationFactory;

    @objid ("7e169b5c-1dec-11e2-8cad-001ec947c8cc")
    private IGmNodeFactory gmNodeFactory;

    @objid ("7e169b63-1dec-11e2-8cad-001ec947c8cc")
    private IDiagramRefresher hiddenModelChangeHandler;

    @objid ("7e169b5e-1dec-11e2-8cad-001ec947c8cc")
    private final List<IGmLinkObject> links = new ArrayList<>();

    @objid ("7e169b5b-1dec-11e2-8cad-001ec947c8cc")
    protected IDiagramRefresher modelChangeHandler;

    @objid ("7e169b55-1dec-11e2-8cad-001ec947c8cc")
    private ModelManager modelManager;

    /**
     * Visibility is 'protected' in order to be accessible to GmSequenceDiagram.
     */
    @objid ("24914a6e-1f69-11e2-a30a-001ec947c8cc")
    protected MultiHashMap<MRef,GmModel> models;

    /**
     * Empty constructor for deserialization.
     */
    @objid ("7e169b6c-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractDiagram() {
        // Empty constructor for (de-)serialisation.
        initModelChangeListeners();
    }

    /**
     * Initialize a GmAbstractDiagram.
     * @param modelManager The model manager
     * @param diagramRef a reference to the diagram.
     */
    @objid ("7e169b6f-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractDiagram(ModelManager modelManager, MRef diagramRef) {
        super((GmAbstractDiagram) null, diagramRef);
        this.modelManager = modelManager;
        this.models = new MultiHashMap<>();
        this.models.putValue(getRepresentedRef(), this);
        initModelChangeListeners();
        
        if (this.drawingLayers.isEmpty()) {
            // Add a default layer
            final GmDrawingLayer childNode = new GmDrawingLayer(this, diagramRef, GmDrawingLayer.LAYER_ID_TOP);
            this.drawingLayers.add(childNode);
        }
        
        this.backgroundDrawingLayer = new GmDrawingLayer(this, diagramRef, GmDrawingLayer.LAYER_ID_BACKGROUND);
    }

    /**
     * Add a drawing to the diagram.
     * @param child a drawing.
     */
    @objid ("98cd15c5-bb69-4f5c-87c2-b3f42b25e77e")
    public void addDrawingLayer(GmDrawingLayer child) {
        this.drawingLayers.add(child);
        
        //child.setParent(this);
        
        // set child style
        // child.getStyle().setCascadedStyle(this.getStyle());
        
        firePropertyChange(IGmObject.PROPERTY_CHILDREN, null, child);
    }

    /**
     * Register a graphic element in the diagram.
     * <p>
     * This method should only be called by the GmModel constructor or its read() method.
     * @param model the graphic element to add.
     */
    @objid ("7e169b77-1dec-11e2-8cad-001ec947c8cc")
    public final void addGraphicModel(IGmObject model) {
        if (model != this && model instanceof GmModel) {
            final GmModel gmModel = (GmModel) model;
            this.models.putValue(gmModel.getRepresentedRef(), gmModel);
        }
        
        if (model instanceof IGmLinkObject) {
            this.links.add((IGmLinkObject) model);
        }
        
        if (model instanceof IGmDrawing) {
            IGmDrawing dg = (IGmDrawing) model;
            IGmDrawing old = this.drawingMap.put(dg.getIdentifier(), dg);
            if (old != null) {
                this.drawingMap.put(dg.getIdentifier(), old);
                throw new IllegalArgumentException("Diagram already contains a "+old.getClass().getSimpleName()+" with '"+dg.getIdentifier()+"' identifier");
            }
        }
    }

    /**
     * Tells whether this composite node support child nodes of the given java class.
     * <p>
     * {@link GmElementLabel GmElementLabel} cannot be contained directly by the diagram's background.
     * @return <i>false</i> if nodeClass is GmElementLabel or a subclass, <i>true</i> otherwise.
     */
    @objid ("7e169b7b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canContain(Class<? extends GmNodeModel> nodeClass) {
        return !GmElementLabel.class.isAssignableFrom(nodeClass);
    }

    /**
     * Empties the model.
     */
    @objid ("7e18fdb1-1dec-11e2-8cad-001ec947c8cc")
    public final void clear() {
        for (final GmNodeModel child : this.getChildren()) {
            child.delete();
        }
        for (final IGmLink l : new ArrayList<>(getStartingLinks())) {
            l.delete();
        }
        for (final IGmLink l : new ArrayList<>(getEndingLinks())) {
            l.delete();
        }
        
        for (final IGmDrawingLayer child : this.getDrawingLayers()) {
            child.delete();
        }
        
        // Add a default foreground layer
        this.drawingLayers.add(new GmDrawingLayer(this, getRepresentedRef(), GmDrawingLayer.LAYER_ID_TOP));
        
        if (this.backgroundDrawingLayer != null) {
            this.backgroundDrawingLayer.delete();
            // Add a default background layer
            this.backgroundDrawingLayer = new GmDrawingLayer(this, getRepresentedRef(), GmDrawingLayer.LAYER_ID_BACKGROUND);
        }
        
        final GmCompositeNode gmParent = getParentNode();
        if (gmParent != null) {
            gmParent.removeChild(this);
        }
        
        final GmLink gmLink = getParentLink();
        if (gmLink != null) {
            gmLink.removeExtension(this);
        }
    }

    /**
     * Removes the model listener.
     */
    @objid ("7e18fdb4-1dec-11e2-8cad-001ec947c8cc")
    public void dispose() {
        if (this.modelChangeHandler != null) {
        
            final IModelChangeSupport modelChangeSupport = this.modelManager.getModelingSession().getModelChangeSupport();
            modelChangeSupport.removePersistentViewListener(this.modelChangeHandler);
            modelChangeSupport.removeModelChangeListener(this.modelChangeHandler);
            modelChangeSupport.removePersistentViewListener(this.hiddenModelChangeHandler);
            modelChangeSupport.removeModelChangeListener(this.hiddenModelChangeHandler);
            this.modelChangeHandler = null;
            this.modelManager = null;
            // Do not forget to delete the diagram itself
            this.delete();
        }
    }

    /**
     * Returns all GmModel that are somehow related to the given reference in this diagram, or an empty list is none is found.
     * @param representedElementRef a reference to a model element for which we are searching Gm.
     * @return the list of all Gm related to the passed reference, or an empty list if none is found.
     */
    @objid ("7e18fdb7-1dec-11e2-8cad-001ec947c8cc")
    public final List<GmModel> getAllGMRelatedTo(MRef representedElementRef) {
        return this.models.getList(representedElementRef);
    }

    /**
     * Returns the list of graphic models (Gm) representing (ie: for which the getRepresentedElement() method does return the
     * element of) the given reference in this diagram or an empty list if none is found.
     * @param representedElementRef a reference to a model element for which we are searching Gm.
     * @return the list of all Gm representing the passed reference, or an empty list if none is found.
     */
    @objid ("7e18fdbf-1dec-11e2-8cad-001ec947c8cc")
    public final List<GmModel> getAllGMRepresenting(MRef representedElementRef) {
        final List<GmModel> modelsRepresenting = new ArrayList<>();
        for (final GmModel model : this.models.getList(representedElementRef)) {
            // Filter the list to keep only the "representing" Gms.
            if (model.getRepresentedElement() != null) {
                modelsRepresenting.add(model);
            }
        }
        return modelsRepresenting;
    }

    /**
     * Get all represented graphic models.
     * @return all graphic models.
     */
    @objid ("7e18fdc7-1dec-11e2-8cad-001ec947c8cc")
    public final Collection<GmModel> getAllModels() {
        final ArrayList<GmModel> ret = new ArrayList<>(this.models.size() * 5);
        
        for (final List<GmModel> l : this.models.values()) {
            ret.addAll(l);
        }
        return ret;
    }

    /**
     * Get the background drawing layer.
     * @return the background drawing layer.
     */
    @objid ("d6e87752-e465-4828-86ae-418b86338533")
    public IGmDrawingLayer getBackgroundDrawingLayer() {
        return this.backgroundDrawingLayer;
    }

    @objid ("7e18fdce-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final GmAbstractDiagram getDiagram() {
        return this;
    }

    /**
     * Get the drawing identified by the given string.
     * @param identifier the drawing identifier.
     * @return the found drawing or <i>null</i>.
     */
    @objid ("d69caf90-c1d2-4ee5-805b-b8bbdf1c16f1")
    public IGmDrawing getDrawing(String identifier) {
        return this.drawingMap.get(identifier);
    }

    /**
     * @return the diagram drawings
     */
    @objid ("f630fc18-b847-4543-beb0-8014587c61ce")
    public List<IGmDrawingLayer> getDrawingLayers() {
        return this.drawingLayers;
    }

    @objid ("7e18fdd3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Get the model manager storing the session and model factory.
     * @return the model manager.
     */
    @objid ("7e18fdde-1dec-11e2-8cad-001ec947c8cc")
    public final ModelManager getModelManager() {
        return this.modelManager;
    }

    /**
     * Tells whether the diagram model is disposed.
     * <p>
     * A disposed diagram model won't react to model modifications and shouldn't be used anymore.
     * @return <code>true</code> if the diagram model is disposed, else <code>false</code>.
     */
    @objid ("7e18fde3-1dec-11e2-8cad-001ec947c8cc")
    public boolean isDisposed() {
        return this.modelManager == null;
    }

    @objid ("7e1b6048-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final boolean isEditable() {
        MObject relatedElement = getRelatedElement();
        return relatedElement != null && !(relatedElement.isShell() || relatedElement.isDeleted())
                && relatedElement.getStatus().isModifiable();
    }

    @objid ("7e18fde8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @objid ("7e18fded-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmAbstractDiagram." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 0
            read_0(in);
            break;
        }
        }
    }

    /**
     * Force refresh of the whole diagram.
     */
    @objid ("7e18fdf1-1dec-11e2-8cad-001ec947c8cc")
    public void refreshAllFromObModel() {
        final ArrayList<GmModel> toUpdate = new ArrayList<>(GmAbstractDiagram.this.models.size() * 5);
        
        for (final List<GmModel> list : GmAbstractDiagram.this.models.values()) {
            toUpdate.addAll(list);
        }
        
        for (final GmModel m : toUpdate) {
            if (m != this)
                m.obElementsUpdated();
        }
    }

    /**
     * Remove a graphic model from the diagram.
     * <p>
     * To be called only by {@link GmModel#delete()}.
     * @param model the graphic element to remove.
     */
    @objid ("7e18fdf4-1dec-11e2-8cad-001ec947c8cc")
    public void removeGraphicModel(IGmObject model) {
        if (model instanceof GmModel) {
            GmModel gmmodel = (GmModel) model;
            this.models.remove(gmmodel.getRepresentedRef(), gmmodel);
        }
        
        if (model instanceof IGmLinkObject) {
            this.links.remove(model);
        }
        
        if (model instanceof IGmDrawing)
            this.drawingMap.remove(((IGmDrawing)model).getIdentifier());
    }

    /**
     * Remove a drawing layer
     * @param gmDrawingLayer a drawing layer
     */
    @objid ("44a65a3a-8d44-48ab-96ec-10da54535eb2")
    public void removeLayer(GmDrawingLayer gmDrawingLayer) {
        if (this.backgroundDrawingLayer == gmDrawingLayer) {
            this.backgroundDrawingLayer = null;
            firePropertyChange(IGmObject.PROPERTY_CHILDREN, gmDrawingLayer,  null);
        } else if (this.drawingLayers.remove(gmDrawingLayer)) {
            firePropertyChange(IGmObject.PROPERTY_CHILDREN, gmDrawingLayer,  null);
        } else {
            throw new IllegalArgumentException(gmDrawingLayer+" not owned by the diagram.");
        }
    }

    /**
     * Set the graphic link model creation factory.
     * @param gmLinkCreationFactory the link factory
     */
    @objid ("7e18fdf8-1dec-11e2-8cad-001ec947c8cc")
    public void setGmLinkCreationFactory(IGmLinkFactory gmLinkCreationFactory) {
        this.gmLinkCreationFactory = gmLinkCreationFactory;
    }

    /**
     * set the graphic node model creation factory.
     * @param gmNodeFactory the node creation factory.
     */
    @objid ("7e1b6009-1dec-11e2-8cad-001ec947c8cc")
    public void setGmNodeFactory(IGmNodeFactory gmNodeFactory) {
        this.gmNodeFactory = gmNodeFactory;
    }

    /**
     * Unmask the given model element as a node inside the given graphic node.
     * @param parentNode The parent graphic node that will contain the element
     * @param newElement The element to unmask
     * @param initialLayoutData The initial layout data of the unmasked element.<br>
     * The specified initial layout data are taken as a hint, they may be taken into account or not.
     * @return The unmasked node, or <code>null</code> if the newElement can't be unmasked.
     */
    @objid ("7e1b600d-1dec-11e2-8cad-001ec947c8cc")
    public final GmNodeModel unmask(GmCompositeNode parentNode, MObject newElement, Object initialLayoutData) {
        List<GmModel> allGMRepresenting = getAllGMRepresenting(new MRef(newElement));
        for (GmModel gmModel : allGMRepresenting) {
            if (gmModel instanceof GmNodeModel) {
                GmNodeModel gmNodeModel = (GmNodeModel) gmModel;
                if (parentNode.equals(gmNodeModel.getParentNode())) {
                    return gmNodeModel;
                }
            }
        }
        
        // No GmNode under this parent, create a new one...
        try {
            return getGmNodeFactory().create(this, parentNode, newElement, initialLayoutData);
        } catch (UnsupportedOperationException e) {
            // Failed to unmask, log error.
            DiagramElements.LOG.warning(e.getMessage());
        
            // No valid GM found, return null
            return null;
        }
    }

    /**
     * Unmask the given link element in the diagram.
     * <p>
     * @param createdLinkElement The link to unmask
     * @param fromNode The source node
     * @param toNode The destination node
     * @param initialLayoutData The initial layout data of the unmasked element.<br>
     * @return the link graphic element or <i>null</i> if one of the 2 extremities cannot be unmasked..
     */
    @objid ("7e1b6014-1dec-11e2-8cad-001ec947c8cc")
    public final GmLink unmaskLink(MObject createdLinkElement, final IGmLinkable fromNode, final IGmLinkable toNode, Object initialLayoutData) {
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("Source and destination nodes must not be null.");
        }
        
        List<GmModel> allGMRepresenting = getAllGMRepresenting(new MRef(createdLinkElement));
        for (GmModel gmModel : allGMRepresenting) {
            if (gmModel instanceof GmLink) {
                GmLink gmLink = (GmLink) gmModel;
                if (fromNode.equals(gmLink.getFrom()) && toNode.equals(gmLink.getTo())) {
                    return gmLink;
                }
            }
        }
        
        // No GmLink between those nodes, create a new one...
        final GmLink newLink = unmaskLink(createdLinkElement);
        
        fromNode.addStartingLink(newLink);
        toNode.addEndingLink(newLink);
        newLink.setLayoutData(initialLayoutData);
        return newLink;
    }

    /**
     * Creates a GmLink for the given element. Link is not initialized (ie: it have no source node, no destination node and no
     * layout data).
     * @param linkElement the element for which to create a GmLink
     * @return the uninitialized GmLink for the element, or <code>null</code>.
     */
    @objid ("7e1b601f-1dec-11e2-8cad-001ec947c8cc")
    public final GmLink unmaskLink(MObject linkElement) {
        return getGmLinkFactory().create(this, linkElement);
    }

    /**
     * Update The diagram graphic model last save date from the represented diagram model element.
     */
    @objid ("7e1b6025-1dec-11e2-8cad-001ec947c8cc")
    public final void updateLastSaveDate() {
        this.lastSavedUiDataVersion = ((AbstractDiagram)this.getRelatedElement()).getUiDataVersion();
    }

    @objid ("7e1b6028-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        out.writeProperty("Links", this.links);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAbstractDiagram." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
        
        // Write background drawings layer
        out.writeProperty(PROP_BG_DRAWING_LAYER, this.backgroundDrawingLayer);
        
        // Write foreground drawings layers
        out.writeProperty(PROP_DRAWING_LAYERS, this.drawingLayers);
    }

    /**
     * Get the factory used to unmask node model elements.
     * @return The graphic node factory
     */
    @objid ("7e1b602c-1dec-11e2-8cad-001ec947c8cc")
    IGmNodeFactory getGmNodeFactory() {
        return this.gmNodeFactory;
    }

    /**
     * Creates the diagram style from the style manager default style.
     */
    @objid ("7e1b6031-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IStyle createStyle(final GmAbstractDiagram aDiagram) {
        final IStyle defaultStyle = DiagramStyles.getStyleManager().getDefaultStyle();
        return new Style(defaultStyle);
    }

    @objid ("7e1b6039-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void doSetVisible(final boolean newVisible) {
        // Avoid useless and harmfull clear if no change
        if (this.visible == newVisible)
            return;
        
        this.visible = newVisible;
        
        super.doSetVisible(newVisible);
        final IModelChangeSupport changeSupport = this.modelManager.getModelingSession().getModelChangeSupport();
        if (newVisible) {
            changeSupport.removePersistentViewListener(this.hiddenModelChangeHandler);
            changeSupport.removeModelChangeListener(this.hiddenModelChangeHandler);
            changeSupport.addPersistentViewListener(this.modelChangeHandler);
            changeSupport.addModelChangeListener(this.modelChangeHandler);
        
            MObject relatedElement = getRelatedElement();
            if (relatedElement != null) {
                DiagramPersistence.loadDiagram(this);
            }
        } else {
            changeSupport.removePersistentViewListener(this.modelChangeHandler);
            changeSupport.removeModelChangeListener(this.modelChangeHandler);
            changeSupport.addPersistentViewListener(this.hiddenModelChangeHandler);
            changeSupport.addModelChangeListener(this.hiddenModelChangeHandler);
        
        }
    }

    @objid ("7e169b74-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected final void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    @objid ("7e1b6043-1dec-11e2-8cad-001ec947c8cc")
    protected void initModelChangeListeners() {
        this.modelChangeHandler = new ModelChangeListener();
        this.hiddenModelChangeHandler = new HiddenModelChangeListener();
    }

    /**
     * Get the factory used to unmask relationship model elements such as Associations, ElementImports or Dependencies.
     * @return The graphic link factory.
     */
    @objid ("7e1b603e-1dec-11e2-8cad-001ec947c8cc")
    private final IGmLinkFactory getGmLinkFactory() {
        return this.gmLinkCreationFactory;
    }

    @objid ("7e1b6045-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private void read_0(IDiagramReader in) {
        // Clear existing/default layers
        for (final IGmDrawingLayer child : new ArrayList<>(getDrawingLayers()))
            child.delete();
        if (this.backgroundDrawingLayer!=null)
            this.backgroundDrawingLayer.delete();
            
        
        // Call inherited loading
        super.read(in);
        
        // re-normalize the identifier (because copied diagrams are deserialized with a wrong id)
        // "this" diagram provides the right one.
        final MRef newId = new MRef(getRelatedElement());
        getRepresentedRef().mc = newId.mc;
        getRepresentedRef().uuid = newId.uuid;
        
        // Resume deserialization : 
        //  Read all links. No need to get the result of readListProperty() : links add themselves to this.links
        in.readListProperty("Links"); 
        
        // Read foreground drawing layers
        this.drawingLayers.addAll((Collection<? extends IGmDrawingLayer>) in.readListProperty(PROP_DRAWING_LAYERS));
        
        if (this.drawingLayers.isEmpty()) {
            // Add a default layer
            final GmDrawingLayer childNode = new GmDrawingLayer(this, newId, GmDrawingLayer.LAYER_ID_TOP);
            this.drawingLayers.add(childNode);
        }
            
        // Read background layer
        this.backgroundDrawingLayer = (GmDrawingLayer) in.readProperty(PROP_BG_DRAWING_LAYER);
        if (this.backgroundDrawingLayer == null) {
            this.backgroundDrawingLayer = new GmDrawingLayer(this, newId, GmDrawingLayer.LAYER_ID_BACKGROUND);
        }
        
        firePropertyChange(IGmObject.PROPERTY_CHILDREN, null, this.drawingLayers);
    }

    /**
     * Updates the Graphic Model from the Ob model.
     */
    @objid ("7e1dc274-1dec-11e2-8cad-001ec947c8cc")
    private class HiddenModelChangeListener implements IDiagramRefresher {
        /**
         * Default constructor.
         */
        @objid ("7e1dc276-1dec-11e2-8cad-001ec947c8cc")
        public HiddenModelChangeListener() {
        }

        @objid ("7e1dc279-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void updateView(final IModelChangeEvent event) {
            final MObject obDiagram = getRelatedElement();
            // Refresh the diagram in the display thread
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (!obDiagram.isShell() && !obDiagram.isDeleted() && !isDisposed() && obDiagram.getStatus().isModifiable()) {
                        final Collection<GmModel> toRefresh = getAllModels();
            
                        for (final GmModel model : toRefresh) {
                            final MObject el = model.getRelatedElement();
                            if (el != null && el.isDeleted()) {
                                model.obElementDeleted();
                            }
                        }
            
                        // Save the refreshed diagram
                        DiagramPersistence.saveDiagram(GmAbstractDiagram.this);
                    }
                }
            });
        }

        @objid ("7e1dc27e-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public final void modelChanged(IModelChangeEvent event) {
            // Do nothing.
        }

    }

    /**
     * Updates the Graphic Model from the Ob model.
     */
    @objid ("7e1b604d-1dec-11e2-8cad-001ec947c8cc")
    private class ModelChangeListener implements IDiagramRefresher {
        /**
         * Default constructor.
         */
        @objid ("7e1dc262-1dec-11e2-8cad-001ec947c8cc")
        public ModelChangeListener() {
        }

        /**
         * Invoked when the model has changed.
         * <p>
         * Delegates to {@link #refreshAllDiagram()}.
         */
        @objid ("7e1dc265-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void updateView(final IModelChangeEvent event) {
            final AbstractDiagram obDiagram = (AbstractDiagram)getRelatedElement();
            // Refresh the diagram in the display thread
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (obDiagram.isShell() || obDiagram.isDeleted() || !obDiagram.getStatus().isModifiable()) {
                        // The diagram has been deleted, do nothing.
                        // Another listener will close the view.
                        return;
                    } else if (obDiagram.getUiDataVersion() != GmAbstractDiagram.this.lastSavedUiDataVersion) {
                        // Get all refs from invalid elements
                        final Set<MRef> refs = new HashSet<>();
                        for (final GmModel model : getAllModels()) {
                            final MObject el = model.getRelatedElement();
                            if (el != null && el.isDeleted()) {
                                refs.add(model.getRepresentedRef());
                            }
                        }
            
                        if (!refs.isEmpty()) {
                            // Load the diagram. All Gm with invalid elements are now ghosts...
                            try {
                                DiagramPersistence.loadDiagram(GmAbstractDiagram.this);
                            } catch (PersistenceException pe) {
                                // Failed to read string, log error.
                                DiagramElements.LOG.error(pe);
                                // FIXME correct handling of failed reading.
                                assert (false) : "Problem persistence: failed to load diagram!";
                            }
            
                            // Delete all Gm which refs were invalid, to avoid unwanted ghosts.
                            int deletedNodes = 0;
                            for (final GmModel model : getAllModels()) {
                                if (refs.contains(model.getRepresentedRef())) {
                                    model.obElementDeleted();
                                    deletedNodes++;
                                }
                            }
            
                            // Save the refreshed diagram if necessary
                            if (deletedNodes > 0) {
                                DiagramPersistence.saveDiagram(GmAbstractDiagram.this);
                            }
                        }
                    } else {
                        refreshAllDiagram();
                    }
                }
            });
        }

        /**
         * Reload the diagram if it has been modified outside of the diagram editor.
         * @param event The change event.
         */
        @objid ("7e1dc26b-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public final void modelChanged(final IModelChangeEvent event) {
            final AbstractDiagram obDiagram = (AbstractDiagram)getRelatedElement();
            
            if (obDiagram.isShell() || obDiagram.isDeleted() ) {
                // The diagram has been deleted, do nothing.
                // Another listener will close the view.
                return;
            } else if (obDiagram.getUiDataVersion() != GmAbstractDiagram.this.lastSavedUiDataVersion) {
                // Schedule a diagram reload
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDisposed()) {
                            try {
                                DiagramPersistence.loadDiagram(GmAbstractDiagram.this);
                            } catch (PersistenceException pe) {
                                // Failed to read string, log error.
                                DiagramElements.LOG.error(pe);
                                // FIXME correct handling of failed reading.
                                assert (false) : "Problem persistence: failed to load diagram!";
                            }
                        }
                    }
                });
            
            }
        }

        /**
         * Force a refresh of the whole diagram.
         */
        @objid ("7e1dc271-1dec-11e2-8cad-001ec947c8cc")
        protected final void refreshAllDiagram() {
            final Collection<GmModel> toRefresh = getAllModels();
            
            for (final GmModel model : toRefresh) {
                final MObject el = model.getRelatedElement();
                if (el != null && el.isDeleted()) {
                    model.obElementDeleted();
                } else {
                    model.obElementsUpdated();
                }
            }
            
            // Save the refreshed diagram
            DiagramPersistence.saveDiagram(GmAbstractDiagram.this);
        }

    }

}

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
                                    

package org.modelio.diagram.elements.core.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.extensions.GmConnectionEndpoinLocator;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.elements.core.link.extensions.IGmLocator;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a link between 2 nodes in the diagram.
 */
@objid ("80100e01-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmLink extends GmModel implements IGmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("80127036-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("80127039-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Tells the source node changed.
     */
    @objid ("8fadff89-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROP_SOURCE_GM = "Source graphic model changed";

    /**
     * Tells the target node changed.
     */
    @objid ("8fb061de-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROP_TARGET_GM = "Target graphic model changed";

    /**
     * Tells the source element changed and is inconsistent from the source node.
     */
    @objid ("8fb061e4-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROP_SOURCE_EL = "Source element model changed";

    /**
     * Tells the target element changed and is inconsistent from the terget node.
     */
    @objid ("8fb2c43a-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROP_TARGET_EL = "Target element model changed";

    @objid ("80100e03-1dec-11e2-8cad-001ec947c8cc")
    protected IGmLinkable from;

    @objid ("80100e04-1dec-11e2-8cad-001ec947c8cc")
    protected IGmLinkable to;

    @objid ("80100e05-1dec-11e2-8cad-001ec947c8cc")
    private Map<GmNodeModel, IGmLocator> extensions = new HashMap<>();

    @objid ("80100e09-1dec-11e2-8cad-001ec947c8cc")
    private final List<IGmLink> startingLinks = new ArrayList<>();

    @objid ("80100e0d-1dec-11e2-8cad-001ec947c8cc")
    private final List<IGmLink> endingLinks = new ArrayList<>();

    /**
     * Initialize a new GmLink.
     * @param diagram The diagram containing the link.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("8012703b-1dec-11e2-8cad-001ec947c8cc")
    public GmLink(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
        
        this.from = null;
        this.to = null;
        init();
    }

    /**
     * Constructor for deserialization only.
     */
    @objid ("80127040-1dec-11e2-8cad-001ec947c8cc")
    public GmLink() {
        init();
    }

    @objid ("80127043-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void addEndingLink(final IGmLink link) {
        this.endingLinks.add(link);
        link.setTo(this);
        firePropertyChange(IGmObject.PROPERTY_LINK_TARGET, null, link);
    }

    /**
     * Add a link extension.
     * @param extension The link extension.
     * @param constraint The extension layout constraint.
     */
    @objid ("80127048-1dec-11e2-8cad-001ec947c8cc")
    public void addExtension(GmNodeModel extension, IGmLocator constraint) {
        this.extensions.put(extension, constraint);
        extension.setParentLink(this);
        firePropertyChange(PROPERTY_CHILDREN, null, extension);
    }

    /**
     * Add a link extension.
     * @param key extension key
     * @param extension the extension to add.
     */
    @objid ("8012704d-1dec-11e2-8cad-001ec947c8cc")
    public void addExtension(String key, GmNodeModel extension) {
        if (key.equals(ExtensionLocation.SourceNW)) {
            final GmConnectionEndpoinLocator constraint = new GmConnectionEndpoinLocator();
            constraint.setEnd(false);
            constraint.setVDistance(-5);
            constraint.setUDistance(5);
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.SourceSE)) {
            final GmConnectionEndpoinLocator constraint = new GmConnectionEndpoinLocator();
            constraint.setEnd(false);
            constraint.setVDistance(5);
            constraint.setUDistance(5);
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.TargetNW)) {
            final GmConnectionEndpoinLocator constraint = new GmConnectionEndpoinLocator();
            constraint.setEnd(true);
            constraint.setVDistance(-5);
            constraint.setUDistance(5);
        
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.TargetSE)) {
            final GmConnectionEndpoinLocator constraint = new GmConnectionEndpoinLocator();
            constraint.setEnd(true);
            constraint.setVDistance(5);
            constraint.setUDistance(5);
        
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.MiddleSE)) {
            /*final GmConnectionLocator constraint = new GmConnectionLocator();
            constraint.setAlignment(ConnectionLocator.MIDDLE);
            constraint.setRelativePosition(PositionConstants.SOUTH_EAST);
            constraint.setGap(5);*/
            final GmFractionalConnectionLocator constraint = new GmFractionalConnectionLocator();
            constraint.setFraction(0.5);
            constraint.setUDistance(0);
            constraint.setVDistance(-20);
        
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.MiddleNW)) {
            /*final GmConnectionLocator constraint = new GmConnectionLocator();
            constraint.setAlignment(ConnectionLocator.MIDDLE);
            constraint.setRelativePosition(PositionConstants.NORTH_WEST);
            constraint.setGap(5);*/
            final GmFractionalConnectionLocator constraint = new GmFractionalConnectionLocator();
            constraint.setFraction(0.5);
            constraint.setUDistance(0);
            constraint.setVDistance(20);
        
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.OnSourceThird)) {
            final GmFractionalConnectionLocator constraint = new GmFractionalConnectionLocator();
            constraint.setFraction(0.25);
            constraint.setUDistance(0);
            constraint.setVDistance(0);
        
            this.extensions.put(extension, constraint);
        } else if (key.equals(ExtensionLocation.OnTargetThird)) {
            final GmFractionalConnectionLocator constraint = new GmFractionalConnectionLocator();
            constraint.setFraction(0.75);
            constraint.setUDistance(0);
            constraint.setVDistance(0);
        
            this.extensions.put(extension, constraint);
        } else {
            throw new IllegalArgumentException("'" + extension + "' is not supproted");
        }
        
        extension.setParentLink(this);
        
        if (getRelatedElement() != null) {
            firePropertyChange(PROPERTY_CHILDREN, null, getRelatedElement().getName());
        }
    }

    @objid ("80127052-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void addStartingLink(final IGmLink link) {
        this.startingLinks.add(link);
        link.setFrom(this);
        firePropertyChange(IGmObject.PROPERTY_LINK_SOURCE, null, link);
    }

    /**
     * By default a link can unmask nothing.
     */
    @objid ("80127057-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    /**
     * By default a link can unmask nothing.
     */
    @objid ("80127060-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    /**
     * Delete the link from the diagram.
     * <p>
     * Delete its links, extensions and then detach from its source and destination.
     */
    @objid ("8014d293-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void delete() {
        // delete outgoing links
        for (IGmLink l : new ArrayList<>(this.startingLinks)) {
            l.delete();
        }
        // delete incoming links
        for (IGmLink l : new ArrayList<>(this.endingLinks)) {
            l.delete();
        }
        
        // delete extensions
        for (GmNodeModel extension : new ArrayList<>(this.extensions.keySet())) {
            extension.delete();
        }
        
        // detach from source
        if (this.from != null) {
            this.from.removeStartingLink(this);
            this.from = null;
        }
        
        // detach from target
        if (this.to != null) {
            this.to.removeEndingLink(this);
            this.to = null;
        }
        
        // now I can die
        super.delete();
    }

    /**
     * Fires a {@link org.modelio.diagram.elements.core.model.IGmObject#PROPERTY_CHILDREN PROPERTY_CHILDREN}
     * property change.
     * <p>
     * To be called when the result of {@link GmNodeModel#isVisible()} on the given link extension changes.
     * @param child The link extension node whose visibility changed.
     */
    @objid ("8014d297-1dec-11e2-8cad-001ec947c8cc")
    public final void fireChildVisibilityChanged(GmNodeModel child) {
        firePropertyChange(PROPERTY_CHILDREN, null, child);
    }

    @objid ("8014d29b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void firePathChanged(final IGmPath path) {
        firePropertyChange(PROPERTY_LAYOUTDATA, null, path);
    }

    @objid ("8014d2a0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final List<IGmLink> getEndingLinks() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.endingLinks;
    }

    /**
     * Get all link extensions.
     * <p>
     * Link extensions are roundly all labels related to the link, eg: association role name and cardinality.
     * @return all link extensions.
     */
    @objid ("8014d2a7-1dec-11e2-8cad-001ec947c8cc")
    public final Collection<GmNodeModel> getExtensions() {
        return this.extensions.keySet();
    }

    /**
     * @return The link source
     */
    @objid ("8014d2ae-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final IGmLinkable getFrom() {
        return this.from;
    }

    /**
     * Returns the element being the source of the represented link (in the Ob model).
     * <p>
     * May return <code>null</code> if {@link #getRelatedElement()} returns <code>null</code>.
     * <p>
     * <em>This method must <strong>NOT</strong> return "<code>this.getFrom().getElement();</code>"
     * but instead must read the actual source of the link returned by {@link #getRelatedElement()}.</em>
     * @return the element being the source of the represented link (in the Ob model).
     */
    @objid ("8014d2b4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public abstract MObject getFromElement();

    /**
     * Get the locator model used to layout the given extension.
     * @param extension A link extension.
     * @return The locator model.
     */
    @objid ("8014d2b8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final IGmLocator getLayoutContraint(IGmObject extension) {
        return this.extensions.get(extension);
    }

    @objid ("8014d2be-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final GmModel getParent() {
        // links have no parent at the Gm graphic model level
        return null;
    }

    @objid ("8014d2c3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final IGmPath getPath() {
        return (IGmPath) getLayoutData();
    }

    /**
     * Get the element representation mode.
     * <p>
     * Return {@link RepresentationMode#STRUCTURED} , no mode is applicable.
     */
    @objid ("8014d2c8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    /**
     * Get the source anchor model.
     * @return the source anchor.
     */
    @objid ("8014d2ce-1dec-11e2-8cad-001ec947c8cc")
    public final Object getSourceAnchor() {
        return getPath().getSourceAnchor();
    }

    /**
     * Get the links starting from this node.
     * @return the links starting from this node.
     */
    @objid ("8014d2d3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final List<IGmLink> getStartingLinks() {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        return this.startingLinks;
    }

    /**
     * Get the target anchor model.
     * @return the target anchor.
     */
    @objid ("8014d2db-1dec-11e2-8cad-001ec947c8cc")
    public final Object getTargetAnchor() {
        return getPath().getTargetAnchor();
    }

    /**
     * @return the link destination
     */
    @objid ("8014d2e0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final IGmLinkable getTo() {
        return this.to;
    }

    /**
     * Returns the element being the target of the represented link (in the Ob model).
     * <p>
     * <em>This methods must <strong>NOT</strong> return "<code>this.getTo().getElement();</code>"
     * but instead must read the actual target of the link returned by {@link #getRelatedElement()}.</em>
     * <p>
     * May return <code>null</code> if {@link #getRelatedElement()} returns <code>null</code>.
     * @return the element being the target of the represented link (in the Ob model).
     */
    @objid ("801734ec-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public abstract MObject getToElement();

    /**
     * <p>
     * Get the extensions nodes currently visible.
     * </p>
     * <p>
     * The returned list is a copy and may be freely modified.
     * </p>
     * <p>
     * Default implementation returns a list of all extensions for which the isVisible method returns <code>true</code>.
     * This method may be overridden to dynamically filter the extensions list, based on current representation mode for
     * example.<br>
     * In this case you must ensure that {@link #styleChanged(StyleKey, Object)} fires a
     * {@link IGmObject#PROPERTY_CHILDREN} property change event in order for the EditParts to be informed of the
     * change.<br>
     * </p>
     * @return The visible link extension nodes.
     */
    @objid ("801734f0-1dec-11e2-8cad-001ec947c8cc")
    public Collection<GmNodeModel> getVisibleExtensions() {
        final Collection<GmNodeModel> allExtensions = getExtensions();
        final Collection<GmNodeModel> ret = new ArrayList<>(allExtensions.size());
        
        for (final GmNodeModel childNode : allExtensions) {
            if (childNode.isVisible())
                ret.add(childNode);
        }
        return ret;
    }

    /**
     * Redefined to refresh also link extensions who relates the same element.
     */
    @objid ("801734f7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void obElementAdded(MObject addedEl) {
        super.obElementAdded(addedEl);
        
        for (GmNodeModel m : getExtensions()) {
            if (m.getRepresentedElement() == null)
                m.obElementAdded(addedEl);
        }
    }

    /**
     * Redefined to refresh also link extensions who relates the same element.
     */
    @objid ("801734fc-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void obElementResolved(MObject ev) {
        super.obElementResolved(ev);
        
        for (GmNodeModel m : getExtensions()) {
            if (m.getRepresentedElement() == null)
                m.obElementResolved(ev);
        }
    }

    /**
     * This method is final. Subclasses should override {@link #readLink(IDiagramReader)} instead.
     */
    @objid ("80173501-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmLink." + MINOR_VERSION_PROPERTY);
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

    @objid ("80173506-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void removeEndingLink(final IGmLink gmLink) {
        this.endingLinks.remove(gmLink);
        gmLink.setTo(null);
        firePropertyChange(IGmObject.PROPERTY_LINK_TARGET, gmLink, null);
    }

    /**
     * Remove a link extension.
     * @param gmNodeModel the link extension to remove.
     * @throws java.lang.IllegalArgumentException if the link does not own this node.
     */
    @objid ("8017350b-1dec-11e2-8cad-001ec947c8cc")
    public void removeExtension(GmNodeModel gmNodeModel) throws IllegalArgumentException {
        assert (this.extensions.containsKey(gmNodeModel));
        
        this.extensions.remove(gmNodeModel);
        firePropertyChange(PROPERTY_CHILDREN, gmNodeModel, null);
        
        gmNodeModel.setParentLink(null);
    }

    @objid ("8017350f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void removeStartingLink(final IGmLink gmLink) {
        this.startingLinks.remove(gmLink);
        gmLink.setFrom(null);
        firePropertyChange(IGmObject.PROPERTY_LINK_SOURCE, gmLink, null);
    }

    /**
     * Update the link origin.
     * <p>
     * This method is intended to be called only by {@link IGmLinkable#addEndingLink(IGmLink)}. It does not fire change
     * event.
     * @param from The new link origin
     */
    @objid ("80173514-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setFrom(IGmLinkable from) {
        if (from != this.from) {
            Object oldFrom = this.from;
            this.from = from;
            firePropertyChange(PROP_SOURCE_GM, oldFrom, from);
        }
    }

    /**
     * Change the given extension location.
     * @param extension The link extension.
     * @param layoutData The extension layout constraint.
     */
    @objid ("80173519-1dec-11e2-8cad-001ec947c8cc")
    public final void setLayoutConstraint(GmNodeModel extension, IGmLocator layoutData) {
        this.extensions.put(extension, layoutData);
        firePropertyChange(PROPERTY_LAYOUTDATA, extension, layoutData);
    }

    @objid ("8017351e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void setLayoutData(final Object layoutData) {
        final IGmPath oldPath = getPath();
        final IGmPath newPath = (IGmPath) layoutData;
        
        if (newPath != null && getStyle() != null) {
            if (newPath.getRouterKind() == null) {
                // Set the router kind from the style if not specified.
        
                newPath.setRouterKind(getRouterFromStyle(getStyle()));
            } else {
                // Update the connection router in the style.
        
                // Inhibit style update notification
                getPath().setRouterKind(newPath.getRouterKind());
        
                // Update the connection router in the style.
                final StyleKey routerStyleKey = getStyleKey(MetaKey.CONNECTIONROUTER);
                if (routerStyleKey != null &&
                        getStyle().getProperty(routerStyleKey) != newPath.getRouterKind())
                    getStyle().setProperty(routerStyleKey, newPath.getRouterKind());
            }
        }
        
        // FIXME: Update anchors connected links
        Object oldSrcAnchor = null;
        Object oldDestAnchor = null;
        
        if (oldPath != null) {
            oldSrcAnchor = oldPath.getSourceAnchor();
            oldDestAnchor = oldPath.getTargetAnchor();
        
            if (oldSrcAnchor instanceof GmAbstractLinkAnchor)
                ((GmAbstractLinkAnchor) oldSrcAnchor).removeLink(this);
        
            if (oldDestAnchor instanceof GmAbstractLinkAnchor)
                ((GmAbstractLinkAnchor) oldDestAnchor).removeLink(this);
        }
        
        if (newPath != null) {
            final Object newSrcAnchor = newPath.getSourceAnchor();
            final Object newDestAnchor = newPath.getTargetAnchor();
        
            if (newSrcAnchor instanceof GmAbstractLinkAnchor && newSrcAnchor != oldSrcAnchor)
                ((GmAbstractLinkAnchor) newSrcAnchor).addLink(this);
        
            if (newDestAnchor instanceof GmAbstractLinkAnchor && newDestAnchor != oldDestAnchor)
                ((GmAbstractLinkAnchor) newDestAnchor).addLink(this);
        }
        
        // Change the path
        super.setLayoutData(layoutData);
    }

    /**
     * Convenience method to change only the source anchor.
     * @param newAnchor the new source anchor.
     * @deprecated use:
     * 
     * <pre>
     * GmPath path = new GmPath(gmlink.getPath());
     * path.setSourceAnchor(newAnchor);
     * gmlink.setLayoutData(path);
     * </pre>
     */
    @objid ("80173523-1dec-11e2-8cad-001ec947c8cc")
    @Deprecated
    public final void setSourceAnchor(final GmAbstractLinkAnchor newAnchor) {
        GmPath path = new GmPath(getPath());
        path.setSourceAnchor(newAnchor);
        setLayoutData(path);
    }

    /**
     * Convenience method to change only the target anchor.
     * @param newAnchor the new target anchor.
     * @deprecated use:
     * 
     * <pre>
     * GmPath path = new GmPath(gmlink.getPath());
     * path.setTargetAnchor(newAnchor);
     * gmlink.setLayoutData(path);
     * </pre>
     */
    @objid ("80173529-1dec-11e2-8cad-001ec947c8cc")
    @Deprecated
    public final void setTargetAnchor(final GmAbstractLinkAnchor newAnchor) {
        GmPath path = new GmPath(getPath());
        path.setTargetAnchor(newAnchor);
        setLayoutData(path);
    }

    /**
     * Update the link destination.
     * <p>
     * This method is intended to be called only by {@link IGmLinkable#addEndingLink(IGmLink)}. It does not fire change
     * event.
     * @param to The new destination
     */
    @objid ("8017352f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTo(IGmLinkable to) {
        if (to != this.to) {
            Object oldTo = this.to;
            this.to = to;
            firePropertyChange(PROP_TARGET_GM, oldTo, to);
        }
    }

    @objid ("80199746-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        assert (this.from != null && this.to != null) : "this.from == null || this.to == null";
        
        out.writeProperty("Source", this.from);
        out.writeProperty("Dest", this.to);
        out.writeProperty("extensions", this.extensions);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * Called by the anchor when its location changes.
     * @param gmLinkAnchor The moved anchor.
     */
    @objid ("8019974a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final void fireAnchorMoved(GmAbstractLinkAnchor gmLinkAnchor) {
        firePropertyChange(PROPERTY_LAYOUTDATA, null, gmLinkAnchor);
    }

    /**
     * Get the connection router id stored in the given style. If no StyleKey is found, the default value for the router
     * is DIRECT.
     * @param style a style
     * @return the connection router.
     */
    @objid ("8019974e-1dec-11e2-8cad-001ec947c8cc")
    final ConnectionRouterId getRouterFromStyle(final IStyle style) {
        final StyleKey routerStyleKey = getStyleKey(MetaKey.CONNECTIONROUTER);
        if (routerStyleKey != null) {
            final ConnectionRouterId newRouter = (ConnectionRouterId) style.getProperty(routerStyleKey);
            return newRouter;
        }
        return ConnectionRouterId.DIRECT;
    }

    /**
     * Convenience method that compare 2 potentially null references.
     * <p>
     * Returns true if both references are null or a.equals(b) return true.
     * @param a first object
     * @param b second object
     * @return true if both references are null or a.equals(b) return true.
     */
    @objid ("80199755-1dec-11e2-8cad-001ec947c8cc")
    protected final boolean areEqual(final Object a, final Object b) {
        if (a == null) {
            return (b == null);
        } else {
            return a.equals(b);
        }
    }

    /**
     * Redefinable method called by {@link GmLink#read(IDiagramReader)} before adding the link to the diagram.
     * <p>
     * Subclasses should redefine this method instead of {@link #read(IDiagramReader)}.
     * <p>
     * The default implementation does nothing.
     * @param in a reader to build the graphic model from.
     */
    @objid ("8019975d-1dec-11e2-8cad-001ec947c8cc")
    protected void readLink(IDiagramReader in) {
        // Nothing to do
    }

    @objid ("80199761-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromObModel() {
        MObject relatedElement = getRelatedElement();
        if (relatedElement != null && !relatedElement.isShell() && !relatedElement.isDeleted()) {
            MObject graphicSourceElement = this.getFromElement();
            if (this.getFrom() == null) {
                // source changed: let edit part know and handle it.
                firePropertyChange(PROP_SOURCE_EL, null, graphicSourceElement);
            } else if (this.getFrom() instanceof GmModel) {
                MObject modelSourceElement = ((GmModel) this.getFrom()).getRelatedElement();
        
                if (!areEqual(modelSourceElement, graphicSourceElement)) {
                    // source changed: let edit part know and handle it.
                    firePropertyChange(PROP_SOURCE_EL, modelSourceElement, graphicSourceElement);
                } else if (this.getFrom() instanceof GmNodeModel && !((GmNodeModel)this.getFrom()).isVisible()) {
                    // source not visible anymore, delete the link
                    this.delete();
                    return;
                }
            }
        
            MObject graphicTargetElement = this.getToElement();
            if (this.getTo() == null) {
                // target changed: let edit part know and handle it.
                firePropertyChange(PROP_TARGET_EL, null, graphicTargetElement);
            } else if (this.getTo() instanceof GmModel) {
                MObject modelTargetElement = ((GmModel) this.getTo()).getRelatedElement();
                if (!areEqual(modelTargetElement, graphicTargetElement)) {
                    // target changed: let edit part know and handle it.
                    firePropertyChange(PROP_TARGET_EL, modelTargetElement, graphicTargetElement);
                } else if (this.getTo() instanceof GmNodeModel && !((GmNodeModel)this.getTo()).isVisible()) {
                    // target not visible anymore, delete the link
                    this.delete();
                    return;
                }
            }
        
        }
    }

    @objid ("80199764-1dec-11e2-8cad-001ec947c8cc")
    private void init() {
        GmPath path = new GmPath();
        path.setPathData(new ArrayList<>());
        setLayoutData(path);
    }

    /**
     * This method is final. Subclasses should override {@link #readLink(IDiagramReader)} instead.
     */
    @objid ("80199766-1dec-11e2-8cad-001ec947c8cc")
    private final void read_0(IDiagramReader in) {
        super.read(in);
        this.from = (IGmLinkable) in.readProperty("Source");
        this.to = (IGmLinkable) in.readProperty("Dest");
        this.extensions = in.readMapProperty("extensions");
        
        // FIXME Connect anchors
        final Object sourceAnchor = getPath().getSourceAnchor();
        if (sourceAnchor instanceof GmAbstractLinkAnchor)
            ((GmAbstractLinkAnchor) sourceAnchor).addLink(this);
        final Object targetAnchor = getPath().getTargetAnchor();
        if (targetAnchor instanceof GmAbstractLinkAnchor)
            ((GmAbstractLinkAnchor) targetAnchor).addLink(this);
        
        // Read child classes data
        readLink(in);
        
        // Connect extensions
        for (GmNodeModel ext : this.extensions.keySet()) {
            ext.setParentLink(this);
        }
        
        // Connect extremities
        if (this.from != null)
            this.from.addStartingLink(this);
        if (this.to != null)
            this.to.addEndingLink(this);
    }

    @objid ("8019976a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}

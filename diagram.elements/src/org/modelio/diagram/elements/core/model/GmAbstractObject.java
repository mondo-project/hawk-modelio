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
                                    

package org.modelio.diagram.elements.core.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.IStyleChangeListener;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Abstract class for all graphic models.
 * <p>
 * All graphic models should inherit from this class instead of directly implementing {@link IGmObject}.
 * 
 * @author cmarin
 */
@objid ("8078f557-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmAbstractObject implements IGmObject, IStyleChangeListener {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("8078f563-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("8078f566-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("91ea33e5-1e83-11e2-8cad-001ec947c8cc")
    protected static final String MINOR_VERSION_PROPERTY = "version";

    /**
     * Stores the position and/or the size of the element in the diagram. Is usually a
     * {@link org.eclipse.draw2d.geometry.Rectangle Rectangle}.
     */
    @objid ("8078f559-1dec-11e2-8cad-001ec947c8cc")
    private Object layoutData = null;

    /**
     * Listeners of all property change events.
     */
    @objid ("8078f55b-1dec-11e2-8cad-001ec947c8cc")
    private PropertyChangeSupport listeners = null;

    /**
     * Style of the graphic element.
     */
    @objid ("8078f55d-1dec-11e2-8cad-001ec947c8cc")
    private IStyle style = null;

    /**
     * The diagram owning this diagram element.
     */
    @objid ("8078f55f-1dec-11e2-8cad-001ec947c8cc")
    private GmAbstractDiagram diagram;

    /**
     * Creates an instance ready to be used.
     * @param diagram the diagram where the object will be.
     */
    @objid ("8078f568-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractObject(GmAbstractDiagram diagram) {
        this.diagram = diagram;
        this.listeners = new PropertyChangeSupport(this);
    }

    /**
     * Creates an empty {@link GmAbstractObject} that will be deserialized.
     */
    @objid ("8078f56c-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractObject() {
        this.listeners = new PropertyChangeSupport(this);
    }

    @objid ("8078f56f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Delete the graphic model from the diagram.
     * <p>
     * This method may be redefined but in this case the inherited method must be called.
     */
    @objid ("8078f573-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void delete() {
        firePropertyChange(IGmObject.PROPERTY_DELETE, null, null);
        if (this.style != null)
            this.style.removeListener(this);
    }

    /**
     * Get the diagram containing this element.
     * @return the diagram.
     */
    @objid ("8078f577-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public GmAbstractDiagram getDiagram() {
        return this.diagram;
    }

    @objid ("8078f57d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getLayoutData() {
        return this.layoutData;
    }

    /**
     * Get the graphical element style.
     * <p>
     * The style contains many properties such has the foreground and background color, the font and some display
     * options. These properties are displayed and editable in a properties tab.
     * </p>
     * <p>
     * Defined final to make sure that lazy initialisation is used.
     * </p>
     * @return the graphical element style.
     */
    @objid ("8078f582-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public final IStyle getStyle() {
        // Lazy initialisation.
        // Diagram's style is needed as "base" style, 
        // but during deserialisation it might not yet 
        // be available.
        if (this.style == null && getDiagram() != null) {
            this.style = createStyle(getDiagram());
            this.style.addListener(this);
        }
        return this.style;
    }

    /**
     * By default all GmAbstractObjects are internal.
     */
    @objid ("8078f588-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isExternal(IDiagramWriter out) {
        return false;
    }

    @objid ("8078f58f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmAbstractObject." + MINOR_VERSION_PROPERTY);
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

    @objid ("8078f593-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }

    @objid ("807b57ad-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLayoutData(Object layoutData) {
        Object oldData = this.layoutData;
        this.layoutData = layoutData;
        firePropertyChange(IGmObject.PROPERTY_LAYOUTDATA, oldData, layoutData);
    }

    /**
     * Called when a property of the style of the element is modified.
     * <p>
     * The element should then update itself from the style change.
     */
    @objid ("807b57b1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(StyleKey property, Object newValue) {
        firePropertyChange(IGmObject.PROPERTY_STYLE, null, getStyle());
    }

    /**
     * Called when a style completely changed .
     * <p>
     * The element should then update itself completely from the style.
     */
    @objid ("807b57b7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(IStyle changedStyle) {
        firePropertyChange(IGmObject.PROPERTY_STYLE, null, changedStyle);
    }

    @objid ("807b57bc-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        out.writeProperty("layoutData", getLayoutData());
        out.writeProperty("Style", this.style);
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAbstractObject." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * Instantiate the graphic model style.
     * <p>
     * Called by the {@link #getStyle()} method as lazy initialisation.
     * <p>
     * Must be redefined to create a style or to return <tt>null<tt/> if
     * {@link #getStyle()} is redefined to return another style.
     * @param aDiagram the diagram where the object will be
     * @return the created style or <tt>null</tt> if the creation is postponed
     */
    @objid ("807b57c0-1dec-11e2-8cad-001ec947c8cc")
    protected abstract IStyle createStyle(GmAbstractDiagram aDiagram);

    @objid ("807b57c4-1dec-11e2-8cad-001ec947c8cc")
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.listeners.firePropertyChange(propertyName, oldValue, newValue);
    }

    @objid ("807b57c9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("807b57ce-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(final IDiagramReader in) {
        this.layoutData = in.readProperty("layoutData");
        this.style = (IStyle) in.readProperty("Style");
        this.diagram = (GmAbstractDiagram) in.getRoot();
        
        if (this.style != null)
            this.style.addListener(this);
    }

    /**
     * Return true if the diagram owning this Gm is editable.
     */
    @objid ("807b57d2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isEditable() {
        return this.diagram.isEditable();
    }

}

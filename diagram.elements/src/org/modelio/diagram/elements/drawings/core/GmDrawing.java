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
                                    

package org.modelio.diagram.elements.drawings.core;

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.Style;

/**
 * Graphic model representing a node or a link that does not represent a model element.
 * 
 * @author cmarin
 */
@objid ("807430a0-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmDrawing extends GmAbstractObject implements IGmDrawing {
    @objid ("807430a5-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Drawing identifier in the diagram.
     */
    @objid ("3e3ff493-b872-4b22-8c90-abb11961f191")
    private String identifier;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("807430a2-1dec-11e2-8cad-001ec947c8cc")
    private static final int minorVersion = 0;

    /**
     * Default constructor.
     * @param diagram the owner diagram.
     * @param identifier the drawing identifier.
     */
    @objid ("110d2aa1-cc0b-46f6-8214-0704c4106da7")
    public GmDrawing(GmAbstractDiagram diagram, String identifier) {
        super(diagram);
        this.identifier = identifier;
        init();
    }

    /**
     * Deserialization only constructor.
     */
    @objid ("359942d3-e6ed-4612-ab94-06b04ca2c05e")
    public GmDrawing() {
        super();
    }

    @objid ("03a05c84-9278-4da6-a745-253794a78eee")
    @Override
    public void delete() {
        if (getDiagram() != null)
            getDiagram().removeGraphicModel(this);
        
        super.delete();
    }

    @objid ("c12dceea-5ecb-4074-9855-47c13e2fdd12")
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @objid ("807430b9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("807430ae-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmDrawing." + MINOR_VERSION_PROPERTY);
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

    @objid ("807430b2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        //        if (GmDrawing.minorVersion != 0) {
        //            out.writeProperty("GmDrawing." + MINOR_VERSION_PROPERTY, Integer.valueOf(GmDrawing.minorVersion));
        //        }
        
        out.writeProperty("id", this.identifier);
    }

    /**
     * Instantiate the graphic model style.
     * <p>
     * Called by the standard {@link GmAbstractObject#GmAbstractObject(GmAbstractDiagram)} constructor.
     * <p>
     * Default implementation makes the style derive from the diagram style if not null, or from the factory style in
     * the other case.
     * <p>
     * Can be redefined to create another style or to return <tt>null<tt/> if
     * {@link #getStyle()} is redefined to return another style.
     * @param aDiagram the diagram where the object will be
     * @return the created style or <tt>null</tt> if the creation is postponed
     */
    @objid ("807430a7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IStyle createStyle(GmAbstractDiagram aDiagram) {
        return new Style(aDiagram.getStyle());
    }

    /**
     * <p>
     * Initialize the object.
     * </p>
     * <p>
     * Must be called before usage by and only by:
     * <ul>
     * <li>The {@link #GmDrawing(GmAbstractDiagram, String)} constructor (but NOT by the parameter less constructor).
     * <li>and the {@link #read(IDiagramReader)} method
     * </ul>
     * </p>
     * <p>
     * The same method may exist in subclasses. In this case:
     * <ul>
     * <li>the child <em>init()</em> method must be private too,
     * <li>it must <strong>never</strong> call <em>super.init()</em>
     * <li>it must be called by the above 2 methods. they must be created if absent.
     * </ul>
     * </p>
     */
    @objid ("41eff6dc-4cee-4034-a795-9a0a55fb53dc")
    private void init() {
        // If the GmAbstractDiagram is reachable.
        if (getDiagram() != null) {
            // Register element in the diagram
            getDiagram().addGraphicModel(this);
        }
    }

    @objid ("807430b6-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        this.identifier = (String) in.readProperty("id");
        
        if (this.identifier == null)
            this.identifier = UUID.randomUUID().toString();
        
        init();
    }

}

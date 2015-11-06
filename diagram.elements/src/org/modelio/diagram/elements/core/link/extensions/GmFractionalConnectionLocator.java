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
                                    

package org.modelio.diagram.elements.core.link.extensions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;

/**
 * Align the figure on the Connection, at a a fraction of the line length from the starting point of the line.
 * <p>
 * 
 * @author cmarin
 */
@objid ("80042256-1dec-11e2-8cad-001ec947c8cc")
public class GmFractionalConnectionLocator implements IGmLocator {
    @objid ("80042258-1dec-11e2-8cad-001ec947c8cc")
    private double fraction;

    /**
     * Distance from the reference point towards the target
     */
    @objid ("80042259-1dec-11e2-8cad-001ec947c8cc")
    private int uDistance;

    /**
     * Distance from the connection.
     */
    @objid ("8004225b-1dec-11e2-8cad-001ec947c8cc")
    private int vDistance;

    /**
     * Rotatable figures orientation: <li>true: toward the target. <li>false: toward the source.
     */
    @objid ("8004225d-1dec-11e2-8cad-001ec947c8cc")
    private boolean towardTarget = true;

    @objid ("8004225f-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Creates a GmFractionalConnectionLocator.
     * @param fraction the position of the reference point as a fraction of the connection length. Must be between 0.0 and
     * 1.0
     * @param uOffset offset toward the next connection point
     * @param vOffset distance from the connection. v < 0 place the figure on the top or left side.
     * @param towardTarget Rotatable figures orientation:
     * <ul>
     * <li>true: toward the target.
     * <li>false: toward the source.
     * </ul>
     */
    @objid ("80042261-1dec-11e2-8cad-001ec947c8cc")
    public GmFractionalConnectionLocator(final double fraction, final int uOffset, final int vOffset, final boolean towardTarget) {
        this(fraction, uOffset, vOffset);
        this.towardTarget = towardTarget;
    }

    /**
     * Default constructor.
     */
    @objid ("80068479-1dec-11e2-8cad-001ec947c8cc")
    public GmFractionalConnectionLocator() {
    }

    /**
     * Creates a GmFractionalConnectionLocator.
     * @param fraction the position of the reference point as a fraction of the connection length. Must be between 0.0 and
     * 1.0
     * @param uOffset offset toward the next connection point
     * @param vOffset distance from the connection. v < 0 place the figure on the top or left side.
     */
    @objid ("8006847c-1dec-11e2-8cad-001ec947c8cc")
    public GmFractionalConnectionLocator(final double fraction, final int uOffset, final int vOffset) {
        this.fraction = fraction;
        this.uDistance = uOffset;
        this.vDistance = vOffset;
    }

    /**
     * Returns the distance in fraction of the connection length from connection source.
     * @return the fractional distance from connection source.
     */
    @objid ("80068485-1dec-11e2-8cad-001ec947c8cc")
    public double getFraction() {
        return this.fraction;
    }

    /**
     * Distance from the reference point towards the target
     * @return Distance from the reference point towards the target
     */
    @objid ("8006848a-1dec-11e2-8cad-001ec947c8cc")
    public int getUDistance() {
        return this.uDistance;
    }

    /**
     * Get the distance from the connection.
     * @return The distance from the connection.
     */
    @objid ("8006848f-1dec-11e2-8cad-001ec947c8cc")
    public int getVDistance() {
        return this.vDistance;
    }

    @objid ("80068494-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isExternal(IDiagramWriter out) {
        return false;
    }

    /**
     * Get the rotatable figures orientation.
     * @return true : toward the target, false: toward the source
     */
    @objid ("8006849a-1dec-11e2-8cad-001ec947c8cc")
    public boolean isTowardTarget() {
        return this.towardTarget;
    }

    @objid ("8006849f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        this.fraction = (Double) in.readProperty("fraction");
        this.uDistance = (Integer) in.readProperty("u");
        this.vDistance = (Integer) in.readProperty("v");
        this.towardTarget = (Boolean) in.readProperty("t");
    }

    /**
     * Sets the distance in fraction of the connection length from connection source.
     * @param d the fractional distance from connection source.
     */
    @objid ("800684a3-1dec-11e2-8cad-001ec947c8cc")
    public void setFraction(final double d) {
        this.fraction = d;
    }

    /**
     * Set the rotatable figures orientation.
     * @param towardTarget true to orient toward the target, false for the source
     */
    @objid ("800684a8-1dec-11e2-8cad-001ec947c8cc")
    public void setTowardTarget(final boolean towardTarget) {
        this.towardTarget = towardTarget;
    }

    /**
     * Distance from the reference point towards the target
     * @param uDistance The distance from the reference point towards the target
     */
    @objid ("800684ad-1dec-11e2-8cad-001ec947c8cc")
    public void setUDistance(final int uDistance) {
        this.uDistance = uDistance;
    }

    /**
     * Distance from the connection.
     * @param vDistance The distance from the connection
     */
    @objid ("800684b2-1dec-11e2-8cad-001ec947c8cc")
    public void setVDistance(final int vDistance) {
        this.vDistance = vDistance;
    }

    @objid ("800684b7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        out.writeProperty("fraction", this.fraction);
        out.writeProperty("u", this.uDistance);
        out.writeProperty("v", this.vDistance);
        out.writeProperty("t", this.towardTarget);
    }

    @objid ("800684bb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}

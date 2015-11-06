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
                                    

package org.modelio.diagram.editor.deployment.elements.deploymentdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.deployment.style.DeploymentAbstractStyleKeyProvider;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagramStyleKeys;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmStateDiagram when its representation mode is
 * RepresentationMode.STRUCTURED
 */
@objid ("9728198d-55b6-11e2-877f-002564c97630")
public class GmDeploymentDiagramStyleKeys extends DeploymentAbstractStyleKeyProvider {
    /**
     * View grid.
     */
    @objid ("1ceffd4b-55c2-11e2-9337-002564c97630")
    public static final StyleKey VIEWGRID = GmAbstractDiagramStyleKeys.VIEWGRID;

    /**
     * Activate snap to grid.
     */
    @objid ("1ceffd4e-55c2-11e2-9337-002564c97630")
    public static final StyleKey SNAPTOGRID = GmAbstractDiagramStyleKeys.SNAPTOGRID;

    /**
     * Grid spacing in pixels.
     */
    @objid ("1ceffd51-55c2-11e2-9337-002564c97630")
    public static final StyleKey GRIDSPACING = GmAbstractDiagramStyleKeys.GRIDSPACING;

    /**
     * Grid color.
     */
    @objid ("1ceffd54-55c2-11e2-9337-002564c97630")
    public static final StyleKey GRIDCOLOR = GmAbstractDiagramStyleKeys.GRIDCOLOR;

    /**
     * Grid transparency: Integer value from 0 (full transaprency) to 255 (no transparency)
     */
    @objid ("1ceffd57-55c2-11e2-9337-002564c97630")
    public static final StyleKey GRIDALPHA = GmAbstractDiagramStyleKeys.GRIDALPHA;

    /**
     * Diagram background color.
     */
    @objid ("1ceffd5a-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = GmAbstractDiagramStyleKeys.FILLCOLOR;

    /**
     * Diagram background image.
     */
    @objid ("1ceffd5d-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLIMAGE = GmAbstractDiagramStyleKeys.FILLIMAGE;

    /**
     * Diagram image transparency. This StyleKey defines the fill transparency. The value applies to both the fill image
     * and the fill color. Integer value from 0 (full transparency) to 255 (no transparency)
     */
    @objid ("1ceffd60-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLALPHA = GmAbstractDiagramStyleKeys.FILLALPHA;

    /**
     * This StyleKey defines the page boundaries lines visibility. Boolean value.
     */
    @objid ("1cf183eb-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOW_PAGES = GmAbstractDiagramStyleKeys.SHOW_PAGES;

    /**
     * This StyleKey defines the page size. String value. Supported string format: <li>discrete values:
     * A5H,A4H,A3H,A2H,A1H,A0H, A5V,A4V,A3V,A2V,A1V,A0V <li>inches values: '8.5" x 3.4"' <li>mm values: '210 mm x 297
     * mm'
     */
    @objid ("1cf183ee-55c2-11e2-9337-002564c97630")
    public static final StyleKey PAGE_SIZE = GmAbstractDiagramStyleKeys.PAGE_SIZE;

}

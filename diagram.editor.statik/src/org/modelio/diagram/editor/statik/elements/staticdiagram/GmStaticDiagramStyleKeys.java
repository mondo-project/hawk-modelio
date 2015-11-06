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
                                    

package org.modelio.diagram.editor.statik.elements.staticdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagramStyleKeys;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a {@link GmStaticDiagram}.
 */
@objid ("36b60c01-55b7-11e2-877f-002564c97630")
public class GmStaticDiagramStyleKeys extends GmAbstractDiagramStyleKeys {
    /**
     * Display the grid.
     */
    @objid ("a7c29fea-55c2-11e2-9337-002564c97630")
     static final StyleKey VIEWGRID = GmAbstractDiagramStyleKeys.VIEWGRID;

    /**
     * Snap to grid.
     */
    @objid ("a7c29fed-55c2-11e2-9337-002564c97630")
     static final StyleKey SNAPTOGRID = GmAbstractDiagramStyleKeys.SNAPTOGRID;

    /**
     * Grid spacing in pixels.
     */
    @objid ("a7c29ff0-55c2-11e2-9337-002564c97630")
     static final StyleKey GRIDSPACING = GmAbstractDiagramStyleKeys.GRIDSPACING;

    /**
     * Grid color.
     */
    @objid ("a7c29ff3-55c2-11e2-9337-002564c97630")
     static final StyleKey GRIDCOLOR = GmAbstractDiagramStyleKeys.GRIDCOLOR;

    /**
     * Grid transparency. Integer value from 0 (full transparency) to 255 (no transparency).
     */
    @objid ("a7c29ff6-55c2-11e2-9337-002564c97630")
     static final StyleKey GRIDALPHA = GmAbstractDiagramStyleKeys.GRIDALPHA;

    /**
     * diagram background color.
     */
    @objid ("a7c29ff9-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = GmAbstractDiagramStyleKeys.FILLCOLOR;

    /**
     * Diagram background image.
     */
    @objid ("a7c29ffc-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLIMAGE = GmAbstractDiagramStyleKeys.FILLIMAGE;

    /**
     * Diagram background image transparency. Integer value from 0 (full transparency) to 255 (no transparency)
     */
    @objid ("a7c29fff-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLALPHA = GmAbstractDiagramStyleKeys.FILLALPHA;

    /**
     * Show diagram page limits.
     */
    @objid ("a7c2a002-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOW_PAGES = GmAbstractDiagramStyleKeys.SHOW_PAGES;

    /**
     * Page size.
     */
    @objid ("a7c2a005-55c2-11e2-9337-002564c97630")
     static final StyleKey PAGE_SIZE = GmAbstractDiagramStyleKeys.PAGE_SIZE;

}

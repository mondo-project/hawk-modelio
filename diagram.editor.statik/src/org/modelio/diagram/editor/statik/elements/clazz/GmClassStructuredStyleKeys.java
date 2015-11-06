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
                                    

package org.modelio.diagram.editor.statik.elements.clazz;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("3445f63b-55b7-11e2-877f-002564c97630")
public final class GmClassStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Representation model.
     */
    @objid ("a517f261-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("CLASS_REPRES_MODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("a517f264-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("CLASS_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("a517f267-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("CLASS_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("a517f26a-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("CLASS_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a517f26d-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("CLASS_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a517f270-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("CLASS_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a517f273-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("CLASS_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a517f276-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("CLASS_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a51978eb-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("CLASS_SHOWSTEREOTYPES", MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a51978ee-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("CLASS_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display class visibility.
     */
    @objid ("a51978f1-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWVISIBILITY = createStyleKey("CLASS_SHOWVISIBILITY", MetaKey.SHOWVISIBILITY);

    /**
     * Display attributes.
     */
    @objid ("a51978f4-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("CLASS_ATT_GROUPVISIBLE", MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a51978f7-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("CLASS_OP_GROUPVISIBLE", MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("a51978fa-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("CLASS_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("a51978fd-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("CLASS_INNERVIEWMODE", MetaKey.InnerGroup.INNERVIEWMODE);

    /**
     * Auto unmask ports.
     */
    @objid ("a5197900-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("CLASS_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("34477cf0-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes stereotype display mode.
         */
        @objid ("a519790b-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("CLASS_ATT_SHOWSTEREOTYPES", MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a519790e-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("CLASS_ATT_SHOWTAGS", MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a5197911-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("CLASS_ATT_SHOWVISIBILITY", MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("34477d0d-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Display operations signature.
         */
        @objid ("a51aff8f-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("CLASS_OP_SHOWSIGNATURE", MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a51aff92-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("CLASS_OP_SHOWSTEREOTYPES", MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a51aff95-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("CLASS_OP_SHOWTAGS", MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("a51aff98-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("CLASS_OP_SHOWVISIBILITY", MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("34490392-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends StaticAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("a51aff9d-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("CLASS_INTERNALSVIEWMODE", MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("a51affa0-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("CLASS_INTERNAL_TEXTCOLOR", MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a51affa3-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("CLASS_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a51affa6-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("CLASS_INTERNAL_SHOWSTEREOTYPES",
                MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a51affa9-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("CLASS_INTERNAL_SHOWTAGS", MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("a51affac-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("CLASS_INTERNAL_AUTOUNMASK", MetaKey.InternalGroup.INTAUTOUNMASK);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("344903b4-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends StaticAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("a51affb1-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("CLASS_INNER_TEXTCOLOR", MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a51affb4-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("CLASS_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Name display mode
         */
        @objid ("a51affb7-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWNAME = createStyleKey("CLASS_INNER_SHOWNAME", MetaKey.InnerGroup.INNERSHOWNAME);

        /**
         * Stereotype display mode.
         */
        @objid ("a51c862b-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("CLASS_INNER_SHOWSTEREOTYPES",
                MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a51c862e-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("CLASS_INNER_SHOWTAGS", MetaKey.InnerGroup.INNERSHOWTAGS);

        /**
         * Display visibility
         */
        @objid ("a51c8631-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("CLASS_INNER_SHOWVISIBILITY", MetaKey.InnerGroup.INNERSHOWVISIBILITY);

    }

}

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
                                    

package org.modelio.diagram.editor.statik.elements.datatype;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("34b86750-55b7-11e2-877f-002564c97630")
public final class DataTypeStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Representation model.
     */
    @objid ("a5661249-55c2-11e2-9337-002564c97630")
    public static final StyleKey REPMODE = createStyleKey("DATATYPE_REPRES_MODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("a566395b-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("DATATYPE_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("a566395e-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("DATATYPE_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("a566606b-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("DATATYPE_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a5668779-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("DATATYPE_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a566877c-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("DATATYPE_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a566ae89-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("DATATYPE_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a566ae8c-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("DATATYPE_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a566d59b-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATATYPE_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a566d59e-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("DATATYPE_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display visibility.
     */
    @objid ("a566fcab-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWVISIBILITY = createStyleKey("DATATYPE_SHOWVISIBILITY",
                                                                 MetaKey.SHOWVISIBILITY);

    /**
     * Display attributes.
     */
    @objid ("a56723b9-55c2-11e2-9337-002564c97630")
    public static final StyleKey ATTGROUPVISIBLE = createStyleKey("DATATYPE_ATT_GROUPVISIBLE",
                                                                  MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a56723bc-55c2-11e2-9337-002564c97630")
    public static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("DATATYPE_OP_GROUPVISIBLE",
                                                                        MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("a5674acb-55c2-11e2-9337-002564c97630")
    public static final StyleKey FEATURES = createStyleKey("DATATYPE_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("34bb7460-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a56798e9-55c2-11e2-9337-002564c97630")
        public static final StyleKey TEXTCOLOR = createStyleKey("DATATYPE_ATT_TEXTCOLOR",
                                                                MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("a56798ec-55c2-11e2-9337-002564c97630")
        public static final StyleKey FONT = createStyleKey("DATATYPE_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a567bffb-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATATYPE_ATT_SHOWSTEREOTYPES",
                                                                      MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a567bffe-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWTAGS = createStyleKey("DATATYPE_ATT_SHOWTAGS",
                                                               MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a567e70b-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWVISIBILITY = createStyleKey("DATATYPE_ATT_SHOWVISIBILITY",
                                                                     MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("34bb747f-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("a5683529-55c2-11e2-9337-002564c97630")
        public static final StyleKey TEXTCOLOR = createStyleKey("DATATYPE_OP_TEXTCOLOR",
                                                                MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("a568352c-55c2-11e2-9337-002564c97630")
        public static final StyleKey FONT = createStyleKey("DATATYPE_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("a5685c39-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSIGNATURE = createStyleKey("DATATYPE_OP_SHOWSIGNATURE",
                                                                    MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a5685c3c-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATATYPE_OP_SHOWSTEREOTYPES",
                                                                      MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a568834a-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWTAGS = createStyleKey("DATATYPE_OP_SHOWTAGS",
                                                               MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("a568834d-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWVISIBILITY = createStyleKey("DATATYPE_OP_SHOWVISIBILITY",
                                                                     MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("34bcfb04-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends StaticAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("a568d169-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("DATATYPE_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("a568d16c-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("DATATYPE_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a568f87b-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("DATATYPE_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a5691f89-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATATYPE_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a5691f8c-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("DATATYPE_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("a569469b-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("DATATYPE_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

}

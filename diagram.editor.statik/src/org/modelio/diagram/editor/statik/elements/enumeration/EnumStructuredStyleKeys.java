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
                                    

package org.modelio.diagram.editor.statik.elements.enumeration;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("34d257e7-55b7-11e2-877f-002564c97630")
public final class EnumStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Representation model.
     */
    @objid ("a65f1868-55c2-11e2-9337-002564c97630")
    public static final StyleKey REPMODE = createStyleKey("ENUM_REPRES_MODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("a65f186b-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("ENUM_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("a65f186e-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("ENUM_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("a65f1871-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("ENUM_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a65f1874-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("ENUM_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a65f1877-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("ENUM_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a6609eeb-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("ENUM_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a6609eee-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("ENUM_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a6609ef1-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("ENUM_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a6609ef4-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("ENUM_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display class visibility.
     */
    @objid ("a6609ef7-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWVISIBILITY = createStyleKey("ENUM_SHOWVISIBILITY",
                                                                 MetaKey.SHOWVISIBILITY);

    /**
     * Display attributes.
     */
    @objid ("a6609efa-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("ENUM_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a6609efd-55c2-11e2-9337-002564c97630")
    public static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("ENUM_OP_GROUPVISIBLE",
                                                                        MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Display enumeration literals.
     */
    @objid ("a6609f00-55c2-11e2-9337-002564c97630")
    public static final StyleKey LITGROUPVISIBLE = createStyleKey("ENUM_LIT_GROUPVISIBLE", Boolean.class);

    /**
     * Filter on features
     */
    @objid ("a6609f03-55c2-11e2-9337-002564c97630")
    public static final StyleKey FEATURES = createStyleKey("ENUM_FEATURES_FILTER", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("a6609f06-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("ENUM_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("34d56503-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Litteral extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a6609f09-55c2-11e2-9337-002564c97630")
        public static final StyleKey TEXTCOLOR = createStyleKey("ENUM_LIT_TEXTCOLOR", Color.class);

        /**
         * Attributes font.
         */
        @objid ("a6609f0c-55c2-11e2-9337-002564c97630")
        public static final StyleKey FONT = createStyleKey("ENUM_LIT_FONT", Font.class);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a6609f0f-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSTEREOTYPES = createStyleKey("ENUM_LIT_SHOWSTEREOTYPES",
                                                                      MetaKey.SHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a6609f12-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWTAGS = createStyleKey("ENUM_LIT_SHOWTAGS", MetaKey.SHOWTAGS);

    }

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("34d5651d-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a6622589-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ENUM_ATT_TEXTCOLOR", MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("a662258c-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ENUM_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a662258f-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ENUM_ATT_SHOWSTEREOTYPES",
                                                           MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a6622592-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ENUM_ATT_SHOWTAGS", MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a6622595-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("ENUM_ATT_SHOWVISIBILITY",
                                                          MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("34d6eb9f-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("a6622598-55c2-11e2-9337-002564c97630")
        public static final StyleKey TEXTCOLOR = createStyleKey("ENUM_OP_TEXTCOLOR",
                                                                MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("a662259b-55c2-11e2-9337-002564c97630")
        public static final StyleKey FONT = createStyleKey("ENUM_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("a662259e-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSIGNATURE = createStyleKey("ENUM_OP_SHOWSIGNATURE",
                                                                    MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a66225a1-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWSTEREOTYPES = createStyleKey("ENUM_OP_SHOWSTEREOTYPES",
                                                                      MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a66225a4-55c2-11e2-9337-002564c97630")
        public static final StyleKey SHOWTAGS = createStyleKey("ENUM_OP_SHOWTAGS",
                                                               MetaKey.OperationGroup.OPSHOWTAGS);

    }

    /**
     * Style keys for all inner enumeration group members.
     * 
     * @author cmarin
     */
    @objid ("34d6ebbe-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends StaticAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("a66225a7-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ENUM_INNER_TEXTCOLOR",
                                                     MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a66225aa-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ENUM_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Name display mode
         */
        @objid ("a66225ad-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWNAME = createStyleKey("ENUM_INNER_SHOWNAME",
                                                    MetaKey.InnerGroup.INNERSHOWNAME);

        /**
         * Stereotype display mode.
         */
        @objid ("a66225b0-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ENUM_INNER_SHOWSTEREOTYPES",
                                                           MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a66225b3-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ENUM_INNER_SHOWTAGS",
                                                    MetaKey.InnerGroup.INNERSHOWTAGS);

        /**
         * Display visibility
         */
        @objid ("a663ac29-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("ENUM_INNER_SHOWVISIBILITY",
                                                          MetaKey.InnerGroup.INNERSHOWVISIBILITY);

    }

}

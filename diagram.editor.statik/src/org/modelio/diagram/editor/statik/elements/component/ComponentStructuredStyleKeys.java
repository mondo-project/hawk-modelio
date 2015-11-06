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
                                    

package org.modelio.diagram.editor.statik.elements.component;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Component style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("349ffd55-55b7-11e2-877f-002564c97630")
public final class ComponentStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Representation mode.
     */
    @objid ("a644d993-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("COMPONENT_REPRES_MODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("a644d996-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("COMPONENT_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("a644d999-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("COMPONENT_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("a644d99c-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("COMPONENT_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a644d99f-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("COMPONENT_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a644d9a2-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("COMPONENT_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a644d9a5-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("COMPONENT_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a644d9a8-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("COMPONENT_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a644d9ab-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMPONENT_SHOWSTEREOTYPES",
                                                           MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a644d9ae-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("COMPONENT_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display attributes.
     */
    @objid ("a644d9b1-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("COMPONENT_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a644d9b4-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("COMPONENT_OP_GROUPVISIBLE",
                                                                 MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("a644d9b7-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("COMPONENT_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("a644d9ba-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("COMPONENT_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    @objid ("a646602b-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("COMPONENT_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("34a30a67-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a646602f-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("COMPONENT_ATT_TEXTCOLOR",
                                                         MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("a6466032-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("COMPONENT_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a6466035-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMPONENT_ATT_SHOWSTEREOTYPES",
                                                               MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a6466038-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("COMPONENT_ATT_SHOWTAGS",
                                                        MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a646603b-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("COMPONENT_ATT_SHOWVISIBILITY",
                                                              MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("34a30a84-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("a6466040-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("COMPONENT_OP_TEXTCOLOR",
                                                         MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("a6466043-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("COMPONENT_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("a6466046-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("COMPONENT_OP_SHOWSIGNATURE",
                                                             MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a6466049-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMPONENT_OP_SHOWSTEREOTYPES",
                                                               MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a646604c-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("COMPONENT_OP_SHOWTAGS",
                                                        MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("a646604f-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("COMPONENT_OP_SHOWVISIBILITY",
                                                              MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("34a4910b-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends StaticAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("a647e6cb-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("COMPONENT_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("a647e6ce-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("COMPONENT_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a647e6d1-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("COMPONENT_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a647e6d4-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMPONENT_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a647e6d7-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("COMPONENT_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("a647e6da-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("COMPONENT_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("34a4912d-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends StaticAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("a647e6df-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("COMPONENT_INNER_TEXTCOLOR",
                                                         MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a647e6e2-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("COMPONENT_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a647e6e5-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMPONENT_INNER_SHOWSTEREOTYPES",
                                                               MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a647e6e8-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("COMPONENT_INNER_SHOWTAGS",
                                                        MetaKey.InnerGroup.INNERSHOWTAGS);

    }

}

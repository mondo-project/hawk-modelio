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
                                    

package org.modelio.diagram.editor.statik.elements.interfaze;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("357ec487-55b7-11e2-877f-002564c97630")
public final class InterfaceStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Representation model.
     */
    @objid ("a5aabc89-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("INTERFACE_REPRES_MODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("a5aabc8c-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("INTERFACE_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("a5aabc8f-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("INTERFACE_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("a5aabc92-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("INTERFACE_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a5aabc95-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("INTERFACE_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a5ac4309-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("INTERFACE_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a5ac430c-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("INTERFACE_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a5ac430f-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("INTERFACE_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a5ac4312-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("INTERFACE_SHOWSTEREOTYPES",
                                                           MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a5ac4315-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("INTERFACE_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display visibility.
     */
    @objid ("a5ac4318-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWVISIBILITY = createStyleKey("INTERFACE_SHOWVISIBILITY", MetaKey.SHOWVISIBILITY);

    /**
     * Display attributes.
     */
    @objid ("a5ac431b-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("INTERFACE_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a5ac431e-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("INTERFACE_OP_GROUPVISIBLE",
                                                                 MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("a5ac4321-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("INTERFACE_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("a5ac4324-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("INTERFACE_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    @objid ("a5ac4327-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("INTERFACE_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("3581d1a0-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a5ac4329-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("INTERFACE_ATT_TEXTCOLOR",
                                                         MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("a5ac432c-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("INTERFACE_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a5ac432f-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("INTERFACE_ATT_SHOWSTEREOTYPES",
                                                               MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a5ac4332-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("INTERFACE_ATT_SHOWTAGS",
                                                        MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a5ac4335-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("INTERFACE_ATT_SHOWVISIBILITY",
                                                              MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("3581d1bf-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("a5adc9a9-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("INTERFACE_OP_TEXTCOLOR",
                                                         MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("a5adc9ac-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("INTERFACE_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("a5adc9af-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("INTERFACE_OP_SHOWSIGNATURE",
                                                             MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a5adc9b2-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("INTERFACE_OP_SHOWSTEREOTYPES",
                                                               MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a5adc9b5-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("INTERFACE_OP_SHOWTAGS",
                                                        MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("a5adc9b8-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("INTERFACE_OP_SHOWVISIBILITY",
                                                              MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("35835843-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends StaticAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("a5adc9bb-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("INTERFACE_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("a5adc9be-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("INTERFACE_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a5adc9c1-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("INTERFACE_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a5adc9c4-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("INTERFACE_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a5adc9c7-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("INTERFACE_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("a5adc9ca-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("INTERFACE_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("35835867-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends StaticAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("a5adc9cd-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("INTERFACE_INNER_TEXTCOLOR",
                                                         MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a5adc9d0-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("INTERFACE_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Name display mode
         */
        @objid ("a5af504b-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWNAME = createStyleKey("INTERFACE_INNER_SHOWNAME",
                                                        MetaKey.InnerGroup.INNERSHOWNAME);

        /**
         * Stereotype display mode.
         */
        @objid ("a5af504e-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("INTERFACE_INNER_SHOWSTEREOTYPES",
                                                               MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a5af5051-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("INTERFACE_INNER_SHOWTAGS",
                                                        MetaKey.InnerGroup.INNERSHOWTAGS);

        /**
         * Display visibility
         */
        @objid ("a5af5054-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("INTERFACE_INNER_SHOWVISIBILITY",
                                                              MetaKey.InnerGroup.INNERSHOWVISIBILITY);

    }

}

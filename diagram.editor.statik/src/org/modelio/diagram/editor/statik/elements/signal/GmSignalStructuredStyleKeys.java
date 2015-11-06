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
                                    

package org.modelio.diagram.editor.statik.elements.signal;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmSendSignalAction when its representation mode is
 * RepresentationMode.STRUCTURED
 */
@objid ("369600bf-55b7-11e2-877f-002564c97630")
public class GmSignalStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    @objid ("a53676c9-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("SIGNAL_REPMODE", MetaKey.REPMODE);

    @objid ("a53676cb-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("SIGNAL_FILLCOLOR", MetaKey.FILLCOLOR);

    @objid ("a53676cd-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("SIGNAL_FILLMODE", MetaKey.FILLMODE);

    @objid ("a5369dda-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("SIGNAL_LINECOLOR", MetaKey.LINECOLOR);

    @objid ("a5369ddc-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("SIGNAL_LINEWIDTH", MetaKey.LINEWIDTH);

    @objid ("a5369dde-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("SIGNAL_FONT", MetaKey.FONT);

    @objid ("a536c4ea-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("SIGNAL_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a536c4ec-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("SIGNAL_SHOWNAME", MetaKey.SHOWNAME);

    @objid ("a536ebfa-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("SIGNAL_SHOWSTEREOTYPES", MetaKey.SHOWSTEREOTYPES);

    @objid ("a5371309-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("SIGNAL_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display class visibility.
     */
    @objid ("a537130b-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWVISIBILITY = createStyleKey("SIGNAL_SHOWVISIBILITY", MetaKey.SHOWVISIBILITY);

    /**
     * Display attributes.
     */
    @objid ("a5373a19-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("SIGNAL_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("a5373a1c-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("SIGNAL_OP_GROUPVISIBLE",
                                                                 MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("a5376129-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("SIGNAL_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("a537612c-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("SIGNAL_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    @objid ("a537883b-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("SIGNAL_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     * 
     * @author cmarin
     */
    @objid ("3697876a-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static final class Attribute extends StaticAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("a537d65a-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("SIGNAL_ATT_TEXTCOLOR",
                                                         MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("a537d65d-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("SIGNAL_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("a537fd69-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("SIGNAL_ATT_SHOWSTEREOTYPES",
                                                               MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("a537fd6c-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("SIGNAL_ATT_SHOWTAGS", MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("a5382479-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("SIGNAL_ATT_SHOWVISIBILITY",
                                                              MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Operations style keys.
     * 
     * @author cmarin
     */
    @objid ("36978787-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends StaticAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("a5384b8b-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("SIGNAL_OP_TEXTCOLOR",
                                                         MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("a538729b-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("SIGNAL_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("a53899a9-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("SIGNAL_OP_SHOWSIGNATURE",
                                                             MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("a53899ac-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("SIGNAL_OP_SHOWSTEREOTYPES",
                                                               MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("a538c0b9-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("SIGNAL_OP_SHOWTAGS",
                                                        MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("a538c0bc-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("SIGNAL_OP_SHOWVISIBILITY",
                                                              MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("36990e0b-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends StaticAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("a5390edb-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("SIGNAL_INNER_TEXTCOLOR",
                                                         MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a53935e9-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("SIGNAL_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a53935ec-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("SIGNAL_INNER_SHOWSTEREOTYPES",
                                                               MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a5395cf9-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("SIGNAL_INNER_SHOWTAGS",
                                                        MetaKey.InnerGroup.INNERSHOWTAGS);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("36990e23-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends StaticAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("a539ab1a-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("SIGNAL_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("a539ab1d-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("SIGNAL_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("a539d229-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("SIGNAL_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("a539d22c-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("SIGNAL_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("a539f939-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("SIGNAL_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("a539f93c-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("SIGNAL_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

}

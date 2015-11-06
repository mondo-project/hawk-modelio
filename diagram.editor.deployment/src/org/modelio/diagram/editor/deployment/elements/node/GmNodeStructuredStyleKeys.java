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
                                    

package org.modelio.diagram.editor.deployment.elements.node;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.deployment.style.DeploymentAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmNode when its representation mode is RepresentationMode.STRUCTURED
 */
@objid ("97420a19-55b6-11e2-877f-002564c97630")
public class GmNodeStructuredStyleKeys extends DeploymentAbstractStyleKeyProvider {
    /**
     * Representation mode.
     */
    @objid ("1cd60ccb-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("NODE_REPMODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("1cd60cce-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("NODE_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("1cd60cd1-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("NODE_FILLMODE", MetaKey.FILLMODE);

    /**
     * Line color.
     */
    @objid ("1cd60cd4-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("NODE_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Line width.
     */
    @objid ("1cd7934b-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("NODE_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("1cd7934e-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("NODE_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("1cd79351-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("NODE_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Display name.
     */
    @objid ("1cd79354-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("NODE_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Display stereotypes.
     */
    @objid ("1cd79357-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("NODE_SHOWSTEREOTYPES", MetaKey.SHOWSTEREOTYPES);

    /**
     * Display tagged values.
     */
    @objid ("1cd7935a-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("NODE_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display attributes.
     */
    @objid ("1cd7935d-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("NODE_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("1cd79360-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("NODE_OP_GROUPVISIBLE",
                                                                 MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("1cd79363-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("NODE_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("1cd79366-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("NODE_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    @objid ("1cd79369-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("NODE_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     */
    @objid ("974390c9-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Attribute extends DeploymentAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("1cd919e9-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("NODE_ATT_TEXTCOLOR", MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("1cd919ec-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("NODE_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("1cd919ef-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("NODE_ATT_SHOWSTEREOTYPES",
                                                               MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("1cd919f2-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("NODE_ATT_SHOWTAGS", MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("1cd919f5-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("NODE_ATT_SHOWVISIBILITY",
                                                              MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("97451748-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends DeploymentAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("1cd919fa-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("NODE_INNER_TEXTCOLOR",
                                                         MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("1cd919fd-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("NODE_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("1cd91a00-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("NODE_INNER_SHOWSTEREOTYPES",
                                                               MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("1cd91a03-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("NODE_INNER_SHOWTAGS",
                                                        MetaKey.InnerGroup.INNERSHOWTAGS);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("97451760-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends DeploymentAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("1cd91a08-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("NODE_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("1cd91a0b-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("NODE_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("1cd91a0e-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("NODE_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("1cd91a11-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("NODE_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("1cd91a14-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("NODE_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("1cd91a17-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("NODE_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

    /**
     * Operations style keys.
     */
    @objid ("97469de3-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends DeploymentAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("1cdaa08b-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("NODE_OP_TEXTCOLOR",
                                                         MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("1cdaa08e-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("NODE_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("1cdaa091-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("NODE_OP_SHOWSIGNATURE",
                                                             MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("1cdaa094-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("NODE_OP_SHOWSTEREOTYPES",
                                                               MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("1cdaa097-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("NODE_OP_SHOWTAGS", MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("1cdaa09a-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("NODE_OP_SHOWVISIBILITY",
                                                              MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

}

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
                                    

package org.modelio.diagram.editor.deployment.elements.artifact;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.deployment.style.DeploymentAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmArtifact when its representation mode is
 * RepresentationMode.STRUCTURED
 */
@objid ("971ef1bd-55b6-11e2-877f-002564c97630")
public class GmArtifactStructuredStyleKeys extends DeploymentAbstractStyleKeyProvider {
    /**
     * Representation mode.
     */
    @objid ("1ce0bb1b-55c2-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("ARTIFACT_REPMODE", MetaKey.REPMODE);

    /**
     * Fill color.
     */
    @objid ("1ce0bb1e-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("ARTIFACT_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("1ce0bb21-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("ARTIFACT_FILLMODE", MetaKey.FILLMODE);

    /**
     * Line color.
     */
    @objid ("1ce0bb24-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("ARTIFACT_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Line width.
     */
    @objid ("1ce0bb27-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("ARTIFACT_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("1ce0bb2a-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("ARTIFACT_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("1ce0bb2d-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("ARTIFACT_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Display name.
     */
    @objid ("1ce0bb30-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = createStyleKey("ARTIFACT_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Display stereotypes.
     */
    @objid ("1ce0bb33-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("ARTIFACT_SHOWSTEREOTYPES",
                                                           MetaKey.SHOWSTEREOTYPES);

    /**
     * Display tagged values.
     */
    @objid ("1ce0bb36-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("ARTIFACT_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Display attributes.
     */
    @objid ("1ce0bb39-55c2-11e2-9337-002564c97630")
     static final StyleKey ATTGROUPVISIBLE = createStyleKey("ARTIFACT_ATT_GROUPVISIBLE",
                                                           MetaKey.AttGroup.ATTSHOWGROUP);

    /**
     * Display operations.
     */
    @objid ("1ce241a9-55c2-11e2-9337-002564c97630")
     static final StyleKey OPERATIONGROUPVISIBLE = createStyleKey("ARTIFACT_OP_GROUPVISIBLE",
                                                                 MetaKey.OperationGroup.OPSHOWGROUP);

    /**
     * Filter on features
     */
    @objid ("1ce241ac-55c2-11e2-9337-002564c97630")
     static final StyleKey FEATURES = createStyleKey("ARTIFACT_FEATURES", MetaKey.VISIBILITYFILTER);

    /**
     * Inner classes visualization mode: labels, diagram or none.
     */
    @objid ("1ce241af-55c2-11e2-9337-002564c97630")
     static final StyleKey INNERVIEWMODE = createStyleKey("ARTIFACT_INNERVIEWMODE",
                                                         MetaKey.InnerGroup.INNERVIEWMODE);

    @objid ("1ce241b2-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWPORTS = createStyleKey("ARTIFACT_SHOWPORTS", MetaKey.AUTOSHOWPORTS);

    /**
     * Attributes style keys.
     */
    @objid ("9720786a-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Attribute extends DeploymentAbstractStyleKeyProvider {
        /**
         * Attributes text color.
         */
        @objid ("1ce241b6-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ARTIFACT_ATT_TEXTCOLOR",
                                                         MetaKey.AttGroup.ATTTEXTCOLOR);

        /**
         * Attributes font.
         */
        @objid ("1ce241b9-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ARTIFACT_ATT_FONT", MetaKey.AttGroup.ATTFONT);

        /**
         * Attributes stereotype display mode.
         */
        @objid ("1ce241bc-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ARTIFACT_ATT_SHOWSTEREOTYPES",
                                                               MetaKey.AttGroup.ATTSHOWSTEREOTYPES);

        /**
         * Display attributes tagged values.
         */
        @objid ("1ce241bf-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ARTIFACT_ATT_SHOWTAGS", MetaKey.AttGroup.ATTSHOWTAGS);

        /**
         * Display attributes visibilities.
         */
        @objid ("1ce241c2-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("ARTIFACT_ATT_SHOWVISIBILITY",
                                                              MetaKey.AttGroup.ATTSHOWVISIBILITY);

    }

    /**
     * Style keys for all inner classifiers group members.
     * 
     * @author cmarin
     */
    @objid ("9721feeb-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Inner extends DeploymentAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("1ce241c7-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ARTIFACT_INNER_TEXTCOLOR",
                                                         MetaKey.InnerGroup.INNERTEXTCOLOR);

        /**
         * Font
         */
        @objid ("1ce241ca-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ARTIFACT_INNER_FONT", MetaKey.InnerGroup.INNERFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("1ce241cd-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ARTIFACT_INNER_SHOWSTEREOTYPES",
                                                               MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("1ce241d0-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ARTIFACT_INNER_SHOWTAGS",
                                                        MetaKey.InnerGroup.INNERSHOWTAGS);

    }

    /**
     * Style keys for all internal structure group members.
     * <p>
     * INTERNALSVIEWMODE and INTERNALS are used for internal structure zone too.
     * 
     * @author cmarin
     */
    @objid ("9721ff03-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class InternalStructure extends DeploymentAbstractStyleKeyProvider {
        /**
         * Internal structure visualization mode: labels, diagram or none.
         */
        @objid ("1ce3c84b-55c2-11e2-9337-002564c97630")
         static final StyleKey INTERNALSVIEWMODE = createStyleKey("ARTIFACT_INTERNALSVIEWMODE",
                                                                 MetaKey.InternalGroup.INTVIEWMODE);

        /**
         * Text color.
         */
        @objid ("1ce3c84e-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ARTIFACT_INTERNAL_TEXTCOLOR",
                                                         MetaKey.InternalGroup.INTTEXTCOLOR);

        /**
         * Font
         */
        @objid ("1ce3c851-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ARTIFACT_INTERNAL_FONT", MetaKey.InternalGroup.INTFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("1ce3c854-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ARTIFACT_INTERNAL_SHOWSTEREOTYPES",
                                                               MetaKey.InternalGroup.INTSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("1ce3c857-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ARTIFACT_INTERNAL_SHOWTAGS",
                                                        MetaKey.InternalGroup.INTSHOWTAGS);

        /**
         * Auto unmask internal structure content. Boolean type.
         */
        @objid ("1ce3c85a-55c2-11e2-9337-002564c97630")
         static final StyleKey AUTOUNMASK = createStyleKey("ARTIFACT_INTERNAL_AUTOUNMASK",
                                                          MetaKey.InternalGroup.INTAUTOUNMASK);

    }

    /**
     * Operations style keys.
     */
    @objid ("97238587-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("hiding")
    public static class Operation extends DeploymentAbstractStyleKeyProvider {
        /**
         * Operations text color.
         */
        @objid ("1ce3c85f-55c2-11e2-9337-002564c97630")
         static final StyleKey TEXTCOLOR = createStyleKey("ARTIFACT_OP_TEXTCOLOR",
                                                         MetaKey.OperationGroup.OPTEXTCOLOR);

        /**
         * Operations text font.
         */
        @objid ("1ce3c862-55c2-11e2-9337-002564c97630")
         static final StyleKey FONT = createStyleKey("ARTIFACT_OP_FONT", MetaKey.OperationGroup.OPFONT);

        /**
         * Display operations signature.
         */
        @objid ("1ce3c865-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSIGNATURE = createStyleKey("ARTIFACT_OP_SHOWSIGNATURE",
                                                             MetaKey.OperationGroup.OPSHOWSIGNATURE);

        /**
         * Operations stereotype display mode.
         */
        @objid ("1ce3c868-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWSTEREOTYPES = createStyleKey("ARTIFACT_OP_SHOWSTEREOTYPES",
                                                               MetaKey.OperationGroup.OPSHOWSTEREOTYPES);

        /**
         * Display operations tagged values.
         */
        @objid ("1ce3c86b-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWTAGS = createStyleKey("ARTIFACT_OP_SHOWTAGS",
                                                        MetaKey.OperationGroup.OPSHOWTAGS);

        /**
         * Display operations visibilities.
         */
        @objid ("1ce3c86e-55c2-11e2-9337-002564c97630")
         static final StyleKey SHOWVISIBILITY = createStyleKey("ARTIFACT_OP_SHOWVISIBILITY",
                                                              MetaKey.OperationGroup.OPSHOWVISIBILITY);

    }

}

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
                                    

package org.modelio.diagram.editor.statik.elements.substitution;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependencyStyleKeys;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link Substitution}.
 * 
 * @author cma
 */
@objid ("a048d8a4-469b-4b3d-b861-1f0b8f3904ea")
public class GmSubstitution extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("6d994206-0b0c-4dc3-a9bc-1eaa30d88720")
    private final int minorVersion = 0;

    @objid ("bb7a4f8c-36e1-4298-b58d-e3dedfd5d7ef")
    private static final int MAJOR_VERSION = 0;

    @objid ("922b2920-b7da-423c-bd53-93a543a9c006")
    private Substitution dependency;

    /**
     * Style keys
     */
    @objid ("4c4998cd-04aa-4990-a42e-09ac81880da9")
    public static final GmDependencyStyleKeys styleKeyProvider = new GmDependencyStyleKeys("SUBSTITUTION");

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param dependency The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("f3920b0f-0598-4ebd-bfad-5ec060710c95")
    public GmSubstitution(GmAbstractDiagram diagram, Substitution dependency, MRef ref) {
        super(diagram, ref);
        this.dependency = dependency;
        
        final GmSubstitutionHeader header = new GmSubstitutionHeader(diagram, ref);
        header.setShowMetaclassKeyword(true);
        addExtension(ExtensionLocation.MiddleNW, header);
    }

    @objid ("ddc63ea1-34b7-4593-87fa-e6e7ab0c166d")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("61fc4604-b9d6-4a18-a1ff-f0a792e8c215")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    /**
     * For deserialization only.
     */
    @objid ("c9ded327-4bc4-47aa-b6a2-48f0e0286aa5")
    public GmSubstitution() {
    }

    @objid ("791eeb60-71ef-4c52-b361-f91e47638963")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.dependency = (Substitution) resolveRef(this.getRepresentedRef());
    }

    @objid ("e75eb3cd-ef1d-4afb-995d-a6023f7ce7f4")
    @Override
    public MObject getFromElement() {
        return this.dependency.getSubstitutingClassifier();
    }

    @objid ("826e4c3f-d5e0-480c-9d67-78cb7a1a7bb2")
    @Override
    public MObject getToElement() {
        return this.dependency.getContract();
    }

    @objid ("a5c28cfe-baf7-424a-8990-2265b325e3ae")
    @Override
    public MObject getRepresentedElement() {
        return this.dependency;
    }

    @objid ("e58c78d0-4527-4e05-96f0-567612277a07")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("8fba69ab-d5a2-4d53-96a1-2b2723dc2997")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmSubstitution." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("2372d3be-6ba0-41d1-a44f-6d4ffcd3dc22")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}

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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams.view;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.editor.BpmnDiagramEditorInput;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.umlcommon.diagramview.GmAbstractDiagramView;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link IStaticDiagram} view in the diagram.
 */
@objid ("62057fc1-55b6-11e2-877f-002564c97630")
public class GmBpmnDiagramView extends GmAbstractDiagramView {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("62057fcb-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("62057fce-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("62057fc5-55b6-11e2-877f-002564c97630")
     static final BpmnDiagramViewStructuredStyleKeys STRUCTURED_KEYS = new BpmnDiagramViewStructuredStyleKeys();

    @objid ("62057fc7-55b6-11e2-877f-002564c97630")
    private static final BpmnDiagramViewSimpleStyleKeys SIMPLE_KEYS = new BpmnDiagramViewSimpleStyleKeys();

    @objid ("62057fc9-55b6-11e2-877f-002564c97630")
    private static final BpmnDiagramViewImageStyleKeys IMAGE_KEYS = new BpmnDiagramViewImageStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("62057fd0-55b6-11e2-877f-002564c97630")
    public GmBpmnDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("62057fd3-55b6-11e2-877f-002564c97630")
    public GmBpmnDiagramView(final GmAbstractDiagram diagram, final AbstractDiagram el, final MRef ref) {
        super(diagram, el, ref);
    }

    @objid ("62057fe2-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("62057fe9-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(final MetaKey metakey) {
        switch (getRepresentationMode()) {
            case IMAGE:
                return IMAGE_KEYS.getStyleKey(metakey);
            case SIMPLE:
                return SIMPLE_KEYS.getStyleKey(metakey);
            case STRUCTURED:
                return STRUCTURED_KEYS.getStyleKey(metakey);
            default:
                return null;
        }
    }

    @objid ("62070659-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        switch (getRepresentationMode()) {
            case IMAGE:
                return IMAGE_KEYS.getStyleKeys();
            case SIMPLE:
                return SIMPLE_KEYS.getStyleKeys();
            case STRUCTURED:
                return STRUCTURED_KEYS.getStyleKeys();
            default:
                return Collections.emptyList();
        }
    }

    @objid ("62070661-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram loadViewedDiagram() {
        BpmnDiagramEditorInput input = new BpmnDiagramEditorInput(getDiagram().getModelManager(), getRepresentedElement());
        return input.getGmDiagram();
    }

    @objid ("62070668-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnDiagramView." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
            case 0: {
                read_0(in);
                break;
            }
            default: {
                assert (false) : "version number not covered!";
                // reading as last handled version: 0
                read_0(in);
                break;
            }
        }
    }

    @objid ("6207066e-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("62070674-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("62070679-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}

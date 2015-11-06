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
                                    

package org.modelio.diagram.editor.sequence.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.sequence.editor.SequenceDiagramEditor;
import org.modelio.diagram.editor.tools.PanSelectionTool;

@objid ("d9a3c0b8-55b6-11e2-877f-002564c97630")
public class SequenceDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("d9a3c0cb-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return SequenceDiagramEditor.ID;
    }

    @objid ("d9a3c0d0-55b6-11e2-877f-002564c97630")
    @Override
    public PaletteRoot initPalette(final AbstractDiagramEditor diagram, final ToolRegistry toolRegistry) {
        PaletteRoot paletteRoot = new PaletteRoot();
        
        PaletteGroup group = new PaletteGroup("main");
        
        SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
        selectionToolEntry.setToolClass(PanSelectionTool.class);
        paletteRoot.setDefaultEntry(selectionToolEntry);
        group.add(selectionToolEntry);
        
        MarqueeToolEntry entry = new MarqueeToolEntry();
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(entry);
        
        paletteRoot.add(group);
        
        paletteRoot.add(createNodeGroup(toolRegistry));
        paletteRoot.add(createLinkGroup(toolRegistry));
        paletteRoot.add(createInformationFlowGroup(toolRegistry));
        paletteRoot.add(createCommonGroup(toolRegistry));
        paletteRoot.add(createDrawGroup(toolRegistry));
        return paletteRoot;
    }

    @objid ("d9a3c0dd-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(final ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorSequence.I18N.getMessage("SequencePaletteGroup.Common"),
                                                null);
        
        group.add(toolRegistry.getTool("CREATE_NOTE"));
        group.add(toolRegistry.getTool("CREATE_CONSTRAINT"));
        group.add(toolRegistry.getTool("CREATE_EXTERNDOCUMENT"));
        group.add(toolRegistry.getTool("CREATE_DEPENDENCY"));
        group.add(toolRegistry.getTool("CREATE_TRACEABILITY"));
        group.add(toolRegistry.getTool("CREATE_RELATED_DIAGRAM_LINK"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("d9a5473c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createLinkGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer p2 = new PaletteDrawer(DiagramEditorSequence.I18N.getMessage("SequencePaletteGroup.Links"));
        
        p2.add(toolRegistry.getTool("CREATE_SYNCHRONOUSMESSAGE"));
        p2.add(toolRegistry.getTool("CREATE_SYNCHRONOUSMESSAGE_INNEREXECUTION"));
        p2.add(toolRegistry.getTool("CREATE_CREATIONMESSAGE"));
        p2.add(toolRegistry.getTool("CREATE_DESTRUCTIONMESSAGE"));
        p2.add(toolRegistry.getTool("CREATE_ASYNCHRONOUSMESSAGE"));
        p2.add(toolRegistry.getTool("CREATE_ASYNCHRONOUSMESSAGE_INNEREXECUTION"));
        p2.add(toolRegistry.getTool("CREATE_REPLY"));
        return p2;
    }

    @objid ("d9a54744-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createNodeGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer p = new PaletteDrawer(DiagramEditorSequence.I18N.getMessage("SequencePaletteGroup.Nodes"));
        
        p.add(toolRegistry.getTool("CREATE_LIFELINE"));
        p.add(toolRegistry.getTool("CREATE_PARTDECOMPOSITION"));
        p.add(toolRegistry.getTool("CREATE_EXECUTIONSPECIFICATION"));
        p.add(toolRegistry.getTool("CREATE_INTERACTIONUSE"));
        p.add(toolRegistry.getTool("CREATE_GATE"));
        p.add(toolRegistry.getTool("CREATE_COMBINEDFRAGMENT"));
        p.add(toolRegistry.getTool("CREATE_INTERACTIONOPERAND"));
        p.add(toolRegistry.getTool("CREATE_STATEINVARIANT"));
        return p;
    }

    @objid ("d9a5474c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorSequence.I18N.getMessage("SequencePaletteGroup.InformationFlow"),
                                                      null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

}

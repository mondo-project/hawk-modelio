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
                                    

package org.modelio.diagram.editor.communication.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.communication.plugin.DiagramEditorCommunication;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Configures the communication diagram palette.
 */
@objid ("7a1a5ada-55b6-11e2-877f-002564c97630")
public class CommunicationDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("7a1a5aed-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return CommunicationDiagramEditor.ID;
    }

    /**
     * Creates the note and constraint and dependency group.
     * @param imageService
     * service used to get metaclasses bitmaps.
     * @return The created group.
     */
    @objid ("7a1a5af2-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorCommunication.I18N.getMessage("CommunicationPaletteGroup.Common"),
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

    /**
     * Create the link group, containing tools to create CommunicationChannels.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("7a1a5afa-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createLinkGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorCommunication.I18N.getMessage("CommunicationPaletteGroup.Links"),
                                                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_COMMUNICATIONCHANNEL"));
        return group;
    }

    /**
     * Create the node group, containing tools to create CommunicationNodes and CommunicationMessages.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("7a1a5b02-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createNodeGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorCommunication.I18N.getMessage("CommunicationPaletteGroup.Nodes"),
                                                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_COMMUNICATIONNODE"));
        group.add(toolRegistry.getTool("CREATE_COMMUNICATIONMESSAGE"));
        return group;
    }

    @objid ("7a1be17f-55b6-11e2-877f-002564c97630")
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

    @objid ("7a1be18b-55b6-11e2-877f-002564c97630")
    private PaletteEntry createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorCommunication.I18N.getMessage("CommunicationPaletteGroup.InformationFlow"),
                                                      null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

}

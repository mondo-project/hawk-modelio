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
                                    

package org.modelio.diagram.editor.composite.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.composite.plugin.DiagramEditorComposite;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Composite diagram palette configurer.
 */
@objid ("8089b19a-55b6-11e2-877f-002564c97630")
public class CompositeDiagramConfigurer extends AbstractDiagramConfigurer {
    /**
     * Creates the note and constraint and dependency group.
     * service used to get metaclasses bitmaps.
     * @return The created group.
     */
    @objid ("8089b19c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorComposite.I18N.getMessage("CompositePaletteGroup.Common"),
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
     * Create the link group, containing tools to create Links.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("8089b1a4-55b6-11e2-877f-002564c97630")
    private PaletteEntry createLinkGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorComposite.I18N.getMessage("CompositePaletteGroup.Links"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_INSTANCELINK"));
        group.add(toolRegistry.getTool("CREATE_USE"));
        group.add(toolRegistry.getTool("CREATE_BINDING"));
        return group;
    }

    /**
     * Create the node group, containing tools to create Actors, UseCases and ExtensionPoints.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("8089b1ac-55b6-11e2-877f-002564c97630")
    private PaletteEntry createNodeGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorComposite.I18N.getMessage("CompositePaletteGroup.Nodes"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_BINDABLEINSTANCE"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTELINK"));
        group.add(toolRegistry.getTool("CREATE_COLLABORATIONUSE"));
        return group;
    }

    @objid ("8089b1c1-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorComposite.I18N.getMessage("CompositePaletteGroup.InformationFlow"),
                null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONITEM"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("27b884c0-a915-4484-b728-2d5d08bd20e1")
    @Override
    public String getContributionURI() {
        return CompositeDiagramEditor.ID;
    }

    @objid ("a9d72740-c75b-402d-a73a-a97385e2e5b7")
    @Override
    public PaletteRoot initPalette(AbstractDiagramEditor diagram, ToolRegistry toolRegistry) {
        PaletteRoot paletteRoot = new PaletteRoot();
        
        // Main group
        PaletteGroup group = new PaletteGroup("main");
        
        SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
        selectionToolEntry.setToolClass(PanSelectionTool.class);
        paletteRoot.setDefaultEntry(selectionToolEntry);
        group.add(selectionToolEntry);
        
        MarqueeToolEntry entry = new MarqueeToolEntry();
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(entry);
        
        paletteRoot.add(group);
        
        // Other groups
        paletteRoot.add(createNodeGroup(toolRegistry));
        paletteRoot.add(createLinkGroup(toolRegistry));
        paletteRoot.add(createInformationFlowGroup(toolRegistry));
        paletteRoot.add(createCommonGroup(toolRegistry));
        paletteRoot.add(createDrawGroup(toolRegistry));
        return paletteRoot;
    }

}

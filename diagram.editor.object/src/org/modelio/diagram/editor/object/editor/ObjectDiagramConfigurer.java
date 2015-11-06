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
                                    

package org.modelio.diagram.editor.object.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.object.plugin.DiagramEditorObject;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.IDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Object diagram palette configurer.
 */
@objid ("807cb94c-55b6-11e2-877f-002564c97630")
public class ObjectDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("9d5b9fb1-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return ObjectDiagramEditor.ID;
    }

    /**
     * Creates the note and constraint and dependency group.
     * @param imageService
     * service used to get metaclasses bitmaps.
     * @return The created group.
     */
    @objid ("9d5d263c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorObject.I18N.getMessage("ObjectPaletteGroup.Common"),
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
    @objid ("9d5d2644-55b6-11e2-877f-002564c97630")
    private PaletteEntry createLinkGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorObject.I18N.getMessage("ObjectPaletteGroup.Links"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_INSTANCELINK"));
        group.add(toolRegistry.getTool("CREATE_NARY_INSTANCELINK"));
        return group;
    }

    /**
     * Create the node group, containing tools to create Actors, UseCases and ExtensionPoints.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("9d5d264c-55b6-11e2-877f-002564c97630")
    private PaletteEntry createNodeGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorObject.I18N.getMessage("ObjectPaletteGroup.Nodes"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_INSTANCE"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTELINK"));
        return group;
    }

    @objid ("9d5d2654-55b6-11e2-877f-002564c97630")
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

    @objid ("9d5d2661-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorObject.I18N.getMessage("ObjectPaletteGroup.InformationFlow"),
                null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONITEM"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

}

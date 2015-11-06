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
                                    

package org.modelio.diagram.editor.deployment.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.deployment.plugin.DiagramEditorDeployment;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.IDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Configures the deployment diagram palette.
 */
@objid ("970ca21a-55b6-11e2-877f-002564c97630")
public class DeploymentDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("970ca22d-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return DeploymentDiagramEditor.ID;
    }

    @objid ("970ca232-55b6-11e2-877f-002564c97630")
    @Override
    public PaletteRoot initPalette(final AbstractDiagramEditor diagram, final ToolRegistry toolRegistry) {
        PaletteRoot paletteRoot = new PaletteRoot();
        
        PaletteGroup group = new PaletteGroup("main");
        
        SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
        selectionToolEntry.setToolClass(PanSelectionTool.class);
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

    /**
     * Creates the note and constraint and dependency group.
     * @param imageService
     * service used to get metaclasses bitmaps.
     * @return The created group.
     */
    @objid ("970ca23f-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorDeployment.I18N.getMessage("DeploymentPaletteGroup.Common"),
                null);
        
        group.add(toolRegistry.getTool("CREATE_NOTE"));
        group.add(toolRegistry.getTool("CREATE_CONSTRAINT"));
        group.add(toolRegistry.getTool("CREATE_EXTERNDOCUMENT"));
        group.add(toolRegistry.getTool("CREATE_DEPENDENCY"));
        group.add(toolRegistry.getTool("CREATE_TRACEABILITY"));
        group.add(toolRegistry.getTool("CREATE_RELATED_DIAGRAM_LINK"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    /**
     * Create the link group, containing tools to create Links.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("970e28bd-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createLinkGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorDeployment.I18N.getMessage("DeploymentPaletteGroup.Links"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_GENERALIZATION"));
        group.add(toolRegistry.getTool("CREATE_ASSOCIATION"));
        group.add(toolRegistry.getTool("CREATE_AGGREGATION"));
        group.add(toolRegistry.getTool("CREATE_COMPOSITION"));
        group.add(toolRegistry.getTool("CREATE_USE"));
        group.add(toolRegistry.getTool("CREATE_MANIFESTATION"));
        group.add(toolRegistry.getTool("CREATE_INSTANCELINK"));
        group.add(toolRegistry.getTool("CREATE_NARY_INSTANCELINK"));
        group.add(toolRegistry.getTool("CREATE_PROVIDED_INTERFACE"));
        group.add(toolRegistry.getTool("CREATE_REQUIRED_INTERFACE"));
        group.add(toolRegistry.getTool("CREATE_DELEGATELINK"));
        group.add(toolRegistry.getTool("CREATE_TEMPLATEBINDING"));
        group.add(toolRegistry.getTool("CREATE_PACKAGEIMPORT"));
        return group;
    }

    /**
     * Create the node group, containing tools to create Actors, UseCases and ExtensionPoints.
     * @param toolRegistry The tool registry.
     * @return The group containing all the tool.
     */
    @objid ("970e28c5-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createNodeGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorDeployment.I18N.getMessage("DeploymentPaletteGroup.Nodes"),
                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_PACKAGE"));
        group.add(toolRegistry.getTool("CREATE_NODE"));
        group.add(toolRegistry.getTool("CREATE_ARTIFACT"));
        group.add(toolRegistry.getTool("CREATE_OPERATION"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTE"));
        group.add(toolRegistry.getTool("CREATE_PORT"));
        group.add(toolRegistry.getTool("CREATE_INSTANCE"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTELINK"));
        return group;
    }

    @objid ("970e28cd-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorDeployment.I18N.getMessage("DeploymentPaletteGroup.InformationFlow"),
                null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONITEM"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

}

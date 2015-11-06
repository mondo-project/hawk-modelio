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
                                    

package org.modelio.diagram.editor.usecase.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;
import org.modelio.diagram.editor.usecase.editor.UseCaseDiagramEditor;

/**
 * Use case diagrams palette configurer.
 */
@objid ("5e2eb8cb-55b7-11e2-877f-002564c97630")
public class UseCaseDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("5e303f7f-55b7-11e2-877f-002564c97630")
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

    @objid ("5e303f67-55b7-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorUseCase.I18N.getMessage("UseCasePaletteGroup.Common"),
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

    @objid ("5e303f6f-55b7-11e2-877f-002564c97630")
    private PaletteEntry createLinkGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorUseCase.I18N.getMessage("UseCasePaletteGroup.Links"),
                                                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_COMMUNICATIONLINK"));
        group.add(toolRegistry.getTool("CREATE_USECASEDEPENDENCYINCLUDE"));
        group.add(toolRegistry.getTool("CREATE_USECASEDEPENDENCYEXTEND"));
        group.add(toolRegistry.getTool("CREATE_GENERALIZATION"));
        return group;
    }

    @objid ("5e303f77-55b7-11e2-877f-002564c97630")
    private PaletteEntry createNodeGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorUseCase.I18N.getMessage("UseCasePaletteGroup.Nodes"),
                                                null);
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        
        group.add(toolRegistry.getTool("CREATE_ACTOR"));
        group.add(toolRegistry.getTool("CREATE_ACTORPRIMARY"));
        group.add(toolRegistry.getTool("CREATE_ACTORSECONDARY"));
        group.add(toolRegistry.getTool("CREATE_USECASE"));
        group.add(toolRegistry.getTool("CREATE_EXTENSIONPOINT"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTE"));
        group.add(toolRegistry.getTool("CREATE_OPERATION"));
        return group;
    }

    @objid ("5e303f8c-55b7-11e2-877f-002564c97630")
    private PaletteDrawer createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorUseCase.I18N.getMessage("UseCasePaletteGroup.InformationFlow"),
                                                      null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONITEM"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("7c19dea7-5eff-11e2-b9cc-001ec947c8cc")
    @Override
    public String getContributionURI() {
        return UseCaseDiagramEditor.ID;
    }

}

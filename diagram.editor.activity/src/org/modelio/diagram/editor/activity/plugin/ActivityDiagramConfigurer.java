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
                                    

package org.modelio.diagram.editor.activity.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.activity.editor.ActivityDiagramEditor;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Activity diagram palette configurer.
 */
@objid ("2b72db5a-55b6-11e2-877f-002564c97630")
public class ActivityDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("2b72db6d-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return ActivityDiagramEditor.ID;
    }

    @objid ("2b72db72-55b6-11e2-877f-002564c97630")
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
        
        paletteRoot.add(this.createControlGroup(toolRegistry));
        paletteRoot.add(this.createEventGroup(toolRegistry));
        paletteRoot.add(this.createDataGroup(toolRegistry));
        paletteRoot.add(this.createFlowGroup(toolRegistry));
        paletteRoot.add(this.createPartitionGroup(toolRegistry));
        paletteRoot.add(this.createInformationFlowGroup(toolRegistry));
        paletteRoot.add(this.createCommonGroup(toolRegistry));
        paletteRoot.add(createDrawGroup(toolRegistry));
        return paletteRoot;
    }

    @objid ("2b72db7f-55b6-11e2-877f-002564c97630")
    private PaletteEntry createPartitionGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Partition"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_ACTIVITYPARTITION_VERTICAL_CONTAINER"));
        group.add(toolRegistry.getTool("CREATE_ACTIVITYPARTITION_HORIZONTAL_CONTAINER"));
        group.add(toolRegistry.getTool("CREATE_ACTIVITYPARTITION_SIBLING"));
        group.add(toolRegistry.getTool("CREATE_ACTIVITYPARTITION_INNER"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    /**
     * Creates the note and constraint and dependency group.
     * @return The created group.
     */
    @objid ("2b72db87-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(final ToolRegistry toolRegistry) {
        // common group
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Common"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_NOTE"));
        group.add(toolRegistry.getTool("CREATE_CONSTRAINT"));
        group.add(toolRegistry.getTool("CREATE_EXTERNDOCUMENT"));
        group.add(toolRegistry.getTool("CREATE_DEPENDENCY"));
        group.add(toolRegistry.getTool("CREATE_TRACEABILITY"));
        group.add(toolRegistry.getTool("CREATE_RELATED_DIAGRAM_LINK"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("2b72db90-55b6-11e2-877f-002564c97630")
    private PaletteEntry createEventGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Event"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_SENDSIGNALACTION"));
        group.add(toolRegistry.getTool("CREATE_ACCEPTCALLEVENTACTION"));
        group.add(toolRegistry.getTool("CREATE_ACCEPTCHANGEEVENTACTION"));
        group.add(toolRegistry.getTool("CREATE_ACCEPTSIGNALACTION"));
        group.add(toolRegistry.getTool("CREATE_ACCEPTTIMEEVENTACTION"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("2b7461fa-55b6-11e2-877f-002564c97630")
    private PaletteEntry createFlowGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Flow"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_SMARTFLOW"));
        group.add(toolRegistry.getTool("CREATE_CONTROLFLOW"));
        group.add(toolRegistry.getTool("CREATE_OBJECTFLOW"));
        group.add(toolRegistry.getTool("CREATE_EXCEPTIONHANDLER"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("2b746202-55b6-11e2-877f-002564c97630")
    private PaletteEntry createDataGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Data"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_INSTANCENODE"));
        group.add(toolRegistry.getTool("CREATE_DATASTORENODE"));
        group.add(toolRegistry.getTool("CREATE_CENTRALBUFFERNODE"));
        group.add(toolRegistry.getTool("CREATE_ACTIVITYPARAMETERNODE"));
        group.add(toolRegistry.getTool("CREATE_INPUTPIN"));
        group.add(toolRegistry.getTool("CREATE_OUTPUTPIN"));
        group.add(toolRegistry.getTool("CREATE_VALUEPIN"));
        group.add(toolRegistry.getTool("CREATE_EXPANSIONNODE_INPUT"));
        group.add(toolRegistry.getTool("CREATE_EXPANSIONNODE_OUTPUT"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("2b74620a-55b6-11e2-877f-002564c97630")
    private PaletteEntry createControlGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.Control"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_OPAQUEACTION"));
        group.add(toolRegistry.getTool("CREATE_CALLBEHAVIORACTION"));
        group.add(toolRegistry.getTool("CREATE_CALLOPERATIONACTION"));
        
        group.add(toolRegistry.getTool("CREATE_CONDITIONALNODE"));
        group.add(toolRegistry.getTool("CREATE_CLAUSE"));
        group.add(toolRegistry.getTool("CREATE_LOOPNODE"));
        group.add(toolRegistry.getTool("CREATE_STRUCTUREDNODE"));
        group.add(toolRegistry.getTool("CREATE_EXPANSIONREGION"));
        
        group.add(toolRegistry.getTool("CREATE_INITIALNODE"));
        group.add(toolRegistry.getTool("CREATE_ACTIVITYFINALNODE"));
        group.add(toolRegistry.getTool("CREATE_FLOWFINALNODE"));
        group.add(toolRegistry.getTool("CREATE_FORKJOINNODE"));
        group.add(toolRegistry.getTool("CREATE_DECISIONMERGENODE"));
        group.add(toolRegistry.getTool("CREATE_INTERRUPTIBLEACTIVITYREGION"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("2b746212-55b6-11e2-877f-002564c97630")
    private PaletteEntry createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorActivity.I18N.getMessage("ActivityPaletteGroup.InformationFlow"), null);
        group.setLargeIcon(null);
        group.setSmallIcon(null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

}

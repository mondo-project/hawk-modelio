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
                                    

package org.modelio.diagram.editor.bpmn.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.diagram.editor.plugin.AbstractDiagramConfigurer;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Configures the BPMN diagram palette.
 */
@objid ("622a1ed4-55b6-11e2-877f-002564c97630")
public class BpmnDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("622ba56a-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return BpmnDiagramEditor.ID;
    }

    /**
     * Creates the note and constraint and dependency group.
     * @return The created group.
     */
    @objid ("622ba56f-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createCommonGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Common"),
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

    @objid ("622ba574-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createEventGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Event"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_BPMNSTARTEVENT"));
        group.add(toolRegistry.getTool("CREATE_BPMNINTERMEDIATETHROWEVENT"));
        group.add(toolRegistry.getTool("CREATE_BPMNINTERMEDIATECATCHEVENT"));
        group.add(toolRegistry.getTool("CREATE_BPMNENDEVENT"));
        group.add(toolRegistry.getTool("CREATE_BPMNBOUNDARYEVENT"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba578-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createFlowGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Flow"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_BPMNSEQUENCEFLOW"));
        group.add(toolRegistry.getTool("CREATE_BPMNMESSAGEFLOW"));
        group.add(toolRegistry.getTool("CREATE_BPMNDATAASSOCIATION"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba57c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createGatewayGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Gateway"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_BPMNEXCLUSIVEGATEWAY"));
        group.add(toolRegistry.getTool("CREATE_BPMNINCLUSIVEGATEWAY"));
        group.add(toolRegistry.getTool("CREATE_BPMNCOMPLEXGATEWAY"));
        group.add(toolRegistry.getTool("CREATE_BPMNPARALLELGATEWAY"));
        group.add(toolRegistry.getTool("CREATE_BPMNEVENTBASEDGATEWAY"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba580-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createObjectGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Object"), null);
        
        group.add(toolRegistry.getTool("CREATE_BPMNDATAINPUT"));
        group.add(toolRegistry.getTool("CREATE_BPMNDATAOUTPUT"));
        group.add(toolRegistry.getTool("CREATE_BPMNDATAOBJECT"));
        group.add(toolRegistry.getTool("CREATE_BPMNDATASTORE"));
        group.add(toolRegistry.getTool("CREATE_BPMNMESSAGE"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba584-55b6-11e2-877f-002564c97630")
    private PaletteEntry createPartitionGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Partition"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_PARTICIPANT"));
        group.add(toolRegistry.getTool("CREATE_LANE"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba588-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createSubProcessGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.SubProcess"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_SUBPROCESS"));
        group.add(toolRegistry.getTool("CREATE_ADHOCSUBPROCESS"));
        group.add(toolRegistry.getTool("CREATE_TRANSACTION"));
        group.add(toolRegistry.getTool("CREATE_CALLACTIVITY"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("622ba58c-55b6-11e2-877f-002564c97630")
    private PaletteDrawer createTaskGroup(ToolRegistry toolRegistry) {
        PaletteDrawer group = new PaletteDrawer(DiagramEditorBpmn.I18N.getMessage("BPMNPaletteGroup.Task"),
                                                null);
        group.add(toolRegistry.getTool("CREATE_BPMNTASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNSENDTASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNRECEIVETASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNSERVICETASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNUSERTASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNMANUALTASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNSCRIPTTASK"));
        group.add(toolRegistry.getTool("CREATE_BPMNBUSINESSRULETASK"));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("622ba590-55b6-11e2-877f-002564c97630")
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
        
        paletteRoot.add(this.createTaskGroup(toolRegistry));
        paletteRoot.add(this.createSubProcessGroup(toolRegistry));
        paletteRoot.add(this.createEventGroup(toolRegistry));
        paletteRoot.add(this.createGatewayGroup(toolRegistry));
        paletteRoot.add(this.createPartitionGroup(toolRegistry));
        paletteRoot.add(this.createObjectGroup(toolRegistry));
        paletteRoot.add(this.createFlowGroup(toolRegistry));
        paletteRoot.add(this.createCommonGroup(toolRegistry));
        paletteRoot.add(this.createDrawGroup(toolRegistry));
        return paletteRoot;
    }

}

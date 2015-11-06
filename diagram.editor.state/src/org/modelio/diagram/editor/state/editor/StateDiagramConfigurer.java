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
                                    

package org.modelio.diagram.editor.state.editor;

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
import org.modelio.diagram.editor.state.plugin.DiagramEditorState;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Configure a State diagram editor.
 */
@objid ("f4eb651a-55b6-11e2-877f-002564c97630")
public class StateDiagramConfigurer extends AbstractDiagramConfigurer {
//    @objid ("f4eb651e-55b6-11e2-877f-002564c97630")
//    @Override
//    public DiagramEditorInput createDiagramEditorInput(final CoreSession session, final IAbstractDiagram target) {
//        return new StateDiagramEditorInput(session, target);
//    }
    @objid ("f4eb652d-55b6-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return StateDiagramEditor.ID;
    }

    @objid ("f4eb6532-55b6-11e2-877f-002564c97630")
    private PaletteEntry createCommonGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer commonGroup = new PaletteDrawer(DiagramEditorState.I18N.getMessage("StatePaletteGroup.Common"),
                                                      null);
        
        commonGroup.add(toolRegistry.getTool("CREATE_NOTE"));
        commonGroup.add(toolRegistry.getTool("CREATE_CONSTRAINT"));
        commonGroup.add(toolRegistry.getTool("CREATE_EXTERNDOCUMENT"));
        commonGroup.add(toolRegistry.getTool("CREATE_DEPENDENCY"));
        commonGroup.add(toolRegistry.getTool("CREATE_TRACEABILITY"));
        commonGroup.add(toolRegistry.getTool("CREATE_RELATED_DIAGRAM_LINK"));
        
        commonGroup.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return commonGroup;
    }

    @objid ("f4eb653a-55b6-11e2-877f-002564c97630")
    private PaletteEntry createStateGroup(final ToolRegistry toolRegistry) {
        PaletteDrawer commonGroup = new PaletteDrawer(DiagramEditorState.I18N.getMessage("StatePaletteGroup.State"),
                                                      null);
        
        commonGroup.add(toolRegistry.getTool("CREATE_STATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_REGION"));
        commonGroup.add(toolRegistry.getTool("CREATE_INTERNALTRANSITION"));
        commonGroup.add(toolRegistry.getTool("CREATE_TRANSITION"));
        commonGroup.add(toolRegistry.getTool("CREATE_INITIALPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_TERMINATEPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_ENTRYPOINTPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_EXITPOINTPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_FORKPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_JOINPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_JUNCTIONPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_CHOICEPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_DEEPHISTORYPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_SHALLOWHISTORYPSEUDOSTATE"));
        commonGroup.add(toolRegistry.getTool("CREATE_CONNECTIONPOINTREFERENCE"));
        commonGroup.add(toolRegistry.getTool("CREATE_FINALSTATE"));
        return commonGroup;
    }

    @objid ("f4eb6542-55b6-11e2-877f-002564c97630")
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
        
        paletteRoot.add(this.createStateGroup(toolRegistry));
        paletteRoot.add(this.createCommonGroup(toolRegistry));
        paletteRoot.add(createDrawGroup(toolRegistry));
        return paletteRoot;
    }

}

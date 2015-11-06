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
                                    

package org.modelio.diagram.editor.statik.plugin;

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
import org.modelio.diagram.editor.statik.editor.StaticDiagramEditor;
import org.modelio.diagram.editor.tools.PanSelectionTool;

/**
 * Configures the diagram palette.
 */
@objid ("36ecfa5a-55b7-11e2-877f-002564c97630")
public class ClassDiagramConfigurer extends AbstractDiagramConfigurer {
    @objid ("36ecfa6d-55b7-11e2-877f-002564c97630")
    @Override
    public String getContributionURI() {
        return StaticDiagramEditor.ID;
    }

    @objid ("36ecfa72-55b7-11e2-877f-002564c97630")
    @Override
    public PaletteRoot initPalette(final AbstractDiagramEditor diagram, final ToolRegistry toolRegistry) {
        PaletteRoot paletteRoot = new PaletteRoot();
        paletteRoot = new PaletteRoot();
        
        PaletteGroup group = new PaletteGroup("main");
        SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
        selectionToolEntry.setToolClass(PanSelectionTool.class);
        paletteRoot.setDefaultEntry(selectionToolEntry);
        group.add(selectionToolEntry);
        
        MarqueeToolEntry entry = new MarqueeToolEntry();
        entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        group.add(entry);
        
        paletteRoot.add(group);
        
        paletteRoot.add(this.createClassGroup(toolRegistry));
        paletteRoot.add(this.createComponentGroup(toolRegistry));
        paletteRoot.add(this.createInstanceGroup(toolRegistry));
        paletteRoot.add(this.createImportGroup(toolRegistry));
        paletteRoot.add(this.createInformationFlowGroup(toolRegistry));
        paletteRoot.add(this.createCommonGroup(toolRegistry));
        paletteRoot.add(this.createDrawGroup(toolRegistry));
        return paletteRoot;
    }

    @objid ("36ecfa7f-55b7-11e2-877f-002564c97630")
    private PaletteEntry createCommonGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.Common"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_NOTE"));
        group.add(toolRegistry.getTool("CREATE_CONSTRAINT"));
        group.add(toolRegistry.getTool("CREATE_EXTERNDOCUMENT"));
        group.add(toolRegistry.getTool("CREATE_DEPENDENCY"));
        group.add(toolRegistry.getTool("CREATE_TRACEABILITY"));
        
        group.add(toolRegistry.getTool("CREATE_ABSTRACTION"));
        group.add(toolRegistry.getTool("CREATE_ELEMENTREALIZATION"));
        group.add(toolRegistry.getTool("CREATE_SUBSTITUTION"));
        
        group.add(toolRegistry.getTool("CREATE_RELATED_DIAGRAM_LINK"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("36ecfa87-55b7-11e2-877f-002564c97630")
    private PaletteEntry createInstanceGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.Instance"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_INSTANCE"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTELINK"));
        group.add(toolRegistry.getTool("CREATE_COLLABORATIONUSE"));
        group.add(toolRegistry.getTool("CREATE_BINDING"));
        group.add(toolRegistry.getTool("CREATE_DELEGATELINK"));
        group.add(toolRegistry.getTool("CREATE_INSTANCELINK"));
        group.add(toolRegistry.getTool("CREATE_NARY_INSTANCELINK"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("36ecfa8f-55b7-11e2-877f-002564c97630")
    private PaletteEntry createImportGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.Import"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_USE"));
        group.add(toolRegistry.getTool("CREATE_ELEMENTIMPORT"));
        group.add(toolRegistry.getTool("CREATE_PACKAGEIMPORT"));
        group.add(toolRegistry.getTool("CREATE_PACKAGEMERGE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("36ee8100-55b7-11e2-877f-002564c97630")
    private PaletteEntry createInformationFlowGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.InformationFlow"),
                                                      null);
        
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOW"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONFLOWNODE"));
        group.add(toolRegistry.getTool("CREATE_INFORMATIONITEM"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("36ee8108-55b7-11e2-877f-002564c97630")
    private PaletteEntry createComponentGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.Component"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_COMPONENT"));
        group.add(toolRegistry.getTool("CREATE_PORT"));
        group.add(toolRegistry.getTool("CREATE_PROVIDED_INTERFACE"));
        group.add(toolRegistry.getTool("CREATE_REQUIRED_INTERFACE"));
        group.add(toolRegistry.getTool("CREATE_COMPONENTREALIZATION"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
        return group;
    }

    @objid ("36ee8110-55b7-11e2-877f-002564c97630")
    private PaletteEntry createClassGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditorStatik.I18N.getMessage("StatikPaletteGroup.Class"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_INTERFACE"));
        group.add(toolRegistry.getTool("CREATE_CLASS"));
        group.add(toolRegistry.getTool("CREATE_ATTRIBUTE"));
        group.add(toolRegistry.getTool("CREATE_OPERATION"));
        group.add(toolRegistry.getTool("CREATE_RAISEDEXCEPTION"));
        group.add(toolRegistry.getTool("CREATE_ASSOCIATION"));
        group.add(toolRegistry.getTool("CREATE_AGGREGATION"));
        group.add(toolRegistry.getTool("CREATE_COMPOSITION"));
        group.add(toolRegistry.getTool("CREATE_NARY_ASSOCIATION"));
        group.add(toolRegistry.getTool("CREATE_SMARTGENERALIZATION"));
        group.add(toolRegistry.getTool("CREATE_GENERALIZATION"));
        group.add(toolRegistry.getTool("CREATE_INTERFACEREALIZATION"));
        group.add(toolRegistry.getTool("CREATE_DATATYPE"));
        group.add(toolRegistry.getTool("CREATE_ENUMERATION"));
        group.add(toolRegistry.getTool("CREATE_ENUMERATIONLITERAL"));
        group.add(toolRegistry.getTool("CREATE_SIGNAL"));
        group.add(toolRegistry.getTool("CREATE_CLASSASSOCIATION"));
        group.add(toolRegistry.getTool("CREATE_TEMPLATEPARAMETER"));
        group.add(toolRegistry.getTool("CREATE_TEMPLATEBINDING"));
        group.add(toolRegistry.getTool("CREATE_COLLABORATION"));
        group.add(toolRegistry.getTool("CREATE_PACKAGE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

}

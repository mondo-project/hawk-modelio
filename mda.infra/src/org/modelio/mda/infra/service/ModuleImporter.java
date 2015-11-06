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
                                    

package org.modelio.mda.infra.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.commands.IModuleAction;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.gproject.data.module.JaxbModelPersistence;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Command;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Commands;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2ContextualMenu.Jxbv2CommandRef;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2ContextualMenu;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams.Jxbv2DiagramType.Jxbv2Palette.Jxbv2ToolRef;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams.Jxbv2DiagramType.Jxbv2Palette;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams.Jxbv2DiagramType.Jxbv2Wizard;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams.Jxbv2DiagramType;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Tools;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Views.Jxbv2PropertyPage;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Views;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Tool;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.mda.infra.service.dynamic.DynamicGuiCreationHelper;
import org.modelio.mda.infra.service.dynamic.GenericDiagramCustomizer.PaletteCommand;
import org.modelio.mda.infra.service.dynamic.GenericDiagramCustomizer;

/**
 * Service class that reads the module.xml file and deserialize its content (basically: commands and diagrams customization).
 * 
 * @author aab
 */
@objid ("1a5e07c3-018d-11e2-9fca-001ec947c8cc")
public class ModuleImporter {
    @objid ("1a5e07cb-018d-11e2-9fca-001ec947c8cc")
    public static void loadDynamicModel(final Path dynamicModelPath, final IModule module, final IMModelServices mModelServices) throws IOException {
        final DynamicGuiCreationHelper mdafactory = new DynamicGuiCreationHelper(module.getConfiguration().getModuleResourcesPath()
                .toFile());
        
        // Load xml File into model (JAXB), using the ModuleLoader from core.project plugin
        final Jxbv2Module jaxbModule = JaxbModelPersistence.loadJaxbModel(dynamicModelPath);
        
        class LoadingRunnable implements Runnable {
            IOException error;
        
            @Override
            public void run() {
                this.error = null;
                Map<String, IModuleAction> commandCache = new HashMap<>();
        
                try {
                    Jxbv2Gui gui = jaxbModule.getGui();
                    if (gui == null)
                        return;
        
                    // Jxbv2Commands
                    final Jxbv2Commands commands = gui.getCommands();
                    if (commands != null) {
                        for (Jxbv2Command cmd : commands.getCommand()) {
                            // standard command, described by a handler class.
                            IModuleAction action = mdafactory.createCommand(cmd, module, mModelServices);
                            commandCache.put(cmd.getId(), action);
                        }
                    }
        
                    // Contextual menu contribtuions
                    Jxbv2ContextualMenu menu = gui.getContextualMenu();
                    if (menu != null) {
                        for (Jxbv2CommandRef ref : menu.getCommandRef()) {
                            IModuleAction action = commandCache.get(ref.getRefid());
                            mdafactory.registerContextualMenuCommand(module, action);
                        }
                    } 
        
                    // Jxbv2PropertyPage
                    final Jxbv2Views views = gui.getViews();
                    if (views != null) {
                        for (Jxbv2PropertyPage pp : views.getPropertyPage()) {
                            IModulePropertyPage propertyPage = mdafactory.createPropertyPage(pp, module);
                            propertyPage.setModule(module);
                            module.getPropertyPages().add(propertyPage);
        
                            // FIXME Design flaw: all commands will appear in all property pages !
                            for (org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Views.Jxbv2PropertyPage.Jxbv2CommandRef ref : pp
                                    .getCommandRef()) {
                                IModuleAction action = commandCache.get(ref.getRefid());
                                mdafactory.registerPropertyPageCommand(module, action);
                            }
                        }
                    }
        
                    // Jxbv2Tools
                    final Jxbv2Tools tools = gui.getTools();
                    if (tools != null) {
                        for (Jxbv2Tool tool : tools.getTool()) {
                            switch (tool.getHandler().getClazz()) {
                            case "Box":
                                mdafactory.registerDiagramBoxCommand(tool, module);
                                break;
                            case "Link":
                                mdafactory.registerDiagramLinkCommand(tool, module);
                                break;
                            case "MultiLink":
                                mdafactory.registerDiagramMultiLinkCommand(tool, module);
                                break;
                            case "AttachedBox":
                                mdafactory.registerDiagramAttachedBoxCommand(tool, module);
                                break;
                            default:
                                mdafactory.registerCustomCommand(tool, module);
                            }
                        }
                    }
        
                    // Diagram Types
                    final Jxbv2Diagrams diagrams = gui.getDiagrams();
                    if (diagrams != null) {
                        for (Jxbv2DiagramType diagram : diagrams.getDiagramType()) {
        
                            GenericDiagramCustomizer customizer = mdafactory.createDiagram(module);
        
                            // Jxbv2Palette tool contributions
                            Jxbv2Palette palette = diagram.getPalette();
                            if (palette != null) {
                                // Keep palette
                                customizer.setKeepPalette(palette.isKeepBasePalette());
        
                                // Jxbv2Commands
                                for (Jxbv2ToolRef toolref : palette.getToolRef()) {
                                    String group = toolref.getGroup();
                                    PaletteCommand paletteCommand = new PaletteCommand(toolref.getRefid(), group);
                                    customizer.registerPaletteCommand(paletteCommand);
        
                                }
                            }
        
                            // Register the diagram
                            mdafactory.registerDiagram(customizer, module, diagram.getBaseDiagram(), diagram.getStereotype());
        
                            // Jxbv2Wizard contribution
                            final Jxbv2Wizard wizard = diagram.getWizard();
                            if (wizard != null) {
                                mdafactory.registerWizard(wizard, module, mModelServices);
                            }
                        }
                    }
                } catch (IOException e) {
                    // Store the error to throw it later.
                    this.error = e;
                }
            }
        
        }
        
        final LoadingRunnable runnable = new LoadingRunnable();
        Display.getDefault().syncExec(runnable);
        if (runnable.error != null) {
            throw runnable.error;
        }
    }

}

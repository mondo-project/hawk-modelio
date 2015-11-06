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
                                    

package org.modelio.diagram.editor.context;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.modelio.api.module.IModule;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.module.browser.commands.ModulePopupManager;

/**
 * ModuleMenuCreator manage Modules commands in diagrams.
 */
@objid ("866308eb-cd0a-41c0-9f60-3b85c3521218")
public class ModuleMenuCreator {
    @objid ("d9cc77ee-1ad5-4803-856d-edb2dc82da37")
    private static ModuleMenuCreator INSTANCE = null;

// FIXME we should not have a module list in here, but somehow access the module registry...
    @objid ("90cf454a-7e8c-4e62-b67e-6086b408c21c")
    private List<IModule> startedModules = new ArrayList<>();

    @objid ("06cb60ea-1266-4a08-9419-0cbe7778aeb1")
    private MPart part;

    @objid ("49c3ea50-aba8-4cd2-ae49-55935abe7f57")
    @Execute
    void execute() {
        // Store a static instance to avoid garbage, and allow module start/stop to be called...
        INSTANCE = this;
    }

    @objid ("17c60090-9bf3-48be-84eb-aed84ac68a4c")
    @Inject
    @Optional
    public void onModuleStarted(@EventTopic(ModelioEventTopics.MODULE_STARTED) final IModule module) {
        // Make sure there is no old module with the same ID, to avoid duplicated popups...
        for (IModule oldModule : new ArrayList<>(INSTANCE.startedModules)) {
            if (oldModule.getName().equals(module.getName())) {
                INSTANCE.startedModules.remove(oldModule);
            }
        }
        INSTANCE.startedModules.add(module);
    }

    @objid ("e5178f26-5d48-4e4c-a440-6a8ab748e938")
    @Inject
    @Optional
    public void onModuleStopped(@EventTopic(ModelioEventTopics.MODULE_STOPPED) final IModule module) {
        INSTANCE.startedModules.remove(module);
    }

    @objid ("9c33aa14-3923-45da-bd11-f38f5209dc56")
    public static void setPart(MPart part) {
        INSTANCE.part = part;
    }

    @objid ("30faa94b-6790-4958-8ff1-8289849aa87c")
    @AboutToShow
    public void aboutToShow(List<MMenuElement> items) {
        if (INSTANCE.part != null) {
            for (IModule module : INSTANCE.startedModules) {
                MMenuElement item = ModulePopupManager.createMenu(module, INSTANCE.part);
                if (item != null) {
                    items.add(item);
                }
            }
        }
    }

    @objid ("5316cecb-313c-4f34-99ef-ab42d3ecf5c2")
    @AboutToHide
    public void aboutToHide(List<MMenuElement> items) {
        if (INSTANCE.part != null) {
            // Cleanup menu items
            if (items != null) {                
                for (MMenuElement item : items) {
                    item.getParent().getChildren().remove(item);
                }
            }
        
            // Cleanup commands & handlers
            for (IModule module : INSTANCE.startedModules) {
                ModulePopupManager.removeMenu(module, INSTANCE.part);
            }
        }
    }

}

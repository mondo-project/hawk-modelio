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
                                    

package org.modelio.model.browser.context;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.modelio.api.module.IModule;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.module.browser.commands.ModulePopupManager;

/**
 * ModuleCommandsForModelBrowser manage Modules commands
 */
@objid ("a25f391f-8de0-4bf9-a51c-a00065365b03")
class ModuleCommandsForModelBrowser {
    @objid ("881f7637-0a74-4dee-acaa-041e3a9f9548")
    private static ModuleCommandsForModelBrowser INSTANCE = null;

/*
     * Warning: the part id is also defined with the processor, in the plugin.xml file.
     */
    @objid ("fba298cb-0239-4900-83c5-66b6baa841a6")
    @Inject
    @Named("org.modelio.browser.part")
    protected static MPart browserView;

// FIXME we should not have a module list in here, but somehow access the module registry...
    @objid ("04561d81-e7bf-4292-9418-b15a2b01313f")
    private List<IModule> startedModules = new ArrayList<>();

    @objid ("176a38bc-c344-490b-a29a-baa6280479a6")
    @Execute
    void execute() {
        // Create instance and put it in the context.
        //context.set(ModuleCommandsForModelBrowser.class, ContextInjectionFactory.make(ModuleCommandsForModelBrowser.class, context));
        INSTANCE = this;
    }

    @objid ("0ce3c88c-fdc9-48ec-80ab-691cc2e77a85")
    @Inject
    @Optional
    static void onModuleStarted(@EventTopic(ModelioEventTopics.MODULE_STARTED) final IModule module) {
        // Make sure there is no old module with the same ID, to avoid duplicated popups...
        for (IModule oldModule : new ArrayList<>(INSTANCE.startedModules)) {
            if (oldModule.getName().equals(module.getName())) {
                INSTANCE.startedModules.remove(oldModule);
            }
        }
        INSTANCE.startedModules.add(module);
    }

    @objid ("e95f5ced-f00c-4df3-9e78-645fa4f8a7c6")
    @Inject
    @Optional
    static void onModuleStopped(@EventTopic(ModelioEventTopics.MODULE_STOPPED) final IModule module) {
        INSTANCE.startedModules.remove(module);
    }

    @objid ("931730a5-d01c-4aeb-b1d4-a706c4e71a43")
    @AboutToShow
    public static void aboutToShow(List<MMenuElement> items) {
        if (browserView != null) {
            for (IModule module : INSTANCE.startedModules) {
                MMenuElement item = ModulePopupManager.createMenu(module, browserView);
                if (item != null) {
                    items.add(item);
                }
            }
        }
    }

    @objid ("263dc59b-6d6c-411e-bcd0-8c0b81e2fbb4")
    @AboutToHide
    public static void aboutToHide(List<MMenuElement> items) {
        if (browserView != null && items != null) {
            // Cleanup menu items
            for (MMenuElement item : items) {
                final MElementContainer<MUIElement> parentItem = item.getParent();
                if (parentItem != null) // parentItem may be null
                    parentItem.getChildren().remove(item);
            }
        
            // Cleanip commands & handlers
            for (IModule module : INSTANCE.startedModules) {
                ModulePopupManager.removeMenu(module, browserView);
            }
        }
    }

}

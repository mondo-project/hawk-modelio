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

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.osgi.framework.Bundle;

@objid ("0053e972-7a19-1006-9c1d-001ec947cd2a")
public class ElementCreationDynamicMenuManager {
    @objid ("008b0286-7a2b-1006-9c1d-001ec947cd2a")
    public static final String MENUID = "org.modelio.model.browser.menu.createelement";

    @objid ("008a07fa-7a2b-1006-9c1d-001ec947cd2a")
    private static final String CONTRIBUTOR_ID = "platform:/plugin/org.modelio.model.browser";

    @objid ("0054108c-7a19-1006-9c1d-001ec947cd2a")
    private static HashMap<String, HashSet<String>> validCases = new HashMap<>();

    @objid ("00545da8-7a19-1006-9c1d-001ec947cd2a")
    @Inject
    private static MApplication application;

    @objid ("0054822e-7a19-1006-9c1d-001ec947cd2a")
    private static HashMap<String, MCommand> commandCache = new HashMap<>();

    @objid ("0054ae34-7a19-1006-9c1d-001ec947cd2a")
    @Inject
    @Named("org.modelio.model.browser.menu.createelement")
    private static MMenu menu;

    @objid ("005440b6-7a19-1006-9c1d-001ec947cd2a")
    public static void configure(MMenu menuToConfigure, MObject obj) {
        // The injected menu might not be the same, use this one instead...
        
        String sourceMetaclass = obj.getMClass().getName();
        
        HashSet<String> validCommands = validCases.get(sourceMetaclass);
        if (validCommands == null) {
            for (MMenuElement item : menuToConfigure.getChildren()) {
                item.setVisible(false);
            }
            return;
        }
        
        for (MMenuElement item : menuToConfigure.getChildren()) {
            item.setVisible(validCommands.contains(item.getElementId()));
        }
    }

    @objid ("0054c720-7a19-1006-9c1d-001ec947cd2a")
    @Inject
    public void execute() {
        Loader loader = new Loader();
        
        Bundle bundle = ModelBrowser.getInstance().getBundle();
        
        IPath explorerContextualMenu = new Path("/res/create-popups.xml");
        URL url = FileLocator.find(bundle, explorerContextualMenu, null);
        loader.loadXML(url);
        
        // free some ressources
        commandCache.clear();
        commandCache = null;
    }

    @objid ("0054dfe4-7a19-1006-9c1d-001ec947cd2a")
    private static MCommand getCommand(String commandId) {
        MCommand command = commandCache.get(commandId);
        if (command == null) {
            for (MCommand c : application.getCommands()) {
                if (commandId.equals(c.getElementId())) {
                    command = c;
                    commandCache.put(commandId, command);
                    break;
                }
            }
        }
        return command;
    }

    @objid ("005507ee-7a19-1006-9c1d-001ec947cd2a")
    public static void register(EntryDescriptor entryDescriptor) {
        String sourceMetaclass = entryDescriptor.sourceMetaclass;
        String dependency = entryDescriptor.parameters.getProperty("dependency", "");
        String targetMetaclass = entryDescriptor.parameters.getProperty("metaclass", "");
        String targetStereotype = entryDescriptor.parameters.getProperty("stereotype", "");
        String i18nKey = entryDescriptor.parameters.getProperty("i18nKey", "");
        
        // create a new item
        MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem();
        MCommand command = getCommand(entryDescriptor.commandId);
        item.setCommand(command);
        
        item.setElementId(sourceMetaclass + dependency + targetMetaclass + targetStereotype);
        
        if (i18nKey.isEmpty()) {
            item.setLabel(ModelBrowser.I18N.getString(item.getElementId() + ".label"));
            item.setTooltip(ModelBrowser.I18N.getString(item.getElementId() + ".tooltip"));
        
            String baseIcon = ModelBrowser.I18N.getString(item.getElementId() + ".icon");
            item.setIconURI("platform:/plugin/org.modelio.core.ui/icons/mmimages/" + baseIcon + ".png");
        } else {
            item.setLabel(ModelBrowser.I18N.getString(i18nKey + ".label"));
            item.setTooltip(ModelBrowser.I18N.getString(i18nKey + ".tooltip"));
        
            String baseIcon = ModelBrowser.I18N.getString(i18nKey + ".icon");
            item.setIconURI("platform:/plugin/org.modelio.core.ui/icons/mmimages/" + baseIcon + ".png");
        }
        
        item.setEnabled(true);
        item.setToBeRendered(true);
        item.setVisible(false);
        
        item.setContributorURI(CONTRIBUTOR_ID);
        
        for (Entry<Object, Object> param : entryDescriptor.parameters.entrySet()) {
            MParameter p = MCommandsFactory.INSTANCE.createParameter();
            p.setName((String) param.getKey());
            p.setValue((String) param.getValue());
            item.getParameters().add(p);
        }
        
        menu.getChildren().add(item);
        
        registerValidCase(sourceMetaclass, item.getElementId());
    }

    @objid ("00551be4-7a19-1006-9c1d-001ec947cd2a")
    private static void registerValidCase(String sourceMetaclass, String contributionId) {
        if (!validCases.containsKey(sourceMetaclass)) {
            validCases.put(sourceMetaclass, new HashSet<String>());
        }
        
        validCases.get(sourceMetaclass).add(contributionId);
    }

    @objid ("987e9920-f1e4-4675-a169-28e7f6e038ae")
    public static void registerSeparator(String sourceMetaclass) {
        // create a new item
        MMenuSeparator item = MMenuFactory.INSTANCE.createMenuSeparator();
        
        // Build a unique ID for the separator...
        item.setElementId(sourceMetaclass + ".separator." + menu.getChildren().size());
        
        item.setToBeRendered(true);
        item.setVisible(true);
        
        item.setContributorURI(CONTRIBUTOR_ID);
        
        menu.getChildren().add(item);
        
        registerValidCase(sourceMetaclass, item.getElementId());
    }

}

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
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.model.browser.plugin.ModelBrowser;

/**
 * ModuleCommandsForModelBrowser manage Modules commands
 */
@objid ("f4908570-99ce-4b58-a87f-0704ddcc921d")
class RelatedDiagramsForModelBrowser {
/*
     * Warning: the part id is also defined with the processor, in the plugin.xml file.
     */
    @objid ("33625b3e-41d4-4006-bd12-5bb0cef527b9")
    @Inject
    @Named("org.modelio.browser.part")
    protected static MPart browserView;

    @objid ("9d68520b-f4de-4fdb-ba4c-eb6d1cd513a0")
    @Inject
    private static MApplication application;

    @objid ("7f999b1d-0d23-4836-98ad-df44bd2a65ca")
    @AboutToShow
    public static void aboutToShow(List<MMenuElement> items) {
        if (browserView == null) {
            // No view injected... should never happen
            return;
        }
        
        // Get current selection
        IEclipseContext context = browserView.getContext();
        final IStructuredSelection selection = (IStructuredSelection) context.get(IServiceConstants.ACTIVE_SELECTION);
        if (selection == null || selection.size() != 1) {
            // No selection means no related diagrams
            return;
        }
        
        final Object first = selection.getFirstElement();
        if (! (first instanceof ModelElement)) {
            // Only model elements have dependencies
            return;
        }
        
        // Get diagrams to display
        final List<AbstractDiagram> relatedDiagrams = getRelatedDiagrams((ModelElement) first);
        if (relatedDiagrams.size() == 0) {
            // No diagrams to relate to
            return;
        }
        
        // Add a separator
        MMenuSeparator separator = MMenuFactory.INSTANCE.createMenuSeparator();
        items.add(separator);
        
        // Add the related diagrams menu
        items.add(createMenu(relatedDiagrams));
    }

    @objid ("d971407e-5adb-4643-be02-c04c7d964336")
    public static MMenu createMenu(final List<AbstractDiagram> relatedDiagrams) {
        final String contributorId = "platform:/plugin/" + ModelBrowser.getInstance().getBundle().getSymbolicName();
        
        // create a new menu
        MMenu elementCreationMenu = MMenuFactory.INSTANCE.createMenu();
        elementCreationMenu.setLabel(ModelBrowser.I18N.getString("RelatedDiagramsMenu.label"));
        elementCreationMenu.setIconURI(contributorId + "/icons/relateddiagram.png");
        
        // make the menu visible
        elementCreationMenu.setEnabled(true);
        elementCreationMenu.setToBeRendered(true);
        elementCreationMenu.setVisible(true);
        
        // bound the menu to the contributing plugin 
        elementCreationMenu.setContributorURI(contributorId);
        
        // add related diagram items
        elementCreationMenu.getChildren().addAll(createDiagramItems(relatedDiagrams, contributorId));
        return elementCreationMenu;
    }

    @objid ("83a6272c-bced-486d-8157-d1164557f54e")
    public static List<AbstractDiagram> getRelatedDiagrams(final ModelElement currentModelElement) {
        final List<AbstractDiagram> relatedDiagrams = new ArrayList<>();
        for (final Dependency dependency : currentModelElement.getDependsOnDependency()) {
            if (dependency.isStereotyped("ModelerModule", "related_diagram")) {
                final ModelElement relatedElement = dependency.getDependsOn();
                if (relatedElement instanceof AbstractDiagram && ! relatedDiagrams.contains(relatedElement)) {
                    relatedDiagrams.add((AbstractDiagram) relatedElement);
                }
            }
        }
        return relatedDiagrams;
    }

    @objid ("fa44b562-d4ee-4164-9ce4-498ba8cb9b87")
    private static List<MMenuElement> createDiagramItems(List<AbstractDiagram> relatedDiagrams, String contributorId) {
        List<MMenuElement> ret = new ArrayList<>();
        for (AbstractDiagram diagram : relatedDiagrams) {
            ret.add(createDiagramItems(diagram, contributorId));
        }
        return ret;
    }

    @objid ("e39e3bdc-4632-4181-bce9-f890af79b0f0")
    private static MMenuElement createDiagramItems(AbstractDiagram diagram, String contributorId) {
        // create a new handled item
        MHandledMenuItem relatedDiagramItem = MMenuFactory.INSTANCE.createHandledMenuItem();
        relatedDiagramItem.setLabel(diagram.getName());
        relatedDiagramItem.setIconURI("platform:/plugin/org.modelio.core.ui/icons/mmimages/" + diagram.getMClass().getName().toLowerCase() + ".png");
        
        // make the menu visible
        relatedDiagramItem.setEnabled(true);
        relatedDiagramItem.setToBeRendered(true);
        relatedDiagramItem.setVisible(true);
        
        // bound the menu to the contributing plugin 
        relatedDiagramItem.setContributorURI(contributorId);
        
        // set the command
        MCommand command = getCommand("org.modelio.app.ui.command.openrelateddiagram");
        relatedDiagramItem.setCommand(command);
        
        // add the opened diagram as parameter
        MParameter p = MCommandsFactory.INSTANCE.createParameter();
        p.setName("org.modelio.app.ui.command.parameter.related_diagram");
        p.setValue(diagram.getUuid().toString());
        relatedDiagramItem.getParameters().add(p);
        return relatedDiagramItem;
    }

    @objid ("3c52ba9d-ea45-4f66-89eb-869188128b09")
    private static MCommand getCommand(String commandId) {
        for (MCommand c : application.getCommands()) {
            if (commandId.equals(c.getElementId())) {
                return c;
            }
        }
        return null;
    }

}

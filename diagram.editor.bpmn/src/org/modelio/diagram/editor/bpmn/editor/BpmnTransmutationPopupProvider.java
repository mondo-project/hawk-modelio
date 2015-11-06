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

import java.net.URL;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.diagram.editor.context.AbstractCreationPopupProvider;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link AbstractCreationPopupProvider} for Bpmn diagram.
 */
@objid ("6d07de5a-39f0-4f44-8e24-d63e21718edc")
public class BpmnTransmutationPopupProvider extends AbstractCreationPopupProvider {
    @objid ("bd0b07df-9f44-4ef4-a177-56d060a37310")
    private BundledMessages I18N = new BundledMessages(DiagramEditorBpmn.LOG, ResourceBundle.getBundle("diagram-transmute-popups"));

    @objid ("d9d4d3f2-f7fe-4b23-8e60-f490b8fef10d")
    @Override
    protected Bundle getBundle() {
        return DiagramEditorBpmn.getContext().getBundle();
    }

    @objid ("19f5c05a-84a6-42ca-a1e8-8976035e44f4")
    @Override
    protected BundledMessages getI18nBundle() {
        return this.I18N;
    }

    @objid ("f8658b18-8de1-4fc4-a75b-09f95c74cc4b")
    @Override
    protected URL getCreatePopupXmlFile() {
        Bundle bundle = getBundle();
        IPath contextualMenuContent = new Path("/res/diagram-transmute-popups.xml");
        URL url = FileLocator.find(bundle, contextualMenuContent, null);
        return url;
    }

    @objid ("6715f0ce-b289-4d7b-a4f6-7e7e9f05e5fc")
    @Override
    protected String getMenuIconPath() {
        return null;
    }

    @objid ("c0079ff8-9493-4142-95a4-f50158b6caf8")
    @Override
    protected String getMenuLabel() {
        return DiagramEditorBpmn.I18N.getString("TransmuteElementMenu.label");
    }

}

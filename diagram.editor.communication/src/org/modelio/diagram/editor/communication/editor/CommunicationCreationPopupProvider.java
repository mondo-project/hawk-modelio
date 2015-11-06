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
                                    

package org.modelio.diagram.editor.communication.editor;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.communication.plugin.DiagramEditorCommunication;
import org.modelio.diagram.editor.context.AbstractCreationPopupProvider;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link AbstractCreationPopupProvider} for Communication diagram.
 */
@objid ("c5c4268b-e882-4bc0-89ec-a40a6fbd68ec")
public class CommunicationCreationPopupProvider extends AbstractCreationPopupProvider {
    @objid ("2cacae19-b776-49c0-b7a6-40dfa32d2a4a")
    private BundledMessages I18N = new BundledMessages(DiagramEditorCommunication.LOG, ResourceBundle.getBundle("diagram-create-popups"));

    @objid ("af33b0fa-c3f9-4eb4-ba3a-130534e19a70")
    @Override
    protected Bundle getBundle() {
        return DiagramEditorCommunication.getContext().getBundle();
    }

    @objid ("6db22ab6-77ff-4329-8b76-6224ae7c252c")
    @Override
    protected BundledMessages getI18nBundle() {
        return this.I18N;
    }

}

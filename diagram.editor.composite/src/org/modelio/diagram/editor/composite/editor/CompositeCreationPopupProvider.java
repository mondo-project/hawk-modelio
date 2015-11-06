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
                                    

package org.modelio.diagram.editor.composite.editor;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.composite.plugin.DiagramEditorComposite;
import org.modelio.diagram.editor.context.AbstractCreationPopupProvider;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link AbstractCreationPopupProvider} for Composite diagram.
 */
@objid ("2866ff4b-211f-4085-893c-1b2f70c4188e")
public class CompositeCreationPopupProvider extends AbstractCreationPopupProvider {
    @objid ("f753edc4-d5a6-47ad-8bc6-56c9bd11ea90")
    private BundledMessages I18N = new BundledMessages(DiagramEditorComposite.LOG, ResourceBundle.getBundle("diagram-create-popups"));

    @objid ("a93c3d24-0fbd-49c9-bc33-66e2fdf7ab34")
    @Override
    protected Bundle getBundle() {
        return DiagramEditorComposite.getContext().getBundle();
    }

    @objid ("0b6eca04-a7a5-4915-a6e4-31bbceac2595")
    @Override
    protected BundledMessages getI18nBundle() {
        return this.I18N;
    }

}

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
                                    

package org.modelio.diagram.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.modelio.diagram.editor.plugin.DiagramEditor;

/**
 * OSGI context function dispatching the {@link DiagramEditorInput} instantiation to all {@link IDiagramEditorInputProvider} registered in the extension point.
 * Fils all {@link DiagramEditorInput} injected references at runtime.
 */
@objid ("82a32f6b-5a57-11e2-9c97-002564c97630")
public class DiagramEditorInputProvider extends ContextFunction {
    @objid ("be73200b-5a77-11e2-9c97-002564c97630")
    private static final String INPUTPROVIDER_ID = "org.modelio.diagram.editor.inputprovider";

    @objid ("be77b3e9-5a77-11e2-9c97-002564c97630")
    public DiagramEditorInputProvider() {
        super();
    }

    @objid ("be77b3eb-5a77-11e2-9c97-002564c97630")
    @Override
    public Object compute(IEclipseContext context) {
        IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(INPUTPROVIDER_ID);
        for (IConfigurationElement e : config) {
            Object o;
            try {
                o = e.createExecutableExtension("class");
        
                if (o instanceof IDiagramEditorInputProvider) {
                    IDiagramEditorInputProvider provider = (IDiagramEditorInputProvider) o;
                    DiagramEditorInput input = provider.compute(context);
                    if (input != null) {
                        return input;
                    }
                }
            } catch (CoreException e1) {
                DiagramEditor.LOG.error(e1);
            }
        }
        return null;
    }

    @objid ("be793a8a-5a77-11e2-9c97-002564c97630")
    public interface IDiagramEditorInputProvider {
        @objid ("be793a8b-5a77-11e2-9c97-002564c97630")
        DiagramEditorInput compute(IEclipseContext context);

    }

}

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
                                    

package org.modelio.diagram.editor.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;

/**
 * Abstract diagram configurer that contains commonly used palette groups.
 * <p>
 * These palette groups can be used by sub classes.
 */
@objid ("0560ae14-ac88-4d66-a93b-04d8f35c2900")
public abstract class AbstractDiagramConfigurer implements IDiagramConfigurer {
    /**
     * Creates a drawings palette group.
     * @param toolRegistry the tool registry
     * @return the created drawings palette group.
     */
    @objid ("9928bbe3-558d-490f-8b84-ad82dd1afda8")
    protected PaletteEntry createDrawGroup(final ToolRegistry toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(DiagramEditor.I18N.getMessage("PaletteGroup.Drawings"),
                                                      null);
        group.add(toolRegistry.getTool("CREATE_DRAWING_RECTANGLE"));
        group.add(toolRegistry.getTool("CREATE_DRAWING_ELLIPSE"));
        group.add(toolRegistry.getTool("CREATE_DRAWING_TEXT"));
        //group.add(toolRegistry.getTool("CREATE_DRAWING_POLYGON"));
        group.add(toolRegistry.getTool("CREATE_DRAWING_LINE"));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

}

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
                                    

package org.modelio.diagram.elements.common.abstractdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IPersistentViewModelChangeListener;

/**
 * Classes implementing this interface must be able to refresh the diagram content from a model change event.
 * <p>
 * They must also reload it if it has been modified and saved outside of the editor, on undo/redo for example.
 * 
 * @author cmarin
 */
@objid ("7e2024c0-1dec-11e2-8cad-001ec947c8cc")
public interface IDiagramRefresher extends IModelChangeListener, IPersistentViewModelChangeListener {

// Empty interface
}

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
                                    

package org.modelio.core.ui.progress;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class is obsolete. Replace by {@link ModelioProgressAdapter}.
 */
@objid ("db7bd859-d0c1-11e1-ba38-001ec947ccaf")
@Deprecated
public class EclipseProgress extends ModelioProgressAdapter {
    @objid ("cc8cea19-d0c2-11e1-ba38-001ec947ccaf")
    @Deprecated
    public EclipseProgress(IProgressMonitor wrapped) {
        super(wrapped);
    }

}

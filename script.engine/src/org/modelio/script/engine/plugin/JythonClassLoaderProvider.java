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
                                    

package org.modelio.script.engine.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.script.engine.core.engine.IClassLoaderProvider;

@objid ("dca7ce7b-5bf0-4e22-8c45-1ddd78f13aa3")
public class JythonClassLoaderProvider implements IClassLoaderProvider {
    @objid ("dd5e185f-4c23-4f6e-9833-0d2650077f8a")
    @Override
    public ClassLoader getClassLoader() {
        return ScriptEnginePlugin.class.getClassLoader();
    }

}

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
                                    

package org.modelio.vaudit.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

/**
 * This class is used as a processor (see the plugin.xml file) to make the injection framework instantiate the ModelShield and put
 * it in the context.
 */
@objid ("46513fe5-1dd5-11e2-82de-002564c97630")
public class VauditInitializer {
    @objid ("82488fe9-1dd5-11e2-82de-002564c97630")
    @Execute
    private static void execute(IEclipseContext context) {
        context.set(org.modelio.vaudit.modelshield.ModelShield.class, 
                ContextInjectionFactory.make(org.modelio.vaudit.modelshield.ModelShield.class, context));
    }

}

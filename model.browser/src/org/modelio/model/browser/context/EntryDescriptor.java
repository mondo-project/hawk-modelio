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

import java.util.Properties;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("00555762-7a19-1006-9c1d-001ec947cd2a")
public class EntryDescriptor {
    @objid ("0088257a-7a2b-1006-9c1d-001ec947cd2a")
    public String sourceMetaclass;

    @objid ("00884c58-7a2b-1006-9c1d-001ec947cd2a")
    public String sourceStereotype;

    @objid ("00887214-7a2b-1006-9c1d-001ec947cd2a")
    public String commandId;

    @objid ("0055756c-7a19-1006-9c1d-001ec947cd2a")
    public Properties parameters = new Properties();

}

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
                                    

package org.modelio.property.handlers;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.modelio.property.PropertyView;

/**
 * Handler for the setHorizontalLayout command, using injection to execute on the {@link PropertyView} MPart.
 * Changes the property view layout to horizontal.
 * @see SetVerticalLayoutHandler
 * @see SetAutoLayoutHandler
 */
@objid ("8dfce6ec-c068-11e1-8c0a-002564c97630")
public class SetHorizontalLayoutHandler {
    @objid ("8dfce6ed-c068-11e1-8c0a-002564c97630")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart e) {
        if (e.getObject() instanceof PropertyView) {
            PropertyView propertyView = (PropertyView) e.getObject();
        
            if (null != propertyView.getPanel()) {
                propertyView.getPanel().disableAutoLayout();
                propertyView.getPanel().setHorizontalLayout();
            }
        }
    }

}

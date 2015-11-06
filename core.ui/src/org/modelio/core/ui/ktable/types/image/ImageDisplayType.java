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
                                    

package org.modelio.core.ui.ktable.types.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.modelio.core.ui.ktable.types.PropertyType;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("8dec1e27-c068-11e1-8c0a-002564c97630")
public class ImageDisplayType extends PropertyType {
    @objid ("a5791beb-c068-11e1-8c0a-002564c97630")
    private String dependency = null;

    @objid ("8dec1e29-c068-11e1-8c0a-002564c97630")
    private Class<? extends Element> metaclass = null;

    @objid ("8dec1e2c-c068-11e1-8c0a-002564c97630")
    public ImageDisplayType(Class<? extends Element> metaclass, String dependency) {
        super(true);
        this.metaclass = metaclass;
        this.dependency = dependency;
    }

    @objid ("8dec1e36-c068-11e1-8c0a-002564c97630")
    public String getDependency() {
        return this.dependency;
    }

    @objid ("8dec1e3a-c068-11e1-8c0a-002564c97630")
    public Class<? extends Element> getMetaclass() {
        return this.metaclass;
    }

    @objid ("0a8a03fb-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        return new ImageCellRenderer(SWT.NONE, getMetaclass(), getDependency());
    }

}

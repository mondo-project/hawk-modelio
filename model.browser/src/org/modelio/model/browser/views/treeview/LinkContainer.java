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
                                    

package org.modelio.model.browser.views.treeview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Browser node that contain link elements.
 */
@objid ("d86d566a-c06d-11e1-abda-001ec947c8cc")
class LinkContainer {
    @objid ("d86d566b-c06d-11e1-abda-001ec947c8cc")
    private MObject element;

    @objid ("68b8428a-2f04-11e2-9ab7-002564c97630")
    private int nbLinks;

    /**
     * C'tor.
     * @param element the related model object.
     * @param nbLinks links count.
     */
    @objid ("d86d566e-c06d-11e1-abda-001ec947c8cc")
    public LinkContainer(MObject element, int nbLinks) {
        this.element = element;
        this.nbLinks = nbLinks;
    }

    /**
     * @return the related model object.
     */
    @objid ("d86d5673-c06d-11e1-abda-001ec947c8cc")
    public MObject getElement() {
        return this.element;
    }

    /**
     * @return the links count.
     */
    @objid ("68b8428d-2f04-11e2-9ab7-002564c97630")
    public int getNbLinks() {
        return this.nbLinks;
    }

    @objid ("2f724e77-3e00-481f-b2cb-6f40530d01b6")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinkContainer other = (LinkContainer) obj;
        if (this.element == null) {
            if (other.element != null)
                return false;
        } else if (!this.element.equals(other.element))
            return false;
        return true;
    }

    @objid ("96fa5a46-803c-4281-9e96-a96ec3c7f5b1")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.element == null) ? 0 : this.element.hashCode());
        return result;
    }

    @objid ("327ca6f1-2f9c-4c96-b00e-303a2915e4c1")
    @Override
    public String toString() {
        return
        super.toString() +
        " LinkContainer = [" +
        "element=" + this.element +
        ", nbLinks=" + this.nbLinks +
        "]";
    }

}

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
                                    

package org.modelio.mda.infra.service.dynamic;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("b7df3c51-9f0a-46b5-9199-c708e49af553")
class GenericHandler {
    @objid ("9c2c3595-5999-49c7-b92f-7624735ed49d")
    public String metaclass;

    @objid ("52a0ff03-a4fd-4ca5-879c-64c723a4bdf0")
    public String stereotype;

    @objid ("a135ca9a-6b9b-4254-91c2-2c9a60cb9bb2")
    public String relation;

    @objid ("8c08f604-eea1-486b-a5f6-a932cdca8be8")
    public GenericHandler(String metaclass, String relation, String stereotype) {
        this.metaclass = metaclass;
        this.relation = relation;
        this.stereotype = stereotype;
    }

    @objid ("a0bbe1ab-e885-4185-80aa-5d2f4b1b5ac5")
    public String getMetaclass() {
        return this.metaclass;
    }

    @objid ("aebd1b48-eed0-47da-a50f-456fb76d1739")
    public String getRelation() {
        return this.relation;
    }

    @objid ("45d334c1-7cf7-4c6b-bd25-40c4f217c0bc")
    public String getStereotype() {
        return this.stereotype;
    }

}

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
                                    

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.07.08 at 06:09:31 PM CEST
//
package org.modelio.gproject.data.module.jaxbv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * <p>Java class for _NoteType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="_NoteType">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="is-hidden" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="uid" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@objid ("cbb78a95-4281-4421-9134-ba04c01bfb3b")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_NoteType")
public class Jxbv2NoteType {
    @objid ("ee291809-8461-4c25-b5d8-baf07a2d7bc6")
    @XmlAttribute(name = "name", required = true)
    protected String name;

    @objid ("ce49fde0-16c1-4350-bf7e-77bd044c2c2e")
    @XmlAttribute(name = "label")
    protected String label;

    @objid ("c99eec46-a288-4746-a403-9539a3af5884")
    @XmlAttribute(name = "is-hidden")
    protected String isHidden;

    @objid ("b2f4de30-6315-438d-8500-f26a5464d966")
    @XmlAttribute(name = "uid")
    protected String uid;

    /**
     * Gets the value of the name property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("6e440e26-d96b-4ff6-9031-9509be1522d8")
    public String getName() {
        return this.name;
    }

    /**
     * Sets the value of the name property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("48de5ef7-f3cf-4657-8cb7-1bbb43880209")
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the label property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("0120eee6-1a3a-4178-a682-20a63c3c4e57")
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the value of the label property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("e846c7ea-4450-4653-9d1d-1c6acafdcf36")
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the isHidden property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("f12ec28f-1b73-4336-9187-5d0fc04a5ef0")
    public String getIsHidden() {
        return this.isHidden;
    }

    /**
     * Sets the value of the isHidden property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("6a62e1d3-9005-4370-8921-a8453ce19af1")
    public void setIsHidden(String value) {
        this.isHidden = value;
    }

    /**
     * Gets the value of the uid property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("e94507b7-81b4-4316-afed-5c755ab85d7c")
    public String getUid() {
        return this.uid;
    }

    /**
     * Sets the value of the uid property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("6bad6339-d84e-4690-aafd-75a08dccdd3b")
    public void setUid(String value) {
        this.uid = value;
    }

}
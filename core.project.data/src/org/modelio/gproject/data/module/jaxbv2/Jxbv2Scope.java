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
 * <p>Java class for _Scope complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="_Scope">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;attribute name="metaclass" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="stereotype" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@objid ("1d28c56e-2c6c-49a5-9aaf-470c779c6462")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_Scope")
public class Jxbv2Scope {
    @objid ("cf6f9cfa-249e-438c-b4ee-1eb4923ed984")
    @XmlAttribute(name = "metaclass")
    protected String metaclass;

    @objid ("f2fbb8d3-09bb-44f1-ad98-d3ec90ff5821")
    @XmlAttribute(name = "stereotype")
    protected String stereotype;

    /**
     * Gets the value of the metaclass property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("fb41c501-30e7-4571-ab34-ceca3e493136")
    public String getMetaclass() {
        return this.metaclass;
    }

    /**
     * Sets the value of the metaclass property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("a4747760-5fa0-49b6-8344-420a1f3da4e2")
    public void setMetaclass(String value) {
        this.metaclass = value;
    }

    /**
     * Gets the value of the stereotype property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("a7b64dd0-decf-4439-801e-730dc1c80bbd")
    public String getStereotype() {
        return this.stereotype;
    }

    /**
     * Sets the value of the stereotype property.
     * @param value allowed object is
     * {@link String }
     */
    @objid ("766a88d7-c7a8-484a-af03-7640bedbd8d3")
    public void setStereotype(String value) {
        this.stereotype = value;
    }

}

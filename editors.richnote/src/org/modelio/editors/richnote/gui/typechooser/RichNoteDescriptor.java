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
                                    

package org.modelio.editors.richnote.gui.typechooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * Create document dialog model.
 */
@objid ("28f320c9-a8b8-4a6f-a159-5490d909d1d1")
public class RichNoteDescriptor {
    @objid ("d2f80b1c-aabf-4fde-b638-f943e0de26aa")
    private String theAbstract;

    @objid ("08428d8e-13f9-48bb-a651-ae73848fafed")
    private String mimeType;

    @objid ("7667abd8-3b62-4177-8242-c4b598bf7820")
    private String path;

    @objid ("7704c76a-39b8-4630-b404-03e3c493a13c")
    private String name;

    @objid ("de92f8ca-b090-470f-ba12-1cb4f5802bb7")
    private ExternDocumentType docType;

    @objid ("5ca117ea-d766-47c2-bd43-0b5564c2d0a6")
    private ModelElement targetElement;

    /**
     * @return the document abstract.
     */
    @objid ("149fe782-6de5-46d6-9fdc-f777d27aab45")
    public String getAbstract() {
        return this.theAbstract;
    }

    /**
     * @return the document MIME type
     */
    @objid ("67806a6f-74b7-4262-b202-e78c2d2643dd")
    public String getChosenMimeType() {
        return this.mimeType;
    }

    /**
     * @return the document type.
     */
    @objid ("6a29cda6-6081-4708-9e9a-1b3db02f018e")
    public ExternDocumentType getDocumentType() {
        return this.docType;
    }

    /**
     * @return the document name.
     */
    @objid ("3e7ced40-8c65-457a-b908-8262b32214a1")
    public String getName() {
        return this.name;
    }

    /**
     * @return the document path
     */
    @objid ("f39c526d-40dc-4fe0-8cc6-c83abb3b7e95")
    public String getPath() {
        return this.path;
    }

    /**
     * @param theAbstract the Abstract to set
     */
    @objid ("b932cdf9-ed9a-48b1-ade3-b420ea505d31")
    void setAbstract(final String theAbstract) {
        this.theAbstract = theAbstract;
    }

    @objid ("7042a7f6-10b3-4125-b64c-3a10df1577f8")
    void setDocumentType(final ExternDocumentType docType) {
        this.docType = docType;
    }

    /**
     * @param mimeType the MIME type
     */
    @objid ("c3c09c47-f158-4b64-9326-7d5ce3c43c98")
    void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    @objid ("b3914e47-194c-4160-afe9-85085833801e")
    void setName(final String text) {
        this.name = text;
    }

    /**
     * @param path the path to set
     */
    @objid ("7c9ece22-c8b2-4d81-a585-cfab64c49610")
    void setPath(final String path) {
        this.path = path;
    }

    /**
     * Initialize the rich note descriptor.
     * @param targetElement The element on which the rich note must be created.
     */
    @objid ("8be9937f-bcef-4d60-b932-02c72651412c")
    public RichNoteDescriptor(final ModelElement targetElement) {
        this.targetElement = targetElement;
    }

    /**
     * @return The element on which the rich note will be created.
     */
    @objid ("d59bbefd-c390-4122-808b-5b76e36cd1d4")
    public ModelElement getTargetElement() {
        return this.targetElement;
    }

}

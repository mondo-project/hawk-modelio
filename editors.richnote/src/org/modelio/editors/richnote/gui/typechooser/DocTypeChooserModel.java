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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Document type tree view field model.
 */
@objid ("dec2f592-1e26-4139-b8c8-985dd513c2f0")
class DocTypeChooserModel {
    @objid ("a70648b0-9681-42e9-8890-b84cb000e8ec")
     Map<ModuleComponent, AdapterModule> mdacAdapters;

    @objid ("8a762274-73ed-4ec2-8557-ff6a1becd592")
    private ModelElement element;

    @objid ("be7afd2e-9591-4e8f-8ecd-7ab9eec233ee")
    private IMModelServices modelService;

    @objid ("b8237a0b-0226-4016-b52a-cb4f8080f5b9")
    DocTypeChooserModel(IMModelServices modelService, ModelElement element) {
        this.modelService = modelService;
        this.element = element;
        this.mdacAdapters = new HashMap<>();
        init();
    }

    @objid ("ed4c2daa-5292-4b07-9d08-bebd590ddeff")
    DocTypeChooserModel(IMModelServices modelService) {
        this.modelService = modelService;
        this.element = null;
        this.mdacAdapters = new HashMap<>();
    }

    @objid ("d4913b46-b8e1-49d9-8042-d9bc79eb4224")
    public AdapterModule getMdacAdapter(ModuleComponent module) {
        return this.mdacAdapters.get(module);
    }

    /**
     * Get accessor for mdacAdapters
     * @return the module nodes.
     */
    @objid ("41ed2acb-25d5-48f8-9645-371c43ae574c")
    public Map<ModuleComponent, AdapterModule> getMdacAdapters() {
        return this.mdacAdapters;
    }

    @objid ("0e8f6e3b-a271-4540-9071-102066cffae3")
    public AdapterStereotype getStereotypeAdapter(ModuleComponent module, Stereotype stereotype) {
        AdapterModule mdacAdapter = this.mdacAdapters.get(module);
        
        if (mdacAdapter != null) {
            return mdacAdapter.getAdapter(stereotype);
        }
        return null;
    }

    @objid ("e0290510-bd79-4fc5-bfab-a981875d3bcb")
    void addDocType(ModuleComponent module, ExternDocumentType noteType) {
        AdapterModule adapter = this.mdacAdapters.get(module);
        
        if (adapter == null) {
            adapter = new AdapterModule(module);
            this.mdacAdapters.put(module, adapter);
        }
        adapter.addDocType(noteType);
    }

    @objid ("4b021394-8d24-4a54-8762-3f809fdbcb51")
    void addStereotype(ModuleComponent mdac, Stereotype stereotype) {
        AdapterModule adapter = this.mdacAdapters.get(mdac);
        
        if (adapter == null) {
            adapter = new AdapterModule(mdac);
            this.mdacAdapters.put(mdac, adapter);
        }
        
        adapter.addStereotype(stereotype);
    }

    @objid ("ebef200e-7dd7-47e5-9f1f-8b9618376709")
    private void init() {
        if (this.modelService == null) {
            return;
        }
        
        List<Stereotype> stereotypes = this.element.getExtension();
        for (Stereotype stereotype : stereotypes) {
            boolean hasDocTypes = false;
            List<ExternDocumentType> docTypes = stereotype.getDefinedExternDocumentType();
            for (ExternDocumentType docType : docTypes) {
                if (!docType.isIsHidden()) {
                    hasDocTypes = true;
                    break;
                }
            }
            
            if (hasDocTypes)
            {
                ModuleComponent module = getModule(stereotype);
                if (module != null ) {
                    addStereotype(module, stereotype);
                }
            }
        }
        
        List<ExternDocumentType> docTypes = this.modelService.findExternDocumentTypes(".*", this.element.getMClass());
        for (ExternDocumentType docType : docTypes) {
            if (docType.isIsHidden())
                continue;
            
            if (docType.getOwnerReference() != null) {
                ModuleComponent module = getModule(docType);
        
                if (module != null) {
                    addDocType(module, docType);
                }
            } else {
                Stereotype docTypeStereo = docType.getOwnerStereotype();
                if (docTypeStereo != null && this.element.isStereotyped(getModule(docTypeStereo).getName(), docTypeStereo.getName())) {
                    ModuleComponent module = getModule(docType);
              
                    if (module != null) {
                        addDocType(module, docType);
                    }
                }
            }
        }
    }

    @objid ("e76b20a8-cc24-435c-90cc-1c1d92c9235b")
    private ModuleComponent getModule(MObject docType) {
        MObject parent = docType.getCompositionOwner();
                
        while( parent!=null && ! (parent instanceof ModuleComponent))
            parent = parent.getCompositionOwner();
        return (ModuleComponent) parent;
    }

}

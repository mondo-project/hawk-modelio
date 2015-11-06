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
                                    

package org.modelio.xmi.util;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.modelio.xmi.plugin.Xmi;
import org.osgi.framework.Bundle;

@objid ("0f89a6a5-7e89-45bf-8f5f-f55fdbe35f77")
public class UMLMetamodel {
    @objid ("3481fb61-8de9-41b1-b14e-6d4c7d04bb92")
    private static UMLMetamodel INSTANCE = null;

    @objid ("7c24dafe-d317-42d2-b781-15c98ddd2686")
    private static Model umlMetamodel = null;

    @objid ("0de73203-ee9f-4f89-86ab-c5f02518e6e9")
    private static Model umlLibrary = null;

    @objid ("96ef5353-c730-48f3-8715-4c4dc4ee53ff")
    private static Model ecoreLibrary = null;

    @objid ("dc7dbfb4-453d-4740-83c3-efa52717bceb")
    public Model getUMLMetamodel() {
        if (umlMetamodel == null)
            umlMetamodel = load(URI.createURI(UMLResource.UML_METAMODEL_URI));
        return umlMetamodel;
    }

    @objid ("bdf105e2-c43d-4b32-bca0-3713aa88f9f7")
    public Model getUMLLibrary() {
        if (umlLibrary == null)
            umlLibrary = load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
        return umlLibrary;
    }

    @objid ("585f791c-9fd2-4fd5-9f78-def56f308496")
    public Model getEcoreLibrary() {
        if (ecoreLibrary == null)
            ecoreLibrary = load(URI.createURI(UMLResource.ECORE_PRIMITIVE_TYPES_LIBRARY_URI));
        return ecoreLibrary;
    }

    @objid ("e8d8b2af-af2a-463c-90e5-1e924e51346c")
    private Model load(final URI uriModel) {
        Model result = null;
        try {
         
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
        
            final Bundle bundle = Platform.getBundle("org.eclipse.uml2.uml.resources");
            
            IPath libraries = new Path("/libraries");
            IPath metamodels = new Path("/metamodels");
            IPath profile = new Path("/profiles");
            
            
            URI uriLibraries = URI.createURI(FileLocator.find(bundle, libraries, null).toExternalForm());
            URI uriMetamodels = URI.createURI(FileLocator.find(bundle, metamodels, null).toExternalForm());
            URI uriProfiles = URI.createURI(FileLocator.find(bundle, profile, null).toExternalForm());
            
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uriLibraries);
        
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uriMetamodels);
        
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uriProfiles);
        
            ResourceSet resoureSet = new ResourceSetImpl();
            Resource resource = resoureSet.getResource(uriModel, true);
            result = (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
        } catch (WrappedException we) {
            Xmi.LOG.error(we);       
        }
        return result;
    }

    @objid ("338abb04-cc6d-4cec-8617-1d9b9b963112")
    private UMLMetamodel() {
    }

    @objid ("9483ea34-680e-4e81-9f2f-64806208d8e8")
    public static UMLMetamodel getInstance() {
        if (INSTANCE == null)
            INSTANCE = new UMLMetamodel();
        return INSTANCE;
    }

}

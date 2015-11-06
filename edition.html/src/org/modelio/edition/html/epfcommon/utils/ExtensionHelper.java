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
                                    

//------------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//which accompanies this distribution, and is available at
//http://www.eclipse.org/legal/epl-v10.html
//
//Contributors:
//IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.epfcommon.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.modelio.edition.html.epfcommon.IHTMLFormatter;
import org.modelio.edition.html.epfcommon.IHTMLParser;
import org.modelio.edition.html.plugin.HtmlTextPlugin;

/**
 * Helper class with methods to retrieve extensions.
 * 
 * @author Phong Nguyen Le
 * @author Weiping Lu
 * @since  1.5
 */
@objid ("3a4ad286-9c83-4420-8a5f-5af4dfa955e6")
public class ExtensionHelper {
    @objid ("485b7a60-3ecb-4eb6-a817-f78ea6388f84")
    private static Map<String, Object> IDToExtensionMap = new HashMap<>();

    /**
     * Create an eclipse executable extension point.
     * @param namespace the namespace for the given extension point
     * (e.g. <code>"org.eclipse.core.resources"</code>)
     * @param extensionPointName the simple identifier of the extension point (e.g. "builders")
     * @return the executable extension point instance or null
     */
    @objid ("0cdbdba4-2b7e-4a07-a445-50a61ccd9447")
    public static Object createExtension(String namespace, String extensionPointName) {
        // Process the contributors.
        //
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(namespace, extensionPointName);
        if (extensionPoint != null) {
            for (IExtension extension : extensionPoint.getExtensions()) {
                for (IConfigurationElement configElement : extension.getConfigurationElements()) {
                    try {
                        return configElement.createExecutableExtension("class");
                    } catch (Exception e) {
                        HtmlTextPlugin.LOG.error(e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get an eclipse executable extension point.
     * <p>
     * Extension point instances created by this method are recorded in a static map
     * so that the existing instance is returned if asked again.
     * @param namespace the namespace for the given extension point
     * (e.g. <code>"org.eclipse.core.resources"</code>)
     * @param extensionPointName the simple identifier of the extension point (e.g. "builders")
     * @return the executable extension point instance or null
     */
    @objid ("da5690f1-682d-4944-aee0-3e80ce63de98")
    public static Object getExtension(String namespace, String extensionPointName) {
        String ID = namespace + '.' + extensionPointName;
        Object ext = IDToExtensionMap.get(ID);
        if(ext == null) {
            synchronized (IDToExtensionMap) {
                ext = IDToExtensionMap.get(ID);
                if(ext == null) {
                    ext = createExtension(namespace, extensionPointName);
                    if(ext != null) {
                        IDToExtensionMap.put(ID, ext);
                    }
                } 
            }
        
        }
        return ext;
    }

    /**
     * This is a special method to handle JTidy extensions.
     * <p>
     * Look for used defined extension points first.
     * Return the default registered extension point instance if no other found.
     * @param namespace the namespace for the given extension point
     * (e.g. <code>"org.eclipse.core.resources"</code>)
     * @param extensionPointName the simple identifier of the extension point (e.g. "builders")
     * @return the JTidy extensions
     */
    @objid ("bcd0d261-80b0-4dd4-98b3-8c5e2670eb8e")
    public static Object createExtensionForJTidy(String namespace, String extensionPointName) {
        List<IHTMLFormatter> formaters = new ArrayList<>();
        List<IHTMLParser> parsers = new ArrayList<>();
        
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(namespace, extensionPointName);
        if (extensionPoint != null) {
            for (IExtension extension : extensionPoint.getExtensions()) {
                for (IConfigurationElement configElement : extension.getConfigurationElements()) {
                    try {
                        String className = configElement.getAttribute("class"); //$NON-NLS-1$
                        if(className != null) {
                            Object obj = configElement.createExecutableExtension("class");
                            if (extensionPointName.equals("htmlFormatter")) { //$NON-NLS-1$
                                formaters.add((IHTMLFormatter)obj);
                            } else if (extensionPointName.equals("htmlParser")) { //$NON-NLS-1$
                                parsers.add((IHTMLParser)obj);
                            }                            
                        }
                    } catch (Exception e) {
                        HtmlTextPlugin.LOG.error(e);
                    }
                }
            }
            
            if (formaters.size() > 0) {
                if (formaters.size() == 1) {
                    return formaters.get(0);
                }
                return selectExtension(formaters);
            } else if (parsers.size() > 0) {
                if (parsers.size() == 1) {
                    return parsers.get(0);
                }
                return selectExtension(parsers);                
            }
        }
        return null;
    }

    @objid ("2ef96078-28b3-44fe-9642-9fe31251112b")
    private static Object selectExtension(List<?> objs) {
        for (Object obj : objs) {
            String name = obj.getClass().getName();
            if (!(name.equals("org.eclipse.epf.common.html.DefaultHTMLFormatter") //$NON-NLS-1$
            || name.equals("org.eclipse.epf.common.html.DefaultHTMLParser"))) { //$NON-NLS-1$
                return obj;
            }
        }
        return null;
    }

}

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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.mda.infra.plugin.MdaInfra;

/**
 * Message class managing i18n for modules.
 * Works in a similar way as eclipse plugins, looking for 'module.properties' files in the module resource directory.
 */
@objid ("1f983345-680e-4712-b8bf-440bcfadf22d")
class ModuleI18n {
    @objid ("407596e3-e3f6-4c7e-9acd-163cadd5544b")
    private File bundleDir;

    @objid ("98928531-9a86-4187-ac62-b075278f7321")
    public ModuleI18n(final File bundleDir) {
        this.bundleDir = bundleDir;
    }

    @objid ("383a20d1-e7bf-464d-9bb5-a3c9b335505e")
    private ResourceBundle getManifestBundle() throws MissingResourceException {
        try (URLClassLoader cl = new URLClassLoader(new URL[] { this.bundleDir.toURI().toURL() })) {
            // Create a class loader initialized with the 'manifest' directory in module resource,
            // then give it to ResourceBundle.getBundle(...) 
            return ResourceBundle.getBundle("module", Locale.getDefault(), cl);
        } catch (IOException e) {
            throw new MissingResourceException(e.getLocalizedMessage(), "module", "");
        } catch (MissingResourceException e) {
            MissingResourceException e2 = new MissingResourceException(e.getLocalizedMessage() + " in '" + this.bundleDir.getPath() + "'", e.getClassName(), e.getKey());
            e2.initCause(e);
            throw e2;
        }
    }

    @objid ("e59e9b92-8e06-4848-ae6d-68563bcae672")
    public String internationalize(final String value) {
        try {
            if(value == null)
                return "";
            
            if (value.startsWith("%")) {
                return getManifestBundle().getString(value.substring(1));
            }
        } catch (MissingResourceException e) {
            MdaInfra.LOG.warning("Missing Resource :" + value);
        }
        return value;
    }

}

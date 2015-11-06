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
                                    

package org.modelio.audit.service;

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.AuditEngine;
import org.modelio.audit.preferences.model.AuditConfigurationModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Audit service interface.
 */
@objid ("1e542651-7add-4b30-b9c4-ae3ead393965")
public interface IAuditService {
    /**
     * Read again the configuration files, compute the audit plan applies it to the engine.
     */
    @objid ("43b57340-a04d-444f-a456-908d85a77193")
    void restart();

    /**
     * Apply the given configuration, saves it in the current configuration file and {@link #restart() restart} the audit service.
     * @param prefModel the audit model to apply
     */
    @objid ("fe7c9451-88b0-4a7f-b6e1-c02c9c808f89")
    void apply(AuditConfigurationModel prefModel);

    /**
     * Audit an element.
     * @param e a model object
     * @param jobId an audit job identifier for reporting
     */
    @objid ("9a8f5fc1-ee1d-4670-b9b3-4df83b06f132")
    void checkElement(MObject e, String jobId);

    /**
     * @return the audit engine.
     */
    @objid ("dd8c293a-2b08-47e8-8776-3c9c51562802")
    AuditEngine getAuditEngine();

    /**
     * Get a copy of the audit configuration model.
     * @return the audit configuration model.
     */
    @objid ("a51a2a06-08fa-4da9-ab01-0c07e78973de")
    AuditConfigurationModel getConfigurationModel();

    /**
     * Get the configuration file used by the audit service.
     * <p>
     * The returned file may be the file defined by {@link #setConfigurationFile(File)},
     * or the default project configuration file.
     * @return the used configuration file.
     */
    @objid ("84a5d1e6-f9d2-4b1a-a410-50125b1ddf2b")
    File getConfigurationFile();

    /**
     * @return the audit configuration factory settings.
     */
    @objid ("45452555-33cc-45c5-8787-9db84f5ff8de")
    AuditConfigurationModel getFactorySettings();

    /**
     * Set the audit configuration file to use.
     * <p>
     * This restart the audit service.
     * @param confFile the new audit configuration file.
     */
    @objid ("b84226bc-922b-4028-8f97-eeb0ee014bd6")
    void setConfigurationFile(File confFile);

    /**
     * Set a default settings file to use.
     * <p>
     * This file will be read after factory settings file and before te project configuration file.
     * @param confFile the factory settings file to use.
     */
    @objid ("56178467-a188-40cd-a6c1-b19e379901df")
    void setDefaultSettingsFile(File confFile);

}

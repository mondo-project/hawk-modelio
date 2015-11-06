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
                                    

package org.modelio.audit.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ResourceBundle;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.audit.engine.AuditEngine;
import org.modelio.audit.engine.impl.AuditPlan;
import org.modelio.audit.plugin.Audit.ResourceNotFoundError;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.preferences.AuditModelController;
import org.modelio.audit.preferences.model.AuditConfigurationModel;
import org.modelio.audit.preferences.persistence.old.OldAuditConfigurator;
import org.modelio.audit.service.IAuditService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Audit service implementation.
 */
@objid ("7ddbb117-45fb-11e2-9b4d-bc305ba4815c")
@Creatable
public class AuditService implements IAuditService {
    @objid ("7ddbb11a-45fb-11e2-9b4d-bc305ba4815c")
    private AuditEngine auditEngine;

    @objid ("7ddbb11b-45fb-11e2-9b4d-bc305ba4815c")
    private GProject openedProject;

    @objid ("93b616c7-3a98-412a-a899-e8390d767b93")
    @Inject
    private StatusReporter errReporter;

    @objid ("b2406960-22f0-4001-aabf-19cd4fdb86ba")
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("auditrules");

    @objid ("4fd2d0ed-357f-45c7-b763-e7d91f4f80e5")
    private AuditModelController modelController;

    @objid ("599c07f0-15cf-421f-9d46-13e12822a4b3")
    private Geometry geometry;

    @objid ("fc30aaff-44c7-494d-ac1c-56f1c93e67d8")
    private ObsoleteGeometry oldGeometry;

    @objid ("7ddbb11c-45fb-11e2-9b4d-bc305ba4815c")
    @Override
    public void setConfigurationFile(File confFile) {
        this.geometry.setConfigurationFile(confFile);
        
        restart();
    }

    @objid ("7ddbb120-45fb-11e2-9b4d-bc305ba4815c")
    @Override
    public void restart() {
        try {
            loadModel();
            
            // Generate the plan
            AuditPlan auditPlan = getProjectConfigurator().createPlan();
            auditPlan.dump(Platform.getLogFileLocation());
        
            // Change the plan
            this.auditEngine.setPlan(auditPlan,this.openedProject.getSession());
        } catch (FileSystemException e) {
            this.errReporter.show(StatusReporter.ERROR, FileUtils.getLocalizedMessage(e), e);
        } catch (IOException e) {
            Audit.LOG.error(e);
            this.errReporter.show(StatusReporter.ERROR, e.getLocalizedMessage(), e);
        }
    }

    @objid ("7ddbb123-45fb-11e2-9b4d-bc305ba4815c")
    @Override
    public void checkElement(MObject element, String jobId) {
        this.auditEngine.checkElement(element,jobId);
    }

    /**
     * Return File descriptor of the local configuration file for the current project.
     * <p>
     * An opened modeling session is assumed to be available.
     * @return File : Configuration file
     */
    @objid ("7ddbb127-45fb-11e2-9b4d-bc305ba4815c")
    @Override
    public File getConfigurationFile() {
        return this.geometry.getConfigurationFile();
    }

    /**
     * This method is called after a project opening. Keep a reference to the modeling session and model services.
     */
    @objid ("7ddbb12d-45fb-11e2-9b4d-bc305ba4815c")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENING) final GProject project) {
        //FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        if(project != null){    
            this.openedProject = project;
            
            try {
                // Ensure the configuration file exists.
                // Do migration of needed
                ensureConfigurationFile();
                
                // Load the model
                loadModel();
        
                // Generate the plan
                AuditPlan auditPlan = getProjectConfigurator().createPlan();
                // auditPlan.dump(Platform.getLogFileLocation());
                
                // Init the audit engine, set the plan and start
                this.auditEngine.setPlan(auditPlan, project.getSession());
                this.auditEngine.start(this.openedProject.getSession());
                
            } catch (FileSystemException e) {
                this.errReporter.show(StatusReporter.ERROR, FileUtils.getLocalizedMessage(e), e);
            } catch (IOException e) {
                Audit.LOG.error(e);
                this.errReporter.show(StatusReporter.ERROR, e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Called when a project is closed. On session close un-reference the modeling session and model services.
     */
    @objid ("7dde127a-45fb-11e2-9b4d-bc305ba4815c")
    @Optional
    @Inject
    void onProjectClosing(@EventTopic(ModelioEventTopics.PROJECT_CLOSING) final GProject closedProject) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        if(closedProject != null){
            // Standard audit
            this.auditEngine.stop(closedProject.getSession());
            this.openedProject = null;        
        }
    }

    @objid ("7dde1283-45fb-11e2-9b4d-bc305ba4815c")
    @Override
    public AuditEngine getAuditEngine() {
        return this.auditEngine;
    }

    /**
     * constructor to be called by E4 engine.
     */
    @objid ("b4c3aaa7-f8a1-41cc-9c0b-d8cf049f14c5")
    public AuditService() {
        this.auditEngine  = new AuditEngine();
        this.modelController = new AuditModelController();
        this.geometry = new Geometry();
        this.oldGeometry = new ObsoleteGeometry();
    }

    @objid ("36b7f133-5e97-483e-aeb0-9965120a1037")
    @Override
    public AuditConfigurationModel getConfigurationModel() {
        return new AuditConfigurationModel(getProjectConfigurator().getModel());
    }

    @objid ("4b43952c-f80d-4afe-90d0-a91d9ab991b7")
    @Override
    public void apply(AuditConfigurationModel auditConfiguration) {
        // Create an audit configurator
        this.modelController.applyAuditConfiguration(auditConfiguration);
        
        try {
            saveConfiguration();
            restart();
        } catch (IOException e) {
            Audit.LOG.error(e);
            this.errReporter.show(StatusReporter.ERROR, e.getLocalizedMessage(), e);
        }
    }

    @objid ("53eb65a1-1cbe-4b86-8f51-368644e4783a")
    private AuditModelController getProjectConfigurator() {
        return this.modelController;
    }

    @objid ("c8161b2b-1836-4e48-a89b-e2a942be8a69")
    @Override
    public AuditConfigurationModel getFactorySettings() {
        AuditModelController ret = new AuditModelController();
        try {
            ret.loadDefinitions(this.geometry.getRuleDefinitionFile(), BUNDLE);
            ret.applyAuditConfiguration(this.geometry.getFactorySettingsFile());
        } catch (IOException e) {
            Audit.LOG.error(e);
            this.errReporter.show(StatusReporter.ERROR, e.getLocalizedMessage(), e);
        }
        return ret.getModel();
    }

    /**
     * Ensure the configuration file exists.
     * @throws java.io.IOException in case of I/O error making the project configuration file.
     */
    @objid ("ede06185-b080-4a4d-a44c-1afab527d695")
    private void ensureConfigurationFile() throws IOException {
        // Migrate factory settings
        File curDefault = null;
        try {
            curDefault = this.geometry.getFactorySettingsFile();
        } catch (ResourceNotFoundError e) {
            // ignore
        }
        
        if (curDefault == null || !curDefault.isFile()) {
            File oldDefault = this.oldGeometry.getFactorySettingsFile();
            File newRulesFile = this.openedProject.getProjectDataPath().resolve("auditdefinitions.properties").toFile();
            
            Audit.LOG.info("Migration: extract rules from '"+oldDefault+"' to '"+newRulesFile+"'");
            
            OldAuditConfigurator oldconf = new OldAuditConfigurator(oldDefault);
            AuditModelController newconf = new AuditModelController();
            newconf.applyAuditConfiguration(oldconf.createPrefModel());
            
            newconf.writeConfiguration(this.geometry.getDefaultProjectConfigurationFile());
            newconf.writeRuleDefinitions(newRulesFile);
        }
        
        // Migrate settings file if needed
        File readFile = this.geometry.findFirstConfigurationFile();
        
        if (readFile == null) {
            // Configuration file missing, look for obsolete configuration files
            readFile = this.oldGeometry.findFirstConfigurationFile();
            if (readFile != null) {
                // Migrate configuration
                final File newFile = this.geometry.getDefaultProjectConfigurationFile();
                Audit.LOG.info("Migrate '"+readFile+"' to '"+newFile+"'");
        
                OldAuditConfigurator oldconf = new OldAuditConfigurator(readFile);
                AuditModelController newconf = new AuditModelController();
                newconf.applyAuditConfiguration(oldconf.createPrefModel());
                
                newconf.writeConfiguration(newFile);
                readFile.delete();
            }
        }
        
        /*
        // If there is no config file (first time project or erased by user)
        // create a new one from the factory settings values
        if (! editFile.equals(readFile)) {
            Files.copy(readFile.toPath(), editFile.toPath());
        }*/
    }

    @objid ("cb7aaa3a-8f81-4781-a48c-55f70ef1e696")
    private void saveConfiguration() throws IOException {
        this.modelController.writeConfiguration(getConfigurationFile());
    }

    /**
     * Creates a new model controller matching the audit service current configuration.
     * @return a new model controller.
     * @throws java.io.IOException in case of I/O error
     */
    @objid ("76e8a01d-9912-44d3-b1d6-5376f260cc8b")
    private AuditModelController createModelController() throws IOException {
        AuditModelController ret = new AuditModelController();
        ret.addDefaultConf(this.geometry.getFactorySettingsFile());
        
        final File defaultSettingsFile = this.geometry.getDefaultSettingsFile();
        if (defaultSettingsFile != null)
            ret.addDefaultConf(defaultSettingsFile);
        
        ret.loadDefinitions(this.geometry.getRuleDefinitionFile(), BUNDLE);
        
        final File confFile = getConfigurationFile();
        if (confFile.isFile())
            ret.applyAuditConfiguration(confFile);
        return ret;
    }

    @objid ("aaa47858-96c4-4adc-a211-69cf9b3b6ffa")
    private void loadModel() throws IOException {
        this.modelController = createModelController();
    }

    /**
     * Get the edited project.
     * @return the edited project.
     */
    @objid ("1a82221b-5501-4a65-89df-42671396dbbe")
    GProject getProject() {
        assert (this.openedProject != null);
        return this.openedProject;
    }

    @objid ("68a6c7cf-9285-4ce0-a5b6-95db17cf4854")
    @Override
    public void setDefaultSettingsFile(File confFile) {
        this.geometry.defaultSettingsFile = confFile;
    }

    /**
     * Current configuration files geometry.
     * <p>
     * Gives the configuration files path.
     */
    @objid ("47824921-74a7-4ae6-b1af-7b20c48425eb")
    private class Geometry {
        @objid ("7ddbb119-45fb-11e2-9b4d-bc305ba4815c")
        private File redefinedFile;

        @objid ("4ffe6fd3-fc23-4359-868c-bda3822b7d48")
         File defaultSettingsFile;

        @objid ("0702a158-b73f-4865-a585-d47019cbaff3")
        public Geometry() {
            // nothing
        }

        @objid ("ccb78196-44cb-4932-b7b4-fab715a79a85")
        public void setConfigurationFile(File confFile) {
            this.redefinedFile = confFile;
        }

        @objid ("0e8a86fb-7247-4ecf-aee9-4f55ba115e66")
        public File getRuleDefinitionFile() {
            return Audit.getBundleFile("res/auditdefinitions.properties");
        }

        /**
         * Find the first configuration file available in the following ordered list: <ul>
         * <li> The set configuration file
         * <li> The default project configuration file
         * <li> The set factory settings file
         * <li> The default factory settings file.
         * @return the configuration file to read.
         */
        @objid ("20c85e9a-c398-427c-aead-6d63572434f1")
        public File findFirstConfigurationFile() {
            final File[] ff = new File [] {getConfigurationFile(), getFactorySettingsFile()};
            for (File f : ff)
                if (f != null && f.isFile())
                    return f ;
            return null;
        }

        /**
         * Return File descriptor of the local configuration file for the current project.
         * <p>
         * An opened modeling session is assumed to be available.
         * @return File : Configuration file
         */
        @objid ("9a5449ce-008e-4055-aafa-bd2db6416caf")
        public File getConfigurationFile() {
            if(this.redefinedFile != null){
                return this.redefinedFile;
            }
            return this.getDefaultProjectConfigurationFile();
        }

        /**
         * Get the default factory settings file.
         * <p>
         * This file always exist unless a Modelio packaging error.
         * @return the default factory settings file.
         */
        @objid ("20512f56-f584-4b83-9b31-5103b61decbc")
        private File getDefaultFactorySettings() {
            return Audit.getBundleFile("res/auditconfiguration.properties");
        }

        @objid ("4b83539a-140c-485e-8960-e2bdaec07400")
        public File getDefaultProjectConfigurationFile() {
            return getProject().getProjectDataPath().resolve(GProject.DATA_SUBDIR).resolve(".config/auditconfiguration.properties").toFile();
        }

        @objid ("1a4dd68f-a0e7-4c81-914e-bf3bdcf12be0")
        public File getFactorySettingsFile() {
            if (this.defaultSettingsFile != null && this.defaultSettingsFile.isFile())
                return this.defaultSettingsFile;
            return getDefaultFactorySettings();
        }

        @objid ("e2244ddc-baaa-4244-bcdd-b31c4d91c6d5")
        public File getDefaultSettingsFile() {
            if (this.defaultSettingsFile != null && this.defaultSettingsFile.isFile())
                return this.defaultSettingsFile;
            return null;
        }

    }

    /**
     * Old configuration files geometry.
     */
    @objid ("322cf7c0-8a4c-4833-8b67-fd0db123f8f0")
    private class ObsoleteGeometry {
        @objid ("caa6a892-0b9a-40c5-a1de-0b490ceabe12")
        public ObsoleteGeometry() {
            // nothing
        }

        @objid ("606395d3-01d9-4499-b6f3-22f755826948")
        private File getDefaultProjectConfigurationFile() {
            return getProject().getProjectDataPath().resolve(GProject.DATA_SUBDIR).resolve(".config/auditconfiguration.xml").toFile();
        }

        /**
         * Find the first configuration file available in the following ordered list: <ul>
         * <li> The default project configuration file
         * <li> The pre RC2  project configuration file
         * <li> The default factory settings file.
         * @return the configuration file to read.
         */
        @objid ("517caf90-28ae-4d37-8f85-9079889600d6")
        public File findFirstConfigurationFile() {
            final File[] ff = new File [] {getDefaultProjectConfigurationFile(),
                    getMisplacedConfigurationPlan(),
                    getFactorySettingsFile()};
            for (File f : ff)
                if (f != null && f.isFile())
                    return f ;
            return null;
        }

        /**
         * Get the old default factory settings file.
         * <p>
         * This file always exist unless a Modelio packaging error.
         * @return the default factory settings file.
         */
        @objid ("fe483377-3a52-4654-9553-cda48115716b")
        File getFactorySettingsFile() {
            return Audit.getBundleFile("res/auditconfiguration.xml");
        }

        /**
         * Return the File descriptor of the misplaced local configuration file for the current project (occupied till the Modelio 3.0 RC2).
         * @return File : Configuration file
         */
        @objid ("4a2da736-39b4-4279-9d05-b35a0914ce54")
        private File getMisplacedConfigurationPlan() {
            return getProject().getProjectDataPath().resolve(".config/auditconfiguration.xml").toAbsolutePath().toFile();
        }

    }

}

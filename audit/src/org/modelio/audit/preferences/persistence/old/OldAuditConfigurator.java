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
                                    

/**
 * 
 */
package org.modelio.audit.preferences.persistence.old;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.preferences.model.AuditConfigurationModel;
import org.modelio.audit.preferences.model.AuditRule;
import org.modelio.audit.service.AuditSeverity;

/**
 * Audit configuration model services.
 * <p>
 * This class allows to modify the audit configuration.
 * The audit configuration is based on a configuration file.
 * All methods that modify the audit configuration report the modifications
 * to the configuration file unless specified.
 * <p>
 * <b>Design note:</b><br>
 * This is a JAXB encapsulation class. This class must remain the only one to know the Jaxb classes corresponding to the audit
 * configuration XML file schema.
 * 
 * @author pvlaemyn
 */
@objid ("7a9f5e42-526b-4abd-a8ae-f8c0e00ed071")
public class OldAuditConfigurator {
    /**
     * The read/edited configuration file.
     */
    @objid ("fd048403-fd1a-46f3-ada4-2e2431781661")
    private File configFile;

    /**
     * instantiate an audit configurator from a file.
     * @param configFile the audit configuration file to handle.
     */
    @objid ("11095062-a564-4c69-ac82-d19e1567a325")
    public OldAuditConfigurator(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Create an audit configuration model from the read audit configuration.
     * @return the read audit configuration model.
     */
    @objid ("e33eff60-b5b9-47a2-a8ee-d7f12d066981")
    public AuditConfigurationModel createPrefModel() {
        // reload config in any case
        Configuration config = this.readConfig();
        
        AuditConfigurationModel prefModel = new AuditConfigurationModel();
        
        for (RuleGroup ruleGroup : config.getRuleGroup()) {
            String category = ruleGroup.getCategory();
            for (Rule ruleEntry : ruleGroup.getRule()) {
                final AuditSeverity severity = convertConfigSeverityToAuditSeverity(ruleEntry.getSeverity());
                final boolean enabled = ruleEntry.getStatus()==ActivationStatus.ENABLED;
                
                final AuditRule rulePref = new AuditRule(ruleEntry.getRuleId(), category, severity, enabled);
                
                rulePref.driverClass = ruleEntry.getDriver().getClazz();
        
                switch (ruleEntry.getStatus()) {
                case ENABLED:
                case DISABLED:
                    prefModel.add(rulePref);
                    break;
        
                case OBSOLETE:
                default:
                    // ignore it
                    break;
                }
            }
        
        }
        return prefModel;
    }

    /**
     * <li> Reads the current configuration model from the file,
     * <li> applies the given configuration to the read configuration
     * <li> save the modified configuration to the configuration file.
     * @param prefModel the configuration model to apply.
     */
    @objid ("fc6b8963-959a-41ab-9b3d-a0604432c35c")
    public void apply(AuditConfigurationModel prefModel) {
        Configuration config = this.readConfig();
        
        for (AuditRule rulePref : prefModel.getRules()){
            Rule rule = this.getRule(config, rulePref.category, rulePref.ruleId);         
            if (rule != null && rule.getStatus() != ActivationStatus.OBSOLETE) {         
                // update enablement
                rule.setStatus((rulePref.enabled) ? ActivationStatus.ENABLED : ActivationStatus.DISABLED);
                // update severity
                rule.setSeverity(this.convertAuditSeverityToConfigSeverity(rulePref.severity));
        
            }       
        }
        
        this.writeConfig(config, this.configFile);
    }

    /**
     * Save the current configuration in 'targetFile'.
     * @param targetFile the target configuration file path.
     */
    @objid ("a39ddc3f-06e5-434d-903a-062fd92845b5")
    public void saveAs(File targetFile) {
        Configuration config = this.readConfig();
        targetFile.getParentFile().mkdirs();
        this.writeConfig(config, targetFile);
    }

    /**
     * Converts a Severity level (from config file) into an AuditSeverity level (from Audit)
     * @param configSeverity @return
     */
    @objid ("a22ff97f-2d2f-44dd-9eca-914875247f49")
    private AuditSeverity convertConfigSeverityToAuditSeverity(Severity configSeverity) {
        switch (configSeverity) {
        case WARNING:
            return AuditSeverity.AuditWarning;
        case TIP:
            return AuditSeverity.AuditAdvice;
        case ERROR:
        default:
            return AuditSeverity.AuditError;
        
        }
    }

    @objid ("5072db85-b9f9-4d99-920d-195c21fcb054")
    private Severity convertAuditSeverityToConfigSeverity(AuditSeverity auditSeverity) {
        switch (auditSeverity) {
        case AuditWarning:
            return Severity.WARNING;
        case AuditAdvice:
            return Severity.TIP;
        case AuditError:
        default:
            return Severity.ERROR;
        
        }
    }

    @objid ("d3de927f-15e3-493c-9b40-7a826e1883ce")
    private Rule getRule(Configuration config, String category, String ruleId) {
        for (RuleGroup ruleGroup : config.getRuleGroup()) {
            if (ruleGroup.getCategory().equals(category)) {
                for (Rule ruleEntry : ruleGroup.getRule()) {
                    if (ruleEntry.getRuleId().equals(ruleId)) {
                        return ruleEntry;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Build a Configuration (Jaxb class) instance representing the current configuration file contents.
     * @return the JAXB audit configuration
     */
    @objid ("721c6312-b9c6-432b-83bd-c9891f6b5cf0")
    private Configuration readConfig() {
        Configuration config = null;
        
        // creation of the JAXBContext able to manage the package
        try (InputStream is = new BufferedInputStream(new FileInputStream(this.configFile))) {
            JAXBContext jc = JAXBContext.newInstance(Configuration.class);
            Unmarshaller u = jc.createUnmarshaller();
            config = (Configuration) u.unmarshal(is);
            return config;
        } catch (JAXBException e) {
            Audit.LOG.error(e);
        } catch (FileNotFoundException e) {
            Audit.LOG.error(e);
        } catch (IOException e) {
            Audit.LOG.error(e);
        }
        return null;
    }

    /**
     * Write the given JAXB configuration in 'targetFile'.
     */
    @objid ("6723b2cf-40ef-4d8f-bce7-b532106b1894")
    private void writeConfig(Configuration config, File targetFile) {
        // creation of the JAXBContext able to manage the package
        try (FileOutputStream os = new FileOutputStream(targetFile)) {
            JAXBContext jc = JAXBContext.newInstance(Configuration.class);
            Marshaller u = jc.createMarshaller();
            u.marshal(config, os);
        } catch (JAXBException e) {
            Audit.LOG.error(e);
        } catch (FileNotFoundException e) {
            Audit.LOG.error(e);
        } catch (IOException e) {
            Audit.LOG.error(e);
        }
    }

}

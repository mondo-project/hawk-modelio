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
                                    

package org.modelio.audit.preferences;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IRule;
import org.modelio.audit.engine.impl.AuditPlan;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.preferences.model.AuditConfigurationModel;
import org.modelio.audit.preferences.model.AuditRule;
import org.modelio.audit.service.AuditSeverity;

/**
 * Audit model controller.
 */
@objid ("8c2e1d6c-51c4-4a9c-bde8-eaf399b226a1")
public class AuditModelController {
    @objid ("31db854e-331d-42db-b57f-085427670dd2")
    private final AuditConfigurationModel model;

    @objid ("1bd668a5-bf4a-44f9-b240-976b16798095")
    private final Properties defaultConf;

    /**
     * Initialize a new controller for a new model.
     */
    @objid ("9845f1b0-12d0-4a05-a341-f4d154f7f65b")
    public AuditModelController() {
        this.model = new AuditConfigurationModel();
        this.defaultConf = new Properties();
    }

    /**
     * Creates a new controller that handles an existing model.
     * @param model the model to handle.
     */
    @objid ("3dc46443-0cf9-4c55-a673-a59a8f2215fc")
    public AuditModelController(AuditConfigurationModel model) {
        this.model = model;
        this.defaultConf = new Properties();
    }

    /**
     * Read the audit rules definitions from a property table with the following format:
     * <pre>ruleid = category,driver.class.name</pre>
     * <p>
     * The description bundle must contain rules description in keys formatted as
     * <code>'ruleid.description'</code>
     * @param rulesFile the configuration
     * @param descBundle the rule description bundle
     * @throws java.io.IOException in case of I/O failure
     */
    @objid ("03300bd7-25e3-4692-a115-df935dcc71f8")
    public void loadDefinitions(File rulesFile, ResourceBundle descBundle) throws IOException {
        Properties dict = new Properties();
        load(rulesFile, dict);
        
        for (Entry<Object, Object> entry : dict.entrySet()) {
            String ruleId = (String) entry.getKey();
            String[] v = ((String) entry.getValue()).split(",");
            
            if (v.length == 2) {
                String category = v[0];
                AuditRule rule = this.model.get(ruleId);
                if (rule == null) {
                    // Create new rule
                    rule = new AuditRule(ruleId, category, null, false);
                    rule.driverClass = v[1];
                    rule.description = getDescription(descBundle, ruleId);
                    this.model.add(rule);
                } else {
                    // Update existing rule
                    rule.driverClass = v[1];
                    rule.description = getDescription(descBundle, ruleId);
                    if (! category.equals(rule.category)) {
                        this.model.remove(rule);
                        rule.category = category;
                        this.model.add(rule);
                    }
                }
            }
        }
    }

    @objid ("95da2af7-8986-4b3c-a923-d41f23e74399")
    private static String getDescription(ResourceBundle descBundle, String ruleId) {
        try {
            return descBundle.getString(ruleId + ".description");
        } catch (MissingResourceException e) {
            return "!" + ruleId + ".description!";
        }
    }

    /**
     * Get the controlled audit model.
     * @return the audit model.
     */
    @objid ("d60d9e45-6cbc-45b8-aaad-8120a90adf9b")
    public AuditConfigurationModel getModel() {
        return this.model;
    }

    /**
     * Write the rules configurations in a {@link Properties}.
     * <p>
     * If the given properties default already contain the same configuration for a rule,
     * the given properties are not updated.
     * @param ret rules configuration.
     */
    @objid ("ff208ef8-0091-4256-bef9-dbfa30a090df")
    private void writeConfiguration(Properties ret) {
        for (AuditRule ruleEntry : this.model.getRules()) {
            
            if (ruleEntry.driverClass != null && ruleEntry.severity != null) {
                String sstatus = ruleEntry.enabled ? "enabled" : "disabled";
                final String value = sstatus+","+ruleEntry.severity.identifier();
                if (this.defaultConf==null || ! value.equals(this.defaultConf.getProperty(ruleEntry.ruleId)))
                    ret.setProperty(ruleEntry.ruleId, value);  
            } else {
                Audit.LOG.debug(ruleEntry.ruleId+" incomplete: driver="+ruleEntry.driverClass+", severity="+ruleEntry.severity+", enabled="+ruleEntry.enabled+ " category="+ruleEntry.category);
            }
        }
    }

    /**
     * Read the rule definitions in a property file:<ul>
     * <li> rules : ruleid = category,driver.class.name
     * @param ruleFile the destination file.
     * @throws java.io.IOException in case of I/O failure
     */
    @objid ("87f5b747-9db2-4c99-b957-9c79fa608666")
    public void writeRuleDefinitions(File ruleFile) throws IOException {
        Properties rules = new Properties();
        
        for (AuditRule ruleEntry : this.model.getRules()) {
            if (ruleEntry.driverClass != null) {
                rules.setProperty(ruleEntry.ruleId, ruleEntry.category+","+ruleEntry.driverClass);
            }
        }
        
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(ruleFile));) {
            rules.store(out, "Audit rule definitions file.\nFormat: ruleid = category,driver.class.name");
        }
    }

    @objid ("4e1956b2-07e6-414a-ac21-85717e4fb848")
    private void load(File f, Properties ret) throws IOException {
        try (final BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));){
            ret.load(is);
        }
    }

    /**
     * Writes the audit model configuration in the configuration file.
     * <p>
     * Rules that have the default configuration are not written in the file.
     * @param file the configuration file.
     * @throws java.io.IOException in case of I/O error.
     */
    @objid ("c811d837-5e37-4f7e-b6a0-aa830c69569f")
    public void writeConfiguration(File file) throws IOException {
        Properties ret = new Properties(this.defaultConf);
        
        writeConfiguration(ret);
        
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {
            ret.store(out, "Audit configuration file.\nFormat: ruleid = enabled|disabled|obsolete,tip|warning|error");
        }
    }

    /**
     * Read an audit configuration from a property table with the following format:
     * <pre>ruleid = enabled|disabled|obsolete,tip|warning|error</pre>
     * @param conf the configuration
     */
    @objid ("a356e9ef-97f4-46f4-b600-1eb4d9e39b31")
    private void applyAuditConfiguration(Properties conf) {
        for (Entry<Object, Object> entry : conf.entrySet()) {
            String ruleId = (String) entry.getKey();
            String[] v = ((String) entry.getValue()).split(",");
            
            if (v.length == 2) {
                AuditRule rule = this.model.get(ruleId);
                if (rule == null ){
                    rule = new AuditRule(ruleId, null, null, false);
                    this.model.add(rule);
                }
                
                try {
                    String sstate = v[0];
                    String sseverity = v[1];
                    rule.enabled = "enabled".equals(sstate);
                    rule.severity = AuditSeverity.fromIdentifier(sseverity);
                } catch (IllegalArgumentException e) {
                    Audit.LOG.warning("Invalid rule configuration:"+entry.toString());
                }
            } else {        
        
                Audit.LOG.warning("Invalid rule configuration:"+entry.toString());
            }
        }
    }

    /**
     * Load the given file as a the default configuration.
     * <p>
     * The default configuration is used on save to avoid writing the default configuration
     * in the target file.
     * @param defaultConfFile the default configuration file.
     * @throws java.io.IOException in case of I/O error
     */
    @objid ("696d0438-bd9e-4aaf-ad78-e71abc9adcd3")
    public void addDefaultConf(File defaultConfFile) throws IOException {
        load(defaultConfFile, this.defaultConf);
        applyAuditConfiguration(this.defaultConf);
    }

    /**
     * Applies the rule enablement state and the severity of the given configuration
     * to the handled configuration.
     * <p>
     * Rules present in the given configuration and absent in this configuration are added to this configuration.
     * Rules absent in the given configuration are kept as is.
     * @param auditConfiguration the configuration to apply.
     */
    @objid ("8c9154eb-2c0e-43c8-92e2-7e0e9a7bdff9")
    public void applyAuditConfiguration(AuditConfigurationModel auditConfiguration) {
        for (AuditRule r : auditConfiguration.getRules())  {
            AuditRule target = this.model.get(r.ruleId);
            if (target == null) {
                this.model.add(new AuditRule(r));
            } else {
                target.enabled = r.enabled;
                target.severity = r.severity;
            }
        }
    }

    /**
     * Applies the given audit configuration file.
     * @param file an audit configuration file.
     * @throws java.io.IOException in case of I/O error
     */
    @objid ("b03d9780-932c-452e-b03e-350e2dd09a16")
    public void applyAuditConfiguration(File file) throws IOException {
        Properties props = new Properties();
        load(file, props);
        
        applyAuditConfiguration(props);
    }

    @objid ("ed65ce4a-f49c-4ddf-89f4-e636511f42ea")
    public AuditPlan createPlan() {
        // reload config in any case
        
        AuditPlan auditPlan = new AuditPlan();
        for (AuditRule ruleEntry : this.model.getRules()) {
            if (ruleEntry.enabled && ruleEntry.driverClass != null) {
                try {
                    Class<?> ruleClass = Class.forName(ruleEntry.driverClass);
                    //System.out.println("Adding rule " + className + " " + ruleClass);
                    if (IRule.class.isAssignableFrom(ruleClass)) {
                        IRule rule = (IRule) ruleClass.newInstance();
                        rule.setSeverity(ruleEntry.severity);
                        rule.autoRegister(auditPlan);
                    }
                } catch (ClassNotFoundException e) {
                    // Should be an old deleted rule, no trace needed.
                } catch (InstantiationException e) {
                    Audit.LOG.error(e);
                } catch (IllegalAccessException e) {
                    Audit.LOG.error(e);
                }
            }
        }
        return auditPlan;
    }

}

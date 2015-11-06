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
                                    

package org.modelio.audit.preferences.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.service.AuditSeverity;

@objid ("cfdbd93f-1294-4f17-8519-df8a0d1d2494")
public class AuditRule {
    @objid ("99209ddd-9f5c-4e1c-bb3d-1268c9d86a3b")
    public boolean enabled;

    @objid ("8b92216b-d14d-4764-b742-9c238242203a")
    public String ruleId;

    @objid ("a5f0286c-937e-46e7-ab71-5ff4cc814f28")
    public String category;

    @objid ("9082049b-d962-4d6c-8eb2-6083f251fb52")
    public String description;

    @objid ("1b5363f2-08d0-4ec5-98a6-d000deb54310")
    public AuditSeverity severity;

    @objid ("77442999-e799-4b21-b03e-f92f79bdac51")
    public String driverClass;

    @objid ("09bf2625-960a-456e-8575-5f12917db9e9")
    public AuditRule(String ruleId, String category, AuditSeverity severity, boolean enabled) {
        this.ruleId = ruleId;
        this.category = category;
        this.severity = severity;
        this.enabled = enabled;
    }

    @objid ("4e4f3c9c-db66-4bdd-bded-b27534bddab9")
    public AuditRule(AuditRule anotherRule) {
        this.ruleId = anotherRule.ruleId;
        this.category = anotherRule.category;
        this.severity = anotherRule.severity;
        this.enabled = anotherRule.enabled;
        this.description = anotherRule.description;
        this.driverClass = anotherRule.driverClass;
    }

}

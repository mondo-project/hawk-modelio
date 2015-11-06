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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Audit rule category model.
 */
@objid ("5dee0e78-97c3-42cb-9aaf-fb8a86256294")
public class AuditCategory {
    @objid ("32f2cc5d-90f4-4cd5-b330-17af3baccdd4")
    private String name;

    @objid ("a836f660-1c6c-403c-8aa7-9293ee4ced33")
    private Set<AuditRule> rules;

    /**
     * Initialize a category.
     * @param categoryName category name
     */
    @objid ("dd86bc9d-fcea-431c-8d95-a8ff8ae67480")
    public AuditCategory(String categoryName) {
        this.rules = new TreeSet<>(new Comparator<AuditRule>() {
        
            @Override
            public int compare(AuditRule o1, AuditRule o2) {
                return o1.ruleId.compareTo(o2.ruleId);
            }
        });
        this.name = categoryName;
    }

    /**
     * Add a rule.
     * @param rulePref a rule
     */
    @objid ("8b55405a-7b63-4fe8-beda-96d41cbb0aa0")
    public void add(AuditRule rulePref) {
        this.rules.add(rulePref);
    }

    /**
     * Get the category rules.
     * @return the category rules.
     */
    @objid ("9102932c-a9bc-43da-84c5-912b1cd6b158")
    public List<AuditRule> getRules() {
        // Compatibility: return a list instead of a set
        return new ArrayList<>(this.rules);
    }

    /**
     * @return the category name.
     */
    @objid ("60e9623b-a3ff-4c1a-90de-bc64ebab1f4b")
    public String getName() {
        return this.name;
    }

    /**
     * Remove a rule
     * @param rulePref a rule
     */
    @objid ("3d045178-c06c-4bb3-957a-e96b894c7805")
    public void remove(AuditRule rulePref) {
        this.rules.remove(rulePref);
    }

}

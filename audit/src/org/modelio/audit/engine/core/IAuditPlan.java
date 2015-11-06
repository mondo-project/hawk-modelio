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
                                    

package org.modelio.audit.engine.core;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("f54b11ef-3906-40bd-bc09-dd5d78e380a3")
public interface IAuditPlan {
    @objid ("43cd7f9d-df7c-4c5b-9d23-e0689e45ec4c")
    void registerRule(String metaclass, IRule rule, int triggers);

    @objid ("a42a9c85-34a4-4073-bda9-927330d7c514")
    List<IRule> getRules(String metaclass, int trigger);

    @objid ("5060fce3-b078-44af-835a-ec528ac702f9")
    void registerRule(IRule rule);

    @objid ("eb7c83c0-d2b1-46fd-a904-09d1f7db3b0a")
    IRule getRuleById(String ruleId);

}

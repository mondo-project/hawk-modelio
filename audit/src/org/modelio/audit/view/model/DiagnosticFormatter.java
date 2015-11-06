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
                                    

package org.modelio.audit.view.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.plugin.Audit;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("7c0dd904-e19c-482a-97e7-11de654901fc")
public class DiagnosticFormatter {
    @objid ("8aec76fb-4e50-4c2c-bac5-c634d7521efc")
    protected static final ResourceBundle bundle = ResourceBundle.getBundle("auditrules");

    @objid ("f329c63c-59e7-4901-a527-5169a34dab31")
    public static String getMessage(IAuditEntry entry) {
        try {
            String pattern = bundle.getString(entry.getRuleId() + ".message");
            return MessageFormat.format(pattern, makeInfos(entry.getLinkedObjects()));
        } catch (MissingResourceException e) {
            Audit.LOG.warning("auditrules: no entry for rule " + entry.getRuleId() + ".message");
            return "!" + entry.getRuleId() + ".message!";
        }
    }

    @objid ("f1f4a4bc-0ae7-4e74-903b-f37b2fccbdf2")
    public static String getDescription(IAuditEntry entry) {
        try {
            return bundle.getString(entry.getRuleId() + ".description");
        } catch (MissingResourceException e) {
            Audit.LOG.warning("auditrules: no entry for rule " + entry.getRuleId() + ".description");
            return "!" + entry.getRuleId() + ".description!";
        }
    }

    @objid ("412fe267-1739-495d-a70d-1af908ac81f7")
    private static Object[] makeInfos(List<Object> linkedObjects) {
        ArrayList<Object> infos = new ArrayList<>();
        for (Object o : linkedObjects) {
            if (o instanceof ModelElement){
                ModelElement element = (ModelElement) o;
                if(!element.isDeleted())
                    infos.add(element.getName());
            }
            else
                infos.add(o);
        }
        return infos.toArray();
    }

}

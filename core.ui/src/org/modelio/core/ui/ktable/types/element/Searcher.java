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
                                    

package org.modelio.core.ui.ktable.types.element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * A Searcher is used to search for elements in an opened modeling session.
 */
@objid ("8dc471dd-c068-11e1-8c0a-002564c97630")
class Searcher {
    @objid ("a4c694cd-c068-11e1-8c0a-002564c97630")
    private String expression = "";

    @objid ("8dc471e6-c068-11e1-8c0a-002564c97630")
    private IMObjectFilter elementFilter = null;

    @objid ("7893f2ae-c202-11e1-80f8-001ec947ccaf")
    private IModel session = null;

    @objid ("aea195cd-c202-11e1-80f8-001ec947ccaf")
    private Class<? extends MObject> targetClass;

    @objid ("8dc471e7-c068-11e1-8c0a-002564c97630")
    @SuppressWarnings("unchecked")
    public Searcher(IModel session, Class<? extends MObject> targetClass, IMObjectFilter elementFilter) {
        this.session = session;
        
        if (targetClass==null)
            throw new IllegalArgumentException("Target class cannot be NULL");
        else if (targetClass.isArray())
            this.targetClass = (Class<? extends MObject>) targetClass.getComponentType();
        else
            this.targetClass = targetClass;
        
        this.elementFilter = elementFilter;
    }

    @objid ("8dc471f1-c068-11e1-8c0a-002564c97630")
    public String getExpression() {
        return this.expression;
    }

    @objid ("8dc471f5-c068-11e1-8c0a-002564c97630")
    public List<MObject> search() {
        List<MObject> rawResults = new ArrayList<> (this.session.findByClass(SmClass.getClass(this.targetClass.getSimpleName()), IModel.NODELETED));
        List<MObject> filteredResults = new ArrayList<>();
        Pattern p = Pattern.compile(this.expression);
        
        for (MObject e : rawResults) {
            if (e instanceof ModelElement) {
                ModelElement me = (ModelElement) e;
                if (p.matcher(me.getName()).matches() && this.elementFilter == null) {
                    filteredResults.add(e);
                } else if (p.matcher(me.getName()).matches() && this.elementFilter.accept(e)) {
                    filteredResults.add(e);
                }
            }
        }
        return filteredResults;
    }

    @objid ("8dc471fd-c068-11e1-8c0a-002564c97630")
    public void setExpression(String expression) {
        this.expression = expression;
    }

}

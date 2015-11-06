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
                                    

package org.modelio.vaudit.nsuse;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.impl.GenericFactory;

/**
 * Namespace use analyzer handler that systematically create a NamespaceUse link.
 */
@objid ("d598370f-ff5c-4412-92d9-874f8f163777")
class NsUseCreateHandler implements INsUseHandler {
    @objid ("98cfd626-2b1e-4ade-89f7-bbb15243ec1c")
    private IRepository repository;

    @objid ("46ffb7f0-daf8-413f-ba4e-d17ee31079dc")
    private GenericFactory genericFactory;

    @objid ("12f4755b-7f6f-43ed-91c7-c2c82dcfeb87")
    private NSUseAnalyser recurser;

    @objid ("f178fff9-e4fa-4faa-8878-1e170e41a23d")
    public NsUseCreateHandler(GenericFactory genericFactory, IRepository repository) {
        this.repository = repository;
        this.genericFactory = genericFactory;
        this.recurser = new NSUseAnalyser(this);
    }

    @objid ("42efc802-3267-418e-aefd-82d2784ace4b")
    @Override
    public void addNSUses(NameSpace aSource, NameSpace aDest, Element aCause) {
        if (aSource != null && aDest != null && aCause != null) {
            // Create the blue link
            NamespaceUse use = addNSUse(aSource, aDest, aCause);
            
            // Analyse the created blue link
            if (use != null) {
                this.recurser.buildFor(use);
            }
        }
    }

    /**
     * Return null if an existing NSU has been used
     * @param aSource the source NameSpace (the user).
     * @param aDest the destination NameSpace (used).
     * @param aCause the cause of the NameSpace use.
     * @return the created NameSpace use.
     */
    @objid ("8cc1f996-db0b-4484-8caf-3dbfdf8113ba")
    private NamespaceUse addNSUse(NameSpace aSource, NameSpace aDest, Element aCause) {
        //if (Vaudit.LOG.isDebugEnabled())
        //    Vaudit.LOG.debug("\t\taddNSUse %s\t%s\t cause=%s", aSource.getName(), aDest.getName(), aCause.toString());
        
        NamespaceUse aUse = null;
        if (aSource != aDest) {
            for (NamespaceUse ns : aSource.getUsedNsu()) {
                if (ns.isValid() && ns.getUsed() == aDest) {
                    ns.getCause().add(aCause);
                    return null;
                }
            }
        
            aUse = this.genericFactory.create(NamespaceUse.class, this.repository);
            if (aUse != null) {
                aUse.getCause().add(aCause);
                aUse.setUser(aSource);
                aUse.setUsed(aDest);
            }
        }
        return aUse;
    }

}

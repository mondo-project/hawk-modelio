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
                                    

package org.modelio.mda.infra.service;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.IPStatus;
import org.modelio.vcore.smkernel.IRStatus;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class is in charge of removing a Jxbv2Module from a project.
 */
@objid ("a0a6bbf9-8fac-4a06-9132-0d188c62ff9d")
class ModuleRemover {
    @objid ("dae2cb9b-c970-4bc5-827c-95e502aaa0ab")
    public static void remove(ModuleComponent module) throws ModuleException {
        CoreSession session = CoreSession.getSession(module);
        
        try (ITransaction t = session.getTransactionSupport().createTransaction("Remove Jxbv2Module")) {
        
            //checkCanRemoveModule(module, null);
            deleteModel(module);
        
            t.commit();
        } catch (Error e) {
            throw new ModuleException("Jxbv2Module could not be deleted", e);
        }
    }

    @objid ("5555d3e9-5b4a-47df-986c-49c7123f6c57")
    private static void deleteModel(ModuleComponent module) {
        // Unlock module and all children
        unlockRecursive((SmObjectImpl) module);
        module.delete();
    }

    @objid ("5e126601-0a08-4e77-b7d9-463fce4bd7cc")
    private static void unlockRecursive(SmObjectImpl obj) {
        for (SmObjectImpl child : obj.getCompositionChildren()) {
            unlockRecursive(child);
        }
        obj.setRStatus(IRStatus.RMASK_MODIFIABLE_REQUIRED, IRStatus.RMASK_MODIFIABLE_FORBIDDEN, 0);
        obj.setPStatus(IPStatus.PMASK_MODIFIABLE_REQUIRED, 0, 0);
    }

    @objid ("b8c81920-3c71-46e6-8bfd-06470ff7d2d6")
    private static void checkCanRemoveModule(ModuleComponent toRemove, IModuleRegistry registry) throws ModuleException {
        // Whether this module is required (strong dependency) by another module
        if (!toRemove.getImpacted().isEmpty()) {
            StringBuilder builder = new StringBuilder(toRemove.getName());
            builder.append(" cannot be removed since it if used by the following modules:\n");
            for (ModuleComponent requiring : toRemove.getImpacted()) {
                IModule loadedMdac = registry.getLoadedModule(requiring);
                builder.append(loadedMdac.getName());
                builder.append("\n");
            }
            throw new ModuleException(builder.toString());
        }
        
        // Whether stereotypes of this module are still used?
        for (MObject o : toRemove.getCompositionChildren()) {
            if (o instanceof Stereotype) {
                Stereotype stereotype = (Stereotype) o;
                if (!stereotype.getExtendedElement().isEmpty()) {
                    StringBuilder builder = new StringBuilder(stereotype.getName());
                    builder.append(" is still used, module ");
                    builder.append(toRemove.getName());
                    builder.append(" cannot be removed. Extended elements list follows:\n");
                    for (ModelElement extended : stereotype.getExtendedElement()) {
                        builder.append(extended.getName());
                        builder.append("\n");
                    }
                    throw new ModuleException(builder.toString());
                }
            }
        }
    }

}

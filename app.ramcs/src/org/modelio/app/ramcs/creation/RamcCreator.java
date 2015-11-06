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
                                    

package org.modelio.app.ramcs.creation;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.ramcs.plugin.AppRamcs;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.factory.ModelFactory;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command that creates a model component (RAMC).
 */
@objid ("8c24adbb-8a92-4d39-8228-a38c2a9171e2")
public class RamcCreator {
    @objid ("1ea41a21-bebf-47e0-bede-6dbee8f77dd5")
    public static Artifact create(Package rootPackage) {
        CoreSession session = CoreSession.getSession(rootPackage);
        IModelFactory factory = ModelFactory.getFactory(session);
        rootPackage.getCompositionChildren();
        List<Stereotype> stereotypes = factory.findStereotype("ModelerModule", "ModelComponentArchive", Metamodel.getMClass(Artifact.class));
        Stereotype mca = stereotypes.isEmpty()? null : stereotypes.get(0);
        Artifact artifact = null;
        try (ITransaction t = session.getTransactionSupport().createTransaction("Create a model component")) {
            
            artifact = factory.createArtifact(computeDefaultName(rootPackage), rootPackage, mca);
            t.commit();
            
        }
        return  artifact;
    }

    @objid ("6ff173c6-89c4-4c9a-8de7-ee25f175eee5")
    private static String computeDefaultName(Package rootPackage) {
        String baseName = AppRamcs.I18N.getString("NewRamcDefaultName");
        int index = 1;
        boolean exist = rootPackage.getCompositionChildren().size()>0;       
        while (exist) {
            for (MObject child : rootPackage.getCompositionChildren()) {
                if (child.getName().equals(baseName + index)) {
                    exist = true;
                    index++;
                    break;
                } else {
                    exist = false;
                }
            }
        }
        return baseName + index;
    }

}

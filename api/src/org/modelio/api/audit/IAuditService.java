/*
 * Copyright 2013 Modeliosoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */  
                                    

package org.modelio.api.audit;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Audit services.
 */
@objid ("4be0dc32-6bf5-11e0-a371-001ec947cd2a")
public interface IAuditService {
    /**
     * Launch the core checker on 'element'.
     * @param element The element to audit.
     * @return true if the check does detect blocking error and false otherwise.
     */
    @objid ("a4058b97-0ecc-11e2-96c4-002564c97630")
    boolean check(MObject element);

    /**
     * Post 'element' in the audit check queue. The {@link Element} will be audited asynchronously by the Audit system and
     * results posted in the audit results view.
     * @param element The element to audit
     */
    @objid ("a405b2aa-0ecc-11e2-96c4-002564c97630")
    void audit(final MObject element);

    /**
     * Launch the core checker on all elements created and modified in a transaction.
     * @param element The transaction to audit.
     * @return true if the check does detect blocking error and false otherwise.
     */
    @objid ("15d2c340-1df4-11e2-bcbe-002564c97630")
    boolean check(ITransaction transaction);

}

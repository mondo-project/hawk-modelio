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
                                    

package org.modelio.diagram.elements.core.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.EContextService;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.elements.core.obfactory.IModelLinkFactory;
import org.modelio.diagram.elements.core.obfactory.ModelLinkFactory;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Makes the link between the Gm model and the Ob model.
 * 
 * @author cma
 */
@objid ("8084e16d-1dec-11e2-8cad-001ec947c8cc")
public class ModelManager {
    @objid ("8084e16f-1dec-11e2-8cad-001ec947c8cc")
    private final ICoreSession session;

    @objid ("8087436f-1dec-11e2-8cad-001ec947c8cc")
    private final IModelLinkFactory modelLinkFactory;

    @objid ("80874370-1dec-11e2-8cad-001ec947c8cc")
    private final IMModelServices modelServices;

    @objid ("5dccea2d-ff30-4181-a9fe-4ccbfe5e7491")
    private final EContextService contextService;

    @objid ("79b94beb-a3cd-4099-bc70-2c3d9dd32ef9")
    private final IActivationService activationService;

    /**
     * Create a model manager.
     * @param session a modeling session.
     * @param modelServices the model services for the session.
     */
    @objid ("80874372-1dec-11e2-8cad-001ec947c8cc")
    public ModelManager(IEclipseContext context) {
        this.session = context.get(IProjectService.class).getSession();
        this.modelServices = context.get(IMModelServices.class);
        this.contextService = context.get(EContextService.class);
        this.activationService = context.get(IActivationService.class);
        
        this.modelLinkFactory = new ModelLinkFactory(this.modelServices);
    }

    /**
     * Get the Ob link factory.
     * @return the link factory.
     */
    @objid ("80874378-1dec-11e2-8cad-001ec947c8cc")
    public IModelLinkFactory getModelLinkFactory() {
        return this.modelLinkFactory;
    }

    /**
     * Get the modeling session.
     * @return the modeling session.
     */
    @objid ("8087437d-1dec-11e2-8cad-001ec947c8cc")
    public ICoreSession getModelingSession() {
        return this.session;
    }

    /**
     * Resolve an MRef.
     * @param <E>
     * the type of the element, makes an automatic cast.
     * @param ref The reference to resolve
     * @return the found MObject or <i>null</i> if the element is not present in the project.
     */
    @objid ("80874382-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    public <E extends Element> E resolveRef(MRef ref) {
        return (E) this.getModelingSession().getModel().findByRef(ref, IModel.NODELETED);
    }

    /**
     * Get the model factory used to create a model object
     * in the same repository as the given object.
     * @param referent a model object
     * @return a model factory.
     */
    @objid ("8087438a-1dec-11e2-8cad-001ec947c8cc")
    public IModelFactory getModelFactory(MObject referent) {
        return this.modelServices.getModelFactory();
    }

    /**
     * Get the model services.
     * @return the model services.
     */
    @objid ("78b5a580-598c-11e2-8539-00137282c51b")
    public IMModelServices getModelServices() {
        return this.modelServices;
    }

    @objid ("7f2d7c50-bec3-4126-b425-e2cb58866454")
    public EContextService getContextService() {
        return this.contextService;
    }

    @objid ("227c2c9f-6f33-4516-bcc9-ab88159c61c1")
    public IActivationService getActivationService() {
        return this.activationService;
    }

}

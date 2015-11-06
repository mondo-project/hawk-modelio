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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.script.ScriptEngine;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.diagram.IDiagramCustomizer;
import org.modelio.api.diagram.tools.IAttachedBoxCommand;
import org.modelio.api.diagram.tools.IBoxCommand;
import org.modelio.api.diagram.tools.ILinkCommand;
import org.modelio.api.diagram.tools.IMultiLinkCommand;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.module.DefaultModuleSession;
import org.modelio.api.module.ILicenseInfos.Status;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IModuleAPIConfiguration;
import org.modelio.api.module.IModuleSession;
import org.modelio.api.module.IModuleUserConfiguration;
import org.modelio.api.module.IParameterEditionModel;
import org.modelio.api.module.IPeerModule;
import org.modelio.api.module.LicenseInfos;
import org.modelio.api.module.commands.ActionLocation;
import org.modelio.api.module.commands.IModuleAction;
import org.modelio.api.module.contrib.WizardContribution;
import org.modelio.api.module.diagrams.DiagramCustomizationDescriptor;
import org.modelio.api.module.diagrams.DiagramToolDescriptor;
import org.modelio.api.module.paramEdition.DefaultParametersEditionModel;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.gproject.ramc.core.model.IModelComponent;
import org.modelio.gproject.ramc.core.packaging.IModelComponentContributor;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.vbasic.version.Version;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class is instantiated when a module is incomplete:
 * <ul>
 * <li>main Java class cannot be loaded or instantiated for any reason.</li>
 * <li>module component is missing.</li>
 * </ul>
 */
@objid ("55ed822f-2f2d-11e2-8f16-002564c97630")
public class BrokenModule implements IModule {
    @objid ("8d85794d-2f2d-11e2-8f16-002564c97630")
    private String moduleName;

    @objid ("8d79928b-2f2d-11e2-8f16-002564c97630")
    private IModuleSession moduleSession;

    @objid ("8d79928c-2f2d-11e2-8f16-002564c97630")
    private IPeerModule peerModule;

    @objid ("8d79928d-2f2d-11e2-8f16-002564c97630")
    private IModuleUserConfiguration moduleConfiguration;

    @objid ("8d79928e-2f2d-11e2-8f16-002564c97630")
    protected IParameterEditionModel parameterEditionModel;

    @objid ("8d799290-2f2d-11e2-8f16-002564c97630")
    private Version moduleVersion;

    /**
     * Instantiate a fake module.
     * the module configuration
     * @param modelingSession a modeling session.
     * @param moduleName the module name
     * @param moduleVersion the module version
     * @param moduleUserConfiguration the user version of the module configuration
     * @param moduleApiConfiguration the api version of the module configuration
     */
    @objid ("8d799291-2f2d-11e2-8f16-002564c97630")
    public BrokenModule(final IModelingSession modelingSession, final String moduleName, Version moduleVersion, final IModuleUserConfiguration moduleUserConfiguration, final IModuleAPIConfiguration moduleApiConfiguration) {
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
        this.moduleConfiguration = moduleUserConfiguration;
        this.moduleSession = new DefaultModuleSession(this);
        this.peerModule = new BrokenPeerModule(moduleName, moduleVersion, moduleApiConfiguration);
    }

    /**
     * Get the configuration associated to this module.
     * 
     * Jxbv2Module configuration provide access to module parameter and resource paths
     * @see IModuleUserConfiguration
     * @return the module configuration.
     */
    @objid ("8d79929d-2f2d-11e2-8f16-002564c97630")
    @Override
    public IModuleUserConfiguration getConfiguration() {
        return this.moduleConfiguration;
    }

    /**
     * Used to return the module description.
     * @return The module description
     */
    @objid ("8d7bf3cb-2f2d-11e2-8f16-002564c97630")
    @Override
    public String getDescription() {
        return "Broken module: " + this.moduleName + " " + this.moduleVersion;
    }

    /**
     * Get the image provided by the module for a given stereotype. The module should return an image if the stereotype
     * is provided by itself, null in the other case. The image life cycle must be handled by the module.
     * @param stereotype a stereotype
     * @param type the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("8d7bf3d1-2f2d-11e2-8f16-002564c97630")
    @Override
    public Image getImage(Stereotype stereotype, ImageType type) {
        return null;
    }

    /**
     * Get the Jython scripting engine configured for having access to all the module classes and the public classes of
     * all required modules.
     * 
     * The following variables are already bound:
     * <ul>
     * <li>SESSION : the MDA modeling session</li>
     * <li> {@link IModule} MODULE : this module</li>
     * <li> {@link ClassLoader} CLASSLOADER : the class loader of the module</li>
     * </ul>
     * @see <a href="http://www.jython.org" > The Jython project homepage</a>
     * @return The Jython scripting engine.
     */
    @objid ("8d7bf3d9-2f2d-11e2-8f16-002564c97630")
    @Override
    public ScriptEngine getJythonEngine() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the module label that is displayed in dialog boxes and other GUIU parts.
     * @return The module label.
     */
    @objid ("8d7bf3de-2f2d-11e2-8f16-002564c97630")
    @Override
    public String getLabel() {
        return this.getName();
    }

    /**
     * Always returns null.
     * @return <code>null</code>.
     */
    @objid ("8d7bf3e4-2f2d-11e2-8f16-002564c97630")
    @Override
    public ModuleComponent getModel() {
        return null;
    }

    /**
     * Used to return the module name.
     * <p>
     * <p>
     * The module name corresponds to the name of the module, as defined in the <i>MDA Designer<i> tool.
     * @return The module name
     */
    @objid ("8d7bf3ea-2f2d-11e2-8f16-002564c97630")
    @Override
    public String getName() {
        return this.moduleName;
    }

    /**
     * Get the parameters model as it must be shown in the module parameters edition dialog.
     * @return The parameters edition model.
     */
    @objid ("8d7bf3f0-2f2d-11e2-8f16-002564c97630")
    @Override
    public IParameterEditionModel getParametersEditionModel() {
        if (this.parameterEditionModel == null)
            this.parameterEditionModel = new DefaultParametersEditionModel(this);
        return this.parameterEditionModel;
    }

    /**
     * Returns the peer module, connected to this module.
     * <p>
     * The peer module represents the public services of this current module.
     * @return The associated peer module
     */
    @objid ("8d7bf3f6-2f2d-11e2-8f16-002564c97630")
    @Override
    public IPeerModule getPeerModule() {
        return this.peerModule;
    }

    /**
     * Returns the session that is connected to the module.
     * <p>
     * <p>
     * The developer can:
     * <p>
     * 
     * <ul>
     * <li>return <code>this</code> and redefine all the operations directly in the module definition.</li>
     * <li>return an new instance of IModuleSession and implement all the needed operations.</li>
     * </ul>
     * @see IModuleSession
     * @return the session that is connected to the module
     */
    @objid ("8d7bf3fc-2f2d-11e2-8f16-002564c97630")
    @Override
    public IModuleSession getSession() {
        return this.moduleSession;
    }

    /**
     * Used to return the module version.
     * @return The module version
     */
    @objid ("8d7e5527-2f2d-11e2-8f16-002564c97630")
    @Override
    public Version getVersion() {
        return this.moduleVersion;
    }

    /**
     * Method automatically called just after the creation of the module.
     * <p>
     * <p>
     * 
     * The module is automatically instantiated at the beginning of the mda lifecycle and the constructor implementation
     * is not accessible to the module developer.
     * <p>
     * <p>
     * 
     * The <code>init</code> method allows the developer to execute the desired initialization code at this step. For
     * example, this is the perfect place to register any IViewpoint this module provides.
     * <p>
     * <p>
     * 
     * This method should never be called by the developer because it is already invoked by the tool.
     */
    @objid ("8d7e552d-2f2d-11e2-8f16-002564c97630")
    @Override
    public void init() {
        // Nothing to do.
    }

    /**
     * Returns the minimum Modelio version that authorize the Jxbv2Module to be activated.
     * @return The minimum Modelio version
     */
    @objid ("8d7e5531-2f2d-11e2-8f16-002564c97630")
    @Override
    public Version getRequiredModelioVersion() {
        return new Version("0.0.0");
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("8d7e5537-2f2d-11e2-8f16-002564c97630")
    @Override
    public Collection<IModuleAction> getModuleActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Register a module action for the contextual popupmenu(s) of the application.
     * @param location The action insertion point in the popupmenu (see {@link ActionLocation})
     * @param action Action to store
     */
    @objid ("8d7e5540-2f2d-11e2-8f16-002564c97630")
    @Override
    public void registerAction(ActionLocation location, IModuleAction action) {
        // Do nothing.
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("8d7e5546-2f2d-11e2-8f16-002564c97630")
    @Override
    public List<IModuleAction> getActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Returns the modeling session this module is loaded into.
     * @return the modeling session this module is loaded into.
     */
    @objid ("8d7e554f-2f2d-11e2-8f16-002564c97630")
    @Override
    public IModelingSession getModelingSession() {
        return null;
    }

    /**
     * Return the defined property pages
     * @return The collection of property pages
     */
    @objid ("8d7e5555-2f2d-11e2-8f16-002564c97630")
    @Override
    public Collection<IModulePropertyPage> getPropertyPages() {
        return Collections.emptyList();
    }

    /**
     * Get the ModelComponent contributor associated to this module.
     * @see IModelComponentContributor
     * @return the module configuration.
     */
    @objid ("8d7e555d-2f2d-11e2-8f16-002564c97630")
    @Override
    public IModelComponentContributor getModelComponentContributor(IModelComponent mc) {
        return null;
    }

    /**
     * Returns current runtime state of this module.
     * @return the current runtime state of this module.
     */
    @objid ("8d7e5563-2f2d-11e2-8f16-002564c97630")
    @Override
    public ModuleRuntimeState getState() {
        return ModuleRuntimeState.Incompatible;
    }

    /**
     * Sets the runtime state of this module.
     * <p>
     * <em>This method is not meant to be called by clients. Use of this method can result in unspecified behaviours.</em>
     * </p>
     * @param runtimeState the new runtime state of this module.
     */
    @objid ("8d7e5569-2f2d-11e2-8f16-002564c97630")
    public void setState(final ModuleRuntimeState runtimeState) {
        throw new UnsupportedOperationException(this.moduleName + ": runtime state of this moduel cannot be changed since it is incompatible with this version of Modelio");
    }

    /**
     * Method automatically called just before the disposal of the module.
     * <p>
     * <p>
     * 
     * 
     * The <code>uninit</code> method allows the developer to execute the desired un-initialization code at this step.
     * For example, if IViewpoints have been registered in the {@link #init()} method, this method is the perfect place
     * to remove them.
     * <p>
     * <p>
     * 
     * This method should never be called by the developer because it is already invoked by the tool.
     */
    @objid ("8d7e556e-2f2d-11e2-8f16-002564c97630")
    @Override
    public void uninit() {
        // Nothing to do.
    }

    @objid ("8d7e5572-2f2d-11e2-8f16-002564c97630")
    @Override
    public final Image getModuleImage() {
        return null;
    }

    /**
     * Get the path to the image representing the module.
     * @return a path relative to the module's resource path.
     */
    @objid ("8d7e5577-2f2d-11e2-8f16-002564c97630")
    @Override
    public String getModuleImagePath() {
        return "";
    }

    @objid ("b20e07ea-7964-4246-a269-910f0462ac91")
    @Override
    public List<DiagramToolDescriptor> getDiagramTools() {
        return Collections.emptyList();
    }

    @objid ("2b370b80-d4e5-4b78-85d2-f6891189ce0e")
    @Override
    public List<DiagramCustomizationDescriptor> getDiagramCustomizations() {
        return Collections.emptyList();
    }

    @objid ("96162d4e-16cf-425d-b203-d71f36e60b2e")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IBoxCommand handler) {
        // not defined for broken modules
    }

    @objid ("97dbb3ee-ef6e-4629-8dc3-73ed08bbe155")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IAttachedBoxCommand handler) {
        // not defined for broken modules
    }

    @objid ("7c0f74f6-a833-4f6c-b4c6-1717adb68856")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, ILinkCommand handler) {
        // not defined for broken modules
    }

    @objid ("401707a0-2bd2-49bd-891a-7f543d713490")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IMultiLinkCommand handler) {
        // not defined for broken modules
    }

    @objid ("48c7c719-127e-4366-b4a1-8866388d2250")
    @Override
    public void registerDiagramCustomization(Stereotype stereotype, Class<? extends AbstractDiagram> baseDiagramClass, IDiagramCustomizer customizer) {
        // not defined for broken modules
    }

    @objid ("e818050b-7c1b-4404-8ed6-587e11bc6959")
    @Override
    public String getLabel(Stereotype stereotype) {
        return stereotype.getName();
    }

    @objid ("cc99f6fb-c14f-4490-9b28-c52f9698ecfd")
    @Override
    public String getLabel(TagType tagType) {
        return tagType.getName();
    }

    @objid ("d6d18047-a011-431a-b245-51ab91e15ce6")
    @Override
    public String getLabel(NoteType noteType) {
        return noteType.getName();
    }

    @objid ("61fe653f-9afb-4792-b5ec-a6436c40a3f3")
    @Override
    public String getLabel(ExternDocumentType docType) {
        return docType.getName();
    }

    @objid ("0cf8586b-c716-4295-998e-43cfd7ca108c")
    @Override
    public String getLabel(String key) {
        return key;
    }

    @objid ("173d5386-8def-43d2-88ed-e606700709b6")
    @Override
    public LicenseInfos getLicenseInfos() {
        return new LicenseInfos(Status.UNDEFINED, null, "");
    }

    @objid ("58ff62c5-fc6c-49ab-9058-5bd45e585968")
    @Override
    public <I> I instanciateExternProcessor(String className, Class<I> clazz, Object... initargs) {
        return null;
    }

    @objid ("e1f84c7a-f9cc-408e-90a8-c3d6dafa1615")
    @Override
    public List<WizardContribution> getDiagramWizardContributions() {
        return Collections.emptyList();
    }

    @objid ("5c5026e7-eb82-4da1-b2c7-c02d0eafa0da")
    @Override
    public void registerDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

    @objid ("60192a53-93ae-4d38-8df9-708fe7510b81")
    @Override
    public void unregisterDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

}

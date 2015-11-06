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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.script.ScriptEngine;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
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
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.vbasic.version.Version;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class represents special modules that just contains extensions.
 * LocalModule is a FakeModule.
 */
@objid ("b317e521-f11c-11e1-af52-001ec947c8cc")
public class FakeModule implements IModule {
    /**
     * Runtime state, defaults to Started.
     */
    @objid ("b419631f-f11c-11e1-af52-001ec947c8cc")
    private ModuleRuntimeState runtimeState = ModuleRuntimeState.Started;

    @objid ("b317e523-f11c-11e1-af52-001ec947c8cc")
    private IModuleSession moduleSession;

    @objid ("b317e524-f11c-11e1-af52-001ec947c8cc")
    private IPeerModule peerModule;

    @objid ("b317e525-f11c-11e1-af52-001ec947c8cc")
    private IModuleUserConfiguration moduleConfiguration;

    @objid ("b317e526-f11c-11e1-af52-001ec947c8cc")
    protected IParameterEditionModel parameterEditionModel;

    @objid ("b317e528-f11c-11e1-af52-001ec947c8cc")
    private ModuleComponent moduleComponent;

    @objid ("8d6a9729-03e6-11e2-8e1f-001ec947c8cc")
    private ImageRegistry imageRegistry;

    /**
     * Instantiate a fake module.
     * @param modelingSession a modeling session.
     * @param moduleHandle the module model element.
     * @param moduleUserConfiguration the user version of the module configuration
     * @param moduleApiConfiguration the api version of the module configuration
     */
    @objid ("b317e52b-f11c-11e1-af52-001ec947c8cc")
    public FakeModule(IModelingSession modelingSession, ModuleComponent moduleHandle, IModuleUserConfiguration moduleUserConfiguration, IModuleAPIConfiguration moduleApiConfiguration) {
        this.moduleComponent = moduleHandle;
        this.moduleConfiguration = moduleUserConfiguration;
        this.moduleSession = new DefaultModuleSession(this);
        this.peerModule = new DefaultPeerModule(moduleHandle, moduleApiConfiguration);
        
        Display.getDefault().syncExec(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                FakeModule.this.imageRegistry = new ImageRegistry();
            }
        });
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("b317e531-f11c-11e1-af52-001ec947c8cc")
    @Override
    public List<IModuleAction> getActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Get the configuration associated to this module.
     * 
     * Jxbv2Module configuration provide access to module parameter and resource paths
     * @see IModuleUserConfiguration
     * @return the module configuration.
     */
    @objid ("b317e53a-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModuleUserConfiguration getConfiguration() {
        return this.moduleConfiguration;
    }

    /**
     * Used to return the module description.
     * @return The module description
     */
    @objid ("b317e540-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getDescription() {
        try {
            return this.getManifestBundle().getString("ModuleDescription");
        } catch (MissingResourceException e) {
            MdaInfra.LOG.warning(MdaInfra.PLUGIN_ID, this.getName()
                    + " module: " + e.toString());
            return "";
        }
    }

    /**
     * Get the image descriptor provided by the module for a given stereotype. The module should return an image if the
     * stereotype is provided by itself, null in the other case.
     * @param stereotype a stereotype
     * @param imageType the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("b317e545-f11c-11e1-af52-001ec947c8cc")
    private ImageDescriptor getImageDescriptor(Stereotype stereotype, ImageType imageType) {
        // If the stereotype is not owned by the current module we return null.
        if (!this.isStereotypeOwner(stereotype)) {
            return null;
        }
        
        ImageDescriptor desc = null;
        
        Path moduleDirectory = this.getConfiguration().getModuleResourcesPath();
        
        String relativePath = null;
        
        if (imageType == ImageType.ICON) {
            relativePath = stereotype.getIcon();
        } else if (imageType == ImageType.IMAGE) {
            relativePath = stereotype.getImage();
        }
        
        if (relativePath == null || relativePath.equals("")) {
            return null;
        }
        
        URL imageUrl;
        try {
            Path imageFile = moduleDirectory.resolve(relativePath);
        
            // Compatibility with Modelio 1.2 : stereotype image path might
            // start with the module name, that concludes the module directory.
            if (!Files.isRegularFile(imageFile)
                    && relativePath.startsWith(moduleDirectory.getFileName().toString())) {
                imageFile = moduleDirectory.getParent().resolve(relativePath);
            }
        
            if (Files.isRegularFile(imageFile)) {
                imageUrl = imageFile.toUri().toURL();
                desc = ImageDescriptor.createFromURL(imageUrl);
                if (desc != ImageDescriptor.getMissingImageDescriptor()) {
                    return desc;
                } else {
                    return null;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * Get the image provided by the module for a given stereotype. The module should return an image if the stereotype
     * is provided by itself, null in the other case. The image life cycle must be handled by the module.
     * @param stereotype a stereotype
     * @param type the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("b317e54d-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Image getImage(Stereotype stereotype, ImageType type) {
        // If only the stereotype was specified we search directly from it.
        if (stereotype != null) {
            String key = getImageKey(stereotype, type);
        
            Image image = this.imageRegistry.get(key);
        
            if (image == null) {
                ImageDescriptor desc = this
                        .getImageDescriptor(stereotype, type);
                if (desc != null) {
                    this.imageRegistry.put(key, desc);
                    image = this.imageRegistry.get(key);
                }
            }
            return image;
        } else {
            return null;
        }
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
    @objid ("b317e554-f11c-11e1-af52-001ec947c8cc")
    @Override
    public ScriptEngine getJythonEngine() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the module label that is displayed in dialog boxes and other GUIU parts.
     * @return The module label.
     */
    @objid ("b317e559-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getLabel() {
        try {
            return this.getManifestBundle().getString("ModuleLabel");
        } catch (MissingResourceException e) {
            MdaInfra.LOG.error(MdaInfra.PLUGIN_ID, this.getName() + " module: "
                    + e.toString());
            return this.getName();
        }
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("b317e55e-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Collection<IModuleAction> getModuleActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Always returns null.
     * @return an Jxbv2Image for this module.
     */
    @objid ("b317e567-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Image getModuleImage() {
        return null;
    }

    /**
     * Get the ModelComponent contributor associated to this module.
     * @see IModelComponentContributor
     * @return the module configuration.
     */
    @objid ("b31a4770-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModelComponentContributor getModelComponentContributor(IModelComponent mc) {
        return null;
    }

    /**
     * Returns the {@link ModuleComponent} model associated with this module.
     * @return the {@link ModuleComponent} model associated with this module.
     */
    @objid ("b31a4776-f11c-11e1-af52-001ec947c8cc")
    @Override
    public ModuleComponent getModel() {
        return this.moduleComponent;
    }

    /**
     * Returns the modeling session this module is loaded into.
     * @return the modeling session this module is loaded into.
     */
    @objid ("b31a477c-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModelingSession getModelingSession() {
        return null;
    }

    /**
     * Used to return the module name.
     * <p>
     * <p>
     * The module name corresponds to the name of the module, as defined in the <i>MDA Designer<i> tool.
     * @return The module name
     */
    @objid ("b31a4782-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getName() {
        return this.moduleComponent.getName();
    }

    /**
     * Get the parameters model as it must be shown in the module parameters edition dialog.
     * @return The parameters edition model.
     */
    @objid ("b31a4788-f11c-11e1-af52-001ec947c8cc")
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
    @objid ("b31a478e-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IPeerModule getPeerModule() {
        return this.peerModule;
    }

    /**
     * Return the defined property pages
     * @return The collection of property pages
     */
    @objid ("b31a4794-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Collection<IModulePropertyPage> getPropertyPages() {
        return Collections.emptyList();
    }

    /**
     * Returns the minimum Modelio version that authorize the Jxbv2Module to be activated.
     * @return The minimum Modelio version
     */
    @objid ("b31a479c-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Version getRequiredModelioVersion() {
        return new Version("0.0.0");
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
    @objid ("b31a47a2-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModuleSession getSession() {
        return this.moduleSession;
    }

    /**
     * Used to return the module version.
     * @return The module version
     */
    @objid ("b31a47a8-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Version getVersion() {
        // TODO FPO getVersion
        // return new Version(this.moduleHandle.getVersion());
        return null;
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
    @objid ("b31a47b7-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void init() {
        // Nothing to do.
    }

    /**
     * Register a module action for the contextual popupmenu(s) of the application.
     * @param location The action insertion point in the popupmenu (see {@link ActionLocation})
     * @param action Action to store
     */
    @objid ("b31a47bb-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void registerAction(ActionLocation location, IModuleAction action) {
        // do nothing
    }

    @objid ("b31a47c1-f11c-11e1-af52-001ec947c8cc")
    private static String getImageKey(Stereotype stereotype, ImageType imageType) {
        return "module." + stereotype.getName() + "." + imageType.name();
    }

    /**
     * Get the {@link ResourceBundle} corresponding to the localized
     * manifest/manifest.properties file in the module resources.
     * @return the resource bundle
     * @throws java.util.MissingResourceException if the file is not found
     */
    @objid ("b31ca9cd-f11c-11e1-af52-001ec947c8cc")
    private ResourceBundle getManifestBundle() throws MissingResourceException {
        final Path manifestDir = this.getConfiguration()
                .getModuleResourcesPath();
        try (final URLClassLoader cl = new URLClassLoader(
                new URL[] { manifestDir.toUri().toURL() })) {
            // Create a class loader initialized with the 'manifest' directory
            // in module resource,
            // then give it to ResourceBundle.getBundle(...)
            return ResourceBundle.getBundle("module", Locale.getDefault(), cl);
        } catch (MalformedURLException e) {
            throw new MissingResourceException(e.getLocalizedMessage(),
                    "module", "");
        } catch (MissingResourceException e) {
            MissingResourceException e2 = new MissingResourceException(
                    e.getLocalizedMessage() + " in '" + manifestDir.toUri()
                            + "'", e.getClassName(), e.getKey());
            e2.initCause(e);
            throw e2;
        } catch (IOException e) {
            MdaInfra.LOG.error(e);
        }
        return null;
    }

    @objid ("b31ca9d1-f11c-11e1-af52-001ec947c8cc")
    private boolean isStereotypeOwner(Stereotype stereotype) {
        final Profile profile = stereotype.getOwner();
        if (profile != null) {
            ModuleComponent module = profile.getOwnerModule();
            if (module != null) {
                return module.equals(this.moduleComponent);
            } else {
                final ModelTree owner = profile.getOwner();
                return this.moduleComponent.equals(owner);
            }
        }
        return false;
    }

    /**
     * Returns current runtime state of this module.
     * @return the current runtime state of this module.
     */
    @objid ("b31ca9d6-f11c-11e1-af52-001ec947c8cc")
    @Override
    public ModuleRuntimeState getState() {
        return this.runtimeState;
    }

    /**
     * Sets the runtime state of this module.
     * <p>
     * <em>This method is not meant to be called by clients. Use of this method can result in unspecified behaviours.</em>
     * </p>
     * @param runtimeState the new runtime state of this module.
     */
    @objid ("b31ca9dc-f11c-11e1-af52-001ec947c8cc")
    public void setState(final ModuleRuntimeState runtimeState) {
        this.runtimeState = runtimeState;
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
    @objid ("b31ca9e1-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void uninit() {
        // Nothing to do.
    }

    /**
     * Get the path to the image representing the module.
     * @return a path relative to the module's resource path.
     */
    @objid ("90ca9ca0-1cf2-11e2-9c7e-bc305ba4815c")
    @Override
    public String getModuleImagePath() {
        return "";
    }

    @objid ("3d7b844d-b393-44f5-ab81-53f1ab98c59e")
    @Override
    public List<DiagramToolDescriptor> getDiagramTools() {
        return Collections.emptyList();
    }

    @objid ("31417efe-8b98-43e1-b88d-e8e285f7a809")
    @Override
    public List<DiagramCustomizationDescriptor> getDiagramCustomizations() {
        return Collections.emptyList();
    }

    @objid ("621ca5a8-ac69-41b5-8dfb-9da097200776")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IBoxCommand handler) {
        // not defined for fake modules
    }

    @objid ("522a5883-371a-4a09-8069-a479ea21276b")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IAttachedBoxCommand handler) {
        // not defined for fake modules
    }

    @objid ("c0f46f0b-d2d6-4085-9e7f-ea32bee7e893")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, ILinkCommand handler) {
        // not defined for fake modules
    }

    @objid ("b78e400f-5597-46e3-837d-fc2973a7902f")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IMultiLinkCommand handler) {
        // not defined for fake modules
    }

    @objid ("826feab8-ad79-4610-b8c4-d4ec211cc557")
    @Override
    public void registerDiagramCustomization(Stereotype stereotype, Class<? extends AbstractDiagram> baseDiagramClass, IDiagramCustomizer customizer) {
        // not defined for fake modules
    }

    @objid ("549a78a1-1158-453e-8710-cd54a5e905ec")
    @Override
    public String getLabel(Stereotype stereotype) {
        return stereotype.getName();
    }

    @objid ("adb67f35-c2be-40c1-bc7e-4dc8a4892e70")
    @Override
    public String getLabel(TagType tagType) {
        return tagType.getName();
    }

    @objid ("318f7a8c-8d95-4f3c-b52a-5ef386b7faca")
    @Override
    public String getLabel(NoteType noteType) {
        return noteType.getName();
    }

    @objid ("592824a0-44d0-4d09-97f9-d70ce7942bf7")
    @Override
    public String getLabel(ExternDocumentType docType) {
        return docType.getName();
    }

    @objid ("0ece6228-caed-4b9c-acf4-b47bc5785f07")
    @Override
    public String getLabel(String key) {
        return key;
    }

    @objid ("4e7caa03-87cc-47b7-bb2e-0bd4564efc72")
    @Override
    public LicenseInfos getLicenseInfos() {
        return new LicenseInfos(Status.UNDEFINED, null, "");
    }

    @objid ("62c88539-71ba-4cd2-b31d-53e4e778e3e5")
    @Override
    public <I> I instanciateExternProcessor(String className, Class<I> clazz, Object... initargs) {
        return null;
    }

    @objid ("80b9d88a-648e-4310-b32e-ed7727b6b2f7")
    @Override
    public List<WizardContribution> getDiagramWizardContributions() {
        return Collections.emptyList();
    }

    @objid ("2aa0fc02-1ae5-43be-abfb-19f79b95560f")
    @Override
    public void registerDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

    @objid ("38cd6ebc-001d-43f5-abcd-2969d1501866")
    @Override
    public void unregisterDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

}

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
 * This class is instantiated when a module is not compatible with the current Modelio version.</br>
 * It usually means a metamodel too old, or newer than the current one.
 */
@objid ("b31f0c67-f11c-11e1-af52-001ec947c8cc")
public class IncompatibleModule implements IModule {
    @objid ("b31f0c69-f11c-11e1-af52-001ec947c8cc")
    private IModuleSession moduleSession;

    @objid ("b31f0c6a-f11c-11e1-af52-001ec947c8cc")
    private IPeerModule peerModule;

    @objid ("b31f0c6b-f11c-11e1-af52-001ec947c8cc")
    private IModuleUserConfiguration moduleConfiguration;

    @objid ("b31f0c6c-f11c-11e1-af52-001ec947c8cc")
    protected IParameterEditionModel parameterEditionModel;

    @objid ("b31f0c6e-f11c-11e1-af52-001ec947c8cc")
    private ModuleComponent moduleComponent;

    @objid ("8d578473-03e6-11e2-8e1f-001ec947c8cc")
    private ImageRegistry imageRegistry;

    /**
     * Instantiate a fake module.
     * @param modelingSession a modeling session.
     * @param moduleComponent the module model element.
     * @param moduleUserConfiguration the user version of the module configuration
     * @param moduleApiConfiguration the api version of the module configuration
     */
    @objid ("b31f0c72-f11c-11e1-af52-001ec947c8cc")
    public IncompatibleModule(final IModelingSession modelingSession, final ModuleComponent moduleComponent, final IModuleUserConfiguration moduleUserConfiguration, final IModuleAPIConfiguration moduleApiConfiguration) {
        this.moduleComponent = moduleComponent;
        this.moduleConfiguration = moduleUserConfiguration;
        this.moduleSession = new DefaultModuleSession(this);
        this.peerModule = new DefaultPeerModule(moduleComponent, moduleApiConfiguration);
        
        Display.getDefault().syncExec(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                IncompatibleModule.this.imageRegistry = new ImageRegistry();
            }
        });
    }

    /**
     * Get the configuration associated to this module.
     * 
     * Jxbv2Module configuration provide access to module parameter and resource paths
     * @see IModuleUserConfiguration
     * @return the module configuration.
     */
    @objid ("b31f0c7b-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModuleUserConfiguration getConfiguration() {
        return this.moduleConfiguration;
    }

    /**
     * Used to return the module description.
     * @return The module description
     */
    @objid ("b3216e7f-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getDescription() {
        return "Jxbv2Module Incompatible with current version of Modelio : " + getName() + " " + getVersion();
    }

    /**
     * Get the image descriptor provided by the module for a given stereotype. The module should return an image if the
     * stereotype is provided by itself, null in the other case.
     * @param stereotype a stereotype
     * @param imageType the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("b3216e85-f11c-11e1-af52-001ec947c8cc")
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
            if (!Files.isRegularFile(imageFile) && relativePath.startsWith(moduleDirectory.getFileName().toString())) {
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
    @objid ("b3216e8d-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Image getImage(Stereotype stereotype, ImageType type) {
        // If only the stereotype was specified we search directly from it.
        if (stereotype != null) {
            String key = IncompatibleModule.getImageKey(stereotype, type);
        
            Image image = this.imageRegistry.get(key);
        
            if (image == null) {
                ImageDescriptor desc = this.getImageDescriptor(stereotype, type);
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
    @objid ("b3216e94-f11c-11e1-af52-001ec947c8cc")
    @Override
    public ScriptEngine getJythonEngine() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the module label that is displayed in dialog boxes and other GUIU parts.
     * @return The module label.
     */
    @objid ("b3216e99-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getLabel() {
        try {
            ResourceBundle manifestBundle = this.getManifestBundle();
            if (manifestBundle != null) {
                return manifestBundle.getString("ModuleLabel");
            }
        } catch (MissingResourceException e) {
            MdaInfra.LOG.error(MdaInfra.PLUGIN_ID, this.getName() + " module: " + e.toString());
        }
        return this.getName();
    }

    /**
     * Returns the {@link ModuleComponent} model associated with this module.
     * @return the {@link ModuleComponent} model associated with this module.
     */
    @objid ("b3216ea4-f11c-11e1-af52-001ec947c8cc")
    @Override
    public ModuleComponent getModel() {
        return this.moduleComponent;
    }

    /**
     * Used to return the module name.
     * <p>
     * <p>
     * The module name corresponds to the name of the module, as defined in the <i>MDA Designer<i> tool.
     * @return The module name
     */
    @objid ("b3216eaa-f11c-11e1-af52-001ec947c8cc")
    @Override
    public String getName() {
        return this.moduleComponent.getName();
    }

    /**
     * Get the parameters model as it must be shown in the module parameters edition dialog.
     * @return The parameters edition model.
     */
    @objid ("b3216eb0-f11c-11e1-af52-001ec947c8cc")
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
    @objid ("b3216eb6-f11c-11e1-af52-001ec947c8cc")
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
    @objid ("b3216ebc-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModuleSession getSession() {
        return this.moduleSession;
    }

    /**
     * Used to return the module version.
     * @return The module version
     */
    @objid ("b3216ec2-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Version getVersion() {
        return new Version(this.moduleComponent.getMajVersion() + "." + this.moduleComponent.getMinVersion() + "." + this.moduleComponent.getMinMinVersion());
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
    @objid ("b3216ec8-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void init() {
        // Nothing to do.
    }

    /**
     * Returns the minimum Modelio version that authorize the Jxbv2Module to be activated.
     * @return The minimum Modelio version
     */
    @objid ("b3216ecc-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Version getRequiredModelioVersion() {
        return new Version("0.0.0");
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("b323d0d6-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Collection<IModuleAction> getModuleActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Register a module action for the contextual popupmenu(s) of the application.
     * @param location The action insertion point in the popupmenu (see {@link ActionLocation})
     * @param action Action to store
     */
    @objid ("b323d0df-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void registerAction(ActionLocation location, IModuleAction action) {
        // Do nothing.
    }

    @objid ("b323d0e5-f11c-11e1-af52-001ec947c8cc")
    private static String getImageKey(final Stereotype stereotype, final ImageType imageType) {
        return "module." + stereotype.getName() + "." + imageType.name();
    }

    /**
     * Get the {@link ResourceBundle} corresponding to the localized manifest/manifest.properties file in the module
     * resources.
     * @return the resource bundle
     * @throws java.util.MissingResourceException if the file is not found
     */
    @objid ("b323d0ed-f11c-11e1-af52-001ec947c8cc")
    private ResourceBundle getManifestBundle() throws MissingResourceException {
        final Path manifestDir = this.getConfiguration().getModuleResourcesPath();
        if (manifestDir != null) {
            try (final URLClassLoader cl = new URLClassLoader(new URL[] { manifestDir.toUri().toURL() })) {
                // Create a class loader initialized with the 'manifest' directory
                // in module resource,
                // then give it to ResourceBundle.getBundle(...)
                return ResourceBundle.getBundle("module", Locale.getDefault(), cl);
            } catch (MalformedURLException e) {
                throw new MissingResourceException(e.getLocalizedMessage(), "module", "");
            } catch (MissingResourceException e) {
                MissingResourceException e2 = new MissingResourceException(e.getLocalizedMessage() + " in '"
                        + manifestDir.toUri() + "'", e.getClassName(), e.getKey());
                e2.initCause(e);
                throw e2;
            } catch (IOException e) {
                MdaInfra.LOG.error(e);
            }
        }
        return null;
    }

    @objid ("b323d0f1-f11c-11e1-af52-001ec947c8cc")
    private boolean isStereotypeOwner(final Stereotype stereotype) {
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
     * Returns the collection of {@link IModuleAction} associated with passed location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed location.
     */
    @objid ("b323d100-f11c-11e1-af52-001ec947c8cc")
    @Override
    public List<IModuleAction> getActions(ActionLocation location) {
        return Collections.emptyList();
    }

    /**
     * Returns the modeling session this module is loaded into.
     * @return the modeling session this module is loaded into.
     */
    @objid ("b323d109-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModelingSession getModelingSession() {
        return null;
    }

    /**
     * Return the defined property pages
     * @return The collection of property pages
     */
    @objid ("b323d10f-f11c-11e1-af52-001ec947c8cc")
    @Override
    public Collection<IModulePropertyPage> getPropertyPages() {
        return Collections.emptyList();
    }

    /**
     * Get the ModelComponent contributor associated to this module.
     * @see IModelComponentContributor
     * @return the module configuration.
     */
    @objid ("b323d117-f11c-11e1-af52-001ec947c8cc")
    @Override
    public IModelComponentContributor getModelComponentContributor(IModelComponent mc) {
        return null;
    }

    /**
     * Returns current runtime state of this module.
     * @return the current runtime state of this module.
     */
    @objid ("b323d11d-f11c-11e1-af52-001ec947c8cc")
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
    @objid ("b323d123-f11c-11e1-af52-001ec947c8cc")
    public void setState(final ModuleRuntimeState runtimeState) {
        throw new UnsupportedOperationException(
                this.moduleComponent.getName()
                + ": runtime state of this moduel cannot be changed since it is incompatible with this version of Modelio");
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
    @objid ("b3263334-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void uninit() {
        // Nothing to do.
    }

    @objid ("999fbce5-178f-11e2-aa0d-002564c97630")
    @Override
    public final Image getModuleImage() {
        return null;
    }

    /**
     * Get the path to the image representing the module.
     * @return a path relative to the module's resource path.
     */
    @objid ("90cf5f62-1cf2-11e2-9c7e-bc305ba4815c")
    @Override
    public String getModuleImagePath() {
        return "";
    }

    @objid ("5bd75a92-9528-441b-9583-dbd1df085035")
    @Override
    public List<DiagramToolDescriptor> getDiagramTools() {
        return Collections.emptyList();
    }

    @objid ("63d67f64-1d09-4fbb-a8ab-60574ed4132e")
    @Override
    public List<DiagramCustomizationDescriptor> getDiagramCustomizations() {
        return Collections.emptyList();
    }

    @objid ("9af4e771-e83b-4058-91e4-fed4bd42e457")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IBoxCommand handler) {
        // not defined for incompatible modules
    }

    @objid ("a5e99d62-562b-465e-ad8f-d771dcf50755")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IAttachedBoxCommand handler) {
        // not defined for incompatible modules
    }

    @objid ("e92886a1-d956-459a-8075-2ecf35dbb360")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, ILinkCommand handler) {
        // not defined for incompatible modules
    }

    @objid ("f4a5118f-fd4b-4b91-a128-b8d19a2996bb")
    @Override
    public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IMultiLinkCommand handler) {
        // not defined for incompatible modules
    }

    @objid ("c37dc2a6-c339-4b48-9b9f-9e7c6ca220f1")
    @Override
    public void registerDiagramCustomization(Stereotype stereotype, Class<? extends AbstractDiagram> baseDiagramClass, IDiagramCustomizer customizer) {
        // not defined for incompatible modules
    }

    @objid ("f835cb65-bed6-4431-bb6b-1ccbb891eb20")
    @Override
    public String getLabel(Stereotype stereotype) {
        return stereotype.getName();
    }

    @objid ("537261f5-f61a-421f-b0c7-ce0d4b73f7b4")
    @Override
    public String getLabel(TagType tagType) {
        return tagType.getName();
    }

    @objid ("83927b53-0d51-425a-a7dc-e4bde902652d")
    @Override
    public String getLabel(NoteType noteType) {
        return noteType.getName();
    }

    @objid ("18fdbbb5-8dbb-4601-b107-21aa9d35411d")
    @Override
    public String getLabel(ExternDocumentType docType) {
        return docType.getName();
    }

    @objid ("baab6ed8-1444-4cdc-813a-6a88e9ef8979")
    @Override
    public String getLabel(String key) {
        return key;
    }

    @objid ("a16af633-2c8e-4203-a4fc-51e1a0c2b882")
    @Override
    public LicenseInfos getLicenseInfos() {
        return new LicenseInfos(Status.UNDEFINED, null, "");
    }

    @objid ("69dd482a-c8e3-4fa6-aa67-47c1037b62df")
    @Override
    public <I> I instanciateExternProcessor(String className, Class<I> clazz, Object... initargs) {
        return null;
    }

    @objid ("a0fe5450-9b6e-4d3a-8978-4bd18bbb4fae")
    @Override
    public List<WizardContribution> getDiagramWizardContributions() {
        return Collections.emptyList();
    }

    @objid ("937d4edc-f35f-44a0-b5ec-1bf8aaca8efe")
    @Override
    public void registerDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

    @objid ("1951b07c-d819-4242-829d-14654433d355")
    @Override
    public void unregisterDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        // Nothing to do
    }

}

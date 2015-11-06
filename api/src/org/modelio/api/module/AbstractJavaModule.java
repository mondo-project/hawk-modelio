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
                                    

package org.modelio.api.module;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.script.ScriptEngine;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.help.internal.HelpPlugin;
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
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.ILicenseInfos.Status;
import org.modelio.api.module.commands.ActionLocation;
import org.modelio.api.module.commands.IModuleAction;
import org.modelio.api.module.contrib.WizardContribution;
import org.modelio.api.module.diagrams.DiagramCustomizationDescriptor;
import org.modelio.api.module.diagrams.DiagramToolDescriptor;
import org.modelio.api.module.paramEdition.DefaultParametersEditionModel;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.api.module.script.IScriptService;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.gproject.ramc.core.model.IModelComponent;
import org.modelio.gproject.ramc.core.packaging.IModelComponentContributor;
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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * Default implementation of the {@link IModule} interface.
 * <p>
 * All Modelio java modules should inherit from this class.
 */
@objid ("a0456ff1-479d-11df-a533-001ec947ccaf")
public abstract class AbstractJavaModule implements IModule {
    /**
     * Runtime state, defaults to loaded.
     */
    @objid ("02a8d537-99e0-11e1-b1e0-001ec947c8cc")
    private ModuleRuntimeState runtimeState = ModuleRuntimeState.Loaded;

    @objid ("550670e9-032a-11e2-9fca-001ec947c8cc")
    private List<Bundle> docBundles = new ArrayList<>();

    @objid ("b78d572d-0e9d-11e0-8636-001ec947cd2a")
    private final Map<ActionLocation, List<IModuleAction>> actionsRegistry;

    @objid ("a047d2d7-479d-11df-a533-001ec947ccaf")
    private final IModuleUserConfiguration configuration;

    /**
     * The Jython engine.
     */
    @objid ("a0457056-479d-11df-a533-001ec947ccaf")
    private javax.script.ScriptEngine jythonEngine = null;

    @objid ("a047d254-479d-11df-a533-001ec947ccaf")
    private final IModelingSession modelingSession;

    @objid ("a047d256-479d-11df-a533-001ec947ccaf")
    protected IParameterEditionModel parameterEditionModel = null;

    @objid ("a045704c-479d-11df-a533-001ec947ccaf")
    protected List<IModulePropertyPage> propertyPages = new ArrayList<>();

    @objid ("e0323981-edf0-11e1-82c2-001ec947ccaf")
    private ModuleComponent moduleComponent;

    @objid ("ab852f83-0dfe-11e2-baba-001ec947c8cc")
    protected ImageRegistry imageRegistry;

    @objid ("2faae81d-5aea-4757-96f2-640170b13a48")
    public List<DiagramToolDescriptor> diagramTools = new ArrayList<>();

    @objid ("883deeea-dc80-464c-b27e-21304e6c1835")
    private List<DiagramCustomizationDescriptor> diagramCustomizations = new ArrayList<>();

    @objid ("fdd785e3-7c11-4a4e-81a2-5d16c5db84c2")
    private final ResourceBundle I18N;

    @objid ("14877ba4-3d14-436f-8deb-29cb8f09957c")
    private List<WizardContribution> wizardContributions = new ArrayList<>();

    /**
     * Main constructor, to instantiate a new module.
     * @param modelingSession the current session.
     * @param moduleComponent the Module representing this module in the model.
     * @param moduleConfiguration the configuration of this module.
     */
    @objid ("a0457000-479d-11df-a533-001ec947ccaf")
    public AbstractJavaModule(IModelingSession modelingSession, ModuleComponent moduleComponent, IModuleUserConfiguration moduleConfiguration) {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                AbstractJavaModule.this.imageRegistry = new ImageRegistry();
            }
        });
        
        this.modelingSession = modelingSession;
        this.moduleComponent = moduleComponent;
        this.configuration = moduleConfiguration;
        
        this.I18N = getManifestBundle();
        
        // Initialize the moduleActions collections.
        this.actionsRegistry = new HashMap<>();
        this.actionsRegistry.put(ActionLocation.menu,
                new ArrayList<IModuleAction>());
        this.actionsRegistry.put(ActionLocation.toolbar,
                new ArrayList<IModuleAction>());
        this.actionsRegistry.put(ActionLocation.property,
                new ArrayList<IModuleAction>());
        this.actionsRegistry.put(ActionLocation.contextualpopup,
                new ArrayList<IModuleAction>());
        this.actionsRegistry.put(ActionLocation.diagramtoolbar,
                new ArrayList<IModuleAction>());
        
        // this.actionsRegistry.put(ActionLocation.elementcreationpopup, new
        // ArrayList<IModuleAction>());
        // this.workbenchActions = new HashMap<WorkbenchActionLocation,
        // List<IWorkbenchAction>>();
        // this.workbenchActions.put(WorkbenchActionLocation.MenuBar, new
        // ArrayList<IWorkbenchAction>());
        // this.workbenchActions.put(WorkbenchActionLocation.ToolBar, new
        // ArrayList<IWorkbenchAction>());
        // this.diagramTypeModuleActions = new HashMap<DiagramType,
        // List<IModuleDiagramAction>>();
        // this.diagramStereotypeModuleActions = new HashMap<Stereotype,
        // List<IModuleDiagramAction>>();
    }

    @objid ("a047d2c2-479d-11df-a533-001ec947ccaf")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractJavaModule other = (AbstractJavaModule) obj;
        if (this.moduleComponent == null) {
            if (other.moduleComponent != null)
                return false;
        } else if (!this.moduleComponent.equals(other.moduleComponent))
            return false;
        return true;
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed
     * location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed
     * location.
     */
    @objid ("b7921bd8-0e9d-11e0-8636-001ec947cd2a")
    @Override
    public List<IModuleAction> getActions(ActionLocation location) {
        if (this.actionsRegistry.containsKey(location))
            return this.actionsRegistry.get(location);
        else
            return Collections.emptyList();
    }

    /**
     * Get the configuration associated to this module.
     * 
     * Module configuration provide access to module parameter and resource
     * paths
     * @see IModuleUserConfiguration
     * @return the module configuration.
     */
    @objid ("a0457012-479d-11df-a533-001ec947ccaf")
    @Override
    public IModuleUserConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Used to return the module description.
     * @return The module description
     */
    @objid ("a047d2f5-479d-11df-a533-001ec947ccaf")
    @Override
    public String getDescription() {
        try {
            return this.I18N.getString("ModuleDescription");
        } catch (MissingResourceException e) {
            Modelio.getInstance().getLogService().warning(this, e.toString());
            return "";
        }
    }

    /**
     * Get the image provided by the module for a given stereotype. The module
     * should return an image if the stereotype is provided by itself, null in
     * the other case. The image life cycle must be handled by the module.
     * @param stereotype a stereotype
     * @param type the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("a047d2d2-479d-11df-a533-001ec947ccaf")
    @Override
    public Image getImage(Stereotype stereotype, ImageType type) {
        // If only the stereotype was specified we search directly from it.
        if (stereotype != null) {
            String key = getImageKey(stereotype, type);
        
            Image image = this.imageRegistry.get(key);
        
            if (image == null) {
                ImageDescriptor desc = getImageDescriptor(stereotype, type);
                if (desc != null) {
                    this.imageRegistry.put(key, desc);
                    image = this.imageRegistry.get(key);
                }
            }
            return image;
        }
        return null;
    }

    /**
     * Get the image descriptor provided by the module for a given stereotype.
     * The module should return an image if the stereotype is provided by
     * itself, null in the other case.
     * @param stereotype a stereotype
     * @param imageType the image type
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("a047d266-479d-11df-a533-001ec947ccaf")
    private ImageDescriptor getImageDescriptor(Stereotype stereotype, ImageType imageType) {
        // If the stereotype is not owned by the current module we return null.
        if (!this.isStereotypeOwner(stereotype)) {
            return null;
        }
        
        ImageDescriptor desc = null;
        Path moduleDirectory = getConfiguration().getModuleResourcesPath();
        String relativePath = null;
        
        if (imageType == ImageType.ICON) {
            relativePath = stereotype.getIcon();
        } else if (imageType == ImageType.IMAGE) {
            relativePath = stereotype.getImage();
        }
        
        if (relativePath == null || relativePath.equals("")) {
            return null;
        }
        
        try {
            Path imageFile = moduleDirectory.resolve(relativePath);
            if (!Files.isRegularFile(imageFile)
                    && relativePath.startsWith(getName())) {
                // Compatibility mode with modelio 1.2, remove the module's name
                relativePath = relativePath.substring(getName().length() + 1);
                imageFile = moduleDirectory.resolve(relativePath);
            }
        
            if (Files.isRegularFile(imageFile)) {
                URL imageUrl = imageFile.toUri().toURL();
                desc = ImageDescriptor.createFromURL(imageUrl);
                if (desc != ImageDescriptor.getMissingImageDescriptor()) {
                    return desc;
                }
                return null;
            }
        } catch (MalformedURLException e) {
            Modelio.getInstance().getLogService().error(this, e.getMessage());
        }
        return desc;
    }

    /**
     * Get the Jython scripting engine configured for having access to all the
     * module classes and the public classes of all required modules.
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
    @objid ("a047d267-479d-11df-a533-001ec947ccaf")
    @Override
    public ScriptEngine getJythonEngine() {
        if (this.jythonEngine == null) {
            IScriptService scriptService = Modelio.getInstance()
                    .getScriptService();
            this.jythonEngine = scriptService.getScriptEngine(getClass()
                    .getClassLoader());
        
            // preset a few variables
            this.jythonEngine.put("SESSION", getModelingSession());
            this.jythonEngine.put("MODULE", this);
            this.jythonEngine.put("modelingSession", getModelingSession());
            this.jythonEngine.put("module", this);
        }
        return this.jythonEngine;
    }

    @objid ("a047d268-479d-11df-a533-001ec947ccaf")
    @Override
    public String getLabel(Stereotype stereotype) {
        return getLabel(stereotype.getLabelKey());
    }

    /**
     * Returns the collection of {@link IModuleAction} associated with passed
     * location.
     * @param location the location for which actions are to be returned.
     * @return the collection of {@link IModuleAction} associated with passed
     * location.
     */
    @objid ("a0457016-479d-11df-a533-001ec947ccaf")
    @Deprecated
    @Override
    public Collection<IModuleAction> getModuleActions(ActionLocation location) {
        return getActions(location);
    }

    /**
     * Get the path to the image representing the module.
     * @return a path relative to the module's resource path.
     */
    @objid ("a0457017-479d-11df-a533-001ec947ccaf")
    @Override
    public String getModuleImagePath() {
        return "";
    }

    /**
     * Returns an ImageDescriptor for this module.
     * @return an ImageDescriptor for this module.
     */
    @objid ("a0457018-479d-11df-a533-001ec947ccaf")
    private ImageDescriptor getModuleImageDescriptor() {
        String relativePath = getModuleImagePath();
        if (relativePath != null && !relativePath.isEmpty()) {
            // 0) compute registry key
            final String moduleKey = getName() + "MainImageKey";
        
            // 1) look in the registry
            ImageDescriptor desc = this.imageRegistry.getDescriptor(moduleKey);
        
            if (desc == null) {
                try {
                    // look in the module resources directory
                    final Path moduleDirectory = getConfiguration()
                            .getModuleResourcesPath();
                    Path imageFile = moduleDirectory.resolve(relativePath
                            .substring(1));
        
                    if (Files.isRegularFile(imageFile)) {
                        final URL imageUrl = imageFile.toUri().toURL();
                        final ImageDescriptor imageDescriptor = ImageDescriptor
                                .createFromURL(imageUrl);
                        this.imageRegistry.put(moduleKey, imageDescriptor);
                        desc = this.imageRegistry.getDescriptor(moduleKey);
                    }
                } catch (MalformedURLException e) {
                    Modelio.getInstance().getLogService()
                    .error(this, e.getMessage());
                }
            }
        
            return desc;
        }
        return null;
    }

    /**
     * Returns the {@link ModuleComponent} model associated with this module.
     * @return the {@link ModuleComponent} model associated with this module.
     */
    @objid ("a047d24e-479d-11df-a533-001ec947ccaf")
    @Override
    public ModuleComponent getModel() {
        return this.moduleComponent;
    }

    /**
     * Returns the modeling session this module is loaded into.
     * @return the modeling session this module is loaded into.
     */
    @objid ("a047d24f-479d-11df-a533-001ec947ccaf")
    @Override
    public IModelingSession getModelingSession() {
        return this.modelingSession;
    }

    /**
     * Used to return the module name.
     * <p>
     * <p>
     * The module name corresponds to the name of the module, as defined in the
     * <i>MDA Designer<i> tool.
     * @return The module name
     */
    @objid ("a047d250-479d-11df-a533-001ec947ccaf")
    @Override
    public String getName() {
        return this.moduleComponent.getName();
    }

    /**
     * Get the parameters model as it must be shown in the module parameters
     * edition dialog.
     * @return The parameters edition model.
     */
    @objid ("a047d251-479d-11df-a533-001ec947ccaf")
    @Override
    public IParameterEditionModel getParametersEditionModel() {
        if (this.parameterEditionModel == null)
            this.parameterEditionModel = new DefaultParametersEditionModel(this);
        return this.parameterEditionModel;
    }

    /**
     * Return the defined property pages
     * @return The collection of property pages
     */
    @objid ("a0457064-479d-11df-a533-001ec947ccaf")
    @Override
    public Collection<IModulePropertyPage> getPropertyPages() {
        return this.propertyPages;
    }

    /**
     * Get the ModelComponent contributor associated to this module.
     * @see IModelComponentContributor
     * @return the module configuration.
     */
    @objid ("757687e3-65e6-11e0-9853-001ec947cd2a")
    @Override
    public IModelComponentContributor getModelComponentContributor(IModelComponent mc) {
        return null;
    }

    /**
     * Returns the minimum Modelio version that authorize the Module to be
     * activated.
     * @return The minimum Modelio version
     */
    @objid ("a0457065-479d-11df-a533-001ec947ccaf")
    @Override
    public Version getRequiredModelioVersion() {
        return new Version(this.moduleComponent.getMinBinVersionCompatibility());
    }

    /**
     * Used to return the module version.
     * @return The module version
     */
    @objid ("a0457068-479d-11df-a533-001ec947ccaf")
    @Override
    public Version getVersion() {
        return new Version(this.moduleComponent.getMajVersion() + "."
                + this.moduleComponent.getMinVersion() + "."
                + this.moduleComponent.getMinMinVersion());
    }

    /**
     * Method automatically called just after the creation of the module.
     * <p>
     * <p>
     * 
     * The module is automatically instantiated at the beginning of the mda
     * lifecycle and the constructor implementation is not accessible to the
     * module developer.
     * <p>
     * <p>
     * 
     * The <code>init</code> method allows the developer to execute the desired
     * initialization code at this step. For example, this is the perfect place
     * to register any IViewpoint this module provides.
     * <p>
     * <p>
     * 
     * This method should never be called by the developer because it is already
     * invoked by the tool.
     */
    @objid ("a047d2c4-479d-11df-a533-001ec947ccaf")
    @Override
    public void init() {
        installDocs();
        installStyles();
    }

    /**
     * Register a module action for the contextual popupmenu(s) of the
     * application.
     * @param location The action insertion point in the popupmenu (see
     * {@link ActionLocation})
     * @param action Action to store
     */
    @objid ("a047d2c5-479d-11df-a533-001ec947ccaf")
    @Override
    public void registerAction(ActionLocation location, IModuleAction action) {
        List<IModuleAction> actionsList = this.actionsRegistry.get(location);
        // Register the command in the moduleActions registry
        actionsList.add(action);
    }

    @objid ("a0457027-479d-11df-a533-001ec947ccaf")
    @Override
    public String toString() {
        final StringBuilder stringBuffer = new StringBuilder(40);
        stringBuffer.append(getClass().getSimpleName());
        stringBuffer.append(" '");
        stringBuffer.append(getName());
        stringBuffer.append("' {");
        stringBuffer.append(this.moduleComponent.getUuid());
        stringBuffer.append("} module");
        return stringBuffer.toString();
    }

    @objid ("a045700f-479d-11df-a533-001ec947ccaf")
    private static String getImageKey(Stereotype stereotype, ImageType imageType) {
        return "module." + stereotype.getCompositionOwner().getName() + "."
                + stereotype.getBaseClassName() + "." + stereotype.getName()
                + "." + imageType.name();
    }

    /**
     * Get the {@link ResourceBundle} corresponding to the localized
     * module.properties file in the module resources.
     * @return the resource bundle
     * @throws java.util.MissingResourceException if the file is not found
     */
    @objid ("a0457010-479d-11df-a533-001ec947ccaf")
    private ResourceBundle getManifestBundle() throws MissingResourceException {
        Path moduleResourcesPath = getConfiguration().getModuleResourcesPath();
        try (final URLClassLoader cl = new URLClassLoader(
                new URL[] { moduleResourcesPath.toUri().toURL() })) {
            // Create a class loader initialized with the 'manifest' directory
            // in module resource, then give it to ResourceBundle.getBundle(...)
            return ResourceBundle.getBundle("module", Locale.getDefault(), cl);
        } catch (MalformedURLException e) {
            throw new MissingResourceException(e.getLocalizedMessage(),
                    "module", "");
        } catch (MissingResourceException e) {
            MissingResourceException e2 = new MissingResourceException(
                    e.getLocalizedMessage() + " in '"
                            + moduleResourcesPath.toUri().toString() + "'",
                            e.getClassName(), e.getKey());
            e2.initCause(e);
            Modelio.getInstance().getLogService()
            .error(this, e.getLocalizedMessage());
            throw e2;
        } catch (IOException e) {
            Modelio.getInstance().getLogService().error(this, e);
        }
        return null;
    }

    /**
     * Returns true if the given stereotype belongs to the module.
     * @param stereotype the stereotype to test
     * @return true if the given stereotype belongs to the module.
     */
    @objid ("a0457011-479d-11df-a533-001ec947ccaf")
    private boolean isStereotypeOwner(Stereotype stereotype) {
        final Profile profile = stereotype.getOwner();
        if (profile != null) {
            ModuleComponent module = profile.getOwnerModule();
            if (module != null) {
                return module.equals(this.moduleComponent);
            }
            final ModelTree owner = profile.getOwner();
            return this.moduleComponent.equals(owner);
        }
        return false;
    }

    @objid ("466b0c4f-9748-11e0-8975-001ec947cd2a")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.moduleComponent == null) ? 0 : this.moduleComponent
                        .hashCode());
        return result;
    }

    /**
     * Returns current runtime state of this module.
     * @return the current runtime state of this module.
     */
    @objid ("0236648a-99e0-11e1-b1e0-001ec947c8cc")
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
    @objid ("02366491-99e0-11e1-b1e0-001ec947c8cc")
    public void setState(final ModuleRuntimeState runtimeState) {
        this.runtimeState = runtimeState;
    }

    /**
     * Method automatically called just before the disposal of the module.
     * <p>
     * <p>
     * 
     * 
     * The <code>uninit</code> method allows the developer to execute the
     * desired un-initialization code at this step. For example, if IViewpoints
     * have been registered in the {@link #init()} method, this method is the
     * perfect place to remove them.
     * <p>
     * <p>
     * 
     * This method should never be called by the developer because it is already
     * invoked by the tool.
     */
    @objid ("08fa08b1-a354-11e1-abf7-001ec947c8cc")
    @Override
    public void uninit() {
        uninstallDocs();
    }

    @objid ("9994c01e-178f-11e2-aa0d-002564c97630")
    @Override
    public final Image getModuleImage() {
        String key = "module.image";
        
        Image image = this.imageRegistry.get(key);
        
        if (image == null) {
            ImageDescriptor desc = getModuleImageDescriptor();
            if (desc != null) {
                this.imageRegistry.put(key, desc);
                image = this.imageRegistry.get(key);
            }
        }
        return image;
    }

    /**
     * Get ids of all defined diagram tools.
     * @return the toolIds.
     */
    @objid ("762d4fa9-9dc4-42bd-93cd-a16d6c7562c6")
    @Override
    public final List<DiagramToolDescriptor> getDiagramTools() {
        return this.diagramTools;
    }

    @objid ("b84b3e8b-6e3f-4f65-8494-8ee3c15ca3ae")
    @Override
    public final List<DiagramCustomizationDescriptor> getDiagramCustomizations() {
        return this.diagramCustomizations;
    }

    @objid ("aa1fa07b-3f01-49f9-9138-33d937183699")
    @Override
    public final void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IBoxCommand handler) {
        this.diagramTools.add(new DiagramToolDescriptor(id, metaclass,
                stereotype, dep, handler));
    }

    @objid ("bfc51349-8379-4fbd-b481-ca62f145289a")
    @Override
    public final void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IAttachedBoxCommand handler) {
        this.diagramTools.add(new DiagramToolDescriptor(id, metaclass,
                stereotype, dep, handler));
    }

    @objid ("69006df2-18dd-449e-b47f-39b64cf61cd8")
    @Override
    public final void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, ILinkCommand handler) {
        this.diagramTools.add(new DiagramToolDescriptor(id, metaclass,
                stereotype, dep, handler));
    }

    @objid ("c699576c-97f1-42d5-8612-879739654d2f")
    @Override
    public final void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IMultiLinkCommand handler) {
        this.diagramTools.add(new DiagramToolDescriptor(id, metaclass,
                stereotype, dep, handler));
    }

    @objid ("f3dfe929-371b-4cce-bcd1-9d42ae8d4b4a")
    @Override
    public final void registerDiagramCustomization(Stereotype stereotype, Class<? extends AbstractDiagram> baseDiagramClass, IDiagramCustomizer customizer) {
        this.diagramCustomizations.add(new DiagramCustomizationDescriptor(
                stereotype, baseDiagramClass, customizer));
    }

    @objid ("4ebad2e9-f676-49ec-b469-d1a1bff5a3f8")
    @Override
    public final String getLabel(TagType tagType) {
        return getLabel(tagType.getLabelKey());
    }

    @objid ("21578d5d-20c4-430e-b2af-fc29f9adbdb7")
    @Override
    public final String getLabel(NoteType noteType) {
        return getLabel(noteType.getLabelKey());
    }

    @objid ("441cfa36-d192-4147-ac55-060f7021df55")
    @Override
    public final String getLabel(ExternDocumentType docType) {
        return getLabel(docType.getLabelKey());
    }

    @objid ("df3e4b34-2623-4b44-9752-f76656be1e4e")
    @Override
    public String getLabel(final String key) {
        if (this.I18N == null)
            return key;
        if (key == null)
            return "";
        try {
            if (key.startsWith("%")) {
                return this.I18N.getString(key.substring(1));
            }
        } catch (MissingResourceException e) {
            // MdaInfra.LOG.warning("Missing Resource :" + value);
        }
        return key;
    }

    /**
     * Get the module label that is displayed in dialog boxes and other GUIU
     * parts.
     * @return The module label.
     */
    @objid ("552faba7-de2d-4c53-9a68-274bfdd999e3")
    @Override
    public String getLabel() {
        try {
            if (this.I18N != null) {
                return this.I18N.getString("ModuleLabel");
            }
        } catch (MissingResourceException e) {
            Modelio.getInstance().getLogService().error(this, e.getMessage());
        }
        return getName();
    }

    @objid ("ce0cb21c-74f2-42ff-b7ca-21e0c8db96c8")
    @Override
    public ILicenseInfos getLicenseInfos() {
        return new LicenseInfos(Status.FREE, null, "");
    }

    @objid ("3291a949-c2d8-43af-9e8e-1f99b12d1240")
    @SuppressWarnings("unchecked")
    @Override
    public <I> I instanciateExternProcessor(String className, Class<I> clazz, Object... initargs) {
        try {
            // Look for a standard class
            Class<I> cls = (Class<I>) Class.forName(className, true, this.getClass().getClassLoader());
        
            Class<?> [] initargsTypes = new Class[initargs.length];
            for (int i = 0; i < initargs.length; i++) {
                initargsTypes[i] = initargs[i].getClass();
            }
        
            Constructor<?> constr = cls.getConstructors()[0];
            return (I) constr.newInstance(initargs);
        } catch (ClassNotFoundException | ClassCastException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Modelio.getInstance().getLogService().warning(this, e.toString());
        }
        return null;
    }

    @objid ("930fd91e-ce1a-416e-beca-3937c613b26a")
    private void installDocs() {
        for (Path docFile : this.configuration.getDocpath()) {
            try {
                BundleContext bundleContext = InternalPlatform.getDefault()
                        .getBundleContext();
        
                Bundle bundleDoc = bundleContext
                        .installBundle("reference:file:/" + docFile);
                bundleDoc.start(Bundle.START_TRANSIENT);
        
                this.docBundles.add(bundleDoc);
        
            } catch (Exception e) { // Ignore
                // Doc DuplicateBundleException: doc already installed
                if (!e.getClass().getSimpleName()
                        .equals("DuplicateBundleException")) {
                    Modelio.getInstance().getLogService()
                    .warning(this, e.getMessage());
                }
            }
        }
        
        // Force the help to reload
        if (!this.docBundles.isEmpty()) {
            HelpPlugin.getTocManager().clearCache();
        }
    }

    @objid ("d43767a1-8219-4723-9d5e-56155bec0751")
    private void installStyles() {
        for (Entry<String, Path> style : this.configuration.getStylePath().entrySet()) {
            try {
                if (Files.exists(style.getValue())) {
                    Modelio.getInstance().getDiagramService().registerStyle(style.getKey(), "default", style.getValue().toFile());
                }
            } catch (Exception e) { // Ignore
                // Doc DuplicateBundleException: doc already installed
                if (!e.getClass().getSimpleName()
                        .equals("DuplicateBundleException")) {
                    Modelio.getInstance().getLogService()
                    .warning(this, e.getMessage());
                }
            }
        }
    }

    @objid ("f22df734-674d-43b6-858d-12e0d2990f78")
    protected void uninstallDocs() {
        if (!this.docBundles.isEmpty()) {
            for (Bundle bundleDoc : this.docBundles) {
                try {
                    bundleDoc.stop();
                    bundleDoc.uninstall();
                } catch (BundleException e) {
                    Modelio.getInstance().getLogService()
                    .warning(this, e.getMessage());
                }
            }
        
            // Empty the local bundle cache this.docBundles.clear();
        
            // Force the help to reload
            HelpPlugin.getTocManager().clearCache();
        }
    }

    @objid ("b300704c-a26a-4940-b32f-ff9dae0dd308")
    @Override
    public void registerDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        this.wizardContributions.add(new WizardContribution(category, contributor));
    }

    @objid ("6132acde-d1ed-4257-8b90-b77e8eb16a73")
    @Override
    public void unregisterDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
        this.wizardContributions.remove(new WizardContribution(category, contributor));
    }

    @objid ("6656d131-48a5-4cc6-ad9e-c5fb969aaaf0")
    @Override
    public List<WizardContribution> getDiagramWizardContributions() {
        return this.wizardContributions;
    }

}

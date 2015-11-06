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
                                    

package org.modelio.core.ui.images;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.modelio.api.module.ILicenseInfos.Status;
import org.modelio.api.module.IModule;
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
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.gproject.gproject.GProject;
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
import org.modelio.vcore.session.api.blob.IBlobChangeEvent;
import org.modelio.vcore.session.api.blob.IBlobChangeListener;
import org.modelio.vcore.session.api.blob.IBlobInfo;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The ModuleImageService provides icons and images for stereotypes brought by modules, and those modules themselves.
 * The returned icons (or images) only represents the raw images (no additional decorations).
 */
@objid ("50ac34ae-177d-11e2-aa0d-002564c97630")
public class ModuleI18NService {
    @objid ("5b4f1a92-177d-11e2-aa0d-002564c97630")
    private static Map<ModuleComponent, IModule> startedModules = new HashMap<>();

    /**
     * Get the icon provided by the module for a given stereotype. The module should return an icon if the stereotype
     * is provided by itself, <code>null</code> in the other case. The image life cycle is handled by the module.
     * @param moduleComponent the module to get the image from.
     * @param stereotype a stereotype
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("5b4f8fc7-177d-11e2-aa0d-002564c97630")
    public static Image getIcon(ModuleComponent moduleComponent, Stereotype stereotype) {
        IModule iModule = startedModules.get(moduleComponent);
        if (iModule == null) {
            iModule = new LocalModule(moduleComponent);
            startedModules.put(moduleComponent, iModule);
        }
        return iModule.getImage(stereotype, IModule.ImageType.ICON);
    }

    /**
     * Get the image provided by the module for a given stereotype. The module should return an image if the stereotype
     * is provided by itself, <code>null</code> in the other case. The image life cycle is handled by the module.
     * @param moduleComponent the module to get the image from.
     * @param stereotype a stereotype
     * @return the stereotype image, or null if the module provides none.
     */
    @objid ("5b4fb6d9-177d-11e2-aa0d-002564c97630")
    public static Image getImage(ModuleComponent moduleComponent, Stereotype stereotype) {
        IModule iModule = startedModules.get(moduleComponent);
        if (iModule == null) {
            iModule = new LocalModule(moduleComponent);
            startedModules.put(moduleComponent, iModule);
        }
        return iModule.getImage(stereotype, IModule.ImageType.IMAGE);
    }

    /**
     * Returns an Image for a module. The image life cycle is handled by the module.
     * @param moduleComponent the module to get the image from.
     * @return an Image for a module. Might be <code>null</code>.
     */
    @objid ("999e5d4c-178f-11e2-aa0d-002564c97630")
    public static Image getModuleImage(ModuleComponent moduleComponent) {
        IModule iModule = startedModules.get(moduleComponent);
        if (iModule != null) {
            return iModule.getModuleImage();
        }
        return null;
    }

    /**
     * Add a new started module for the image service to look into.
     * @param moduleComponent the module component part of the module.
     * @param iModule the runtime part of the module.
     */
    @objid ("5b4fdde9-177d-11e2-aa0d-002564c97630")
    public static void registerModule(ModuleComponent moduleComponent, IModule iModule) {
        startedModules.put(moduleComponent, iModule);
    }

    /**
     * Remove a stopped module from the image service.
     * @param moduleComponent the module component part of the module.
     */
    @objid ("5b5004f7-177d-11e2-aa0d-002564c97630")
    public static void unregisterModule(ModuleComponent moduleComponent) {
        startedModules.remove(moduleComponent);
    }

    @objid ("cb0513c4-174f-4258-bae2-d9f886ff7ebe")
    public static String getLabel(Stereotype stereotype) {
        IModule iModule = startedModules.get(stereotype.getModule());
        if (iModule != null) {
            return iModule.getLabel(stereotype);
        }
        return stereotype.getName();
    }

    @objid ("44ab8de4-e165-4d2c-9bf7-9e873f964013")
    public static String getLabel(TagType tagType) {
        IModule iModule = startedModules.get(tagType.getModule());
        if (iModule != null) {
            return iModule.getLabel(tagType);
        }
        return tagType.getName();
    }

    @objid ("154229ff-25cf-44a5-8f40-e570921f1e9e")
    public static String getLabel(NoteType noteType) {
        IModule iModule = startedModules.get(noteType.getModule());
        if (iModule != null) {
            return iModule.getLabel(noteType);
        }
        return noteType.getName();
    }

    @objid ("d39152b3-5cc1-439f-b438-107b6162e400")
    public static String getLabel(ExternDocumentType docType) {
        IModule iModule = startedModules.get(docType.getModule());
        if (iModule != null) {
            return iModule.getLabel(docType);
        }
        return docType.getName();
    }

    @objid ("a028ae95-45bf-44cc-8ab4-19bab9d7cf2c")
    public static String getLabel(ModuleComponent module) {
        IModule iModule = startedModules.get(module);
        if (iModule != null) {
            return iModule.getLabel();
        }
        return module.getName();
    }

    /**
     * Almost empty implementation of IModule, providing the {@link IModule#getImage(Stereotype, ImageType)} method only.
     * <p>
     * It relies on blobs to read the stereotype images, and stores them in an {@link ImageRegistry} using the blob key.
     * </br>
     * A 'BlobListener' clears updated/deleted blobs from the registry, as these images can change at runtime with the 'edit stereotype' command.
     * </p>
     */
    @objid ("9118e989-9e40-47cd-8385-ec78383bcd13")
    private static class LocalModule implements IModule {
        @objid ("6548a70f-36b8-4a33-b8dd-2149d3d6078a")
        protected ImageRegistry imageRegistry;

        @objid ("712b64ee-35da-42d8-b9b7-078fb77dc921")
        private ModuleComponent moduleComponent;

        /**
         * Default c'tor
         */
        @objid ("64712871-e452-45d4-a01e-114a8f47a4e3")
        LocalModule(ModuleComponent moduleComponent) {
            this.moduleComponent = moduleComponent;
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    LocalModule.this.imageRegistry = new ImageRegistry();
                }
            });
            
            registerBlobListener(moduleComponent);
        }

        @objid ("d30a7aa5-1a7b-4b94-89fc-21ba24df5d1c")
        public void registerBlobListener(ModuleComponent mc) {
            GProject.getProject(mc).getSession().getBlobSupport().addBlobChangeListener(new IBlobChangeListener() {
                @Override
                public void blobsChanged(IBlobChangeEvent ev) {
                    Set<IBlobInfo> blobs = new HashSet<>();
                    blobs.addAll(ev.getUpdatedBlobs());
                    blobs.addAll(ev.getDeletedBlobs());
            
                    // Remove all modified images from the registry.
                    for (IBlobInfo blob : blobs) {
                        LocalModule.this.imageRegistry.remove(blob.getKey());
                    }
                }
            });
        }

        @objid ("5d967f9a-f575-4f1a-9230-78dd917045b0")
        @Override
        public Image getModuleImage() {
            return ElementImageService.getIcon(this.moduleComponent);
        }

        @objid ("9e896833-f33e-4149-8004-753a8810578f")
        @Override
        public ModuleComponent getModel() {
            return this.moduleComponent;
        }

        @objid ("95895b64-767b-4d33-9806-988b6c8a429f")
        @Override
        public String getLabel() {
            return this.moduleComponent.getName();
        }

        @objid ("56304137-3a4b-4990-a47d-0b444dcc704a")
        @Override
        public Image getImage(Stereotype stereotype, ImageType type) {
            // If only the stereotype was specified we search directly from it.
            if (stereotype != null) {
                String blobKey = getBlobKey(stereotype, type);
            
                Image image = this.imageRegistry.get(blobKey);
            
                if (image == null) {
                    ImageDescriptor desc = getImageDescriptor(stereotype, blobKey);
                    if (desc != null) {
                        this.imageRegistry.put(blobKey, desc);
                        image = this.imageRegistry.get(blobKey);
                    }
                }
                return image;
            }
            return null;
        }

        @objid ("6108aa40-2da9-472e-8318-b1ba8b33c081")
        private static String getBlobKey(Stereotype stereotype, ImageType imageType) {
            // Compute the blob key
            String blobKey = stereotype.getUuid().toString();
            if (imageType == ImageType.ICON) {
                blobKey += ".icon";
            } else {
                blobKey += ".image";
            }
            return blobKey;
        }

        /**
         * Get the image descriptor provided by the module for a given stereotype.
         * The module should return an image if the stereotype is provided by
         * itself, null in the other case.
         * @param imageType the image type
         * @param stereotype a stereotype
         * @return the stereotype image, or null if the module provides none.
         */
        @objid ("df41da1c-7f04-4060-8aca-de16196d1e1b")
        private ImageDescriptor getImageDescriptor(Stereotype stereotype, String blobKey) {
            // If the stereotype is not owned by the current module we return null.
            if (!this.isStereotypeOwner(stereotype)) {
                return null;
            }
            
            ImageDescriptor desc = ImageDescriptor.createFromURL(getImageUrlFromBlob(stereotype, blobKey));
            if (desc != ImageDescriptor.getMissingImageDescriptor()) {
                return desc;
            }
            return null;
        }

        @objid ("6cd05520-e7ca-48da-8460-bb98acdf732e")
        public URL getImageUrlFromBlob(Stereotype stereotype, String blobKey) {
            // Get existing image stored in a blob
            IRepository repository = GProject.getProject(stereotype).getFragment(stereotype).getRepository();
            try (InputStream stream = repository.readBlob(blobKey)) {
                if (stream != null) {
                    File imageFile = File.createTempFile(stereotype.getUuid().toString(), blobKey);
                    Files.copy(stream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    // Make sure the image is deleted when leaving Modelio
                    imageFile.deleteOnExit();
            
                    return imageFile.toURI().toURL();
                }
            } catch (IOException e) {
                // No image to load, ignore error...
            }
            return null;
        }

        /**
         * Returns true if the given stereotype belongs to the module.
         * @param stereotype the stereotype to test
         * @return true if the given stereotype belongs to the module.
         */
        @objid ("7c94e616-1917-40fe-a2fe-f70e25963ec1")
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

        @objid ("ae734495-5117-42b0-aba5-e8c18c9fabe9")
        @Override
        public String getLabel(Stereotype stereotype) {
            return stereotype.getName();
        }

        @objid ("55e5760a-03c1-4427-9ea4-ede5cdbe424a")
        @Override
        public String getLabel(TagType tagType) {
            return tagType.getName();
        }

        @objid ("5d3b8a92-b9d6-4cff-bc89-2a9fc33f14e6")
        @Override
        public String getLabel(NoteType noteType) {
            return noteType.getName();
        }

        @objid ("7cd3e78f-835f-428c-b9e3-6cf892cf4c1a")
        @Override
        public String getLabel(ExternDocumentType docType) {
            return docType.getName();
        }

        @objid ("5939a62b-1dc6-4898-a347-5efd7570d983")
        @Override
        public String getLabel(String key) {
            return key;
        }

// Every other method is Unsupported...
        @objid ("b17f2ce2-a64f-42ed-9590-6cd7f9059c06")
        @Override
        public ScriptEngine getJythonEngine() {
            throw new UnsupportedOperationException();
        }

        @objid ("87a0f2da-f24e-44d8-a0b0-eac6002267d6")
        @Override
        public IModuleUserConfiguration getConfiguration() {
            throw new UnsupportedOperationException();
        }

        @objid ("8326d29e-5d6c-42c5-ba7e-1df5190eea7b")
        @Override
        public String getDescription() {
            throw new UnsupportedOperationException();
        }

        @objid ("d030d713-591d-4a26-a8fd-e97406d18f7a")
        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @objid ("4ba3f5af-79e8-4983-b3ce-a02cbdfba7f8")
        @Override
        public IParameterEditionModel getParametersEditionModel() {
            throw new UnsupportedOperationException();
        }

        @objid ("437d3d28-ea82-441f-8707-185331ab1489")
        @Override
        public IPeerModule getPeerModule() {
            throw new UnsupportedOperationException();
        }

        @objid ("fd085cc5-0798-405a-bc96-8caeffbd4688")
        @Override
        public IModuleSession getSession() {
            throw new UnsupportedOperationException();
        }

        @objid ("2e6c523d-cdfa-45fc-8f88-bc498d269c34")
        @Override
        public Version getVersion() {
            throw new UnsupportedOperationException();
        }

        @objid ("8595a3f9-6a5c-48fd-ba43-d473759ae3e4")
        @Override
        public void init() {
            throw new UnsupportedOperationException();
        }

        @objid ("341ee3c0-853a-4b5e-a144-761a0cd3049e")
        @Override
        public Version getRequiredModelioVersion() {
            throw new UnsupportedOperationException();
        }

        @objid ("eb6ca336-d322-4211-af12-3bb6d67ac20e")
        @Override
        public Collection<IModuleAction> getModuleActions(ActionLocation location) {
            throw new UnsupportedOperationException();
        }

        @objid ("4307f450-6196-4227-85f5-1e532c8c6e6c")
        @Override
        public void registerAction(ActionLocation location, IModuleAction action) {
            throw new UnsupportedOperationException();
        }

        @objid ("c18f10f2-0d3d-4479-bb8d-b444373a453e")
        @Override
        public List<IModuleAction> getActions(ActionLocation location) {
            throw new UnsupportedOperationException();
        }

        @objid ("5f93136e-b040-4100-9077-29be0f1c9f93")
        @Override
        public IModelingSession getModelingSession() {
            throw new UnsupportedOperationException();
        }

        @objid ("8643625c-a1eb-4a83-94dd-2f00aba0ca0b")
        @Override
        public Collection<IModulePropertyPage> getPropertyPages() {
            throw new UnsupportedOperationException();
        }

        @objid ("899a5b5e-c1f3-4550-ad19-70dee1002f9f")
        @Override
        public IModelComponentContributor getModelComponentContributor(IModelComponent mc) {
            throw new UnsupportedOperationException();
        }

        @objid ("1a29ff36-c6e7-4b09-820e-27e1ba85c989")
        @Override
        public ModuleRuntimeState getState() {
            throw new UnsupportedOperationException();
        }

        @objid ("c84f219d-0345-4777-b670-ae78a0038eb5")
        @Override
        public void uninit() {
            throw new UnsupportedOperationException();
        }

        @objid ("a932e078-f837-432f-bab8-b8bd2b48343a")
        @Override
        public String getModuleImagePath() {
            throw new UnsupportedOperationException();
        }

        @objid ("a7da52c5-4775-4f34-917d-911963d41806")
        @Override
        public List<DiagramToolDescriptor> getDiagramTools() {
            throw new UnsupportedOperationException();
        }

        @objid ("97c9366a-b223-4921-bb00-08a2c0604042")
        @Override
        public List<DiagramCustomizationDescriptor> getDiagramCustomizations() {
            throw new UnsupportedOperationException();
        }

        @objid ("6d908dd9-ca8d-4679-ab2b-49f8c1793551")
        @Override
        public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IBoxCommand handler) {
            throw new UnsupportedOperationException();
        }

        @objid ("faaf0b2e-97bd-404d-9a4b-476d38edc6bd")
        @Override
        public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IAttachedBoxCommand handler) {
            throw new UnsupportedOperationException();
        }

        @objid ("4300288d-47a7-414b-8338-87c00d81b50a")
        @Override
        public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, ILinkCommand handler) {
            throw new UnsupportedOperationException();
        }

        @objid ("99fc0db4-28af-4b70-83ad-3e0ee0176a75")
        @Override
        public void registerCustomizedTool(String id, Class<? extends MObject> metaclass, Stereotype stereotype, String dep, IMultiLinkCommand handler) {
            throw new UnsupportedOperationException();
        }

        @objid ("4dc6efdb-2ae2-4371-97f2-3d4d7bf89e62")
        @Override
        public void registerDiagramCustomization(Stereotype stereotype, Class<? extends AbstractDiagram> baseDiagramClass, IDiagramCustomizer customizer) {
            throw new UnsupportedOperationException();
        }

        @objid ("63cfca37-7535-411c-ab9d-3e1a06d2bab9")
        @Override
        public LicenseInfos getLicenseInfos() {
            return new LicenseInfos(Status.UNDEFINED, null, "");
        }

        @objid ("247c6cbb-4590-46d9-af79-ac1841fb293c")
        @Override
        public <I> I instanciateExternProcessor(String className, Class<I> clazz, Object... initargs) {
            return null;
        }

        @objid ("0ee207ae-a476-47dc-a23d-0707d99a02ba")
        @Override
        public List<WizardContribution> getDiagramWizardContributions() {
            return Collections.emptyList();
        }

        @objid ("2c7449dd-6c91-4ff0-a8a5-b0848712ebbd")
        @Override
        public void registerDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
            throw new UnsupportedOperationException();
        }

        @objid ("0841fa7a-fe2d-4db0-9cce-f056b47c6f71")
        @Override
        public void unregisterDiagramWizardContribution(ContributorCategory category, IDiagramWizardContributor contributor) {
            throw new UnsupportedOperationException();
        }

    }

}

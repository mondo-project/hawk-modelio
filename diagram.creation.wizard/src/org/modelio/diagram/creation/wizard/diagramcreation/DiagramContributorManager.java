package org.modelio.diagram.creation.wizard.diagramcreation;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.osgi.framework.Bundle;

@objid ("f1fec33e-dc07-4cb1-9b7b-cc420fb4a242")
public class DiagramContributorManager {
    @objid ("7b93e889-307b-417f-9402-3ddb98794a8c")
    private static final String DIAGRAM_CREATION_EXTENSION_ID = "org.modelio.diagram.creation.wizard.contributor";

    @objid ("72189203-8ea3-4c63-8af6-31af135af057")
    private Map<ContributorCategory, List<IDiagramWizardContributor>> contributors;

    @objid ("ffc43870-ac1c-4397-adf1-f1f0dd9ab286")
    private static DiagramContributorManager instance = null;

    @objid ("58a68a77-502e-4b32-8753-284c21046a9d")
    private Map<String, ContributorCategory> categories;

    @objid ("fcad7a3c-e606-41af-942d-c2f8d04d41e0")
    public static DiagramContributorManager getInstance() {
        if (instance == null) {
            instance = new DiagramContributorManager();
        }
        return instance;
    }

    @objid ("4ce97c94-0cc4-4c1a-a289-a7352e8132d6")
    public DiagramContributorManager() {
        this.contributors = new HashMap<>();
        this.categories = new HashMap<>();
        readExtensions();
    }

    @objid ("789c4aab-6085-46de-b304-e8990ecde786")
    private void readExtensions() {
        IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(DIAGRAM_CREATION_EXTENSION_ID);
        for (IConfigurationElement element : config) {
            if (element.getName().equals("diagramcreation")) {
                parseDiagramCreation(element);
            }        
        }
    }

    @objid ("312d5892-b443-4db2-893a-55bfe69ba5eb")
    private void parseDiagramCreation(IConfigurationElement element) {
        String categoryType = element.getAttribute("categoryType");
        String categoryLabel = element.getAttribute("categoryLabel");
        //FIXME
        String iconPath = element.getAttribute("categoryIcon");
        Object contributor;
        
        try {
            contributor = element.createExecutableExtension("contributor");
            ContributorCategory category = getCategory(categoryType);
            if (category == null) {                
                String bundleId = element.getNamespaceIdentifier();
                category = createCategory(categoryType, categoryLabel, iconPath, bundleId);
            }
            if (contributor instanceof IDiagramWizardContributor) {
                addContributor(category, (IDiagramWizardContributor) contributor);
            }
        
        } catch (CoreException e) {
            DiagramCreationWizard.LOG.error(e);
        }
    }

    @objid ("f37ea6d9-a104-40ac-a82d-754f44377424")
    public void addContributor(ContributorCategory category, IDiagramWizardContributor contributor) {
        List<IDiagramWizardContributor> categoryContributors = this.contributors.get(category);
        if (categoryContributors == null) {
            categoryContributors = new ArrayList<>();
            this.contributors.put(category, categoryContributors);
        }
        categoryContributors.add(contributor);
    }

    @objid ("970588fa-5c99-4d2e-9808-f5f5c35a1b32")
    public void removeContributor(ContributorCategory category, IDiagramWizardContributor contributor) {
        List<IDiagramWizardContributor> categoryContributors = this.contributors.get(category);
        if (categoryContributors != null) {
            categoryContributors.remove(contributor);
            // Remove category from the list if it has no more contributors
            if (categoryContributors.size() == 0) {
                if (category.getIcon() != null && !category.getIcon().isDisposed()) {
                    category.getIcon().dispose();
                }
                this.contributors.remove(category);
            }
        }
    }

    @objid ("ed934092-558f-44c3-a17c-a30ff1ba86ab")
    public Map<ContributorCategory, List<IDiagramWizardContributor>> getContributorsMap() {
        return this.contributors;
    }

    @objid ("d3117da0-8707-4848-8885-30398f635015")
    public List<IDiagramWizardContributor> getContributors(String category) {
        return this.contributors.get(category);
    }

    @objid ("4304038b-5cbc-4236-bd6d-d775730bc61d")
    public Set<ContributorCategory> getCategories() {
        return this.contributors.keySet();
    }

    @objid ("2aa626e8-f1f5-4da7-8db1-32ee94da3b3b")
    public List<IDiagramWizardContributor> getAllContributorsList() {
        List<IDiagramWizardContributor> allContributors = new ArrayList<>();
        for (List<IDiagramWizardContributor> cons : this.contributors.values()) {
            allContributors.addAll(cons);
        }
        return allContributors;
    }

    @objid ("286787ee-aad5-4705-856c-5d22db3f5c0c")
    private ContributorCategory getCategory(String type) {
        return this.categories.get(type);
    }

    @objid ("5aa58ec4-f623-46f5-ae8e-1f9c29483410")
    private ContributorCategory createCategory(String type, String label, String iconPath, String bundleId) {
        Image icon = getCategoryIcon(bundleId, iconPath);
        ContributorCategory category = new ContributorCategory(type, label, icon);
        this.categories.put(type, category);
        return category;
    }

    @objid ("b45f0211-d061-4f04-9e18-9dd2db0e250e")
    private Image getCategoryIcon(String bundleId, String iconPathString) {
        Image icon = null;
        if (iconPathString != null) {
            Bundle bundle = Platform.getBundle(bundleId);
            IPath iconPath = new Path(iconPathString);
            URL iconUrl = FileLocator.find(bundle, iconPath, null);
            icon = (ImageDescriptor.createFromURL(iconUrl)).createImage(); 
        }
        return icon;
    }

}

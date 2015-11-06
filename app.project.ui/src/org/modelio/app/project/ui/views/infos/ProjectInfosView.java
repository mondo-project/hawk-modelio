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
                                    

package org.modelio.app.project.ui.views.infos;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.project.ui.plugin.AppProjectIcons;
import org.modelio.app.project.ui.views.urls.UrlEntry;
import org.modelio.gproject.data.project.ProjectDescriptor;

/**
 * This view displays information about the project currently selected in the
 * workspace project list as a set of 'sections'.
 * Practically, the view has to distinguish between two cases:
 * <ul>
 * <li>the displayed project is the currently opened project
 * <li>the displayed project is NOT the currently opened project
 * 
 * In the first case, most sections will allow modifying the project
 * configuration while such modifications are forbidden in the second case.
 * 
 * @author phv
 */
@objid ("00777d56-ef91-1fc5-854f-001ec947cd2a")
public class ProjectInfosView {
    /**
     * The ID of the view as specified by the extension.
     */
    @objid ("004a15be-ef9d-1fc5-854f-001ec947cd2a")
    public static final String ID = "org.modelio.model.workspace.views.ProjectPageView";

    @objid ("0070fbd4-9092-105c-84ef-001ec947cd2a")
    @Inject
    @Optional
    public MApplication application;

    @objid ("00711ed4-9092-105c-84ef-001ec947cd2a")
    @Inject
    @Optional
    private EModelService modelService;

    @objid ("0071415c-9092-105c-84ef-001ec947cd2a")
    @Inject
    @Optional
    public EPartService partService;

    /**
     * The project adapter to the project that is currently being displayed by
     * the view.
     */
    @objid ("0042676a-cc89-1061-b3c0-001ec947cd2a")
     ProjectAdapter projectAdapter;

    @objid ("897dd708-7020-4c29-8f6c-1f947e769784")
    private ProjectPageView projectInfoPage;

    /**
     * C'tor
     */
    @objid ("0006334e-ef92-1fc5-854f-001ec947cd2a")
    public ProjectInfosView() {
    }

    /**
     * Creates the SWT controls.
     * <p>
     * Called by E4 injection.
     * @param parent the parent composite.
     */
    @objid ("0006421c-ef92-1fc5-854f-001ec947cd2a")
    @PostConstruct
    public void createControls(final Composite parent) {
        this.projectInfoPage = new ProjectPageView();
        this.projectInfoPage.createControls(parent);
        
        updateProjectInfoPage();
    }

    /**
     * Workspace tree selection always comes as ProjectDescriptor.
     * @param selectedProject the project selected in the workspace browser
     */
    @objid ("00076606-ef92-1fc5-854f-001ec947cd2a")
    @Optional
    @Inject
    public void onSelectionChanged(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        this.projectAdapter = null;
        if (selection != null) {
            List<ProjectDescriptor> projectDescriptors = getSelectedElements(selection);
            if (projectDescriptors.size() == 1) {        
                this.projectAdapter = new ProjectAdapter(projectDescriptors.get(0));        
            }            
        }
        updateProjectInfoPage();
        updateBrowsers();
    }

    @objid ("0039371c-6a48-1fcf-b5e2-001ec947cd2a")
    @Focus
    void setFocus() {
        // Do nothing here
    }

    @objid ("005ee188-e216-1062-b3c0-001ec947cd2a")
    void updateBrowsers() {
        List<UrlEntry> urls;
        if (this.projectAdapter != null) {
            urls = this.projectAdapter.getUrls();
        } else {
            urls = new ArrayList<>();
        }
        // refresh documentation browsers
        MPartStack stack = (MPartStack) this.modelService.find("org.modelio.app.project.ui.partstack.infos", this.application);
        // delete previous browsers
        {
            for (MPart view : this.partService.getParts()) {
                if (view.getElementId().equals(ProjectPageView.ID)) {
                    if (!isUrlEntryExist(((ProjectPageView) view.getObject()).getUrl(),view.getLabel(), urls)) {                       
                        this.partService.hidePart(view, true /*
                         * force removal from
                         * model
                         */);
                    }
                }
            }
        }
        
        // create new documentation browsers
        {
            for (UrlEntry entry : urls) {
                if (!isProjectPageViewExist(entry.url, entry.name)) {                 
                    MPart view = MBasicFactory.INSTANCE.createPart();
                    view.setElementId(ProjectPageView.ID);
                    view.setIconURI("platform:/plugin/org.modelio.app.project.ui/icons/url.png");
                    view.setContributionURI("bundleclass://org.modelio.app.project.ui/org.modelio.app.project.ui.views.infos.ProjectPageView");
                    view.setLabel(entry.name);
                    view.setCloseable(false);
                    stack.getChildren().add(view);
                    this.partService.showPart(view, PartState.VISIBLE);
                    ((ProjectPageView) view.getObject()).setUrl(entry.url);
                }
            }
            stack.setSelectedElement(stack.getChildren().get(0));
        }
    }

    @objid ("5e1a6ab3-1225-4d16-a45c-05c35f538ee6")
    private void updateProjectInfoPage() {
        if (this.projectInfoPage != null) {
            if (this.projectAdapter!=null && this.projectAdapter.getProjectDescriptor()!=null) {
                ProjectInfoHtmlGenerator htmlPage = new ProjectInfoHtmlGenerator(this.projectAdapter);
                if (!htmlPage.getPageUrl().equals("")) {
                    this.projectInfoPage.setUrl(htmlPage.getPageUrl());                
                }
            } else {
                this.projectInfoPage.setUrl(null);
            }
        }
    }

    @objid ("ddf76814-abf0-4af4-a07f-d87382f63667")
    private boolean isUrlEntryExist(String url, String name, List<UrlEntry> urls) {
        for (UrlEntry entry : urls) {
            if (entry.url.equals(url) && entry.name.equals(name)) {                
                return true;
            }
        }
        return false;
    }

    @objid ("c3210e5b-1aef-40f0-a932-f9218b457b36")
    private boolean isProjectPageViewExist(String url, String name) {
        for (MPart view : this.partService.getParts()) {
            if (view.getElementId().equals(ProjectPageView.ID)) {
                if (((ProjectPageView) view.getObject()).getUrl().equals(url) && view.getLabel().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    @objid ("ce5dcfaf-dcac-4e90-92d1-e015cc36b93f")
    private List<ProjectDescriptor> getSelectedElements(final IStructuredSelection selection) {
        List<ProjectDescriptor> selectedElements = new ArrayList<>();
        if (selection.size() > 0) {
            Object[] elements = selection.toArray();
            for (Object element : elements) {
                if (element instanceof ProjectDescriptor) {
                    selectedElements.add((ProjectDescriptor) element);
                }
            }
        }
        return selectedElements;
    }

}

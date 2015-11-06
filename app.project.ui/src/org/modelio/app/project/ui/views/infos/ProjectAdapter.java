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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.app.project.ui.views.urls.PropertiesUrlAdapter;
import org.modelio.app.project.ui.views.urls.UrlEntry;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.project.GProperties;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.gproject.GProject;

@objid ("00533464-cc89-1061-b3c0-001ec947cd2a")
class ProjectAdapter {
    @objid ("007a779a-e216-1062-b3c0-001ec947cd2a")
    private PropertiesUrlAdapter propertiesUrlAdapter;

    @objid ("bad518e3-9350-4434-b842-48c1ac51e1a8")
    private final ProjectDescriptor projectDescriptor;

    @objid ("007a2d3a-cc89-1061-b3c0-001ec947cd2a")
    public ProjectAdapter(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
        
        this.propertiesUrlAdapter = new PropertiesUrlAdapter(projectDescriptor.getProperties());
    }

    @objid ("007a46a8-cc89-1061-b3c0-001ec947cd2a")
    public boolean isSameAs(GProject project) {
        return (project != null) && (project.getName().equals(getName()));
    }

    @objid ("007a6598-cc89-1061-b3c0-001ec947cd2a")
    public boolean isSameAs(ProjectDescriptor aProjectDescriptor) {
        return (aProjectDescriptor != null) && (aProjectDescriptor.getName().equals(getName()));
    }

    @objid ("007a8320-cc89-1061-b3c0-001ec947cd2a")
    public String getName() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getName();
        }
        return "";
    }

    @objid ("007a9c8e-cc89-1061-b3c0-001ec947cd2a")
    public List<UrlEntry> getUrls() {
        return this.propertiesUrlAdapter.getUrls();
    }

    @objid ("007ac40c-cc89-1061-b3c0-001ec947cd2a")
    public ProjectDescriptor getProjectDescriptor() {
        return this.projectDescriptor;
    }

    /**
     * @return the project properties.
     */
    @objid ("007addf2-cc89-1061-b3c0-001ec947cd2a")
    public GProperties getProperties() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getProperties();
        }
        return new GProperties();
    }

    @objid ("007afb20-cc89-1061-b3c0-001ec947cd2a")
    public Path getPath() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getPath();
        }
        return null;
    }

    @objid ("007b1588-cc89-1061-b3c0-001ec947cd2a")
    public Object[] getFragments() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getFragments().toArray();
        }
        return null;
    }

    @objid ("007b3c5c-cc89-1061-b3c0-001ec947cd2a")
    public Object[] getModules() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getModules().toArray();
        }
        return null;
    }

    @objid ("016b5ac4-3fd8-4f3b-812c-abf2973e360b")
    public String getStoragePathString() {
        if (this.projectDescriptor != null) {
            return this.projectDescriptor.getPath().toString();
        }
        return "";
    }

    @objid ("6cf4fda0-9d6e-4e3e-afc0-cea2798791a1")
    public String getStorageLastModificationTimeString() {
        if (this.projectDescriptor != null) {            
            try {
                FileTime lastLastModifiedTime = Files.getLastModifiedTime(this.projectDescriptor.getPath().resolve("project.conf"));
                Date date = new Date(lastLastModifiedTime.toMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat(AppProjectUi.I18N.getString("ProjectInfoHtmlPage.lastLastModifiedTimeFormat")); 
                return dateFormat.format(date);
            } catch (IOException e) {
                AppProjectUi.LOG.error(e);
            }
        }
        return "";
    }

    /**
     * Get the work models fragments
     * FragmentType: EXML_SVN, EXML
     * @return
     */
    @objid ("ad04a4f1-e835-4ced-8c1f-ea5d79bc0d11")
    public List<FragmentDescriptor> getWorkModelsFragments() {
        List<FragmentDescriptor> workModelsFragments = new ArrayList<>();       
        for(Object element : getFragments()) {
            if ((element != null) && (element instanceof FragmentDescriptor)) {
                switch (((FragmentDescriptor) element).getType()) {
                    case EXML_SVN:
                    case EXML:
                        workModelsFragments.add((FragmentDescriptor) element);
                        break;
                    default:
                        break;
                }
            }
        }
        return workModelsFragments;
    }

    /**
     * Get the libraries fragments
     * FragmentType: EXML_URL, RAMC
     * @return
     */
    @objid ("97a85dc7-62fa-422c-9cd3-67698d3bbf5e")
    public List<FragmentDescriptor> getLibrariesFragments() {
        List<FragmentDescriptor> librariesFragments = new ArrayList<>();       
        for(Object element : getFragments()) {
            if ((element != null) && (element instanceof FragmentDescriptor)) {
                switch (((FragmentDescriptor) element).getType()) {
                    case EXML_URL:
                    case RAMC:
                        if (!((FragmentDescriptor) element).getId().equals("PredefinedTypes")) {                            
                            librariesFragments.add((FragmentDescriptor) element);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return librariesFragments;
    }

}

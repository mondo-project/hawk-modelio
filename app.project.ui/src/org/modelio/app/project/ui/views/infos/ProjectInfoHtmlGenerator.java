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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.data.project.ModuleDescriptor;
import org.osgi.framework.Bundle;

/**
 * Generator of project info html page.
 * @author xzhang
 */
@objid ("a7c01e3d-95f3-4582-9317-a121642a4743")
public class ProjectInfoHtmlGenerator {
    @objid ("823e2aab-d147-4782-bd3b-7f86e69069bd")
    private static final String INFO_CONTACT = "info.contact";

    @objid ("212ae85a-bc95-4b17-a515-1a07302603b8")
    private static final String INFO_DESCRIPTION = "info.description";

    @objid ("ba2c9ecc-26a5-4a66-8120-31315eea9508")
    private String pageUrl;

    @objid ("daad0acb-6c2c-4a2f-9fcf-f49715b03257")
    private static final String INFO_PROJECT_LOGO_NAME = "info.projetLogoName";

    @objid ("4e327312-dbed-4979-9858-9323fabb9acb")
    private ProjectAdapter projectAdapter;

    @objid ("0484192c-4e10-45ef-86eb-f8d09f650958")
    public ProjectInfoHtmlGenerator(ProjectAdapter projectAdapter) {
        this.projectAdapter = projectAdapter;
        createProjectInfoPageFromTemplate();
    }

    @objid ("a90d91c9-bfb8-4e7b-8b24-09a9a7d49d40")
    private String createProjectInfoPageFromTemplate() {
        this.pageUrl = "";
        Bundle bundle = Platform.getBundle(AppProjectUi.PLUGIN_ID);
        // Get the template file
        IPath projectInfoPagePath = new Path("/res/projectInfo.html");
        URL templateUrl = FileLocator.find(bundle, projectInfoPagePath, null);
        File pageTemplate = null;
        String templatePathString = "";
        try {
            templatePathString = FileLocator.toFileURL(templateUrl).getPath();
            pageTemplate = new File(templatePathString);
        } catch (IOException e) {
            AppProjectUi.LOG.error("Error when getting the template file from url");
            AppProjectUi.LOG.debug(e);
        }
        if(pageTemplate==null || !pageTemplate.exists()){    
            AppProjectUi.LOG.error("Project Info html template does not exist.");
            return "";
        }
        // Get the .runtime file
        java.nio.file.Path runtimeProjectInfoPagePath = this.projectAdapter.getProjectDescriptor().getPath().resolve(".runtime").resolve("projectInfo.html");
        File projectInfoPageFile = null;
        try {
            projectInfoPageFile = runtimeProjectInfoPagePath.toFile();
            projectInfoPageFile.getParentFile().mkdirs();
            projectInfoPageFile.createNewFile();
        } catch (IOException e) {
            AppProjectUi.LOG.error("Error when getting the .runtime file");
            AppProjectUi.LOG.debug(e);
        }
        try {
            Files.copy(pageTemplate.toPath(), runtimeProjectInfoPagePath, StandardCopyOption.REPLACE_EXISTING);
            fillProjectInfoPage(projectInfoPageFile);
            this.pageUrl = runtimeProjectInfoPagePath.toString();
        } catch (IOException e) {
            AppProjectUi.LOG.error("Error copying file");
            AppProjectUi.LOG.debug(e);
        }
        return this.pageUrl;
    }

    @objid ("1f206bce-d96d-448f-ae3c-9f2aad7f9446")
    private void fillProjectInfoPage(File file) {
        String line = "";
        StringBuffer oldtext = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF8"));) {
        
            while((line = reader.readLine()) != null)
            {
                oldtext.append(line);
                oldtext.append(System.getProperty("line.separator"));
            }
            reader.close();
        } catch (IOException ioe) {
            AppProjectUi.LOG.error("Error when copying the template file");
            AppProjectUi.LOG.debug(ioe);
        }
        // replace strings in a the template file
        StringBuffer newtext = replaceTemplateStrings(oldtext);
        getFilePathOf("/res/img/");     //force loading the img folder
        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF8"));) {
            writer.write(newtext.toString());
            writer.close();
        } catch (IOException ioe) {
            AppProjectUi.LOG.error("Error when writing the html page");
            AppProjectUi.LOG.debug(ioe);
        }
    }

    @objid ("08459806-084e-4c05-a48c-a2d22d5fbabb")
    private StringBuffer replaceTemplateStrings(StringBuffer source) {
        // Replace all the fix content of source file
        replaceTemplateFixContent(source);
        
        // Add css file
        replaceAll(source, "$css_file_ path", getFilePathOf("/res/projectInfo.css"));
        
        // Add javascript file
        replaceAll(source, "$javascript_file_path", getFilePathOf("/res/projectInfo.js"));
        
        // Add project icon
        replaceAll(source, "$project_icon", addProjectLogo());
        
        // Add project name
        replaceAll(source, "$project_name", this.projectAdapter.getName());
        
        // Add body font family/size
        Font font = Display.getDefault().getSystemFont();
        String fontFamily = font.getFontData()[0].getName();
        int fontSize = font.getFontData()[0].getHeight();
        replaceAll(source, "$body_font_family", fontFamily);
        replaceAll(source, "$body_font_size", Integer.toString(fontSize)+"pt");
        
        // Add project description
        replaceAll(source, "$project_description", this.projectAdapter.getProperties().getValue(INFO_DESCRIPTION, "").replace("\\n", "\n"));
        
        // Basic info
        String contactName = this.projectAdapter.getProperties().getValue(INFO_CONTACT, "");
        replaceAll(source, "$project_contact", contactName);
        replaceAll(source, "$mail_subject", this.projectAdapter.getName().replaceAll(" ", "%20"));
        replaceAll(source, "$project_storage_path", this.projectAdapter.getStoragePathString());
        replaceAll(source, "$project_storage_last_modification_time", this.projectAdapter.getStorageLastModificationTimeString());
        
        if (contactName==null ||contactName.isEmpty()) {
            replaceAll(source, "$display_sendMail_icon", "none");
        } else {
            replaceAll(source, "$display_sendMail_icon", "block");
        }
        
        // WorkModels Fragments section
        List<FragmentDescriptor> workModelsFragments = this.projectAdapter.getWorkModelsFragments();
        if (workModelsFragments.size()>0) {            
            replaceAll(source, "$tbl_workModels_content", createWorkModelsTableContent(workModelsFragments));
        } else {    // Hide container if no WorkModels
            hideContainer(source, "AccordionContainerWorkModels");
        }
        // Libraries Fragments section
        List<FragmentDescriptor> librariesFragments = this.projectAdapter.getLibrariesFragments();
        if (librariesFragments.size()>0) {            
            replaceAll(source, "$tbl_libraries_content", createLibrariesTableContent(librariesFragments));
        } else {    // Hide container if no Libraries
            hideContainer(source, "AccordionContainerLibraries");
        }
        
        // Modules section
        replaceAll(source, "$tbl_modules_content", createModulesTableContent(this.projectAdapter.getModules()));
        return source;
    }

    /**
     * @return
     */
    @objid ("16046109-3c97-44f4-aa5d-d22e5504a155")
    private String addProjectLogo() {
        String iconName = this.projectAdapter.getProperties().getValue(INFO_PROJECT_LOGO_NAME);
        if (iconName == null) return "";
        String addIconString = "";
        if (iconName.startsWith("http")) {
            try {
                URI uri = new URI(iconName);
        
                addIconString += "<img src=\"";
                addIconString += uri.toString();
                addIconString += "\" height=\"64\" width=\"64\">";
            } catch (URISyntaxException e) {
                addIconString = "";
            }
        } else {
            try {
                java.nio.file.Path iconPath = this.projectAdapter.getPath().resolve("data").resolve(iconName);
                if (Files.exists(iconPath)) {
                    addIconString += "<img src=\"";
                    addIconString += iconPath.toString();
                    addIconString += "\" height=\"64\" width=\"64\">";
                }
            } catch (InvalidPathException e) {
                addIconString = "";
            }
        }
        return addIconString;
    }

    /**
     * Add javascript code to hide a container
     * @param idToHide
     * @param content @return
     */
    @objid ("d5d92b4b-96f7-44c4-b465-33d684f7485c")
    private StringBuffer hideContainer(StringBuffer sb, String idToHide) {
        String hideString = "document.getElementById('" + idToHide + "').style.display = 'none';";
        return replaceAll(sb, "//windowOnLoadFunction", "//windowOnLoadFunction"+System.getProperty("line.separator")+hideString);
    }

    /**
     * Replace the string like [#stringKey#] by the string of the stringKey in the properties file
     * Define all the string of the html template placed in the properties like [#stringKey#] (must have the same 'stringKey' in the properties file)
     * @param content @return
     */
    @objid ("dcd6d884-7f43-4a68-ad30-e04664bb9986")
    private StringBuffer replaceTemplateFixContent(StringBuffer sb) {
        Pattern myPattern = Pattern.compile("\\[\\#([^\\]]*)\\#\\]");
        Matcher m = myPattern.matcher(sb.toString());
        while (m.find()) {
            String toReplace = m.group(1);
            replaceAll(sb, "[#"+toReplace+"#]", AppProjectUi.I18N.getString(toReplace));
        }
        return sb;
    }

    /**
     * @param fileName
     * @return full file path
     */
    @objid ("3c808cf3-7d0b-4c1f-88db-4eb781304cc5")
    private String getFilePathOf(String fileName) {
        String path="";
        Bundle bundle = Platform.getBundle(AppProjectUi.PLUGIN_ID);
        String s = "platform:/plugin/"+bundle.getSymbolicName()+"/"+fileName;   // To avoid the space in the bundle path
        URL url = null;
        try {
            url = new URL(s);
            path = FileLocator.toFileURL(url).getPath();
        } catch (Exception e) {
            AppProjectUi.LOG.debug("File path %s is not found!", s);
            AppProjectUi.LOG.error(e);
        }
        return "file://"+path;  //add protocol
    }

    /**
     * Create Work Models Fragments table content
     * Columns: Name, Type, Uri
     * @param fragments @return
     */
    @objid ("e6e1e47c-e272-42d3-8feb-e1d7ab2ee2dc")
    private String createWorkModelsTableContent(List<FragmentDescriptor> fragments) {
        String content = "";
        for(FragmentDescriptor fragment : fragments) {                              
            content += "<tr>";
            String fIconPath = getFragmentIconPath(fragment.getType());
            String addIconString = "<img src=\"" + getFilePathOf(fIconPath) + "\"> ";
            boolean isDistance = fragment.getType().equals(FragmentType.EXML_SVN);
            String fType = isDistance ? AppProjectUi.I18N.getString("ProjectInfoHtmlGenerator.workmodeltype.distant"):AppProjectUi.I18N.getString("ProjectInfoHtmlGenerator.workmodeltype.local");
            String fUriString = isDistance ? fragment.getUri().toString().replaceAll("%20", " ") : "";
            content += "<td>" + addIconString + fragment.getId() + "</td>";
            content += "<td>" + fType + "</td>";
            content += "<td>" + fUriString + "</td>";
            content += "</tr>"; 
        }
        return content;
    }

    /**
     * Create Libraries Fragments table content
     * Columns: Name, Version, Description
     * @param fragments @return
     */
    @objid ("c86615b6-3c1e-4a46-a380-a959070bdf76")
    private String createLibrariesTableContent(List<FragmentDescriptor> fragments) {
        String content = "";
        for(FragmentDescriptor fragment : fragments) {                              
            content += "<tr>";
            String fIconPath = getFragmentIconPath(fragment.getType());
            String addIconString = "<img src=\"" + getFilePathOf(fIconPath) + "\"> ";
            String fVersion = fragment.getProperties().getValue("FragmentVersion");
            String fDescription = fragment.getProperties().getValue("FragmentDescription");
            content += "<td>"+ addIconString + fragment.getId() + "</td>";
            content += "<td>" + (fVersion==null?"":fVersion) + "</td>";
            content += "<td><textarea class='librarieDescriptorTextarea' rows='3' readonly>" + (fDescription==null?"":fDescription) + "</textarea></td>";
            content += "</tr>";           
        }
        return content;
    }

    /**
     * Create Modules table content
     * Columns: Name, Version
     * @param modules @return
     */
    @objid ("1cbfb808-3be4-49b4-9119-3334ce2cebde")
    private String createModulesTableContent(Object[] modules) {
        String content = "";
        String mIconPath = "icons/modulecomponent.png";
        String addIconString = "<img src=\"" + getFilePathOf(mIconPath) + "\"> ";
        for( Object element : modules ) {
            if (element instanceof ModuleDescriptor) {
                content += "<tr>";
                content += "<td>" + addIconString + ((ModuleDescriptor) element).getName() + "</td>";
                content += "<td>" + ((ModuleDescriptor) element).getVersion().toString() + "</td>";
                content += "</tr>";           
            }
        }
        return content;
    }

    @objid ("4d3b51fe-cd4c-4115-9748-2c829a562868")
    public String getPageUrl() {
        return this.pageUrl;
    }

    /**
     * Replace all the oldString by the newString in the given stringBuffer
     * @param stringBuffer
     * @param oldString
     * @param newString @return
     */
    @objid ("40e702ce-7244-4ed3-97b6-b3abaedf93f7")
    private StringBuffer replaceAll(StringBuffer stringBuffer, String oldString, String newString) {
        int index = stringBuffer.indexOf(oldString);
        while (index != -1) // replace all
        {
            stringBuffer.replace(index, index + oldString.length(), newString);
            index += newString.length();    // get the beginning index of the next replacement if need
            index = stringBuffer.indexOf(oldString, index);
        }
        return stringBuffer;
    }

    @objid ("aa60b3b5-539d-481b-abcd-20c7957954a8")
    private String getFragmentIconPath(FragmentType type) {
        switch (type) {
        case EXML:
            return "icons/exmlfragment.png";
        case EXML_URL:
            return "icons/httpfragment.png";
        case EXML_SVN:
            return "icons/svnfragment.png";
        case RAMC:
            return "icons/ramcfragment.png";
        case MDA:
            return "icons/mdafragment.png";
        default:
            return "";
        }
    }

}

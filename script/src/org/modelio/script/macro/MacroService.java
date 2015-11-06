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
                                    

package org.modelio.script.macro;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.script.macro.catalog.Catalog;
import org.modelio.script.macro.catalog.Macro;
import org.modelio.script.plugin.Script;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.osgi.framework.Bundle;

@objid ("5c36035b-5a91-4d8a-8bf6-f08dd97ed7ae")
@Creatable
public class MacroService implements IMacroService {
    @objid ("95d2dce6-6676-4535-a8cc-abe8a22e6ab4")
     Catalog modelioCatalog;

    @objid ("597ea110-2069-4763-bef5-8071e23ed541")
     Catalog workspaceCatalog;

    @objid ("5ee7779d-7cd2-4edb-ba7d-a33a49d26b6c")
     Catalog projectCatalog;

    @objid ("b5b3d299-7dee-4322-a992-365e704f9e9a")
    @PostConstruct
    void initialize(ModelioEnv env, IProjectService projectService) {
        Path modelioMacroCatalogPath = env.getMacroCatalogPath();
        File modelioMacroCatalog = modelioMacroCatalogPath.toFile();
        if (!modelioMacroCatalog.exists()) {
            createModelioMacroCatalogFile(modelioMacroCatalogPath);
        } else {          
            if (modelioMacroCatalog.isDirectory() && modelioMacroCatalog.list().length==0) {            
                createModelioMacroCatalogFile(modelioMacroCatalogPath);
            }
        }
        this.modelioCatalog = new Catalog("Modelio", env.getMacroCatalogPath(), true);  // The modelio catalog is readonly
        this.workspaceCatalog = new Catalog("Workspace", projectService.getWorkspace().resolve("macros"), false);
    }

    @objid ("33adeb7e-96fa-46dd-ab8d-2e4fb15ca60b")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject project) {
        if (project != null) {
            this.projectCatalog = new Catalog(project.getName(), project.getProjectDataPath().resolve("macros"), false);
        }
    }

    @objid ("c1325c8e-a2f4-4248-a5f5-b9eac5a721af")
    @Inject
    @Optional
    void onProjectClosed(@SuppressWarnings("unused")
@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject project) {
        this.projectCatalog = null;
    }

    @objid ("8525c402-ad5e-492d-9850-f5db8331cd71")
    @Override
    public List<Macro> getMacros() {
        List<Macro> macros = new ArrayList<>();
        macros.addAll(this.modelioCatalog.getMacros());
        macros.addAll(this.workspaceCatalog.getMacros());
        if (this.projectCatalog != null)
            macros.addAll(this.projectCatalog.getMacros());
        return macros;
    }

    @objid ("b0230437-43c3-4040-a742-71ac9bd9f146")
    @Override
    public List<Macro> getMacros(Collection<MObject> elements) {
        List<Macro> macros = new ArrayList<>();
        for (Macro m : getMacros()) {
            if (m.isRunnableOn(elements))
                macros.add(m);
        }
        return macros;
    }

    @objid ("1c2712ca-0514-454e-a71f-0dacc8e19e2c")
    @Override
    public List<Macro> getMacros(Scope scope) {
        switch (scope) {
            case MODELIO:
                return this.modelioCatalog.getMacros();
            case WORSPACE:
                return this.workspaceCatalog.getMacros();
            case PROJECT:
                return (this.projectCatalog != null) ? this.projectCatalog.getMacros() : new ArrayList<Macro>();
            default:
                return Collections.emptyList();
        }
    }

    @objid ("fa9314f7-0569-41c6-91c4-dd6337f002c5")
    @Override
    public void addMacro(Macro macro, Scope scope) {
        switch (scope) {
            case MODELIO:
                this.modelioCatalog.addMacro(macro);
                break;
            case WORSPACE:
                this.workspaceCatalog.addMacro(macro);
                break;
            case PROJECT:
                if (this.projectCatalog != null)
                    this.projectCatalog.addMacro(macro);
                break;
            default:
                break;
        }
    }

    @objid ("5b759f97-caab-4012-91eb-fcab398f9635")
    @Override
    public void removeMacro(Macro macro, Scope scope) {
        switch (scope) {
            case MODELIO:
                this.modelioCatalog.removeMacro(macro);
                break;
            case WORSPACE:
                this.workspaceCatalog.removeMacro(macro);
                break;
            case PROJECT:
                if (this.projectCatalog != null)
                    this.projectCatalog.removeMacro(macro);
                break;
            default:
                break;
        }
    }

    @objid ("4adc2230-d3d1-45a6-9ec8-8ca8de4b069a")
    @Override
    public Catalog getCatalog(Scope scope) {
        switch (scope) {
        case MODELIO:
            return this.modelioCatalog;
        case WORSPACE:
            return this.workspaceCatalog;
        case PROJECT:
            return this.projectCatalog;
        default:
            return null;
        }
    }

    /**
     * Create medelio macro catalog file
     * @param macrosPath
     */
    @objid ("461e569c-ff21-41af-b990-4dc0e7827a73")
    private void createModelioMacroCatalogFile(Path macrosPath) {
        String sourceMacrosFileString = getFilePathOf("/res/macros");
        File scriptCatalogFile = new File(sourceMacrosFileString);
        
        try {
            FileUtils.copyDirectoryTo(scriptCatalogFile.toPath(), macrosPath);
        } catch (IOException e) {
            Script.LOG.error(e);
        }
        File catalogFile = new File(macrosPath.resolve("scripts.catalog").toString());
        catalogFile.renameTo(new File(macrosPath.resolve(".catalog").toString()));
    }

    @objid ("b1ee3aed-56a0-4a1b-9ac6-322ddcf66b17")
    private String getFilePathOf(String fileName) {
        String path="";
        Bundle bundle = Platform.getBundle(Script.PLUGIN_ID);
        String s = "platform:/plugin/"+bundle.getSymbolicName()+"/"+fileName;   // To avoid the space in the bundle path
        URL url = null;
        try {
            url = new URL(s);
            path = FileLocator.toFileURL(url).getPath();
        } catch (Exception e) {
            Script.LOG.debug("File path %s is not found!", s);
            Script.LOG.error(e);
        }
        return path;
    }

    @objid ("3b6c7920-099e-470d-a192-12360000104b")
    public class CopyDirVisitor extends SimpleFileVisitor<Path> {
        @objid ("77a9350a-9728-4e64-994b-df86694bcd20")
        private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

        @objid ("a9849ca2-70d2-4cd7-999f-a51f63e9fd33")
        private Path fromPath;

        @objid ("53177484-9240-4c2b-8cd5-b5d61e318f1a")
        private Path toPath;

        @objid ("bf510b21-041e-4763-9de5-5cc547ea28fa")
        public CopyDirVisitor(Path fromPath, Path toPath, StandardCopyOption copyOption) {
            this.fromPath = fromPath;
            this.toPath = toPath;
            this.copyOption = copyOption;
        }

        @objid ("3ef30baa-8e7d-4996-a415-79b0798ac56f")
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path targetPath = this.toPath.resolve(this.fromPath.relativize(dir));
            
            if(!Files.exists(targetPath)){
            
                Files.createDirectory(targetPath);
            
            }
            return FileVisitResult.CONTINUE;
        }

        @objid ("1ef39107-ca01-4b6f-b747-4cbd666c45f3")
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, this.toPath.resolve(this.fromPath.relativize(file)), this.copyOption);
            return FileVisitResult.CONTINUE;
        }

    }

}

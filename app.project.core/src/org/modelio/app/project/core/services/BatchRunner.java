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
                                    

package org.modelio.app.project.core.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map.Entry;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.Modelio;
import org.modelio.app.project.core.creation.ProjectCreationDataModel;
import org.modelio.app.project.core.creation.ProjectCreator;
import org.modelio.app.project.core.plugin.AppProjectCore;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectAuthenticationException;
import org.modelio.script.engine.core.engine.IScriptRunner;
import org.modelio.script.engine.core.engine.ScriptRunnerFactory;

@objid ("00470784-8d2e-10b4-9941-001ec947cd2a")
class BatchRunner implements Runnable {
    @objid ("00471044-8d2e-10b4-9941-001ec947cd2a")
    private final CommandLineData batchData;

    @objid ("0047219c-8d2e-10b4-9941-001ec947cd2a")
    private final IProjectService projectService;

    @objid ("004730a6-8d2e-10b4-9941-001ec947cd2a")
    public BatchRunner(IProjectService projectService, CommandLineData batchData) {
        this.projectService = projectService;
        this.batchData = batchData;
    }

    @objid ("00474a32-8d2e-10b4-9941-001ec947cd2a")
    @Override
    public void run() {
        AppProjectCore.LOG.debug("running batch data : %s", this.batchData);
        // // Change the workspace if required
        if (this.batchData.getWorkspace() != null) {
            File wkdir = new File(this.batchData.getWorkspace());
            try {
                wkdir = wkdir.getCanonicalFile();
            } catch (final IOException e) {
                AppProjectCore.LOG.error(e);
            }
        
            if (wkdir.exists()) {
                if (wkdir.isDirectory() && wkdir.canWrite()) {                    
                    this.projectService.changeWorkspace(wkdir.toPath());
                } else {                    
                    AppProjectCore.LOG.error("Invalid workspace directory: %s", this.batchData.getWorkspace());
                    System.exit(-1);
                }
            } else {
                wkdir.mkdirs();
                this.projectService.changeWorkspace(wkdir.toPath());
            }
        }
        
        // Handle -create option
        final String projectName = this.batchData.getProjectName();
        if (this.batchData.isCreate() && projectName != null) {
            // Check option parameters
            // The project must not exist in the current workspace
            if (!Files.exists(this.projectService.getWorkspace().resolve(projectName))) {
                ProjectCreationDataModel pm = new ProjectCreationDataModel(this.projectService.getWorkspace());
                pm.setProjectName(projectName);
        
                ProjectCreator projectCreator = new ProjectCreator();
        
                this.projectService.createProject(projectCreator, pm);
            } else {
                AppProjectCore.LOG.error("The project '%s'  already exists in the workspace.", projectName);
                System.exit(-1);
            }
        }
        
        // Check option parameters
        // The project must exist in the current workspace
        GProject openedProject = this.projectService.getOpenedProject();
        if (projectName != null) {
            assert (openedProject == null);
            try {
                this.projectService.openProject(projectName, null, null);
            } catch (GProjectAuthenticationException e) {
                AppProjectCore.LOG.error(e);
            }
            openedProject = this.projectService.getOpenedProject();
            if (openedProject == null) {
                AppProjectCore.LOG
                .error("The project '%s'  could not be opened in the workspace.", projectName);
                System.exit(-1);
            }
        }
        
        File scriptFile = null;
        // The script file must be accessible
        if (this.batchData.getScript() != null) {
            scriptFile = new File(this.batchData.getScript());
            if (!(scriptFile.exists() && scriptFile.canRead() && scriptFile.isFile())) {
                AppProjectCore.LOG.error("The Script file: '%s' was not found or not accessible.", scriptFile.getAbsolutePath());
                System.exit(-1);
            }
        }
        
        // If a project was opened and if there is a script to launch
        // run in batch mode
        if (openedProject != null && scriptFile != null) {
            AppProjectCore.LOG.info("Running script: %s", scriptFile);
            int retcode = 0;
            try {
                if (scriptFile.getName().endsWith(".py")) {
                    final IScriptRunner scriptRunner = ScriptRunnerFactory.getInstance().getScriptRunner("jython");
                    scriptRunner.addClassLoader(scriptRunner.getEngine().getClass().getClassLoader());
                    scriptRunner.bind("coreSession", openedProject.getSession());
                    scriptRunner.bind("modelingSession", Modelio.getInstance().getModelingSession());
                    scriptRunner.bind("parameters", this.batchData.getBatchParameters());
                    
                    for (Entry<String, String> p : this.batchData.getBatchParameters().entrySet())
                        scriptRunner.bind(p.getKey(), p.getValue());
        
                    scriptRunner.runFile(scriptFile.toPath(), null, null);
                } else if (scriptFile.getName().endsWith(".jar")) {
                    // Run .jar main class as a E4 processor
                    new JarRunner().runJarFile(openedProject, scriptFile, this.batchData.getBatchParameters());
                } else {
                    AppProjectCore.LOG.error("The script must a Python script (.py), aborting...");
                }
            } catch (Throwable e) {
                AppProjectCore.LOG.error("Unexpected exception:");
                AppProjectCore.LOG.error(e);
                retcode = -1;
            } finally {
                this.projectService.closeProject(openedProject);
            }
        
            System.exit(retcode);
        }
    }

}

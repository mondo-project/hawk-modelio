package org.modelio.api.impl.app;

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.modelio.api.app.IModelioContext;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.vbasic.version.Version;

@objid ("0c8c9168-dc59-4af9-97d4-72d44b05742f")
public class ModelioContext implements IModelioContext {
    @objid ("d1720ac9-e9c4-4820-8fea-464db62453ea")
    private IProjectService projectService;

    /**
     * Gets the internal build number of Modelio. The build number is different from the Modelio version number. The
     * build number is used only for debugging or problem analyzing.
     */
    @objid ("0a4f696a-7f62-4209-ab9d-0c290cd0f24f")
    public String getBuildNumber() {
        return ModelioEnv.MODELIO_VERSION;
    }

    /**
     * Get the language defined for ressources. The returned value is the value that Locale.getDefault().getLanguage()
     * returns if this value is supported by Modelio. Otherwise 'us' is returned.
     * @return a String containing the language used for Modelio resources.
     */
    @objid ("8b3ba8b8-4d09-4d8e-9064-983191cc6b26")
    @Override
    public String getLanguage() {
        String language = Platform.getNL();
        if(language.contains("_")){
            Platform.getNL().substring(0,language.lastIndexOf("_"));
        }
        return Platform.getNL();
    }

    /**
     * Get the version of the current Modelio
     * @return an object of the Version class that represent the version of the current Modelio.
     */
    @objid ("1fac425c-de46-4e1e-b4d3-45a97af65df0")
    @Override
    public Version getVersion() {
        return new Version(ModelioEnv.MODELIO_VERSION);
    }

    @objid ("c15f7253-d64d-4950-a048-85b14d9708ef")
    @Override
    public File getProjectSpacePath() {
        GProject openedProject = this.projectService.getOpenedProject();
        if (openedProject != null) {
            return openedProject.getProjectPath().toFile();
        }
        return null;
    }

    @objid ("9675b57d-2e1b-41a6-9a42-abaca7ded8ed")
    @Override
    public File getWorkspacePath() {
        return this.projectService.getWorkspace().toFile();
    }

    @objid ("043b6707-6d21-4449-9b07-032f9f3cc2e3")
    public ModelioContext(IProjectService projectService) {
        this.projectService = projectService;
    }

}

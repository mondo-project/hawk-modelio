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

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.modelio.app.core.IModelioService;
import org.modelio.app.project.core.creation.IProjectCreationData;
import org.modelio.app.project.core.creation.IProjectCreator;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.FragmentConflictException;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectAuthenticationException;
import org.modelio.vbasic.auth.IAuthData;
import org.modelio.vbasic.progress.IModelioProgress;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * IProjectService: modelio application scoped services dealing with
 * <ul>
 * <li>workspace management</li>
 * <li>project management</li>
 * 
 * IProjectService has a singleton instance available for injection.
 * 
 * @author phv
 */
@objid ("0056d718-9dc5-103b-a520-001ec947cd2a")
public interface IProjectService extends IModelioService {
    /**
     * Changes the current workspace to the given path. Changing the current workspace is not possible when a project is currently
     * opened.
     * @param path the path of the new workspace to use. Must be a valid directory path.
     * @throws java.lang.IllegalArgumentException If the given path is cannot be used as a workspace path.
     * @throws java.lang.IllegalStateException If a project is currently opened.
     */
    @objid ("0082e218-acc2-103b-a520-001ec947cd2a")
    void changeWorkspace(final Path path) throws IllegalArgumentException, IllegalStateException;

    /**
     * Opens a project in the application.
     * <p>
     * On successful return the given project receives an active CoreSession instance.
     * @param project The project to open. The project must not be already opened.
     * @param authData project authentication data.
     * @param monitor a progress monitor.
     * @throws java.io.IOException If the project opening failed at the IO level.
     * @throws java.lang.IllegalArgumentException If <code>project</code> is null.
     * @throws java.lang.IllegalStateException If a project is currently opened.
     * @throws org.modelio.gproject.gproject.GProjectAuthenticationException if the authentication fails
     */
    @objid ("0082f550-acc2-103b-a520-001ec947cd2a")
    void openProject(final ProjectDescriptor project, IAuthData authData, IProgressMonitor monitor) throws GProjectAuthenticationException, IllegalArgumentException, IOException, IllegalStateException;

    /**
     * Closes the project currently opened in the application.
     * @param project The project to close. Must be equal to the currently opened project.
     * @throws java.lang.IllegalArgumentException If <code>project</code> is null or different from the currently opened project.
     * @throws java.lang.IllegalStateException If no project is currently opened.
     */
    @objid ("0083087e-acc2-103b-a520-001ec947cd2a")
    void closeProject(final GProject project) throws IllegalArgumentException, IllegalStateException;

    /**
     * Saves the contents of the project currently opened in the application.
     * @param monitor a progress monitor. If <code>null</code>, no progress will be reported.
     * @throws java.io.IOException If the project saving failed at the IO level.
     * @throws java.lang.IllegalStateException If no project is currently opened.
     */
    @objid ("00831bd4-acc2-103b-a520-001ec947cd2a")
    void saveProject(IProgressMonitor monitor) throws IOException, IllegalStateException;

    /**
     * Gets the currently opened project.
     * @return the currently opened project or null if none.
     */
    @objid ("00832174-acc2-103b-a520-001ec947cd2a")
    GProject getOpenedProject();

    /**
     * Gets the current workspace path.
     * @return the current workspace path or null if none.
     */
    @objid ("008329e4-acc2-103b-a520-001ec947cd2a")
    Path getWorkspace();

    /**
     * Fires a refresh of the workspace contents. The workspace directory will be re-scanned for new/removed projects and the
     * workspace internal data structures updated accordingly.
     */
    @objid ("0083325e-acc2-103b-a520-001ec947cd2a")
    void refreshWorkspace();

    /**
     * Deletes a project from the workspace. This operation is definitive. The project must not be currently opened.
     * @param projectToDelete the project to delete.
     * @throws java.io.IOException in case of I/O failure.
     * @throws java.nio.file.FileSystemException in case of file system error.
     * @Throws IllegalStateException If <code>project</code> is currently opened.
     */
    @objid ("008838ee-8c65-103c-a520-001ec947cd2a")
    void deleteProject(ProjectDescriptor projectToDelete) throws IOException, FileSystemException;

    /**
     * Exports the whole contents of a project into a single archive file.
     * @param project the project to export. It must not be opened.
     * @param archivePath the path of the file archive to produce.
     * @param monitor a progress monitor.
     */
    @objid ("0088453c-8c65-103c-a520-001ec947cd2a")
    void exportProject(ProjectDescriptor project, Path archivePath, IModelioProgress monitor);

    /**
     * Imports a project from an 'export' archive into the workspace.
     * @param archivePath the path of the file archive that contains the project to import. This archive must have been produced by the
     * 'export' command of the application.
     */
    @objid ("0088563a-8c65-103c-a520-001ec947cd2a")
    void importProject(Path archivePath);

    /**
     * Creates a new project in the current workspace. The nature and the properties of the project to create are passed in the
     * <code>dataModel</code> argument.
     * @param delegateProjectCreator the delegated project creator, can be null. If null a standard default project creator will be used
     * @param data the nature, characteristics and properties of the project to create.
     */
    @objid ("00886224-8c65-103c-a520-001ec947cd2a")
    void createProject(IProjectCreator delegateProjectCreator, IProjectCreationData data);

    /**
     * Adds a model fragment to the currently opened project.
     * @param project the project to modify
     * @param fragmentDescriptor the descriptor of the fragment to add.
     * @param monitor a progress monitor.
     * @throws org.modelio.gproject.gproject.FragmentConflictException if a fragment with same name or URI is already deployed.
     */
    @objid ("00545222-bb2f-103c-a520-001ec947cd2a")
    void addFragment(GProject project, FragmentDescriptor fragmentDescriptor, IProgressMonitor monitor) throws FragmentConflictException;

    /**
     * @return the modeling session.
     */
    @objid ("0054641a-bb2f-103c-a520-001ec947cd2a")
    ICoreSession getSession();

    /**
     * Remove a model fragment from the currently opened project.
     * <p>
     * All fragment datas will be deleted from disk.
     * @param project the project to modify
     * @param fragment the fragment to remove
     */
    @objid ("002f56d4-a4c3-1044-a30e-001ec947cd2a")
    void removeFragment(GProject project, IProjectFragment fragment);

    /**
     * Opens the project name 'projectName' in the current workspace.
     * @param projectName the project name.
     * @param authData authentication data, may be <code>null</code> if not needed.
     * @param monitor a progress monitor.
     * @throws org.modelio.gproject.gproject.GProjectAuthenticationException in case of authentication failure
     */
    @objid ("004e41e8-8d1e-10b4-9941-001ec947cd2a")
    void openProject(String projectName, IAuthData authData, IProgressMonitor monitor) throws GProjectAuthenticationException;

    /**
     * @param nodeId a preference node identifier.
     * @return the preference store for the node.
     */
    @objid ("60dd0f06-fe9e-4698-9611-18477c247b19")
    IPreferenceStore getProjectPreferences(String nodeId);

    /**
     * Tells whether the session or the project preferences needs to be saved.
     * @return <code>true</code> if the session or the project preferences needs to be saved, <code>false</code> otherwise.
     */
    @objid ("38cdfb9d-8aaf-4b8a-b00c-55319d7ec41c")
    boolean isDirty();

    /**
     * Creates a new project in the current workspace. The nature and the properties of the project to create are passed in the
     * <code>dataModel</code> argument.
     * @param projectCreator the delegated project creator, can be null. If null a standard default project creator will be used
     * @param data the nature, characteristics and properties of the project to create.
     * @param monitor a progress monitor
     * @throws java.io.IOException in case of failure
     */
    @objid ("8e2f8b3f-a57d-4898-b25d-1ad77925152d")
    void createProject(IProjectCreator projectCreator, IProjectCreationData data, IProgressMonitor monitor) throws IOException;

    /**
     * Rename a project and adapt its directory to match the new name.
     * @param projectDescriptor the project to edit.
     * @param name the new name.
     * @throws java.io.IOException in case of I/O failure.
     * @throws java.nio.file.FileSystemException in case of file system error.
     */
    @objid ("0c049288-0008-4133-a8ae-527b5c3a01de")
    void renameProject(ProjectDescriptor projectDescriptor, String name) throws IOException, FileSystemException;

}

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
                                    

package org.modelio.app.model.imp.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.model.imp.plugin.AppModelImport;
import org.modelio.gproject.model.api.MTools;
import org.modelio.gproject.model.importer.defaultimporter.DefaultImporter;
import org.modelio.metamodel.analyst.AnalystItem;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.PropertyContainer;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Imports many elements from a project to the local project.
 */
@objid ("aa9a3209-13db-441b-8d4f-734320726209")
public class ModelImporter implements IRunnableWithProgress {
    @objid ("768b7ef2-cc2b-46c9-8c3a-c717356426e6")
    private ModelImportDataModel importedModel;

    @objid ("66b7978f-cb11-4277-94a3-416fa72a6819")
    private ICoreSession localSession;

    @objid ("41ecbda7-664b-4556-a518-55dbe3642aa6")
    private Project localProject;

    @objid ("9fe04026-eb8a-4f80-b6f8-e44307a599df")
    private ModuleComponent localModule;

    @objid ("65e56b52-1378-4fc8-9b8e-4b4b589882d5")
    private AnalystProject localAnalystProject;

    /**
     * Initialize the model importer
     * @param localSession the model to import elements into
     * @param importedModel the model elements to import.
     */
    @objid ("8ab86f97-c77b-488c-b773-15625fadb57c")
    public ModelImporter(ICoreSession localSession, Project localProject, AnalystProject localAnalystProject, ModuleComponent localModule, ModelImportDataModel importedModel) {
        this.importedModel = importedModel;
        this.localProject = localProject;
        this.localAnalystProject = localAnalystProject;
        this.localModule = localModule;
        this.localSession = localSession;
    }

    @objid ("fb8e79cd-7aa1-4291-ad9a-17d9d2b624b8")
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        List<SmObjectImpl> importedUmlElements = new ArrayList<>();
        List<SmObjectImpl> importedAnalystElements = new ArrayList<>();
        List<SmObjectImpl> importedAnalystProperties = new ArrayList<>();
        List<SmObjectImpl> importedMdaElement = new ArrayList<>();
        List<SmObjectImpl> importedDiagrams = new ArrayList<>();
        
        monitor.beginTask(AppModelImport.I18N.getString("ImportModelDialog.ImportProgressMessage"), IProgressMonitor.UNKNOWN);
        
        // Dispatch the elements to import in the appropriate lists
        for (SmObjectImpl elementToImport : this.importedModel.getElementsToImport()) {
            dispatchElementToImport(elementToImport,
                    elementToImport,
                    importedUmlElements,
                    importedAnalystElements,
                    importedAnalystProperties,
                    importedDiagrams,
                    importedMdaElement);
        }
        
        // Init local roots
        Package localUmlRoot = this.localProject.getModel();
        DiagramSet localDiagramRoot = this.localProject.getDiagramRoot();
        PropertyContainer propertyRoot = this.localAnalystProject.getPropertyRoot();
        
        if (this.importedModel.isReidentify()) {
            // Import by copy
            try {
                List<List<? extends MObject>> toCopy = new ArrayList<>();
                List<MObject> target = new ArrayList<>();
                
                if (this.localModule != null && !importedMdaElement.isEmpty()) {
                    toCopy.add(importedMdaElement);
                    target.add(this.localModule);
                }
                if (localUmlRoot != null && !importedUmlElements.isEmpty()) {
                    toCopy.add(importedUmlElements);
                    target.add(localUmlRoot);
                }
                if (!importedAnalystElements.isEmpty()) {
                    toCopy.add(importedAnalystElements);
                    target.add(this.localAnalystProject);
                }
                if (!importedAnalystProperties.isEmpty()) {
                    toCopy.add(importedAnalystProperties);
                    target.add(propertyRoot);
                }
                if (localDiagramRoot != null && !importedDiagrams.isEmpty()) {
                    toCopy.add(importedDiagrams);
                    target.add(localDiagramRoot);
                }
                
                if (toCopy.size() > 0) {
                    MTools.getModelTool().copyElements(toCopy, target);
                }
            } catch (RuntimeException e) {
                displayError(e);
                throw new InvocationTargetException(e);
            } catch (Exception e) {
                displayError(e);
                InterruptedException e2 = new InterruptedException();
                e2.initCause(e);
                throw e2;
            }
        } else {
            // Standard import
            try {
                ICoreSession importedSession = this.importedModel.getImportedProject().getSession();
                DefaultImporter importer = new DefaultImporter();
        
                if (localUmlRoot != null && ! importedUmlElements.isEmpty()) {
                    importer.execute(this.localSession,
                            (SmObjectImpl) localUmlRoot, 
                            importedSession,
                            importedUmlElements);
                }
                if (! importedAnalystElements.isEmpty()) {
                    importer.execute(this.localSession,
                            (SmObjectImpl) this.localAnalystProject,
                            importedSession,
                            importedAnalystElements);
                }
                if (localDiagramRoot != null && ! importedDiagrams.isEmpty()) {
                    importer.execute(this.localSession,
                            (SmObjectImpl) localDiagramRoot,
                            importedSession,
                            importedDiagrams);
                }
            } catch (RuntimeException e) {
                displayError(e);
                throw new InvocationTargetException(e);
            } catch (Exception e) {
                displayError(e);
                InterruptedException e2 = new InterruptedException();
                e2.initCause(e);
                throw e2;
            }
        }
        
        monitor.done();
    }

    @objid ("6b65d253-6ec4-4a7d-b3fa-d78f7113a43c")
    private boolean isAnalystRoot(AnalystItem c) {
        return c.getCompositionOwner() instanceof AnalystProject;
    }

    @objid ("b7a84aed-5e77-4f62-8e71-0fa912ec0e2c")
    private void dispatchElementToImport(SmObjectImpl elementToImport, MObject dispatchKey, List<SmObjectImpl> umlsToImport, List<SmObjectImpl> importedAnalystElements, List<SmObjectImpl> importedAnalystProperties, final List<SmObjectImpl> diagramsToImport, List<SmObjectImpl> importedProfiles) throws IllegalArgumentException {
        if (dispatchKey instanceof Profile) {
            importedProfiles.add(elementToImport);
        } else if (dispatchKey instanceof NameSpace) {
            umlsToImport.add(elementToImport);
        } else if (dispatchKey instanceof AnalystItem) {
            importedAnalystElements.add(elementToImport);
        } else if (dispatchKey instanceof PropertyTableDefinition || dispatchKey instanceof PropertyDefinition || dispatchKey instanceof PropertyType) {
            importedAnalystProperties.add(elementToImport);
        } else if (dispatchKey instanceof DiagramSet || dispatchKey instanceof AbstractDiagram) {
            diagramsToImport.add(elementToImport);
        } else {
            final MObject parent = dispatchKey.getCompositionOwner();
            if (parent == null) {
                throw new IllegalArgumentException(dispatchKey.toString() + " dispatch key is not handled.");
            } else {
                dispatchElementToImport(elementToImport,
                        parent,
                        umlsToImport,
                        importedAnalystElements,
                        importedAnalystProperties,
                        diagramsToImport,
                        importedProfiles);
            }
        }
    }

    @objid ("6f29a48f-2550-4ab6-8714-fae2fa3ea0dc")
    private void displayError(final Exception e) {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e.getLocalizedMessage());
            }
        });
    }

}

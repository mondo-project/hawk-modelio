package org.modelio.diagram.browser.model.core;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.mda.Project;

@objid ("003dbdfa-0d4f-10c6-842f-001ec947cd2a")
public class ProjectRoot {
    @objid ("003dc6ba-0d4f-10c6-842f-001ec947cd2a")
    private final Project project;

    @objid ("003ddcb8-0d4f-10c6-842f-001ec947cd2a")
    public ProjectRoot(Project project) {
        this.project = project;
    }

}

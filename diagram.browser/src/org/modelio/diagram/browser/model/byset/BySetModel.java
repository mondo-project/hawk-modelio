package org.modelio.diagram.browser.model.byset;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.modelio.diagram.browser.model.core.AbstractModel;
import org.modelio.gproject.gproject.GProject;

@objid ("00450e3e-0d4f-10c6-842f-001ec947cd2a")
public class BySetModel extends AbstractModel {
    @objid ("00452694-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ITreeContentProvider getContentProvider(GProject project) {
        return new BySetContentProvider(project);
    }

}

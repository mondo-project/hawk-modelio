package org.modelio.diagram.browser.model.bytype;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.modelio.diagram.browser.model.core.AbstractModel;
import org.modelio.gproject.gproject.GProject;

@objid ("004067c6-0d4f-10c6-842f-001ec947cd2a")
public class ByTypeModel extends AbstractModel {
    @objid ("00408080-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ITreeContentProvider getContentProvider(GProject project) {
        return new ByTypeContentProvider(project);
    }

}

package org.modelio.diagram.browser.model.flat;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.modelio.diagram.browser.model.core.AbstractModel;
import org.modelio.gproject.gproject.GProject;

@objid ("0026cc44-0d4f-10c6-842f-001ec947cd2a")
public class FlatModel extends AbstractModel {
    @objid ("0026e490-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ITreeContentProvider getContentProvider(GProject project) {
        return new FlatContentProvider(project);
    }

}

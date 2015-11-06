package org.modelio.diagram.browser.model.bycontext;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.modelio.diagram.browser.model.core.AbstractModel;
import org.modelio.gproject.gproject.GProject;

@objid ("002b53f4-0d4f-10c6-842f-001ec947cd2a")
public class ByCtxModel extends AbstractModel {
    @objid ("002b7096-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ITreeContentProvider getContentProvider(GProject project) {
        return new ByCtxContentProvider(project);
    }

}

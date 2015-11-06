package org.modelio.diagram.browser.model.core;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.modelio.core.ui.images.ElementDecoratedStyledLabelProvider;
import org.modelio.diagram.browser.model.IBrowserModel;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("003ceccc-0d4f-10c6-842f-001ec947cd2a")
public abstract class AbstractModel implements IBrowserModel {
    @objid ("003d02b6-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ICellModifier getLabelEditor(TreeViewer browserView, ICoreSession iCoreSession) {
        return new DefaultLabelEditor(browserView, iCoreSession);
    }

    @objid ("003d379a-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public IBaseLabelProvider getLabelProvider(TreeViewer browserView) {
        DefaultLabelProvider baseProvider = new DefaultLabelProvider(browserView);
        return new ElementDecoratedStyledLabelProvider(baseProvider, true, true);
    }

    @objid ("003d7ed0-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public ViewerSorter getSorter() {
        return new DefaultSorter();
    }

}

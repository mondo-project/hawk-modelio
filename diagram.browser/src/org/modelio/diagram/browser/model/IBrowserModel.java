package org.modelio.diagram.browser.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.modelio.gproject.gproject.GProject;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * An IBrowserModel is used to configure the diagram browser tree aspect and behavior. The IbrowserModel provides the tree's content
 * provider, label provider, editing support and content sorter.
 * 
 * @author phv
 */
@objid ("003083d8-0d4f-10c6-842f-001ec947cd2a")
public interface IBrowserModel {
    @objid ("00309b2a-0d4f-10c6-842f-001ec947cd2a")
    ITreeContentProvider getContentProvider(GProject project);

    @objid ("0030b614-0d4f-10c6-842f-001ec947cd2a")
    IBaseLabelProvider getLabelProvider(TreeViewer browserView);

    @objid ("0030ce88-0d4f-10c6-842f-001ec947cd2a")
    ViewerSorter getSorter();

    @objid ("0030d694-0d4f-10c6-842f-001ec947cd2a")
    ICellModifier getLabelEditor(TreeViewer browserView, ICoreSession iCoreSession);

}

package org.modelio.diagram.creation.wizard.diagramcreation.treeview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.TextStyle;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.ui.UIColor;

/**
 * StyledCellLabelProvider for the diagram creation contributors tree viewer in the diagram creation wizard dialog.
 * @author xzhang
 */
@objid ("5928579c-426b-40d9-bfa1-2f099087ff1e")
public class ContributorTreeLabelProvider extends StyledCellLabelProvider {
    @objid ("2ce81c51-8584-469e-8642-7c0d6aa294dc")
    public static final Styler VALID = new Styler() {
        @Override
        public void applyStyles(TextStyle textStyle) {
            //Nothing to do
        }
    };

    @objid ("c3ec3538-ad11-41a9-8602-4edd8a3171cd")
    public static final Styler INVALID = new Styler() {
        @Override
        public void applyStyles(TextStyle textStyle) {
            textStyle.foreground = UIColor.NONMODIFIABLE_ELEMENT;
        }
    };

    @objid ("789ef5e5-3061-45f9-b188-273724620f8d")
    private ModelElement context;

    /**
     * @param context
     */
    @objid ("1932b109-fe4c-4b87-b378-909aa84cb76d")
    public ContributorTreeLabelProvider(ModelElement context) {
        this.context = context;
    }

    @objid ("5f3b4588-6a53-42db-a73d-210877929d59")
    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        StyledString text = new StyledString();
        
        if (element instanceof DiagramCategory) {
            DiagramCategory category = (DiagramCategory) element;
            text.append(category.getName());
            cell.setImage(category.getIcon());
        } else if (element instanceof IDiagramWizardContributor){
            IDiagramWizardContributor contributor = (IDiagramWizardContributor) element;
            Styler styler;
            if (contributor.accept(this.context)) {              
                styler  = VALID;
            } else {
                styler = INVALID;     // gray
            }
            text.append(contributor.getLabel(), styler);
            cell.setImage(contributor.getIcon());
        }
        cell.setText(text.getString());
        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }

}

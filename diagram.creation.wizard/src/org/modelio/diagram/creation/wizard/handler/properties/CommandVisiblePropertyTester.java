package org.modelio.diagram.creation.wizard.handler.properties;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.diagram.creation.wizard.diagramcreation.DiagramContributorManager;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * {@link PropertyTester} that tells whether commands are visible for the given selection.
 */
@objid ("cca2b44f-5d77-4ef9-91d0-3c535837a305")
public class CommandVisiblePropertyTester extends PropertyTester {
    /**
     * Default constructor.
     */
    @objid ("078ed052-e124-481c-8cfc-599d8ab1800e")
    public CommandVisiblePropertyTester() {
        // nothing
    }

    @objid ("0780cb72-6ba6-4a2a-8188-2cfe5fc86e88")
    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        try{    
            if (!(receiver instanceof IStructuredSelection)) {
                return false;
            }
            
            final IStructuredSelection selection = (IStructuredSelection) receiver;        
            DiagramContributorManager contributorManager = DiagramContributorManager.getInstance();
            ModelElement selectedElement = getSelectedElement(selection);
            if (selectedElement == null) return false;
            if (property.equals("opendiagramwizard")) {
                for (IDiagramWizardContributor contributor : contributorManager.getAllContributorsList()) {
                    if (contributor.accept(selectedElement)) {
                        return true;
                    }
                }
            } else {
                if (args != null && args.length > 0) {
                    String configurerName = args[0].toString();
                    if (configurerName != null) {
                        for (IDiagramWizardContributor contributor : contributorManager.getAllContributorsList()) {
                            if (contributor.getClass().getSimpleName().equals(configurerName)) {
                                return contributor.accept(selectedElement);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @objid ("02aed10e-ec70-487d-80c1-6e66697f3be3")
    private ModelElement getSelectedElement(final IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object element = selection.getFirstElement();
            if (element instanceof ModelElement) {
                return (ModelElement) element;
            } else if (element instanceof IAdaptable) {
                return (ModelElement) ((IAdaptable) element).getAdapter(ModelElement.class);
            }
        }
        return null;
    }

}

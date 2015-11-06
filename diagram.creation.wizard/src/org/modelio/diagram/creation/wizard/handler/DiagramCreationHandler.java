package org.modelio.diagram.creation.wizard.handler;

import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.IModelioService;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.creation.wizard.contributor.AbstractDiagramCreationContributor;
import org.modelio.diagram.creation.wizard.diagramcreation.DiagramContributorManager;
import org.modelio.diagram.creation.wizard.diagramcreation.DiagramWizardDialog;
import org.modelio.diagram.creation.wizard.diagramcreation.DiagramWizardModel;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * @see org.eclipse.core.commands.IHandler
 */
@objid ("9ea3ba75-b099-4a9f-9ec6-c3652862fe05")
public class DiagramCreationHandler {
    @objid ("2003d1ee-1dea-40d3-a8ef-91c53b7a8a15")
    @Inject
    protected IProjectService projectService;

    @objid ("f6c54863-eb19-4233-affc-3afefa603994")
    @Inject
    @Optional
    protected IMModelServices mmServices;

    @objid ("0e1205a7-4c89-4b40-873e-60bab9e6e788")
    @Inject
    protected IModelioEventService eventService;

    @objid ("e417604b-bb2f-44c2-ad2f-61b344f97775")
    @Inject
    protected IModelioPickingService pickingService;

    @objid ("251f98e3-e51b-46a1-a362-6fb12cdcbe54")
    private DiagramWizardDialog dialog = null;

    /**
     * The command has been executed, so extract extract the needed information from the application context.
     */
    @objid ("aa773628-c456-4c2f-8d24-2bd8e6fd0899")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, @Optional
@Named("contributor") final String contributorName) {
        ModelElement selectedElement = getSelectedElement(selection);
        
        if (this.dialog == null || this.dialog.getShell() == null) {
            DiagramWizardModel resultModel = null;
            IDiagramWizardContributor selectedContributor = null;
            DiagramContributorManager contributorManager = DiagramContributorManager.getInstance();
            for (IDiagramWizardContributor contributor : contributorManager.getAllContributorsList()) {
                if (contributor instanceof AbstractDiagramCreationContributor) {                
                    ((AbstractDiagramCreationContributor)contributor).setProjectService(this.projectService);
                    ((AbstractDiagramCreationContributor)contributor).setModelService(this.mmServices);
                }
                if (contributor.getClass().getSimpleName().equals(contributorName)) {
                    selectedContributor = contributor;
                    break;
                }
            }
        
            if (selectedContributor == null) {
                DiagramWizardModel dataModel = new DiagramWizardModel();
                dataModel.setContext(selectedElement);
                dataModel.setShowInvalidDiagram(false);
        
                this.dialog = new DiagramWizardDialog(Display.getDefault().getActiveShell(),
                        contributorManager,
                        dataModel, this.projectService, this.mmServices, this.pickingService);
                this.dialog.open();
                resultModel = this.dialog.getResultModel();
                if (resultModel != null) {
                    selectedContributor = resultModel.getSelectedContributor();
                }
            } else {
                resultModel = new DiagramWizardModel();
                resultModel.setContext(selectedElement);
                resultModel.setName(selectedContributor.getLabel());
                resultModel.setSelectedContributor(selectedContributor);
            }
        
            if (resultModel != null && selectedContributor != null) {
                ICoreSession session = this.projectService.getSession();
        
                try (ITransaction t = session.getTransactionSupport().createTransaction("Create diagram");) {
                    AbstractDiagram diagram = selectedContributor.actionPerformed(resultModel.getContext(), resultModel.getName(), resultModel.getDescription());
        
                    t.commit();
                    if (diagram != null) {
                        openDiagramEditor(diagram);
                    }
                }
            }
            this.dialog = null;
        } else {
            this.dialog.getShell().setFocus();
        }
        return null;
    }

    @objid ("079ac237-622a-4515-bc5a-6b5e46904d44")
    protected void openDiagramEditor(final AbstractDiagram diagram) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                DiagramCreationHandler.this.eventService.postAsyncEvent(new IModelioService() {
                    @Override
                    public String getName() {
                        return "openEditor : AbstractDiagram";
                    }
                }, ModelioEvent.EDIT_ELEMENT, diagram);
            }
        });
    }

    @objid ("3a2dad2c-f22a-444d-84ae-5020b1749e4f")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, @Optional
@Named("contributor") final String contributorName) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        if (this.mmServices == null) {
            return false;
        }
        ModelElement selectedElement = getSelectedElement(selection);
        if (selectedElement == null) {
            return false;
        }
        if (!selectedElement.getStatus().isRamc()) {
            DiagramContributorManager contributorManager = DiagramContributorManager.getInstance();
            if (contributorName == null) {
                for (IDiagramWizardContributor contributor : contributorManager.getAllContributorsList()) {
                    if (contributor.accept(selectedElement)) {
                        return true;
                    }
                }
            } else {
                for (IDiagramWizardContributor contributor : contributorManager.getAllContributorsList()) {
                    if (contributor.getClass().getSimpleName().equals(contributorName)) {
                        return contributor.accept(selectedElement);
                    }
                }
            }
        }
        return false;
    }

    @objid ("a9ba8768-c7ea-4389-b546-2fa4be32553c")
    private static ModelElement getSelectedElement(final Object selection) {
        if (selection instanceof ModelElement) {
            return (ModelElement) selection;
        } else if (selection instanceof IStructuredSelection) {
            if (((IStructuredSelection) selection).size() == 1) {
                Object element = ((IStructuredSelection) selection).getFirstElement();
                if (element instanceof ModelElement) {
                    return (ModelElement) element;
                } else if (element instanceof IAdaptable) {
                    return (ModelElement) ((IAdaptable) element).getAdapter(ModelElement.class);
                }
            }
        }
        return null;
    }

}

package org.modelio.model.search.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.model.search.dialog.SearchDialog;
import org.modelio.model.search.engine.searchers.model.ModelSearchCriteria;
import org.modelio.model.search.plugin.ModelSearch;
import org.modelio.model.search.searchers.model.ModelSearchPanel;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("000f3f70-c59e-10ab-8258-001ec947cd2a")
public class OpenSearchDialogHandler {
    @objid ("000f47fe-c59e-10ab-8258-001ec947cd2a")
    @Execute
    public void execute(final IEclipseContext context, final IProjectService projectService, final IModelioNavigationService navigationService, @Named(IServiceConstants.ACTIVE_SHELL) final Shell shell) throws InvocationTargetException, InterruptedException {
        final GProject project = projectService.getOpenedProject();
        if (project == null) {
            return;
        }
        
        ModelSearch.LOG.info("Opening advanced search dialog in project '%s' ", project.getName());
        
        final ICoreSession session = project.getSession();
               
        
        final List<Element> results = new ArrayList<>();
        
        final SearchDialog searchDialog = SearchDialog.getInstance(shell, session, results, navigationService);
        searchDialog.setBlockOnOpen(false);
        searchDialog.open();
        
        final ModelSearchCriteria searchCriteria = new ModelSearchCriteria();
        searchCriteria.setExpression(".*");
        searchCriteria.addMetaclass(Metamodel.getMClass(NameSpace.class));
        searchCriteria.setIncludeRamc(false);
        searchCriteria.setStereotype("");
        searchDialog.initCriteria(ModelSearchPanel.class, searchCriteria, null);
    }

    @objid ("000f9812-c59e-10ab-8258-001ec947cd2a")
    @CanExecute
    public boolean canExecute(final IProjectService projectService) {
        return (projectService.getOpenedProject() != null);
    }

}

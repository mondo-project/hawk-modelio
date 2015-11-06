package org.modelio.model.search;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.model.search.dialog.SearchDialog;
import org.modelio.model.search.engine.searchers.model.ModelSearchCriteria;
import org.modelio.model.search.engine.searchers.model.ModelSearchEngine;
import org.modelio.model.search.plugin.ModelSearch;
import org.modelio.model.search.searchers.model.ModelSearchPanel;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Provide a custom toolbar field (based on a combo) to provide the quick search
 * service (search a name space from name pattern)
 */
@objid ("000fd412-c59e-10ab-8258-001ec947cd2a")
public class QuickSearchCombo {
    @objid ("000fdd22-c59e-10ab-8258-001ec947cd2a")
    protected Combo searchCombo;

    @objid ("00393dfc-a34e-10ac-8258-001ec947cd2a")
    protected GProject project;

    @objid ("005a2738-ec63-10ac-8258-001ec947cd2a")
    @Inject
    protected IModelioNavigationService navigationService;

    @objid ("000ff564-c59e-10ab-8258-001ec947cd2a")
    @PostConstruct
    void createWidget(final Composite parent) {
        // Create a composite to place the label in
        final Composite comp = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        comp.setLayout(gridLayout);
        
        // Create a Combo for the search.
        this.searchCombo = new Combo(comp, SWT.DROP_DOWN | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        this.searchCombo.setToolTipText(ModelSearch.I18N.getString("QuickSearch.tooltip"));
        this.searchCombo.addSelectionListener(new SCWSelectionListener(this));
        this.searchCombo.setEnabled(false);
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.minimumWidth = 120;
        gd.widthHint = 120;
        this.searchCombo.setLayoutData(gd);
        
        this.searchCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                final Combo combo = (Combo) e.getSource();
                if (ModelSearchCriteria.isValidExpression(combo.getText())) {
                    combo.setForeground(combo.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
                } else {
                    combo.setForeground(combo.getDisplay().getSystemColor(SWT.COLOR_RED));
                }
            }
        });
    }

    /**
     * This method is called when the end-user validate (press ENTER) the filter
     * combo text.<br/>
     * It runs a default search using:
     * <ul>
     * <li>metaclass filter = NameSpace</li>
     * <li>includeRamc = true</li>
     * <li>no stereotype </li<
     * </ul>
     * @param expression the search expression
     */
    @objid ("00102d0e-c59e-10ab-8258-001ec947cd2a")
    protected void runSearch(String expression) {
        if (this.project == null) {
            return; // not expected, but in any case cannot do anything without
            // a project
        }
        
        final ICoreSession session = this.project.getSession();
        final ModelSearchEngine searchEngine = new ModelSearchEngine();
        final ModelSearchCriteria searchCriteria = new ModelSearchCriteria();
        searchCriteria.setExpression(expression);
        searchCriteria.addMetaclass(NameSpace.class);
        searchCriteria.setIncludeRamc(true);
        searchCriteria.setStereotype("");
        
        final List<Element> found = searchEngine.search(session, searchCriteria);
        switch (found.size()) {
        case 0:
            // No element found
            runExtendedSearch(expression);
            break;
        case 1:
            // One matching element found => navigate
            this.navigationService.fireNavigate(found.get(0));
            break;
        default:
            // Several matching elements => propose for choice.
            final SearchDialog dlg = SearchDialog.getInstance(this.searchCombo.getShell(), session, found, this.navigationService);
            dlg.setBlockOnOpen(false);
            dlg.open();
            dlg.initCriteria(ModelSearchPanel.class, searchCriteria, found);
            break;
        }
    }

    @objid ("0010400a-c59e-10ab-8258-001ec947cd2a")
    private void runExtendedSearch(String expression) {
        final ICoreSession session = this.project.getSession();
        final ModelSearchEngine searchEngine = new ModelSearchEngine();
        final ModelSearchCriteria searchCriteria = new ModelSearchCriteria();
        
        searchCriteria.addMetaclass(NameSpace.class);
        searchCriteria.setIncludeRamc(true);
        searchCriteria.setStereotype("");
        
        searchCriteria.setExpression((expression.endsWith(".*") == false) ? expression + ".*" : expression);
        
        final List<Element> found = searchEngine.search(session, searchCriteria);
        switch (found.size()) {
        case 1:
            // One matching element found => navigate
            this.navigationService.fireNavigate(found.get(0));
            break;
        case 0:
        default:
            // No Element found or several matching elements => propose for choice.
            final SearchDialog dlg = SearchDialog.getInstance(this.searchCombo.getShell(), session, found, this.navigationService);
            dlg.setBlockOnOpen(false);
            dlg.open();
            dlg.initCriteria(ModelSearchPanel.class, searchCriteria, found);
            break;
        }
    }

    @objid ("0010cade-c59e-10ab-8258-001ec947cd2a")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject openedProject) {
        // @UIEventTopic doesn't seems to be working here...
        ModelSearch.LOG.debug("onProjectOpened() %s", openedProject.getName());
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                QuickSearchCombo.this.searchCombo.setEnabled(true);
                QuickSearchCombo.this.searchCombo.removeAll();
                QuickSearchCombo.this.project = openedProject;
            }
        });
    }

    @objid ("00110512-c59e-10ab-8258-001ec947cd2a")
    @Inject
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject closedProject) {
        // @UIEventTopic doesn't seems to be working here...
        ModelSearch.LOG.debug("onProjectClosed() %s", closedProject);
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (! QuickSearchCombo.this.searchCombo.isDisposed()) {
                    QuickSearchCombo.this.searchCombo.setEnabled(false);
                    QuickSearchCombo.this.searchCombo.removeAll();
                    SearchDialog.closeInstance();
                }
                QuickSearchCombo.this.project = null;
            }
        });
    }

    @objid ("00106d8c-c59e-10ab-8258-001ec947cd2a")
    class SCWSelectionListener implements SelectionListener {
        @objid ("001075ca-c59e-10ab-8258-001ec947cd2a")
         QuickSearchCombo scw;

        @objid ("00107ce6-c59e-10ab-8258-001ec947cd2a")
        public SCWSelectionListener(QuickSearchCombo scw) {
            this.scw = scw;
        }

        @objid ("0010900a-c59e-10ab-8258-001ec947cd2a")
        @Override
        public void widgetSelected(SelectionEvent e) {
            final String expression = this.scw.searchCombo.getText();
            this.scw.searchCombo.setText(expression);
            this.scw.searchCombo.setSelection(new Point(expression.length(), expression.length()));
        }

        @objid ("0010ad2e-c59e-10ab-8258-001ec947cd2a")
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            final String expression = this.scw.searchCombo.getText();
            this.scw.runSearch(expression);
            this.scw.searchCombo.add(expression, 0);
            this.scw.searchCombo.setText("");
        }

    }

}

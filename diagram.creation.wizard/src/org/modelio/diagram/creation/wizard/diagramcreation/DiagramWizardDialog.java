package org.modelio.diagram.creation.wizard.diagramcreation;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.textelement.ITextElementSelectionListener;
import org.modelio.core.ui.textelement.TextElement;
import org.modelio.diagram.creation.wizard.diagramcreation.treeview.ContributorCategoryModel;
import org.modelio.diagram.creation.wizard.diagramcreation.treeview.ContributorSorter;
import org.modelio.diagram.creation.wizard.diagramcreation.treeview.ContributorTreeContentProvider;
import org.modelio.diagram.creation.wizard.diagramcreation.treeview.ContributorTreeLabelProvider;
import org.modelio.diagram.creation.wizard.diagramcreation.treeview.DiagramCategory;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("1f9f50ed-0a1e-458a-84a4-34fbd6da6f0c")
public class DiagramWizardDialog extends ModelioDialog implements Listener {
    @objid ("9728eea8-d918-4123-b62f-f29c7138631a")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_diagrams_creating_diagram.html";

    @objid ("6b1f8db1-ac8b-4f76-9610-cf5c5a30ad64")
    protected TextElement contextText;

    @objid ("9fa82c0c-cd04-4de5-94eb-3025a92c9b54")
     DiagramWizardModel dataModel;

    @objid ("ac91dcef-b0ba-4a28-80e2-63bc2024481a")
    protected DiagramWizardModel resultModel = null;

    @objid ("4ad60f6c-07c9-480f-a649-e789e540fec2")
    protected Button okButton;

    @objid ("415a5a6b-faa0-434b-a1a4-f89577b9efdc")
    protected Button cancelButton;

    @objid ("c14f59ef-c3d3-4e9d-87cd-edf025bce332")
    protected Button hideInvalidCheckBox;

    @objid ("f92487ed-b552-4210-9269-944d2d63c3aa")
    protected Text nameText;

    @objid ("e791c327-798b-4880-a2f7-f0430a341c53")
    protected StyledText descriptionText;

    @objid ("667d4ce2-df90-491a-bdf1-a9477186555f")
    protected TreeViewer treeViewer;

    @objid ("67b8738f-7cf4-4346-bbec-617d11d5530a")
    protected StyledText detailsText;

    @objid ("92bcd34a-bc99-444f-95d6-b057ed2546e8")
    protected IProjectService projectService;

    @objid ("347a39a7-447a-495f-9b4a-7af94dbfd2cb")
    protected IMModelServices mmServices;

    @objid ("cfc0d2ef-a01d-4531-8ae2-b27e85853842")
    protected IModelioPickingService pickingService;

    @objid ("505648c6-411e-4f36-9069-c7be12f93550")
    private DiagramContributorManager contributorManager;

    @objid ("37a67537-6350-4421-b24b-67dd306783a8")
    private ContributorTreeContentProvider contentProvider;

    @objid ("d45f3f63-0f45-4f8a-9c57-d9ba5d16a246")
    private IMObjectFilter elementFilter;

    @objid ("bc8544a3-4aec-4f18-96f5-e057265297de")
    public void setShowInvalidDiagram(final Boolean value) {
        // if the selectedContributor is invalid and change to not show invalid mode,
        // clear the texts and messages
        if (!value) {
            IDiagramWizardContributor selectedContributor = this.dataModel.getSelectedContributor();
            if (selectedContributor != null && !selectedContributor.accept(this.dataModel.getContext())) {
                this.nameText.setText("");
                setDefaultContributor(null);
                updateDetailsText("");
                setMessage("");
            }
            setDefaultContributor(null);
        }
        this.dataModel.setShowInvalidDiagram(value);
    }

    @objid ("5272a843-0480-4428-874d-5253bd510827")
    public void setDefaultContributor(final IDiagramWizardContributor contributor) {
        this.dataModel.setSelectedContributor(contributor);
    }

    @objid ("c8eae388-4fc3-4b1d-9a4c-75fc5ec39fc3")
    void update() {
        IDiagramWizardContributor selectedContributor = this.dataModel.getSelectedContributor();
        
        // Reset allowed metaclasses...
        if (selectedContributor != null) {
            this.contextText.getAcceptedMetaclasses().clear();
            this.contextText.getAcceptedMetaclasses().addAll(selectedContributor.getAcceptedMetaclasses());
            this.contextText.setAcceptNullValue(false);
            this.contextText.getTextControl().setForeground(UIColor.RED);
        }
        this.contentProvider.setShowInvalid(this.dataModel.isShowInvalidDiagram());
        this.treeViewer.refresh();
        Object[] expandedElements = this.treeViewer.getExpandedElements();
        this.treeViewer.setExpandedElements(expandedElements);
        
        if (selectedContributor != null) {
            this.nameText.setText(selectedContributor.getLabel());
            updateDetailsText(selectedContributor.getDetails());
            setMessage(selectedContributor.getInformation());
            if (selectedContributor.accept(this.dataModel.getContext())) {
                this.contextText.getTextControl().setForeground(UIColor.BLACK);
            }
        }
    }

    @objid ("db126456-06e9-401e-9ad7-2c6940f35530")
    private void addListeners() {
        this.nameText.addListener(SWT.Modify, this);
        this.descriptionText.addListener(SWT.Modify, this);
        this.hideInvalidCheckBox.addListener(SWT.Selection, this);
        this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) DiagramWizardDialog.this.treeViewer.getSelection();
                if (selection.getFirstElement() instanceof IDiagramWizardContributor) {
                    setDefaultContributor((IDiagramWizardContributor) selection.getFirstElement());
                    update();
                    updateOkButton();
                }
            }
        });
        this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            
            @Override
            public void doubleClick(DoubleClickEvent event) {
                okPressed();    // Double click to create the selected diagram
            }
        });
        this.cancelButton.addListener(CANCEL, this);
        this.okButton.addListener(OK, this);
        this.contextText.addListener(new ITextElementSelectionListener() {
            
            @Override
            public void selectedElementChanged(MObject oldElement, MObject newElement) {
                DiagramWizardDialog.this.dataModel.setContext((ModelElement) newElement);
                update();
                updateOkButton();
            }
        });
        this.detailsText.addListener(SWT.MouseDown, this);
    }

    @objid ("c9247f5a-8c13-47bd-939d-c065b6b564c2")
    @Override
    protected void okPressed() {
        super.okPressed(); 
        this.resultModel = this.dataModel;
    }

    @objid ("2c438d24-2bd0-4f4c-a660-4bf6245eff3b")
    protected void openBrowser() {
        String helpUrl = this.dataModel.getSelectedContributor().getHelpUrl();
        if (helpUrl != null) {
            BrowserDialog dialog = new BrowserDialog(this.getShell(), helpUrl);
            dialog.open();
        }
    }

    @objid ("e6154d02-619b-4bdd-b0d5-fbb7e800b7fc")
    @Override
    public void init() {
        Shell shell = getShell();
        
        shell.setSize(800, 600);
        // The shell has to be centered
        int x = (this.getShell().getSize().x / 2) - 250;
        int y = (this.getShell().getSize().y / 2) - 100;
        shell.setLocation(x, y);
        shell.setMinimumSize(800, 600);
        shell.setText(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.ShellTitle"));
        setTitle(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.Title"));
        setMessage(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.Message"));
        
        this.nameText.setText(this.dataModel.getName());
        this.descriptionText.setText(this.dataModel.getDescription());
        this.hideInvalidCheckBox.setSelection(!this.dataModel.isShowInvalidDiagram());
        
        addListeners();
        
        if (this.dataModel.getSelectedContributor() == null) {
            ModelElement initialContext = this.dataModel.getContext();
            MObject context = initialContext;
        
            List<IDiagramWizardContributor> validContributors = getValidContributors(context);
            while (validContributors.size() == 0 && context != null) {
                context = context.getCompositionOwner();
                validContributors = getValidContributors(context);
            }
        
            if (validContributors.size() > 0) {
                IDiagramWizardContributor newContributor = validContributors.get(0);
                this.dataModel.setSelectedContributor(newContributor);
                this.contextText.getAcceptedMetaclasses().addAll(newContributor.getAcceptedMetaclasses());
                this.contextText.setAcceptNullValue(false);
                this.contextText.setValue(context);
                this.dataModel.setContext((ModelElement) context);
                DiagramCategory category = this.getContributorCategoryModel().getCategoryOfContributor(newContributor);
                this.treeViewer.setExpandedElements(new Object[]{category});
                this.treeViewer.setSelection(new StructuredSelection(newContributor), true);
            }
        } else {
            // Get the first valid context...
            IDiagramWizardContributor contributor = this.dataModel.getSelectedContributor();
            ModelElement initialContext = this.dataModel.getContext();
            MObject context = initialContext;
        
            while (!contributor.accept(context) && context != null) {
                context = context.getCompositionOwner();
            }
        
            this.contextText.getAcceptedMetaclasses().addAll(contributor.getAcceptedMetaclasses());
            this.contextText.setAcceptNullValue(false);
            this.contextText.setValue(context);
            this.dataModel.setContext((ModelElement) context);
        }
    }

    @objid ("dfed3213-6b30-48b3-bdf2-e34e2496fbe7")
    public DiagramWizardModel getResultModel() {
        return this.resultModel;
    }

    @objid ("3ba41043-418f-478a-8245-5253493d105a")
    @Override
    public boolean close() {
        this.contextText.activatePicking(null);
        this.contextText.activateCompletion(null);
        return super.close();
    }

    @objid ("43e0fce7-c9e4-41f7-b702-c521ad852902")
    private void updateDetailsText(final String text) {
        String helpUrl = null;
        if (this.dataModel.getSelectedContributor()!=null) helpUrl = this.dataModel.getSelectedContributor().getHelpUrl();
        if (helpUrl != null) {
            // When an URL exists, add an hyperlink after the text.
            String moreText = DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.More");
            this.detailsText.setText(text + " " + moreText);
            StyleRange style = new StyleRange();
            style.underline = true;
            style.underlineStyle = SWT.UNDERLINE_LINK;
        
            int[] ranges = {this.detailsText.getText().length() - moreText.length(), moreText.length()}; 
            StyleRange[] styles = {style};
            this.detailsText.setStyleRanges(ranges, styles);
        } else {
            this.detailsText.setText(text);   
        }
    }

    @objid ("52e2480d-b76d-4a54-9aa6-3b5816804629")
    public DiagramWizardDialog(final Shell parentShell, final DiagramContributorManager contributorManager, final DiagramWizardModel dataModel, IProjectService projectService, IMModelServices mmService, IModelioPickingService pickingService) {
        super(parentShell);
        this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX);
        
        this.contributorManager = contributorManager;
        this.dataModel = dataModel;
        this.projectService = projectService;
        this.mmServices = mmService;
        this.pickingService = pickingService;
        
        this.contentProvider = new ContributorTreeContentProvider(this.dataModel.getContext(), this.dataModel.isShowInvalidDiagram());
        this.elementFilter = new IMObjectFilter() {
            
            @Override
            public boolean accept(MObject element) {
                if (DiagramWizardDialog.this.dataModel.getSelectedContributor() != null) {                    
                    return DiagramWizardDialog.this.dataModel.getSelectedContributor().accept(element);
                }
                return false;
            }
        };
    }

    @objid ("74a6a243-c2c7-40f6-891f-fe46490813e3")
    public List<IDiagramWizardContributor> getValidContributors(final MObject context) {
        List<IDiagramWizardContributor> result = new ArrayList<>();
        for(IDiagramWizardContributor type :  this.contributorManager.getAllContributorsList()){
            if(context != null){
                if(type.accept(context)){
                    result.add(type);
                }
            }
        }
        return result;
    }

    @objid ("bab7c3bf-a7d2-4608-b888-aa89e92a0fc0")
    @Override
    public void handleEvent(final Event event) {
        if (event.widget.equals(this.nameText)) {
            this.dataModel.setName(this.nameText.getText());
        } else if (event.widget.equals(this.descriptionText)) {
            this.dataModel.setDescription(this.descriptionText.getText());
        } else if (event.widget.equals(this.hideInvalidCheckBox)) {
            setShowInvalidDiagram(!this.hideInvalidCheckBox.getSelection());
            update();
        } else if (event.widget.equals(this.detailsText)) {
            try {
                int offset = this.detailsText.getOffsetAtLocation(new Point (event.x, event.y));
                StyleRange style = this.detailsText.getStyleRangeAtOffset(offset);
                if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
                    openBrowser();
                }
            } catch (IllegalArgumentException e) {
                // no character under event.x, event.y
            }
        }
        
        updateOkButton();
    }

    @objid ("c9db9dd4-a9cf-4e08-b462-28a675e0e7d6")
    protected void updateOkButton() {
        ModelElement context = this.dataModel.getContext();
        if (this.dataModel.getSelectedContributor() != null && context != null && this.dataModel.getSelectedContributor().accept(context)) {
            this.okButton.setEnabled(true);
        } else {
            this.okButton.setEnabled(false);
        }
    }

    @objid ("825a2d6a-684f-4ef1-806c-6b3a4b630b16")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        this.okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        this.cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @objid ("3b6f4f10-775b-43dc-a80b-ca0028d58f1e")
    @Override
    public Control createContentArea(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite g1 = new Composite(composite, SWT.NONE);
        GridLayout gl1 = new GridLayout(1, false);
        
        g1.setLayout(gl1);
        FormData fdDiagramG1 = new FormData();
        fdDiagramG1.top = new FormAttachment(0, 10);
        fdDiagramG1.bottom = new FormAttachment(100, -10);
        fdDiagramG1.left = new FormAttachment(0, 10);
        fdDiagramG1.right = new FormAttachment(40, 0);
        g1.setLayoutData(fdDiagramG1);
        
        // Diagrams list tree viewer
        this.treeViewer = new TreeViewer(g1, SWT.V_SCROLL | SWT.BORDER);
        this.treeViewer.setContentProvider(this.contentProvider);
        this.treeViewer.setLabelProvider(new ContributorTreeLabelProvider(this.dataModel.getContext()));
        this.treeViewer.setSorter(new ContributorSorter(this.dataModel.getContext()));
        this.treeViewer.setInput(new ContributorCategoryModel(this.contributorManager.getContributorsMap()));
        this.treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
        
        // Hide invalid checkbox
        this.hideInvalidCheckBox = new Button(g1, SWT.CHECK);
        this.hideInvalidCheckBox.setText(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.HideInvalid")); 
        this.hideInvalidCheckBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Composite g3 = new Composite(composite, SWT.NONE);
        g3.setLayout(new GridLayout(2, false));
        
        FormData fdG3 = new FormData();
        fdG3.top = new FormAttachment(65, 0);
        fdG3.bottom = new FormAttachment(100, -10);
        fdG3.left = new FormAttachment(g1, 10);
        fdG3.right = new FormAttachment(100, -10);
        g3.setLayoutData(fdG3);
        
        Group g2 = new Group(composite, SWT.NONE);
        g2.setLayout(new GridLayout(2, false));
        
        FormData fdG2 = new FormData();
        fdG2.top = new FormAttachment(0, 10);
        fdG2.left = new FormAttachment(g1, 10);
        fdG2.right = new FormAttachment(100, -10);
        fdG2.bottom = new FormAttachment(g3, -10);
        g2.setLayoutData(fdG2);
          
        Label nameLabel = new Label(g2, SWT.NONE);
        nameLabel.setText(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.Name"));
        nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        this.nameText = new Text(g2, SWT.BORDER);
        this.nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Label contextLabel = new Label(g2, SWT.NONE);
        contextLabel.setText(DiagramCreationWizard.I18N.getMessage("Ui.DiagramCreationWizard.Context"));
        contextLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        this.contextText = new TextElement(g2, SWT.NONE);
        this.contextText.setAcceptNullValue(false);
        this.contextText.getTextControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        this.contextText.activatePicking(this.pickingService); 
        this.contextText.activateCompletion(this.projectService.getSession());
        this.contextText.setFilter(this.elementFilter);
        this.contextText.activateDragAndDrop(this.projectService.getSession()); 
        
        Label descriptionLabel = new Label(g2, SWT.NONE);
        descriptionLabel.setText(DiagramCreationWizard.I18N.getString("Ui.DiagramCreationWizard.Description"));
        descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        this.descriptionText = new StyledText(g2, SWT.BORDER | SWT.WRAP);
        this.descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.descriptionText.setBackground(UIColor.TEXT_READONLY_BG);
        this.descriptionText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        
        this.descriptionText.addFocusListener(new FocusListener() {
        
            @Override
            public void focusLost(FocusEvent e) {
                DiagramWizardDialog.this.descriptionText.setBackground(UIColor.TEXT_READONLY_BG);
            }
        
            @Override
            public void focusGained(FocusEvent e) {
                DiagramWizardDialog.this.descriptionText.setBackground(UIColor.TEXT_WRITABLE_BG);
        
            }
        });
        
        this.detailsText = new StyledText(g3, SWT.MULTI | SWT.WRAP);
        this.detailsText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        this.detailsText.setForeground(UIColor.LABEL_TIP_FG);
        this.detailsText.setEditable(false);
        this.detailsText.setBackground(g3.getBackground());
        return composite;
    }

    @objid ("e5eba316-e86b-4fd9-b5db-90ede6487573")
    private ContributorCategoryModel getContributorCategoryModel() {
        return (ContributorCategoryModel) this.treeViewer.getInput();
    }

    @objid ("763f832c-825a-47d3-aca8-2c4ed70d988b")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

}

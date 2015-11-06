package org.modelio.model.search.searchers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.modelio.metamodel.Metamodel;
import org.modelio.model.search.ISearchController;
import org.modelio.model.search.ISearchPanel;
import org.modelio.model.search.dialog.SearchDialog.MetaclassLabelProvider;
import org.modelio.model.search.engine.ISearchCriteria;
import org.modelio.model.search.engine.searchers.model.ModelSearchCriteria;
import org.modelio.model.search.plugin.ModelSearch;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * Model search criteria panel. It is composed of:
 * <ul>
 * <li>a name pattern field</li>
 * <li>a metaclass selection table</li>
 * <li>a checkbox to include or not the 'ramc' elements</li<
 * <li>a text field to enter a (unique) stereotype name</li>
 * </ul>
 */
@objid ("53e74d23-1a1a-44df-ab92-66cdaf9960f0")
public class ModelSearchPanel implements ISearchPanel {
    @objid ("04129e46-96eb-4214-ad65-13e56836cd25")
    private Group topGroup;

    @objid ("d61a65f8-2696-469c-a90f-8ab4f6e35373")
    private Text textfield = null;

    @objid ("fd51dd58-2ded-4c19-8330-4bc32e384a7a")
    private Button includeRamcCheckBox;

    @objid ("ccf2e4ea-79e3-4d17-acb7-82a966e89a40")
    private Button caseSensitiveCheckBox;

    @objid ("23b0f378-ab4a-4aff-8f05-ae3402cbc137")
    private Text stereotypeText;

    @objid ("860e0d57-f1ab-4bb7-a823-4e1ffb0bab0d")
    private ModelSearchCriteria searchCriteria;

    @objid ("20d84b83-2aeb-4d39-bcfd-87ed00b6ad11")
    protected ISearchController searchController;

    @objid ("9270f8ab-7da1-4484-8926-ef3078bf6b99")
    private TableComboViewer metaclassCombo;

    @objid ("adea39e7-a628-4e28-86df-a75b20afd176")
    @Override
    public void initialize(Composite parent, ICoreSession session, ISearchController theSearchController) {
        this.searchController = theSearchController;
        
        this.topGroup = new Group(parent, SWT.NONE);
        this.topGroup.setText(ModelSearch.I18N.getString("ModelSearch.CriteriaGroup.label")); //$NON-NLS-1$
        
        final GridLayout gridLayout = new GridLayout();
        GridData gridData = null;
        gridLayout.numColumns = 3;
        this.topGroup.setLayout(gridLayout);
        
        // The name pattern field and its label
        final Label nameLabel = new Label(this.topGroup, SWT.NONE);
        nameLabel.setText(ModelSearch.I18N.getString("ModelSearch.NamePattern.label")); //$NON-NLS-1$
        this.textfield = new Text(this.topGroup, SWT.SINGLE | SWT.BORDER);
        
        gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        this.textfield.setLayoutData(gridData);
        
        this.textfield.setToolTipText(ModelSearch.I18N.getString("ModelSearch.NamePattern.tooltip"));
        // Prevent CR from going to the default button
        this.textfield.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    e.detail = SWT.TRAVERSE_NONE;
                }
            }
        });
        
        this.textfield.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                final Text text = (Text) e.getSource();
                if (ModelSearchCriteria.isValidExpression(text.getText())) {
                    text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
                } else {
                    text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
                }
            }
        });
        
        this.textfield.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR ) {
                    ModelSearchPanel.this.searchController.runSearch();
                }
            }
        
            @Override
            public void keyPressed(KeyEvent e) {
                // do nothing
            }
        });
        
        this.caseSensitiveCheckBox = new Button(this.topGroup, SWT.CHECK);
        this.caseSensitiveCheckBox.setText("");
        this.caseSensitiveCheckBox.setToolTipText(ModelSearch.I18N.getString("ModelSearch.NameCase.tooltip"));
        gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        this.caseSensitiveCheckBox.setLayoutData(gridData);
        
        // The metaclass selector
        final Label metaclassLabel = new Label(this.topGroup, SWT.NONE);
        metaclassLabel.setText(ModelSearch.I18N.getString("ModelSearch.MetaclassSelector.label")); //$NON-NLS-1$
        
        this.metaclassCombo = new TableComboViewer(this.topGroup, SWT.READ_ONLY | SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        this.metaclassCombo.getTableCombo().setLayoutData(gridData);
        // set the content and label provider
        this.metaclassCombo.setContentProvider(ArrayContentProvider.getInstance());
        this.metaclassCombo.setLabelProvider(new MetaclassLabelProvider());
        this.metaclassCombo.getTableCombo().setToolTipText(ModelSearch.I18N.getString("ModelSearch.MetaclassSelector.tooltip"));
        
        final List<MClass> allMetaclasses = new ArrayList<MClass>(SmClass.getRegisteredClasses());
        Collections.sort(allMetaclasses, new Comparator<MClass>() {
            @Override
            public int compare(MClass o1, MClass o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        
        this.metaclassCombo.setInput(allMetaclasses);
        
        // The 'include ramc' option
        final Label includeRamcLabel = new Label(this.topGroup, SWT.NONE);
        includeRamcLabel.setText(ModelSearch.I18N.getString("ModelSearch.IncludeRamc.label")); //$NON-NLS-1$
        
        this.includeRamcCheckBox = new Button(this.topGroup, SWT.CHECK);
        this.includeRamcCheckBox.setToolTipText(ModelSearch.I18N.getString("ModelSearch.IncludeRamc.tooltip")); //$NON-NLS-1$
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.horizontalSpan = 2;
        this.includeRamcCheckBox.setLayoutData(gridData);
        
        // The 'stereotype' criterion
        final Label stereotypeLabel = new Label(this.topGroup, SWT.NONE);
        stereotypeLabel.setText(ModelSearch.I18N.getString("ModelSearch.Stereotype.label")); //$NON-NLS-1$
        
        this.stereotypeText = new Text(this.topGroup, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 2;
        this.stereotypeText.setLayoutData(gridData);
        this.stereotypeText.setToolTipText(ModelSearch.I18N.getString("ModelSearch.Stereotype.tooltip"));
        
        // Setup default values for criteria
        final ModelSearchCriteria defaultCriteria = new ModelSearchCriteria();
        defaultCriteria.setExpression(".*");
        defaultCriteria.setStereotype("");
        defaultCriteria.setIncludeRamc(false);
        defaultCriteria.addMetaclass(Metamodel.getMClass("NameSpace"));
        setCriteria(defaultCriteria);
    }

    @objid ("2085432e-37ae-4207-ba2c-b183395a0477")
    @Override
    public Control getControl() {
        return this.topGroup;
    }

    @objid ("d042b204-a503-4bd4-8d1d-b0de28fd9fe0")
    @Override
    public ISearchCriteria getCriteria() {
        this.searchCriteria.reset();
        this.searchCriteria.setExpression(this.textfield.getText());
        this.searchCriteria.setIncludeRamc(this.includeRamcCheckBox.getSelection());
        this.searchCriteria.addMetaclass((MClass) ((IStructuredSelection) this.metaclassCombo.getSelection()).getFirstElement());
        this.searchCriteria.setStereotype(this.stereotypeText.getText().trim());
        this.searchCriteria.setCaseSensitive(this.caseSensitiveCheckBox.getSelection());
        return this.searchCriteria;
    }

    @objid ("d1f57fd9-6576-4b34-ae48-d371eac5705e")
    @Override
    public void setCriteria(ISearchCriteria searchCriteria) {
        assert (searchCriteria instanceof ModelSearchCriteria);
        final ModelSearchCriteria criteria = (ModelSearchCriteria) searchCriteria;
        
        this.searchCriteria = criteria;
        
        this.textfield.setText(criteria.getExpression());
        this.includeRamcCheckBox.setSelection(criteria.isIncludeRamc());
        
        this.metaclassCombo.setSelection(new StructuredSelection(criteria.getMetaclasses()));
        
        this.stereotypeText.setText(criteria.getStereotype());
        this.caseSensitiveCheckBox.setSelection(this.searchCriteria.isCaseSensitive());
    }

    @objid ("69907fa1-dbf4-4b13-9838-96b04aa4b855")
    public ModelSearchPanel() {
    }

}

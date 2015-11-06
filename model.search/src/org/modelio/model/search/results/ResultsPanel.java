package org.modelio.model.search.results;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.core.ui.images.AbstractModelioElementLabelProvider;
import org.modelio.core.ui.images.ElementDecoratedStyledLabelProvider;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.core.ui.images.StandardModelStyleProvider;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.model.search.plugin.ModelSearch;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("ccf7d325-0d5f-4a70-8515-0947b65cd873")
public class ResultsPanel {
    @objid ("770b9119-d27c-4d63-b305-067aec6dcb4d")
    private Group topGroup;

    @objid ("6956c4bd-e9ed-4e8a-b13f-8c287518dc96")
    private TableViewer resultsViewer = null;

    @objid ("e8fe62a5-4c81-4fab-8457-b83c580e5939")
    private List<Element> results;

    @objid ("5580f7d3-0d83-406a-8153-04af7255fdd7")
    private IModelioNavigationService navigationService;

    @objid ("d18d5d6a-165a-4f9c-8221-8f3a794e33cc")
    public void showResults(List<Element> results) {
        this.results = results;
        this.resultsViewer.setInput(results);
        
        final int nMatch = (results != null) ? results.size() : 0;
        this.topGroup.setText(ModelSearch.I18N.getMessage("SearchDialog.results", Integer.toString(nMatch)));
    }

    @objid ("15ad13fd-3d61-4c9e-b22b-d6682a0bc662")
    public Control getControl() {
        return this.topGroup;
    }

    @objid ("c38077f7-43fd-4680-89fd-9ad83f4cc64d")
    public ResultsPanel(Composite parent, IModelioNavigationService navigationService) {
        this.navigationService = navigationService;
        this.topGroup = new Group(parent, SWT.NONE);
        this.topGroup.setText(ModelSearch.I18N.getMessage("SearchDialog.results", "no ")); //$NON-NLS-1$
        
        final GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        this.topGroup.setLayout(gridLayout2);
        
        this.resultsViewer = new TableViewer(this.topGroup, SWT.MULTI | SWT.FULL_SELECTION);
        this.resultsViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Make lines and make header visible
        final Table table = this.resultsViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        // First column: element name and icon
        final TableViewerColumn viewerColumn1 = new TableViewerColumn(this.resultsViewer, SWT.NONE);
        final TableColumn column1 = viewerColumn1.getColumn();
        column1.setText(ModelSearch.I18N.getString("SearchDialog.results.name"));
        column1.setWidth(250);
        column1.setResizable(true);
        column1.setMoveable(false);
        
        viewerColumn1.setLabelProvider(new ElementDecoratedStyledLabelProvider(new AbstractModelioElementLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Note) {
                    return ((Note) element).getModel().getName();
                } else
                    return ((Element) element).getName();
            }
        
            @Override
            public Image getImage(Object obj) {
                final Element element = (Element) obj;
                return ElementImageService.getIcon(element);
            }
        
            @Override
            public StyledString getStyledText(Object object) {
                return new StyledString(getText(object), null);
                // return StandardModelStyleProvider.getStyleRanges(object,
                // getText(object));
            }
        
        }, true, false));
        
        // Second column: element name space
        final TableViewerColumn viewerColumn3 = new TableViewerColumn(this.resultsViewer, SWT.NONE);
        final TableColumn column3 = viewerColumn3.getColumn();
        column3.setText(ModelSearch.I18N.getString("SearchDialog.results.from"));
        column3.setWidth(500);
        column3.setResizable(true);
        column3.setMoveable(false);
        viewerColumn3.setLabelProvider(new StyledCellLabelProvider() {
        
            @Override
            public void update(ViewerCell cell) {
                final Element e = (Element) cell.getElement();
                cell.setImage(null);
                final String text = makeText(e);
                cell.setText(text);
                cell.setStyleRanges(StandardModelStyleProvider.getStyleRanges(e, text));
            }
        
            private String makeText(MObject obj) {
                final StringBuffer text = new StringBuffer();
                MObject parent = obj.getCompositionOwner();
                while (parent != null) {
                    text.insert(0, '.');
                    text.insert(0, parent.getName());
                    parent = parent.getCompositionOwner();
                }
                return text.toString();
            }
        
        });
        
        this.resultsViewer.setContentProvider(new ResultsViewerContentProvider(this.results));
        // this.resultsViewer.setLabelProvider(new
        // ElementDecoratedStyledLabelProvider(new ResultsViewerLabelProvider(),
        // true,
        // true));
        
        this.resultsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        
                if (!selection.isEmpty()) {
                    ArrayList<MObject> selected = new ArrayList<>();
                    for (Object o : selection.toList()) {
                        if (o instanceof Note) {
                            selected.add(((Note) o).getSubject());
                        } else if (o instanceof MObject) {
                            selected.add((MObject) o);
                        }
                    }
                    ResultsPanel.this.navigationService.fireNavigate(selected);
                }
            }
        });
        
        this.resultsViewer.setComparator(new ResultsViewerComparator());
        this.resultsViewer.setInput(null);
    }

    @objid ("e2828b9c-e203-4a0f-b58d-641055580bd3")
    public static class ResultsViewerContentProvider implements IStructuredContentProvider {
        @objid ("7f201c7b-f557-4020-8921-6c58d91d83ed")
        private List<Element> results = null;

        @objid ("57d306ee-9cf5-4a83-b129-880c2a47fd62")
        @Override
        public Object[] getElements(Object arg0) {
            return this.results.toArray();
        }

        @objid ("47bc9640-b9ed-435b-9f1d-9ffbbe54c2db")
        @Override
        public void dispose() {
        }

        @objid ("76a63686-5db6-447d-a9f2-2ae2366d827b")
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.results = (List<Element>) newInput;
        }

        @objid ("eb39915e-5668-48bd-9647-704e31a3966b")
        public ResultsViewerContentProvider(List<Element> results) {
            this.results = results;
        }

    }

    @objid ("92e8d1bd-330b-4d45-bf3b-e90588911bfd")
    public static class ResultsViewerComparator extends ViewerComparator {
        @objid ("89865c32-07e5-43e5-af43-ac4a9bab2f46")
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            final MObject o1 = (MObject) e1;
            final MObject o2 = (MObject) e2;
            
            int rc = compareMetaclasses(o1, o2);
            if (rc != 0) {
                return rc;
            }
            
            rc = compareModifiable(o1, o2);
            if (rc != 0) {
                return rc;
            }
            
            rc = compareRamc(o1, o2);
            if (rc != 0) {
                return rc;
            }
            return o1.getName().compareTo(o2.getName());
        }

        @objid ("a614211b-ee04-4f80-aad2-450508bc417b")
        private int compareMetaclasses(MObject o1, MObject o2) {
            return o1.getMClass().getName().compareTo(o2.getMClass().getName());
        }

        @objid ("1a83a7fd-801e-47e9-9465-d6683727fb73")
        private int compareModifiable(MObject o1, MObject o2) {
            return (o2.isModifiable() ? 1 : 0) - (o1.isModifiable() ? 1 : 0);
        }

        @objid ("a3259d7e-d5ff-4e90-b0ff-cf9b8e90b5ee")
        private int compareRamc(MObject o1, MObject o2) {
            return (o1.getStatus().isRamc() ? 1 : 0) - (o2.getStatus().isRamc() ? 1 : 0);
        }

    }

    @objid ("8f344be5-bac1-48a2-ae4d-33bd498e5dcc")
    public static class ResultsViewerLabelProvider extends AbstractModelioElementLabelProvider {
        @objid ("ad1e411a-54b2-421a-b218-ad8d0e2e5ed7")
        @Override
        public Image getImage(Object obj) {
            final Element element = (Element) obj;
            return ElementImageService.getIcon(element);
        }

        @objid ("dd214ec5-53f2-41d5-9b75-b19c20e2da7d")
        @Override
        public String getText(Object obj) {
            String text = new String();
            if (obj instanceof NameSpace) {
                final NameSpace ns = (NameSpace) obj;
                String parentStr = new String();
                ModelTree parent = ns.getOwner();
                while (parent != null) {
                    parentStr = parent.getName() + "." + parentStr; //$NON-NLS-1$
                    parent = parent.getOwner();
                }
                text = parentStr + ns.getName();
            } else {
                text = obj.toString();
            }
            return text;
        }

        @objid ("f3b016e4-b59e-4c3a-a1a8-2effde927343")
        @Override
        public StyledString getStyledText(Object element) {
            return new StyledString(getText(element));
        }

    }

}

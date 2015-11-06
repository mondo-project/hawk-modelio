/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */  
                                    

package org.modelio.app.model.imp.impl.ui;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.GoalContainer;
import org.modelio.metamodel.analyst.PropertyContainer;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

/**
 * Content provider for the tree viewer.
 */
@objid ("14166fc9-202f-4d98-9826-f6c29837527c")
class ImportModelContentProvider implements ITreeContentProvider {
    @objid ("19f69e63-b8ab-4a7e-9966-cc34fb85def7")
    @Override
    public void dispose() {
        // Nothing to dispose
    }

    @objid ("ce1a62e5-c7ec-472c-b292-6fb5dc30ce2d")
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IProjectFragment) {
            IProjectFragment fragment = (IProjectFragment) parentElement;
            return fragment.getRoots().toArray();
        } if (parentElement instanceof MObject) {
            ImportModelContentVisitor contentVisitor = new ImportModelContentVisitor();
            List<MObject> childrenElements = contentVisitor.getChildren((MObject) parentElement);
        
            List<MObject> displayedChildren = new ArrayList<>();
        
            for (MObject child : childrenElements) {
                MStatus childStatus = child.getStatus();
                if (!childStatus.isRamc()) {
                    displayedChildren.add(child);
                }
            }
        
            return childrenElements.toArray();
        } else {
            return new Object[0];
        }
    }

    @objid ("2db88265-97cb-4699-a664-581edade4cca")
    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof GProject) {
            GProject project = (GProject) inputElement;
        
            return getFragments(project).toArray();                
        }
        
        // Nothing to return yet
        return new Object[0];
    }

    @objid ("b5d8d2fc-5a73-401b-822c-cf71c639c8ee")
    private List<IProjectFragment> getFragments(GProject project) {
        List<IProjectFragment> fragments = new ArrayList<>();
        for (IProjectFragment iProjectFragment : project.getFragments()) {
            // Ignore MDA and RAMC fragments
            if (iProjectFragment.getType() == FragmentType.MDA || iProjectFragment.getType() == FragmentType.RAMC) {
                continue;
            } else {
                fragments.add(iProjectFragment);
            }
        }
        return fragments;
    }

    @objid ("4abfca32-8c60-4ed1-aeab-24ed48f642c9")
    @Override
    public Object getParent(Object element) {
        return null;
    }

    @objid ("5c270099-b2d7-4e45-b2b4-8512a9fc7139")
    @Override
    public boolean hasChildren(Object parent) {
        // TODO: optimize?
        return getChildren(parent).length > 0;
    }

    @objid ("78255ed3-4857-491f-87c8-03636bb32b29")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // Nothing to change
    }

    /**
     * This class computes the list of elements to import when importing a given element. It is based on a mmvisitor.
     */
    @objid ("773b782e-8004-4471-a7c8-b9ba0a682287")
    private static class ImportModelContentVisitor extends DefaultModelVisitor {
        @objid ("6bae9982-02cd-4bb2-8ab7-40660ec31771")
         List<MObject> result;

        /**
         * Constructor.
         */
        @objid ("9065cd66-1263-4ab8-b971-a7e83fe6e4b6")
        public ImportModelContentVisitor() {
            super();
            this.result = null;
        }

        /**
         * Get the child elements for the given one.
         * @param parent a model elements
         * @return child elements to display
         */
        @objid ("af3c406d-a029-4750-9d66-5443190e5123")
        public List<MObject> getChildren(MObject parent) {
            this.result = new ArrayList<>();
            parent.accept(this);
            return this.result;
        }

        @objid ("c72babf0-1997-42f9-bd02-a8e12100ac57")
        @Override
        public Object visitProject(Project project) {
            this.result.add(project.getModel());
            this.result.add(project.getDiagramRoot() );
            return super.visitProject(project);
        }

        @objid ("575cda53-f72b-4bf5-bda2-07eabdbd2624")
        @Override
        public Object visitAnalystProject(AnalystProject analystProject) {
            this.result.addAll(analystProject.getRequirementRoot());
            this.result.addAll(analystProject.getGoalRoot());
            this.result.addAll(analystProject.getBusinessRuleRoot());
            this.result.addAll(analystProject.getDictionaryRoot());
            this.result.add(analystProject.getPropertyRoot());
            return super.visitAnalystProject(analystProject);
        }

        @objid ("037740ab-d4aa-44e2-a634-a8603ba7bc74")
        @Override
        public Object visitDiagramSet(final DiagramSet theDiagramSet) {
            this.result.addAll(theDiagramSet.getSub());
            return super.visitDiagramSet(theDiagramSet);
        }

        @objid ("fbc715bf-000d-4b99-acfa-be606e194bf8")
        @Override
        public Object visitDictionary(Dictionary theDictionary) {
            this.result.addAll(theDictionary.getOwnedDictionary());
            //            this.result.addAll(theDictionary.getOwnedTerm());
            return super.visitDictionary(theDictionary);
        }

        @objid ("fe260119-697b-485c-8da0-69adecd0736b")
        @Override
        public Object visitModelTree(ModelTree theModelTree) {
            // Template parameter
            this.result.addAll(theModelTree.getOwnedElement(TemplateParameter.class));
            
            // Package
            this.result.addAll(theModelTree.getOwnedElement(Package.class));
            
            // Interface
            this.result.addAll(theModelTree.getOwnedElement(Interface.class));
            
            // Class
            List<Class> elements = theModelTree.getOwnedElement(Class.class);
            for (ModelTree obModelTree : elements) {
                if (!(obModelTree instanceof Component)) {
                    this.result.add(obModelTree);
                }
            }
            
            // Actor
            this.result.addAll(theModelTree.getOwnedElement(Actor.class));
            
            // UseCase
            this.result.addAll(theModelTree.getOwnedElement(UseCase.class));
            
            // Signal
            this.result.addAll(theModelTree.getOwnedElement(Signal.class));
            
            // Node
            this.result.addAll(theModelTree.getOwnedElement(Node.class));
            
            // Component
            this.result.addAll(theModelTree.getOwnedElement(Component.class));
            
            // Artifact
            this.result.addAll(theModelTree.getOwnedElement(Artifact.class));
            
            // Collaboration
            this.result.addAll(theModelTree.getOwnedElement(Collaboration.class));
            
            // DataType
            this.result.addAll(theModelTree.getOwnedElement(DataType.class));
            
            // Enumeration
            this.result.addAll(theModelTree.getOwnedElement(Enumeration.class));
            
            // Enumeration
            this.result.addAll(theModelTree.getOwnedElement(InformationItem.class));
            return super.visitModelTree(theModelTree);
        }

        @objid ("c3f7d894-7791-42b9-87df-7decae4708ae")
        @Override
        public Object visitRequirementContainer(RequirementContainer theRequirementContainer) {
            this.result.addAll(theRequirementContainer.getOwnedContainer());
            //            this.result.addAll(theRequirementContainer.getOwnedRequirement());
            return super.visitRequirementContainer(theRequirementContainer);
        }

        @objid ("ff294a0c-5454-4c4c-91fe-98b8487f25aa")
        @Override
        public Object visitGoalContainer(GoalContainer theGoalContainer) {
            this.result.addAll(theGoalContainer.getOwnedContainer());
            //            this.result.addAll(theGoalContainer.getOwnedGoal());
            return super.visitGoalContainer(theGoalContainer);
        }

        @objid ("2943cc35-0090-48ae-941c-7d995dfc35fa")
        @Override
        public Object visitBusinessRuleContainer(BusinessRuleContainer theBusinessRuleContainer) {
            this.result.addAll(theBusinessRuleContainer.getOwnedContainer());
            //            this.result.addAll(theBusinessRuleContainer.getOwnedRule());
            return super.visitBusinessRuleContainer(theBusinessRuleContainer);
        }

        @objid ("79009501-11c9-4835-9837-0431973a9d72")
        @Override
        public Object visitModuleComponent(ModuleComponent theModuleComponent) {
            // Profile
            this.result.addAll(theModuleComponent.getOwnedProfile());
            return super.visitModuleComponent(theModuleComponent);
        }

        @objid ("fdb39878-752f-45ae-b6f9-1626d99e6344")
        @Override
        public Object visitPropertyContainer(PropertyContainer thePropertyContainer) {
            // PropertyTableDefinition
            this.result.addAll(thePropertyContainer.getDefinedTable());
            
            // PropertyType
            this.result.addAll(thePropertyContainer.getDefinedType());
            return super.visitPropertyContainer(thePropertyContainer);
        }

    }

}

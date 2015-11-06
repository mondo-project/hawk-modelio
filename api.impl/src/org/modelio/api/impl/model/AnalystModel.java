package org.modelio.api.impl.model;

import java.util.Collection;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.api.model.IAnalystModel;
import org.modelio.api.model.IUmlModel;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.AnalystPropertyTable;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.GoalContainer;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * This class represents the root model of requirement classes.
 * <p>
 * <p>
 * Add the requirement containers and dictionaries can be accessed using this class.<br>
 * To get the current requirement model, please do as following:
 * <p>
 * <p>
 * <code>
 * IModelingSession session = Modelio.getInstance().getModelingSession();<br>
 * RequirementModel = session.getRequirementModel();
 * </code>
 * <p>
 * <p>
 * This requirement model is accessible only if the Requirement MDAC is deployed in the project.
 * 
 * @see IUmlModel
 */
@objid ("dd245cb7-bece-4d88-8181-f052596b20ee")
public class AnalystModel implements IAnalystModel {
    @objid ("ebd2fff0-1fb0-43ce-87dc-ef9bfe695dfa")
    private IModelFactory coreFactory;

    /**
     * Gives access to the project.
     * <p>
     * Note: The IProject must not be stored directly because it may be deleted and replaced by another when connecting
     * to a Subversion repository.
     */
    @objid ("82c2321b-8833-4d34-b2b7-4d5e21673184")
    private IModel model;

    /**
     * Instantiate the requirement model.
     * @param coreFactory a model element creation factory.
     * @param imodel accessor to the model
     */
    @objid ("a97a5e03-84a4-49d9-8cbc-c99bebba10b7")
    public AnalystModel(IModelFactory coreFactory, IModel imodel) {
        this.coreFactory = coreFactory;
        this.model = imodel;
    }

    /**
     * Create a new dictionary without container.<br>
     * The returned dictionary has to be added in the dictionary hierarchy. The following example shows a creation of a
     * dictionary element directly in the root dictionary.
     * <p>
     * <p>
     * <code>
     * IModelingSession session = Modelio.getInstance().getModelingSession();<br>
     * IAnalystModel model = session.getRequirementModel();<br>
     * Dictionary root = model.getRootDictionary();<br><br>
     * 
     * Dictionary newDictionary = model.createDictionary();<br>
     * newDictionary.setName ("dic1");<br>
     * root.addOwned (newDictionary);
     * </code>
     * @return The created dictionary
     */
    @objid ("474ebe44-7fdf-4949-aa57-583d448d448e")
    @Override
    public Dictionary createDictionary() {
        return this.coreFactory.createDictionary();
    }

    /**
     * Create a new enumerated property type.<br>
     * The returned property type has to be added in the admin requirement model, using the
     * {@link #addPropertyType(PropertyType)} method.
     * @see #addPropertyType(PropertyType)
     * @return The created enumerated property type
     */
    @objid ("d6117518-b6a1-438f-ae3d-07f7f5c408f4")
    @Override
    public EnumeratedPropertyType createEnumeratedPropertyType() {
        return this.coreFactory.createEnumeratedPropertyType();
    }

    /**
     * Create a new property.<br>
     * The returned property has to be added to a property set, defined in the admin requirement model.
     * @return The created enumerated property type
     */
    @objid ("722b6f5d-35d2-4e7f-a785-ba1ca2dea222")
    @Override
    public PropertyDefinition createProperty() {
        return this.coreFactory.createPropertyDefinition();
    }

    /**
     * Create a new enumeration litteral for the enumerated property type.<br>
     * The returned enumeration litteral has to be added to a enumerated property type.
     * @see EnumeratedPropertyType#getLitteral()
     * @return The created enumeration litteral
     */
    @objid ("f2d0c375-a579-4ecf-bbbf-7a146889860e")
    @Override
    public PropertyEnumerationLitteral createPropertyEnumerationLitteral() {
        return this.coreFactory.createPropertyEnumerationLitteral();
    }

    /**
     * Create a new property set.<br>
     * The returned property set has to be added to the admin requirement model
     * @return The created property set
     */
    @objid ("3acb1fba-b8d5-4956-a14b-a6621ee6b0e5")
    @Override
    public PropertyTableDefinition createPropertySet() {
        return this.coreFactory.createPropertyTableDefinition();
    }

    /**
     * Create a new simple property type.<br>
     * The returned property type has to be added to the admin requirement model, using the
     * {@link #addPropertyType(PropertyType)} method.
     * @see #addPropertyType(PropertyType)
     * @return The created property type
     */
    @objid ("0afb7e6c-2618-4237-b285-2e83dd648a39")
    @Override
    public PropertyType createPropertyType() {
        return this.coreFactory.createPropertyType();
    }

    /**
     * Create a new property value set.<br>
     * The returned property value set has to be defined on the following elements: IRequirementContainer, IRequirement,
     * IDictionary, ITerm.
     * @see RequirementContainer#setDefaultSet(PropertyValueSet)
     * @see Requirement#setProperties(PropertyValueSet)
     * @see Dictionary#setDefaultSet(PropertyValueSet)
     * @see Term#setProperties(PropertyValueSet)
     * @return The created property value set
     */
    @objid ("67812d03-9e11-4e8c-8b25-5af210e087ca")
    @Override
    public AnalystPropertyTable createPropertyTable() {
        return this.coreFactory.createAnalystPropertyTable();
    }

    /**
     * Create a new requirement without container.<br>
     * The returned requirement has to be added in the requirement container hierarchy. The following example shows the
     * creation of a requirement directly in the root requirement container.
     * <p>
     * <p>
     * <code>
     * IModelingSession session = Modelio.getInstance().getModelingSession();<br>
     * IAnalystModel model = session.getRequirementModel();<br>
     * RequirementContainer root = model.getRootContainer();<br><br>
     * 
     * Requirement newRequirement = model.createRequirement();<br>
     * newRequirement.setName ("req1");<br>
     * newRequirement.setText ("All the methods have to be documented");<br>
     * root.addOwned (newRequirement);<br>
     * </code>
     * @return The created requirement
     */
    @objid ("2fab599b-08df-42c8-910c-c8e8a34c57d7")
    @Override
    public Requirement createRequirement() {
        return this.coreFactory.createRequirement();
    }

    /**
     * Create a new requirement container without container.<br>
     * The returned requirement container has to be added in the requirement container hierarchy. The following example
     * shows the creation of a requirement container directly in the root requirement container.
     * <p>
     * <p>
     * <code>
     * IModelingSession session = Modelio.getInstance().getModelingSession();<br>
     * IAnalystModel model = session.getRequirementModel();<br>
     * RequirementContainer root = model.getRootContainer();<br><br>
     * 
     * RequirementContainer newRequirementContainer = model.createRequirement();<br>
     * newRequirementContainer.setName ("functional requirements");<br>
     * root.addOwned (newRequirementContainer);<br>
     * </code>
     * @return The created requirement container
     */
    @objid ("946a5aec-d1a9-40e3-a0c1-c3a331e4220e")
    @Override
    public RequirementContainer createRequirementContainer() {
        return this.coreFactory.createRequirementContainer();
    }

    /**
     * Create a new term without container.<br>
     * The returned term has to be added in the dictionary hierarchy. The following example shows the creation of a term
     * directly in the root dictionary.
     * <p>
     * <p>
     * <code>
     * IModelingSession session = Modelio.getInstance().getModelingSession();<br>
     * IAnalystModel model = session.getRequirementModel();<br>
     * Dictionary root = model.getRootDictionary();<br><br>
     * 
     * Term newTerm = model.createRequirement();<br>
     * newTerm.setName ("system");<br>
     * newTerm.setDefinition ("The system is the place where the program should run");<br>
     * root.addOwned (newTerm);<br>
     * </code>
     * @return The created requirement
     */
    @objid ("71aa1d3c-8175-48f7-9d31-ea762c07c7fe")
    @Override
    public Term createTerm() {
        return this.coreFactory.createTerm();
    }

    /**
     * Get the main business rules container that owns the business rules elements.<br>
     * The requirement elements are the business rules containers and business rules.
     * @return The root business rules container
     */
    @objid ("62d5048e-0c59-468e-bacb-ad38a3019601")
    @Override
    public List<BusinessRuleContainer> getRootBusinessRuleContainer() {
        final AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getBusinessRuleRoot();
        } else {
            return null;
        }
    }

    /**
     * Get the main dictionary that owns the sub dictionaries and terms.
     * @return The root dictionary
     */
    @objid ("2eaf79b8-79b0-4a09-a326-2aed7d8bb92f")
    @Override
    public List<Dictionary> getRootDictionary() {
        final AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getDictionaryRoot();
        } else {
            return null;
        }
    }

    /**
     * Get the main goal container that owns the goal elements.<br>
     * The requirement elements are the goal containers and goals.
     * @return The root goal container
     */
    @objid ("4b08ed2f-b477-4169-a43b-30375654b411")
    @Override
    public List<GoalContainer> getRootGoalContainer() {
        final AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getGoalRoot();
        } else {
            return null;
        }
    }

    /**
     * Get the main requirement container that owns the requirement elements.<br>
     * The requirement elements are the requirement containers and requirements.
     * @return The root requirement container
     */
    @objid ("f674fdc2-2569-4852-8815-2283a6e959c9")
    @Override
    public List<RequirementContainer> getRootRequirementContainer() {
        final AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getRequirementRoot();
        } else {
            return null;
        }
    }

    /**
     * Get the list of known property types.
     * <p>
     * <p>
     * Default property types already exist and can be extended by the user. This method permits to return the list of
     * known property types.
     * @return The list of known property types.
     */
    @objid ("8f71e1fe-efdd-48dc-9d25-f4f597da9bd3")
    @Override
    public List<PropertyType> getPropertyTypes() {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getPropertyRoot().getDefinedType();
        } else {
            return null;
        }
    }

    /**
     * Get the list of known property sets.
     * <p>
     * Default property sets already exist and can be extended by the user. This method permits to return the list of
     * known property sets.
     * @return The list of known property sets.
     */
    @objid ("c173870f-714c-4422-91a8-07a0ebe42658")
    @Override
    public EList<PropertyTableDefinition> getPropertySets() {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null) {
            return reqProject.getPropertyRoot().getDefinedTable();
        } else {
            return null;
        }
    }

    /**
     * Add a new property type in the requirement admin model.
     * <p>
     * <p>
     * New property types can be created, using the {@link #createPropertyType()} method and can added
     * to this model through the use of this method.
     * @see #removePropertyType(PropertyType)
     * @param type property type
     */
    @objid ("8adbcf55-aceb-4e37-a51d-1a174f409fa1")
    @Override
    public void addPropertyType(PropertyType type) {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null)
            reqProject.getPropertyRoot().getDefinedType().add(type);
    }

    /**
     * Remove an existing property type from the requirement administration model.
     * @see #addPropertyType(PropertyType)
     * @param type property type
     */
    @objid ("eb68e0b0-9533-463e-8e20-5444a8c88864")
    @Override
    public void removePropertyType(PropertyType type) {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null)
            reqProject.getPropertyRoot().getDefinedType().remove(type);
    }

    /**
     * Add a new property set in the requirement administration model.
     * <p>
     * <p>
     * New property sets can be created, using the {@link #createPropertySet()} method and can added to
     * this model through the use of this method.
     * @param set property set
     */
    @objid ("ade919a2-9acc-4194-a688-64ed8e3bdd52")
    @Override
    public void addPropertySet(PropertyTableDefinition set) {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null)
            reqProject.getPropertyRoot().getDefinedTable().add(set);
    }

    /**
     * Remove an existing property set from the requirement administration model.
     * @param set property set
     */
    @objid ("4b925de0-bccd-424b-b645-6e114d271e1f")
    @Override
    public void removePropertySet(PropertyTableDefinition set) {
        AnalystProject reqProject = this.getAnalystProject();
        if (reqProject != null)
            reqProject.getPropertyRoot().getDefinedTable().remove(set);
    }

    /**
     * Get the project.
     * <p>
     * Note: The IProject must not be stored directly because it may be deleted and replaced by another when connecting
     * to a Subversion repository.
     * @return the project
     */
    @objid ("b3f57eee-8f27-4d8a-a640-1add727ecf37")
    private AnalystProject getAnalystProject() {
        Collection<? extends MObject> findByClass = this.model.findByClass(SmClass.getClass(AnalystProject.class), IModel.ISVALID);
        for (MObject project : findByClass) {
            return (AnalystProject) project;
        }
        return null;
    }

    @objid ("75f5fcc1-d89a-4036-bb17-1304741b5a74")
    @Override
    public Goal createGoal() {
        return this.coreFactory.createGoal();
    }

    @objid ("43a27c32-ba92-4819-be3b-54a8094415e6")
    @Override
    public GoalContainer createGoalContainer() {
        return this.coreFactory.createGoalContainer();
    }

    @objid ("0bf90de6-bd81-4097-a83a-61dc56d06e53")
    @Override
    public BusinessRule createBusinessRule() {
        return this.coreFactory.createBusinessRule();
    }

    @objid ("49092605-215f-4b7c-82d2-8857eacc95c0")
    @Override
    public BusinessRuleContainer createBusinessRuleContainer() {
        return this.coreFactory.createBusinessRuleContainer();
    }

    @objid ("14103bb1-09e5-4350-b81c-d215f756451e")
    @Override
    public GoalContainer createGoalContainer(String name, GoalContainer owner) {
        return this.coreFactory.createGoalContainer(name, owner);
    }

    @objid ("2d3d7172-f6bc-4773-8860-52794ca7f81c")
    @Override
    public BusinessRuleContainer createBusinessRuleContainer(String name, BusinessRuleContainer owner) {
        return this.coreFactory.createBusinessRuleContainer(name, owner);
    }

    @objid ("e9fd9ae2-7203-479b-83ef-ad9a60596c48")
    @Override
    public Term createTerm(String name, Dictionary owner) {
        return this.coreFactory.createTerm(name, owner);
    }

    @objid ("76c3718b-d7af-4b62-b388-5f991b4b2bbb")
    @Override
    public Goal createGoal(String name, GoalContainer owner) {
        return this.coreFactory.createGoal(name, owner);
    }

    @objid ("19dceab6-cbb4-4a68-8d0e-390f9386f409")
    @Override
    public Goal createGoal(String name, Goal owner) {
        return this.coreFactory.createGoal(name, owner);
    }

    @objid ("f08194e5-bc0b-42ff-838e-730d7c8c461c")
    @Override
    public Dictionary createDictionary(String name, Dictionary owner) {
        return this.coreFactory.createDictionary(name, owner);
    }

    @objid ("d1acd6cf-f322-4773-a200-86822aee34a4")
    @Override
    public BusinessRule createBusinessRule(String name, BusinessRule owner) {
        return this.coreFactory.createBusinessRule(name, owner);
    }

    @objid ("d025a527-d750-4e1f-9b2b-303cf24d743e")
    @Override
    public BusinessRule createBusinessRule(String name, BusinessRuleContainer owner) {
        return this.coreFactory.createBusinessRule(name, owner);
    }

    @objid ("ac8ecfab-0cd9-4ed5-af05-256c747c52d4")
    @Override
    public RequirementContainer createRequirementContainer(String name, RequirementContainer owner) {
        return this.coreFactory.createRequirementContainer(name, owner);
    }

    @objid ("a4837b24-e13d-4fdc-b276-9c12c2e5163c")
    @Override
    public Requirement createRequirement(String name, RequirementContainer owner) {
        return this.coreFactory.createRequirement(name, owner);
    }

    @objid ("840edc05-c3c9-4ab0-bd99-3e00e69efba7")
    @Override
    public Requirement createRequirement(String name, Requirement owner) {
        return this.coreFactory.createRequirement(name, owner);
    }

}

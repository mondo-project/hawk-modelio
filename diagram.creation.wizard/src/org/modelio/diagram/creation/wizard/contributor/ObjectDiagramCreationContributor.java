package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ObjectDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("fd71ebee-f6e3-4978-909a-87d31a90a620")
public class ObjectDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("388c4d0e-63a6-4786-9784-d841a8c27d3e")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        StaticDiagram diagram = this.createObjectDiagram(modelFactory,
                                                          diagramName,
                                                          diagramContext);
        
        if (diagram != null) {
            if (diagramName.equals(this.getLabel())) {                
                setElementDefaultName(diagram);
            } else {
                diagram.setName(diagramName);
            }
            putNoteContent(diagram, "description", diagramDescription);
        }
        return diagram;
    }

    @objid ("0f4ee93c-f057-407e-9b45-f9b324c20c9b")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(Artifact.class));
        allowedMetaclasses.add(Metamodel.getMClass(BindableInstance.class));
        allowedMetaclasses.add(Metamodel.getMClass(Class.class));
        allowedMetaclasses.add(Metamodel.getMClass(Collaboration.class));
        allowedMetaclasses.add(Metamodel.getMClass(Component.class));
        allowedMetaclasses.add(Metamodel.getMClass(Instance.class));
        allowedMetaclasses.add(Metamodel.getMClass(Node.class));
        allowedMetaclasses.add(Metamodel.getMClass(Package.class));
        return allowedMetaclasses;
    }

    @objid ("3ec7ed9e-7366-4dd8-8aab-582d8f40d1db")
    private StaticDiagram createObjectDiagram(final IModelFactory modelFactory, final String diagramName, final ModelElement diagramContext) {
        // Create the Object diagram
        StaticDiagram diagram;
        //TODO fix creation
        diagram = modelFactory.createObjectDiagram(diagramName, diagramContext, null);
        return diagram;
    }

    @objid ("6928ea38-4883-4c8f-8158-f639b6bc00c8")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Object.Name");
    }

    @objid ("fecb5132-f07c-49ab-9cbd-8a2517782d50")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(ObjectDiagram.class));
    }

    @objid ("1173cf77-8c40-4ace-974e-5e554438a357")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Object.Information");
    }

    @objid ("a8afe739-67fc-4c6b-9205-27c4b9badfad")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Object.Details");
    }

}

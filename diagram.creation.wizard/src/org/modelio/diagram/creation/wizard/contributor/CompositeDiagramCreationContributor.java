package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.CompositeStructureDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.vcore.smkernel.mapi.MClass;

/**
 * Creation contributor for "Composite structure" diagrams.
 */
@objid ("9a4f178d-88a9-4e71-acbd-303e15a0f02b")
public class CompositeDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("51aaaa11-a3f0-4349-bea1-fd15d6e8a557")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        StaticDiagram diagram = this.createCompositeDiagram(modelFactory,
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

    @objid ("2cacdc5b-0dde-47dd-9f87-2971aae6f3c0")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(Collaboration.class));
        allowedMetaclasses.add(Metamodel.getMClass(Component.class));
        allowedMetaclasses.add(Metamodel.getMClass(Class.class));
        allowedMetaclasses.add(Metamodel.getMClass(Node.class));
        return allowedMetaclasses;
    }

    @objid ("a8668476-d993-40a4-ad96-ce4803fd9816")
    private StaticDiagram createCompositeDiagram(final IModelFactory modelFactory, final String diagramName, final ModelElement diagramContext) {
        // Create the Composite diagram       
        StaticDiagram diagram = modelFactory.createCompositeStructureDiagram();
        diagram.setName(diagramName);
        diagram.setOrigin(diagramContext);
        return diagram;
    }

    @objid ("1a7722cf-b89b-4c03-b9b8-5b180f0190c2")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Composite.Name");
    }

    @objid ("a8ad66d6-0a74-41ed-b229-457b648507a2")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(CompositeStructureDiagram.class));
    }

    @objid ("ee34be9c-a973-4ca2-b866-88d8137f5516")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Composite.Information");
    }

    @objid ("21628747-3435-4bc5-baeb-ab2a49797a66")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Composite.Details");
    }

}

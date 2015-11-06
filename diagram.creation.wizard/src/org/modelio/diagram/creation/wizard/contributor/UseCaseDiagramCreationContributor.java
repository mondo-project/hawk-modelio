package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.diagrams.UseCaseDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("ccb1a50b-4d4a-4a7f-8522-653bb74f2664")
public class UseCaseDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("6e62d737-ea71-4d66-9752-6cf19e47995d")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        AbstractDiagram diagram = this.createUseCaseDiagram(modelFactory,
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

    @objid ("88bc2c36-9cd6-41af-a531-dbbb53d37c88")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(Package.class));
        allowedMetaclasses.add(Metamodel.getMClass(Class.class));
        allowedMetaclasses.add(Metamodel.getMClass(Interface.class));
        allowedMetaclasses.add(Metamodel.getMClass(Component.class));
        allowedMetaclasses.add(Metamodel.getMClass(UseCase.class));
        return allowedMetaclasses;
    }

    @objid ("ccf81529-c52e-4a1c-8ff4-3ab675269992")
    private StaticDiagram createUseCaseDiagram(final IModelFactory modelFactory, final String diagramName, final ModelElement diagramContext) {
        // Create the UseCase diagram 
        StaticDiagram diagram;
        diagram = modelFactory.createUseCaseDiagram(diagramName, diagramContext, null);
        return diagram;
    }

    @objid ("98adab97-5467-468a-9edc-dd6ae75b0aab")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.UseCase.Name");
    }

    @objid ("48ce1fa8-a89f-4b9e-ac12-e1dc36558b9e")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(UseCaseDiagram.class));
    }

    @objid ("cf487b66-bfa7-4967-935a-d1122c67e7ad")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.UseCase.Information");
    }

    @objid ("3f99e689-ead0-40e7-a779-89581d9852f8")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.UseCase.Details");
    }

}

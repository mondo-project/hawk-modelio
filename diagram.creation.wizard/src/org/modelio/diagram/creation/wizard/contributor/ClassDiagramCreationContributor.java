package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("39851f12-62b1-432e-ba8a-9047d482fbbd")
public class ClassDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("feff20f2-74f8-4bf1-85c3-afc12bbf0e71")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        ClassDiagram diagram = modelFactory.createClassDiagram(diagramName, diagramContext, null);
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

    @objid ("b2f8ad1a-a2ca-4770-b982-7407c9cd5d07")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(Class.class));
        allowedMetaclasses.add(Metamodel.getMClass(Package.class));
        allowedMetaclasses.add(Metamodel.getMClass(Component.class));
        allowedMetaclasses.add(Metamodel.getMClass(Interface.class));
        return allowedMetaclasses;
    }

    @objid ("416af298-7e52-4174-9db1-73fb872270a4")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Class.Name");
    }

    @objid ("4f423b2f-a1dd-4703-b042-c56c9fbfec73")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(ClassDiagram.class));
    }

    @objid ("fc9be969-5317-4368-8d6d-fdcd9540afe4")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Class.Information");
    }

    @objid ("460a1706-3d68-431e-b5f3-412931c6d926")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Class.Details");
    }

}

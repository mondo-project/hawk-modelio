package org.modelio.diagram.creation.wizard.contributor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

@objid ("6e6bbe5a-f63f-49ae-9d8d-e126a0253778")
public abstract class AbstractDiagramCreationContributor implements IDiagramWizardContributor {
    @objid ("ebd3374a-da80-463c-bca9-f00ced55e010")
    protected IProjectService projectService;

    @objid ("e48559d6-2aa8-4a04-b2c4-1a28eecac5be")
    protected IMModelServices mmServices;

    @objid ("68f19d1c-719b-45d5-a020-59e98957ff37")
    @Override
    public boolean accept(final MObject owner) {
        if (owner != null) {
            for (MClass metaclass : getAcceptedMetaclasses()) {
                if (metaclass.hasBase(owner.getMClass())) {
                    MStatus elementStatus = owner.getStatus();
                    if (owner.getMClass().isCmsNode() && elementStatus.isCmsManaged()) {
                        return !elementStatus.isRamc();
                    } else {
                        return owner.isModifiable();
                    }
                }
            }
        }
        return false;
    }

    @objid ("3076df7e-6b7d-4684-8d83-41787238186b")
    @Override
    public String getHelpUrl() {
        return null;
    }

    @objid ("140be8e7-c8d8-4df7-ae0a-022ebe6f98a4")
    public void setProjectService(IProjectService projectService) {
        this.projectService = projectService;
    }

    @objid ("298e6780-9869-436a-90b3-68820f12eb19")
    public void setModelService(IMModelServices mmServices) {
        this.mmServices = mmServices;
    }

    /**
     * Set the content of the first note with the given type on the &lt;element&gt; ModelElement.
     * @param element
     * @param moduleName
     * @param type
     * @param content
     */
    @objid ("ba335241-50ea-4632-9fa1-0033ff16f84a")
    public static void putNoteContent(ModelElement element, String type, String content) {
        //FIXME
        //element.putNoteContent("ModelerModule", type, content);
    }

    @objid ("ec37c3cb-954d-43dc-996a-f647cffa0e82")
    protected void setElementDefaultName(ModelElement element) {
        element.setName(this.mmServices.getElementNamer().getUniqueName(element));
    }

}

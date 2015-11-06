package org.modelio.api.impl.model;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.IMetamodelExtensions;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.vcore.smkernel.mapi.MClass;

/**
 * This class is used to get the module extensions.<br>
 * It's not possible to add or update such extensions using this class. The only
 * way to create new extensions is to use the <i>MDA Modeler tool</i>.
 * <p>
 * <p>
 * 
 * The known extensions are the following:
 * <ul>
 * <li>Stereotypes ({@link Stereotype})</li>
 * <li>Tag types ({@link TagType})</li>
 * <li>Note types ({@link NoteType})</li>
 * <li>RichNote types ({@link ExternDocumentType})</li>
 * </ul>
 */
@objid ("6409a672-168b-4358-9ee1-08bfbdb44335")
public class MetamodelExtensions implements IMetamodelExtensions {
    @objid ("fc85f2c7-15c6-4757-933b-7c642ce10d7e")
    private IMModelServices modelService;

    /**
     * Default constructor initializing the model service.
     * @param modelService the model service used to find mda extensions.
     */
    @objid ("b9ee8668-665f-4fde-8a1e-81da6117d149")
    public MetamodelExtensions(IMModelServices modelService) {
        this.modelService = modelService;
    }

    @objid ("d1987829-8635-45c9-8a81-9d8d3751fcc8")
    @Override
    public List<Stereotype> findStereotypes(String stereotypeName, MClass metaclass) {
        return this.modelService.findStereotypes(stereotypeName, metaclass);
    }

    @objid ("451a7110-bdb2-48e4-af11-bbae6a27fff9")
    @Override
    public Stereotype getStereotype(String stereotypeName, MClass metaclass) {
        try {
            return this.modelService.getStereotype(stereotypeName, metaclass);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("5eaacbcc-ade1-45b2-adcd-2f9568ba57f1")
    @Override
    public List<Stereotype> findStereotypes(String moduleName, String stereotypeName, MClass metaclass) {
        return this.modelService.findStereotypes(moduleName, stereotypeName, metaclass);
    }

    @objid ("fbf68202-087c-4164-8ff4-d514bb65a248")
    @Override
    public Stereotype getStereotype(String moduleName, String stereotypeName, MClass metaclass) {
        try {
            return this.modelService.getStereotype(moduleName, stereotypeName, metaclass);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("e271cb4a-a7bd-4089-9990-38e0b3430db9")
    @Override
    public List<NoteType> findNoteTypes(String noteTypeName, MClass metaclass) {
        return this.modelService.findNoteTypes(noteTypeName, metaclass);
    }

    @objid ("f5048fb3-ddcb-4a40-959d-6f50e51cf7f2")
    @Override
    public NoteType getNoteType(String moduleName, String noteTypeName, MClass metaclass) {
        try {
            return this.modelService.getNoteType(moduleName, noteTypeName, metaclass);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("65800c35-4002-499d-8b68-33f88e02fa62")
    @Override
    public List<NoteType> findNoteTypes(String moduleName, String noteTypeName, MClass metaclass) {
        return this.modelService.findNoteTypes(moduleName, noteTypeName, metaclass);
    }

    @objid ("b7c789bb-de38-45ea-957a-d3d9a6512dc7")
    @Override
    public NoteType getNoteType(Stereotype stereotype, String noteTypeName) {
        try {
            return this.modelService.getNoteType(stereotype, noteTypeName);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("8a2c709f-e6df-49fb-bddb-99dc9441c53d")
    @Override
    public List<ExternDocumentType> findExternDocumentTypes(String externDocumentTypeName, MClass metaclass) {
        return this.modelService.findExternDocumentTypes(externDocumentTypeName, metaclass);
    }

    @objid ("329ec535-7d2b-4706-8739-a26835cfd48a")
    @Override
    public List<ExternDocumentType> findExternDocumentTypes(String moduleName, String externDocumentTypeName, MClass metaclass) {
        return this.modelService.findExternDocumentTypes(moduleName, externDocumentTypeName, metaclass);
    }

    @objid ("3f5cc3c6-0e56-46f4-9b51-003f99d1ef57")
    @Override
    public ExternDocumentType getExternDocumentType(String moduleName, String externDocumentTypeName, MClass metaclass) {
        try {
            return this.modelService.getExternDocumentType(moduleName, externDocumentTypeName, metaclass);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("c1039865-3d70-444f-b2eb-431d8157dc34")
    @Override
    public ExternDocumentType getExternDocumentType(Stereotype stereotype, String externDocumentTypeName) {
        try {
            return this.modelService.getExternDocumentType(stereotype, externDocumentTypeName) ;
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("ae21accb-96ac-4ff9-956c-19b8689dbbaf")
    @Override
    public List<TagType> findTagTypes(String tagTypeName, MClass metaclass) {
        return this.modelService.findTagTypes(tagTypeName, metaclass) ;
    }

    @objid ("6a1104ae-e110-43a5-bb03-fd15d6ebb193")
    @Override
    public List<TagType> findTagTypes(String moduleName, String tagTypeName, MClass metaclass) {
        return this.modelService.findTagTypes(moduleName, tagTypeName, metaclass);
    }

    @objid ("c4d88778-63cb-4d29-99d4-346045df34b5")
    @Override
    public TagType getTagType(String moduleName, String tagTypeName, MClass metaclass) {
        try {
            return this.modelService.getTagType(moduleName, tagTypeName, metaclass);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("5fe5eb24-f885-4fd1-888d-bb0db43af2d9")
    @Override
    public TagType getTagType(Stereotype stereotype, String tagTypeName) {
        try {
            return this.modelService.getTagType(stereotype, tagTypeName);
        } catch (org.modelio.metamodel.factory.ElementNotUniqueException e) {
            return null;
        }
    }

    @objid ("47acd5b5-ee1b-4605-b122-b34e4e6cd5c5")
    @Override
    public String getLabel(Stereotype stereotype) {
        return ModuleI18NService.getLabel(stereotype);
    }

    @objid ("25b8a355-b042-48e7-9d19-7cb868c8a44a")
    @Override
    public String getLabel(TagType tagType) {
        return ModuleI18NService.getLabel(tagType);
    }

    @objid ("5c2431fd-79fd-4f43-af6b-bc13f0fff1b0")
    @Override
    public String getLabel(NoteType noteType) {
        return ModuleI18NService.getLabel(noteType);
    }

    @objid ("a1cf88d8-89c6-451e-ac3e-a344c973fc59")
    @Override
    public String getLabel(ExternDocumentType docType) {
        return ModuleI18NService.getLabel(docType);
    }

}

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
                                    

package org.modelio.core.ui.nsu;

import java.util.Iterator;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.PackageImport;
import org.modelio.metamodel.uml.statik.PackageMerge;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.RaisedException;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.visitors.DefaultModelVisitor;

@objid ("a1e9417a-a03c-4aae-b8d1-f54172abfdfa")
class CauseLabelProvider extends DefaultModelVisitor {
    @objid ("d36708a6-103a-4796-bbf5-c7352b49e974")
    private static CauseLabelProvider singleton = new CauseLabelProvider();

    @objid ("d46af244-7937-4f4d-bf96-d599c6cb5e89")
    public static String get(final Element el) {
        return (String) el.accept(singleton);
    }

    @objid ("c21fae06-5a81-430e-9a0e-934cf93bd9a2")
    @Override
    public Object visitAssociationEnd(final AssociationEnd aEnd) {
        return (CoreUi.I18N.getMessage("causeanalyzer.association_role", aEnd.getName(), aEnd.getTarget().getName())); //$NON-NLS-1$
    }

    @objid ("ccbb4a8c-bbe1-4cf1-9283-cfff89590de6")
    @Override
    public Object visitAttribute(final Attribute att) {
        return (CoreUi.I18N.getMessage("causeanalyzer.attribute", att.getName(), att.getType().getName())); //$NON-NLS-1$
    }

    @objid ("3c43c63d-cf72-49bc-a275-abaf693493ec")
    @Override
    public Object visitGeneralization(final Generalization g) {
        return CoreUi.I18N.getMessage("causeanalyzer.generalization", g.getSubType().getName(), g.getSuperType().getName()); //$NON-NLS-1$
    }

    @objid ("46f07208-a6e2-4c03-904b-a3ff7e4df3af")
    @Override
    public Object visitNamespaceUse(final NamespaceUse theNSU) {
        NameSpace user = theNSU.getUser();
        NameSpace used = theNSU.getUsed();
        return CoreUi.I18N.getMessage("causeanalyzer.namespaceuse", user.getMClass().getName(), user.getName(),
                used.getMClass().getName(), used.getName()); //$NON-NLS-1$
    }

    @objid ("bf713eeb-1321-43a3-93a8-f3e5f85aaa38")
    @Override
    public Object visitModelElement(final ModelElement me) {
        return (CoreUi.I18N.getMessage("causeanalyzer.unknowncause", me.getName(), me.getMClass().getName())); //$NON-NLS-1$
    }

    @objid ("f8f6461d-b989-4b1a-8e6b-fa6424b57ea9")
    @Override
    public Object visitElementImport(final ElementImport imp) {
        ModelElement src = imp.getImportingNameSpace();
        if (src == null)
            src = imp.getImportingOperation();
        return (CoreUi.I18N.getMessage("causeanalyzer.element_import", src.getName(), imp.getImportedElement().getName())); //$NON-NLS-1$
    }

    @objid ("3426c91e-4d4c-4c6d-9f3e-a4ff03cb50f3")
    @Override
    public Object visitParameter(final Parameter theParameter) {
        Operation op = (Operation) theParameter.getCompositionOwner();
        
        if (theParameter.equals(op.getReturn()))
            return (CoreUi.I18N.getMessage("causeanalyzer.return_parameter", op.getName(), theParameter.getType().getName())); //$NON-NLS-1$
        else
            return (CoreUi.I18N.getMessage("causeanalyzer.io_parameter", op.getName(), theParameter.getName(), theParameter
                    .getType().getName())); //$NON-NLS-1$
    }

    @objid ("a06b0673-c33f-4885-89fe-f052f7e374c7")
    @Override
    public Object visitUsage(final Usage usage) {
        return (CoreUi.I18N.getMessage("causeanalyzer.usage", usage.getImpacted().getName(), usage.getDependsOn().getName())); //$NON-NLS-1$
    }

    @objid ("ab925c53-af68-4708-9505-e7c26a278e15")
    @Override
    public Object visitDependency(final Dependency dep) {
        String msgName;
        if (dep.isStereotyped("ModelerModule", "trace"))
            msgName = "causeanalyzer.dependency.trace";
        else if (dep.isStereotyped("ModelerModule", "flow"))
            msgName = "causeanalyzer.dependency.flow";
        else
            msgName = "causeanalyzer.dependency.none";
        return (CoreUi.I18N.getMessage(msgName, dep.getImpacted().getName(), dep.getDependsOn().getName()));
    }

    @objid ("8c133443-a91d-475b-ac00-18487a9a1464")
    @Override
    public Object visitRaisedException(final RaisedException raised) {
        return (CoreUi.I18N.getMessage("causeanalyzer.raisedexception", raised.getThrower().getName(), raised.getThrownType()
                .getName())); //$NON-NLS-1$
    }

    @objid ("0c0cb3fb-93b7-4270-a2cd-2e67de784d1c")
    @Override
    public Object visitInterfaceRealization(final InterfaceRealization ir) {
        return (CoreUi.I18N.getMessage("causeanalyzer.interfacerealization", ir.getImplementer().getName(), ir.getImplemented()
                .getName())); //$NON-NLS-1$
    }

    @objid ("c8ac1de9-c6a3-4587-904c-85e9d8c130f6")
    @Override
    public Object visitTemplateBinding(final TemplateBinding tb) {
        ModelElement instanciatedTemplate = tb.getInstanciatedTemplate();
        if (instanciatedTemplate == null)
            instanciatedTemplate = tb.getInstanciatedTemplateOperation();
        
        ModelElement boundElement = tb.getBoundElement();
        if (boundElement == null)
            boundElement = tb.getBoundOperation();
        return (CoreUi.I18N.getMessage("causeanalyzer.templatebinding", instanciatedTemplate.getName(), boundElement.getName())); //$NON-NLS-1$
    }

    @objid ("0d190ae6-cee5-4ec6-906d-a66fda6268c9")
    @Override
    public Object visitPackageMerge(final PackageMerge merge) {
        return CoreUi.I18N.getMessage("causeanalyzer.packagemerge", merge.getReceivingPackage().getName(), merge.getMergedPackage()
                .getName()); //$NON-NLS-1$
    }

    @objid ("505633df-db64-4e77-9439-66411d9af1c1")
    @Override
    public Object visitPackageImport(final PackageImport imp) {
        ModelElement src = imp.getImportingNameSpace();
        if (src == null)
            src = imp.getImportingOperation();
        return (CoreUi.I18N.getMessage("causeanalyzer.packageimport", src.getName(), imp.getImportedPackage().getName())); //$NON-NLS-1$
    }

    @objid ("48b437cb-5399-416c-a9d1-ccdd2e5d0a07")
    @Override
    public Object visitUseCaseDependency(final UseCaseDependency theUseCaseDependency) {
        String key;
        if (theUseCaseDependency.isStereotyped("ModelerModule", "include"))
            key = "causeanalyzer.usecasedependency.include"; //$NON-NLS-1$
        else if (theUseCaseDependency.isStereotyped("ModelerModule", "extend"))
            key = "causeanalyzer.usecasedependency.extend"; //$NON-NLS-1$
        else
            return super.visitUseCaseDependency(theUseCaseDependency);
        return (CoreUi.I18N.getMessage(key, theUseCaseDependency.getOrigin().getName(), theUseCaseDependency.getTarget().getName()));
    }

    @objid ("b82dc785-c266-401b-9c0c-1b5345dfe055")
    @Override
    public Object visitBinding(final Binding theBinding) {
        ModelElement dest = theBinding.getRepresentedFeature();
        
        ModelElement src = theBinding.getConnectorEndRole();
        if (src==null) src = theBinding.getConnectorRole();
        if (src==null) src = theBinding.getRole();
        
        theBinding.getOwner();
        return (CoreUi.I18N.getMessage("causeanalyzer.binding", 
                                           theBinding.getOwner().getName(),
                                           src.getName(),
                                           src.getMClass().getName(),
                                           dest.getName())); //$NON-NLS-1$
    }

    @objid ("a1e90e46-cfb4-4d91-90fa-c03948164b2e")
    @Override
    public Object visitManifestation(final Manifestation theManifestation) {
        return (CoreUi.I18N.getMessage("causeanalyzer.manifestation", theManifestation.getOwner().getName(), theManifestation
                .getUtilizedElement().getName())); //$NON-NLS-1$
    }

    @objid ("114b40d0-554f-458d-9171-c4af64cfb7aa")
    @Override
    public Object visitConnectorEnd(final ConnectorEnd theConnector) {
        String msg1 = (CoreUi.I18N.getMessage("causeanalyzer.connectorend", theConnector.getName(),  theConnector.getRepresentedFeature().getName())); //$NON-NLS-1$
        
        ModelElement feature = theConnector.getRepresentedFeature();
        if (feature != null) {
            return msg1 + "\n"
                    + CoreUi.I18N.getMessage("causeanalyzer.connectorend.represents", theConnector.getName(), feature.getName());
        
        } else {
            return msg1;
        }
    }

    @objid ("5667eed4-d5d7-4317-bf0b-aebf91ec5de5")
    @Override
    public Object visitBindableInstance(final BindableInstance theBindableInstance) {
        String msg1 = (CoreUi.I18N.getMessage("causeanalyzer.bindableinstance", theBindableInstance.getName(), theBindableInstance
                .getBase().getName())); //$NON-NLS-1$
        
        ModelElement feature = theBindableInstance.getRepresentedFeature();
        if (feature != null) {
            return msg1
                    + "\n"
                    + CoreUi.I18N.getMessage("causeanalyzer.bindableinstance.represents", theBindableInstance.getName(),
                            feature.getName());
        
        } else {
            return msg1;
        }
    }

    @objid ("b98892bd-4b42-481a-b09f-7a6555255c6a")
    private CauseLabelProvider() {
    }

    @objid ("07c54ae2-e211-4337-bd9f-763a30c05c21")
    @Override
    public Object visitAttributeLink(final AttributeLink theAttributeLink) {
        return (CoreUi.I18N.getMessage("causeanalyzer.attributelink", theAttributeLink.getName(), theAttributeLink.getBase()
                .getName())); //$NON-NLS-1$
    }

    @objid ("1fd5843d-d1c4-4357-b589-5a9f8b3da8dd")
    @Override
    public Object visitInformationFlow(final InformationFlow theInformationFlow) {
        theInformationFlow.getConveyed();
        return CoreUi.I18N.getMessage("causeanalyzer.informationflow", getSymbol(theInformationFlow.getOwner()),
                theInformationFlow.getName(), getSymbol(theInformationFlow.getInformationSource()),
                getSymbol(theInformationFlow.getInformationTarget())); //$NON-NLS-1$
    }

    @objid ("4846c082-65d9-4a74-a9ea-1d3a149e7e08")
    @Override
    public Object visitInformationItem(final InformationItem theInformationItem) {
        return CoreUi.I18N.getMessage("causeanalyzer.informationitem", theInformationItem.getName(),
                getSymbol(theInformationItem.getRepresented())); //$NON-NLS-1$
    }

    @objid ("4bb493da-07f8-43b7-bf7b-fb478a582d11")
    private String getSymbol(final ModelElement el) {
        if (el == null)
            return "<none>";
        return el.getName();
    }

    @objid ("9467cf58-d98c-4291-8829-cc7825ab0bde")
    private String getSymbol(final Iterable<? extends ModelElement> elements) {
        final StringBuilder s = new StringBuilder();
        
        final Iterator<? extends ModelElement> it = elements.iterator();
        while (it.hasNext()) {
            s.append(getSymbol(it.next()));
            if (it.hasNext())
                s.append(", ");
        }
        
        if (s.length() == 0)
            return "<none>";
        return s.toString();
    }

    @objid ("b6367627-368d-4b58-8760-07271a0ec0c0")
    @Override
    public Object visitInstance(final Instance theInstance) {
        return CoreUi.I18N.getMessage("causeanalyzer.instance", theInstance.getName(), theInstance.getBase().getName()); //$NON-NLS-1$
    }

    @objid ("ec2853db-afc2-43f9-92f8-f04126b287b7")
    @Override
    public Object visitLinkEnd(final LinkEnd theConnector) {
        String msg = (CoreUi.I18N.getMessage("causeanalyzer.linkend", theConnector.getName(), 
                                             theConnector.getTarget().getName())); //$NON-NLS-1$
        
        if (theConnector.getModel() != null) {
            String msg1 = (CoreUi.I18N.getMessage("causeanalyzer.linkend.model", theConnector.getSource().getName(),
                    theConnector.getName(), theConnector.getModel().getOwner().getName(), theConnector.getModel().getName(),
                    theConnector.getTarget().getName())); //$NON-NLS-1$
        
            return msg + "\n" + msg1;
        }
        return msg;
    }

    @objid ("ef3d9bb9-56ae-47b5-afa6-4042387608ce")
    @Override
    public Object visitObjectNode(final ObjectNode theObjectNode) {
        ModelElement represented = theObjectNode.getRepresented();
        if (represented == null)
            represented = theObjectNode.getRepresentedAttribute();
        if (represented == null)
            represented = theObjectNode.getRepresentedRealParameter();
        if (represented == null)
            represented = theObjectNode.getRepresentedRole();
        
        String msg1 = null;
        String msg2 = null;
        
        if (theObjectNode.getType() != null)
            msg1 = CoreUi.I18N.getMessage("causeanalyzer.objectnode.type", theObjectNode.getName(), theObjectNode.getType()
                    .getName()); //$NON-NLS-1$
        
        if (represented != null)
            msg2 = CoreUi.I18N.getMessage("causeanalyzer.objectnode.represents", theObjectNode.getName(), represented.getName(),
                    represented.getMClass().getName()); //$NON-NLS-1$
        
        if (msg1 == null)
            return msg2;
        else if (msg2 == null)
            return msg1;
        else
            return msg1 + "\n" + msg2;
    }

    @objid ("2fe1c790-bbda-48e3-834b-1c2042f46864")
    @Override
    public Object visitMessage(final Message theMessage) {
        return (CoreUi.I18N.getMessage("causeanalyzer.message", theMessage.getName(), getSymbol(theMessage.getInvoked()))); //$NON-NLS-1$
    }

}

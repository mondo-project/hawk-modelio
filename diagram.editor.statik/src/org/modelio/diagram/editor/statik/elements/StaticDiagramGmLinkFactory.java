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
                                    

package org.modelio.diagram.editor.statik.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.association.GmAssociation;
import org.modelio.diagram.editor.statik.elements.associationclass.GmClassAssociationLink;
import org.modelio.diagram.editor.statik.elements.bindinglink.GmBindingLink;
import org.modelio.diagram.editor.statik.elements.collabuselink.GmCollabUseLink;
import org.modelio.diagram.editor.statik.elements.componentRealization.GmComponentRealization;
import org.modelio.diagram.editor.statik.elements.connector.GmConnectorLink;
import org.modelio.diagram.editor.statik.elements.elementimport.GmElementImport;
import org.modelio.diagram.editor.statik.elements.generalization.GmGeneralization;
import org.modelio.diagram.editor.statik.elements.informationflowlink.GmInformationFlowLink;
import org.modelio.diagram.editor.statik.elements.instancelink.GmInstanceLink;
import org.modelio.diagram.editor.statik.elements.naryassoc.GmNAssocEndLink;
import org.modelio.diagram.editor.statik.elements.naryconnector.GmNConnectorEndLink;
import org.modelio.diagram.editor.statik.elements.narylink.GmNLinkEndLink;
import org.modelio.diagram.editor.statik.elements.packageimport.GmPackageImport;
import org.modelio.diagram.editor.statik.elements.packagemerge.GmPackageMerge;
import org.modelio.diagram.editor.statik.elements.providedinterface.GmProvidedInterfaceLink;
import org.modelio.diagram.editor.statik.elements.raisedexception.GmRaisedException;
import org.modelio.diagram.editor.statik.elements.realization.GmInterfaceRealization;
import org.modelio.diagram.editor.statik.elements.requiredinterface.GmRequiredInterfaceLink;
import org.modelio.diagram.editor.statik.elements.substitution.GmSubstitution;
import org.modelio.diagram.editor.statik.elements.templatebinding.GmTemplateBinding;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.ComponentRealization;
import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.NaryConnectorEnd;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.metamodel.uml.statik.PackageImport;
import org.modelio.metamodel.uml.statik.PackageMerge;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.metamodel.uml.statik.RaisedException;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("36b91937-55b7-11e2-877f-002564c97630")
public class StaticDiagramGmLinkFactory implements IGmLinkFactory {
    @objid ("36b9193b-55b7-11e2-877f-002564c97630")
    private static final StaticDiagramGmLinkFactory _instance = new StaticDiagramGmLinkFactory();

    /**
     * @return the singleton instance of the link factory for State diagram.
     */
    @objid ("36b9193d-55b7-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    /**
     * Creates the link factory.
     */
    @objid ("36b91944-55b7-11e2-877f-002564c97630")
    private StaticDiagramGmLinkFactory() {
    }

    @objid ("36b91947-55b7-11e2-877f-002564c97630")
    @Override
    public GmLink create(GmAbstractDiagram diagram, MObject linkElement) {
        return (GmLink) linkElement.accept(new ImplVisitor(diagram));
    }

    /**
     * Register an GmLink factory extension.
     * <p>
     * Extension GmLink factories are called first when looking for an GmLink.
     * @param id id for the GmLink extension factory
     * @param factory the edit part factory.
     */
    @objid ("36b91954-55b7-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("36b9195c-55b7-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("36ba9fb9-55b7-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("a7c8ba69-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("36ba9fc0-55b7-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("36ba9fdb-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCollaborationUse(final CollaborationUse theUse) {
            return new GmCollabUseLink(this.diagram, theUse, new MRef(theUse));
        }

        @objid ("36ba9fe4-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("36ba9fec-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitElementImport(ElementImport theElementImport) {
            return new GmElementImport(this.diagram, theElementImport, new MRef(theElementImport));
        }

        @objid ("36ba9ff4-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitGeneralization(Generalization theGeneralization) {
            return new GmGeneralization(this.diagram, theGeneralization, new MRef(theGeneralization));
        }

        @objid ("36bc2661-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitPackageImport(final PackageImport thePackageImport) {
            return new GmPackageImport(this.diagram, thePackageImport, new MRef(thePackageImport));
        }

        @objid ("36bc266a-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitPackageMerge(final PackageMerge thePackageMerge) {
            return new GmPackageMerge(this.diagram, thePackageMerge, new MRef(thePackageMerge));
        }

        @objid ("36bc2673-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitRaisedException(final RaisedException theRaisedException) {
            return new GmRaisedException(this.diagram, theRaisedException, new MRef(theRaisedException));
        }

        @objid ("36bc267c-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInformationFlow(final InformationFlow theFlow) {
            if (theFlow.getInformationSource().size() == 1 && theFlow.getInformationTarget().size() == 1) {
                return new GmInformationFlowLink(this.diagram, theFlow, new MRef(theFlow));
            }
            return super.visitInformationFlow(theFlow);
        }

        @objid ("36bc2685-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitProvidedInterface(final ProvidedInterface theProvidedInterface) {
            return new GmProvidedInterfaceLink(this.diagram,
                    theProvidedInterface,
                    new MRef(theProvidedInterface));
        }

        @objid ("36bc268e-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitRequiredInterface(final RequiredInterface theRequiredInterface) {
            return new GmRequiredInterfaceLink(this.diagram,
                    theRequiredInterface,
                    new MRef(theRequiredInterface));
        }

        @objid ("36bc2697-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInterfaceRealization(final InterfaceRealization theInterfaceRealization) {
            return new GmInterfaceRealization(this.diagram,
                    theInterfaceRealization,
                    new MRef(theInterfaceRealization));
        }

        @objid ("36bdacfe-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitTemplateBinding(final TemplateBinding theTemplateBinding) {
            return new GmTemplateBinding(this.diagram, theTemplateBinding, new MRef(theTemplateBinding));
        }

        @objid ("36bdad07-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitClassAssociation(final ClassAssociation theAssoc) {
            return new GmClassAssociationLink(this.diagram, theAssoc, new MRef(theAssoc));
        }

        @objid ("828f5907-df5e-41e6-b189-50016b10fbfe")
        @Override
        public Object visitAssociationEnd(AssociationEnd role) {
            return new GmAssociation(this.diagram,
                    role,
                    new MRef(role),
                    new MRef(role.getAssociation()));
        }

        @objid ("75ba39e1-96c6-4eae-8501-2ccd74fe6f84")
        @Override
        public Object visitNaryAssociationEnd(NaryAssociationEnd role) {
            return new GmNAssocEndLink(this.diagram, role, new MRef(role));
        }

        @objid ("67587c9e-5676-4d39-87b4-6e8a71b6b382")
        @Override
        public Object visitBinding(Binding theBinding) {
            //TODO dfd
            return new GmBindingLink(this.diagram, theBinding, new MRef(theBinding));
        }

        @objid ("78977fe7-b15c-4423-bfbb-d970d2710161")
        @Override
        public Object visitLinkEnd(LinkEnd role) {
            return new GmInstanceLink(this.diagram,
                    role,
                    new MRef(role),
                    new MRef(role.getLink()));
        }

        @objid ("42f8487f-c36f-4bd4-b0c2-f88be0b7ee12")
        @Override
        public Object visitNaryLinkEnd(NaryLinkEnd role) {
            return new GmNLinkEndLink(this.diagram, role, new MRef(role));
        }

        @objid ("aa05fcba-1a31-4803-8cf0-c85d69e74927")
        @Override
        public Object visitConnectorEnd(final ConnectorEnd role) {
            // If lollipop Connector is not a link
            if (role.getConsumer() != null || role.getProvider() != null || role.getOpposite().getConsumer() != null || role.getOpposite().getProvider() != null)
                return null;
            return new GmConnectorLink(this.diagram,
                    role,
                    new MRef(role),
                    new MRef(role.getLink()));
        }

        @objid ("f4e730b6-3faf-41da-b954-cc37980d2059")
        @Override
        public Object visitNaryConnectorEnd(final NaryConnectorEnd role) {
            return new GmNConnectorEndLink(this.diagram, role, new MRef(role));
        }

        @objid ("1349b248-97d8-43a9-bd0f-92c50cf59c74")
        @Override
        public Object visitAssociation(Association association) {
            return new GmAssociation(this.diagram,
                    association.getEnd().get(0),
                    new MRef(association.getEnd().get(0)),
                    new MRef(association));
        }

        @objid ("ed32b086-fb63-4501-831a-14e836312cb1")
        @Override
        public Object visitLink(Link link) {
            return new GmInstanceLink(this.diagram,
                    link.getLinkEnd().get(0),
                    new MRef(link.getLinkEnd().get(0)),
                    new MRef(link));
        }

        @objid ("4a647ddd-07e2-4478-81f0-e6f206191d0e")
        @Override
        public Object visitConnector(final Connector connector) {
            // If lollipop Connector is not a link
            if (connector.getLinkEnd().get(0).getConsumer() != null ||connector.getLinkEnd().get(0).getProvider() != null || connector.getLinkEnd().get(0).getOpposite().getConsumer() != null || connector.getLinkEnd().get(0).getOpposite().getProvider() != null)
                return null;
            return new GmConnectorLink(this.diagram,
                    (ConnectorEnd) connector.getLinkEnd().get(0),
                    new MRef(connector.getLinkEnd().get(0)),
                    new MRef(connector));
        }

        @objid ("2c47fdb8-ab78-4103-bc2c-0d21a42b9819")
        @Override
        public Object visitSubstitution(Substitution obj) {
            return new GmSubstitution(this.diagram, obj, new MRef(obj));
        }

        @objid ("f43c0819-ff4c-4900-8ff7-27a550294ca1")
        @Override
        public Object visitComponentRealization(ComponentRealization obj) {
            return new GmComponentRealization(this.diagram, obj, new MRef(obj));
        }

    }

}

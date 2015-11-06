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
                                    

package org.modelio.diagram.editor.bpmn.elements.gmfactory;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.bpmndataassociation.GmBpmnDataAssociation;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessageLink;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow.GmBpmnMessageFlow;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.GmBpmnSequenceFlow;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("620b9a3a-55b6-11e2-877f-002564c97630")
public class BpmnGmLinkFactory implements IGmLinkFactory {
    @objid ("620b9a3e-55b6-11e2-877f-002564c97630")
    private static final BpmnGmLinkFactory _instance = new BpmnGmLinkFactory();

    /**
     * Creates the link factory for a diagram.
     */
    @objid ("620b9a40-55b6-11e2-877f-002564c97630")
    public BpmnGmLinkFactory() {
    }

    @objid ("620b9a43-55b6-11e2-877f-002564c97630")
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
    @objid ("620b9a50-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("620b9a57-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the singleton instance of the link factory for State diagram.
     */
    @objid ("620b9a5b-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("620d20da-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("728c4cab-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("620d20e1-55b6-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("620d20e6-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("620d20ee-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnSequenceFlow(BpmnSequenceFlow element) {
            final GmBpmnSequenceFlow gm_object = new GmBpmnSequenceFlow(this.diagram,
                                                                        element,
                                                                        new MRef(element));
            return gm_object;
        }

        @objid ("620d20f6-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnMessageFlow(BpmnMessageFlow element) {
            final GmBpmnMessageFlow gm_object = new GmBpmnMessageFlow(this.diagram,
                                                                      element,
                                                                      new MRef(element));
            return gm_object;
        }

        @objid ("620d20fe-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnDataAssociation(BpmnDataAssociation element) {
            final GmBpmnDataAssociation gm_object = new GmBpmnDataAssociation(this.diagram,
                                                                              element,
                                                                              new MRef(element));
            return gm_object;
        }

        @objid ("620d2106-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnMessage(final BpmnMessage theNote) throws ClassCastException {
            final GmBpmnMessageLink link = new GmBpmnMessageLink(this.diagram, new MRef(theNote));
            return link;
        }

    }

}

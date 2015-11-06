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
                                    

package org.modelio.diagram.elements.umlcommon.informationflowgroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.metamodel.visitors.IModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Factory that creates an information flow realized by a given link.
 * <p>
 * The source and destination of the information flow will be those of the given link. The information flow will be owned by the
 * namespace owning both source and destination.
 * 
 * @author cmarin
 */
@objid ("81734c69-1dec-11e2-8cad-001ec947c8cc")
class InformationFlowFactory {
    @objid ("81734c6b-1dec-11e2-8cad-001ec947c8cc")
     IModelFactory modelFactory;

    @objid ("81734c6c-1dec-11e2-8cad-001ec947c8cc")
    private CreateVisitor impl;

    @objid ("81734c6d-1dec-11e2-8cad-001ec947c8cc")
    private IModelVisitor canCreateImpl;

    /**
     * Constructor.
     * @param modelFactory a model factory. Can be get on the GmDiagram.
     */
    @objid ("81734c6e-1dec-11e2-8cad-001ec947c8cc")
    public InformationFlowFactory(final IModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        this.impl = new CreateVisitor();
        this.canCreateImpl = new CanCreateVisitor();
    }

    /**
     * Create an information flow realizing the given link.
     * @param realizingLink a model link or a model link end.
     * @return the created information flow.
     */
    @objid ("81734c73-1dec-11e2-8cad-001ec947c8cc")
    public InformationFlow createInformationFlow(final MObject realizingLink) {
        return (InformationFlow) realizingLink.accept(this.impl);
    }

    @objid ("8175aeaa-1dec-11e2-8cad-001ec947c8cc")
    public boolean canCreateInformationFlow(final MObject realizingLink) {
        return (Boolean) realizingLink.accept(this.canCreateImpl);
    }

    /**
     * Implementation based on visitor design pattern
     */
    @objid ("8175aebd-1dec-11e2-8cad-001ec947c8cc")
    private class CreateVisitor extends DefaultModelVisitor {
        @objid ("8175aebf-1dec-11e2-8cad-001ec947c8cc")
        CreateVisitor() {
        }

        @objid ("8175aec1-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitMessage(final Message theMessage) {
            final EList<Lifeline> srcs = theMessage.getSendEvent().getCovered();
            final EList<Lifeline> dests = theMessage.getReceiveEvent().getCovered();
            
            InformationFlow ret = createInformationFlow(srcs, dests);
            
            ret.getRealizingMessage().add(theMessage);
            return ret;
        }

        @objid ("8175aec8-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitAssociationEnd(final AssociationEnd role) {
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            srcs.add(role.getSource());
            dests.add(role.getTarget());
            InformationFlow ret = createInformationFlow(srcs, dests);
            ret.getRealizingFeature().add(role);
            return ret;
        }

        @objid ("8175aecf-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitLinkEnd(final LinkEnd role) {
            final Instance src = role.getSource();
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>();
            
            srcs.add(src);
            dests.add(role.getTarget());
            InformationFlow ret = createInformationFlow(srcs, dests);
            ret.getRealizingLink().add(role);
            return ret;
        }

        @objid ("8175aed6-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitActivityEdge(final ActivityEdge theEdge) {
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            srcs.add(theEdge.getSource());
            dests.add(theEdge.getTarget());
            
            final InformationFlow ret = createInformationFlow(srcs, dests);
            
            ret.getRealizingActivityEdge().add(theEdge);
            return ret;
        }

        @objid ("8175aedd-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitCommunicationMessage(final CommunicationMessage theMessage) {
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            CommunicationChannel channel = theMessage.getChannel();
            if (channel != null) {
                srcs.add(channel.getStart());
                dests.add(channel.getEnd());
            } else {
                channel = theMessage.getInvertedChannel();
            
                dests.add(channel.getStart());
                srcs.add(channel.getEnd());
            }
            
            final InformationFlow ret = createInformationFlow(srcs, dests);
            
            ret.getRealizingCommunicationMessage().add(theMessage);
            return ret;
        }

        /**
         * Utility method that creates an InformationFlow from the given sources to the given targets.
         * <p>
         * Sets its owner to the namespace owning all sources and targets.
         * <p>
         * The only remaining work is to add realizing links.
         * @param srcs The sources
         * @param dests The targets
         * @return The created information flow.
         */
        @objid ("81734c7a-1dec-11e2-8cad-001ec947c8cc")
        InformationFlow createInformationFlow(final Collection<? extends ModelElement> srcs, final Collection<? extends ModelElement> dests) {
            InformationFlow ret = InformationFlowFactory.this.modelFactory.createInformationFlow();
            
            for (ModelElement e : srcs)
                ret.getInformationSource().add(e);
            
            for (ModelElement e : dests)
                ret.getInformationTarget().add(e);
            
            Collection<MObject> c = new ArrayList<MObject>(srcs);
            c.addAll(dests);
            
            ret.setOwner(getCommonNameSpace(c));
            return ret;
        }

        /**
         * Get the namespace in the composition hierarchy that owns all the given elements.
         * @param elements The elements to search
         * @return The common namespace owning them.
         */
        @objid ("44d089fb-1eae-11e2-8cad-001ec947c8cc")
        @SuppressWarnings("unchecked")
        private NameSpace getCommonNameSpace(final Collection<MObject> elements) {
            final ArrayList<MObject>[] paths = new ArrayList[elements.size()];
            
            int i = 0;
            for (MObject el : elements) {
                paths[i] = new ArrayList<>();
                final ArrayList<MObject> l = paths[i];
                while (el != null) {
                    l.add(el);
                    el = el.getCompositionOwner();
                }
                i++;
            }
            
            int max = paths[0].size();
            for (ArrayList<MObject> l : paths) {
                Collections.reverse(l);
                if (max > l.size())
                    max = l.size();
            }
            
            NameSpace ret = null;
            i = 0;
            do {
                MObject el = paths[0].get(i);
                if (el instanceof NameSpace) {
                    for (ArrayList<MObject> l2 : paths) {
                        if (el.equals(l2.get(i))) {
                            ret = (NameSpace) el;
                        } else {
                            return ret;
                        }
                    }
                } else if (ret != null) {
                    return ret;
                }
                i++;
            
            } while (i < max);
            
            // Should never reach this point.
            throw new IllegalArgumentException("No common namespace between " + elements);
        }

    }

    @objid ("8175aee4-1dec-11e2-8cad-001ec947c8cc")
    private class CanCreateVisitor extends DefaultModelVisitor {
        @objid ("8175aee5-1dec-11e2-8cad-001ec947c8cc")
        public CanCreateVisitor() {
            // nothing
        }

        @objid ("8175aee7-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitElement(final Element arg0) {
            return Boolean.FALSE;
        }

        @objid ("8175aeee-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitMessage(final Message theMessage) {
            final EList<Lifeline> srcs = theMessage.getSendEvent().getCovered();
            final EList<Lifeline> dests = theMessage.getReceiveEvent().getCovered();
            return (MTools.getAuthTool().canModify(theMessage) && canCreateInformationFlow(srcs, dests));
        }

        @objid ("817810f6-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitActivityEdge(final ActivityEdge theEdge) {
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            srcs.add(theEdge.getSource());
            dests.add(theEdge.getTarget());
            return (MTools.getAuthTool().canModify(theEdge) && canCreateInformationFlow(srcs, dests));
        }

        @objid ("817810fd-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitAssociationEnd(final AssociationEnd role) {
            if (!MTools.getAuthTool().canModify(role.getSource()))
                return false;
            
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            srcs.add(role.getSource());
            dests.add(role.getTarget());
            return canCreateInformationFlow(srcs, dests);
        }

        @objid ("81781104-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitCommunicationMessage(final CommunicationMessage theMessage) {
            if (!MTools.getAuthTool().canModify(theMessage))
                return false;
            
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            CommunicationChannel channel = theMessage.getChannel();
            if (channel != null) {
                srcs.add(channel.getStart());
                dests.add(channel.getEnd());
            } else {
                channel = theMessage.getInvertedChannel();
            
                dests.add(channel.getStart());
                srcs.add(channel.getEnd());
            }
            return canCreateInformationFlow(srcs, dests);
        }

        @objid ("8178110b-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitLinkEnd(final LinkEnd role) {
            if (!MTools.getAuthTool().canModify(role.getSource()))
                return false;
            
            final ArrayList<ModelElement> srcs = new ArrayList<>(1);
            final ArrayList<ModelElement> dests = new ArrayList<>(1);
            
            srcs.add(role.getSource());
            dests.add(role.getTarget());
            return canCreateInformationFlow(srcs, dests);
        }

        /**
         * Get the namespace in the composition hierarchy that owns all the given elements.
         * @param elements The elements to search
         * @return The common namespace owning them.
         */
        @objid ("8175aea1-1dec-11e2-8cad-001ec947c8cc")
        @SuppressWarnings("unchecked")
        private NameSpace getCommonNameSpace(final Collection<MObject> elements) {
            final ArrayList<MObject>[] paths = new ArrayList[elements.size()];
            
            int i = 0;
            for (MObject el : elements) {
                paths[i] = new ArrayList<>();
                final ArrayList<MObject> l = paths[i];
                while (el != null) {
                    l.add(el);
                    el = el.getCompositionOwner();
                }
                i++;
            }
            
            int max = paths[0].size();
            for (ArrayList<MObject> l : paths) {
                Collections.reverse(l);
                if (max > l.size())
                    max = l.size();
            }
            
            NameSpace ret = null;
            i = 0;
            do {
                MObject el = paths[0].get(i);
                if (el instanceof NameSpace) {
                    for (ArrayList<MObject> l2 : paths) {
                        if (el.equals(l2.get(i))) {
                            ret = (NameSpace) el;
                        } else {
                            return ret;
                        }
                    }
                } else if (ret != null) {
                    return ret;
                }
                i++;
            
            } while (i < max);
            
            // Should never reach this point.
            throw new IllegalArgumentException("No common namespace between " + elements);
        }

        /**
         * Utility method that tells whether an information flow can be created from the given sources to the given targets.
         * <p>
         * <p>
         * Check that the namespace owning all sources and targets is modifiable.
         * <p>
         * @param srcs The sources
         * @param dests The targets
         * @return true if an information flow can be created, else false.
         */
        @objid ("8175aeb0-1dec-11e2-8cad-001ec947c8cc")
        private boolean canCreateInformationFlow(final Collection<? extends ModelElement> srcs, final Collection<? extends ModelElement> dests) {
            Collection<MObject> c = new ArrayList<MObject>(srcs);
            c.addAll(dests);
            return MTools.getAuthTool().canModify(getCommonNameSpace(c));
        }

    }

}

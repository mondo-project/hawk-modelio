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
                                    

package org.modelio.diagram.elements.gmfactory;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.diagram.elements.umlcommon.abstraction.GmAbstraction;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependency;
import org.modelio.diagram.elements.umlcommon.diagramholder.GmDiagramHolderLink;
import org.modelio.diagram.elements.umlcommon.elementRealization.GmElementRealization;
import org.modelio.diagram.elements.umlcommon.externdocument.GmExternDocumentLink;
import org.modelio.diagram.elements.umlcommon.namespaceuse.GmNamespaceUse;
import org.modelio.diagram.elements.umlcommon.note.GmNoteLink;
import org.modelio.diagram.elements.umlcommon.usage.GmUsage;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Abstraction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.statik.ElementRealization;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("8100db5e-1dec-11e2-8cad-001ec947c8cc")
public class GmLinkFactory implements IGmLinkFactory {
    /**
     * Cascaded factories catalog(if any).
     */
    @objid ("8100db60-1dec-11e2-8cad-001ec947c8cc")
    private Map<String, IGmLinkFactory> cascadedFactories = new HashMap<>();

    @objid ("8100db65-1dec-11e2-8cad-001ec947c8cc")
    private static final GmLinkFactory _instance = new GmLinkFactory();

    /**
     * @return the singleton instance of the default factory.
     */
    @objid ("8100db67-1dec-11e2-8cad-001ec947c8cc")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    /**
     * Creates the link factory for a diagram.
     * the diagram the factory will be attached to.
     */
    @objid ("8100db6c-1dec-11e2-8cad-001ec947c8cc")
    private GmLinkFactory() {
    }

    /**
     * Creates a graphic link representing the given link element.
     * @param diagram the diagram in which the gm is to be created
     * @param linkElement The model element to display
     * @return the created graphic link
     */
    @objid ("8100db6f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public GmLink create(GmAbstractDiagram diagram, MObject linkElement) {
        // First ask to the cascaded factories
        for (IGmLinkFactory factory : this.cascadedFactories.values()) {
            GmLink newLink = factory.create(diagram, linkElement);
            if (newLink != null)
                return newLink;
        }
        return (GmLink) linkElement.accept(new ImplVisitor(diagram));
    }

    /**
     * Register an GmLink factory extension.
     * <p>
     * Extension GmLink factories are called first when looking for an GmLink.
     * @param id id for the GmLink extension factory
     * @param factory the edit part factory.
     */
    @objid ("8100db77-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        this.cascadedFactories.put(id, factory);
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("81033dba-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void unregisterFactory(String id) {
        this.cascadedFactories.remove(id);
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("81033dbf-1dec-11e2-8cad-001ec947c8cc")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("81033dc1-1dec-11e2-8cad-001ec947c8cc")
        private GmAbstractDiagram diagram;

        @objid ("81033dc2-1dec-11e2-8cad-001ec947c8cc")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("81033dc5-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitDependency(final Dependency theDep) {
            if (theDep.isStereotyped("ModelerModule", "related_diagram")) {
                if (theDep.getDependsOn() == null || theDep.getDependsOn() instanceof AbstractDiagram) {
                    // It is a related diagram link
                    return new GmDiagramHolderLink(this.diagram, new MRef(theDep), theDep);
                }
            }
            
            // It is a standard dependency link
            return new GmDependency(this.diagram, theDep, new MRef(theDep));
        }

        @objid ("81033dcc-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitElement(Element theElement) {
            return null;
        }

        /**
         * Creates a GmNoteLink between the annotated element and the note.
         * <p>
         * The link destination must be a GmNote.
         * @throws java.lang.ClassCastException if the destination node is not a GmNote.
         */
        @objid ("81033dd2-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitNote(Note theNote) throws ClassCastException {
            final GmNoteLink link = new GmNoteLink(this.diagram, new MRef(theNote));
            return link;
        }

        @objid ("81033dd9-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitNamespaceUse(final NamespaceUse theNamespaceUse) {
            return new GmNamespaceUse(this.diagram, theNamespaceUse, new MRef(theNamespaceUse));
        }

        @objid ("81033de0-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitExternDocument(final ExternDocument theExternDocument) throws ClassCastException {
            final GmExternDocumentLink link = new GmExternDocumentLink(this.diagram,
                                                                       new MRef(theExternDocument));
            return link;
        }

        @objid ("8d7c9dd2-58d6-42b8-b15e-f4a57e1ea836")
        @Override
        public Object visitUsage(Usage theUsage) {
            return new GmUsage(this.diagram, theUsage, new MRef(theUsage));
        }

        @objid ("c47386ef-ea96-4641-a298-ecc60cec98c5")
        @Override
        public Object visitAbstraction(Abstraction obj) {
            return new GmAbstraction(this.diagram, obj, new MRef(obj));
        }

        @objid ("93e9231d-20af-4539-85a8-b13e1b9f8b16")
        @Override
        public Object visitElementRealization(ElementRealization obj) {
            return new GmElementRealization(this.diagram, obj, new MRef(obj));
        }

    }

}

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
                                    

package org.modelio.linkeditor.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.modelio.linkeditor.options.LinkEditorOptions;
import org.modelio.linkeditor.view.background.BackgroundModel;
import org.modelio.linkeditor.view.node.EdgeBus;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class is in charge of building the tree by exploring the model.
 */
@objid ("1bb9b8f2-5e33-11e2-b81d-002564c97630")
class TreeBuilder {
    @objid ("1bb9b8f4-5e33-11e2-b81d-002564c97630")
    public static void buildLeftTree(final BackgroundModel graph, final GraphNode node, final int depth, final LinkEditorOptions options) {
        // Extract element from node.
        MObject nodeElement = node.getData();
        // Based on options, look for from links.
        if (options.isInheritanceShown()) {
            buildInheritanceLeftTree(graph, node, nodeElement, depth, options);
        }
        if (options.isNamespaceUseShown()) {
            buildNamespaceUseLeftTree(graph, node, nodeElement, depth, options);
        }
        if (options.isAssociationShown() && nodeElement instanceof Classifier) {
            buildAssociationLeftTree(graph, node, nodeElement, depth, options);
        }
        if (options.isDependencyShown() && nodeElement instanceof ModelElement) {
            buildDependencyLeftTree(graph, node, nodeElement, depth, options);
        }
        if (options.isTraceShown() && nodeElement instanceof ModelElement) {
            buildTraceLeftTree(graph, node, nodeElement, depth, options);
        }
        if (options.isImportShown() && nodeElement instanceof NameSpace) {
            buildElementImportLeftTree(graph, node, nodeElement, depth, options);
        }
    }

    @objid ("1bb9b8fe-5e33-11e2-b81d-002564c97630")
    public static void buildRightTree(final BackgroundModel graph, final GraphNode node, final int depth, final LinkEditorOptions options) {
        // Extract element from node.
        MObject nodeElement = node.getData();
        // Based on options, look for from links.
        if (options.isInheritanceShown()) {
            buildInheritanceRightTree(graph, node, nodeElement, depth, options);
        }
        if (options.isNamespaceUseShown()) {
            buildNamespaceUseRightTree(graph, node, nodeElement, depth, options);
        }
        if (options.isAssociationShown() && nodeElement instanceof Classifier) {
            buildAssociationRightTree(graph, node, nodeElement, depth, options);
        }
        if (options.isDependencyShown() && nodeElement instanceof ModelElement) {
            buildDependencyRightTree(graph, node, nodeElement, depth, options);
        }
        if (options.isTraceShown() && nodeElement instanceof ModelElement) {
            buildTraceRightTree(graph, node, nodeElement, depth, options);
        }
        if (options.isImportShown()) {
            buildElementImportRightTree(graph, node, nodeElement, depth, options);
        }
    }

    @objid ("1bb9b908-5e33-11e2-b81d-002564c97630")
    private static void buildAssociationLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        Classifier classifierElement = (Classifier) nodeElement;
        
        // Gather ends targeting the selected classifier
        // associatedNode <-----> classifierElement
        // associatedNode ------- classifierElement
        for (AssociationEnd assocEnd : classifierElement.getOwnedEnd()) {
            AssociationEnd opposite = assocEnd.getOpposite();
            boolean bothNavigable = opposite.isNavigable() && assocEnd.isNavigable();
            boolean noneNavigable = !opposite.isNavigable() && !assocEnd.isNavigable();
            if (bothNavigable || noneNavigable) {
                GraphNode associatedNode = new GraphNode(opposite.getSource());
        
                Edge assocEndEdge = createEdge(opposite, associatedNode, node, graph);
                // Add to the graph
                graph.addNode(associatedNode);
                graph.addEdge(assocEndEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, associatedNode, depth - 1, options);
                }
            }
        }
        
        // Gather ends targeting the selected classifier, that aren't navigable in the other side
        // associatedNode ------> classifierElement
        for (AssociationEnd opposite : classifierElement.getTargetingEnd()) {
            GraphNode associatedNode = null;
            if (opposite.isNavigable() && !opposite.getOpposite().isNavigable()) {
                associatedNode = new GraphNode(opposite.getSource());
            }
        
            if (associatedNode != null) {
                Edge assocEndEdge = createEdge(opposite, associatedNode, node, graph);
                // Add to the graph
                graph.addNode(associatedNode);
                graph.addEdge(assocEndEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, associatedNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bb9b914-5e33-11e2-b81d-002564c97630")
    private static void buildAssociationRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        Classifier classifierElement = (Classifier) nodeElement;
        
        // Gather only ends targeting another classifier
        // classifierElement ------> associatedNode
        for (AssociationEnd assocEnd : classifierElement.getOwnedEnd()) {
            if (assocEnd.isNavigable() && !assocEnd.getOpposite().isNavigable()) {
                GraphNode associatedNode = new GraphNode(assocEnd.getTarget());
                Edge assocEdge = createEdge(assocEnd, node, associatedNode, graph);
                // Add to the graph
                graph.addNode(associatedNode);
                graph.addEdge(assocEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, associatedNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bb9b920-5e33-11e2-b81d-002564c97630")
    private static void buildDependencyLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        ModelElement modelElement = (ModelElement) nodeElement;
        for (Dependency dependency : modelElement.getImpactedDependency()) {
            boolean passedFilter = true;
            if (options.isDependencyFiltered()) {
                // Apply filter
                passedFilter = false;
                for (Stereotype allowedStereotype : options.getDependencyFilter()) {
                    if (dependency.isStereotyped(allowedStereotype.getModule().getName(), allowedStereotype.getName())) {
                        passedFilter = true;
                        break;
                    }
                }
            } else if (dependency.isStereotyped("ModelerModule", "trace")) {
                // Filter traceabilities in all cases, since there is a dedicated button and filter for them.
                passedFilter = false;
            }
            if (passedFilter) {
                GraphNode impactedNode = new GraphNode(dependency.getImpacted());
                Edge dependencyEdge = createEdge(dependency, impactedNode, node, graph);
                // Add to the graph
                graph.addNode(impactedNode);
                graph.addEdge(dependencyEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, impactedNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbc1a24-5e33-11e2-b81d-002564c97630")
    private static void buildDependencyRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        ModelElement modelElement = (ModelElement) nodeElement;
        for (Dependency dependency : modelElement.getDependsOnDependency()) {
            boolean passedFilter = true;
            if (options.isDependencyFiltered()) {
                // Apply filter
                passedFilter = false;
                for (Stereotype allowedStereotype : options.getDependencyFilter()) {
                    if (dependency.isStereotyped(allowedStereotype.getModule().getName(), allowedStereotype.getName())) {
                        passedFilter = true;
                        break;
                    }
                }
            } else if (dependency.isStereotyped("ModelerModule", "trace")) {
                // Filter traceabilities in all cases, since there is a dedicated button and filter for them.
                passedFilter = false;
            }
            if (passedFilter && dependency.getDependsOn() != null /*<- also filtering "dead end" dependencies.*/) {
                GraphNode dependsOnNode = new GraphNode(dependency.getDependsOn());
                Edge dependencyEdge = createEdge(dependency, node, dependsOnNode, graph);
                // Add to the graph
                graph.addNode(dependsOnNode);
                graph.addEdge(dependencyEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, dependsOnNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbc1a30-5e33-11e2-b81d-002564c97630")
    private static void buildElementImportLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        NameSpace namespaceElement = (NameSpace) nodeElement;
        for (ElementImport elementImport : namespaceElement.getImporting()) {
            GraphNode importedNode;
            if (elementImport.getImportingOperation() != null) {
                importedNode = new GraphNode(elementImport.getImportingOperation());
            } else {
                importedNode = new GraphNode(elementImport.getImportingNameSpace());
            }
            Edge importEdge = createEdge(elementImport, importedNode, node, graph);
            // Add to the graph
            graph.addNode(importedNode);
            graph.addEdge(importEdge);
            // If depth not 0, recurse.
            if (depth > 1) {
                buildLeftTree(graph, importedNode, depth - 1, options);
            }
        }
    }

    @objid ("1bbc1a3c-5e33-11e2-b81d-002564c97630")
    private static void buildElementImportRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        if (nodeElement instanceof Operation) {
            Operation operationElement = (Operation) nodeElement;
            for (ElementImport elementImport : operationElement.getOwnedImport()) {
                GraphNode importedNode = new GraphNode(elementImport.getImportedElement());
                Edge importEdge = createEdge(elementImport, node, importedNode, graph);
                // Add to the graph
                graph.addNode(importedNode);
                graph.addEdge(importEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, importedNode, depth - 1, options);
                }
        
            }
        }
        if (nodeElement instanceof NameSpace) {
            NameSpace namespaceElement = (NameSpace) nodeElement;
            for (ElementImport elementImport : namespaceElement.getOwnedImport()) {
                GraphNode importedNode = new GraphNode(elementImport.getImportedElement());
                Edge importEdge = createEdge(elementImport, node, importedNode, graph);
                // Add to the graph
                graph.addNode(importedNode);
                graph.addEdge(importEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, importedNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbc1a48-5e33-11e2-b81d-002564c97630")
    private static void buildInheritanceLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        if (nodeElement instanceof NameSpace) {
            NameSpace namespaceElement = (NameSpace) nodeElement;
            for (Generalization generalisation : namespaceElement.getSpecialization()) {
                // Create node for the element at the other end
                GraphNode subTypeNode = new GraphNode(generalisation.getSubType());
                Edge generalisationEdge = createEdge(generalisation, subTypeNode, node, graph);
                // Add to the graph
                graph.addNode(subTypeNode);
                graph.addEdge(generalisationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, subTypeNode, depth - 1, options);
                }
            }
        }
        if (nodeElement instanceof Interface) {
            Interface interfaceElement = (Interface) nodeElement;
            for (InterfaceRealization interfaceRealization : interfaceElement.getImplementedLink()) {
                // Create node for the element at the other end
                GraphNode implementerNode = new GraphNode(interfaceRealization.getImplementer());
                Edge interfaceRealizationEdge = createEdge(interfaceRealization, implementerNode, node, graph);
                // Add to the graph
                graph.addNode(implementerNode);
                graph.addEdge(interfaceRealizationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, implementerNode, depth - 1, options);
                }
            }
        }
        if (nodeElement instanceof Operation) {
            Operation operationElement = (Operation) nodeElement;
            for (Operation operationRedefinition : operationElement.getRedefinition()) {
                // Create node for the element at the other end
                GraphNode redefinitionNode = new GraphNode(operationRedefinition);
                Edge operationRedefinitionEdge = createEdge(operationRedefinition,
                        redefinitionNode,
                        node,
                        graph);
                // Add to the graph
                graph.addNode(redefinitionNode);
                graph.addEdge(operationRedefinitionEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, redefinitionNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbc1a54-5e33-11e2-b81d-002564c97630")
    private static void buildInheritanceRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        if (nodeElement instanceof NameSpace) {
            NameSpace namespaceElement = (NameSpace) nodeElement;
            for (Generalization generalisation : namespaceElement.getParent()) {
                // Create node for the element at the other end
                GraphNode superTypeNode = new GraphNode(generalisation.getSuperType());
                Edge generalisationEdge = createEdge(generalisation, node, superTypeNode, graph);
                // Add to the graph
                graph.addNode(superTypeNode);
                graph.addEdge(generalisationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, superTypeNode, depth - 1, options);
                }
            }
            for (InterfaceRealization interfaceRealization : namespaceElement.getRealized()) {
                // Create node for the element at the other end
                GraphNode implementedInterfaceNode = new GraphNode(interfaceRealization.getImplemented());
                Edge interfaceRealisationEdge = createEdge(interfaceRealization,
                        node,
                        implementedInterfaceNode,
                        graph);
                // Add to the graph
                graph.addNode(implementedInterfaceNode);
                graph.addEdge(interfaceRealisationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, implementedInterfaceNode, depth - 1, options);
                }
            }
        }
        if (nodeElement instanceof Operation) {
            Operation operationElement = (Operation) nodeElement;
            Operation redefinedOperation = operationElement.getRedefines();
            if (redefinedOperation != null) {
                // Create node for the element at the other end
                GraphNode redefinedOperationNode = new GraphNode(redefinedOperation);
                Edge redefiningOperationEdge = createEdge(operationElement,
                        node,
                        redefinedOperationNode,
                        graph);
                // Add to the graph
                graph.addNode(redefinedOperationNode);
                graph.addEdge(redefiningOperationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, redefinedOperationNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbc1a60-5e33-11e2-b81d-002564c97630")
    private static void buildNamespaceUseLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        if (nodeElement instanceof NameSpace) {
            NameSpace namespaceElement = (NameSpace) nodeElement;
        
            for (NamespaceUse use : namespaceElement.getUserNsu()) {
                // Create node for the element at the other end
                GraphNode userNode = new GraphNode(use.getUser());
                Edge generalisationEdge = createEdge(use, userNode, node, graph);
                // Add to the graph
                graph.addNode(userNode);
                graph.addEdge(generalisationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, userNode, depth - 1, options);
                }
            }
        } else if (nodeElement instanceof NamespaceUse) {
            NamespaceUse useEl = (NamespaceUse) nodeElement;
            NameSpace user = useEl.getUser();
            if (user != null) {
                // Create node for the element at the other end
                GraphNode userNode = new GraphNode(user);
                Edge edge = createEdge(useEl, userNode, node, graph);
                // Add to the graph
                graph.addNode(userNode);
                graph.addEdge(edge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, userNode, depth - 1, options);
                }
            }
        
            // --- cause graph
            /*
            for (MObject cause : useEl.getcause()) {
                // Create node for the element at the other end
                GraphNode userNode = new GraphNode(cause);
                Edge edge = createEdge(cause, userNode, node, graph);
                // Add to the graph
                graph.addNode(userNode);
                graph.addEdge(edge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, userNode, depth - 1, options);
                }
            }*/
        
        }
    }

    @objid ("1bbc1a6c-5e33-11e2-b81d-002564c97630")
    private static void buildNamespaceUseRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        if (nodeElement instanceof NameSpace) {
            NameSpace namespaceElement = (NameSpace) nodeElement;
            for (NamespaceUse use : namespaceElement.getUsedNsu()) {
                // Create node for the element at the other end
                GraphNode usedNode = new GraphNode(use.getUsed());
                Edge generalisationEdge = createEdge(use, node, usedNode, graph);
                // Add to the graph
                graph.addNode(usedNode);
                graph.addEdge(generalisationEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, usedNode, depth - 1, options);
                }
            }
        } else if (nodeElement instanceof NamespaceUse) {
            NamespaceUse useEl = (NamespaceUse) nodeElement;
            NameSpace user = useEl.getUsed();
            if (user != null) {
                // Create node for the element at the other end
                GraphNode userNode = new GraphNode(user);
                Edge edge = createEdge(useEl, node, userNode, graph);
                // Add to the graph
                graph.addNode(userNode);
                graph.addEdge(edge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, userNode, depth - 1, options);
                }
            }
        
            // --- cause graph
            /*
            for (MObject cause : useEl.getcausing()) {
                // Create node for the element at the other end
                GraphNode userNode = new GraphNode(cause);
                Edge edge = createEdge(cause, userNode, node, graph);
                // Add to the graph
                graph.addNode(userNode);
                graph.addEdge(edge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, userNode, depth - 1, options);
                }
            }*/
        
        }
    }

    @objid ("1bbc1a78-5e33-11e2-b81d-002564c97630")
    private static void buildTraceLeftTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        ModelElement modelElement = (ModelElement) nodeElement;
        for (Dependency dependency : modelElement.getImpactedDependency()) {
            if (dependency.isStereotyped("ModelerModule", "trace")) {
                GraphNode impactedNode = new GraphNode(dependency.getImpacted());
                Edge dependencyEdge = createEdge(dependency, impactedNode, node, graph);
                // Add to the graph
                graph.addNode(impactedNode);
                graph.addEdge(dependencyEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildLeftTree(graph, impactedNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbe7b84-5e33-11e2-b81d-002564c97630")
    private static void buildTraceRightTree(final BackgroundModel graph, final GraphNode node, final MObject nodeElement, final int depth, final LinkEditorOptions options) {
        ModelElement modelElement = (ModelElement) nodeElement;
        for (Dependency dependency : modelElement.getDependsOnDependency()) {
        
            if (dependency.isStereotyped("ModelerModule", "trace") && dependency.getDependsOn() != null /*<- also filtering "dead end" dependencies.*/) {
                GraphNode dependsOnNode = new GraphNode(dependency.getDependsOn());
                Edge dependencyEdge = createEdge(dependency, node, dependsOnNode, graph);
                // Add to the graph
                graph.addNode(dependsOnNode);
                graph.addEdge(dependencyEdge);
                // If depth not 0, recurse.
                if (depth > 1) {
                    buildRightTree(graph, dependsOnNode, depth - 1, options);
                }
            }
        }
    }

    @objid ("1bbe7b90-5e33-11e2-b81d-002564c97630")
    private static Edge createEdge(final Object data, final GraphNode fromNode, final GraphNode toNode, final BackgroundModel graph) {
        Node realFrom = fromNode;
        Node realTo = toNode;
        if (fromNode.outgoing.size() > 0) {
            // If there is at least one edge already
            if (fromNode.outgoing.getEdge(0).target instanceof EdgeBus) {
                // If it leads to a bus use it
                realFrom = fromNode.outgoing.getEdge(0).target;
            } else {
                // otherwise create it
                Edge modifiedEdge = fromNode.outgoing.getEdge(0);
                EdgeBus bus = new EdgeBus();
                Edge busEdge = new Edge(fromNode, bus);
                modifiedEdge.setSource(bus);
                graph.addNode(bus);
                graph.addEdge(busEdge);
                realFrom = bus;
            }
        }
        if (toNode.incoming.size() > 0) {
            // If there is at least one edge already
            if (toNode.incoming.getEdge(0).source instanceof EdgeBus) {
                // If it leads to a bus use it
                realTo = toNode.incoming.getEdge(0).source;
            } else {
                // otherwise create it
                Edge modifiedEdge = toNode.incoming.getEdge(0);
                EdgeBus bus = new EdgeBus();
                Edge busEdge = new Edge(bus, toNode);
                modifiedEdge.setTarget(bus);
                graph.addNode(bus);
                graph.addEdge(busEdge);
                realTo = bus;
            }
        }
        return new Edge(data, realFrom, realTo);
    }

}

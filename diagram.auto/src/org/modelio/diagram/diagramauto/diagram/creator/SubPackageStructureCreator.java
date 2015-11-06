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
                                    

package org.modelio.diagram.diagramauto.diagram.creator;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.InvalidDestinationPointException;
import org.modelio.api.diagram.InvalidPointsPathException;
import org.modelio.api.diagram.InvalidSourcePointException;
import org.modelio.diagram.diagramauto.diagram.DiagramStyleHandle;
import org.modelio.diagram.diagramauto.diagram.layout.DiagonalLayout;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.Package;

@objid ("6ea3c15a-6a80-4418-84db-93923707e9bf")
public class SubPackageStructureCreator extends AbstractDiagramCreator {
    @objid ("dc2c84aa-e4e4-46c3-b441-fc2d525bedac")
    public List<IDiagramNode> _contentDgs;

    @objid ("77d98f85-f2ae-4863-9c99-ff93bc09fdd7")
    public SubPackageStructureCreator(IMModelServices modelServices) {
        super(modelServices);
        
        this._contentDgs = new ArrayList<>();
    }

    @objid ("59fdfe02-8d2b-4997-9a0b-dcba038ae62e")
    @Override
    public String getAutoDiagramName() {
        return "subpackage_structure_autodiagram";
    }

    @objid ("bc3e8181-162e-43b9-8f15-8d8bc87a546c")
    @Override
    public String getAutoDiagramGroup() {
        return "SubPackage Structure";
    }

    @objid ("98aa1323-e088-489a-b929-14600654ef44")
    @Override
    public ModelElement getAutoDiagramContext(final ModelElement main) {
        return main;
    }

    @objid ("732f94cd-fcb5-4e1a-beb8-64c887bb201a")
    @Override
    protected void initialUnmasking(final IDiagramHandle dh, final ModelElement main) {
        if (main instanceof org.modelio.metamodel.uml.statik.Package) {
            initialUnmasking(dh, (ModelTree) main);
        }
    }

    @objid ("bab7ec8a-a51d-4d5d-8b0b-a260c093b0e3")
    private void initialUnmasking(final IDiagramHandle dh, final ModelTree main) {
        // Mask old content
        for (IDiagramNode node : dh.getDiagramNode().getNodes()) {
            node.mask();
        }
        
        // unmask content
        for (ModelTree child : main.getOwnedElement()) {
            if (child instanceof org.modelio.metamodel.uml.statik.Package) {
                List<IDiagramGraphic> nodes = dh.unmask(child, 0, 0);
                if ((nodes != null) && (nodes.size() > 0)) {
                    IDiagramNode node = (IDiagramNode) nodes.get(0);
                    node.setRepresentationMode(1);
        
                    // Add intern/extern style
                    initStyle(main, node);
        
                    this._contentDgs.add(node);
                }
            }
        
        }
        
        // Unmask blue links
        for (ModelTree child : main.getOwnedElement()) {
        
            if (child instanceof org.modelio.metamodel.uml.statik.Package) {
                for (NamespaceUse blueLink : getNamespaceUseToElement((org.modelio.metamodel.uml.statik.Package) child)) {
                    dh.unmask(blueLink, 0, 0);
                }
            }
        }
    }

    @objid ("452ce988-c665-4fa5-8592-06fa9d23a7d0")
    private List<NamespaceUse> getNamespaceUseToElement(final NameSpace namespaceElt) {
        List<NamespaceUse> nsus = new ArrayList<>();
        
        for (int k = 0; k < namespaceElt.getUsedNsu().size(); ++k) {
            NamespaceUse blueLink = namespaceElt.getUsedNsu().get(k);
            final NameSpace used = blueLink.getUsed();
            if ((used instanceof Package) && isInSamePackage(namespaceElt, used)) {
                nsus.add(blueLink);
            }
        }
        return nsus;
    }

    @objid ("433e8b3f-389d-4650-86c6-ec223b11dd9f")
    public void initStyle(final ModelTree main, final IDiagramNode node) {
        if (main.equals(node.getElement())) {
            node.setStyle(new DiagramStyleHandle(DiagramStyles.getStyleManager().getStyle(DiagramStyles.MAIN_STYLE_NAME)));
        } else if (node.getElement().getStatus().isRamc()) {
            node.setStyle(new DiagramStyleHandle(DiagramStyles.getStyleManager().getStyle(DiagramStyles.RAMC_STYLE_NAME)));
        } else {
            node.setStyle(new DiagramStyleHandle(DiagramStyles.getStyleManager().getStyle(DiagramStyles.INTERN_STYLE_NAME)));
        }
    }

    @objid ("a719621f-9d96-4064-9bfd-7aaf907ec95b")
    private boolean isInSamePackage(final ModelTree elt1, final ModelTree elt2) {
        ModelTree parent1 = getOwnerPackage(elt1);
        ModelTree parent2 = getOwnerPackage(elt2);
        return parent1 != null && parent2 != null && parent1.equals(parent2);
    }

    @objid ("4b56f666-ccfa-417c-bbff-41af1be3b131")
    private ModelTree getOwnerPackage(final ModelTree elt) {
        ModelTree parent = elt.getOwner();
        
        // Take parents for Inner elements
        while ((parent != null)) {
            if (parent instanceof Package) {
                return parent;
            } else {
                parent = parent.getOwner();
            }
        }
        return parent;
    }

    @objid ("c658a4b2-8d7f-4ba0-910c-6b4c0c045cb6")
    @Override
    protected void layout(final IDiagramHandle dh) {
        DiagonalLayout layout = new DiagonalLayout();
        try {
            layout.layout(dh, this._contentDgs);
        } catch (InvalidSourcePointException | InvalidPointsPathException | InvalidDestinationPointException e) {
            // Should never happen
            e.printStackTrace();
        }
    }

    @objid ("9e009f97-c1e1-442c-9b68-b2b84ef068c1")
    @Override
    public ModelElement getMainElement(AbstractDiagram autoDiagram) {
        return autoDiagram.getOrigin();
    }

}

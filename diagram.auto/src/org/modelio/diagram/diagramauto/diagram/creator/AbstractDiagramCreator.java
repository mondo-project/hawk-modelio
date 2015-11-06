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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.autodiagram.IDiagramCreator;
import org.modelio.api.modelio.Modelio;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("7b0608cc-5339-4a12-91aa-df36556c07a9")
public abstract class AbstractDiagramCreator implements IDiagramCreator {
    @objid ("82e36ced-0fe5-4c0e-ba2e-cc78480e886a")
    protected IMModelServices modelServices;

    @objid ("a068b3d7-d0c3-4804-ad04-d37951c95983")
    public AbstractDiagramCreator(IMModelServices modelServices) {
        this.modelServices = modelServices;
    }

    @objid ("d2278822-5961-4832-b4ba-3b7572246fb8")
    @Override
    public final AbstractDiagram createDiagram(final ModelElement main) {
        // get the auto diagram
        AbstractDiagram diagram = getExistingAutoDiagram(main);
        try {
            if (diagram == null) {
                diagram = createAutoDiagram(main);
            }
        
            if (diagram == null) {
                return null;
            }
        
            // get the diagram handle to work with
            try (IDiagramHandle dh = Modelio.getInstance().getDiagramService().getDiagramHandle(diagram)) {
                dh.setBatchMode(true);
        
                // perform the inital unmasking (mainly nodes)
                initialUnmasking(dh, main);
        
                // layout the diagram
                layout(dh);
        
                // open the resulting diagram
                dh.save();
                dh.setBatchMode(false);
                return diagram;
            }
        } catch (Error e) {
            e.printStackTrace();
            if (diagram != null) {
                diagram.delete();
            }
            return null;
        }
    }

    @objid ("bbbe495b-7957-486c-94ec-0fe2a0406fcc")
    @Override
    public final AbstractDiagram getExistingAutoDiagram(final ModelElement main) {
        ModelElement context = getAutoDiagramContext(main);
        if (context != null) {
            for (AbstractDiagram diagram : context.getProduct()) {
                if (diagram.getName().equals(main.getName() + " (" + getAutoDiagramName() + ")")) {
                    return diagram;
                }
            }
        }
        return null;
    }

    @objid ("8128a926-bcfa-4a9b-889e-d25217dc22c8")
    private AbstractDiagram createAutoDiagram(final ModelElement main) {
        ModelElement context = getAutoDiagramContext(main);
        DiagramSet rootSet = getDiagramSet(context);
        // get global auto diagram root
        DiagramSet autoSet = null;
        for (DiagramSet dset : rootSet.getSub()) {
            if (dset.getName().equals("Auto Diagrams")) {
                autoSet = dset;
                break;
            }
        }
        if (autoSet == null) {
        
            autoSet = this.modelServices.getModelFactory().createDiagramSet();
            autoSet.setName("Auto Diagrams");
            rootSet.getSub().add(autoSet);
        }
        
        // get root for the current diagram
        DiagramSet autogroup = null;
        for (DiagramSet dset : autoSet.getSub()) {
            if (dset.getName().equals(getAutoDiagramGroup())) {
                autogroup = dset;
                break;
            }
        }
        if (autogroup == null) {
            autogroup = this.modelServices.getModelFactory().createDiagramSet();
            autogroup.setName(getAutoDiagramGroup());
            autoSet.getSub().add(autogroup);
        }
        
        Stereotype stereotype = this.modelServices.findStereotypes("AutoDiagram", Metamodel.getMClass(ClassDiagram.class)).get(0);
        AbstractDiagram diagram = this.modelServices.getModelFactory().createClassDiagram();
        diagram.setName(main.getName() + " (" + getAutoDiagramName() + ")");
        diagram.getExtension().add(stereotype);
        context.getProduct().add(diagram);
        autogroup.getReferencedDiagram().add(diagram);
        return diagram;
    }

    @objid ("d2fe6e08-6c0a-49ef-93b0-3fa813f5585e")
    private DiagramSet getDiagramSet(ModelElement context) {
        if (context == null) {
            return null;
        }
        for (MObject root : GProject.getProject(context).getFragment(context).getRoots()) {
            if (root instanceof Project) {
                Project project = (Project) root;
                return project.getDiagramRoot();
        
            }
        }
        return null;
    }

    @objid ("af1ac11c-3710-4f9a-900d-aca9dd06863a")
    protected abstract void initialUnmasking(final IDiagramHandle dh, final ModelElement main);

    @objid ("86b6e380-ffa6-43f4-9d2a-e38a6b33177c")
    protected abstract void layout(final IDiagramHandle dh);

}

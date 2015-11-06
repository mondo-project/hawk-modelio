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
                                    

package org.modelio.model.browser.handlers.state;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.stateMachineModel.DeepHistoryPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.EntryPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ExitPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.KindOfStateMachine;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ShallowHistoryPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.model.browser.handlers.CreateElementHandler;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;

@objid ("8b4990d7-c9d8-11e1-b479-001ec947c8cc")
public class CreatePseudoStateHandler extends CreateElementHandler {
    @objid ("4e4e5932-ccde-11e1-97e5-001ec947c8cc")
    private StateMachine getEnclosingStateMachine(State s) {
        State state = s;
        Region region = null;
        while ((state != null)) {
            // Get the region holding the state:
            // either it is a direct child of the StateMachine
            // or it is a child of a parent State.
            region = state.getParent();
            if (region.getRepresented() == null) {
                state = region.getParent();
            } else {
                return region.getRepresented();
            }
        }
        return null;
    }

    @objid ("4e4e5937-ccde-11e1-97e5-001ec947c8cc")
    private StateMachine getEnclosingStateMachine(Region region) {
        if (region.getRepresented() != null) {
            return region.getRepresented();
        }
        // else
        return getEnclosingStateMachine(region.getParent());
    }

    @objid ("4e4e5945-ccde-11e1-97e5-001ec947c8cc")
    private boolean isAllowed(String metaclass) {
        return !((Metamodel.getMClass(EntryPointPseudoState.class).getName().equals(metaclass)) 
                || (Metamodel.getMClass(ExitPointPseudoState.class).getName().equals(metaclass))
                || (Metamodel.getMClass(ShallowHistoryPseudoState.class).getName().equals(metaclass)) 
                || (Metamodel.getMClass(DeepHistoryPseudoState.class).getName().equals(metaclass)));
    }

    @objid ("4e4e594a-ccde-11e1-97e5-001ec947c8cc")
    private Element getEffectiveOwner(Element owner, MClass metaclass) {
        if (owner instanceof StateMachine) {
            // Entry and Exit points are added to the StateMachine, and not to
            // the TopRegion
            if ((Metamodel.getMClass(EntryPointPseudoState.class) == metaclass) || (Metamodel.getMClass(ExitPointPseudoState.class) == metaclass)) {
                return owner;
            }
        
            // If top region doesn't exist yet, create it.
            StateMachine stateMachine = (StateMachine) owner;
            if (stateMachine.getTop() == null) {
                Region topRegion = this.mmServices.getModelFactory(owner).createRegion();
                stateMachine.setTop(topRegion);
            }
            return stateMachine.getTop();
        } else if (owner instanceof Region) {
            return owner;
        } else {
            return null;
        }
    }

    @objid ("00914a06-d19c-1006-9c1d-001ec947cd2a")
    @Override
    protected boolean doCanExecute(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype) {
        StateMachine stateMachine = null;
        if (owner instanceof State) {
            stateMachine = getEnclosingStateMachine((State) owner);
        } else if (owner instanceof Region) {
            stateMachine = getEnclosingStateMachine((Region) owner);
        } else if (owner instanceof StateMachine) {
            stateMachine = (StateMachine) owner;
        }
        if (stateMachine != null && stateMachine.getKind() == KindOfStateMachine.PROTOCOL && !isAllowed(metaclass.getName())) {
            return false;
        }
        return super.doCanExecute(owner, metaclass, dependency, stereotype);
    }

    @objid ("0091a1d6-d19c-1006-9c1d-001ec947cd2a")
    @Override
    protected Element doCreate(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype) {
        Element effectiveOwner = getEffectiveOwner(owner, metaclass);
        return super.doCreate(effectiveOwner, metaclass, dependency, stereotype);
    }

    @objid ("5264b0c9-9598-452b-9d45-cbaf242634ae")
    @Override
    protected Element getSelectedElement(final Object selection, final MClass metaclass) {
        Element selectedOwner = super.getSelectedElement(selection,metaclass);
        Element selectedElement = getEffectiveOwner(selectedOwner, metaclass);
        return selectedElement;
    }

}

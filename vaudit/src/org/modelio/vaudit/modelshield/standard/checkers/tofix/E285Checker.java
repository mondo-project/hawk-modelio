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
                                    

package org.modelio.vaudit.modelshield.standard.checkers.tofix;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E285:
 * <ul>
 * <li>desc = In a group, neither sub-groups nor parent groups can contain crossing nodes or flows.</li>
 * <li>what = The ''{0}'' element is held in both ''{1}'' and its sub-group ''{2}''.</li>
 * </ul>
 */
@objid ("00741170-e20e-1f69-b3fb-001ec947cd2a")
public class E285Checker implements IChecker {
    @objid ("0011b44e-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E285";

    @objid ("0000510e-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /* === BEGIN CXX CODE
             TransitiveClosure downwardToChildren;
             downwardToChildren
                  // navigate downward, from ActivityGroup trough nested ActivityNodes or ActivityEdges
                  .addDef( ActivityPartitionClass(), ActivityPartitionContainedNodeActivityNode(), ActivityNodeClass())
        
                  .addDef( ActivityNodeClass(), ActivityNodeIncomingActivityEdge(), ActivityEdgeClass())
                  .addDef( ActivityNodeClass(), ActivityNodeOutgoingActivityEdge(), ActivityEdgeClass())
        
                  .addDef( ActivityNodeClass(), ActivityActionInputInputPin(), InputPinClass())
                  .addDef( ActivityNodeClass(), ActivityActionOutputOutputPin(), OutputPinClass())
        
                  .addDef( ActivityNodeClass(), ActivityNodeOutgoingActivityEdge(), ActivityEdgeClass())
                  .addDef( ActivityNodeClass(), ActivityNodeIncomingActivityEdge(), ActivityEdgeClass())
        
                  .addDef( ActivityPartitionClass(), ActivityPartitionSubPartitionActivityPartition(), ActivityPartitionClass())
        
                  ;
        
             TransitiveClosure upwardToFirstActivityGroups;
             upwardToFirstActivityGroups
                 // navigate upward, from ActivityNodes or ActivityEdges to
                 // the containing Activities, eventually trough some pins
        
                  .addDef( ActivityEdgeClass(), ActivityEdgeSourceActivityNode(), ActivityNodeClass())
                  .addDef( ActivityEdgeClass(), ActivityEdgeTargetActivityNode(), ActivityNodeClass())
        
                  .addDef( InputPinClass(), InputPinInputingActivityAction(), ActivityNodeClass())
                  .addDef( OutputPinClass(), OutputPinOutputingActivityAction(), ActivityNodeClass())
        
                  .addDef( ClauseClass(), ClauseOwnerConditionalNode(), ConditionalNodeClass())
        
                  .addDef( ActivityNodeClass(), ActivityNodeOwnerClauseClause(), ClauseClass())
                  .addDef( ActivityNodeClass(), ActivityNodeOwnerNodeStructuredActivityNode(), StructuredActivityNodeClass())
                  .addDef( ActivityNodeClass(), ActivityNodeOwnerPartitionActivityPartition(), ActivityPartitionClass())
                  ;
        
             TransitiveClosure isSubPartitionOf
              ( ActivityPartitionClass(), ActivityPartitionSuperPartitionActivityPartition(), ActivityPartitionClass()) ;
        
             if (objectHasBaseClass(&object, ActivityPartitionClass()))
             {
                  set<SmObject*> children = downwardToChildren.getRelatedNotOfType(object, ActivityGroupClass());
                  set<SmObject*>::iterator it;
                  for (it = children.begin(); it != children.end(); ++it)
                       checkE285(rule, **it, session);
             }
             else
             {
                  set<SmObject*> directGroups = upwardToFirstActivityGroups.getRelatedOfType(object, ActivityGroupClass());
        
                  if (directGroups.size() > 1)
                  {
                       set<SmObject*>::iterator it, it2, it_beg, it_end;
                       it_beg = directGroups.begin();
                       it_end = directGroups.end();
                       CR_string msg;
        
                       for (it = it_beg; it != it_end; ++it)
                       {
                            SmObject* ag1 = *it;
                            for (it2 = it_beg; it2 != it_end; it2++)
                            {
                                 SmObject* ag2 = *it2;
                                 if (ag2 == ag1)
                                      continue;
        
                                 if (isSubPartitionOf.isRelated(*ag1, *ag2))
                                 {
                                      ModelElement& element = dynamic_cast<ModelElement&>(object);
                                      ModelElement* el1 = dynamic_cast<ModelElement*>(ag1);
                                      ModelElement* el2 = dynamic_cast<ModelElement*>(ag2);
                                      msg.pformat(rule.whatHappen.c_str(),
                                                  element.get_Name().c_str(),
                                                  el2->get_Name().c_str(),
                                                  el1->get_Name().c_str()
                                           );
                                      AuditDiagnosticEntry entry(session.getPlan(), rule, object, "", msg.c_str(), rule.severity);
                                      entry.addLinkEntry(*el2);
                                      entry.addLinkEntry(*el1);
        
                                      session.addDiagnosticEntry(entry);
                                 };
                            }
                       }
                  }
             }
        === END CXX CODE */
    }

    @objid ("000052ee-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=ActivityPartition, feature=ContainedNode
        plan.registerChecker(this, Metamodel.getMClass(ActivityPartition.class), TriggerType.AnyTrigger, "ContainedNode");
        
        // trigger=*, metaclass=ActivityNode, feature=OwnerClause
        plan.registerChecker(this, Metamodel.getMClass(ActivityNode.class), TriggerType.AnyTrigger, "OwnerClause");
        
        // trigger=*, metaclass=ActivityNode, feature=OwnerNode
        plan.registerChecker(this, Metamodel.getMClass(ActivityNode.class), TriggerType.AnyTrigger, "OwnerNode");
        
        // trigger=*, metaclass=ActivityNode, feature=OwnerPartition
        plan.registerChecker(this, Metamodel.getMClass(ActivityNode.class), TriggerType.AnyTrigger, "OwnerPartition");
        
        // trigger=*, metaclass=InputPin, feature=Inputing
        plan.registerChecker(this, Metamodel.getMClass(InputPin.class), TriggerType.AnyTrigger, "Inputing");
        
        // trigger=*, metaclass=OutputPin, feature=Outputing
        plan.registerChecker(this, Metamodel.getMClass(OutputPin.class), TriggerType.AnyTrigger, "Outputing");
        
        // trigger=*, metaclass=Clause, feature=Owner
        plan.registerChecker(this, Metamodel.getMClass(Clause.class), TriggerType.AnyTrigger, "Owner");
        
        // trigger=*, metaclass=ActivityEdge, feature=Source
        plan.registerChecker(this, Metamodel.getMClass(ActivityEdge.class), TriggerType.AnyTrigger, "Source");
        
        // trigger=*, metaclass=ActivityEdge, feature=Target
        plan.registerChecker(this, Metamodel.getMClass(ActivityEdge.class), TriggerType.AnyTrigger, "Target");
    }

}

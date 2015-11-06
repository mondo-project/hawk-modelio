package org.modelio.diagram.browser.model.core;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.DiagramSet;

@objid ("00338a1a-0d4f-10c6-842f-001ec947cd2a")
public class DiagramRef {
    @objid ("0033947e-0d4f-10c6-842f-001ec947cd2a")
     AbstractDiagram referencedDiagram;

    @objid ("0033ae0a-0d4f-10c6-842f-001ec947cd2a")
     DiagramSet referenceOwner;

    @objid ("0033c05c-0d4f-10c6-842f-001ec947cd2a")
    public DiagramRef(AbstractDiagram referencedDiagram, DiagramSet referenceOwner) {
        this.referencedDiagram = referencedDiagram;
        this.referenceOwner = referenceOwner;
    }

    @objid ("0033f16c-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.referenceOwner == null) ? 0 : this.referenceOwner.hashCode());
        result = prime * result + ((this.referencedDiagram == null) ? 0 : this.referencedDiagram.hashCode());
        return result;
    }

    @objid ("00341610-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DiagramRef)) {
            return false;
        }
        DiagramRef other = (DiagramRef) obj;
        if (this.referenceOwner == null) {
            if (other.referenceOwner != null) {
                return false;
            }
        } else if (!this.referenceOwner.equals(other.referenceOwner)) {
            return false;
        }
        if (this.referencedDiagram == null) {
            if (other.referencedDiagram != null) {
                return false;
            }
        } else if (!this.referencedDiagram.equals(other.referencedDiagram)) {
            return false;
        }
        return true;
    }

    @objid ("003440ae-0d4f-10c6-842f-001ec947cd2a")
    public AbstractDiagram getReferencedDiagram() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.referencedDiagram;
    }

    @objid ("003495cc-0d4f-10c6-842f-001ec947cd2a")
    public DiagramSet getReferenceOwner() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.referenceOwner;
    }

    @objid ("0034be80-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public String toString() {
        return "DiagramRef [referenceOwner=" + this.referenceOwner + ", referencedDiagram=" + this.referencedDiagram + "]";
    }

}

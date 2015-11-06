package org.modelio.api.impl.mc;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.mc.IModelComponentDescriptor;

@objid ("d496f21a-73cf-4add-8047-a8d7576cf2fe")
public class ModelComponentDescriptor implements IModelComponentDescriptor {
    @objid ("36bb8551-ab58-4d47-a42a-64ac617d9d6f")
    private String name = "";

    @objid ("9811473e-20b1-41de-9368-a2b6ec46df54")
    private String version = "";

    @objid ("b66881dd-1698-4096-8f31-08d3bc03115c")
    public ModelComponentDescriptor(final String name, final String version) {
        this.name = name;
        this.version = version;
    }

    @objid ("abf77703-4fca-4291-9a90-9959862c97c9")
    @Override
    public String getName() {
        return this.name;
    }

    @objid ("c4c82fa7-9527-42f1-8b42-890e5feea432")
    @Override
    public String getVersion() {
        return this.version;
    }

    @objid ("dc27b654-b85c-4e27-9425-ab801ab7673f")
    @Override
    public String toString() {
        return this.name + " " + this.version;
    }

    @objid ("09ab4c3a-7148-4d02-af02-a01f01109be0")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @objid ("e954d847-9f75-4680-81e9-0b7e38538c07")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModelComponentDescriptor other = (ModelComponentDescriptor) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (this.version == null) {
            if (other.version != null)
                return false;
        } else if (!this.version.equals(other.version))
            return false;
        return true;
    }

}

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
                                    

package org.modelio.vaudit.nsuse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.data.project.DefinitionScope;
import org.modelio.gproject.data.project.GProperties;
import org.modelio.vaudit.plugin.Vaudit;
import org.modelio.vcore.session.api.blob.BlobInfo;
import org.modelio.vcore.session.api.blob.IBlobInfo;
import org.modelio.vcore.session.api.repository.IRepository;

/**
 * Used to store the namespace uses repository state.
 * <ul>
 * <li> {@value #FRAGMENTS_KEY} : tells which fragments namespace uses were computed for.<br>
 * Stored a flag as a blob in the namespace use repository.
 * <li> {@value #INITIALIZED_KEY} : to test the namespace use repository has not been deleted.<br>
 * Stored as aGProject project property.
 * </ul>
 */
@objid ("9faa423a-f463-4242-a523-6179c7636545")
class NsUseState {
    @objid ("a5bfc872-ea06-4c4f-8ee0-60b3221623a7")
    private static final String FRAGMENTS_KEY = "namespaceuse.fragments";

    @objid ("cfda6f41-173c-4e87-af99-eb1bdb7b204f")
    private static final String INITIALIZED_KEY = "namespaceuse.initialized";

    @objid ("9cc6205d-460f-45b3-a31a-3c4290d2383b")
    private final GProperties projProps;

    @objid ("80110773-5aa6-4cb1-b79c-5836741006cc")
    private final IRepository nsUseRepo;

    @objid ("3586bea5-f932-4717-a00f-223cc2d53eb3")
    public NsUseState(IRepository nsUseRepo, GProperties projProps) {
        this.projProps = projProps;
        this.nsUseRepo = nsUseRepo;
    }

    @objid ("fb802aa3-0dfe-43b0-b92e-3880f5f2ad64")
    public boolean isInitialized() {
        try {
            IBlobInfo inf = this.nsUseRepo.readBlobInfo(INITIALIZED_KEY);
            return inf != null;
        } catch (IOException e) {
            Vaudit.LOG.warning(e);
            return false;
        }
    }

    @objid ("c83e2e71-082c-4a16-aecd-ed993f7e91ff")
    public void setInitialized() {
        try (OutputStream os = this.nsUseRepo.writeBlob(new BlobInfo(INITIALIZED_KEY, "Namespace uses initialized"));){
            os.write("done".getBytes());
            //
        } catch (IOException e) {
            Vaudit.LOG.warning(e);
        }
    }

    @objid ("ecfda989-f736-4133-acd0-baa4707aebcd")
    public Collection<String> getHandledFragments() {
        String fragmentsString = this.projProps.getValue(FRAGMENTS_KEY, "");
        if (fragmentsString == null || fragmentsString.isEmpty())
            return Collections.emptyList();
        else
            return new ArrayList<>(Arrays.asList(fragmentsString.split(";")));
    }

    @objid ("3fd9f238-ab11-4f02-9751-f73309e39f46")
    public void setHandledFragments(Collection<String> fragments) {
        StringBuilder s = new StringBuilder(fragments.size() * 10);
        
        for (String f : fragments) {
            if (s.length() > 0)
                s.append(";");
            s.append(f.replace(';', '_'));
        }
        
        this.projProps.setProperty(FRAGMENTS_KEY, s.toString(), DefinitionScope.LOCAL);
    }

}

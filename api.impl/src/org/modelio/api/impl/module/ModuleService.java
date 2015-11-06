package org.modelio.api.impl.module;

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IModuleService;
import org.modelio.api.module.IPeerModule;
import org.modelio.mda.infra.service.IModuleRegistry;

/**
 * Implementation of IModuleService, using the {@link IModuleRegistry} to get started modules.
 */
@objid ("37c5b60b-dc4e-412b-abae-2341a06f97cd")
public class ModuleService implements IModuleService {
    @objid ("c95932f2-7bda-42a3-8640-abb86c3f4e1e")
    private IModuleRegistry moduleRegistry;

    @objid ("94d42a01-22b9-4b8a-9398-614371e0823a")
    public ModuleService(IModuleRegistry moduleRegistry) {
        this.moduleRegistry = moduleRegistry;
    }

    @objid ("dd3df7a3-8e37-4693-868f-0e5bbb73b322")
    @Override
    public Collection<IPeerModule> getAllPeerModules() {
        Collection<IPeerModule> ret = new ArrayList<>();
        for (IModule module : this.moduleRegistry.getStartedModules()) {
            ret.add(module.getPeerModule());
        }
        return ret;
    }

    @objid ("6bdecc4e-6c7f-48ce-83e9-e6db0e07ab2a")
    @Override
    public <T extends IPeerModule> T getPeerModule(final Class<T> peerClass) {
        for (IModule module : this.moduleRegistry.getStartedModules()) {
            IPeerModule peerModule = module.getPeerModule();
            if (peerClass.isAssignableFrom(peerModule.getClass())) {
                return peerClass.cast(peerModule);
            }
        }
        return null;
    }

    @objid ("5fac7393-13a8-4db2-9283-d1d4fc78a07b")
    @Override
    public IPeerModule getPeerModule(String moduleName) {
        for (IModule module : this.moduleRegistry.getStartedModules()) {
            if (module.getName().equals(moduleName)) {
                return module.getPeerModule();
            }
        }
        return null;
    }

}

package org.modelio.api.impl.log;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.impl.plugin.ApiImpl;
import org.modelio.api.log.ILogService;
import org.modelio.api.module.IModule;

/**
 * Implementation of the log service.
 */
@objid ("68732128-9ada-47db-bbbc-056fa93ee96e")
public final class LogService implements ILogService {
    @objid ("8e8e70a9-ad92-4cc2-a227-3751ffa46247")
    private static final String API_PLUGIN_ID = "org.modelio.api";

    @objid ("29e7fe99-84b2-4a3e-9cfe-ad73665b842f")
    @Override
    public void info(final IModule module, final String msg) {
        ApiImpl.LOG.info(getModuleId(module));
        ApiImpl.LOG.info(msg);
    }

    @objid ("099227f4-b8f4-46fc-a217-8270da3de693")
    @Override
    public void warning(final IModule module, final String msg) {
        ApiImpl.LOG.warning(getModuleId(module));
        ApiImpl.LOG.warning(msg);
    }

    @objid ("c554cb8f-2bc4-4e81-a8d7-404d3908ddad")
    @Override
    public void error(final IModule module, final String msg) {
        ApiImpl.LOG.error(getModuleId(module));
        ApiImpl.LOG.error(msg);
    }

    @objid ("10a19340-b7fa-47d6-b6a8-0f51d0bc3668")
    @Override
    public void info(final IModule module, final Throwable t) {
        ApiImpl.LOG.info(getModuleId(module));
        ApiImpl.LOG.info(t);
    }

    @objid ("e8a77a88-88dd-4bf1-8021-86cb18dd1ea9")
    @Override
    public void warning(final IModule module, final Throwable t) {
        ApiImpl.LOG.warning(getModuleId(module));
        ApiImpl.LOG.warning(t);
    }

    @objid ("08a48389-2eb0-4767-bb64-80fbbb578b0a")
    @Override
    public void error(final IModule module, final Throwable t) {
        ApiImpl.LOG.error(getModuleId(module));
        ApiImpl.LOG.error(t);
    }

    /**
     * Get an identifier for the module.
     * <p>
     * Returns in this preference order: <ul>
     * <li> the module name
     * <li> the module identifier (the module model is not available)
     * <li> {@value #API_PLUGIN_ID} (there is no module)
     * <li>
     * @param module a module
     * @return an identifier for the module
     */
    @objid ("a2f9dac6-b411-4dfb-b0ab-852bf4d22e59")
    private static String getModuleId(final IModule module) {
        if (module == null ) {
            return API_PLUGIN_ID;
        } else {
            return module.getName();
        }
    }

}

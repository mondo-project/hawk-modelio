package org.modelio.api.impl.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.Modelio;
import org.modelio.script.engine.core.engine.IClassLoaderProvider;

/**
 * Class providing a classloader to the script.engine plugin to make the api plugin accessible with jython.
 * <p>
 * The api plugin can't depend on script.engine, so we have to implement this class here instead.
 * </p>
 * <p>
 * See also plugin.xml for extension point declaration.
 * </p>
 */
@objid ("32788b97-4c1a-4dc5-b46a-aaa630407837")
public class JythonClassLoaderProvider implements IClassLoaderProvider {
    @objid ("64e3c54b-0ffa-4289-9a35-c212ed2179c6")
    @Override
    public ClassLoader getClassLoader() {
        return Modelio.class.getClassLoader();
    }

}

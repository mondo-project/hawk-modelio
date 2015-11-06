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
                                    

package org.modelio.mda.infra.service.dynamic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.diagram.IDiagramCustomizer;
import org.modelio.api.diagram.tools.IAttachedBoxCommand;
import org.modelio.api.diagram.tools.IBoxCommand;
import org.modelio.api.diagram.tools.ILinkCommand;
import org.modelio.api.diagram.tools.IMultiLinkCommand;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModule;
import org.modelio.api.module.commands.ActionLocation;
import org.modelio.api.module.commands.DefaultModuleAction;
import org.modelio.api.module.commands.IModuleAction;
import org.modelio.api.module.commands.IModuleContextualCommand;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Command;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Handler.Jxbv2HParameter;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Handler;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Diagrams.Jxbv2DiagramType.Jxbv2Wizard;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Module.Jxbv2Gui.Jxbv2Views.Jxbv2PropertyPage;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Scope;
import org.modelio.gproject.data.module.jaxbv2.Jxbv2Tool;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.factory.ElementNotUniqueException;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * Helper class handling creation of commands, diagram tools & diagram customizers and registering them on a given module.
 */
@objid ("796e7728-1279-41e4-a421-d9e6575b5833")
public class DynamicGuiCreationHelper {
    @objid ("0dea8c47-8be8-413b-bf54-524e99986968")
    public DynamicGuiCreationHelper(final File bundleDir) {
    }

    @objid ("a92fd10d-786d-4320-93ab-4d900fbe9e2b")
    public IModulePropertyPage createPropertyPage(final Jxbv2PropertyPage property, final IModule module) throws IOException {
        try {
            ClassLoader loader = module.getSession().getClass().getClassLoader();
            Class<?> commandClass = loader.loadClass(property.getClazz());
            String name = module.getLabel(property.getId());
            String label = module.getLabel(property.getLabel());
            String image = property.getImage();
        
            Constructor<?> ctor = commandClass.getConstructor(IModule.class, String.class, String.class, String.class);
            IModulePropertyPage propertypage = (IModulePropertyPage) ctor.newInstance(module, name, label, image);
        
            return propertypage;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IOException(MdaInfra.I18N.getMessage("L43_class_not_found", property.getClazz()));
        } catch (ClassCastException e) {
            throw new IOException(MdaInfra.I18N.getMessage("L45_class_cast_exception", property.getClazz()));
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("4dfdf10a-75db-473e-9ca1-6e3b20543a68")
    public void registerDiagramBoxCommand(final Jxbv2Tool command, final IModule module) throws IOException {
        try {
            String name = module.getLabel(command.getId());
        
            List<GenericScope> scopes = new ArrayList<>();
            for (Jxbv2Scope scope : command.getScopeTarget()) {
                scopes.add(new GenericScope(scope.getMetaclass(), scope.getStereotype()));
            }
        
            // Generic handler for diagrams
            GenericHandler handler = createGenericHandler(command.getHandler());
        
            if (scopes.isEmpty()) {
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_scope", command.getId()));
            }
        
            String label = module.getLabel(command.getLabel());
            String tooltip = module.getLabel(command.getTooltip());
        
            ImageDescriptor descriptor = null;
            if (command.getImage() != null && !command.getImage().equals("")) {
                String bitmap = module.getConfiguration().getModuleResourcesPath() + "/" + module.getLabel(command.getImage());
                descriptor = ImageDescriptor.createFromImage(new Image(new Shell().getDisplay(), bitmap));
            }
        
            String relation = handler.getRelation();
            MClass metaclass = null;
            if (handler.getMetaclass() != null)
                metaclass = Metamodel.getMClass(handler.getMetaclass());
        
            if (metaclass == null)
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_metaclass", command.getId()));
        
            Stereotype stereotypeClass = null;
            if (handler.getStereotype() != null) {
                stereotypeClass = getStereotype(metaclass, handler.getStereotype());
            }
        
            GenericBoxCommand commandInstance = new GenericBoxCommand(label, descriptor, tooltip, handler, scopes);
        
            module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, commandInstance);
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("177bb9e6-20b3-4f68-8160-65c4032e16eb")
    public void registerDiagramAttachedBoxCommand(final Jxbv2Tool tool, final IModule module) throws IOException {
        try {
            String name = module.getLabel(tool.getId());
            String label = module.getLabel(tool.getLabel());
            String tooltype = module.getLabel(tool.getTooltip());
        
        
            List<GenericScope> scopes = new ArrayList<>();
            for (Jxbv2Scope scope : tool.getScopeTarget()) {
                scopes.add(new GenericScope(scope.getMetaclass(), scope.getStereotype()));
            }
            if (scopes.isEmpty()) {
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_scope", tool.getId()));
            }
        
            // Generic handler for diagrams
            GenericHandler handler = createGenericHandler(tool.getHandler());
        
            ImageDescriptor descriptor = null;
            if (tool.getImage() != null && !tool.getImage().equals("")) {
                String bitmap = module.getConfiguration().getModuleResourcesPath() + "/" + module.getLabel(tool.getImage());
                descriptor = ImageDescriptor.createFromImage(new Image(new Shell().getDisplay(), bitmap));
            }
        
            String relation = handler.getRelation();
        
            MClass metaclass = null;
            if (handler.getMetaclass() != null)
                metaclass = Metamodel.getMClass(handler.getMetaclass());
            if (metaclass == null)
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_metaclass", tool.getId()));
        
            Stereotype stereotypeClass = null;
            if (handler.getStereotype() != null) {
                stereotypeClass = getStereotype(metaclass, handler.getStereotype());
            }
        
            IAttachedBoxCommand commandInstance = new GenericAttachedBoxCommand(label, descriptor, tooltype, handler, scopes);
            module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, commandInstance);
        
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("3b3a1a80-c2e9-4803-8775-dfeac389545d")
    public void registerDiagramLinkCommand(final Jxbv2Tool command, final IModule module) throws IOException {
        String stereotype = "";
        try {
            String name = module.getLabel(command.getId());
        
            // Generic handler for diagrams
            GenericHandler handler = createGenericHandler(command.getHandler());
        
            List<GenericScope> sources = new ArrayList<>();
            if (command.getScopeSource() != null) {
                for (Jxbv2Scope jxbScope : command.getScopeSource()) {
                    sources.add(new GenericScope(jxbScope.getMetaclass(), jxbScope.getStereotype()));
                }
            }
            List<GenericScope> target = new ArrayList<>();
            if (command.getScopeTarget() != null) {
                for (Jxbv2Scope jxbScope : command.getScopeTarget()) {
                    target.add(new GenericScope(jxbScope.getMetaclass(), jxbScope.getStereotype()));
                }
            }
        
            if (sources.isEmpty() || target.isEmpty()) {
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_scope", command.getId()));
            }
        
            ImageDescriptor descriptor = null;
            String label = module.getLabel(command.getLabel());
        
            String tooltype = module.getLabel(command.getTooltip());
            if (command.getImage() != null && !command.getImage().equals("")) {
                String bitmap = module.getConfiguration().getModuleResourcesPath() + "/" + module.getLabel(command.getImage());
                descriptor = ImageDescriptor.createFromImage(new Image(new Shell().getDisplay(), bitmap));
            }
        
            stereotype = handler.getStereotype();
            String relation = handler.getRelation();
        
            MClass metaclass = null;
            if (handler.getMetaclass() != null) {
                metaclass = Metamodel.getMClass(handler.getMetaclass());
            } else {
                metaclass = Metamodel.getMClass("Element");
            }
        
            Stereotype stereotypeClass = null;
            if (stereotype != null && metaclass != null) {
                stereotypeClass = getStereotype(metaclass, stereotype);
            }
        
            ILinkCommand commandInstance = new GenericLinkCommand(label, descriptor, tooltype, handler, sources, target);
            module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, commandInstance);
        
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("60b90e31-e4b4-4695-967c-4b883761c810")
    private Stereotype getStereotype(MClass metaclass, String stereotype) {
        return Modelio.getInstance().getModelingSession().getMetamodelExtensions().getStereotype(stereotype, metaclass);
    }

    @objid ("c814225a-6b18-4d31-853f-ac230f6f8084")
    public GenericDiagramCustomizer createDiagram(final IModule module) {
        return new GenericDiagramCustomizer(module);
    }

    @objid ("eae5e01d-2645-4b3c-aa86-6391b5775bc4")
    @SuppressWarnings("unchecked")
    public void registerDiagram(IDiagramCustomizer customizer, IModule module, String baseDiagram, String stereotype) {
        MClass baseDiagramClass = null;
        if (baseDiagram != null) {
            baseDiagramClass = Metamodel.getMClass(baseDiagram);
        } else {
            baseDiagramClass = Metamodel.getMClass(AbstractDiagram.class);
        }
        
        Stereotype stereotypeClass = null;
        if (stereotype != null) {
            stereotypeClass = getStereotype(baseDiagramClass, stereotype);
        }
        
        module.registerDiagramCustomization(stereotypeClass,
                (Class<? extends AbstractDiagram>) Metamodel.getJavaInterface(baseDiagramClass), customizer);
    }

    /**
     * @param loadedHandler @return
     */
    @objid ("7fb37eb4-b211-4210-ace5-0ac359ef7cc7")
    private GenericHandler createGenericHandler(final Jxbv2Handler loadedHandler) {
        String metaclass = null;
        String stereotype = null;
        String relation = null;
        for (Jxbv2HParameter param : loadedHandler.getHParameter()) {
            switch (param.getName()) {
            case "metaclass":
                metaclass = param.getValue();
                break;
            case "stereotype":
                stereotype = param.getValue();
                break;
            case "relation":
                relation = param.getValue();
                break;
            default:
                continue;
            }
        }
        GenericHandler handler = new GenericHandler(metaclass, relation, stereotype);
        return handler;
    }

    @objid ("57e7ccdc-779a-44f5-b155-7404873430e6")
    public IModuleAction createCommand(Jxbv2Command cmd, IModule module, IMModelServices mModelServices) throws IOException {
        // Create the handler
        IModuleContextualCommand handler = createHandler(cmd, module);
        
        // Create the module action
        // Instantiate a module action wrapping this handler so it can be used with the platform mechanisms.
        String name = module.getLabel(cmd.getId());
        String label = module.getLabel(cmd.getLabel());
        String bitmap = module.getLabel(cmd.getImage());
        String tooltype = module.getLabel(cmd.getTooltip());
        String group = module.getLabel(cmd.getGroup());
        String imageGroup = module.getLabel(cmd.getGroupImage());
        boolean editionMode = cmd.getModifyModel() == null && cmd.getModifyModel().equals("true");
        
        IModuleAction action = new DefaultModuleAction(module, name, label, tooltype, bitmap, group, imageGroup, editionMode,
                editionMode, handler);
        
        // Define for which metaclass/stereotype this command should be available.
        for (Jxbv2Scope scope : cmd.getScope()) {
            if (scope.getStereotype() != null && scope.getMetaclass() != null) {
                SmClass metaclass = SmClass.getClass(scope.getMetaclass());
                if (metaclass == null)
                    throw new IOException(MdaInfra.I18N.getMessage("L44_metaclass_not_found", scope.getMetaclass()));
        
                Class<? extends MObject> javaClass = SmClass.getClass(scope.getMetaclass()).getJavaInterface();
                try {
                    Stereotype stereotype = mModelServices.getStereotype(scope.getStereotype(), metaclass);
                    if (stereotype == null)
                        throw new IOException(MdaInfra.I18N.getMessage("L44_stereotype_not_found", scope.getStereotype()));
        
                    action.addAllowedStereotype(stereotype);
                    action.addAllowedMetaclass(javaClass);
                } catch (ElementNotUniqueException e) {
                    throw new IOException(MdaInfra.I18N.getMessage("L44_stereotype_not_found", scope.getStereotype()));
                }
            } else if (scope.getMetaclass() != null) {
                Class<? extends MObject> metaclass = SmClass.getClass(scope.getMetaclass()).getJavaInterface();
                if (metaclass == null)
                    throw new IOException(MdaInfra.I18N.getMessage("L44_metaclass_not_found", scope.getMetaclass()));
                action.addAllowedMetaclass(metaclass);
            }
        }
        return action;
    }

    /**
     * @param cmd
     * @throws IOException
     * @param module @return
     */
    @objid ("93c86c7c-058d-41ed-89db-4ef5d868c672")
    private IModuleContextualCommand createHandler(Jxbv2Command cmd, IModule module) throws IOException {
        assert (cmd != null);
        assert (cmd.getHandler() != null);
        
        switch (cmd.getHandler().getClazz()) {
        case "GenericElementCreationHandler":
            return createElementCreationHandler(cmd, module);
        case "GenericDiagramCreationHandler":
            return createDiagramCreationHandler(cmd, module);
        default:
            return createCustomHandler(cmd, module);
        }
    }

    /**
     * @param cmd
     * @throws IOException
     * @param module @return
     */
    @objid ("1c6eaefa-4eb7-4ed1-9a5d-1846b4915c9b")
    private IModuleContextualCommand createCustomHandler(Jxbv2Command cmd, IModule module) throws IOException {
        IModuleContextualCommand handler;
        try {
            // Load and instantiate the handler class in the same class loader as the module.
            ClassLoader loader = module.getClass().getClassLoader();
            Class<?> handlerClass = loader.loadClass(cmd.getHandler().getClazz());
            handler = (IModuleContextualCommand) handlerClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IOException(MdaInfra.I18N.getMessage("L43_class_not_found", cmd.getHandler().getClazz()));
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
        return handler;
    }

    /**
     * @param cmd
     * @throws IOException
     * @param module @return
     */
    @objid ("bc735f9f-aa0a-495c-8edd-8574e6abb7f7")
    private IModuleContextualCommand createDiagramCreationHandler(Jxbv2Command cmd, IModule module) throws IOException {
        // Generic diagram creation command, collect params
        String metaclass = null;
        String stereotype = null;
        String relation = null;
        String style = null;
        boolean open = false;
        for (Jxbv2HParameter param : cmd.getHandler().getHParameter()) {
            switch (param.getName()) {
            case "metaclass":
                metaclass = param.getValue();
                break;
            case "stereotype":
                stereotype = param.getValue();
                break;
            case "relation":
                relation = param.getValue();
                break;
            case "style":
                style = param.getValue();
                break;
            case "open":
                open = "true".equals(param.getValue());
                break;
            default:
                continue;
            }
        }
        // Instantiate generic handler
        return new GenericDiagramCreationCommand(module.getLabel(cmd.getId()), metaclass, stereotype, relation, style, open);
    }

    /**
     * @param cmd
     * @throws IOException
     * @param module @return
     */
    @objid ("727f98cb-9b5e-4171-aa18-3eda0b30778f")
    private IModuleContextualCommand createElementCreationHandler(Jxbv2Command cmd, IModule module) throws IOException {
        // Generic element creation command, collect params
        String metaclass = null;
        String stereotype = null;
        String relation = null;
        for (Jxbv2HParameter param : cmd.getHandler().getHParameter()) {
            switch (param.getName()) {
            case "metaclass":
                metaclass = param.getValue();
                break;
            case "stereotype":
                stereotype = param.getValue();
                break;
            case "relation":
                relation = param.getValue();
                break;
            default:
                continue;
            }
        }
        // Instantiate generic handler
        return new GenericElementCreationCommand(module.getLabel(cmd.getId()), metaclass, stereotype, relation);
    }

    @objid ("732911af-073c-4566-8cae-747714c38ca9")
    public void registerContextualMenuCommand(IModule module, IModuleAction action) {
        module.registerAction(ActionLocation.contextualpopup, action);
    }

    @objid ("fe414990-d269-47c8-8db1-882257cd2a01")
    public void registerPropertyPageCommand(IModule module, IModuleAction action) {
        module.registerAction(ActionLocation.property, action);
    }

    @objid ("6364aef6-0b89-4d91-accf-987406e5094d")
    public void registerCustomCommand(Jxbv2Tool command, IModule module) throws IOException {
        try {
            String name = module.getLabel(command.getId());
        
            List<GenericScope> scopes = new ArrayList<>();
            for (Jxbv2Scope scope : command.getScopeTarget()) {
                scopes.add(new GenericScope(scope.getMetaclass(), scope.getStereotype()));
            }
        
            MClass metaclass = null;
            String stereotype = null;
            String relation = null;
            for (Jxbv2HParameter param : command.getHandler().getHParameter()) {
                switch (param.getName()) {
                case "metaclass":
                    metaclass = Metamodel.getMClass(param.getValue());
                    break;
                case "stereotype":
                    stereotype = param.getValue();
                    break;
                case "relation":
                    relation = param.getValue();
                    break;
                default:
                    continue;
                }
            }
        
            if (metaclass == null)
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_metaclass", command.getId()));
        
            Stereotype stereotypeClass = null;
            if (stereotype != null) {
                stereotypeClass = getStereotype(metaclass, stereotype);
            }
        
            // Load and instantiate the handler class in the same class loader as the module.
            ClassLoader loader = module.getClass().getClassLoader();
            Class<?> handlerClass = loader.loadClass(command.getHandler().getClazz());
            Object commandInstance = handlerClass.newInstance();
        
            if (commandInstance instanceof IBoxCommand) {
                module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, (IBoxCommand) commandInstance);
            } else if (commandInstance instanceof ILinkCommand) {
                module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, (ILinkCommand) commandInstance);
            } else if (commandInstance instanceof IAttachedBoxCommand) {
                module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, (IAttachedBoxCommand) commandInstance);
            } else if (commandInstance instanceof IMultiLinkCommand) {
                module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, (IMultiLinkCommand) commandInstance);
            } else {
                throw new IOException(MdaInfra.I18N.getMessage("L43_class_not_found", command.getHandler().getClazz()));
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IOException(MdaInfra.I18N.getMessage("L43_class_not_found", command.getHandler().getClazz()));
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("afad31c9-15f9-4836-8ac3-d0e385004edf")
    public void registerDiagramMultiLinkCommand(final Jxbv2Tool command, final IModule module) throws IOException {
        String stereotype = "";
        try {
            String name = module.getLabel(command.getId());
        
            // Generic handler for diagrams
            GenericHandler handler = createGenericHandler(command.getHandler());
        
            List<GenericScope> sources = new ArrayList<>();
            if (command.getScopeSource() != null) {
                for (Jxbv2Scope jxbScope : command.getScopeSource()) {
                    sources.add(new GenericScope(jxbScope.getMetaclass(), jxbScope.getStereotype()));
                }
            }
        
            if (sources.isEmpty()) {
                throw new IOException(MdaInfra.I18N.getMessage("L43_invalid_tool_scope", command.getId()));
            }
        
            ImageDescriptor descriptor = null;
            String label = module.getLabel(command.getLabel());
        
            String tooltype = module.getLabel(command.getTooltip());
            if (command.getImage() != null && !command.getImage().equals("")) {
                String bitmap = module.getConfiguration().getModuleResourcesPath() + "/" + module.getLabel(command.getImage());
                descriptor = ImageDescriptor.createFromImage(new Image(new Shell().getDisplay(), bitmap));
            }
        
            stereotype = handler.getStereotype();
            String relation = handler.getRelation();
        
            MClass metaclass = null;
            if (handler.getMetaclass() != null) {
                metaclass = Metamodel.getMClass(handler.getMetaclass());
            } else {
                metaclass = Metamodel.getMClass("Element");
            }
        
            Stereotype stereotypeClass = null;
            if (stereotype != null && metaclass != null) {
                stereotypeClass = getStereotype(metaclass, stereotype);
            }
        
            IMultiLinkCommand commandInstance = new GenericMultiLinkCommand(label, descriptor, tooltype, handler, sources);
            module.registerCustomizedTool(name, Metamodel.getJavaInterface(metaclass), stereotypeClass, relation, commandInstance);
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    @objid ("b7393195-7aa1-42fe-b99a-652401798060")
    public void registerWizard(Jxbv2Wizard wizard, IModule module, IMModelServices mModelServices) throws IOException {
        final String details     = module.getLabel(wizard.getDetails());
        final String helpUrl     = wizard.getHelpUrl();
        final String information = module.getLabel(wizard.getInformation());
        final String label       = module.getLabel(wizard.getLabel());
        
        final ImageDescriptor iconDescriptor;
        if (wizard.getIcon() != null && !wizard.getIcon().equals("")) {
            String bitmap = module.getConfiguration().getModuleResourcesPath() + "/" + module.getLabel(wizard.getIcon());
            iconDescriptor = ImageDescriptor.createFromImage(new Image(new Shell().getDisplay(), bitmap));
        } else {
            iconDescriptor = null;
        }
        
        final List<Jxbv2Scope> scopes = wizard.getScope();
        final List<MClass> allowedMetaclasses = new ArrayList<>();
        final List<Stereotype> allowedStereotypes = new ArrayList<>();
        for (Jxbv2Scope scope : scopes) {
            MClass metaclass = Metamodel.getMClass(scope.getMetaclass());
            Stereotype stereotype = null;
            try {
                if (scope.getStereotype() != null) {
                    stereotype = mModelServices.getStereotype(scope.getStereotype(), metaclass);
                    if (stereotype == null) {
                        throw new IOException(MdaInfra.I18N.getMessage("L44_stereotype_not_found", scope.getStereotype()));
                    } else {
                        allowedStereotypes.add(stereotype);
                    }
                }
            } catch (ElementNotUniqueException e) {
                throw new IOException(MdaInfra.I18N.getMessage("L44_stereotype_not_found", scope.getStereotype()));
            }
        
            allowedMetaclasses.add(metaclass);
        }
        
        final Jxbv2Handler handler = wizard.getHandler();
        if (handler != null) {
            // Collect params
            MClass metaclass = Metamodel.getMClass(StaticDiagram.class);
            Stereotype stereotypeClass = null;
            String style = null;
            for (Jxbv2HParameter param : handler.getHParameter()) {
                switch (param.getName()) {
                case "metaclass":
                    metaclass = Metamodel.getMClass(param.getValue());
                    break;
                case "stereotype":
                    stereotypeClass = getStereotype(metaclass, param.getValue());
                    break;
                case "style":
                    style = param.getValue();
                    break;
                default:
                    continue;
                }
            }
        
            // Create handler
            IDiagramWizardContributor contributor;
            switch (handler.getClazz()) {
            case "GenericWizardContributor":
                contributor = new GenericWizardContributor(metaclass, stereotypeClass, style, label, helpUrl, information, iconDescriptor, details, allowedMetaclasses, allowedStereotypes);
                break;
            default:
                contributor = createCustomWizardContributor(handler, module);
                break;
            }
        
            module.registerDiagramWizardContribution(new ContributorCategory(module.getLabel(), module.getLabel(), module.getModuleImage()), contributor);
        }
    }

    @objid ("c157d1e0-ea4b-4c40-9b21-b7a449724199")
    private IDiagramWizardContributor createCustomWizardContributor(Jxbv2Handler handler, IModule module) throws IOException {
        try {
            // Load and instantiate the handler class in the same class loader as the module.
            ClassLoader loader = module.getClass().getClassLoader();
            Class<?> handlerClass = loader.loadClass(handler.getClazz());
            return (IDiagramWizardContributor) handlerClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IOException(MdaInfra.I18N.getMessage("L43_class_not_found", handler.getClazz()));
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

}

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
                                    

package org.modelio.module.propertytab.propertytab;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.commands.ExpressionContext;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.ui.MCoreExpression;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUiFactory;
import org.eclipse.e4.ui.model.application.ui.advanced.MAdvancedFactory;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.commands.ActionLocation;
import org.modelio.api.module.commands.IModuleAction;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.module.commands.ExecuteModuleActionHandler;
import org.modelio.module.commands.IsVisibleExpression;
import org.modelio.module.commands.ModuleCommandsRegistry;
import org.osgi.framework.FrameworkUtil;

@objid ("c885dc10-1eba-11e2-9382-bc305ba4815c")
public class ModulePropertyViewHandler {
    @objid ("044f0911-1ebb-11e2-9382-bc305ba4815c")
    private static final String URI_SEPARATOR = "/";

    @objid ("044f3022-1ebb-11e2-9382-bc305ba4815c")
    private static final String PLATFORM_PREFIX = "platform:/plugin/";

    @objid ("044f5733-1ebb-11e2-9382-bc305ba4815c")
    private static final String BUNDLE_PREFIX = "bundleclass://";

    @objid ("044f7e43-1ebb-11e2-9382-bc305ba4815c")
    private static final String DYNAMIC_MODULE_VIEW_TAG = "View brought by a module";

    @objid ("044fa553-1ebb-11e2-9382-bc305ba4815c")
    private static final String MODULE_TOOLBAR_CONTRIBUTION = "module property page toolbar contribution";

    @objid ("f3c84d5c-b2f3-4250-b083-825756b94633")
    private static final String MODULE_STACK_TAG = "MODULE_STACK";

    @objid ("c8862a33-1eba-11e2-9382-bc305ba4815c")
    @Execute
    static void execute(IEclipseContext context) {
        // Create instance and put it in the context.
        context.set(ModulePropertyViewHandler.class, ContextInjectionFactory.make(ModulePropertyViewHandler.class, context));
    }

    @objid ("c8865142-1eba-11e2-9382-bc305ba4815c")
    @Inject
    @Optional
    void onModuleStarted(@EventTopic(ModelioEventTopics.MODULE_STARTED) final IModule module, final MApplication application, final EModelService modelService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                for (IModulePropertyPage propertyPage : module.getPropertyPages()) {
                    MPart part = MBasicFactory.INSTANCE.createPart();
                    part.setElementId(module + "_" + propertyPage.getName());
                    part.setContributorURI(PLATFORM_PREFIX + FrameworkUtil.getBundle(ModulePropertyViewHandler.class).getSymbolicName());
                    part.setContributionURI(BUNDLE_PREFIX + FrameworkUtil.getBundle(ModulePropertyView.class).getSymbolicName() + URI_SEPARATOR + ModulePropertyView.class.getName());
        
                    part.getTags().add(DYNAMIC_MODULE_VIEW_TAG);
                    part.getTags().add(module.getName());
                    part.getTags().add(propertyPage.getName());
        
                    part.setLabel(propertyPage.getLabel());
                    part.setIconURI(getModuleImagePath(module));
        
                    // Insert the module actions with ActionLocation.propertypage
                    List<IModuleAction> actions = module.getActions(ActionLocation.property);
                    if (actions != null && !actions.isEmpty()) {
                        MToolBar toolbar = MMenuFactory.INSTANCE.createRenderedToolBar();
                        toolbar.setVisible(true);
                        toolbar.setToBeRendered(true);
                        for (IModuleAction action : actions) {
                            // MCommand
                            MCommand command = ModuleCommandsRegistry.getCommand(module, action);
                            // MHandler
                            final MHandler handler = createAndActivateHandler(part, command, module, action);
        
                            // MHandledItem
                            MHandledToolItem item = createAndInsertItem(toolbar, action);
                            // Bind to command
                            item.setCommand(command);
        
                            Expression visWhen = new IsVisibleExpression(handler.getObject(), item);
                            MCoreExpression isVisibleWhenExpression = MUiFactory.INSTANCE.createCoreExpression();
                            isVisibleWhenExpression.setCoreExpressionId("programmatic.value");
                            isVisibleWhenExpression.setCoreExpression(visWhen);
        
                            item.setVisibleWhen(isVisibleWhenExpression);
                        }
                        part.setToolbar(toolbar);
                    }
        
                    // Add the shared part to the window
                    List<MTrimmedWindow> trimmedWindow = modelService.findElements(application, "org.modelio.app.ui.trimmed", MTrimmedWindow.class, null);
                    trimmedWindow.get(0).getSharedElements().add(part);
                    
                    // Add a placeholder where the view needs to be added in the different perspectives
                    List<MPerspectiveStack> perspectiveStacks = modelService.findElements(application, "org.modelio.app.ui.stack.perspectives", MPerspectiveStack.class, null);
                    List<MPerspective> perspectives = modelService.findElements(perspectiveStacks.get(0), null, MPerspective.class, null);
                    for (MPerspective perspective : perspectives) {
                        List<String> tagsToMatch = new ArrayList<>();
                        tagsToMatch.add(MODULE_STACK_TAG);
                        List<MPartStack> partStack = modelService.findElements(perspective, null, MPartStack.class, tagsToMatch);
                        if (partStack.size() > 0) {
                            final MPartStack mPartStack = partStack.get(0);
                            
                            final MPlaceholder placeholder = MAdvancedFactory.INSTANCE.createPlaceholder();
                            placeholder.setRef(part);
                            
                            mPartStack.getChildren().add(placeholder);
                            mPartStack.setSelectedElement(placeholder);
                        }
                    }
                }
            }
        });
    }

    @objid ("c8867855-1eba-11e2-9382-bc305ba4815c")
    @Inject
    @Optional
    void onModuleStopped(@EventTopic(ModelioEventTopics.MODULE_STOPPED) final IModule module, final MApplication application, final EModelService modelService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                // Add the shared part to the window
                List<MTrimmedWindow> trimmedWindow = modelService.findElements(application, "org.modelio.app.ui.trimmed", MTrimmedWindow.class, null);
                
                final List<MUIElement> sharedElements = trimmedWindow.get(0).getSharedElements();
                for (MUIElement element : new ArrayList<>(sharedElements)) {
                    List<String> tags = element.getTags();
                    if (tags.contains(DYNAMIC_MODULE_VIEW_TAG) && tags.contains(module.getName())) {
                        final MPart mPart = (MPart)element;
                        element.setToBeRendered(false);
                        sharedElements.remove(mPart);
                        mPart.setObject(null);
                        
                        for (MPlaceholder placeholder : modelService.findElements(application, null, MPlaceholder.class, null)) {
                            if (mPart.equals(placeholder.getRef())) {
                                placeholder.setParent(null);
                                placeholder.setRenderer(false);
                            }
                        }
                    }
                }
                
                
            }
        });
    }

    @objid ("c886c673-1eba-11e2-9382-bc305ba4815c")
    protected static MHandler createAndActivateHandler(MPart part, MCommand command, IModule module, IModuleAction action) {
        // Instantiate the actual handler class
        Object handler = new ExecuteModuleActionHandler(module, action);
        // Fit it into a MHandler
        MHandler mHandler = MCommandsFactory.INSTANCE.createHandler();
        mHandler.setObject(handler);
        // Bind it to the corresponding command
        mHandler.setCommand(command);
        // Define scope of this handler as the browser view.
        part.getHandlers().add(mHandler);
        
        // If the part already have a context, it means the e4 model has already been read.
        // In this case, activate the handler "by hand" since this part of the model may not be read again.
        // Otherwise, the activation will be done automatically when the model is read.
        IEclipseContext context = part.getContext();
        if (context != null) {
            EHandlerService handlerService = context.get(EHandlerService.class);
            handlerService.activateHandler(command.getElementId(), handler);
        }
        return mHandler;
    }

    @objid ("c886ed84-1eba-11e2-9382-bc305ba4815c")
    protected static MHandledToolItem createAndInsertItem(MToolBar toolbar, IModuleAction action) {
        Path bitmapPath = action.getBitmapPath();
        String iconURI = bitmapPath != null ? bitmapPath.toUri().toString() : "";
        MHandledToolItem item = createToolBarElement(action.getLabel(), action.getTooltip(), iconURI);
        toolbar.getChildren().add(item);
        return item;
    }

    @objid ("c8871493-1eba-11e2-9382-bc305ba4815c")
    private static MHandledToolItem createToolBarElement(String label, String tooltip, String iconURI) {
        // create a new item
        MHandledToolItem item = MMenuFactory.INSTANCE.createHandledToolItem();
        // item.setElementId(module.getName() + commandId);
        item.setLabel(label);
        item.setTooltip(tooltip);
        item.setIconURI(iconURI);
        item.setEnabled(true);
        item.setToBeRendered(true);
        item.setVisible(true);
        item.getTags().add(MODULE_TOOLBAR_CONTRIBUTION);
        return item;
    }

    /**
     * This method is used to handle visibility of the tool items (which <em>should</em> be handled by the visibleWhen MExpression
     * but this doesn't work in current version of e4).
     */
    @objid ("c8873ba4-1eba-11e2-9382-bc305ba4815c")
    @SuppressWarnings({ "static-method", "unused" })
    @Optional
    @Inject
    void selectionChanged(@Named(IServiceConstants.ACTIVE_SELECTION) final ISelection selection, EModelService modelService, MApplication application, IEclipseContext context) {
        List<String> tagsToMatch = new ArrayList<>();
        tagsToMatch.add(DYNAMIC_MODULE_VIEW_TAG);
        List<MPart> mParts = modelService.findElements(application, null, MPart.class, tagsToMatch);
        tagsToMatch.remove(DYNAMIC_MODULE_VIEW_TAG);
        tagsToMatch.add(MODULE_TOOLBAR_CONTRIBUTION);
        
        for (MPart mPart : mParts) {
            final MToolBar toolbar = mPart.getToolbar();
            if (toolbar != null) {
                List<MHandledToolItem> toolItems = modelService.findElements(toolbar, null, MHandledToolItem.class, tagsToMatch);
        
                final ExpressionContext expressionContext = new ExpressionContext(context);
        
                for (MHandledToolItem toolItem : toolItems) {
                    IsVisibleExpression expression = (IsVisibleExpression) (((MCoreExpression) toolItem.getVisibleWhen())
                            .getCoreExpression());
                    if (expression != null) {
                        // Creates dependency on a predefined value that can be "poked" by the evaluation service
                        ExpressionInfo info = expression.computeExpressionInfo();
                        String[] names = info.getAccessedPropertyNames();
                        for (String name : names) {
                            expressionContext.getVariable(name + ".evaluationServiceLink"); //$NON-NLS-1$
                        } 
                        boolean visible = (expression.evaluate(expressionContext) != EvaluationResult.FALSE);
                        if (visible != toolItem.isVisible()) {
                            toolItem.setVisible(visible);
                        }
                    }
                }
            }
        }
    }

    @objid ("c88789c5-1eba-11e2-9382-bc305ba4815c")
    protected static String getModuleImagePath(IModule module) {
        String relativePath = module.getModuleImagePath();
        if (relativePath != null && !relativePath.isEmpty()) {
            final Path moduleDirectory = module.getConfiguration().getModuleResourcesPath();
            Path imageFile = moduleDirectory.resolve(relativePath.substring(1));      
            if (Files.isRegularFile(imageFile)) {
                return imageFile.toUri().toString();
            }
        }
        return null;
    }

}

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
                                    

package org.modelio.app.ui.persp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.modelio.app.ui.plugin.AppUi;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@objid ("4db4fdd8-3a8f-4794-99e6-eea50769bdc6")
public class PerspectiveSwitcher {
    @objid ("a6605b37-b873-4011-b716-0a62f2e0699f")
    public static final String PERSPECTIVE_SWITCHER_ID = "org.eclipse.e4.ui.PerspectiveSwitcher"; // $NON-NLS-1$

    @objid ("f5b473ac-8991-4a17-bfd4-10185d64ce68")
    private ToolBar psTB;

    @objid ("49932a9b-3a11-414a-a735-c0d9ce28880c")
    private Composite comp;

    @objid ("4806af49-854e-4bae-aa75-c6d418eac4ef")
    private Image backgroundImage;

    @objid ("0785a402-d3b1-4bcc-8289-f58746c67024")
    private Image perspectiveImage;

    @objid ("a0139d8a-182e-49db-a410-917e801a9128")
     Color borderColor;

    @objid ("f8fe2b28-e0e1-4ef0-b1de-f07d3589d32f")
     Color curveColor;

    @objid ("c7ee5601-09a8-415c-91ec-5998257721bb")
     Control toolParent;

    @objid ("ea56249b-62a4-45fc-8d0e-b1ccdfe76c11")
    @Inject
    protected IEventBroker eventBroker;

    @objid ("7af47043-9934-4c14-92b7-ac90989216d9")
    @Inject
     EModelService modelService;

    @objid ("c27b3b86-f6c4-43b1-b553-eaa91ab2a05a")
    @Inject
    private EHandlerService handlerService;

    @objid ("2e45f582-5e08-4985-8a04-e83a202fa491")
    @Inject
    private ECommandService commandService;

    @objid ("7cf0bccc-47b8-4f6a-abf8-86ac026e1187")
    @Inject
    private MWindow window;

    @objid ("b7439697-a043-40c7-912b-899110f403fc")
    private MToolControl psME;

    @objid ("ceb366ff-3541-4162-9664-7fcf7bcf3f7d")
     IPropertyChangeListener propertyChangeListener;

    @objid ("4845f972-da76-43a8-8f7c-7ce8d793abd2")
    private EventHandler selectionHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			if (PerspectiveSwitcher.this.psTB.isDisposed()) {
				return;
			}
			MUIElement changedElement = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
			if (PerspectiveSwitcher.this.psME == null || !(changedElement instanceof MPerspectiveStack))
				return;
			MWindow perspWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(changedElement);
			MWindow switcherWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(PerspectiveSwitcher.this.psME);
			if (perspWin != switcherWin)
				return;
			MPerspectiveStack perspStack = (MPerspectiveStack) changedElement;
			if (!perspStack.isToBeRendered())
				return;
			MPerspective selElement = perspStack.getSelectedElement();
			for (ToolItem ti : PerspectiveSwitcher.this.psTB.getItems()) {
				ti.setSelection(ti.getData() == selElement);
			}
		}
	};

    @objid ("c431b0d1-eeec-433b-be41-bc69ba92c5e6")
    private EventHandler toBeRenderedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			if (PerspectiveSwitcher.this.psTB.isDisposed()) {
				return;
			}
			MUIElement changedElement = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
			if (PerspectiveSwitcher.this.psME == null || !(changedElement instanceof MPerspective))
				return;
			MWindow perspWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(changedElement);
			MWindow switcherWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(PerspectiveSwitcher.this.psME);
			if (perspWin != switcherWin)
				return;
			MPerspective persp = (MPerspective) changedElement;
			if (!persp.getParent().isToBeRendered())
				return;
			if (changedElement.isToBeRendered()) {
				addPerspectiveItem(persp);
			} else {
				removePerspectiveItem(persp);
			}
		}
	};

    @objid ("983aca84-316e-4891-8bc7-60bb680261dc")
    private EventHandler labelHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			if (PerspectiveSwitcher.this.psTB.isDisposed()) {
				return;
			}
			MUIElement changedElement = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
			if (PerspectiveSwitcher.this.psME == null || !(changedElement instanceof MPerspective))
				return;
			String attName = (String) event.getProperty(UIEvents.EventTags.ATTNAME);
			Object newValue = event.getProperty(UIEvents.EventTags.NEW_VALUE);
			MWindow perspWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(changedElement);
			MWindow switcherWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(PerspectiveSwitcher.this.psME);
			if (perspWin != switcherWin)
				return;
			MPerspective perspective = (MPerspective) changedElement;
			if (!perspective.isToBeRendered())
				return;
			for (ToolItem ti : PerspectiveSwitcher.this.psTB.getItems()) {
				if (ti.getData() == perspective) {
					updateToolItem(ti, attName, newValue);
				}
			}
			// update the layout
			PerspectiveSwitcher.this.psTB.pack();
			PerspectiveSwitcher.this.psTB.getShell().layout(new Control[] { PerspectiveSwitcher.this.psTB }, SWT.DEFER);
		}
		private void updateToolItem(ToolItem ti, String attName, Object newValue) {
			boolean showText = PrefUtil.getAPIPreferenceStore().getBoolean(
					IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR);
			if (showText && UIEvents.UILabel.LABEL.equals(attName)) {
				String newName = (String) newValue;
				ti.setText(newName);
			} else if (UIEvents.UILabel.TOOLTIP.equals(attName)) {
				String newTTip = (String) newValue;
				ti.setToolTipText(newTTip);
			}
		}
	};

    @objid ("4538cf36-48d0-447f-b780-d12c64279fad")
    private EventHandler childrenHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			if (PerspectiveSwitcher.this.psTB.isDisposed()) {
				return;
			}
			Object changedObj = event.getProperty(UIEvents.EventTags.ELEMENT);
			String eventType = (String) event.getProperty(UIEvents.EventTags.TYPE);
			if (changedObj instanceof MWindow && UIEvents.EventTypes.ADD.equals(eventType)) {
				MUIElement added = (MUIElement) event.getProperty(UIEvents.EventTags.NEW_VALUE);
				if (added instanceof MPerspectiveStack) {
				}
			}
			if (PerspectiveSwitcher.this.psME == null || !(changedObj instanceof MPerspectiveStack))
				return;
			MWindow perspWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor((MUIElement) changedObj);
			MWindow switcherWin = PerspectiveSwitcher.this.modelService.getTopLevelWindowFor(PerspectiveSwitcher.this.psME);
			if (perspWin != switcherWin)
				return;
			if (UIEvents.EventTypes.ADD.equals(eventType)) {
				MPerspective added = (MPerspective) event.getProperty(UIEvents.EventTags.NEW_VALUE);
				// Adding invisible elements is a NO-OP
				if (!added.isToBeRendered())
					return;
				addPerspectiveItem(added);
			} else if (UIEvents.EventTypes.REMOVE.equals(eventType)) {
				MPerspective removed = (MPerspective) event.getProperty(UIEvents.EventTags.OLD_VALUE);
				// Removing invisible elements is a NO-OP
				if (!removed.isToBeRendered())
					return;
				removePerspectiveItem(removed);
			}
		}
	};

    @objid ("bf5a465f-d409-4d19-92d7-36f34a61a34c")
    @PostConstruct
    void init() {
        this.eventBroker.subscribe(UIEvents.ElementContainer.TOPIC_CHILDREN, this.childrenHandler);
        this.eventBroker.subscribe(UIEvents.UIElement.TOPIC_TOBERENDERED, this.toBeRenderedHandler);
        this.eventBroker.subscribe(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT, this.selectionHandler);
        this.eventBroker.subscribe(UIEvents.UILabel.TOPIC_ALL, this.labelHandler);
        
        setPropertyChangeListener();
    }

    @objid ("de015fef-0e01-41c6-9cd8-a5f9da432e09")
    @PreDestroy
    void cleanUp() {
        if (this.perspectiveImage != null) {
            this.perspectiveImage.dispose();
            this.perspectiveImage = null;
        }
        
        this.eventBroker.unsubscribe(this.toBeRenderedHandler);
        this.eventBroker.unsubscribe(this.childrenHandler);
        this.eventBroker.unsubscribe(this.selectionHandler);
        this.eventBroker.unsubscribe(this.labelHandler);
        
        PrefUtil.getAPIPreferenceStore().removePropertyChangeListener(this.propertyChangeListener);
    }

    @objid ("a3491422-474c-4ebb-9b21-87e5982edded")
    @PostConstruct
    void createWidget(final Composite parent, final MToolControl toolControl) {
        this.psME = toolControl;
        this.comp = new Composite(parent, SWT.NONE);
        RowLayout layout = new RowLayout(SWT.HORIZONTAL);
        layout.marginLeft = layout.marginRight = 8;
        layout.marginBottom = 4;
        layout.marginTop = 4;
        this.comp.setLayout(layout);
        
        ToolBar firstTB = new ToolBar(this.comp, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
        final ToolItem firstSeparator = new ToolItem(firstTB, SWT.SEPARATOR);
        
        this.psTB = new ToolBar(this.comp, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
        
        ToolBar lastTB = new ToolBar(this.comp, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
        final ToolItem lastSeparator = new ToolItem(lastTB, SWT.SEPARATOR);
        
        // this.comp.addPaintListener(new PaintListener() {
        // @Override
        // public void paintControl(PaintEvent e) {
        // paint(e);
        // }
        // });
        this.toolParent = ((Control) toolControl.getParent().getWidget());
        this.toolParent.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                // if (PerspectiveSwitcher.this.borderColor == null)
                // PerspectiveSwitcher.this.borderColor =
                // e.display.getSystemColor(SWT.COLOR_BLACK);
                // e.gc.setForeground(PerspectiveSwitcher.this.borderColor);
                // Rectangle bounds = ((Control) e.widget).getBounds();
                // e.gc.drawLine(0, bounds.height - 1, bounds.width,
                // bounds.height - 1);
        
            }
        });
        
        this.comp.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });
        
        // psTB.addMenuDetectListener(new MenuDetectListener() {
        // public void menuDetected(MenuDetectEvent e) {
        // ToolBar tb = (ToolBar) e.widget;
        // Point p = new Point(e.x, e.y);
        // p = psTB.getDisplay().map(null, psTB, p);
        // ToolItem item = tb.getItem(p);
        // if (item == null)
        //                    E4Util.message("  ToolBar menu"); //$NON-NLS-1$
        // else {
        // MPerspective persp = (MPerspective) item.getData();
        // if (persp == null)
        //                        E4Util.message("  Add button Menu"); //$NON-NLS-1$
        // else
        // openMenuFor(item, persp);
        // }
        // }
        // });
        
        this.psTB.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposeTBImages();
            }
        
        });
        
        this.psTB.getAccessible().addAccessibleListener(new AccessibleAdapter() {
            @Override
            public void getName(AccessibleEvent e) {
                if (0 <= e.childID && e.childID < PerspectiveSwitcher.this.psTB.getItemCount()) {
                    ToolItem item = PerspectiveSwitcher.this.psTB.getItem(e.childID);
                    if (item != null) {
                        e.result = item.getToolTipText();
                    }
                }
            }
        });
        
        // FIXME: this is the button that should popup a dialog to choose a
        // perspective
        // final ToolItem createItem = new ToolItem(this.psTB, SWT.PUSH);
        // createItem.setImage(getOpenPerspectiveImage());
        // createItem.setToolTipText(WorkbenchMessages.OpenPerspectiveDialogAction_tooltip);
        // createItem.addSelectionListener(new SelectionListener() {
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // selectPerspective();
        // }
        //
        // @Override
        // public void widgetDefaultSelected(SelectionEvent e) {
        // selectPerspective();
        // }
        // });
        // new ToolItem(this.psTB, SWT.SEPARATOR);
        
        MPerspectiveStack stack = getPerspectiveStack();
        if (stack != null) {
            // Create an item for each perspective that should show up
            for (MPerspective persp : stack.getChildren()) {
                if (persp.isToBeRendered()) {
                    if (persp.getTags().contains("user"))
                        addPerspectiveItem(persp);
                }
            }
        }
    }

    @objid ("62135f58-59c1-4532-9d4c-471264e63d3c")
    private Image getOpenPerspectiveImage() {
        if (this.perspectiveImage == null || this.perspectiveImage.isDisposed()) {
            ImageDescriptor desc = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_NEW_PAGE);
            this.perspectiveImage = desc.createImage();
        }
        return this.perspectiveImage;
    }

    @objid ("d6d98beb-5d47-41f3-bfd4-584f34127d17")
    MPerspectiveStack getPerspectiveStack() {
        List<MPerspectiveStack> psList = this.modelService.findElements(this.window, null, MPerspectiveStack.class, null);
        if (psList.size() > 0)
            return psList.get(0);
        return null;
    }

    @objid ("05d545b5-e3bc-41d9-9899-1ca90572f119")
    private ToolItem addPerspectiveItem(final MPerspective persp) {
        final ToolItem psItem = new ToolItem(this.psTB, SWT.RADIO);
        psItem.setData(persp);
        
        String iconUri = persp.getIconURI();
        ImageDescriptor imageDescriptor = null;
        try {
            imageDescriptor = ImageDescriptor.createFromURL(new URL(iconUri));
        } catch (MalformedURLException e) {
            AppUi.LOG.error(e);
        }
        
        boolean foundImage = false;
        if (imageDescriptor != null) {
            final Image image = imageDescriptor.createImage(false);
            if (image != null) {
                psItem.setImage(image);
                psItem.addListener(SWT.Dispose, new Listener() {
                    @Override
                    public void handleEvent(org.eclipse.swt.widgets.Event event) {
                        image.dispose();
                    }
                });
                foundImage = true;
                psItem.setToolTipText(persp.getLocalizedLabel());
            }
        }
        
        if (!foundImage) {
            psItem.setText(persp.getLocalizedLabel());
            psItem.setToolTipText(persp.getLocalizedTooltip());
        }
        
        psItem.setSelection(persp == persp.getParent().getSelectedElement());
        
        psItem.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MPerspective persp = (MPerspective) e.widget.getData();
                persp.getParent().setSelectedElement(persp);
            }
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                MPerspective persp = (MPerspective) e.widget.getData();
                persp.getParent().setSelectedElement(persp);
            }
        });
        
        // psItem.addListener(SWT.MenuDetect, new Listener() {
        // public void handleEvent(org.eclipse.swt.widgets.Event event) {
        // MPerspective persp = (MPerspective) event.widget.getData();
        // openMenuFor(psItem, persp);
        // }
        // });
        
        // update the layout
        this.psTB.pack();
        this.psTB.getShell().layout(new Control[] { this.psTB }, SWT.DEFER);
        return psItem;
    }

    @objid ("a402bb8d-5b50-4b3e-9d66-e1503d6d7252")
    private void selectPerspective() {
        // let the handler perform the work to consolidate all the code
        ParameterizedCommand command = this.commandService.createCommand(IWorkbenchCommandConstants.PERSPECTIVES_SHOW_PERSPECTIVE,
                Collections.EMPTY_MAP);
        this.handlerService.executeHandler(command);
    }

    @objid ("64688a17-8394-4a4f-881b-ba9785d05738")
    private void openMenuFor(final ToolItem item, final MPerspective persp) {
        final Menu menu = new Menu(this.psTB);
        menu.setData(persp);
        if (persp.getParent().getSelectedElement() == persp) {
            addSaveAsItem(menu);
            addResetItem(menu);
        }
        
        if (persp.isVisible()) {
            addCloseItem(menu);
        }
        
        new MenuItem(menu, SWT.SEPARATOR);
        // addDockOnSubMenu(menu);
        addShowTextItem(menu);
        
        Rectangle bounds = item.getBounds();
        Point point = this.psTB.toDisplay(bounds.x, bounds.y + bounds.height);
        menu.setLocation(point.x, point.y);
        menu.setVisible(true);
        menu.addMenuListener(new MenuListener() {
        
            @Override
            public void menuHidden(MenuEvent e) {
                PerspectiveSwitcher.this.psTB.getDisplay().asyncExec(new Runnable() {
        
                    @Override
                    public void run() {
                        menu.dispose();
                    }
        
                });
            }
        
            @Override
            public void menuShown(MenuEvent e) {
                // Nothing to do
            }
        
        });
    }

    @objid ("b796e0ad-c48a-4a93-b26f-a1bfb29d31f9")
    private void addCloseItem(final Menu menu) {
        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
        menuItem.setText(WorkbenchMessages.WorkbenchWindow_close);
        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MPerspective persp = (MPerspective) menu.getData();
                if (persp != null)
                    closePerspective(persp);
            }
        });
    }

    @objid ("ebce72df-40dd-4c1e-a99a-ca245f5152f3")
    private void closePerspective(final MPerspective persp) {
        // MWindow win = modelService.getTopLevelWindowFor(persp);
        // WorkbenchPage page = (WorkbenchPage)
        // win.getContext().get(IWorkbenchPage.class);
        // String perspectiveId = persp.getElementId();
        // IPerspectiveDescriptor desc = getDescriptorFor(perspectiveId);
        // page.closePerspective(desc, perspectiveId, true, false);
        
        // removePerspectiveItem(persp);
    }

    @objid ("4d827a9a-456d-426c-898f-c61d8e83de9f")
    private void addSaveAsItem(final Menu menu) {
        final MenuItem saveAsMenuItem = new MenuItem(menu, SWT.Activate);
        saveAsMenuItem.setText(WorkbenchMessages.PerspectiveBar_saveAs);
        final IWorkbenchWindow workbenchWindow = this.window.getContext().get(IWorkbenchWindow.class);
        workbenchWindow.getWorkbench().getHelpSystem().setHelp(saveAsMenuItem, IWorkbenchHelpContextIds.SAVE_PERSPECTIVE_ACTION);
        saveAsMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (PerspectiveSwitcher.this.psTB.isDisposed())
                    return;
                IHandlerService handlerService = (IHandlerService) workbenchWindow.getService(IHandlerService.class);
                IStatus status = Status.OK_STATUS;
                try {
                    handlerService.executeCommand(IWorkbenchCommandConstants.WINDOW_SAVE_PERSPECTIVE_AS, null);
                } catch (ExecutionException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotDefinedException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotEnabledException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotHandledException e) {
                }
                if (!status.isOK())
                    StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
            }
        });
    }

    @objid ("6a86fc4b-3de6-4478-9f48-7f97b14aa062")
    private void addResetItem(final Menu menu) {
        final MenuItem resetMenuItem = new MenuItem(menu, SWT.Activate);
        resetMenuItem.setText(WorkbenchMessages.PerspectiveBar_reset);
        final IWorkbenchWindow workbenchWindow = this.window.getContext().get(IWorkbenchWindow.class);
        workbenchWindow.getWorkbench().getHelpSystem().setHelp(resetMenuItem, IWorkbenchHelpContextIds.RESET_PERSPECTIVE_ACTION);
        resetMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (PerspectiveSwitcher.this.psTB.isDisposed())
                    return;
                IHandlerService handlerService = (IHandlerService) workbenchWindow.getService(IHandlerService.class);
                IStatus status = Status.OK_STATUS;
                try {
                    handlerService.executeCommand(IWorkbenchCommandConstants.WINDOW_RESET_PERSPECTIVE, null);
                } catch (ExecutionException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotDefinedException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotEnabledException e) {
                    status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, e.getMessage(), e);
                } catch (NotHandledException e) {
                }
                if (!status.isOK())
                    StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
            }
        });
    }

    @objid ("eeb90df6-9395-401c-89af-16f1ab0b2c3c")
    private void addShowTextItem(final Menu menu) {
        final MenuItem showtextMenuItem = new MenuItem(menu, SWT.CHECK);
        showtextMenuItem.setText(WorkbenchMessages.PerspectiveBar_showText);
        showtextMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean preference = showtextMenuItem.getSelection();
                if (preference != PrefUtil.getAPIPreferenceStore().getDefaultBoolean(
                        IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR)) {
                    PrefUtil.getInternalPreferenceStore().setValue(IPreferenceConstants.OVERRIDE_PRESENTATION, true);
                }
                PrefUtil.getAPIPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR, preference);
                changeShowText(preference);
            }
        });
        showtextMenuItem.setSelection(PrefUtil.getAPIPreferenceStore().getBoolean(
                IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));
    }

    @objid ("3bbe355c-5f03-4dcc-9abb-dcf1ffef27f6")
    private void setPropertyChangeListener() {
        this.propertyChangeListener = new IPropertyChangeListener() {
        
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR.equals(propertyChangeEvent.getProperty())) {
                    Object newValue = propertyChangeEvent.getNewValue();
                    boolean showText = true; // default
                    if (newValue instanceof Boolean)
                        showText = ((Boolean) newValue).booleanValue();
                    else if ("false".equals(newValue)) //$NON-NLS-1$
                        showText = false;
                    changeShowText(showText);
                }
            }
        };
        PrefUtil.getAPIPreferenceStore().addPropertyChangeListener(this.propertyChangeListener);
    }

    @objid ("7e391469-5bd2-4ff7-b8f5-6df2a3403c97")
    private void changeShowText(final boolean showText) {
        ToolItem[] items = this.psTB.getItems();
        for (int i = 0; i < items.length; i++) {
            MPerspective persp = (MPerspective) items[i].getData();
            if (persp != null)
                if (showText) {
                    if (persp.getLabel() != null)
                        items[i].setText(persp.getLocalizedLabel());
                    items[i].setToolTipText(persp.getLocalizedTooltip());
                } else {
                    Image image = items[i].getImage();
                    if (image != null) {
                        items[i].setText(""); //$NON-NLS-1$
                        items[i].setToolTipText(persp.getLocalizedLabel());
                    }
                }
        }
        // update fix the layout
        this.psTB.pack();
        this.psTB.getShell().layout(new Control[] { this.psTB }, SWT.DEFER);
    }

    @objid ("3b16c889-4ecc-4cfd-85ac-ccf01216a8de")
    private void removePerspectiveItem(final MPerspective toRemove) {
        ToolItem psItem = getItemFor(toRemove);
        if (psItem != null) {
            psItem.dispose();
        }
        
        // update the layout
        this.psTB.pack();
        this.psTB.getShell().layout(new Control[] { this.psTB }, SWT.DEFER);
    }

    @objid ("cf770f75-d6fa-43bc-9bdc-636e7258c258")
    protected ToolItem getItemFor(final MPerspective persp) {
        if (this.psTB == null)
            return null;
        
        for (ToolItem ti : this.psTB.getItems()) {
            if (ti.getData() == persp)
                return ti;
        }
        return null;
    }

    @objid ("440c1b63-e539-406c-81ab-e08a22d1c402")
    void paint(final PaintEvent e) {
        GC gc = e.gc;
        Point size = this.comp.getSize();
        if (this.curveColor == null)
            this.curveColor = e.display.getSystemColor(SWT.COLOR_BLACK);
        int h = size.y;
        int[] simpleCurve = new int[] { 0, h - 1, 1, h - 1, 2, h - 2, 2, 1, 3, 0 };
        // draw border
        gc.setForeground(this.curveColor);
        gc.setAdvanced(true);
        if (gc.getAdvanced()) {
            gc.setAntialias(SWT.ON);
        }
        gc.drawPolyline(simpleCurve);
        
        Rectangle bounds = ((Control) e.widget).getBounds();
        bounds.x = bounds.y = 0;
        Region r = new Region();
        r.add(bounds);
        int[] simpleCurveClose = new int[simpleCurve.length + 4];
        System.arraycopy(simpleCurve, 0, simpleCurveClose, 0, simpleCurve.length);
        int index = simpleCurve.length;
        simpleCurveClose[index++] = bounds.width;
        simpleCurveClose[index++] = 0;
        simpleCurveClose[index++] = bounds.width;
        simpleCurveClose[index++] = bounds.height;
        r.subtract(simpleCurveClose);
        Region clipping = new Region();
        gc.getClipping(clipping);
        r.intersect(clipping);
        gc.setClipping(r);
        Image b = this.toolParent.getBackgroundImage();
        if (b != null && !b.isDisposed())
            gc.drawImage(b, 0, 0);
        
        r.dispose();
        clipping.dispose();
        // // gc.fillRectangle(bounds);
        // Rectangle mappedBounds = e.display.map(comp, comp.getParent(),
        // bounds);
        // ((Composite) toolParent).drawBackground(gc, bounds.x, bounds.y,
        // bounds.width,
        // bounds.height, mappedBounds.x, mappedBounds.y);
    }

    @objid ("253b5c58-3de6-4aec-a50c-43786cbe2368")
    void resize() {
        Point size = this.comp.getSize();
        Image oldBackgroundImage = this.backgroundImage;
        this.backgroundImage = new Image(this.comp.getDisplay(), size.x, size.y);
        GC gc = new GC(this.backgroundImage);
        this.comp.getParent().drawBackground(gc, 0, 0, size.x, size.y, 0, 0);
        Color background = this.comp.getBackground();
        Color border = this.comp.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        RGB backgroundRGB = background.getRGB();
        // TODO naive and hard coded, doesn't deal with high contrast, etc.
        Color gradientTop = new Color(this.comp.getDisplay(), backgroundRGB.red + 12, backgroundRGB.green + 10,
                backgroundRGB.blue + 10);
        int h = size.y;
        int curveStart = 0;
        int curve_width = 5;
        
        int[] curve = new int[] { 0, h, 1, h, 2, h - 1, 3, h - 2, 3, 2, 4, 1, 5, 0, };
        int[] line1 = new int[curve.length + 4];
        int index = 0;
        int x = curveStart;
        line1[index++] = x + 1;
        line1[index++] = h;
        for (int i = 0; i < curve.length / 2; i++) {
            line1[index++] = x + curve[2 * i];
            line1[index++] = curve[2 * i + 1];
        }
        line1[index++] = x + curve_width;
        line1[index++] = 0;
        
        int[] line2 = new int[line1.length];
        index = 0;
        for (int i = 0; i < line1.length / 2; i++) {
            line2[index] = line1[index++] - 1;
            line2[index] = line1[index++];
        }
        
        // custom gradient
        gc.setForeground(gradientTop);
        gc.setBackground(background);
        gc.drawLine(4, 0, size.x, 0);
        gc.drawLine(3, 1, size.x, 1);
        gc.fillGradientRectangle(2, 2, size.x - 2, size.y - 3, true);
        gc.setForeground(background);
        gc.drawLine(2, size.y - 1, size.x, size.y - 1);
        gradientTop.dispose();
        
        gc.setForeground(border);
        gc.drawPolyline(line2);
        gc.dispose();
        this.comp.setBackgroundImage(this.backgroundImage);
        if (oldBackgroundImage != null)
            oldBackgroundImage.dispose();
    }

    @objid ("70e536ac-9a01-44c2-aa61-f49624d223e8")
    void dispose() {
        cleanUp();
        
        if (this.backgroundImage != null) {
            this.comp.setBackgroundImage(null);
            this.backgroundImage.dispose();
            this.backgroundImage = null;
        }
    }

    @objid ("b11766e4-aead-42cb-a0e6-d3da0ec7cabd")
    void disposeTBImages() {
        ToolItem[] items = this.psTB.getItems();
        for (int i = 0; i < items.length; i++) {
            Image image = items[i].getImage();
            if (image != null) {
                items[i].setImage(null);
                image.dispose();
            }
        }
    }

    @objid ("737ac5c7-62e2-4ad9-a59f-e96bdedff93b")
    public void setKeylineColor(final Color borderColor, final Color curveColor) {
        this.borderColor = borderColor;
        this.curveColor = curveColor;
    }

}

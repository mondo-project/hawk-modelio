package org.modelio.api.impl.modelio;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.app.IModelioContext;
import org.modelio.api.app.navigation.INavigationService;
import org.modelio.api.app.picking.IPickingProvider;
import org.modelio.api.app.picking.IPickingService;
import org.modelio.api.audit.IAuditService;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.diagram.IDiagramCustomizer;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramService;
import org.modelio.api.diagram.autodiagram.IAutoDiagramFactory;
import org.modelio.api.diagram.style.IStyleHandle;
import org.modelio.api.diagram.tools.IAttachedBoxCommand;
import org.modelio.api.diagram.tools.IBoxCommand;
import org.modelio.api.diagram.tools.ILinkCommand;
import org.modelio.api.diagram.tools.IMultiLinkCommand;
import org.modelio.api.editor.IEditionService;
import org.modelio.api.exchange.IExchangeService;
import org.modelio.api.impl.app.ModelioContext;
import org.modelio.api.impl.app.ScriptService;
import org.modelio.api.impl.app.picking.PickingService;
import org.modelio.api.impl.app.picking.PickingSessionProxy;
import org.modelio.api.impl.exchange.ExchangeService;
import org.modelio.api.impl.log.LogService;
import org.modelio.api.impl.mc.ModelComponentService;
import org.modelio.api.impl.meta.MetamodelService;
import org.modelio.api.impl.model.ImageService;
import org.modelio.api.impl.model.ModelManipulationService;
import org.modelio.api.impl.model.SharedModelingSession;
import org.modelio.api.impl.module.ModuleService;
import org.modelio.api.log.ILogService;
import org.modelio.api.mc.IModelComponentService;
import org.modelio.api.meta.IMetamodelService;
import org.modelio.api.model.IImageService;
import org.modelio.api.model.IModelManipulationService;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModuleService;
import org.modelio.api.module.script.IScriptService;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class a the class that represents the modelio application.
 * <p>
 * It is a singleton. It can be accessed by <code>Modelio.getInstance()</code> method.
 */
@objid ("d8736b08-26ab-4300-976b-ef3ec3a9e3d1")
public class ModelioImpl extends Modelio {
    private final class NullDiagramService implements IDiagramService {
		@Override
		public IAutoDiagramFactory getAutoDiagramFactory() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IStyleHandle getStyle(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IStyleHandle> listStyles() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IStyleHandle registerStyle(String styleName, String baseStyleName, File styleData) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IDiagramHandle getDiagramHandle(AbstractDiagram diagram) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PaletteEntry getRegisteredTool(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void registerCustomizedTool(String id, MClass metaclass, Stereotype stereotype, String dependency,
				IBoxCommand handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerCustomizedTool(String id, MClass metaclass, Stereotype stereotype, String dependency,
				IAttachedBoxCommand handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerCustomizedTool(String id, MClass metaclass, Stereotype stereotype, String dependency,
				ILinkCommand handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerCustomizedTool(String id, MClass metaclass, Stereotype stereotype, String dependency,
				IMultiLinkCommand handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerDiagramCustomization(Stereotype stereotype, MClass baseDiagramClass,
				IDiagramCustomizer customizer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDiagramCustomization(Stereotype stereotype, MClass baseDiagramClass,
				IDiagramCustomizer customizer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterCustomizedTool(String id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerDiagramContributor(ContributorCategory category,
				IDiagramWizardContributor contributor) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDiagramContributor(ContributorCategory category,
				IDiagramWizardContributor contributor) {
			// TODO Auto-generated method stub
			
		}
	}

	@objid ("473953da-ef7d-4563-89d4-02d1f24e33c4")
    @Inject
    private IEclipseContext eclipseContext;

    @objid ("172cb89e-fd7f-4bc6-af5c-41ebd702fee5")
    private boolean servicesInitialized;

    @objid ("366bf3b3-49ea-4a24-99ff-e571691f9e32")
    private IModelingSession modelingSession;

    @objid ("71bc1743-3b13-438c-9350-7d42c87bcf76")
    private GProject openedProject;

    @objid ("4a1cd047-af90-4d1c-8ea5-03f14998e88e")
    private Map<Class<?>, Object> serviceMap = new HashMap<>();

    @objid ("3b9c361a-b9ee-49af-84f3-61729a901869")
    @Override
    public IAuditService getAuditService() {
        return getService(IAuditService.class);
    }

    @objid ("85361f17-253e-4885-a54e-3f3a0a31fc11")
    @Override
    public IModelioContext getContext() {
        return new ModelioContext(this.eclipseContext.get(IProjectService.class));
    }

    @objid ("894e1fb1-599e-46f1-8817-fac43a8731c4")
    @Override
    public IDiagramService getDiagramService() {
        return  getService(IDiagramService.class);
    }

    @objid ("fc671a0e-3363-4702-b375-888700cffeb6")
    @Override
    public IEditionService getEditionService() {
        return  getService(IEditionService.class);
    }

    @objid ("018a4b0d-bf37-4507-99fa-3d10358c8415")
    @Override
    public IImageService getImageService() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return  getService(IImageService.class);
    }

    @objid ("5a77c6f0-4a08-40cf-b15e-feca2a9d8161")
    @Override
    public ILogService getLogService() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return getService(ILogService.class);
    }

    @objid ("9c6913f6-c96d-41c7-b228-f436c5c687c2")
    @Override
    public IModelingSession getModelingSession() {
        if(this.modelingSession == null){
            this.modelingSession = new SharedModelingSession(this.openedProject, this.eclipseContext.get(IMModelServices.class));
        }
        return this.modelingSession;
    }

    @objid ("3a392484-7f01-42ac-9540-3e15de613045")
    @Override
    public IModuleService getModuleService() {
        return getService(IModuleService.class);
    }

    /**
     * Get the navigation service.
     * <p>
     * The navigation service allow to force selection in all the view/dialog that are registered has
     * NavigationListener.
     * @return the navigation service.
     */
    @objid ("ee682731-1eaf-4ed3-9a1f-25cc0c15f615")
    @Override
    public INavigationService getNavigationService() {
        return  getService(INavigationService.class);
    }

    @objid ("987def87-b52b-4509-926e-edd031a6434b")
    @Override
    public IPickingService getPickingService() {
        return getService(IPickingService.class);
    }

    @objid ("0b1d2672-4f47-466f-a219-6f00ba442910")
    @Override
    public IModelComponentService getModelComponentService() {
        return getService(IModelComponentService.class);
    }

    @objid ("f9f80c5d-00aa-45a3-8f0e-d0ee7433d7b0")
    @Override
    public IScriptService getScriptService() {
        return getService(IScriptService.class);
    }

    @objid ("3650f193-8394-4332-8af5-53759737e409")
    @Override
    public IMetamodelService getMetamodelService() {
        return new MetamodelService();
    }

    @objid ("9222d44e-dd97-4c04-a3e6-1c61e89a658e")
    @Override
    public IModelManipulationService getModelManipulationService() {
        return  getService(IModelManipulationService.class);
    }

    @objid ("a618e022-c10d-4055-9c88-895f8203c4c8")
    @Override
    public IExchangeService getExchangeService() {
        return getService(IExchangeService.class);
    }

    @objid ("f820a10f-612e-4dcf-b3ea-b601a6f8202c")
    @SuppressWarnings("unchecked")
    @Override
    public synchronized <I> I getService(Class<I> serviceInterface) {
        if(!this.servicesInitialized ){
            initializeServices();   
        }
        return (I) this.serviceMap.get(serviceInterface);
    }

    /**
     * Called by E4 injection to initialize the instance
     * @param context the eclipse context.
     */
    @objid ("42b90af5-1701-49d6-bb27-315df2f68a70")
    @Execute
    void initialize(IEclipseContext context) {
        this.eclipseContext = context;
        Modelio.instance = this;
    }

    /**
     * 'Project closed' Eclipse 4 event handler.
     * @param closedProject the closed project
     */
    @objid ("4ee6a698-080e-4674-804a-50a6dcb9af24")
    @Inject
    @Optional
    public synchronized void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject closedProject) {
        this.serviceMap = new HashMap<>();
        this.servicesInitialized = false;
    }

    @objid ("64d34c74-89d2-4563-999e-2889f7f3e111")
    @Inject
    @Optional
    public void onProjectOpening(@EventTopic(ModelioEventTopics.PROJECT_OPENING) final GProject newProject) {
        this.openedProject =  newProject;
        this.modelingSession = null;
    }

    @objid ("28340bcd-9a5c-4d77-8876-d1e20b9c2734")
    @Override
    public synchronized <I> void registerService(Class<I> serviceInterface, I service) {
        this.serviceMap.put(serviceInterface, service);
    }

    /**
     * Package private constructor.
     */
    @objid ("e290b87e-70bd-4f42-9e3d-2466409ecdbb")
    ModelioImpl() {
    }

    @objid ("2b27227a-0b07-4d78-a8aa-d7d70e871539")
    @Inject
    @Optional
    void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // @UIEventTopic doesn't seems to be working here...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                for(IPickingProvider provider : ((PickingService)getPickingService()).getPickingProvider()){
                    provider.enterPickingMode(new PickingSessionProxy(session));
                }
            }           
        });
    }

    @objid ("9cd03e62-28d2-462c-ae55-ead92df7ab6a")
    @Inject
    @Optional
    void onPickingStop(@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // @UIEventTopic doesn't seems to be working here...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {                
                for(IPickingProvider provider : ((PickingService)getPickingService()).getPickingProvider()){
                    provider.enterPickingMode(new PickingSessionProxy(session));
                }
            }           
        });
    }

    @objid ("43028687-2cd4-4142-9ce5-4d195a16c10a")
    private void initializeServices() {
        Map<Class<?>, Object> services = this.serviceMap;
        
        IExchangeService exchangeService = new ExchangeService(this.eclipseContext);
        services.put(IExchangeService.class, exchangeService);
              
        IImageService imageService = new ImageService();
        services.put(IImageService.class, imageService);
              
        ILogService logService = new LogService();
        services.put(ILogService.class, logService);
              
        IModelManipulationService modelManipulationService = new ModelManipulationService();
        services.put(IModelManipulationService.class, modelManipulationService);
              
//        INavigationService navigationService = ContextInjectionFactory.make(NavigationService.class, this.eclipseContext);
//        services.put(INavigationService.class, navigationService);
              
        IPickingService pickingService = new PickingService(this.eclipseContext.get(IModelioPickingService.class));
        services.put(IPickingService.class, pickingService);
         
        IModelComponentService modelComponentService = new ModelComponentService(this.eclipseContext.get(IProjectService.class));
        services.put(IModelComponentService.class, modelComponentService); 
        
        IScriptService scriptService = new ScriptService(this.eclipseContext.get(org.modelio.mda.infra.service.IModuleService.class),this.eclipseContext.get(IProjectService.class));
        services.put(IScriptService.class, scriptService);
        
        final org.modelio.mda.infra.service.IModuleService coreService = this.eclipseContext.get(org.modelio.mda.infra.service.IModuleService.class);
        services.put(org.modelio.mda.infra.service.IModuleService.class, coreService);
        
        IModuleService moduleService = new ModuleService(coreService.getModuleRegistry());
        services.put(IModuleService.class, moduleService);

        // Extra null diagram service, to reduce errors when running without diagram plugins
        services.put(IDiagramService.class,  new NullDiagramService());

        this.servicesInitialized = true;
    }

}

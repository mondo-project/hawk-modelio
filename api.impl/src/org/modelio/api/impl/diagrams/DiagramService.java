package org.modelio.api.impl.diagrams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
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
import org.modelio.api.impl.plugin.ApiImpl;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.api.style.StyleHandle;
import org.modelio.diagram.api.tools.AttacherBoxToolEntry;
import org.modelio.diagram.api.tools.BoxToolEntry;
import org.modelio.diagram.api.tools.LinkToolEntry;
import org.modelio.diagram.api.tools.MultiLinkToolEntry;
import org.modelio.diagram.creation.wizard.diagramcreation.DiagramContributorManager;
import org.modelio.diagram.diagramauto.services.AutoDiagramFactory;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.plugin.DiagramEditorsManager;
import org.modelio.diagram.editor.plugin.IDiagramConfigurer;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.link.ModelioLinkCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;

/**
 * Default implementation of IDiagramService.
 * All tool related services are delegated to the ToolRegistry from DiagramEditor.
 */
@objid ("de5f4384-565e-400d-b403-134dd90a85b8")
public class DiagramService implements IDiagramService {
    @objid ("e2a53de0-1433-4790-aaba-0787258adbcb")
    private EContextService contextService;

    @objid ("06ebb816-57d8-406e-b21a-2b9e10c038bc")
    private ToolRegistry toolService;

    @objid ("2114ff67-c23a-4afb-b81a-0d3c70319039")
    private IDiagramConfigurerRegistry configurerRegistry;

    @objid ("5381935d-10ab-436a-b24e-7bdedeff5011")
    private DiagramEditorsManager editorManager;

    @objid ("c7e3fe59-5408-4763-befa-a1e27ee65f82")
    private IProjectService projectService;

    @objid ("1a76979e-eec0-43a8-ad95-a93f4e0ad5c6")
    private IMModelServices modelServices;

    @objid ("cdf51ce4-90d3-4dd4-8d2f-56c608180dcf")
    private ModelManager manager;

    @objid ("9bfbae09-a357-4378-9d3b-344201600ee3")
    public DiagramService(IEclipseContext eclipseContext) {
        this.toolService = eclipseContext.get(ToolRegistry.class); 
        this.configurerRegistry = eclipseContext.get(IDiagramConfigurerRegistry.class);
        this.projectService = eclipseContext.get(IProjectService.class);
        this.editorManager = eclipseContext.get(DiagramEditorsManager.class);
        this.modelServices = eclipseContext.get(IMModelServices.class);
        this.contextService = eclipseContext.get(EContextService.class);
        
        
        this.manager = new ModelManager(eclipseContext);
    }

    @objid ("dae02bd5-bd0f-4f94-a242-eadf7c91c9c3")
    @Override
    public IAutoDiagramFactory getAutoDiagramFactory() {
        return new AutoDiagramFactory(this.modelServices);
    }

    @objid ("b07e3fe3-4d06-491d-82ae-fd3323b95708")
    @Override
    public IDiagramHandle getDiagramHandle(final AbstractDiagram diagram) {
        return DiagramHandle.create(this.manager, diagram,this.projectService,this.editorManager);
    }

    @objid ("79f4c2e5-e6ab-486f-9932-cefb07847c57")
    @Override
    public ToolEntry getRegisteredTool(final String id) {
        return this.toolService.getTool(id);
    }

    @objid ("f0af0ae9-d153-4f6b-aafd-b3868090827c")
    @Override
    public IStyleHandle getStyle(final String name) {
        NamedStyle style = DiagramStyles.getStyleManager().getStyle(name);
        if (style != null) {
            return new StyleHandle(style);
        }
        return null;
    }

    @objid ("009020dc-9642-42f9-a7e2-f4b7b99b92ae")
    @Override
    public List<IStyleHandle> listStyles() {
        ArrayList<IStyleHandle> results = new ArrayList<>();
        for (String s : DiagramStyles.getStyleManager().getAvailableStyles()) {
            results.add(new StyleHandle(s));
        }
        return results;
    }

    @objid ("44495430-822a-489b-b9db-73cc479da9bc")
    @Override
    public void registerCustomizedTool(final String id, MClass metaclass, final Stereotype stereotype, final String dependency, final IBoxCommand handler) {
        ModelioCreationContext context = new ModelioCreationContext(metaclass.getName(), dependency,stereotype);
        ToolEntry toolentry = new BoxToolEntry(handler.getLabel(),
                                               handler.getTooltip(),
                                               context,
                                               handler.getBitmap(),
                                               handler.getBitmap(),
                                               handler);
        
        this.toolService.registerTool(id, toolentry);
    }

    @objid ("fa62b9f3-6337-4d22-86f2-9f9ba8609924")
    @Override
    public void registerCustomizedTool(final String id, MClass metaclass, final Stereotype stereotype, final String dependency, final IAttachedBoxCommand handler) {
        ModelioCreationContext context = new ModelioCreationContext(metaclass.getName(),dependency,stereotype);
        ToolEntry toolEntry = new AttacherBoxToolEntry(handler.getLabel(),
                                                       handler.getTooltip(),
                                                       context,
                                                       handler.getBitmap(),
                                                       handler.getBitmap(),
                                                       handler);
        
        this.toolService.registerTool(id, toolEntry);
    }

    @objid ("0b119cd3-c21d-490d-8b8a-74108e7ca749")
    @Override
    public void registerCustomizedTool(final String id, MClass metaclass, final Stereotype stereotype, final String dependency, final ILinkCommand handler) {
        ModelioLinkCreationContext context = new ModelioLinkCreationContext(metaclass.getName(), stereotype);
        
        // Create and register tool entry
        ToolEntry toolEntry = new LinkToolEntry(handler.getLabel(),
                                                handler.getTooltip(),
                                                context,
                                                handler.getBitmap(),
                                                handler.getBitmap(),
                                                handler);
        
        this.toolService.registerTool(id, toolEntry);
    }

    @objid ("ee9cae64-147a-4719-962c-6fec284027b7")
    @Override
    public void registerCustomizedTool(final String id, MClass metaclass, final Stereotype stereotype, final String dependency, final IMultiLinkCommand handler) {
        ModelioLinkCreationContext context = new ModelioLinkCreationContext(metaclass.getName(),stereotype);
        
        // Create and register tool entry
        ToolEntry toolEntry = new MultiLinkToolEntry(handler.getLabel(),
                                                     handler.getTooltip(),
                                                     context,
                                                     handler.getBitmap(),
                                                     handler.getBitmap(),
                                                     handler);
        
        this.toolService.registerTool(id, toolEntry);
    }

    @objid ("9b50c294-66de-4b51-9390-749c7dd3c27a")
    @Override
    public void registerDiagramCustomization(final Stereotype stereotype, MClass baseDiagramClass, final IDiagramCustomizer customizer) {
        assert (stereotype != null);
        
        IDiagramConfigurer diagramConfigurer = new DiagramCustomizer(baseDiagramClass, customizer,this.configurerRegistry);
        
        this.configurerRegistry.registerDiagramConfigurer(baseDiagramClass.getName(),stereotype.getName(), diagramConfigurer);
    }

    @objid ("afcd98af-c3c1-4e9e-91c2-8c082e9a1a71")
    @Override
    public IStyleHandle registerStyle(final String styleName, final String baseStyleName, final File styleData) {
        if (styleData.isFile() && styleData.exists()) {
            try {
                DiagramStyles.getStyleManager().createStyle(styleName,baseStyleName,styleData.toURI().toURL());
            } catch (Exception e) {
                ApiImpl.LOG.error("org.modelio.api", e);
                return null;
            }
        } else {
            DiagramStyles.getStyleManager().createStyle(styleName, baseStyleName);
        }
        return new StyleHandle(styleName);
    }

    @objid ("d4923f20-fd2a-4025-8c6a-4b3c04731b5c")
    @Override
    public void unregisterDiagramCustomization(final Stereotype stereotype, MClass baseDiagramClass, final IDiagramCustomizer customizer) {
        assert (stereotype != null);
        
        IDiagramConfigurer diagramConfigurer = new DiagramCustomizer(baseDiagramClass, customizer,this.configurerRegistry);
        
        this.configurerRegistry.unregisterDiagramConfigurer(baseDiagramClass.getName(),stereotype.getName(),diagramConfigurer);
    }

    @objid ("847373dd-98c5-4944-ac73-19cebc4b6235")
    @Override
    public void unregisterCustomizedTool(String id) {
        this.toolService.unregisterTool(id);
    }

    @objid ("99c060ea-2cfe-4a58-a343-c7d676f8010d")
    @Override
    public void registerDiagramContributor(ContributorCategory category, IDiagramWizardContributor contributor) {
        DiagramContributorManager.getInstance().addContributor(category, contributor);
    }

    @objid ("e88b676d-6308-4f9f-a238-6536ebb0457c")
    @Override
    public void unregisterDiagramContributor(ContributorCategory category, IDiagramWizardContributor contributor) {
        DiagramContributorManager.getInstance().removeContributor(category, contributor);
    }

    @objid ("2fec08c1-afb4-4499-af8d-856137f91fe2")
    class DiagramCustomizer implements IDiagramConfigurer {
        @objid ("55b2d7ef-23e2-403a-bc2b-22d48438e8bf")
        private IDiagramConfigurer baseConfigurer;

        @objid ("94ff3b98-8907-4638-a18d-3fe110f2a982")
        private IDiagramCustomizer customizer;

        @objid ("1676df4a-6385-4792-bbc1-d7be9e22900d")
        public DiagramCustomizer(final MClass baseDiagramClass, final IDiagramCustomizer customizer, IDiagramConfigurerRegistry configurerRegistry) {
            this.customizer = customizer;
            this.baseConfigurer = configurerRegistry.getConfigurer(baseDiagramClass.getName());
        }

        @objid ("053cb650-e38e-4ac3-9089-1e3ad62ee16e")
        @Override
        public String getContributionURI() {
            return this.baseConfigurer.getContributionURI();
        }

        @objid ("a335ff41-9f9b-4f35-841c-1a759c9f8d28")
        @Override
        public PaletteRoot initPalette(final AbstractDiagramEditor diagram, final ToolRegistry toolRegistry) {
            PaletteRoot paletteRoot = null;
            if (this.customizer.keepBasePalette()) {
                paletteRoot = this.baseConfigurer.initPalette(diagram, toolRegistry);
            } else {
                paletteRoot = new PaletteRoot();
            }
            
            this.customizer.fillPalette(paletteRoot);
            return paletteRoot;
        }

/* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @objid ("b660fb04-cbaf-442b-8dfc-3c858d90d191")
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((this.baseConfigurer == null) ? 0 : this.baseConfigurer.hashCode());
            result = prime * result + ((this.customizer == null) ? 0 : this.customizer.hashCode());
            return result;
        }

/* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @objid ("9ce9e0a0-39f6-4651-98b2-9ce8e7bd2f2c")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DiagramCustomizer other = (DiagramCustomizer) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (this.baseConfigurer == null) {
                if (other.baseConfigurer != null)
                    return false;
            } else if (!this.baseConfigurer.equals(other.baseConfigurer))
                return false;
            if (this.customizer == null) {
                if (other.customizer != null)
                    return false;
            } else if (!this.customizer.equals(other.customizer))
                return false;
            return true;
        }

        @objid ("e80970e6-1fef-4b12-bb4b-7ae877be2985")
        private DiagramService getOuterType() {
            return DiagramService.this;
        }

    }

}

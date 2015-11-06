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
                                    

package org.modelio.core.ui.images;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.smkernel.IRStatus;
import org.modelio.vcore.smkernel.ISmObjectData;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.StatusState;
import org.modelio.vcore.smkernel.mapi.MStatus;

/**
 * {@link StyledCellLabelProvider Styled label provider} to use to draw {@link SmObjectImpl} icons
 * with their CMS and audit status.
 */
@objid ("00872c2e-b6e9-100f-85b1-001ec947cd2a")
public final class ElementDecoratedStyledLabelProvider extends StyledCellLabelProvider {
    @objid ("0086f33a-b6e9-100f-85b1-001ec947cd2a")
    private boolean showCms;

    @objid ("0086f420-b6e9-100f-85b1-001ec947cd2a")
    private boolean showAudit;

    @objid ("0086f506-b6e9-100f-85b1-001ec947cd2a")
    private final int cmsHeight;

    @objid ("0086f5e2-b6e9-100f-85b1-001ec947cd2a")
    private final int cmsWidth;

    @objid ("0086f6c8-b6e9-100f-85b1-001ec947cd2a")
    private final int referenceHeight;

    @objid ("0086f7a4-b6e9-100f-85b1-001ec947cd2a")
    private final int referenceWidth;

    @objid ("0086fe3e-b6e9-100f-85b1-001ec947cd2a")
    private static final String IMAGES_PATH = "icons/";

    @objid ("0086edea-b6e9-100f-85b1-001ec947cd2a")
    private static final Image reference = loadImage("refoverlay.png");

    @objid ("0086fbc8-b6e9-100f-85b1-001ec947cd2a")
    private static final Image cmsReadWrite = loadImage("CMS_READWRITE.png");

    @objid ("0086fd08-b6e9-100f-85b1-001ec947cd2a")
    private static final Image cmsReadOnly = loadImage("CMS_READONLY.png");

    @objid ("0034e9fa-09e9-1010-85b1-001ec947cd2a")
    private static final Image cmsToAdd = loadImage("CMS_TOADD.png");

    @objid ("bb9109ab-4822-4825-a958-be817734b138")
    private static final Image cmsConflict = loadImage("CMS_CONFLICT.png");

    @objid ("bdf24139-2449-497b-a607-7dc718ab85c8")
    private static final Image cmsModified = loadImage("CMS_MODIFIED.png");

    @objid ("000497a0-d823-100f-85b1-001ec947cd2a")
    private static final Image placeholder = loadImage("placeholder.png");

    @objid ("00944e40-f04f-100f-85b1-001ec947cd2a")
    private static final Image auditError = loadImage("auditerror.png");

    @objid ("00945fb6-f04f-100f-85b1-001ec947cd2a")
    private static final Image auditWarning = loadImage("auditwarning.png");

    @objid ("0094714a-f04f-100f-85b1-001ec947cd2a")
    private static final Image auditAdvice = loadImage("auditadvice.png");

    @objid ("009482fc-f04f-100f-85b1-001ec947cd2a")
    private final IModelioElementLabelProvider baseProvider;

    @objid ("f8e60892-001d-4464-a5fc-1ed0ccc4f068")
    private static final Image cmsReadWriteNoLock = loadImage("CMS_READWRITE.nolock.png");

    @objid ("db929c89-2b38-4453-8a97-d4be9c56b65e")
    private static final Image cmsModifiedNoLock = loadImage("CMS_MODIFIED.nolock.png");

    @objid ("a6111546-c975-45ad-b21d-5a0974352a01")
    private static final Image userReadOnly = loadImage("USER_READONLY.png");

// ------------------------------------------------------------------------------------
    /**
     * Initialize a new styled label provider.
     * @param baseProvider the provider used to compute the label.
     */
    @objid ("0086f894-b6e9-100f-85b1-001ec947cd2a")
    public ElementDecoratedStyledLabelProvider(IModelioElementLabelProvider baseProvider) {
        this(baseProvider, true, true);
    }

    /**
     * Initialize a new styled label provider.
     * @param baseProvider the provider used to compute the label.
     * @param showCms display the CMS state
     * @param showAudit display the audit state
     */
    @objid ("0086f948-b6e9-100f-85b1-001ec947cd2a")
    public ElementDecoratedStyledLabelProvider(IModelioElementLabelProvider baseProvider, boolean showCms, boolean showAudit) {
        this.baseProvider = baseProvider;
        
        configure(showCms, showAudit);
        
        this.cmsHeight = ElementDecoratedStyledLabelProvider.cmsReadWrite.getImageData().height;
        this.cmsWidth = ElementDecoratedStyledLabelProvider.cmsReadWrite.getImageData().width;
        
        this.referenceHeight = ElementDecoratedStyledLabelProvider.reference.getImageData().height;
        this.referenceWidth = ElementDecoratedStyledLabelProvider.reference.getImageData().width;
    }

    /**
     * Reconfigure this styled provider.
     * @param newShowCms <code>true</code> to display the CMS state.
     * @param newShowAudit <code>true</code> to display the audit state.
     */
    @objid ("0086f9e8-b6e9-100f-85b1-001ec947cd2a")
    public void configure(boolean newShowCms, boolean newShowAudit) {
        this.showCms = newShowCms;
        this.showAudit = newShowAudit;
    }

/*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.OwnerDrawLabelProvider#measure(org.eclipse.
     * swt.widgets.Event,
     * java.lang.Object)
     */
    @objid ("0086fa7e-b6e9-100f-85b1-001ec947cd2a")
    @Override
    protected void measure(final Event event, final Object obj) {
        super.measure(event, obj);
    }

    @objid ("0086fb28-b6e9-100f-85b1-001ec947cd2a")
    @Override
    protected void paint(final Event event, final Object obj) {
        super.paint(event, obj);
        int curX = event.x;
        final int curY = event.y;
        
        // Draw reference
        if (this.baseProvider.showAsReference(obj)) {
            event.gc.drawImage(ElementDecoratedStyledLabelProvider.reference, curX, curY + event.height - this.referenceHeight - 2);
        }
        
        // Draw CMS
        if (this.showCms) {
            if (obj instanceof Element) {
                final Image cmsDecoration = getCmsImage((Element) obj);
                if (cmsDecoration != null) {
                    event.gc.drawImage(cmsDecoration, curX, curY + 1);
                }
            }
        
            curX += this.cmsWidth + 1;
        }
        
        // Draw icon
        final Image icon = this.baseProvider.getImage(obj);
        int iconHeight = 0;
        int iconWidth = 0;
        if (icon != null) {
            iconHeight = icon.getImageData().height;
            iconWidth = icon.getImageData().height;
            int i = (event.height - iconHeight) - 2;
            event.gc.drawImage(icon, curX, curY + (i >= 0 ? i : 0));
            curX += iconWidth;
        }
        
        // Draw audit
        if (this.showAudit) {
            if (obj instanceof SmObjectImpl) {
                final ISmObjectData data = ((SmObjectImpl) obj).getData();
                int audit = 0;
                if (data.hasAllStatus(IRStatus.AUDIT1) == StatusState.TRUE) {
                    audit += 1;
                }
                if (data.hasAllStatus(IRStatus.AUDIT2) == StatusState.TRUE) {
                    audit += 2;
                }
        
                switch (audit) {
                case 1:
                    event.gc.drawImage(auditAdvice, curX - 2, curY + 2);
                    break;
                case 2:
                    event.gc.drawImage(auditWarning, curX - 2, curY + 2);
                    break;
                case 3:
                    event.gc.drawImage(auditError, curX - 2, curY + 2);
                    break;
                case 0:
                default:
                    break;
                } // end switch
            }
        }
    }

    @objid ("0086ff4c-b6e9-100f-85b1-001ec947cd2a")
    private static Image loadImage(String imageFileName) {
        ImageDescriptor desc = null;
        Image image = null;
        
        // Get the relative file name
        final StringBuilder path = new StringBuilder(IMAGES_PATH);
        path.append(imageFileName);
        
        final IPath imagePath = new Path(path.toString());
        final URL url = FileLocator.find(CoreUi.getContext().getBundle(), imagePath, null);
        assert (url != null);
        
        if (url != null) {
            desc = ImageDescriptor.createFromURL(url);
            image = desc.createImage();
            assert (image != null);
        }
        return image;
    }

    @objid ("00051cca-d823-100f-85b1-001ec947cd2a")
    @Override
    public final void update(ViewerCell cell) {
        final Object obj = cell.getElement();
        
        if (obj != null) {
            cell.setImage(placeholder);
            final StyledString s = this.baseProvider.getStyledText(obj);
            cell.setText(s.getString());
            cell.setStyleRanges(s.getStyleRanges());
        }
        
        super.update(cell);
    }

    @objid ("25104a14-4bb3-4ef2-b178-ab7f4c287885")
    private Image getCmsImage(Element obj) {
        final MStatus status = obj.getStatus();
        
        if (status.isCmsManaged()) {
        
            if (status.isCmsConflict()) {
                return cmsConflict;
            }
        
            if (status.isCmsModified() || status.isDirty()) {
                // Display the object as CMS modified if either modified in memory or on disk working copy.
                if (status.isLockingNeeded()) {
                    return cmsModified;
                } else {
                    return cmsModifiedNoLock;    
                }
            }
        
            if (! status.isUserWrite()) {
                return userReadOnly;
            } else if (status.isCmsReadOnly()) {
                return cmsReadOnly;
            } else if (status.isLockingNeeded()) {
                return cmsReadWrite;
            } else {
                return cmsReadWriteNoLock;
            }
        } else if (status.isCmsToAdd()) {
            return cmsToAdd;
        }
        return null;
    }

}

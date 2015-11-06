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
                                    

package com.sun.star.comp.beans;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XVclWindowPeer;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.beans.NamedValue;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.SystemDependent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class represents a local office window.
 * 
 * @since OOo 2.0.0
 */
@objid ("5956ff9b-db63-4bb1-924e-e7e3340443ca")
public class SwtWinOfficeWindow extends Composite implements XEventListener {
    @objid ("cd83e1bc-0815-4073-bcfd-ce45ec76a497")
    private boolean bPeer = false;

    @objid ("4030293b-a58f-41a6-8d07-579d27d3e46d")
    private transient XWindowPeer mWindow;

    @objid ("12de200a-c3f0-4285-8990-afd093172170")
    private transient XWindow xWindow;

    @objid ("053c2d22-0aac-4e70-896a-5bb7523f3bbb")
    private transient OfficeConnection mConnection;

    /**
     * Constructor.
     * @param connection The office connection object the window
     * belongs to.
     * @param parent a widget which will be the parent of the new instance (cannot be null)
     */
    @objid ("05b936b8-4599-43ae-9e96-7e9fbe39045a")
    protected SwtWinOfficeWindow(final OfficeConnection connection, final Composite parent) {
        super(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND |SWT.NO_REDRAW_RESIZE | SWT.NO_MERGE_PAINTS);
        
        this.mConnection = connection;
        this.mConnection.addEventListener(this);
    }

    /**
     * We make sure that the office window is notified that the parent
     * will be removed.
     */
    @objid ("6c940259-3cbe-48c4-bbe7-906d2dc919ff")
    @Override
    public void dispose() {
        System.out.println("SwtOfficeWindow.dispose");
        
        try {
            releaseSystemWindow();
        } catch (java.lang.Exception e) {
            System.err.println("LocaleOfficeWindow.dispose: Exception in releaseSystemWindow.");
            e.printStackTrace(System.err);
        }
        super.dispose();
    }

    /**
     * Retrieves an UNO XWindowPeer object associated with the OfficeWindow.
     * @return The UNO XWindowPeer object associated with the OfficeWindow.
     */
    @objid ("6dcab69f-2fbc-43cf-87d1-da296e88116f")
    public XWindowPeer getUNOWindowPeer() {
        if (this.mWindow == null) {
            try {
                UnoCaller.call(this.mConnection.getComponentContext(), new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        unoInnerCreateUnoWindow2();
                        return null;
                    }
                });
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } 
            
        }
        return this.mWindow;
    }

    /**
     * Receives a notification about the connection has been closed.
     * This method has to set the connection to <code>null</code>.
     * @source The event object.
     */
    @objid ("1eda5301-e04b-477f-af36-0e7b31c9702d")
    @Override
    public void disposing(final EventObject source) {
        // the window will be disposed by the framework
        this.mWindow = null;
        this.xWindow = null;
        this.mConnection    = null;
    }

    /**
     * Returns an AWT toolkit.
     */
    @objid ("b0e3ca58-b672-430d-a359-17197c113159")
    private XToolkit queryAWTToolkit() {
        // Create a UNO toolkit.
        XMultiComponentFactory  compfactory;
        XComponentContext xContext = this.mConnection.getComponentContext();
        if ( xContext != null ) {
            compfactory     = xContext.getServiceManager();
            XMultiServiceFactory    factory;
            factory = UnoRuntime.queryInterface(XMultiServiceFactory.class, compfactory);
            
            try {
                Object object = factory.createInstance( "com.sun.star.awt.Toolkit");
                return UnoRuntime.queryInterface(XToolkit.class, object);
            } catch (com.sun.star.uno.Exception e) {
                throw new RuntimeException("Failed getting com.sun.star.awt.Toolkit:"+e.toString(), e);
            }
        } else
            return null;
    }

    /**
     * called when system parent is available, reparents the bean window
     */
    @objid ("0c44a80f-a111-4a29-9902-945c0ba0e504")
    private synchronized void aquireSystemWindow() {
        if ( !this.bPeer ) {
            try {
                UnoCaller.call(this.mConnection.getComponentContext(), new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        unoInnerAcquireSystemWindow();
                        return true;
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * / called when system parent is about to die, reparents the bean window
     */
    @objid ("0001cc6e-b283-423f-ae53-10b89a1323bc")
    private synchronized void releaseSystemWindow() {
        if ( this.bPeer )
        {
            System.out.println("SwtOfficeWindow.releaseSystemWindow");
            
            try {
                UnoCaller.callOtherThread(new Callable<IOException>() {
                    
                    @Override
                    public IOException call() throws Exception {
                        unoInnerReleaseWindow();
                        return null;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    /**
     * Overriding {@link Composite#setVisible(boolean)} due to Java bug (no showing event).
     */
    @objid ("5dc71708-6759-44ea-a1c6-8a1824f2f19f")
    @Override
    public void setVisible(final boolean b) {
        // Java-Bug: componentShown() is never called :-(
        // is still at least in Java 1.4.1_02
        if ( b ) {
            getDisplay().syncExec(new SwtVisibilitySetter(b));
            aquireSystemWindow();
        } else {
            releaseSystemWindow();
            getDisplay().syncExec(new SwtVisibilitySetter(b));
        }
    }

    /**
     * To be called from the UNO thread.
     * @return the XWindowPeer .
     */
    @objid ("b645d752-45b7-45ba-bae2-3a8586d2a567")
    XWindowPeer unoInnerCreateUnoWindow2() {
        com.sun.star.awt.XToolkit xToolkit = queryAWTToolkit();
        
        // some JNI functions will not work without this
        getDisplay().syncExec(new SwtVisibilitySetter(true));
        
        // no wrapping necessary, simply use the HWND
        com.sun.star.awt.XSystemChildFactory xFac = UnoRuntime.queryInterface(com.sun.star.awt.XSystemChildFactory.class, xToolkit);
        
        Integer nativeHandle = (int) getNativeWindow();
        byte[] lIgnoredProcessID = new byte[0]; 
        this.mWindow = xFac.createSystemChild(nativeHandle,   
                                              lIgnoredProcessID,   
                                              getNativeWindowSystemType());
        
        this.xWindow = UnoRuntime.queryInterface(com.sun.star.awt.XWindow.class, this.mWindow);
        this.xWindow.setVisible( this.bPeer );
        return this.mWindow;
    }

    /**
     * Retrieves a platform dependent system window identifier.
     * @return The system window identifier.
     */
    @objid ("1826119c-ba98-41be-8989-55e3361dfa2a")
    private long getNativeWindow() {
        if (OSDetect.isLinux()) {
            // 'embeddedHandle' field exists only on Linux, get it by introspection
            // in order to compile even on Windows.
            try {
                final Object object = getClass().getField("embeddedHandle").get(this);
                
                if (object instanceof Integer)
                    return (Integer) object;
                else 
                    return (Long) object;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            return this.handle;
        }
    }

    /**
     * Retrieves a platform dependent system window type.
     * @return The system window type.
     */
    @objid ("b3c487fa-5739-4164-8ad2-4b34e004fe05")
    private short getNativeWindowSystemType() {
        if (OSDetect.isWindows()) {
            return SystemDependent.SYSTEM_WIN32;
        } else
            return SystemDependent.SYSTEM_XWINDOW;
    }

    /**
     * Returns an Any containing a sequences of {@link com.sun.star.beans.NamedValue}.
     * <li>One NamedValue contains the name "WINDOW" and the value is a Long representing the window handle.
     * <li>The second NamedValue has the name "XEMBED" and the value is true, when the XEmbed
     * protocol shall be used fore embedding the native Window.
     */
    @objid ("d7855529-3ecf-4c21-9b32-2f3ea66257a3")
    protected Any getWrappedWindowHandle() {
        NamedValue window = new NamedValue("WINDOW", new Any(new Type(Long.class), new Long(getNativeWindow())));
        NamedValue xembed = new NamedValue("XEMBED", new Any(Type.BOOLEAN, Boolean.FALSE));
        
        if (getNativeWindowSystemType() == SystemDependent.SYSTEM_XWINDOW )
        {
            String vendor = System.getProperty("java.vendor");
            if ((vendor.equals("Sun Microsystems Inc.") || vendor.equals("Oracle Corporation"))
                    && Boolean.valueOf(System.getProperty("sun.awt.xembedserver")).booleanValue())
            {
                xembed = new NamedValue("XEMBED", new Any(Type.BOOLEAN, Boolean.TRUE));
            }
        }
        return new Any(new Type("[]com.sun.star.beans.NamedValue"),
                       new NamedValue[] {window, xembed});
    }

    /**
     * To be called from {@link UnoCaller} thread by releaseSystemWindow().
     */
    @objid ("80821399-2c5e-4d32-abe2-3aee4fbcef68")
    final void unoInnerReleaseWindow() {
        // hide document window
        if (this.xWindow != null)
            this.xWindow.setVisible( false );
            
        // set null parent
        XVclWindowPeer xVclWindowPeer = UnoRuntime.queryInterface(XVclWindowPeer.class, this.mWindow);
        if (xVclWindowPeer != null)
            xVclWindowPeer.setProperty( "PluginParent", new Long(0) );
        
        this.bPeer = false;
    }

    /**
     * To be called from {@link UnoCaller} thread by aquireSystemWindow().
     */
    @objid ("667c0ce1-1bec-4575-9f79-a3bc02ae4f41")
    final void unoInnerAcquireSystemWindow() {
        // set real parent
        XVclWindowPeer xVclWindowPeer = UnoRuntime.queryInterface(XVclWindowPeer.class, this.mWindow);
            
        xVclWindowPeer.setProperty( "PluginParent", getWrappedWindowHandle());
        this.bPeer = true;
        
        // show document window
        this.xWindow.setVisible( true );
    }

    /**
     * Used to set SWT visibility in the SWT display thread.
     */
    @objid ("b68093cd-0a79-4cb8-bb18-d65548924432")
    private class SwtVisibilitySetter implements Runnable {
        @objid ("76891b07-590c-4d0d-a92e-bef4d47b7c63")
        private boolean vis;

        @objid ("7b56586c-b0c9-4373-a1c7-4a0d81187217")
        public SwtVisibilitySetter(boolean vis) {
            this.vis = vis;
        }

        @objid ("bb6d7cc3-87e0-4602-875a-a883b64f6801")
        @SuppressWarnings("synthetic-access")
        @Override
        public void run() {
            SwtWinOfficeWindow.super.setVisible(this.vis);
        }

    }

}

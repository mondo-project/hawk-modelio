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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.sun.star.awt.XCallback;
import com.sun.star.awt.XRequestCallback;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import org.eclipse.swt.widgets.Display;

/**
 * Used to call a task in the OpenOffice main thread.
 * <p>
 * 
 * Table of OpenOffice callbacks requirements:
 * <pre>
 * Method                             Needs   Needs      Listener
 * Callback Listener
 * ---------------------------------------------------------------------
 * XComponentLoader from Desktop
 * .loadComponentFromUrl            Y       N  doc.XEventListener
 * ?? OnLoadFinished
 * ?? OnLoadDone
 * XDesktop.terminate                   Y       O  XTerminateListener
 * XComponentLoader from Frame
 * .loadComponentFromUrl            Y       N  doc.XEventListener
 * ?? OnLoadFinished
 * ?? OnLoadClosed
 * XDocumentInsertable
 * .insertDocumentFromURL           Y       N  doc.XEventListener
 * ?? OnInsertDone
 * ?? OnLoadDone
 * ?? OnLoadFinished
 * XDocumentIndex.update                Y       ?  ???
 * XRefreshable.refresh                 Y       Y  XRefreshListener
 * XPrintable.print (with Wait)         Y       N  XPrintListener
 * XStorable.storeToURL                 Y       Y  doc.XEventListener
 * ?? OnSaveDone
 * ?? OnSaveAsDone
 * ?? OnSaveFinished
 * 
 * XCloseable.close                     ?       O  lang.XEventListener
 * XComponent.dispose                   ?       O  lang.XEventListener
 * 
 * UnoRuntime.queryInterface            N       N
 * XComponent.addEventListener          N       N
 * XComponentContext.getServiceManager  N       N
 * XController.getFrame                 N       N
 * XDesktop.addTerminateListener        N       N
 * XDocumentIndexesSupplier
 * .getDocumentIndexes              N       N
 * XModel.getCurrentController          N       N
 * XModifiable.setModified              N       N
 * XMultiComponentFactory
 * .createInstanceWithContext       N       N
 * XPrintable.setPrinter                N       N
 * XPrintJobBroadcaster
 * .addPrintJobListener             N       N
 * XPrintJobBroadcaster
 * .removePrintJobListener          N       N
 * XRefreshable.addRefreshListener      N       N
 * XRefreshable.removeRefreshListener   N       N
 * XServiceInfo.supportsService         N       N
 * XTextDocument.getText                N       N
 * XText.createTextCursor               N       N
 * </pre>
 * 
 * 
 * @author cma
 * @param <T>
 * the type of the result returned by the task.
 * @see Callable
 * @see <a href="http://wiki.services.openoffice.org/wiki/Framework/Article/Asynchronous_Callback_Service">OpenOffice Asynchronous callback service</a>
 * @see <a href="http://www.mail-archive.com/dev@api.openoffice.org/msg08705.html">[api-dev] Do all calls into Office require the XCallback</a>
 * @see <a href="http://openoffice.2283327.n4.nabble.com/Xstorable-storeToUrl-locks-soffice-bin-td2770915.html">Xstorable.storeToUrl locks soffice.bin</a>
 * @see <a href="https://svn.apache.org/repos/asf/incubator/ooo/trunk/main/qadevOOo/tests/java/ifc/awt/_XMessageBoxFactory.java">XMessageBoxFactory.java</a>
 * @see <a href="https://svn.apache.org/repos/asf/incubator/ooo/trunk/main/swext/mediawiki/src/com/sun/star/wiki/MainThreadDialogExecutor.java">MainThreadDialogExecutor.java</a>
 */
@objid ("22bbcfba-e9f3-4940-b40e-849da07ef042")
public class UnoCaller<T> implements XCallback {
    @objid ("6d10116f-ca7e-4003-90ab-38ccb0cb96bd")
    private boolean taskCalled = false;

    @objid ("4944f62a-21c5-4a84-a2d7-eb8df20ad9d7")
    private T res;

    @objid ("afb5698c-cc07-4ecf-b562-a1fd23c916ac")
    private Callable<T> runnable;

    @objid ("65329d8a-c23e-4771-baf4-776d1284b19c")
    private Throwable throwable;

    /**
     * Thread used to execute OpenOffice UNO calls in another thread.
     * <p>
     * Necessary because it seems OpenOffice fails answering calls launched from the SWT thread.
     */
    @objid ("37255090-3cf9-46ee-b784-11f0f9826f54")
    private static final ExecutorService unoThreadExecutor = Executors.newCachedThreadPool();

    @objid ("fdd4738f-f464-4702-9673-e313b7676519")
    private UnoCaller(final Callable<T> r) {
        this.runnable = r;
    }

    /**
     * Call an operation in the OpenOffice main thread.
     * <p>
     * If called from the SWT Display thread, run an inner event loop while waiting.
     * @throws com.sun.star.uno.RuntimeException if the task couldn't be scheduled.
     * @param xContext the OpenOffice context.
     * @param r the task to call
     * @return the result of the task.
     * @throws java.lang.reflect.InvocationTargetException if the task thrown an exception, it is encapsulated in this exception.
     */
    @objid ("4053a2ed-cbf9-466a-abc7-7db2852d6c36")
    public static <T> T call(final XComponentContext xContext, final Callable<T> r) throws InvocationTargetException, com.sun.star.uno.RuntimeException {
        UnoCaller<T> aExecutor = new UnoCaller<T>(r);
        return GetCallback( xContext, aExecutor, true );
    }

    @objid ("5cd863d7-1ecf-4385-859d-827c72a7a54a")
    private static <T> T GetCallback(final XComponentContext xContext, final UnoCaller<T> aExecutor, boolean refreshDisplay) throws InvocationTargetException, com.sun.star.uno.RuntimeException {
        if ( aExecutor != null )
        {
            XMultiComponentFactory xFactory = xContext.getServiceManager();
        
            if ( xFactory == null )
                throw new com.sun.star.uno.RuntimeException("Cannot get XMultiComponentFactory from the XComponentContext.");
        
            try {
                XRequestCallback xRequest = UnoRuntime.queryInterface(XRequestCallback.class,
                                                                      xFactory.createInstanceWithContext
                                                                      ( "com.sun.star.awt.AsyncCallback", xContext ) );
                if ( xRequest != null ) {
                    xRequest.addCallback( aExecutor, Any.VOID );
                    do {
                        if (refreshDisplay)
                            refreshDisplay();
                        Thread.yield();
                    } while( !aExecutor.taskCalled );
                }
            } catch( com.sun.star.uno.Exception e ) {
                throw (com.sun.star.uno.RuntimeException) new com.sun.star.uno.RuntimeException().initCause(e);
            }
        
            if (aExecutor.throwable != null)
                throw new InvocationTargetException(aExecutor.throwable);
        
            return aExecutor.res;
        
        }
        return null;
    }

    /**
     * Read and process SWT event loop events until there is none left.
     */
    @objid ("2b49fbf8-7666-44e1-b6b9-e9edb007f66e")
    private static void refreshDisplay() {
        if (Display.getCurrent() != null) {
            while (Display.getCurrent().readAndDispatch())
                {}
        }
    }

    /**
     * notifies the callback implementation
     * @param aData private data which was provided when the callback was requested.
     */
    @objid ("7e4ff865-6604-4499-8b14-f6b9062f3d33")
    @Override
    public void notify(final Object aData) {
        try {
            this.res = this.runnable.call();
        } catch (Throwable t) {
            this.throwable = t;
        }
        this.taskCalled = true;
    }

    /**
     * Call the given callable in a dedicated thread.
     * <p>
     * Should be called from the SWT thread. in this case the GUI will refresh while waiting for the operation to finish.
     * <p>
     * Necessary because it seems OpenOffice fails answering calls launched from the SWT thread on Windows OS.
     * It also seems that the SWT thread must be able to answer windows event.
     * @param t the operation to call. The operation may return an IOException if it fails.
     * @throws java.io.IOException in case of failure.
     */
    @objid ("5887bbf4-d274-48f7-8c9d-da98cb2bb8d5")
    public static void callOtherThread(final Callable<IOException> t) throws IOException {
        // Calls must be done in a thread INDEPENDENT from the SWT thread.
        
        // Launch the call
        Future<IOException> res = unoThreadExecutor.submit(t);
        
        try {
            // Wait while refreshing GUI if in GUI thread
            final Display display = Display.getCurrent();
        
            if (display != null)
                while(!res.isDone())
                    if(! display.readAndDispatch())
                        Thread.sleep(10);
        
            // Process the result
            IOException error = res.get();
            if (error != null)
                throw error;
        
        } catch (InterruptedException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        } catch (ExecutionException e) {
            throw new IOException(e.getCause().getLocalizedMessage(), e);
        }
    }

    /**
     * Call an operation in the OpenOffice main thread.
     * <p>
     * If called from the SWT Display thread, run an inner event loop while waiting.
     * @throws com.sun.star.uno.RuntimeException if the task couldn't be scheduled.
     * @param xContext the OpenOffice context.
     * @param r the task to call
     * @param refreshDisplay if <code>true</code> and called from the SWT Display thread, run an inner event loop while waiting.
     * @return the result of the task.
     * @throws java.lang.reflect.InvocationTargetException if the task thrown an exception, it is encapsulated in this exception.
     */
    @objid ("7e9f7fb6-e456-4870-bd73-a1fedc0a3344")
    public static <T> T call(final XComponentContext xContext, final Callable<T> r, boolean refreshDisplay) throws InvocationTargetException, com.sun.star.uno.RuntimeException {
        UnoCaller<T> aExecutor = new UnoCaller<T>(r);
        return GetCallback( xContext, aExecutor, refreshDisplay);
    }

}

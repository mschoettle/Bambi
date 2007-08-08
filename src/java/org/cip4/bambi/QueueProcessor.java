/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2007 The International Cooperation for the Integration of 
 * Processes in  Prepress, Press and Postpress (CIP4).  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        The International Cooperation for the Integration of 
 *        Processes in  Prepress, Press and Postpress (www.cip4.org)"
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of 
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior written
 *    permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For
 * details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR
 * THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the The International Cooperation for the Integration 
 * of Processes in Prepress, Press and Postpress and was
 * originally based on software 
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG 
 * copyright (c) 1999-2001, Agfa-Gevaert N.V. 
 *  
 * For more information on The International Cooperation for the 
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *  
 * 
 */
package org.cip4.bambi;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.auto.JDFAutoQueue.EnumQueueStatus;
import org.cip4.jdflib.auto.JDFAutoQueueEntry.EnumQueueEntryStatus;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.jmf.JDFCommand;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFQueue;
import org.cip4.jdflib.jmf.JDFQueueEntry;
import org.cip4.jdflib.jmf.JDFQueueSubmissionParams;
import org.cip4.jdflib.jmf.JDFResponse;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.util.UrlUtil;


/**
 *
 * @author  rainer
 *
 *
 */
public class QueueProcessor implements IQueueProcessor
{

    protected class SubmitQueueEntryHandler implements IMessageHandler
    {
    
        /* (non-Javadoc)
         * @see org.cip4.bambi.IMessageHandler#handleMessage(org.cip4.jdflib.jmf.JDFMessage, org.cip4.jdflib.jmf.JDFMessage)
         */
        public boolean handleMessage(JDFMessage m, JDFResponse resp, String queueEntryID, String workstepID)
        {
            if(m==null || resp==null)
            {
                return false;
            }
            log.debug("Handling"+m.getType());
            EnumType typ=m.getEnumType();
            //TODO handle errors
            if(EnumType.SubmitQueueEntry.equals(typ))
            {
                JDFQueueSubmissionParams qsp=m.getQueueSubmissionParams(0);
                if(qsp!=null)
                {
                    JDFDoc doc=qsp.getURLDoc();
                    if(doc!=null)
                    {
                        JDFResponse r2=addEntry((JDFCommand)m, doc);
                        if(r2!=null)
                        {
                            resp.mergeElement(r2, false);
                            return true;
                        }
                    }
                }
            }

            return false;        
        }
    
    
    
    
    
        /* (non-Javadoc)
         * @see org.cip4.bambi.IMessageHandler#getFamilies()
         */
        public EnumFamily[] getFamilies()
        {
            return new EnumFamily[]{EnumFamily.Command};
        }
    
        /* (non-Javadoc)
         * @see org.cip4.bambi.IMessageHandler#getMessageType()
         */
        public EnumType getMessageType()
        {
            return EnumType.SubmitQueueEntry;
        }
    }

    protected class QueueStatusHandler implements IMessageHandler
	{
	
	    /* (non-Javadoc)
	     * @see org.cip4.bambi.IMessageHandler#handleMessage(org.cip4.jdflib.jmf.JDFMessage, org.cip4.jdflib.jmf.JDFMessage)
	     */
	    public boolean handleMessage(JDFMessage m, JDFResponse resp, String queueEntryID, String workstepID)
	    {
	        if(m==null || resp==null)
	        {
	            return false;
	        }
	        log.debug("Handling "+m.getType());
	        EnumType typ=m.getEnumType();
	        if(EnumType.QueueStatus.equals(typ))
	        {
	        	JDFQueue q = resp.appendQueue();
	        	
	        	if (_theQueue != null)
	        	{
	        		q.setDeviceID( _theQueue.getDeviceID() );
	        		q.setQueueStatus(_theQueue.getQueueStatus());
	        		for (int i=0;i<_theQueue.getEntryCount();i++)
	        		{
	        			q.copyElement(_theQueue.getEntry(i), null);
	        		}
	        		
	        		return true;
	        	}
	        	else 
	        	{
	        		log.error("queue is null");
	        		return false;
	        	}
	        }
	
	        return false;        
	    }
	
	
	
	
	
	    /* (non-Javadoc)
	     * @see org.cip4.bambi.IMessageHandler#getFamilies()
	     */
	    public EnumFamily[] getFamilies()
	    {
	        return new EnumFamily[]{EnumFamily.Query};
	    }
	
	    /* (non-Javadoc)
	     * @see org.cip4.bambi.IMessageHandler#getMessageType()
	     */
	    public EnumType getMessageType()
	    {
	        return EnumType.QueueStatus;
	    }
	
	}

	private static Log log = LogFactory.getLog(QueueProcessor.class.getName());
    private File _queueFile;
    private static final long serialVersionUID = -876551736245089033L;
    private JDFQueue _theQueue;
    private Vector _listeners;
    private static final String jdfDir=DeviceServlet.baseDir+"JDFDir"+File.separator;
     
    public QueueProcessor(ISignalDispatcher _signalDispatcher, String deviceID)
    {
		super();
		this.init(deviceID);
    }
    
    /**
     * @param jmfHandler
     */
    public void addHandlers(IJMFHandler jmfHandler)
    {
        jmfHandler.addHandler(this.new SubmitQueueEntryHandler());
        jmfHandler.addHandler(this.new QueueStatusHandler());
    }

    private void init(String deviceID) 
    {
        log.info("QueueProcessor construct");
      	_queueFile=new File(DeviceServlet.baseDir+File.separator+"theQueue_"+deviceID+".xml");       
        _queueFile.getParentFile().mkdirs();
        new File(jdfDir).mkdirs();
        JDFDoc d=JDFDoc.parseFile(_queueFile.getAbsolutePath());
        if(d!=null)
        {
            log.info("refreshing queue");
            _theQueue=(JDFQueue) d.getRoot();
            
        }
        else
        {
            d=new JDFDoc(ElementName.QUEUE);
            log.info("creating new queue");
            _theQueue=(JDFQueue) d.getRoot();
            _theQueue.setQueueStatus(EnumQueueStatus.Waiting);
        }
        _theQueue.setAutomated(true);
        _theQueue.setDeviceID(deviceID);
        _listeners=new Vector();
	}

    public IQueueEntry getNextEntry()
    {
    	if (log != null) // dirty hack, static log gets trashed too soon on Tomcat undeploy
    		log.debug("getNextEntry");
        JDFQueueEntry qe=_theQueue.getNextExecutableQueueEntry();
        if(qe==null)
            return null;
        String docURL=BambiNSExtension.getDocURL(qe);
        docURL=UrlUtil.urlToFile(docURL).getAbsolutePath();
        JDFDoc doc=JDFDoc.parseFile(docURL);
        return new QueueEntry(doc,qe);        
    }


    /* (non-Javadoc)
     * @see org.cip4.bambi.IQueueProcessor#addListener(java.lang.Object)
     */
    public void addListener(Object o)
    {
        log.info("adding new listener");
        _listeners.add(o);        
    }

     /* (non-Javadoc)
     * @see org.cip4.bambi.IQueueProcessor#addEntry(org.cip4.jdflib.jmf.JDFCommand, org.cip4.jdflib.core.JDFDoc)
     */
    public JDFResponse addEntry(JDFCommand submitQueueEntry, JDFDoc theJDF)
    {
        if(submitQueueEntry==null || theJDF==null)
        {
            log.error("error submitting new queueentry");
            return null;
        }
        if(!_theQueue.canAccept())
            return null;
        
        JDFQueueSubmissionParams qsp=submitQueueEntry.getQueueSubmissionParams(0);
        if(qsp==null)
        {
            log.error("error submitting new queueentry");
            return null;
        }
        
        JDFResponse r=qsp.addEntry(_theQueue, null);
        JDFQueueEntry newQE=r.getQueueEntry(0);
        if(r.getReturnCode()!=0 || newQE==null)
        {
            log.error("error submitting queueentry: "+r.getReturnCode());
            return r;
        }
        //       myQueue.replaceElement(r.getQueue(0));   // copy the updated queue  
        if(!storeDoc(newQE,theJDF))
        {
            log.error("error storing queueentry: "+r.getReturnCode());
        }
        persist();
        notifyListeners();
        return r;
    }

    /**
     * @param newQE
     * @param theJDF
     */
    private boolean storeDoc(JDFQueueEntry newQE, JDFDoc theJDF)
    {
        if(newQE==null || theJDF==null)
        {
            log.error("error storing queueentry");
            return false;
        }
        String newQEID=newQE.getQueueEntryID();
        newQE=_theQueue.getEntry(newQEID);
        if(newQE==null)
        {
            log.error("error fetching queueentry: QueueEntryID="+newQEID);
            return false;
        }
        String theDocFile=jdfDir+newQEID+".jdf";
        theJDF.write2File(theDocFile, 0, true);
        try
        {
            BambiNSExtension.setDocURL(newQE, UrlUtil.fileToUrl(new File(theDocFile), false));
        }
        catch (MalformedURLException x)
        {
            log.error("invalid file name: "+theDocFile);
        }
        return true;
    }

    private void notifyListeners()
    {
        for(int i=0;i<_listeners.size();i++)
        {
            final Object elementAt = _listeners.elementAt(i);
            synchronized (elementAt)
            {
                elementAt.notifyAll();               
            }
         }
    }

    /**
     * make the memory queue persistant
     *
     */
    private synchronized void persist()
    {
        log.info("persisting queue to"+_queueFile.getAbsolutePath());
        _theQueue.getOwnerDocument_KElement().write2File(_queueFile.getAbsolutePath(), 0, true);
    }

    /* (non-Javadoc)
     * @see org.cip4.bambi.IQueueProcessor#getQueue()
     */
    public JDFQueue getQueue()
    {
        return _theQueue;
    }

    /* (non-Javadoc)
     * @see org.cip4.bambi.IQueueProcessor#updateEntry(java.lang.String, org.cip4.jdflib.auto.JDFAutoQueueEntry.EnumQueueEntryStatus)
     */
    public void updateEntry(String queueEntryID, EnumQueueEntryStatus status)
    {
        if(queueEntryID==null)
            return;
        JDFQueueEntry qe=_theQueue.getEntry(queueEntryID);
        qe.setQueueEntryStatus(status);
        persist();
        notifyListeners();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String s="[QueueProcessor: ] Status= "+_theQueue.getQueueStatus().getName()+" Num Entries: "+_theQueue.numEntries(null)+"\n Queue:\n";
        s+=_theQueue.toString();
        return s;
    }
}

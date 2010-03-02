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

package org.cip4.bambi.proxy;

import java.util.HashSet;

import org.cip4.bambi.core.AbstractDeviceProcessor;
import org.cip4.bambi.core.BambiNSExtension;
import org.cip4.bambi.core.queues.IQueueEntry;
import org.cip4.bambi.core.queues.QueueEntry;
import org.cip4.jdflib.auto.JDFAutoQueueEntry.EnumQueueEntryStatus;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.JDFElement.EnumNodeStatus;
import org.cip4.jdflib.jmf.JDFQueueEntry;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.ThreadUtil;

/**
 * 
 * @author prosirai
 */
public class ProxyDispatcherProcessor extends AbstractDeviceProcessor
{
	private static final long serialVersionUID = -384333582645081254L;
	private final IProxyProperties proxyProperties;
	private final OrphanCleaner cleaner;

	/**
	 * @param parent the owner device
	 */
	public ProxyDispatcherProcessor(final ProxyDevice parent)
	{
		super();
		_parent = parent;
		proxyProperties = parent.getProxyProperties();
		cleaner = new OrphanCleaner();
	}

	/**
	 * @see org.cip4.bambi.core.AbstractDeviceProcessor#processDoc(org.cip4.jdflib.node.JDFNode, org.cip4.jdflib.jmf.JDFQueueEntry)
	 * @param nod
	 * @param qe
	 * @return always Waiting
	 */
	@Override
	public EnumQueueEntryStatus processDoc(final JDFNode nod, final JDFQueueEntry qe)
	{
		// nop - the submission processor does the real work
		return EnumQueueEntryStatus.Waiting;

	}

	/**
	 * do whatever needs to be done on idle by default, just tell the StatusListner that we are bored
	 */
	@Override
	protected void idleProcess()
	{
		// nop
	}

	private class OrphanCleaner
	{
		private AbstractDeviceProcessor waitProc;
		private long lastClean;

		/**
		 * 
		 */
		public OrphanCleaner()
		{
			super();
			waitProc = null;
			lastClean = 0;
		}

		/**
		 * 
		 */
		protected void cleanOrphans()
		{
			long t = System.currentTimeMillis();
			if (t - lastClean < 30000)
				return;
			lastClean = t;
			/**
			 * clean up orphaned or duplicate processors
			 */
			HashSet<String> setQE = new HashSet<String>();
			for (int i = 0; true; i++)
			{
				AbstractDeviceProcessor proc = getParent().getProcessor(null, i);
				if (proc == null)
					break;
				if (!proc.isActive())
				{
					proc.shutdown();
				}
				else
				{
					IQueueEntry iqe = proc.getCurrentQE();
					if (iqe != null)
					{
						String qe = iqe.getQueueEntryID();
						if (setQE.contains(qe)) // remove duplicates
						{
							getLog().warn("removing duplicate processor ");
							proc.shutdown();
						}
						else
						{
							setQE.add(qe);
						}
						JDFQueueEntry qentry = iqe.getQueueEntry();
						if (qentry == null)
						{
							proc.shutdown();
						}
						else
						{
							EnumQueueEntryStatus qes = qentry.getQueueEntryStatus();
							if (EnumQueueEntryStatus.Aborted.equals(qes) || EnumQueueEntryStatus.Completed.equals(qes))
							{
								if (waitProc == null || waitProc != proc)
								{
									waitProc = proc;
									break; // no flip-flop wanted - always zapp first
								}
								else if (waitProc == proc && (proc instanceof ProxyDeviceProcessor))
								{
									((ProxyDeviceProcessor) proc).finalizeProcessDoc(null);
									waitProc = null;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param root the Kelement root this is not really a processor to display - ignore call
	 */
	@Override
	public void addToDisplayXML(final KElement root)
	{
		return;
	}

	/**
	 * 
	 * 
	 * @see org.cip4.bambi.core.AbstractDeviceProcessor#stopProcessing(org.cip4.jdflib.core.JDFElement.EnumNodeStatus)
	 */
	@Override
	public EnumNodeStatus stopProcessing(final EnumNodeStatus newStatus)
	{
		return null;
	}

	@Override
	protected boolean finalizeProcessDoc(final EnumQueueEntryStatus qes)
	{
		int maxPush = proxyProperties.getMaxPush();
		// if we can't push, there is no need to constantly check the queue
		for (int i = 0; i < 100; i++) // check every 5 minutes
		{
			if (_parent.activeProcessors() < 1 + maxPush)
				break;
			ThreadUtil.sleep(3000);
			if (i % 10 == 0)
				cleaner.cleanOrphans();
		}
		return _parent.activeProcessors() < 1 + maxPush;
	}

	@Override
	protected boolean initializeProcessDoc(final JDFNode node, final JDFQueueEntry qe)
	{
		currentQE = null;
		if (_parent.activeProcessors() >= 1 + proxyProperties.getMaxPush())
		{
			BambiNSExtension.setDeviceURL(qe, null);
			cleaner.cleanOrphans();
			return false; // no more push
		}
		qe.setDeviceID(proxyProperties.getSlaveDeviceID());
		final IQueueEntry iqe = new QueueEntry(node, qe);
		final ProxyDeviceProcessor pdb = ((ProxyDevice) _parent).submitQueueEntry(iqe, proxyProperties.getSlaveURL());
		if (pdb == null)
		{
			BambiNSExtension.setDeviceURL(qe, null); // see above clean up any multiple markers
		}
		return pdb != null;
	}

	/**
	 * @see org.cip4.bambi.core.AbstractDeviceProcessor#getCurrentQE()
	 * @return
	*/
	@Override
	public IQueueEntry getCurrentQE()
	{
		// we never have a qe of our own
		return null;
	}

	/**
	 * @see org.cip4.bambi.core.AbstractDeviceProcessor#isActive()
	 * @return
	*/
	@Override
	public boolean isActive()
	{
		// dispatchers are always active
		return true;
	}

}
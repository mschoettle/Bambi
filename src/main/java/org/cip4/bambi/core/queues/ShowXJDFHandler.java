/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2015 The International Cooperation for the Integration of 
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
package org.cip4.bambi.core.queues;

import java.io.File;
import java.io.InputStream;

import org.cip4.bambi.core.AbstractDevice;
import org.cip4.bambi.core.ContainerRequest;
import org.cip4.bambi.core.XMLResponse;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.FileUtil;

/**
 * @author Dr. Rainer Prosi, Heidelberger Druckmaschinen AG
 * 
 * 13.01.2009
 */
public class ShowXJDFHandler extends ShowHandler
{
	/**
	 * @param device
	 */
	public ShowXJDFHandler(final AbstractDevice device)
	{
		super(device);
	}

	/**
	 * @param request
	 */
	@Override
	protected boolean isMyRequest(final ContainerRequest request)
	{
		boolean b = request.isMyContext("showJDF");
		if (b)
		{
			final String jobPartID = request.getParameter(AttributeName.JOBPARTID);
			b = jobPartID != null;
		}
		return b;
	}

	/**
	 * @param request
	 * @param f
	 */
	@Override
	protected XMLResponse processFile(final ContainerRequest request, final File f)
	{
		final String jobPartID = request.getParameter(AttributeName.JOBPARTID);
		final InputStream is = FileUtil.getBufferedInputStream(f);
		if (is == null)
		{
			log.warn("cannot process file: " + f);
			return null;
		}
		JDFDoc doc = JDFDoc.parseStream(is);
		if (doc == null)
		{
			log.warn("cannot parse file: " + f);
			return null;
		}
		final JDFNode n = doc.getJDFRoot();
		final JDFNode nPart = n.getJobPart(jobPartID, null);
		if (nPart == null)
		{
			log.warn("no node with jobPartID: " + jobPartID);
			return null;
		}
		final XJDF20 xjdf20 = getViewXJDFConverter();
		final KElement xRoot = xjdf20.makeNewJDF(nPart, null);
		if (xRoot == null)
		{
			log.warn("could not convert node with jobPartID: " + jobPartID);
			return null;
		}
		doc = new JDFDoc(xRoot.getOwnerDocument());
		doc = prepareRoot(doc, request);
		XMLResponse r = new XMLResponse(doc.getRoot());
		return r;
	}

	/**
	 * 
	 * get the converter to display single nodes
	 * @param node 
	 * @return
	 */
	protected XJDF20 getViewXJDFConverter()
	{
		final XJDF20 xjdf20 = new XJDF20();
		xjdf20.setRetainAll(true);
		xjdf20.setWantProduct(false);
		xjdf20.setHTMLColor(true);

		return xjdf20;
	}
}

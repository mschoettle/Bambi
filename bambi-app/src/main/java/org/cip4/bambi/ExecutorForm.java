/**
 * The CIP4 Software License, Version 1.0
 *
 * Copyright (c) 2001-2009 The International Cooperation for the Integration of 
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

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

/**
 * Business Logic of Bambi Application.
 * @author stefanmeissner
 * @date 29.08.2013
 */
public class ExecutorForm {

	private JFrame frmCipBambiapp;

	private JTextArea textArea;

	private JTextField txtPort;

	private JTextField txtContext;

	private Thread bambiThread;

	private JButton btnStop;

	private JButton btnStart;

	private JButton btnOpen;

	private JLabel label;

    private BambiServer bambiServer;

    private boolean auto;

    /**
     * Create the application window.
     * @param context default value for context.
     * @param port default value for port.
     * @param auto auto start.
     */
	public ExecutorForm(String context, String port, boolean auto) {

		// init form
		initialize();

		// redirect output
		PrintStream printStream = new PrintStream(new JTextAreaOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);

        // set default values
        txtContext.setText(context);
        txtPort.setText(port);
        this.auto = auto;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCipBambiapp = new JFrame();
		frmCipBambiapp.setIconImage(Toolkit.getDefaultToolkit().getImage(ExecutorForm.class.getResource("/org/cip4/bambi/bambi_128.png")));
		frmCipBambiapp.getContentPane().setBackground(Color.WHITE);
		frmCipBambiapp.setTitle("CIP4 BambiApp");
		frmCipBambiapp.setBounds(100, 100, 719, 445);
		frmCipBambiapp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		btnStart = new JButton("Start Bambi");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				startBambiApp();
			}
		});

		JLabel lblBambiapp = new JLabel("BambiApp");
		lblBambiapp.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));

		textArea = new JTextArea();

		btnStop = new JButton("Stop Bambi");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopBambiApp();
			}
		});

		btnOpen = new JButton("Open URL");
		btnOpen.setEnabled(false);
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openUrl();
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textArea);

		txtPort = new JTextField();
		txtPort.setText("8080");
		txtPort.setColumns(10);

		JLabel lblUrl = new JLabel("http://localhost:");

		txtContext = new JTextField();
		txtContext.setText("bambi");
		txtContext.setColumns(10);
        txtContext.setEnabled(false);

		JLabel lblContext = new JLabel("/");

		JLabel lblUrlBambi = new JLabel("URL Bambi:");
		lblUrlBambi.setFont(new Font("SansSerif", Font.BOLD, 12));

		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(new ImageIcon(ExecutorForm.class.getResource("/org/cip4/bambi/bambi_128.png")));

		label = new JLabel("");
		label.setIcon(new ImageIcon(ExecutorForm.class.getResource("/org/cip4/bambi/cip4.png")));

		GroupLayout groupLayout = new GroupLayout(frmCipBambiapp.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
										.addGroup(
												Alignment.LEADING,
												groupLayout
														.createSequentialGroup()
														.addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addGroup(
																groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(
																				groupLayout
																						.createSequentialGroup()
																						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblUrl).addComponent(btnStart))
																						.addPreferredGap(ComponentPlacement.RELATED)
																						.addGroup(
																								groupLayout
																										.createParallelGroup(Alignment.LEADING)
																										.addGroup(groupLayout.createSequentialGroup().addGap(6).addComponent(btnStop))
																										.addGroup(
																												groupLayout
																														.createParallelGroup(Alignment.TRAILING)
																														.addComponent(label)
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, 48,
																																				GroupLayout.PREFERRED_SIZE)
																																		.addPreferredGap(ComponentPlacement.RELATED)
																																		.addComponent(lblContext)
																																		.addPreferredGap(ComponentPlacement.RELATED)
																																		.addComponent(txtContext, GroupLayout.PREFERRED_SIZE, 91,
																																				GroupLayout.PREFERRED_SIZE)
																																		.addPreferredGap(ComponentPlacement.RELATED)
																																		.addComponent(btnOpen))))).addComponent(lblUrlBambi)
																		.addComponent(lblBambiapp, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)).addGap(135))).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblIcon))
										.addGroup(
												groupLayout
														.createSequentialGroup()
														.addGap(15)
														.addGroup(
																groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(
																				groupLayout.createSequentialGroup().addComponent(lblBambiapp).addPreferredGap(ComponentPlacement.UNRELATED)
																						.addComponent(lblUrlBambi)).addComponent(label))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(
																groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblUrl)
																		.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblContext)
																		.addComponent(txtContext, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																		.addComponent(btnOpen)).addGap(11)
														.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnStart).addComponent(btnStop)))).addGap(15)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE).addContainerGap()));
		frmCipBambiapp.getContentPane().setLayout(groupLayout);
	}

    /**
     * Display dialog.
     */
    public void display() {
        frmCipBambiapp.setVisible(true);

        // auto start
        if(auto) {
            startBambiApp();
        }
    }

	/**
	 * Starts Bambi in a jetty web server environment.
	 * @throws IOException
	 */
	private void startBambiApp() {

		final int port = Integer.parseInt(txtPort.getText());
		final String context = txtContext.getText();

		btnStart.setEnabled(false);
		btnStop.setEnabled(true);
		btnOpen.setEnabled(true);
		txtContext.setEnabled(false);
		txtPort.setEnabled(false);

        bambiServer = new BambiServer();
		bambiThread = new Thread() {
			public void run() {
				bambiServer.start(port, context);
			}
		};
		bambiThread.start();

	}



	/**
	 * Stop Bambi Appication.
	 */
	private void stopBambiApp() {

		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		btnOpen.setEnabled(false);
		// txtContext.setEnabled(true);
		txtPort.setEnabled(true);

		// stop bambi
		Thread t = new Thread() {
			public void run() {
				try {
                    bambiServer.stop();
					bambiThread.interrupt();
					System.out.println("Bambi Server has stopped.....");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	/**
	 * Open URL in web browser, if supported.
	 */
	private void openUrl() {
		if (Desktop.isDesktopSupported()) {
			try {

				String url = String.format("http://localhost:%s/%s", txtPort.getText(), txtContext.getText());

				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception e) {
			}
		} else {
		}
	}
}

/*
 * Copyright (c) 2004-2011 tcpmon authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * $Id$
 */
package com.codegoogle.tcpmon;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.codegoogle.tcpmon.bookmark.Bookmark;
import com.codegoogle.tcpmon.bookmark.BookmarkManager;

/**
 * The main GUI class
 * 
 * @author Inderjeet Singh
 */
public final class MainWindow extends javax.swing.JFrame {

  private static final long serialVersionUID = 1L;
  private final BookmarkManager bookmarkManager;

  // Main tcpmon configuration
  private final Configuration configuration;

  /**
   * @return Configuration - configuration for the tcpmon app.
   */
  public Configuration getConfiguration() {
    return configuration;
  }

  /**
   * Creates new form MainWindow
   * 
   * @param bookmarkManager
   *          Bookmark manager.
   * @param configuration
   *          tcpmon configuration to populate the fields.
   */
  public MainWindow(BookmarkManager bookmarkManager, Configuration configuration) {
    this.bookmarkManager = bookmarkManager;
    this.configuration = configuration;

    // Set debug level.
    Debug.level = configuration.getDebugLevel();

    initComponents();

    // for non-generated UI stuff
    initOtherComponents();

    java.net.URL helpURL = MainWindow.class.getResource("/readme.html");
    if (helpURL != null) {
      try {
        tpInfo.setPage(helpURL);
      } catch (IOException e) {
        System.err.println("Attempted to read a bad URL: " + helpURL);
      }
    } else {
      System.err.println("Couldn't find file: readme.html");
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  private void initComponents() {// GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    tabbedPane = new javax.swing.JTabbedPane();
    adminPanel = new javax.swing.JPanel();
    pConnection = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    pConnectionInfo = new javax.swing.JPanel();
    lLocalPort = new javax.swing.JLabel();
    tfLocalPort = new javax.swing.JTextField();
    lRemoteHost = new javax.swing.JLabel();
    tfRemoteHost = new javax.swing.JTextField();
    lRemotePort = new javax.swing.JLabel();
    tfRemotePort = new javax.swing.JTextField();
    lSsl = new javax.swing.JLabel();
    cbSsl = new javax.swing.JCheckBox();
    bAddMonitor = new javax.swing.JButton();
    pInfo = new javax.swing.JPanel();
    spInfo = new javax.swing.JScrollPane();
    tpInfo = new javax.swing.JTextPane();

    getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    tabbedPane.setMinimumSize(new java.awt.Dimension(640, 480));
    tabbedPane.setPreferredSize(new java.awt.Dimension(640, 480));
    adminPanel.setLayout(new java.awt.BorderLayout());

    adminPanel.setMinimumSize(new java.awt.Dimension(400, 400));
    adminPanel.setPreferredSize(new java.awt.Dimension(400, 400));
    pConnection.setLayout(new java.awt.GridBagLayout());

    jLabel1.setText("Create a New TCP Monitor Connection: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
    pConnection.add(jLabel1, gridBagConstraints);

    pConnectionInfo.setLayout(new java.awt.GridBagLayout());

    pConnectionInfo.setMinimumSize(new java.awt.Dimension(150, 100));
    pConnectionInfo.setPreferredSize(new java.awt.Dimension(150, 100));
    lLocalPort.setText("Local Port: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.ipadx = 16;
    gridBagConstraints.ipady = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pConnectionInfo.add(lLocalPort, gridBagConstraints);

    tfLocalPort.setText(configuration.getLocalPort());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.ipadx = 119;
    gridBagConstraints.ipady = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
    pConnectionInfo.add(tfLocalPort, gridBagConstraints);

    lRemoteHost.setText("Server Name: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.ipadx = 3;
    gridBagConstraints.ipady = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    pConnectionInfo.add(lRemoteHost, gridBagConstraints);

    tfRemoteHost.setText(configuration.getRemoteHost());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.ipadx = 119;
    gridBagConstraints.ipady = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
    pConnectionInfo.add(tfRemoteHost, gridBagConstraints);

    lRemotePort.setText("Server Port: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.ipadx = 6;
    gridBagConstraints.ipady = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
    pConnectionInfo.add(lRemotePort, gridBagConstraints);

    tfRemotePort.setText(configuration.getRemotePort());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.ipadx = 119;
    gridBagConstraints.ipady = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 10, 16, 0);
    pConnectionInfo.add(tfRemotePort, gridBagConstraints);

    lSsl.setText("SSL Server: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.ipadx = 6;
    gridBagConstraints.ipady = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
    pConnectionInfo.add(lSsl, gridBagConstraints);

    cbSsl.setSelected(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.ipadx = 119;
    gridBagConstraints.ipady = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 10, 16, 0);
    pConnectionInfo.add(cbSsl, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.ipadx = 90;
    gridBagConstraints.ipady = 40;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(15, 30, 0, 0);
    pConnection.add(pConnectionInfo, gridBagConstraints);

    bAddMonitor.setText("Add Monitor");
    bAddMonitor.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bAddMonitorActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 90, 278, 0);
    pConnection.add(bAddMonitor, gridBagConstraints);

    adminPanel.add(pConnection, java.awt.BorderLayout.WEST);

    pInfo.setLayout(new java.awt.BorderLayout());

    tpInfo.setEditable(false);
    spInfo.setViewportView(tpInfo);

    pInfo.add(spInfo, java.awt.BorderLayout.CENTER);

    adminPanel.add(pInfo, java.awt.BorderLayout.CENTER);

    tabbedPane.addTab("Admin", adminPanel);

    getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

    pack();
  }// GEN-END:initComponents

  private void bAddMonitorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bAddMonitorActionPerformed
    MonitorPanel p = new MonitorPanel();
    TunnelConfig tc =
        new TunnelConfig(tfRemoteHost.getText(), tfRemotePort.getText(), tfLocalPort.getText(),
            true, cbSsl.isSelected());
    p.start(tc);
    tabbedPane.addTab("Port " + tc.localPort, p);
    // Select the tab that just got created.
    tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(p));
  }// GEN-LAST:event_bAddMonitorActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel adminPanel;
  private javax.swing.JButton bAddMonitor;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel lLocalPort;
  private javax.swing.JLabel lRemoteHost;
  private javax.swing.JLabel lRemotePort;
  private javax.swing.JLabel lSsl;
  private javax.swing.JPanel pConnection;
  private javax.swing.JPanel pConnectionInfo;
  private javax.swing.JPanel pInfo;
  private javax.swing.JScrollPane spInfo;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTextField tfLocalPort;
  private javax.swing.JTextField tfRemoteHost;
  private javax.swing.JTextField tfRemotePort;
  private javax.swing.JTextPane tpInfo;
  private javax.swing.JCheckBox cbSsl;

  // End of variables declaration//GEN-END:variables

  private void initOtherComponents() {
    JMenuBar menubar = new JMenuBar();
    menubar.add(createMenuFromBookmarks());

    setJMenuBar(menubar);

    if (configuration.isAutoStart()) {
      bAddMonitorActionPerformed(null);
    }

    pack();
  }

  public JCheckBox getCbSsl() {
    return cbSsl;
  }

  public JTextField getTfLocalPort() {
    return tfLocalPort;
  }

  public JTextField getTfRemoteHost() {
    return tfRemoteHost;
  }

  public JTextField getTfRemotePort() {
    return tfRemotePort;
  }

  JMenu createMenuFromBookmarks() {
    final JMenu bookmarkMenu = new JMenu("Bookmarks");

    JMenuItem addBookmarkMenuItem = new JMenuItem(new AbstractAction("Add a bookmark") {
      public void actionPerformed(ActionEvent e) {
        String bookmarkName =
            (String) JOptionPane.showInputDialog(MainWindow.this, "Bookmark name:", "Add bookmark",
                JOptionPane.YES_NO_OPTION, null, null, getTfRemoteHost().getText() + ":"
                    + getTfRemotePort().getText());
        if (bookmarkName != null) {
          bookmarkManager.add(new Bookmark(bookmarkName, getTfLocalPort().getText(),
              getTfRemoteHost().getText(), getTfRemotePort().getText(), getCbSsl().isSelected()));
          reloadBookmarkInMenu(bookmarkMenu);
          repaint();
        }
      }
    });
    bookmarkMenu.add(addBookmarkMenuItem);
    bookmarkMenu.addSeparator();

    reloadBookmarkInMenu(bookmarkMenu);

    return bookmarkMenu;
  }

  private void reloadBookmarkInMenu(JMenu bookmarkMenu) {
    // Clear previous entries, if any.
    while (bookmarkMenu.getItemCount() > 1) {
      bookmarkMenu.remove(bookmarkMenu.getItemCount() - 1);
    }
    // Load the bookmarks into menu.
    List<Bookmark> bookmarks = bookmarkManager.list();
    if (bookmarks != null) {
      for (final Bookmark bookmark : bookmarks) {
        JMenuItem menuEntry = new JMenuItem(new AbstractAction(bookmark.getName()) {

          public void actionPerformed(ActionEvent e) {
            tfLocalPort.setText(bookmark.getLocalPort());
            tfRemoteHost.setText(bookmark.getRemoteHost());
            tfRemotePort.setText(bookmark.getRemotePort());
            cbSsl.setSelected(bookmark.isSslServer());
            tabbedPane.setSelectedIndex(0);
          }

        });
        bookmarkMenu.add(menuEntry);
      }
    }

    if (Utils.isEmptyOrNull(bookmarks)) {
      JMenuItem noBookmark = new JMenuItem("No bookmark");
      noBookmark.setEnabled(false);
      bookmarkMenu.add(noBookmark);
    }
  }
}

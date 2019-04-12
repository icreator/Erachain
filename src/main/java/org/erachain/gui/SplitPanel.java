/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erachain.gui;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import org.erachain.gui.library.MSplitPane;
import org.erachain.gui.library.MTable;
import org.erachain.lang.Lang;
import org.erachain.settings.Settings;


/**
 * @author Саша
 */
public class SplitPanel extends javax.swing.JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public JPanel search_Info_Panel;
    public JLabel Label_search_Info_Panel;
    // Variables declaration - do not modify
    public javax.swing.JButton button1ToolBarLeftPanel;
    public javax.swing.JButton button2ToolBarLeftPanel;
    public javax.swing.JButton jButton1_jToolBar_RightPanel;
    public javax.swing.JButton jButton2_jToolBar_RightPanel;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JPanel jPanel_RightPanel;
    public javax.swing.JScrollPane jScrollPaneJPanelRightPanel;
    public javax.swing.JScrollPane jScrollPanelLeftPanel;
    public MSplitPane jSplitPanel;
    @SuppressWarnings("rawtypes")
    public MTable jTableJScrollPanelLeftPanel;
    public javax.swing.JToolBar jToolBarRightPanel;
    public javax.swing.JPanel leftPanel;
    public javax.swing.JPanel rightPanel1;
    public javax.swing.JTextField searchTextField_SearchToolBar_LeftPanel;
    public javax.swing.JToolBar searchToolBar_LeftPanel;
    public javax.swing.JLabel searthLabel_SearchToolBar_LeftPanel;
    public javax.swing.JToolBar toolBarLeftPanel;
    public JCheckBox searchMyJCheckBoxLeftPanel;
    public JCheckBox searchFavoriteJCheckBoxLeftPanel;
    private JSONObject settingsJSONbuf;
    /**
     * Creates new form Doma2
     */

    public SplitPanel(String str) {
        super();

        initComponents();
        search_Info_Panel = new JPanel();
        search_Info_Panel.setLayout(new java.awt.BorderLayout());
        Label_search_Info_Panel = new javax.swing.JLabel();
        Label_search_Info_Panel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        search_Info_Panel.add(Label_search_Info_Panel, java.awt.BorderLayout.CENTER);

        set_Divider_Parameters(str);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    @SuppressWarnings("rawtypes")
    private void initComponents() {

        settingsJSONbuf = new JSONObject();
        settingsJSONbuf = Settings.getInstance().Dump();

        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPanel = new MSplitPane(MSplitPane.VERTICAL_SPLIT, true);
        //      jSplitPanel.M_setDividerSize(20);
        leftPanel = new javax.swing.JPanel();
        toolBarLeftPanel = new javax.swing.JToolBar();
        button1ToolBarLeftPanel = new javax.swing.JButton();
        button2ToolBarLeftPanel = new javax.swing.JButton();
        searchToolBar_LeftPanel = new javax.swing.JToolBar();
        searthLabel_SearchToolBar_LeftPanel = new javax.swing.JLabel();
        searchTextField_SearchToolBar_LeftPanel = new javax.swing.JTextField();
        jScrollPanelLeftPanel = new javax.swing.JScrollPane();

        rightPanel1 = new javax.swing.JPanel();
        jToolBarRightPanel = new javax.swing.JToolBar();
        jButton1_jToolBar_RightPanel = new javax.swing.JButton();
        jButton2_jToolBar_RightPanel = new javax.swing.JButton();
        jPanel_RightPanel = new javax.swing.JPanel();
        jScrollPaneJPanelRightPanel = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();

        jSplitPanel.setBorder(null);

        leftPanel.setLayout(new java.awt.GridBagLayout());

        toolBarLeftPanel.setFloatable(false);
        toolBarLeftPanel.setRollover(true);

        button1ToolBarLeftPanel.setText("jButton1");
        button1ToolBarLeftPanel.setFocusable(false);
        button1ToolBarLeftPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button1ToolBarLeftPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button1ToolBarLeftPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1_ToolBar_LeftPanelActionPerformed(evt);
            }
        });
        toolBarLeftPanel.add(button1ToolBarLeftPanel);

        button2ToolBarLeftPanel.setText("jButton2");
        button2ToolBarLeftPanel.setFocusable(false);
        button2ToolBarLeftPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button2ToolBarLeftPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarLeftPanel.add(button2ToolBarLeftPanel);
        button2ToolBarLeftPanel.getAccessibleContext().setAccessibleDescription("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
        leftPanel.add(toolBarLeftPanel, gridBagConstraints);

        searchToolBar_LeftPanel.setFloatable(false);
        searchToolBar_LeftPanel.setRollover(true);
        searchToolBar_LeftPanel.setVisible(false);


        searchMyJCheckBoxLeftPanel = new JCheckBox();
        searchMyJCheckBoxLeftPanel.setText(Lang.getInstance().translate("My") + " ");
        searchToolBar_LeftPanel.add(searchMyJCheckBoxLeftPanel);

        searchFavoriteJCheckBoxLeftPanel = new JCheckBox();
        searchFavoriteJCheckBoxLeftPanel.setText(Lang.getInstance().translate("Favorite") + " ");
        searchToolBar_LeftPanel.add(searchFavoriteJCheckBoxLeftPanel);


        searthLabel_SearchToolBar_LeftPanel.setText("    " + Lang.getInstance().translate("Search") + ":   ");
        searthLabel_SearchToolBar_LeftPanel.setToolTipText("");
        searchToolBar_LeftPanel.add(searthLabel_SearchToolBar_LeftPanel);

        searchTextField_SearchToolBar_LeftPanel.setToolTipText("");
        searchTextField_SearchToolBar_LeftPanel.setAlignmentX(1.0F);
        searchTextField_SearchToolBar_LeftPanel.setMinimumSize(new java.awt.Dimension(200, 20));
        searchTextField_SearchToolBar_LeftPanel.setName(""); // NOI18N
        searchTextField_SearchToolBar_LeftPanel.setPreferredSize(new java.awt.Dimension(200, 20));
        searchToolBar_LeftPanel.add(searchTextField_SearchToolBar_LeftPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 10);
        leftPanel.add(searchToolBar_LeftPanel, gridBagConstraints);

        jScrollPanelLeftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTableJScrollPanelLeftPanel = new MTable(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        //      jScrollPanelLeftPanel.setViewportView(jTableJScrollPanelLeftPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 8, 8);
        leftPanel.add(jScrollPanelLeftPanel, gridBagConstraints);
        jScrollPanelLeftPanel.setMinimumSize(new Dimension(0, 0));
        leftPanel.setMinimumSize(new Dimension(0, 0));
        jSplitPanel.setLeftComponent(leftPanel);

        rightPanel1.setMinimumSize(new java.awt.Dimension(150, 0));
        rightPanel1.setName(""); // NOI18N
        rightPanel1.setLayout(new java.awt.GridBagLayout());
        //  rightPanel1.setBackground(new Color(0,0,0));

        jToolBarRightPanel.setFloatable(false);
        jToolBarRightPanel.setRollover(true);

        jButton1_jToolBar_RightPanel.setText("jButton1");
        jButton1_jToolBar_RightPanel.setFocusable(false);
        jButton1_jToolBar_RightPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1_jToolBar_RightPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarRightPanel.add(jButton1_jToolBar_RightPanel);

        jButton2_jToolBar_RightPanel.setText("jButton2");
        jButton2_jToolBar_RightPanel.setToolTipText("");
        jButton2_jToolBar_RightPanel.setFocusable(false);
        jButton2_jToolBar_RightPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2_jToolBar_RightPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBarRightPanel.add(jButton2_jToolBar_RightPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 8);
        rightPanel1.add(jToolBarRightPanel, gridBagConstraints);

        jPanel_RightPanel.setAlignmentX(1.0F);
        jPanel_RightPanel.setAlignmentY(1.0F);

        jScrollPaneJPanelRightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPaneJPanelRightPanel.setAlignmentX(1.0F);
        jScrollPaneJPanelRightPanel.setAlignmentY(1.0F);
        jScrollPaneJPanelRightPanel.setAutoscrolls(true);
        jScrollPaneJPanelRightPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPaneJPanelRightPanel.setName(""); // NOI18N
        jScrollPaneJPanelRightPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        jScrollPaneJPanelRightPanel.setVerifyInputWhenFocusTarget(false);
        jScrollPaneJPanelRightPanel.setWheelScrollingEnabled(false);
        jScrollPaneJPanelRightPanel.setFocusable(false);

        jLabel2.setText(" ");
        jLabel2.setToolTipText("");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setMaximumSize(new java.awt.Dimension(0, 0));
        jLabel2.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel2.setName(""); // NOI18N
        jScrollPaneJPanelRightPanel.setViewportView(jLabel2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        rightPanel1.add(jScrollPaneJPanelRightPanel, gridBagConstraints);
        jScrollPaneJPanelRightPanel.setMinimumSize(new Dimension(0, 0));
        jScrollPaneJPanelRightPanel.setPreferredSize(new Dimension(350, 350));
        rightPanel1.setMinimumSize(new Dimension(0, 0));
        rightPanel1.setPreferredSize(new Dimension(350, 350));
        jSplitPanel.setRightComponent(rightPanel1);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );

        jSplitPanel.setDividerLocation(0.3);

        this.jTableJScrollPanelLeftPanel.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    }

    private void button1_ToolBar_LeftPanelActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void set_Divider_Parameters(String str) {
        settingsJSONbuf = Settings.getInstance().getJSONObject();
    //    settingsJSONbuf = Settings.getInstance().read_setting_JSON();
        JSONObject params;
        params = new JSONObject();
        if (!settingsJSONbuf.containsKey("Main_Frame_Setting")) return;
        params = (JSONObject) settingsJSONbuf.get("Main_Frame_Setting");
        if (!params.containsKey(str)) return;
       // преобразуем все в ыекштп т.к. JSONObject в методе GET  преобразует <String>"2" -> <int>2
        HashMap param = (HashMap) params.get(str);
        if (param.containsKey("Div_Last_Loc"))
            jSplitPanel.setLastDividerLocation(new Integer(param.get("Div_Last_Loc")+""));
        if (param.containsKey("Div_Loc")) jSplitPanel.setDividerLocation(new Integer(param.get("Div_Loc")+""));
        int ii = new Integer(param.get("Div_Orientation")+"");
        if (param.containsKey("Div_Orientation")) jSplitPanel.setOrientation(ii);
        jSplitPanel.set_button_title();

    }

    public void onClose() {
    }
}
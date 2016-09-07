package gui.items.statement;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.JTable;

import org.mapdb.Fun.Tuple2;

import core.account.PublicKeyAccount;
import core.transaction.R_SignNote;
import core.transaction.Transaction;
import database.DBSet;
import gui.models.Renderer_Left;
import gui.models.Renderer_Right;
import lang.Lang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Саша
 */
public class Statement_Info extends javax.swing.JPanel {

    /**
     * Creates new form Statement_Info
     * @param statement 
     */
	 R_SignNote statement;
	
	
    public Statement_Info(Transaction transaction) {
        initComponents();
        
        Tuple2<BigDecimal, List<Tuple2<Integer, Integer>>> signs = DBSet.getInstance().getVouchRecordMap().get(transaction.getBlockHeight(DBSet.getInstance()),transaction.getSeqNo(DBSet.getInstance()));
  	  
        if (signs != null){
  	    
        
       
  	    	Statements_Vouch_Table_Model table_sing_model = new Statements_Vouch_Table_Model(transaction.getCreator().getAddress());
  	        jTable_Sign = new JTable (table_sing_model);
  	      jTable_Sign.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
  	    jTable_Sign.setDefaultRenderer(String.class, new Renderer_Left(jTable_Sign.getFontMetrics(jTable_Sign.getFont()),table_sing_model.get_Column_AutoHeight())); // set renderer
  	  jTable_Sign.setDefaultRenderer(PublicKeyAccount.class, new Renderer_Left(jTable_Sign.getFontMetrics(jTable_Sign.getFont()),table_sing_model.get_Column_AutoHeight())); // set renderer
			
  	    	
  	    }
        
        
        jScrollPane4.setViewportView(jTable_Sign);
        statement = (R_SignNote)transaction;
        jTextArea_Body.setText(new String( statement.getData(), Charset.forName("UTF-8") ));
        jSplitPane1.setDividerLocation(350);//.setDividerLocation((int)(jSplitPane1.getSize().getHeight()/0.5));//.setLastDividerLocation(0);
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel_Title = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea_Body = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabelTitlt_Table_Sign = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
    //    jTable_Sign = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        jLabel_Title.setText(Lang.getInstance().translate("Statement"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 11);
        add(jLabel_Title, gridBagConstraints);

        jSplitPane1.setBorder(null);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
       
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jTextArea_Body.setColumns(20);
        jTextArea_Body.setRows(5);
        jScrollPane3.setViewportView(jTextArea_Body);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 11, 11);
        jPanel1.add(jScrollPane3, gridBagConstraints);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelTitlt_Table_Sign.setText(Lang.getInstance().translate("Signatures")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(12, 11, 0, 11);
        jPanel2.add(jLabelTitlt_Table_Sign, gridBagConstraints);

       

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 11, 11);
        jPanel2.add(jScrollPane4, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(jSplitPane1, gridBagConstraints);
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabelTitlt_Table_Sign;
    private javax.swing.JLabel jLabel_Title;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable_Sign;
    private javax.swing.JTextArea jTextArea_Body;
    // End of variables declaration                   
}

package gui.items.persons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.glassfish.jersey.internal.util.Base64;
import org.mapdb.Fun.Tuple3;

import core.account.Account;
import core.account.PublicKeyAccount;
import core.item.assets.AssetCls;
import core.item.persons.PersonCls;
import core.transaction.Transaction;
import database.DBSet;
import gui.items.accounts.Account_Confiscate_Debt_Dialog;
import gui.items.accounts.Account_Lend_Dialog;
import gui.items.accounts.Account_Repay_Debt_Dialog;
import gui.items.accounts.Account_Send_Dialog;
import gui.items.accounts.Account_Take_Hold_Dialog;
import gui.items.mails.Mail_Send_Dialog;
import gui.models.PersonAccountsModel;
import gui.models.PersonStatusesModel;
import gui.models.Renderer_Boolean;
import gui.models.Renderer_Left;
import lang.Lang;
import utils.TableMenuPopupUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Саша
 */
public class Person_info_panel_001 extends javax.swing.JPanel {

    private static final Object[] String = null;
	/**
     * Creates new form person_info
     */
    public Person_info_panel_001(PersonCls person, boolean full) {
    	 key_jLabel = new javax.swing.JLabel();
       if (person != null)     	initComponents(person, full);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents(PersonCls person, boolean full) {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        name_jTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        gender_jTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        birthday_jTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        deathday_jTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        race_jTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        birth_Latitude_jTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        birth_Longitude_jTextField = new javax.swing.JTextField();
        Skin_Color_jLabel = new javax.swing.JLabel();
        skin_Color_jTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        eye_Color_jTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        hair_Сolor_jTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        height_jTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        description_jTextPane = new javax.swing.JTextPane();
       
        iconLabel = new javax.swing.JLabel();

        
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy"); // HH:mm");
        
        setMaximumSize(new java.awt.Dimension(400, 300));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(454, 500));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 7, 0};
        layout.rowHeights = new int[] {0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0};
        setLayout(layout);

        int gridy = 2;
        jLabel1.setText(Lang.getInstance().translate("Name")+":");
        jLabel1.setAlignmentY(0.2F);
        jLabel1.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel1, gridBagConstraints);

        name_jTextField.setEditable(false);
        name_jTextField.setText(person.getName());
        name_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                name_jTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = gridy++;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(name_jTextField, gridBagConstraints);

        jLabel2.setText(Lang.getInstance().translate("Description")+":");
        jLabel2.setAlignmentY(0.2F);
        jLabel2.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel2, gridBagConstraints);

        jLabel3.setText(Lang.getInstance().translate("Gender")+":");
        jLabel3.setAlignmentY(0.2F);
        jLabel3.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel3, gridBagConstraints);

        gender_jTextField.setEditable(false);
        gender_jTextField.setText(person.getGender()+"");
        gender_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gender_jTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(gender_jTextField, gridBagConstraints);

        jLabel4.setText(Lang.getInstance().translate("Birthday")+":");
        jLabel4.setAlignmentY(0.2F);
        jLabel4.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel4, gridBagConstraints);

        birthday_jTextField.setEditable(false);
        birthday_jTextField.setText(new Date (person.getBirthday())+"");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(birthday_jTextField, gridBagConstraints);

        
        
        
        jLabel5.setText(Lang.getInstance().translate("Deathday")+":");
        jLabel5.setAlignmentY(0.2F);
        jLabel5.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel5, gridBagConstraints);

        deathday_jTextField.setEditable(false);
        Long end = person.getDeathday();
        if (end == null || end <= person.getBirthday()){
        	deathday_jTextField.setText( "-");
        deathday_jTextField.setVisible(false);
        jLabel5.setVisible(false);
        }
        else
        	deathday_jTextField.setText( new Date (end)+"");
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(deathday_jTextField, gridBagConstraints);

        if (full) {
	        jLabel6.setText(Lang.getInstance().translate("Person number")+":");
	        jLabel6.setAlignmentY(0.2F);
	        jLabel6.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 12;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel6, gridBagConstraints);
	
	        race_jTextField.setEditable(false);
	        race_jTextField.setText(person.getRace()+"");
	        race_jTextField.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                race_jTextFieldActionPerformed(evt);
	            }
	        });
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 12;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(race_jTextField, gridBagConstraints);
	
	        jLabel7.setText(Lang.getInstance().translate("Birth Latitude")+":");
	        jLabel7.setAlignmentY(0.2F);
	        jLabel7.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 14;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel7, gridBagConstraints);
	
	        birth_Latitude_jTextField.setEditable(false);
	        birth_Latitude_jTextField.setText(person.getBirthLatitude()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 14;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(birth_Latitude_jTextField, gridBagConstraints);
	
	        jLabel8.setText(Lang.getInstance().translate("Birth Longitude")+":");
	        jLabel8.setAlignmentY(0.2F);
	        jLabel8.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 16;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel8, gridBagConstraints);
	
	        birth_Longitude_jTextField.setEditable(false);
	        birth_Longitude_jTextField.setText(person.getBirthLongitude()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 16;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(birth_Longitude_jTextField, gridBagConstraints);
	
	        Skin_Color_jLabel.setText(Lang.getInstance().translate("Skin Color")+":");
	        Skin_Color_jLabel.setAlignmentY(0.2F);
	        Skin_Color_jLabel.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 18;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(Skin_Color_jLabel, gridBagConstraints);
	
	        skin_Color_jTextField.setEditable(false);
	        skin_Color_jTextField.setText(person.getSkinColor()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 18;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(skin_Color_jTextField, gridBagConstraints);
	
	        jLabel10.setText(Lang.getInstance().translate("Eye Color")+":");
	        jLabel10.setAlignmentY(0.2F);
	        jLabel10.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 20;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel10, gridBagConstraints);
	
	        eye_Color_jTextField.setEditable(false);
	        eye_Color_jTextField.setText(person.getEyeColor()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 20;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(eye_Color_jTextField, gridBagConstraints);
	
	        jLabel11.setText(Lang.getInstance().translate("Hair Сolor")+":");
	        jLabel11.setAlignmentY(0.2F);
	        jLabel11.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 22;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel11, gridBagConstraints);
	
	        hair_Сolor_jTextField.setEditable(false);
	        hair_Сolor_jTextField.setText(person.getHairСolor()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 22;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(hair_Сolor_jTextField, gridBagConstraints);
	
	        jLabel12.setText(Lang.getInstance().translate("Height")+":");
	        jLabel12.setAlignmentY(0.2F);
	        jLabel12.setAutoscrolls(true);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 24;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
	        add(jLabel12, gridBagConstraints);
	
	        height_jTextField.setEditable(false);
	        height_jTextField.setText(person.getHeight()+"");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 24;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
	        add(height_jTextField, gridBagConstraints);
        }

        jLabel13.setText(Lang.getInstance().translate("Creator")+":");
        jLabel13.setAlignmentY(0.2F);
        jLabel13.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 1);
        add(jLabel13, gridBagConstraints);

        jTextField13.setEditable(false);
        jTextField13.setText(person.getOwner().toString());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(jTextField13, gridBagConstraints);
        
        JPopupMenu creator_Meny = new JPopupMenu();
        JMenuItem copy_Creator_Address1 = new JMenuItem(Lang.getInstance().translate("Copy Address"));
  		copy_Creator_Address1.addActionListener(new ActionListener()
  		{
  			public void actionPerformed(ActionEvent e) 
  			{
  				
  				      				
  				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  				StringSelection value = new StringSelection(person.getOwner().getAddress().toString());
  			    clipboard.setContents(value, null);
  			}
  		});
  		 creator_Meny.add(copy_Creator_Address1);
  		 

   		JMenuItem Send_Coins_Crator = new JMenuItem(Lang.getInstance().translate("Send Coins"));
   		Send_Coins_Crator.addActionListener(new ActionListener()
   		{
   			public void actionPerformed(ActionEvent e) 
   			{
   				new Account_Send_Dialog(null, null, new Account(person.getOwner().getAddress().toString()),null);
   			}
   		});
   		creator_Meny.add(Send_Coins_Crator);

   		JMenuItem Send_Mail_Creator = new JMenuItem(Lang.getInstance().translate("Send Mail"));
   		Send_Mail_Creator.addActionListener(new ActionListener()
   		{
   			public void actionPerformed(ActionEvent e) 
   			{
   			
   				new Mail_Send_Dialog(null, null, new Account(person.getOwner().getAddress().toString()),null);
   			}
   		});
   		creator_Meny.add(Send_Mail_Creator);
   		
  		 
  		 
  		 
  		 
  		 
  		 
  		 
  		 
  		 
  		 
  		 
        
  		jTextField13.add(creator_Meny);
  		jTextField13.setComponentPopupMenu(creator_Meny);
  		//TableMenuPopupUtil.installContextMenu(jTextField13, creator_Meny);
        

        jLabel14.setText(Lang.getInstance().translate("Statuses"));
        jLabel14.setAlignmentY(0.2F);
        jLabel14.setAutoscrolls(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(jLabel14, gridBagConstraints);

        PersonStatusesModel statusModel = new PersonStatusesModel (person.getKey());
        jTable1.setModel(statusModel);
        
      
        //CHECKBOX FOR FAVORITE
        		TableColumn to_Date_Column1 = jTable1.getColumnModel().getColumn( PersonStatusesModel.COLUMN_PERIOD);	
        		//favoriteColumn.setCellRenderer(new Renderer_Boolean()); //personsTable.getDefaultRenderer(Boolean.class));
        		to_Date_Column1.setMinWidth(80);
        		to_Date_Column1.setMaxWidth(200);
        		to_Date_Column1.setPreferredWidth(120);//.setWidth(30);
        
       // jTable1.setPreferredSize(new java.awt.Dimension(100, 64));
        
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(jScrollPane1, gridBagConstraints);

        jLabel15.setText(Lang.getInstance().translate("Accounts"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(jLabel15, gridBagConstraints);

        
        
        
     // GET CERTIFIED ACCOUNTS
        
     	
        
        
        
        PersonAccountsModel personModel = new PersonAccountsModel(person.getKey());
       
        
        jTable2.setModel(personModel);
        
      //CHECKBOX FOR FAVORITE
      		TableColumn to_Date_Column = jTable2.getColumnModel().getColumn( PersonAccountsModel.COLUMN_TO_DATE);	
      		//favoriteColumn.setCellRenderer(new Renderer_Boolean()); //personsTable.getDefaultRenderer(Boolean.class));
      		to_Date_Column.setMinWidth(50);
      		to_Date_Column.setMaxWidth(200);
      		to_Date_Column.setPreferredWidth(80);//.setWidth(30);
        
       
      		
      		
      		
        
        /*
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
        		table_addresses,
            title
        ));
        */
      //  jTable2.setPreferredSize(new java.awt.Dimension(100, 64));
        jScrollPane2.setViewportView(jTable2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 8, 10);
        add(jScrollPane2, gridBagConstraints);

        description_jTextPane.setEditable(false);
        description_jTextPane.setText(person.getDescription());
        jScrollPane3.setViewportView(description_jTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        add(jScrollPane3, gridBagConstraints);

        key_jLabel.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;//.FIRST_LINE_END;//.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        add(key_jLabel, gridBagConstraints);

     //   iconLabel.setEditable(false);
     //   iconLabel.setText(Lang.getInstance().translate("Key")+":" + person.getKey()+"");
        iconLabel.setIcon(new ImageIcon(person.getImage()));
        
        
        
      //в формате Base64;
     //   java.lang.String aa = "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";
     //   ImageIcon ii;
     //   byte[] bl = Base64.decode(aa.getBytes());
     //   ii = new ImageIcon(bl);
     //   key_jLabel .setIcon(ii);
        
     //   person.getImage();
        // в формате Blob
        /*
        ResultSet rs = null; // результат из базы данных
         Blob bl1 =  person.getImage();;
		try {
			bl1 = rs.getBlob(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			ii = new ImageIcon(bl1.getBytes(1, (int) bl1.length()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    //    .setIcon(ii);
        */
        
   //   key_jLabel.setIcon(new ImageIcon(person.getImage()));;
      key_jLabel.setText(Lang.getInstance().translate("Key")+":" + person.getKey()+"");
       
       //ImageIcon ii = new ImageIcon(person.getImage());
     //  key_jLabel.setIcon(new ImageIcon(ii.getImage().getScaledInstance(100, 100, ii.getImage().SCALE_DEFAULT)));
       
      
        
    //    key_jLabel.setPreferredSize(new Dimension(50,70));
    //    key_jLabel.setMaximumSize(new Dimension(50,70));
   //     key_jLabel.setMaximumSize(new Dimension(50,70));
   //     key_jLabel.setSize(new Dimension(50,70));
       
                
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 6);
        add(iconLabel, gridBagConstraints);
        
        
        
   
      //MENU
      		JPopupMenu menu = new JPopupMenu();	
      		
      		JMenuItem copyAddress = new JMenuItem(Lang.getInstance().translate("Copy Address"));
      		copyAddress.addActionListener(new ActionListener()
      		{
      			public void actionPerformed(ActionEvent e) 
      			{
      				int row = jTable2.getSelectedRow();
      				row = jTable2.convertRowIndexToModel(row);
      				      				
      				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      				StringSelection value = new StringSelection(personModel.getAccount_String(row));
      			    clipboard.setContents(value, null);
      			}
      		});
      		menu.add(copyAddress);
      		
      		JMenuItem copy_Creator_Address = new JMenuItem(Lang.getInstance().translate("Copy Creator Address"));
      		copy_Creator_Address.addActionListener(new ActionListener()
      		{
      			public void actionPerformed(ActionEvent e) 
      			{
      				int row = jTable2.getSelectedRow();
      				row = jTable2.convertRowIndexToModel(row);
      				      				
      				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      				StringSelection value = new StringSelection(personModel.get_Creator_Account(row));
      			    clipboard.setContents(value, null);
      			}
      		});
      		menu.add(copy_Creator_Address);
      		
      		
      		
      		JMenuItem Send_Coins_item_Menu = new JMenuItem(Lang.getInstance().translate("Send Coins"));
      		Send_Coins_item_Menu.addActionListener(new ActionListener()
      		{
      			public void actionPerformed(ActionEvent e) 
      			{
      				
      				int row = jTable2.getSelectedRow();
      				row = jTable2.convertRowIndexToModel(row);
      				Account account = personModel.getAccount(row);
      				
      				new Account_Send_Dialog(null, null,account, null);
      				
      				
      				
      			}
      		});
      		menu.add(Send_Coins_item_Menu);

      		JMenuItem Send_Mail_item_Menu = new JMenuItem(Lang.getInstance().translate("Send Mail"));
      		Send_Mail_item_Menu.addActionListener(new ActionListener()
      		{
      			public void actionPerformed(ActionEvent e) 
      			{
      				
      				int row = jTable2.getSelectedRow();
      				row = jTable2.convertRowIndexToModel(row);
      				Account account = personModel.getAccount(row);
      				
      				new Mail_Send_Dialog(null,null,account,null);
      				
      				
      			
      				
      			}
      		});
      		menu.add(Send_Mail_item_Menu);
      		
        	
      		
      		////////////////////
      		TableMenuPopupUtil.installContextMenu(jTable2, menu);  // SELECT ROW ON WHICH CLICKED RIGHT BUTTON
      	
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }// </editor-fold>                        

    private void name_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void gender_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        // TODO add your handling code here:
    }                                                 

    private void race_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void key_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              


    // Variables declaration - do not modify                     
    private javax.swing.JLabel Skin_Color_jLabel;
    private javax.swing.JTextField birth_Latitude_jTextField;
    private javax.swing.JTextField birth_Longitude_jTextField;
    private javax.swing.JTextField birthday_jTextField;
    private javax.swing.JTextField deathday_jTextField;
    private javax.swing.JTextPane description_jTextPane;
    private javax.swing.JTextField eye_Color_jTextField;
    private javax.swing.JTextField gender_jTextField;
    private javax.swing.JTextField hair_Сolor_jTextField;
    private javax.swing.JTextField height_jTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField13;
    public javax.swing.JLabel key_jLabel;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JTextField name_jTextField;
    private javax.swing.JTextField race_jTextField;
    private javax.swing.JTextField skin_Color_jTextField;
    // End of variables declaration                   
}

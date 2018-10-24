package org.erachain.gui.items.records;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.erachain.controller.Controller;
import org.erachain.core.item.unions.UnionCls;
import org.erachain.core.transaction.Transaction;
import org.erachain.datachain.DCSet;
import org.erachain.gui.Split_Panel;
import org.erachain.gui.library.MTable;
import org.erachain.gui.library.library;
import org.erachain.gui.models.Debug_Transactions_Table_Model;
import org.erachain.gui.transaction.TransactionDetailsFactory;
import org.erachain.lang.Lang;
import org.erachain.utils.TableMenuPopupUtil;

//import java.awt.ScrollPaneLayout;
//import java.awt.la

@SuppressWarnings("serial")
public class Records_UnConfirmed_Panel extends JPanel // JPanel

{

    private static Records_UnConfirmed_Panel instance;
    private Debug_Transactions_Table_Model transactionsModel;
    private MTable transactionsTable;
    private Records_UnConfirmed_Panel th;

    public Records_UnConfirmed_Panel() {
        th = this;
        setName(Lang.getInstance().translate("Unconfirmed Records"));
        // this.parent = parent;
        this.setLayout(new GridBagLayout());
        // this.setLayout(new ScrollPaneLayout());
        // ScrollPaneLayout

        // PADDING
        // this.setBorder(new EmptyBorder(10, 10, 10, 10));
        // this.setSize(500, 500);
        // this.setLocation(20, 20);
        // this.setMaximizable(true);
        // this.setTitle(Lang.getInstance().translate("Accounts"));
        // this.setClosable(true);
        // this.setResizable(true);
        // this.setBorder(true);

        // TABLE GBC
        GridBagConstraints tableGBC = new GridBagConstraints();
        tableGBC.fill = GridBagConstraints.BOTH;
        tableGBC.anchor = GridBagConstraints.NORTHWEST;
        tableGBC.weightx = 1;
        tableGBC.weighty = 1;
        tableGBC.gridx = 1;
        tableGBC.gridy = 1;

        // TRANSACTIONS
        this.transactionsModel = new Debug_Transactions_Table_Model();
        this.transactionsTable = new MTable(this.transactionsModel);
        /*
         * //TRANSACTIONS SORTER Map<Integer, Integer> indexes = new
         * TreeMap<Integer, Integer>();
         * indexes.put(WalletTransactionsTableModel.COLUMN_CONFIRMATIONS,
         * TransactionMap.TIMESTAMP_INDEX);
         * indexes.put(WalletTransactionsTableModel.COLUMN_TIMESTAMP,
         * TransactionMap.TIMESTAMP_INDEX);
         * indexes.put(WalletTransactionsTableModel.COLUMN_CREATOR,
         * TransactionMap.ADDRESS_INDEX);
         * indexes.put(WalletTransactionsTableModel.COLUMN_AMOUNT,
         * TransactionMap.AMOUNT_INDEX); CoreRowSorter sorter = new
         * CoreRowSorter(transactionsModel, indexes);
         * transactionsTable.setRowSorter(sorter);
         *
         * //Custom renderer for the String column; //RenderingHints.
         * this.transactionsTable.setDefaultRenderer(Long.class, new
         * Renderer_Right()); // set renderer //
         * this.transactionsTable.setDefaultRenderer(String.class, new
         * Renderer_Left(this.transactionsTable.getFontMetrics(this.
         * transactionsTable.getFont()),transactionsModel.get_Column_AutoHeight(
         * ))); // set renderer
         * this.transactionsTable.setDefaultRenderer(Boolean.class, new
         * Renderer_Boolean()); // set renderer
         * this.transactionsTable.setDefaultRenderer(Double.class, new
         * Renderer_Right()); // set renderer
         * this.transactionsTable.setDefaultRenderer(Integer.class, new
         * Renderer_Right()); // set renderer
         *
         * this.transactionsTable.setSelectionMode(ListSelectionModel.
         * SINGLE_SELECTION );
         *
         * TableColumn column_Size =
         * this.transactionsTable.getColumnModel().getColumn(
         * WalletTransactionsTableModel.COLUMN_SIZE);
         * column_Size.setMinWidth(50); column_Size.setMaxWidth(1000);
         * column_Size.setPreferredWidth(70);
         *
         * TableColumn column_Confirm =
         * this.transactionsTable.getColumnModel().getColumn(
         * WalletTransactionsTableModel.COLUMN_CONFIRMATIONS);//.COLUMN_SIZE);
         * column_Confirm.setMinWidth(50); column_Confirm.setMaxWidth(1000);
         * column_Confirm.setPreferredWidth(70);
         *
         * TableColumn column_Fee =
         * this.transactionsTable.getColumnModel().getColumn(
         * WalletTransactionsTableModel.COLUMN_FEE);//.COLUMN_SIZE);
         * column_Fee.setMinWidth(80); column_Fee.setMaxWidth(1000);
         * column_Fee.setPreferredWidth(80);
         *
         * TableColumn column_Date =
         * this.transactionsTable.getColumnModel().getColumn(
         * WalletTransactionsTableModel.COLUMN_TIMESTAMP);//.COLUMN_FEE);//.
         * COLUMN_SIZE); column_Date.setMinWidth(120);
         * column_Date.setMaxWidth(1000); column_Date.setPreferredWidth(120);
         */
        // TRANSACTION DETAILS
        this.transactionsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // GET ROW
                    // int row = transactionsTable.getSelectedRow();
                    // row = transactionsTable.convertRowIndexToModel(row);

                    // GET TRANSACTION
                    // Transaction transaction =
                    // transactionsModel.get(row);

                    // SHOW DETAIL SCREEN OF TRANSACTION
                    // TransactionDetailsFactory.getInstance().createTransactionDetail(transaction);
                }
            }
        });

        Split_Panel record_stpit = new Split_Panel("");
        record_stpit.toolBar_LeftPanel.setVisible(false);
        record_stpit.jToolBar_RightPanel.setVisible(false);
        record_stpit.searchToolBar_LeftPanel.setVisible(false);

        // Dimension size = MainFrame.getInstance().desktopPane.getSize();
        // this.setSize(new
        // Dimension((int)size.getWidth()-100,(int)size.getHeight()-100));
        // record_stpit.jSplitPanel.setDividerLocation((int)(size.getWidth()/1.618));

        // show
        // record_stpit.jTable_jScrollPanel_LeftPanel.setModel(transactionsModel);
        record_stpit.jTable_jScrollPanel_LeftPanel = transactionsTable;
        record_stpit.jTable_jScrollPanel_LeftPanel.isFontSet();

        record_stpit.jScrollPanel_LeftPanel.setViewportView(record_stpit.jTable_jScrollPanel_LeftPanel);

        // обработка изменения положения курсора в таблице
        record_stpit.jTable_jScrollPanel_LeftPanel.getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @SuppressWarnings({"unused"})
                    @Override
                    public void valueChanged(ListSelectionEvent arg0) {
                        String dateAlive;
                        String date_birthday;
                        String message;
                        // устанавливаем формат даты
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy"); // HH:mm");
                        // создаем объект персоны
                        UnionCls union;
                        if (record_stpit.jTable_jScrollPanel_LeftPanel.getSelectedRow() >= 0) {

                            // GET ROW
                            int row = record_stpit.jTable_jScrollPanel_LeftPanel.getSelectedRow();
                            try {
                                row = record_stpit.jTable_jScrollPanel_LeftPanel.convertRowIndexToModel(row);
                            

                            // GET TRANSACTION
                            Transaction transaction = transactionsModel.getTransaction(row);
                            // SHOW DETAIL SCREEN OF TRANSACTION
                            // TransactionDetailsFactory.getInstance().createTransactionDetail(transaction);

                            JPanel panel = new JPanel();
                            panel.setLayout(new GridBagLayout());
                            // panel.setBorder(javax.swing.BorderFactory.createLineBorder(new
                            // java.awt.Color(0, 0, 0)));

                            // TABLE GBC
                            GridBagConstraints tableGBC = new GridBagConstraints();
                            tableGBC.fill = GridBagConstraints.BOTH;
                            tableGBC.anchor = GridBagConstraints.FIRST_LINE_START;
                            tableGBC.weightx = 1;
                            tableGBC.weighty = 1;
                            tableGBC.gridx = 0;
                            tableGBC.gridy = 0;
                            panel.add(TransactionDetailsFactory.getInstance().createTransactionDetail(transaction),
                                    tableGBC);
                            JLabel jLabel9 = new JLabel();
                            jLabel9.setText("");
                            GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 1;
                            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
                            panel.add(jLabel9, gridBagConstraints);

                            record_stpit.jScrollPane_jPanel_RightPanel.setViewportView(panel);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                record_stpit.jScrollPane_jPanel_RightPanel.setViewportView(null);
                            }
                        }
                    }
                });

        this.add(record_stpit, tableGBC);

        JPopupMenu menu = new JPopupMenu();

        JMenuItem item_Rebroadcast = new JMenuItem(Lang.getInstance().translate("Rebroadcast"));

        item_Rebroadcast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // code Rebroadcast

                int row = record_stpit.jTable_jScrollPanel_LeftPanel.getSelectedRow();
                row = record_stpit.jTable_jScrollPanel_LeftPanel.convertRowIndexToModel(row);
                Transaction trans = transactionsModel.getTransaction(row);
                DCSet dcSet = DCSet.getInstance();
                trans.setDC(dcSet, Transaction.FOR_NETWORK, DCSet.getInstance().getBlockMap().size() + 1, 1);
                if (trans.isValid(Transaction.FOR_NETWORK, 0) == Transaction.VALIDATE_OK)
                    Controller.getInstance().broadcastTransaction(trans);

            }
        });

        menu.add(item_Rebroadcast);
        JMenuItem item_Delete = new JMenuItem(Lang.getInstance().translate("Delete"));
        item_Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // code delete
                int row = record_stpit.jTable_jScrollPanel_LeftPanel.getSelectedRow();
                row = record_stpit.jTable_jScrollPanel_LeftPanel.convertRowIndexToModel(row);
                Transaction trans = transactionsModel.getTransaction(row);
                DCSet.getInstance().getTransactionMap().delete(trans);

            }
        });

        menu.add(item_Delete);
        

        // save jsot transactions
        JMenuItem item_Save = new JMenuItem(Lang.getInstance().translate("Save"));
        item_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = record_stpit.jTable_jScrollPanel_LeftPanel.getSelectedRow();
                row = record_stpit.jTable_jScrollPanel_LeftPanel.convertRowIndexToModel(row);
                Transaction trans = transactionsModel.getTransaction(row);
                if (trans == null) return;
                // save
                library.saveTransactionJSONtoFileSystem(getParent(), trans);
            }

            
        });
        menu.add(item_Save);
        
        TableMenuPopupUtil.installContextMenu(record_stpit.jTable_jScrollPanel_LeftPanel, menu);

        // this.add(this.transactionsTable);

        transactionsModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent arg0) {
                // TODO Auto-generated method stub
                th.setName(Lang.getInstance().translate("Unconfirmed Records:" + transactionsModel.getRowCount()));
            }

        });

    }

    public static Records_UnConfirmed_Panel getInstance() {

        if (instance == null) {
            instance = new Records_UnConfirmed_Panel();
        }

        return instance;

    }

}
package org.erachain.gui.items.assets;

import org.erachain.core.item.assets.AssetCls;
import org.erachain.database.SortableList;
import org.erachain.gui.SplitPanel;
import org.erachain.gui.library.MTable;
import org.erachain.gui.models.BalanceFromAddressTableModel;
import org.erachain.lang.Lang;
import org.erachain.utils.TableMenuPopupUtil;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class MyBalanceTab extends SplitPanel {

    /**
     *
     */

    private static final long serialVersionUID = 1L;
    final MTable table;
    protected int row;
    BalanceFromAddressTableModel BalancesModel;
    private SortableList<Tuple2<String, Long>, Tuple3<BigDecimal, BigDecimal, BigDecimal>> balances;
    private MyBalanceTab th;

    @SuppressWarnings({"null", "unchecked", "rawtypes"})
    public MyBalanceTab() {
        super("MyBalanceTab");
        th = this;
        this.setName(Lang.getInstance().translate("My Balance"));
        searthLabel_SearchToolBar_LeftPanel.setText(Lang.getInstance().translate("Search") + ":  ");
        // not show buttons
        button1ToolBarLeftPanel.setVisible(false);
        button2ToolBarLeftPanel.setVisible(false);
        jButton1_jToolBar_RightPanel.setVisible(false);
        jButton2_jToolBar_RightPanel.setVisible(false);

        //TABLE

        BalancesModel = new BalanceFromAddressTableModel();
        table = new MTable(BalancesModel);


        //assetsModel.getAsset(row)
        //POLLS SORTER
        RowSorter sorter = new TableRowSorter(BalancesModel);
        table.setRowSorter(sorter);
//	Map<Integer, Integer> indexes = new TreeMap<Integer, Integer>();
//	CoreRowSorter sorter = new CoreRowSorter(assetsModel, indexes);
//	table.setRowSorter(sorter);

        //CHECKBOX FOR DIVISIBLE
//	TableColumn divisibleColumn = table.getColumnModel().getColumn(WalletItemAssetsTableModel.COLUMN_DIVISIBLE);
//	divisibleColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));

        //CHECKBOX FOR CONFIRMED
//	TableColumn confirmedColumn = table.getColumnModel().getColumn(WalletItemAssetsTableModel.COLUMN_CONFIRMED);
//	confirmedColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));

        //CHECKBOX FOR FAVORITE
//	TableColumn favoriteColumn = table.getColumnModel().getColumn(WalletItemAssetsTableModel.COLUMN_FAVORITE);
//	favoriteColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));


// column #1
        TableColumn column1 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_ASSET_KEY);//.COLUMN_CONFIRMED);
        column1.setMinWidth(1);
        column1.setMaxWidth(1000);
        column1.setPreferredWidth(20);
        column1.setWidth(20);


        // column #1
        TableColumn column2 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_A);//.COLUMN_CONFIRMED);
        column2.setMinWidth(50);
        column2.setMaxWidth(1000);
        column2.setPreferredWidth(50);


        // column #1
        TableColumn column3 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_B);//.COLUMN_CONFIRMED);
        column3.setMinWidth(50);
        column3.setMaxWidth(1000);
        column3.setPreferredWidth(50);

        // column #1
        TableColumn column4 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_C);//.COLUMN_CONFIRMED);
        column4.setMinWidth(50);
        column4.setMaxWidth(1000);
        column4.setPreferredWidth(50);


        // column #1
        TableColumn column5 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_ASSET_NAME);//.COLUMN_CONFIRMED);
        column5.setMinWidth(50);
        column5.setMaxWidth(1000);
        column5.setPreferredWidth(150);
	
/*		// column #1
		TableColumn column4 = table.getColumnModel().getColumn(BalanceFromAddressTableModel.COLUMN_ACCOUNT);//.COLUMN_KEY);//.COLUMN_CONFIRMED);
		column4.setMinWidth(50);
		column4.setMaxWidth(1000);
		column4.setPreferredWidth(50);
		column4.setCellRenderer(new RendererRight());
*/


// add listener
//		jTableJScrollPanelLeftPanel.getSelectionModel().addListSelectionListener(table);
// show	
        this.jTableJScrollPanelLeftPanel.setModel(BalancesModel);
        this.jTableJScrollPanelLeftPanel = table;
        jTableJScrollPanelLeftPanel.getSelectionModel().addListSelectionListener(new search_listener());
        jScrollPanelLeftPanel.setViewportView(jTableJScrollPanelLeftPanel);

        // UPDATE FILTER ON TEXT CHANGE
        searchTextField_SearchToolBar_LeftPanel.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            public void onChange() {

                // GET VALUE
                String search = searchTextField_SearchToolBar_LeftPanel.getText();

                // SET FILTER
                BalancesModel.fireTableDataChanged();
                RowFilter filter = RowFilter.regexFilter(".*" + search + ".*", 1);
                ((DefaultRowSorter) sorter).setRowFilter(filter);
                BalancesModel.fireTableDataChanged();

            }
        });


        //MENU
        JPopupMenu assetsMenu = new JPopupMenu();

        assetsMenu.addAncestorListener(new AncestorListener() {


            @Override
            public void ancestorAdded(AncestorEvent arg0) {
                // TODO Auto-generated method stub
                row = table.getSelectedRow();
                if (row < 1) {
                    assetsMenu.disable();
                }

                row = table.convertRowIndexToModel(row);


            }

            @Override
            public void ancestorMoved(AncestorEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void ancestorRemoved(AncestorEvent arg0) {
                // TODO Auto-generated method stub

            }


        });


        JMenuItem sell = new JMenuItem(Lang.getInstance().translate("To sell"));
        sell.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssetCls asset = BalancesModel.getAsset(row);
                String account = BalancesModel.getAccount(row);
                //AssetPairSelect a = new AssetPairSelect(asset.getKey(), "To sell", account);
                new ExchangeFrame(asset, null, "To sell", account);


            }
        });


        JMenuItem excahge = new JMenuItem(Lang.getInstance().translate("Exchange"));
        excahge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssetCls asset = BalancesModel.getAsset(row);
                //	new AssetPairSelect(asset.getKey(), "","");
                new ExchangeFrame(asset, null, "", "");
            }
        });
        assetsMenu.add(excahge);


        JMenuItem buy = new JMenuItem(Lang.getInstance().translate("Buy"));
        buy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssetCls asset = BalancesModel.getAsset(row);
                //		new AssetPairSelect(asset.getKey(), "Buy","");
                new ExchangeFrame(asset, null, "Buy", "");
            }
        });

        assetsMenu.addSeparator();
        assetsMenu.add(buy);

        assetsMenu.add(sell);
        assetsMenu.addSeparator();

        assetsMenu.addPopupMenuListener(new PopupMenuListener() {


                                            @Override
                                            public void popupMenuCanceled(PopupMenuEvent arg0) {
                                                // TODO Auto-generated method stub

                                            }

                                            @Override
                                            public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
                                                // TODO Auto-generated method stub

                                            }

                                            @Override
                                            public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
                                                // TODO Auto-generated method stub

                                                row = table.getSelectedRow();
                                                row = table.convertRowIndexToModel(row);
                                                Class<? extends Object> order = BalancesModel.getColumnClass(row);

                                                //IF ASSET CONFIRMED AND NOT ERM
			/*
				favorite.setVisible(true);
				//CHECK IF FAVORITES
				if(Controller.getInstance().isItemFavorite(order))
				{
					favorite.setText(Lang.getInstance().translate("Remove Favorite"));
				}
				else
				{
					favorite.setText(Lang.getInstance().translate("Add Favorite"));
				}
				/*	
				//this.favoritesButton.setPreferredSize(new Dimension(200, 25));
				this.favoritesButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						onFavoriteClick();
					}
				});	
				this.add(this.favoritesButton, labelGBC);
				*/


                                            }

                                        }

        );


        JMenuItem details = new JMenuItem(Lang.getInstance().translate("Details"));
        details.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //		AssetCls asset = assetsModel.getAsset(row);
//			new AssetFrame(asset);
            }
        });
//	assetsMenu.add(details);
        JMenuItem dividend = new JMenuItem(Lang.getInstance().translate("Pay dividend"));
        dividend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //		AssetCls asset = assetsModel.getAsset(row);
                //		new PayDividendFrame(asset);
            }
        });
        assetsMenu.add(dividend);
     //   table.setComponentPopupMenu(assetsMenu);
        TableMenuPopupUtil.installContextMenu(table, assetsMenu);  // SELECT ROW ON WHICH CLICKED RIGHT BUTTON


        //MOUSE ADAPTER
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                table.setRowSelectionInterval(row, row);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                table.setRowSelectionInterval(row, row);
	/*		
			if(e.getClickCount() == 2)
			{
				row = table.convertRowIndexToModel(row);
				AssetCls asset = assetsModel.getAsset(row);
				new AssetFrame(asset);
			}
			if(e.getClickCount() == 1 & e.getButton() == e.BUTTON1)
			{
				
				if (table.getSelectedColumn() == WalletItemAssetsTableModel.COLUMN_FAVORITE){
					row = table.convertRowIndexToModel(row);
					AssetCls asset = orderModel.getAsset(row);
					favoriteSet( table);
					
					
					
				}
				
				
			}
			*/
            }
        });


    }

    public void onIssueClick() {
        new IssueAssetFrame();
    }

    public void onAllClick() {
        new AllAssetsFrame();
    }

    public void onMyOrdersClick() {
        new MyOrdersFrame();
    }

    public void favorite_set(JTable assetsTable) {


        int row = assetsTable.getSelectedRow();
        row = assetsTable.convertRowIndexToModel(row);

//Order order = ordersModel.getOrder(row);
//new AssetPairSelect(asset.getKey());
/*
if(order.getKey() >= AssetCls.INITIAL_FAVORITES)
{
	//CHECK IF FAVORITES
	if(Controller.getInstance().isItemFavorite(asset))
	{
		
		Controller.getInstance().removeItemFavorite(asset);
	}
	else
	{
		
		Controller.getInstance().addItemFavorite(asset);
	}
		

	assetsTable.repaint();

}
*/
    }

    //listener select row
    class search_listener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent arg0) {
            AssetCls asset = null;
            if (table.getSelectedRow() >= 0)
                asset = BalancesModel.getAsset(table.convertRowIndexToModel(table.getSelectedRow()));
            if (asset == null) return;
            //AssetDetailsPanel001 info_panel = new AssetDetailsPanel001(asset);
            //info_panel.setPreferredSize(new Dimension(jScrollPaneJPanelRightPanel.getSize().width-50,jScrollPaneJPanelRightPanel.getSize().height-50));
            int div = th.jSplitPanel.getDividerLocation();
            int or = th.jSplitPanel.getOrientation();
            AssetInfo info_panel = new AssetInfo(asset, false);
            //info_panel.setPreferredSize(new Dimension(jScrollPaneJPanelRightPanel.getSize().width-50,jScrollPaneJPanelRightPanel.getSize().height-50));
            jScrollPaneJPanelRightPanel.setViewportView(info_panel);
            //jSplitPanel.setRightComponent(info_panel);
            jSplitPanel.setDividerLocation(div);
            jSplitPanel.setOrientation(or);

        }
    }

}

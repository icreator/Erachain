package org.erachain.gui.items.assets;

import org.erachain.controller.Controller;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.core.item.assets.Order;
import org.erachain.core.item.assets.Trade;
import org.erachain.core.transaction.CreateOrderTransaction;
import org.erachain.core.transaction.Transaction;
import org.erachain.database.SortableList;
import org.erachain.datachain.DCSet;
import org.erachain.gui.MainFrame;
import org.erachain.gui.library.IssueConfirmDialog;
import org.erachain.gui.library.MTable;
import org.erachain.gui.transaction.CreateOrderDetailsFrame;
import org.erachain.lang.Lang;
import org.erachain.utils.Pair;
import org.erachain.utils.TableMenuPopupUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class EchangeSellBuyPanel extends JTabbedPane {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public CreateOrderPanel buyOrderPanel;
    String action;
    String account;
    private AssetCls have;
    private AssetCls want;
    private SellOrdersTableModel sellOrdersTableModel;
    private BuyOrdersTableModel buyOrdersTableModel;
    private TradesTableModel tradesTableModel;
    private CreateOrderPanel sellOrderPanel;
    private JPopupMenu sellOrdersMenu = new JPopupMenu();
    private JPopupMenu buyOrdersMenu = new JPopupMenu();
    private JPanel jPanel_Trade;
    private JPanel jPanel_History;

    public EchangeSellBuyPanel(AssetCls have2, AssetCls want2, String action2, String account2) {
        // TODO Auto-generated constructor stub

        this.action = action2;
        this.have = have2;
        this.want = want2;
        this.account = account2;
        new javax.swing.JTabbedPane();
        jPanel_Trade = new javax.swing.JPanel();
        jPanel_History = new javax.swing.JPanel();

        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_Trade.setLayout(new java.awt.GridBagLayout());

        // LABEL GBC
        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.insets = new Insets(0, 5, 5, 0);
        labelGBC.fill = GridBagConstraints.HORIZONTAL;
        labelGBC.anchor = GridBagConstraints.NORTHWEST;
        labelGBC.weightx = 0;

        // ORDER GBC
        GridBagConstraints orderGBC = new GridBagConstraints();
        orderGBC.insets = new Insets(0, 5, 5, 0);
        orderGBC.fill = GridBagConstraints.BOTH;
        orderGBC.anchor = GridBagConstraints.NORTHWEST;
        orderGBC.weightx = 1;
        orderGBC.gridy = 2;

        // TABLE GBC
        GridBagConstraints tableGBC = new GridBagConstraints();
        tableGBC.insets = new Insets(0, 5, 5, 0);
        tableGBC.fill = GridBagConstraints.BOTH;
        tableGBC.anchor = GridBagConstraints.NORTHWEST;
        tableGBC.weightx = 1;
        tableGBC.weighty = 1;
        tableGBC.gridy = 4;

        // CREATE TITLE LABEL
        JLabel lblTitle = new JLabel(Lang.getInstance().translate("Buy %have%").replace("%have%", this.have.toString())
                .replace("%want%", this.want.toString()));// this.have.toString()
        // + " / " +
        // this.want.toString());

        // lblTitle.setFont(new Font("Serif", Font.PLAIN, 18));
        // jPanel_Trade.add(lblTitle, labelGBC);
        if (action == "To sell")
            lblTitle.setVisible(false);

        // CREATE BUY LABEL
        labelGBC.gridy = 1;
        JLabel lblBuy = new JLabel(Lang.getInstance().translate("Sell %want%").replace("%have%", this.have.toString())
                .replace("%want%", this.want.toString()));
        // lblBuy.setFont(new Font("Serif", Font.PLAIN, 18));
        // jPanel_Trade.add(lblBuy, labelGBC);
        if (action == "To sell")
            lblBuy.setVisible(false);

        // CREATE SELL LABEL

        labelGBC.gridy = 0;
        labelGBC.gridx = 1;
        if (action == "To sell")
            labelGBC.gridx = 0;

        // CREATE TITLE LABEL
        JLabel lblTitle1 = new JLabel(Lang.getInstance().translate("Sell %have%")
                .replace("%have%", this.have.toString()).replace("%want%", this.want.toString()));

        // lblTitle1.setFont(new Font("Serif", Font.PLAIN, 18));
        // jPanel_Trade.add(lblTitle1, labelGBC);
        if (action == "Buy")
            lblTitle1.setVisible(false);

        labelGBC.gridy = 1;

        JLabel lblSell = new JLabel(Lang.getInstance().translate("Buy %want%").replace("%have%", this.have.toString())
                .replace("%want%", this.want.toString()));

        // lblSell.setFont(new Font("Serif", Font.PLAIN, 18));
        // jPanel_Trade.add(lblSell, labelGBC);
        if (action == "Buy")
            lblSell.setVisible(false);

        // CREATE BUY PANEL
        buyOrderPanel = new CreateOrderPanel(this.want, this.have, true, account);
        jPanel_Trade.add(buyOrderPanel, orderGBC);
        // buyOrderPanel.setBackground(Color.BLUE);
        if (action == "To sell")
            buyOrderPanel.setVisible(false);

        // CREATE SELL PANEL
        orderGBC.gridx = 1;

        sellOrderPanel = new CreateOrderPanel(this.have, this.want, false, account);

        // sellOrderPanel.setBackground(Color.BLUE);

        orderGBC.fill = GridBagConstraints.BOTH;
        if (action == "To sell") {
            orderGBC.gridx = 0;
            orderGBC.fill = GridBagConstraints.BOTH;

        }

        jPanel_Trade.add(sellOrderPanel, orderGBC);
        if (action == "Buy")
            sellOrderPanel.setVisible(false);

        // sellOrderPanel.setPreferredSize(new
        // Dimension((int)sellOrderPanel.getPreferredSize().getWidth(),
        // (int)buyOrderPanel.getPreferredSize().getHeight()));
        // sellOrderPanel.setMinimumSize(new
        // Dimension((int)sellOrderPanel.getPreferredSize().getWidth(),
        // (int)buyOrderPanel.getPreferredSize().getHeight()));

        orderGBC.fill = GridBagConstraints.BOTH;

        // CREATE SELL ORDERS LABEL
        labelGBC.gridx = 0;
        labelGBC.gridy = 3;
        JLabel lblSellOrders = new JLabel(Lang.getInstance().translate("Sell Orders"));
        // lblSellOrders.setFont(new Font("Serif", Font.PLAIN, 18));
        jPanel_Trade.add(lblSellOrders, labelGBC);
        if (action == "To sell")
            lblSellOrders.setVisible(false);

        // CREATE BUY ORDERS LABEL
        labelGBC.gridx = 1;
        if (action == "To sell")
            labelGBC.gridx = 0;
        JLabel lblBuyOrders = new JLabel(Lang.getInstance().translate("Buy Orders"));
        // lblBuyOrders.setFont(new Font("Serif", Font.PLAIN, 18));
        jPanel_Trade.add(lblBuyOrders, labelGBC);
        if (action == "Buy")
            lblBuyOrders.setVisible(false);

        // CREATE SELL ORDERS TABLE
        this.sellOrdersTableModel = new SellOrdersTableModel(this.have, this.want);
        final MTable sellOrdersTable = new MTable(this.sellOrdersTableModel);

        // ORDER INFO
        JMenuItem orderInfo = new JMenuItem(Lang.getInstance().translate("Order info"));
        orderInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sellOrdersTableModel.getSortableList().isEmpty())
                    return;

                int row = sellOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                row = sellOrdersTable.convertRowIndexToModel(row);

                Order order = sellOrdersTableModel.getOrder(row);
                
                Transaction createOrder = DCSet.getInstance().getTransactionFinalMap().get(order.getId());

                IssueConfirmDialog dd = new IssueConfirmDialog(MainFrame.getInstance(), true, createOrder,
                        (int) (MainFrame.getInstance().getWidth() / 1.2),
                        (int) (MainFrame.getInstance().getHeight() / 1.2),
                        "");

                CreateOrderDetailsFrame ww = new CreateOrderDetailsFrame((CreateOrderTransaction) createOrder);
                dd.jScrollPane1.setViewportView(ww);
                dd.setLocationRelativeTo(null);
                dd.setVisible(true);

            }
        });
        sellOrdersMenu.add(orderInfo);

        // MENU on MY ORDERS
        JMenuItem trades = new JMenuItem(Lang.getInstance().translate("Trades"));
        trades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sellOrdersTableModel.getSortableList().isEmpty())
                    return;

                int row = sellOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                row = sellOrdersTable.convertRowIndexToModel(row);

                Order order = sellOrdersTableModel
                        .getOrder(row);
                new TradesFrame(order);
            }
        });
        sellOrdersMenu.add(trades);

        JMenuItem cancel = new JMenuItem(Lang.getInstance().translate("Cancel"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sellOrdersTableModel.getSortableList().isEmpty())
                    return;
                int row = sellOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                int row1 = sellOrdersTable.convertRowIndexToModel(row);

                Order order = sellOrdersTableModel.getOrder(row1);
                new CancelOrderFrame(order);
            }
        });
        sellOrdersMenu.add(cancel);
    //    sellOrdersTable.setComponentPopupMenu(sellOrdersMenu);
        TableMenuPopupUtil.installContextMenu(sellOrdersTable, sellOrdersMenu);  // SELECT ROW ON WHICH CLICKED RIGHT BUTTON


        sellOrdersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MTable target = (MTable) e.getSource();
                int row = target.getSelectedRow();

                if (row > sellOrdersTableModel.orders.size())
                    return;

                Pair<Long, Order> rowObj = sellOrdersTableModel.orders.get(row);
                if (rowObj == null)
                    return;
                Order order = rowObj.getB();
                if (order == null)
                    return;

                if (Controller.getInstance().isAddressIsMine(order.getCreator().getAddress()))
                    sellOrdersMenu.getComponent(1).setEnabled(true);
                else
                    sellOrdersMenu.getComponent(1).setEnabled(false);

                if (e.getClickCount() == 2) {

                    if (row < sellOrdersTableModel.orders.size()) {
                        buyOrderPanel.txtAmountHave.setText(order.getAmountHaveLeft().toPlainString());
                        // MAKE BIG SCALE
                        buyOrderPanel.txtPrice.setText(Order.calcPrice(order.getAmountHave(), order.getAmountWant(), 2).toPlainString());

                    }
                }
            }
        });

        JScrollPane sellScrollPane = new JScrollPane(sellOrdersTable);

        jPanel_Trade.add(sellScrollPane, tableGBC);

        if (action == "To sell")
            sellScrollPane.setVisible(false);

        // CREATE BUY ORDERS TABLE
        tableGBC.gridx = 1;
        if (action == "To sell")
            tableGBC.gridx = 0;
        this.buyOrdersTableModel = new BuyOrdersTableModel(this.want, this.have);
        final MTable buyOrdersTable = new MTable(this.buyOrdersTableModel);

        buyOrdersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MTable target = (MTable) e.getSource();
                int row = target.getSelectedRow();

                if (row > buyOrdersTableModel.orders.size())
                    return;

                Pair<Long, Order> rowObj = buyOrdersTableModel.orders.get(row);
                if (rowObj == null)
                    return;
                Order order = rowObj
                        .getB();
                if (order == null)
                    return;

                if (Controller.getInstance().isAddressIsMine(order.getCreator().getAddress()))
                    buyOrdersMenu.getComponent(1).setEnabled(true);
                else
                    buyOrdersMenu.getComponent(1).setEnabled(false);

                if (e.getClickCount() == 2) {
                    sellOrderPanel.txtAmountHave.setText(order.getAmountWant().toPlainString());
                    // MAKE BIG SCALE
                    sellOrderPanel.txtPrice.setText(Order.calcPrice(order.getAmountWant(), order.getAmountHave(), 2).toPlainString());
                }
            }
        });
        
        // ORDER INFO
        JMenuItem buyOrderInfo = new JMenuItem(Lang.getInstance().translate("Order info"));
        buyOrderInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buyOrdersTableModel.getSortableList().isEmpty())
                    return;

                int row = buyOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                row = buyOrdersTable.convertRowIndexToModel(row);

                Order order = buyOrdersTableModel.getOrder(row);
                Transaction createOrder = DCSet.getInstance().getTransactionFinalMap().get(order.getId());

                IssueConfirmDialog dd = new IssueConfirmDialog(MainFrame.getInstance(), true, createOrder,
                        (int) (MainFrame.getInstance().getWidth() / 1.2),
                        (int) (MainFrame.getInstance().getHeight() / 1.2),
                        "");

                CreateOrderDetailsFrame ww = new CreateOrderDetailsFrame((CreateOrderTransaction) createOrder);
                dd.jScrollPane1.setViewportView(ww);
                dd.setLocationRelativeTo(null);
                dd.setVisible(true);

            }
        });
        buyOrdersMenu.add(buyOrderInfo);

        // MENU on MY ORDERS
        JMenuItem buyTrades = new JMenuItem(Lang.getInstance().translate("Trades"));
        buyTrades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SortableList<Long, Order> sl = buyOrdersTableModel.getSortableList();
                if (sl.isEmpty())
                    return;

                int row = buyOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                row = buyOrdersTable.convertRowIndexToModel(row);

                Order order = buyOrdersTableModel.getOrder(row);
                if (order != null)
                    new TradesFrame(order);
            }
        });
        buyOrdersMenu.add(buyTrades);
        JMenuItem buyCancel = new JMenuItem(Lang.getInstance().translate("Cancel"));
        buyCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SortableList<Long, Order> sl = buyOrdersTableModel.getSortableList();
                if (sl.isEmpty())
                    return;
                int row = buyOrdersTable.getSelectedRow();
                if (row < 0)
                    return;
                row = buyOrdersTable.convertRowIndexToModel(row);

                Order order = buyOrdersTableModel.getOrder(row);
                new CancelOrderFrame(order);
            }
        });
        buyOrdersMenu.add(buyCancel);
  //      buyOrdersTable.setComponentPopupMenu(buyOrdersMenu);
        TableMenuPopupUtil.installContextMenu(buyOrdersTable, buyOrdersMenu);  // SELECT ROW ON WHICH CLICKED RIGHT BUTTON


        JScrollPane buyScrollPane = new JScrollPane(buyOrdersTable);
        jPanel_Trade.add(buyScrollPane, tableGBC);

        if (action == "Buy")
            buyScrollPane.setVisible(false);

        addTab(Lang.getInstance().translate("Trade"), jPanel_Trade);

        jPanel_History.setLayout(new java.awt.GridBagLayout());

        // CREATE TRADE HISTORY LABEL
        labelGBC.gridx = 0;
        labelGBC.gridy = 0;
        JLabel lblTradeHistory = new JLabel(Lang.getInstance().translate("Trade History"));
        // lblTradeHistory.setFont(new Font("Serif", Font.PLAIN, 18));
        jPanel_History.add(lblTradeHistory, labelGBC);

        // CREATE TRADE HISTORY TABLE
        tableGBC.gridx = 0;
        tableGBC.gridy = 1;
        tableGBC.gridwidth = 2;
        this.tradesTableModel = new TradesTableModel(this.have, this.want);
        final MTable tradesTable = new MTable(this.tradesTableModel);

        ////
        tradesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MTable target = (MTable) e.getSource();
                int row = target.getSelectedRow();

                if (row > tradesTableModel.getSortableList().size())
                    return;

                Trade trade = tradesTableModel.getTrade(row);
                if (trade == null)
                    return;

                DCSet db = DCSet.getInstance();
                Order initiator = Order.getOrder(db, trade.getInitiator());
                boolean type = initiator.getHave() == have.getKey();

                if (e.getClickCount() == 2) {

                    if (type) {
                        BigDecimal price = trade.calcPriceRevers();
                        sellOrderPanel.txtAmountHave.setText(trade.getAmountWant().toPlainString());
                        sellOrderPanel.txtPrice.setText(price.toPlainString());

                        buyOrderPanel.txtAmountHave.setText(trade.getAmountWant().toPlainString());
                        buyOrderPanel.txtPrice.setText(price.toPlainString());
                    } else {
                        BigDecimal price = trade.calcPrice();
                        sellOrderPanel.txtAmountHave.setText(trade.getAmountHave().toPlainString());
                        sellOrderPanel.txtPrice.setText(price.toPlainString());

                        buyOrderPanel.txtAmountHave.setText(trade.getAmountHave().toPlainString());
                        buyOrderPanel.txtPrice.setText(price.toPlainString());
                    }
                }
            }
        });

        ////
        jPanel_History.add(new JScrollPane(tradesTable), tableGBC);
        addTab(Lang.getInstance().translate("Trade History"), jPanel_History);

    }

}
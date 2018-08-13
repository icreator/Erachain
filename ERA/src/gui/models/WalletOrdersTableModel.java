package gui.models;

import controller.Controller;
import core.account.Account;
import core.item.assets.Order;
import datachain.DCSet;
import datachain.SortableList;
import lang.Lang;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;
import org.mapdb.Fun.Tuple5;
import utils.DateTimeFormat;
import utils.ObserverMessage;
import utils.Pair;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class WalletOrdersTableModel extends TableModelCls<Tuple2<String, Long>, Order> implements Observer {
    public static final int COLUMN_TIMESTAMP = 0;
    public static final int COLUMN_AMOUNT = 1;
    public static final int COLUMN_HAVE = 2;
    public static final int COLUMN_WANT = 3;
    public static final int COLUMN_PRICE = 4;
    public static final int COLUMN_FULFILLED = 5;
    public static final int COLUMN_CREATOR = 6;
    public static final int COLUMN_STATUS = 7;
    
    int start =0,step=100;

    private SortableList<Tuple2<String, Long>, Order> orders;
    List<Pair<Tuple2<String, Long>, Order>> pp = new ArrayList<Pair<Tuple2<String, Long>, Order>>();
    private String[] columnNames = Lang.getInstance().translate(new String[]{"Timestamp", "Amount", "Have", "Want", "Price", "Fulfilled", "Creator", "Status"});

    public WalletOrdersTableModel() {
        Controller.getInstance().addWalletListener(this);
    }

    @Override
    public SortableList<Tuple2<String, Long>, Order> getSortableList() {
        return this.orders;
    }

    @Override
    public Class<? extends Object> getColumnClass(int c) {     // set column type
        Object o = getValueAt(0, c);
        return o == null ? Null.class : o.getClass();
    }

    public Order getOrder(int row) {
        return this.pp.get(row).getB();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int index) {
        return this.columnNames[index];
    }

    @Override
    public int getRowCount() {
        //	 return this.orders.size();
        return (this.pp == null) ? 0 : this.pp.size();

    }

    @Override
    public Object getValueAt(int row, int column) {
        if (this.pp == null || row > this.pp.size() - 1) {
            return null;
        }

        Order order = this.pp.get(row).getB();
        //order.setDC(DCSet.getInstance());

        switch (column) {
            case COLUMN_TIMESTAMP:

                return DateTimeFormat.timestamptoString(order.getId());

            case COLUMN_HAVE:

                return DCSet.getInstance().getItemAssetMap().get(order.getHave()).getShort();

            case COLUMN_WANT:

                return DCSet.getInstance().getItemAssetMap().get(order.getWant()).getShort();

            case COLUMN_AMOUNT:

                return order.getAmountHave().toPlainString();

            case COLUMN_PRICE:

                return order.getPrice();

            case COLUMN_FULFILLED:

                return order.getFulfilledHave().toPlainString();

            case COLUMN_CREATOR:

                return order.getCreator().getPersonAsString();

            case COLUMN_STATUS:

                if (order.getAmountHave().compareTo(order.getFulfilledHave()) == 0) {
                    return "DONE";
                } else {

                    if (DCSet.getInstance().getCompletedOrderMap().contains(order.getId()))
                        return "Canceled";

                    if (DCSet.getInstance().getOrderMap().contains(order.getId()))
                        return "ACTIVE";

                    return "unconfirmed";

                }

        }

        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            this.syncUpdate(o, arg);
        } catch (Exception e) {
            //GUI ERROR
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void syncUpdate(Observable o, Object arg) {
        ObserverMessage message = (ObserverMessage) arg;

        //CHECK IF NEW LIST
        if (message.getType() == ObserverMessage.WALLET_RESET_ORDER_TYPE
                || message.getType() == ObserverMessage.WALLET_LIST_ORDER_TYPE) {
            if (this.orders == null) {
                this.orders = (SortableList<Tuple2<String, Long>, Order>) message.getValue();
                this.orders.registerObserver();
            }
            getInterval(start,step);
            this.fireTableDataChanged();
        } else if (message.getType() == ObserverMessage.WALLET_ADD_ORDER_TYPE) {
            //CHECK IF LIST UPDATED
            Pair<Tuple2<String, Long>, Order> item = (Pair<Tuple2<String, Long>, Order>) message.getValue();
            this.orders.add(0, item);
            getInterval(start,step);
            //this.fireTableRowsInserted(0, 0);

        } else if (message.getType() == ObserverMessage.WALLET_REMOVE_ORDER_TYPE) {
            //CHECK IF LIST UPDATED
            this.orders.remove(0);
            getInterval(start,step);
            if (false) {
                if (this.orders.size() > 3) {
                    this.fireTableRowsDeleted(0, 0);
                } else {
                    this.fireTableDataChanged();
                }
            }
           
        }
       
    }

    public void removeObservers() {
        this.orders.removeObserver();
        Controller.getInstance().deleteWalletObserver(this);
    }

    @Override
    public Object getItem(int k) {
        // TODO Auto-generated method stub
        return this.orders.get(k).getB();
    }
    
    public void getInterval(int start,int step){
        this.start = start;
        this.step = step;
       // pp.c.clear();
        int end = start+step;
        if (end > orders.size()) end = orders.size();
        pp = this.orders.subList(start, end);
        
    }
    public void setInterval(int start, int step){
        getInterval(start,step);
    }
    
    
}

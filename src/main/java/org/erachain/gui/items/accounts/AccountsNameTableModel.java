package org.erachain.gui.items.accounts;

import org.erachain.controller.Controller;
import org.erachain.core.account.Account;
import org.erachain.database.SortableList;
import org.erachain.gui.ObserverWaiter;
import org.erachain.gui.models.SortedListTableModelCls;
import org.erachain.utils.ObserverMessage;
import org.erachain.utils.Pair;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;
import org.mapdb.Fun.Tuple5;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class AccountsNameTableModel extends SortedListTableModelCls<String, Tuple2<String, String>> implements ObserverWaiter {
    public static final int COLUMN_ADDRESS = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_DESCRIPTION = 3;
    public static final int COLUMN_PERSON = 4;
    public final int COLUMN_NO = 0;

    private Account accountCLS;
    private Pair<String, Tuple2<String, String>> account;

    public AccountsNameTableModel() {
        super(Controller.getInstance().wallet.database.getAccountsPropertisMap(),
                new String[]{"No.", "Account", "Name", "Description", "Person"},
                new Boolean[]{true, false, false, false}, false);

        addObservers();

    }

    public AccountsNameTableModel(String[] columnNames) {
        super(new String[]{"No.", "Account", "Name", "Description", "Person"},
                new Boolean[]{true, false, false, false}, false);

        addObservers();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (this.listSorted == null || row > this.listSorted.size() - 1) {
            return null;
        }

        account = this.listSorted.get(row);
        if (account == null) {
            return null;
        }
        accountCLS = new Account(account.getA());


        Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>> balance;
        Tuple3<BigDecimal, BigDecimal, BigDecimal> unconfBalance;
        String str;

        JSONObject answer;
        switch (column) {
            case COLUMN_ADDRESS:
                return account.getA();
            case COLUMN_NAME:
                return account.getB().a;
            case COLUMN_PERSON:
                return accountCLS.viewPerson();
			/*
		case COLUMN_WAINTING_BALANCE:
			if (this.asset == null) return "-";
			balance = account.getBalance(this.asset.getKey(DLSet.getInstance()));
			unconfBalance = account.getUnconfirmedBalance(this.asset.getKey(DLSet.getInstance()));
			str = NumberAsString.getInstance().numberAsString(unconfBalance.a.subtract(balance.a))
					+ "/" + unconfBalance.b.subtract(balance.b).toPlainString()
					+ "/" + unconfBalance.c.subtract(balance.c).toPlainString();
			return str;
			 
		case COLUMN_FEE_BALANCE:
			if (this.asset == null) return "-";
			return account.getBalanceUSE(Transaction.FEE_KEY);
*/

            case COLUMN_NO:
                return row + 1;
            case COLUMN_DESCRIPTION:

                answer = (JSONObject) JSONValue.parse(account.getB().b);
                answer = answer == null ? new JSONObject() : answer;
                // set papams
                if (answer.containsKey("description")) {
                    return answer.get("description");
                }
                return "";
			/*

		case COLUMN_GENERATING_BALANCE:

			if(this.asset == null || this.asset.getKey() == AssetCls.FEE_KEY)
			{
				return  NumberAsString.getInstance().numberAsString(account.getGeneratingBalance());
			}
			else
			{
				return NumberAsString.getInstance().numberAsString(BigDecimal.ZERO);
			}
			 */

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public synchronized void syncUpdate(Observable o, Object arg) {
        ObserverMessage message = (ObserverMessage) arg;

        if (message.getType() == ObserverMessage.WALLET_ACCOUNT_PROPERTIES_LIST) {

            needUpdate = false;
            getInterval();
            this.fireTableDataChanged();

        } else if (message.getType() == ObserverMessage.WALLET_ACCOUNT_PROPERTIES_ADD) {

            needUpdate = false;
            getInterval();
            this.fireTableDataChanged();

        } else if (message.getType() == ObserverMessage.WALLET_ACCOUNT_PROPERTIES_DELETE) {

            needUpdate = false;
            getInterval();
            this.fireTableDataChanged();
        }

    }

    @Override
    public void getInterval() {
        this.listSorted = new SortableList<String, Tuple2<String, String>>(map, map.getKeys());
        this.listSorted.sort();
    }

    public void addObservers() {
        if (Controller.getInstance().doesWalletDatabaseExists()) {
            map.addObserver(this);
        } else {
            // ожидаем открытия кошелька
            Controller.getInstance().wallet.addWaitingObserver(this);
        }
    }

    public void deleteObservers() {
        if (Controller.getInstance().doesWalletDatabaseExists()) {
            map.deleteObserver(this);
        }
    }
}

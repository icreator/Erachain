package org.erachain.gui.models;

import org.erachain.controller.Controller;
import org.erachain.core.naming.Name;
import org.erachain.core.naming.NameSale;
import org.erachain.database.SortableList;
import org.erachain.database.wallet.NameSaleMap;
import org.erachain.utils.ObserverMessage;
import org.erachain.utils.Pair;
import org.mapdb.Fun.Tuple2;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class NameSalesComboBoxModel extends DefaultComboBoxModel<NameSale> implements Observer {

    private SortableList<Tuple2<String, String>, BigDecimal> nameSales;

    public NameSalesComboBoxModel() {
        Controller.getInstance().addWalletListener(this);
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
        if (message.getType() == ObserverMessage.LIST_NAME_SALE_TYPE) {
            if (this.nameSales == null) {
                this.nameSales = (SortableList<Tuple2<String, String>, BigDecimal>) message.getValue();
                this.nameSales.registerObserver();
                this.nameSales.sort(NameSaleMap.NAME_INDEX);
            }

            this.onDataChanged();
        }

        //CHECK IF LIST UPDATED
        if (message.getType() == ObserverMessage.ADD_NAME_SALE_TYPE || message.getType() == ObserverMessage.REMOVE_NAME_SALE_TYPE) {
            this.onDataChanged();
        }
		
		/*9ObserverMessage message = (ObserverMessage) arg;
		
		if(message.getType() == ObserverMessage.LIST_NAME_TYPE)
		{
			//GET SELECTED ITEM
			Name selected = (Name) this.getSelectedItem();
						
			//EMPTY LIST
			this.removeAllElements();
				
			//INSERT ALL ACCOUNTS
			List<Pair<Account, Name>> names =  (List<Pair<Account, Name>>) message.getValue();
			for(Pair<Account, Name> name: names)
			{
				this.addElement(name.getB());
			}
				
			//RESET SELECTED ITEM
			if(this.getIndexOf(selected) != -1)
			{
				this.setSelectedItem(selected);
			}
		}*/
    }

    public void onDataChanged() {
        //GET SELECTED ITEM
        Name selected = (Name) this.getSelectedItem();

        //EMPTY LIST
        this.removeAllElements();

        //INSERT ALL ACCOUNTS
        for (Pair<Tuple2<String, String>, BigDecimal> entry : this.nameSales) {
            NameSale nameSale = new NameSale(entry.getA().b, entry.getB());
            this.addElement(nameSale);
        }

        //RESET SELECTED ITEM
        if (this.getIndexOf(selected) != -1) {
            this.setSelectedItem(selected);
        }
    }
}

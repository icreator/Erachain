package org.erachain.gui.items.persons;

import org.erachain.controller.Controller;
import org.erachain.core.item.persons.PersonCls;
import org.erachain.datachain.DCSet;
import org.erachain.datachain.SortableList;
import org.erachain.gui.models.TableModelCls;
import org.erachain.lang.Lang;
import org.mapdb.Fun.Tuple2;
import org.erachain.utils.ObserverMessage;

import javax.validation.constraints.Null;
import java.util.*;

@SuppressWarnings("serial")
public class Persons_Favorite_TableModel extends TableModelCls<Tuple2<String, String>, PersonCls> implements Observer {
    public static final int COLUMN_KEY = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS = 2;
    public static final int COLUMN_CONFIRMED = 3;
    public static final int COLUMN_FAVORITE = 4;

    private List<PersonCls> persons;


    private String[] columnNames = Lang.getInstance().translate(new String[]{"Key", "Name", "Publisher", "Confirmed", "Favorite"});
    private Boolean[] column_AutuHeight = new Boolean[]{false, true, true, false, false};

    public Persons_Favorite_TableModel() {
        super.COLUMN_FAVORITE = COLUMN_FAVORITE;
        addObservers();


        //addObservers();
        //fill((Set<Long>) Controller.getInstance().wallet.database.getPersonFavoritesSet());


    }

    @Override
    public SortableList<Tuple2<String, String>, PersonCls> getSortableList() {
        return null;
    }

    // читаем колонки которые изменяем высоту
    public Boolean[] get_Column_AutoHeight() {

        return this.column_AutuHeight;
    }

    // устанавливаем колонки которым изменить высоту
    public void set_get_Column_AutoHeight(Boolean[] arg0) {
        this.column_AutuHeight = arg0;
    }

    public Class<? extends Object> getColumnClass(int c) {     // set column type
        Object o = getValueAt(0, c);
        return o == null ? Null.class : o.getClass();
    }

    public PersonCls getItem(int row) {
        return this.persons.get(row);

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
        if (persons == null) return 0;
        return this.persons.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (this.persons == null || row > this.persons.size() - 1) {
            return null;
        }

        PersonCls person = this.persons.get(row);
        if (person == null)
            return null;


        switch (column) {
            case COLUMN_KEY:

                return person.getKey(DCSet.getInstance());

            case COLUMN_NAME:

                return person.viewName();

            case COLUMN_ADDRESS:

                return person.getOwner().getPersonAsString();

            case COLUMN_CONFIRMED:

                return person.isConfirmed();

            case COLUMN_FAVORITE:

                return person.isFavorite();

        }

        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        //	try
        //	{
        this.syncUpdate(o, arg);
        //	}
        //	catch(Exception e)
        //	{
        //GUI ERROR
        //	}
    }

    @SuppressWarnings("unchecked")
    public synchronized void syncUpdate(Observable o, Object arg) {
        ObserverMessage message = (ObserverMessage) arg;

        int type = message.getType();
        //CHECK IF NEW LIST
        if (type == ObserverMessage.LIST_PERSON_FAVORITES_TYPE && persons == null) {
            persons = new ArrayList<PersonCls>();
            fill((Set<Long>) message.getValue());
            fireTableDataChanged();
        } else if (type == ObserverMessage.ADD_PERSON_FAVORITES_TYPE) {
            persons.add(Controller.getInstance().getPerson((long) message.getValue()));
            fireTableDataChanged();
        } else if (type == ObserverMessage.DELETE_PERSON_FAVORITES_TYPE) {
            persons.remove(Controller.getInstance().getPerson((long) message.getValue()));
            fireTableDataChanged();
        }

    }

    public void fill(Set<Long> set) {

        //	persons.clear();

        PersonCls person;
        for (Long s : set) {

            if ( s < 1)
                continue;

            person = Controller.getInstance().getPerson(s);
            if (person == null)
                continue;

            persons.add(person);

        }


    }


    public void removeObservers() {

        Controller.getInstance().wallet.database.getPersonFavoritesSet().deleteObserver(this);

    }

    public void addObservers() {

        Controller.getInstance().wallet.database.getPersonFavoritesSet().addObserver(this);
    }


}
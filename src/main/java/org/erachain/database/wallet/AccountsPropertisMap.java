package org.erachain.database.wallet;

import org.erachain.dbs.DBTab;
import org.erachain.dbs.DCUMapImpl;
import org.erachain.utils.ObserverMessage;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;

// <Account, Tuple2<Title,JSON_String>>
public class AccountsPropertisMap extends DCUMapImpl<String, Tuple2<String, String>> {
    public static final int NAME_INDEX = 1;
    public static final int OWNER_INDEX = 2;
    static Logger LOGGER = LoggerFactory.getLogger(AccountsPropertisMap.class.getName());
    BTreeMap<String, String> titleIndex;

    public AccountsPropertisMap(DWSet dWSet, DB database) {
        super(dWSet, database);

        if (databaseSet.isWithObserver()) {
            this.observableData.put(DBTab.NOTIFY_LIST, ObserverMessage.WALLET_ACCOUNT_PROPERTIES_LIST);
            this.observableData.put(DBTab.NOTIFY_ADD, ObserverMessage.WALLET_ACCOUNT_PROPERTIES_ADD);
            this.observableData.put(DBTab.NOTIFY_REMOVE, ObserverMessage.WALLET_ACCOUNT_PROPERTIES_DELETE);
        }
    }

    @Override
    protected void getMap() {
        // OPEN MAP
        map = database.createTreeMap("accounts_propertis_map")
                //.keySerializer(BTreeKeySerializer.STRING)
                //.valueSerializer(new NameSerializer())
                .counterEnable()
                .makeOrGet();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getMemoryMap() {
        map = new TreeMap<String, Tuple2<String, String>>(Fun.COMPARATOR);
    }

    @Override
    protected Tuple2<String, String> getDefaultValue() {
        return null;
    }

}

package org.erachain.database.wallet;

import org.mapdb.DB;
import org.erachain.utils.ObserverMessage;

import java.util.*;

public class FavoriteItemMap extends Observable {

    protected DWSet dWSet;
    protected SortedSet<Long> itemsSet;

    protected int observerFavorites;

    // favorites init SET
    public FavoriteItemMap(DWSet dWSet, DB database, int observerFavorites,
                           String treeSet, int initialAdd) {
        this.dWSet = dWSet;
        this.observerFavorites = observerFavorites;

        //OPEN MAP
        itemsSet = database.getTreeSet(treeSet + "Favorites");

        for (long i = 1; i <= initialAdd; i++) {
            //CHECK IF CONTAINS ITEM
            if (!itemsSet.contains(i)) {
                add(i);
            }
        }
    }

    public void replace(List<Long> keys) {
        this.itemsSet.clear();
        this.itemsSet.addAll(keys);
        this.dWSet.commit();

        //NOTIFY
        this.notifyFavorites();
    }

    public void add(Long key) {
        this.itemsSet.add(key);
        this.dWSet.commit();

        //NOTIFY
        this.notifyFavorites();
    }

    public void delete(Long key) {
        this.itemsSet.remove(key);
        this.dWSet.commit();

        //NOTIFY
        this.notifyFavorites();
    }

    public boolean contains(Long key) {
        return this.itemsSet.contains(key);
    }

    public long size() {
        return this.itemsSet.size();
    }

    public Collection<Long> getFromToKeys(long fromKey, long toKey) {
        return this.itemsSet.subSet(fromKey, toKey);
    }

    @Override
    public void addObserver(Observer o) {
        //ADD OBSERVER
        super.addObserver(o);

        //NOTIFY LIST
        this.notifyFavorites();
    }

    protected void notifyFavorites() {
        this.setChanged();
        this.notifyObservers(new ObserverMessage(this.observerFavorites, this.itemsSet));
    }
}
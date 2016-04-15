package database;

import java.util.HashMap;
import java.util.Map;

import org.mapdb.Atomic;
import org.mapdb.DB;

//import utils.ObserverMessage;
import database.DBSet;
//import org.mapdb.Serializer;
//import database.serializer.ItemSerializer;
import qora.item.ItemCls;

public abstract class Item_Map extends DBMap<Long, ItemCls> 
{

	protected Map<Integer, Integer> observableData = new HashMap<Integer, Integer>();
	
	protected int type;
	//protected String name;
	//protected Serializer serializer;
	
	protected Atomic.Long atomicKey;
	protected long key;
	
	public Item_Map(DBSet databaseSet, DB database, int type, String name,
			int observeAdd,
			int observeRemove,
			int observeList
			)
	{
		super(databaseSet, database);
		
		this.type = type;
		//this.name = name;
		//this.Serializer = serializer;
		this.atomicKey = database.getAtomicLong(name +"_key");
		this.key = this.atomicKey.get();
		
		this.observableData.put(DBMap.NOTIFY_ADD, observeAdd);
		this.observableData.put(DBMap.NOTIFY_REMOVE, observeRemove);
		this.observableData.put(DBMap.NOTIFY_LIST, observeList);
	}

	public Item_Map(Item_Map parent) 
	{
		super(parent);
		
		this.key = this.getKey();
	}
	
	protected long getKey()
	{
		return this.key;
	}
	
	protected void createIndexes(DB database){}

	/*
	@Override
	protected Map<Long, ItemCls> getMap(DB database, int type, String name) 
	{
		
		// type+name not initialized yet!
		this.type = type;

		//OPEN MAP
		return database.createTreeMap(name)
				.valueSerializer(new ItemSerializer(type))
				.makeOrGet();
	}
	*/

	@Override
	protected Map<Long, ItemCls> getMemoryMap() 
	{
		return new HashMap<Long, ItemCls>();
	}

	@Override
	protected ItemCls getDefaultValue() 
	{
		return null;
	}
	
	@Override
	protected Map<Integer, Integer> getObservableData() 
	{
		return this.observableData;
	}
	
	public long add(ItemCls item)
	{
		//INCREMENT ATOMIC KEY IF EXISTS
		if(this.atomicKey != null)
		{
			this.atomicKey.incrementAndGet();
		}
		
		//INCREMENT KEY
		this.key++;
		
		//INSERT WITH NEW KEY
		this.set(this.key, item);
		
		//RETURN KEY
		return this.key;
	}
	
	
	public void delete(long key)
	{
		super.delete(key);
		
		//DECREMENT ATOMIC KEY IF EXISTS
		if(this.atomicKey != null)
		{
			this.atomicKey.decrementAndGet();
		}
		
		//DECREMENT KEY
		 this.key = key - 1;
	}
}

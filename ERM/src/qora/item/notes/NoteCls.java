package qora.item.notes;


//import database.DBMap;
import database.DBSet;
import database.Item_Map;
import database.wallet.FavoriteItemNote;
import database.IssueItemMap;
import qora.account.Account;

import qora.item.ItemCls;

public abstract class NoteCls extends ItemCls {
	
	protected static final int NOTE = 1;
	protected static final int SAMPLE = 2;
	protected static final int PAPER = 3;
	
	public NoteCls(byte[] typeBytes, Account creator, String name, String description)
	{
		super(typeBytes, creator, name, description);
		
	}
	public NoteCls(int type, Account creator, String name, String description)
	{
		this(new byte[TYPE_LENGTH], creator, name, description);
		this.typeBytes[0] = (byte)type;
	}

	//GETTERS/SETTERS
		
	public String getItemType() { return "note"; }
	
	// DB
	public Item_Map getDBMap(DBSet db)
	{
		return db.getNoteMap();
	}
	public IssueItemMap getDBIssueMap(DBSet db)
	{
		return db.getIssueNoteMap();
	}	
	
	/*
	public long getKey(DBSet db) {
		// TODO if ophran ?
		if (this.key <0) this.key = db.getIssueNoteMap().get(this.reference);
		return this.key;
	}
		
	public boolean isConfirmed(DBSet db) {
		return db.getIssueNoteMap().contains(this.reference);
	}	
	
	public long insertToMap(DBSet db)
	{
		//INSERT INTO DATABASE
		ItemNoteMap dbMap = db.getNoteMap();
		int mapSize = dbMap.size();
		//LOGGER.info("GENESIS MAP SIZE: " + assetMap.size());
		long key = 0l;
		if (mapSize == 0) {
			// initial map set
			dbMap.set(0l, this);
		} else {
			key = dbMap.add(this);
			//this.asset.setKey(key);
		}
		
		//SET ORPHAN DATA
		db.getIssueNoteMap().set(this.reference, key);
		
		return key;
		
	}
	
	public long removeFromMap(DBSet db)
	{
		//DELETE FROM DATABASE
		long key = db.getIssueNoteMap().get(this.reference);
		db.getNoteMap().delete(key);	
				
		//DELETE ORPHAN DATA
		db.getIssueNoteMap().delete(this.reference);
		
		return key;

	}
	
	*/
}

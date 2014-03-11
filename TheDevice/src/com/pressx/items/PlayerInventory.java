package com.pressx.items;

import com.pressx.managers.Sounds;
import com.pressx.managers.Textures;
import com.pressx.thedevice.GameStats;

public class PlayerInventory{
	final int TOTALITEMS = 5;
	
	public int numExperience = 132;//temporary
	
	public Item[] allItems = new Item[TOTALITEMS];
	private boolean[] itemOwned = new boolean[TOTALITEMS];
	public Item item0,item1;
	
	public PlayerInventory(Sounds s, Textures t){
		allItems[0] = new Item_Vortex(s,t);
		allItems[1] = new Item_Mine(s,t);
		allItems[2] = new Item_Gear(s,t);
		allItems[3] = new Item_Control(s,t);
		allItems[4] = new Item_Mine(s,t);
		//for now
		itemOwned[0] = true;
		itemOwned[1] = true;
		item0 = allItems[0];
		item1 = allItems[1];
	}
	
	public void updateItemShopButtons(ShopItem item){
		item.setState(checkItemIsEquipped(item) ? ShopItem.ShopItemState.EQUIPPED : checkItemIsOwned(item) ? ShopItem.ShopItemState.UNLOCKED : ShopItem.ShopItemState.LOCKED);
	}
	public void updateAllItemShopButtons(){
		for(Item item : allItems)
			updateItemShopButtons(item);
	}

	/////Get Info
	public boolean checkLoadoutFull(){
		return item0 != null && item1 != null;
	}
	int getIndexOfItem(ShopItem item){
		for(int i = 0; i < TOTALITEMS; i++)
			if(allItems[i] == item) return i;
		return -1;
		
	}
	public boolean checkItemIsEquipped(ShopItem item){
		return item0 == item || item1 == item;
	}
	public boolean checkItemIsOwned(ShopItem item){
		return itemOwned[getIndexOfItem(item)];
	}
	
	/////Equip/Unequip
	public void equipItem(Item item){
		System.out.println("YAY");
		if(item0 == null)
			item0 = item;
		else if(item1 == null)
			item1 = item;
	}
	public void equipItem(int index,Item item){
		if(index == 0)
			item0 = item;
		else
			item1 = item;
	}
	public void unequipItem(ShopItem item){
		if(item1 == item)
			item1 = null;
		else if(item0 == item)
			item0 = null;
	}
	
	/////Unlock
	public void unlockItem(ShopItem item){
		itemOwned[getIndexOfItem(item)] = true;
	}
	public boolean tryPurchaseItem(ShopItem item){
		if(numExperience < item.getCost()) return false;
		numExperience -= item.getCost();
		unlockItem(item);
		return true;
	}
}

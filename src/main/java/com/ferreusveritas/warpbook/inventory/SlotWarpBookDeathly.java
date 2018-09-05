package com.ferreusveritas.warpbook.inventory;

import com.ferreusveritas.warpbook.inventory.container.InventoryWarpBookSpecial;
import com.ferreusveritas.warpbook.item.DeathlyWarpPageItem;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWarpBookDeathly extends Slot {
	public SlotWarpBookDeathly(InventoryWarpBookSpecial inventorySpecial, int i, int j, int k) {
		super(inventorySpecial, i, j, k);
	}
	
	public static boolean itemValid(ItemStack itemStack) {
		return itemStack.getItem() instanceof DeathlyWarpPageItem;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemValid(itemStack);
	}
	
}

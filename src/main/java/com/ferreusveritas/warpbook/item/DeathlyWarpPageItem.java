package com.ferreusveritas.warpbook.item;

import com.ferreusveritas.warpbook.WarpBookMod;
import com.ferreusveritas.warpbook.core.WarpColors;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DeathlyWarpPageItem extends Item implements IColorable {
	public DeathlyWarpPageItem(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(16);
		setCreativeTab(WarpBookMod.tabBook);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex) {
		switch(tintIndex) {
			case 0: return pageColor();
			case 1: return symbolColor();
			default: return 0x00FFFFFF;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int pageColor() {
		return WarpColors.DEATHLY.getColor();
	}
	
	@SideOnly(Side.CLIENT)
	public int symbolColor() {
		return 0x00BBBBBB;
	}
}

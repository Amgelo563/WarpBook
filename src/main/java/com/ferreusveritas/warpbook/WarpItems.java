package com.ferreusveritas.warpbook;

import java.util.List;

import com.ferreusveritas.warpbook.item.DeathlyWarpPageItem;
import com.ferreusveritas.warpbook.item.LegacyWarpPageItem;
import com.ferreusveritas.warpbook.item.UnboundWarpPageItem;
import com.ferreusveritas.warpbook.item.UnboundWarpPotionItem;
import com.ferreusveritas.warpbook.item.WarpBookItem;
import com.ferreusveritas.warpbook.item.WarpItem;
import com.ferreusveritas.warpbook.item.WarpPageItem;
import com.ferreusveritas.warpbook.item.WarpPotionItem;
import com.ferreusveritas.warpbook.warps.WarpHyper;
import com.ferreusveritas.warpbook.warps.WarpLocus;
import com.ferreusveritas.warpbook.warps.WarpPlayer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

public class WarpItems {
	
	public WarpBookItem warpBookItem;
	public Item warpClusterItem;
	
	public WarpItem playerWarpPageItem;
	public WarpItem hyperWarpPageItem;
	public WarpItem locusWarpPageItem;
	public WarpItem unboundWarpPageItem;
	public WarpItem legacyPageItem;
	public DeathlyWarpPageItem deathlyWarpPageItem;
	
	public WarpItem unboundWarpPotionItem;
	public WarpItem locusWarpPotionItem;
	public WarpItem playerWarpPotionItem;
	public WarpItem hyperWarpPotionItem;
	
	public WarpItems() {
		
		//Misc
		warpBookItem = new WarpBookItem("warpbook");
		warpClusterItem = new WarpItem("warpcluster") {
			@Override public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) { }
		};
		
		//Pages
		unboundWarpPageItem = new UnboundWarpPageItem("unboundwarppage");
		locusWarpPageItem = new WarpPageItem("boundwarppage").setWarp(new WarpLocus()).setCloneable(true);
		playerWarpPageItem = new WarpPageItem("playerwarppage").setWarp(new WarpPlayer()).setCloneable(false);
		hyperWarpPageItem = new WarpPageItem("hyperwarppage").setWarp(new WarpHyper()).setCloneable(true);
		deathlyWarpPageItem = new DeathlyWarpPageItem("deathlywarppage");
		legacyPageItem = new LegacyWarpPageItem("warppage");
		
		//Potions
		unboundWarpPotionItem = new UnboundWarpPotionItem("unboundwarppotion");
		locusWarpPotionItem = new WarpPotionItem("boundwarppotion").setWarp(new WarpLocus()).setCloneable(true);
		playerWarpPotionItem = new WarpPotionItem("playerwarppotion").setWarp(new WarpPlayer()).setCloneable(true);
		hyperWarpPotionItem = new WarpPotionItem("hyperwarppotion").setWarp(new WarpHyper()).setCloneable(true);
	}
	
	public void register(IForgeRegistry<Item> registry) {
		
		//Misc
		registry.register(warpBookItem);
		registry.register(warpClusterItem);
		
		//Pages
		registry.register(unboundWarpPageItem);
		registry.register(locusWarpPageItem);
		registry.register(playerWarpPageItem);
		registry.register(hyperWarpPageItem);
		registry.register(deathlyWarpPageItem);
		registry.register(legacyPageItem);
		
		//Potions
		registry.register(unboundWarpPotionItem);
		registry.register(locusWarpPotionItem);
		registry.register(playerWarpPotionItem);
		registry.register(hyperWarpPotionItem);
		
		ItemBlock itemBlock = new ItemBlock(WarpBookMod.blocks.bookCloner);
		itemBlock.setRegistryName(WarpBookMod.blocks.bookCloner.getRegistryName());
		registry.register(itemBlock);
		
		itemBlock = new ItemBlock(WarpBookMod.blocks.teleporter);
		itemBlock.setRegistryName(WarpBookMod.blocks.teleporter.getRegistryName());
		registry.register(itemBlock);
	}
	
}

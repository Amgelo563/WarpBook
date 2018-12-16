package com.ferreusveritas.warpbook;

import com.ferreusveritas.warpbook.block.BlockBookCloner;
import com.ferreusveritas.warpbook.block.BlockTeleporter;
import com.ferreusveritas.warpbook.tileentity.TileEntityBookCloner;
import com.ferreusveritas.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public BlockBookCloner bookCloner;
	public BlockTeleporter teleporter;
	
	public ModBlocks() {
		bookCloner = new BlockBookCloner();
		teleporter = new BlockTeleporter();
	}
	
	public void register(IForgeRegistry<Block> registry) {
		GameRegistry.registerTileEntity(TileEntityBookCloner.class, new ResourceLocation(ModConstants.MODID, "tileEntityBookCloner"));
		GameRegistry.registerTileEntity(TileEntityTeleporter.class, new ResourceLocation(ModConstants.MODID, "tileEntityTeleporter"));
		
		registry.register(bookCloner);
		registry.register(teleporter);
	}
	
}

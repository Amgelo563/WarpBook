package com.ferreusveritas.warpbook.compat;

import java.util.List;

import com.ferreusveritas.warpbook.block.BlockTeleporter;
import com.ferreusveritas.warpbook.item.WarpItem;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WailaTeleporterHandler implements IWailaDataProvider {
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		NBTTagCompound nbtData = accessor.getNBTData();
		
		//Attempt to get species from server via NBT data
		if(nbtData.hasKey("warpname")) {
			String warpName = nbtData.getString("warpname");
			tooltip.add(warpName);
		}
		
		return tooltip;
	}
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block instanceof BlockTeleporter) {
			BlockTeleporter teleporterBlock = (BlockTeleporter) block;
			ItemStack warpItemStack = teleporterBlock.getWarpItemStack(world, pos);
			if(warpItemStack.getItem() instanceof WarpItem) {
				WarpItem warpItem = (WarpItem) warpItemStack.getItem();
				String warpName = warpItem.getName(world, warpItemStack);
				tag.setString("warpname", warpName);
			}
		}
		
		return tag;
	}
	
}

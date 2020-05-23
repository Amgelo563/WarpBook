package com.ferreusveritas.warpbook.util;

import com.ferreusveritas.warpbook.WarpBook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class WarpUtils {

	public static ItemStack bindItemStackToLocation(ItemStack stack, World world, EntityPlayer player) {
		BlockPos p = player.getPosition();
		Bind(stack, p.getX(), p.getY(),	p.getZ(), player.dimension);
		player.openGui(WarpBook.instance, WarpBook.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		WarpBook.formingPages.put(player, stack);
		return stack;
	}
	
	public static void Bind(ItemStack page, int x, int y, int z, int dim) {
		if (!page.hasTagCompound()) {
			page.setTagCompound(new NBTTagCompound());
		}
		page.getTagCompound().setInteger("posX", x);
		page.getTagCompound().setInteger("posY", y);
		page.getTagCompound().setInteger("posZ", z);
		page.getTagCompound().setInteger("dim", dim);
	}
	
	public static ItemStack bindItemStackToPlayer(ItemStack stack, EntityPlayer toPlayer) {
		stack = new ItemStack(stack.getItem(), stack.getCount());
		stack.setTagCompound(new NBTTagCompound());
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			stack.getTagCompound().setString("playeruuid", toPlayer.getGameProfile().getId().toString());
			stack.getTagCompound().setString("player", toPlayer.getGameProfile().getName());
		}
		return stack;
	}
	
}

package com.ferreusveritas.warpbook.core;

import java.math.RoundingMode;
import java.util.Arrays;

import com.ferreusveritas.warpbook.ModConstants;
import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.net.packet.PacketEffect;
import com.ferreusveritas.warpbook.util.CommandUtils;
import com.ferreusveritas.warpbook.util.MathUtils;
import com.ferreusveritas.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Teleporter;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class WarpDrive {
	
	public boolean queueWarp(EntityPlayer player, ItemStack warpItem) {
		
		if (!player.world.isRemote && warpItem.getItem() instanceof IDeclareWarp) {
			Waypoint wp = ((IDeclareWarp)warpItem.getItem()).getWaypoint(player, warpItem);
			if (wp == null) {
				CommandUtils.showError(player, I18n.format("help.waypointnotexist"));
			}
			
			if (player.getEntityData().hasKey(ModConstants.MODID)) {
				return false;
			}
			
			if (Arrays.asList(WarpBook.disabledDestinations).contains(new Integer(wp.dim))) {
				CommandUtils.showError(player, I18n.format("help.cantgoto"));
				return false;
			}
			if (Arrays.asList(WarpBook.disabledLeaving).contains(new Integer(player.dimension))) {
				CommandUtils.showError(player, I18n.format("help.cantleave"));
				return false;
			}
			
			NBTTagCompound wpData = new NBTTagCompound();
			wp.writeToNBT(wpData);
			player.getEntityData().setTag(ModConstants.MODID, wpData);
			
		}
		
        return true;
	}
	
	
	public void processWarp(EntityPlayer player, Waypoint wp) {
		
		//Setup effect packets
		PacketEffect oldDim = new PacketEffect(true, MathUtils.round(player.posX, RoundingMode.DOWN), MathUtils.round(player.posY, RoundingMode.DOWN), MathUtils.round(player.posZ, RoundingMode.DOWN));
		PacketEffect newDim = new PacketEffect(false, wp.x, wp.y, wp.z);
		NetworkRegistry.TargetPoint oldPoint = new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64);
		NetworkRegistry.TargetPoint newPoint = new NetworkRegistry.TargetPoint(wp.dim, wp.x, wp.y, wp.z, 64);
		
		boolean crossDim = player.dimension != wp.dim;
		
		if (crossDim) {
			if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, wp.dim)) {
				return;
			}
			
			if (player.isServerWorld()) {
				player.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) player, wp.dim, new Teleporter(((EntityPlayerMP) player).getServerWorld()) {
					@Override public void placeInPortal(Entity entityIn, float rotationYaw) { }
					@Override public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) { return true; }
					@Override public boolean makePortal(Entity entityIn) { return true; }
					@Override public void removeStalePortalLocations(long worldTime) { }
				});
				
			}
		}
		
		//Update player
		player.addExhaustion(calculateExhaustion(player.getEntityWorld().getDifficulty(), WarpBook.exhaustionCoefficient, crossDim));
		player.setPositionAndUpdate(wp.x + 0.5, wp.y + 0.5, wp.z + 0.5);

		//Send effect packets
		WarpBook.network.sendToAllAround(oldDim, oldPoint);
		WarpBook.network.sendToAllAround(newDim, newPoint);
	}

	private static float calculateExhaustion(EnumDifficulty difficultySetting, float exhaustionCoefficient, boolean crossDim) {
		float scaleFactor = 0.0f;
		switch (difficultySetting) {
		case EASY:
			scaleFactor = 1.0f;
			break;
		case NORMAL:
			scaleFactor = 1.5f;
			break;
		case HARD:
			scaleFactor = 2.0f;
			break;
		case PEACEFUL:
			scaleFactor = 0.0f;
			break;
		}
		return exhaustionCoefficient * scaleFactor * (crossDim ? 2.0f : 1.0f);
	}
	
}

package com.ferreusveritas.warpbook.event;

import com.ferreusveritas.warpbook.ModConstants;
import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WarpEventHandler {
	
	public static final WarpEventHandler EVENT_HANDLER = new WarpEventHandler();
	
	private WarpEventHandler() {}
	
	@SubscribeEvent
	public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
			if (event.player.getEntityData().hasKey(ModConstants.MODID)) {
				EntityPlayerMP player = (EntityPlayerMP) event.player;
				NBTTagCompound data = event.player.getEntityData().getCompoundTag(ModConstants.MODID);
				Waypoint wp = new Waypoint(data);
				WarpBook.warpDrive.processWarp(player, wp);
				event.player.getEntityData().removeTag(ModConstants.MODID);
			}
		}
	}
}

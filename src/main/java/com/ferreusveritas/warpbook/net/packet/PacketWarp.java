package com.ferreusveritas.warpbook.net.packet;

import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.util.net.NetUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWarp implements IMessage, IMessageHandler<PacketWarp, IMessage> {
	int pageSlot;
	
	public PacketWarp() {
	}
	
	public PacketWarp(int pageSlot) {
		this.pageSlot = pageSlot;
	}
	
	public static ItemStack getPageById(EntityPlayer player, int pageSlot) {
		try {
			NBTTagList stack = WarpBook.lastHeldBooks.get(player).getTagCompound().getTagList("WarpPages", Constants.NBT.TAG_COMPOUND);
			ItemStack page = new ItemStack(stack.getCompoundTagAt(pageSlot));
			return page;
		}
		catch (ClassCastException e) {
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public IMessage onMessage(PacketWarp message, MessageContext ctx) {
		EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
		ItemStack page = getPageById(player, message.pageSlot);
		WarpBook.warpDrive.queueWarp(player, page);
		
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		pageSlot = buffer.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(pageSlot);
	}
	
}

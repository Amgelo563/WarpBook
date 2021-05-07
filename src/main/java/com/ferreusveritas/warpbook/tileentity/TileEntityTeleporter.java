package com.ferreusveritas.warpbook.tileentity;

import com.ferreusveritas.warpbook.core.WarpColors;
import com.ferreusveritas.warpbook.item.WarpItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTeleporter extends TileEntity {
	private ItemStack warpItem;
	private boolean locked;
	
	public TileEntityTeleporter() {
		warpItem = ItemStack.EMPTY;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.write(syncData);
		return new SPacketUpdateTileEntity(this.pos, 1, syncData);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		read(pkt.getNbtCompound());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		read(tag);
	}

	//Packages up the data on the server to send to the client.  Client handles it with handleUpdateTag() which reads it with readFromNBT()
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		write(tag);
		
		return tag;
	}
	
	private void read(NBTTagCompound tag) {
		warpItem = new ItemStack(tag.getCompoundTag("warpitem"));
		if(tag.hasKey("locked", NBT.TAG_BYTE)) {
			locked = tag.getByte("locked") != 0;
		}
	}
	
	private void write(NBTTagCompound tag) {
		if (!warpItem.isEmpty()) {
			NBTTagCompound pageTag = new NBTTagCompound();
			warpItem.writeToNBT(pageTag);
			tag.setTag("warpitem", pageTag);
			tag.setByte("locked", (byte)(locked ? 1 : 0));
		}
	}
	
	public ItemStack getWarpItem() {
		return warpItem;
	}
	
	public void setWarpItem(ItemStack stack) {
		warpItem = stack;
		markDirty();
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void lock() {
		locked = true;
	}
	
	@Override
	public boolean shouldRefresh(World w, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return super.shouldRefresh(world, pos, oldState, newState);
	}
	
	@SideOnly(Side.CLIENT)
	public WarpColors getWarpColor() {
		return warpItem.getItem() instanceof WarpItem ? ((WarpItem) warpItem.getItem()).getWarpColor() : WarpColors.UNBOUND;
	}
	
}

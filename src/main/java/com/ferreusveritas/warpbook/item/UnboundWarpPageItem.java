package com.ferreusveritas.warpbook.item;

import java.util.List;

import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.util.WarpUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnboundWarpPageItem extends WarpPageItem {

	public UnboundWarpPageItem(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			if(!world.isRemote) {
				player.getHeldItemMainhand().shrink(1);
				if (player.getHeldItemMainhand().isEmpty()) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
				}
				ItemStack newPage = WarpUtils.bindItemStackToPlayer(new ItemStack(WarpBook.items.playerWarpPageItem), player);
				EntityItem entityItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, newPage);
				player.world.spawnEntity(entityItem);
			}
		}
		else {
			WarpUtils.bindItemStackToLocation(new ItemStack(WarpBook.items.locusWarpPageItem), world, player);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
	}

}

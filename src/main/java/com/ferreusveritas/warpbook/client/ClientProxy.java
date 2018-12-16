package com.ferreusveritas.warpbook.client;

import com.ferreusveritas.warpbook.Proxy;
import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.item.IColorable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy {
	@Override
	public void registerRenderers() {
				
		//Misc
		regMesh(WarpBook.items.warpBookItem);
		regMesh(WarpBook.items.warpClusterItem);

		//Pages
		regMesh(WarpBook.items.unboundWarpPageItem);
		regMesh(WarpBook.items.locusWarpPageItem);
		regMesh(WarpBook.items.playerWarpPageItem);
		regMesh(WarpBook.items.hyperWarpPageItem);
		regMesh(WarpBook.items.deathlyWarpPageItem);
		for(int i = 0; i < 6; i++) {
			regMesh(WarpBook.items.legacyPageItem, i);
		}

		//Potions
		regMesh(WarpBook.items.unboundWarpPotionItem);
		regMesh(WarpBook.items.locusWarpPotionItem);
		regMesh(WarpBook.items.playerWarpPotionItem);
		regMesh(WarpBook.items.hyperWarpPotionItem);		
		
		regMesh(Item.getItemFromBlock(WarpBook.blocks.bookCloner));
		regMesh(Item.getItemFromBlock(WarpBook.blocks.teleporter));
	
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
				new IBlockColor() {
					@Override
					public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex) {
						return WarpBook.blocks.teleporter.getColor(state, access, pos, tintIndex);
					}
				}
				, new Block[] {WarpBook.blocks.teleporter}
			);
	}
	
	private void regMesh(Item item) {
		regMesh(item, 0);
	}
	
	private void regMesh(Item item, int meta) {
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		
		//Register Color Handler for the item.
		if(item instanceof IColorable) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
					new IItemColor() {
						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex) {
							return ((IColorable) item).getColor(stack, tintIndex);
						}
						
					}
					, new Item[] {item}
				);
		}
	}
	
}

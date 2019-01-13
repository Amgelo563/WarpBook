package com.ferreusveritas.warpbook.block;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.ferreusveritas.warpbook.WarpBook;
import com.ferreusveritas.warpbook.core.WarpColors;
import com.ferreusveritas.warpbook.item.UnboundWarpPotionItem;
import com.ferreusveritas.warpbook.item.WarpItem;
import com.ferreusveritas.warpbook.item.WarpPotionItem;
import com.ferreusveritas.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTeleporter extends Block implements ITileEntityProvider, IColorableBlock {
	protected static final AxisAlignedBB TELEPORTER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 4.0D / 16.0D, 1.0D);
	
	public BlockTeleporter() {
		super(Material.IRON);
		setUnlocalizedName("teleporter");
		setRegistryName("teleporter");
		setCreativeTab(WarpBook.tabBook);
		setSoundType(SoundType.STONE);
		setHardness(10.0f);
		setResistance(20.0f);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTeleporter();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			IBlockState newState = state;
			Optional<TileEntityTeleporter> teleporter = getTeleportTileEntity(world, pos);
			if(teleporter.isPresent()) {
				ItemStack heldItemStack = player.getHeldItem(hand);
				if(player.canPlayerEdit(pos, facing, heldItemStack)) {
					Item heldItem = heldItemStack.getItem();
					if (!heldItemStack.isEmpty()) {
						if(heldItem instanceof WarpPotionItem && !(heldItem instanceof UnboundWarpPotionItem) ) {
							teleporter.get().setWarpItem(player.getHeldItem(hand));
							if(!player.isCreative()) {
								if (player.getHeldItem(hand).getCount() < 1) {
									player.getHeldItem(hand).shrink(1);
								}
								else {
									player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
								}
								player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
							}
						} else
						if(heldItem == Items.WATER_BUCKET) {
							teleporter.get().setWarpItem(ItemStack.EMPTY);
						} else {
							return false;
						}
					}
				}
				world.notifyBlockUpdate(pos, state, newState, 3);
				teleporter.get().markDirty();
			}
		}
		return true;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityPlayer && !worldIn.isBlockPowered(pos) ) {
			Optional<TileEntityTeleporter> tile = getTeleportTileEntity(worldIn, pos);
			if(tile.isPresent()) {
				ItemStack page = tile.get().getWarpItem();
				if (!page.isEmpty()) {
					WarpBook.warpDrive.queueWarp((EntityPlayer)entityIn, page);
				}
			}
		}
	}
	
	protected Optional<TileEntityTeleporter> getTeleportTileEntity(IBlockAccess access, BlockPos pos) {
		TileEntity tile = access.getTileEntity(pos);
		if(tile instanceof TileEntityTeleporter) {
			return Optional.of((TileEntityTeleporter) tile);
		}
		return Optional.empty();
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return TELEPORTER_AABB;
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess access, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack(this, 1, 0);
		NBTTagCompound tag = getNBT(stack);
		
		ItemStack warpItem = getWarpItemStack(access, pos);
		if(!warpItem.isEmpty()) {
			NBTTagCompound warpTag = warpItem.serializeNBT();
			if(warpTag != null) {
				tag.setTag("warpitem", warpTag);
				stack.setTagCompound(tag);
			}
		}
		
		drops.add(stack);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return getWarpEncodedItemStack(world, pos);
	}
	
	protected ItemStack getWarpEncodedItemStack(IBlockAccess access, BlockPos pos) {
		ItemStack stack = new ItemStack(this, 1, 0);
		
		ItemStack warpItem = getWarpItemStack(access, pos);
		if(!warpItem.isEmpty()) {
			NBTTagCompound warpTag = warpItem.serializeNBT();
			if(warpTag != null) {
				NBTTagCompound tag = getNBT(stack);
				tag.setTag("warpitem", warpTag);
				stack.setTagCompound(tag);
			}
		}
		
		return stack;
	}
	
	public NBTTagCompound getNBT(ItemStack itemStack) {
		return itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		
		if(!stack.isEmpty()) {
			ItemStack warpStack = getWarpItemStack(stack);
			if(!warpStack.isEmpty()) {
				Optional<TileEntityTeleporter> tile = getTeleportTileEntity(world, pos);
				if(tile.isPresent()) {
					tile.get().setWarpItem(warpStack);
				}
			}
		}
	
		
	}
	
	/**
 	 * Retrieves the contained itemStack that is presumably a warp item from the tile entity in the
 	 * world.
	 * 
	 * @param access
	 * @param pos
	 * @return
	 */
	public ItemStack getWarpItemStack(IBlockAccess access, BlockPos pos) {
		Optional<TileEntityTeleporter> tile = getTeleportTileEntity(access, pos);
		return tile.isPresent() ? tile.get().getWarpItem() : ItemStack.EMPTY;
	}
	
	/**
	 * Retrieves the contained itemStack that is presumably a warp item from an ItemStack.
	 * 
	 * @param The stack of this block that could contain a warp item
	 * @return The contained warp item or ItemStack.EMPTY if there isn't one
	 */
	public ItemStack getWarpItemStack(ItemStack stack) {
		if(!stack.isEmpty()) {
			NBTTagCompound tag = stack.getTagCompound();
			tag = getNBT(stack);
			NBTTagCompound warpTag = tag.getCompoundTag("warpitem");
			if(warpTag != null) {
				ItemStack warpItemStack = new ItemStack(warpTag);
				if(warpItemStack.getItem() instanceof WarpItem) {
					return warpItemStack;
				}
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex) {
		
		Optional<TileEntityTeleporter> tile = getTeleportTileEntity(access, pos);
		if(tile.isPresent()) {
			WarpColors wc = tile.get().getWarpColor();
			
			if(wc == WarpColors.UNBOUND) {
				return 0xFF888888;
			}
			
			switch (tintIndex) {
				default: return 0xFFFFFFFF;
				case 0: return wc.getColor(); //Base color
				case 1: return wc.getSpecColor(); //Specular color
			}
		}
		
		return 0xFFFFFFFF;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if(face == EnumFacing.DOWN) {
			return BlockFaceShape.SOLID;
		}
		
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		
		ItemStack warpItemStack = getWarpItemStack(stack);
		
		String destName = "§4§kUnbound";
		
		if(!warpItemStack.isEmpty()) {
			Item item = warpItemStack.getItem();
			if(item instanceof WarpItem) {
				WarpItem warpItem = (WarpItem) item;
				destName = "§a" + warpItem.getName(world, warpItemStack);
			}
		}
		
		tooltip.add(destName);
	}
}

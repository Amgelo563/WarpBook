package com.ferreusveritas.warpbook.crafting;

import com.ferreusveritas.warpbook.item.WarpBookItem;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class WarpBookColorShapeless extends ShapelessRecipes {
	ItemStack recipeOutput;
	
	public WarpBookColorShapeless(ItemStack recipeOutput, NonNullList<Ingredient> ingredients) {
		super("", recipeOutput, ingredients);
		this.recipeOutput = recipeOutput;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		
		EnumDyeColor color = null;
		ItemStack book = ItemStack.EMPTY;
		ItemStack water = ItemStack.EMPTY;
		
		try {
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack workingStack = inventory.getStackInSlot(i);
				if(workingStack.getItem() instanceof WarpBookItem) {
					if(!book.isEmpty()) {
						return ItemStack.EMPTY;
					}
					book = workingStack.copy();
				}
				else if( getItemDyeColor(workingStack) != null ) {
					if(color != null || !water.isEmpty()) {
						return ItemStack.EMPTY;
					}
					color = getItemDyeColor(workingStack);
				}
				else if(workingStack.getItem() == Items.WATER_BUCKET) {
					if(color != null || !water.isEmpty() ) {
						return ItemStack.EMPTY;
					}
					water = workingStack.copy();
				}
			}
			
			if(!book.isEmpty()) {
				if(!book.hasTagCompound()) {
					book.setTagCompound(new NBTTagCompound());
				}
				
				if(color != null) {
					book.getTagCompound().setInteger("color", color.getColorValue());
				}
				else if(!water.isEmpty()) {
					book.getTagCompound().removeTag("color");
				}
				
				return book;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ItemStack.EMPTY;
	}
	
	private EnumDyeColor getItemDyeColor(ItemStack stack) {
		String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray",
			"Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"
        };
		
		int[] oreIds = OreDictionary.getOreIDs(stack);
		
		for(int oreId : oreIds) {
			String oreName = OreDictionary.getOreName(oreId);
			for(int dye = 0; dye < 16; dye++) {
				if( oreName.equals("dye" + dyes[dye]) ) {
					return EnumDyeColor.values()[dye];
				}
			}
		}
		
		return null;
	}
	
}
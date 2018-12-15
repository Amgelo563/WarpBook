package com.ferreusveritas.warpbook;

import com.ferreusveritas.warpbook.crafting.WarpBookColorShapeless;
import com.ferreusveritas.warpbook.crafting.WarpPageShapeless;
import com.ferreusveritas.warpbook.item.WarpPageItem;
import com.ferreusveritas.warpbook.item.WarpPotionItem;
import com.ferreusveritas.warpbook.util.JavaUtils;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

public class Crafting {
	
	public void register(IForgeRegistry<IRecipe> registry) {
		
		PotionType awkward = PotionType.REGISTRY.getObject(new ResourceLocation("awkward"));
		
		//Brewing recipe for unbound warp page
		BrewingRecipeRegistry.addRecipe(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), awkward),
				new ItemStack(WarpBookMod.items.warpClusterItem), //Warp Cluster
				new ItemStack(WarpBookMod.items.unboundWarpPotionItem));
		
		//Recipe to copy bound warp page to an unbound page
		copyPageToUnboundPage(registry, (WarpPageItem) WarpBookMod.items.locusWarpPageItem);
		
		//Recipe to copy hyper warp page to an unbound page
		copyPageToUnboundPage(registry, (WarpPageItem) WarpBookMod.items.hyperWarpPageItem);

		//Recipe to apply locus potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.locusWarpPotionItem, (WarpPageItem) WarpBookMod.items.locusWarpPageItem);
		
		//Recipe to apply player potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.playerWarpPotionItem, (WarpPageItem) WarpBookMod.items.playerWarpPageItem);
		
		//Recipe to apply hyper potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.hyperWarpPotionItem, (WarpPageItem) WarpBookMod.items.hyperWarpPageItem);
		
		String[] dyeValues = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
		
		//Recipe to color a warp book cover
		for(EnumDyeColor color : EnumDyeColor.values()) {
			ItemStack dyedBook = new ItemStack(WarpBookMod.items.warpBookItem);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("color", getColorValue(color));
			dyedBook.setTagCompound(tag);
			registry.register(
				new WarpBookColorShapeless(
					dyedBook,//Output
					NonNullList.from(null,
						Ingredient.fromStacks(new ItemStack(WarpBookMod.items.warpBookItem)),
						new OreIngredient("dye" + dyeValues[color.getDyeDamage()])
					)
				).setRegistryName(Properties.modid, "dyeWarpBook_" + color.getDyeDamage())
			);
		}
		
		//Recipe to clear the color from a warp book cover
		ItemStack dyedBook = new ItemStack(WarpBookMod.items.warpBookItem);
		NBTTagCompound tag = new NBTTagCompound();
		//We'll set the washing example to magenta because it's pretty ugly and full of regret. ;)
		tag.setInteger("color", getColorValue(EnumDyeColor.MAGENTA));
		dyedBook.setTagCompound(tag);
		registry.register(
			new WarpBookColorShapeless(
					new ItemStack(WarpBookMod.items.warpBookItem),//Output
				NonNullList.from(null,
					Ingredient.fromStacks(dyedBook),
					Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET))
				)
			).setRegistryName(Properties.modid, "dyeWarpBook_X")
		);
		
	}
	
	public int getColorValue(EnumDyeColor from) {
		return (int) JavaUtils.getRestrictedObject(EnumDyeColor.class, from, "field_193351_w", "colorValue");
	}
	
	private void potionToPaper(IForgeRegistry<IRecipe> registry, WarpPotionItem potionIn, WarpPageItem pageOut) {
		
		if (potionIn.isWarpCloneable(new ItemStack(potionIn))) {
			for(Item paper : new Item[]{Items.PAPER, WarpBookMod.items.unboundWarpPageItem}) {
				String recipeName = upperFirst(pageOut.getRegistryName().getResourcePath()) + 
					"From" + upperFirst(potionIn.getRegistryName().getResourcePath()) + 
					"And" + upperFirst(paper.getRegistryName().getResourcePath());
			
				registry.register(
					new WarpPageShapeless(
						new ItemStack(pageOut),//Output
						NonNullList.from(null,
							Ingredient.fromStacks(new ItemStack(potionIn)),
							Ingredient.fromStacks(new ItemStack(paper))
						)
					).setRegistryName(Properties.modid, recipeName)
				);
			}
		}
	}
	
	private void copyPageToUnboundPage(IForgeRegistry<IRecipe> registry, WarpPageItem page) {
		
		if (page.isWarpCloneable(new ItemStack(page))) {
			String recipeName = upperFirst(page.getRegistryName().getResourcePath()) + "FromCopy";
		
			registry.register(
				new WarpPageShapeless(
					new ItemStack(page, 2),//Output
						NonNullList.from(null,
							Ingredient.fromStacks(new ItemStack(page, 1)),
							Ingredient.fromStacks(new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1)
						)
					)
				).setRegistryName(Properties.modid, recipeName)
			);
		}

	}
	
	private String upperFirst(String stringIn) {
		return stringIn.substring(0,1).toUpperCase() + stringIn.substring(1);
	}
}

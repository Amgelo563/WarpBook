package com.ferreusveritas.warpbook.compat;

import static hunternif.mc.atlas.api.AtlasAPI.getMarkerAPI;
import static hunternif.mc.atlas.api.AtlasAPI.getPlayerAtlases;

import com.ferreusveritas.warpbook.ModConstants;
import hunternif.mc.atlas.registry.MarkerRegistry;
import hunternif.mc.atlas.registry.MarkerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AntiqueAtlasCompat {
  public static final String WARP_BOOK_WAYPOINT_MARKER_PAGE = "warpbook_waypoint_marker";

  public static final String ANTIQUE_ATLAS_ID = "antiqueatlas";

  @SideOnly(Side.CLIENT)
  public static void registerTexture() {
    // Register page's texture
    String pageMarkerPath = ModConstants.MODID + ":textures/gui/antiqueatlas/"+ WARP_BOOK_WAYPOINT_MARKER_PAGE +".png";
    MarkerRegistry.register(
        new MarkerType(
            new ResourceLocation(ANTIQUE_ATLAS_ID, WARP_BOOK_WAYPOINT_MARKER_PAGE),
            new ResourceLocation(pageMarkerPath)
        )
    );
  }

  public static void addMarker(EntityPlayer entityPlayer, String name) {
    int x = (int) entityPlayer.posX;
    int z = (int) entityPlayer.posZ;
    List<Integer> atlases = getPlayerAtlases(entityPlayer);

    for (Integer atlas : atlases) {
      getMarkerAPI().putMarker(entityPlayer.world, true, atlas, WARP_BOOK_WAYPOINT_MARKER_PAGE, name, x, z);
    }
  }
}

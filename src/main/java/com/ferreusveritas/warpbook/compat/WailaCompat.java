package com.ferreusveritas.warpbook.compat;

import com.ferreusveritas.warpbook.block.BlockTeleporter;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		WailaTeleporterHandler teleporterHandler = new WailaTeleporterHandler();
		
		registrar.registerBodyProvider(teleporterHandler, BlockTeleporter.class);
		registrar.registerNBTProvider(teleporterHandler, BlockTeleporter.class);
	}
	
}

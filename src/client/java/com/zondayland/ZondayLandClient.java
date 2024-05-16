package com.zondayland;

import com.zondayland.network.ModClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zondayland.ZondayLand.MOD_ID;

public class ZondayLandClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID + "(client)");

	@Override
	public void onInitializeClient() {
		ModClientPacketHandler.register();
	}
}
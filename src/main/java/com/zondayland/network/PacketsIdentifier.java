package com.zondayland.network;

import com.zondayland.ZondayLand;
import net.minecraft.resources.ResourceLocation;

public class PacketsIdentifier {
    // Server to Client packets
    public static final ResourceLocation CLAIM_SUCCESS = new ResourceLocation(ZondayLand.MOD_ID, "claim_success");
    public static final ResourceLocation UNCLAIM_SUCCESS = new ResourceLocation(ZondayLand.MOD_ID, "unclaim_success");

    public static final ResourceLocation COIN_MACHINE_WHEEL_STEP = new ResourceLocation(ZondayLand.MOD_ID, "coin_machine_wheel_step");
    public static final ResourceLocation COIN_MACHINE_WHEEL_STOP = new ResourceLocation(ZondayLand.MOD_ID, "coin_machine_wheel_stop");
    public static final ResourceLocation COIN_MACHINE_WIN = new ResourceLocation(ZondayLand.MOD_ID, "coin_machine_win");
    public static final ResourceLocation COIN_MACHINE_LOSE = new ResourceLocation(ZondayLand.MOD_ID, "coin_machine_lose");

    // Client to server packets
    public static final ResourceLocation COIN_MACHINE_PLAY = new ResourceLocation(ZondayLand.MOD_ID, "coin_machine_play");
    public static final ResourceLocation BLACKJACK_PLAY = new ResourceLocation(ZondayLand.MOD_ID, "blackjack_play");
}

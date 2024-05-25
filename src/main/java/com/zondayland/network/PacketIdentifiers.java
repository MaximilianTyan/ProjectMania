package com.zondayland.network;

import com.zondayland.ZondayLand;
import net.minecraft.resources.ResourceLocation;

public class PacketIdentifiers {
    public static final ResourceLocation CLAIM_SUCCESS = new ResourceLocation(ZondayLand.MOD_ID, "claim_success");
    public static final ResourceLocation UNCLAIM_SUCCESS = new ResourceLocation(ZondayLand.MOD_ID, "unclaim_success");
}

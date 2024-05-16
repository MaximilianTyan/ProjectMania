package com.zondayland.network;

import com.zondayland.ZondayLand;
import net.minecraft.util.Identifier;

public class PacketIdentifiers {
    public static final Identifier CLAIM_SUCCESS = new Identifier(ZondayLand.MOD_ID, "claim_success");
    public static final Identifier UNCLAIM_SUCCESS = new Identifier(ZondayLand.MOD_ID, "unclaim_success");
}

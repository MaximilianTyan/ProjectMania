package com.zondayland.registry;

import com.zondayland.items.claims.ClaimParchmentItem;
import com.zondayland.items.claims.ServerClaimItem;
import com.zondayland.items.claims.ServerUnclaimItem;
import com.zondayland.items.claims.UnclaimParchmentItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.zondayland.ZondayLand.LOGGER;
import static com.zondayland.ZondayLand.MOD_ID;

public class ModItems {
    public static final Item LOGO = new Item(new FabricItemSettings().maxCount(1));
    public static final Item CLAIM_PARCHMENT = new ClaimParchmentItem(new FabricItemSettings().maxCount(1));
    public static final Item UNCLAIM_PARCHMENT = new UnclaimParchmentItem(new FabricItemSettings().maxCount(1));
    public static final Item SERVER_CLAIM = new ServerClaimItem(new FabricItemSettings().maxCount(1));
    public static final Item SERVER_UNCLAIM = new ServerUnclaimItem(new FabricItemSettings().maxCount(1));

    public static void register() {
        LOGGER.info("Registering items");
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "logo"), LOGO);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "claim_parchment"), CLAIM_PARCHMENT);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "unclaim_parchment"), UNCLAIM_PARCHMENT);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "server_claim"), SERVER_CLAIM);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "server_unclaim"), SERVER_UNCLAIM);
    }
}

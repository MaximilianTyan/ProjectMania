package io.github.maximiliantyan.projectmania.network;

import io.github.maximiliantyan.projectmania.ModMain;
import net.minecraft.resources.ResourceLocation;

public class PacketsIdentifier {

    public class S2C {
        // Server to Client packets
        public static final ResourceLocation CLAIM_SUCCESS = new ResourceLocation(ModMain.MOD_ID, "claim_success");
        public static final ResourceLocation UNCLAIM_SUCCESS = new ResourceLocation(ModMain.MOD_ID, "unclaim_success");

        public static final ResourceLocation COIN_MACHINE_STARTED = new ResourceLocation(ModMain.MOD_ID, "coin_machine_started");
        public static final ResourceLocation COIN_MACHINE_WHEEL_STEP = new ResourceLocation(ModMain.MOD_ID, "coin_machine_wheel_step");
        public static final ResourceLocation COIN_MACHINE_WHEEL_STOP = new ResourceLocation(ModMain.MOD_ID, "coin_machine_wheel_stop");
        public static final ResourceLocation COIN_MACHINE_WIN = new ResourceLocation(ModMain.MOD_ID, "coin_machine_win");
        public static final ResourceLocation COIN_MACHINE_LOSE = new ResourceLocation(ModMain.MOD_ID, "coin_machine_lose");


    }

    public class C2S {
        // Client to server packets
        public static final ResourceLocation COIN_MACHINE_PLAY = new ResourceLocation(ModMain.MOD_ID, "coin_machine_play");

        public static final ResourceLocation BLACKJACK_PLAY = new ResourceLocation(ModMain.MOD_ID, "blackjack_play");
        public static final ResourceLocation BLACKJACK_CONTINUE = new ResourceLocation(ModMain.MOD_ID, "blackjack_continue");
        public static final ResourceLocation BLACKJACK_RETRACT = new ResourceLocation(ModMain.MOD_ID, "blackjack_retract");
    }
}

package io.github.maximiliantyan.projectmania;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;
import java.util.Map;

public class ModConfig extends MidnightConfig {

    @Entry(category = "vote", name = "Vote address")
    public static String voteAddress = "https://projectmania.com/vote";

    @Entry(category = "CoinMachine", name = "Loot Table [item:chance(from 0.0 to 1.0)]")
    public static Map<String, Double> CoinMachineLootTable = Map.ofEntries(
            Map.entry("minecraft:copper_ingot", 50 / 100d),
            Map.entry("minecraft:iron_ingot", 25 / 100d),
            Map.entry("minecraft:gold_ingot", 10 / 100d),
            Map.entry("minecraft:emerald", 5 / 100d),
            Map.entry("minecraft:diamond", 1 / 100d),
            Map.entry("minecraft:netherite_ingot", 0.1 / 100d)
    );

    @Entry(category = "CoinMachine", name = "Valid coins")
    public static List<String> ValidCoins = List.of("minecraft:gold_nugget");

    @Entry(category = "Network", name="Connection read timeout (server & client) [s]")
    public static int readTimeoutSeconds = 120;

    @Entry(category = "Network", name="Login timeout (server) [s]")
    public static int loginTimeoutSeconds = 120;

    @Entry(category = "Network", name="Keep alive packet interval (server) [s]")
    public static int keepAlivePacketIntervalSeconds = 120;

}

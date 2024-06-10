package com.zondayland;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;
import java.util.Map;

public class ModConfig extends MidnightConfig {

    @Entry(category = "CoinMachine", name = "Loot Table")
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

}

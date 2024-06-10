package com.zondayland.utils;

import com.zondayland.mixin.PlayerMixin;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.Level;

@FunctionalInterface
public interface PlayerTicked {
    public void tick(Level level, Player player);
}

package com.zondayland.utils;

import org.spongepowered.asm.mixin.Unique;

public interface PlayerTicksAccessor {
    @Unique
    public void zondayLand$registerTickedClass(PlayerTicked tickedClass);

    @Unique
    public void zondayLand$removeTickedClass(PlayerTicked tickedClass);

    @Unique
    public boolean zondayLand$hasTickedClass(PlayerTicked tickedClass);
}
package io.github.maximiliantyan.projectmania.utils;

import org.spongepowered.asm.mixin.Unique;

public interface PlayerTicksAccessor {
    @Unique
    public void projectmania$registerTickedClass(PlayerTicked tickedClass);

    @Unique
    public void projectmania$removeTickedClass(PlayerTicked tickedClass);

    @Unique
    public boolean projectmania$hasTickedClass(PlayerTicked tickedClass);
}
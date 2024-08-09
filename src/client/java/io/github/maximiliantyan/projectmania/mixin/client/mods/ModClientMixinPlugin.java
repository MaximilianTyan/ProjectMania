package io.github.maximiliantyan.projectmania.mixin.client.mods;

import io.github.maximiliantyan.projectmania.ModClient;
import io.github.maximiliantyan.projectmania.mixin.ModConditionnalMixinPlugin;
import org.slf4j.Logger;

public abstract class ModClientMixinPlugin extends ModConditionnalMixinPlugin {
    @Override
    public Logger getLogger() {
        return ModClient.LOGGER;
    }
}

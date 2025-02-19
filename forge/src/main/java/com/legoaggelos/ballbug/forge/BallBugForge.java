package com.legoaggelos.ballbug.forge;

import dev.architectury.platform.forge.EventBuses;
import com.legoaggelos.ballbug.BallBug;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BallBug.MOD_ID)
public class BallBugForge {
    public BallBugForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(BallBug.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        BallBug.init();
    }
}

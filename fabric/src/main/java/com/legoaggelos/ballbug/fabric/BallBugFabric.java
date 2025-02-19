package com.legoaggelos.ballbug.fabric;

import com.legoaggelos.ballbug.BallBug;
import net.fabricmc.api.ModInitializer;

public class BallBugFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BallBug.init();
    }
}

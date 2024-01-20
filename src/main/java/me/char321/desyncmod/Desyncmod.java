package me.char321.desyncmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;

public class Desyncmod implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {

    }

    static class PosParticle extends SpriteBillboardParticle {

        protected PosParticle(ClientWorld clientWorld, double x, double y, double z) {
            super(clientWorld, x, y, z);
        }

        @Override
        public ParticleTextureSheet getType() {
            return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
        }
    }

}

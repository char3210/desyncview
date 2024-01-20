package me.char321.desyncmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @Shadow private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Inject(method="render",at=@At(value="INVOKE",target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V", ordinal=0))
    public void e(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        if (client.player == null) return;
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        Vec3d pos = camera.getPos();
        ArrowEntity entity = EntityType.ARROW.create(world);
        if (entity == null) {
            return;
        }
        entity.setPos(client.player.getX(), client.player.getY()-0.5, client.player.getZ());
        entity.setVelocityClient(0,-1,0);
        entity.age = 4;
//        entity.kill();
        this.renderEntity(entity, pos.x, pos.y, pos.z, 1, matrices, immediate);
        Entity entity2 = EntityType.EYE_OF_ENDER.create(world);
        if (entity2 == null) {
            return;
        }
        IntegratedServer server = client.getServer();
        if (server == null) return;
        List<Entity> entities = server.getOverworld().getEntities(EntityType.PLAYER, x -> true);
        if (entities.isEmpty()) return;
        Entity serverplayer = entities.get(0);
        entity2.setPos(serverplayer.getX(), serverplayer.getY(), serverplayer.getZ());
        entity2.age = 4;
//        entity.kill();
        if (Math.abs(client.player.getX() - serverplayer.getX()) > 1e-9 ||
                Math.abs(client.player.getZ() - serverplayer.getZ()) > 1e-9) {
            this.renderEntity(entity2, pos.x, pos.y, pos.z, 1, matrices, immediate);
        }

    }
}

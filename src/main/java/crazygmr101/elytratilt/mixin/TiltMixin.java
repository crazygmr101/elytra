package crazygmr101.elytratilt.mixin;

import crazygmr101.elytratilt.Configs;
import crazygmr101.elytratilt.Smoother;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class TiltMixin {

    @ModifyArg(method = "renderWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"
            ),
            index = 0)
    private MatrixStack mixin(MatrixStack matrices, float tickDelta, long limitTime,
                              boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                              LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null)
            return matrices;
        if (camera.isThirdPerson() && ! Configs.third_person_tilt) {
            Smoother.clear();
            return matrices;
        }
        if (player.isFallFlying()) {
            matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));
            matrices.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));
            matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(Smoother.get_new_yaw()));
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));
        }
        return matrices;
    }
}

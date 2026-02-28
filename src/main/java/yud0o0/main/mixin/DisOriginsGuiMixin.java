package yud0o0.main.mixin;

import io.github.apace100.origins.networking.packet.s2c.OpenChooseOriginScreenS2CPacket;
import io.github.apace100.origins.origin.OriginLayerManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OriginLayerManager.class)
public class DisOriginsGuiMixin {

    @Redirect(
            method = "updateData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/api/networking/v1/ServerPlayNetworking;send(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/packet/CustomPayload;)V"
            )
    )
    private void stopOpeningScreen(ServerPlayerEntity player, CustomPayload packet) {
        if (packet instanceof OpenChooseOriginScreenS2CPacket) {
            return;
        }
        ServerPlayNetworking.send(player, packet);
    }
}
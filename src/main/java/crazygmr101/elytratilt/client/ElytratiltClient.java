package crazygmr101.elytratilt.client;

import crazygmr101.elytratilt.Configs;
import crazygmr101.elytratilt.Smoother;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ElytratiltClient implements ClientModInitializer {

    private final KeyBinding clearElytraTilt =
            KeyBindingHelper.registerKeyBinding(
                    new KeyBinding(
                            "key.elytra.clear",
                            InputUtil.Type.KEYSYM,
                            GLFW.GLFW_KEY_HOME,
                            "key.elytra.category"
                    )
            );
    private final KeyBinding toggleThirdPersonTilt =
            KeyBindingHelper.registerKeyBinding(
                    new KeyBinding(
                            "key.elytra.tilt",
                            InputUtil.Type.KEYSYM,
                            GLFW.GLFW_KEY_DELETE,
                            "key.elytra.category"
                    )
            );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    if (client.player != null) {
                        Smoother.add_yaw(client.player.bodyYaw);
                    } else
                        Smoother.clear();
                    while (clearElytraTilt.wasPressed()) {
                        Smoother.clear();
                        client.player.sendMessage(new LiteralText("Levelled elytra"), false);
                    }
                    while (toggleThirdPersonTilt.wasPressed()) {
                        Configs.third_person_tilt = !Configs.third_person_tilt;
                        client.player.sendMessage(new LiteralText(
                                        "3rd person tilt turned " + (Configs.third_person_tilt ? "on" : "off")),
                                false
                        );
                    }
                }
        );
    }
}

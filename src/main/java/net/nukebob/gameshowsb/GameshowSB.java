package net.nukebob.gameshowsb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.nukebob.gameshowsb.screen.GameShowScreen;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Date;

public class GameshowSB implements ClientModInitializer {
	public static final String MOD_ID = "gameshowsb";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static KeyBinding openScreen;
	private static KeyBinding resetTimer;
	private static KeyBinding pauseTimer;
	private static KeyBinding swapDoubleTimer;

	public static int timerLength = 20;
	public static boolean showTimer = false;
	public static boolean listBlocks = false;

	public static int doubleTimerRedLength = 20;
	public static int doubleTimerBlueLength = 20;
	public static boolean showDoubleTimer = false;
	public static boolean redTurn = true;
	public static double doubleTimerRed = 0.0;
	public static double doubleTimerBlue = 0.0;
	public static double doubleTimerStartValue = 0.0;
	public static boolean doubleTimerRunning = false;
	public static Date doubleTimerStarted=new Date();
	public static boolean redLostFirst=true;

	public static boolean timerRunning = false;
	public static Date timerStarted=new Date();
	public static double timerStartValue=0.0;

	public static double timer=0.0;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Gameshow SB hath been loadeth upon thy client");

		openScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gameshowsb.open_screen",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				"category.gameshowsb.game_show"
		));
		resetTimer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gameshowsb.reset_timer",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_Z,
				"category.gameshowsb.game_show"
		));
		pauseTimer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gameshowsb.pause_timer",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_X,
				"category.gameshowsb.game_show"
		));
		swapDoubleTimer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gameshowsb.swap_double_timer",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.gameshowsb.game_show"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openScreen.wasPressed()) {
				client.setScreen(new GameShowScreen());
			}
			while (resetTimer.wasPressed()) {
				if (!showDoubleTimer) {
					timerStartValue = timerLength;
					timerStarted = new Date();
					timer = timerLength;
				} else {
					doubleTimerStartValue = redTurn ? doubleTimerRedLength : doubleTimerBlueLength;
					doubleTimerStarted = new Date();
					doubleTimerRed = doubleTimerRedLength;
					doubleTimerBlue = doubleTimerBlueLength;
				}
			}
			while (pauseTimer.wasPressed()) {
				if (!showDoubleTimer) {
					timerStartValue = timer;
					timerRunning = !timerRunning;
					timerStarted = new Date();
				} else {
					doubleTimerStartValue = redTurn ? doubleTimerRed : doubleTimerBlue;
					doubleTimerRunning = !doubleTimerRunning;
					doubleTimerStarted = new Date();
				}
			}
			while (swapDoubleTimer.wasPressed()) {
				redTurn=!redTurn;
				doubleTimerStartValue = redTurn ? doubleTimerRed : doubleTimerBlue;
				doubleTimerStarted = new Date();
			}
		});

		HudLayerRegistrationCallback.EVENT.register(wrapper -> wrapper.addLayer(new IdentifiedLayer() {
            @Override
            public Identifier id() {
                return Identifier.of(MOD_ID, "gameshow");
            }

            @Override
            public void render(DrawContext context, RenderTickCounter tickCounter) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (timerRunning) {
                    timer = timerStartValue-(new Date().getTime()-timerStarted.getTime())/1000.0;
                }
                if (showTimer) {
                    context.drawText(client.textRenderer, "ᴛɪᴍᴇʀ", 52, context.getScaledWindowHeight()-60, Colors.WHITE, true);
                    if (timer < 0) {
                        context.drawText(client.textRenderer, Text.literal("00.000").withColor(Colors.LIGHT_RED), 50, context.getScaledWindowHeight()-50, Colors.WHITE, true);
                    } else context.drawText(client.textRenderer, new DecimalFormat("00.000").format(timer), 50, context.getScaledWindowHeight()-50, Colors.WHITE, true);
                }

                if (listBlocks) {
					if (client.crosshairTarget!=null&&client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
						BlockState blockState = client.world.getBlockState(((BlockHitResult) client.crosshairTarget).getBlockPos());
						context.drawText(client.textRenderer, blockState.isOf(Blocks.AIR) ? "" : blockState.getBlock().getName().getString(), 50, context.getScaledWindowHeight() - 25, Colors.YELLOW, true);
						context.drawItem(new ItemStack(blockState.getBlock().asItem()), 30, context.getScaledWindowHeight() - 30);
						context.drawText(client.textRenderer, "ʙʟᴏᴄᴋ", 52, context.getScaledWindowHeight() - 35, Colors.WHITE, true);
					}
                }

                if (doubleTimerRunning) {
                    if (redTurn) doubleTimerRed = doubleTimerStartValue-(new Date().getTime()-doubleTimerStarted.getTime())/1000.0;
                    else doubleTimerBlue = doubleTimerStartValue-(new Date().getTime()-doubleTimerStarted.getTime())/1000.0;
                }
                if (showDoubleTimer) {
                    context.drawText(client.textRenderer, "ᴅᴏᴜʙʟᴇ ᴛɪᴍᴇʀ", 132, context.getScaledWindowHeight()-60, Colors.WHITE, true);
                    if (doubleTimerRed < 0) {
                        if (doubleTimerBlue > 0) redLostFirst = true;
                        context.drawText(client.textRenderer, "00.000", 130, context.getScaledWindowHeight()-50, 16751772, true);
                    } else context.drawText(client.textRenderer, new DecimalFormat("00.000").format(doubleTimerRed), 130, context.getScaledWindowHeight()-50, Colors.RED, true);
                    if (doubleTimerBlue < 0) {
                        if (doubleTimerRed > 0) redLostFirst = false;
                        context.drawText(client.textRenderer, "00.000", 130, context.getScaledWindowHeight()-40, 10399231, true);
                    } else context.drawText(client.textRenderer, new DecimalFormat("00.000").format(doubleTimerBlue), 130, context.getScaledWindowHeight()-40, Colors.BLUE, true);
                    context.drawText(client.textRenderer, "◀", 170, context.getScaledWindowHeight()-(redTurn?50:40),Colors.YELLOW,true);
                    if (doubleTimerRed < 0 || doubleTimerBlue < 0) context.drawText(client.textRenderer, "☠", 180, context.getScaledWindowHeight()-(redLostFirst?50:40),Colors.WHITE,true);
                }
            }
        }));
	}
}
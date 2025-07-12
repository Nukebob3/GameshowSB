package net.nukebob.gameshowsb.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.gameshowsb.GameshowSB;

import java.util.function.Consumer;

public class SwitchButtonWidget extends ButtonWidget {
    private boolean switched;
    private final Consumer<Boolean> consumer;
    private boolean pressed;

    public SwitchButtonWidget(int x, int y, Consumer<Boolean> consumer) {
        super(x, y, 21, 14, Text.empty(), button -> {}, DEFAULT_NARRATION_SUPPLIER);
        this.consumer = consumer;
    }

    @Override
    public void onPress() {
        switched = !switched;
        consumer.accept(switched);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        pressed=true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        pressed=false;
    }

    public void setSwitched(boolean switched) {
        this.switched = switched;
    }

    public boolean isSwitched() {
        return switched;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(GameshowSB.MOD_ID,"textures/gui/sprites/"+(isSwitched()?"on":"off")+"_switch"+(pressed?"_pressed":"")+".png"), getX(), getY(), 0, 0, width, height, 21, 14);
    }
}

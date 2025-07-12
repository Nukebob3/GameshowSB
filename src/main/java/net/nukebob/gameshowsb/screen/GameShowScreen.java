package net.nukebob.gameshowsb.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.nukebob.gameshowsb.GameshowSB;

import java.util.Random;

public class GameShowScreen extends Screen {
    TextFieldWidget timer;

    TextFieldWidget redTickets;
    TextFieldWidget blueTickets;

    TextFieldWidget doubleTimerRed;
    TextFieldWidget doubleTimerBlue;

    public GameShowScreen() {
        super(Text.literal("Game Show"));
    }

    @Override
    protected void init() {
        //Timer
        timer = new TextFieldWidget(textRenderer, 50, height/2, 20, 20, Text.literal(""+ GameshowSB.timerLength));
        timer.setText(""+GameshowSB.timerLength);
        timer.setChangedListener(text -> {
            try {
                GameshowSB.timerLength = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        });
        timer.setMaxLength(2);
        addDrawableChild(timer);
        SwitchButtonWidget timerEnabled = new SwitchButtonWidget(50, height/2+30, bool -> GameshowSB.showTimer = bool);
        timerEnabled.setSwitched(GameshowSB.showTimer);
        addDrawableChild(timerEnabled);
        addDrawable(new TextWidget(50-2, height/2-20, textRenderer.getWidth("Timer"), textRenderer.fontHeight, Text.literal("Timer"), textRenderer));

        //Tickets
        addDrawable(new TextWidget(150-5, height/2-20, textRenderer.getWidth("Raffle"), textRenderer.fontHeight, Text.literal("Raffle"), textRenderer));
        addDrawable(new TextWidget(148, height/2-10, textRenderer.getWidth("r"), textRenderer.fontHeight, Text.literal("r").withColor(Colors.RED), textRenderer));
        addDrawable(new TextWidget(168, height/2-10, textRenderer.getWidth("b"), textRenderer.fontHeight, Text.literal("b").withColor(Colors.BLUE), textRenderer));
        redTickets = new TextFieldWidget(textRenderer, 140, height/2, 20, 20, Text.literal("0").withColor(Colors.RED));
        redTickets.setText("0");
        redTickets.setMaxLength(1);
        blueTickets = new TextFieldWidget(textRenderer, 160, height/2, 20, 20, Text.literal("0").withColor(Colors.BLUE));
        blueTickets.setText("0");
        blueTickets.setMaxLength(1);
        addDrawableChild(redTickets);
        addDrawableChild(blueTickets);
        addDrawableChild(ButtonWidget.builder(Text.literal("DRAW"), button -> {
            try {
                int total = Integer.parseInt(redTickets.getText())+Integer.parseInt(blueTickets.getText());
                int draw = new Random().nextInt(total);
                button.setMessage(Text.literal("DRAW").withColor((draw < Integer.parseInt(redTickets.getText()))?Colors.RED:Colors.BLUE));
            } catch (Exception ignored){}
        }).dimensions(140,height/2+20,40,20).build());

        //Guess The Block
        addDrawable(new TextWidget(250-10, height/2-20, textRenderer.getWidth("List blocks"), textRenderer.fontHeight, Text.literal("List Blocks"), textRenderer));
        SwitchButtonWidget listBlocks = new SwitchButtonWidget(250, height/2+30, bool -> GameshowSB.listBlocks = bool);
        listBlocks.setSwitched(GameshowSB.listBlocks);
        addDrawableChild(listBlocks);

        //Double Timer
        doubleTimerRed = new TextFieldWidget(textRenderer, 350, height/2, 20, 20, Text.literal(""+ GameshowSB.doubleTimerRedLength));
        doubleTimerRed.setText(""+GameshowSB.doubleTimerRedLength);
        doubleTimerRed.setChangedListener(text -> {
            try {
                GameshowSB.doubleTimerRedLength = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        });
        doubleTimerRed.setMaxLength(2);
        doubleTimerBlue = new TextFieldWidget(textRenderer, 370, height/2, 20, 20, Text.literal(""+ GameshowSB.doubleTimerBlueLength));
        doubleTimerBlue.setText(""+GameshowSB.doubleTimerBlueLength);
        doubleTimerBlue.setChangedListener(text -> {
            try {
                GameshowSB.doubleTimerBlueLength = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {}
        });
        doubleTimerBlue.setMaxLength(2);
        addDrawableChild(doubleTimerRed);
        addDrawableChild(doubleTimerBlue);
        addDrawableChild(ButtonWidget.builder(Text.literal("T").withColor(GameshowSB.redTurn?Colors.RED:Colors.BLUE), button -> {
            GameshowSB.redTurn=!GameshowSB.redTurn;
            button.setMessage(Text.literal("T").withColor(GameshowSB.redTurn?Colors.RED:Colors.BLUE));
        }).dimensions(390,height/2,20,20).build());
        SwitchButtonWidget doubleTimerEnabled = new SwitchButtonWidget(350, height/2+30, bool -> GameshowSB.showDoubleTimer = bool);
        doubleTimerEnabled.setSwitched(GameshowSB.showDoubleTimer);
        addDrawableChild(doubleTimerEnabled);
        addDrawable(new TextWidget(358, height/2-10, textRenderer.getWidth("r"), textRenderer.fontHeight, Text.literal("r").withColor(Colors.RED), textRenderer));
        addDrawable(new TextWidget(378, height/2-10, textRenderer.getWidth("b"), textRenderer.fontHeight, Text.literal("b").withColor(Colors.BLUE), textRenderer));
        addDrawable(new TextWidget(350-2, height/2-20, textRenderer.getWidth("Double Timer"), textRenderer.fontHeight, Text.literal("Double Timer"), textRenderer));
    }
}

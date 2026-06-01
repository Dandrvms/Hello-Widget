package com.ceos.widgetfx;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Starblend
 */
public class Widget extends StackPane {

    private final Tile text;
    private final AnimationTimer timer;
    private long lastChange = 0;
    private int cindex = 0;
    private boolean wrestart = false;
    private final long SPEED = 150_000_000L;
    private final long RESTART_TIME = 1_500_000_000L;
    private String displayedText = "Hola mundo";

    public Widget() {
        text = createTile();
        getChildren().add(text);

        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastChange == 0) {
                    lastChange = now;
                    return;
                }

                if (wrestart) {
                    if (now - lastChange >= RESTART_TIME) {
                        cindex = 0;
                        text.setDescription("");
                        wrestart = false;
                        lastChange = now;
                    }
                    return;
                }

                if (now - lastChange >= SPEED) {
                    if (cindex <= displayedText.length()) {
                        text.setDescription(displayedText.substring(0, cindex));
                        cindex++;
                        lastChange = now;
                    } else {
                        wrestart = true;
                        lastChange = now;
                    }
                }
            }

        };

        timer.start();
    }

    public void setDisplayedText(String text) {
        this.displayedText = text;
        this.cindex = 0; // Restart animation with new text
    }

    private Tile createTile() {
        return TileBuilder.create()
                .skinType(SkinType.CENTER_TEXT)
                .title("test")
                .text("another test")
                .backgroundColor(Color.DARKGREEN)
                .build();
    }

    /**
     * Stop the timer when the widget is no longer used.
     */
    public void stop() {
        timer.stop();
    }
//    public static void main(String[] args) {
//        launch(args);
//    }
}

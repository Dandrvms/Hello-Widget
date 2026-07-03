package com.ceos.widgetfx;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Componente visual del Widget.
 * 
 * 
 * @author Daniel
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

        setPadding(new Insets(1));
        setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
        
        this.setPickOnBounds(true);
        text.setMouseTransparent(true);
        
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

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (text != null) {
            text.resizeRelocate(0, 0, getWidth(), getHeight());
        }
    }

    public void setDisplayedText(String text) {
        this.displayedText = text;
        this.cindex = 0;
    }
    
    public void setTileBackgroundColor(Color color){
        text.setBackgroundColor(color);
    }
    
    public void setTileFont(Font font){
        text.setCustomFont(font);
    }

    private Tile createTile() {
        return TileBuilder.create()
                .skinType(SkinType.CENTER_TEXT)
                .title("test")
                .text("another test")
                .backgroundColor(Color.DARKGREEN)
                .customFontEnabled(true)
                .build();
    }

    public void stop() {
        timer.stop();
    }
}

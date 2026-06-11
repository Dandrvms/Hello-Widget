package com.ceos.widgetph;

import org.csstudio.display.builder.model.properties.WidgetFont;
import org.csstudio.display.builder.representation.javafx.JFXUtil;
import org.csstudio.display.builder.representation.javafx.widgets.JFXBaseRepresentation;
import org.phoebus.ui.color.WidgetColor;

/**
 *
 * @author Daniel
 */
public class HelloWidgetRepresentation extends JFXBaseRepresentation<com.ceos.widgetfx.Widget, HelloWidget> {

    @Override
    protected com.ceos.widgetfx.Widget createJFXNode() throws Exception {
        return new com.ceos.widgetfx.Widget();
    }
    
    @Override
    protected void registerListeners() {
        super.registerListeners();
        
        final HelloWidget model = (HelloWidget) model_widget;
        final com.ceos.widgetfx.Widget node = (com.ceos.widgetfx.Widget) jfx_node;
        
        model.propText().addUntypedPropertyListener((prop, old, text) -> node.setDisplayedText((String) text));

        node.setDisplayedText(model.propText().getValue());
        

        model.propWidth().addUntypedPropertyListener((prop, old, val) -> {
            node.setPrefWidth(((Number) val).doubleValue());
        });
        
        model.propHeight().addUntypedPropertyListener((prop, old, val) -> {
            node.setPrefHeight(((Number) val).doubleValue());
        });
        
        node.setPrefSize(model.propWidth().getValue().doubleValue(), model.propHeight().getValue().doubleValue());
       
        
        model.propBackgroundColor().addUntypedPropertyListener((prop, old, val) -> {
            node.setTileBackgroundColor(JFXUtil.convert((WidgetColor) val));
        });
        
        model.propFont().addUntypedPropertyListener((prop, old, val) -> {
            node.setTileFont(JFXUtil.convert((WidgetFont) val));
        });
        
        
    }
    
}

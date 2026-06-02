package com.ceos.widgetph;

import org.csstudio.display.builder.representation.javafx.widgets.JFXBaseRepresentation;

/**
 *
 * @author Starblend
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
        // Initial value
        node.setDisplayedText(model.propText().getValue());
        
        model.propWidth().addUntypedPropertyListener((p, old, val) -> {
            node.setPrefWidth((Double) val);
        });
        
        model.propHeight().addUntypedPropertyListener((p, old, val) -> {
            node.setPrefHeight((Double) val);
        });
        
        node.setPrefSize(model.propWidth().getValue(), model.propHeight().getValue());
    }
    
}

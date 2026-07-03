package com.ceos.widgetph;

import org.csstudio.display.builder.model.DirtyFlag;
import org.csstudio.display.builder.model.UntypedWidgetPropertyListener;
import org.csstudio.display.builder.model.WidgetProperty;
import org.csstudio.display.builder.representation.javafx.JFXUtil;
import org.csstudio.display.builder.representation.javafx.widgets.JFXBaseRepresentation;

/**
 *
 * @author Daniel
 */
public class HelloWidgetRepresentation extends JFXBaseRepresentation<com.ceos.widgetfx.Widget, HelloWidget> {
    private DirtyFlag dirty_look = new DirtyFlag();
    private final UntypedWidgetPropertyListener listener = this::manage;

    @Override
    protected com.ceos.widgetfx.Widget createJFXNode() throws Exception {
        return new com.ceos.widgetfx.Widget();
    }
    
    @Override
    protected void registerListeners() {
        super.registerListeners();
        
        final HelloWidget model = model_widget;
        model.propText().addUntypedPropertyListener(listener);
        model.propWidth().addUntypedPropertyListener(listener);
        model.propHeight().addUntypedPropertyListener(listener);
        model.propBackgroundColor().addUntypedPropertyListener(listener);
        model.propFont().addUntypedPropertyListener(listener);
        
        
    }

    private void manage(final WidgetProperty<?> prop, final Object old, final Object val){
        dirty_look.mark();
        toolkit.scheduleUpdate(this);
    }

    @Override
    public void updateChanges(){
        super.updateChanges();

        if(dirty_look.checkAndClear()){

            int width = model_widget.propWidth().getValue();
            int height = model_widget.propHeight().getValue();
            jfx_node.setPrefWidth(width);
            jfx_node.setPrefHeight(height);

            jfx_node.setDisplayedText(model_widget.propText().getValue());
            jfx_node.setTileBackgroundColor(JFXUtil.convert((model_widget.propBackgroundColor().getValue())));
            jfx_node.setTileFont(JFXUtil.convert(model_widget.propFont().getValue()));

        }
    }
    
}

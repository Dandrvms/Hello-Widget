package com.ceos.widgetph;

import org.csstudio.display.builder.model.Widget;
import org.csstudio.display.builder.model.WidgetCategory;
import org.csstudio.display.builder.model.WidgetDescriptor;

/**
 *
 * @author Starblend
 */
public class HelloWidgetDescriptor extends WidgetDescriptor {
    
    public HelloWidgetDescriptor() {
        super(HelloWidget.WIDGET_TYPE, WidgetCategory.MONITOR, "Hello Widget", "", "Widget básico para demostración.");
    }

    @Override
    public Widget createWidget() {
        return new HelloWidget();
    }
    
    @Override
    public String getType(){
        return HelloWidget.WIDGET_TYPE;
    }
    
    @Override
    public String getName(){
        return "Hello Widget";
    }
    
    @Override
    public WidgetCategory getCategory(){
       return WidgetCategory.MONITOR; 
    }
   
    @Override
    public String getDescription(){
        return "Widget básico para demostración.";
    }
    
    
}

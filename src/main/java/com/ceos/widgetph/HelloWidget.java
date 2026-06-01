package com.ceos.widgetph;

import java.util.List;
import org.csstudio.display.builder.model.Widget;
import org.csstudio.display.builder.model.WidgetProperty;
import org.csstudio.display.builder.model.properties.CommonWidgetProperties;

/**
 *
 * @author Starblend
 */
public class HelloWidget extends Widget {
    
    public static final String WIDGET_TYPE = "ejemplo";
    
    private WidgetProperty<String> text;

    public HelloWidget(){
        super(WIDGET_TYPE);
    }
    
    @Override
    protected void defineProperties(final List<WidgetProperty<?>> properties){
        super.defineProperties(properties);
        text = CommonWidgetProperties.propText.createProperty(this, "Texto de Ejemplo");
        properties.add(text);
    }

    public WidgetProperty<String> propText() {
        return text;
    }
    
}

package com.ceos.widgetph;

import java.util.List;
import org.csstudio.display.builder.model.Widget;
import org.csstudio.display.builder.model.WidgetProperty;
import org.csstudio.display.builder.model.persist.WidgetFontService;
import org.csstudio.display.builder.model.properties.CommonWidgetProperties;
//import org.csstudio.display.builder.model.properties.WidgetColor;
import org.phoebus.ui.color.WidgetColor;
import org.csstudio.display.builder.model.properties.WidgetFont;

/**
 *
 * @author Starblend
 */
public class HelloWidget extends Widget {

    public static final String WIDGET_TYPE = "ejemplo";

    private WidgetProperty<String> text;
    private WidgetProperty<WidgetColor> backgroundColor;
    private WidgetProperty<WidgetFont> font;

    public HelloWidget() {
        super(WIDGET_TYPE);
    }

    @Override
    protected void defineProperties(final List<WidgetProperty<?>> properties) {
        super.defineProperties(properties);
        text = CommonWidgetProperties.propText.createProperty(this, "Texto de Ejemplo");
        backgroundColor = CommonWidgetProperties.propBackgroundColor.createProperty(this, new WidgetColor(0, 100, 0));
        font = CommonWidgetProperties.propFont.createProperty(this, WidgetFontService.get("Default Bold"));

        properties.add(backgroundColor);
        properties.add(font);
        properties.add(text);
    }

    public WidgetProperty<WidgetColor> propBackgroundColor() {
        return backgroundColor;
    }

    public WidgetProperty<WidgetFont> propFont() {
        return font;
    }

    public WidgetProperty<String> propText() {
        return text;
    }

}

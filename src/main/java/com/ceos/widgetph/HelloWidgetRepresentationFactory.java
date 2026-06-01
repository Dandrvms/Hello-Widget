package com.ceos.widgetph;

import org.csstudio.display.builder.representation.WidgetRepresentation;
import org.csstudio.display.builder.representation.WidgetRepresentationFactory;
import javafx.scene.Parent;

/**
 * Factory for creating the JavaFX representation of the HelloWidget.
 */
public class HelloWidgetRepresentationFactory implements WidgetRepresentationFactory<Parent, com.ceos.widgetfx.Widget> {

    @Override
    public WidgetRepresentation<Parent, com.ceos.widgetfx.Widget, org.csstudio.display.builder.model.Widget> create() {
        return (WidgetRepresentation) new HelloWidgetRepresentation();
    }
}

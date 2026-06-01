package com.ceos.widgetph;

import java.util.Map;
import org.csstudio.display.builder.model.WidgetDescriptor;
import org.csstudio.display.builder.model.WidgetFactory;
import org.csstudio.display.builder.representation.WidgetRepresentationFactory;
import org.csstudio.display.builder.representation.spi.WidgetRepresentationsService;

/**
 * Service that provides the representation factory for the HelloWidget.
 */
public class HelloWidgetRepresentationsService implements WidgetRepresentationsService {

    @Override
    public Map<WidgetDescriptor, WidgetRepresentationFactory<?, ?>> getWidgetRepresentationFactories() {
        return Map.of(new HelloWidgetDescriptor(), new HelloWidgetRepresentationFactory());
    }
}

package com.ceos.widgetph;

import java.util.Collections;
import java.util.Map;
import org.csstudio.display.builder.model.WidgetDescriptor;
import org.csstudio.display.builder.representation.WidgetRepresentation;
import org.csstudio.display.builder.representation.WidgetRepresentationFactory;
import org.csstudio.display.builder.representation.spi.WidgetRepresentationsService;

import static java.util.Map.entry;

/**
 * Service that provides the representation factory for the HelloWidget.
 */
public class HelloWidgetRepresentationsService implements WidgetRepresentationsService {

    @Override
    public <TWP, TW> Map<WidgetDescriptor, WidgetRepresentationFactory<TWP, TW>> getWidgetRepresentationFactories() {
        return Map.ofEntries(
                entry(new HelloWidgetDescriptor(), () -> (WidgetRepresentation) new HelloWidgetRepresentation()));
    }
}

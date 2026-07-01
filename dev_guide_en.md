# Widget Development for Phoebus

This development can be divided into two parts: Creating the JavaFX node and wrapping it in Phoebus.

## Phase 1: Develop the JavaFX Component
Phoebus widgets are essentially JavaFX nodes. Any component that inherits from the `Node` class can be used: `Pane`, `StackPane`, `Canvas`, `Group`, `Image`, among others.

The first phase consists of developing a JavaFX visual component to define how it should look in Phoebus.

```Java
public class MyNode extends Pane {
    public MyNode() {
        
    }
}
```

Direct methods can be included in this component so they can be called from Phoebus to change some of its properties, such as the color:

```Java
public void setBackgroundColor(Color color){
    this.setBackground(Background.fill(color));
}
```

## Phase 2: Wrap the Component with Phoebus Classes
After developing the independent component, it must be instantiated using Phoebus's classes and interfaces.

## **`Widget`**
This is the data model where the widget's properties are defined. It extends the `Widget` class if it will be simple, or the `PVWidget` class if it will be able to read/write PVs. Other classes are `WritablePVWidget`, `VisibleWidget`, or any existing widget if the goal is to extend its functionality.

```Java

import org.csstudio.display.builder.model.Widget;

public class MyWidget extends Widget{

    String WIDGET_TYPE = "custom"; //unique identifier for the widget
    
    public MyWidget() {
        super(WIDGET_TYPE);
    }
}

```

Properties are defined by overriding the method:

```Java
@Override
protected void defineProperties(final List<WidgetProperty<?>> properties){
    super.defineProperties(properties);
}
```

To define new properties, they are created as variables of type `WidgetProperty<?>`, where '?' is the property's data type (String, int, double, etc.).

They are then added to the model using the `CommonWidgetProperties` class. This class has methods for adding properties according to the type. If the type is not covered by these methods, it can be defined in the Widget class itself.

In this case, the property for the background color has a native Phoebus type called `WidgetColor`:

```Java
//Definition of the new property
private WidgetProperty<WidgetColor> backgroundColor;

@Override
protected void defineProperties(final List<WidgetProperty<?>> properties) {
    super.defineProperties(properties);
    //The property is created by assigning a default value
    backgroundColor = CommonWidgetProperties.propBackgroundColor.createProperty(this, new WidgetColor(0, 100, 0));

    //It is added to the general properties list
    properties.add(backgroundColor);

}
```

## **`Descriptor`**
The widget's metadata. This class can be embedded in the Widget class or exist in a separate file. It extends `WidgetDescriptor` and defines the following fields:

1. The type or unique identifier previously defined.
2. The widget's category, i.e., in which palette group the widget will appear.
3. The name the widget will have in the palette.
4. The path to the icon it will have in the palette.
5. The description that will appear in a tooltip.

```Java
import org.csstudio.display.builder.model.WidgetCategory;
import org.csstudio.display.builder.model.WidgetDescriptor;

public MyWidgetDescriptor extends WidgetDescriptor {
    public MyWidgetDescriptor() {
        super(/*type*/ MyWidget.WIDGET_TYPE, /*category*/ WidgetCategory.MONITOR, /*name*/ "Custom", /*icon*/ "path/to/your/16x16icon.png", /*description*/ "A short description");
    }
    
    //The method that instantiates the Widget model
    @Override
    public void createWidget(){
        return new MyWidget();
    }
}
```

## **`Representation`**
Defines the widget's visualization and responses to user interaction. Here, listeners are registered that manipulate the model and use the component's own methods (the JavaFX Node) to execute some function. It can extend `RegionBaseRepresentation` or `JFXBaseRepresentation` and must specify the model and node within "<>":

```Java
import org.csstudio.display.builder.representation.javafx.widgets.JFXBaseRepresentation;

public class MyWidgetRepresentation extends JFXBaseRepresentation<MyNode, MyWidget>{
    
    //The method that instantiates the widget's visualization
    //The return type is the class defined for the node
    @Override
    protected MyNode createJFXNode() throws Exception {
        return new MyNode();
    }
    
    
    //Event listeners are registered
    @Override
    protected void registerListeners(){
        super.registerListeners();
    }
}
```

The representation has the protected definitions `model_widget` and `jfx_node`. These allow direct calls to the respective model and node.

A listener is added to each new property defined in the model using `addUntypedPropertyListener`. This method receives the parameters `prop` (the modified property), `old` (the previous value), and `val` (the new value).

```Java
public class MyWidgetRepresentation extends JFXBaseRepresentation<MyNode, MyWidget>{
    @Override
    protected MyNode createJFXNode() throws Exception {
        return new MyNode();
    }
    @Override
    protected void registerListeners(){
        super.registerListeners();
        
        //color property
        model_widget.addUntypedPropertyListener((prop, old, val) -> {
            //The node is called directly with the method defined in phase 1 and the new value is passed
            jfx_node.setBackgroundColor(JFXUtil.convert((WidgetColor) val));
        })
                
        //Properties to handle resizing are also added
        model_widget.propWidth().addUntypedPropertyListener((prop, old, val) -> {
            jfx_node.setPrefWidth(((Number) val).doubleValue());
        });

        model_widget.propHeight().addUntypedPropertyListener((prop, old, val) -> {
            jfx_node.setPrefHeight(((Number) val).doubleValue());
        });

        jfx_node.setPrefSize(model.propWidth().getValue().doubleValue(), model.propHeight().getValue().doubleValue());
    }

    //A method is created to return the property so it can be modified from the representation
    public WidgetProperty<WidgetColor> propBackgroundColor() {
        return backgroundColor;
    }

}
```

This is the basics for the widget's minimum functionality. The next step is to register it in the Phoebus SPI.

## Interfaces for the Phoebus SPI
## **`WidgetsService`**
Informs Phoebus of the Widget's existence. The implemented class is responsible for registering the `Descriptor`, which in turn creates the widget.
```Java
public class MyWidgetsService implements WidgetsService {
    @Override
    public Collection<WidgetDescriptor> getWidgetDescriptors() {
        return List.of(new MyWidgetDescriptor());
    }
    
}
```
## **`WidgetRepresentationsService`**
Associates a descriptor with the corresponding representation.

```Java
public class MyWidgetRepresentationsService implements WidgetRepresentationsService {

    @Override
    public <TWP, TW> Map<WidgetDescriptor, WidgetRepresentationFactory<TWP, TW>> getWidgetRepresentationFactories() {
        return Map.ofEntries(
                entry(new MyWidgetDescriptor(), () -> (WidgetRepresentation) new MyWidgetRepresentation()));
    }
}
```

## `META-INF/services`
The files for the Phoebus SPI must be specified. To do this, create the `META-INF` directory and the `services` subdirectory within the project's `resources` folder.

In this location, two files are created: One for `WidgetsService` and another for `WidgetRepresentationsService`. These files must be named after the corresponding Phoebus interface name. Their content must be the fully qualified name (package.package.Class) of the file with the corresponding class.

For WidgetsService, create the file `org.csstudio.display.builder.model.spi.WidgetsService`
```
com.package.where.is.MyWidgetsService
```

For WidgetRepresentationsService, create the file `org.csstudio.display.builder.representation.spi.WidgetRepresentationsService`
```
com.package.where.is.MyWidgetRepresentationsService
```


## pom.xml
It is important to include the necessary dependencies for JavaFX. Phoebus dependencies, however, should be marked as `provided`, since the widget will run inside Phoebus. That being the case, a pom for this project could look like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.widget</groupId>
    <artifactId>mywidget</artifactId>
    <version>1</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <javafx.version>21.0.3</javafx.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-base</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-graphics</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.phoebus</groupId>
            <artifactId>app-display-model</artifactId>
            <version>5.0.5</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.phoebus</groupId>
            <artifactId>app-display-representation-javafx</artifactId>
            <version>5.0.5</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

# Final Notes
It is important to highlight that this guide constitutes a very basic development, leaving some topics aside. Furthermore, it covers the development of static widgets that do not use PVs directly, although by inheriting from any `PVWidget`, Phoebus handles that automatically for the widget.

On the other hand, widgets already have a defined runtime for basic operations. If a widget is intended for highly complex tasks, such as managing multiple PVs, complex data structures, or requiring multiple execution threads, a specific runtime must be created for that widget. This runtime is created by inheriting from `WidgetRuntime` in a class like `MyWidgetRuntime`. Said runtime must also be included in `META-INF/services` so that Phoebus can find it.

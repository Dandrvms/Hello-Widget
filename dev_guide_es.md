# Desarrollo de widgets para Phoebus

Este desarrollo puede dividirse en dos partes: Crear el nodo JavaFX y envolverlo en Phoebus.
## Fase 1: Desarrollar el componente JavaFX:
Los widgets de phoebus son en esencia nodos de javafx. Cualquier componente que herede de la clase `Node` puede funcionar: `Pane`, `StackPane`, `Canvas`, `Group`, `Image`, entre otros.

La primera fase consiste en desarrollar un componente visual JavaFx para definir cómo deberá verse en Phoebus.

```Java
public class MyNode extends Pane {
    public MyNode() {
        
    }
}
```

En este componente se pueden incluir métodos directos que puedan ser llamados desde phoebus para cambiar algunas de sus propiedades, como el color:

```Java
public void setBackgroundColor(Color color){
    this.setBackground(Background.fill(color));
}
```

## Fase 2: Envolver el componente con las clases de Phoebus:
Tras desarrollar el componente independiente, hay que instanciarlo utilizando las clases e interfaces de Phoebus. 


## **`Widget`**
Es el modelo de datos donde se definen las propiedades del widget. Se extiende de la clase `Widget` si será algo simple o de la clase `PVWidget` si será capaz de leer/escribir pv's. Otras clases son `WritablePVWidget`, `VisibleWidget`, o cualquier widget ya existente si lo que se busca es extender su funcionalidad.

```Java

import org.csstudio.display.builder.model.Widget;

public class MyWidget extends Widget{

    String WIDGET_TYPE = "custom"; //identificador único para el widget
    
    public MyWidget() {
        super(WIDGET_TYPE);
    }
}

```

Se definen las propiedades sobreescribiendo el método:

```Java
@Override
protected void defineProperties(final List<WidgetProperty<?>> properties){
    super.defineProperties(properties);
}
```

Para definir propiedades nuevas, se crean como variables de tipo `WidgetProperty<?>`, donde '?' es el tipo de dato de la propiedad (String, int, double, etc).

Luego se agregan al modelo utilizando la clase `CommonWidgetProperties`. Esta clase tiene métodos para agregar propiedades según el tipo. Si el tipo no está comprendido entre esos métodos, puede ser definido en la misma clase Widget.

En este caso, la propiedad para el color de fondo tiene un tipo nativo de Phoebus llamdo `WidgetColor`:

```Java
//Definición de la nueva propiedad
private WidgetProperty<WidgetColor> backgroundColor;

@Override
protected void defineProperties(final List<WidgetProperty<?>> properties) {
    super.defineProperties(properties);
    //Se crea la propiedad asignando un valor por defecto
    backgroundColor = CommonWidgetProperties.propBackgroundColor.createProperty(this, new WidgetColor(0, 100, 0));

    //Se agrega a la lista general de propiedades
    properties.add(backgroundColor);

}

//Se crea un método que retorne la propiedad para que pueda ser modificada desde la representación
public WidgetProperty<WidgetColor> propBackgroundColor() {
    return backgroundColor;
}
```

## **`Descriptor`**
Los metadatos del widget. Esta clase puede estar embebida en la clase Widget o existir en un archivo aparte. Extiende de `WidgetDescriptor` y define los siguientes campos:

1. El tipo o identificador único antes definido.
2. La categoría del widget, esto es, en que grupo de la paleta aparecerá el widget.
3. El nombre que tendrá el widget en la paleta.
4. La ruta al ícono que tendrá en la paleta.
5. La descripción que aparecerá en un tooltip.

```Java
import org.csstudio.display.builder.model.WidgetCategory;
import org.csstudio.display.builder.model.WidgetDescriptor;

public MyWidgetDescriptor extends WidgetDescriptor {
    public MyWidgetDescriptor() {
        super(/*tipo*/ MyWidget.WIDGET_TYPE, /*categoría*/ WidgetCategory.MONITOR, /*nombre*/ "Custom", /*ícono*/ "ruta/a/tu/ícono16x16.png", /*descripción*/ "Una descripción corta");
    }
    
    //El método que instancia el modelo del Widget
    @Override
    public void createWidget(){
        return new MyWidget();
    }
}
```

## **`Representation`**

Define la visualización del widget y las respuestas ante la interacción del usuario. Aquí se registran escuchadores que manipulan el modelo y utilizan los métodos propios del componente (javafx Node) para ejecutar alguna función. Puede extender de `RegionBaseRepresentation` o `JFXBaseRepresentation` y deben colocarse el model y el nodo entre "<>":

```Java
import org.csstudio.display.builder.representation.javafx.widgets.JFXBaseRepresentation;

public class MyWidgetRepresentation extends JFXBaseRepresentation<MyNode, MyWidget>{
    
    //El método que instancia la visualización del widget
    //El tipo de retorno es la clase definida para el nodo
    @Override
    protected MyNode createJFXNode() throws Exception {
        return new MyNode();
    }
    
    
    //Se registran los listeners de eventos
    @Override
    protected void registerListeners(){
        super.registerListeners();
    }
}
```

La representación tiene las definiciones protected `model_widget` y `jfx_node`. Estas permiten llamar al respectivo modelo y nodo directamente. 

A cada propiedad nueva definida en el modelo se le agrega un listener con `addUntypedPropertyListener`. Este método recibe los parámetros `prop` (la propiedad modificada), `old` (el valor anterior) y `val` (el nuevo valor)

```Java
public class MyWidgetRepresentation extends JFXBaseRepresentation<MyNode, MyWidget>{
    @Override
    protected MyNode createJFXNode() throws Exception {
        return new MyNode();
    }
    @Override
    protected void registerListeners(){
        super.registerListeners();
        
        //propiedad de color
        model_widget.addUntypedPropertyListener((prop, old, val) -> {
            //Se llama directamente al nodo con el método definido en la fase 1 y se le pasa el nuevo valor
            jfx_node.setBackgroundColor(JFXUtil.convert((WidgetColor) val));
        })
                
        //Se agregan también propiedades para gestionar el redimensionamiento
        model_widget.propWidth().addUntypedPropertyListener((prop, old, val) -> {
            jfx_node.setPrefWidth(((Number) val).doubleValue());
        });

        model_widget.propHeight().addUntypedPropertyListener((prop, old, val) -> {
            jfx_node.setPrefHeight(((Number) val).doubleValue());
        });

        jfx_node.setPrefSize(model.propWidth().getValue().doubleValue(), model.propHeight().getValue().doubleValue());
    }

}
```

Eso es lo básico para el funcionamiento mínimo del widget. El siguiente paso es registrarlo en el SPI de Phoebus.

## Interfaces para el spi de Phoebus:
## **`WidgetsService`**
Le informa a Phoebus de la existencia del Widget. La clase implementada se encarga de registrar el `Descriptor` que a su vez crea el widget.
```Java
public class MyWidgetsService implements WidgetsService {
    @Override
    public Collection<WidgetDescriptor> getWidgetDescriptors() {
        return List.of(new MyWidgetDescriptor());
    }
    
}
```
## **`WidgetRepresentationsService`**
Asocia un descriptor con la representación correspondiente.

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
Se deben especificar los archivos para el spi de Phoebus. Para ello, en la carpeta `resources` del proyecto se crea el directorio `META-INF` y el subdirectorio `services`.

En esta ubicación se crean dos archivos: Uno para `WidgetsService` y otro para `WidgetRepresentationsService`. Estos archivos deben tener por nombre el nombre de la interfaz de Phoebus correspondiente. Su contenido debe ser el nombre completo (paquete.paquete.Clase) del archivo con la clase correspondiente.

Para WidgetsService se crea el archivo `org.csstudio.display.builder.model.spi.WidgetsService`
```
com.paquete.donde.esta.MyWidgetsService
```

Para WidgetRepresentationsService se crea el archivo `org.csstudio.display.builder.representation.spi.WidgetRepresentationsService`
```
com.paquete.donde.esta.MyWidgetRepresentationsService
```


## pom.xml
Es importante incluir las dependencias necesarias para javafx. Las dependencias de Phoebus, sin embargo, deben marcarse como `provided`, ya que el widget se ejecutará dentro de Phoebus. Siendo ese el caso, un pom para este proyecto podría lucir así:

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

# Notas finales
Es importante destacar que esta guía constituye un desarrollo muy básico, dejando algunos temas por fuera. Además, trata el desarrollo de widgets estáticos que no utilizan pv's directamente, aunque al heredar de cualquier PVWdiget, Phoebus lo hace automáticamente por el widget.

Por otro lado, los widgets ya tienen un runtime definido para operaciones básicas. Si un widget está destinado a tareas de alta complejidad, como gestión de múltiples pv's, estructuras de datos complejas o necesita múltiples hilos de ejecución, se debe crear un runtime particular para dicho widget. Este runtime se crea heredando de `WidgetRuntime` en una clase como MyWidgetRuntime. Dicho runtime también debe incluirse en `META-INF/services` para que Phoebus pueda encontrarlo.


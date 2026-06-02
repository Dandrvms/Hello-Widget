### Load plugin
To load the plugin, include the .jar file in the phoebus classpath.


#### windows
Make a batch file, such as `phoebus.bat`, and modify the exec command: set the path as `%WIDGET_JAR%` and phoebus jar as `%PH_JAR%`
```powershell
java -Dorg.csstudio.display.builder.representation.spi.WidgetRepresentationsService=FINE -Dfile.encoding=UTF-8 -cp "%PH_JAR%;%WIDGET_JAR%" org.phoebus.product.Launcher
```

#### linux
Make a bash script, such as `phoebus.sh`, and modify the exec command: set the path to the jar as `$WIDGET_JAR`, and phoebus jar would be `$PH_JAR`
```
java -Dorg.csstudio.display.builder.representation.spi.WidgetRepresentationsService=FINE -Dfile.encoding=UTF-8 -cp "$PH_JAR:$WIDGET_JAR" org.phoebus.product.Launcher
```

Finally execute the script and in the display builder you will be able to see the "Hello widget" in the Monitors tab.

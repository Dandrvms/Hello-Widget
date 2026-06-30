# Phoebus Custom Widget

This repository provides a sample custom widget for the [Phoebus Control System IDE](https://github.com/ControlSystemStudio/phoebus). If you want to run a standalone app, build the project then type`mvn javafx:run` in the root.

## Prerequisites
- **Java JDK 21** (Must match the version used by your Phoebus installation).
- **Apache Maven 3.9+**.
- **Phoebus 5.0.5+**.

---

## Build Instructions
This project uses the `maven-shade-plugin` to package all external dependencies (such as TilesFX) into a single "Fat JAR". It automatically filters out digital signatures to prevent `SecurityException` errors in the Phoebus modular environment.

1. Compile and package the widget:
   ```bash
   mvn clean install
   ```
2. The resulting file will be located at: `target/widget-0.13.0-SNAPSHOT.jar`.

---

## Deployment & Integration
Phoebus is a modular application (JPMS). By default, its launcher uses the `-jar` command, which ignores external classpaths. To load this plugin, you must use a custom launch script that invokes the main launcher class directly using the `-cp` (classpath) argument.

### Option A: Windows (Batch Script)
Create a file named `launch_phoebus.bat` in your project root:

```batch
@echo off
setlocal ENABLEDELAYEDEXPANSION

:: --- CONFIGURATION ---
:: Path to your Phoebus product folder
set "PH_HOME=C:\Path\To\phoebus-product\target"
:: Path to this widget JAR
set "WIDGET_JAR=%~dp0target\widget-0.13.0-SNAPSHOT.jar"

:: 1. Locate the main Phoebus product JAR
FOR /F "tokens=* USEBACKQ" %%F IN (`dir /B /S "%PH_HOME%\product-*.jar"`) DO (SET "PH_JAR=%%F")

if "%PH_JAR%"=="" (
    echo [ERROR] Could not find Phoebus product JAR in %PH_HOME%
    pause
    exit /b
)

echo [INFO] Launching Phoebus with Custom Widget Suite...

:: 2. Launch using the Launcher class and explicit classpath
java -Dfile.encoding=UTF-8 ^
     -cp "%PH_JAR%;%WIDGET_JAR%" ^
     org.phoebus.product.Launcher %*
pause
```

### Option B: Linux / macOS (Shell Script)
Create a file named `launch_phoebus.sh` in your project root:

```bash
#!/bin/bash

# --- CONFIGURATION ---
PH_HOME="/home/user/phoebus-product/target"
WIDGET_JAR="$(pwd)/target/widget-0.13.0-SNAPSHOT.jar"

# 1. Locate the main Phoebus product JAR
PH_JAR=$(find "$PH_HOME" -name "product-*.jar" | head -n 1)

if [ -z "$PH_JAR" ]; then
    echo "ERROR: Could not find Phoebus product JAR in $PH_HOME"
    exit 1
fi

echo "Launching Phoebus with Custom Widget Suite..."

# 2. Launch using the Launcher class and explicit classpath
java -Dfile.encoding=UTF-8 \
     -cp "${PH_JAR}:${WIDGET_JAR}" \
     org.phoebus.product.Launcher "$@"
```
*Don't forget to grant execution permissions: `chmod +x launch_phoebus.sh`*

---

### Why this approach?
We avoid copying the JAR into Phoebus's internal folders to keep your installation clean. By using a custom script, you can update Phoebus or your widget suite independently without breaking the link between them.

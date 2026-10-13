# Building Snake Game

This document describes how to build and package the Snake Game locally for Windows and Linux.

## Requirements

- Java 21 JDK
- Maven
- Git

Additional platform-specific tools:

### Windows
- WiX Toolset 3.14
- `jpackage` (included with JDK 21)

### Linux
- `fakeroot`
- `jpackage` (included with JDK 21)

---

## 1. Build the Project

From the project root directory:

```bash
mvn clean package
```

This will:

- run the tests
- compile the project
- create the application JAR
- copy runtime dependencies into `target/libs`

Expected output:

```text
target/
├── snake-game-1.0-SNAPSHOT.jar
└── libs/
```

---

## 2. Prepare Packaging Files

Create a temporary directory for `jpackage`.

### Windows PowerShell

```powershell
Remove-Item package-input -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory package-input

Copy-Item target\snake-game-1.0-SNAPSHOT.jar package-input\
Copy-Item target\libs\*.jar package-input\
```

### Linux

```bash
rm -rf package-input
mkdir package-input

cp target/snake-game-1.0-SNAPSHOT.jar package-input/
cp target/libs/*.jar package-input/
```

---

# Windows

## 3. Create a Windows App Image

```powershell
jpackage `
  --type app-image `
  --name SnakeGame `
  --input package-input `
  --main-jar snake-game-1.0-SNAPSHOT.jar `
  --main-class Launcher `
  --icon src\main\resources\icon.ico `
  --dest dist
```

Run the generated application:

```powershell
.\dist\SnakeGame\SnakeGame.exe
```

---

## 4. Create a Windows Installer

WiX Toolset 3.14 must be installed and available in `PATH`.

You can verify it with:

```powershell
candle.exe -?
light.exe -?
```

Build the installer:

```powershell
jpackage `
  --type exe `
  --name SnakeGame `
  --input package-input `
  --main-jar snake-game-1.0-SNAPSHOT.jar `
  --main-class Launcher `
  --icon src\main\resources\icon.ico `
  --dest dist `
  --win-menu `
  --win-shortcut `
  --win-dir-chooser
```

The installer will be created in:

```text
dist/
```

---

# Linux

## 5. Install Packaging Tools

On Ubuntu/Debian:

```bash
sudo apt update
sudo apt install fakeroot
```

---

## 6. Create a Linux App Image

```bash
jpackage \
  --type app-image \
  --name SnakeGame \
  --input package-input \
  --main-jar snake-game-1.0-SNAPSHOT.jar \
  --main-class Launcher \
  --icon src/main/resources/icon.png \
  --dest dist
```

Run it:

```bash
./dist/SnakeGame/bin/SnakeGame
```

---

## 7. Create a Debian Package

```bash
jpackage \
  --type deb \
  --name SnakeGame \
  --input package-input \
  --main-jar snake-game-1.0-SNAPSHOT.jar \
  --main-class Launcher \
  --icon src/main/resources/icon.png \
  --dest dist \
  --linux-shortcut \
  --linux-menu-group Games
```

Install the generated package:

```bash
sudo apt install ./dist/*.deb
```

---

# Tests

Run unit tests separately with:

```bash
mvn test
```

A successful run should finish with:

```text
BUILD SUCCESS
```

---

# Generated Files

The following directories are generated locally and should not be committed:

```text
target/
dist/
package-input/
```

They should be included in `.gitignore`.

---

# Notes

- Windows packages must be built on Windows.
- Linux packages must be built on Linux.
- The packaged application includes its own Java runtime, so users do not need to install Java separately.
- High score data is stored outside the application in the user's home directory:

```text
~/.snake-game/highscore.txt
```

Automated builds are handled separately by GitHub Actions.

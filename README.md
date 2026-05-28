# Todo App (DHSN Prüfungsleistung)

Java Swing Anwendung zur Verwaltung von Todo-Listen.

## Voraussetzungen

- Java 11 oder höher (kein Maven erforderlich)

## Bauen und Starten (Windows)

```powershell
.\build.ps1
java -jar todoapp.jar
```

Das Skript kompiliert alle Quelldateien und erzeugt `todoapp.jar` im Projektverzeichnis.

## Manuell bauen (Linux/Mac)

```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
jar cfe todoapp.jar de.dhsn.todoapp.App -C out .
java -jar todoapp.jar
```

## Datenspeicherung

Die Listen werden automatisch in `~/.todoapp/data.ser` gespeichert (beim Schließen des Fensters).
Beim nächsten Start werden sie automatisch geladen.

## Funktionen

- Mehrere Todo-Listen verwalten und löschen (Rechtsklick in der Sidebar)
- Zwei Typen: Fließtext-Listen und Checkbox-Listen
- Checkbox-Items: abgehakt → automatisch ans Ende, durchgestrichen + ausgegraut
- Persistenz über App-Neustarts hinweg

## Klassendiagramm

Das Klassendiagramm befindet sich in `DIAGRAM.puml` (PlantUML-Format).
Online rendern: https://www.plantuml.com/plantuml/

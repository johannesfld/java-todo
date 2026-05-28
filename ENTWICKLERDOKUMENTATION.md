# Entwicklerdokumentation - Todo App

## 1. Projektkontext

Diese Dokumentation beschreibt die Entwicklung einer Todo-Applikation als Prüfungsleistung im Rahmen des Java-Moduls an der DHSN. Das Projekt wurde im Zeitraum April bis Mai 2026 von Johannes Fahland und Lorenz Grummt als Zweierteam umgesetzt.

Vorgabe war eine Java-Swing-Anwendung mit mindestens zwei Arten von Todo-Listen, persistenter Datenspeicherung und einer Vererbungshierarchie. Als Abgabeformat wird eine ausführbare `.jar`-Datei erwartet. Externe Bibliotheken wurden bewusst nicht eingesetzt, da die Aufgabenstellung auf Standard-Java-11-Mittel beschränkt ist.

## 2. Aufgabenteilung im Team

Wir haben das Projekt nach dem natürlichen Schnitt zwischen Modell/Persistenz und Benutzeroberfläche aufgeteilt.

Lorenz hat sich um die Modellklassen (`model/`) und die Datenpersistenz (`persistence/`) gekümmert. Dort ist die Datenstruktur und die Vererbungshierarchie entstanden. Johannes hat die komplette Swing-Oberfläche implementiert, also das Hauptfenster, die Sidebar, die Panels für die beiden Listentypen und den Dialog zum Erstellen neuer Listen. README und diese Dokumentation haben wir gemeinsam geschrieben.

## 3. Architektur

Die Anwendung ist in drei Pakete aufgeteilt:

- `model` - alle Datenklassen (TodoList, TodoItem und ihre Unterklassen)
- `persistence` - Laden und Speichern via Java-Serialisierung
- `ui` - alle Swing-Komponenten

Das vollständige Klassendiagramm liegt als `DIAGRAM.puml` im Projektverzeichnis (PlantUML-Format, online renderbar unter plantuml.com).

Der Einstiegspunkt ist `App.java`. Die Klasse setzt das Look-and-Feel und startet dann das `MainWindow` auf dem Event-Dispatch-Thread, wie es für Swing-Anwendungen sein soll.

## 4. Modellklassen

Die Vererbungshierarchie sieht so aus: `TodoList` ist eine abstrakte Basisklasse mit Titel-Feld und `toString()`-Methode für die Sidebar. Davon erben `TextTodoList` (speichert einen langen Fließtext) und `CheckboxTodoList` (verwaltet eine Liste von `CheckboxTodoItem`-Objekten).

Für die Items gilt ähnliches: `TodoItem` ist die Basis mit Text-Feld, `CheckboxTodoItem` fügt einen `checked`-Status hinzu.

Warum Vererbung statt zum Beispiel einem Typ-Enum? Mit einer `instanceof`-Abfrage im `MainWindow` können wir direkt das passende Panel anzeigen, ohne eine riesige Switch-Kette zu schreiben. Das hat sich beim Implementieren als angenehm herausgestellt.

Ein wichtiger Punkt in `CheckboxTodoList` ist die `getSortedItems()`-Methode. Sie gibt nicht die interne Liste zurück, sondern eine neue Liste in der die offenen Items vorne stehen und die erledigten am Ende. Das stellt sicher, dass das UI immer konsistent aussieht egal in welcher Reihenfolge die Items hinzugefügt oder abgehakt wurden.

```java
public List<CheckboxTodoItem> getSortedItems() {
    List<CheckboxTodoItem> offen = new ArrayList<>();
    List<CheckboxTodoItem> erledigt = new ArrayList<>();
    for (CheckboxTodoItem item : items) {
        if (item.isChecked()) {
            erledigt.add(item);
        } else {
            offen.add(item);
        }
    }
    List<CheckboxTodoItem> result = new ArrayList<>(offen);
    result.addAll(erledigt);
    return result;
}
```

Alle Modellklassen implementieren `Serializable`, was für die Persistenz (siehe nächster Abschnitt) notwendig ist.

## 5. Persistenz

Wir speichern die Listen als serialisiertes Java-Objekt unter `~/.todoapp/data.ser`. Das Verzeichnis wird beim ersten Speichern automatisch angelegt.

Warum Serialisierung und nicht JSON? JSON hätte eine externe Bibliothek oder aufwändiges eigenes Parsing bedeutet. Java-Serialisierung funktioniert direkt und für die Anforderungen dieser Aufgabe reicht es vollkommen aus. Nachteil ist, dass die Datei nicht menschenlesbar ist und bei Klassenänderungen inkompatibel werden kann - aber das ist für eine Prüfungsabgabe kein Problem.

Gespeichert wird beim Schließen des Fensters (im `WindowListener` von `MainWindow`). Geladen wird direkt beim Start. Falls keine Datei existiert, startet die App mit leerer Liste.

## 6. UI-Architektur

Das Hauptfenster (`MainWindow`) ist ein `JFrame` mit einem `JSplitPane`: links die `ListSidebar` (210px breit), rechts ein Content-Bereich der je nach ausgewählter Liste entweder ein `TextListPanel` oder ein `CheckboxListPanel` anzeigt.

Alle Farben, Schriften und Größenangaben stehen zentral in `Theme.java`. Das hat sich als praktisch erwiesen weil wir an einer Stelle die Farben anpassen konnten ohne überall suchen zu müssen.

`RoundedButton` ist eine eigene Swing-Komponente die `JButton` erweitert. Wir haben sie gebaut weil der Standard-Button unter Nimbus nicht so aussah wie wir es wollten. Das Custom-Painting in `paintComponent()` war zunächst verwirrend, vor allem weil Nimbus eigenständig Hintergründe setzt - am Ende hat der Aufruf von `setOpaque(false)` und `setContentAreaFilled(false)` zusammen mit dem komplett eigenen Zeichnen funktioniert.

```java
// wir malen selbst, standard-rendering aus
setOpaque(false);
setContentAreaFilled(false);
setBorderPainted(false);
setFocusPainted(false);
```

Ein etwas kniffliger Punkt ist der `flushToModel()`-Mechanismus. Das `TextListPanel` synchronisiert den Text nicht bei jedem Tastendruck ins Modell (das wäre ineffizient), sondern erst wenn das Panel den Fokus verliert oder wenn das Fenster geschlossen wird. `MainWindow` ruft deshalb vor dem Speichern `flushToModel()` auf dem aktuellen Panel auf. Für das `CheckboxListPanel` ist das nicht nötig weil dort jede Aktion (Checkbox setzen, Item löschen) sofort ins Modell schreibt.

Wir haben uns für Nimbus als Look-and-Feel entschieden weil das Windows-Standard-LAF Custom-Painting an manchen Stellen überschreibt und die App dann nicht mehr so aussieht wie gewollt. Nimbus ist damit etwas portabler auch wenn es auf jedem System ein wenig anders aussehen kann.

## 7. Build und Ausführung

Da auf unserem Entwicklungsrechner kein Maven installiert war, haben wir ein PowerShell-Skript `build.ps1` geschrieben das nur `javac` und `jar` aus dem JDK benötigt. Maven ist aber auch konfiguriert (`pom.xml`) und kann mit `mvn package` benutzt werden falls es vorhanden ist.

```powershell
.\build.ps1
java -jar todoapp.jar
```

Für Linux/Mac geht es auch manuell:

```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
jar cfe todoapp.jar de.dhsn.todoapp.App -C out .
```

Eine kleine Kuriosität: beim ersten Versuch hat das Build-Skript eine Datei mit dem Namen `nul` angelegt. Das ist ein reservierter Gerätename unter Windows - weder umbenennen noch normal löschen hat geklappt. Git konnte die Datei nicht stagen und hat eine kryptische Fehlermeldung ausgegeben. Am Ende hat nur ein spezieller Löschbefehl mit dem erweiterten Pfad-Prefix `\\?\` geholfen. Die Datei steht deshalb sicherheitshalber in der `.gitignore`.

## 8. Bekannte offene Punkte

Folgende Dinge haben wir bewusst nicht umgesetzt weil sie ausserhalb der Anforderungen lagen:

- Kein Drag-and-Drop zum Umsortieren von Listen oder Items
- Keine Bestätigung beim Löschen eines Items (man kann versehentlich auf das X klicken)
- Der `PersistenceManager` gibt Fehler nur auf `stderr` aus. Ein echter Fehler-Dialog wäre besser, aber für die Abgabe war das nicht mehr notwendig

## 9. Reflexion

Rückblickend würden wir manche Dinge anders angehen. Die meiste unerwartete Zeit hat das Styling mit Nimbus gekostet - was unter dem Windows-Standard-LAF funktioniert hat war unter Nimbus plötzlich anders, und umgekehrt. Wer das zum ersten Mal macht, der sollte sich früh für ein LAF entscheiden und dann dabei bleiben.

Die Aufteilung Modell/Persistenz vs. UI hat sich bewährt. Wir konnten parallel arbeiten sobald die Schnittstellen (also die Modellklassen) festgelegt waren. Was wir beim nächsten Mal früher machen würden: eine zentrale Theme-Klasse von Anfang an anlegen statt Farben zuerst direkt in den Komponenten zu hardcoden und sie dann nachträglich rauszuziehen.

Serialisierung als Persistenz-Ansatz würden wir im Nachhinein nochmal überdenken. Für diese Aufgabe war es die einfachste Lösung, aber sobald man die Klassen umbaut ist die gespeicherte Datei wertlos. Für ein echtes Projekt wäre JSON trotz des Zusatzaufwands robuster.

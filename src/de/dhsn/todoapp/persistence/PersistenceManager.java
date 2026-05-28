package de.dhsn.todoapp.persistence;

import de.dhsn.todoapp.model.TodoList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Kümmert sich um das Speichern und Laden der Todo-Listen.
 * Nutzt Java-Serialisierung, Datei liegt unter ~/.todoapp/data.ser
 */
public class PersistenceManager {

    private static final String DIR = System.getProperty("user.home") + File.separator + ".todoapp";
    private static final String FILE = DIR + File.separator + "data.ser";

    /** Speichert alle Listen in die Datei. */
    public void save(List<TodoList> lists) {
        try {
            Files.createDirectories(Paths.get(DIR));
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
                oos.writeObject(new ArrayList<>(lists));
            }
        } catch (IOException e) {
            // TODO: hier evtl. einen Fehler-Dialog anzeigen statt nur stderr
            System.err.println("fehler beim speichern: " + e.getMessage());
        }
    }

    /** Lädt die gespeicherten Listen. Gibt leere Liste zurück wenn keine Datei existiert. */
    @SuppressWarnings("unchecked")
    public List<TodoList> load() {
        File f = new File(FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<TodoList>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("fehler beim laden: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

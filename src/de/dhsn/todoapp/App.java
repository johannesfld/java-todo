package de.dhsn.todoapp;

import de.dhsn.todoapp.persistence.PersistenceManager;
import de.dhsn.todoapp.ui.MainWindow;
import de.dhsn.todoapp.ui.Theme;

import javax.swing.*;

/**
 * Einstiegspunkt der Todo-Applikation.
 * Startet die Swing-GUI auf dem Event-Dispatch-Thread.
 */
public class App {

    /**
     * Main-Methode. Startet die Applikation.
     *
     * @param args werden nicht verwendet
     */
    public static void main(String[] args) {
        // nimbus laf, lässt sich besser stylen als windows-laf
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        // checkbox-hintergrund global setzen, sonst malt nimbus drüber
        UIManager.put("CheckBox.background", Theme.BG_CONTENT);

        SwingUtilities.invokeLater(() -> {
            PersistenceManager mgr = new PersistenceManager();
            MainWindow window = new MainWindow(mgr);
            window.setVisible(true);
        });
    }
}

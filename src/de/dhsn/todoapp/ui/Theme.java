package de.dhsn.todoapp.ui;

import java.awt.*;

/**
 * Zentrale Theme-Konstanten für Farben und Schriften.
 * Alle UI-Klassen holen sich ihre Werte von hier.
 */
public class Theme {

    // hintergrundfarben
    public static final Color BG_SIDEBAR   = new Color(0xEEEEF2);
    public static final Color BG_CONTENT   = new Color(0xFFFFFF);

    // akzentfarben (indigo)
    public static final Color ACCENT       = new Color(0x5865F2);
    public static final Color ACCENT_LIGHT = new Color(0xDDE1FF);

    // textfarben
    public static final Color TEXT_DARK    = new Color(0x1E1E2E);
    public static final Color TEXT_SUB     = new Color(0x6B7280);
    public static final Color TEXT_CHECKED = new Color(0x9CA3AF);

    // trennlinien & hover
    public static final Color DIVIDER         = new Color(0xE5E7EB);
    public static final Color HOVER_ROW       = new Color(0xF3F4F6);
    public static final Color SIDEBAR_BORDER  = new Color(0xD1D5DB);

    // badge-farben: fließtext (indigo) und checkbox (grün)
    public static final Color BADGE_TEXT_BG      = new Color(0xEEF2FF);
    public static final Color BADGE_TEXT_FG      = new Color(0x4F46E5);
    public static final Color BADGE_CHECKBOX_BG  = new Color(0xF0FDF4);
    public static final Color BADGE_CHECKBOX_FG  = new Color(0x16A34A);

    // fonts
    public static final Font FONT_BASE         = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_HEADER       = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_SIDEBAR_TITLE = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BADGE        = new Font("SansSerif", Font.BOLD, 10);
    public static final Font FONT_LIST_ITEM    = new Font("SansSerif", Font.PLAIN, 13);

    // abstände und radien
    public static final int PADDING_SIDEBAR_H = 16;
    public static final int PADDING_SIDEBAR_V = 8;
    public static final int ROW_HEIGHT        = 40;
    public static final int ARC_BUTTON        = 10;
    public static final int ARC_BADGE         = 6;
    public static final int ARC_SELECTION     = 8;
    public static final int ARC_CARD          = 10;

    private Theme() {}
}

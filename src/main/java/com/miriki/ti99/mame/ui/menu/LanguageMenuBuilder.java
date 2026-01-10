package com.miriki.ti99.mame.ui.menu;

import java.awt.Component;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.tools.IconLoader;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

/**
 * Builds the language selection menu.
 */
public class LanguageMenuBuilder {

    private final MainAppFrameComponents ui;
    private final MainAppFrame frame;

    public LanguageMenuBuilder(MainAppFrameComponents ui, MainAppFrame frame) {
        this.ui = ui;
        this.frame = frame;
    }

    /**
     * Updates the selected state of all language items.
     */
    private void updateLanguageSelection() {

        Locale current = I18n.getLocale();

        for (Component c : ui.menuLang.getMenuComponents()) {
            if (c instanceof JCheckBoxMenuItem item) {
                Locale itemLocale = (Locale) item.getClientProperty("locale");
                item.setSelected(itemLocale.equals(current));
            }
        }
    }

    /**
     * Populates the given language menu.
     */
    public void build(JMenu menu) {

        ui.menuLang = menu;

        // Create menu items
        ui.menuLangEnglishGB = createLangItem(menu, "menu.lang.english_gb", "flag_en_gb.png", I18n.LOCALE_EN_GB, true);
        ui.menuLangEnglishUS = createLangItem(menu, "menu.lang.english_us", "flag_en_us.png", I18n.LOCALE_EN_US, false);
        ui.menuLangEnglishAU = createLangItem(menu, "menu.lang.english_au", "flag_en_au.png", I18n.LOCALE_EN_AU, false);

        ui.menuLangGermanDE = createLangItem(menu, "menu.lang.german_de", "flag_de_de.png", I18n.LOCALE_DE_DE, true);
        ui.menuLangGermanAT = createLangItem(menu, "menu.lang.german_at", "flag_de_at.png", I18n.LOCALE_DE_AT, false);
        ui.menuLangGermanCH = createLangItem(menu, "menu.lang.german_ch", "flag_de_ch.png", I18n.LOCALE_DE_CH, false);

        ui.menuLangFrenchFR = createLangItem(menu, "menu.lang.french_fr", "flag_fr_fr.png", I18n.LOCALE_FR_FR, false);
        ui.menuLangItalianIT = createLangItem(menu, "menu.lang.italian_it", "flag_it_it.png", I18n.LOCALE_IT_IT, false);

        // Group items (radio behavior)
        ButtonGroup grp = new ButtonGroup();
        grp.add(ui.menuLangEnglishGB);
        grp.add(ui.menuLangEnglishUS);
        grp.add(ui.menuLangEnglishAU);
        grp.add(ui.menuLangGermanDE);
        grp.add(ui.menuLangGermanAT);
        grp.add(ui.menuLangGermanCH);
        grp.add(ui.menuLangFrenchFR);
        grp.add(ui.menuLangItalianIT);

        // Update checkmarks when menu opens
        menu.addMenuListener(new MenuListener() {
            @Override public void menuSelected(MenuEvent e) { updateLanguageSelection(); }
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        updateLanguageSelection();
    }

    /**
     * Creates a language menu item with icon, locale metadata and action handler.
     */
    private JCheckBoxMenuItem createLangItem(JMenu parent,
                                             String i18nKey,
                                             String iconFile,
                                             Locale locale,
                                             boolean enabled) {

        JCheckBoxMenuItem item = I18nMenuFactory.createCheckBoxItem(parent, i18nKey);

        item.setIcon(IconLoader.load(iconFile));
        item.putClientProperty("locale", locale);
        item.setEnabled(enabled);

        item.addActionListener(e -> {
            I18n.setLocale(locale);
            frame.refreshUI();
        });

        return item;
    }
}

package com.miriki.ti99.mame.localization;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.ui.MainAppFrameComponents;

/**
 * Validates I18n usage across the UI.
 */
public final class I18nValidator {

    private static final Logger log = LoggerFactory.getLogger(I18nValidator.class);
    private static final String I18N_PROPERTY_KEY = "i18n";

    private final MainAppFrameComponents ui;
    private final ResourceBundle bundle;
    private final JComponent rootComponent;
    private final JMenuBar menuBar;

    private final Set<String> definedKeys = new TreeSet<>();
    private final Set<String> usedKeys = new HashSet<>();

    public I18nValidator(MainAppFrameComponents ui, ResourceBundle bundle) {
        this(ui, bundle, null, null);
    }

    public I18nValidator(MainAppFrameComponents ui,
                         ResourceBundle bundle,
                         JComponent rootComponent,
                         JMenuBar menuBar) {
        this.ui = ui;
        this.bundle = bundle;
        this.rootComponent = rootComponent;
        this.menuBar = menuBar;
    }

    /**
     * Validates that all used keys exist in the bundle.
     */
    public void validate() {
        definedKeys.clear();
        usedKeys.clear();

        collectDefinedKeysFromBundle();
        collectUsedKeysFromUiFields();

        if (rootComponent != null) {
            scanComponentTree(rootComponent);
        }

        if (menuBar != null) {
            scanMenuBar(menuBar);
        }

        reportMissingKeys();
    }

    /**
     * Reports keys defined in the bundle but unused in the UI.
     */
    public void validateUnusedKeys() {
        for (String key : definedKeys) {
            if (!usedKeys.contains(key)) {
                log.debug("Unused I18n key: {}", key);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Bundle analysis
    // -------------------------------------------------------------------------

    private void collectDefinedKeysFromBundle() {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            definedKeys.add(keys.nextElement());
        }
    }

    // -------------------------------------------------------------------------
    // UI registry scanning
    // -------------------------------------------------------------------------

    private void collectUsedKeysFromUiFields() {
        Field[] fields = ui.getClass().getFields();

        for (Field f : fields) {
            Object value;
            try {
                value = f.get(ui);
            } catch (IllegalAccessException e) {
                log.warn("Unable to access UI field: {}", f.getName(), e);
                continue;
            }

            if (value instanceof JComponent comp) {
                checkComponent(comp, "field:" + f.getName());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Component tree scanning
    // -------------------------------------------------------------------------

    private void scanComponentTree(JComponent root) {
        checkComponent(root, "tree");

        for (int i = 0; i < root.getComponentCount(); i++) {
            var child = root.getComponent(i);
            if (child instanceof JComponent childComp) {
                scanComponentTree(childComp);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Menu scanning
    // -------------------------------------------------------------------------

    private void scanMenuBar(JMenuBar bar) {
        for (int i = 0; i < bar.getMenuCount(); i++) {
            JMenu menu = bar.getMenu(i);
            if (menu != null) {
                scanMenu(menu);
            }
        }
    }

    private void scanMenu(JMenu menu) {
        checkComponent(menu, "menu");

        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item == null) continue;

            if (item instanceof JMenu subMenu) {
                scanMenu(subMenu);
            } else if (item instanceof JComponent comp) {
                checkComponent(comp, "menuItem");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Core component check
    // -------------------------------------------------------------------------

    private void checkComponent(JComponent comp, String context) {
        Object value = comp.getClientProperty(I18N_PROPERTY_KEY);
        if (value == null) return;

        String key = value.toString().trim();
        if (key.isEmpty()) return;

        usedKeys.add(key);

        if (!bundle.containsKey(key)) {
            if (context == null) {
                log.warn("Missing I18n key '{}' for component {}", key, comp.getClass().getSimpleName());
            } else {
                log.warn("Missing I18n key '{}' for component {} (context: {})",
                        key, comp.getClass().getSimpleName(), context);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Reporting
    // -------------------------------------------------------------------------

    private void reportMissingKeys() {
        // Missing keys are logged directly in checkComponent().
    }
}

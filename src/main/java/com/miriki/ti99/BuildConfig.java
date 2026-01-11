package com.miriki.ti99;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BuildConfig {

    private static final Logger log = LoggerFactory.getLogger(BuildConfig.class);

    public static final boolean DEV = Boolean.getBoolean("devMode");

    static {
        if (DEV) {
            log.info("⚙️  Dev-Modus aktiv – Test-Features werden angezeigt");
        } else {
            log.debug("BuildConfig geladen – Dev-Modus ist deaktiviert");
        }
    }
}

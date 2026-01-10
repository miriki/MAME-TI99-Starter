package com.miriki.ti99.mame.ui.mamedevices;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.miriki.ti99.mame.ui.UiConstants;

/**
 * Registry of all supported PEB devices.
 */
public class PebDevices {

    // -------------------------------------------------------------------------
    // Device registry
    // -------------------------------------------------------------------------

    private static final List<PebDevice> DEVICES = List.of(
        new PebDevice("32kmem",        "32K Memory",     "TI-99 32KiB memory expansion card"),
        new PebDevice("bwg",           "BWG Fdc",        "SNUG BwG Floppy Controller"),
        new PebDevice("ccdcc",         "Cc Dcc",         "CorComp Disk Controller Card"),
        new PebDevice("ccfdc",         "Cc Fdc",         "CorComp Floppy Disk Controller Card Rev A"),
        new PebDevice("ddcc1",         "D Dcc 1",        "Myarc Disk Controller Card"),
        new PebDevice("evpc",          "EV PC",          "SNUG Enhanced Video Processor Card"),
        new PebDevice("forti",         "FORTi",          "FORTi Sound Card"),
        new PebDevice("hfdc",          "H Fdc",          "Myarc Hard and Floppy Disk Controller"),
        new PebDevice("horizon",       "Horizon",        "Horizon 4000B RAMdisk"),
        new PebDevice("hsgpl",         "Hs GPL",         "SNUG High-speed GPL card"),
        new PebDevice("ide",           "IDE",            "Nouspikel IDE interface card"),
        new PebDevice("myarcmem",      "Myarc Mem",      "Myarc Memory expansion card MEXP-1"),
        new PebDevice("pcode",         "P-Code",         "TI-99 P-Code Card"),
        new PebDevice("pgram",         "PGRAM",          "PGRAM(+) memory card"),
        new PebDevice("samsmem",       "SAMS Mem",       "SuperAMS memory expansion card"),
        new PebDevice("sidmaster",     "Sid Master",     "SID Master 99"),
        new PebDevice("speechadapter", "Speech Adapter", "TI-99 Speech synthesizer adapter card"),
        new PebDevice("tifdc",         "TI Fdc",         "TI-99 Standard DSSD Floppy Controller"),
        new PebDevice("tipi",          "TI Pi",          "TiPi card"),
        new PebDevice("tirs232",       "TI RS232",       "TI-99 RS232/PIO interface"),
        new PebDevice("usbsm",         "USB Sm",         "Nouspikel USB/Smartmedia card"),
        new PebDevice("whtscsi",       "WHT SCSI",       "Western Horizon Technologies SCSI host adapter")
    );

    /** Lookup table: comboName → PebDevice (lowercase keys). */
    private static final Map<String, PebDevice> BY_COMBO =
        DEVICES.stream().collect(Collectors.toMap(
            d -> d.getComboName().toLowerCase(),
            d -> d
        ));

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Returns all registered devices. */
    public static List<PebDevice> all() {
        return DEVICES;
    }

    /** Returns the device for a given comboName (case‑insensitive). */
    public static PebDevice byComboName(String comboName) {
        if (comboName == null) return null;
        return BY_COMBO.get(comboName.toLowerCase());
    }

    /** Returns a ComboBox model: NONE + all comboNames. */
    public static String[] comboModel() {
        String[] arr = new String[DEVICES.size() + 1];
        arr[0] = UiConstants.CBX_SEL_NONE;

        for (int i = 0; i < DEVICES.size(); i++) {
            arr[i + 1] = DEVICES.get(i).getComboName();
        }

        return arr;
    }

    /** Returns the human‑readable title for UI tabs. */
    public static String toPrettyTitle(String comboName) {
        PebDevice d = byComboName(comboName);
        return d != null ? d.getPrettyTitle() : comboName;
    }

    /** Returns the lowercase parameter name for MAME. */
    public static String toParamName(String comboName) {
        PebDevice d = byComboName(comboName);
        return d != null ? d.getParamName() : comboName.toLowerCase();
    }

    /** Returns the internal panel name for showing/hiding device tabs. */
    public static String toInternalName(String comboName) {
        PebDevice d = byComboName(comboName);
        return d != null ? d.getInternalName() : ("Panel_PEB_" + comboName);
    }
}

//############################################################################

/**
 * Internal data class representing a single PEB device.
 */
class PebDevice {

    private final String comboName;
    private final String prettyTitle;
    private final String paramName;
    private final String internalName;
    private final String deviceComment;

    /**
     * Creates a new device entry.
     */
    public PebDevice(String comboName, String prettyTitle, String cardComment) {
        this.comboName = comboName;
        this.prettyTitle = prettyTitle;
        this.paramName = comboName.toLowerCase();
        this.internalName = "Panel_PEB_" + comboName;
        this.deviceComment = cardComment;
    }

    public String getComboName()     { return comboName; }
    public String getPrettyTitle()   { return prettyTitle; }
    public String getParamName()     { return paramName; }
    public String getInternalName()  { return internalName; }
    public String getDeviceComment() { return deviceComment; }

    @Override
    public String toString() {
        return comboName;
    }
}

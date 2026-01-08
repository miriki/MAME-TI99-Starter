package com.miriki.ti99.mame.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.dto.EmulatorOptionsDTO;
import com.miriki.ti99.mame.ui.UiConstants;

//############################################################################

public class EmulatorStart {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( EmulatorStart.class );
	
	// --------------------------------------------------
	
    private static String stripExtension(String name) {
    	
        int dot = name.lastIndexOf('.');
        
        return (dot >= 0) ? name.substring(0, dot) : name;
        
    } // stripExtension

	@SuppressWarnings("unused")
	public static String emulatorOptionsConcatenate( EmulatorOptionsDTO dto ) {
		
		log.debug( "----- start: emulatorOptionsConcatenate( {dto} )" );
		
        StringBuilder sb = new StringBuilder();
        
		String chk = "";
		
		// emulator (mame)
		
		String mame_AddOpt = "";
		String mame_RomPath = ""; // incl. CartPath
		
		// console
		
		String mame_Machine = "";
		
		String mame_GromPort = "";
		String mame_Cartridge = "";
		
		String mame_Joystick = "";
		
		String mame_IoPort = "";

		// peb slots
		
		String[] mame_PebDevices = new String[9];
		
		// media
		
		int Fdd_Max = 0;
		int Hdd_Max = 0;

		// -----
		
        chk = dto.mame_AddOpt;
        if ( ! dto.mame_AddOpt.trim().isEmpty() ) {
        	mame_AddOpt = dto.mame_AddOpt;
        }

        List<String> paths = new ArrayList<>();
        if (!dto.mame_RomPath.isEmpty())  paths.add(dto.mame_RomPath);
        if (!dto.mame_CartPath.isEmpty()) paths.add(dto.mame_CartPath);
        mame_RomPath = String.join(";", paths);
        
        chk = dto.mame_Machine.trim();
        if ( ! UiConstants.CBX_SEL_NONE.equals( chk )) {
        	mame_Machine = chk;
        }
                
        chk = dto.mame_JoyPort.trim();
        if ( ! UiConstants.CBX_SEL_NONE.equals( chk )) {
        	mame_Joystick = chk;
        }

        chk = dto.mame_GromPort.trim();
        if ( ! UiConstants.CBX_SEL_NONE.equals( chk )) {
        	mame_GromPort = chk;
        }
        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) { 
        	mame_GromPort = "";
        }
        
        // Cartridge
        if ("geneve".equals(dto.mame_Machine) || "genmod".equals(dto.mame_Machine)) {
            if (dto.cartEntry != null) {
                log.warn("selected 'Cartridge' is ignored due to incompatibility with machine '{}'", dto.mame_Machine);
            }
        } else {
            if (dto.cartEntry != null) {
                String ext = dto.cartEntry.getMediaExt();

                if ("zip".equalsIgnoreCase(ext)) {
                    // ZIP → Name ohne Extension
                    String name = stripExtension(dto.cartEntry.getDisplayName());
                    mame_Cartridge = name;
                } else {
                    // RPK → voller Pfad
                    // appendMedia(sb, "-cart", dto.cartPathP);
                	mame_Cartridge = dto.cartPathP.toString();
                }
            }
        }

		chk = dto.mame_IoPort.trim();
        if ( ! UiConstants.CBX_SEL_NONE.equals( chk )) {
        	mame_IoPort = chk;

        	switch ( mame_IoPort ) {

	        	case "peb":

	        		// fetch device x in slot 2..8
	        		
	        		for ( int i = 2; i < 9; i++ ) {
	            		
	        	        chk = (String) dto.PebDevices[i].trim();
	        	        if ( ! UiConstants.CBX_SEL_NONE.equals( chk )) {
	        	        	mame_PebDevices[i] = " -ioport:peb:slot" + i + " " + chk;
	        	        	
	        	        	String pebslot = " -ioport:peb:slot" + i + ":" + chk + ":";
	        	        	switch (chk) {
		    	        		case "32kmem":
		    	        			// ...
		    	        			break;
		    	        		case "bwg":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                bwg '0' .. '3'" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'bwg' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		        	        			if ( ! dto.bwg_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.bwg_0.trim(); }
		        	        			if ( ! dto.bwg_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.bwg_1.trim(); }
		        	        			if ( ! dto.bwg_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.bwg_2.trim(); }
		        	        			if ( ! dto.bwg_3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.bwg_3.trim(); }
		        	        			Fdd_Max = 4;
		    	        	        }
		    	        			break;
		    	        		case "ccdcc":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ccdcc '0' .. '3'" );
	        	        			if ( ! dto.ccdcc_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ccdcc_0.trim(); }
	        	        			if ( ! dto.ccdcc_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ccdcc_1.trim(); }
	        	        			if ( ! dto.ccdcc_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ccdcc_2.trim(); }
	        	        			if ( ! dto.ccdcc_3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ccdcc_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "ccfdc":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ccfdc '0' .. '3'" );
	        	        			if ( ! dto.ccfdc_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ccfdc_0.trim(); }
	        	        			if ( ! dto.ccfdc_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ccfdc_1.trim(); }
	        	        			if ( ! dto.ccfdc_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ccfdc_2.trim(); }
	        	        			if ( ! dto.ccfdc_3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ccfdc_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "ddcc1":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ddcc1 '0' .. '3'" );
	        	        			if ( ! dto.ddcc1_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ddcc1_0.trim(); }
	        	        			if ( ! dto.ddcc1_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ddcc1_1.trim(); }
	        	        			if ( ! dto.ddcc1_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ddcc1_2.trim(); }
	        	        			if ( ! dto.ddcc1_3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ddcc1_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "evpc":
		    	        			// "colorbus"
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'evpc' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	if ( ! dto.evpc_colorbus.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "colorbus " + dto.evpc_colorbus.trim(); }
		    	        	        }
		    	        			break;
		    	        		case "forti":
		    	        			// ...
		    	        			break;
	        	        		case "hfdc":
	        	        			// "f1" .. "f4" fd, "h1" .. "h3" hd
	        	        			if ( ! dto.hfdc_f1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f1 " + dto.hfdc_f1.trim(); }
	        	        			if ( ! dto.hfdc_f2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f2 " + dto.hfdc_f2.trim(); }
	        	        			if ( ! dto.hfdc_f3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f3 " + dto.hfdc_f3.trim(); }
	        	        			if ( ! dto.hfdc_f4.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f4 " + dto.hfdc_f4.trim(); }
	        	        			if ( ! dto.hfdc_h1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h1 " + dto.hfdc_h1.trim(); }
	        	        			if ( ! dto.hfdc_h2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h2 " + dto.hfdc_h2.trim(); }
	        	        			if ( ! dto.hfdc_h3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h3 " + dto.hfdc_h3.trim(); }
	        	        			Fdd_Max = 4;
	        	        			Hdd_Max = 3;
	        	        			break;
		    	        		case "horizon":
		    	        			// ...
		    	        			break;
	        	        		case "hsgpl":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'hsgpl' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
	        	        			break;
		    	        		case "ide":
		    	        			// "0" .. "1"
	        	        			if ( ! dto.ide_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "ata:0 " + dto.ide_0.trim(); }
	        	        			if ( ! dto.ide_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "ata:1 " + dto.ide_1.trim(); }
		    	        			break;
		    	        		case "myarcmem":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'myarcmem' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "pcode":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'pcode' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "pgram":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'pgram' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "samsmem":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'samsmem' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "sidmaster":
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'sidmaster' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "speechadapter":
		    	        			// "conn", "conn:speechsyn:extport"
		    	        			// assume: if "speechadapter" is selected for a slot then "conn:speechsyn" is selected for that adapter and "conn:speechsyn:extport" is left empty
		    	        			mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "conn speechsyn";
		    	        			break;
		    	        		case "tifdc":
		    	        			// "0" .. "2" fd
	        	        			if ( ! dto.tifdc_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.tifdc_0.trim(); }
	        	        			if ( ! dto.tifdc_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.tifdc_1.trim(); }
	        	        			if ( ! dto.tifdc_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.tifdc_2.trim(); }
	        	        			Fdd_Max = 3;
		    	        			break;
		    	        		case "tipi":
		    	        			// ...
		    	        			break;
		    	        		case "tirs232":
		    	        			// ...
		    	        			break;
		    	        		case "usbsm":
		    	        			// ...
		    	        			break;
		    	        		case "whtscsi":
		    	        			// "0" .. "6" - "7" is the adapter itself
	        	        			if ( ! dto.whtscsi_0.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:0 " + dto.whtscsi_0.trim(); }
	        	        			if ( ! dto.whtscsi_1.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:1 " + dto.whtscsi_1.trim(); }
	        	        			if ( ! dto.whtscsi_2.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:2 " + dto.whtscsi_2.trim(); }
	        	        			if ( ! dto.whtscsi_3.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:3 " + dto.whtscsi_3.trim(); }
	        	        			if ( ! dto.whtscsi_4.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:4 " + dto.whtscsi_4.trim(); }
	        	        			if ( ! dto.whtscsi_5.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:5 " + dto.whtscsi_5.trim(); }
	        	        			if ( ! dto.whtscsi_6.equals( UiConstants.CBX_SEL_NONE )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:6 " + dto.whtscsi_6.trim(); }
	        	        			// mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:7 controller";
		    	        			break;
	        	        		default:
	        	        			// ...
	        	        			break;
	        	        	}

	        	        }
	                
	        		}
	            
	        		break;
        	
	        	case "speechsyn":
	        		
	        		break;
	        	
	        	case "splitter":
	        		
	        		break;
	        	
	        	case "arcturus":
	        		
	        		break;
	        		
	        	default:
	        		
	        		break;
	        
        	}

        }
        
        if ( ! mame_Machine.isEmpty() ) { sb.append( mame_Machine ); }
        
        if ( ! mame_AddOpt.isEmpty() ) { sb.append( " " + mame_AddOpt ); }
        
        if ( ! mame_RomPath.isEmpty() ) { sb.append( " -rp " + mame_RomPath + "" ); }
        
        if ( ! mame_GromPort.isEmpty() ) { 
            if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
	        	log.warn( "selected 'GromPort' is ignored due to incompatibility with machine 'Geneve' !" );
            } else {
            	// result = result + " -gromport " + mame_GromPort;
                if ( ! mame_GromPort.isEmpty() ) { sb.append( " -gromport " + mame_GromPort ); }
            }
        }
        
        // ----- cartridge -----

        if ("geneve".equals(dto.mame_Machine) || "genmod".equals(dto.mame_Machine)) {
            if (dto.cartPathP != null) {
                log.warn("selected 'Cartridge' is ignored due to incompatibility with machine '{}'", dto.mame_Machine);
            }
        } else {
            // appendMedia(sb, "-cart", dto.cartPathP);
            if ( ! mame_Cartridge.isEmpty() ) { sb.append( " -cart " + mame_Cartridge + "" ); }
        }
        
        // if ( ! mame_Joystick.isEmpty() ) { result = result + " -joyport " + mame_Joystick; }
        if ( ! mame_Joystick.isEmpty() ) { sb.append( " -joyport " + mame_Joystick ); }
        
        if ( ! mame_IoPort.isEmpty() ) { 

            sb.append( " -ioport " + mame_IoPort );
        
	        // if "ioport: peb" is selected then scan for the slots
	        if ( "peb".equals(mame_IoPort) ) {
	    		for ( int i = 2; i < 9; i++ ) {
		        	if (( mame_PebDevices[i] != null ) && ( ! mame_PebDevices[i].isEmpty() )) { sb.append( mame_PebDevices[i] ); }
	    		}
	        }
	
	    	// if "speechsyn"
	    	
	    	// if "splitter"
	    	
	    	// if "arcturus
        
        }
        
        appendMedia( sb, "-flop1", dto.fddPathRel1 );
        appendMedia( sb, "-flop2", dto.fddPathRel2 );
        appendMedia( sb, "-flop3", dto.fddPathRel3 );
        appendMedia( sb, "-flop4", dto.fddPathRel4 );

        appendMedia( sb, "-hard1", dto.hddPathRel1 );
        appendMedia( sb, "-hard2", dto.hddPathRel2 );
        appendMedia( sb, "-hard3", dto.hddPathRel3 );

        if ("geneve".equals(dto.mame_Machine) || "genmod".equals(dto.mame_Machine)) {
        	log.warn("selected 'Cassette' is ignored due to incompatibility with machine '{}'", dto.mame_Machine);
        } else {
	        appendMedia( sb, "-cass1", dto.cassPathRel1 );
	        appendMedia( sb, "-cass2", dto.cassPathRel2 );
        }
        
        String result = sb.toString();
        
        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) { 
        	result = result.replace(" -ioport peb", "");
        	result = result.replace(" -ioport:peb:", " -peb:");
        }
        
        log.debug( "----- end: emulatorOptionsConcatenate() ---> '{}'", result );

        // System.out.println( "--> " + result );
		return result;
		
	} // emulatorOptionsConcatenate()
	
	// --------------------------------------------------
	
	private static void appendMedia( StringBuilder sb, String option, Path fullPath ) {
		
	    log.trace( "appendMedia: option='{}', base='{}'", option, fullPath );
	    
	    if ( fullPath == null ) {
	        log.trace( "appendMedia: SKIP for option='{}'", option );
	        return;
	    }
	    
	    sb.append(" ")
	    	.append(option)
	    	.append(" ")
	    	// .append( "\"" )
	    	.append(fullPath)
	    	// .append("\"")
	    	;
	    
	} // appendMedia
	
	// --------------------------------------------------
	
	public static boolean emulatorStartProgram( EmulatorOptionsDTO dto ) {
		
		log.debug( "----- start: emulatorStartProgram( {dto} )" );
		
		boolean result = true;
		
		String mame_WorkingPath = "";
		String mame_Executable = "";
		String mame_Parameter = "";
		
		mame_WorkingPath = dto.mame_WorkingPath;
		mame_Executable = dto.mame_Executable;
		log.trace("DTO BEFORE CONCAT: mame_DSK1='{}'", dto.mame_DSK1);
		mame_Parameter = emulatorOptionsConcatenate( dto );
		
	    try {
	        // Kommando zusammenbauen
	    	String fullExecutablePath = mame_WorkingPath + File.separator + mame_Executable;
	        List<String> command = new ArrayList<>();
	        command.add(fullExecutablePath);

	        // Parameter anhängen – hier per Split, falls deine Funktion einen String liefert
	        if (mame_Parameter != null && !mame_Parameter.isEmpty()) {
	            command.addAll( Arrays.asList( mame_Parameter.split( " " )));
	        }
	        
	        // ProcessBuilder mit Arbeitsverzeichnis
	        ProcessBuilder pb = new ProcessBuilder( command );
	        pb.directory( new File( mame_WorkingPath ));

	        // Ausgabe/Fehler direkt in die Konsole umleiten
	        pb.inheritIO();

	        // Prozess starten
	        Process process = pb.start();

	        // Optional: auf Ende warten
	        int exitCode = process.waitFor();
	        log.trace( "Programm beendet mit Code: {}", exitCode );

	    } catch ( IOException ex ) {

	        log.error( "Programm kann nicht gestartet werden!" );
	        ex.printStackTrace();
	        result = false; // Fehlerfall
	        
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Interrupt-Status wieder setzen
	        e.printStackTrace(); // oder eigene Behandlung

	    }

        log.debug( "----- end: emulatorStartProgram() ---> {}", result );

	    return result;
		
	} // emulatorStartProgram()
	
	// --------------------------------------------------
	
} // class MameTools

//############################################################################

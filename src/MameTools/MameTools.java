package MameTools;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

public class MameTools {

    private static final Logger log = LoggerFactory.getLogger( MameTools.class );
	
	// --------------------------------------------------
	
	public static String emulatorOptionsConcatenate( EmulatorOptionsDTO dto ) {
		
		log.debug( "emulatorOptionsConcatenate()" );
		
		String result = "";
		
		final String cbxSelNone = "------";

		String chk = "";
		
		// System.out.println( "emulatorOptionsConcatenate( EmulatorOptionsDTO dto )" );

		// System.out.println( "    getting 'main panel' fields" );

		// emulator (mame)
		
		/*
		String mame_Executable = "";
		String mame_WorkingPath = "";
		*/

		String mame_AddOpt = "";
		String mame_RomPath = ""; // incl. CartPath
		// String mame_CartPath = "";
		
		String mame_DskPath = "";
		String mame_WdsPath = "";
		String mame_CsPath = "";

		// console
		
		String mame_Machine = "";
		
		String mame_GromPort = "";
		String mame_Cartridge = "";
		
		String mame_Joystick = "";
		
		String mame_IoPort = "";

		// peb slots
		
		String[] mame_PebDevices = new String[9];
		
		// media
		
		String mame_CS1 = "";
		String mame_CS2 = "";

		String mame_DSK1 = "";
		String mame_DSK2 = "";
		String mame_DSK3 = "";
		String mame_DSK4 = "";
		int Fdd_Max = 0;

		String mame_WDS1 = "";
		String mame_WDS2 = "";
		String mame_WDS3 = "";
		int Hdd_Max = 0;

		// -----
		
        /*
        chk = dto.mame_Executable;
        if ( ! chk.isEmpty() ) {
        	mame_Executable = chk;
        }

        chk = dto.mame_WorkingPath;
        if ( ! chk.isEmpty() ) {
        	mame_WorkingPath = chk;
        }
        */

        chk = dto.mame_AddOpt;
        if ( ! dto.mame_AddOpt.trim().isEmpty() ) {
        	mame_AddOpt = dto.mame_AddOpt;
        }

        /*
        chk = dto.mame_RomPath + ";" + dto.mame_CartPath;
        if ( ! chk.isEmpty() ) {
        	mame_RomPath = chk;
        }
        */
        
        chk = dto.mame_RomPath.trim();
        if ( ! chk.isEmpty() ) {
        	mame_RomPath = chk;
        	chk = dto.mame_CartPath.trim();
            if ( ! chk.isEmpty() ) {
            	// a + b
            	mame_RomPath = mame_RomPath + ";" + chk;
            // } else {
            	// a
            	// mame_RomPath = mame_RomPath;
            }
        } else {
        	// mame_RomPath = "";
        	chk = dto.mame_CartPath.trim();
            if ( ! chk.isEmpty() ) {
            	// b
            	mame_RomPath = chk;
            // } else {
            	// -
            	// mame_RomPath = "";
            }
        }

        chk = dto.mame_DskPath.trim();
        mame_DskPath = chk;

        chk = dto.mame_WdsPath.trim();
        mame_WdsPath = chk;

        chk = dto.mame_CsPath.trim();
        mame_CsPath = chk;

        chk = dto.mame_Machine.trim();
        if ( ! cbxSelNone.equals( chk )) {
        	mame_Machine = chk;
        }
                
        chk = dto.mame_JoyPort.trim();
        if ( ! cbxSelNone.equals( chk )) {
        	mame_Joystick = chk;
        }

        chk = dto.mame_GromPort.trim();
        if ( ! cbxSelNone.equals( chk )) {
        	mame_GromPort = chk;
        }
        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) { 
        	mame_GromPort = "";
        }
                
        chk = dto.mame_Cartridge.trim();
        if ( ! cbxSelNone.equals( chk )) {
        	mame_Cartridge = chk;
        }
        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) { 
        	mame_Cartridge = "";
        }
        
        /*
		for ( int i = 1; i < 9; i++ ) {
	        mame_PebDevices[i] = "";
		}
		*/
    
        // System.out.println( "    getting 'ioport' device" );

        chk = dto.mame_IoPort.trim();
        if ( ! cbxSelNone.equals( chk )) {
        	mame_IoPort = chk;
        	// System.out.println( "        getting 'ioport' device: " + mame_IoPort );

        	switch ( mame_IoPort ) {

	        	case "peb":

	        		// System.out.println( "            peb: getting 'peb slots' fields" );

	        		// fetch device x in slot 2..8
	        		
	        		for ( int i = 2; i < 9; i++ ) {
	            		
	        	        chk = (String) dto.PebDevices[i].trim();
	        	        if ( ! "------".equals( chk )) {
	        	        	mame_PebDevices[i] = " -ioport:peb:slot" + i + " " + chk;
	    	    			// System.out.println( "                " + i + ": '" + mame_PebDevices[i] + "'" );
	        	        	
	        	        	String pebslot = " -ioport:peb:slot" + i + ":" + chk + ":";
	        	        	switch (chk) {
		    	        		// case "32kmem":
		    	        			// ...
		    	        		// 	break;
		    	        		case "bwg":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                bwg '0' .. '3'" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'bwg' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		        	        			if ( ! dto.bwg_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.bwg_0.trim(); }
		        	        			if ( ! dto.bwg_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.bwg_1.trim(); }
		        	        			if ( ! dto.bwg_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.bwg_2.trim(); }
		        	        			if ( ! dto.bwg_3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.bwg_3.trim(); }
		        	        			Fdd_Max = 4;
		    	        	        }
		    	        			break;
		    	        		case "ccdcc":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ccdcc '0' .. '3'" );
	        	        			if ( ! dto.ccdcc_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ccdcc_0.trim(); }
	        	        			if ( ! dto.ccdcc_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ccdcc_1.trim(); }
	        	        			if ( ! dto.ccdcc_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ccdcc_2.trim(); }
	        	        			if ( ! dto.ccdcc_3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ccdcc_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "ccfdc":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ccfdc '0' .. '3'" );
	        	        			if ( ! dto.ccfdc_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ccfdc_0.trim(); }
	        	        			if ( ! dto.ccfdc_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ccfdc_1.trim(); }
	        	        			if ( ! dto.ccfdc_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ccfdc_2.trim(); }
	        	        			if ( ! dto.ccfdc_3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ccfdc_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "ddcc1":
		    	        			// "0" .. "3" fd
		    	        			// System.out.println( "                ddcc1 '0' .. '3'" );
	        	        			if ( ! dto.ddcc1_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.ddcc1_0.trim(); }
	        	        			if ( ! dto.ddcc1_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.ddcc1_1.trim(); }
	        	        			if ( ! dto.ddcc1_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.ddcc1_2.trim(); }
	        	        			if ( ! dto.ddcc1_3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "3 " + dto.ddcc1_3.trim(); }
	        	        			Fdd_Max = 4;
		    	        			break;
		    	        		case "evpc":
		    	        			// "colorbus"
		    	        			// System.out.println( "                evpc 'colorbus'" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'evpc' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	if ( ! dto.evpc_colorbus.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "colorbus " + dto.evpc_colorbus.trim(); }
		    	        	        }
		    	        			break;
		    	        		case "forti":
		    	        			// System.out.println( "                forti" );
		    	        			// ...
		    	        			break;
	        	        		case "hfdc":
	        	        			// "f1" .. "f4" fd, "h1" .. "h3" hd
	        	        			// System.out.println( "                hfdc 'f1' .. 'f4', 'h1' .. 'h3'" );
	        	        			if ( ! dto.hfdc_f1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f1 " + dto.hfdc_f1.trim(); }
	        	        			if ( ! dto.hfdc_f2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f2 " + dto.hfdc_f2.trim(); }
	        	        			if ( ! dto.hfdc_f3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f3 " + dto.hfdc_f3.trim(); }
	        	        			if ( ! dto.hfdc_f4.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "f4 " + dto.hfdc_f4.trim(); }
	        	        			if ( ! dto.hfdc_h1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h1 " + dto.hfdc_h1.trim(); }
	        	        			if ( ! dto.hfdc_h2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h2 " + dto.hfdc_h2.trim(); }
	        	        			if ( ! dto.hfdc_h3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "h3 " + dto.hfdc_h3.trim(); }
	        	        			Fdd_Max = 4;
	        	        			Hdd_Max = 3;
	        	        			break;
		    	        		case "horizon":
		    	        			// System.out.println( "                horizon" );
		    	        			// ...
		    	        			break;
	        	        		case "hsgpl":
	        	        			// System.out.println( "                hsgpl" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'hsgpl' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
	        	        			break;
		    	        		case "ide":
		    	        			// "0" .. "1"
		    	        			// System.out.println( "                ide '0' .. '1'" );
	        	        			if ( ! dto.ide_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "ata:0 " + dto.ide_0.trim(); }
	        	        			if ( ! dto.ide_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "ata:1 " + dto.ide_1.trim(); }
		    	        			break;
		    	        		case "myarcmem":
		    	        			// System.out.println( "                myarcmem" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'myarcmem' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "pcode":
		    	        			// System.out.println( "                pcode" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'pcode' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "pgram":
		    	        			// System.out.println( "                pgram" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'pgram' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "samsmem":
		    	        			// System.out.println( "                samsmem" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'samsmem' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "sidmaster":
		    	        			// System.out.println( "                sidmaster" );
		    	        	        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
		    	        	        	log.warn( "selected 'sidmaster' device is ignored due to incompatibility with machine 'Geneve' !" );
		    	        	        	mame_PebDevices[i] = "";
		    	        	        } else {
		    	        	        	// ...
		    	        	        }
		    	        			break;
		    	        		case "speechadapter":
		    	        			// System.out.println( "                speechadapter 'conn', 'extport'" );
		    	        			// "conn", "conn:speechsyn:extport"
		    	        			// assume: if "speechadapter" is selected for a slot then "conn:speechsyn" is selected for that adapter and "conn:speechsyn:extport" is left empty
		    	        			mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "conn speechsyn";
	        	        			// if ( ! dto.speech_conn.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "conn " + dto.speech_conn.trim(); }
		    	        			break;
		    	        		case "tifdc":
		    	        			// "0" .. "2" fd
		    	        			// System.out.println( "                tifdc '0' .. '2'" );
	        	        			if ( ! dto.tifdc_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "0 " + dto.tifdc_0.trim(); }
	        	        			if ( ! dto.tifdc_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "1 " + dto.tifdc_1.trim(); }
	        	        			if ( ! dto.tifdc_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "2 " + dto.tifdc_2.trim(); }
	        	        			Fdd_Max = 3;
		    	        			break;
		    	        		case "tipi":
		    	        			// System.out.println( "                tipi" );
		    	        			// ...
		    	        			break;
		    	        		case "tirs232":
		    	        			// System.out.println( "                tirs232" );
		    	        			// ...
		    	        			break;
		    	        		case "usbsm":
		    	        			// System.out.println( "                usbsm" );
		    	        			// ...
		    	        			break;
		    	        		case "whtscsi":
		    	        			// "0" .. "6" - "7" is the adapter itself
		    	        			// System.out.println( "                whtscsi '0' .. '6'" );
	        	        			if ( ! dto.whtscsi_0.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:0 " + dto.whtscsi_0.trim(); }
	        	        			if ( ! dto.whtscsi_1.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:1 " + dto.whtscsi_1.trim(); }
	        	        			if ( ! dto.whtscsi_2.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:2 " + dto.whtscsi_2.trim(); }
	        	        			if ( ! dto.whtscsi_3.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:3 " + dto.whtscsi_3.trim(); }
	        	        			if ( ! dto.whtscsi_4.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:4 " + dto.whtscsi_4.trim(); }
	        	        			if ( ! dto.whtscsi_5.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:5 " + dto.whtscsi_5.trim(); }
	        	        			if ( ! dto.whtscsi_6.equals( cbxSelNone )) { mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:6 " + dto.whtscsi_6.trim(); }
	        	        			// mame_PebDevices[i] = mame_PebDevices[i] + pebslot + "scsibus:7 controller";
		    	        			break;
	        	        		default:
	        	        			// System.out.println( "                !!! unknown !!!" );
	        	        			// ...
	        	        			break;
	        	        	}
	        	        	// System.out.println( "                " + i + ": --> '" + mame_PebDevices[i] + "'" );

	        	        // } else {

	        	        	// mame_PebDevices[i] = "";

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
        
        chk = dto.mame_CS1;
        if ( ! cbxSelNone.equals( chk )) {
        	mame_CS1 = chk;
        }

        chk = dto.mame_CS2;
        if ( ! cbxSelNone.equals( chk )) {
        	mame_CS2 = chk;
        }

        if ( Fdd_Max > 0 ) {
            chk = dto.mame_DSK1.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_DSK1 = chk;
            }
        } else {
        	log.warn( "no controller for Fdd 1 !" );
        }

        if ( Fdd_Max > 1 ) {
            chk = dto.mame_DSK2.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_DSK2 = chk;
            }
        } else {
        	log.warn( "no controller for Fdd 2 !" );
        }

        if ( Fdd_Max > 2 ) {
            chk = dto.mame_DSK3.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_DSK3 = chk;
            }
        } else {
        	log.warn( "no controller for Fdd 3 !" );
        }

        if ( Fdd_Max > 3 ) {
            chk = dto.mame_DSK4.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_DSK4 = chk;
            }
        } else {
        	log.warn( "no controller for Fdd 4 !" );
        }

        if ( Hdd_Max > 0 ) {
            chk = dto.mame_WDS1.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_WDS1 = chk;
            }
        } else {
        	log.warn( "no controller for Hdd 1 !" );
        }

        if ( Hdd_Max > 1 ) {
            chk = dto.mame_WDS2.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_WDS2 = chk;
            }
        } else {
        	log.warn( "no controller for Hdd 2 !" );
        }

        if ( Hdd_Max > 2 ) {
            chk = dto.mame_WDS3.trim();
            if ( ! cbxSelNone.equals( chk )) {
            	mame_WDS3 = chk;
            }
        } else {
        	log.warn( "no controller for Hdd 3 !" );
        }
        
        if ( ! mame_Machine.isEmpty() ) { result = result + mame_Machine; }
        
        if ( ! mame_AddOpt.isEmpty() ) { result = result + " " + mame_AddOpt; }
        
        if ( ! mame_RomPath.isEmpty() ) { result = result + " -rp " + mame_RomPath; }
        
        if ( ! mame_GromPort.isEmpty() ) { 
            if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
	        	log.warn( "selected 'GromPort' is ignored due to incompatibility with machine 'Geneve' !" );
            } else {
            	result = result + " -gromport " + mame_GromPort;
            }
        }
        if ( ! mame_Cartridge.isEmpty() ) { 
            if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) {
	        	log.warn( "selected 'Cartridge' is ignored due to incompatibility with machine 'Geneve' !" );
            } else {
            	result = result + " -cart " + mame_Cartridge;
            }
        }
        
        if ( ! mame_Joystick.isEmpty() ) { result = result + " -joyport " + mame_Joystick; }
        
        if ( ! mame_IoPort.isEmpty() ) { 

        	// System.out.println( "(" + 0 + ") : " + mame_IoPort );
        	result = result + " -ioport " + mame_IoPort;
        
	        // if "ioport: peb" is selected then scan for the slots
	        if ( "peb".equals(mame_IoPort) ) {
	    		for ( int i = 2; i < 9; i++ ) {
	    			// System.out.println( "(" + i + ") : " + mame_PebDevices[i] );
		        	if (( mame_PebDevices[i] != null ) && ( ! mame_PebDevices[i].isEmpty() )) { result = result + mame_PebDevices[i]; }
	    		}
	        }
	
	    	// if "speechsyn"
	    	
	    	// if "splitter"
	    	
	    	// if "arcturus
        
        }
        
        if ( ! mame_CS1.isEmpty() ) { result = result + " -cass1 " + mame_CsPath + mame_CS1; }
    	if ( ! mame_CS2.isEmpty() ) { result = result + " -cass2 " + mame_CsPath + mame_CS2; }

        if ( ! mame_DSK1.isEmpty() ) { result = result + " -flop1 " + mame_DskPath + mame_DSK1; }
        if ( ! mame_DSK2.isEmpty() ) { result = result + " -flop2 " + mame_DskPath + mame_DSK2; }
        if ( ! mame_DSK3.isEmpty() ) { result = result + " -flop3 " + mame_DskPath + mame_DSK3; }
        if ( ! mame_DSK4.isEmpty() ) { result = result + " -flop4 " + mame_DskPath + mame_DSK4; }

        if ( ! mame_WDS1.isEmpty() ) { result = result + " -hard1 " + mame_WdsPath + mame_WDS1; }
        if ( ! mame_WDS2.isEmpty() ) { result = result + " -hard2 " + mame_WdsPath + mame_WDS2; }
        if ( ! mame_WDS3.isEmpty() ) { result = result + " -hard3 " + mame_WdsPath + mame_WDS3; }
        
        if (( "geneve".equals( mame_Machine )) || ( "genmod".equals( mame_Machine ))) { 
        	result = result.replace(" -ioport peb", "");
        	result = result.replace(" -ioport:peb:", " -peb:");
        }
        
        // System.out.println( "--> " + result );
		return result;
		
	} // emulatorOptionsConcatenate()
	
	// --------------------------------------------------
	
	public static boolean emulatorStartProgram( EmulatorOptionsDTO dto ) {
		
		log.debug( "emulatorStartProgram()" );
		
		boolean result = true;
		
		String mame_WorkingPath = "";
		String mame_Executable = "";
		String mame_Parameter = "";
		
		// System.out.println( "emulatorStartProgram( EmulatorOptionsDTO dto )" );

		// result = true;

		mame_WorkingPath = dto.mame_WorkingPath;
		// System.out.println( "path: " + mame_WorkingPath );
		mame_Executable = dto.mame_Executable;
		// System.out.println( "exe:  " + mame_Executable );
		// mame_Parameter = txtEmulatorOptions.getText().trim();
		mame_Parameter = emulatorOptionsConcatenate( dto );
		// System.out.println( "parm: " + mame_Parameter );
		
	    try {
	        // Kommando zusammenbauen
	    	String fullExecutablePath = mame_WorkingPath + File.separator + mame_Executable;
	        List<String> command = new ArrayList<>();
	        command.add(fullExecutablePath);

	        // Parameter anhängen – hier per Split, falls deine Funktion einen String liefert
	        if (mame_Parameter != null && !mame_Parameter.isEmpty()) {
	            command.addAll(Arrays.asList(mame_Parameter.split(" ")));
	        }
	        // System.out.println("Kommando-Argumente:");
	        for (int i = 0; i < command.size(); i++) {
	        	// System.out.println("[" + i + "] " + command.get(i));
	        }
	        
	        // ProcessBuilder mit Arbeitsverzeichnis
	        ProcessBuilder pb = new ProcessBuilder(command);
	        pb.directory(new File(mame_WorkingPath));

	        // Ausgabe/Fehler direkt in die Konsole umleiten
	        pb.inheritIO();

	        // Prozess starten
	        Process process = pb.start();

	        // Optional: auf Ende warten
	        int exitCode = process.waitFor();
	        System.out.println("Programm beendet mit Code: " + exitCode);

	    } catch (IOException ex) {

	        ex.printStackTrace();
	        result = false; // Fehlerfall
	        
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Interrupt-Status wieder setzen
	        e.printStackTrace(); // oder eigene Behandlung

	    }

	    return result;
		
	} // emulatorStartProgram()
	
	// --------------------------------------------------
	
} // class MameTools

//############################################################################

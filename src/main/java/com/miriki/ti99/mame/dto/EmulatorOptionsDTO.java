package com.miriki.ti99.mame.dto;

import java.nio.file.Path;

//############################################################################

public class EmulatorOptionsDTO {

	// --------------------------------------------------
	
	public Path cartPathP;
	public Path fddPathP1;
	public Path fddPathP2;
	public Path fddPathP3;
	public Path fddPathP4;
	public Path hddPathP1;
	public Path hddPathP2;
	public Path hddPathP3;
	public Path cassPathP1;
	public Path cassPathP2;

	public Path cartPathRel;
	public Path fddPathRel1;
	public Path fddPathRel2;
	public Path fddPathRel3;
	public Path fddPathRel4;
	public Path hddPathRel1;
	public Path hddPathRel2;
	public Path hddPathRel3;
	public Path cassPathRel1;
	public Path cassPathRel2;
	
	// emulator (mame)

    public String mame_WorkingPath = "";

    public String mame_Executable = "";
	
    public String mame_RomPath = "";
    
    public String mame_CartPath = "";
	
	// console
    
    public String mame_Machine = "";
    
    public String mame_GromPort = "";
    
    public String mame_Cartridge = "";
    // public CartridgeEntry mame_Cartridge = null;
    public CartridgeEntry cartEntry;
    public String cartDisplayName;
    
    public String mame_JoyPort = "";
	
    public String mame_IoPort = "";
    
    // media

    public String mame_DskPath = "";
    
    public String mame_DSK1 = "";
    public String mame_DSK2 = "";
    public String mame_DSK3 = "";
    public String mame_DSK4 = "";

    public String mame_WdsPath = "";
    
    public String mame_WDS1 = "";
    public String mame_WDS2 = "";
    public String mame_WDS3 = "";
    
    public String mame_CsPath = "";

    public String mame_CS1 = "";
    public String mame_CS2 = "";

	// peb slots
    
    public String[] PebDevices = new String[9];

    // -----
    
    public String bwg_0 = "";
    public String bwg_1 = "";
    public String bwg_2 = "";
    public String bwg_3 = "";
    
    public String ccdcc_0 = "";
    public String ccdcc_1 = "";
    public String ccdcc_2 = "";
    public String ccdcc_3 = "";

    public String ccfdc_0 = "";
    public String ccfdc_1 = "";
    public String ccfdc_2 = "";
    public String ccfdc_3 = "";

    public String ddcc1_0 = "";
    public String ddcc1_1 = "";
    public String ddcc1_2 = "";
    public String ddcc1_3 = "";

    public String evpc_colorbus = "";
    
    public String hfdc_f1 = "";
    public String hfdc_f2 = "";
    public String hfdc_f3 = "";
    public String hfdc_f4 = "";
    public String hfdc_h1 = "";
    public String hfdc_h2 = "";
    public String hfdc_h3 = "";

    public String ide_0 = "";
    public String ide_1 = "";

    public String tifdc_0 = "";
    public String tifdc_1 = "";
    public String tifdc_2 = "";

    public String whtscsi_0 = "";
    public String whtscsi_1 = "";
    public String whtscsi_2 = "";
    public String whtscsi_3 = "";
    public String whtscsi_4 = "";
    public String whtscsi_5 = "";
    public String whtscsi_6 = "";
    // public String whtscsi_7 = "controller";

    // other
    
    public String mame_AddOpt = "";

	// --------------------------------------------------
	
} // class EmulatorOptionsDTO

//############################################################################

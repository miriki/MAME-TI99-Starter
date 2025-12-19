package MameTools;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

//############################################################################

public class EmulatorOptionsDTO {

	// --------------------------------------------------
	
    // private static final Logger log = LoggerFactory.getLogger( EmulatorOptionsDTO.class );
	
	// emulator (mame)

    public String mame_Executable = "";
    public String mame_WorkingPath = "";
	
    public String mame_AddOpt = "";

    public String mame_RomPath = "";
    public String mame_CartPath = "";
	
    public String mame_DskPath = "";
    public String mame_WdsPath = "";
    public String mame_CsPath = "";

	// console
    
    public String mame_Machine = "";
    
    public String mame_GromPort = "";
    public String mame_Cartridge = "";
    
    public String mame_JoyPort = "";
	
    public String mame_IoPort = "";

	// peb slots
    
    /*
    public String pebdevice2 = "";
    public String pebdevice3 = "";
    public String pebdevice4 = "";
    public String pebdevice5 = "";
    public String pebdevice6 = "";
    public String pebdevice7 = "";
    public String pebdevice8 = "";
    */
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
    
    // media
	
    public String mame_CS1 = "";
    public String mame_CS2 = "";

    public String mame_DSK1 = "";
    public String mame_DSK2 = "";
    public String mame_DSK3 = "";
    public String mame_DSK4 = "";

    public String mame_WDS1 = "";
    public String mame_WDS2 = "";
    public String mame_WDS3 = "";
    
	// --------------------------------------------------
	
} // class EmulatorOptionsDTO

//############################################################################


package MameTools;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import MameTools.model.FiadFile;
import MameTools.model.TiFileType;
import MameTools.util.FileUtils;

public class TiFilesConverter {

    // private static final Logger log = LoggerFactory.getLogger(TiFilesConverter.class);

    public static List<FiadFile> convertFiles(List<File> files) throws Exception {
        List<FiadFile> result = new ArrayList<>();

        for (File f : files) {
        	
        	String tiName = FileUtils.toTiName(f.getName());
        	
        	byte[] data = Files.readAllBytes(f.toPath());
        	
        	byte[] header = TiFilesHeaderBuilder.buildHeader(
        	    // f.getName(),
        	    tiName,
        	    data.length,
        	    TiFileType.PROGRAM,
        	    0,
        	    0
        		);

        	FiadFile fiad = new FiadFile(
        		tiName,
        	    TiFileType.PROGRAM,
        	    0,
        	    0,
        	    header,
        	    data
        		);

        	result.add(fiad);
            
        }

        return result;
    }
}

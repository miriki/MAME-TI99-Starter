package MameTools;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MameTools.model.FiadFile;
import MameTools.model.DiskFormat;

// Aufruf mit
// new DiskBuilder( DiskFormat.SSSD, false ).buildDiskFromDirectory( new File( "C:/TI99/FIAD/SomeDiskTest" ));
// new DiskBuilder(DiskFormat.SSSD, true)

public class DiskBuilder {

    private static final Logger log = LoggerFactory.getLogger(DiskBuilder.class);

    private final DiskFormat format;
    private final boolean recursive;

    public DiskBuilder(DiskFormat format, boolean recursive) { 
    	this.format = format;
    	this.recursive = recursive; 
    }
    
    public File buildDiskFromDirectory(File directory) throws Exception {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        // 1. Verzeichnis rekursiv einlesen
        List<File> files = DirectoryScanner.scan(directory, recursive);

        // 2. Dateien → FIAD konvertieren
        List<FiadFile> fiadFiles = TiFilesConverter.convertFiles(files);

        // 3. DSK-Image erzeugen
        byte[] dskImage = DskImageWriter.createEmptyImage(format);
        // VIB schreiben
        VIBWriter.writeVIB(dskImage, format, directory.getName());

        // 4. FIAD-Dateien ins Image schreiben
        for (FiadFile fiad : fiadFiles) {
        	log.debug("### ADDING {}", fiad.getTiName());
            DskImageWriter.addFile(dskImage, fiad);
        }

        // 5. Image speichern
        File output = new File(directory.getParentFile(), directory.getName() + ".dsk");
        DskImageWriter.writeImage(output, dskImage);

        return output;
    }
}

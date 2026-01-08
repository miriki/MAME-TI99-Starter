package MameTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class DirectoryScanner {

    // private static final Logger log = LoggerFactory.getLogger(DirectoryScanner.class);

	public static List<File> scan(File dir, boolean recursive) {
	    List<File> result = new ArrayList<>();
	    scanInternal(dir, recursive, result);
	    return result;
	}

	private static void scanInternal(File dir, boolean recursive, List<File> result) {
	    for (File f : dir.listFiles()) {
	        if (f.isDirectory()) {
	            if (recursive) {
	                scanInternal(f, true, result);
	            }
	        } else {
	            result.add(f);
	        }
	    }
	}
}

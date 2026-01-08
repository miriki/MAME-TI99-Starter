package MameTools;

import java.io.ByteArrayOutputStream;
import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import MameTools.model.TiFileType;

public class RecordEncoder {

    // private static final Logger log = LoggerFactory.getLogger(RecordEncoder.class);

    public static byte[] encodeRecords(List<byte[]> records, 
                                       TiFileType type, 
                                       int recordLength) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (byte[] r : records) {

            switch (type) {

                case DISFIX:
                    // fixed-length records
                    int len = Math.min(r.length, recordLength);
                    out.write(r, 0, len);

                    // pad to full record length
                    for (int i = len; i < recordLength; i++) {
                        out.write(0x00);
                    }
                    break;

                case DISVAR:
                    // variable-length record: [len][data...]
                    out.write(r.length);
                    out.write(r, 0, r.length);
                    break;

                case INTFIX:
                    // PROGRAM hat keine Records – sollte hier nicht landen
                    out.write(r, 0, r.length);
                    break;

                case INTVAR:
                    // PROGRAM hat keine Records – sollte hier nicht landen
                    out.write(r, 0, r.length);
                    break;

                case PROGRAM:
                    // PROGRAM hat keine Records – sollte hier nicht landen
                    out.write(r, 0, r.length);
                    break;

            }
        }

        return out.toByteArray();
    }
}

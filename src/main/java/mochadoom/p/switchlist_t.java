package mochadoom.p;

import mochadoom.w.CacheableDoomObject;
import mochadoom.w.DoomBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
//
// P_SWITCH
//

public class switchlist_t
        implements CacheableDoomObject {
    
    public switchlist_t(){
        
    }
    
    // Were char[9]
    public String name1;

    public String name2;

    public short episode;

    public switchlist_t(String name1, String name2, int episode) {
        super();
        this.name1 = name1;
        this.name2 = name2;
        this.episode = (short) episode;
    }

    @Override
    public void unpack(ByteBuffer buf)
            throws IOException {
        // Like most Doom structs...
        buf.order(ByteOrder.LITTLE_ENDIAN);
        this.name1 = DoomBuffer.getNullTerminatedString(buf, 9);
        this.name2 = DoomBuffer.getNullTerminatedString(buf, 9);
        this.episode = buf.getShort();
    }

    public final static int size() {
        return 20;
    }

    public String toString() {
        return String.format("%mochadoom.s %mochadoom.s %d", name1, name2, episode);
    }
}
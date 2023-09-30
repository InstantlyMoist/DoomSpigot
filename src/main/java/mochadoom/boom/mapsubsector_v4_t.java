package mochadoom.boom;

import mochadoom.w.CacheableDoomObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class mapsubsector_v4_t implements CacheableDoomObject{
	
    public char     numsegs;
  	/** Index of first one, segs are stored sequentially. */
  	public int     firstseg;
  
  	@Override
	public void unpack(ByteBuffer buf)
        throws IOException {
    buf.order(ByteOrder.LITTLE_ENDIAN);
    this.numsegs = buf.getChar();
    this.firstseg = buf.getInt();        
	} 

	public static int sizeOf(){
    return 6;
	}
	
}

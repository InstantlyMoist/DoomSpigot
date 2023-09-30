package mochadoom.boom;

import mochadoom.utils.C2JUtils;
import mochadoom.w.CacheableDoomObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class mapsubsector_znod_t implements CacheableDoomObject{
	
    public long numsegs;
      
  	@Override
	public void unpack(ByteBuffer buf)
        throws IOException {
    buf.order(ByteOrder.LITTLE_ENDIAN);
    this.numsegs = C2JUtils.unsigned(buf.getInt());
	} 

	public static final int sizeOf(){
    return 4;
	}
	
}

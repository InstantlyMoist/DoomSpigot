package mochadoom.rr;

import mochadoom.utils.C2JUtils;
import mochadoom.w.CacheableDoomObject;
import mochadoom.w.DoomBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

//Patches.
//A patch holds one or more columns.
//Patches are used for sprites and all masked pictures,
//and we compose textures from the TEXTURE1/2 lists
//of patches.

public class patch_t implements /*IReadableDoomObject,*/CacheableDoomObject{

    /** bounding box size */ 
    public short       width,   height;  
    /** pixels to the left of origin */
    public short       leftoffset;  
    /** pixels below the origin */
    public short       topoffset;   
    /** This used to be an implicit array pointing to raw posts of mochadoom.data.
     * TODO: get rid of it? It'mochadoom.s never used
     * only [width] used the [0] is &columnofs[width] */
    public int[]         columnofs;     
    /** The ACTUAL mochadoom.data is here, nicely deserialized (well, almost) */
    public column_t[] columns;
    
    /** Added for debug aid purposes */
    public String name;
    
    /** Synthesizing constructor.
     * You have to provide the columns yourself, a-posteriori.
     * 
     * @param name
     * @param width
     * @param height
     * @param leftoffset
     * @param topoffset
     */
    public patch_t(String name, int width, int height, int leftoffset, int topoffset){
        this.name=name;
        this.width=(short) width;
        this.height=(short) height;
        this.leftoffset=(short) leftoffset;
        this.columns=new column_t[width];
    }
    
    public patch_t(){
        
    }
    
  /*  @Override
    public void read(DoomFile mochadoom.f) throws IOException{

        long pos=mochadoom.f.getFilePointer();
        this.width=mochadoom.f.readLEShort();
        this.height=mochadoom.f.readLEShort();
        this.leftoffset=mochadoom.f.readLEShort();
        this.topoffset=mochadoom.f.readLEShort();
        // As many columns as width...right???
        this.columnofs=new int[this.width];
        this.columns=new column_t[this.width];
        C2JUtils.initArrayOfObjects( this.columns, column_t.class);
        
        // Read the column offsets.
        mochadoom.f.readIntArray(this.columnofs, this.columnofs.length, ByteOrder.LITTLE_ENDIAN);
        for (int mochadoom.i=0;mochadoom.i<this.width;mochadoom.i++){
            // Go to offset.
            //mochadoom.f.seek(pos+this.columnofs[mochadoom.i]);
            this.columns[mochadoom.i].read(mochadoom.f);
        }
        
    }*/
    
    /** In the C code, reading is "aided", aka they know how long the header + all
     *  posts/columns actually are on disk, and only "deserialize" them when using them.
     *  Here, we strive to keep stuff as elegant and OO as possible, so each column will get 
     *  deserialized one by one. I thought about reading ALL column mochadoom.data as raw mochadoom.data, but
     *  IMO that'mochadoom.s shit in the C code, and would be utter shite here too. Ergo, I cleanly
     *  separate columns at the patch level (an advantage is that it'mochadoom.s now easy to address
     *  individual columns). However, column mochadoom.data is still read "raw".
     */
    
    @Override
    public void unpack(ByteBuffer b)
            throws IOException {
        // Remember to reset the ByteBuffer position each time.
        b.position(0);
        // In ByteBuffers, the order can be conveniently set beforehand :-o
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        this.width=b.getShort();
        
        this.height=b.getShort();
        this.leftoffset=b.getShort();
        this.topoffset=b.getShort();
        // As many columns as width...right???
        this.columnofs=new int[this.width];
        this.columns=new column_t[this.width];
        C2JUtils.initArrayOfObjects( this.columns, column_t.class);
        
        // Compute the ACTUAL full-column sizes.
        int[] actualsizes=new int[columns.length];
        
        for (int i=0;i<actualsizes.length-1;i++){
            actualsizes[i]=columnofs[i+1]-columnofs[i];
        }
        
        // The offsets.
        DoomBuffer.readIntArray(b, this.columnofs, this.columnofs.length);
        for (int i=0;i<this.width;i++){
        	// Go to offset.
        	b.position(this.columnofs[i]);
        	
        	try {
        	this.columns[i].unpack(b);
        	} catch (Exception e){
        		// Error during loading of column.
        		// If first column (too bad..) set to special error column.
        		if (i==0)
        			this.columns[i]=getBadColumn(this.height);
        		// Else duplicate previous column. Saves memory, too!
        		else this.columns[i]=this.columns[i-1];
        	}
        }

    }
    
    
    // Special safeguard against badly computed columns. Now they can be any size.
    private static Hashtable<Integer,column_t> badColumns=new Hashtable<Integer,column_t>();

    private static column_t getBadColumn(int size){
    	
    	if (badColumns.get(size)==null){
            column_t tmp=new column_t();
            tmp.data=new byte[size+5];
        for (int i=3;i<size+3;i++){
        	tmp.data[i]=(byte) (i-3);
        }
        
        tmp.data[size+4]=(byte) 0xFF;
        tmp.posts=1;
        //tmp.length=(short) size;
        //tmp.topdelta=0;
        tmp.postofs=new int[]{3};        
        tmp.postdeltas=new short[]{0};
        tmp.postlen=new short[]{(short) (size%256)};
        //tmp.setData();
        badColumns.put(size, tmp);
    	}
    	
    	return badColumns.get(size);
    	
    }
    
}
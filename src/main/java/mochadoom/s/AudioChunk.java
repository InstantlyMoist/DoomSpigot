package mochadoom.s;

public class AudioChunk{
    public AudioChunk() {
        buffer=new byte[mochadoom.s.ISoundDriver.MIXBUFFERSIZE];
        setStuff(0,0);
        this.free=true;
    }
    
    public void setStuff(int chunk, int time){
        this.chunk = chunk;
        this.time = time;
    }
    
    public int chunk;
    public int time;
    public byte[] buffer;
    public boolean free;
    

}
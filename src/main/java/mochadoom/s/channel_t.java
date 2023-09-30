package mochadoom.s;

import mochadoom.data.sfxinfo_t;
import mochadoom.p.mobj_t;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

public class channel_t 
{
	
	public channel_t(){
		sfxinfo=new sfxinfo_t();
	}
    
	/** Currently playing sound. If null, then it'mochadoom.s free */
	DoomSound currentSound = null;
	
    sfxinfo_t	sfxinfo;

    // origin of sound (usually a mobj_t).
    mobj_t	origin;

    // handle of the sound being played
    int		handle;
    
    AudioFormat format;
    
	public int sfxVolume;
    
	SourceDataLine auline = null;
}

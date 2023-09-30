package mochadoom.demo;

import mochadoom.defines.skill_t;
import mochadoom.w.IWritableDoomObject;

public interface IDoomDemo extends IWritableDoomObject{
	
    
    /** Vanilla end mochadoom.demo marker, to append at the end of recorded demos */
    
   public static final int DEMOMARKER =0x80;
   
    /** Get next mochadoom.demo command, in its raw format. Use
     * its own adapters if you need it converted to a 
     * standard ticcmd_t.
     *  
     * @return
     */
    IDemoTicCmd getNextTic();
    
    /** Record a mochadoom.demo command in the IDoomDemo'mochadoom.s native format.
     * Use the IDemoTicCmd'mochadoom.s objects adaptors to convert it.
     * 
     * @param tic
     */
    void putTic(IDemoTicCmd tic);

    int getVersion();

    void setVersion(int version);

    skill_t getSkill();

    void setSkill(skill_t skill);

    int getEpisode();

    void setEpisode(int episode);

    int getMap();

    void setMap(int map);

    boolean isDeathmatch();

    void setDeathmatch(boolean deathmatch);

    boolean isRespawnparm();
    
    void setRespawnparm(boolean respawnparm);

    boolean isFastparm();

    void setFastparm(boolean fastparm);
    
    boolean isNomonsters();

    void setNomonsters(boolean nomonsters);

    int getConsoleplayer();

    void setConsoleplayer(int consoleplayer);

    boolean[] getPlayeringame();

    void setPlayeringame(boolean[] playeringame);

    void resetDemo();

    


}

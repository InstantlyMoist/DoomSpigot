package mochadoom.p;

import mochadoom.doom.SourceCode.P_Tick;
import mochadoom.doom.thinker_t;

import static mochadoom.doom.SourceCode.P_Tick.*;

public interface ThinkerList {

    @P_Tick.C(P_AddThinker)
    void AddThinker(thinker_t thinker);
    @P_Tick.C(P_RemoveThinker)
    void RemoveThinker(thinker_t thinker);
    @P_Tick.C(P_InitThinkers)
    void InitThinkers();
    
    thinker_t getRandomThinker();
    thinker_t getThinkerCap();
}

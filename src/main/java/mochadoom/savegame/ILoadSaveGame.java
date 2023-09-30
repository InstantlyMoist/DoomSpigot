package mochadoom.savegame;

import mochadoom.p.ThinkerList;

public interface ILoadSaveGame {
    void setThinkerList(ThinkerList li);
    void doSave(ThinkerList li);
    void doLoad(ThinkerList li);
}

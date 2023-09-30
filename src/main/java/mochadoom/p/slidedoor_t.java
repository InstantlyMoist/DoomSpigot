package mochadoom.p;

import mochadoom.rr.SectorAction;
import mochadoom.rr.line_t;
import mochadoom.rr.sector_t;

import static mochadoom.p.ActiveStates.T_SlidingDoor;

public class slidedoor_t extends SectorAction {
    public sdt_e type;
    public line_t line;
    public int frame;
    public int whichDoorIndex;
    public int timer;
    public sector_t frontsector;
    public sector_t backsector;
    public sd_e status;

    public slidedoor_t() {
        type = sdt_e.sdt_closeOnly;
        status = sd_e.sd_closing;
        thinkerFunction = T_SlidingDoor;
    }
}

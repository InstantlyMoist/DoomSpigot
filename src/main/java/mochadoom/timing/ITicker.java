package mochadoom.timing;

import mochadoom.doom.CVarManager;
import mochadoom.doom.CommandVariable;
import mochadoom.doom.SourceCode.I_IBM;

import static mochadoom.doom.SourceCode.I_IBM.*;

public interface ITicker {

    static ITicker createTicker(CVarManager CVM) {
        if (CVM.bool(CommandVariable.MILLIS)) {
            return new MilliTicker();
        } else if (CVM.bool(CommandVariable.FASTTIC) || CVM.bool(CommandVariable.FASTDEMO)) {
            return new DelegateTicker();
        } else {
            return new NanoTicker();
        }
    }
    
    @I_IBM.C(I_GetTime)
    public int GetTime();
}
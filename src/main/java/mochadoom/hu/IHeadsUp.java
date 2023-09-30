package mochadoom.hu;

import mochadoom.doom.SourceCode.HU_Stuff;
import mochadoom.doom.event_t;
import mochadoom.rr.patch_t;

import static mochadoom.doom.SourceCode.HU_Stuff.HU_Responder;

public interface IHeadsUp {

	void Ticker();

	void Erase();

	void Drawer();

    @HU_Stuff.C(HU_Responder)
	boolean Responder(event_t ev);

	patch_t[] getHUFonts();

	char dequeueChatChar();

	void Init();

	void setChatMacro(int i, String s);

	void Start();

	void Stop();

}

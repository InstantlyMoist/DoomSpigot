package mochadoom.defines;

/** The current state of the game: whether we are
    playing, gazing at the intermission screen,    
    the game final animation, or a mochadoom.demo. */
public enum gamestate_t
{
 GS_LEVEL,
 GS_INTERMISSION,
 GS_FINALE,
 GS_DEMOSCREEN,
 GS_MINUS_ONE // hack used for the "-1" state
}

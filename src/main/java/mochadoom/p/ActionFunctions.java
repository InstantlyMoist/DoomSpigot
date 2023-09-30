/*
 * Copyright (C) 1993-1996 by id Software, Inc.
 * Copyright (C) 2017 Good Sign
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mochadoom.p;

import mochadoom.automap.IAutoMap;
import mochadoom.data.sounds;
import mochadoom.defines.skill_t;
import mochadoom.doom.DoomMain;
import mochadoom.doom.player_t;
import mochadoom.hu.IHeadsUp;
import mochadoom.i.IDoomSystem;
import mochadoom.p.Actions.ActionsAttacks;
import mochadoom.p.Actions.ActionsEnemies;
import mochadoom.p.Actions.ActionsThinkers;
import mochadoom.p.Actions.ActiveStates.Ai;
import mochadoom.p.Actions.ActiveStates.Attacks;
import mochadoom.p.Actions.ActiveStates.Thinkers;
import mochadoom.p.Actions.ActiveStates.Weapons;
import mochadoom.rr.SceneRenderer;
import mochadoom.s.ISoundOrigin;
import mochadoom.st.IDoomStatusBar;
import mochadoom.utils.TraitFactory;
import mochadoom.utils.TraitFactory.SharedContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionFunctions extends UnifiedGameMap implements
    ActionsThinkers, ActionsEnemies, ActionsAttacks, Ai, Attacks, Thinkers, Weapons
{
    private final SharedContext traitsSharedContext;
    
    public ActionFunctions(final DoomMain<?, ?> DOOM) {
        super(DOOM);
        this.traitsSharedContext = buildContext();
    }
    
    private SharedContext buildContext() {
        try {
            return TraitFactory.build(this, ACTION_KEY_CHAIN);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ActionFunctions.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public AbstractLevelLoader levelLoader() {
        return DOOM.levelLoader;
    }

    @Override
    public IHeadsUp headsUp() {
        return DOOM.headsUp;
    }

    @Override
    public IDoomSystem doomSystem() {
        return DOOM.doomSystem;
    }

    @Override
    public IDoomStatusBar statusBar() {
        return DOOM.statusBar;
    }

    @Override
    public IAutoMap<?, ?> autoMap() {
        return DOOM.autoMap;
    }

    @Override
    public SceneRenderer<?, ?> sceneRenderer() {
        return DOOM.sceneRenderer;
    }

    @Override
    public UnifiedGameMap.Specials getSpecials() {
        return SPECS;
    }

    @Override
    public UnifiedGameMap.Switches getSwitches() {
        return SW;
    }

    @Override
    public void StopSound(ISoundOrigin origin) {
        DOOM.doomSound.StopSound(origin);
    }

    @Override
    public void StartSound(ISoundOrigin origin, sounds.sfxenum_t s) {
        DOOM.doomSound.StartSound(origin, s);
    }

    @Override
    public void StartSound(ISoundOrigin origin, int s) {
        DOOM.doomSound.StartSound(origin, s);
    }

    @Override
    public player_t getPlayer(int number) {
        return DOOM.players[number];
    }

    @Override
    public skill_t getGameSkill() {
        return DOOM.gameskill;
    }

    @Override
    public mobj_t createMobj() {
        return mobj_t.createOn(DOOM);
    }

    @Override
    public int LevelTime() {
        return DOOM.leveltime;
    }

    @Override
    public int P_Random() {
        return DOOM.random.P_Random();
    }

    @Override
    public int ConsolePlayerNumber() {
        return DOOM.consoleplayer;
    }

    @Override
    public int MapNumber() {
        return DOOM.gamemap;
    }

    @Override
    public boolean PlayerInGame(int number) {
        return DOOM.playeringame[number];
    }

    @Override
    public boolean IsFastParm() {
        return DOOM.fastparm;
    }

    @Override
    public boolean IsPaused() {
        return DOOM.paused;
    }

    @Override
    public boolean IsNetGame() {
        return DOOM.netgame;
    }

    @Override
    public boolean IsDemoPlayback() {
        return DOOM.demoplayback;
    }

    @Override
    public boolean IsDeathMatch() {
        return DOOM.deathmatch;
    }

    @Override
    public boolean IsAutoMapActive() {
        return DOOM.automapactive;
    }

    @Override
    public boolean IsMenuActive() {
        return DOOM.menuactive;
    }

    /**
     * TODO: avoid, deprecate
     */
    @Override
    public DoomMain<?, ?> DOOM() {
        return DOOM;
    }

    @Override
    public SharedContext getContext() {
        return traitsSharedContext;
    }

    @Override
    public ActionsThinkers getThinkers() {
        return this;
    }

    @Override
    public ActionsEnemies getEnemies() {
        return this;
    }

    @Override
    public ActionsAttacks getAttacks() {
        return this;
    }
}

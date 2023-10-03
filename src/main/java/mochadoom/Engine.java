/*
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

package mochadoom;

import me.kyllian.doom.DoomPlugin;
import mochadoom.awt.DoomWindow;
import mochadoom.awt.DoomWindowController;
import mochadoom.awt.EventBase.KeyStateInterest;
import mochadoom.awt.EventHandler;
import mochadoom.doom.CVarManager;
import mochadoom.doom.CommandVariable;
import mochadoom.doom.ConfigManager;
import mochadoom.doom.DoomMain;
import mochadoom.i.Strings;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mochadoom.awt.EventBase.KeyStateSatisfaction.*;
import static mochadoom.g.Signals.ScanCode.*;

public class Engine {
    private static volatile Engine instance;
    private DoomPlugin plugin;
    private Player player;
    public boolean running = true;
    
    public final CVarManager cvm;
    public final ConfigManager cm;
    public final DoomWindowController<?, EventHandler> windowController;
    private final DoomMain<?, ?> DOOM;
    
    @SuppressWarnings("unchecked")
    public Engine(DoomPlugin plugin, Player player, final String... argv) throws IOException {
        instance = this;
        instance.plugin = plugin;
        instance.player = player;
        
        // reads command line arguments
        this.cvm = new CVarManager(Arrays.asList(argv));
        
        // reads default.cfg and mochadoom.cfg
        this.cm = new ConfigManager();
        
        // intiializes stuff
        this.DOOM = new DoomMain<>();
        
        // opens a window
        this.windowController = /*cvm.bool(CommandVariable.AWTFRAME)
            ? */DoomWindow.createCanvasWindowController(
                DOOM.graphicSystem::getScreenImage,
                DOOM::PostEvent,
                DOOM.graphicSystem.getScreenWidth(),
                DOOM.graphicSystem.getScreenHeight()
            )/* : DoomWindow.createJPanelWindowController(
                DOOM.graphicSystem::getScreenImage,
                DOOM::PostEvent,
                DOOM.graphicSystem.getScreenWidth(),
                DOOM.graphicSystem.getScreenHeight()
            )*/;
        
        windowController.getObserver().addInterest(
            new KeyStateInterest<>(obs -> {
                EventHandler.fullscreenChanges(windowController.getObserver(), windowController.switchFullscreen());
                return WANTS_MORE_ATE;
            }, SC_LALT, SC_ENTER)
        ).addInterest(
            new KeyStateInterest<>(obs -> {
                if (!windowController.isFullscreen()) {
                    if (DOOM.menuactive || DOOM.paused || DOOM.demoplayback) {
                        EventHandler.menuCaptureChanges(obs, DOOM.mousecaptured = !DOOM.mousecaptured);
                    } else { // can also work when not DOOM.mousecaptured
                        EventHandler.menuCaptureChanges(obs, DOOM.mousecaptured = true);
                    }
                }
                return WANTS_MORE_PASS;
            }, SC_LALT)
        ).addInterest(
            new KeyStateInterest<>(obs -> {
                if (!windowController.isFullscreen() && !DOOM.mousecaptured && DOOM.menuactive) {
                    EventHandler.menuCaptureChanges(obs, DOOM.mousecaptured = true);
                }
                
                return WANTS_MORE_PASS;
            }, SC_ESCAPE)
        ).addInterest(
            new KeyStateInterest<>(obs -> {
                if (!windowController.isFullscreen() && !DOOM.mousecaptured && DOOM.paused) {
                    EventHandler.menuCaptureChanges(obs, DOOM.mousecaptured = true);
                }
                return WANTS_MORE_PASS;
            }, SC_PAUSE)
        );
    }

    public void run() {
        try {
            instance.DOOM.setupLoop();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Temporary solution. Will be later moved in more detalied place
     */
    public static void updateFrame() {
        instance.windowController.updateFrame();
    }
        
    public String getWindowTitle(double frames) {
        if (cvm.bool(CommandVariable.SHOWFPS)) {
            return String.format("%s - %s FPS: %.2f", Strings.MOCHA_DOOM_TITLE, DOOM.bppMode, frames);
        } else {
            return String.format("%s - %s", Strings.MOCHA_DOOM_TITLE, DOOM.bppMode);
        }
    }

    public static Engine getEngine() {
        Engine local = Engine.instance;
        if (local == null) {
            synchronized (Engine.class) {
                local = Engine.instance;
                if (local == null) {
                    try {
                        Engine.instance = local = new Engine(null, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
                        throw new Error("This launch is DOOMed");
                    }
                }
            }
        }

        return local;
    }
    
    public static CVarManager getCVM() {
        return getEngine().cvm;
    }
    
    public static ConfigManager getConfig() {
        return getEngine().cm;
    }

    public DoomPlugin getPlugin() {
        return plugin;
    }

    public Player getPlayer() {
        return player;
    }
}

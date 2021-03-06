/*
 * MediathekView
 * Copyright (C) 2008 W. Xaver
 * W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.mediathekview.mtool;

import de.mediathekview.mtool.console.Check;
import de.mediathekview.mtool.gui.MtGui;
import de.mediathekview.mtool.tools.MtLog;
import javafx.application.Application;
import mServer.crawler.CrawlerTool;
import mServer.tool.MserverDaten;

public class Main {

    public Main() {
    }

    private enum StartupMode {

        VERSION, GUI, CHECK
    }

    public static void main(String[] args) {

        StartupMode state = StartupMode.GUI;

        if (args != null) {
            for (String s : args) {
                s = s.toLowerCase();
                switch (s) {
                    case "-d":
                        MserverDaten.debug = true;
                        break;
                    case "-v":
                        state = StartupMode.VERSION;
                        break;
                    case "-c":
                        state = StartupMode.CHECK;
                        break;
                }
            }
        }

        switch (state) {
            case VERSION:
                MtLog.versionsMeldungen(Main.class.toString());
                System.exit(0);
                break;
            case GUI:
                java.awt.EventQueue.invokeLater(() -> {
                    CrawlerTool.startMsg();
                    Application.launch(MtGui.class, args);
                });
                break;
            case CHECK:
                new Check(args).start();
                System.exit(0);
        }

    }

}

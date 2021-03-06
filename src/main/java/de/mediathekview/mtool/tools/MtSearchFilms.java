/*
 * MediathekView
 * Copyright (C) 2016 W. Xaver
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
package de.mediathekview.mtool.tools;

import javax.swing.event.EventListenerList;

import de.mediathekview.mlib.Config;
import de.mediathekview.mlib.filmesuchen.ListenerFilmeLaden;
import de.mediathekview.mlib.filmesuchen.ListenerFilmeLadenEvent;
import javafx.application.Platform;
import mServer.crawler.FilmeSuchen;

public class MtSearchFilms {

    private enum ListenerMelden {

        START, PROGRESS, FINISHED
    }
    public FilmeSuchen msFilmeSuchen;
    private final EventListenerList listeners = new EventListenerList();
    private boolean istAmLaufen = false;

    public MtSearchFilms() {
        msFilmeSuchen = new FilmeSuchen();
        msFilmeSuchen.addAdListener(new ListenerFilmeLaden() {
            @Override
            public synchronized void start(ListenerFilmeLadenEvent event) {
                for (ListenerFilmeLaden l : listeners.getListeners(ListenerFilmeLaden.class)) {
                    Platform.runLater(() -> l.start(event));
                }
            }

            @Override
            public synchronized void progress(ListenerFilmeLadenEvent event) {
                for (ListenerFilmeLaden l : listeners.getListeners(ListenerFilmeLaden.class)) {
                    Platform.runLater(() -> l.progress(event));
                }
            }

            @Override
            public synchronized void fertig(ListenerFilmeLadenEvent event) {
                // Ergebnisliste listeFilme eintragen -> Feierabend!
                MtGuiData.mtFilmList.loadFilmList(msFilmeSuchen.listeFilmeNeu);
                istAmLaufen = false;
                for (ListenerFilmeLaden l : listeners.getListeners(ListenerFilmeLaden.class)) {
                    Platform.runLater(() -> l.fertig(event));
                }
                Config.setStop(false);
            }
        });
    }

    // #######################################
    // Filme bei den Sendern laden
    // #######################################
    public void filmeBeimSenderSuchen() {
        // Filme bei allen Sender suchen
        if (!istAmLaufen) {
            // nicht doppelt starten
            istAmLaufen = true;
            msFilmeSuchen.filmeBeimSenderLaden(MtGuiData.mtFilmList.getFilmList());
        }
    }

    public void updateSender(String[] sender) {
        // Filme nur bei EINEM Sender suchen (nur update)
        if (!istAmLaufen) {
            // nicht doppelt starten
            istAmLaufen = true;
            msFilmeSuchen.updateSender(sender, MtGuiData.mtFilmList.getFilmList());
        }
    }

    // #######################################
    // #######################################
    public static String[] getSenderNamen() {
        return FilmeSuchen.getNamenSender();
    }

    // ###########################
    // Listener
    // ###########################
    public void addAdListener(ListenerFilmeLaden listener) {
        listeners.add(ListenerFilmeLaden.class, listener);
    }

}

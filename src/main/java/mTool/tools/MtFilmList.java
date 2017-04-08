/*
 * MediathekView
 * Copyright (C) 2017 W. Xaver
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
package mTool.tools;

import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.filmlisten.FilmlisteLesen;
import javafx.application.Platform;

public class MtFilmList {

    public static void loadFilmList(String url) {
        ListeFilme lf = new ListeFilme();
        new FilmlisteLesen().readFilmListe(url, lf, 0 /*all days*/);
        loadFilmList(lf);
    }

    public static void loadFilmList(ListeFilme lf) {
        Platform.runLater(() -> {
            // wegen Label size
            MtGuiData.listeFilme.clear();
            MtGuiData.listeFilme.addAll(lf);
        });
    }

    public static ListeFilme getFilmList() {
        ListeFilme lf = new ListeFilme();
        lf.addAll(MtGuiData.listeFilme);
        return lf;
    }

    public static void writeFilmList(String url) {
        new FilmlisteLesen().readFilmListe(url, getFilmList(), 0 /*all days*/);
    }

}

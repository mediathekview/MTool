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
package mtool.tools;

import de.mediathekview.mlib.Const;
import de.mediathekview.mlib.daten.DatenFilm;
import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.filmlisten.FilmlisteLesen;
import de.mediathekview.mlib.filmlisten.WriteFilmlistJson;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class MtFilmList extends SimpleListProperty<FilmData> {

    public MtFilmList() {
        super(FXCollections.observableArrayList());
    }

    public void loadFilmList(String url) {
        ListeFilme lf = new ListeFilme();
        new FilmlisteLesen().readFilmListe(url, lf, 0 /*all days*/);
        lf.stream().forEach(f -> addFilm(f));
    }

    public void loadFilmList(ListeFilme lf) {
        Platform.runLater(() -> {
            // wegen Label size
            clear();
            lf.stream().forEach(f -> addFilm(f));
        });
    }

    public ListeFilme getFilmList() {
        ListeFilme lf = new ListeFilme();
        this.stream().forEach(f -> lf.add(f.getFilm()));
        return lf;
    }

    public void writeFilmList(String url) {
        if (url.endsWith(Const.FORMAT_XZ)) {
            new WriteFilmlistJson().filmlisteSchreibenJsonCompressed(url, getFilmList());
        } else {
            new WriteFilmlistJson().filmlisteSchreibenJson(url, getFilmList());
        }
    }

    private void addFilm(DatenFilm film) {
        FilmData fd = new FilmData(film);
        add(fd);
    }

//    public static ObservableList<FilmData> getObservableList(ListeFilme listeFilme) {
//        ArrayList<FilmData> liste = new ArrayList<>();
//        for (DatenFilm d : listeFilme) {
//            FilmData fd = new FilmData(d);
//            liste.add(fd);
//        }
//        return FXCollections.observableList(liste);
//    }
//
//    public static ObservableList<FilmData> getObservableList(SimpleListProperty<DatenFilm> listeFilme) {
//        ArrayList<FilmData> liste = new ArrayList<>();
//        for (DatenFilm d : listeFilme) {
//            FilmData fd = new FilmData(d);
//            liste.add(fd);
//        }
//        return FXCollections.observableList(liste);
//    }
}

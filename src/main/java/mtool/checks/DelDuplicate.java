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
package mtool.checks;

import de.mediathekview.mlib.daten.DatenFilm;
import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.filmlisten.FilmlisteLesen;
import de.mediathekview.mlib.filmlisten.WriteFilmlistJson;
import mtool.tools.FilmData;

import java.util.HashSet;
import java.util.List;

public class DelDuplicate {

    int before = 0, after = 0;
    boolean simulate = true;
    String url;
    ListeFilme filmList = new ListeFilme();
    HashSet<String> hash = new HashSet<>();

    private void start() {
        new FilmlisteLesen().readFilmListe(url, filmList, 0 /*all days*/);
        before = filmList.size();

        System.out.println("");
        System.out.println("");
        System.out.println("---------------------");
        System.out.println("vorher:    " + before);
    }

    private void end() {
        hash.clear();
        after = filmList.size();
        // ==========================================
        System.out.println("danach:    " + after);
        System.out.println("Differenz: " + (before - after));
        System.out.println("---------------------");

        if (!simulate) {
            new WriteFilmlistJson().filmlisteSchreibenJson(url, filmList);
        }

    }

    private void del(boolean senderUrl, List<FilmData> list) {
        list.removeIf(film -> {
            if (!hash.contains(getHash(senderUrl, film.getFilm()))) {
                hash.add(getHash(senderUrl, film.getFilm()));
                return false;
            } else {
                return true;
            }
        });
        hash.clear();
    }

    private void delDf(boolean senderUrl, List<DatenFilm> list) {
        list.removeIf(film -> {
            if (!hash.contains(getHash(senderUrl, film))) {
                hash.add(getHash(senderUrl, film));
                return false;
            } else {
                return true;
            }
        });
        hash.clear();
    }

    public int delSenderUrl(String url, boolean simulate) {
        this.simulate = simulate;
        this.url = url;
        start();
        delDf(true, filmList);
        end();
        return before - after;
    }

    public void delSenderUrl(List<FilmData> list) {
        del(true, list);
    }

    public int delUrl(String url, boolean simulate) {
        this.simulate = simulate;
        this.url = url;
        start();
        delDf(false, filmList);
        end();
        return before - after;
    }

    public void delUrl(List<FilmData> list) {
        del(false, list);
    }

    private String getHash(boolean su, DatenFilm film) {
        if (su) {
            return film.arr[DatenFilm.FILM_SENDER] + film.arr[DatenFilm.FILM_URL];
        } else {
            return film.arr[DatenFilm.FILM_URL];
        }
    }
}

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
package mTool.checks;

import de.mediathekview.mlib.daten.DatenFilm;
import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.filmlisten.FilmlisteLesen;
import de.mediathekview.mlib.filmlisten.WriteFilmlistJson;
import java.util.HashSet;

public class ChangeArteDeFr {

    boolean simulate = true;
    String url;
    ListeFilme filmList = new ListeFilme();
    HashSet<String> hash = new HashSet<>();

    private void start() {
        new FilmlisteLesen().readFilmListe(url, filmList, 0 /*all days*/);

        System.out.println("");
        System.out.println("");
        System.out.println("---------------------");
        
        int[] before = getArteCount();
        System.out.println("vorher:    ");
        System.out.println("ARTE.DE:   " + before[0]);
        System.out.println("ARTE.FR:   " + before[1]);
    }

    private void end() {
        int[] before = getArteCount();
        System.out.println("danach:    ");
        System.out.println("ARTE.DE:   " + before[0]);
        System.out.println("ARTE.FR:   " + before[1]);
        System.out.println("---------------------");

        if (!simulate) {
            new WriteFilmlistJson().filmlisteSchreibenJson(url, filmList);
        }
    }
    
    private int[] getArteCount() {
        int[] counts = new int[2];
        counts[0] = 0;
        counts[1] = 0;
        
        filmList.forEach(film -> {
            switch (film.arr[DatenFilm.FILM_SENDER]) {
                case "ARTE.DE":
                    counts[0]++;
                    break;
                case "ARTE.FR":
                    counts[1]++;
                    break;
            }            
        });
        
        return counts;
    }
    
    public void change(String url, boolean simulate) {
        this.simulate = simulate;
        this.url = url;
        start();
        run();
        end();
    }

    private void run() {
        filmList.forEach(film -> {
            switch (film.arr[DatenFilm.FILM_SENDER]) {
                case "ARTE.DE":
                case "ARTE.FR":
                    String languageCode = getWebsiteLanguageCode(film);
                    switch (languageCode.toLowerCase()) {
                        case "de":
                            film.arr[DatenFilm.FILM_SENDER] = "ARTE.DE";
                            break;
                        case "fr":
                            film.arr[DatenFilm.FILM_SENDER] = "ARTE.FR";
                            break;
                    }
                    break;
            }
        });
    }
    
    private String getWebsiteLanguageCode(DatenFilm film) {
        String website = film.arr[DatenFilm.FILM_WEBSEITE];
        if(website.isEmpty()) {
            return "";
        }
        return website.replaceFirst("https://www.arte.tv/", "")
                .replaceFirst("http://www.arte.tv/", "")
                .substring(0,2);
    }
}

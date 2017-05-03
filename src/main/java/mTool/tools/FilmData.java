/*
 * MediathekView
 * Copyright (C) 2014 W. Xaver
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

import de.mediathekview.mlib.daten.DatenFilm;

public class FilmData {

    private final DatenFilm film;

    public FilmData(DatenFilm film) {
        this.film = film;
    }

    public int getNumber() {
        return film.nr;
    }

    public void setNumber(int number) {
        film.nr = number;
    }

    public String getSender() {
        return film.arr[DatenFilm.FILM_SENDER];
    }

    public void setSender(String fName) {
        film.arr[DatenFilm.FILM_SENDER] = fName;
    }

    public String getThema() {
        return film.arr[DatenFilm.FILM_THEMA];
    }

    public void setThema(String fName) {
        film.arr[DatenFilm.FILM_THEMA] = fName;
    }

    public String getTitle() {
        return film.arr[DatenFilm.FILM_TITEL];
    }

    public void setTitle(String fName) {
        film.arr[DatenFilm.FILM_TITEL] = fName;
    }

    public String getUrl() {
        return film.arr[DatenFilm.FILM_URL];
    }

    public void setUrl(String fName) {
        film.arr[DatenFilm.FILM_URL] = fName;
    }

    public DatenFilm getFilm() {
        return film;
    }

}

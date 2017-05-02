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
import javafx.beans.property.SimpleStringProperty;

public class FilmData {

    private final SimpleStringProperty sender;
    private final SimpleStringProperty thema;
    private final SimpleStringProperty title;
    private final SimpleStringProperty url;
    private final DatenFilm film;

    public FilmData(DatenFilm film) {
        this.sender = new SimpleStringProperty(film.arr[DatenFilm.FILM_SENDER]);
        this.thema = new SimpleStringProperty(film.arr[DatenFilm.FILM_THEMA]);
        this.title = new SimpleStringProperty(film.arr[DatenFilm.FILM_TITEL]);
        this.url = new SimpleStringProperty(film.arr[DatenFilm.FILM_URL]);
        this.film = film;
    }

    public String getSender() {
        return film.arr[DatenFilm.FILM_SENDER];
    }

    public void setSender(String fName) {
        sender.set(fName);
        film.arr[DatenFilm.FILM_SENDER] = fName;
    }

    public String getThema() {
        return film.arr[DatenFilm.FILM_THEMA];
    }

    public void setThema(String fName) {
        thema.set(fName);
        film.arr[DatenFilm.FILM_THEMA] = fName;
    }

    public String getTitle() {
        return film.arr[DatenFilm.FILM_TITEL];
    }

    public void setTitle(String fName) {
        title.set(fName);
        film.arr[DatenFilm.FILM_TITEL] = fName;
    }

    public String getUrl() {
        return film.arr[DatenFilm.FILM_URL];
    }

    public void setUrl(String fName) {
        url.set(fName);
        film.arr[DatenFilm.FILM_URL] = fName;
    }

    public DatenFilm getFilm() {
        return film;
    }

}

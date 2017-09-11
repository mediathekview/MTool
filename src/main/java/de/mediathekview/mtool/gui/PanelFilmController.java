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
package de.mediathekview.mtool.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.mediathekview.mtool.tools.FilmData;
import de.mediathekview.mtool.tools.MtFilmList;
import de.mediathekview.mtool.tools.MtGuiData;
import de.mediathekview.mtool.tools.MtSearchFilms;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PanelFilmController implements Initializable {

    private final MtFilmList filteredList = new MtFilmList();

    @FXML
    TableView<FilmData> table;
    @FXML
    ComboBox<String> cbxSender;
    @FXML
    TextField txtThema;
    @FXML
    TextField txtTitle;
    @FXML
    Button btnDelFilter;
    @FXML
    Label lblSum;
    @FXML
    Button btnDelSel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnDelFilter.setOnAction(a -> delFilter());
        btnDelSel.setOnAction(a -> delSelFilms());
        ObservableList<String> obsSender = FXCollections.observableArrayList();
        obsSender.add("");
        obsSender.addAll(MtSearchFilms.getSenderNamen());
        cbxSender.setItems(obsSender);
        cbxSender.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filter();
            }
        });

        txtThema.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filter();
            }
        });
        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filter();
            }
        });

        table.getColumns().clear();
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<FilmData, String> senderColumn = new TableColumn<>("Sender");
        TableColumn<FilmData, String> nrColumn = new TableColumn<>("Nr");
        TableColumn<FilmData, String> themaColumn = new TableColumn<>("Thema");
        TableColumn<FilmData, String> titleColumn = new TableColumn<>("Titel");
        TableColumn<FilmData, String> urlColumn = new TableColumn<>("URL");
        TableColumn actionDel = new TableColumn("");
        actionDel.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        TableColumn actionDown = new TableColumn("");
        actionDown.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<FilmData, String>, TableCell<FilmData, String>> cellFactory
                = (final TableColumn<FilmData, String> param) -> {
                    final TableCell<FilmData, String> cell = new TableCell<FilmData, String>() {

                final Button btn = new Button("Delete");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction((ActionEvent event) -> {
                            FilmData filmDate = getTableView().getItems().get(getIndex());
                            MtGuiData.mtFilmList.remove(filmDate);
                            filteredList.remove(filmDate);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    return cell;
                };

        actionDel.setCellFactory(cellFactory);
        actionDel.getStyleClass().add("center");

        nrColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        nrColumn.setPrefWidth(100);
        nrColumn.getStyleClass().add("center");

        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        senderColumn.setPrefWidth(100);
        senderColumn.getStyleClass().add("center");

        themaColumn.setCellValueFactory(new PropertyValueFactory<>("thema"));
        themaColumn.setPrefWidth(200);

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setPrefWidth(300);

        table.getColumns().addAll(actionDel, nrColumn, senderColumn, themaColumn, titleColumn, urlColumn);
        table.itemsProperty().addListener((observable, oldItems, newItems)
                -> lblSum.textProperty().bind(Bindings.size(newItems).asString("Anzahl Filme: %s")));
        loadList();
    }

    private void loadList() {
        table.setItems(MtGuiData.mtFilmList);
    }

    private void filter() {
        filteredList.clear();
        String sender = cbxSender.getSelectionModel().getSelectedItem() == null ? "" : cbxSender.getSelectionModel().getSelectedItem();
        String thema = txtThema.getText().toLowerCase();
        String title = txtTitle.getText().toLowerCase();

        if (sender.isEmpty() && thema.isEmpty() && title.isEmpty()) {
            loadList();
            return;
        }

        MtGuiData.mtFilmList.stream().forEach(film -> {
            if ((sender.isEmpty() || sender.equals(film.getSender()))
                    && (thema.isEmpty() || film.getThema().toLowerCase().contains(thema))
                    && (title.isEmpty() || film.getTitle().toLowerCase().contains(title))) {
                filteredList.add(film);
            }
        });
        table.setItems(filteredList);
    }

    private void delFilter() {
        cbxSender.getSelectionModel().selectFirst();
        txtThema.clear();
        txtTitle.clear();
    }

    private void delSelFilms() {
        ArrayList<FilmData> selection = new ArrayList<>();
        table.getSelectionModel().getSelectedItems().stream().forEach(film -> selection.add(film));

        selection.stream().forEach(film -> {
            filteredList.remove(film);
            MtGuiData.mtFilmList.remove(film);
        });
    }
}

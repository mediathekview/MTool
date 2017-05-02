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
package mTool.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import mTool.tools.FilmData;
import mTool.tools.MtGuiData;

public class PanelFilmController implements Initializable {
    
    @FXML
    TableView<FilmData> table;
    @FXML
    Button btnLoad;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnLoad.setOnAction(a -> loadList());
        
        table.getColumns().clear();
        table.setEditable(false);
        TableColumn<FilmData, String> senderColumn = new TableColumn<>("Sender");
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
//                                    filmDate.play();
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    return cell;
                };
        
        actionDel.setCellFactory(cellFactory);
        
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        themaColumn.setCellValueFactory(new PropertyValueFactory<>("thema"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        
        table.getColumns().addAll(senderColumn, themaColumn, titleColumn, urlColumn, actionDel);
    }
    
    private void loadList() {
//        final ListProperty<FilmData> workers = new SimpleListProperty<>(FXCollections.observableArrayList());
//        final ObservableList<FilmData> ol = FilmList.getObservableList(MtGuiData.listeFilme);
//        Bindings.bindContent(ol, workers);
        
        table.setItems(MtGuiData.mtFilmList);
    }
    
}

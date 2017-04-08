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
package mTool.gui;

import mTool.tools.MtGuiData;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import mTool.tools.MtFilmList;

public class MtGuiController implements Initializable {

    @FXML
    private TextField txtFilmList;
    @FXML
    public Label lblSum;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSelect;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnLoad;
    @FXML
    TabPane tPane;

    private AnchorPane search;
    private AnchorPane del;
    private AnchorPane tool;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MtGuiData.mlibGuiController = this;
        initPanelGui();
    }

    private void initPanelGui() {
        if (MtGuiData.pathFilmlist.isEmpty()) {
            txtFilmList.setText(System.getProperty("user.home") + File.separator + ".mediathek3" + File.separator + "filme.json");
        } else {
            txtFilmList.setText(MtGuiData.pathFilmlist);
        }

        MtFilmList.loadFilmList(txtFilmList.getText());
        lblSum.textProperty().bind(MtGuiData.listeFilme.sizeProperty().asString());

        btnDelete.setOnAction(e -> MtGuiData.listeFilme.clear());
        btnSave.setOnAction(e -> MtFilmList.writeFilmList(txtFilmList.getText()));
        btnSelect.setOnAction(e -> getPath());
        btnLoad.setOnAction(e -> MtFilmList.loadFilmList(txtFilmList.getText()));

        try {
            FXMLLoader fXMLLoader;

            PanelSearchController ps = new PanelSearchController();
            fXMLLoader = new FXMLLoader(getClass().getResource("/mTool/gui/PanelSearch.fxml"));
            fXMLLoader.setController(ps);
            search = (AnchorPane) fXMLLoader.load();
            tPane.getTabs().add(new Tab("Suchen", search));

            PanelDelController pd = new PanelDelController();
            fXMLLoader = new FXMLLoader(getClass().getResource("/mTool/gui/PanelDel.fxml"));
            fXMLLoader.setController(pd);
            del = (AnchorPane) fXMLLoader.load();
            tPane.getTabs().add(new Tab("Löschen", del));

            PanelToolController pt = new PanelToolController();
            fXMLLoader = new FXMLLoader(getClass().getResource("/mTool/gui/PanelTool.fxml"));
            fXMLLoader.setController(pt);
            tool = (AnchorPane) fXMLLoader.load();
            tPane.getTabs().add(new Tab("Tools", tool));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPath() {
        FileChooser chooser = new FileChooser();
        if (!txtFilmList.getText().equals("")) {
            chooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
        }
        File f = chooser.showOpenDialog(null);
        if (f != null) {
            try {
                txtFilmList.setText(f.getAbsolutePath());
                MtFilmList.loadFilmList(txtFilmList.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

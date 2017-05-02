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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mTool.checks.DelDuplicate;
import mTool.tools.MtGuiData;

public class PanelToolController implements Initializable {

    @FXML
    Button btnDelSenderUrl;
    @FXML
    Button btnDelUrl;
    @FXML
    TextField txtDelSenderUrl;
    @FXML
    TextField txtDelUrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPanelTool();
    }

    private void initPanelTool() {
        btnDelSenderUrl.setOnAction(e -> {
            int size = MtGuiData.mtFilmList.size();
            new DelDuplicate().delSenderUrl(MtGuiData.mtFilmList);
            txtDelSenderUrl.setText(size - MtGuiData.mtFilmList.size() + "");
        });

        btnDelUrl.setOnAction(e -> {
            int size = MtGuiData.mtFilmList.size();
            new DelDuplicate().delUrl(MtGuiData.mtFilmList);
            txtDelUrl.setText(size - MtGuiData.mtFilmList.size() + "");
        });

    }

}

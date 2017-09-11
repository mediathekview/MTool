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
package de.mediathekview.mtool.gui;

import java.net.URL;
import java.util.ResourceBundle;

import de.mediathekview.mlib.Config;
import de.mediathekview.mlib.filmesuchen.ListenerFilmeLaden;
import de.mediathekview.mlib.filmesuchen.ListenerFilmeLadenEvent;
import de.mediathekview.mtool.tools.MtSearchFilms;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import mServer.crawler.CrawlerConfig;

public class PanelSearchController implements Initializable {

    @FXML
    private Label lblSum;
    @FXML
    private Label lblProgress;
    @FXML
    private Label lblPercent;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnAllSender;
    @FXML
    private ProgressBar pBar;
    @FXML
    private GridPane pSender;
    @FXML
    private RadioButton rbShort;
    @FXML
    private RadioButton rbLong;
    @FXML
    private RadioButton rbMax;
    @FXML
    private CheckBox cbDebug;
    @FXML
    private CheckBox cbUpdate;

    private Button[] buttonSender;
    private String[] senderArray;
    private MtSearchFilms mtSearchFilms;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPanelSearch();
    }

    private void initPanelSearch() {
        lblSum.setText("");
        btnStop.setOnAction(e -> {
            RotateTransition tr = new RotateTransition();
            tr.setNode(btnStop);
            tr.setDuration(Duration.millis(750));
            tr.setFromAngle(0);
            tr.setToAngle(360);
            tr.setAutoReverse(true);
            tr.setCycleCount(1);
            tr.play();

            Config.setStop(true);
        });
        mtSearchFilms = new MtSearchFilms();

        rbShort.setSelected(true);
        CrawlerConfig.senderLoadHow = CrawlerConfig.LOAD_SHORT;

        rbShort.setOnAction(e -> CrawlerConfig.senderLoadHow = CrawlerConfig.LOAD_SHORT);
        rbLong.setOnAction(e -> CrawlerConfig.senderLoadHow = CrawlerConfig.LOAD_LONG);
        rbMax.setOnAction(e -> CrawlerConfig.senderLoadHow = CrawlerConfig.LOAD_MAX);

        Config.debug = cbDebug.isSelected();
        cbDebug.setOnAction(e -> Config.debug = cbDebug.isSelected());

        CrawlerConfig.updateFilmliste = false;
        cbUpdate.setSelected(false);
        cbUpdate.setOnAction(e -> CrawlerConfig.updateFilmliste = cbUpdate.isSelected());

        btnAllSender.setOnAction(e -> new Thread(() -> {
            disableButton(true);
            mtSearchFilms.filmeBeimSenderSuchen();
        }).start());

        addSender();

        mtSearchFilms.addAdListener(new ListenerFilmeLaden() {
            @Override
            public void progress(ListenerFilmeLadenEvent event) {
                Platform.runLater(() -> {
                    if (event.max == 0) {
                        pBar.setProgress(0);
                        lblPercent.setText("");
                    } else if (event.progress == 0) {
                        pBar.setProgress(0);
                        lblPercent.setText("0%");
                    } else {
                        double prog = 1.0 * event.progress / event.max;
                        if (prog < 0) {
                            prog = 0;
                        } else if (prog > 1) {
                            prog = 0.99;
                        }
                        pBar.setProgress(prog);
                        int i = (int) (100 * prog);
                        lblPercent.setText(i + "%");
                    }
                    lblProgress.setText(textLaenge(80, event.text, true /* mitte */, false /*addVorne*/));
                    lblSum.setText(event.count + "");
                });
            }

            @Override
            public void fertig(ListenerFilmeLadenEvent event) {
                Platform.runLater(() -> {
                    pBar.setProgress(0);
                    lblPercent.setText("");
                    disableButton(false);
                });
            }
        });
    }

    private void addSender() {
        senderArray = MtSearchFilms.getSenderNamen();
        buttonSender = new Button[senderArray.length];
        for (int ii = 0; ii < MtSearchFilms.getSenderNamen().length; ++ii) {
            buttonSender[ii] = new Button(senderArray[ii]);
            buttonSender[ii].setOnAction(new ActionLoadSender(senderArray[ii]));
        }

        pSender.setHgap(10);
        pSender.setVgap(10);
        pSender.setPadding(new Insets(10));
        int zeile = 0, spalte = 0, count = 0;
        for (String aSender : senderArray) {
            Button btn = buttonSender[count];
            btn.setText(aSender);
            btn.setMaxWidth(Double.MAX_VALUE);
            pSender.add(btn, spalte, zeile);

            ++spalte;
            if (spalte >= 5) {
                ++zeile;
                spalte = 0;
            }
            ++count;
        }

    }

    private static String textLaenge(int max, String text, boolean mitte, boolean addVorne) {
        if (text.length() > max) {
            if (mitte) {
                text = text.substring(0, 25) + " .... " + text.substring(text.length() - (max - 31));
            } else {
                text = text.substring(0, max - 1);
            }
        }
        while (text.length() < max) {
            if (addVorne) {
                text = " " + text;
            } else {
                text = text + " ";
            }
        }
        return text;
    }

    private void disableButton(boolean disable) {
        for (Button aButtonSender : buttonSender) {
            aButtonSender.setDisable(disable);
        }
        btnAllSender.setDisable(disable);
    }

    private class ActionLoadSender implements EventHandler<ActionEvent> {

        private final String sender;

        public ActionLoadSender(String ssender) {
            sender = ssender;
        }

        @Override
        public void handle(ActionEvent t) {
            lblProgress.setText("");
            lblSum.setText("");
            disableButton(true);
            mtSearchFilms.updateSender(new String[]{sender} /* senderAllesLaden */);
        }
    }

}

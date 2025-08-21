package com.utils.app;

import java.awt.*;
import com.utils.app.Buttons.AppButton;
import com.utils.files.SizeComparator;
import logs.AppLogger;

public class Scenes {
    public static class AppProcessPanel extends Panel {
        private static final AppLogger logger = AppLogger.getLogger(AppProcessPanel.class);
        private TextField srcField;
        private TextField tgtField;
        private Label statusLabel;

        public AppProcessPanel(String sceneTitle, Runnable onBack, java.util.function.BiConsumer<String, String> onRun, boolean isCompression) {
            super(new BorderLayout(10, 10));

            Label titleLabel = new Label(sceneTitle, Label.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(titleLabel, BorderLayout.NORTH);

            Panel form = new Panel(new GridLayout(2, 1, 10, 10));
            Panel srcRow = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            Label srcLabel = new Label("Source file");
            srcField = new TextField(20);
            srcField.setEditable(false);

            AppButton btnSrcBrowse = new AppButton("...", e -> {
                FileDialog fd = new FileDialog((Frame) null, "Select file: ", FileDialog.LOAD);
                fd.setVisible(true);
                if (fd.getFile() != null) {
                    String srcPath = fd.getDirectory() + fd.getFile();
                    if (isCompression && srcPath.toLowerCase().endsWith(".huf")) {
                        statusLabel.setText("Error! File with .huf-extension cannot be compressed!");
                        logger.severe("Error! File with .huf-extension cannot be compressed!");
                        return;
                    } else if (!isCompression && !srcPath.toLowerCase().endsWith(".huf")) {
                        statusLabel.setText("Error! File without .huf-extension cannot be decompressed!");
                        logger.severe("Warning! File without .huf-extension cannot be decompressed!");
                        return;
                    }
                    srcField.setText(srcPath);
                    tgtField.setText(isCompression ? srcPath + ".huf" : srcPath.replaceAll("\\.huf$", ""));
                    statusLabel.setText("");
                }
            }) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(20, 20); // to square shape
                }
            };

            srcRow.add(srcLabel);
            srcRow.add(srcField);
            srcRow.add(btnSrcBrowse);

            Panel tgtRow = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            Label tgtLabel = new Label("Target file");
            tgtField = new TextField(20);
            tgtField.setEditable(false);
            tgtRow.add(tgtLabel);
            tgtRow.add(tgtField);

            form.add(srcRow);
            form.add(tgtRow);
            add(form, BorderLayout.CENTER);

            statusLabel = new Label("", Label.CENTER);

            AppButton btnRun = new AppButton("Run", e -> {
                String src = srcField.getText().trim();
                String tgt = tgtField.getText().trim();
                if (!src.isEmpty() && !tgt.isEmpty()) {
                    try {
                        onRun.accept(src, tgt);
                        statusLabel.setText("Completed! " + SizeComparator.getRatio(isCompression, src, tgt));
                        logger.info("Success!");
                    } catch (Exception ex) {
                        logger.severe("Error, unable to process file!");
                        statusLabel.setText("Error, unable to process file!");
                    }
                }
            });

            AppButton btnBack = new AppButton("Back", e -> onBack.run());

            Panel bottomButtons = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 5));
            bottomButtons.add(btnRun);
            bottomButtons.add(btnBack);

            Panel southPanel = new Panel(new BorderLayout());
            southPanel.add(statusLabel, BorderLayout.NORTH);
            southPanel.add(bottomButtons, BorderLayout.SOUTH);
            add(southPanel, BorderLayout.SOUTH);
        }
    }
}

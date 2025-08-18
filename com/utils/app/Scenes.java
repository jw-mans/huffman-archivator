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
            Panel form = new Panel(new GridLayout(3, 2, 10, 10));
            srcField = new TextField(20);
            tgtField = new TextField(20);
            statusLabel = new Label("", Label.CENTER);
            
            AppButton btnRun = new AppButton("Run", e -> {
                String src = srcField.getText().trim();
                String tgt = tgtField.getText().trim();
                if(!src.isEmpty() && !tgt.isEmpty()) {
                    try { 
                        onRun.accept(src, tgt); 
                        statusLabel.setText("Completed! Compression ratio: " + SizeComparator.getRatio(isCompression, src, tgt));
                    }
                    catch(Exception ex) { 
                        logger.info("Error, unable to process file!"); 
                        statusLabel.setText("ERROR!");
                    }
                }
            });
            AppButton btnBack = new AppButton("Back", e -> onBack.run());

            form.add(new Label("Source file"));
            form.add(srcField);
            form.add(new Label("Target file"));
            form.add(tgtField);
            form.add(btnRun);
            form.add(btnBack);

            add(new Label(sceneTitle, Label.CENTER), BorderLayout.NORTH);
            add(form, BorderLayout.CENTER);
            add(statusLabel, BorderLayout.SOUTH);
        }
    }
}

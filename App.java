import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.src.Coder;
import com.src.encoder.Encoder;
import com.src.decoder.Decoder;
import com.utils.app.Buttons.*;
import com.utils.app.Scenes.AppProcessPanel;

public class App {
    public static void main(String[] args) {
        Frame appFrame = new Frame("Optimal Compressor (by jw-mans)");
        appFrame.setSize(500, 300);
        appFrame.setLayout(new FlowLayout());

        CardLayout cl = new CardLayout();
        Panel root = new Panel(cl);

        // -------- Main Scene --------

        Panel mainScene = new Panel(new BorderLayout(0, 20));
        
        // title 
        Label title = new Label("Optimal Compressor", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainScene.add(title, BorderLayout.NORTH);

        Panel btnPanel = new Panel(new GridLayout(3, 1, 0, 10));
        AppButton btnCompress = new AppButton("Compress file", e -> {
            cl.show(root, "compressScene");
        });
        AppButton btnDecompress = new AppButton("Decompress file", e -> {
            cl.show(root, "decompressScene");
        });
        AppButton btnExit = new AppButton("Exit", e -> {
            appFrame.dispose();
            System.exit(0);
        });

        btnPanel.add(btnCompress);
        btnPanel.add(btnDecompress);
        btnPanel.add(btnExit);

        Panel flowWrapper = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowWrapper.add(btnPanel);
        mainScene.add(flowWrapper, BorderLayout.CENTER);

        // ------ Compress Scene ------
        AppProcessPanel compressScene = new AppProcessPanel(
            "Compress", 
            () -> cl.show(root, "mainScene"), 
            (src, tgt) -> {
                Coder encoder = new Encoder();
                encoder.code(src, tgt);
            },
            true
        );

        // ----- Decompress Scene -----
        
        AppProcessPanel decompressScene = new AppProcessPanel(
            "Decompression",
            () -> cl.show(root, "mainScene"),
            (src, tgt) -> {
                Coder decoder = new Decoder();
                decoder.code(src, tgt);
            },
            false
        );

        // ----------------------------
        root.add(mainScene, "mainScene");
        root.add(compressScene, "compressScene");
        root.add(decompressScene, "decompressScene");

        appFrame.add(root);
        cl.show(root, "mainScene");
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                appFrame.dispose();
                System.exit(0);
            } 
        });

        appFrame.setVisible(true);
    }
}
package com.utils.app;
import java.awt.*;
import java.awt.event.*;

public class Buttons {
    public static class AppButton extends Button {
        public AppButton(String label, ActionListener actionListener) {
            super(label);
            addActionListener(actionListener);
        }
    }
}
package com.navi7468.ui;

import com.navi7468.graphics.BitmapFont;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class UIHandler {
    private BitmapFont bitmapFont;
    private List<String> elements;
    private boolean visible;
    private float lineSpacing;

    public UIHandler(BitmapFont bitmapFont, float lineSpacing) {
        this.bitmapFont = bitmapFont;
        this.elements = new ArrayList<>();
        this.visible = true;
        this.lineSpacing = lineSpacing;
    }

    public void addElement(String text) {
        elements.add(text);
    }

    public void updateElement(int index, String text) {
        if (index >= 0 && index < elements.size()) {
            elements.set(index, text);
        }
    }

    public void removeElement(String text) {
        elements.remove(text);
    }

    public void clearElements() {
        elements.clear();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(float startX, float startY, float scale) {
        if (!visible) return;

        float y = startY;
        for (String element : elements) {
            bitmapFont.renderText(element, startX, y, scale);
            y += lineSpacing * scale;
        }
    }
}

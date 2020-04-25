package com.shanebeestudios.mcdeop.util;

@SuppressWarnings("unused")
public class Format extends java.awt.Color {

    // COLORS
    public static final Format WHITE = new Format(255, 255, 255);
    public static final Format LIGHT_GRAY = new Format(192, 192, 192);
    public static final Format GRAY = new Format(128, 128, 128);
    public static final Format DARK_GRAY = new Format(64, 64, 64);
    public static final Format BLACK = new Format(0, 0, 0);
    public static final Format RED = new Format(255, 0, 0);
    public static final Format PINK = new Format(255, 175, 175);
    public static final Format ORANGE = new Format(255, 200, 0);
    public static final Format YELLOW = new Format(255, 255, 0);
    public static final Format GREEN = new Format(0, 255, 0);
    public static final Format MAGENTA = new Format(255, 0, 255);
    public static final Format CYAN = new Format(0, 255, 255);
    public static final Format BLUE = new Format(0, 0, 255);

    // FORMAT
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001b[1m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String INVERSE = "\u001b[7m";

    private Format(java.awt.Color c) {
        super(c.getRGB());
    }

    public Format(int r, int g, int b) {
        super(r, g, b);
    }

    public Format(int rgb) {
        super(rgb);
    }

    public Format(String hex) {
        super(java.awt.Color.decode(hex).getRGB());
    }

    @Override
    public String toString() {
        return "\u001B[38;2;" + getRed() + ";" + getGreen() + ";" + getBlue() + "m";
    }

}

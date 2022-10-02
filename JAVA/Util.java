package JAVA;

import java.awt.image.BufferedImage;

public class Util {
    private String PrefixText = "010101010101011001010101011010010010100101011010";

    public String getPrefixText() {
        return PrefixText;
    }

    public int bitToPixel(String bits, int rgb) {
        int[] bitValues = new int[4];
        bitValues[0] = Integer.parseInt(bits.substring(0, 1));
        bitValues[1] = Integer.parseInt(bits.substring(1, 2));
        bitValues[2] = Integer.parseInt(bits.substring(2, 3));
        bitValues[3] = Integer.parseInt(bits.substring(3, 4));

        int a = (rgb >>> 25) * 2 + bitValues[0];
        int r = ((rgb >>> 17) * 2 + bitValues[1]) & 0xFF;
        int g = ((rgb >>> 9) * 2 + bitValues[2]) & 0xFF;
        int b = ((rgb >>> 1) * 2 + bitValues[3]) & 0xFF;

        rgb = (a << 24) + (r << 16) + (g << 8) + b;
        return rgb;
    }

    public int maxLength(BufferedImage image) {
        int len = 0;
        len = (image.getWidth() * image.getHeight() * 4 - getPrefixText().length()) / 8;
        String lenS = Integer.toBinaryString(len);
        if (lenS.length() % 8 == 0) {
            len -= lenS.length() / 8;
        } else {
            len -= lenS.length() / 8 + 1;
        }
        return len - 1;
    }

    public String restock(String bin, int len) {
        while (bin.length() < len) {
            bin = "0" + bin;
        }
        return bin;
    }

    public String pixelToBit(int rgb) {
        int a = (rgb >> 24) & 0x1;
        int r = (rgb >> 16) & 0x1;
        int g = (rgb >> 8) & 0x1;
        int b = rgb & 0x1;

        return String.valueOf(a) + String.valueOf(r) + String.valueOf(g) + String.valueOf(b);
    }
}

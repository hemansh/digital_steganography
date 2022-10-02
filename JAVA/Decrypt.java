package JAVA;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Decrypt extends Util{
    public String getText(String path) {
        File outimg = new File(path);
        BufferedImage img = null;
        try {
            BufferedImage bildIn = ImageIO.read(outimg);
            img = new BufferedImage(bildIn.getWidth(), bildIn.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.drawImage(bildIn, 0, 0, null);
            g.dispose();
        } catch (IOException e) {
            System.out.println(e);
        }
        int[] data = imageToArray(img);
        int pos = 0;

        if (!discoverFromTo(data, pos, getPrefixText().length() / 4).equals(getPrefixText())) {
            System.out.println("No prefix!");
            return "";
        }
        pos += getPrefixText().length() / 4;

        String lenBitfolge = discoverFromTo(data, pos, pos + getBinLenLen(img));
        int len = Integer.parseInt(lenBitfolge, 2);

        pos += getBinLenLen(img);

        String textBitfolge = discoverFromTo(data, pos, pos + len * 2);

        return binToString(textBitfolge);
    }

    public int getBinLenLen(BufferedImage img) {
        int maxLen = maxLength(img);
        String maxLenS = Integer.toBinaryString(maxLen);
        return (maxLenS.length() + (4 - maxLenS.length() % 4)) / 4;
    }

    public int[] imageToArray(BufferedImage img) {
        int[] data = new int[img.getHeight() * img.getWidth()];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                data[y * img.getWidth() + x] = img.getRGB(x, y);
            }
        }

        return data;
    }

    public String discoverFromTo(int[] data, int a, int z) {
        String result = "";
        for (int i = a; i < z; i++) {
            result += pixelToBit(data[i]);
        }
        return result;
    }

    public String binToString(String bin) {
        String characters = "";
        if (bin.length() % 8 == 0) {
            for (int i = 0; i < bin.length() / 8; i++) {
                String bits = bin.substring(i * 8, (i + 1) * 8);
                int ascii = Integer.parseInt(bits, 2);
                char c = (char) ascii;
                characters += String.valueOf(c);
            }
        }
        return characters;
    }

}

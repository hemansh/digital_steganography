package JAVA;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.*;

public class Encrypt extends Util{
    boolean encode(String imgPath, String msg) {
        if (msg.length() == 0) {
            System.out.println("No given message");
            return false;
        }
        File inpimg = new File(imgPath);
        BufferedImage img = null;
        try {
            BufferedImage bildIn = ImageIO.read(inpimg);
            img = new BufferedImage(bildIn.getWidth(), bildIn.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.drawImage(bildIn, 0, 0, null);
            g.dispose();
        } catch (IOException e) {
            System.out.println(e);
        }
        String binary = getPrefixText() + getBinLen(img, msg) + toBin(msg);
        boolean ret = hide(img, binary);
        return ret;
    }

    public String getBinLen(BufferedImage img, String text) {
        int maxLen = maxLength(img);
        String lenS = Integer.toBinaryString(text.length());
        String maxLenS = Integer.toBinaryString(maxLen);
        if (text.length() <= maxLen) {
            lenS = restock(lenS, maxLenS.length() + (4 - maxLenS.length() % 4));
        } else {
            System.out.println("Text is too long.");
        }
        return lenS;
    }

    public boolean hide(BufferedImage img, String binary) {
        int pos = 0;
        for (int y = 0; y < (binary.length() / 4) / img.getWidth() + 1; y++) {
            for (int x = 0; x < img.getWidth() && pos < binary.length(); x++) {
                int rgb = img.getRGB(x, y);
                String bits = binary.substring(pos, pos + 4);
                img.setRGB(x, y, bitToPixel(bits, rgb));
                pos += 4;
            }
        }
        File outfile = new File("JAVA/out.jpg");
        export(img, outfile);
        return true;
    }

    public String toBin(String characters) {
        String bin = "";
        for (int i = 0; i < characters.length(); i++) {
            int ascii = (int) characters.charAt(i);
            String asciiS = Integer.toBinaryString(ascii);
            while (asciiS.length() < 8) {
                asciiS = "0" + asciiS;
            }
            bin += asciiS;
        }
        return bin;
    }

    public void export(BufferedImage img, File outFile) {
        try {
            ImageIO.write(img, "png", outFile);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

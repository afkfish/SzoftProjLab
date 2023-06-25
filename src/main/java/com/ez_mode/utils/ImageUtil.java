package com.ez_mode.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ImageUtil {
  private static final String basePath = "src/main/resources/images/";

  private static String getSubPath(String name) {
    switch (name) {
      case "nomad", "plumber" -> {
        return "character/";
      }
      case "sticky", "slippery" -> {
        return "overlay/";
      }
      case "break", "movedown", "moveleft", "moveright", "moveup", "pickuppipe", "pickuppump", "repair", "setpump" -> {
        return "action/";
      }
      default -> {
        return "object/";
      }
    }
  }

  public static Image getImage(String name, int size) {
    String subPath = getSubPath(name);
    ImageIcon icon = new ImageIcon(basePath + subPath + name + ".png");
    return new ScalableImage(icon.getImage()).scale(size);
  }

  public static Image scale(Image image, int size) {
    return image.getScaledInstance(size, size, Image.SCALE_DEFAULT);
  }

  public static ScalableImage combine(Image base, Image overlay) {
    int w = Math.max(base.getWidth(null), overlay.getWidth(null));
    int h = Math.max(base.getHeight(null), overlay.getHeight(null));
    BufferedImage combined = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);

    // adds the two layers of the images
    Graphics g = combined.getGraphics();
    g.drawImage(base, 0, 0, null);
    g.drawImage(overlay, 0, 0, null);
    g.dispose();

    return new ScalableImage(combined);
  }

  public static ImageIcon getTransparent() {
    return new ImageIcon(new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB));
  }
  
  public static class ScalableImage {
    private final Image image;

    ScalableImage(Image image) {
      this.image = image;
    }

    public Image scale(int size) {
      return this.image.getScaledInstance(size, size, Image.SCALE_DEFAULT);
    }
  }
}

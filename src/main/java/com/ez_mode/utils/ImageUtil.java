package com.ez_mode.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ImageUtil {
  private static final String basePath = "src/main/resources/images/";
  private static final HashMap<String, ScalableImage> imageCache = new HashMap<>();

  public static void loadImageCache() {
    File baseDir = new File(basePath);
    File[] subDirs = baseDir.listFiles();
    assert subDirs != null;
    for (File subDir : subDirs) {
      File[] images = subDir.listFiles();
      assert images != null;
      for (File image : images) {
        String name = image.getName().split("\\.")[0];
        if (!imageCache.containsKey(name)) {
          getImage(name, 200);
        }
      }
    }
  }

  private static String getSubPath(@NotNull String name) {
    switch (name) {
      case "nomad", "plumber" -> {
        return "character/";
      }
      case "sticky", "slippery" -> {
        return "overlay/";
      }
      case "break",
          "movedown",
          "moveleft",
          "moveright",
          "moveup",
          "pickuppipe",
          "pickuppump",
          "repair",
          "setpump" -> {
        return "action/";
      }
      default -> {
        return "object/";
      }
    }
  }

  public static Image getImage(String name, int size) {
    if (name == null) return null;

    if (imageCache.containsKey(name)) return imageCache.get(name).scale(size);

    String subPath = getSubPath(name);
    ImageIcon icon = new ImageIcon(basePath + subPath + name + ".png");
    imageCache.put(name, new ScalableImage(icon.getImage()));

    return new ScalableImage(icon.getImage()).scale(size);
  }

  public static Image scale(@NotNull Image image, int size) {
    return image.getScaledInstance(size, size, Image.SCALE_DEFAULT);
  }

  public static ScalableImage combine(@NotNull Image base, @NotNull Image overlay) {
    int w = Math.max(base.getWidth(null), overlay.getWidth(null));
    int h = Math.max(base.getHeight(null), overlay.getHeight(null));
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    // adds the two layers of the images
    Graphics g = combined.getGraphics();
    g.drawImage(base, 0, 0, null);
    g.drawImage(overlay, 0, 0, null);
    g.dispose();

    return new ScalableImage(combined);
  }

  public static ScalableImage combine(String base, String overlay) {
    if (overlay == null) return imageCache.get(base);
    return combine(imageCache.get(base).getImage(), imageCache.get(overlay).getImage());
  }

  public static ScalableImage combine(Image base, String overlay) {
    if (overlay == null) return new ScalableImage(base);
    Image overlayImage = imageCache.get(overlay).getImage();
    return combine(base, overlayImage);
  }

  public static ScalableImage getTransparent() {
    return new ScalableImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
  }

  public static class ScalableImage {
    private final Image image;

    ScalableImage(Image image) {
      this.image = image;
    }

    public Image getImage() {
      return this.image;
    }

    public Image scale(int size) {
      return this.image.getScaledInstance(size, size, Image.SCALE_DEFAULT);
    }
  }
}

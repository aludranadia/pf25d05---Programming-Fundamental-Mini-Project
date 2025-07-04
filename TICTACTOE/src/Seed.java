import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {
    CROSS("X", "images/cross.gif"),
    NOUGHT("O", "images/not.gif"),
    NO_SEED(" ", null);

    private String displayName;
    private Image img = null;

    private Seed(String name, String imageFilename) {
        this.displayName = name;

        if (imageFilename != null) {
            URL imgURL = getClass().getClassLoader().getResource(imageFilename);
            ImageIcon icon = null;
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
                img = icon.getImage();
            } else {
                System.err.println("Couldn't find file " + imageFilename);
            }
        }
    }

    public String getDisplayName() {
        return displayName;
    }
    public Image getImage() {
        return img;
    }
}
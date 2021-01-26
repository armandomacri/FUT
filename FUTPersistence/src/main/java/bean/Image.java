package bean;

import java.awt.image.BufferedImage;

public class Image {
    private String imgUrl;
    private BufferedImage bufferedImage;

    public Image(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() { return this.imgUrl; }
    public BufferedImage getBufferedImage() { return this.bufferedImage; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    public void setBufferedImage(BufferedImage bufferedImage) { this.bufferedImage = bufferedImage; }

}

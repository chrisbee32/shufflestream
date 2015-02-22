package com.shufflestream.imageaction;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by miguelalvarado on 2/21/15.
 */
public class ResizeAction extends ImageAction {
    public ResizeAction(String sourceImage) { super(sourceImage);}

    public void executeAction() throws Exception {
        BufferedImage sourceImg = ImageIO.read(new File(sourceImage));
        BufferedImage destImg;

        if(sourceImg.getHeight() >= sourceImg.getWidth()){
            destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 800);
        }
        else{
            destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, 800);
        }

        String dirOut = "/Users/miguelalvarado/outtests";
        String imgOut = "/Users/miguelalvarado/outtests/pepe.jpg";

        File fileDirectory = new File(dirOut);
        File newFile = new File(imgOut);

        // Make sure that the output directory exists
        if (!fileDirectory.exists()){
            fileDirectory.mkdir();
        }

        if (newFile.exists()){
            newFile.delete();
        }

        newFile.createNewFile();

        ImageIO.write(destImg, "jpg", newFile);
    }
}

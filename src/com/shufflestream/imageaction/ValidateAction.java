package com.shufflestream.imageaction;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import org.imgscalr.Scalr;

/**
 * Created with IntelliJ IDEA.
 * User: miguelalvarado
 * Date: 2/21/15
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidateAction extends ImageAction {

    public ValidateAction(String sourceImage, String destinationImage) { super(sourceImage, destinationImage);}

    public void executeAction() throws Exception {
        BufferedImage sourceImg = ImageIO.read(new File(sourceImage));
        BufferedImage destImg = null;

        // TODO: Validation code goes here
    }
}

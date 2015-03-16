package com.shufflestream.imageaction;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriter;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: miguelalvarado
 * Date: 2/21/15
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 *
 * Class ResizeAction
 *
 * Description: This class will do the work of rezising an image based on the given dimensions
 *
 */
public class ResizeAction extends ImageAction {
    private int newWidth = 0;
    private int newHeight = 0;

    public ResizeAction(String sourceImage,
                        String destinationImage,
                        int newWidth, int newHeight) {

        super(sourceImage, destinationImage);

        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    public void executeAction() throws Exception {
        BufferedImage sourceImg = ImageIO.read(new File(sourceImage));
        BufferedImage destImg=null;

        // This is where the transform happens, this one line does it all and returns a raster that then
        // can be written as a file
        if (newWidth != 0)
            destImg = Scalr.resize(sourceImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, newWidth);

        if (newHeight != 0 )
            destImg = Scalr.resize(sourceImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, newHeight);

        // Make sure our directory is there and if destination file is there already, well... kill it!
        File fullNewFile = new File(destinationImage);
        File fileDirectory = new File(fullNewFile.getParent());

        if (!fileDirectory.exists()){
            fileDirectory.mkdir();
        }

        if (fullNewFile.exists()){
            fullNewFile.delete();
        }
        fullNewFile.createNewFile();

        // Use image writer and jpgWriteParam so we can set the highest quality possible (1f)
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(1f);

        // Let's get some streams going to flush the file
        ImageOutputStream outStream = ImageIO.createImageOutputStream(fullNewFile);
        jpgWriter.setOutput(outStream);
        IIOImage outputImage = new IIOImage(destImg, null, null);
        jpgWriter.write(null, outputImage, jpgWriteParam);
        jpgWriter.dispose();
    }
}

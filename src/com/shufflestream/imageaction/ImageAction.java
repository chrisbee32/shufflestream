package com.shufflestream.imageaction;

/**
 * Created with IntelliJ IDEA.
 * User: miguelalvarado
 * Date: 2/21/15
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 *
 * Class Image Action
 *
 * Description: This class is the based class for various image actions that can be taken against
 * uploaded images. There are various steps required for when an image is uploaded, from validation
 * to cropping, generation of various renditions (different qualities, scales), etc. This base
 * class represents *any* Image Action.
 *
 */
public abstract class ImageAction  {
    protected String sourceImage = new String();
    protected String destinationImage = new String();

    public ImageAction(String sourceImage){
        this.sourceImage = sourceImage;
    }

    public abstract void executeAction() throws Exception;

}

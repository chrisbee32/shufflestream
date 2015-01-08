package com.shufflestream.controllers;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

@Controller
public class WriteController {

    private static String bucketName = "shufflestream";
    private static String imageKeyNameFolder = "images/";
    private static String metaKeyNameFolder = "meta/";
    private static String channelKeyNameFolder = "channels/";

    // should take in form data for channel name
    @RequestMapping(value = "/createchannel", method = RequestMethod.POST)
    public String createchannel(Model model, @RequestParam("channel") String channel) throws IOException, ClassNotFoundException {

        // get existing channel list (List<String>) via the read API used in the read controller (make util?)
        List<String> channels = ShuffleUtil.getChannels();

        // add the new value to the channel list
        channels.add(channel);

        // write the channel list object to s3
        File channelFile = ShuffleUtil.createWritableChannelFile(channels);
        FileInputStream channelStream = new FileInputStream(channelFile);
        System.out.println("Chan file to be written: " + channelFile.getCanonicalPath());
        AmazonS3 s3 = ShuffleUtil.s3Conn();
        ObjectMetadata chanObjectMetadata = new ObjectMetadata();
        chanObjectMetadata.setContentLength(channelFile.length());
        String channelKeyName = channelKeyNameFolder + channelFile.getName();
        try {
            s3.putObject(new PutObjectRequest(bucketName, channelKeyName, channelStream, chanObjectMetadata));
        } catch (AmazonServiceException e) {
            System.out.println(e.getErrorMessage());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorType());
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (AmazonClientException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            channelStream.close();
        }

        return "redirect:/managechannels";
    }

    // takes in form data and writes to S3
    @RequestMapping(value = "/addcontent", method = RequestMethod.POST)
    public String addcontent(Model model, @RequestParam("file") MultipartFile file,
            @RequestParam("Title") String title, @RequestParam("Artist") String artist,
            @RequestParam("Description") String description, @RequestParam("ArtistWebsite") String artistWebsite,
            @RequestParam("Channel") String channel) throws IOException {

        String imageKeyName = imageKeyNameFolder + file.getOriginalFilename();

        // create ShuffleObject (except assetUrl)
        ShuffleObject shuffleObject = new ShuffleObject();
        shuffleObject.setAssetUrl(imageKeyName);
        shuffleObject.setArtist(artist);
        shuffleObject.setArtistWebsite(artistWebsite);
        shuffleObject.setDescription(description);
        shuffleObject.setTitle(title);
        shuffleObject.setChannel(channel);

        String metaKeyName = metaKeyNameFolder + shuffleObject.hashCode();
        System.out.println("Shuffle object: " + metaKeyName + " - " + shuffleObject.toString());

        // convert multipartfile and shuffle object to writeable Files
        File uploadedFile = ShuffleUtil.createWriteableImageFile(file);
        File uploadedMeta = ShuffleUtil.createWritableMetaFile(shuffleObject);

        // TODO: edit image file

        // read file from disk
        FileInputStream imageStream = new FileInputStream(uploadedFile);
        FileInputStream metaStream = new FileInputStream(uploadedMeta);

        // create connection, set metadata properties and write to s3
        AmazonS3 s3 = ShuffleUtil.s3Conn();
        ObjectMetadata imageObjectMetadata = new ObjectMetadata();
        imageObjectMetadata.setContentLength(uploadedFile.length());
        ObjectMetadata metaObjectMetadata = new ObjectMetadata();
        metaObjectMetadata.setContentLength(uploadedMeta.length());

        try {
            s3.putObject(new PutObjectRequest(bucketName, imageKeyName, imageStream, imageObjectMetadata));
            s3.putObject(new PutObjectRequest(bucketName, metaKeyName, metaStream, metaObjectMetadata));

        } catch (AmazonServiceException e) {
            System.out.println(e.getErrorMessage());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorType());
            System.out.println(e.getMessage());
            e.printStackTrace();
            shuffleObject.setAssetUrl("no asset");
        } catch (AmazonClientException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            imageStream.close();
        }

        model.addAttribute("uploadFileName", file.getName());
        return "redirect:/upload";
    }

}

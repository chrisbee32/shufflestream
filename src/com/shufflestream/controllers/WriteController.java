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

//TODO: fix image urls - HTML/URL escape 
//TODO: update managechannel to allchannels - make channels clickable to managechannel?channel=foobar page 
//TODO: create new managechannel?channel=foobar template that shows thumbnail images and editable metadata for each image in channel 

@Controller
public class WriteController {

    private static String assetUrlRoot = "http://s3.amazonaws.com/shufflestream/images/";

    // should take in form data for channel name
    @RequestMapping(value = "/createchannel", method = RequestMethod.POST)
    public String createchannel(Model model, @RequestParam("channel") String channel) throws IOException, ClassNotFoundException {

        // get existing channel list (List<String>) via the get method
        List<String> channels = ShuffleUtil.getChannels();

        // add the new value to the channel list
        channels.add(channel);

        // write the channel list object to s3
        ShuffleUtil.createChannel(channels);

        return "redirect:/managechannels";
    }

    // takes in form data and writes to S3
    @RequestMapping(value = "/addcontent", method = RequestMethod.POST)
    public String addcontent(Model model, @RequestParam("file") MultipartFile file,
            @RequestParam("Title") String title, @RequestParam("Artist") String artist,
            @RequestParam("Description") String description, @RequestParam("ArtistWebsite") String artistWebsite,
            @RequestParam("Channel") String channel) throws IOException {

        String assetUrl = assetUrlRoot + file.getOriginalFilename();

        // create ShuffleObject (except assetUrl)
        ShuffleObject shuffleObject = new ShuffleObject();
        shuffleObject.setAssetUrl(assetUrl);
        shuffleObject.setArtist(artist);
        shuffleObject.setArtistWebsite(artistWebsite);
        shuffleObject.setDescription(description);
        shuffleObject.setTitle(title);
        shuffleObject.setChannel(channel);

        ShuffleUtil.createContent(shuffleObject, file);

        model.addAttribute("uploadFileName", file.getName());
        return "redirect:/upload";
    }

}

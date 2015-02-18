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
import java.util.Random;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
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
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.net.URLEncoder;
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
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

@Controller
public class WriteController {

    private static String assetUrlRoot = "http://s3.amazonaws.com/shuffle2/images/";

    // should take in form data for channel name
    @RequestMapping(value = "/createchannel", method = RequestMethod.POST)
    public String createchannel(Model model, @RequestParam("channel") String channel, @RequestParam("description") String desc) throws IOException,
            ClassNotFoundException {

        // write the channel list object to s3
        ShuffleUtil.createChannelInDb(channel, desc);

        return "redirect:/createchannel";
    }

    // takes in form data and writes to S3
    @RequestMapping(value = "/addcontent", method = RequestMethod.POST)
    public String addcontent(Model model, @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> allRequestParams) throws IOException {

        String assetUrl = assetUrlRoot + URLEncoder.encode(file.getOriginalFilename(), "UTF-8");

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100000);

        // create attributes for ShuffleObject
        Map<String, String> attr = new HashMap<String, String>();
        attr.put("Geo Location", allRequestParams.get("Geo Location").toString());
        attr.put("Medium", allRequestParams.get("Medium").toString());
        attr.put("Time Period", allRequestParams.get("Time Period").toString());
        attr.put("Subject", allRequestParams.get("Subject").toString());
        attr.put("Color", allRequestParams.get("Color").toString());
        attr.put("Pallette", allRequestParams.get("Pallette").toString());
        attr.put("Light", allRequestParams.get("Light").toString());
        attr.put("Temperature", allRequestParams.get("Temperature").toString());
        attr.put("Location Type", allRequestParams.get("Location Type").toString());
        attr.put("Environment", allRequestParams.get("Environment").toString());
        attr.put("Style", allRequestParams.get("Style").toString());
        attr.put("Mood", allRequestParams.get("Mood").toString());
        attr.put("Motion", allRequestParams.get("Motion").toString());
        attr.put("Coherence", allRequestParams.get("Coherence").toString());
        attr.put("Predominant Space", allRequestParams.get("Predominant Space").toString());
        attr.put("Rhythm", allRequestParams.get("Rhythm").toString());
        attr.put("Texture", allRequestParams.get("Texture").toString());
        attr.put("Mass", allRequestParams.get("Mass").toString());
        attr.put("Time Of Day", allRequestParams.get("Time Of Day").toString());

        // create ShuffleObject
        ShuffleObject shuffleObject = new ShuffleObject();
        shuffleObject.setId(randomInt);
        shuffleObject.setAssetUrl_orig(assetUrl);
        shuffleObject.setAssetUrl_thumb(assetUrl);
        shuffleObject.setAssetUrl_med(assetUrl);
        shuffleObject.setAssetUrl_large(assetUrl);
        shuffleObject.setArtist(allRequestParams.get("Artist").toString());
        shuffleObject.setArtistWebsite(allRequestParams.get("ArtistWebsite").toString());
        shuffleObject.setDescription(allRequestParams.get("Description").toString());
        shuffleObject.setTitle(allRequestParams.get("Title").toString());
        shuffleObject.setChannel(allRequestParams.get("Channel").toString());
        shuffleObject.setCreatedDate(DateTime.now());
        shuffleObject.setUpdatedDate(DateTime.now());
        shuffleObject.setActive(true);
        shuffleObject.setAttributes(attr);

        ShuffleUtil.createContentInS3(file);
        ShuffleUtil.createMetaInDb(shuffleObject);

        model.addAttribute("uploadFileName", file.getName());
        return "redirect:/upload";
    }
}

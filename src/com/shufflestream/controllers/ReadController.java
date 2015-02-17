package com.shufflestream.controllers;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.codehaus.jackson.JsonGenerator;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shufflestream.pojo.ShuffleChannel;
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

@Controller
public class ReadController {

    // JSP URLs
    @RequestMapping("/createchannel")
    public String createchannel(Model model) throws IOException, ClassNotFoundException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = (String) chan.getChannelName();
            channels.add(s);
        }
        model.addAttribute("channels", channels);
        return "createchannel";
    }

    @RequestMapping("/managechannel")
    public String managechannel(@RequestParam(value = "channel", required = false) String channel, Model model) throws ClassNotFoundException,
            IOException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = (String) chan.getChannelName();
            channels.add(s);
        }
        model.addAttribute("channels", channels);

        List<ShuffleObject> listFromDb = new ArrayList<ShuffleObject>();
        if (channel != null) {
            listFromDb = ShuffleUtil.getContentFromDb(channel);
        }
        else {
            listFromDb = ShuffleUtil.getContentFromDb();
        }
        model.addAttribute("content", listFromDb);

        return "managechannel";
    }

    @RequestMapping("/upload")
    public String upload(Model model) throws ClassNotFoundException, IOException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = (String) chan.getChannelName();
            channels.add(s);
        }
        model.addAttribute("channels", channels);

        Map<String, List<String>> attributes = getAttributes();
        model.addAttribute("attributes", attributes);

        return "upload";
    }

    // JSON URLs
    @RequestMapping(value = "/getchannels", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ShuffleChannel> getchannels(Model model) throws ClassNotFoundException, IOException {
        List<ShuffleChannel> channels = ShuffleUtil.getChannelsfromDb();
        return channels;
    }

    @RequestMapping(value = "/getcontent", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ShuffleObject> getcontent(@RequestParam(value = "channel", required = false) String channel, Model model)
            throws ClassNotFoundException, IOException {

        List<ShuffleObject> listFromDb = new ArrayList<ShuffleObject>();

        if (channel != null) {
            listFromDb = ShuffleUtil.getContentFromDb(channel);
        }
        else {
            listFromDb = ShuffleUtil.getContentFromDb();
        }
        return listFromDb;
    }

    // get attributes from config (couldn't get this to work in separate class - NPE)
    @Value("${att1.key}")
    String att1Key;
    @Value("${att1.values}")
    String att1Value;

    @Value("${att2.key}")
    String att2Key;
    @Value("${att2.values}")
    String att2Value;

    @Value("${att3.key}")
    String att3Key;
    @Value("${att3.values}")
    String att3Value;

    private Map<String, List<String>> getAttributes() {
        Map<String, List<String>> items = new HashMap<String, List<String>>();

        List<String> att1List = Arrays.asList(att1Value.split("\\s*,\\s*"));
        items.put(att1Key, att1List);

        List<String> att2List = Arrays.asList(att2Value.split("\\s*,\\s*"));
        items.put(att2Key, att2List);

        List<String> att3List = Arrays.asList(att3Value.split("\\s*,\\s*"));
        items.put(att3Key, att3List);

        return items;
    }

}

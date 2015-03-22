package com.shufflestream.controllers;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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
import com.shufflestream.pojo.ShuffleChannel;
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

@Controller
public class WriteController {

    private static String assetUrlRoot = "http://s3.amazonaws.com/shuffle2/images/";

    // Channel methods
    @RequestMapping(value = "/admin/createchannel", method = RequestMethod.POST)
    public String createchannel(Model model, @RequestParam("channel") String channelName, @RequestParam("description") String desc)
            throws IOException,
            ClassNotFoundException {

        ShuffleChannel channelObject = new ShuffleChannel();
        channelObject.setChannelName(channelName);
        channelObject.setDescription(desc);
        channelObject.setActive(true);

        // write the channel list object to s3
        ShuffleUtil.createChannelInDb(channelObject);

        return "redirect:/admin/createchannel";
    }

    @RequestMapping(value = "/admin/editchannel", method = RequestMethod.POST)
    public String editchannel(Model model, @RequestParam("id") String id, @RequestParam("ChannelName") String channelName,
            @RequestParam("Description") String desc, @RequestParam("Active") String active) throws IOException,
            ClassNotFoundException {

        ShuffleChannel originalChannel = ShuffleUtil.getChannelsfromDbSingle(id);

        ShuffleChannel channelObject = new ShuffleChannel();
        channelObject.setId(originalChannel.getId());
        channelObject.setChannelName(channelName);
        channelObject.setDescription(desc);
        channelObject.setActive(Boolean.parseBoolean(active));
        channelObject.setThumbnailUrl(originalChannel.getThumbnailUrl());

        ShuffleUtil.createChannelInDb(channelObject);

        return "redirect:/admin/createchannel";
    }

    // Content methods
    @RequestMapping(value = "/admin/addcontent", method = RequestMethod.POST)
    public String addcontent(Model model, @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> allRequestParams, @RequestParam String[] Channels) throws IOException {

        String assetUrl = assetUrlRoot + URLEncoder.encode(file.getOriginalFilename(), "UTF-8");

        Map<String, Integer> channelList = new HashMap<String, Integer>();
        for (String channel : Channels) {
            channelList.put(channel, 0);
        }

        Map<String, String> attr = new HashMap<String, String>();
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
        shuffleObject.setAssetUrl_orig(assetUrl);
        shuffleObject.setAssetUrl_thumb(assetUrl);
        shuffleObject.setAssetUrl_med(assetUrl);
        shuffleObject.setAssetUrl_large(assetUrl);
        shuffleObject.setArtist(allRequestParams.get("Artist").toString());
        shuffleObject.setArtistWebsite(allRequestParams.get("ArtistWebsite").toString());
        shuffleObject.setDescription(allRequestParams.get("Description").toString());
        shuffleObject.setGeoLocation(allRequestParams.get("Geo Location").toString());
        shuffleObject.setTitle(allRequestParams.get("Title").toString());
        shuffleObject.setChannels(channelList);
        shuffleObject.setCreatedDate(DateTime.now());
        shuffleObject.setUpdatedDate(DateTime.now());
        shuffleObject.setActive(true);
        shuffleObject.setSortOrderInChannel(0);
        shuffleObject.setAttributes(attr);

        ShuffleUtil.createContentInS3(file);
        ShuffleUtil.createMetaInDb(shuffleObject);

        return "redirect:/admin/upload";
    }

    @RequestMapping(value = "/admin/editcontent", method = RequestMethod.POST)
    public String editcontent(Model model, @RequestParam("id") String id,
            @RequestParam Map<String, String> allRequestParams, @RequestParam String[] Channels)
            throws IOException {

        ShuffleObject originalObject = ShuffleUtil.getContentFromDbSingle(id);
        Map<String, Integer> channelList = new HashMap<String, Integer>();

        for (String channel : Channels) {
            Integer order = originalObject.getChannels().get(channel) != null ? originalObject.getChannels().get(channel) : 0;
            channelList.put(channel, order);
        }

        Map<String, String> attr = new HashMap<String, String>();
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
        shuffleObject.setId(originalObject.getId());
        shuffleObject.setAssetUrl_orig(originalObject.getAssetUrl_orig());
        shuffleObject.setAssetUrl_thumb(originalObject.getAssetUrl_thumb());
        shuffleObject.setAssetUrl_med(originalObject.getAssetUrl_med());
        shuffleObject.setAssetUrl_large(originalObject.getAssetUrl_large());
        shuffleObject.setArtist(allRequestParams.get("Artist").toString());
        shuffleObject.setArtistWebsite(allRequestParams.get("ArtistWebsite").toString());
        shuffleObject.setDescription(allRequestParams.get("Description").toString());
        shuffleObject.setGeoLocation(allRequestParams.get("Geo Location").toString());
        shuffleObject.setTitle(allRequestParams.get("Title").toString());
        shuffleObject.setChannels(channelList);
        shuffleObject.setCreatedDate(originalObject.getCreatedDate());
        shuffleObject.setUpdatedDate(DateTime.now());
        shuffleObject.setActive(Boolean.parseBoolean(allRequestParams.get("Active")));
        shuffleObject.setAttributes(attr);

        ShuffleUtil.createMetaInDb(shuffleObject);

        return "redirect:/admin/editcontent?id=" + id;
    }

    @RequestMapping(value = "/admin/updateorder", method = RequestMethod.POST)
    public String updateorder(Model model, @RequestParam("id") String id, @RequestParam("ordervalue") String updatedVal,
            @RequestParam("channelParam") String channelParam) {
        Map<String, Integer> channels = new ConcurrentHashMap<String, Integer>();
        channels = ShuffleUtil.getContentFromDbSingle(id).getChannels();

        try {
            Iterator<String> itr = channels.keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                if (key.equals(channelParam)) {
                    channels.remove(channelParam);
                    channels.put(channelParam, Integer.parseInt(updatedVal));
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }

        ShuffleUtil.updateMetaInDb(id, "Channels", channels);

        return "redirect:/admin/managechannel?channel=" + channelParam;
    }

    @RequestMapping(value = "/admin/makecover", method = RequestMethod.POST)
    public String makecover(Model madel, @RequestParam("id") String id, @RequestParam("channelParam") String channelParam)
            throws ClassNotFoundException, IOException {
        ShuffleObject so = ShuffleUtil.getContentFromDbSingle(id);
        String thumb = so.getAssetUrl_thumb();

        List<ShuffleChannel> sc = ShuffleUtil.getChannelsfromDb();
        ShuffleChannel channel = new ShuffleChannel();
        for (ShuffleChannel chan : sc) {
            if (chan.getChannelName().equals(channelParam)) {
                channel = chan;
            }
        }
        channel.setThumbnailUrl(thumb);

        ShuffleUtil.createChannelInDb(channel);

        return "redirect:/admin/managechannel?channel=" + channelParam;
    }
}

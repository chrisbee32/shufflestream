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

import com.shufflestream.pojo.VisualDNA;
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

    // Login URLs
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/loginerror", method = RequestMethod.GET)
    public String loginError(Model model) {
        model.addAttribute("error", "true");
        return "login";
    }

    // JSP URLs

    @RequestMapping("/admin/createvisualdna")
    public String createdna(Model model) throws IOException, ClassNotFoundException {
        List<VisualDNA> dnaFromDb = ShuffleUtil.getDNAfromDb();
        List<String> dnal = new ArrayList<String>();

        for (VisualDNA vdna : dnaFromDb) {
            String s = vdna.getUUID();
            dnal.add(s);
        }
        model.addAttribute("dna", dnaFromDb);
        return "createvisualdna";
    }



    @RequestMapping("/admin/createchannel")
    public String createchannel(Model model) throws IOException, ClassNotFoundException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = (String) chan.getChannelName();
            channels.add(s);
        }
        model.addAttribute("channels", channelsFromDb);
        return "createchannel";
    }

    @RequestMapping("/admin/managechannel")
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
            listFromDb = new ArrayList<ShuffleObject>();
        }
        model.addAttribute("content", listFromDb);

        return "managechannel";
    }

    @RequestMapping("/admin/upload")
    public String upload(Model model) throws ClassNotFoundException, IOException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = chan.getChannelName();
            channels.add(s);
        }
        model.addAttribute("channels", channels);

        Map<String, List<String>> attributes = getAttributes();
        model.addAttribute("attributes", attributes);

        return "upload";
    }

    @RequestMapping("/admin/editcontent")
    public String editcontent(@RequestParam(value = "id", required = true) String id, Model model) throws ClassNotFoundException,
            IOException {
        List<ShuffleChannel> channelsFromDb = ShuffleUtil.getChannelsfromDb();
        List<String> channels = new ArrayList<String>();

        for (ShuffleChannel chan : channelsFromDb) {
            String s = chan.getChannelName();
            channels.add(s);
        }

        model.addAttribute("channels", channels);

        ShuffleObject singleContentFromDb = new ShuffleObject();
        singleContentFromDb = ShuffleUtil.getContentFromDbSingle(id);
        model.addAttribute("item", singleContentFromDb);

        Map<String, List<String>> attributes = getAttributes();
        model.addAttribute("attributes", attributes);

        return "editcontent";
    }

    @RequestMapping("/admin/editchannel")
    public String editchannel(@RequestParam(value = "id", required = true) String id, Model model) throws ClassNotFoundException,
            IOException {
        ShuffleChannel singleChannelFromDb = ShuffleUtil.getChannelsfromDbSingle(id);
        model.addAttribute("channel", singleChannelFromDb);

        return "editchannel";
    }

    // JSON URLs
    @RequestMapping(value = "/getchannels", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ShuffleChannel> getchannels(Model model) throws ClassNotFoundException, IOException {
        List<ShuffleChannel> channels = ShuffleUtil.getChannelsfromDb();
        List<ShuffleChannel> activeChannels = new ArrayList<ShuffleChannel>();
        for (ShuffleChannel sc : channels) {
            if (sc.getActive() != false) {
                activeChannels.add(sc);
            }
        }
        return activeChannels;
    }

    @RequestMapping(value = "/getcontent", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<ShuffleObject> getcontent(@RequestParam(value = "channel", required = false) String channel, Model model)
            throws ClassNotFoundException, IOException {
        List<ShuffleObject> listFromDb = new ArrayList<ShuffleObject>();
        List<ShuffleObject> activeListFromDb = new ArrayList<ShuffleObject>();
        if (channel != null) {
            listFromDb = ShuffleUtil.getContentFromDb(channel);
        }
        else {
            listFromDb = ShuffleUtil.getContentFromDb();
        }
        for (ShuffleObject so : listFromDb) {
            if (so.getActive() != false) {
                activeListFromDb.add(so);
            }
        }
        return activeListFromDb;
    }

    // get attributes from config (couldn't get this to work in separate class - NPE)
    @Value("${subject.values}")
    String subject;

    @Value("${time.values}")
    String time;

    @Value("${medium.values}")
    String medium;

    @Value("${color.values}")
    String color;

    @Value("${pallette.values}")
    String pallette;

    @Value("${light.values}")
    String light;

    @Value("${temperature.values}")
    String temperature;

    @Value("${locationtype.values}")
    String locationtype;

    @Value("${environment.values}")
    String environment;

    @Value("${style.values}")
    String style;

    @Value("${mood.values}")
    String mood;

    @Value("${motion.values}")
    String motion;

    @Value("${coherence.values}")
    String coherence;

    @Value("${predominantspace.values}")
    String predominantspace;

    @Value("${rhythm.values}")
    String rhythm;

    @Value("${texture.values}")
    String texture;

    @Value("${mass.values}")
    String mass;

    @Value("${timeofday.values}")
    String timeofday;

    private Map<String, List<String>> getAttributes() {
        Map<String, List<String>> items = new HashMap<String, List<String>>();
        items.put("Subject", Arrays.asList(subject.split("\\s*,\\s*")));
        items.put("Time Period", Arrays.asList(time.split("\\s*,\\s*")));
        items.put("Medium", Arrays.asList(medium.split("\\s*,\\s*")));
        items.put("Color", Arrays.asList(color.split("\\s*,\\s*")));
        items.put("Pallette", Arrays.asList(pallette.split("\\s*,\\s*")));
        items.put("Light", Arrays.asList(light.split("\\s*,\\s*")));
        items.put("Temperature", Arrays.asList(temperature.split("\\s*,\\s*")));
        items.put("Location Type", Arrays.asList(locationtype.split("\\s*,\\s*")));
        items.put("Environment", Arrays.asList(environment.split("\\s*,\\s*")));
        items.put("Style", Arrays.asList(style.split("\\s*,\\s*")));
        items.put("Mood", Arrays.asList(mood.split("\\s*,\\s*")));
        items.put("Motion", Arrays.asList(motion.split("\\s*,\\s*")));
        items.put("Coherence", Arrays.asList(coherence.split("\\s*,\\s*")));
        items.put("Predominant Space", Arrays.asList(predominantspace.split("\\s*,\\s*")));
        items.put("Rhythm", Arrays.asList(rhythm.split("\\s*,\\s*")));
        items.put("Texture", Arrays.asList(texture.split("\\s*,\\s*")));
        items.put("Mass", Arrays.asList(mass.split("\\s*,\\s*")));
        items.put("Time Of Day", Arrays.asList(timeofday.split("\\s*,\\s*")));

        return items;
    }

}

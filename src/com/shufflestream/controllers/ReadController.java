package com.shufflestream.controllers;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

@Controller
public class ReadController {

    private static String bucketName = "shufflestream";

    @RequestMapping("/upload")
    public String upload(Model model) throws ClassNotFoundException, IOException {
        List<String> channels = ShuffleUtil.getChannels();
        model.addAttribute("channels", channels);
        return "upload";
    }

    @RequestMapping(value = "/getcontent", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map<String, ShuffleObject> listcontent(@RequestParam(value = "channel", required = false) String channel, Model model)
            throws ClassNotFoundException, IOException {
        Map<String, ShuffleObject> map = new HashMap<String, ShuffleObject>();
        if (channel != null) {
            map = ShuffleUtil.getContent(channel);
        }
        else {
            map = ShuffleUtil.getContent();
        }
        return map;
    }

    @RequestMapping("/allchannels")
    public String allchannels(Model model) throws IOException, ClassNotFoundException {
        List<String> channels = ShuffleUtil.getChannels();
        model.addAttribute("channels", channels);
        return "allchannels";
    }

}

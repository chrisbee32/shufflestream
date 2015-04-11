package com.shufflestream.controllers;

/**
 * Created by dylan on 4/10/15.
 */


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

import com.shufflestream.pojo.VisualDNA;
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
import com.shufflestream.pojo.VisualDNA;
import com.shufflestream.pojo.ShuffleObject;
import com.shufflestream.util.ShuffleUtil;

@Controller
public class UtilController {

    private static String assetUrlRoot = "http://s3.amazonaws.com/shuffle2/images/";

    // Channel methods
    @RequestMapping(value = "/admin/createvisualdna", method = RequestMethod.POST)
    public String createvisualdna(Model model, @RequestParam("title") String title,
                                  @RequestParam("description") String desc, @RequestParam("UUID") String uuid,
                                  @RequestParam("parentId") String parentId, @RequestParam("scaleMin") String scaleMin,
                                  @RequestParam("scaleMax") String scaleMax, @RequestParam("intefaceOrder") String intefaceOrder,
                                  @RequestParam("group") String group, @RequestParam("isTopDNA") String isTopDNA,
                                  @RequestParam("scaleValues") List<String> scaleValues)
            throws IOException,
            ClassNotFoundException {

        VisualDNA dnaObject = new VisualDNA();

        dnaObject.setDescription(desc);
        dnaObject.setTitle(title);  // get the title from input request string
        dnaObject.setUUID(uuid);
        dnaObject.setParentId(Integer.parseInt(parentId));
        dnaObject.setScaleMin(Integer.parseInt(scaleMin));
        dnaObject.setScaleMax(Integer.parseInt(scaleMax));
        dnaObject.setInterfaceOrder(Integer.parseInt(intefaceOrder));
        dnaObject.setGroup(group);
        boolean isTP = false;
         if(isTopDNA.equalsIgnoreCase("true")){
              isTP = true;
         }

        dnaObject.setIsTopDNA( isTP );
        dnaObject.setScaleValues(scaleValues);

        // write the DNA list object to s3
        ShuffleUtil.createDNAInDb(dnaObject);

        return "redirect:/admin/createdna";
    }


}

package com.shufflestream.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

@Controller
public class WriteController {

    // should take in form data for channel name
    @RequestMapping("/createchannel")
    public String createchannel(Model model) {
        // generate channelID

        // create mongo connection

        // write channel object to mongo

        // should return back channel name and success/failure message to view
        return "createchannel";
    }

    // should take in form data for content name, caption
    @RequestMapping("/addcontent")
    public String addcontent(Model model) {
        // get channelID from request

        // create mongo connection

        // write content object to mongo - needs to have relationship to channel in mongo

        // should return back content name, channel assigned and success/failure message to view
        return "addcontent";
    }

}

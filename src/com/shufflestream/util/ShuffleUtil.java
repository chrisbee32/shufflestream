package com.shufflestream.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.shufflestream.pojo.ShuffleObject;

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

public class ShuffleUtil {

    // UTILITY METHODS//
    // read multipartFile and write it to disk
    public static File createWriteableImageFile(MultipartFile multipartFile) throws IOException {
        System.out.println("multipart file: " + multipartFile.toString());
        File f = new File("temp_image.jpg");
        System.out.println("image file: " + f.toString());
        FileOutputStream out = new FileOutputStream(f);
        out.write(multipartFile.getBytes());
        out.close();
        return f;
    }

    public static File createWritableMetaFile(ShuffleObject so) throws IOException {
        System.out.println("shuffle object: " + so.toString());
        File f = new File("temp_meta.ser");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(so);
        System.out.println("meta file: " + f.toString());
        oos.close();
        fos.close();
        return f;

    }

    // connect to s3
    public static AmazonS3 s3Conn() {

        System.out.println(System.getenv("AWS_ACCESS_KEY"));
        System.out.println("");
        System.out.println(System.getenv("AWS_SECRET_KEY"));
        System.out.println("");
        System.out.println(System.getenv("M2_HOME"));
        System.out.println("");
        System.out.println(System.getenv("LOGNAME"));
        System.out.println("");
        System.out.println(System.getenv("SECURITYSESSIONID"));
        System.out.println("");
        System.out.println(System.getenv("PATH"));

        // AWSCredentials credentials = new BasicAWSCredentials("AKIAJSQYINU6WTAXABZQ", "UlhO9JUMz6cqcuInv35tC4XtxiOsn6/OkUKN3m3h");
        AWSCredentials credentials = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
        AmazonS3 s3conn = new AmazonS3Client(credentials);

        return s3conn;
    }

}

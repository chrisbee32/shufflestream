package com.shufflestream.util;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.shufflestream.pojo.ShuffleChannel;
import com.shufflestream.pojo.ShuffleObject;

import java.io.*;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.image.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

public class ShuffleUtil {

    private static String bucketName = "shuffle2";
    private static String imageKeyNameFolder = "images/";
    private static String metaKeyNameFolder = "meta/";
    private static String channelKeyNameFolder = "channels/";

    // /////////////////////
    // //AWS Connections////
    // ////////////////////

    // connect to s3
    private static AWSCredentials AWSConn() {
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
        } catch (Exception e) {
            credentials = new ProfileCredentialsProvider().getCredentials();
        }
        return credentials;
    }

    public static AmazonS3 s3Conn() {
        AWSCredentials credentials = AWSConn();
        AmazonS3 s3conn = new AmazonS3Client(credentials);
        return s3conn;
    }

    public static AmazonDynamoDBClient DynamoDBConn() {
        AWSCredentials credentials = AWSConn();
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        return dynamoDB;
    }

    // /////////////////////
    // //Read Methods////
    // ////////////////////

    public static List<Map<String, String>> getChannelsfromDb() throws IOException, ClassNotFoundException {
        List<Map<String, String>> channels = new ArrayList<Map<String, String>>();
        String tableName = "ShuffleChannel1";
        AmazonDynamoDBClient dynamoDb = ShuffleUtil.DynamoDBConn();

        ScanRequest scanRequest = new ScanRequest().withTableName(tableName);
        ScanResult result = dynamoDb.scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()) {

            Map<String, String> channel = new HashMap<String, String>();
            for (Map.Entry<String, AttributeValue> kvp : item.entrySet())
            {
                String key = kvp.getKey();
                String value = kvp.getValue().getS();
                channel.put(key, value);
            }
            channels.add(channel);
        }
        return channels;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getChannels() throws IOException, ClassNotFoundException {
        List<String> channels = new ArrayList<String>();
        AmazonS3 s3 = ShuffleUtil.s3Conn();
        S3Object obj = null;
        try {
            obj = s3.getObject(new GetObjectRequest(bucketName, "channels/chan.ser"));
        } catch (AmazonServiceException e1) {
            e1.printStackTrace();
        } catch (AmazonClientException e1) {
            e1.printStackTrace();
        }
        if (obj != null) {
            InputStream is = obj.getObjectContent();
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(is);
                channels = (List<String>) ois.readObject();
            } catch (EOFException e) {
            }
        }
        return channels;
    }

    public static Map<String, List<ShuffleObject>> getContent() {
        Map<String, List<ShuffleObject>> map = getAllContent();
        return map;
    }

    // overloaded getContent to filter for channel
    public static Map<String, List<ShuffleObject>> getContent(String channel) {
        Map<String, List<ShuffleObject>> map = getAllContent();
        Map<String, List<ShuffleObject>> filteredMap = new HashMap<String, List<ShuffleObject>>();
        List<ShuffleObject> filteredList = new ArrayList<ShuffleObject>();

        // /WIP///
        // for (Map.Entry<String, List<ShuffleObject>> mapEntry : map.entrySet()) {
        //
        // }

        for (String s : map.keySet()) {
            for (List<ShuffleObject> sol : map.values()) {
                for (ShuffleObject so : sol) {
                    if (so.getChannel() != null && so.getChannel().equals(channel)) {
                        filteredList.add(so);
                    }
                }
            }
            filteredMap.put(s, filteredList);
        }

        return filteredMap;
    }

    // read DB methods
    private static Map<String, List<ShuffleObject>> getAllContent() {
        Map<String, List<ShuffleObject>> map = new HashMap<String, List<ShuffleObject>>();
        List<ShuffleObject> shuffleList = new ArrayList<ShuffleObject>();
        try {
            AmazonS3 s3 = s3Conn();
            List<S3ObjectSummary> summaries = null;
            ObjectListing listing = s3.listObjects(bucketName, "meta");
            summaries = listing.getObjectSummaries();

            for (S3ObjectSummary obj : summaries) {
                ShuffleObject so = new ShuffleObject();
                S3Object object = s3.getObject(new GetObjectRequest(bucketName, obj.getKey()));
                InputStream is = object.getObjectContent();
                ObjectInputStream ois;
                try {
                    ois = new ObjectInputStream(is);
                    so = (ShuffleObject) ois.readObject();
                    shuffleList.add(so);
                } catch (EOFException e) {
                }
            }
        } catch (Exception e) {
            System.out.println("Util failed to make connection");
            map.put("error - bad connection", shuffleList);
            e.printStackTrace();
        }

        map.put("content", shuffleList);
        return map;
    }

    // /////////////////////
    // //Write Methods////
    // ////////////////////
    public static void createChannelInDb(String channel, String description) {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100000);
        String Id = Integer.toString(randomInt);

        String tableName = "ShuffleChannel1";
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("Id", new AttributeValue().withN(Id));
        item.put("ChannelName", new AttributeValue(channel));
        item.put("Description", new AttributeValue(description));
        item.put("ThumbnailUrl", new AttributeValue("https://s3.amazonaws.com/" + bucketName + "/" + imageKeyNameFolder));
        item.put("TotalContent", new AttributeValue().withN("0"));
        item.put("CreatedDate", new AttributeValue(DateTime.now().toString()));
        item.put("UpdatedDate", new AttributeValue(DateTime.now().toString()));
        item.put("Active", new AttributeValue().withBOOL(true));

        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        AmazonDynamoDBClient dynamoDB = ShuffleUtil.DynamoDBConn();
        dynamoDB.putItem(putItemRequest);
    }

    public static void createChannel(List<String> channels) throws IOException, ClassNotFoundException {
        File channelFile = createWritableChannelFile(channels);
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
    }

    public static void createContent(ShuffleObject shuffleObject, MultipartFile file) throws IOException {
        String metaKeyName = metaKeyNameFolder + shuffleObject.hashCode();
        String imageKeyName = imageKeyNameFolder + file.getOriginalFilename();
        System.out.println("Shuffle object: " + metaKeyName + " - " + shuffleObject.toString());

        // convert multipartfile and shuffle object to writeable Files
        File uploadedFile = createWriteableImageFile(file);
        File uploadedMeta = createWritableMetaFile(shuffleObject);

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
    }

    // /////////////////////
    // //File I/O Methods////
    // ////////////////////

    // read multipartFile and write it to disk
    private static File createWriteableImageFile(MultipartFile multipartFile) throws IOException {
        System.out.println("multipart file: " + multipartFile.toString());
        File f = new File("image.jpg");
        System.out.println("image file: " + f.toString());
        FileOutputStream out = new FileOutputStream(f);
        out.write(multipartFile.getBytes());
        out.close();
        return f;
    }

    // read ShuffleObject and write it to disk
    private static File createWritableMetaFile(ShuffleObject so) throws IOException {
        System.out.println("shuffle object: " + so.toString());
        File f = new File("meta.ser");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(so);
        System.out.println("meta file: " + f.toString());
        oos.close();
        fos.close();
        return f;
    }

    // read list<String> and write it to disk
    private static File createWritableChannelFile(List<String> chan) throws IOException {
        System.out.println("shuffle chan: " + chan);
        File f = new File("chan.ser");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(chan);
        System.out.println("chan file: " + f.toString());
        oos.close();
        fos.close();
        return f;
    }
}

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
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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

    private static String tableNameChan = "ShuffleChannel1";
    private static String tableNameContent = "ShuffleContent3";

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

    public static List<ShuffleChannel> getChannelsfromDb() throws IOException, ClassNotFoundException {
        AmazonDynamoDBClient dynamoDb = ShuffleUtil.DynamoDBConn();

        ScanRequest scanRequest = new ScanRequest().withTableName(tableNameChan);
        ScanResult result = dynamoDb.scan(scanRequest);

        List<ShuffleChannel> channels = createShuffleChannelList(result);

        return channels;
    }

    public static List<ShuffleObject> getContentFromDb() {
        AmazonDynamoDBClient dynamoDb = ShuffleUtil.DynamoDBConn();

        ScanRequest scanRequest = new ScanRequest().withTableName(tableNameContent);
        ScanResult result = dynamoDb.scan(scanRequest);

        List<ShuffleObject> returnList = createShuffleObjectList(result);

        return returnList;
    }

    public static List<ShuffleObject> getContentFromDb(String channel) {
        AmazonDynamoDBClient dynamoDb = ShuffleUtil.DynamoDBConn();

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(channel));
        scanFilter.put("Channel", condition);

        ScanRequest scanRequest = new ScanRequest().withTableName(tableNameContent).withScanFilter(scanFilter);
        ScanResult result = dynamoDb.scan(scanRequest);

        List<ShuffleObject> returnList = createShuffleObjectList(result);

        return returnList;
    }

    // /////////////////////
    // //Write Methods////
    // ////////////////////
    public static void createChannelInDb(String channel, String description) {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100000);
        String Id = Integer.toString(randomInt);

        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("Id", new AttributeValue().withN(Id));
        item.put("ChannelName", new AttributeValue(channel));
        item.put("Description", new AttributeValue(description));
        item.put("ThumbnailUrl", new AttributeValue("https://s3.amazonaws.com/" + bucketName + "/" + imageKeyNameFolder));
        item.put("TotalContent", new AttributeValue().withN("0"));
        item.put("CreatedDate", new AttributeValue(DateTime.now().toString()));
        item.put("UpdatedDate", new AttributeValue(DateTime.now().toString()));
        item.put("Active", new AttributeValue().withBOOL(true));

        PutItemRequest putItemRequest = new PutItemRequest(tableNameChan, item);
        AmazonDynamoDBClient dynamoDB = ShuffleUtil.DynamoDBConn();
        dynamoDB.putItem(putItemRequest);
    }

    public static void createMetaInDb(ShuffleObject shuffleObject) {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100000);
        String Id = Integer.toString(randomInt);

        DateTime dt = DateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withLocale(Locale.US);
        String now = formatter.print(dt);

        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        Map<String, AttributeValue> attr = new HashMap<String, AttributeValue>();
        item.put("Id", new AttributeValue().withN(Id));
        item.put("AssetUrl_orig", new AttributeValue(shuffleObject.getAssetUrl_orig()));
        item.put("AssetUrl_thumb", new AttributeValue(shuffleObject.getAssetUrl_thumb()));
        item.put("AssetUrl_med", new AttributeValue(shuffleObject.getAssetUrl_med()));
        item.put("AssetUrl_large", new AttributeValue(shuffleObject.getAssetUrl_large()));
        item.put("Title", new AttributeValue(shuffleObject.getTitle()));
        item.put("Artist", new AttributeValue(shuffleObject.getArtist()));
        item.put("ArtistWebsite", new AttributeValue(shuffleObject.getArtistWebsite()));
        item.put("Channel", new AttributeValue(shuffleObject.getChannel()));
        item.put("CreatedDate", new AttributeValue(now));
        item.put("UpdatedDate", new AttributeValue(now));
        item.put("Active", new AttributeValue().withBOOL(shuffleObject.getActive()));
        for (Map.Entry<String, String> at : shuffleObject.getAttributes().entrySet()) {
            AttributeValue av = new AttributeValue(at.getValue());
            attr.put(at.getKey(), av);
        }
        item.put("Attributes", new AttributeValue().withM(attr));

        PutItemRequest putItemRequest = new PutItemRequest(tableNameContent, item);
        AmazonDynamoDBClient dynamoDB = ShuffleUtil.DynamoDBConn();
        dynamoDB.putItem(putItemRequest);
    }

    public static void createContentInS3(MultipartFile file) throws IOException {

        String imageKeyName = imageKeyNameFolder + file.getOriginalFilename();

        // convert multipartfile and shuffle object to writeable Files
        File uploadedFile = createWriteableImageFile(file);

        // TODO: edit image file

        // read file from disk
        FileInputStream imageStream = new FileInputStream(uploadedFile);

        // create connection, set metadata properties and write to s3
        AmazonS3 s3 = ShuffleUtil.s3Conn();
        ObjectMetadata imageObjectMetadata = new ObjectMetadata();
        imageObjectMetadata.setContentLength(uploadedFile.length());

        try {
            s3.putObject(new PutObjectRequest(bucketName, imageKeyName, imageStream, imageObjectMetadata));

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
            imageStream.close();
        }
    }

    // /////////////////////
    // //Utility Methods////
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

    private static List<ShuffleObject> createShuffleObjectList(ScanResult result) {
        List<ShuffleObject> list = new ArrayList<ShuffleObject>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        for (Map<String, AttributeValue> item : result.getItems()) {
            ShuffleObject so = new ShuffleObject();
            for (Map.Entry<String, AttributeValue> kvp : item.entrySet()) {
                String key = kvp.getKey();
                if (key.equals("Id")) {
                    String value = kvp.getValue().getN();
                    so.setId(Integer.parseInt(value));
                }
                if (key.equals("Title")) {
                    String value = kvp.getValue().getS();
                    so.setTitle(value);
                }
                if (key.equals("Channel")) {
                    String value = kvp.getValue().getS();
                    so.setChannel(value);
                }
                if (key.equals("Artist")) {
                    String value = kvp.getValue().getS();
                    so.setArtist(value);
                }
                if (key.equals("Description")) {
                    String value = kvp.getValue().getS();
                    so.setDescription(value);
                }
                if (key.equals("AssetUrl_orig")) {
                    String value = kvp.getValue().getS();
                    so.setAssetUrl_orig(value);
                }
                if (key.equals("AssetUrl_thumb")) {
                    String value = kvp.getValue().getS();
                    so.setAssetUrl_thumb(value);
                }
                if (key.equals("AssetUrl_med")) {
                    String value = kvp.getValue().getS();
                    so.setAssetUrl_med(value);
                }
                if (key.equals("AssetUrl_large")) {
                    String value = kvp.getValue().getS();
                    so.setAssetUrl_large(value);
                }
                if (key.equals("ArtistWebsite")) {
                    String value = kvp.getValue().getS();
                    so.setArtistWebsite(value);
                }
                if (key.equals("CreatedDate")) {
                    String value = kvp.getValue().getS();
                    so.setCreatedDate(formatter.parseDateTime(value));
                }
                if (key.equals("UpdatedDate")) {
                    String value = kvp.getValue().getS();
                    so.setUpdatedDate(formatter.parseDateTime(value));
                }
                if (key.equals("Active")) {
                    Boolean value = kvp.getValue().getBOOL();
                    so.setActive(value);
                }
                if (key.equals("Attributes")) {
                    System.out.println("CB:::::Key " + key + "Val " + kvp.getValue());
                    Map<String, String> value = new HashMap<String, String>();
                    if (key != null && kvp.getValue() != null) {
                        for (Map.Entry<String, AttributeValue> entry : kvp.getValue().getM().entrySet()) {
                            value.put(entry.getKey(), entry.getValue().getS());
                        }
                    }
                    so.setAttributes(value);
                }
            }
            list.add(so);
        }
        return list;
    }

    private static List<ShuffleChannel> createShuffleChannelList(ScanResult result) {
        List<ShuffleChannel> list = new ArrayList<ShuffleChannel>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS-08:00");
        for (Map<String, AttributeValue> item : result.getItems()) {
            ShuffleChannel channel = new ShuffleChannel();
            for (Map.Entry<String, AttributeValue> kvp : item.entrySet())
            {
                String key = kvp.getKey();
                if (key.equals("Id")) {
                    String value = kvp.getValue().getN();
                    channel.setId(Integer.parseInt(value));
                }
                if (key.equals("ChannelName")) {
                    String value = kvp.getValue().getS();
                    channel.setChannelName(value);
                }
                if (key.equals("Description")) {
                    String value = kvp.getValue().getS();
                    channel.setDescription(value);
                }
                if (key.equals("ThumbnailUrl")) {
                    String value = kvp.getValue().getS();
                    channel.setThumbnailUrl(value);
                }
                if (key.equals("TotalContent")) {
                    channel.setTotalContent(item.entrySet().size());
                }
                if (key.equals("CreatedDate")) {
                    String value = kvp.getValue().getS();
                    channel.setCreatedDate(formatter.parseDateTime(value));
                }
                if (key.equals("UpdatedDate")) {
                    String value = kvp.getValue().getS();
                    channel.setUpdatedDate(formatter.parseDateTime(value));
                }
                if (key.equals("Active")) {
                    Boolean value = kvp.getValue().getBOOL();
                    channel.setActive(value);
                }
            }
            list.add(channel);
        }

        return list;
    }
}

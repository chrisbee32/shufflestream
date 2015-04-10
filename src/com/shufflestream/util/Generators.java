package com.shufflestream.util;


import java.util.UUID;

/**
 * Created by dylan on 4/10/15.
 */
public class Generators {



    public String generateRandomUUID(){

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return  randomUUIDString;
    }





}

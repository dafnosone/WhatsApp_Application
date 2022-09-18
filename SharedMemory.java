package com.example.dschat;

import com.example.dschat.structs.Topic;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class with only public static fields in order
 *  to have a consistent memory block across activities
 */
public class SharedMemory
{
    public static String AppPath = null;
    public static String Username = null;
    public static ArrayList<InetSocketAddress> Brokers = null;
    public static ArrayList<Topic> Topics = new ArrayList<>();
    public static String Upload = null;


    public static InetSocketAddress SelectBroker(final String topicname) { return Brokers.get(getMd5(topicname)%Brokers.size()); }

    private static int getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            int md5Dec = Integer.parseInt(hashtext.substring(0, 5), 16);
            return md5Dec;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
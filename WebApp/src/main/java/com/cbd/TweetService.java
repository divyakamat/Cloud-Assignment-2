package com.cbd;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.sql.*;

/**
 * Created by Divya on 2/27/2015.
 */

@Path("/tweet")
public class TweetService {

    /*
    @GET
    @Produces("application/json")
    public String printString(){
        return("Hello World blah");
    }*/

    public void register(){

    }

    @GET
    @Produces("application/json")
    public Tweet[] getAllTweets(){

        System.out.println("MySQL JDBC Driver Registered!");
        System.out.println("here!!");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return null;
        }

        try {
            Connection conn = DriverManager
                    .getConnection("jdbc:mysql://localhost:mysqlinstance.cymg3toxvsj9.us-east-1.rds.amazonaws.com/project", "divyakamat", "divyakamat");
            Statement stmt1 = conn.createStatement();
            String query = new String("SELECT * FROM tweets;");
            ResultSet rs = stmt1.executeQuery(query);
            int count = 0;
            while(rs.next())
                count++;
            Tweet[] tweet = new Tweet[count];

            System.out.println("here i am!!");
            rs.first();
            for(int i=0;i<count;i++)
            {
                System.out.println(rs.getLong(1));
                tweet[i] = new Tweet(rs.getLong(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4));
                rs.next();
            }
            conn.close();
            return tweet;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("errror!");
        }

        return null;
    }

    @GET
    @Path("/getHashTag/{hash}")
    @Produces("application/json")
    public Tweet[] getHashTweets(@PathParam("hash") String hashTag) {

        System.out.println("MySQL JDBC Driver Registered!");
        System.out.println("here!!");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return null;
        }

        try {
            Connection conn = DriverManager
                    .getConnection("jdbc:mysql://mysqlinstance.cymg3toxvsj9.us-east-1.rds.amazonaws.com:3306/project", "divyakamat", "divyakamat");
            Statement stmt1 = conn.createStatement();
            String query = new String("SELECT * FROM tweets WHERE text LIKE '%#" + hashTag +"%' ;");
            System.out.println(query);
            ResultSet rs = stmt1.executeQuery(query);
            int count = 0;
            while(rs.next())
                count++;
            Tweet[] tweet = new Tweet[count];

            System.out.println("here i am!!");
            rs.first();
            for(int i=0;i<count;i++)
            {
                tweet[i] = new Tweet(rs.getLong(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4));
                rs.next();
            }
            conn.close();
            return tweet;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("errror!");
        }

        return null;
    }


    public class Tweet{
        long tweetId;
        String text;
        double latitude,longitude;

        public Tweet(long tid, String content, double lat, double lon){
            tweetId = tid;
            text = content;
            latitude = lat;
            longitude = lon;
        }
    }
}

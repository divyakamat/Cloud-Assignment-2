package tweetmap;

/**
 * Created by Divya on 2/26/2015.
 */


import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.sql.*;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class getTweets {
    /**
     * Main entry of this application.
     *
     * @param args
     */

    private final Connection conn;

    public getTweets() throws SQLException{
       conn = DriverManager
                .getConnection("jdbc:mysql://mysqlinstance.cymg3toxvsj9.us-east-1.rds.amazonaws.com:3306/project", "divyakamat", "divyakamat");
    }

    public Connection getConn() {
        return conn;
    }

    public static void main(String[] args) throws TwitterException, SQLException {
        //just fill this

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;
        Statement stmt =null;

        final getTweets getTweetsObject = new getTweets();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("s4Go5WLRyLxli1oDwkWWrTfBi")
                .setOAuthConsumerSecret("BuHdaTPcg6JDF2Aw1dv2aizMUo9B3KgklpOc5c7pd8GxKAjW6x")
                .setOAuthAccessToken("3064214433-hij3HAUFHsCjD4nGLyi5sRulXj23PYz22ZUG4I5")
                .setOAuthAccessTokenSecret("uLZdhHIXM9oJ5nScSHGtbEDeU9D3JXreows21snIbm6Mt");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
         StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+ " " + status.getGeoLocation());
                Statement stmt1 = null;
                long count = 0;
                String query = null;
                try {
                    stmt1 = getTweetsObject.getConn().createStatement();
                    query = "SELECT COUNT(*) FROM tweets;";
                    ResultSet rs = stmt1.executeQuery(query);
                    rs.next();
                    count = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                if(status.getGeoLocation() != null)
                {

                    try {

                        if(count < 20000) {
                            query = "INSERT INTO tweets VALUES(" + status.getId() + ", '" + status.getText() + "'," + status.getGeoLocation().getLatitude()
                                    + "," + status.getGeoLocation().getLongitude() + ", '" + status.getPlace().getName() + "');";
                            stmt1.executeUpdate(query);
                            count++;
                        }
                        else {
                            query = "SELECT * FROM tweets LIMIT 1";
                            ResultSet rs = stmt1.executeQuery(query);
                            rs.next();
                            long id = rs.getLong(1);
                            query = "DELETE FROM tweets WHERE tweet_id = " + id + ";";
                            stmt1.executeUpdate(query);
                            query = "INSERT INTO tweets VALUES(" + status.getId() + ", '" + status.getText() + "'," + status.getGeoLocation().getLatitude()
                                    + "," + status.getGeoLocation().getLongitude() + ", '" + status.getPlace().getName() + "');";
                            stmt1.executeUpdate(query);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("errror!");
                    }

                }
            }



            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}
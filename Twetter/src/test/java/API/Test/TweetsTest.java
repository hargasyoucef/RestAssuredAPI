package API.Test;

import groovy.json.internal.Exceptions;
import io.restassured.response.Response;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tweets.TweetsUser;

import java.util.UUID;

public class TweetsTest {
    protected TweetsUser tweetsUser;
    protected long tweetID;

    @BeforeClass
    public void setUp(){
        this.tweetsUser = new TweetsUser();
    }

    @Test
    public void testGetUserTimeLine(){
        tweetsUser.getUserTimeLine();
    }

    @Test
    public void testCreateTweet(){
        String tweet = "the life is short, so just enjoy it!jjjhjhj! "+ UUID.randomUUID();
        Response response =tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_OK); // error 200
        this.tweetID = response.path("id");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testCannotTweetSameTweetTwiceInRow()throws Exception{
        String tweet = "the life is short, so just enjoy it!! "+ UUID.randomUUID();
        Response response =tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_OK);
        response =tweetsUser.createTweet(tweet);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN); // error 403
        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test(dependsOnMethods = {"testCreateTweet"})
    public void testUserCanDeleteTweet(){
        Response response = this.tweetsUser.deleteTweet(this.tweetID);
        response.then().statusCode(HttpStatus.SC_OK);
        Assert.assertEquals(200, response.getStatusCode());
    }
}
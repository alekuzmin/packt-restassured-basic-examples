package demo;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.GetCourse;

import static io.restassured.RestAssured.given;

public class JiraTest {

    @Test
    public void authentication() {
        SessionFilter session = new SessionFilter(); //awesome
        // Login
        RestAssured.baseURI = "http://localhost:8777";
        given().log().all().header("Content-Type", "application/json")
                .body("{ \"username\": \"songvo\", \"password\": \"1234\" }")
                .filter(session)
                .when().post("rest/auth/1/session")
                .then().log().all().assertThat().statusCode(200).extract().asString();
    }

    /**
     * Incase work with HTTPS in the real lift
     */
    @Test
    public void authenticationWithHTTPS() {
        SessionFilter session = new SessionFilter();
        // Login
        RestAssured.baseURI = "http://localhost:8777";
        given().relaxedHTTPSValidation().header("Content-Type", "application/json")
                .body("{ \"username\": \"songvo\", \"password\": \"1234\" }")
                .filter(session)
                .when().post("rest/auth/1/session")
                .then().log().all().assertThat().statusCode(200).extract().asString();
    }

    @Test
    public void addCommentToExistingIssue() {
        SessionFilter session = new SessionFilter();
        // Login
        RestAssured.baseURI = "http://localhost:8777";
        given().log().all().header("Content-Type", "application/json")
                .body("{ \"username\": \"songvo\", \"password\": \"1234\" }")
                .filter(session)
                .when().post("rest/auth/1/session")
                .then().log().all().assertThat().statusCode(200).extract().asString();

        String expectedMessage = "MON-25 - Comment from Song Vo with love";
        // Add comment
        RestAssured.baseURI = "http://localhost:8777";
        String addCommentResponse = given().pathParam("key", "MON-25")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \""+expectedMessage+"\"\n" +
                        "}")
                .filter(session)
                .when().post("rest/api/2/issue/{key}/comment")
                .then().assertThat().statusCode(201).extract().response().asString();
        JsonPath js2 = new JsonPath(addCommentResponse);
        String commentId = js2.get("id");

//        // Add attachment
//        given().header("X-Atlassian-Token", "no-check").filter(session)
//                .header("Content-Type", "multipart/form-data")
//                .multiPart("file", new File("src\\test\\resources\\jira.txt"))
//                .when().post("rest/api/2/issue/MON-25/attachments")
//                .then().log().all().assertThat().statusCode(200);

        // Get issue -> Verify comment body
        String response = given().filter(session).pathParam("key", "MON-25").log().all()
                .queryParam("fields", "comment")
                .when().get("rest/api/2/issue/{key}")
                .then().log().all().extract().response().asString();
        System.out.println(response);
        JsonPath js = new JsonPath(response);
        int commentCount = js.get("fields.comment.comments.size()");
        for(int i = 0; i < commentCount; i++) {
            String commentID2 = js.getString("fields.comment.comments["+i+"].id");
            if(commentID2.equals(commentId)) {
                String actualMessage = js.get("fields.comment.comments["+i+"].body");
                System.out.println(actualMessage);
                Assert.assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    // Cannot run this one
    public void getIssuesAndDeserialization() {
        SessionFilter session = new SessionFilter();
        // Get issue
        GetCourse getCourse = given().filter(session).pathParam("key", "MON-25")
                .queryParam("fields", "comment")
                .expect().defaultParser(Parser.JSON) //Verify response should be JSON
                .when().get("rest/api/2/issue/{key}").as(GetCourse.class); // Example. Not real
    }
}

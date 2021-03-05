package demo;

import files.Payload;
import files.ReUsableMethod;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;

public class BasicTest {
    @Test
    public void createMapFromJsonFile() throws IOException {
        // read content of the file to String -> content of file can convert into Byte -> Byte date to String

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/addPlace.json"))))
                .when().post("maps/api/place/add/json")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);

        JsonPath js = new JsonPath(response); //for parsing Json
        String placeId = js.getString("place_id");
        System.out.println(placeId);
    }

    @Test
    public void createMapNew() {
        // Given - all input details
        // When - submit the api - resource, http
        // Then - validate the response
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(Payload.AddPlace())
                .when().post("maps/api/place/add/json")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);

        JsonPath js = new JsonPath(response); //for parsing Json
        String placeId = js.getString("place_id");
        System.out.println(placeId);

        // Update place
        String newAddress = "39B Truong Son, HCM";
        given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"place_id\": \"" + placeId + "\",\n" +
                        "    \"address\": \"" + newAddress + "\",\n" +
                        "    \"key\": \"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));

        // Get place
        String getPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all()
                .statusCode(200).extract().response().asString();

        JsonPath js2 = ReUsableMethod.rawToJson(getPlaceResponse);
        String actualAddress = js2.getString("address");
        System.out.println(actualAddress);

        // Assertion
        Assert.assertEquals(actualAddress, newAddress);

    }

    @Test
    public void getMap() {
        given().log().all()
                .queryParam("key","qaclick123")
                .queryParam("place_id","6136dcc7ceec78136e72183d6a90695f")
                .when().get("https://rahulshettyacademy.com/maps/api/place/get/json").then().log().all();
    }

    @Test
    public void test01() {
        given().when().get("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1").then().log().all();
    }
}

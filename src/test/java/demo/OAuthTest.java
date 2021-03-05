package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.GetCourse;
import pojo.WebAutomation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OAuthTest {

    @Test
    public void getCourse() throws IOException {
        String[] courseTitles = {"Head first Java", "Cypress", "Protractor"};
        ObjectMapper objectMapper = new ObjectMapper();
        String response = new String(Files.readAllBytes(Paths.get("src/test/resources/getCourses.json")));
        GetCourse gc = objectMapper.readValue(response, GetCourse.class);

        System.out.println(gc.getLinkedIn());

//        List<Api> api = gc.getCourses().getApi();
//        for (Api value : api) {
//            if(value.getCourseTitle().equals("New courses")){
//                System.out.println(value.getPrice());
//            }
//        }

        List<String> actualTitle = new ArrayList<>();
        List<WebAutomation> wa = gc.getCourses().getWebAutomation();
        for (WebAutomation value : wa) {
            actualTitle.add(value.getCourseTitle());
        }

        List<String> expectedList = Arrays.asList(courseTitles);
        Assert.assertEquals(expectedList, actualTitle);
    }

}

package files;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {
    @Test
    public void sumOfCourses() {
        JsonPath js = new JsonPath(Payload.CoursePrice());
        int count = js.getInt("courses.size()");
        // 6. Verify if Sum of all Course prices matches with Purchase Amount
        int sum = 0;
        for(int i = 0; i < count; i++) {
           int price = js.get("courses["+i+"].price");
           int copies = js.get("courses["+i+"].copies");
           int amount = copies * price;
           sum = sum + amount;
           System.out.println(amount);
        }
        Assert.assertEquals(sum, js.getInt("dashboard.purchaseAmount"));
    }
}

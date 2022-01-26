package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Arrays;


public class BlockChainTestStepdefs {
Boolean correctChainVerification = true;
String ENDPOINT = "https://api.blockcypher.com/v1/btc/main";
    @Given("the test endpoint is available")
    public void theTestEndpointIsAvailable() {
        RestAssured.baseURI = ENDPOINT;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/blocks/1");
        Assert.assertEquals("The status code is not the expected!",200,response.getStatusCode());
    }

    @When("the request of the first {string} blocks starting from block number {string} to the endpoint is performed")
    public void theRequestOfTheFirstBlocksStartingFromBlockNumberToTheEndpointIsPerformed(String numberOfBlocks, String startingBlockNumber ){

        // Some defensive programming
        if (!startingBlockNumber.matches("^[0-9]+$") || !numberOfBlocks.matches("^[0-9]+$")){
            throw new Error("Error: the input parameters should be positive integers");
        }

        RestAssured.baseURI = ENDPOINT;
        RequestSpecification request = RestAssured.given();
        JSONObject jsonObject = null;

        int parsedNumberOfBlocks = Integer.parseInt(numberOfBlocks);
        int parsedStartingBlockNumber = Integer.parseInt(startingBlockNumber);
        Response response;
        String uri,body;
            for(int z = (Integer.parseInt(startingBlockNumber)); z<parsedNumberOfBlocks+parsedStartingBlockNumber;z++){
                uri = ("/blocks/" + String.valueOf(z));
                response = request.get(uri);
                 body = response.getBody().asString();
                try{
                    jsonObject = new JSONObject(body);
                    String currentHashValue = jsonObject.get("hash").toString(); // We get the hash value of the first block
                    uri = ("/blocks/" + String.valueOf(z+1));
                    response = request.get(uri);
                    body = response.getBody().asString();
                    jsonObject = new JSONObject(body);
                    String nextBlockPreviousHashValue = jsonObject.get("prev_block").toString(); // We get the value of prev_block from the 2nd block
                    if(!nextBlockPreviousHashValue.equals(currentHashValue)){
                        correctChainVerification = false; // We store the failure in order to use it in other step
                    }
                }catch (JSONException e){
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
    }

    @Then("the response of the endpoint shows the first {int} blocks starting from block {int} correctly chained")
    public void theResponseOfTheEndpointShowsTheFirstBlocksStartingFromBlockCorrectlyChained(int arg0, int arg1) {
        Assert.assertTrue("Error: Incorrect block chaining",correctChainVerification);
    }
}

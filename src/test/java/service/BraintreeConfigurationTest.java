package springexample;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

public class BraintreeConfigurationTest {
    private static BraintreeConfiguration configuration;
    private BraintreeGateway gateway;

    @BeforeClass
    public static void createProperties() throws Exception {
        File mockConfigFile = File.createTempFile("config.properties", ".tmp");
        FileWriter fileWriter = new FileWriter(mockConfigFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("BT_ENVIRONMENT=sandbox\n");
        bufferedWriter.write("BT_MERCHANT_ID=MERCHANT_ID\n");
        bufferedWriter.write("BT_PUBLIC_KEY=PUBLIC_KEY\n");
        bufferedWriter.write("BT_PRIVATE_KEY=PRIVATE_KEY\n");
        bufferedWriter.close();
        configuration = new BraintreeConfiguration(mockConfigFile.getAbsolutePath());
        mockConfigFile.delete();
    }

    @Before
    public void setUp() {
        gateway = configuration.gateway();
    }

    @Test
    public void checkGatewayEnvironment() {
        String expectedEnvironment = Environment.SANDBOX.getEnvironmentName();
        String actualEnvironment = gateway.getConfiguration().getEnvironment().getEnvironmentName();

        Assert.assertEquals("failure - received unexpected environment", expectedEnvironment, actualEnvironment);
    }

    @Test
    public void checkGatewayMerchantPath() {
        String expectedMerchantPath = "/merchants/MERCHANT_ID";
        String actualMerchantPath = gateway.getConfiguration().getMerchantPath();

        Assert.assertEquals("failure - received unexpected merchantPath", expectedMerchantPath, actualMerchantPath);
    }

    @Test
    public void checkGatewayPublicKey() {
        String expectedPublicKey = "PUBLIC_KEY";
        String actualPublicKey = gateway.getConfiguration().getPublicKey();

        Assert.assertEquals("failure - received unexpected publicKey", expectedPublicKey, actualPublicKey);
    }

    @Test
    public void checkGatewayPrivateKey() {
        String expectedPrivateKey = "PRIVATE_KEY";
        String actualPrivateKey = gateway.getConfiguration().getPrivateKey();

        Assert.assertEquals("failure - received unexpected privateKey", expectedPrivateKey, actualPrivateKey);
    }
}

package springexample;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

public class BraintreeGatewayFactoryTest {
    private static File mockConfigFile;
    private static Map<String, String> mockConfigMapping;

    @BeforeClass
    public static void createPropertiesFile() throws IOException {
        mockConfigFile = File.createTempFile("config.properties", ".tmp");
        FileWriter fileWriter = new FileWriter(mockConfigFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("BT_ENVIRONMENT=development\n");
        bufferedWriter.write("BT_MERCHANT_ID=MERCHANT_ID\n");
        bufferedWriter.write("BT_PUBLIC_KEY=PUBLIC_KEY\n");
        bufferedWriter.write("BT_PRIVATE_KEY=PRIVATE_KEY\n");
        bufferedWriter.close();
    }

    @BeforeClass
    public static void createEnvironment() {
        mockConfigMapping = new HashMap<String, String>();
        mockConfigMapping.put("BT_ENVIRONMENT", "sandbox");
        mockConfigMapping.put("BT_MERCHANT_ID", "OTHER_MERCHANT_ID");
        mockConfigMapping.put("BT_PUBLIC_KEY", "OTHER_PUBLIC_KEY");
        mockConfigMapping.put("BT_PRIVATE_KEY", "OTHER_PRIVATE_KEY");
    }

    @Test
    public void loadsCorrectlyFromFile() {
        BraintreeGateway gateway = BraintreeGatewayFactory.fromConfigFile(mockConfigFile);

        String expectedEnvironment = Environment.DEVELOPMENT.getEnvironmentName();
        String expectedMerchantPath = "/merchants/MERCHANT_ID";
        String expectedPrivateKey = "PRIVATE_KEY";
        String expectedPublicKey = "PUBLIC_KEY";

        String actualMerchantPath = gateway.getConfiguration().getMerchantPath();
        String actualPrivateKey = gateway.getConfiguration().getPrivateKey();
        String actualEnvironment = gateway.getConfiguration().getEnvironment().getEnvironmentName();
        String actualPublicKey = gateway.getConfiguration().getPublicKey();

        Assert.assertEquals("failure - received unexpected environment", expectedEnvironment, actualEnvironment);
        Assert.assertEquals("failure - received unexpected merchantPath", expectedMerchantPath, actualMerchantPath);
        Assert.assertEquals("failure - received unexpected publicKey", expectedPublicKey, actualPublicKey);
        Assert.assertEquals("failure - received unexpected privateKey", expectedPrivateKey, actualPrivateKey);
    }

    @Test
    public void loadsCorrectlyFromMapping() {
        BraintreeGateway gateway = BraintreeGatewayFactory.fromConfigMapping(mockConfigMapping);

        String expectedEnvironment = Environment.SANDBOX.getEnvironmentName();
        String expectedMerchantPath = "/merchants/OTHER_MERCHANT_ID";
        String expectedPrivateKey = "OTHER_PRIVATE_KEY";
        String expectedPublicKey = "OTHER_PUBLIC_KEY";

        String actualMerchantPath = gateway.getConfiguration().getMerchantPath();
        String actualPrivateKey = gateway.getConfiguration().getPrivateKey();
        String actualEnvironment = gateway.getConfiguration().getEnvironment().getEnvironmentName();
        String actualPublicKey = gateway.getConfiguration().getPublicKey();

        Assert.assertEquals("failure - received unexpected environment", expectedEnvironment, actualEnvironment);
        Assert.assertEquals("failure - received unexpected merchantPath", expectedMerchantPath, actualMerchantPath);
        Assert.assertEquals("failure - received unexpected publicKey", expectedPublicKey, actualPublicKey);
        Assert.assertEquals("failure - received unexpected privateKey", expectedPrivateKey, actualPrivateKey);
    }

    @Test
    public void throwsNullPointerExceptionIfMissingElementsFromFile() throws IOException {
        File emptyFile = File.createTempFile("empty.properties", ".tmp");
        Exception exception = null;
        try {
            BraintreeGateway gateway = BraintreeGatewayFactory.fromConfigFile(emptyFile);
        } catch (Exception e) {
            exception = e;
        }

        emptyFile.delete();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof NullPointerException);
    }

    @Test
    public void logsErrorAndThrowsNullPointerExceptionIfFileNotFound() {
        PrintStream preservedErrorStream = System.err;
        ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
        Exception exception = null;

        try {
            System.setErr(new PrintStream(errorOutputStream));
            BraintreeGateway gateway = BraintreeGatewayFactory.fromConfigFile(new File("notARealConfig.properties"));
        } catch (Exception e) {
            exception = e;
        } finally {
            System.setErr(preservedErrorStream);
        }

        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof NullPointerException);
        Assert.assertTrue(errorOutputStream.toString().contains("Exception:"));
    }

    @Test
    public void throwsNullPointerExceptionIfMissingElementsFromMapping() {
        Map<String, String> emptyMapping = new HashMap<String, String>();
        Exception exception = null;

        try {
            BraintreeGateway gateway = BraintreeGatewayFactory.fromConfigMapping(emptyMapping);
        } catch (Exception e) {
            exception = e;
        }

        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof NullPointerException);
    }

    @AfterClass
    public static void deletePropertiesFile() {
        mockConfigFile.delete();
    }
}

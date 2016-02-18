package springexample;

import com.braintreegateway.BraintreeGateway;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BraintreeConfiguration {
    private static String environment;
    private static String merchantId;
    private static String publicKey;
    private static String privateKey;

    public BraintreeConfiguration(String fileName) {
        readProperties(fileName);
    }

    private void readProperties(String fileName) {
        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.err.println(fileName + " not found.");
            return;
        }

        try {
            properties.load(inputStream);

            environment = properties.getProperty("BT_ENVIRONMENT");
            merchantId = properties.getProperty("BT_MERCHANT_ID");
            publicKey = properties.getProperty("BT_PUBLIC_KEY");
            privateKey = properties.getProperty("BT_PRIVATE_KEY");
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            try { inputStream.close(); }
            catch (IOException e) { System.err.println("Exception: " + e); }
        }
    }

    public BraintreeGateway gateway() {
        return new BraintreeGateway(environment, merchantId, publicKey, privateKey);
    }
}

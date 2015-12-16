package service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BraintreeConfiguration {
    private static Environment environment;
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
            try {
                properties.load(inputStream);

                environment = properties.getProperty("environment").contains("SANDBOX") ? Environment.SANDBOX : Environment.PRODUCTION;
                merchantId = properties.getProperty("merchantId");
                publicKey = properties.getProperty("publicKey");
                privateKey = properties.getProperty("privateKey");
            } catch (Exception e) {
                System.err.println("Exception: " + e);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }

    public BraintreeGateway gateway() {
        return new BraintreeGateway(environment, merchantId, publicKey, privateKey);
    }
}

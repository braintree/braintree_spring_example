package service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BraintreeConfiguration {

    BraintreeGateway gateway;
    InputStream inputStream = null;

    public BraintreeGateway gateway() {
        try {
            Properties properties = new Properties();
            String fileName = "src/config.properties";
            inputStream = new FileInputStream(fileName);

            if (inputStream != null) {
                properties.load(inputStream);
                Environment environment = properties.getProperty("environment").contains("SANDBOX") ? Environment.SANDBOX : Environment.PRODUCTION;
                String merchantId = properties.getProperty("merchantId");
                String publicKey = properties.getProperty("publicKey");
                String privateKey = properties.getProperty("privateKey");
                gateway = new BraintreeGateway(environment, merchantId, publicKey, privateKey);
            } else {
                throw new FileNotFoundException(fileName + " not found");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        return gateway;
    }
}

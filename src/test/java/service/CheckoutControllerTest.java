package springexample;

import java.math.BigDecimal;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CheckoutControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeClass
    public static void setupConfig() {
        File configFile = new File("config.properties");
        try {
            if(configFile.exists() && !configFile.isDirectory()) {
                Application.gateway = BraintreeGatewayFactory.fromConfigFile(configFile);
            } else {
                Application.gateway = BraintreeGatewayFactory.fromConfigMapping(System.getenv());
            }
        } catch (NullPointerException e) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
        }
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void checkoutReturnsOK() throws Exception {
        mockMvc.perform(get("/checkouts"))
            .andExpect(status().isOk());
    }

    @Test
    public void rendersNewView() throws Exception {
        mockMvc.perform(get("/checkouts"))
            .andExpect(view().name("checkouts/new"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("clientToken"))
            .andExpect(xpath("//script[@src='https://js.braintreegateway.com/v2/braintree.js']").exists());
    }

    @Test
    public void rendersErrorsOnTransactionFailure() throws Exception {
        mockMvc.perform(post("/checkouts")
                .param("payment_method_nonce", "fake-valid-nonce")
                .param("amount", "2000.00"))
            .andExpect(status().isFound());
    }

    @Test
    public void rendersErrorsOnInvalidAmount() throws Exception {
        mockMvc.perform(post("/checkouts")
                .param("payment_method_nonce", "fake-valid-nonce")
                .param("amount", "-1.00"))
            .andExpect(status().isFound())
            .andExpect(flash().attributeExists("errorDetails"));

        mockMvc.perform(post("/checkouts")
                .param("payment_method_nonce", "fake-valid-nonce")
                .param("amount", "not_a_valid_amount"))
            .andExpect(status().isFound())
            .andExpect(flash().attributeExists("errorDetails"));
    }

    @Test
    public void redirectsOnTransactionNotFound() throws Exception {
        mockMvc.perform(post("/checkouts/invalid-transaction"))
            .andExpect(status().isFound());
    }

    @Test
    public void redirectsRootToNew() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isFound());
    }
}

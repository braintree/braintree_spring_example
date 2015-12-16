package service;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {

    private static String configFileName = "config.properties";

    private BraintreeConfiguration config = new BraintreeConfiguration(configFileName);
    private BraintreeGateway gateway = config.gateway();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
        return "redirect:checkouts";
    }

    @RequestMapping(value = "/checkouts", method = RequestMethod.GET)
    public String checkout(Model model) {
        String clientToken = gateway.clientToken().generate();
        model.addAttribute("clientToken", clientToken);

        return "checkouts/new";
    }

    @RequestMapping(value = "/checkouts", method = RequestMethod.POST)
    public String postForm(@RequestParam("amount") String amount, @RequestParam("payment_method_nonce") String nonce, Model model, final RedirectAttributes redirectAttributes) {
        TransactionRequest request = new TransactionRequest()
            .amount(new BigDecimal(amount))
            .paymentMethodNonce(nonce)
            .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            return "redirect:checkouts/" + transaction.getId();
        } else {
            Transaction transaction = result.getTransaction();
            redirectAttributes.addFlashAttribute("errors", "Transaction failed!");
            redirectAttributes.addFlashAttribute("errorDetails", transaction.getStatus());
            return "redirect:checkouts";
        }
    }

    @RequestMapping(value = "/checkouts/{transactionId}")
    public String getTransaction(@PathVariable String transactionId, Model model) {
        Transaction transaction;
        CreditCard creditCard;
        Customer customer;

        try {
            transaction = gateway.transaction().find(transactionId);
            creditCard = transaction.getCreditCard();
            customer = transaction.getCustomer();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return "redirect:/checkouts";
        }

        model.addAttribute("transaction", transaction);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("customer", customer);

        return "checkouts/show";
    }
}

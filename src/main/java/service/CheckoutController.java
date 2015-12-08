package service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.braintreegateway.*;

@Controller
public class CheckoutController {

    BraintreeConfiguration config = new BraintreeConfiguration();

    @RequestMapping("/checkout")
    public String checkout(Model model) {
        BraintreeGateway gateway = config.gateway();
        String clientToken = gateway.clientToken().generate();
        model.addAttribute("clientToken", clientToken);
        return "checkout";
    }
}

package com.soumya.Project.Management.controller;

import com.razorpay.RazorpayClient;
import com.soumya.Project.Management.enums.PlanType;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.service.UserService;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Value("${razorpay.api.key}")
    private String razorpayKey;
    @Value("${razorpay.api.secret}")
    private String razorpaySecret;
    @Autowired
    private UserService userService;

    @PostMapping("/{planType}")
    public ResponseEntity<?> createPaymentLink(
            @RequestHeader("Authorization") String jwt,
            @PathVariable PlanType planType
            ) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        int amount = 399*100; // paisa to rupee
        if (planType.equals(PlanType.ANNUAL)){
            amount *= 12; // 1 year
            amount = (int) (amount * (0.75)); // 25% discount
        }

        try {
            RazorpayClient client = new RazorpayClient(razorpayKey,razorpaySecret);
            JSONObject paymentResponse = new JSONObject();
            paymentResponse.put("amount",amount);
            paymentResponse.put("currency","INR");

            JSONObject customer = new JSONObject();
            String customerName = user.getFirstName()+" "+user.getLastName();
            customer.put("name",customerName);
            customer.put("email",user.getEmail());

            paymentResponse.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("email",true);

            paymentResponse.put("notify",notify);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

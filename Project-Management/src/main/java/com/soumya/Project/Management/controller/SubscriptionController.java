package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.enums.PlanType;
import com.soumya.Project.Management.model.Subscription;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.service.SubscriptionService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserSubscription(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Subscription usersSubscription = subscriptionService.getUsersSubscription(user.getId());
        return new ResponseEntity<>(usersSubscription, HttpStatus.OK);
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<?> upgradeUserSubscription(
            @RequestHeader("Authorization") String jwt,
            @RequestParam PlanType planType
            ) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Subscription subscription = subscriptionService.upgradeSubscription(user.getId(), planType);
        return new ResponseEntity<>(subscription,HttpStatus.ACCEPTED);
    }
}

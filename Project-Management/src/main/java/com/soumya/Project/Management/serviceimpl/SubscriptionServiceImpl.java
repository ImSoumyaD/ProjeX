package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.enums.PlanType;
import com.soumya.Project.Management.model.Subscription;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.SubscriptionRepository;
import com.soumya.Project.Management.service.SubscriptionService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
//    @Autowired
//    private UserService userService;

    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStart(LocalDate.now());
        subscription.setSubscriptionEnd(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUsersSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        if (!isValid(subscription)){ // if subscription is expired
            subscription.setPlanType(PlanType.FREE);
            subscription.setSubscriptionStart(LocalDate.now());
            subscription.setSubscriptionEnd(LocalDate.now().plusMonths(12));
            subscriptionRepository.save(subscription);
        }
        return subscription;
    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        subscription.setPlanType(planType);
        subscription.setSubscriptionStart(LocalDate.now());
        if (planType.equals(PlanType.ANNUAL)){
            subscription.setSubscriptionEnd(LocalDate.now().plusMonths(12));
        }
        else if (planType.equals(PlanType.MONTHLY)){ // we can use only else
            subscription.setSubscriptionEnd(LocalDate.now().plusMonths(1));
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        if (subscription.getPlanType().equals(PlanType.FREE)){
            return true;
        }
        LocalDate endDate = subscription.getSubscriptionEnd();
        return endDate.isAfter(LocalDate.now()) || endDate.isEqual(LocalDate.now());
    }
}

package com.soumya.Project.Management.service;

import com.soumya.Project.Management.enums.PlanType;
import com.soumya.Project.Management.model.Subscription;
import com.soumya.Project.Management.model.User;

public interface SubscriptionService {
    Subscription createSubscription(User user);
    Subscription getUsersSubscription(Long userId);
    Subscription upgradeSubscription(Long userId, PlanType planType);
    boolean isValid(Subscription subscription);
}

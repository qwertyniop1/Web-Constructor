package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;

public class OnSiteCreateEvent extends RewardEvent{

    public OnSiteCreateEvent(User user, int quantity) {
        super(user, RewardType.SITE, quantity);
    }
}

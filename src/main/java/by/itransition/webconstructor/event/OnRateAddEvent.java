package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;

public class OnRateAddEvent extends RewardEvent{

    public OnRateAddEvent(User user, int value) {
        super(user, RewardType.RATE, value);
    }
}

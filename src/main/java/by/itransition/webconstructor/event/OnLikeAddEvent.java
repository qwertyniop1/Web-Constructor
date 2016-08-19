package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;

public class OnLikeAddEvent extends RewardEvent {

    public OnLikeAddEvent(User user, int value) {
        super(user, RewardType.LIKE, value);
    }
}

package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;

public class OnCommentAddEvent extends RewardEvent{

    public OnCommentAddEvent(User user, int value) {
        super(user, RewardType.COMMENT, value);
    }
}

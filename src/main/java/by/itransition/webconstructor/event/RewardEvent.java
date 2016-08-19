package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;
import org.springframework.context.ApplicationEvent;

public class RewardEvent extends ApplicationEvent{

    private final User user;

    private final RewardType type;

    private final int value;

    public RewardEvent(User user, RewardType type, int value) {
        super(user);
        this.user = user;
        this.value = value;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public int getValue() {
        return value;
    }

    public RewardType getType() {
        return type;
    }

}

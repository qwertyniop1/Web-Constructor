package by.itransition.webconstructor.event;

import by.itransition.webconstructor.domain.RewardType;
import by.itransition.webconstructor.domain.User;
import by.itransition.webconstructor.repository.RewardRepository;
import by.itransition.webconstructor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class RewardListener implements ApplicationListener<RewardEvent> {

    private static final Map<RewardType, List<Integer>> levels;
    static {
        Map<RewardType, List<Integer>> tmpMap = new HashMap<>();
        tmpMap.put(RewardType.SITE, new ArrayList<>(Arrays.asList(5, 25, 50, 0)));
        tmpMap.put(RewardType.COMMENT, new ArrayList<>(Arrays.asList(20, 50, 100, 0)));
        tmpMap.put(RewardType.RATE, new ArrayList<>(Arrays.asList(10, 50, 100, 0)));
        tmpMap.put(RewardType.LIKE, new ArrayList<>(Arrays.asList(20, 100, 200, 0)));
        levels = Collections.unmodifiableMap(tmpMap);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Override
    public void onApplicationEvent(RewardEvent rewardEvent) {
        int value = rewardEvent.getValue();
        RewardType type = rewardEvent.getType();
        List<Integer> currentLevels = levels.get(type);
        if (currentLevels.contains(value)) {
            User user = rewardEvent.getUser();
            user.removeReward(rewardRepository.findByTypeAndRequirement(type, value));
            user.addReward(rewardRepository.findByTypeAndRequirement(type, currentLevels.get(currentLevels.indexOf(value) + 1)));
            userRepository.save(user);
        }
    }
}

package by.itransition.webconstructor.repository;

import by.itransition.webconstructor.domain.Reward;
import by.itransition.webconstructor.domain.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByTypeOrderByRequirementAsc(RewardType type);

    Reward findByTypeAndRequirement(RewardType type, int requirement);

}

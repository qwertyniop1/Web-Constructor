package by.itransition.webconstructor.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rewards")
public class Reward implements Comparable<Reward>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int requirement;

    @Enumerated(EnumType.STRING)
    private RewardType type;

    private String icon;

    @Override
    public int compareTo(Reward o) {
        return (int) (this.id - o.getId());
    }
}

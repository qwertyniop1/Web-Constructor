package by.itransition.webconstructor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"pages", "rates"})
@ToString(exclude = {"pages", "rates"})
@Entity
@Table(name = "sites")
public class Site implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "New site";

    @Lob
    private String description;

    private String logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Page> pages = new HashSet<>(0);

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rate> rates = new HashSet<>(0);

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuOrientation menuOrientation;

    @CreationTimestamp
    private Date creationDate;

    @UpdateTimestamp
    private Date updateDate;

//    public void addRate(Rate rate) {
//        this.rates.add(rate);
//    }

}

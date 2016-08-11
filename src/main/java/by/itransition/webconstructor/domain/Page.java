package by.itransition.webconstructor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"elements", "comments"})
@ToString(exclude = {"elements", "comments"})
@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "New page";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(name = "layout")
    private int layoutId = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Element> elements = new HashSet<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>(0);

    public void addElement(Element element) {
        this.elements.add(element);
    }

    public void clearElements() {
        this.elements.clear();
    }

    @CreationTimestamp
    private Date creationDate;

    @UpdateTimestamp
    private Date updateDate;

}

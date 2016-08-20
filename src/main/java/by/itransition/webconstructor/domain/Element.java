package by.itransition.webconstructor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Data
@Entity
@Table(name = "elements")
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    private int location;

    private int width;

    private int height;

    private String url;

    @Field
    @Analyzer(definition = "customanalyzer")
    @Lob
    private String text;

    private boolean autoplay;

    private boolean videoLoop;

    private boolean chart;

}

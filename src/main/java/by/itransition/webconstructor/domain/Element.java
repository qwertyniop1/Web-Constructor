//package by.itransition.webconstructor.domain;
//
//import lombok.Data;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(name = "elements")
//public class Element {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private Type type;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "page_id", nullable = false)
//    private Page page;
//
//}

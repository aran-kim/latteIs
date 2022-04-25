package site.LatteIs.latteIs.web.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MBTI implements Serializable {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int Q1;
    private int Q2;
    private int Q3;
    private int Q4;
    private int Q5;
    private int Q6;
    private int Q7;
    private int Q8;
    private int Q9;
    private int Q10;
    private int Q11;
    private int Q12;
    private String mbti;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

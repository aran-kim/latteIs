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

    private int Q1; // I/E
    private int Q2; // I/E
    private int Q3; // I/E
    private int Q4; // S/N
    private int Q5; // S/N
    private int Q6; // S/N
    private int Q7; // T/F
    private int Q8; // T/F
    private int Q9; // T/F
    private int Q10; // J/P
    private int Q11; // J/P
    private int Q12; // J/P

    private String mbti;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

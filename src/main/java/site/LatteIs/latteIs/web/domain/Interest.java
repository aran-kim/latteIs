package site.LatteIs.latteIs.web.domain;

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
public class Interest implements Serializable {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sex;
    private String birthday;
    private String location;
    private String height;
    private String religion;
    private String amount_of_alcohol;
    private String smoking;
    private String job;
    private String character;
    private String hobby;
    private String friend_style;
    private String MBTI;

    @ManyToOne
    @JoinColumn(name = "uesr_id")
    private User user;
}

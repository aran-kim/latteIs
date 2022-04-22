package site.LatteIs.latteIs.web.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    // OAuth를 위해 구성한 추가 필드 2개
    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;
    private String nickName;
    private int init;
}

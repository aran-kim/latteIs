package site.LatteIs.latteIs.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.LatteIs.latteIs.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column
    private String picture;

    @Enumerated(EnumType.STRING) //JPA로 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지를 결정(숫자로 저장하면 값이 무슨 코드인지 모름)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(Long id, String name, String email, String picture, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }
}

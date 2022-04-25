package site.LatteIs.latteIs.chat;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import site.LatteIs.latteIs.web.domain.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type; // 메시지 타입
    private String message; // 메시지

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom; // 방번호

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 메시지 보낸사람

}

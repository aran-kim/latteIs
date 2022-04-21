package site.LatteIs.latteIs.chat;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.socket.WebSocketSession;
import site.LatteIs.latteIs.chat.ChatMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom implements Serializable {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String roomname;
    private String masterusername;
    @CreationTimestamp
    private Timestamp createDate;
}
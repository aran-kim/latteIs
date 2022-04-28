package site.LatteIs.latteIs.chat;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;


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
    private int maxnumber;
    private int currentnumber;
    @CreationTimestamp
    private Timestamp createDate;
}
package site.LatteIs.latteIs.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "select * from chat_message where room_id = ?1 order by create_date", nativeQuery = true)
    List<ChatMessage> findAllMessageByRoomId(int room_id);

}

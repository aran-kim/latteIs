package site.LatteIs.latteIs.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "select * from chat_message where room_id = ?1 order by create_date", nativeQuery = true)
    List<ChatMessage> findAllMessageByRoomId(int room_id);

    @Query(value = "select * from chat_message where create_date >= (select create_date from chat_message where type = \"ENTER\" and room_id = ?1 and user_id = ?2) order by create_date desc;", nativeQuery = true)
    List<ChatMessage> findAllMessageByRoomIdandUserId(long room_id, int user_id);

    @Query(value = "select * from chat_message where type = \"ENTER\" and room_id = ?1 and user_id = ?2", nativeQuery = true)
    ChatMessage findMessageByRoomIdandUserId(long room_id, int user_id);

    @Query(value = "select * from chat_message where type = \"ENTER\" and user_id = ?1", nativeQuery = true)
    List<ChatMessage> findMessageByUserId(int user_id);

    @Query(value = "select * from chat_message where type = \"ENTER\" and room_id = ?1", nativeQuery = true)
    List<ChatMessage> findMessageByRoomId(long user_id);
}

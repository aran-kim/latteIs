package site.LatteIs.latteIs.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByMasterusername(String Masterusername);
    ChatRoom findById(long id);

    @Query(value = "select * from chat_room where roomname like ?;" , nativeQuery = true)
    List<ChatRoom> findBySearchkey(String key);
}

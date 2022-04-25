package site.LatteIs.latteIs.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByMasterusername(String Masterusername);
    ChatRoom findById(long id);
}

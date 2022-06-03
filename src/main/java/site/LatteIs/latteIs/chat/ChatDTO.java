package site.LatteIs.latteIs.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {

    private String user;
    private String type;
    private String chatRoom;
    private String message;
}

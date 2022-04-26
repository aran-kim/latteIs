package site.LatteIs.latteIs;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.LatteIs.latteIs.chat.ChatMessage;
import site.LatteIs.latteIs.chat.ChatMessageRepository;
import site.LatteIs.latteIs.chat.ChatRoomRepository;
import site.LatteIs.latteIs.web.domain.entity.Follower;
import site.LatteIs.latteIs.web.domain.entity.Post;
import site.LatteIs.latteIs.web.domain.repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SpringBootTest
class LatteIsApplicationTests {

	@Autowired
	ChatMessageRepository chatMessageRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowerRepository followerRepository;
	@Autowired
	FollowingRepository followingRepository;
	@Autowired
	BlacklistRepository blacklistRepository;

	@Test
	void contextLoads() throws CoolsmsException {
		String key = "NCSAG57JXAJ6Q2EE";
		String secret = "HI4QIK7YXNFPXC4TR8FJN3Q3O8KZVF5O";
		Message coolsms = new Message(key, secret);

		Random rand = new Random();
		String numStr = "";
		for(int i = 0; i < 4; i++){
			String randChar = Integer.toString(rand.nextInt(10));
			numStr += randChar;
		}
		System.out.println("수신자 번호: " + "01050321598");
		System.out.println("인증 번호: " + numStr);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", "01050321598");    // 수신전화번호
		params.put("from", "01087756263");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
		params.put("type", "SMS");
		params.put("text", "라때는... 휴대폰인증 테스트 : 인증번호는" + "["+numStr+"]" + "입니다."); // 문자 내용

		coolsms.send(params);
	}

	@Test
	public void querytest(){
		Post post1 = postRepository.findById(1);
		System.out.println("findByID: " + post1);
/*		Post post2 = postRepository.testQuery(1);
		System.out.println("testQuery: " + post2);
		User user2 = post2.getUser();
		int user2id = user2.getId();
		Post post = postRepository.findUsername(32);
		System.out.println("findUsername: " + post);
		User user = userRepository.findById(user2id);
		System.out.println("user : " + user);*/
	}

	@Test
	public void 엔터체크(){
	}


/*	@Test
	public void ffbtest(){
		int user_id = 6;
		Follower follower = followerRepository.findByUserId(user_id);
		System.out.println(follower);
		System.out.println(followerRepository.countFollowerByUserId(user_id));


	}*/
}

package site.LatteIs.latteIs;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.LatteIs.latteIs.web.domain.entity.*;
import site.LatteIs.latteIs.web.domain.repository.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SpringBootTest
class LatteIsApplicationTests {

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
	@Autowired
	InterestRepository interestRepository;
	@Autowired
	MBTIRepository mbtiRepository;
	@Autowired
	BoardRepository boardRepository;

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
	public void strTest(){
		int[] num = {1, 1, 2, 0};
		//num[1] = 1; num[2] = 1; num[3] = 2; num[4] = 0;
		String[] str = {"I_E", "S_N", "T_F", "J_P"};
		//str[1] = "I_E"; str[2] = "S_N"; str[3] = "T_F"; str[4] = "J_P";
		for (int i = 0; i < 4; i++)
			System.out.print("str 출력 테스트 : " + str[i]);
		System.out.println();
		for(int i = 0; i < 4; i++){
			if(num[i] > 1)
				str[i] = str[i].substring(str[i].lastIndexOf("_") + 1);
			else
				str[i] = str[i].substring(0,1);
		}
		for (int i = 0; i < 4; i++)
			System.out.print(str[i]);
		System.out.println();
		String tmp;
		tmp = String.join("", str);
		System.out.println(tmp);

	}

	@Test
	public void postTest(){

		User userinfo = userRepository.findByUsername("test13");
		Interest userInterest = interestRepository.findByUserId(userinfo.getId());

		//String userMBTI = userInterest.getMbti();
		String userMBTI = "ENTJ";
		System.out.println("사용자의 MBTI : " + userMBTI);
		String good;
		if(userMBTI.equals("ENFJ") || userMBTI.equals("INTJ")){
			if(userMBTI.equals("ENFJ"))
				good = "INTJ";
			else
				good = "ENFJ";
		}
		else if(userMBTI.equals("ENTJ") || userMBTI.equals("INFJ")){
			if(userMBTI.equals("ENTJ"))
				good = "INFJ";
			else
				good = "ENTJ";
		}
		else if(userMBTI.equals("ESTJ") || userMBTI.equals("ISFJ")){
			if(userMBTI.equals("ESTJ"))
				good = "ISFJ";
			else
				good = "ESTJ";
		}
		else if(userMBTI.equals("ESFJ") || userMBTI.equals("ISTJ")){
			if(userMBTI.equals("ESFJ"))
				good = "ISTJ";
			else
				good = "ESFJ";
		}
		else if(userMBTI.equals("ENFP") || userMBTI.equals("ISFP")){
			if(userMBTI.equals("ENFP"))
				good = "ISFP";
			else
				good = "ENFP";
		}
		else if(userMBTI.equals("ENTP") || userMBTI.equals("ISTP")){
			if(userMBTI.equals("ENTP"))
				good = "ISTP";
			else
				good = "ENTP";
		}
		else if(userMBTI.equals("ESTP") || userMBTI.equals("INTP")){
			if(userMBTI.equals("ESTP"))
				good = "INTP";
			else
				good = "ESTP";
		}
		else if(userMBTI.equals("ESFP") || userMBTI.equals("INFP")){
			if(userMBTI.equals("ESFP"))
				good = "INFP";
			else
				good = "ESFP";
		}
		else
			good = null;
		System.out.println("good : " + good);
		good = "ESTJ"; //테스트용

		List<Interest> userList = interestRepository.findAllByMbtiandUniversity(good, userInterest.getUniversity(), userInterest.getUser().getId());

		String userinfoFrinedStyle = userInterest.getFriend_style();
		String[] arr = {"", "", ""};
		System.out.println(userinfoFrinedStyle.length());
		if(userinfoFrinedStyle.contains(",")){
			System.out.println(userinfoFrinedStyle.split(",").length + "개");
			int start = 0, end = 0;
			for(int k = 0; k < userinfoFrinedStyle.split(",").length; k++){
				end = userinfoFrinedStyle.indexOf(",", start);
				if(end == -1)
					end = userinfoFrinedStyle.length();
				arr[k] = userinfoFrinedStyle.substring(start, end);
				arr[k] = arr[k].replaceAll("[^a-z]", "");
				start = end + 1;
				System.out.println("arr[" + k + "]: " + arr[k]);
			}
		}
		else{
			System.out.println("1개");
			arr[0] = userinfoFrinedStyle.replaceAll("[^a-z]", "");
			for(int i = 0; i < arr.length; i++)
				System.out.println(arr[i]);
		}
		String userFriendStyle = "";
		int cnt = 0;
		for(int i = 0; i < userList.size(); i++){
			userFriendStyle = userList.get(i).getFriend_style();
			System.out.println(userFriendStyle);
			cnt = 0;
			for(int j = 0; j < arr.length; j++){
				if(userFriendStyle.contains(arr[j]) && !arr[j].equals("")){
					//model.addattributes("number" + i, i);
					System.out.println(arr[j] + " 겹침");
					cnt++;
				}
			}
			if (cnt == 0)
				System.out.println(userList.get(i).getUser().getUsername() + "과는 좋아하는 스타일이 같지 않습니다.");
			else
				System.out.println(userList.get(i).getUser().getUsername() + "과는 좋아하는 스타일이 같습니다.");
		}
		System.out.println("userList : " + userList);
	}

	@Test
	public void testage(){
		Interest interest = interestRepository.findByUserId(6);
		System.out.println(interest.getBirthday());

		int year = Integer.parseInt(interest.getBirthday().substring(0, 4));
		int month = Integer.parseInt(interest.getBirthday().substring(5, 7));
		int day = Integer.parseInt(interest.getBirthday().substring(8, 10));
		int cyear = Calendar.getInstance().get(Calendar.YEAR);

		int age = cyear - year + 1;
		System.out.println(age);
	}

	@Test
	public void boardTest(){
		List<Board> boardList = boardRepository.findAll();
		List<Post> postList0 = postRepository.findThreeByBoardId(boardList.get(0).getId());
		List<Post> postList1 = postRepository.findThreeByBoardId(boardList.get(1).getId());
		List<Post> postList2 = postRepository.findThreeByBoardId(boardList.get(2).getId());
		System.out.println(postList1);

		List<Post> postList = postRepository.findAllThreePost();
		int cnt = 1;
		for(int i = 1; i <= 4; i++){
			for(cnt = cnt-1; cnt < postList.size(); cnt++){
				if(postList.get(cnt).getBoard().getId() == i){
					System.out.println("post : " + postList.get(cnt).getBoard());
				}
			}
		}
	}

	@Test
	public void interestTest(){
		Interest interest = interestRepository.findByUserId(33);
		System.out.println(interest.getHobby());
		String[] arr = {"", "", ""};
		if(interest.getHobby().split(",").length == 1){
			arr[0] =
			arr[1] = "";
			arr[2] = "";
		}
		arr = interest.getHobby().split(",");
		System.out.println(arr[1]);
		System.out.println(arr[2]);

		for(int i = 0; i < arr.length; i++){

			arr[i] = arr[i].replaceAll("[^a-z]","");
			if(!arr[i].equals(""))
				arr[i] = "%" + arr[i] + "%";
			System.out.println(arr[i]);
		}
		List<Interest> userList = interestRepository.findAllByEqualInterest(arr[0], arr[1], arr[2], interest.getUniversity(), interest.getUser().getId());
		System.out.println(userList);

	}

	@Test
	public void splitTest(){

		String name = "안녕 하세요,]";
		String[] regdx = {",", "\\[", "\\]"};
		for(int i = 0; i < regdx.length; i++)
			name = name.replaceAll(regdx[i], "*");
		System.out.println(name);


		Interest interest = interestRepository.findByUserId(6);
		String hobby = interest.getHobby();
		System.out.println(hobby);
		String[] arr = {"", "", ""};
		if(hobby.contains(",")){
			System.out.println(hobby.split(",").length + "개");
			int start = 0, end = 0;
			for(int i = 0; i < hobby.split(",").length; i++){
				end = hobby.indexOf(",", start);
				if(end == -1)
					end = hobby.length();
				arr[i] = hobby.substring(start, end);
				arr[i] = arr[i].replaceAll("[^a-z]","");
				start = end + 1;
				System.out.println("arr["+ i + "]: " + arr[i]);
			}
		}
		else{
			System.out.println("1개");
			arr[0] = hobby.replaceAll("[^a-z]","");
			for(int i = 0; i < arr.length; i ++)
				System.out.println(arr[i]);
		}
		List<Interest> userList = interestRepository.findAllByEqualInterest(arr[0], arr[1], arr[2], interest.getUniversity(), interest.getUser().getId());
		System.out.println(userList);

	}
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	static Connection connection;
	static ArrayList<Integer> grade;
	public static void main(String[] args) {
		// 1단계 : 64.2222 / 63.1606 / 99.84 -> 232.7314
		// 2단계 : 55.925  / 58.258  / 99.876 -> 216.92
		// 총합 : 449.65
		//내가 선택한 실력이 비슷한데(a-b 가 낮음) 시간이 3 -> 실제로는 차이 엄청남 -> 가중치 적게줘야함
		//내가 선택한 실력이 비슷한데(a-b 가 낮음) 시간이 높음 -> 실제로도 차이 x -> 가중치 높게
		//내가 선택한 실력이 차이나는데(a-b 가 높음) 시간이 3 -> 실제로도 차이남 -> 가중치 적게
		//내가 선택한 실력이 차이나는데(a-b 가 높음) 시간이 높음 -> 실제로는 비슷함 차이x ->가중치 높게
		
		//두개의 차이 / 시간 -> 
		// TODO Auto-generated method stub
		connection = Connection.getInstance();
		String key = connection.startAPI(2);
		connection.setAUTHKEY(key);
		int time =0 ;
		while(time<596) {
			time++;
			//전략 -> A와 B가 싸웠을때, 진사람의 가중치만큼 점수추가.
			//쎈사람 이기면 많이오르고 약한사람 이기면 적게오름.
			grade = new ArrayList<>();
			grade.add(0);
			ArrayList<WatingUser> watingUser = watingLine(connection.watingLineAPI());
			HashMap<Integer, Integer> users = users(connection.userInfoAPI());
			ArrayList<GameResult> result = gameResult(connection.gameResult());
			//유저 등급 세팅
			for (Entry<Integer, Integer> entrySet : users.entrySet()) {
				if(entrySet.getValue()!=0) grade.add(entrySet.getValue());
				else grade.add(5000);
			}
			//결과 반영하고
			updateGrade(result, grade);
			//웨이팅 유저에 등급 부여해주고
			setGrade(grade, watingUser);
			//sort -> 등급순
			Collections.sort(watingUser);
//			for(int i=0;i<watingUser.size();i++) {
//				System.out.print("("+watingUser.get(i).id+" "+watingUser.get(i).weight+"),");
//			}
			connection.changeGradeAPI(changeGradeToJSONObject(grade));
			//매칭 해주고
			ArrayList<int[]> match = setMatch(watingUser);
			JSONObject matchJSON = matchToJSONObject(match);
			connection.matchAPI(matchJSON);
		}
		//스코어 뽑기
		JSONObject score = connection.scoreAPI();
		System.out.println("status : "+score.getString("status")+ 
				"\n efficiency_score :" +score.getString("efficiency_score")+
				"\n accuracy_score1 :" +score.getString("accuracy_score1")+
				"\n accuracy_score2 :" +score.getString("accuracy_score2")+
				"\n score :" +score.getString("score"));
	}
	
	
	public static ArrayList<WatingUser> watingLine(JSONObject jsonObject){
		//API로 뽑아낸 기다리는 순서 arrayList로 변환
		ArrayList watingUser = new ArrayList<>();
		JSONArray array = jsonObject.getJSONArray("waiting_line");
		for(int i=0;i<array.length();i++) {
			int id = array.getJSONObject(i).getInt("id");
			int from = array.getJSONObject(i).getInt("from");
			WatingUser user = new WatingUser(id,from);
			watingUser.add(user);
		}
		return watingUser;
	}
	
	public static HashMap<Integer,Integer> users(JSONObject jsonObject){
		//유저정보 HashMap으로 변환
		HashMap<Integer,Integer> hashMap = new HashMap<>();
		JSONArray array = jsonObject.getJSONArray("user_info");
		for(int i=0;i<array.length();i++) {
			int id = array.getJSONObject(i).getInt("id");
			int grade = array.getJSONObject(i).getInt("grade");
			hashMap.put(id, grade);
		}
		return hashMap;
	}
	
	public static void setGrade(ArrayList<Integer> grade, ArrayList<WatingUser> watingLine) {
		//점수표 작성.
//		int min = Integer.MAX_VALUE;
//		for (Entry<Integer, Integer> entrySet : user.entrySet()) {
//			min = Math.min(entrySet.getValue(), min);
//		}
		for(int i=0;i<watingLine.size();i++) {
			WatingUser watingUser = watingLine.get(i);
			int g = grade.get(watingUser.getId());
			
//			grade = grade/min;
//			grade = grade
			watingLine.get(i).setWeight(g);
		}
	}
	
	public static ArrayList<GameResult> gameResult(JSONObject jsonObject){
		//게임결과 ArrayList로 변환.
		ArrayList<GameResult> results = new ArrayList<>();
		JSONArray array = jsonObject.getJSONArray("game_result");
		
		for(int i=0;i<array.length();i++) {
			int win = array.getJSONObject(i).getInt("win");
			int lose = array.getJSONObject(i).getInt("lose");
			int taken = array.getJSONObject(i).getInt("taken");
			GameResult gameResult = new GameResult(win, lose, taken);
			results.add(gameResult);
		}
		return results;
	}
	
	public static void updateGrade(ArrayList<GameResult> results, ArrayList<Integer> grade) {
		//게임결과 반영
		for(GameResult result : results) {
			int win = result.win;
			int lose = result.lose;
			int taken = result.taken;
			//걸린시간이 높을수록 둘다 비슷
			//걸린시간이 짧을수록 한놈은 잘하고 한놈은 못함.
			//3부터 40이니까
			//3-10, 10-20,20-30,30-40 나눠서
			//차이개남     차이많이   차이많이조금 차이비슷
			//6 0     1 5      2 4    3 3
			if(grade.get(win) == grade.get(lose)) {
				grade.set(win, grade.get(win)+20);
				grade.set(lose, grade.get(lose)-20);
				continue;
			}
			
			if(taken>=3  && taken<10) {
				if(grade.get(win) > grade.get(lose)) {
					grade.set(win, grade.get(win)+25);
					grade.set(lose, grade.get(lose)-20);
				}else {
					grade.set(win, grade.get(win)+15);
					grade.set(lose, grade.get(lose)-10);
				}
//				grade.set(win, grade.get(win)+1);
			}else if(taken>=10 && taken<20) {
				if(grade.get(win) > grade.get(lose)) {
					grade.set(win, grade.get(win)+23);
					grade.set(lose, grade.get(lose)-18);
				}else {
					grade.set(win, grade.get(win)+17);
					grade.set(lose, grade.get(lose)-12);
				}
			}else if(taken>=20 && taken <30) {
				if(grade.get(win) > grade.get(lose)) {
					grade.set(win, grade.get(win)+21);
					grade.set(lose, grade.get(lose)-16);
				}else {
					grade.set(win, grade.get(win)+19);
					grade.set(lose, grade.get(lose)-14);
				}
			}else {
				if(grade.get(win) > grade.get(lose)) {
					grade.set(win, grade.get(win)+20);
					grade.set(lose, grade.get(lose)-18);
				}else {
					grade.set(win, grade.get(win)+20);
					grade.set(lose, grade.get(lose)-18);
				}
			}
	
//			int weight = 
//			grade.set(win, grade.get(win)+taken);
		}
	}
	
	public static ArrayList<int[]> setMatch(ArrayList<WatingUser> watingUser) {
		//게임상대 전송. 가중치순으로 첫번째,두번째 .. 세번째,네번째 ..이렇게 진행
		ArrayList<int[]> pairs = new ArrayList<>();
		for(int i=0;i<watingUser.size()-1;i=i+2) {
			int[] pair = new int[2];
			pair[0] = watingUser.get(i).id;
			pair[1] = watingUser.get(i+1).id;
			pairs.add(pair);
		}
		return pairs;
	}
	
	public static JSONObject matchToJSONObject(ArrayList<int[]> match) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pairs", match);
//		System.out.println(jsonObject);
		return jsonObject;
	}
	
	public static JSONArray changeGradeToJSONObject(ArrayList<Integer> grade) {
		//게임결과 반영한거를 업데이트
		JSONArray jsonArray = new JSONArray();
		for(int i=1;i<grade.size();i++) {
			JSONObject jo = new JSONObject();
			jo.put("id", i);
			jo.put("grade", grade.get(i));
			jsonArray.put(jo);
		}
		System.out.println(jsonArray);
		return jsonArray;
	}
}

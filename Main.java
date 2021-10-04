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
		// 1�ܰ� : 64.2222 / 63.1606 / 99.84 -> 232.7314
		// 2�ܰ� : 55.925  / 58.258  / 99.876 -> 216.92
		// ���� : 449.65
		//���� ������ �Ƿ��� ����ѵ�(a-b �� ����) �ð��� 3 -> �����δ� ���� ��û�� -> ����ġ ���������
		//���� ������ �Ƿ��� ����ѵ�(a-b �� ����) �ð��� ���� -> �����ε� ���� x -> ����ġ ����
		//���� ������ �Ƿ��� ���̳��µ�(a-b �� ����) �ð��� 3 -> �����ε� ���̳� -> ����ġ ����
		//���� ������ �Ƿ��� ���̳��µ�(a-b �� ����) �ð��� ���� -> �����δ� ����� ����x ->����ġ ����
		
		//�ΰ��� ���� / �ð� -> 
		// TODO Auto-generated method stub
		connection = Connection.getInstance();
		String key = connection.startAPI(2);
		connection.setAUTHKEY(key);
		int time =0 ;
		while(time<596) {
			time++;
			//���� -> A�� B�� �ο�����, ������� ����ġ��ŭ �����߰�.
			//���� �̱�� ���̿����� ���ѻ�� �̱�� ���Կ���.
			grade = new ArrayList<>();
			grade.add(0);
			ArrayList<WatingUser> watingUser = watingLine(connection.watingLineAPI());
			HashMap<Integer, Integer> users = users(connection.userInfoAPI());
			ArrayList<GameResult> result = gameResult(connection.gameResult());
			//���� ��� ����
			for (Entry<Integer, Integer> entrySet : users.entrySet()) {
				if(entrySet.getValue()!=0) grade.add(entrySet.getValue());
				else grade.add(5000);
			}
			//��� �ݿ��ϰ�
			updateGrade(result, grade);
			//������ ������ ��� �ο����ְ�
			setGrade(grade, watingUser);
			//sort -> ��޼�
			Collections.sort(watingUser);
//			for(int i=0;i<watingUser.size();i++) {
//				System.out.print("("+watingUser.get(i).id+" "+watingUser.get(i).weight+"),");
//			}
			connection.changeGradeAPI(changeGradeToJSONObject(grade));
			//��Ī ���ְ�
			ArrayList<int[]> match = setMatch(watingUser);
			JSONObject matchJSON = matchToJSONObject(match);
			connection.matchAPI(matchJSON);
		}
		//���ھ� �̱�
		JSONObject score = connection.scoreAPI();
		System.out.println("status : "+score.getString("status")+ 
				"\n efficiency_score :" +score.getString("efficiency_score")+
				"\n accuracy_score1 :" +score.getString("accuracy_score1")+
				"\n accuracy_score2 :" +score.getString("accuracy_score2")+
				"\n score :" +score.getString("score"));
	}
	
	
	public static ArrayList<WatingUser> watingLine(JSONObject jsonObject){
		//API�� �̾Ƴ� ��ٸ��� ���� arrayList�� ��ȯ
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
		//�������� HashMap���� ��ȯ
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
		//����ǥ �ۼ�.
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
		//���Ӱ�� ArrayList�� ��ȯ.
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
		//���Ӱ�� �ݿ�
		for(GameResult result : results) {
			int win = result.win;
			int lose = result.lose;
			int taken = result.taken;
			//�ɸ��ð��� �������� �Ѵ� ���
			//�ɸ��ð��� ª������ �ѳ��� ���ϰ� �ѳ��� ����.
			//3���� 40�̴ϱ�
			//3-10, 10-20,20-30,30-40 ������
			//���̰���     ���̸���   ���̸������� ���̺��
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
		//���ӻ�� ����. ����ġ������ ù��°,�ι�° .. ����°,�׹�° ..�̷��� ����
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
		//���Ӱ�� �ݿ��ѰŸ� ������Ʈ
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

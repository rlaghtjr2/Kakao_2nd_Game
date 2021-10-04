import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Connection {
	static final String X_AUTH_TOKEN = "";
	static final String BASE_URL = "";
	static String AUTH_KEY ;
	static Connection instance = new Connection();
	
	static Connection getInstance() {
		return instance;
	}
	
	static void setAUTHKEY(String key) {
		AUTH_KEY = key;
	}
	
	static String getKey() {
		return AUTH_KEY;
	}
	static String startAPI(int stage) {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/start");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("X-Auth-Token", X_AUTH_TOKEN);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        //commands라는 JSONArray를 담을 JSONObject 생성
	        JSONObject commands = new JSONObject();
	        commands.put("problem", stage);
	        //request에 쓰기
	        bw.write(commands.toString());
	        bw.flush();
	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음 start");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson.getString("auth_key");
	}
	
	static JSONObject watingLineAPI() {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/waiting_line");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
//	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        //commands라는 JSONArray를 담을 JSONObject 생성
//	        JSONObject commands = new JSONObject();
//	        //request에 쓰기
//	        bw.write(commands.toString());
//	        bw.flush();
//	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음 waiting");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
//	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
	
	static JSONObject gameResult() {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/game_result");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
//	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//	        //commands라는 JSONArray를 담을 JSONObject 생성
//	        JSONObject commands = new JSONObject();
//	        //request에 쓰기
//	        bw.write(commands.toString());
//	        bw.flush();
//	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음 gameResult ");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
//	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
	
	static JSONObject userInfoAPI() {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/user_info");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
//	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        //commands라는 JSONArray를 담을 JSONObject 생성
//	        JSONObject commands = new JSONObject();
//	        //request에 쓰기
//	        bw.write(commands.toString());
//	        bw.flush();
//	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음 userInfo ");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
//	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
	
	static JSONObject matchAPI(JSONObject pairs) {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/match");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        //commands라는 JSONArray를 담을 JSONObject 생성
	        JSONObject commands = new JSONObject();
	        //request에 쓰기
	        commands.put("pairs", pairs);
	        bw.write(pairs.toString());
	        bw.flush();
	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음 match");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
	
	static JSONObject changeGradeAPI(JSONArray command) {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/change_grade");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        //commands라는 JSONArray를 담을 JSONObject 생성
	        JSONObject commands = new JSONObject();
	        //request에 쓰기
//	        System.out.println(command);
	        commands.put("commands", command);
//	        System.out.println(commands);
	        bw.write(commands.toString());
	        bw.flush();
	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음  changeGrade");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
//	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
	
	static JSONObject scoreAPI() {
	    HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL(BASE_URL + "/score");

	        conn = (HttpURLConnection) url.openConnection();
	        //Request 형식 설정
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", AUTH_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");

	        //request에 JSON data 준비
	        conn.setDoOutput(true);
//	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//	        //commands라는 JSONArray를 담을 JSONObject 생성
//	        JSONObject commands = new JSONObject();
//	        //request에 쓰기
//	        bw.write(commands.toString());
//	        bw.flush();
//	        bw.close();
	        
	        //보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 400) {
	            System.out.println("400:: 해당 명령을 실행할 수 없음  Score");
	        } else if (responseCode == 401) {
	            System.out.println("401:: X-Auth-Token Header가 잘못됨");
	        } else if (responseCode == 500) {
	            System.out.println("500:: 서버 에러, 문의 필요");
	        } else { // 성공 후 응답 JSON 데이터받기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
//	            System.out.println(sb);
	            responseJson = new JSONObject(sb.toString());
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return responseJson;
	}
}

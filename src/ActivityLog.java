import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

public class ActivityLog {
    // 운동 및 식단 기록을 저장할 내부 클래스
    static class Activity {
        String type; // 운동 또는 식단
        String name; // 활동 이름
        int calories; // 소모/섭취 칼로리
        LocalDate date; // 기록 날짜

        public Activity(String type, String name, int calories, LocalDate date) {
            this.type = type;
            this.name = name;
            this.calories = calories;
            this.date = date;
        }

        @Override
        public String toString() {
            return String.format("%s: %s, %d칼로리, 날짜: %s", type, name, calories, date);
        }
    }

    // 운동 및 식단 데이터를 저장할 리스트
    private List<Activity> activities;

    // 생성자
    public ActivityLog() {
        this.activities = new ArrayList<>();
    }

    // 활동 기록 추가
    public void addActivity(String type, String name, int calories, LocalDate date) {
        activities.add(new Activity(type, name, calories, date));
    }

    // 특정 날짜의 총 칼로리 소모/섭취 계산
    public int getTotalCalories(String type, LocalDate date) {
        return activities.stream()
                .filter(a -> a.type.equalsIgnoreCase(type) && a.date.equals(date))
                .mapToInt(a -> a.calories)
                .sum();
    }

    // 기록 JSON으로 저장
    public String saveToJSON() {
        JSONArray jsonArray = new JSONArray();
        for (Activity activity : activities) {
            JSONObject obj = new JSONObject();
            obj.put("type", activity.type);
            obj.put("name", activity.name);
            obj.put("calories", activity.calories);
            obj.put("date", activity.date.toString());
            jsonArray.put(obj);
        }
        return jsonArray.toString();
    }

    // JSON으로부터 기록 불러오기
    public void loadFromJSON(String jsonData) {
        JSONArray jsonArray = new JSONArray(jsonData);
        activities.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            addActivity(
                    obj.getString("type"),
                    obj.getString("name"),
                    obj.getInt("calories"),
                    LocalDate.parse(obj.getString("date")));
        }
    }

    // 활동 기록 출력
    public void printActivities() {
        activities.forEach(System.out::println);
    }

    public static void main(String[] args) {
        ActivityLog log = new ActivityLog();

        // 데이터 추가
        log.addActivity("운동", "달리기", 300, LocalDate.now());
        log.addActivity("식단", "샐러드", 200, LocalDate.now());

        // 총 칼로리 계산
        System.out.println("오늘 운동 소모 칼로리: " + log.getTotalCalories("운동", LocalDate.now()));
        System.out.println("오늘 식단 섭취 칼로리: " + log.getTotalCalories("식단", LocalDate.now()));

        // JSON 저장 및 불러오기
        String jsonData = log.saveToJSON();
        System.out.println("JSON 데이터 저장: " + jsonData);

        log.loadFromJSON(jsonData);
        log.printActivities();
    }
}

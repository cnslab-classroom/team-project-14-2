import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Recommendation {

    //외부 객체 클래스 
    private User user;
    //private HealthMetric healthMetric;
    //private ActivityLog activityLog;

    //운동한 시간
    private double timeWeek;     
    private double timeMonth;

    //섭취한 칼로리
    private double calorieWeek;
    private double calorieMonth; 

    //장기 데이터 저장소 
    private List<String> weeklyReports = new ArrayList<>(); //주간 리포트
    private List<String> monthlyReports = new ArrayList<>(); //월간 리포트 

    //목표몸무게도 받아오기
    //-> 목표 몸무게까지 얼마나 더 감량해야 하는지 표시하게 

    //User 클래스에서 권장 몸무게 받아옴 
    double[] idealWeightRange = user.calculateIdealWeightRange();
    double minWeight = idealWeightRange[0];
    double maxWeight = idealWeightRange[1];

    
    //생성자
    public Recommendation(User user) {
        
        this.user = new User(null, 0, 0, 0, null);
    //this.healthMetric = new HealthMetric();
    //this.activityLog = new ActivityLog();
        this.timeWeek = 0;
        this.timeMonth = 0;
        this.calorieWeek = 0;
        this.calorieMonth = 0;
    }



// 하루 기록 업데이트 -> ActivityLog 클래스로부터 받아와야
public void updateDailyLog(double dailyTime, double dailyCalories) {
    this.timeWeek += dailyTime;
    this.timeMonth += dailyTime;
    this.calorieWeek += dailyCalories;
    this.calorieMonth += dailyCalories;
}

// 주간 리포트 저장
private void saveWeeklyReport() {
    String report = LocalDate.now() + " 주간 리포트: 총 운동 시간: " + timeWeek + "시간, 총 섭취 칼로리: " + calorieWeek + "kcal";
    weeklyReports.add(report);
}

// 월간 리포트 저장
private void saveMonthlyReport() {
    String report = LocalDate.now() + " 월간 리포트: 총 운동 시간: " + timeMonth + "시간, 총 섭취 칼로리: " + calorieMonth + "kcal";
    monthlyReports.add(report);
}


//주간 보고서
public void weeklyReport()
{
    System.out.println("이번주 총 운동 시간은 " + timeWeek + "시간 입니다.");
    System.out.println("이번주 총 섭취 칼로리는 " + calorieWeek + "kcal 입니다.");
    printRecommend();
    saveWeeklyReport();  // 저장
    resetWeeklyData();   // 초기화
}



//월간 보고서 
public void monthlyReport()
{
    System.out.println("이번달 총 운동 시간은 " + timeMonth + "시간 입니다.");
    System.out.println("이번달 총 섭취 칼로리는 " + calorieMonth + "kcal 입니다.");
    printRecommend();
    saveMonthlyReport(); // 저장
    resetMonthlyData();  // 초기화
}

// 주간 데이터 초기화
private void resetWeeklyData() {
    this.timeWeek = 0;
    this.calorieWeek = 0;
}

// 월간 데이터 초기화
private void resetMonthlyData() {
    this.timeMonth = 0;
    this.calorieMonth = 0;
}

// 연말 리포트 출력
public void printYearlyReport() {
    System.out.println("연간 주간 리포트:");
    for (String report : weeklyReports) {
        System.out.println(report);
    }

    System.out.println("\n연간 월간 리포트:");
    for (String report : monthlyReports) {
        System.out.println(report);
    }
}

// 체지방률에 따라 성별별,나이별 높음 낮음 정상으로 구분 
public int checkBodyFatPercentage() {
    int age = user.getAge();
    double bodyFatPercentage = user.calculateBodyFatPercentage();

    if (user.getGender()=="male") { // 성별: 남성
        if (age >= 18 && age <= 39) {
            if (bodyFatPercentage > 20) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 9 && bodyFatPercentage <= 20) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        } else if (age >= 40 && age <= 59) {
            if (bodyFatPercentage > 22) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 12 && bodyFatPercentage <= 22) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        } else if (age >= 60) {
            if (bodyFatPercentage > 24) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 14 && bodyFatPercentage <= 24) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        }
    } else if (user.getGender()=="female") { // 성별: 여성
        if (age >= 18 && age <= 39) {
            if (bodyFatPercentage > 30) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 19 && bodyFatPercentage <= 30) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        } else if (age >= 40 && age <= 59) {
            if (bodyFatPercentage > 32) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 21 && bodyFatPercentage <= 32) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        } else if (age >= 60) {
            if (bodyFatPercentage > 34) {
                return 1; // 체지방량 높음
            } else if (bodyFatPercentage >= 23 && bodyFatPercentage <= 34) {
                return 2; // 체지방량 정상
            } else {
                return 3; // 체지방량 낮음
            }
        }
    }

    return 0; // 기본값: 유효하지 않은 경우
}


//권장몸무게보다 현 사용자의 몸무게가 무거운지 가벼운지 일치하나 구분
public int checkIdealWeight()
{
    

    if(user.getWeight()>maxWeight)
    {
        return 1; //권장 몸무게보다 무거움 -> 체중 감량 필요
    }
    
    else if(user.getWeight() >= minWeight && user.getWeight() <= maxWeight)
    {
        return 2; //권장 몸무게 범위 내 
    }
    else if(user.getWeight()<minWeight)
    {
        return 3; //권장 몸무게보다 가벼움 -> 체중 증량 필요 
    }
    else
    {
        return 0; // 기본값: 유효하지 않은 경우
    }
    }


//사용자의 현상황에 적합한 제안 출력 
public void printRecommend()
{
    if(checkBodyFatPercentage()==1 && checkIdealWeight()==1) //체지방률 높 ,권장몸무게 높 
    {
        System.out.println("감량이 필요합니다! 식단과 병행하는 고강도 운동이 필요합니다.일일 권장 칼로리에 300~500kcal를\r\n" + //
                        "줄인 고단백 저탄수 식단을 섭취해 주세요.\r\n" + //
                        "고강도 유산소운동과 근력 운동을 병행해 주세요");
    }
    else if(checkBodyFatPercentage()==2 && checkIdealWeight()==2)//체지방률 o ,권장몸무게 o
    {
        System.out.println("잘하고 있어요! 현재 상태를 유지하기 위해 일일 권장 칼로리를 섭취하고, \r\n" + //
                        "    가벼운 생활 운동을 병행하도록 해요.");
    }
    else if(checkBodyFatPercentage()==3 && checkIdealWeight()==3)//체지방률 낮 ,권장몸무게 낮
    {
        System.out.println("체중 증가와 근력 향상이 필요합니다. 일일 권장 칼로리에 300~500kcal를\r\n" + //
                        "추가하여 고단백 고열량 식단을 섭취해 주세요.\r\n" + //
                        "근력 운동을 중심으로 운동해 주세요.\r\n" + //
                        "");
    }
    else if(checkBodyFatPercentage()==2 && checkIdealWeight()==1)//체지방률 o ,권장몸무게 높
    {
        System.out.println("현재 체지방률을 유지하며  체중을 줄입시다!\r\n" + //
                        "저칼로리 식단과 함께 유산소 운동, 근력 운동을 병행합시다.");
    }
    else if(checkBodyFatPercentage()==2 && checkIdealWeight()==3)//체지방률 o ,권장몸무게 낮
    {
        System.out.println("현재 체지방률을 유지하며 체중 증가가 필요합니다.\r\n" + //
                        "고칼로리 식단과 함께 근력 운동 위주의 운동과 함께 가벼운 유산소 운동을 병행합시다.");
    }
    else if(checkBodyFatPercentage()==1 && checkIdealWeight()==2)//체지방률 높 ,권장몸무게 o
    {
        System.out.println("체지방을 줄이고 체중을 유지합시다!\r\n" + //
                        "저탄수 고단백 식단과 함께 고강도 유산소 운동과 근력 운동을 병행합시다!");
    }
    else if(checkBodyFatPercentage()==3 && checkIdealWeight()==1)//체지방률 낮 ,권장몸무게 높
    {
        System.out.println("아주 건강한 몸을 잘 유지하고 계시는 군요! 현재 상태를 유지하기 위해 노력합시다!");
    }

    


    
}
        









}

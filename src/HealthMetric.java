import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class HealthMetric {
    private Connection connection;

    // 생성자: 데이터베이스 연결 및 초기화
    public HealthMetric() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:health_data.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터베이스 초기화
    private void initializeDatabase() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS health_metrics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                bmi REAL NOT NULL,
                body_fat REAL NOT NULL,
                sleep_hours REAL NOT NULL
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // BMI 계산
    public double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    // 체지방률 계산
    public double calculateBodyFatPercentage(double bmi, int age, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return (1.20 * bmi) + (0.23 * age) - 16.2;
        } else {
            return (1.20 * bmi) + (0.23 * age) - 5.4;
        }
    }

    // 수면 시간 계산
    public double calculateSleepHours(String sleepTime, String wakeTime) {
        LocalTime sleep = LocalTime.parse(sleepTime);
        LocalTime wake = LocalTime.parse(wakeTime);

        long minutesBetween = ChronoUnit.MINUTES.between(sleep, wake);
        if (minutesBetween < 0) {
            minutesBetween += 24 * 60; // 다음 날로 넘어가는 경우 처리
        }

        return minutesBetween / 60.0;
    }

    // 건강 데이터 추가
    public void addRecord(String date, double bmi, double bodyFatPercentage, double sleepHours) {
        String insertQuery = "INSERT INTO health_metrics (date, bmi, body_fat, sleep_hours) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, date);
            preparedStatement.setDouble(2, bmi);
            preparedStatement.setDouble(3, bodyFatPercentage);
            preparedStatement.setDouble(4, sleepHours);
            preparedStatement.executeUpdate();
            System.out.println("데이터가 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 건강 데이터 조회
    public void printAllRecords() {
        String selectQuery = "SELECT * FROM health_metrics";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                System.out.printf("날짜: %s, BMI: %.2f, 체지방률: %.2f%%, 수면 시간: %.2f시간\n",
                        resultSet.getString("date"),
                        resultSet.getDouble("bmi"),
                        resultSet.getDouble("body_fat"),
                        resultSet.getDouble("sleep_hours"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 건강 지표 분석
    public void analyzeHealthMetrics(double bmi, double bodyFatPercentage, double sleepHours) {
        System.out.println("\n===== 건강 지표 분석 =====");

        // BMI 분석
        if (bmi < 18.5) {
            System.out.println("- 저체중 (BMI: " + bmi + ")");
        } else if (bmi < 25) {
            System.out.println("- 정상 체중 (BMI: " + bmi + ")");
        } else {
            System.out.println("- 과체중 또는 비만 (BMI: " + bmi + ")");
        }

        // 체지방률 분석
        if (bodyFatPercentage < 10) {
            System.out.println("- 체지방률 낮음 (" + bodyFatPercentage + "%)");
        } else if (bodyFatPercentage <= 20) {
            System.out.println("- 체지방률 정상 (" + bodyFatPercentage + "%)");
        } else {
            System.out.println("- 체지방률 높음 (" + bodyFatPercentage + "%)");
        }

        // 수면 시간 분석
        if (sleepHours < 7) {
            System.out.println("- 수면 부족 (" + sleepHours + "시간)");
        } else if (sleepHours > 9) {
            System.out.println("- 수면 과다 (" + sleepHours + "시간)");
        } else {
            System.out.println("- 적정 수면 시간 (" + sleepHours + "시간)");
        }
    }

    // 데이터베이스 연결 종료
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메인 실행
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HealthMetric healthMetric = new HealthMetric();

        while (true) {
            System.out.println("\n===== 건강 데이터 관리 시스템 =====");
            System.out.println("1. 건강 데이터 추가");
            System.out.println("2. 저장된 데이터 조회");
            System.out.println("3. 건강 지표 분석 결과");
            System.out.println("4. 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("날짜 (YYYY-MM-DD): ");
                    String date = scanner.next();

                    System.out.print("키(m): ");
                    double height = scanner.nextDouble();

                    System.out.print("몸무게(kg): ");
                    double weight = scanner.nextDouble();

                    System.out.print("나이: ");
                    int age = scanner.nextInt();

                    System.out.print("성별 (male/female): ");
                    String gender = scanner.next();

                    System.out.print("취침 시간 (HH:mm): ");
                    String sleepTime = scanner.next();

                    System.out.print("기상 시간 (HH:mm): ");
                    String wakeTime = scanner.next();

                    double bmi = healthMetric.calculateBMI(weight, height);
                    double bodyFat = healthMetric.calculateBodyFatPercentage(bmi, age, gender);
                    double sleepHours = healthMetric.calculateSleepHours(sleepTime, wakeTime);

                    healthMetric.addRecord(date, bmi, bodyFat, sleepHours);
                    healthMetric.analyzeHealthMetrics(bmi, bodyFat, sleepHours);
                    break;

                case 2:
                    healthMetric.printAllRecords();
                    break;

                case 3:
                    System.out.println("건강 데이터 추가 후 분석을 확인하세요.");
                    break;

                case 4:
                    healthMetric.closeConnection();
                    scanner.close();
                    return;

                default:
                    System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }
}


import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
    public double calculateBMI(User user) {
        double weight = user.getWeight();
        double height = user.getHeight();
        return weight / (height * height);
    }

    // 체지방률 계산
    public double calculateBodyFatPercentage(double bmi, User user, String gender) {
        int age = user.getAge();
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
}

public class User {
  // 사용자 기본 정보
  private String name;
  private int age;
  private double height; // 단위: cm
  private double weight; // 단위: kg

  // 생성자
  public User(String name, int age, double height, double weight) {
      this.name = name;
      this.age = age;
      this.height = height;
      this.weight = weight;
  }

  // getter와 setter
  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public int getAge() {
      return age;
  }

  public void setAge(int age) {
      this.age = age;
  }

  public double getHeight() {
      return height;
  }

  public void setHeight(double height) {
      this.height = height;
  }

  public double getWeight() {
      return weight;
  }

  public void setWeight(double weight) {
      this.weight = weight;
  }

  // BMI 계산 메서드
  public double calculateBMI() {
      double heightInMeters = height / 100.0; // cm를 m로 변환
      return weight / (heightInMeters * heightInMeters);
  }

  // 체지방률 계산 메서드
  // 남성: 체지방률 = (1.20 × BMI) + (0.23 × 나이) − 16.2
  // 여성: 체지방률 = (1.20 × BMI) + (0.23 × 나이) − 5.4
  public double calculateBodyFatPercentage(boolean isMale) {
      double bmi = calculateBMI();
      if (isMale) {
          return (1.20 * bmi) + (0.23 * age) - 16.2;
      } else {
          return (1.20 * bmi) + (0.23 * age) - 5.4;
      }
  }

  // 권장 체중 계산 (BMI 기준 18.5~24.9)
  public double[] calculateIdealWeightRange() {
      double heightInMeters = height / 100.0;
      double minWeight = 18.5 * (heightInMeters * heightInMeters);
      double maxWeight = 24.9 * (heightInMeters * heightInMeters);
      return new double[] { minWeight, maxWeight };
  }

  // 목표 체중 안내 메서드
  public void printGoalInfo(double goalWeight) {
      double[] idealRange = calculateIdealWeightRange();
      double weightToLose = weight - goalWeight;
      System.out.println("현재 BMI: " + String.format("%.2f", calculateBMI()));
      System.out.println("권장 체중 범위: " + String.format("%.2f", idealRange[0]) + "kg ~ " + String.format("%.2f", idealRange[1]) + "kg");

      if (goalWeight < idealRange[0] || goalWeight > idealRange[1]) {
          System.out.println("경고: 목표 체중이 권장 범위를 벗어났습니다.");
      }

      System.out.println("목표 체중까지 감량해야 할 무게: " + String.format("%.2f", weightToLose) + "kg");
      System.out.println("일일 500칼로리 적자 기준 감량 예상 소요 시간: 약 " + (weightToLose * 7700 / 500) + "일");
  }
}

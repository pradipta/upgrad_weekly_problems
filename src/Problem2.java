import java.util.concurrent.ExecutionException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Problem Statement: Figure out why Bob's scores are null and fix it.
 * Restrictions: Do not modify WhereIsMyData#main
 *
 * Please do not discuss solutions on our common group so that everyone gets fair chance to attempt.
 * You can share SOLUTION WITH REASONING via direct message on slack.
 *
 * Collective findings will be shared by Monday.
 * */

public class Problem2 implements Problem {
    @Override
    public void main(String[] args) throws Exception {
        StudentInMemoryRepository studentRepository = new StudentInMemoryRepository();
        ExamService examService = new ExamService();

        Student charley = studentRepository.add("Charley");
        examService.giveExam(charley);

        Student alice = studentRepository.add("Alice");
        examService.giveExam(alice);

        Student bob = studentRepository.add("Bob");
        examService.giveExam(bob);

        Student adam = studentRepository.add("Adam");
        examService.giveExam(adam);

        Student brad = studentRepository.add("Brad");
        examService.giveExam(brad);

        System.out.println("Score for bob: " + examService.getScore(bob));
    }
}


class Student {
    private String name;
    private int rollNumber = -1;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return name.equals(student.name); //since names are unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", rollNumber=" + rollNumber +
                '}';
    }
}

class ExamService {

    private static final Random RANDOM = new Random();

    private static final Map<Student, List<Integer>> SCORES = new HashMap<>();

    List<Integer> giveExam(Student student) {
        List<Integer> score = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            score.add(Math.abs(RANDOM.nextInt(100)));
        }
        SCORES.put(student, score);
        return score;
    }

    List<Integer> getScore(Student student) {
        return SCORES.get(student);
    }
}

class StudentInMemoryRepository {

    private Set<Student> students = new TreeSet<>(Comparator.comparingInt(Student::getRollNumber));

    public Student add(String name) {
        Student student = new Student(name);
        reAssignRoleNumbers(student);
        students.add(student);
        return student;
    }

    /*
     * If new student is added, he should get roll number as per his name's alphabetical order.
     * ASSUME THAT THERE WON'T BE A STUDENT WITH EXACT SAME NAME
     * Example: existing students with roll nos.: Alice(1), Bob(2), Charley(3)
     * case 1: newStudent name: Adam
     * Expected list by roll number: Adam(1), Alice(2), Bob(3), Charley(4)
     * case 2: newStudent name: Brad
     * Expected list by roll number: Adam(1), Alice(2), Bob(3), Brad(4), Charley(5)
     */
    private void reAssignRoleNumbers(Student newStudent) {
        if (students.isEmpty()) {
            newStudent.setRollNumber(1);
            return;
        }

        for (Student student : students) {
            int result = student.getName().compareTo(newStudent.getName());
            if (result > 0) {
                if (newStudent.getRollNumber() == -1) {
                    newStudent.setRollNumber(student.getRollNumber());
                }
                student.setRollNumber(student.getRollNumber() + 1);
            }
        }
    }

    public void printAllStudents() {
        System.out.println(students.toString());
    }
}

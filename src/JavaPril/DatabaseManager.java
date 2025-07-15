package JavaPril;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;
    private PreparedStatement statement;

    public DatabaseManager() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/studentgrades", "sa", "");
            createTables();
            createSubjectTable(); 
            createGradeTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), subject VARCHAR(255), grade INT)");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSubjectTable() {
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS subjects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createGradeTable() {
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS grades (id INT AUTO_INCREMENT PRIMARY KEY, grade INT)");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addSubject(Subject subject) {
        try {
            String name = subject.getName();

            statement = connection.prepareStatement("INSERT INTO subjects (name) VALUES (?)");
            statement.setString(1, name);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGrade(Grade grade) {
        try {
            
            int gradeValue = grade.getGrade();

            statement = connection.prepareStatement("INSERT INTO grades (grade) VALUES (?)");
            
            statement.setInt(1, gradeValue);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM subjects");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Subject subject = new Subject(name);
                subject.setId(id);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM grades");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
               
                int gradeValue = resultSet.getInt("grade");

                Grade grade = new Grade(gradeValue);
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return grades;
    }

    public void addStudent(Student student) {
        try {
            String name = student.getName();
            String subject = student.getSubject();
            int grade = student.getGrade();

            statement = connection.prepareStatement("INSERT INTO students (name, subject, grade) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, subject);
            statement.setInt(3, grade);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student updatedStudent) {
        int id = updatedStudent.getId();
        try {
            statement = connection.prepareStatement("UPDATE students SET name=?, subject=?, grade=? WHERE id=?");
            statement.setString(1, updatedStudent.getName());
            statement.setString(2, updatedStudent.getSubject());
            statement.setInt(3, updatedStudent.getGrade());
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        try {
            statement = connection.prepareStatement("DELETE FROM students WHERE id=?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int id) {
        try {
            statement = connection.prepareStatement("DELETE FROM subjects WHERE id=?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGrade(int id) {
        try {
            statement = connection.prepareStatement("DELETE FROM grades WHERE id=?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM students");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String subject = resultSet.getString("subject");
                int grade = resultSet.getInt("grade");

                Student student = new Student(id, name, subject, grade);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Student> searchStudents(String criterion) {
        List<Student> students = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM students WHERE name LIKE ? OR subject LIKE ?");
            statement.setString(1, "%" + criterion + "%");
            statement.setString(2, "%" + criterion + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String subject = resultSet.getString("subject");
                int grade = resultSet.getInt("grade");

                Student student = new Student(id, name, subject, grade);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}

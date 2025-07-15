package JavaPril;
public class Student {
    private int id;
    private String name;
    private String subject;
    private int grade;

    public Student(String name, String subject, int grade) {
        this.name = name;
        this.subject = subject;
        this.grade = grade;
    }

    public Student(int id, String name, String subject, int grade) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }
}
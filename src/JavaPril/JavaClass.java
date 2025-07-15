package JavaPril;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class JavaClass {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private DatabaseManager databaseManager;
    private StudentTableModel studentTableModel;
    private JTextField txtStudentName;
    private JTextField txtStudentSubject;
    private JTextField txtStudentGrade;
    private JTable studentTable;

    public JavaClass() {
        databaseManager = new DatabaseManager();
        initialize();
        studentTableModel.setData(databaseManager.getAllStudents());
    }

    private void initialize() {
        frame = new JFrame("Student Grades Manager");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JPanel studentPanel = createStudentPanel();
        tabbedPane.addTab("Ученици", studentPanel);

        JPanel subjectPanel = createSubjectPanel();
        tabbedPane.addTab("Предмети", subjectPanel);

        JPanel gradePanel = createGradePanel();
        tabbedPane.addTab("Оценки", gradePanel);

        JPanel queryPanel = createQueryPanel();
        tabbedPane.addTab("Справка", queryPanel);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        studentTableModel = new StudentTableModel();

        List<Student> initialStudents = databaseManager.getAllStudents();
        studentTableModel.setData(initialStudents);

        studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 3));

        JLabel lblName = new JLabel("Име:");
        txtStudentName = new JTextField();
        JLabel lblSubject = new JLabel("Предмет:");
        txtStudentSubject = new JTextField();
        JLabel lblGrade = new JLabel("Оценка:");
        txtStudentGrade = new JTextField();
        JButton btnInsert = new JButton("Въведи");
        JButton btnUpdate = new JButton("Промени");
        JButton btnDelete = new JButton("Изтрий");

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);


                    databaseManager.deleteStudent(studentId);


                    studentTableModel.setData(databaseManager.getAllStudents());
                    studentTableModel.fireTableDataChanged();
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
                    String name = txtStudentName.getText();
                    String subject = txtStudentSubject.getText();
                    int grade = Integer.parseInt(txtStudentGrade.getText());              
                    Student updatedStudent = new Student(studentId, name, subject, grade);
                    databaseManager.updateStudent(updatedStudent);
                    studentTableModel.setData(databaseManager.getAllStudents());
                    studentTableModel.fireTableDataChanged();
                    txtStudentName.setText("");
                    txtStudentSubject.setText("");
                    txtStudentGrade.setText("");
                }
            }
        });
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtStudentName.getText();
                String subject = txtStudentSubject.getText();
                int grade = Integer.parseInt(txtStudentGrade.getText());
                Student student = new Student(name, subject, grade);
                databaseManager.addStudent(student);
                studentTableModel.setData(databaseManager.getAllStudents());
                studentTableModel.fireTableDataChanged();
                txtStudentName.setText("");
                txtStudentSubject.setText("");
                txtStudentGrade.setText("");
            }
        });
        inputPanel.add(lblName);
        inputPanel.add(txtStudentName);
        inputPanel.add(lblSubject);
        inputPanel.add(txtStudentSubject);
        inputPanel.add(lblGrade);
        inputPanel.add(txtStudentGrade);
        inputPanel.add(btnInsert);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);
        panel.add(inputPanel, BorderLayout.SOUTH);
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = studentTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    studentTable.clearSelection();
                }
            }
        });

        return panel;
    }


    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        DefaultTableModel subjectTableModel = new DefaultTableModel();
        JTable subjectTable = new JTable(subjectTableModel);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        List<Subject> initialSubjects = databaseManager.getAllSubjects();
        for (Subject subject : initialSubjects) {
            subjectTableModel.addRow(new Object[]{subject.getId(), subject.getName()});
        }

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        JLabel lblSubjectName = new JLabel("Име на предмет:");
        JTextField txtSubjectName = new JTextField();
        JButton btnInsertSubject = new JButton("Въведи");
        JButton btnDeleteSubject = new JButton("Изтрий");
        btnInsertSubject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String subjectName = txtSubjectName.getText();
                databaseManager.addSubject(new Subject(subjectName));
                subjectTableModel.setRowCount(0);
                List<Subject> updatedSubjects = databaseManager.getAllSubjects();
                for (Subject subject : updatedSubjects) {
                    subjectTableModel.addRow(new Object[]{subject.getId(), subject.getName()});
                }
                txtSubjectName.setText("");
            }
        });
        btnDeleteSubject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = subjectTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int subjectId = (int) subjectTableModel.getValueAt(selectedRow, 0);
                    databaseManager.deleteSubject(subjectId);
                    subjectTableModel.removeRow(selectedRow);
                }
            }
        });
        inputPanel.add(lblSubjectName);
        inputPanel.add(txtSubjectName);
        inputPanel.add(btnInsertSubject);
        inputPanel.add(btnDeleteSubject);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        DefaultTableModel gradeTableModel = new DefaultTableModel();
        JTable gradeTable = new JTable(gradeTableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        List<Grade> initialGrades = databaseManager.getAllGrades();
        for (Grade grade : initialGrades) {
            gradeTableModel.addRow(new Object[]{grade.getGrade()});
        }

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        JLabel lblGrade = new JLabel("Оценка:");
        JTextField txtGrade = new JTextField();
        JButton btnInsertGrade = new JButton("Въведи");
        JButton btnDeleteGrade = new JButton("Изтрий");
        btnInsertGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int gradeValue = Integer.parseInt(txtGrade.getText());
                databaseManager.addGrade(new Grade(gradeValue));
                gradeTableModel.setRowCount(0);
                List<Grade> updatedGrades = databaseManager.getAllGrades();
                for (Grade grade : updatedGrades) {
                    gradeTableModel.addRow(new Object[]{grade.getGrade()});
                }

                txtGrade.setText("");
            }
        });
        btnDeleteGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = gradeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int gradeId = (int) gradeTableModel.getValueAt(selectedRow, 0);
                    databaseManager.deleteGrade(gradeId);
                    gradeTableModel.removeRow(selectedRow);
                }
            }
        });

        inputPanel.add(lblGrade);
        inputPanel.add(txtGrade);
        inputPanel.add(btnInsertGrade);
        inputPanel.add(btnDeleteGrade);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }




    private JPanel createQueryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel lblCriterion = new JLabel("Въведи ученик или предмет:");
        JTextField txtCriterion = new JTextField();
        JButton btnQuery = new JButton("Справка");
        DefaultTableModel queryTableModel = new DefaultTableModel();
        JTable queryTable = new JTable(queryTableModel);
        JScrollPane scrollPane = new JScrollPane(queryTable);
        btnQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String criterion = txtCriterion.getText();
                List<Student> students = databaseManager.searchStudents(criterion);
                queryTableModel.setDataVector(getQueryTableData(students), getQueryTableColumns());
            }
        });
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 3));
        inputPanel.add(lblCriterion);
        inputPanel.add(txtCriterion);
        inputPanel.add(btnQuery);

        panel.add(inputPanel, BorderLayout.NORTH);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Object[][] getQueryTableData(List<Student> students) {
        Object[][] data = new Object[students.size()][4];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getName();
            data[i][2] = student.getSubject();
            data[i][3] = student.getGrade();
        }
        return data;
    }

    private String[] getQueryTableColumns() {
        return new String[]{"ID", "Име", "Предмет", "Оценка"};
    }

    private class StudentTableModel extends AbstractTableModel {
    	private static final long serialVersionUID = 1L;

    	private String[] columnNames = {"ID", "Име", "Предмет", "Оценка"};
        private Object[][] data = {};

        public void setData(List<Student> students) {
            data = new Object[students.size()][4];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getId();
                data[i][1] = student.getName();
                data[i][2] = student.getSubject();
                data[i][3] = student.getGrade();
            }
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JavaClass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

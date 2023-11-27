package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList; // Data Structure   yang membolehkan storage and manipulation pada Array.
import java.util.Collections; // untuk sorting operations pada studentList dalam method sortStudents()
import java.util.Comparator; // Sorting algorithm, susun ikut nama dan fakulti
import java.util.List; // untuk List<Student> studentList

// Sila Tekan Load untuk memuat data yang telah disimpan dalam record sebelum ini, daripada file bername save_file

/* Created with IntelliJ IDEA 2023.1.1
*  Student Record Database
* Team member: Azzam,Hanan,Amir Fitri
* fungsi:
* Add/remove student
* Sorting berdasarkan Nama atau Fakulti
* Save / Load  data
* daripada file dengan mengunakan serialize yang membolehkan semua object disimpan dalam file, dan di load semula untuk sesi seterusnya
* */
public class StudentRecordSystemGUI extends JFrame {
    private JTextField nameField, matrixField, facultyField, matrixNumberField;
    private JTextArea recordArea;
    private JComboBox<String> sortComboBox;
    private JComboBox<String> studentComboBox; // Changed type to String
    private JButton addButton, removeButton, sortButton, viewButton, saveButton, loadButton;
    private List<Student> studentList; //declares a private instance variable named studentList of type List<Student>.

    public StudentRecordSystemGUI() {
        studentList = new ArrayList<>();

        setTitle("Student Record System");
        setSize(1100, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        nameField = new JTextField(15);
        matrixField = new JTextField(10);
        facultyField = new JTextField(10);
        recordArea = new JTextArea(15, 30);
        sortComboBox = new JComboBox<>(new String[]{"Name", "Faculty"});
        addButton = new JButton("Add Student");
        removeButton = new JButton("Remove Student");
        sortButton = new JButton("Sort Students");
        viewButton = new JButton("View Record");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        studentComboBox = new JComboBox<>(); // Initialize the combo box
        matrixNumberField = new JTextField(10);

        // Create panels
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel recordPanel = new JPanel();

        // Set layouts
        setLayout(new BorderLayout());
        inputPanel.setLayout(new GridLayout(3, 2));
        recordPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(1, 7)); // Increase the grid layout columns to accommodate the new combo box

        // Add components to panels
        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Matrix Number: "));
        inputPanel.add(matrixField);
        inputPanel.add(new JLabel("Faculty: "));
        inputPanel.add(facultyField);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(new JLabel("Matrix Number: "));
        buttonPanel.add(matrixNumberField);
        buttonPanel.add(sortButton);
        buttonPanel.add(sortComboBox);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        recordPanel.add(new JScrollPane(recordArea), BorderLayout.CENTER);
        recordPanel.add(viewButton, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(recordPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortStudents();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewRecord();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveRecords();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadRecords();
            }
        });
    }

    /* try-catch block around the addStudent() method.
     It checks the length of the matrix number and verifies
     if it is a valid integer using Integer.parseInt(). */

    private void addStudent() {
        String name = nameField.getText();
        String matrixNumber = matrixField.getText();
        String faculty = facultyField.getText();

        if (!name.isEmpty() && !matrixNumber.isEmpty() && !faculty.isEmpty()) { //user must fill all data field!!
            try {
                if (matrixNumber.length() != 6) {
                    throw new IllegalArgumentException("Matrix number must have exactly six characters.");
                }
                Integer.parseInt(matrixNumber); // Check if the matrix number is a valid integer

                Student student = new Student(name, matrixNumber, faculty);
                studentList.add(student);
                clearFields();
                recordArea.append("Student added: " + student.toString() + "\n");

                // Update the combo box with the updated student list
                updateStudentComboBox();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid matrix number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**/
    private void removeStudent() {
        String matrixNumber = matrixNumberField.getText(); // Get the matrix number from the text field
        for (Student student : studentList) {
            if (student.getMatrixNumber().equals(matrixNumber)) {
                studentList.remove(student);
                clearFields();
                recordArea.setText("");
                recordArea.append("Student removed: " + student.toString() + "\n");

                // Update the combo box with the updated student list
                updateStudentComboBox();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
// Susun list pelajar berdasarkan Nama atau fakulti

    private void sortStudents() {
        String selectedSort = (String) sortComboBox.getSelectedItem();
        if (selectedSort.equals("Name")) {
            Collections.sort(studentList, Comparator.comparing(Student::getName));
        } else if (selectedSort.equals("Faculty")) {
            Collections.sort(studentList, Comparator.comparing(Student::getFaculty));
        }
        viewRecord();
    }

    // Display Record of the student based on current list
    private void viewRecord() {
        recordArea.setText("");
        for (Student student : studentList) {
            recordArea.append(student.toString() + "\n");
        }
    }

    //Clear the data field for text
    private void clearFields() {
        nameField.setText("");
        matrixField.setText("");
        facultyField.setText("");
        matrixNumberField.setText("");
    }

    // save kan record pada satu file bernama save_file yang terdapat dalam folder project
    private void saveRecords() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("save_file"))) {
            outputStream.writeObject(studentList);
            JOptionPane.showMessageDialog(this, "Records saved successfully.", "Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving records.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // load record yang telah disimpan dari sesi sebelumnya
    private void loadRecords() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("save_file"))) {
            studentList = (List<Student>) inputStream.readObject();
            viewRecord();
            updateStudentComboBox();
            JOptionPane.showMessageDialog(this, "Records loaded successfully.", "Load", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading records.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudentComboBox() {
        studentComboBox.removeAllItems();
        for (Student student : studentList) {
            studentComboBox.addItem(String.valueOf(student));
        }
    }

    // main program ada di sini, tekan run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentRecordSystemGUI().setVisible(true);
            }
        });
    }

    // memudahkan penyimpan data dalam bentuk array dengan mengunakkan Serializable
    private class Student implements Serializable {
        private String name;
        private String matrixNumber;
        private String faculty;

        public Student(String name, String matrixNumber, String faculty) {
            this.name = name;
            this.matrixNumber = matrixNumber;
            this.faculty = faculty;
        }

        public String getName() {
            return name;
        }

        public String getMatrixNumber() {
            return matrixNumber;
        }

        public String getFaculty() {
            return faculty;
        }

        public String toString() {
            return "Name: " + name + ", Matrix Number: " + matrixNumber + ", Faculty: " + faculty;
        }
    }
}

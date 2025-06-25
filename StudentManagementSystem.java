import java.io.*;
import java.util.*;

class Student {
    private String id;
    private String name;
    private double marks;

    public Student(String id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getMarks() { return marks; }

    public void setName(String name) { this.name = name; }
    public void setMarks(double marks) { this.marks = marks; }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Marks: " + marks;
    }

    public String toCSV() {
        return id + "," + name + "," + marks;
    }

    public static Student fromCSV(String line) {
        String[] parts = line.split(",");
        return new Student(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

public class StudentManagementSystem {
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();

        int choice;
        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search by Name");
            System.out.println("6. Sort Students");
            System.out.println("7. Save to File");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewStudents(); break;
                case 3: updateStudent(); break;
                case 4: deleteStudent(); break;
                case 5: searchStudent(); break;
                case 6: sortStudents(); break;
                case 7: saveToFile(); break;
                case 8: 
                    saveToFile();
                    System.out.println("Exiting...");
                    break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 8);
    }

    private static void addStudent() {
        scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter marks: ");
        
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Enter a number for marks: ");
            scanner.next();
        }
        double marks = scanner.nextDouble();

        students.add(new Student(id, name, marks));
        System.out.println("Student added.");
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private static void updateStudent() {
        scanner.nextLine();
        System.out.print("Enter student ID to update: ");
        String id = scanner.nextLine();
        boolean found = false;

        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) {
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new marks: ");
                while (!scanner.hasNextDouble()) {
                    System.out.print("Invalid input. Enter a number for marks: ");
                    scanner.next();
                }
                double marks = scanner.nextDouble();
                s.setName(name);
                s.setMarks(marks);
                System.out.println("Student updated.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    private static void deleteStudent() {
        scanner.nextLine();
        System.out.print("Enter student ID to delete: ");
        String id = scanner.nextLine();
        boolean removed = students.removeIf(s -> s.getId().equalsIgnoreCase(id));
        System.out.println(removed ? "Student deleted." : "Student not found.");
    }

    private static void searchStudent() {
        scanner.nextLine();
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(name)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No student found with name containing '" + name + "'");
        }
    }

    private static void sortStudents() {
        System.out.println("Sort by:");
        System.out.println("1. Name");
        System.out.println("2. Marks");
        int sortChoice = scanner.nextInt();
        if (sortChoice == 1) {
            students.sort(Comparator.comparing(Student::getName));
        } else if (sortChoice == 2) {
            students.sort(Comparator.comparingDouble(Student::getMarks).reversed());
        } else {
            System.out.println("Invalid choice!");
            return;
        }
        System.out.println("Students sorted.");
        viewStudents();
    }

    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.write(s.toCSV());
                writer.newLine();
            }
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(Student.fromCSV(line));
            }
            System.out.println("Loaded " + students.size() + " students from file.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}

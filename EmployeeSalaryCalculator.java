import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;class Employee {
    private String id;
    private String name;
    private String category;
    private double basicSalary;

    public Employee(String id, String name, String category, double basicSalary) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basicSalary = basicSalary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getBasicSalary() {
        return basicSalary;
    }
}


class ConsoleInput {

    private Scanner scanner = new Scanner(System.in);

    public void demoInput() {
        System.out.print("Enter employee ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();

        System.out.println("Employee: " + id + " - " + name);
    }
}


class SalaryBreakdown {
    private double basicSalary;
    private double allowance;
    private double deduction;
    private double netSalary;

    public SalaryBreakdown(double basicSalary, double allowance,
                           double deduction, double netSalary) {
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.deduction = deduction;
        this.netSalary = netSalary;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public double getDeduction() {
        return deduction;
    }

    public double getNetSalary() {
        return netSalary;
    }
}


class SalaryCalculator {

    
    public SalaryBreakdown calculateSalary(Employee employee) {

        double basic = employee.getBasicSalary();
        double allowance = 0;
        double deduction = 0;

        
        switch (employee.getCategory()) {
            case "Manager":
                allowance = basic * 0.30;
                deduction = basic * 0.10;
                break;

            case "Developer":
                allowance = basic * 0.20;
                deduction = basic * 0.08;
                break;

            case "HR":
                allowance = basic * 0.15;
                deduction = basic * 0.07;
                break;

            case "Intern":
                allowance = basic * 0.10;
                deduction = basic * 0.05;
                break;

            default:
                allowance = basic * 0.10;
                deduction = basic * 0.05;
        }

        
        if (basic >= 100000) {
            allowance += 5000;
        } else if (basic >= 50000) {
            allowance += 2000;
        } else {
            allowance += 500;
        }

        double grossSalary = basic + allowance;

        // Expression/operator used for net salary
        double netSalary = grossSalary - deduction;

        return new SalaryBreakdown(basic, allowance, deduction, netSalary);
    }
}


public class EmployeeSalaryCalculator extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField basicField;

    private JComboBox<String> categoryBox;
    private JTextArea slipArea;

    private SalaryCalculator salaryCalculator;
    private List<Employee> employees;

    public EmployeeSalaryCalculator() {

        salaryCalculator = new SalaryCalculator();
        employees = new ArrayList<>();

        setTitle("Employee Salary Calculator");
        setSize(900, 1100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("EMPLOYEE SALARY CALCULATOR", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));

        formPanel.add(new JLabel("Employee ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Employee Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Employee Category:"));
        String[] categories = {"Manager", "Developer", "HR", "Intern"};
        categoryBox = new JComboBox<>(categories);
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Basic Salary (₹):"));
        basicField = new JTextField();
        formPanel.add(basicField);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        JPanel controlsPanel = new JPanel(new BorderLayout(10, 15));
        controlsPanel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton calculateButton = new JButton("Calculate Salary");
        JButton clearButton = new JButton("Clear");
        JButton showAllButton = new JButton("Show All Employees");

        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(showAllButton);

        controlsPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(controlsPanel, BorderLayout.NORTH);

        slipArea = new JTextArea();
        // Give the output area enough vertical space to show a full salary slip.
        slipArea.setRows(24);
        slipArea.setEditable(false);
        slipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        slipArea.setBorder(BorderFactory.createTitledBorder("Salary Slip / Output"));

        JScrollPane scrollPane = new JScrollPane(slipArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        calculateButton.addActionListener(e -> calculateSalary());
        clearButton.addActionListener(e -> clearFields());
        showAllButton.addActionListener(e -> showAllEmployees());
    }

    
    private void calculateSalary() {

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();

            if (id.isEmpty() || name.isEmpty() || basicField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            double basicSalary = Double.parseDouble(basicField.getText().trim());

            if (basicSalary <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Basic salary must be greater than 0.",
                        "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Employee employee = new Employee(id, name, category, basicSalary);

            
            employees.add(employee);

            SalaryBreakdown result = salaryCalculator.calculateSalary(employee);

            displaySalarySlip(employee, result);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric salary.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    
    private void displaySalarySlip(Employee employee, SalaryBreakdown result) {

        StringBuilder output = new StringBuilder();

        output.append("============================================\n");
        output.append("              SALARY SLIP\n");
        output.append("============================================\n");

        output.append(String.format("Employee ID       : %s%n", employee.getId()));
        output.append(String.format("Employee Name     : %s%n", employee.getName()));
        output.append(String.format("Category          : %s%n", employee.getCategory()));

        output.append("--------------------------------------------\n");

        output.append(String.format("Basic Salary      : ₹%.2f%n", result.getBasicSalary()));
        output.append(String.format("Allowance         : ₹%.2f%n", result.getAllowance()));
        output.append(String.format("Deduction         : ₹%.2f%n", result.getDeduction()));

        output.append("--------------------------------------------\n");

        output.append(String.format("NET SALARY        : ₹%.2f%n", result.getNetSalary()));

        output.append("============================================\n");
        output.append("Employee saved successfully.\n");
        output.append("Total employees stored: " + employees.size() + "\n");

        slipArea.setText(output.toString());
    }

    // Method to demonstrate LOOP processing of multiple employees
    private void showAllEmployees() {

        if (employees.isEmpty()) {
            slipArea.setText("No employees have been registered yet.");
            return;
        }

        StringBuilder output = new StringBuilder();

        output.append("============================================================\n");
        output.append("                 ALL EMPLOYEE SALARIES\n");
        output.append("============================================================\n\n");

        double totalNetSalary = 0;

        // LOOP: processes all registered employees
        for (Employee employee : employees) {

            SalaryBreakdown result =
                    salaryCalculator.calculateSalary(employee);

            output.append("Employee ID   : ").append(employee.getId()).append("\n");
            output.append("Name          : ").append(employee.getName()).append("\n");
            output.append("Category      : ").append(employee.getCategory()).append("\n");
            output.append(String.format("Basic Salary  : ₹%.2f%n", result.getBasicSalary()));
            output.append(String.format("Allowance     : ₹%.2f%n", result.getAllowance()));
            output.append(String.format("Deduction     : ₹%.2f%n", result.getDeduction()));
            output.append(String.format("Net Salary    : ₹%.2f%n", result.getNetSalary()));

            output.append("--------------------------------------------\n");

            totalNetSalary += result.getNetSalary();
        }

        output.append(String.format(
                "%nTotal Net Salary of All Employees: ₹%.2f%n",
                totalNetSalary
        ));

        slipArea.setText(output.toString());
    }

    // Method to clear GUI fields
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        basicField.setText("");
        categoryBox.setSelectedIndex(0);
        slipArea.setText("");
    }

    public static void main(String[] args) {

        if (args.length > 0 && "--console".equalsIgnoreCase(args[0])) {
            runConsoleMode();
            return;
        }

        // Starts GUI safely using Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            EmployeeSalaryCalculator app = new EmployeeSalaryCalculator();
            app.setVisible(true);
        });
    }

    // Interactive terminal mode; the GUI remains the default when no option is given.
    private static void runConsoleMode() {
        Scanner scanner = new Scanner(System.in);
        SalaryCalculator calculator = new SalaryCalculator();
        List<Employee> employees = new ArrayList<>();

        System.out.println("EMPLOYEE SALARY CALCULATOR - CONSOLE MODE");
        while (true) {
            System.out.println("\n1. Register employee and calculate salary");
            System.out.println("2. Show all employee salaries");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if ("3".equals(choice)) {
                System.out.println("Goodbye.");
                return;
            } else if ("1".equals(choice)) {
                System.out.print("Employee ID: ");
                String id = scanner.nextLine().trim();
                System.out.print("Employee name: ");
                String name = scanner.nextLine().trim();
                if (id.isEmpty() || name.isEmpty()) {
                    System.out.println("ID and name are required.");
                    continue;
                }

                String category;
                while (true) {
                    System.out.print("Category (Manager, Developer, HR, Intern): ");
                    category = scanner.nextLine().trim();
                    if (category.equalsIgnoreCase("Manager") || category.equalsIgnoreCase("Developer")
                            || category.equalsIgnoreCase("HR") || category.equalsIgnoreCase("Intern")) {
                        category = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
                        break;
                    }
                    System.out.println("Choose Manager, Developer, HR, or Intern.");
                }

                System.out.print("Basic salary (₹): ");
                double basic;
                try {
                    basic = Double.parseDouble(scanner.nextLine().trim());
                    if (!Double.isFinite(basic) || basic <= 0) {
                        System.out.println("Salary must be a positive number.");
                        continue;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Enter a valid numeric salary.");
                    continue;
                }

                Employee employee = new Employee(id, name, category, basic);
                employees.add(employee);
                printConsoleSlip(employee, calculator.calculateSalary(employee));
            } else if ("2".equals(choice)) {
                if (employees.isEmpty()) {
                    System.out.println("No employees have been registered yet.");
                    continue;
                }
                double totalNet = 0;
                for (Employee employee : employees) {
                    SalaryBreakdown result = calculator.calculateSalary(employee);
                    printConsoleSlip(employee, result);
                    totalNet += result.getNetSalary();
                }
                System.out.printf("Total net salary: ₹%.2f%n", totalNet);
            } else {
                System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    private static void printConsoleSlip(Employee employee, SalaryBreakdown result) {
        System.out.println("\n---------------- SALARY SLIP ----------------");
        System.out.println("Employee ID   : " + employee.getId());
        System.out.println("Name          : " + employee.getName());
        System.out.println("Category      : " + employee.getCategory());
        System.out.printf("Basic salary  : ₹%.2f%n", result.getBasicSalary());
        System.out.printf("Allowance     : ₹%.2f%n", result.getAllowance());
        System.out.printf("Deduction     : ₹%.2f%n", result.getDeduction());
        System.out.printf("NET SALARY    : ₹%.2f%n", result.getNetSalary());
        System.out.println("---------------------------------------------");
    }
}

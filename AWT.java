import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeePayrollSystem extends Frame implements ActionListener {
    Label l1, l2, l3, l4, l5, l6, l7, l8, l9;
    TextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9;
    Button calcBtn, saveBtn, fetchBtn, updateBtn, addEmployeeBtn, updateEmployeeBtn, viewAllEmployeesBtn, backBtn;
    Connection conn;
    Panel mainPanel;

    public EmployeePayrollSystem() {
        setTitle("Employee Payroll System");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupDatabase();

        ScrollPane scrollPane = new ScrollPane();
        mainPanel = new Panel(new CardLayout());
        scrollPane.add(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        showHomePage();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    void setupDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:employee.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS employee_payroll (" +
                "emp_id TEXT PRIMARY KEY," +
                "name TEXT," +
                "designation TEXT," +
                "salary_per_day REAL," +
                "worked_days INTEGER," +
                "total_days INTEGER," +
                "allowances REAL," +
                "deductions REAL," +
                "net_salary REAL)");
        } catch (Exception e) {
            showError("Database Error: " + e.getMessage());
        }
    }

    void showHomePage() {
        Panel homePage = new Panel(new GridBagLayout());
        homePage.setBackground(Color.decode("#E8F0FE"));
        homePage.setFont(new Font("SansSerif", Font.BOLD, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        Label welcome = new Label("Employee Payroll System", Label.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        homePage.add(welcome, gbc);

        gbc.gridy = 1;
        addEmployeeBtn = new Button("Add New Employee");
        addEmployeeBtn.setBackground(new Color(76, 175, 80));
        addEmployeeBtn.setForeground(Color.WHITE);
        addEmployeeBtn.setPreferredSize(new Dimension(200, 40));
        addEmployeeBtn.addActionListener(this);
        homePage.add(addEmployeeBtn, gbc);

        gbc.gridy = 2;
        updateEmployeeBtn = new Button("Update Employee");
        updateEmployeeBtn.setBackground(new Color(33, 150, 243));
        updateEmployeeBtn.setForeground(Color.WHITE);
        updateEmployeeBtn.setPreferredSize(new Dimension(200, 40));
        updateEmployeeBtn.addActionListener(this);
        homePage.add(updateEmployeeBtn, gbc);

        gbc.gridy = 3;
        viewAllEmployeesBtn = new Button("View All Employees");
        viewAllEmployeesBtn.setBackground(new Color(156, 39, 176));
        viewAllEmployeesBtn.setForeground(Color.WHITE);
        viewAllEmployeesBtn.setPreferredSize(new Dimension(200, 40));
        viewAllEmployeesBtn.addActionListener(this);
        homePage.add(viewAllEmployeesBtn, gbc);

        mainPanel.removeAll();
        mainPanel.add(homePage);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void showAddEmployeePage() {
        Panel addPage = new Panel(new BorderLayout());
        Panel centerPanel = new Panel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        l1 = new Label("Employee ID:");        tf1 = new TextField(20);
        l2 = new Label("Name:");               tf2 = new TextField(20);
        l3 = new Label("Designation:");        tf3 = new TextField(20);
        l4 = new Label("Total Working Days:"); tf4 = new TextField(20);
        l5 = new Label("Worked Days:");        tf5 = new TextField(20);
        l6 = new Label("Salary per Day:");     tf6 = new TextField(20);
        l7 = new Label("Allowances:");         tf7 = new TextField("0", 20);
        l8 = new Label("Deductions:");         tf8 = new TextField("0", 20);
        l9 = new Label("Net Salary:");         tf9 = new TextField(20); tf9.setEditable(false);

        Label[] labels = {l1, l2, l3, l4, l5, l6, l7, l8, l9};
        TextField[] fields = {tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9};

        for (Label l : labels) {
            l.setFont(labelFont);
            l.setBackground(Color.WHITE);
        }

        for (TextField tf : fields) {
            tf.setFont(fieldFont);
            tf.setBackground(new Color(240, 240, 240));
            tf.setForeground(Color.BLACK);
        }

        calcBtn = new Button("Calculate Salary");
        saveBtn = new Button("Save to DB");

        Button[] buttons = {calcBtn, saveBtn};
        Color[] buttonColors = {
            new Color(255, 153, 51),
            new Color(0, 153, 76)
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 16));
            buttons[i].setBackground(buttonColors[i]);
            buttons[i].addActionListener(this);
        }

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            centerPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            centerPanel.add(fields[i], gbc);
        }

        Panel actionButtonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtonPanel.setBackground(Color.WHITE);
        actionButtonPanel.add(calcBtn);
        actionButtonPanel.add(saveBtn);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        centerPanel.add(actionButtonPanel, gbc);

        Panel paddedCenter = new Panel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(700, 550);
            }
        };
        paddedCenter.setBackground(Color.WHITE);

        Panel topPad = new Panel();    topPad.setPreferredSize(new Dimension(0, 30));
        Panel bottomPad = new Panel(); bottomPad.setPreferredSize(new Dimension(0, 30));
        Panel leftPad = new Panel();   leftPad.setPreferredSize(new Dimension(30, 0));
        Panel rightPad = new Panel();  rightPad.setPreferredSize(new Dimension(30, 0));

        paddedCenter.add(topPad, BorderLayout.NORTH);
        paddedCenter.add(bottomPad, BorderLayout.SOUTH);
        paddedCenter.add(leftPad, BorderLayout.WEST);
        paddedCenter.add(rightPad, BorderLayout.EAST);
        paddedCenter.add(centerPanel, BorderLayout.CENTER);

        Panel centerWrapper = new Panel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        centerWrapper.setBackground(Color.decode("#E8F0FE"));
        centerWrapper.add(paddedCenter);

        Panel backButtonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(Color.decode("#E8F0FE"));
        backBtn = new Button("Back to Home");
        backBtn.setBackground(new Color(244, 67, 54));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.addActionListener(this);
        backButtonPanel.add(backBtn);

        addPage.add(centerWrapper, BorderLayout.CENTER);
        addPage.add(backButtonPanel, BorderLayout.SOUTH);

        mainPanel.removeAll();
        mainPanel.add(addPage);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void showUpdateEmployeePage() {
        Panel updatePage = new Panel(new BorderLayout());
        Panel centerPanel = new Panel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        l1 = new Label("Employee ID:");        tf1 = new TextField(20);
        l2 = new Label("Name:");               tf2 = new TextField(20);
        l3 = new Label("Designation:");        tf3 = new TextField(20);
        l4 = new Label("Total Working Days:"); tf4 = new TextField(20);
        l5 = new Label("Worked Days:");        tf5 = new TextField(20);
        l6 = new Label("Salary per Day:");     tf6 = new TextField(20);
        l7 = new Label("Allowances:");         tf7 = new TextField("0", 20);
        l8 = new Label("Deductions:");         tf8 = new TextField("0", 20);
        l9 = new Label("Net Salary:");         tf9 = new TextField(20); tf9.setEditable(false);

        Label[] labels = {l1, l2, l3, l4, l5, l6, l7, l8, l9};
        TextField[] fields = {tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9};

        for (Label l : labels) {
            l.setFont(labelFont);
            l.setBackground(Color.WHITE);
        }

        for (TextField tf : fields) {
            tf.setFont(fieldFont);
            tf.setBackground(new Color(240, 240, 240));
            tf.setForeground(Color.BLACK);
        }

        fetchBtn = new Button("Fetch Employee");
        calcBtn = new Button("Calculate Salary");
        updateBtn = new Button("Update Employee");
        Button clearBtn = new Button("Clear Fields");

        Button[] buttons = {fetchBtn, calcBtn, updateBtn, clearBtn};
        Color[] buttonColors = {
            new Color(33, 150, 243),
            new Color(255, 153, 51),
            new Color(0, 153, 76),
            new Color(117, 117, 117)
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 16));
            buttons[i].setBackground(buttonColors[i]);
            buttons[i].addActionListener(this);
        }

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            centerPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            centerPanel.add(fields[i], gbc);
        }

        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(fetchBtn);
        buttonPanel.add(calcBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);

        Panel paddedCenter = new Panel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(700, 550);
            }
        };
        paddedCenter.setBackground(Color.WHITE);

        Panel topPad = new Panel();    topPad.setPreferredSize(new Dimension(0, 30));
        Panel bottomPad = new Panel(); bottomPad.setPreferredSize(new Dimension(0, 30));
        Panel leftPad = new Panel();   leftPad.setPreferredSize(new Dimension(30, 0));
        Panel rightPad = new Panel();  rightPad.setPreferredSize(new Dimension(30, 0));

        paddedCenter.add(topPad, BorderLayout.NORTH);
        paddedCenter.add(bottomPad, BorderLayout.SOUTH);
        paddedCenter.add(leftPad, BorderLayout.WEST);
        paddedCenter.add(rightPad, BorderLayout.EAST);
        paddedCenter.add(centerPanel, BorderLayout.CENTER);

        Panel centerWrapper = new Panel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        centerWrapper.setBackground(Color.decode("#E8F0FE"));
        centerWrapper.add(paddedCenter);

        Panel backButtonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(Color.decode("#E8F0FE"));
        backBtn = new Button("Back to Home");
        backBtn.setBackground(new Color(244, 67, 54));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.addActionListener(this);
        backButtonPanel.add(backBtn);

        updatePage.add(centerWrapper, BorderLayout.CENTER);
        updatePage.add(backButtonPanel, BorderLayout.SOUTH);

        mainPanel.removeAll();
        mainPanel.add(updatePage);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void showViewAllEmployeesPage() {
        Panel viewPage = new Panel(new BorderLayout());
        viewPage.setBackground(Color.decode("#E8F0FE"));

        Panel centerPanel = new Panel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        Label titleLabel = new Label("All Employees Details", Label.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243));
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        int numEmployees = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM employee_payroll");
            if (rsCount.next()) {
                numEmployees = rsCount.getInt(1);
            }
        } catch (SQLException sqle) {
            showError("SQL Error: " + sqle.getMessage());
            return;
        }

        final int columns = 3;
        int rows = (numEmployees + columns - 1) / columns;
        if (rows == 0) rows = 1;

        Panel employeesPanel = new Panel(new GridLayout(rows, columns, 20, 20)) {
            @Override
            public Dimension getPreferredSize() {
                int employeeBoxWidth = 250;
                int hgap = 20;
                int totalWidth = columns * employeeBoxWidth + (columns - 1) * hgap;
                int employeeBoxHeight = 400;
                int vgap = 20;
                int componentCount = getComponentCount();
                int actualRows = (componentCount + columns - 1) / columns;
                int totalHeight = actualRows * employeeBoxHeight + (actualRows - 1) * vgap;
                return new Dimension(totalWidth, totalHeight);
            }
        };
        employeesPanel.setBackground(Color.WHITE);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee_payroll");
            while (rs.next()) {
                Panel employeeBox = new Panel(new GridBagLayout());
                employeeBox.setBackground(new Color(245, 245, 245));
                employeeBox.setPreferredSize(new Dimension(250, 400));

                Panel boxWrapper = new Panel(new BorderLayout());
                boxWrapper.setBackground(new Color(200, 200, 200));
                Panel topPad = new Panel();    topPad.setPreferredSize(new Dimension(0, 10));
                Panel bottomPad = new Panel(); bottomPad.setPreferredSize(new Dimension(0, 10));
                Panel leftPad = new Panel();   leftPad.setPreferredSize(new Dimension(10, 0));
                Panel rightPad = new Panel();  rightPad.setPreferredSize(new Dimension(10, 0));
                boxWrapper.add(topPad, BorderLayout.NORTH);
                boxWrapper.add(bottomPad, BorderLayout.SOUTH);
                boxWrapper.add(leftPad, BorderLayout.WEST);
                boxWrapper.add(rightPad, BorderLayout.EAST);
                boxWrapper.add(employeeBox, BorderLayout.CENTER);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 15, 5, 15);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;

                Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
                Label idLabel = new Label("ID: " + rs.getString("emp_id"));
                idLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                employeeBox.add(idLabel, gbc);
                gbc.gridy++;

                Label nameLabel = new Label("Name: " + rs.getString("name"));
                nameLabel.setFont(labelFont);
                employeeBox.add(nameLabel, gbc);
                gbc.gridy++;

                Label designationLabel = new Label("Designation: " + rs.getString("designation"));
                designationLabel.setFont(labelFont);
                employeeBox.add(designationLabel, gbc);
                gbc.gridy++;

                Label totalDaysLabel = new Label("Total Days: " + rs.getInt("total_days"));
                totalDaysLabel.setFont(labelFont);
                employeeBox.add(totalDaysLabel, gbc);
                gbc.gridy++;

                Label workedDaysLabel = new Label("Worked Days: " + rs.getInt("worked_days"));
                workedDaysLabel.setFont(labelFont);
                employeeBox.add(workedDaysLabel, gbc);
                gbc.gridy++;

                Label salaryLabel = new Label("Salary/Day: $" + String.format("%.2f", rs.getDouble("salary_per_day")));
                salaryLabel.setFont(labelFont);
                employeeBox.add(salaryLabel, gbc);
                gbc.gridy++;

                Label allowancesLabel = new Label("Allowances: $" + String.format("%.2f", rs.getDouble("allowances")));
                allowancesLabel.setFont(labelFont);
                employeeBox.add(allowancesLabel, gbc);
                gbc.gridy++;

                Label deductionsLabel = new Label("Deductions: $" + String.format("%.2f", rs.getDouble("deductions")));
                deductionsLabel.setFont(labelFont);
                employeeBox.add(deductionsLabel, gbc);
                gbc.gridy++;

                Label netSalaryLabel = new Label("Net Salary: $" + String.format("%.2f", rs.getDouble("net_salary")));
                netSalaryLabel.setFont(labelFont);
                employeeBox.add(netSalaryLabel, gbc);

                employeesPanel.add(boxWrapper);
            }
        } catch (SQLException sqle) {
            showError("SQL Error: " + sqle.getMessage());
        }

        while (employeesPanel.getComponentCount() < rows * columns) {
            Panel emptyPanel = new Panel();
            emptyPanel.setPreferredSize(new Dimension(250, 400));
            emptyPanel.setBackground(new Color(245, 245, 245));
            employeesPanel.add(emptyPanel);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.add(employeesPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        Panel backButtonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(Color.decode("#E8F0FE"));
        backBtn = new Button("Back to Home");
        backBtn.setBackground(new Color(244, 67, 54));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.addActionListener(this);
        backButtonPanel.add(backBtn);

        Panel paddedCenter = new Panel(new BorderLayout());
        paddedCenter.setBackground(Color.WHITE);

        Panel topPad = new Panel();    topPad.setPreferredSize(new Dimension(0, 30));
        Panel bottomPad = new Panel(); bottomPad.setPreferredSize(new Dimension(0, 30));
        Panel leftPad = new Panel();   leftPad.setPreferredSize(new Dimension(30, 0));
        Panel rightPad = new Panel();  rightPad.setPreferredSize(new Dimension(30, 0));

        paddedCenter.add(topPad, BorderLayout.NORTH);
        paddedCenter.add(bottomPad, BorderLayout.SOUTH);
        paddedCenter.add(leftPad, BorderLayout.WEST);
        paddedCenter.add(rightPad, BorderLayout.EAST);
        paddedCenter.add(centerPanel, BorderLayout.CENTER);

        Panel centerWrapper = new Panel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        centerWrapper.setBackground(Color.decode("#E8F0FE"));
        centerWrapper.add(paddedCenter);

        viewPage.add(centerWrapper, BorderLayout.CENTER);
        viewPage.add(backButtonPanel, BorderLayout.SOUTH);

        mainPanel.removeAll();
        mainPanel.add(viewPage);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == addEmployeeBtn) {
                showAddEmployeePage();
            } else if (ae.getSource() == updateEmployeeBtn) {
                showUpdateEmployeePage();
            } else if (ae.getSource() == viewAllEmployeesBtn) {
                showViewAllEmployeesPage();
            } else if (ae.getSource() == backBtn) {
                showHomePage();
            } else if (ae.getSource() == calcBtn) {
                int totalDays = Integer.parseInt(tf4.getText());
                int workedDays = Integer.parseInt(tf5.getText());
                double salaryPerDay = Double.parseDouble(tf6.getText());
                double allowances = Double.parseDouble(tf7.getText());
                double deductions = Double.parseDouble(tf8.getText());

                if (totalDays <= 0) {
                    showError("Total working days must be greater than 0.");
                    return;
                }

                if (workedDays > totalDays) {
                    showError("Worked days cannot exceed total working days.");
                    return;
                }

                double net = (workedDays * salaryPerDay) + allowances - deductions;
                tf9.setText(String.format("%.2f", net));
            } else if (ae.getSource() == saveBtn) {
                if (tf1.getText().isEmpty() || tf2.getText().isEmpty() || tf3.getText().isEmpty() ||
                    tf4.getText().isEmpty() || tf5.getText().isEmpty() ||
                    tf6.getText().isEmpty() || tf7.getText().isEmpty() ||
                    tf8.getText().isEmpty() || tf9.getText().isEmpty()) {
                    showError("Please complete all fields before saving.");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employee_payroll VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, tf1.getText());
                ps.setString(2, tf2.getText());
                ps.setString(3, tf3.getText());
                ps.setDouble(4, Double.parseDouble(tf6.getText()));
                ps.setInt(5, Integer.parseInt(tf5.getText()));
                ps.setInt(6, Integer.parseInt(tf4.getText()));
                ps.setDouble(7, Double.parseDouble(tf7.getText()));
                ps.setDouble(8, Double.parseDouble(tf8.getText()));
                ps.setDouble(9, Double.parseDouble(tf9.getText()));
                ps.executeUpdate();

                showSuccess("Record Saved Successfully.");
            } else if (ae.getSource() == fetchBtn) {
                if (tf1.getText().isEmpty()) {
                    showError("Please enter an Employee ID to fetch.");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM employee_payroll WHERE emp_id = ?"
                );
                ps.setString(1, tf1.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tf2.setText(rs.getString("name"));
                    tf3.setText(rs.getString("designation"));
                    tf4.setText(String.valueOf(rs.getInt("total_days")));
                    tf5.setText(String.valueOf(rs.getInt("worked_days")));
                    tf6.setText(String.valueOf(rs.getDouble("salary_per_day")));
                    tf7.setText(String.valueOf(rs.getDouble("allowances")));
                    tf8.setText(String.valueOf(rs.getDouble("deductions")));
                    tf9.setText(String.valueOf(rs.getDouble("net_salary")));
                } else {
                    showError("Employee not found.");
                }
            } else if (ae.getSource() == updateBtn) {
                if (tf1.getText().isEmpty() || tf2.getText().isEmpty() || tf3.getText().isEmpty() ||
                    tf4.getText().isEmpty() || tf5.getText().isEmpty() ||
                    tf6.getText().isEmpty() || tf7.getText().isEmpty() ||
                    tf8.getText().isEmpty() || tf9.getText().isEmpty()) {
                    showError("Please complete all fields before updating.");
                    return;
                }

                Dialog confirmDialog = new Dialog(this, "Confirm Update", true);
                confirmDialog.setLayout(new FlowLayout());
                confirmDialog.add(new Label("Are you sure you want to update this employee?"));
                Button confirmBtn = new Button("Yes");
                Button cancelBtn = new Button("No");

                confirmBtn.addActionListener(e -> {
                    try {
                        PreparedStatement ps = conn.prepareStatement(
                            "UPDATE employee_payroll SET name = ?, designation = ?, total_days = ?, worked_days = ?, salary_per_day = ?, allowances = ?, deductions = ?, net_salary = ? WHERE emp_id = ?"
                        );
                        ps.setString(1, tf2.getText());
                        ps.setString(2, tf3.getText());
                        ps.setInt(3, Integer.parseInt(tf4.getText()));
                        ps.setInt(4, Integer.parseInt(tf5.getText()));
                        ps.setDouble(5, Double.parseDouble(tf6.getText()));
                        ps.setDouble(6, Double.parseDouble(tf7.getText()));
                        ps.setDouble(7, Double.parseDouble(tf8.getText()));
                        ps.setDouble(8, Double.parseDouble(tf9.getText()));
                        ps.setString(9, tf1.getText());
                        ps.executeUpdate();

                        showSuccess("Record Updated Successfully!");
                        confirmDialog.setVisible(false);
                    } catch (SQLException se) {
                        showError("SQL Error: " + se.getMessage());
                    }
                });

                cancelBtn.addActionListener(e -> confirmDialog.setVisible(false));

                confirmDialog.add(confirmBtn);
                confirmDialog.add(cancelBtn);
                confirmDialog.setSize(300, 120);
                confirmDialog.setLocationRelativeTo(this);
                confirmDialog.setVisible(true);
            } else if (ae.getSource() instanceof Button && ae.getActionCommand().equals("Clear Fields")) {
                tf1.setText("");
                tf2.setText("");
                tf3.setText("");
                tf4.setText("");
                tf5.setText("");
                tf6.setText("");
                tf7.setText("0");
                tf8.setText("0");
                tf9.setText("");
            }
        } catch (NumberFormatException nfe) {
            showError("Please enter valid numeric values.");
        } catch (SQLException se) {
            showError("SQL Error: " + se.getMessage());
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    void showError(String msg) {
        Dialog d = new Dialog(this, "Error", true);
        d.setLayout(new FlowLayout());
        d.add(new Label(msg));
        Button b = new Button("OK");
        b.addActionListener(e -> d.setVisible(false));
        d.add(b);
        d.setSize(300, 100);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    void showSuccess(String msg) {
        Dialog d = new Dialog(this, "Success", true);
        d.setLayout(new FlowLayout());
        d.add(new Label(msg));
        Button b = new Button("OK");
        b.addActionListener(e -> d.setVisible(false));
        d.add(b);
        d.setSize(300, 100);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        new EmployeePayrollSystem();
    }
}

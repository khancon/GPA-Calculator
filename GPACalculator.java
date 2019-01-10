/* Name: Ahnaf Khan (aak6sk)
 * Computing ID: aak6sk
 * Instructor: Basit*/

// Source: docs.oracle.com
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class GPACalculator{
	
	//Private instance variables
	//Used for calculating current GPA
	private ArrayList<Double> creditValues, gradeValues;
	private String[] grades = {null,"A","A-","B+","B","B-","C+","C","C-","D+","D","F"};
	private double[] numGrades = {3.0, 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.3, 1.0, 0.0};
	//All Major JFrames, JPanels, JLabels, JTextFields, and JButtons in program 
	private JFrame frame;
	//Frame adds masterPanel of BorderLayout which has a topPanel (North), midPanel (Center), and bottomPanel (South)
	//topPanel of BorderLayout has
		// courseTitle (located North) panel of GridLayout has Grid Layout for the headings of the different course row titles: Course Name, Grade, Credit Hours, Enter Course Info, Delete Row, and Status
		// coursePanel (located South) contains the actual course info
	//midPanel contains the targetGPA panel, currentGPA panel, requiredGPA panel, and a warningPanel panel for various warnings
	//bottomPanel contains the buttons for the various functions for the GUI
	private JPanel masterPanel;
	private JPanel topPanel, coursePanel, courseTitle, directionPanel, bottomPanel;
	private JPanel midPanel, currentPanel, targetPanel, requiredPanel, warningPanel;
	private JLabel currentGPALabel, currentGPADisplay, requiredGPALabel, requiredGPADisplay, targetGPALabel, warningLabel;
	private JTextField targetGPATextField;
	private JScrollPane scrollPane;
	private JButton addCourse, refresh, currentGPAButton, requiredGPAButton;
	//other private variables for program bookkeeping;
	private int courseTotal;
	private final static int columns = 6;
	private final static int FONT_SIZE = 16;
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GPACalculator theApp = new GPACalculator(); // our GUI application - call to constructor 
			}
		});
	}
	
	public GPACalculator(){
    	createAndShowGUI();
    }
	public void createAndShowGUI() {
		
		//store the actual data for credits entered and grades entered
    	creditValues = new ArrayList<Double>();
    	gradeValues = new ArrayList<Double>();
    	
        // Start --- Create and set up the window.
        frame = new JFrame("SimpleFrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350,450);
		frame.setTitle("GPA Calculator");
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		
		//set up main panels to include in frame	
		masterPanel = new JPanel(new BorderLayout());
		topPanel = new JPanel(new BorderLayout());
		midPanel = new JPanel(new GridLayout(4, 1));
		bottomPanel = new JPanel(new FlowLayout());
		
		//from top to bottom - input directionPanel that contains directions on how to use GUI app; goes into topPanel
		directionPanel = new JPanel(new GridLayout(1,1));
		
		//directions 
		JTextArea directions = new JTextArea("*NOTE: Must manually press enter course button for each course info in order to calculate GPA. Course rows can be deleted only if that course has not already been entered. Current GPA is calculated using rows that contain both grades and credit-hours entered. Must enter credit-hours without grades in order to calculate required GPA based on target GPA entered and current GPA.*");
		directions.setLineWrap(true);
		directions.setWrapStyleWord(true);
		directions.setEditable(false);
		directions.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		directionPanel.add(directions);
		
		//Courses title/input panel goes into topPanel, underneath directionPanel
		courseTitle = new JPanel(new GridLayout(1,columns));
		
		//Course panel headings		
		JLabel courseTitleLabel = new JLabel("COURSE NAME", JLabel.CENTER);
		courseTitleLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		JLabel gradeTitleLabel = new JLabel("GRADE", JLabel.CENTER);
		gradeTitleLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		JLabel creditHoursLabel = new JLabel("CREDIT-HOURS", JLabel.CENTER);
		creditHoursLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		JLabel enterButtonLabel = new JLabel("ENTER", JLabel.CENTER);
		enterButtonLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		JLabel deleteButtonLabel = new JLabel("DELETE ROW", JLabel.CENTER);
		deleteButtonLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		JLabel statusLabel = new JLabel("STATUS", JLabel.CENTER);
		statusLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		
		//adding course panel headings to courseTitle panel
		courseTitle.add(courseTitleLabel);
		courseTitle.add(gradeTitleLabel);
		courseTitle.add(creditHoursLabel);
		courseTitle.add(enterButtonLabel);
		courseTitle.add(deleteButtonLabel);
		courseTitle.add(statusLabel);
		

		//Initialize coursePanel - contains all the course rows and input fields
		//Create initial five lines of Course (Text Field)		Grade (ComboBox)	Credits (JTextField) in coursePanel
		//More courses can be added into coursePanel with the addCourse button in bottomPanel
		coursePanel = new JPanel(new GridLayout(courseTotal, columns));
		for(int i = 0; i < 5; i++) {
	    	addCourseLine(i+1); //written down below
	    	courseTotal++; //with each course added, update total number of courses inputted
		}
		
		//Initialize midPanel - displays Target GPA (with textfield for input), Current GPA and required gpa
		//method written down below
		initializeGPAPanel(midPanel);
		

		// Create add Course line button with function to add a course line
		// added to bottomPanel down below
		addCourse = new JButton("Add Course");
		addCourse.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		addCourse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCourseLine(courseTotal+1); //defined below
				courseTotal++;
				frame.setVisible(true);
			}
		});
		
		//If the user wants to start over, they can hit the refresh button
		//same thing as adding 15 blank credit-hours rows or 5 course rows of 3 credits each
		//clears out all previous data entered in previous run before refresh button is hit
		refresh = new JButton("Refresh All (Add 15 Blank Credit-Hours)");
		refresh.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseTotal = 0;
				coursePanel.removeAll();
				coursePanel.updateUI();
				for(int i = 0; i < 5; i++) {
			    	addCourseLine(i+1);
			    	courseTotal++;
				}
				currentGPAButton.setEnabled(false);
		    	requiredGPAButton.setEnabled(false);
				
				creditValues.clear();
				gradeValues.clear();
				
		    	currentGPADisplay.setText("-.-");
				currentGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
				
				requiredGPADisplay.setText("-.-");
				requiredGPADisplay.setForeground(Color.BLACK);
				requiredGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
				
				targetGPATextField.setText("0.0");
				targetGPATextField.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
				
				warningLabel.setText("");
				warningLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));

			}
			
		});
		
		//initialization of button that calculates just the current GPA based on the rows that contain BOTH grade and credits entered
		currentGPAButton = new JButton("Calculate Current GPA");
		currentGPAButton.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		currentGPAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//try - catch method to properly handle wrong inputs for the credits textfield (can only take doubles or ints)
				try {
					currentGPADisplay.setText("" + calculateCurrentGPA(gradeValues,creditValues));
					currentGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));	 
				}
				catch (Exception ex) {
					warningLabel.setText("***WARNING: CREDIT-HOUR INPUTS MUST BE DECIMAL VALUES***");
					warningLabel.setForeground(Color.RED);
					warningLabel.setFont(new Font("Serif", Font.BOLD, 24));
				}
			}
			
		});
		//set enabled to false until at least one course line with both credit and grade is entered (enabled to true in the addCourse method)
		currentGPAButton.setEnabled(false);
		
		//required GPA button that calculates the required GPA based on target gpa entered in textfield, current GPA, and taken, untaken, and total credits entered
		requiredGPAButton = new JButton("Calculate Required GPA");
		requiredGPAButton.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		requiredGPAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//try-catch method to properly handle wrong inputs for the targetGPA textfield (can only take doubles or inputs)
				try {
					double requiredGPA = calculateRequiredGPA(gradeValues, creditValues);
					//if requiredGPA for set of blank credits is greater than the max of 4.0 --> display warning with suggestions
					if(requiredGPA > 4.0) {
						requiredGPADisplay.setText("" + calculateRequiredGPA(gradeValues, creditValues));
						requiredGPADisplay.setForeground(Color.RED);
						requiredGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
						
						warningLabel.setText("*GPA TOO HIGH. ADD MORE CREDIT HOURS OR LOWER YOUR TARGET GPA*");
						warningLabel.setForeground(Color.RED);
						warningLabel.setFont(new Font("Serif", Font.BOLD, 24));
					}
					//if requiredGPA for set of blank credits is less than 2.0 --> display warning with suggestions
					else if(requiredGPA < 2.0) {
						requiredGPADisplay.setText("" + calculateRequiredGPA(gradeValues, creditValues));
						requiredGPADisplay.setForeground(Color.RED);
						requiredGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
						
						warningLabel.setText("*GPA TOO LOW. ADD FEWER CREDIT HOURS OR RAISE YOUR TARGET GPA*");
						warningLabel.setForeground(Color.RED);
						warningLabel.setFont(new Font("Serif", Font.BOLD, 24));
					}
					else {
						requiredGPADisplay.setText("" + calculateRequiredGPA(gradeValues, creditValues));
						requiredGPADisplay.setForeground(Color.BLACK);
						requiredGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
						
						warningLabel.setText("");
						warningLabel.setFont(new Font("Serif", Font.BOLD, 16));
					}
				}
				catch (Exception ex) {
					warningLabel.setText("***WARNING: TARGET GPA MUST BE A DECIMAL VALUE.***");
					warningLabel.setForeground(Color.RED);
					warningLabel.setFont(new Font("Serif", Font.BOLD, 24));
				}
			}
		});
		//set enabled to false until at least one course line with a valid credit value is entered (enabled to true in the addCourse method)
		requiredGPAButton.setEnabled(false);
		
		//add topPanel stuff accordingly
		topPanel.add(directionPanel, BorderLayout.NORTH);
		topPanel.add(courseTitle, BorderLayout.CENTER);
		topPanel.add(coursePanel, BorderLayout.SOUTH);
		
		//add bottomPanel stuff accordingly (midPanel stuff already added above)
		bottomPanel.add(addCourse);
		bottomPanel.add(refresh);	
		bottomPanel.add(currentGPAButton);
		bottomPanel.add(requiredGPAButton);
		
		//add all three panels to the mainPanel
		masterPanel.add(topPanel, BorderLayout.NORTH);
		masterPanel.add(midPanel, BorderLayout.CENTER);
		masterPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		//allow the masterPanel to be scroll-able to easily adjust window size
		scrollPane = new JScrollPane(masterPanel);
		
		//final frame stuff
		frame.add(scrollPane);
        frame.setLocationRelativeTo(null); // Center it on Screen
        frame.setVisible(true);// Set visibility to true
    }
	
	//returns double --> calculates required GPA
	public double calculateRequiredGPA(ArrayList<Double> grades, ArrayList<Double> credits) {
		//Formula for requiredGPA = [(targetGPA * totalCredits) - (currentGPA - currentCredits)] / untaken credits, according to collab
		
		//targetGPA given by user input
		double targetGPA = Double.parseDouble(targetGPATextField.getText());
		
		//Calculating totalCredits
		double totalCredits = 0.0;
	    for(double element : credits) {
	    	totalCredits += element;
	    }
	    
		//calculating currentGPA
		double currentGPA = calculateCurrentGPA(grades, credits);    

	    //Calculating currentCredits
	    double currentCredits = 0;
	    for(int i = 0; i < grades.size(); i++)
	    {
	    	currentCredits += credits.get(i);
	    }

	    //Calculating untakenCredits
	    double untakenCredits = totalCredits - currentCredits;

	    //Calculating requiredGPA
		double requiredGPA = ((totalCredits * targetGPA)-(currentGPA*currentCredits))/untakenCredits;
		return requiredGPA;
	}
	//calculates current GPA based on lines entered with both grade and credit
	public double calculateCurrentGPA(ArrayList<Double> grades, ArrayList<Double> credits)
	{
		double sum = 0.0;
		double creditSum = 0.0;
	    for(int i = 0; i < grades.size(); i++)
	    {
	        sum += (grades.get(i)*credits.get(i));
	        creditSum += credits.get(i);
	        System.out.println("Run " + i + ": " + "Grade = " + grades.get(i) + ", Credit = " + credits.get(i));
	    }
	    double currentGPA = sum/creditSum;
	    return currentGPA;
	}

	//midPanel GPA stuff initialization
	public void initializeGPAPanel(JPanel mpanel) {
		currentPanel = new JPanel(new GridLayout(1,2));
		requiredPanel = new JPanel(new GridLayout(1,2));
		targetPanel = new JPanel(new GridLayout(1,2));
		warningPanel = new JPanel(new FlowLayout());
		
		//current GPA GUIs - need JLabel for "Current GPA: " and another JLabel that displays Current GPA
		/* MUST HAVE A BUTTON THAT SAYS CALCULATE Current GPA*/
		currentGPALabel = new JLabel("CURRENT GPA: ", JLabel.LEFT);
		currentGPALabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		currentGPADisplay = new JLabel("-.-", JLabel.CENTER);
		currentGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		currentPanel.add(currentGPALabel); currentPanel.add(currentGPADisplay);
		
		//required GPA GUIs - need JLabel for "Required GPA: " and another JLabel that displays the Required GPA
		/* MUST HAVE A BUTTON THAT "Calculate Required GPA" which will remain inactive until both 
		  Current GPA and Target GPA parameters have been filled*/
		requiredGPALabel = new JLabel("GPA REQUIRED ON ENTERED UNFILLED CREDIT-HOURS: ", JLabel.LEFT);
		requiredGPALabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		requiredGPADisplay = new JLabel("-.-", JLabel.CENTER);
		requiredGPADisplay.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		requiredPanel.add(requiredGPALabel); requiredPanel.add(requiredGPADisplay);
		
		//target GPA GUIs - need JLabel for "Target GPA: " and a JTextField where user can enter Target GPA
		targetGPALabel = new JLabel("ENTER TARGET GPA: ", JLabel.LEFT);
		targetGPALabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		targetGPATextField = new JTextField("0.0", JTextField.CENTER);
		targetGPATextField.setHorizontalAlignment(JLabel.CENTER);
		targetGPATextField.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		targetPanel.add(targetGPALabel); targetPanel.add(targetGPATextField);
		
		//warning label - need JLabel that alerts whether or not Required GPA is above 4.0 or below 2.0
		warningLabel = new JLabel("", JLabel.CENTER);
		warningLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
		warningPanel.add(warningLabel);
		
		mpanel.add(targetPanel);
		mpanel.add(currentPanel);
		mpanel.add(requiredPanel);
		mpanel.add(warningPanel);
	}
	
	//adds new courseLine	
	public void addCourseLine(int index) {	  	
		//enter course name (optional)
    	JTextField course = new JTextField("Enter Course Name", JTextField.CENTER);
    	course.setHorizontalAlignment(JLabel.CENTER);
    	course.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		this.coursePanel.add(course);
    	
		//grades located in combobox
		JComboBox grade = new JComboBox(grades);
		((JLabel)grade.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		grade.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		this.coursePanel.add(grade);
		
		//textfield to enter double or int-value credits
		JTextField credit = new JTextField("3.0", JTextField.CENTER);
    	credit.setHorizontalAlignment(JLabel.CENTER);
		credit.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		//this.creditsList.add(credit);
		this.coursePanel.add(credit);
		
		//enter course info and delete row buttons
		JButton enter = new JButton("Enter Course " + index);
		enter.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		JButton delete = new JButton("Delete");
		delete.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
		
		//indicates status of each row after certain button is pressed and based on what has been entered
		JLabel status = new JLabel("No Action", JLabel.CENTER);
		status.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));

		//enter button function
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String creditText = credit.getText();
					String gradeText = (String) grade.getSelectedItem();
					String courseText = course.getText();
					final String targetText = targetGPATextField.getText();
					if(creditText != null) {
						int position = 0;
						creditValues.add(Double.parseDouble(creditText));
						if(gradeText != null) {
							for(int i = 0; i < grades.length; i++) {
								if(grades[i] == gradeText) {
									position = i;
								}
							}
							gradeValues.add(numGrades[position]);
							status.setText("GRADES & CREDITS ENTERED");
							status.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
							currentGPAButton.setEnabled(true);
						}
						else {
							status.setText("CREDITS ENTERED");
							status.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
						}
						if(targetText != "0.0") {
							requiredGPAButton.setEnabled(true);
						}
					}
					credit.setEditable(false);
					grade.setEditable(false);	
					course.setEditable(false);
					enter.setEnabled(false);
					delete.setEnabled(false);
				}
				//catches whether credit-hours input is entered as either int or double
				catch(NumberFormatException numEx) {
					warningLabel.setText("***WARNING: CREDIT-HOUR INPUTS MUST BE DECIMAL VALUES.***");
					warningLabel.setForeground(Color.RED);
					warningLabel.setFont(new Font("Serif", Font.BOLD, 24));
				}
				
			}
		});
		this.coursePanel.add(enter);
		
		//Deleting a single row will simply disable all the textfields and interactive properties of the java swing/awt
		//components of that row, without any contribution to the current GPA. However, once a course has been entered, it cannot be deleted
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				credit.setEditable(false);
				grade.setEditable(false);	
				course.setEditable(false);
				enter.setEnabled(false);
				delete.setEnabled(false);
				status.setText("COURSE DELETED");
				status.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
			}
		});
		this.coursePanel.add(delete);
		this.coursePanel.add(status);
		
	}
}

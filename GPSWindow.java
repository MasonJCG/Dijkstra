import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;

/**
 * @author Mason Grant Description: A class that makes a GUI for easier user
 *         navigation.
 */
public class GPSWindow {

	private JFrame frmProjectGps;
	private JTextField enterMapTextBox;
	private JComboBox startComboBox;
	private JComboBox endComboBox;

	boolean txtFile = true;
	boolean dataLoaded = false;
	int travel = 0;
	Graph g;
	private final ButtonGroup costGroup = new ButtonGroup();
	private JRadioButton timeButton;
	private JRadioButton distButton;
	private JTextArea printPathsArea;
	private JRadioButton symbolsButton;
	private JRadioButton addressButton;
	private final ButtonGroup formatGroup = new ButtonGroup();
	private JScrollPane scrollPane;
	private final ButtonGroup travelGroup = new ButtonGroup();
	private JRadioButton carButton;
	private JRadioButton bikeButton;
	private JRadioButton footButton;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JRadioButton highwaysButton;
	private JLabel lblNewLabel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GPSWindow window = new GPSWindow();
					window.frmProjectGps.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GPSWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmProjectGps = new JFrame();
		frmProjectGps.setTitle("Project4 GPS");
		frmProjectGps.setBounds(100, 100, 700, 500);
		frmProjectGps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProjectGps.getContentPane().setLayout(null);

		enterMapTextBox = new JTextField();
		enterMapTextBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
		enterMapTextBox.setHorizontalAlignment(SwingConstants.CENTER);
		enterMapTextBox.setText("Enter Map Text File");
		enterMapTextBox.setBounds(10, 11, 131, 37);
		frmProjectGps.getContentPane().add(enterMapTextBox);
		enterMapTextBox.setColumns(10);

		JButton loadFileButton = new JButton("Load File");
		loadFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printPathsArea.setText("");

				String fileName = enterMapTextBox.getText();

				// We get the file name and make sure it's a valid file and file type.
				// If it's valid, then we load the data into the comboBoxes.
				try {
					Scanner kb = new Scanner(new File(fileName));

					g = new Graph(enterMapTextBox.getText());
					if (!(fileName.substring(fileName.length() - 4, fileName.length()).equals(".txt"))) {
						txtFile = false;
						enterMapTextBox.setText("Invalid file type, try again.");
					} else {
						for (Vertex v : g.verticesIterable) {
							String item = v.symbol + " " + v.address;
							startComboBox.addItem(item);
							endComboBox.addItem(item);
						}
						dataLoaded = true;
					}
				} catch (Exception f) {
					enterMapTextBox.setText("Invalid file, try again.");
				}

			}
		});
		loadFileButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		loadFileButton.setBounds(151, 11, 91, 37);
		frmProjectGps.getContentPane().add(loadFileButton);

		timeButton = new JRadioButton("Time");
		costGroup.add(timeButton);
		timeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (travel == 0)
					Graph.useDistanceCost = false;
				Graph.useHighways = false;
			}
		});
		timeButton.setBounds(533, 111, 55, 25);
		frmProjectGps.getContentPane().add(timeButton);

		JLabel selecCostLabel = new JLabel("Select a cost to find a path (Distance is the default)");
		selecCostLabel.setBounds(390, 85, 325, 14);
		frmProjectGps.getContentPane().add(selecCostLabel);

		distButton = new JRadioButton("Distance");
		costGroup.add(distButton);
		distButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph.useHighways = false;
				Graph.useDistanceCost = true;
				travelGroup.clearSelection();
				travel = 0;
			}
		});
		distButton.setBounds(390, 106, 86, 25);
		frmProjectGps.getContentPane().add(distButton);

		startComboBox = new JComboBox();
		startComboBox.setMaximumRowCount(30);
		startComboBox.setBounds(10, 136, 160, 22);
		frmProjectGps.getContentPane().add(startComboBox);

		endComboBox = new JComboBox();
		endComboBox.setMaximumRowCount(30);
		endComboBox.setBounds(205, 136, 160, 22);
		frmProjectGps.getContentPane().add(endComboBox);

		JLabel startPointLabel = new JLabel("Starting Point");
		startPointLabel.setBounds(47, 116, 97, 14);
		frmProjectGps.getContentPane().add(startPointLabel);

		JLabel endPointLabel = new JLabel("Ending Point");
		endPointLabel.setBounds(253, 116, 78, 14);
		frmProjectGps.getContentPane().add(endPointLabel);

		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printPathsArea.setText("");

				// We clear the text area, then try to get the shortest path depending on the
				// conditions.

				if (dataLoaded) {
					String startSymbol = startComboBox.getSelectedItem().toString().substring(0, 1);
					String endSymbol = endComboBox.getSelectedItem().toString().substring(0, 1);
					try {
						Path p = Dijkstra.shortestPath(g, startSymbol, endSymbol);

						// Preparing the path information to fit with our print statements to the text
						// area.
						String path = p.path;
						int cost = 0;
						path = path.substring(2);

						// Checking if the two locations entered are the same location or not.
						// Printing into text area a cost of 0 if the locations are the same.
						if (startSymbol.equals(endSymbol)) {
							if (!g.returnAddress) {
								printPathsArea.append(startSymbol + " -> " + endSymbol);
								printPathsArea.append(Graph.useDistanceCost ? " (0 miles)"
										: (Graph.useHighways ? "(0 highways)" : "(0 minutes)"));
								printPathsArea.append("\n");
							} else {
								String address = startComboBox.getSelectedItem().toString().substring(2);
								printPathsArea.append(address + " -> " + address);
								printPathsArea.append(Graph.useDistanceCost ? " (0 miles)"
										: (Graph.useHighways ? "(0 minutes)" : "(0 minutes)"));
								printPathsArea.append("\n");
							}
						}

						// Convert the path from the Dijkstra algorithm to easy to read and understand
						// output-
						// - which changes depending on what the user wants to see.
						for (int i = 0; i < path.length(); i += 2) {
							for (Edge ed : g.edges) {
								if (ed.fromVertex.symbol.equals(path.substring(i, i + 1))
										&& ed.toVertex.symbol.equals(path.substring(i + 1, i + 2))) {
									// If the user wants to know how long it would take to walk between locations.
									if (travel == 2) {
										// approx 20 mins to walk a mile
										printPathsArea.append(ed.toStringNoCost());
										int hours = (ed.distanceCost * 20) / 60;
										int mins = (ed.distanceCost * 20) % 60;
										if (hours < 1 && mins > 0) {
											printPathsArea.append("(" + mins + " minutes)\n");
										} else if (hours > 1 && mins > 0) {
											if (hours == 1) {
												printPathsArea
														.append("(" + hours + " hour and " + mins + " minutes)\n");
											} else {
												printPathsArea
														.append("(" + hours + " hours and " + mins + " minutes)\n");
											}
										} else {
											if (hours == 1) {
												printPathsArea.append("(" + hours + " hour)\n");
											} else {
												printPathsArea.append("(" + hours + " hours)\n");
											}
										}
										cost += ed.distanceCost * 20;
										g.useDistanceCost = false;

										// If the user wants to know how long it would take to bike between locations.
									} else if (travel == 1) {
										// approx most 5 mins to bike a mile
										printPathsArea.append(ed.toStringNoCost());
										int hours = (ed.distanceCost * 5) / 60;
										int mins = (ed.distanceCost * 5) % 60;
										if (hours < 1 && mins > 0) {
											printPathsArea.append("(" + mins + " minutes)\n");
										} else if (hours > 1 && mins > 0) {
											if (hours == 1) {
												printPathsArea
														.append("(" + hours + " hour and " + mins + " minutes)\n");
											} else {
												printPathsArea
														.append("(" + hours + " hours and " + mins + " minutes)\n");
											}
										} else {
											if (hours == 1) {
												printPathsArea.append("(" + hours + " hour)\n");
											} else {
												printPathsArea.append("(" + hours + " hours)\n");
											}
										}
										cost += ed.distanceCost * 5;
										g.useDistanceCost = false;

										// If the user wants to know distance, time (by car), or highways between
										// locations.
									} else {
										printPathsArea.append(ed.toString());
										cost = p.totalCost;
									}
								}
							}

						}

						// Printing in the text area different total costs depending on what the user
						// wants.

						if (g.useHighways) {
							printPathsArea.append("\nTotal Cost = " + cost + " highways.");
							return;
						}
						if (g.useDistanceCost) {
							printPathsArea.append("\nTotal Cost = " + cost + " miles.");
						} else {
							int totalHours = cost / 60;
							int totalMins = cost % 60;
							if (totalHours > 0) {
								printPathsArea.append(
										"\nTotal Cost = " + totalHours + " hours and " + totalMins + " minutes.");
							} else {
								printPathsArea.append("\nTotal Cost = " + cost + " minutes.");
							}
						}

					} catch (Exception g) {
						printPathsArea.append("There is no path between these points, try a different combination.");
					}

				} else {
					printPathsArea.append("You haven't loaded any data in yet!");
				}
			}
		});
		submitButton.setBounds(470, 376, 91, 37);
		frmProjectGps.getContentPane().add(submitButton);

		JLabel lblNewLabel = new JLabel("(Try \"MapInfo.txt\")");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(10, 53, 137, 14);
		frmProjectGps.getContentPane().add(lblNewLabel);

		JLabel selectOutputLabel = new JLabel("Select the output format (Symbols are default)");
		selectOutputLabel.setBounds(390, 11, 325, 14);
		frmProjectGps.getContentPane().add(selectOutputLabel);

		symbolsButton = new JRadioButton("Symbols");
		symbolsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.returnAddress = false;
			}
		});
		formatGroup.add(symbolsButton);
		symbolsButton.setBounds(390, 32, 86, 25);
		frmProjectGps.getContentPane().add(symbolsButton);

		addressButton = new JRadioButton("Addresses");
		addressButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.returnAddress = true;
			}
		});
		formatGroup.add(addressButton);
		addressButton.setBounds(533, 32, 90, 25);
		frmProjectGps.getContentPane().add(addressButton);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 173, 355, 256);
		frmProjectGps.getContentPane().add(scrollPane);

		printPathsArea = new JTextArea();
		scrollPane.setViewportView(printPathsArea);
		printPathsArea.setEditable(false);

		JLabel selectTravelLabel = new JLabel("Select method of travel (By Car is the default)");
		selectTravelLabel.setBounds(390, 188, 288, 37);
		frmProjectGps.getContentPane().add(selectTravelLabel);

		carButton = new JRadioButton("By Car");
		carButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				travel = 0;
			}
		});
		travelGroup.add(carButton);
		carButton.setBounds(390, 259, 68, 23);
		frmProjectGps.getContentPane().add(carButton);

		bikeButton = new JRadioButton("By Bike");
		bikeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph.useDistanceCost = true;
				travel = 1;
				timeButton.doClick();
			}
		});
		travelGroup.add(bikeButton);
		bikeButton.setBounds(478, 259, 83, 23);
		frmProjectGps.getContentPane().add(bikeButton);

		footButton = new JRadioButton("By Foot");
		footButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph.useDistanceCost = true;
				travel = 2;
				timeButton.doClick();
			}
		});
		travelGroup.add(footButton);
		footButton.setBounds(563, 259, 78, 23);
		frmProjectGps.getContentPane().add(footButton);

		JLabel lbloptionOnlyAffects = new JLabel("(Option only affects time output)");
		lbloptionOnlyAffects.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbloptionOnlyAffects.setBounds(390, 225, 198, 14);
		frmProjectGps.getContentPane().add(lbloptionOnlyAffects);

		lblNewLabel_1 = new JLabel("-----------------------------------------------------------------------");
		lblNewLabel_1.setBounds(390, 313, 288, 14);
		frmProjectGps.getContentPane().add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel("-----------------------------------------------------------------------");
		lblNewLabel_2.setBounds(390, 163, 288, 14);
		frmProjectGps.getContentPane().add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("-----------------------------------------------------------------------");
		lblNewLabel_3.setBounds(390, 64, 288, 14);
		frmProjectGps.getContentPane().add(lblNewLabel_3);

		highwaysButton = new JRadioButton("Highways");
		highwaysButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph.useDistanceCost = false;
				travelGroup.clearSelection();
				travel = 0;
				Graph.useHighways = true;
			}
		});
		costGroup.add(highwaysButton);
		highwaysButton.setBounds(390, 134, 109, 23);
		frmProjectGps.getContentPane().add(highwaysButton);

		lblNewLabel_4 = new JLabel("By Mason Grant");
		lblNewLabel_4.setBounds(10, 440, 198, 14);
		frmProjectGps.getContentPane().add(lblNewLabel_4);
	}
}

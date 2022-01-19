import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UAWindow {

	private JFrame frame;
	
	/* ARGUMENTS */
	public String filepath;
	public int maxIterationCount;
	public int knapsackCapacity;
	public int populationCount;
	public double mutationPercent;
	public double minFitnessScore;
	
	/* COMPONENTS */
	private JTextField filepathText;
	private JTextField maxItText;
	private JTextField knapCapacityText;
	private JTextField popCountText;
	private JTextField mutPercentText;
	private JTextField minFitScoreText;
	private JTextArea resultsTextArea;
	private JScrollPane resultsPane;
	
	public UAWindow() {
		this.maxIterationCount = 1000;
		this.knapsackCapacity = 30;
		this.populationCount = 10;
		this.mutationPercent = 0.05;
		this.minFitnessScore = 50;
		
		this.frame = new JFrame();
		this.initComponents();
		this.frame.setSize(600, 480);
		this.frame.setTitle("Binary Knapsack Problem via Genetic Algorithms");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(null);
		this.frame.setVisible(true);
	}
	
	private void initComponents() {
		JLabel argumentsLabel = new JLabel();
		argumentsLabel.setText("Arguments");
		argumentsLabel.setBounds(15, 15, 100, 15);
		this.frame.add(argumentsLabel);
		
		JLabel filepathLabel = new JLabel();
		filepathLabel.setText("File Path:");
		filepathLabel.setBounds(15, 40, 100, 15);
		this.frame.add(filepathLabel);
		
		JButton getFile = new JButton();
		getFile.setText("Get File");
		getFile.setBounds(130, 40, 100, 15);
		getFile.addActionListener(new GetFileActionListener(this));
		this.frame.add(getFile);

		this.filepathText = new JTextField();
		filepathText.setText("");
		filepathText.setEnabled(false);
		filepathText.setBounds(245, 40, 300, 15);
		this.frame.add(filepathText);
		
		JLabel maxItLabel = new JLabel();
		maxItLabel.setText("Max Iterations:");
		maxItLabel.setBounds(15, 65, 100, 15);
		this.frame.add(maxItLabel);
		
		this.maxItText = new JTextField();
		maxItText.setText("");
		maxItText.setBounds(130, 65, 100, 15);
		this.frame.add(maxItText);
		
		JLabel knapCapacityLabel = new JLabel();
		knapCapacityLabel.setText("Knapsack Cap:");
		knapCapacityLabel.setBounds(15, 90, 100, 15);
		this.frame.add(knapCapacityLabel);
		
		this.knapCapacityText = new JTextField();
		knapCapacityText.setText("");
		knapCapacityText.setBounds(130, 90, 100, 15);
		this.frame.add(knapCapacityText);
		
		JLabel popCountLabel = new JLabel();
		popCountLabel.setText("Pop. Count:");
		popCountLabel.setBounds(15, 115, 100, 15);
		this.frame.add(popCountLabel);
		
		this.popCountText = new JTextField();
		popCountText.setText("");
		popCountText.setBounds(130, 115, 100, 15);
		this.frame.add(popCountText);
		
		JLabel mutPercentLabel = new JLabel();
		mutPercentLabel.setText("Mutation %:");
		mutPercentLabel.setBounds(15, 140, 100, 15);
		this.frame.add(mutPercentLabel);
		
		this.mutPercentText = new JTextField();
		mutPercentText.setText("");
		mutPercentText.setBounds(130, 140, 100, 15);
		this.frame.add(mutPercentText);
		
		JLabel minFitScoreLabel = new JLabel();
		minFitScoreLabel.setText("Min Fitness:");
		minFitScoreLabel.setBounds(15, 165, 100, 15);
		this.frame.add(minFitScoreLabel);
		
		this.minFitScoreText = new JTextField();
		minFitScoreText.setText("");
		minFitScoreText.setBounds(130, 165, 100, 15);
		this.frame.add(minFitScoreText);
		
		JButton runButton = new JButton();
		runButton.setText("Run");
		runButton.addActionListener(new RunButtonActionListener(this));
		runButton.setBounds(15, 190, 100, 15);
		this.frame.add(runButton);
		
		this.resultsTextArea = new JTextArea();
		this.resultsTextArea.setBounds(15, 215, 300, 200);
		this.resultsTextArea.setText("");
		this.resultsPane = new JScrollPane(this.resultsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.resultsPane.setBounds(15, 215, 500, 200);
		this.frame.add(resultsPane);
	}
	
	public void setFilePathText(String text) {
		this.filepathText.setText(text);		
	}
	
	public void setMaxItText(String text) {
		this.maxItText.setText(text);
	}
	
	public String getMaxItText() {
		return this.maxItText.getText();
	}

	public void setKnapCapacityText(String text) {
		this.knapCapacityText.setText(text);
	}
	
	public String getKnapCapacityText() {
		return this.knapCapacityText.getText();
	}
	
	public void setPopCountText(String text) {
		this.popCountText.setText(text);
	}
	
	public String getPopCountText() {
		return this.popCountText.getText();
	}
	
	public void setMutPercentText(String text) {
		this.mutPercentText.setText(text);
	}
	
	public String getMutPercentText() {
		return this.mutPercentText.getText();
	}
	
	public void setMinFitScoreText(String text) {
		this.minFitScoreText.setText(text);
	}
	
	public String getMinFitScoreText() {
		return this.minFitScoreText.getText();
	}
	
	public JTextArea getResultsTextArea() {
		return this.resultsTextArea;
	}
	
}

class GetFileActionListener implements ActionListener {

	private UAWindow window;
	
	public GetFileActionListener(UAWindow window) {
		super();
		this.window = window;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		int val = fileChooser.showOpenDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String filepath = file.getAbsolutePath();
			window.filepath = filepath;
			window.setFilePathText(filepath);
		}
	}
	
}

class RunButtonActionListener implements ActionListener {
	
	private UAWindow window;
	
	public RunButtonActionListener(UAWindow window) {
		super();
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		window.maxIterationCount = Integer.parseInt(window.getMaxItText());
		window.knapsackCapacity = Integer.parseInt(window.getKnapCapacityText());
		window.populationCount = Integer.parseInt(window.getPopCountText());
		window.mutationPercent = Double.parseDouble(window.getMutPercentText());
		window.minFitnessScore = Double.parseDouble(window.getMinFitScoreText());
		UAGeneticAlgorithm GA = new UAGeneticAlgorithm();
		GA.RunGeneticAlgorithm(window.filepath, window.maxIterationCount, window.knapsackCapacity, window.populationCount, window.mutationPercent, window.minFitnessScore, window);
	}
	
}

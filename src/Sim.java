import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Sim{
	private JFrame frame;
	private Canvas canvas;
	private JPanel bottomPanel, repQualPanel, showLinesSub, speedPanel, mutationPanel;
	private JTextField label1, label2, label3, label4;
	private JTextArea credits;
	private JSlider rate, mutation, mapAlter;
	private ButtonGroup repQualGroup;
	private JRadioButton[] repQuals;
	private JCheckBox showLines, smoothMotion;
	private EntityHandler ents;
	private Map map;
	private int averageX, topQuarterX;
	private boolean smooth;
	
	public Sim(int width, int height, int blockDims){
		Util.setBlockDims(blockDims);
		canvas = new Canvas(width*blockDims, height*blockDims);
		ents = new EntityHandler();
		map = new Map(width, height);
		map.generateRandomly(16, 6, .1);
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2, 5));
		
		showLinesSub = new JPanel(new GridLayout(2, 1));
		showLines = new JCheckBox("Show Population Lines");
		showLines.setSelected(false);
		
		speedPanel = new JPanel(new GridLayout(2, 1));
		label1 = new JTextField("Speed (frames/second)");
		label1.setEditable(false);
		speedPanel.add(label1);
		rate = new JSlider(JSlider.HORIZONTAL, 10, 210, 50);
		rate.setSnapToTicks(true);
		rate.setPaintTicks(true);
		rate.setPaintLabels(true);
		rate.setPaintTrack(true);
		rate.setMajorTickSpacing(50);
		rate.setMinorTickSpacing(10);
		speedPanel.add(rate);
		bottomPanel.add(speedPanel);
		
		label3 = new JTextField("Reproduction Qualification");
		label3.setEditable(false);
		showLinesSub.add(showLines);
		showLinesSub.add(label3);
		bottomPanel.add(showLinesSub);
		label4 = new JTextField("Environment (Map) Alteration");
		label4.setEditable(false);
		bottomPanel.add(label4);
		
		smoothMotion = new JCheckBox("Smooth Motion (worse pathfinding)");
		smoothMotion.setSelected(false);
		bottomPanel.add(smoothMotion);
		
		mutationPanel = new JPanel(new GridLayout(2, 1));
		label2 = new JTextField("Mutation Potential");
		label2.setEditable(false);
		mutationPanel.add(label2);
		mutation = new JSlider(JSlider.HORIZONTAL, 0, 250, 100);
		mutation.setSnapToTicks(true);
		mutation.setPaintTicks(true);
		mutation.setPaintLabels(true);
		mutation.setPaintTrack(true);
		mutation.setMajorTickSpacing(50);
		mutation.setMinorTickSpacing(10);
		mutationPanel.add(mutation);
		bottomPanel.add(mutationPanel);
		
		repQualPanel = new JPanel(new GridLayout(3, 2));
		repQualGroup = new ButtonGroup();
		repQuals = new JRadioButton[3];
		repQuals[0] = new JRadioButton("No Qualification");
		repQuals[1] = new JRadioButton("1/2 Line");
		repQuals[2] = new JRadioButton("3/4 Line");
		repQuals[2].setSelected(true);
		repQualGroup.add(repQuals[0]);
		repQualGroup.add(repQuals[1]);
		repQualGroup.add(repQuals[2]);
		repQualPanel.add(repQuals[0]);
		repQualPanel.add(repQuals[1]);
		repQualPanel.add(repQuals[2]);
		bottomPanel.add(repQualPanel);
		
		mapAlter = new JSlider(JSlider.HORIZONTAL, 0, 10, 2);
		mapAlter.setSnapToTicks(true);
		mapAlter.setPaintTicks(true);
		mapAlter.setPaintLabels(true);
		mapAlter.setPaintTrack(true);
		mapAlter.setMajorTickSpacing(2);
		mapAlter.setMinorTickSpacing(1);
		bottomPanel.add(mapAlter);
		
		credits = new JTextArea("Created by Evan Williams\n"+
								"15 Sep 2012 - 21 Sep 2012\n"+
								"evanw555@gmail.com");
		credits.setEditable(false);
		credits.setBackground(Color.LIGHT_GRAY);
		bottomPanel.add(credits);
		
		frame = new JFrame();
		frame.setTitle("Natural Selection Simulator");
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.toFront();
		canvas.requestFocus();
		frame.setVisible(true);
		
		
	}
	
	public void run(){
		Refs.setSim(this);
		Refs.setCanvas(canvas);
		Refs.setEnts(ents);
		Refs.setMap(map);
		//TODO TEMP
			ents.add(new Creature());
			//
		long startTime = 0;
		long endTime = 0;
		long timeElapsed = 0;
		int delay = 1000/rate.getValue();
		averageX = topQuarterX = 0;
		smooth = false;
		while(true){
			smooth = smoothMotion.isSelected();
			startTime = System.currentTimeMillis();
			canvas.repaint();
			map.update();
			ents.update();
			if(ents.hasPopulationAbove(0)){
				averageX = ents.getAverageX();
				topQuarterX = ents.getTopQuarterX();
			}
			endTime = System.currentTimeMillis();
			timeElapsed = endTime - startTime;
			delay = 1000/rate.getValue();
			Util.sleep(Util.trimInt((int)(delay - timeElapsed), 0, 99999999));
		}
	}
	
	public void paint(Graphics g){
		map.paint(g);
		ents.paint(g);
	}
	
	public double getMutation(){
		return ((double)mutation.getValue())*.00001;
	}
	
	public int getAverageX(){
		return averageX;
	}
	
	public int getTopQuarterX(){
		return topQuarterX;
	}
	
	public int getReproductionQualificationValue(){
		if(repQuals[0].isSelected())
			return 0;
		if(repQuals[1].isSelected())
			return 1;
		return 2;
	}
	
	public boolean showLines(){
		return showLines.isSelected();
	}
	
	public double getMapAlterationValue(){
		return .0005*mapAlter.getValue();
	}
	
	public boolean isSmoothMotion(){
		return smooth;
	}
}
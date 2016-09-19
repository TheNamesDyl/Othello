package me.Dylan.Othello;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ScoreView implements ChangeListener{
	private Model model;
	private JFrame jf;
	private JLabel light, dark, turn;
	private Point p = new Point(600,100);
	private Dimension d = new Dimension(100,100);
	private BorderLayout bl = new BorderLayout();
	private Image appImage;

	public ScoreView(Model model){
		this.model = model;
		model.addChangeListener(this);
		
		
		jf = new JFrame();
		jf.setLocation(p);
		jf.setMinimumSize(d);
		light = new JLabel();
		dark = new JLabel();
		turn = new JLabel();
		jf.setLayout(bl);
		jf.add(light, BorderLayout.NORTH);
		jf.add(dark, BorderLayout.SOUTH);
		jf.add(turn, BorderLayout.CENTER);
		try {
			appImage = ImageIO.read(getClass().getResource("appImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jf.setIconImage(appImage);
		jf.setTitle("Scores");
		jf.pack();
		jf.setVisible(true);
		updateView();
		

	}
	
	public void updateView(){
		light.setText("Light Score " + this.model.getCount(true));
		if(model.getTurn()){
			turn.setText("Turn: Light");
		}else{
			turn.setText("Turn: Dark");
		}
		dark.setText("Dark Score " + this.model.getCount(false));
		
		jf.repaint();
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		updateView();
		
	}

}

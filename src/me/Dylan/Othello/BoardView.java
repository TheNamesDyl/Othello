package me.Dylan.Othello;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class BoardView implements ChangeListener, ActionListener{
	private Model model;
	private GridLayout gl = new GridLayout();
	private JFrame jf = new JFrame();
	private JButton jb = new JButton();
	private List<DButton> buttons = new ArrayList<DButton>();
	private Dimension d = new Dimension(500,500);
	private Point p = new Point(450,260);
	private Controller controller;
	private int counter;
	private Image whiteChecker;
	private Image blackChecker;
	private Image appImage;

	public BoardView(Model model, Controller controller){
		this.model = model;
		this.controller = controller;
	    model.addChangeListener(this);
	    
		gl.setColumns(model.getSize());
		gl.setRows(model.getSize());
		
		jf.setLayout(gl);
		jf.setMinimumSize(d);

		for(int i = 0; i<model.getSize(); i++){
			for(int i2 = 0; i2<model.getSize(); i2++){
				buttons.add(new DButton(i2,i));
				buttons.get(counter).addActionListener(this);
				jf.add(buttons.get(counter));
				counter = counter + 1;
				
			}
		}
		counter = 0;
		jf.pack();
		jf.setTitle("Othello/Reversi");
		try {
			appImage = ImageIO.read(getClass().getResource("appImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jf.setIconImage(appImage);

		jf.setLocation(p);
		jf.setVisible(true);

		updateView();
	}
	private void updateView(){
		try {
			whiteChecker = ImageIO.read(getClass().getResource("whitechecker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			blackChecker = ImageIO.read(getClass().getResource("blackChecker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(DButton buttons: buttons){
			if(controller.isMenuDone()){
				if(model.isOccupied(buttons.getRow(), buttons.getCol())){
					buttons.setEnabled(true); //just to keep it so the colors dont get weird on the images
					buttons.setOpaque(false);
					buttons.setText("");
					if(model.isOccupiedBy(buttons.getRow(), buttons.getCol(), true)){


						buttons.setIcon(new ImageIcon(whiteChecker));
						buttons.setContentAreaFilled(false);
						buttons.setBorderPainted(false);
					}else if(model.isOccupiedBy(buttons.getRow(), buttons.getCol(), false)){

						buttons.setIcon(new ImageIcon(blackChecker));
						buttons.setContentAreaFilled(false);
						buttons.setBorderPainted(false);
						
					}
				}else{

					

				    buttons.setForeground(Color.black);
				    buttons.setContentAreaFilled(true);
				    buttons.setBorderPainted(true);
					buttons.setIcon(null);
					buttons.setOpaque(true);

					if(controller.isLegalMove(buttons.getRow(), buttons.getCol(), model.getTurn())){
						buttons.setBackground(Color.LIGHT_GRAY);
						buttons.setEnabled(true);
						
					}else{
					    buttons.setBackground(Color.DARK_GRAY);
						buttons.setEnabled(false);
						
					}
				}
			}else{
				buttons.setEnabled(false);
			}
		}

//		if(!controller.isTurns(model.getTurn())){
	//		if(!controller.isTurns(model.getTurn() && !controller.isTurns(!model.getTurn()))){
	//			System.out.println("reset");
	//			model.reset();
	//		}else{
	//			model.takeTurn();
	//		}

//		}
		jf.repaint();

	 
		
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		updateView();

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.actionPerformed(e);
		
	}
	
	
	

}
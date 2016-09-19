package me.Dylan.Othello;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller implements ActionListener{
	private Model model;
	private boolean firstCheck = false;
	private int rowAIChoice = 0;
	private int columnAIChoice = 0;
	private int score = 0;
	private int maxScore = 0;
	private int maxRow = 0;
	private int maxCol = 0;
	private Model m2;
	private JFrame jf;
	private BorderLayout bl;
	private JButton vsplayer, vscomputer, easyButton, mediumButton, hardButton,onlinePlay;
	private Point p = new Point(300,195);
	private Image appImage;
	private boolean humanvshuman,easy,medium,hard, isMenuDone = false;
	public Controller(Model model) {
		this.model = model;
		jf = new JFrame();
		vsplayer = new JButton("Human vs Human");
		vscomputer = new JButton("Human vs Computer");
		onlinePlay = new JButton("Online Play");
        onlinePlay.setEnabled(false);
		bl = new BorderLayout();
		jf.setLayout(bl);
		jf.add(vsplayer, BorderLayout.NORTH);
		jf.add(onlinePlay, BorderLayout.SOUTH);
		jf.setTitle("Menu");
		try {
			appImage = ImageIO.read(getClass().getResource("appImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jf.setIconImage(appImage);
		jf.add(vscomputer, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);
		jf.setLocation(p);
		vsplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				isMenuDone = true;
				humanvshuman = true;
				jf.setVisible(false);
				modelNotify();
			}
		});
		vscomputer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				humanvshuman = false;
				difficultyWindow();
			}
		});
		
	}
	private void difficultyWindow(){
		easyButton = new JButton("Easy");
		mediumButton = new JButton("Medium");
		hardButton = new JButton("Hard");
		jf.remove(vscomputer);
		jf.remove(vsplayer);
		jf.remove(onlinePlay);
		jf.add(easyButton, BorderLayout.NORTH);
		jf.add(mediumButton, BorderLayout.CENTER);
		jf.add(hardButton, BorderLayout.SOUTH);
		jf.pack();
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				isMenuDone = true;
				easy = true;
				jf.setVisible(false);
				modelNotify();
			}
		});
		mediumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				isMenuDone = true;
				medium = true;
				jf.setVisible(false);
				modelNotify();
			}
		});
		hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				isMenuDone = true;
				hard = true;
				jf.setVisible(false);
				modelNotify();
			}
		});

	}
	public boolean isMenuDone(){
		return isMenuDone;
	}
	private void modelNotify(){ // the only reason I added this was to avoid making model final when I called it further up
		model.notifyChangeListeners();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		Dimensioned hasDim = (Dimensioned)ae.getSource();
		
		int row = hasDim.getRow();
		int col = hasDim.getCol();
		
		move(row, col, model.getTurn(),false);
		
	}
	
	public boolean isLegalMove(int row, int column, boolean player){
		
		if(isLegalMoveInDirection(row,column,1,0,player) ||  
				isLegalMoveInDirection(row,column,1,1,player) || 
				isLegalMoveInDirection(row,column,0,1,player) || 
				isLegalMoveInDirection(row,column,-1,1,player) || 
				isLegalMoveInDirection(row,column,-1,0,player) || 
				isLegalMoveInDirection(row,column,-1,-1,player) || 
				isLegalMoveInDirection(row,column,0,-1,player) || 
				isLegalMoveInDirection(row,column,1,-1,player) &&
				!isOnBoard(row,column)){
			return true;
		}else{
			return false;
		}
		
		
	}
	
	private void move(int row, int column, boolean player, boolean computerChoice){

		if(isLegalMove(row,column,player)){

			
			flip(row,column,player);
			model.occupy(row,column,player);
			model.takeTurn();
			
		    if(!(isTurns(!player))){ //if there is not turns for the opposite player
		    	if(!(isTurns(player))){
		    		endGame(model.getCount(true), model.getCount(false));
		    		model.reset();
		    	}else{
		    		model.takeTurn();
		    		if(computerChoice){//if its the computers turn again it nneeds to go again
			    		if(hard){
			    			bestMove(model, model.getTurn(), 6,6);
			    		}else if(medium){
			    			bestMove(model, model.getTurn(), 3,3);
			    		}else{
			    			bestMove(model, model.getTurn(), 1,1);
			    		}
			    		move(rowAIChoice, columnAIChoice, model.getTurn(), true);
		    			
		    		}
		    	}
		    	
		    	
		    }else{ //otherwise goes to the ai.
		    
		    	if(!computerChoice && !humanvshuman){
		    		if(hard){
		    			bestMove(model, model.getTurn(), 6,6);
		    		}else if(medium){
		    			bestMove(model, model.getTurn(), 3,3);
		    		}else{
		    			bestMove(model, model.getTurn(), 1,1);
		    		}
		    		move(rowAIChoice, columnAIChoice, model.getTurn(), true);
		    	}
		    }

		    
		}
		
	}
	private void endGame(int whiteScore, int darkScore){
		final JFrame jf = new JFrame();
		JLabel jl;
		Dimension d = new Dimension(200,200);
		Point p = new Point(250,250);
		JButton jb = new JButton("OK");
		jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jf.setVisible(false);
			}
		});
		BorderLayout bl = new BorderLayout();
		if(whiteScore > darkScore){
			jl = new JLabel("White has won with a score of " + whiteScore + " points");
		}else if (whiteScore < darkScore){
			jl = new JLabel("Black has won with a score of " + darkScore + " points");
		}else{
			jl = new JLabel ("There has been a tie of " + darkScore + " points");
		}
		jf.setLayout(bl);
		jf.add(jb, BorderLayout.SOUTH);
		jf.setMinimumSize(d);
		jf.setLocation(p);
		jf.add(jl);
		jf.pack();
		jf.setVisible(true);
		
		
	}
	
	private boolean isOnBoard(int row, int column){

		if(model.isOccupied(row, column)){
			return true;
		}else{
			return false;
		}
		
	}
	private boolean isLegalMoveInDirection(int row, int column, int dRow, int dCol, boolean player){
		if(isOnBoard(row, column)){
			return false;
		}else{
			for(;;){
				if(!((dRow == -1 && row == 0) || 
						(dCol == -1 && column == 0) || 
						(dCol == 1 && column == model.getSize()-1) || 
						(dRow ==1 && row == model.getSize()-1))){

					if(firstCheck && model.isOccupiedBy(row+dRow, column+dCol, player)){
						firstCheck = false;//just to reset the value for next time.
						return true;
					}else if(firstCheck && model.isOccupiedBy(row+dRow, column+dCol, !player)){

						
					}else if(firstCheck){
						firstCheck = false;
						return false;
					}
					
					if(!(firstCheck) && model.isOccupiedBy(row+dRow, column+dCol, player)){
						break;

					}else if(!(model.isOccupied(row+dRow, column+dCol))){
						break;
					}else{	
						row = row + dRow;
						column = column + dCol;
						firstCheck = true;
					}
				}else{
					firstCheck = false;
					return false;
				}
			}
			firstCheck = false;
			return false;
		}
		
	}
	private void flip(int row, int column,boolean player){
		if(isLegalMoveInDirection(row,column,1,0,player)){
			flipDirection(row,column,1,0,player);
		}
		if(isLegalMoveInDirection(row,column,1,1,player)){
			flipDirection(row,column,1,1,player);
		}
		if(isLegalMoveInDirection(row,column,0,1,player)){
			flipDirection(row,column,0,1,player);
		}
		if(isLegalMoveInDirection(row,column,-1,1,player)){
			flipDirection(row,column,-1,1,player);
		}
		if(isLegalMoveInDirection(row,column,-1,0,player)){
			flipDirection(row,column,-1,0,player);
		}
		if(isLegalMoveInDirection(row,column,-1,-1,player)){
			flipDirection(row,column,-1,-1,player);
		}
		if(isLegalMoveInDirection(row,column,0,-1,player)){
			flipDirection(row,column,0,-1,player);
		}
		if (isLegalMoveInDirection(row,column,1,-1,player)){
			flipDirection(row,column,1,-1,player);
		}
	}
	private void flipDirection(int row, int column, int dRow, int dCol, boolean player){
		for(;;){
			if(model.isOccupiedBy(row+dRow,column+dCol, !player)){
				model.occupy(row+dRow, column+dCol, player);
				row = row+dRow;
				column = column+dCol;
				
			}else{
				break;
			}
			
			

			
		}
		
		
	}
	public int bestMove(Model m, boolean player, int level, int maxLevel){
		maxScore = 0;
		score = 0;
		



		if(level == 0){
			score = m2.getCount(player);
			return score;
		}

		for(int column = 0; column < m.getSize(); column ++){
			for(int row = 0; row< m.getSize(); row++){
			
				if(isLegalMove(row,column,player)){
					
					if(level == maxLevel){
					    this.m2 = (Model) m.clone();
						
					}
				  

					m2.occupy(row, column, player);
					score = bestMove(m2, !player, level-1, maxLevel);
					if(score >= maxScore && level == maxLevel){
						maxScore = score;
						this.rowAIChoice = row;
						this.columnAIChoice = column;
					}
				}
			}
		}


		return score;
	}
	
	public boolean isTurns(boolean player){
		for(int column = 0; column < model.getSize(); column++){
			for(int row = 0; row < model.getSize(); row ++){
				if(isLegalMove(row, column, player)){
					return true;
				}
			}
		}
		return false;
		
	}
	
	
	public static void main(String[] args) throws IllegalNumberException {
		Model m = new Model(8);
		Controller c = new Controller(m);
		BoardView v = new BoardView(m,c); 
		ScoreView sv = new ScoreView(m);
	}
	
}
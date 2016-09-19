package me.Dylan.Othello;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Model{
	
	public static final boolean LIGHT = true;
	public static final boolean DARK = false;
	public boolean currentTurn = true;
	private int[][] boardArray;
	private int splitter;
	private static String toString = "";
	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private ChangeEvent ce;
	
	
	public Model(int size) throws IllegalNumberException{
		if(size % 2 == 0 && size >= 4){
			boardArray = new int[size][size];

			reset();
			
			
		}else{
			throw new IllegalNumberException();
			
		}
		
	}
	public void reset(){
		if(!getTurn()){//just to make it so it's always white who goes first
			takeTurn();
		}
		for(int row = 0; row < boardArray.length; row++){
			for(int column = 0; column < boardArray.length; column ++){
				
			
				boardArray[row][column] = 0;
				

			}
		}

		splitter = boardArray.length/2;
		this.occupy(splitter-1,splitter-1,LIGHT); 
		this.occupy(splitter,splitter-1,DARK);
		this.occupy(splitter,splitter,LIGHT);
		this.occupy(splitter-1,splitter,DARK);

		notifyChangeListeners();
	}
	
	private boolean isEmpty(){
		for(int column = 0; column < boardArray.length; column++){
			for(int row = 0; row < boardArray.length; row ++){
				if(boardArray[row][column] == 1 || boardArray[row][column] == 2){
					return false;
				}
			}
		}
		return true;
	}
	public String toString(){
		for(int i = 0; i < boardArray.length; i++){
			for(int i2 = 0; i2 < boardArray.length; i2++){

				
				if(boardArray[i][i2] == 0){
					toString = toString + ".";
				}else if(boardArray[i][i2] == 1){
					toString = toString + "L";
				}else{
					toString = toString + "D";
				}
				if(i2 == boardArray.length-1){
					toString = toString + "\n";
				}
			}
		}
		
		return toString;
	}
	
	public int getSize(){
		return boardArray.length;
	}
	
	public int getCount(boolean player){
		int counter = 0;
		for(int i = 0; i < boardArray.length; i++){
			for(int i2 = 0; i2 < boardArray.length; i2++){
				if(boardArray[i][i2] == 1 && player){
					counter++;

				}else if(boardArray[i][i2] == 2 && !player){
					counter++;
				}
			}
		}
		return counter;
	}
	public void occupy( int row, int column, boolean player){

		if(player){
			boardArray[row][column] = 1;	
		}else{
			boardArray[row][column] = 2;
		}

	}
	public boolean isOccupied(int row, int column){
		if(boardArray[row][column] == 0){
			return false;
		}else{
			return true;
		}
		
	}
	public boolean isOccupiedBy(int row, int column, boolean player){
		if(boardArray[row][column] == 1 && player){
			return true;
		}else if(boardArray[row][column] == 2 && !player){
			return true;
			
		}else{
			return false;
		}
			
	}
	public void takeTurn(){
		currentTurn = !currentTurn;
		notifyChangeListeners();
	}
	public boolean getTurn(){
		return currentTurn;
	}
	public void addChangeListener(ChangeListener cl){
		listeners.add(cl);
	}
	public void removeChangeListener(ChangeListener cl){
		listeners.remove(cl);
	}
	public void notifyChangeListeners(){
	    for (ChangeListener name : listeners) {
	    	name.stateChanged(ce = new ChangeEvent(name));
	    }

	}
	public Object clone(){
		try {
			Model m = new Model(this.getSize());
			for (int i=0; i<this.getSize(); i++) {
				for (int j=0; j<this.getSize(); j++) {
					m.boardArray[i][j] = this.boardArray[i][j];
				}
			}
			return m;
		} catch (IllegalNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	
	public static void main(String[] args) throws IllegalNumberException{

		Model model = new Model(8);
		Controller controller = new Controller(model);


		BoardView bv = new BoardView(model, controller);
		ScoreView sv = new ScoreView(model);

	}


}
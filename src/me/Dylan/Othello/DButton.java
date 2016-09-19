package me.Dylan.Othello;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

public class DButton extends JButton implements Dimensioned{
	
	private int row;
	private int col;
	
	public DButton(int row, int col){
		super();
		this.row = row;
		this.col = col;
		//this.setText(this.row + "," + this.col);
		//this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	
	

}

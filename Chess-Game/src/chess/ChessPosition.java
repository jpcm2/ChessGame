package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition() {}

	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Erro instanciando o ChessPosition");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Position toPosition() {
		Position pos = new Position(8 - this.row, this.column - 'a');
		return pos; 
	}
	
	protected static ChessPosition fromPosition(Position position) {
		ChessPosition cp = new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
		return cp;
	}

	@Override
	public String toString() {
		return "" + this.column + this.row;
	}
	
	
}

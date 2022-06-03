package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	private Color color;
	
	public ChessPiece() {}

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		if(p == null) {
			return false;
		}
		if(p.getColor() == this.color) {
			return false;
		}
		return true;
	}
}

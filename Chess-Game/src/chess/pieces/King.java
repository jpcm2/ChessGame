package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{
	
	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public String toString() {
		return "K";
	}
	
	private boolean testTorreRoque(Position position) {
		ChessPiece p = (ChessPiece)this.getBoard().piece(position);
		return p != null && p instanceof Torre && p.getColor() == this.getColor() && p.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		for(int i = -1; i <= 1; ++i) {
			for(int  j = -1; j <= 1; ++j) {
				if(i == 0 && j == 0) {
					continue;
				}
				p.setValues(position.getRow() + i, position.getColumn() + j);
				if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
			}
		}
		if(this.getMoveCount() == 0 && !chessMatch.getCheck()) {
			Position torre1 = new Position(this.position.getRow(), this.position.getColumn() + 3);
			if(testTorreRoque(torre1)) {
				Position p1 =  new Position(this.position.getRow(), this.position.getColumn() + 2);
				Position p2 =  new Position(this.position.getRow(), this.position.getColumn() + 1);
				if(this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
					mat[this.position.getRow()][this.position.getColumn() + 2] = true;
				}
			}
			Position torre2 = new Position(this.position.getRow(), this.position.getColumn() - 4);
			if(testTorreRoque(torre2)) {
				Position p1 =  new Position(this.position.getRow(), this.position.getColumn() - 2);
				Position p2 =  new Position(this.position.getRow(), this.position.getColumn() - 1);
				Position p3 =  new Position(this.position.getRow(), this.position.getColumn() - 3);
				if(this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null && this.getBoard().piece(p3) == null) {
					mat[this.position.getRow()][this.position.getColumn() - 2] = true;
				}
			}
		}
		return mat;
	}
	
}

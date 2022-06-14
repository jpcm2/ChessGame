package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Peao extends ChessPiece{
	
	private ChessMatch chessMatch;
	public Peao(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0,0);
		
		if(this.getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 2, position.getColumn());
			Position pExtra = new Position(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(pExtra) && !getBoard().thereIsAPiece(pExtra) && this.getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			if(this.position.getRow() == 3) {
				Position esquerda = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if(this.getBoard().positionExists(esquerda) && this.isThereOpponentPiece(esquerda) && this.getBoard().piece(esquerda) == chessMatch.getEnPassant()) {
					mat[esquerda.getRow() - 1][esquerda.getColumn()] = true;
				}
				Position direita = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if(this.getBoard().positionExists(direita) && this.isThereOpponentPiece(direita) && this.getBoard().piece(direita) == chessMatch.getEnPassant()) {
					mat[direita.getRow() - 1][direita.getColumn()] = true;
				}
			}
		}else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 2, position.getColumn());
			Position pExtra = new Position(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(pExtra) && !getBoard().thereIsAPiece(pExtra) && this.getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}
			if(this.position.getRow() == 4) {
				Position esquerda = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if(this.getBoard().positionExists(esquerda) && this.isThereOpponentPiece(esquerda) && this.getBoard().piece(esquerda) == chessMatch.getEnPassant()) {
					mat[esquerda.getRow() + 1][esquerda.getColumn()] = true;
				}
				Position direita = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if(this.getBoard().positionExists(direita) && this.isThereOpponentPiece(direita) && this.getBoard().piece(direita) == chessMatch.getEnPassant()) {
					mat[direita.getRow() + 1][direita.getColumn()] = true;
				}
			}
		}
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}
}

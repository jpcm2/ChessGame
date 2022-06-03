package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Torre;

public class ChessMatch {
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
		this.initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] tab = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i < board.getRows(); ++i) {
			for(int j = 0; j < board.getColumns(); ++j) {
				tab[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return tab;
	}
	
	public ChessPiece performChessMove(ChessPosition origem, ChessPosition fim) {
		Position original = origem.toPosition();
		Position destino = fim.toPosition();
		validateOriginalPosition(original);
		//validateDestinoPosition(destino);
		Piece pecaCapturada = makeMove(original, destino);
		return (ChessPiece) pecaCapturada;
 	}
	
	private Piece makeMove(Position origem, Position destino) {
		Piece aux = board.removePiece(origem);
		Piece capturada = board.removePiece(destino);
		board.placePiece(aux, destino);
		return capturada;
	}
	
	private void validateOriginalPosition(Position teste) {
		if(!board.thereIsAPiece(teste)) {
			throw new ChessException("Posição não possui nenhuma peça");
		}
		if(!board.piece(teste).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe movimentos possíveis para realizar um movimento");
		}
	}
	
	private void validateDestinoPosition(Position teste) {
		if(!board.piece(teste).possibleMove(teste)) {
			throw new ChessException("A peça escolhida não pode se mover para essa posição");
		}
	} 
	
	
	private void placeNewPiece(char column, int row , ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Torre(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('c', 1, new Torre(board, Color.WHITE));
        placeNewPiece('c', 2, new Torre(board, Color.WHITE));
        placeNewPiece('d', 2, new Torre(board, Color.WHITE));
        placeNewPiece('e', 2, new Torre(board, Color.WHITE));
        placeNewPiece('e', 1, new Torre(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Torre(board, Color.BLACK));
        placeNewPiece('c', 8, new Torre(board, Color.BLACK));
        placeNewPiece('d', 7, new Torre(board, Color.BLACK));
        placeNewPiece('e', 7, new Torre(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}

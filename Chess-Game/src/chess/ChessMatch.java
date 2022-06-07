package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Torre;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	
	private List<ChessPiece> pecasNoTabuleiro = new ArrayList<>();
	private List<ChessPiece> pecasCapturadas = new ArrayList<>();
	
	public int getTurn() {
		return turn;
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public ChessMatch() {
		board = new Board(8, 8);
		this.turn = 1;
		this.currentPlayer = Color.WHITE;
		check = false;
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
	
	public boolean[][] movimentosPossiveis(ChessPosition origem){
		Position position = origem.toPosition();
		validateOriginalPosition(position);
		return board.piece(position).possibleMoves();
	}
	public ChessPiece performChessMove(ChessPosition origem, ChessPosition fim) {
		Position original = origem.toPosition();
		Position destino = fim.toPosition();
		validateOriginalPosition(original);
		validateDestinoPosition(original, destino);
		Piece pecaCapturada = makeMove(original, destino);
		
		if(testCheck(currentPlayer)) {
			desfazerMovimento(original, destino, pecaCapturada);
			throw new ChessException("Voce não pode se colocar em check");
		}
		this.check = testCheck(opponent(currentPlayer)) ? true: false;
		nextTurn();
		return (ChessPiece) pecaCapturada;
 	}
	
	private Piece makeMove(Position origem, Position destino) {
		Piece aux = board.removePiece(origem);
		Piece capturada = board.removePiece(destino);
		board.placePiece(aux, destino);
		if(capturada != null) {
			pecasCapturadas.add((ChessPiece)capturada);
			pecasNoTabuleiro.remove((ChessPiece)capturada);
		}
		return capturada;
	}
	
	private void desfazerMovimento(Position origem, Position destino, Piece capturada) {
		Piece aux = board.removePiece(destino);
		board.placePiece(aux, origem);
		if(capturada != null) {
			board.placePiece(capturada, destino);
			pecasCapturadas.remove(capturada);
			pecasNoTabuleiro.add((ChessPiece)capturada);
		}
	}
	
	private void validateOriginalPosition(Position teste) {
		if(currentPlayer != ((ChessPiece)(board.piece(teste))).getColor()) {
			throw new ChessException("Cor da peça selecionada é incorreta!");
		}
		if(!board.thereIsAPiece(teste)) {
			throw new ChessException("Posição não possui nenhuma peça");
		}
		if(!board.piece(teste).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe movimentos possíveis para realizar um movimento");
		}
	}
	
	private void validateDestinoPosition(Position origem, Position destino) {
		if(!board.piece(origem).possibleMove(destino)) {
			throw new ChessException("A peça escolhida não pode se mover para essa posição");
		}
	} 
	
	
	private void placeNewPiece(char column, int row , ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		pecasNoTabuleiro.add(piece);
	}
	
	private void nextTurn() {
		turn++;
		if(currentPlayer == Color.WHITE) {
			currentPlayer = Color.BLACK;
		}else {
			currentPlayer = Color.WHITE;
		}
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK: Color.WHITE;
	}
	
	private ChessPiece rei(Color color) {
		List<Piece> list = pecasNoTabuleiro.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece pe: list) {
			if(pe instanceof King) {
				return (ChessPiece)pe;
			}
		}
		throw new IllegalStateException("Não existe esse rei no tabuleiro");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = rei(color).getChessPosition().toPosition();
		List<Piece> oponente = pecasNoTabuleiro.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p: oponente) {
			boolean[][] matriz = p.possibleMoves();
			if(matriz[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
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

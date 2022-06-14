package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bispo;
import chess.pieces.Cavalo;
import chess.pieces.King;
import chess.pieces.Peao;
import chess.pieces.Rainha;
import chess.pieces.Torre;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<ChessPiece> pecasNoTabuleiro = new ArrayList<>();
	private List<ChessPiece> pecasCapturadas = new ArrayList<>();
	
	public int getTurn() {
		return turn;
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public boolean getCheckmate() {
		return this.checkMate;
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
		
		if(testCheckMate(opponent(currentPlayer))) {
			this.checkMate = true;
		}else {
			nextTurn();
		}
		return (ChessPiece) pecaCapturada;
 	}
	
	private Piece makeMove(Position origem, Position destino) {
		ChessPiece aux = (ChessPiece)board.removePiece(origem);
		aux.increaseMoveCount();
		Piece capturada = board.removePiece(destino);
		board.placePiece(aux, destino);
		if(capturada != null) {
			pecasCapturadas.add((ChessPiece)capturada);
			pecasNoTabuleiro.remove((ChessPiece)capturada);
		}
		
		if(aux instanceof King && destino.getColumn() == origem.getColumn() + 2) {
			Position origemTorre = new Position(origem.getRow(), origem.getColumn() + 3);
			Position destinoTorre = new Position(origem.getRow(), origem.getColumn() + 1);
			ChessPiece torre = (ChessPiece)board.removePiece(origemTorre);
			board.placePiece(torre, destinoTorre);
			torre.increaseMoveCount();
		}
		
		if(aux instanceof King && destino.getColumn() == origem.getColumn() - 2) {
			Position origemTorre = new Position(origem.getRow(), origem.getColumn() - 4);
			Position destinoTorre = new Position(origem.getRow(), origem.getColumn() - 1);
			ChessPiece torre = (ChessPiece)board.removePiece(origemTorre);
			board.placePiece(torre, destinoTorre);
			torre.increaseMoveCount();
		}
		
		return capturada;
	}
	
	private void desfazerMovimento(Position origem, Position destino, Piece capturada) {
		ChessPiece aux = (ChessPiece)board.removePiece(destino);
		aux.decreaseMoveCount();
		board.placePiece(aux, origem);
		if(capturada != null) {
			board.placePiece(capturada, destino);
			pecasCapturadas.remove(capturada);
			pecasNoTabuleiro.add((ChessPiece)capturada);
		}
		
		if(aux instanceof King && destino.getColumn() == origem.getColumn() + 2) {
			Position origemTorre = new Position(origem.getRow(), origem.getColumn() + 3);
			Position destinoTorre = new Position(origem.getRow(), origem.getColumn() + 1);
			ChessPiece torre = (ChessPiece)board.removePiece(destinoTorre);
			board.placePiece(torre, origemTorre);
			torre.decreaseMoveCount();
		}
		
		if(aux instanceof King && destino.getColumn() == origem.getColumn() - 2) {
			Position origemTorre = new Position(origem.getRow(), origem.getColumn() - 4);
			Position destinoTorre = new Position(origem.getRow(), origem.getColumn() - 1);
			ChessPiece torre = (ChessPiece)board.removePiece(destinoTorre);
			board.placePiece(torre, origemTorre);
			torre.decreaseMoveCount();
		}
	}
	
	private void validateOriginalPosition(Position teste) {
		if(board.piece(teste) == null) {
			throw new ChessException("Posição inválida");
		}
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
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> pecasAmigas = pecasNoTabuleiro.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p: pecasAmigas) {
			boolean[][] mat = p.possibleMoves();
			for(int i = 0; i < mat.length; ++i) {
				for(int j = 0; j <  mat.length; ++j) {
					if(mat[i][j]) {
						Position origem = ((ChessPiece)p).getChessPosition().toPosition();
						Position destino = new Position(i, j);
						Piece capturada = makeMove(origem, destino);
						boolean testCheck = testCheck(color);
						desfazerMovimento(origem, destino, capturada);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void initialSetup() {
		placeNewPiece('a', 1, new Torre(board, Color.WHITE));
		placeNewPiece('b', 1, new Cavalo(board, Color.WHITE));
		placeNewPiece('c', 1, new Bispo(board, Color.WHITE));
		placeNewPiece('d', 1, new Rainha(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bispo(board, Color.WHITE));
        placeNewPiece('h', 1, new Torre(board, Color.WHITE));
        placeNewPiece('a', 2, new Peao(board, Color.WHITE));
        placeNewPiece('b', 2, new Peao(board, Color.WHITE));
        placeNewPiece('c', 2, new Peao(board, Color.WHITE));
        placeNewPiece('d', 2, new Peao(board, Color.WHITE));
        placeNewPiece('e', 2, new Peao(board, Color.WHITE));
        placeNewPiece('f', 2, new Peao(board, Color.WHITE));
        placeNewPiece('g', 2, new Peao(board, Color.WHITE));
        placeNewPiece('g', 1, new Cavalo(board, Color.WHITE));
        placeNewPiece('h', 2, new Peao(board, Color.WHITE));

        placeNewPiece('a', 8, new Torre(board, Color.BLACK));
        placeNewPiece('b', 8, new Cavalo(board, Color.BLACK));
        placeNewPiece('c', 8, new Bispo(board, Color.BLACK));
        placeNewPiece('d', 8, new Rainha(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bispo(board, Color.BLACK));
        placeNewPiece('h', 8, new Torre(board, Color.BLACK));
        placeNewPiece('a', 7, new Peao(board, Color.BLACK));
        placeNewPiece('b', 7, new Peao(board, Color.BLACK));
        placeNewPiece('c', 7, new Peao(board, Color.BLACK));
        placeNewPiece('d', 7, new Peao(board, Color.BLACK));
        placeNewPiece('e', 7, new Peao(board, Color.BLACK));
        placeNewPiece('f', 7, new Peao(board, Color.BLACK));
        placeNewPiece('g', 7, new Peao(board, Color.BLACK));
        placeNewPiece('g', 8, new Cavalo(board, Color.BLACK));
        placeNewPiece('h', 7, new Peao(board, Color.BLACK));
	}
}

package boardgame;

public class Board {
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board() {}

	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Número de linhas ou Colunas insuficientes");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) {
		if(row < 0 || row >= this.rows) {
			throw new BoardException("Linha do tabuleiro inválida");
		}
		if(column < 0 || column >= this.columns) {
			throw new BoardException("Coluna do tabuleiro inválida");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if(!this.positionExists(position)) {
			throw new BoardException("Posição inválida");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if(this.thereIsAPiece(position)) {
			throw new BoardException("Já existe uma peça nessa posição");
		}
		piece.position = position;
		pieces[position.getRow()][position.getColumn()] = piece;
	}
	
	private boolean positionExists(int row, int column) {
		if(row >= 0 && row < this.rows && column >= 0 && column < this.columns) {
			return true;
		}
		return false;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!this.positionExists(position)) {
			throw new BoardException("Posição inválida");
		}
		if(piece(position.getRow(), position.getColumn()) == null) {
			return false;
		}else {
			return true;
		}
	}
}

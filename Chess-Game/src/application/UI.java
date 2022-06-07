package application;

import java.util.Arrays;
import java.util.Currency;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String leitura = sc.nextLine();
			char column = leitura.charAt(0);
			int row = Integer.parseInt(leitura.substring(1));
			return new ChessPosition(column, row);
		}
		catch( Exception e) {
			throw new InputMismatchException("Posição inválida. O formato deve ser (Coluna: a - h) e (Linha: 1- 8)");
		}
	}
	
	public static void printBoard(ChessPiece[][] pieces) {
		for (int i = 0; i < pieces.length; ++i) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; ++j) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		for (int i = 0; i < pieces.length; ++i) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; ++j) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	private static void printPiece(ChessPiece piece, boolean backGround) {
		if(backGround) {
			System.out.print(ANSI_GREEN_BACKGROUND);
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
	
	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> capturadas) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		printPecasCapturadas(capturadas);
		System.out.println("Turno: " + chessMatch.getTurn());
		if(!chessMatch.getCheckmate()) {
			System.out.println("Esperando o jogador: " + chessMatch.getCurrentPlayer());
			if(chessMatch.getCheck()) {
				System.out.println("CHECK");
			}
		}else {
			System.out.println("CHECKMATE!");
			System.out.println("VENCEDOR: " + chessMatch.getCurrentPlayer());
		}
	}
	
	private static void printPecasCapturadas(List<ChessPiece> capturadas) {
		List<ChessPiece> brancasCapturadas = capturadas.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
		List<ChessPiece> pretasCapturadas = capturadas.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
		System.out.println("Peças Capturadas");
		System.out.print("Brancas: ");
		System.out.println(Arrays.toString(brancasCapturadas.toArray()));
		System.out.println();
		System.out.print("Pretas: ");
		System.out.println(Arrays.toString(pretasCapturadas.toArray()));
	}
}

package com.vini.game.piece;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vini.game.Game;
import com.vini.game.board.Board;
import com.vini.game.board.BoardBuilder;
import com.vini.game.enums.ColorEnum;
import com.vini.game.enums.PieceEnum;
import com.vini.game.piece.pieces.Pawn;

public class PawnTest {
	private Game game;
	private Pawn piece;
	private BoardBuilder builder;

	@BeforeEach
	public void setup() {
		this.builder = new BoardBuilder();
		this.game = new Game(this.builder.result(), ColorEnum.BLACK);
		this.piece = new Pawn(this.builder.result());
		this.piece
			.setColor(ColorEnum.BLACK)
			.setGame(this.game);
	}

	@Test
	public void firstMoves() {
		Board board = this.builder
			.buildEmptySquare().buildEmptySquare().buildEmptySquare().buildRow()
			.buildEmptySquare().buildEmptySquare().buildEmptySquare().buildRow()
			.buildEmptySquare().buildEmptySquare().buildEmptySquare().buildRow()
			.result();

		int[] position = {1, 0};
		board.table().get(position[1]).set(position[0], this.piece);

		this.piece.setPosition(position);
		this.piece.structureMoves();
		this.piece.updateMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false, false, false));
			add(Arrays.asList(false, true, false));
			add(Arrays.asList(false, true, false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}

	@Test
	public void nonFirstMoves() {
		Board board = this.builder
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.result();

		int[] position = {0, 0};
		board.table().get(position[1]).set(position[0], this.piece);

		this.piece.setPosition(position);
		this.piece.structureMoves();
		this.piece.updateMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false));
			add(Arrays.asList(true));
			add(Arrays.asList(true));
			add(Arrays.asList(false));
		}};

		assertTrue(pieceMoves.equals(expected));

		int[] nextPosition = {0, 1};
		this.game.move(this.piece.position(), nextPosition);
		this.piece.updateMoves();

		pieceMoves = this.piece.moves();
		expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false));
			add(Arrays.asList(false));
			add(Arrays.asList(true));
			add(Arrays.asList(false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}

	@Test
	public void piercingAttack() {
		Board board = this.builder
			.buildEmptySquare().buildEmptySquare().buildEmptySquare().buildRow()
			.buildPiece(PieceEnum.PAWN, ColorEnum.BLACK).buildEmptySquare().buildPiece(PieceEnum.PAWN, ColorEnum.WHITE).buildRow()
			.buildEmptySquare().buildEmptySquare().buildEmptySquare().buildRow()
			.result();

		int[] position = {1, 0};
		board.table().get(position[1]).set(position[0], this.piece);

		this.piece.setPosition(position);
		this.piece.structureMoves();
		this.piece.updateMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false, false, false));
			add(Arrays.asList(false, true, true));
			add(Arrays.asList(false, true, false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}

	@Test
	public void pieceBlock() {
		Board board = this.builder
			.buildEmptySquare().buildRow()
			.buildPiece(PieceEnum.PAWN, ColorEnum.WHITE).buildRow()
			.buildEmptySquare().buildRow()
			.result();

		int[] position = {0, 0};
		board.table().get(position[1]).set(position[0], this.piece);

		this.piece.setPosition(position);
		this.piece.structureMoves();
		this.piece.updateMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false));
			add(Arrays.asList(false));
			add(Arrays.asList(false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}

	@Test
	public void enPassantMove() {
		Board board = this.builder
			.buildEmptySquare().buildEmptySquare().buildRow()
			.buildEmptySquare().buildEmptySquare().buildRow()
			.buildEmptySquare().buildPiece(PieceEnum.PAWN, ColorEnum.WHITE).buildRow()
			.result();

		int[] position = {0, 0};
		board.table().get(position[1]).set(position[0], this.piece);
		this.piece.setPosition(position);
		this.piece.structureMoves();

		int[] targetPosition = {1, 2};
		IPiece targetPiece = board.findPiece(targetPosition);
		targetPiece.structureMoves();

		this.game.refreshPieceMoves();

		targetPosition[1] -= 2;
		this.game.move(targetPiece.position(), targetPosition);

		this.game.refreshPieceMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(false, false));
			add(Arrays.asList(true, true));
			add(Arrays.asList(true, false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}

	@Test
	public void whiteMoves() {
		Board board = this.builder
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.buildEmptySquare().buildRow()
			.result();

		int[] position = {0, 2};
		board.table().get(position[1]).set(position[0], this.piece);

		this.piece.setPosition(position);
		this.piece.structureMoves();
		this.piece.setColor(ColorEnum.WHITE);
		this.piece.updateMoves();

		List<List<Boolean>> pieceMoves = this.piece.moves();
		List<List<Boolean>> expected = new ArrayList<List<Boolean>>() {{
			add(Arrays.asList(true));
			add(Arrays.asList(true));
			add(Arrays.asList(false));
			add(Arrays.asList(false));
			add(Arrays.asList(false));
		}};

		assertTrue(pieceMoves.equals(expected));
	}
}

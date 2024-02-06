package com.vini.socket.lib;

import com.vini.game.board.Board;
import com.vini.game.interfaces.IPiece;
import com.vini.game.lib.Position;

import java.util.ArrayList;
import java.util.List;

public class TableRepresentation {

    private final Board board;
    public final List<Row> rows;

    public TableRepresentation(Board board) {
        this.rows = new ArrayList<>(board.getHeight());
        this.board = board;

        for (int row = 0; row < board.getHeight(); row++) {
            this.rows.add(new Row(row));
        }
    }

    class Row {
        public List<Square> squares;

        public Row(int row) {
            var position = new Position(null, row);
            this.squares = new ArrayList<>(board.getWidth());

            for (int column = 0; column < board.getWidth(); column++) {
                position.x = column;
                this.squares.add(new Square(position));
            }
        }
    }

    class Square {

        public String classes = "square";
        public List<Move> moves = new ArrayList<>();

        public Square(Position position) {
            IPiece piece = board.findPiece(position);

            if (piece == null) {
                return;
            }

            this.classes = "piece";
            this.classes += " " + piece.getIdentifiers();
            var moveTable = piece.getMoves();

            for (int i = 0; i < moveTable.size(); i++) {
                if (!moveTable.get(i)) {
                    continue;
                }
                var movePosition = Position.fromIndex(i, board.getWidth());

                this.moves.add(new Move(piece, movePosition));
            }
        }
    }

    class Move {
        public int offsetX, offsetY;
        public String classes = "move";

        public Move(IPiece piece, Position movePosition) {
            this.offsetX = movePosition.x - piece.getPosition().x;
            this.offsetY = movePosition.y - piece.getPosition().y;

            IPiece targetSquare = board.findPiece(movePosition);
            if (targetSquare == null) {
                return;
            }
            if (targetSquare.getColor() == piece.getColor()) {
                return;
            }

            this.classes += " enemy";
        }
    }
}

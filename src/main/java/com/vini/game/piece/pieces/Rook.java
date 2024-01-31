package com.vini.game.piece.pieces;

import com.vini.game.board.Board;
import com.vini.game.enums.PieceEnum;
import com.vini.game.lib.Position;
import com.vini.game.piece.IPiece;
import com.vini.game.piece.Piece;
import com.vini.game.piece.PieceHelper;

public class Rook extends Piece {
    private final int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

    protected boolean isFirstMove = true;

    public Rook(Board board) {
        super(board);
    }

    @Override
    public IPiece updateMoves() {
        Position position = new Position(null, null);
        for (int[] direction : this.directions) {
            position.x = this.position().x.intValue();
            position.y = this.position().y.intValue();

            while (true) {
                position.x += direction[0];
                position.y += direction[1];

                if (!board.isInsideTable(position)) {
                    break;
                }
                ;

                IPiece target = board.findPiece(position);

                if (PieceHelper.isAlly(this, target)) {
                    break;
                }

                this.moves().get(position.y).set(position.x, true);

                if (PieceHelper.isEnemy(this, target)) {
                    break;
                }
            }
        }

        return this;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public PieceEnum fen() {
        return PieceEnum.ROOK;
    }
}
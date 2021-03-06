package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    private int steps;
    private  PuzzleBoard previousBoard;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<>();
        int length = parentWidth/NUM_TILES;
        int k = 0;
        steps = 0;
        previousBoard = null;
        bitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, false);

        for (int i = 0; i < NUM_TILES; i++) {
            for(int j = 0; j < NUM_TILES; j++) {
                if(k==NUM_TILES*NUM_TILES-1){
                    PuzzleTile tile = null;
                    tiles.add(tile);
                } else {
                    Bitmap tileMap = Bitmap.createBitmap(bitmap, j * length, i * length, length, length);
                    PuzzleTile tile = new PuzzleTile(tileMap, k);
                    tiles.add(tile);
                }
                k++;
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        previousBoard = otherBoard;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        this.steps = otherBoard.steps + 1;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
        this.steps = 0;
        this.previousBoard = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> neighboursBoard = new ArrayList<>();
        int tileX = 0;
        int tileY = 0;
        for(int i = 0; i< NUM_TILES * NUM_TILES; i++){
            if(tiles.get(i) == null){
                tileX = i % NUM_TILES;
                tileY = i / NUM_TILES;
                break;
            }
        }
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES) {
                PuzzleBoard copyBoard = new PuzzleBoard(this);
                copyBoard.swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                neighboursBoard.add(copyBoard);
            }
        }
        return neighboursBoard;
    }

    public int priority() {
        int manhattanDistance = 0;
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            if (tiles.get(i) != null) {
                int position = tiles.get(i).getNumber();
                int rightPositionRow = position % NUM_TILES;
                int rightPositionCol = position / NUM_TILES;
                int currentRow = i % NUM_TILES;
                int currentCol = i / NUM_TILES;
                manhattanDistance += (Math.abs(currentRow - rightPositionRow)) + (Math.abs(currentCol - rightPositionCol));
            }
        }
        return manhattanDistance + steps;
    }


    public PuzzleBoard getPreviousBoard() {
        return previousBoard;
    }

}

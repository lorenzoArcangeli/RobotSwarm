package it.unicam.cs.mpmgc.robotswarm.model;

/**
 * Use to simplify the handle of the elements' positions in the Environment
 */

public record Coordinate(double x, double y) {

    public boolean isZero() {
        return x == 0 && y == 0;
    }
}

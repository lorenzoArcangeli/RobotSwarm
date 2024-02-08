package it.unicam.cs.mpmgc.robotswarm.utilites;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;

import java.util.stream.IntStream;

public record ShapeData(String label, String shape, double[] args) {
    public static ShapeData fromString(String[] elements) {
        return new ShapeData(elements[0],
            elements[1],
            IntStream.range(2,elements.length).mapToDouble(i -> Double.parseDouble(elements[i])).toArray()
        );
    }

    /**
     * returns if the shape data contains the given coordinate
     * @param point to verify
     * @return true if the shape contains the given coordinate
     */
    public boolean contains(Coordinate point){
        if(this.shape().equals("CIRCLE"))
            return circleShapeDataContains(point);
        return rectangleShapeDataContains(point);
    }

    private boolean circleShapeDataContains(Coordinate point){
        return Math.sqrt(Math.pow(point.x()-this.args()[0],2)+
                Math.pow(point.y()-this.args()[1],2))<this.args()[2];
    }

    private boolean rectangleShapeDataContains(Coordinate point){
        return point.x()<=this.args()[0]+this.args()[2]/2 && point.x()>=this.args()[0]-this.args()[2]/2&&
                point.y()<=this.args()[1]+this.args()[3]/2 && point.y()>=this.args()[1]-this.args()[3]/2;
    }

}

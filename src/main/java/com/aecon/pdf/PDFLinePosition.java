package com.aecon.utils.pdf;

import java.util.HashMap;

public class PDFLinePosition {

    public String lineName;
    private HashMap<String, HashMap<String, Boolean>> positions;

    public PDFLinePosition(String lineName) {
        this.lineName = lineName;
        this.positions = new HashMap<>();
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public HashMap<String, HashMap<String, Boolean>> getPositions() {
        return positions;
    }

    public void setPositions(HashMap<String, HashMap<String, Boolean>> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "PDF Line Position relative to headers{" +
                positions.toString() +
                '}';
    }
}

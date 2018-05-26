package Hashfunktions;

import data.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class Bucket {

    private List<DataPoint> dataPoints;
    private List<Double> hashValues;

    public Bucket() {

        setDataPoints(new ArrayList<>());
        setHashValues(new ArrayList<>());
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<Double> getHashValues() {
        return hashValues;
    }

    public void setHashValues(List<Double> hashValues) {
        this.hashValues = hashValues;
    }
}

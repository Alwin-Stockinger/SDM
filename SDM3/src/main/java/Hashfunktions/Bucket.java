package Hashfunktions;

import data.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bucket {

    private Map<DataPoint, Double> hashValues;

    public Bucket() {

        hashValues = new HashMap<>();
    }

    public List<DataPoint> getDataPoints() {
        List<DataPoint> dataPoints = new ArrayList<>();
        for (DataPoint dataPoint : hashValues.keySet()) {
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }

    public List<Double> getHashValues() {
        List<Double> hashValuesList = new ArrayList<>();
        for (Double hashValue : hashValues.values()) {
            hashValuesList.add(hashValue);
        }
        return hashValuesList;
    }

    public Map<DataPoint, Double> getHashValuesMap() {
        return hashValues;
    }
}

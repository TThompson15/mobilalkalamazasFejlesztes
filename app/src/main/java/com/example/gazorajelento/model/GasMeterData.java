package com.example.gazorajelento.model;

import java.time.LocalDateTime;
import java.util.List;

public class GasMeterData {

    private List<GasMeterInfo> data;

    public GasMeterData() {
    }

    public GasMeterData(List<GasMeterInfo> data) {
        this.data = data;
    }

    public List<GasMeterInfo> getData() {
        return data;
    }

    public void setData(List<GasMeterInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GasMeterData{" +
                "data=" + data +
                '}';
    }
}

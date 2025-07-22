package org.BookTicket.Entities;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private List<String> stations;
    private Map<String, String> stationsTime;

    public Train(){

    }

    public Train(String trainId, String trainNo, List<List<Integer>> seats, List<String> stations, Map<String, String> stationsTime){
        this.trainId=trainId;
        this.trainNo=trainNo;
        this.seats=seats;
        this.stations=stations;
        this.stationsTime=stationsTime;
    }

    public String getTrainId(){
        return trainId;
    }
    public void setTrainId(String trainId){
        this.trainId=trainId;
    }

    public String getTrainNo(){
        return trainNo;
    }
    public void setTrainNo(String trainNo){
        this.trainNo=trainNo;
    }

    public List<List<Integer>> getSeats(){
        return seats;
    }
    public void setSeats(List<List<Integer>> seats){
        this.seats=seats;
    }
     public List<String> getStations(){
        return stations;
     }
     public void setStations(List<String> stations){
        this.stations=stations;
     }

     public Map<String, String> getStationsTime(){
        return stationsTime;
     }
     public void setStationsTime(Map<String, String> stationsTime){
        this.stationsTime=stationsTime;
     }
     public String getTrainInfo(){
        return String.format("Train ID:%s Train No:%s",trainId, trainNo);
     }



}

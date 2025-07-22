package org.BookTicket.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.BookTicket.Entities.Train;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private Train train;
    private List<Train> trainList;
    private static final ObjectMapper objectMapper=new ObjectMapper();
    public static final String TRAINS_PATH="src/main/resources/localDataBase/trains.json";

    public TrainService() throws IOException
    {
      if(trainList==null){
          trainList=loadTrains();
      }
    }
    private List<Train> loadTrains() throws IOException{
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("localDataBase/trains.json");
        if(inputStream==null){
            System.out.println("trains.json not found.Empty train list");
            return new ArrayList<>();
        }
        return objectMapper.readValue(inputStream, new TypeReference<List<Train>>(){});
    }


    public List<Train> searchTrains(String source, String destination)
    {
        if(trainList==null){
            System.out.println("Train list is not initialized");
            return new ArrayList<>();
        }
      return trainList.stream().filter(train-> validTrain(train, source, destination)).collect(Collectors.toList());
    }
    private boolean validTrain(Train train, String source, String destination)
    {
        if(train.getStations()==null) return false;
        List<String> stationOrder=train.getStations().stream().map(String::toLowerCase).collect(Collectors.toList());
        int sourceIndex=  stationOrder.indexOf(source.toLowerCase());
        int destinationIndex=stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex!=-1 && destinationIndex!=-1 && sourceIndex<destinationIndex;
    }
    public void addTrain(Train newTrain){
        Optional<Train> existingTrain=trainList.stream().filter(train->train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if(existingTrain.isPresent())
        {
            updateTrain(newTrain);
        }
        else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }
    public void updateTrain(Train updatedTrain)
    {
        OptionalInt index= IntStream.range(0, trainList.size()).filter(i->trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId())).findFirst();
        if (index.isPresent()){
            //If the train is already present in the list replace existing with updated train
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        }
        else{
            addTrain(updatedTrain);
        }
    }


    private void saveTrainListToFile() {
        try {
            File file = new File(TRAINS_PATH);
            file.getParentFile().mkdirs(); // ✅ Ensure the folders exist
            objectMapper.writeValue(file, trainList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

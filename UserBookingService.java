package org.BookTicket.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.BookTicket.Entities.Tickets;
import org.BookTicket.Entities.Train;
import org.BookTicket.Entities.User;
import org.BookTicket.util.UserServiceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class UserBookingService {

    private User user;
    private List<User> userList;
    private static List<User> cachedUserList=null;

    private static final ObjectMapper objectMapper=new ObjectMapper();
    private static final String USERS_JSON_PATH="localDataBase/users.json";
    private static final String USERS_JSON_FILE_PATH="app/src/main/resources/localDataBase/users.json";


    public UserBookingService() throws IOException{
        if(cachedUserList==null){
            System.out.println("First time loading user list....");
            cachedUserList=loadUserList();
        }
        this.userList=cachedUserList;
    }
    public List<User> loadUserList() throws IOException{
        System.out.println("Debug:"+getClass().getClassLoader().getResource("localDataBase/users.json"));
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream(USERS_JSON_PATH);
        if(inputStream==null){
            throw new FileNotFoundException("users.json not found in resources/localDataBase");
        }
        return objectMapper.readValue(inputStream, new TypeReference<List<User>>(){} );
    }

    public boolean userLogin(User loginAttempt){
        if(loginAttempt == null || loginAttempt.getName()==null || loginAttempt.getPassword()==null){
            System.out.println("Username and password can be left empty.PLease enter username and password.");
            return false;
        }
        Optional<User> foundUser=userList.stream().filter(u-> u.getName()!=null && u.getHashPassword()!=null && u.getName().equals(loginAttempt.getName()) && UserServiceUtil.checkPassword(loginAttempt.getPassword(), u.getHashPassword())).findFirst();
        if(foundUser.isPresent()){
            this.user=foundUser.get();
            return true;
        }
        return false;
    }

    public boolean userSignUp(User user1)
    {
        try{
            if(userList==null)
            {
                userList=new ArrayList<>();
            }
            boolean userExists=userList.stream().anyMatch(u->u.getName()!=null && u.getName().equals(user1.getName()));
            if(userExists){
                System.out.println("Username already exists.Please enter another name");
                return false;
            }
            userList.add(user1);
            saveUserListToFile();
            return true;
        }catch(IOException e) {
            return false;
        }
    }
    private void saveUserListToFile() throws IOException {
        File userFile = new File("app/build/resources/main/localDataBase/users.json");
        userFile.getParentFile().mkdirs();
        objectMapper.writeValue(userFile, userList);
    }
    public void fetchBooking()
    {
        user.printTickets();
    }
    public boolean cancelBooking() throws IOException {

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter ticket ID to cancel");
        String ticketId=sc.nextLine();
        if(ticketId==null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be a null value.");
            return false;

        }
        boolean removed=user.getTicketsBooked().removeIf(ticket->ticket.getTicketId().equals(ticketId));

        if(removed){
            System.out.println("Ticket with id:"+ticketId+"has been cancelled");
            return true;
        }
        else{
            System.out.println("No ticket found with ID:"+ticketId);
            return false;
        }
    }
    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService= new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex)
        {
            System.out.println("Error fetching trains."+ex.getMessage());
            return new ArrayList<>();
        }
    }
    public List<List<Integer>> fetchSeats(Train trainSelectedForBooking){
        return trainSelectedForBooking.getSeats();
    }
    public Boolean bookTrainSeat(Train trainSelectedForBooking, int row, int column){
        try{
            TrainService trainService=new TrainService();
            List<List<Integer>> seats=trainSelectedForBooking.getSeats();
            if(row >= 0 && row < seats.size() && column >= 0 && column < seats.get(row).size())
            {
                if(seats.get(row).get(column)==0)
                {
                    seats.get(row).set(column,1);
                    trainSelectedForBooking.setSeats(seats);
                    trainService.addTrain(trainSelectedForBooking);
                    String ticketId= UUID.randomUUID().toString();
                    Tickets ticket = new Tickets(ticketId, user.getName(), "source_placeholder", "destination_placeholder", new SimpleDateFormat("yyyy-mm-dd").format(new Date()), trainSelectedForBooking);
                    user.getTicketsBooked().add(ticket);
                    return true;//Booking Done
                }else{
                    return false;//Seat is already booked
                }
            }
            else{
                return false;//Invalid row or column index
            }


        }catch(IOException ex) {
            return null;
        }
    }
    public boolean isUserLoggedIn()
    {
        return user!=null;
    }
    public void setUser(User user)
    {
        this.user=user;
    }


}

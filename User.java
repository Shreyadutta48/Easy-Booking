package org.BookTicket.Entities;

import java.util.List;

public class User {
    private String name;
    private String userId;
    private String password;
    private String hashPassword;
    List<Tickets> ticketsBooked;

    public User(String name, String userId, String password, String hashPassword, List<Tickets> ticketsBooked)
    {
        this.name = name;
        this.userId=userId;
        this.password=password;
        this.hashPassword=hashPassword;
        this.ticketsBooked=ticketsBooked;

    }
    public User(){}
    public String getName()
    {
        return name;
    }
    public void setName(String Name){
        this.name=name;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getHashPassword(){
        return hashPassword;
    }
    public void setHashPassword(String hashPassword){
        this.hashPassword=hashPassword;
    }

    public List<Tickets> getTicketsBooked(){
        return ticketsBooked;
    }
    public void setTicketsBooked(List<Tickets> ticketsBooked){
        this.ticketsBooked=ticketsBooked;
    }
    public void printTickets(){
        if(ticketsBooked==null){
            System.out.println("No booking found.");
            return;
        }
        for(int i=0;i<ticketsBooked.size();i++)
            System.out.println(ticketsBooked.get(i).getTicketInfo());
    }
    public boolean cancelTicket(String ticketId)
    {
        return ticketsBooked.removeIf(ticket->ticket.getTicketId().equals(ticketId));
    }

}

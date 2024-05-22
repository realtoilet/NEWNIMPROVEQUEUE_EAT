package com.example.queueeat;

import java.util.List;

public class OrderClass {
    String email;
    List<ForOrderClass> order;
    int seat;
    int queueNumber;
    String docID;

    public OrderClass(String email, List<ForOrderClass> order, int seat, int queueNumber, String docID) {
        this.email = email;
        this.order = order;
        this.seat = seat;
        this.queueNumber = queueNumber;
        this.docID = docID;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ForOrderClass> getOrder() {
        return order;
    }

    public void setOrder(List<ForOrderClass> order) {
        this.order = order;
    }

}

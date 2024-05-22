package com.example.queueeat;

public class StockClass {
    String name, type;
    int stocks;
    String docID;

    public StockClass(String name, int stocks, String docID,String type) {
        this.type = type;
        this.name = name;
        this.stocks = stocks;
        this.docID = docID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}

package com.example.queueeat;

import android.net.Uri;

public class ProductClass {
    String itemName;

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    String itemType;
    Uri itemURL;
    double itemPrice;
    int itemQuantity;
    String docId;
    int itemStocks;

    public ProductClass(String itemName, Uri itemURL, double itemPrice, int itemQuantity, String docId, int itemStocks, String itemType) {
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.docId = docId;
        this.itemStocks = itemStocks;
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public int getItemStocks() {
        return itemStocks;
    }

    public void setItemStocks(int itemStocks) {
        this.itemStocks = itemStocks;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Uri getItemURL() {
        return itemURL;
    }

    public void setItemURL(Uri itemURL) {
        this.itemURL = itemURL;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

}

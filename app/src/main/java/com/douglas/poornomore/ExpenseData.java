package com.douglas.poornomore;

public class ExpenseData {

    private String mDate;
    private String mCategory;
    private String mAmount;

    public ExpenseData(String mDate, String mCategory, String mAmount) {
        this.mDate = mDate;
        this.mCategory = mCategory;
        this.mAmount = mAmount;
    }

    //Getters

    public String getmDate() {
        return mDate;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmAmount() {
        return mAmount;
    }

    //Setters

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }
}

package com.example.onloadtest44;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.


public class PositionExists {

    private String[] cansAssigned = new String[100];

    private int i = 0;

    public void addToNamesData(String canAssetName)
    {
        this.cansAssigned[i] = canAssetName;

        i += 1;
    }

    public String[] getListOfExistingCans()
    {
        return cansAssigned;
    }


}

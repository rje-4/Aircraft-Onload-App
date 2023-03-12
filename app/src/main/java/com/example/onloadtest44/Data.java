package com.example.onloadtest44;

import androidx.appcompat.app.AppCompatActivity;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.

public class Data extends AppCompatActivity
{
    public String[] canNames = new String[100];
    public String[] canPositions = new String[100];
    public String[] canWeights = new String[100];
    public String[] canInspections = new String[100];
    public String[] canVerifications = new String[100];

    public int i = 0;
    public int j = 0;
    public int w = 0;
    public int u = 0;
    public int v = 0;

    public void addToNames(String canName)
    {
        this.canNames[i] = canName;
        i += 1;
    }

    public void addToPositions(String canPosition)
    {
        this.canPositions[j] = canPosition;
        j += 1;
    }

    public void addToWeights(String canWeight)
    {
        this.canWeights[w] = canWeight;
        w += 1;
    }

    public void addToInspections(String canInspection)
    {
        this.canInspections[u] = canInspection;
        u += 1;
    }

    public void addToVerifications(String canVerification)
    {
        this.canVerifications[v] = canVerification;
        v += 1;
    }

    public String[] getFinalWeightArray()
    {
        return this.canWeights;
    }

    public String[] getFinalNameArray()
    {
        return this.canNames;
    }

    public String[] getFinalPositionArray()
    {
        return this.canPositions;
    }

    public String[] getFinalInspectionArray()
    {
        return this.canInspections;
    }

    public String[] getFinalVerificationArray()
    {
        return this.canVerifications;
    }

}

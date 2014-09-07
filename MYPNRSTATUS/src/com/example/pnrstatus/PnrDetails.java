package com.example.pnrstatus;

import java.io.Serializable;

public class PnrDetails implements Serializable
{
	String pnrNo;
	String trainName;
	String dateOfJ;
	String currStat;
	String time;
	
	public PnrDetails()
	{
		super();
	}
	
	public PnrDetails(String pnrNo,String trainName,String dateOfJ,String currStat,String time)
	{
		super();
		this.pnrNo = pnrNo;
		this.trainName = trainName;
		this.dateOfJ = dateOfJ;
		this.currStat = currStat;
		this.time = time;
	}

	public String getPnrNo() 
	{
		return pnrNo;
	}
	public void setPnrNo(String pnrNo) 
	{
		this.pnrNo = pnrNo;
	}
	public String getTrainName() 
	{
		return trainName;
	}
	public void setTrainName(String trainName) 
	{
		this.trainName = trainName;
	}
	public String getDateOfJ() 
	{
		return dateOfJ;
	}
	public void setDateOfJ(String dateOfJ) 
	{
		this.dateOfJ = dateOfJ;
	}
	
	public String getCurrStat() 
	{
		return currStat;
	}
	public void setCurrStat(String currStat) 
	{
		this.currStat = currStat;
	}
	
	public String getTime() 
	{
		return time;
	}
	public void setTime(String time) 
	{
		this.time = time;
	}

	@Override
	public String toString() 
	{
		return pnrNo+" "+dateOfJ+" "+currStat;
	}

	
}

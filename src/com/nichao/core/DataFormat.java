package com.nichao.core;

public class DataFormat {

	public double precision=0;
	public double recall=0;
	public double fmeasure=0;
	public double auc=0;
	public int num=0;
	public double RedundancyRate=0;
	public double RedundancyRate1=0;
	
	public DataFormat()
	{
		precision=0;
		recall=0;
		fmeasure=0;
		auc=0;
		num=0;
		RedundancyRate=0;
		RedundancyRate1=0;
	}
	
	public void setData(double pre, double rec, double fme, double au,int nu, double Redundancy, double redundancy1)
	{
		precision=pre;
		recall=rec;
		fmeasure=fme;
		auc=au;	
		num=nu;
		RedundancyRate=Redundancy;
		RedundancyRate1=redundancy1;
	}
	public void add(double pre, double rec, double fme, double au, int nu, double Redundancy, double redundancy1)
	{
		precision+=pre;
		recall+=rec;
		fmeasure+=fme;
		auc+=au;
		num+=nu;
		RedundancyRate+=Redundancy;
		RedundancyRate1+=redundancy1;
	}
	
	public double getPrecision()
	{
		return precision;
	}
	public double getRecall()
	{
		return recall;
	}
	public double getAuc()
	{
		return auc;
	}
	public double getFmeasure()
	{
		return fmeasure;
	}
	public int getNumfeatures()
	{
		return num;
	}	
	public double getRedundancy()
	{
		return RedundancyRate;
	}
	public double getRedundancy1()
	{
		return RedundancyRate1;
	}
	public void clear()
	{
		 precision=0;
		 recall=0;
		 fmeasure=0;
		 auc=0;
		 num=0;
		 RedundancyRate=0;
		 RedundancyRate1=0;
	}
}

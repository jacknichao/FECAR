package com.nichao.util;

import weka.attributeSelection.SymmetricalUncertAttributeEval;
import weka.core.Instances;
import weka.filters.supervised.attribute.Discretize;

import java.text.DecimalFormat;

public class FeatureFeature {
	
	//“—≤‚ ‘£¨’˝»∑°£
	public static double[][] getFfSu(Instances instances)
	{
		double su[][]=new double[instances.numAttributes()-1][instances.numAttributes()-1];
		try {			
			instances.setClassIndex(instances.numAttributes()-1);		
			Discretize discretize=new Discretize();
			
		  	discretize.setInputFormat(instances);			
			instances=weka.filters.Filter.useFilter(instances, discretize);
			
			DecimalFormat df = new DecimalFormat( "0.000 ");
			SymmetricalUncertAttributeEval sU=new SymmetricalUncertAttributeEval();
			
			for(int i=0;i<instances.numAttributes()-1;++i)
			{
				if(instances.attribute(i).numValues()==1)
				{
					for(int k=0;k<instances.numAttributes()-1;++k) {su[i][k]=0; su[k][i]=0;}
					su[i][i]=1;
				}
					
				else
				{
					instances.setClassIndex(i);
					sU.buildEvaluator(instances);
					for(int j=i;j<instances.numAttributes()-1;++j)
					  if(instances.attribute(j).numValues()!=1) 
					  {
						  su[i][j]=Double.parseDouble(df.format(sU.evaluateAttribute(j)));
						  su[j][i]= su[i][j];
					  }
									
				}
				
			}
							
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return su;		
	}	
}

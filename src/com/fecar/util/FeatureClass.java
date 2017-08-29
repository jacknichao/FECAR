package com.fecar.util;

import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.attributeSelection.SymmetricalUncertAttributeEval;
import weka.core.Instances;

public class FeatureClass {
		
	//已测试，正确
	public double[]getFTSu(Instances instances)
	{
		double su[]=new double[instances.numAttributes()-1];
		try {			
			instances.setClassIndex(instances.numAttributes()-1);		

			SymmetricalUncertAttributeEval sU=new SymmetricalUncertAttributeEval();
			
			sU.buildEvaluator(instances);
			
			for(int i=0;i<instances.numAttributes()-1;++i)
			su[i]=sU.evaluateAttribute(i);
					 				
			
							
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return su;		
	}	
	
	//已测试，正确
		public double[]getFTIG(Instances instances)
		{
			double IG[]=new double[instances.numAttributes()-1];
			try {			
				instances.setClassIndex(instances.numAttributes()-1);		

				InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
				infoGain.buildEvaluator(instances);
				
				for(int i=0;i<instances.numAttributes()-1;++i)
				IG[i]=infoGain.evaluateAttribute(i);				
								
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return IG;		
		}	
		
//已测试，正确
		public double[]getFTRelief(Instances instances)
		{
			double RF[]=new double[instances.numAttributes()-1];
			try {			
				instances.setClassIndex(instances.numAttributes()-1);		

				ReliefFAttributeEval RelifF=new ReliefFAttributeEval();
				String []options={"-M","600"};
				RelifF.setOptions(options);
				RelifF.buildEvaluator(instances);
				
				for(int i=0;i<instances.numAttributes()-1;++i)
				RF[i]=RelifF.evaluateAttribute(i);				
								
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return RF;		
		}			
	
//已测试，正确
	public double[]getFTChisquare(Instances instances)
	{
		double CS[]=new double[instances.numAttributes()-1];
		try {			
			instances.setClassIndex(instances.numAttributes()-1);		

			ChiSquaredAttributeEval cSquaredAttributeEval = new ChiSquaredAttributeEval();
			cSquaredAttributeEval.buildEvaluator(instances);
			
			for(int i=0;i<instances.numAttributes()-1;++i)
			CS[i]=cSquaredAttributeEval.evaluateAttribute(i);				
							
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return CS;		
	}	


}

package com.nichao.core;

import com.nichao.util.FeatureFeature;
import weka.core.Instances;


public class RedundancyRate {

	/**
	 * @param
	 */
		
	//统计权重大于等于threshold的边的数量，用sigmoid函数进行归一化。
	public double getRedundancyRate(Instances instances, double threshold)
	{
		int num=instances.numAttributes()-1;
		
		if(num==1) return 0;
		double [][] SimilarityMatrix = FeatureFeature.getFfSu(instances);
		
			
		double sum=0;
		for(int i=0;i<num;++i)
			for(int j=0;j<num;++j)
				if(SimilarityMatrix[i][j]>=0.5)
					sum+=SimilarityMatrix[i][j];
		
		sum=sum-num;//减到与自身的相关性
		sum=sum/2;
		double redundancy=(1/(1+Math.exp(-0.1*sum))-0.5)*2;
			
		return redundancy;
	}
	
	public double getRedundancyRate1(Instances instances, double threshold)
	{
		int num=instances.numAttributes()-1;
		
		if(num==1) return 0;
		double [][] SimilarityMatrix =FeatureFeature.getFfSu(instances);
		
			
		double sum=0;
		for(int i=0;i<num;++i)
			for(int j=0;j<num;++j)
				if(SimilarityMatrix[i][j]>=0.5)
					sum+=SimilarityMatrix[i][j];
		
		sum=sum-num;//减到与自身的相关性

		double redundancy=sum/(num*(num-1));
			
		return redundancy;
	}

}

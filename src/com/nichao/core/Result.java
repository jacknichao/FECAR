package com.nichao.core;

import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * 该类主要用来保存实验结果以被上层函数调用
 */
public class Result {
	/**
	 * 实际选出的特征的个数，有些基准特征选择方法需要指定特征的个数如OneR_Ranker，FECAR等等
	 * 而有些基准的特征选择算法则会自动筛选出来特征个数
	 */
	private int realSelectedNum = 0;

	private Evaluation evaluation = null;

	private String fileName = null;

	private int outerIterate = 0;

	private int innerIterate = 0;

	private Instances instances = null;

	private int targetNumSelect = 0;

	private BaseClassiferEnum baseClassiferEnum = null;

	private FeatureSelectMethodEnum fsEnum = null;


	public Result(String fileName,int outerIterate,int innerIterate,int realSelectedNum,
	              BaseClassiferEnum baseClassiferEnum,
	              FeatureSelectMethodEnum fsEnum,
	              Evaluation evaluation ){
		this.fileName=fileName;
		this.outerIterate=outerIterate;
		this.innerIterate=innerIterate;
		this.realSelectedNum=realSelectedNum;
		this.baseClassiferEnum=baseClassiferEnum;
		this.fsEnum=fsEnum;
		this.evaluation=evaluation;
	}


	public int getRealSelectedNum() {
		return realSelectedNum;
	}

	public void setRealSelectedNum(int realSelectedNum) {
		this.realSelectedNum = realSelectedNum;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}



	@Override
	public String toString() {
		return fileName + "," + outerIterate + "," + innerIterate + "," + realSelectedNum + ","
				+ baseClassiferEnum + "," + fsEnum + "," + evaluation.weightedAreaUnderROC() + "\r\n";

	}
}

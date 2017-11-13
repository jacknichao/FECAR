package com.nichao.core;

import weka.classifiers.Evaluation;

/**
 * 该类主要用来保存实验结果以被上层函数调用
 */
public class Result {
	/**
	 * 实际选出的特征的个数，有些基准特征选择方法需要指定特征的个数如OneR_Ranker，FECAR等等
	 * 而有些基准的特征选择算法则会自动筛选出来特征个数
	 * */
	private int realSelectedNum=0;

	private Evaluation evaluation=null;


	/**
	 *
	 * @param realSelectedNum 实际选择出来的特征的个数
	 * @param evaluation 特征选择算法的评价结果
	 */
	public Result(int realSelectedNum,Evaluation evaluation){
		this.realSelectedNum=realSelectedNum;
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
}

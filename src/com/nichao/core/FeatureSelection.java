package com.nichao.core;

import com.nichao.util.FeatureClass;
import com.nichao.util.FeatureFeature;
import weka.attributeSelection.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.util.ArrayList;


public class FeatureSelection {

	/**(1)Filter-base ranking methods***************************************************************************************************/

	/**
	 * Filter-based ranking method:
	 * statistic based
	 * ChiSquared+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] ChiSquared_Ranker(Instances train,int numSelected){

		AttributeSelection attrsel=new AttributeSelection();
		ChiSquaredAttributeEval eval=new ChiSquaredAttributeEval();
		Ranker search=new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices=null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * Filter-based ranking method:
	 * probability based
	 * GainRatio+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] GainRatio_Ranker(Instances train,int numSelected){

		AttributeSelection attrsel=new AttributeSelection();
		GainRatioAttributeEval eval=new GainRatioAttributeEval();
		Ranker search=new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices=null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * Filter-based ranking method:
	 * probability based
	 * InfoGain+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] InfoGain_Ranker(Instances train,int numSelected){

		AttributeSelection attrsel=new AttributeSelection();
		InfoGainAttributeEval eval=new InfoGainAttributeEval();
		Ranker search=new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices=null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * Filter-based ranking method:
	 * probability based
	 * SymmetricalUncert+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] SymmetricalUncert_Ranker(Instances train,int numSelected){

		AttributeSelection attrsel=new AttributeSelection();
		SymmetricalUncertAttributeEval eval=new SymmetricalUncertAttributeEval();
		Ranker search=new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices=null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}



	/**
	 * Filter-based ranking method:
	 * instance based
	 * ReliefF+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] ReliefF_Ranker(Instances train,int numSelected){

		AttributeSelection attrsel=new AttributeSelection();
		ReliefFAttributeEval eval=new ReliefFAttributeEval();

		Ranker search=new Ranker();
		search.setNumToSelect(numSelected);


		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices=null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}



	/**
	 * Filter-based ranking method:
	 * classifier based
	 * SVM+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] SVM_Ranker(Instances train,int numSelected) {

		AttributeSelection attrsel = new AttributeSelection();
		SVMAttributeEval eval = new SVMAttributeEval();

		Ranker search = new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * Filter-based ranking method:
	 * classifier based
	 * OneR+Ranker
	 * @param train 选择特在的训练数据集
	 * @param  numSelected 选择的特征个数
	 * @return 选择的特征索引
	 */
	public static int[] OneR_Ranker(Instances train,int numSelected) {

		AttributeSelection attrsel = new AttributeSelection();
		OneRAttributeEval eval=new OneRAttributeEval();

		Ranker search = new Ranker();
		search.setNumToSelect(numSelected);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}



	/**(2)Filter-base subset methods***************************************************************************************************/


	/**
	 * Filter-based subset method:
	 * correlation based
	 * CfsSubsetEval+GreedyStepwise
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] CfsSubset_GreegyStepwise(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		CfsSubsetEval eval=new CfsSubsetEval();
		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}



	/**
	 * Filter-based subset method:
	 * consistency based
	 * ConsistencySubset+GreedyStepwise
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] ConsistencySubset_GreegyStepwise(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		ConsistencySubsetEval eval=new ConsistencySubsetEval();
		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}




	/**(3)FECAR****************************************************************************************************/

	/**
	 * 使用信息增益来处理相关性完成关于聚类的属性选择。
	 ** @param train 用来寻找保留特征的训练数据
	 * @param numSelect 需要选择的特征数量
	 * @return 保留的特征索引
	 */
	private static int[] Cluster_IG_FeatureSelection(Instances train, int numSelect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = FeatureFeature.getFfSu(train);
		double[] trelev = FeatureClass.getFCIG(train);

		int numclusters=(int)Math.floor(Math.log(train.numAttributes()-1)/2);

		return Kmedoid_attrsel(train, numclusters, numSelect, similarity, trelev);
	}

	/**
	 *使用chisquare来处理相关性完成关于聚类的属性选择。
	 * @param train 用来寻找保留特征的训练数据
	 * @param numSelect 需要选择的特征数量
	 * @return 保留的特征索引
	 */
	private static int[] Cluster_ChiSquare_FeatureSelection(Instances train,int numSelect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = FeatureFeature.getFfSu(train);
		double[] trelev = FeatureClass.getFCChisquare(train);

		int numclusters=(int)Math.floor(Math.log(train.numAttributes()-1)/2);

		return Kmedoid_attrsel(train, numclusters, numSelect, similarity, trelev);
	}

	/**
	 * 使用信息增益来处理相关性完成关于聚类的属性选择。
	 * @param train 用来寻找保留特征的训练数据
	 * @param numSelect 需要选择的特征数量
	 * @return 保留的特征索引
	 */
	private static int[] Cluster_ReliefF_FeatureSelection(Instances train,  int numSelect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = FeatureFeature.getFfSu(train);
		double[] trelev = FeatureClass.getFCRelief(train);

		int numclusters=(int)Math.floor(Math.log(train.numAttributes()-1)/2);

		return Kmedoid_attrsel(train, numclusters, numSelect, similarity, trelev);
	}

	/**
	 *使用su来处理相关性完成关于聚类的属性选择。
	 * @param train 用来寻找保留特征的训练数据
	 * @param numSelect 需要选择的特征数量
	 * @return 保留的特征索引
	 */
	private static int[]  Cluster_SU_FeatureSelection(Instances train,  int numSelect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = FeatureFeature.getFfSu(train);
		double[] trelev = FeatureClass.getFCSu(train);

		int numclusters=(int)Math.floor(Math.log(train.numAttributes()-1)/2);

		return Kmedoid_attrsel(train, numclusters, numSelect, similarity, trelev);
	}

	/**
	 *使用我们自己实现的方法做属性选择。 前两个参数分别是训练集和测试集，后两个参数法分别是要聚的簇的数量和要选择的属性数量,
	 最后两个参数是根据训练集算出来的属性的两两相关性矩阵和与类别相关性矩阵。
	 */
	private static int[] Kmedoid_attrsel(Instances train, int numclusters, int numSelect, double[][] similarity, double[] trelev) {
		int[] selectedArr=null;
		try {

			Clusters clusters = new Clusters(similarity, trelev, numclusters, train.numAttributes() - 1);
			clusters.clustrUnitlConvergence();
			clusters.rankClusters();//完成聚类

			ArrayList<Integer> selectedArraylist = clusters.selectAttributes(numSelect);
			selectedArr = new int[selectedArraylist.size()];
			for (int i = 0; i < selectedArraylist.size(); ++i)
				selectedArr[i] = selectedArraylist.get(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedArr;
	}


	/**(4) wrapper based***************************************************************************************************/

	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(forward)
	 * 基分类器是NB
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseNB(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		NaiveBayes naiveBayes=new NaiveBayes();
		eval.setClassifier(naiveBayes);
		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(forward)
	 * 基分类器是J48
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseJ48(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		J48 j48=new J48();
		eval.setClassifier(j48);
		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(forward)
	 * 基分类器是LR
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseLR(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		Logistic logistic=new Logistic();
		eval.setClassifier(logistic);

		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(forward)
	 * 基分类器是IBK
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseKNN(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		IBk iBk=new IBk();
		eval.setClassifier(iBk);

		GreedyStepwise search=new GreedyStepwise();

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}




	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(backward)
	 * 基分类器是NB
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseNB_Back(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		NaiveBayes naiveBayes=new NaiveBayes();
		eval.setClassifier(naiveBayes);

		GreedyStepwise search=new GreedyStepwise();
		search.setSearchBackwards(true);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(backward)
	 * 基分类器是J48
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseJ48_Back(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		J48 j48=new J48();
		eval.setClassifier(j48);

		GreedyStepwise search=new GreedyStepwise();
		search.setSearchBackwards(true);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(backward)
	 * 基分类器是LR
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseLR_Back(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		Logistic logistic=new Logistic();
		eval.setClassifier(logistic);

		GreedyStepwise search=new GreedyStepwise();
		search.setSearchBackwards(true);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * wrapper-based
	 * wrapper based greedy forward selection
	 * WrapperSubset+GreedyStepwise(backward)
	 * 基分类器是KNN
	 * @param train 选择特在的训练数据集
	 * @return 选择的特征索引
	 */
	public static int[] WrapperSubset_GreegyStepwiseKNN_Back(Instances train) {

		AttributeSelection attrsel = new AttributeSelection();

		WrapperSubsetEval eval=new WrapperSubsetEval();
		IBk iBk=new IBk();
		eval.setClassifier(iBk);

		GreedyStepwise search=new GreedyStepwise();
		search.setSearchBackwards(true);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);

		int[] indices = null;
		try {
			attrsel.SelectAttributes(train);
			indices = attrsel.selectedAttributes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return indices;
	}


	/**
	 * 调用指定的特征选择方法来完成特征选择任务
	 * @param train 训练集
	 * @param featureSelectMethodEnum   特征选择方法的枚举
	 * @param numSelect 需要选择的特征数量
	 * @return 返回需要保留的属性的索引数组
	 */
	public static int[] doFeatureSelection(Instances train, FeatureSelectMethodEnum featureSelectMethodEnum,int numSelect){
		int[] selectedIndex=null;
		switch (featureSelectMethodEnum){
			case Cluster_ChiSquare_FeatureSelection:
				selectedIndex=Cluster_ChiSquare_FeatureSelection(train,numSelect);
				break;
			case Cluster_ReliefF_FeatureSelection:
				selectedIndex=Cluster_ReliefF_FeatureSelection(train,numSelect);
				break;
			case Cluster_SU_FeatureSelection:
				selectedIndex=Cluster_SU_FeatureSelection(train,numSelect);
				break;
			case Cluster_IG_FeatureSelection:
				selectedIndex=Cluster_IG_FeatureSelection(train,numSelect);
				break;
			case SVM_Ranker:
				selectedIndex=SVM_Ranker(train,numSelect);
				break;
			case OneR_Ranker:
				selectedIndex=OneR_Ranker(train,numSelect);
				break;
			case ReliefF_Ranker:
				selectedIndex=ReliefF_Ranker(train,numSelect);
				break;
			case InfoGain_Ranker:
				selectedIndex=InfoGain_Ranker(train,numSelect);
				break;
			case GainRatio_Ranker:
				selectedIndex=GainRatio_Ranker(train,numSelect);
				break;
			case ChiSquared_Ranker:
				selectedIndex=ChiSquared_Ranker(train,numSelect);
				break;
			case CfsSubset_GreegyStepwise:
				selectedIndex=CfsSubset_GreegyStepwise(train);
				break;
			case SymmetricalUncert_Ranker:
				selectedIndex=SymmetricalUncert_Ranker(train,numSelect);
				break;
			case ConsistencySubset_GreegyStepwise:
				selectedIndex=ConsistencySubset_GreegyStepwise(train);
				break;
			case WrapperSubset_GreegyStepwiseLR:
				selectedIndex=WrapperSubset_GreegyStepwiseLR(train);
				break;
			case WrapperSubset_GreegyStepwiseNB:
				selectedIndex=WrapperSubset_GreegyStepwiseNB(train);
				break;
			case WrapperSubset_GreegyStepwiseJ48:
				selectedIndex=WrapperSubset_GreegyStepwiseJ48(train);
				break;
			case WrapperSubset_GreegyStepwiseKNN:
				selectedIndex=WrapperSubset_GreegyStepwiseKNN(train);
				break;
			case WrapperSubset_GreegyStepwiseNB_Back:
				selectedIndex=WrapperSubset_GreegyStepwiseNB_Back(train);
				break;
			case WrapperSubset_GreegyStepwiseJ48_Back:
				selectedIndex=WrapperSubset_GreegyStepwiseJ48_Back(train);
				break;
			case WrapperSubset_GreegyStepwiseLR_Back:
				selectedIndex=WrapperSubset_GreegyStepwiseLR_Back(train);
				break;
			case WrapperSubset_GreegyStepwiseKNN_Back:
				selectedIndex=WrapperSubset_GreegyStepwiseKNN_Back(train);
				break;
				default:
				throw  new RuntimeException("we can`t handle this case:"+featureSelectMethodEnum);
		}

		return selectedIndex;

	}


}

package com.fecar.core;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

public class ClassifierValidation {
	

	/**
	 * 使用指定的分类器在测试集上进行3-折交叉验证
	 * @param test              验证集
	 * @param classifierName    分类器名称的全路径
	 * @param options           基分类器在构造时候的构造选项
	 * @return
	 */
	public static Evaluation doThreeCrossValidation(Instances test,String classifierName, String[] options){
		Evaluation evaluation=null;
		try {
			test.setClassIndex(test.numAttributes()-1);
			Classifier classifier= (Classifier) Utils.forName(Classifier.class,classifierName,options);

			evaluation=new Evaluation(test);

			evaluation.crossValidateModel(classifier, test,3, new Random(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return evaluation;
	}


	/**
	 * 使用指定的分类器在测试集上进行3-折交叉验证
	 * @param test              验证集
	 * @param baseClassiferEnum    基分类器名的枚举
	 * @param options           基分类器在构造时候的构造选项
	 * @return
	 */
	public static  Evaluation  classify(Instances test, BaseClassiferEnum baseClassiferEnum,String[] options) {
		Evaluation evaluation = null;
		switch (baseClassiferEnum) {
			case NB:
				evaluation = doThreeCrossValidation(test, "weka.classifiers.bayes.NaiveBayes", options);
				break;
			case LR:
				evaluation = doThreeCrossValidation(test, "weka.classifiers.functions.Logistic", options);
				break;
			case IBK:
				evaluation = doThreeCrossValidation(test, "weka.classifiers.lazy.IBk", options);
				break;
			case J48:
				evaluation = doThreeCrossValidation(test, "weka.classifiers.trees.J48", options);
				break;
			default:
				throw new RuntimeException("未知的基分类器，请查证!" + baseClassiferEnum);
		}

		return evaluation;
	}



}

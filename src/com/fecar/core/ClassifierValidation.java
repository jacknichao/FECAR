package com.fecar.core;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

public class ClassifierValidation {
	

	/**
	 * ʹ��ָ���ķ������ڲ��Լ��Ͻ���3-�۽�����֤
	 * @param test              ��֤��
	 * @param classifierName    ���������Ƶ�ȫ·��
	 * @param options           ���������ڹ���ʱ��Ĺ���ѡ��
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
	 * ʹ��ָ���ķ������ڲ��Լ��Ͻ���3-�۽�����֤
	 * @param test              ��֤��
	 * @param baseClassiferEnum    ������������ö��
	 * @param options           ���������ڹ���ʱ��Ĺ���ѡ��
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
				throw new RuntimeException("δ֪�Ļ������������֤!" + baseClassiferEnum);
		}

		return evaluation;
	}



}

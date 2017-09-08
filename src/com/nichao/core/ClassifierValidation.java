package com.nichao.core;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

/**
 * 完成训练性能和适应值的计算
 * 训练性能是指：在对训练集和测试集完成特征过滤之后，在训练集train上构建训练模型，然后在测试集test上进行预测
 * 适应值：直接在测试集test上完成交叉检验
 */
public class ClassifierValidation {


	/**
	 * 计算预测性能
	 * <p>
	 * 在训练集上构建预测模型后，在测试集上进行预测
	 *
	 * @param train          训练集
	 * @param test           验证集
	 * @param classifierName 分类器名称的全路径
	 * @param options        基分类器在构造时候的构造选项
	 * @return
	 */
	public static Evaluation doPredictEvaluation(Instances train, Instances test, String classifierName, String[] options) {
		Evaluation evaluation = null;
		try {
			train.setClassIndex(train.numAttributes() - 1);
			test.setClassIndex(test.numAttributes() - 1);

			Classifier classifier = (Classifier) Utils.forName(Classifier.class, classifierName, options);
			classifier.buildClassifier(train);
			evaluation = new Evaluation(test);

			evaluation.evaluateModel(classifier, test);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return evaluation;
	}


	/**
	 * 计算适应值
	 * <p>
	 * 使用指定的分类器在测试集上进行3-折交叉验证
	 *
	 * @param test           验证集
	 * @param classifierName 分类器名称的全路径
	 * @param options        基分类器在构造时候的构造选项
	 * @return
	 */
	public static Evaluation doThreeCrossValidation(Instances test, String classifierName, String[] options) {
		Evaluation evaluation = null;
		try {
			test.setClassIndex(test.numAttributes() - 1);
			Classifier classifier = (Classifier) Utils.forName(Classifier.class, classifierName, options);

			evaluation = new Evaluation(test);

			evaluation.crossValidateModel(classifier, test, 3, new Random(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return evaluation;
	}


	/**
	 * 使用指定的分类器在测试集上进行3-折交叉验证
	 *
	 * @param train             过滤后的训练集数据
	 * @param test              验证集
	 * @param baseClassiferEnum 基分类器名的枚举
	 * @param options           基分类器在构造时候的构造选项
	 * @return 返回两个Evaluation结果：
	 * 第一个表示在训练集上train训练，在测试集test进行测试得到的结果
	 * 第二个结果表示直接在测试集上进行三折交叉检验得到的结果，也就是在多目标优化中，在验证集上的适应值
	 */
	public static Evaluation[] classify(Instances train, Instances test, BaseClassiferEnum baseClassiferEnum, String[] options) {
		Evaluation[] evaluation = new Evaluation[2];
		switch (baseClassiferEnum) {
			case NB:
				evaluation[0] = doPredictEvaluation(new Instances(train), new Instances(test), "weka.classifiers.bayes.NaiveBayes", options);
				evaluation[1] = doThreeCrossValidation(new Instances(test), "weka.classifiers.bayes.NaiveBayes", options);
				break;
			case LR:
				evaluation[0] = doPredictEvaluation(new Instances(train), new Instances(test), "weka.classifiers.functions.Logistic", options);
				evaluation[1] = doThreeCrossValidation(new Instances(test), "weka.classifiers.functions.Logistic", options);
				break;
			case KNN:
				evaluation[0] = doPredictEvaluation(new Instances(train), new Instances(test), "weka.classifiers.lazy.IBk", options);
				evaluation[1] = doThreeCrossValidation(test, "weka.classifiers.lazy.IBk", options);
				break;
			case J48:
				evaluation[0] = doPredictEvaluation(new Instances(train), new Instances(test), "weka.classifiers.trees.J48", options);
				evaluation[1] = doThreeCrossValidation(new Instances(test), "weka.classifiers.trees.J48", options);
				break;
			default:
				throw new RuntimeException("未知的基分类器，请查证!" + baseClassiferEnum);
		}

		return evaluation;
	}


}

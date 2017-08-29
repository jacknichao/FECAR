package com.fecar.driver;

import com.fecar.core.*;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;

public class Driver {
	/**
	 * 项目数据的基准路径
	 */
	private static String basePath="/home/jacknichao/datasets/Relink/";



	/**
	 * 将源项目分成70%的训练集和30%的验证集，对两者进行特征过滤，最后在30%的数据集上3-折交叉验证
	 * @param fullfilename  有完整路径的项目名称
	 * @param baseClassiferEnum 基分类器枚举
	 * @param numSelected   需要选择的特征个数
	 * @return 3-折交叉验证的结果
	 */
	public static Evaluation doCrossValidation(String fullfilename,BaseClassiferEnum baseClassiferEnum,int numSelected)
	{
		Evaluation 	evaluations=null;
		try {
			Instances instances=ConverterUtils.DataSource.read(fullfilename);
			instances.setClassIndex(instances.numAttributes()-1);
			if (instances.classAttribute().isNominal()) {
				instances.stratify(10);
			}

			//计算前70%的实例的索引
			int index = (int) Math.round(instances.numInstances() * 0.7);
			Instances train = new Instances(instances, 0, index);
			Instances test = new Instances(instances, index, instances.numInstances() - index);

			//medoid+IG属性选择的结果
			FeatureSelection.doFeatureSelection(train,test, FeatureSelectMethodEnum.Cluster_IG_FeatureSelection,numSelected,(int)Math.floor(numSelected/2));
			evaluations= ClassifierValidation.classify(test,baseClassiferEnum,null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return evaluations;
	}



	public void getThreeCrossValidation(String fullfilename,BaseClassiferEnum baseClassiferEnum,int numSelected)
	{
		double wauc=0.0d;
		double wfm=0.0d;
		int num=10;
		for(int i=0;i<num;++i)
		{
			Evaluation evaluation=doCrossValidation(fullfilename,baseClassiferEnum,numSelected);
			wauc+=evaluation.weightedAreaUnderROC();
			wfm+=evaluation.weightedFMeasure();
		}

		wauc=wauc/num;
		wfm=wfm/num;

		System.out.println("平均性能:"+wauc+"---"+wfm);
	}





	/**
	 *
	 * @param fullfilename
	 * @param baseClassiferEnum
	 */
	public static void doDefectPrediciton(String fullfilename,BaseClassiferEnum baseClassiferEnum)
	{

		try {
			File file=new File(fullfilename);

			String resultfilename="./"+file.getName()+"_"+baseClassiferEnum.toString()+".csv";

			Writer wrResult=new OutputStreamWriter(new FileOutputStream(resultfilename));
			Instances instances=new Instances(new BufferedReader(new FileReader(fullfilename)));

			int numAttr = instances.numAttributes()-1;
			wrResult.write("Selected,Wauc,WFm\r\n");

			for(int i=5;i<101;i=i+5)
			{

				int seleNum = numAttr*i/100;
				Evaluation evaluation=doCrossValidation(fullfilename,baseClassiferEnum,seleNum);

				wrResult.write(Integer.toString(seleNum)+",");
				wrResult.write(evaluation.weightedAreaUnderROC()+",");
				wrResult.write(evaluation.weightedFMeasure()+",");
				wrResult.write("\r\n");
			}

			wrResult.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public static void main(String[] args) {
		try {

			String projectFullPath="/home/jacknichao/datasets/Relink/Apache.arff";

			Driver.doDefectPrediciton(projectFullPath,BaseClassiferEnum.NB);
		System.out.println("finished");
	        
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

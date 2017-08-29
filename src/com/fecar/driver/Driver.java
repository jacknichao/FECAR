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
	 * ��Ŀ���ݵĻ�׼·��
	 */
	private static String basePath="/home/jacknichao/datasets/Relink/";



	/**
	 * ��Դ��Ŀ�ֳ�70%��ѵ������30%����֤���������߽����������ˣ������30%�����ݼ���3-�۽�����֤
	 * @param fullfilename  ������·������Ŀ����
	 * @param baseClassiferEnum ��������ö��
	 * @param numSelected   ��Ҫѡ�����������
	 * @return 3-�۽�����֤�Ľ��
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

			//����ǰ70%��ʵ��������
			int index = (int) Math.round(instances.numInstances() * 0.7);
			Instances train = new Instances(instances, 0, index);
			Instances test = new Instances(instances, index, instances.numInstances() - index);

			//medoid+IG����ѡ��Ľ��
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

		System.out.println("ƽ������:"+wauc+"---"+wfm);
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

package com.nichao.driver;

import com.nichao.core.*;
import com.nichao.util.MyTools;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Driver {

	/**
	 * 将源项目分成70%的训练集和30%的验证集，对两者进行特征过滤，最后在30%的数据集上3-折交叉验证
	 * @param instances  项目数据的实例
	 * @param baseClassiferEnum 基分类器枚举
	 * @param numSelected   需要选择的特征个数
	 * @return 3-折交叉验证的结果
	 */
	public static Evaluation doCrossValidation(Instances instances,int outerIterate, int innerIterate,int numSelected,BaseClassiferEnum baseClassiferEnum, FeatureSelectMethodEnum fsEnum)
	{
		Evaluation 	evaluations=null;
		try {
			instances.setClassIndex(instances.numAttributes()-1);

			//计算前70%的实例的索引
			int index = (int) Math.round(instances.numInstances() * 0.7);
			Instances train = new Instances(instances, 0, index);
			Instances test = new Instances(instances, index, instances.numInstances() - index);



			//除了不使用特征选择技术的，都需要进行特征过滤处理
			if(fsEnum!=FeatureSelectMethodEnum.FULL) {
				int[] savedFeature = FeatureSelection.doFeatureSelection(train, fsEnum, numSelected);
				Remove remove = new Remove();
				//相反选择设置，表示输入的属性索引都是需要保存的
				remove.setInvertSelection(true);
				remove.setAttributeIndicesArray(savedFeature);
				remove.setInputFormat(train);
				//Filter默认会产生新的Instances因此需要将train和test指向新产生的Instances
				test = Filter.useFilter(test, remove);
			}

			evaluations= ClassifierValidation.classify(test,baseClassiferEnum,null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return evaluations;
	}


	/**
	 * 完成内层十次循环
	 * @param fileName  文件名称
	 * @param instances 被外层随机化后的项目实例
	 * @param outerIterate  外层循环的编号
	 * @param numSelect 需要选择的特征个数
	 * @param writer    保存结果的文件句柄
	 */
	public static void doDefectPrediciton(String fileName,Instances instances,int outerIterate,int numSelect,BufferedWriter writer)
	{

		try {
			//这里进行内层的10次随机循环
			for (int innerIterate = 1; innerIterate <= 10; innerIterate++) {
				//内层随机化
				instances.setClassIndex(instances.numAttributes() - 1);
				instances.randomize(new Random(outerIterate + 1));
				if (instances.classAttribute().isNominal()) {
					instances.stratify(10);
				}

				//遍历每一个特征选择方法
				for (FeatureSelectMethodEnum fsEnum : FeatureSelectMethodEnum.values()) {
					//遍历每一个集分类
					for (BaseClassiferEnum baseClassiferEnum : BaseClassiferEnum.values()) {

						//用序列花操作过后的实例重新生成一份，以防止对后续实验结果产生影响
						Evaluation evaluation = doCrossValidation(new Instances(instances), outerIterate, innerIterate, numSelect, baseClassiferEnum, fsEnum);
						writer.write(fileName + "," + outerIterate + "," + innerIterate + "," + numSelect + "," + baseClassiferEnum + "," + fsEnum + "," + evaluation.weightedAreaUnderROC() + "\r\n");
					}

				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}




	public static void main(String[] args) {
		ArrayList<File> currenctDataSet=null;

		String datasetName=MyTools.getBaseInfo("datasetName");
		String resultPath=MyTools.getBaseInfo("resultPath");

		if(!Files.exists(Paths.get(resultPath))){
			try {
				Files.createDirectories(Paths.get(resultPath));
			} catch (IOException e) {
				System.out.println("创建结果目录："+resultPath+" 失败，请检查!");
			}
		}


		try {
			//遍历每一个数据集中的所有项目
			for(String dataset:datasetName.split(",")) {
				System.out.println("开始运行项目：\t"+dataset+"\t"+new Date().toString());


				//得到该数据集中的所有项目
				currenctDataSet = MyTools.getProjects(dataset);
				String resultFile = resultPath + datasetName+".csv";
				BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(resultFile));
				//写入表头
				bufferedWriter.write("projectName,outerIterate,innerIterate,numSelected,baseClassifier,featureSelectionMethod,weightedAUC\r\n");

				//当前数据集中需要选择的特征个数
				int numSelect=Integer.parseInt(MyTools.getBaseInfo(dataset+"SelectNum"));

				for (File file : currenctDataSet) {
					//外层10次随机循环
					for (int outerIterate = 1; outerIterate <= 10; outerIterate++) {
						Instances instances = ConverterUtils.DataSource.read(file.toString());
						//先进行外层的随机化,为了避免内层随机化与外层的一样，内层的随机因子将在外层的随机因子上加1
						instances.setClassIndex(instances.numAttributes()-1);
						instances.randomize(new Random(outerIterate));
						if (instances.classAttribute().isNominal()) {
							instances.stratify(10);
						}

						doDefectPrediciton(file.getName(),instances, outerIterate, numSelect, bufferedWriter);
						bufferedWriter.flush();
					}
				}
				bufferedWriter.flush();
				bufferedWriter.close();

				System.out.println("结束运行项目：\t"+dataset+"\t"+new Date().toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

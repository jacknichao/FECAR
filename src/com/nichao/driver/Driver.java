package com.nichao.driver;

import com.nichao.core.*;
import com.nichao.util.MyTools;
import org.omg.PortableServer.THREAD_POLICY_ID;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 使用多线程的方式运行交叉检验任务
 */
class CrossValidation implements Runnable {

	private String fileName = null;
	private int outerIterate = 0;
	private int innerIterate = 0;
	private Instances instances = null;
	private int targetNumSelect = 0;
	private BaseClassiferEnum baseClassiferEnum = null;
	private FeatureSelectMethodEnum fsEnum = null;
	private BufferedWriter bufferedWriter = null;


	public CrossValidation(String fileName, int outerIterate, int innerIterate, Instances instances, int targetNumSelect, BaseClassiferEnum baseClassiferEnum, FeatureSelectMethodEnum fsEnum, BufferedWriter bufferedWriter) {

		this.fileName = fileName;
		this.outerIterate = outerIterate;
		this.innerIterate = innerIterate;
		this.instances = instances;
		this.targetNumSelect = targetNumSelect;
		this.baseClassiferEnum = baseClassiferEnum;
		this.fsEnum = fsEnum;
		this.bufferedWriter = bufferedWriter;
	}

	/**
	 * 同步每一个写出结果的线程
	 *
	 * @param result
	 */
	synchronized public void writeResults(Result result) {
		String resultStr = fileName + "," + outerIterate + "," + innerIterate + "," + result.getRealSelectedNum() + ","
				+ baseClassiferEnum + "," + fsEnum + "," + result.getEvaluation().weightedAreaUnderROC() + "\r\n";
		try {
			bufferedWriter.write(resultStr);
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		Result result = null;
		Evaluation evaluations = null;
		int realSelectedNum = 0;
		try {
			instances.setClassIndex(instances.numAttributes() - 1);

			//计算前70%的实例的索引
			int index = (int) Math.round(instances.numInstances() * 0.7);
			Instances train = new Instances(instances, 0, index);
			Instances test = new Instances(instances, index, instances.numInstances() - index);


			//除了不使用特征选择技术的，都需要进行特征过滤处理
			if (fsEnum != FeatureSelectMethodEnum.FULL) {
				int[] savedFeature = new FeatureSelection().doFeatureSelection(train, fsEnum, targetNumSelect);
				//获得实际选择出来的特征个数
				realSelectedNum = savedFeature.length;

				Remove remove = new Remove();
				//相反选择设置，表示输入的属性索引都是需要保存的
				remove.setInvertSelection(true);
				remove.setAttributeIndicesArray(savedFeature);
				remove.setInputFormat(test);
				//Filter默认会产生新的Instances因此需要将train和test指向新产生的Instances
				test = Filter.useFilter(test, remove);

				//删除训练集中的属性
				train = Filter.useFilter(train, remove);
			} else {
				realSelectedNum = train.numAttributes() - 1;
			}

			//ClassifierValidation类的内部同时完成预测性能的计算
			evaluations = ClassifierValidation.classify(train, test, baseClassiferEnum, null);

			result = new Result(realSelectedNum, evaluations);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//最终将结果写入到文件中
			writeResults(result);
			System.err.println("完成作业:\t"+this);
		}
	}
}

public class Driver {

	/**
	 *外循环的次数
	 */
	private static int outerIterate_ = 10;

	/**
	 * 内循环的次数
	 */
	private static int innerIterate_ = 10;


	/**
	 * 提交作业的时间间隔，如每一秒提交一次
	 */
	private static int interval_=1;


	/**
	 * 完成内层十次循环
	 *
	 * @param fileName        文件名称
	 * @param outerInstances  被外层随机化后的项目实例
	 * @param outerIterate    外层循环的编号
	 * @param targetNumSelect 需要选择的特征个数
	 * @param writer          保存结果的文件句柄
	 * @param pool            线程池
	 */
	public static void doDefectPrediciton(String fileName, Instances outerInstances, int outerIterate, int targetNumSelect, BufferedWriter writer, ExecutorService pool) {

		try {
			//这里进行内层的10次循环,某些方法得到的结果一致，但是有些方法每一次运行得到的结果都是不一样的
			for (int innerIterate = 1; innerIterate <= innerIterate_; innerIterate++) {

				//新建一份内层循环的实例集合
				Instances innerInstances = new Instances(outerInstances);

				innerInstances.setClassIndex(innerInstances.numAttributes() - 1);

				//make the same running environment with MOFES
				//innerInstances.randomize(new Random(outerIterate + innerIterate));

				//遍历每一个特征选择方法
				for (FeatureSelectMethodEnum fsEnum : FeatureSelectMethodEnum.values()) {
					//遍历每一个集分类
					for (BaseClassiferEnum baseClassiferEnum : BaseClassiferEnum.values()) {

						//对于每一种组合情况都用一个线程去运行
						CrossValidation crossValidation = new CrossValidation(fileName, outerIterate, innerIterate,
								new Instances(innerInstances),
								targetNumSelect, baseClassiferEnum, fsEnum, writer);

						//提交作业
						pool.submit(crossValidation);
						System.out.println("提交作业:\t"+crossValidation+"\t" + fileName + "\t" + outerIterate + "\t" + innerIterate + "\t" + fsEnum + "\t" + baseClassiferEnum);

						if(interval_>=1) {
							Thread.sleep(interval_);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		ArrayList<File> currenctDataSet = null;

		String datasetName = MyTools.getBaseInfo("datasetName");
		String resultPath = MyTools.getBaseInfo("resultPath");
		int numThread= Integer.parseInt(MyTools.getBaseInfo("numThread"));

		outerIterate_ = Integer.parseInt(MyTools.getBaseInfo("outerIterate"));
		innerIterate_ = Integer.parseInt(MyTools.getBaseInfo("innerIterate"));
		interval_ = Integer.parseInt(MyTools.getBaseInfo("interval"));


		if (!Files.exists(Paths.get(resultPath))) {
			try {
				Files.createDirectories(Paths.get(resultPath));
			} catch (IOException e) {
				System.out.println("创建结果目录：" + resultPath + " 失败，请检查!");
			}
		}


		try {
			//遍历每一个数据集中的所有项目
			String[] strs = datasetName.split(",");
			for (String dataset : strs) {
				System.out.println("开始运行项目：\t" + dataset + "\t" + new Date().toString());

				//创建一个线程池
				ExecutorService pool = Executors.newFixedThreadPool(numThread);
				System.out.println("创建线程池：\t" + numThread);

				//得到该数据集中的所有项目
				currenctDataSet = MyTools.getProjects(dataset);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				String resultFile = resultPath + datasetName + sdf.format(new Date()) + ".csv";
				BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(resultFile));
				//写入表头
				bufferedWriter.write("projectName,outerIterate,innerIterate,numSelected,baseClassifier,featureSelectionMethod,weightedAUC\r\n");

				//当前数据集中需要选择的特征个数
				int targetNumSelect = Integer.parseInt(MyTools.getBaseInfo(dataset + "SelectNum"));


				for (File file : currenctDataSet) {
					Instances originalInstances = ConverterUtils.DataSource.read(file.toString());

					//外层10次随机循环
					for (int outerIterate = 1; outerIterate <= outerIterate_; outerIterate++) {
						//新建一个外层循环的实例集合
						Instances outerInstances = new Instances(originalInstances);
						//先进行外层的随机化,为了避免内层随机化与外层的一样，内层的随机因子将在外层的随机因子上加1
						outerInstances.setClassIndex(outerInstances.numAttributes() - 1);
						outerInstances.randomize(new Random(outerIterate));
						if (outerInstances.classAttribute().isNominal()) {
							outerInstances.stratify(10);
						}

						doDefectPrediciton(file.getName(), outerInstances, outerIterate, targetNumSelect, bufferedWriter, pool);
					}
				}
				System.out.println("提交了所有任务：\t" + dataset + "\t" + new Date().toString());

				while (!pool.isTerminated()){
					Thread.sleep(1000);
				};

				bufferedWriter.flush();
				bufferedWriter.close();
				pool.shutdown();

				System.out.println("结束运行项目：\t" + dataset + "\t" + new Date().toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


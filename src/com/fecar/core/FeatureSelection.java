package com.fecar.core;

import com.fecar.util.FeatureClass;
import com.fecar.util.FeatureFeature;
import weka.attributeSelection.*;
import weka.core.Instances;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import weka.attributeSelection.ChiSquaredAttributeEval;

public class FeatureSelection {

	/**
	 * @param
	 */
	/*
	 * **已测试，正确。
	 */
	private static int[] sort(double[] similarity, int num) {
		double sim[] = new double[num];

		for (int i = 0; i < num; ++i)
			sim[i] = similarity[i];

		int[] order = new int[num];
		for (int i = 0; i < num; ++i) order[i] = i;

		for (int i = 0; i < num; ++i) {
			int index = i;
			double max = sim[i];
			for (int j = i; j < num; ++j)
				if (sim[j] > max) {
					max = sim[j];
					index = j;
				}

			sim[index] = sim[i];
			sim[i] = max;

			int temindex = order[i];
			order[i] = order[index];
			order[index] = temindex;

		}
		return order;
	}

	/**
	 * 判断一个数i是否在数组arr中出现过
	 * 若出现过则返回true
	 * 否则返回false
	 */
	private static boolean existInArr(int i, int[] arr) {
		for (int j = 0; j < arr.length; ++j)
			if (arr[j] == i) return true;
		return false;
	}

	/**
	 * 返回需要删掉的属性的序号，这些序号按从大到小排序（方便后面的删除操作）。
	 * 第一个参数是原始的属性数量(不包括类别属性),也就是0~n-2.第二个参数是要保留的属性的序号。
	 */
	private static int[] DelAttrNum(int numAttr, int[] attrRetain) {
		int dele[] = new int[numAttr - attrRetain.length];

		int j = 0;
		for (int i = 0; i < numAttr; ++i) {
			if (!existInArr(i, attrRetain)) {
				dele[j] = i;
				j++;
			}
		}

		for (int i = 0; i < dele.length; ++i) {
			int max = dele[i];
			int maxindex = i;
			for (int k = i; k < dele.length; ++k) {
				if (dele[k] > max) {
					max = dele[k];
					maxindex = k;
				}
			}
			int tem = dele[i];
			dele[i] = max;
			dele[maxindex] = tem;
		}
		return dele;
	}

	/*
	 * 根据属性和类标的SU相关值来选择属性，其中 num是要保留的属性的个数。**该方法已测试，正确
	 */
	private static void RankSUFeature(Instances train, Instances test, int num) {
		try {
			//若要保留的属性数量多于等于原属性的数量，则直接返回。
			if (num >= train.numAttributes() - 1) return;

			double SU[] = new FeatureClass().getFTSu(train);

			//sort存放属性的序号，按信息增益从大到小排序。
			int sort[] = sort(SU, train.numAttributes() - 1);

			//用于存放该删除的属性数量和该删除的属性序号。
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//将del中的元素由大到小排序，方便后面的删除操作
			for (int i = 0; i < delnum; ++i) {
				int tem = del[i];

				int max = tem;
				int index = i;
				for (int j = i; j < delnum; ++j)
					if (del[j] > max) {
						max = del[j];
						index = j;
					}
				del[i] = max;
				del[index] = tem;
			}

			Remove(train, test, del);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * 根据属性和类标的信息增益相关值来选择属性，其中 num是要保留的属性的个数。**该方法已测试，正确
	 */
	private static void RankIGFeature(Instances train, Instances test, int num) {
		try {
			//若要保留的属性数量多于等于原属性的数量，则直接返回。
			if (num >= train.numAttributes() - 1) return;

			double IG[] = new FeatureClass().getFTIG(train);

			//sort存放属性的序号，按信息增益从大到小排序。
			int sort[] = sort(IG, train.numAttributes() - 1);

			//用于存放该删除的属性数量和该删除的属性序号。
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//将del中的元素由大到小排序，方便后面的删除操作
			for (int i = 0; i < delnum; ++i) {
				int tem = del[i];

				int max = tem;
				int index = i;
				for (int j = i; j < delnum; ++j)
					if (del[j] > max) {
						max = del[j];
						index = j;
					}
				del[i] = max;
				del[index] = tem;
			}

			Remove(train, test, del);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * 根据属性和类标的chisquare相关值来选择属性，其中 num是要保留的属性的个数。**该方法已测试，正确
	 */
	private static void RankCSFeature(Instances train, Instances test, int num) {
		try {
			//若要保留的属性数量多于等于原属性的数量，则直接返回。
			if (num >= train.numAttributes() - 1) return;

			double CS[] = new FeatureClass().getFTChisquare(train);

			//sort存放属性的序号，按信息增益从大到小排序。
			int sort[] = sort(CS, train.numAttributes() - 1);

			//用于存放该删除的属性数量和该删除的属性序号。
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//将del中的元素由大到小排序，方便后面的删除操作
			for (int i = 0; i < delnum; ++i) {
				int tem = del[i];

				int max = tem;
				int index = i;
				for (int j = i; j < delnum; ++j)
					if (del[j] > max) {
						max = del[j];
						index = j;
					}
				del[i] = max;
				del[index] = tem;
			}

			Remove(train, test, del);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * 根据属性和类标的releifF相关值来选择属性，其中 num是要保留的属性的个数。**该方法已测试，正确
	 */
	private static void RankRFFeature(Instances train, Instances test, int num) {
		try {
			//若要保留的属性数量多于等于原属性的数量，则直接返回。
			if (num >= train.numAttributes() - 1) return;

			double RF[] = new FeatureClass().getFTRelief(train);

			//sort存放属性的序号，按信息增益从大到小排序。
			int sort[] = sort(RF, train.numAttributes() - 1);

			//用于存放该删除的属性数量和该删除的属性序号。
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//将del中的元素由大到小排序，方便后面的删除操作
			for (int i = 0; i < delnum; ++i) {
				int tem = del[i];

				int max = tem;
				int index = i;
				for (int j = i; j < delnum; ++j)
					if (del[j] > max) {
						max = del[j];
						index = j;
					}
				del[i] = max;
				del[index] = tem;
			}

			Remove(train, test, del);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * 采用搜索的方式来选择属性
	 * 计算训练集Ser_train中各个属性的相关性，然后通过聚类的方法来删掉一些属性。同时删掉测试集Ser_test中的相应属性。
	  */
	private static void CFS_FeatueSection(Instances train, Instances test) {
		try {
			train.setClassIndex(train.numAttributes() - 1);
			CfsSubsetEval cfs = new CfsSubsetEval();
			cfs.buildEvaluator(train);
			BestFirst bFirst = new BestFirst();
			String[] options = {"-D", "1", "-N", "5"};//前向搜索
			bFirst.setOptions(options);
			int[] SerAttr = bFirst.search(cfs, train);
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);

			Remove(train, test, SerDelAttr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	/*
	 * FCBF属性选择
	 */
	private static void FCBF_FeatureSelection(Instances train, Instances test) {
		try {
			train.setClassIndex(train.numAttributes() - 1);
			SymmetricalUncertAttributeSetEval su = new SymmetricalUncertAttributeSetEval();
			su.buildEvaluator(train);
			FCBFSearch fcbfSearch = new FCBFSearch();
			int num = (int) (Math.sqrt(train.numAttributes() - 1) * Math.log10(train.numAttributes() - 1));
			String[] options = {"-D", "false", "-T", "-1.7976931348623157E308", "-N", Integer.toString(num)};
			fcbfSearch.setOptions(options);
			int[] SerAttr = fcbfSearch.search(su, train);
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);


			Remove(train, test, SerDelAttr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * consistency属性选择
	 */
	private static void Consistency_FeatureSelection(Instances train, Instances test) {
		try {
			train.setClassIndex(train.numAttributes() - 1);
			ConsistencySubsetEval consis = new ConsistencySubsetEval();
			consis.buildEvaluator(train);
			BestFirst bFirst = new BestFirst();
			String[] options = {"-D", "1", "-N", "5"};//前向搜索
			bFirst.setOptions(options);
			int[] SerAttr = bFirst.search(consis, train);
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);

			Remove(train, test, SerDelAttr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	//使用信息增益来处理相关性完成关于聚类的属性选择。
	private static void Cluster_IG_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTIG(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//使用chisquare来处理相关性完成关于聚类的属性选择。
	private static void Cluster_ChiSquare_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTChisquare(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//使用信息增益来处理相关性完成关于聚类的属性选择。
	private static void Cluster_ReliefF_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTRelief(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//使用chisquare来处理相关性完成关于聚类的属性选择。
	private static void Cluster_SU_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTSu(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//使用我们自己实现的方法做属性选择。 前两个参数分别是训练集和测试集，后两个参数法分别是要聚的簇的数量和要选择的属性数量,
	//最后两个参数是根据训练集算出来的属性的两两相关性矩阵和与类别相关性矩阵。
	private static void Kmedoid_attrsel(Instances train, Instances test, int numclusters, int Numselect, double[][] similarity, double[] trelev) {
		try {

			Clusters clusters = new Clusters(similarity, trelev, numclusters, train.numAttributes() - 1);
			clusters.clustrUnitlConvergence();
			clusters.rankClusters();//完成聚类

			ArrayList<Integer> selectedAttr = clusters.selectAttributes(Numselect);
			int[] SerAttr = new int[selectedAttr.size()];
			for (int i = 0; i < selectedAttr.size(); ++i)
				SerAttr[i] = selectedAttr.get(i);
			//找出要保留的属性
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);
			//找出要删掉的属性
			Remove(train, test, SerDelAttr);//完成删除属性的操作。

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 将train和test中SerDelAttr[]中存储的属性去掉。
	private static void Remove(Instances train, Instances test, int SerDelAttr[]) {
		for (int i = 0; i < SerDelAttr.length; ++i) {
			train.deleteAttributeAt(SerDelAttr[i]);
			test.deleteAttributeAt(SerDelAttr[i]);
		}
	}


	/**
	 * 调用指定的特征选择方法来完成特征选择任务
	 * @param train 训练集
	 * @param test  测试集
	 * @param featureSelectMethodEnum   特征选择方法的枚举
	 * @param numSelect 需要选择的特征数量
	 * @param numClusters   当使用聚类技术完整特征选择时，指定需要聚簇的簇个数
	 */
	public static void doFeatureSelection(Instances train, Instances test, FeatureSelectMethodEnum featureSelectMethodEnum,int numSelect, int numClusters ){
		switch (featureSelectMethodEnum){
			case RankCSFeature:
				RankCSFeature(train,test,numSelect);
				break;
			case RankIGFeature:
				RankIGFeature(train,test,numSelect);
				break;
			case RankRFFeature:
				RankRFFeature(train,test,numSelect);
				break;
			case RankSUFeature:
				RankSUFeature(train,test,numSelect);
			case CFS_FeatueSection:
				CFS_FeatueSection(train,test);
				break;
			case FCBF_FeatureSelection:
				FCBF_FeatureSelection(train,test);
				break;
			case Cluster_IG_FeatureSelection:
				Cluster_IG_FeatureSelection(train,test,numClusters,numSelect);
				break;
			case Cluster_SU_FeatureSelection:
				Cluster_SU_FeatureSelection(train,test,numClusters,numSelect);
				break;
			case Cluster_ReliefF_FeatureSelection:
				Cluster_ReliefF_FeatureSelection(train,test,numClusters,numSelect);
				break;
			case Cluster_ChiSquare_FeatureSelection:
				Cluster_ChiSquare_FeatureSelection(train,test,numClusters,numSelect);
				break;
			case Consistency_FeatureSelection:
				Consistency_FeatureSelection(train,test);
				break;
				default:
					throw new RuntimeException("未知的特征选择技术:"+featureSelectMethodEnum);
		}

	}


}

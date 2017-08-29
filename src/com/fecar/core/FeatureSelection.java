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
	 * **�Ѳ��ԣ���ȷ��
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
	 * �ж�һ����i�Ƿ�������arr�г��ֹ�
	 * �����ֹ��򷵻�true
	 * ���򷵻�false
	 */
	private static boolean existInArr(int i, int[] arr) {
		for (int j = 0; j < arr.length; ++j)
			if (arr[j] == i) return true;
		return false;
	}

	/**
	 * ������Ҫɾ�������Ե���ţ���Щ��Ű��Ӵ�С���򣨷�������ɾ����������
	 * ��һ��������ԭʼ����������(�������������),Ҳ����0~n-2.�ڶ���������Ҫ���������Ե���š�
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
	 * �������Ժ�����SU���ֵ��ѡ�����ԣ����� num��Ҫ���������Եĸ�����**�÷����Ѳ��ԣ���ȷ
	 */
	private static void RankSUFeature(Instances train, Instances test, int num) {
		try {
			//��Ҫ�����������������ڵ���ԭ���Ե���������ֱ�ӷ��ء�
			if (num >= train.numAttributes() - 1) return;

			double SU[] = new FeatureClass().getFTSu(train);

			//sort������Ե���ţ�����Ϣ����Ӵ�С����
			int sort[] = sort(SU, train.numAttributes() - 1);

			//���ڴ�Ÿ�ɾ�������������͸�ɾ����������š�
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//��del�е�Ԫ���ɴ�С���򣬷�������ɾ������
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
	 * �������Ժ�������Ϣ�������ֵ��ѡ�����ԣ����� num��Ҫ���������Եĸ�����**�÷����Ѳ��ԣ���ȷ
	 */
	private static void RankIGFeature(Instances train, Instances test, int num) {
		try {
			//��Ҫ�����������������ڵ���ԭ���Ե���������ֱ�ӷ��ء�
			if (num >= train.numAttributes() - 1) return;

			double IG[] = new FeatureClass().getFTIG(train);

			//sort������Ե���ţ�����Ϣ����Ӵ�С����
			int sort[] = sort(IG, train.numAttributes() - 1);

			//���ڴ�Ÿ�ɾ�������������͸�ɾ����������š�
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//��del�е�Ԫ���ɴ�С���򣬷�������ɾ������
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
	 * �������Ժ�����chisquare���ֵ��ѡ�����ԣ����� num��Ҫ���������Եĸ�����**�÷����Ѳ��ԣ���ȷ
	 */
	private static void RankCSFeature(Instances train, Instances test, int num) {
		try {
			//��Ҫ�����������������ڵ���ԭ���Ե���������ֱ�ӷ��ء�
			if (num >= train.numAttributes() - 1) return;

			double CS[] = new FeatureClass().getFTChisquare(train);

			//sort������Ե���ţ�����Ϣ����Ӵ�С����
			int sort[] = sort(CS, train.numAttributes() - 1);

			//���ڴ�Ÿ�ɾ�������������͸�ɾ����������š�
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//��del�е�Ԫ���ɴ�С���򣬷�������ɾ������
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
	 * �������Ժ�����releifF���ֵ��ѡ�����ԣ����� num��Ҫ���������Եĸ�����**�÷����Ѳ��ԣ���ȷ
	 */
	private static void RankRFFeature(Instances train, Instances test, int num) {
		try {
			//��Ҫ�����������������ڵ���ԭ���Ե���������ֱ�ӷ��ء�
			if (num >= train.numAttributes() - 1) return;

			double RF[] = new FeatureClass().getFTRelief(train);

			//sort������Ե���ţ�����Ϣ����Ӵ�С����
			int sort[] = sort(RF, train.numAttributes() - 1);

			//���ڴ�Ÿ�ɾ�������������͸�ɾ����������š�
			int delnum = 0;
			int[] del = new int[train.numAttributes() - 1 - num];
			for (int i = train.numAttributes() - 2; i >= num; --i) {
				del[delnum] = sort[i];
				delnum++;
			}


			//��del�е�Ԫ���ɴ�С���򣬷�������ɾ������
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
	 * ���������ķ�ʽ��ѡ������
	 * ����ѵ����Ser_train�и������Ե�����ԣ�Ȼ��ͨ������ķ�����ɾ��һЩ���ԡ�ͬʱɾ�����Լ�Ser_test�е���Ӧ���ԡ�
	  */
	private static void CFS_FeatueSection(Instances train, Instances test) {
		try {
			train.setClassIndex(train.numAttributes() - 1);
			CfsSubsetEval cfs = new CfsSubsetEval();
			cfs.buildEvaluator(train);
			BestFirst bFirst = new BestFirst();
			String[] options = {"-D", "1", "-N", "5"};//ǰ������
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
	 * FCBF����ѡ��
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
	 * consistency����ѡ��
	 */
	private static void Consistency_FeatureSelection(Instances train, Instances test) {
		try {
			train.setClassIndex(train.numAttributes() - 1);
			ConsistencySubsetEval consis = new ConsistencySubsetEval();
			consis.buildEvaluator(train);
			BestFirst bFirst = new BestFirst();
			String[] options = {"-D", "1", "-N", "5"};//ǰ������
			bFirst.setOptions(options);
			int[] SerAttr = bFirst.search(consis, train);
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);

			Remove(train, test, SerDelAttr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	//ʹ����Ϣ�����������������ɹ��ھ��������ѡ��
	private static void Cluster_IG_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTIG(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//ʹ��chisquare�������������ɹ��ھ��������ѡ��
	private static void Cluster_ChiSquare_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTChisquare(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//ʹ����Ϣ�����������������ɹ��ھ��������ѡ��
	private static void Cluster_ReliefF_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTRelief(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//ʹ��chisquare�������������ɹ��ھ��������ѡ��
	private static void Cluster_SU_FeatureSelection(Instances train, Instances test, int numclusters, int Numselect) {
		train.setClassIndex(train.numAttributes() - 1);
		double[][] similarity = new FeatureFeature().getFfSu(train);
		double[] trelev = new FeatureClass().getFTSu(train);

		Kmedoid_attrsel(train, test, numclusters, Numselect, similarity, trelev);
	}

	//ʹ�������Լ�ʵ�ֵķ���������ѡ�� ǰ���������ֱ���ѵ�����Ͳ��Լ����������������ֱ���Ҫ�۵Ĵص�������Ҫѡ�����������,
	//������������Ǹ���ѵ��������������Ե���������Ծ�������������Ծ���
	private static void Kmedoid_attrsel(Instances train, Instances test, int numclusters, int Numselect, double[][] similarity, double[] trelev) {
		try {

			Clusters clusters = new Clusters(similarity, trelev, numclusters, train.numAttributes() - 1);
			clusters.clustrUnitlConvergence();
			clusters.rankClusters();//��ɾ���

			ArrayList<Integer> selectedAttr = clusters.selectAttributes(Numselect);
			int[] SerAttr = new int[selectedAttr.size()];
			for (int i = 0; i < selectedAttr.size(); ++i)
				SerAttr[i] = selectedAttr.get(i);
			//�ҳ�Ҫ����������
			int SerDelAttr[] = new FeatureSelection().DelAttrNum(train.numAttributes() - 1, SerAttr);
			//�ҳ�Ҫɾ��������
			Remove(train, test, SerDelAttr);//���ɾ�����ԵĲ�����

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// ��train��test��SerDelAttr[]�д洢������ȥ����
	private static void Remove(Instances train, Instances test, int SerDelAttr[]) {
		for (int i = 0; i < SerDelAttr.length; ++i) {
			train.deleteAttributeAt(SerDelAttr[i]);
			test.deleteAttributeAt(SerDelAttr[i]);
		}
	}


	/**
	 * ����ָ��������ѡ�񷽷����������ѡ������
	 * @param train ѵ����
	 * @param test  ���Լ�
	 * @param featureSelectMethodEnum   ����ѡ�񷽷���ö��
	 * @param numSelect ��Ҫѡ�����������
	 * @param numClusters   ��ʹ�þ��༼����������ѡ��ʱ��ָ����Ҫ�۴صĴظ���
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
					throw new RuntimeException("δ֪������ѡ����:"+featureSelectMethodEnum);
		}

	}


}

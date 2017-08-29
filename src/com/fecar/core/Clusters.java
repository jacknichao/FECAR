package com.fecar.core;

import com.fecar.util.FeatureClass;
import com.fecar.util.FeatureFeature;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Clusters {

	/**
	 * @param args
	 */
	
	public double[][]similarities;//���ϵ������
	public double[]trelevance;//�ͽ���������ϵ��
	public int numClusters;//�ص�����
	public int numAttribute;//ԭʼ���Ե�����
	public ArrayList<ArrayList<Integer>> clusters;//�����洢�س�Ա
	public ArrayList<Integer> Medoids;//�����洢K���ص�����
	
	
	//���캯��
	public Clusters(double [][]simis,double[]Trele, int k, int numAttr)
	{
				
	    //���ôص�������ԭʼ���Ե�����
		numClusters=k;
		numAttribute=numAttr;
	
		
		//�����¿ռ�
		similarities=new double[numAttr][numAttr];
		trelevance=new double[numAttr];
		clusters=new ArrayList<ArrayList<Integer>>();
		Medoids=new ArrayList<Integer>();
		
		//����similarity����
		for(int i=0;i<numAttr;++i)
			for(int j=0;j<numAttr;++j)
				similarities[i][j]=simis[i][j];
		
		//��ʼ��trelevance����
		for(int i=0;i<numAttr;++i)
			trelevance[i]=Trele[i];
		
		//����K������
		for(int i=0;i<numClusters;++i)
		{
			 ArrayList<Integer> cluster=new ArrayList<Integer>();
			 clusters.add(cluster);
		}
		
			
		//��ʼ������
		initiateMedoids();
	}
	
	//ѡ���������������numcluster��������Ϊ����
	public void initiateMedoids()
	{
		boolean visited[]=new boolean[numAttribute];
		for(int i=0;i<numAttribute;++i) visited[i]=false;
		
		//�ҳ�K������
		for(int initiated=0;initiated<numClusters;)
		{
			//�ҳ���ǰδ���ʹ��������У���������������Ǹ�����
			double max=-1;
			int index=0;
			
		   for(int i=0;i<numAttribute;++i)
			if(!visited[i]&&trelevance[i]>max)
			{
				max=trelevance[i];
				index=i;
			}
			
		
			//��ʾindex�����ѷ��ʹ�
			visited[index]=true;
			
			
			Medoids.add(index);
			initiated++;
			
		}
		
	}	
	
	//�ҳ���clusterID���ص����ģ������������������������������
	public int findMedoid(int clusterID)
	{
		double  max=0;
		int Medoid=0;
		for(int i=0;i<clusters.get(clusterID).size();++i)
		{
			double sum=0;
			
			for(int j=0;j<clusters.get(clusterID).size();++j)
				sum=sum+similarities[clusters.get(clusterID).get(i)][clusters.get(clusterID).get(j)];
			if(sum>max)
			{
				max=sum;
				Medoid=clusters.get(clusterID).get(i);
			}
		}
		return Medoid;
	}
	
	//���¸����ص��ʵ�
	public void updateMedoid()
	{
		
		for(int i=0;i<numClusters;++i)
		{
			int medoid=findMedoid(i);
			Medoids.set(i, medoid);
		}
	}
	
	//���ص�attrID�����������صĴغš�ĳ����������ĳ���ص��ҽ�����������ô��ʵ������Դ����������ص��ʵ������ԡ�
	public int nearestCluster(int attrID)
	{
		int clusterId=0;
		double max=-1;
		for(int i=0;i<numClusters;++i)
			if(similarities[Medoids.get(i)][attrID]>max)
			{
				max=similarities[Medoids.get(i)][attrID];
				clusterId=i;
			}
		return clusterId;
	}
	
	//���ôصĴس�Աȫ��ɾ�� ����������������
	public void clearClusters()
	{
		for(int i=0;i<numClusters;++i)
			  clusters.get(i).clear();
	}
	
	
	//����attrID�����Լ��뵽��clusterID������
	public void addElementToCluster(int clusterID, int attrID)
	{
		clusters.get(clusterID).add(attrID);
	}
	
	//����������ָ�ɵ������������
	public void cluster()
	{
		for(int i=0;i<numAttribute;++i)
		{
			int m=nearestCluster(i);
			addElementToCluster(m, i);			
		}
	}
	
	//���ƣ���arraylist1���Ƶ�arraylist2��
	public void copyArraylist(ArrayList<Integer> arrayList1, ArrayList<Integer> arrayList2)
	{
		for(int i=0;i<arrayList1.size();++i)
			arrayList2.set(i, arrayList1.get(i));
	}
			
	public void clustrUnitlConvergence()
	{
		ArrayList<Integer> CopyOfMedoids=new ArrayList<Integer>();
		for(int i=0;i<numClusters;++i)
			CopyOfMedoids.add(-1);
		
		while(!Medoids.equals(CopyOfMedoids))
		{
			clearClusters();
			cluster();			
			copyArraylist(Medoids, CopyOfMedoids);
			updateMedoid();
			
		}
	}
		
	//����clusterID���صĴس�Ա������������������ɴ�С�������򡣼���һ�����ڣ���0��λ�ÿ�ʼ��������������������𽥼�С��
		public void rankInnerCluster(int clusterID)
		{
			ArrayList<Integer> cluster=clusters.get(clusterID);
			for(int i=0;i<cluster.size();++i)
			{
				double max=trelevance[cluster.get(i)];
				int index=i;
				for(int j=i;j<cluster.size();++j)
					if(trelevance[cluster.get(j)]>max)
					{
						max=trelevance[cluster.get(j)];
						index=j;
					}
				int tem=cluster.get(i);
				cluster.set(i, cluster.get(index));
				cluster.set(index, tem);
			}
		}
		
	//�����е�clusterִ�������rankInnerCluster������
	public void rankClusters()
	{
		for(int i=0;i<numClusters;++i)
			rankInnerCluster(i);
	}
	
	
	//����ǰ�ص���������
	public void outputCluster()
	{
		for(int i=0;i<numClusters;++i)
		{
			System.out.print(clusters.get(i).size());
			System.out.print("--");
			System.out.print(Medoids.get(i));
			System.out.print(": ");
			for(int j=0;j<clusters.get(i).size();++j)
			{
				System.out.print(clusters.get(i).get(j));
				System.out.print(",");
			}
			System.out.println();
		}
		System.out.println("***************************");
	}
	
	//�������
	public void outputSimilarityMatrix()
	{
		for(int i=0;i<numAttribute;++i)
		{
		   for(int j=0;j<numAttribute;++j)
			{
				System.out.print(similarities[i][j]);
				System.out.print(",");
			}
			System.out.println();
		}
	}
	
	//���trelevance
	public void outputTrelevance()
	{
		for(int i=0;i<numAttribute;++i)
			System.out.println(trelevance[i]);
	}
	
	//������ĵ�
	public void outputMedoids()
	{
		for(int i=0;i<numClusters;++i)
			
		{
			System.out.print(Medoids.get(i));
			System.out.print(" ");
		}
		System.out.println();
	}
		
	//����غ���clusterID�Ĵ��ڸ������Ե������
	public void outputInnerClusterSimilairtys(int clusterID)
	{
		System.out.println("��"+Integer.toString(clusterID)+"�����ϵ�������ǣ�");
		ArrayList<Integer> cluster=clusters.get(clusterID);
		for(int i=0;i<cluster.size();++i)
		{
			for(int j=0;j<cluster.size();++j)
			{
				System.out.print(similarities[cluster.get(i)][cluster.get(j)]);
				System.out.print(",");
			}
			System.out.println();
		}
		
	}
	//��������ص������ϵ������
	public void outputClusterSimilarity()
	{
		for(int i=0;i<numClusters;++i)
			outputInnerClusterSimilairtys(i);
	}
	
	//�����ĸ������Ǵ�ÿ����ѡ�����ԵĲ��ԣ�ʵ���У��õ��ǵ�һ��������
	//���ձ�����ÿ������ѡ������ǰx������
	public ArrayList<Integer> selectAttributes(int numSelected)
	{
		ArrayList<Integer> selectedAttributes=new ArrayList<Integer>();
		for(int i=0;i<numClusters;++i)
		{
			for(int j=0;j<Math.ceil(((double)clusters.get(i).size()/numAttribute)*numSelected);++j)
			 selectedAttributes.add(clusters.get(i).get(j));
		}
		
		return selectedAttributes;
	}
		
	
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		   	Instances instances=new Instances(new BufferedReader(new FileReader("E:/eclipse���ݼ�/post/eclipse-metrics-files-2.0.arff")));
			double [][]similarity=new FeatureFeature().getFfSu(instances);
			double []trelev=new FeatureClass().getFTSu(instances);
			Clusters clusters=new Clusters(similarity,trelev, 3, instances.numAttributes()-1);
			clusters.clustrUnitlConvergence();
			clusters.rankClusters();
			clusters.outputCluster();	
			clusters.outputClusterSimilarity();
			ArrayList<Integer> ss=clusters.selectAttributes(8);
			for(int i=0;i<ss.size();++i)
				System.out.println(ss.get(i));
		  //  clusters.selectAttributes(15);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	

	}

}

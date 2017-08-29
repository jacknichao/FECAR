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
	
	public double[][]similarities;//相关系数矩阵
	public double[]trelevance;//和结果的相关性系数
	public int numClusters;//簇的数量
	public int numAttribute;//原始属性的数量
	public ArrayList<ArrayList<Integer>> clusters;//用来存储簇成员
	public ArrayList<Integer> Medoids;//用来存储K个簇的中心
	
	
	//构造函数
	public Clusters(double [][]simis,double[]Trele, int k, int numAttr)
	{
				
	    //设置簇的数量和原始属性的数量
		numClusters=k;
		numAttribute=numAttr;
	
		
		//开辟新空间
		similarities=new double[numAttr][numAttr];
		trelevance=new double[numAttr];
		clusters=new ArrayList<ArrayList<Integer>>();
		Medoids=new ArrayList<Integer>();
		
		//复制similarity矩阵
		for(int i=0;i<numAttr;++i)
			for(int j=0;j<numAttr;++j)
				similarities[i][j]=simis[i][j];
		
		//初始化trelevance矩阵
		for(int i=0;i<numAttr;++i)
			trelevance[i]=Trele[i];
		
		//生成K个聚类
		for(int i=0;i<numClusters;++i)
		{
			 ArrayList<Integer> cluster=new ArrayList<Integer>();
			 clusters.add(cluster);
		}
		
			
		//初始化质心
		initiateMedoids();
	}
	
	//选择与结果相关性最大的numcluster个属性作为质心
	public void initiateMedoids()
	{
		boolean visited[]=new boolean[numAttribute];
		for(int i=0;i<numAttribute;++i) visited[i]=false;
		
		//找出K个质心
		for(int initiated=0;initiated<numClusters;)
		{
			//找出当前未访问过的属性中，与结果相关性最大的那个属性
			double max=-1;
			int index=0;
			
		   for(int i=0;i<numAttribute;++i)
			if(!visited[i]&&trelevance[i]>max)
			{
				max=trelevance[i];
				index=i;
			}
			
		
			//标示index属性已访问过
			visited[index]=true;
			
			
			Medoids.add(index);
			initiated++;
			
		}
		
	}	
	
	//找出第clusterID个簇的质心，即簇内与其他属性相关性最大的属性
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
	
	//更新各个簇的质点
	public void updateMedoid()
	{
		
		for(int i=0;i<numClusters;++i)
		{
			int medoid=findMedoid(i);
			Medoids.set(i, medoid);
		}
	}
	
	//返回第attrID个属性所属簇的簇号。某个属性属于某个簇当且仅当该属性离该簇质点的相关性大于离其他簇的质点的相关性。
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
	
	//将该簇的簇成员全部删掉 ，仅保留质心属性
	public void clearClusters()
	{
		for(int i=0;i<numClusters;++i)
			  clusters.get(i).clear();
	}
	
	
	//将第attrID个属性加入到第clusterID个簇中
	public void addElementToCluster(int clusterID, int attrID)
	{
		clusters.get(clusterID).add(attrID);
	}
	
	//将各个属性指派到最近的质心中
	public void cluster()
	{
		for(int i=0;i<numAttribute;++i)
		{
			int m=nearestCluster(i);
			addElementToCluster(m, i);			
		}
	}
	
	//复制，将arraylist1复制到arraylist2中
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
		
	//将第clusterID个簇的簇成员按照其与结果的相关性由大到小进行排序。即，一个簇内，从0号位置开始各个属性与结果的相关性逐渐减小。
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
		
	//对所有的cluster执行上面的rankInnerCluster操作。
	public void rankClusters()
	{
		for(int i=0;i<numClusters;++i)
			rankInnerCluster(i);
	}
	
	
	//将当前簇的情况输出来
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
	
	//输出矩阵
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
	
	//输出trelevance
	public void outputTrelevance()
	{
		for(int i=0;i<numAttribute;++i)
			System.out.println(trelevance[i]);
	}
	
	//输出中心点
	public void outputMedoids()
	{
		for(int i=0;i<numClusters;++i)
			
		{
			System.out.print(Medoids.get(i));
			System.out.print(" ");
		}
		System.out.println();
	}
		
	//输出簇号是clusterID的簇内各个属性的相关性
	public void outputInnerClusterSimilairtys(int clusterID)
	{
		System.out.println("簇"+Integer.toString(clusterID)+"的相关系数矩阵是：");
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
	//输出各个簇的相关性系数矩阵
	public void outputClusterSimilarity()
	{
		for(int i=0;i<numClusters;++i)
			outputInnerClusterSimilairtys(i);
	}
	
	//以下四个函数是从每个簇选择属性的策略，实验中，用的是第一个方法。
	//按照比例从每个簇中选择排名前x的属性
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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		   	Instances instances=new Instances(new BufferedReader(new FileReader("E:/eclipse数据集/post/eclipse-metrics-files-2.0.arff")));
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

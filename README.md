# FeatureSelectionFactory
This project implements different feature selection methods, such as (1) filter-base ranking methods; (2) Filter-based subset methods; (3) FECAR； (4) Wrapper-base methods. The details are as following:


 
## (1) Filter-based ranking methods

###	(1.1 statistic based)
		ChiSquared + Ranker

###	(1.2 probability-based)
		GainRatio + Ranker
		InfoGain + Ranker
		SymmetricalUncert + Ranker

###	(1.3 instance based)
		ReliefF + Ranker

###	(1.4 classifier based )
		SVMAttribute  + Ranker
		OneRAttribute  + Ranker

## (2) Filter-based subset methods

###	(2.1 correlation-based )
		CfsSubsetEval + GreedyStepwise

###	(2.2 consistency-based)
		ConsistencySubsetEval + GreedyStepwise
		
## (3) FECAR

###	(3.1)Cluster_IG_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
###	(3.2)Cluster_ChiSquare_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。
###	(3.3)Cluster_ReliefF_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
###	(3.4)Cluster_SU_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。

## (4) Wrapper-based

###	(4.1)
	wrapper based greedy forward selection
###	(4.2)
	wrapper based greedy backward selection



##USAGE (IDEA):
1) git clone https://github.com/jacknichao/FeatureSelectionFactory.git

2) import this project with IDEA

3) build this project

4) create an artifact and make sure that the main class is "com.nichao.driver.Driver".
   after that ,a jar will be created, such as FeatureSelectionFactory.jar

5) edit /configs/baseinfo.properties and provide the required fields

6) make a copy of /configs/baseinfo.properties

7) put baseinfo.properties and FeatureSelectionFactory.jar in same directory

8) write down this command 'java -jar FeatureSelectionFactory.jar', then press the return key, 
   
9) up here, you will see running information on the terminal screen 
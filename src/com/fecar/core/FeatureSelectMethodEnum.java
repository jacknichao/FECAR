package com.fecar.core;

public enum FeatureSelectMethodEnum {
	RankSUFeature,//根据属性和类标的SU相关值来选择属性
	RankIGFeature,//根据属性和类标的信息增益相关值来选择属性
	RankCSFeature,//根据属性和类标的chisquare相关值来选择属性
	RankRFFeature,//根据属性和类标的releifF相关值来选择属性
	CFS_FeatueSection,//采用搜索的方式来选择属性, 计算训练集中各个属性的相关性，然后通过聚类的方法来删掉一些属性
	FCBF_FeatureSelection,//FCBF属性选择
	Consistency_FeatureSelection,//consistency属性选择
	Cluster_IG_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
	Cluster_ChiSquare_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。
	Cluster_ReliefF_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
	Cluster_SU_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。

}

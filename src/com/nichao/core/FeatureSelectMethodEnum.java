package com.nichao.core;

public enum FeatureSelectMethodEnum {
	ChiSquared_Ranker,
	GainRatio_Ranker,
	InfoGain_Ranker,
	SymmetricalUncert_Ranker,
	ReliefF_Ranker,
	SVM_Ranker,
	OneR_Ranker,


	CfsSubset_GreegyStepwise,
	ConsistencySubset_GreegyStepwise,


	Cluster_IG_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
	Cluster_ChiSquare_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。
	Cluster_ReliefF_FeatureSelection,//使用信息增益来处理相关性完成关于聚类的属性选择。
	Cluster_SU_FeatureSelection,//使用chisquare来处理相关性完成关于聚类的属性选择。

	WrapperSubset_GreegyStepwiseNB,//使用NB作为基分类器
	WrapperSubset_GreegyStepwiseJ48,//使用F48作为基分类器
	WrapperSubset_GreegyStepwiseLR,//使用LR作为基分类器
	WrapperSubset_GreegyStepwiseKNN,//使用KNN作为基分类器

	WrapperSubset_GreegyStepwiseNB_Back,//使用NB作为基分类器
	WrapperSubset_GreegyStepwiseJ48_Back,//使用F48作为基分类器
	WrapperSubset_GreegyStepwiseLR_Back,//使用LR作为基分类器
	WrapperSubset_GreegyStepwiseKNN_Back,//使用KNN作为基分类器

	FULL//不用使用特征选择技术
}

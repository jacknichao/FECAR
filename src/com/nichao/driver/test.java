package com.nichao.driver;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;


public class test {

	public static void main(String[] args) throws  Exception{

		Instances train= ConverterUtils.DataSource.read("/home/jacknichao/datasets/Relink/Apache.arff");
	/*	int[] indices={2,6,10};
		Remove remove=new Remove();
//		remove.setOptions(new String[]{"-V"});
		remove.setInvertSelection(true);
		remove.setAttributeIndicesArray(indices);
		remove.setInputFormat(train);
		//Filter默认会产生新的Instances因此需要将train和test指向新产生的Instances

		System.out.println("before:"+train.numAttributes());
		train= Filter.useFilter(train,remove);
		System.out.println("after:"+train.numAttributes());

		for(int i=0;i<train.numAttributes();i++){

			System.out.println(train.attribute(i).name());
		}


		/*AttributeSelection attrsel=new AttributeSelection();
		CfsSubsetEval eval=new CfsSubsetEval();
		GreedyStepwise search=new GreedyStepwise();
		search.setSearchBackwards(true);

		attrsel.setEvaluator(eval);
		attrsel.setSearch(search);
		attrsel.SelectAttributes(train);
		int[] indices=attrsel.selectedAttributes();
		System.out.println(Utils.arrayToString(indices));


		Remove remove=new Remove();
		//相反选择设置，表示输入的属性索引都是需要保存的
		remove.setInvertSelection(true);
		remove.setAttributeIndicesArray(indices);
		remove.setInputFormat(train);
		//Filter默认会产生新的Instances因此需要将train和test指向新产生的Instances
		train= Filter.useFilter(train,remove);
*/


	//	System.out.println(Utils.arrayToString(FeatureSelection.test(train)));


	}
}

package com.nichao.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MyTools {
	private static HashMap<String,String> hashMap=null;

	static {
		if(hashMap==null) hashMap=new HashMap<>();
		Properties prop = new Properties();
		try {
			prop.load(MyTools.class.getClassLoader()
					.getResourceAsStream("baseinfo.properties"));

			for(String pName: prop.stringPropertyNames()){
				hashMap.put(pName,prop.get(pName).toString());
			}

		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据指定的属性名称来找到对应的属性值
	 * @param key 属性名
	 * @return
	 */
	public static String getBaseInfo(String key){
		if(hashMap.containsKey(key)){
			return hashMap.get(key);
		}else
			return null;

	}


	/**
	 * 根据给定的项目名称，找到改项目下的所有工程
	 * @param datasetName
	 * @return
	 */
	public static ArrayList<File> getProjects(String datasetName){
		ArrayList<File> projects=new ArrayList<>();
		String url=hashMap.get("basepath")+datasetName+"/";
		File dir=new File(url);

		for(File file: dir.listFiles()){
			if(file.isFile())
				projects.add(file);

		}

		return projects;


	}

	private static void outputAll(){
		for(Map.Entry<String,String> entry:hashMap.entrySet()){
			System.out.println(entry.getKey()+"-->"+entry.getValue());
		}

	}


	public static void main(String[] args){
		MyTools.outputAll();

		ArrayList<File> projects=MyTools.getProjects("AEEEM");
		for(File f:projects){
			System.out.println(f.toString());
		}

	}
}

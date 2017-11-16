package com.nichao.driver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TT {


	public static void main(String[] args) throws  Exception{
		BufferedWriter writer=new BufferedWriter(new FileWriter("out.txt"));
		ExecutorService pool= Executors.newFixedThreadPool(5);
		List<Future<String>> list=new ArrayList<Future<String>>();

		for(int i=1;i<=3;i++){
			EM em=new EM(5,writer);
			list.add(pool.submit(em));
		}
		pool.shutdown();

		for(Future<String> fs:list){
			while(!fs.isDone());
			System.out.println(fs.get());
		}
	}
}



class EM implements Callable<String>{
	private BufferedWriter writer;
	private int times;

	public EM(int times,BufferedWriter writer){
		this.times=times;
		this.writer=writer;
	}


	@Override
	public String call() throws Exception {
		try{
			for(int i =1;i<=times;i++){

				System.out.println(Thread.currentThread().getId()+"\t"+i);
				writer.write(Thread.currentThread().getId()+"\t"+i+"\r\n");
				writer.flush();

				Thread.sleep(1000);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return Thread.currentThread().getId()+"is ok!";
	}
}

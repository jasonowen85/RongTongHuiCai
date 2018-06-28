package com.shyx.rthc.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;
/**
 * 手机信息工具类
 * @author lian
 *
 */
public class PhoneUtil {
	
	/**
	 * 判断SD卡是否存在
	 * @return
	 */
	public static boolean existSDCard() {  
		 return Environment.getExternalStorageState().equals(  
		    Environment.MEDIA_MOUNTED); 
		  
		 }  
	/**
	 * 计算SD卡剩余可用空间
	 * @return
	 */
	public static long getSDFreeSize(){  
	     //取得SD卡文件路径  
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //获取单个数据块的大小(Byte)  
	     long blockSize = sf.getBlockSize();   
	     //空闲的数据块的数量  
	     long freeBlocks = sf.getAvailableBlocks();  
	     //返回SD卡空闲大小  
	     return freeBlocks * blockSize;  //单位Byte  
//	     //return (freeBlocks * blockSize)/1024;   //单位KB  
//	     return (freeBlocks * blockSize)/1024 /1024; //单位MB  
	   } 
	/**
	 * SD卡总容量
	 * @return
	 */
	 public static long getSDAllSize(){
	      //取得SD卡文件路径
	      File path = Environment.getExternalStorageDirectory(); 
	      StatFs sf = new StatFs(path.getPath()); 
	      //获取单个数据块的大小(Byte)
	      long blockSize = sf.getBlockSize(); 
	      //获取所有数据块数
	      long allBlocks = sf.getBlockCount();
	      //返回SD卡大小
	      //return allBlocks * blockSize; //单位Byte
	      //return (allBlocks * blockSize)/1024; //单位KB
	      return (allBlocks * blockSize)/1024/1024; //单位MB
	    }	
	 
	

}

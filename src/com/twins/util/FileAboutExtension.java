package com.twins.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author twins make for the file operation
 */

public class FileAboutExtension {
	public FileAboutExtension() {
	}
	private ArrayList<String> dirNameList = new ArrayList<String>();
	/**
	 * get file from a directory by twins
	 * reference of the language
	 * @param dir, language
	 */
	public List<File> getFileFromDir(final String dir, final String language) {
		if (!dir.equals("")) {
			File fileDir = new File(dir);
			List<File> filelist = new ArrayList<File>();
			File[] files = fileDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if(files[i].getName().endsWith(language) && files[i].isFile()) {
					filelist.add(files[i]);
				}
			}
			return filelist;
		}
		return null;
	}

	/**
	 * 通过递归获得一个文件目录下的所有文件，自己可以扩充这个方法。
	 * @param strPath
	 */
	public ArrayList<String> getDirectoryFiles(final String strPath) {
		ArrayList<String> fileNameList = new ArrayList<String>();
		try {
			File f = new File(strPath);
			if (f.isDirectory()) 
			{
				File[] fList = f.listFiles();
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isDirectory()) {
						System.out.println(fList[j].getPath());
						getDirectory(fList[j].getPath()); // 在getDir函数里面又调用了getDir函数本身
					}
				}
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isFile()) {
						fileNameList.add(fList[j].getPath());
					}
				}
			}
			return fileNameList;
		} catch (Exception e) {
			System.out.println("Error： " + e);
		}
		fileNameList.add(strPath);
		return fileNameList;
	}
	
	/**
	 * 这里获得一个根目录下的所有子目录，如此，可以通过相关的目录，进行一些操作。
	 * 递归添加目录注意的一点就是自身目录必须要添加
	 * @param strPath
	 * @return
	 */
	public ArrayList<String> getDirectory(final String strPath) {
		
		try {
			File f = new File(strPath);
			if (f.isDirectory()) 
			{
				File[] fList = f.listFiles();
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isDirectory()) {
						getDirectory(fList[j].getPath()); // 在getDir函数里面又调用了getDir函数本身
					}
				}
				dirNameList.add(strPath);
			}
			return dirNameList;
		} catch (Exception e) {
			System.out.println("Error： " + e);
		}
		return null;
	}
	
	/**
	 * 递归删除一个目录下的所有文件。
	 * @param strPath
	 * @return
	 */
	public boolean deleteDirectory(final String strPath) {
		try {
			File f = new File(strPath);
			if (f.isDirectory()) 
			{
				File[] fList = f.listFiles();
				for (int j = 0; j < fList.length; j++) {
					if (fList[j].isDirectory()) {
						deleteDirectory(fList[j].getPath()); // 在getDir函数里面又调用了getDir函数本身
					}
				}
				for (int j = 0; j < fList.length; j++) {

					if (fList[j].isFile()) {
						fList[j].delete();
					}
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("Error： " + e);
		}
		return false;
	}
	
	public static void main(final String[] args) {
		FileAboutExtension fs = new FileAboutExtension();
		ArrayList<String> name = new ArrayList<String>();
		name = fs.getDirectory("F:\\uploadfile");
	    for(String n:name) {
	    	System.out.println(n);
	    }
		//fs.getDirectory("F:\\java\\");
	}
}

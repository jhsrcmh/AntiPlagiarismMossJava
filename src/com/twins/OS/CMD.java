/**
 * 通过moss完成一个反抄袭插件
 */

package com.twins.OS;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.twins.DB.ResultDao;
import com.twins.util.FileAboutExtension;

public class CMD implements Runnable {

	private InputStream ins = null;
	private static String url;
	public CMD(InputStream instream) {
		this.ins = instream;
	}

	public CMD() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ins));
			String line = null;
			while ((line = reader.readLine()) != null) {
				
				// 判定是否有返回值，从而判断上传是否完整进行
				if(line.startsWith("http://moss.stanford.edu/results/"))
				{
					CMD.url = line;
				}
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct the check according language and directory
	 * @param userId
	 * @param language
	 * @param directory/ this directory make the 
	 * @return
	 */
	private String getUrlOfCmd(int userId, String language, String directory) {
		//./moss -u 3 -l java java/*.java
		String [] cmd = new String[] { "cmd.exe", "/C", "D:\\cygwin\\bin\\perl.exe moss -u "+userId+" -l "+ language + " " + directory+"/*."+language };
		 try {
	            Process process = Runtime.getRuntime().exec(cmd);
	            //开启两个线程进行监听
	            new Thread(new CMD(process.getInputStream())).start();
	            new Thread(new CMD(process.getErrorStream())).start();
	            process.getOutputStream().close();
	            int exitValue = process.waitFor();
	            process.destroy();
	            System.out.println("return: " + exitValue);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return CMD.url;
	}
	
	/**
	 * get the detail difference from two directory, using recursion
	 * @param userId
	 * @param language
	 * @param directoryA
	 * @param directoryB
	 * @return
	 */
	private String getTwoDirectoryDiff(int userId, String language, String directoryA, String directoryB) {
		String [] cmd = new String[] { "cmd.exe", "/C", "D:\\cygwin\\bin\\perl.exe moss -u "+userId+" -l "+ language + " " + directoryA+"/*."+language+" "+directoryB+"/*."+language };
		 try {
	            Process process = Runtime.getRuntime().exec(cmd);
	            new Thread(new CMD(process.getInputStream())).start();
	            new Thread(new CMD(process.getErrorStream())).start();
	            process.getOutputStream().close();
	            int exitValue = process.waitFor();
	            process.destroy();
	            System.out.println("return: " + exitValue);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return CMD.url;
	}
	
	/**
	 * get the difference using baseFile
	 * @param userId
	 * @param language
	 * @param baseFile
	 * @param directory
	 * @return
	 */
	private String getDiffFromBase(int userId, String language, String baseFile, String directory) {
		String [] cmd = new String[] { "cmd.exe", "/C", "D:\\cygwin\\bin\\perl.exe moss -u "+userId+" -l "+ language + " -b " +baseFile+" "+directory+"/*."+language };
		 try {
	            Process process = Runtime.getRuntime().exec(cmd);
	            new Thread(new CMD(process.getInputStream())).start();
	            new Thread(new CMD(process.getErrorStream())).start();
	            process.getOutputStream().close();
	            int exitValue = process.waitFor();
	            process.destroy();
	            System.out.println("return: " + exitValue);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return CMD.url;
	}
	/**
	 * exec a task using cmd
	 * @author twins
	 * @param task
	 * @return
	 */
	private static boolean cmd(String task) {
		String [] cmd = new String[] { "cmd.exe", "/C", task };
		 try {
	            Process process = Runtime.getRuntime().exec(cmd);
	            //开启两个线程进行监听
	            new Thread(new CMD(process.getInputStream())).start();
	            new Thread(new CMD(process.getErrorStream())).start();
	            process.getOutputStream().close();
	            process.destroy();
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return false;
	}
	
	/**
	 * if a task is filled cpu, kill it forcely!
	 * @param taskName
	 * @return
	 */
	private static boolean taskKill(String taskName) {
		String [] cmd = new String[] { "cmd.exe", "/C", "taskkill /im " + taskName +" /f" };
		 try {
	            Process process = Runtime.getRuntime().exec(cmd);
	            //开启两个线程进行监听
	            new Thread(new CMD(process.getInputStream())).start();
	            new Thread(new CMD(process.getErrorStream())).start();
	            process.getOutputStream().close();
	            process.destroy();
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return false;
	}
	
	/**
	 * 返回学生的抄袭比例。
	 * @param a 相似度
	 */
	public static void getPer(float a) {
		ResultDao rd = new ResultDao();
		System.out.println("抄袭比例为："+rd.getPercentage(a));
	}
	
	/**
	 * main functions
	 * @param args
	 */
	public static void main(String args[]) {
		CMD cmd = new CMD();
		FileAboutExtension fae = new FileAboutExtension();
		ArrayList<String> fileList = fae.getDirectory("F:\\uploadfile");
		for(String dir : fileList) {
			List<File> files = fae.getFileFromDir(dir, "java");
			if(!files.isEmpty()) {
				System.out.println("directory name is "+ dir);
				String urlhere = cmd.getUrlOfCmd(3, "java", dir);
				JSoup.getConnection(urlhere);
			}
		}	
		// kill perl.exe if exist
		if(taskKill("perl.exe")) {
			System.out.println("success and kill perl.exe");
		}
		getPer(10);
	}
}

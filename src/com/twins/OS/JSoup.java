package com.twins.OS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.twins.DB.ResultDao;
import com.twins.entity.Result;

/**
 * http://jsoup.org/cookbook/input/load-document-from-url
 * @author Administrator
 *
 */
public class JSoup {
	public JSoup(){}
	
	/**
	 * get the list of the 
	 * @param url
	 * @return
	 */
	public static boolean getConnection(String url) {
		Document doc;
		//ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		try {
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<String> errorList = new ArrayList<String>();
			doc = Jsoup.connect(url).get();
			Elements newsHeadlines = doc.select("body").select("tbody").select("tr").select("td").select("a");//获得a的连接
			Elements newsError = doc.select("body").select("tbody").select("tr").select("td[align]");
			
			// matched files
			for (Element link : newsHeadlines) {
				String linktext = link.text();
				list.add(linktext);
			}
			// error matched lines
			for(Element error : newsError) {
				String errortext = error.text();
				errorList.add(errortext);
			}
			return dealList(list, errorList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * deal the list of fileName and error
	 * @param fileName
	 * @param error
	 * @return
	 */
	public static boolean dealList(ArrayList<String> fileName, ArrayList<String> error) {
		//转换为数组进行处理
		if(!fileName.isEmpty()) {
			ArrayList<Result> resultList = new ArrayList<Result>();
			int length = fileName.size();
			for(int i = 0; i < length; i+=2) {
				Result rs = new Result();
				rs.setFileA(dealFileName(fileName.get(i)));
				rs.setFileB(dealFileName(fileName.get(i+1)));
				rs.setSameLines(Integer.parseInt(error.get(i/2)));
				rs.setScore(Float.parseFloat(dealLastString(fileName.get(i))));
				resultList.add(rs);
				rs = null;
			}
			
			ResultDao rd = new ResultDao();
			rd.addResultWithBatch(resultList);
			rd = null;
			resultList = null;
			return true;
		}
		return false;
	}
	
	/**
	 * To get the score according strings
	 * @param str
	 * @return
	 */
	public static String dealLastString(String str) {
		int index = str.indexOf("(");
		int last = str.indexOf("%");
		return str.substring(index + 1, last);
	}
	
	public static String dealFirstString(String str) {
		int length = str.length();
		return str.substring(0, length - 5);
	}
	/**
	 * To get the fileName
	 * @param str
	 * @return
	 */
	public static String dealFileName(String str) {
		int index = str.indexOf("/");
		int index_curl = str.indexOf("(");
		if(index != -1) {
			return str.substring(index+1, index_curl);
		}
		return str;
	}
	/**
	 * @return detailed error messages
	 * @author twins
	 * @param url
	 */
	public static void getDetailOfError(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements newsHeadlines = doc.select("center").select("table").select("tbody").select("tr").select("th");
			for (Element link : newsHeadlines) {
				  String linkText= link.text();
				  System.out.println(linkText);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * tags的写法有一下的几种：（即所谓的selector method)
	 * http://jsoup.org/apidocs/org/jsoup/select/Selector.html
	 * http://jsoup.org/cookbook/extracting-data/selector-syntax
	 * @param url
	 * @param tag
	 */
	public static void main(String [] args) {
	    getConnection("http://moss.stanford.edu/results/599719755/");
	}
}
	

package com.twins.OS;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.tags.Div;

/**
 * @author twins 
 * help with https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/
 * @version
 */
public class HtmlPaser {

	/**
	 * Function to get all links in a html file.
	 * @param url
	 */
	public static void extracLinks(String url) {
			try {
				Parser parser = new Parser(url);
				parser.setEncoding("gb2312");
	//过滤 <frame> 标签的 filter，用来提取 frame 标签里的 src 属性所、表示的链接
				NodeFilter frameFilter = new NodeFilter() {
					public boolean accept(Node node) {
						if (node.getText().startsWith("frame src=")) {
							return true;
						} else {
							return false;
						}
					}
				};
	//OrFilter 来设置过滤 <a> 标签，<img> 标签和 <frame> 标签，三个标签是 or 的关系
				OrFilter rorFilter = new OrFilter(new NodeClassFilter(LinkTag.class), new NodeClassFilter(ImageTag.class));
				OrFilter linkFilter = new OrFilter(rorFilter, frameFilter);
		//得到所有经过过滤的标签
				NodeList list = parser.extractAllNodesThatMatch(linkFilter);
				for (int i = 0; i < list.size(); i++) {
					Node tag = list.elementAt(i);
					if (tag instanceof LinkTag)//<a> 标签 
					{
						LinkTag link = (LinkTag) tag;
						String linkUrl = link.getLink();//url
						String text = link.getLinkText();//链接文字
						System.out.println(linkUrl + "**********" + text);
					}
					else if (tag instanceof ImageTag)//<img> 标签
					{
						ImageTag image = (ImageTag) list.elementAt(i);
						System.out.print(image.getImageURL() + "********");//图片地址
						System.out.println(image.getText());//图片文字
					}
					else//<frame> 标签
					{
	//提取 frame 里 src 属性的链接如 <frame src="test.html"/>
						String frame = tag.getText();
						int start = frame.indexOf("src=");
						frame = frame.substring(start);
						int end = frame.indexOf(" ");
						if (end == -1)
							end = frame.indexOf(">");
						frame = frame.substring(5, end - 1);
						System.out.println(frame);
					}
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
	}
	
	public void getAttributeValue(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
		} catch (ParserException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * output the file without html tags
	 * @param url
	 */
	public void getContentHaveNoTag(String url) {
		StringBean sb = new StringBean();
		sb.setLinks(false);//设置结果中去点链接
		sb.setURL(url);//设置你所需要滤掉网页标签的页面 url
		System.out.println(sb.getStrings());//打印结果
	}

	//遍历所有节点，输出包含关键字的节点
	public static void extractKeyWordText(String url, String keyword) {
		try {
            //生成一个解析器对象，用网页的 url 作为参数
			Parser parser = new Parser(url);
			//设置网页的编码,这里只是请求了一个 gb2312 编码网页
			parser.setEncoding("gb2312");
			//迭代所有节点, null 表示不使用 NodeFilter
			NodeList list = parser.parse(null);
            //从初始的节点列表跌倒所有的节点
			processNodeList(list, keyword);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	private static void processNodeList(NodeList list, String keyword) {
		//迭代开始
		SimpleNodeIterator iterator = list.elements();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();
			//得到该节点的子节点列表
			NodeList childList = node.getChildren();
			//孩子节点为空，说明是值节点
			if (null == childList)
			{
				//得到值节点的值
				String result = node.toPlainTextString();
				//若包含关键字，则简单打印出来文本
				if (result.indexOf(keyword) != -1)
					System.out.println(result);
			} //end if
			//孩子节点不为空，继续迭代该孩子节点
			else 
			{
				processNodeList(childList, keyword);
			}//end else
		}//end wile
	}
	
	/**
	 * 获得指定标签内的内容
	 * @param url
	 * @param tag
	 */
	public static void getContentTage(String url, String tag) {
		try {
			Parser myParser = new Parser(url);

			// 设置编码
			myParser.setEncoding("GBK");
			NodeFilter filter = new TagNameFilter(tag);
			NodeList nodeList = myParser.extractAllNodesThatMatch(filter);
			Div tabletag = (Div) nodeList.elementAt(0);

			System.out.println(tabletag.toHtml());
			System.out.println("==============");
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws ParserException {
		// TODO Auto-generated method stub
		String url = "https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/";
		getContentTage(url, "div");
	}
}
/*
 * String title; String constellation; String body; String summary;
 * 
 * Parser parser = new Parser( "http://astro.sina.com.cn/sagittarius.html");
 * parser.setEncoding("utf-8");
 * 
 * NodeFilter filter_constellation_summart = new AndFilter( (new
 * TagNameFilter(" td ")), (new HasChildFilter( new TagNameFilter(" b "))));
 * 
 * NodeFilter filter_title = new AndFilter(new TagNameFilter(" font "), new
 * HasAttributeFilter(" class ", " f1491 "));
 * 
 * NodeFilter filter_body = new AndFilter(new TagNameFilter(" td "), new
 * HasAttributeFilter(" width ", " 30% "));
 * 
 * NodeList nodelist = parser.parse(filter_constellation_summart); Node
 * node_constellation = nodelist.elementAt(0); constellation =
 * node_constellation.getFirstChild().getNextSibling() .toHtml();
 * 
 * Node node_summary = nodelist.elementAt(1); NodeList summary_nodelist =
 * node_summary.getChildren(); summary = summary_nodelist.elementAt(3).toHtml()
 * + summary_nodelist.elementAt(5).toHtml();
 * 
 * parser.reset();
 * 
 * nodelist = parser.parse(filter_title); Node node_title =
 * nodelist.elementAt(0); title =
 * node_title.getNextSibling().getNextSibling().toHtml(); // title =
 * node_title.getNextSibling().getNextSibling().toHtml() ;
 * 
 * parser.reset();
 * 
 * nodelist = parser.parse(filter_body); Node node_body = nodelist.elementAt(0);
 * Parser body_parser = new Parser(node_body.toHtml());
 * 
 * TextExtractingVisitor visitor = new TextExtractingVisitor();
 * body_parser.visitAllNodesWith(visitor); body = visitor.getExtractedText();
 * 
 * // System.out.println(node_summary.getChildren().toHtml()) ; //
 * System.out.println(node_body.toHtml()) ; // System.out.println(title.trim())
 * ; // System.out.println(constellation.trim()) ; //
 * System.out.println(body.trim()) ; System.out.println(summary.trim());
 * 
 * }
 */
/* 这些都是管理员相关事件的集合包*/
package Action;

import bean.admin;

import com.opensymphony.xwork2.ActionSupport;
import DAO.adminDao;

/* 代码修改和维护的过程中*/

public class AboutAdminAction extends ActionSupport {
	
	private int adminID; // admin's id
	private String adminName; //name
	private String adminPas; //password
	private String adminInfo; //information
	private String adminrePas; //admin repassword
	
	adminDao add = new adminDao();	

	// 添加管理员
	public String addAdmin()
	{
		admin ad = new admin();
		ad.setAdminName(this.adminName);
		ad.setAdminPas(this.adminPas);
		
		add.addAdmin(ad);
		
		this.addFieldError("success","添加成功");
		
		return this.INPUT;
	}
	public void validateAddAdmin()
	{
		if(this.adminName.trim().equals(""))
		{
			this.addFieldError("name", "用户姓名不能为空");
		}
		if(this.adminPas.trim().equals(""))
		{
			this.addFieldError("pas", "用户密码不能为空");
		}
		
		if(!this.adminPas.trim().equals(this.adminrePas))
		{
			this.addFieldError("reps", "两次密码不同");
		}
		
	}
	
	public String getAdminrePas() {
		return adminrePas;
	}
	public void setAdminrePas(String adminrePas) {
		this.adminrePas = adminrePas; // to set the adminrePas;
	}
	public int getAdminID() {
		return adminID;
	}
	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminPas() {
		return adminPas;
	}
	public void setAdminPas(String adminPas) {
		this.adminPas = adminPas;
	}
	public String getAdminInfo() {
		return adminInfo;
	}
	public void setAdminInfo(String adminInfo) {
		this.adminInfo = adminInfo;
	}
	
}

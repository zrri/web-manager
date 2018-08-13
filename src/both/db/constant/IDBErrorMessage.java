/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2012 Bankit Tech, All rights reserved.
 */
package both.db.constant;

/**
 * 数据库错误信息常量
 * 
 */
public interface IDBErrorMessage
{
	public final String 访问数据库异常 = "访问数据库异常";

	public final String 创建SQL语句出错 = "创建SQL语句出错";

	public final String 调用查询排序字段为空 = "调用查询排序字段为空";

	public final String 调用查询限制数量字段为空 = "调用查询限制数量字段为空";

	public final String 交易处理异常 = "交易处理异常";

	public final String 数据库模式DBMODE不支持 = "数据库模式DBMODE为空";

	public final String 生成SEQUENCE出错 = "生成SEQUENCE出错";

	public final String 重置SEQUENCE出错 = "重置SEQUENCE出错";
	
	public final String 读取BTOPCONFIG出错 = "读取BTOPCONFIG出错";
	
	public final String 记录流水信息失败 = "记录流水信息失败";
}

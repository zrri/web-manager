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
 * 存储过程名称常量
 * @author 董国伟
 * @date   2012-11-15
 * 
 * @history
 *  2012-11-23 董国伟 代码重构
 */
public interface DbProcedureName {

	// 存储过程（获取下个值）名称
	public final static String DBPROC_NEXTVAL = "NextVal";
	// 存储过程输出参数名称
	public final static String PROC_OUTPUT_PARAM = "nextVal";
	// 存储过程（创建流水号表）名称
	public final static String DBPROC_CREATESEQUENCE = "sp_CreateSequence";
	
	//FUNCTION获取当前机构的所有上级节点机构
	public final static String FUNCTION_GETALLPARENTID = "getallparentid";
}

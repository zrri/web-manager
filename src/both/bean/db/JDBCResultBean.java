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
/*
 * org.ailab.bean.InBean.java
 * Created by jc @ 2011-12-17 下午3:29:27
 */

package both.bean.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bankit.phoenix.trade.IBean;

public class JDBCResultBean implements IBean {

	/**
	 * 返回数据 列表
	 */
	private List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

	/**
	 * 返回数据 单一数据
	 */
	private Map<String, Object> resultMap = new HashMap<String, Object>();

	/**
	 * 返回码
	 */
	private int ret = 0;

	/**
	 * 执行事务操作时，返回的int数组
	 */
	private int[] retArray;

	/**
	 * 错误信息
	 */
	private String errorMessage;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JDBCResultBean() {
		// TODO Auto-generated constructor stub
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public int[] getRetArray() {
		return retArray;
	}

	public void setRetArray(int[] retArray) {
		this.retArray = retArray;
	}
}

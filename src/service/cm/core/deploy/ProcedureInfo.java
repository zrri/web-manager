package service.cm.core.deploy;

import com.alibaba.fastjson.JSONObject;

import service.cm.core.deploy.constant.Action;

/**
 * 过程信息
 * 
 * @author 江成
 * 
 */
public class ProcedureInfo {
	/**
	 * 任务ID
	 */
	private String taskid;

	/**
	 * 动作类型
	 */
	private Action action;

	/**
	 * 执行结果 01-成功 02-失败 03-警告
	 */
	private String status;

	/**
	 * 具体信息
	 */
	private String detail;

	/**
	 * 获取动作
	 * 
	 * @return
	 */
	public Action getAction() {
		return this.action;
	}

	/**
	 * 设置动作
	 * 
	 * @param action
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * 获取执行状态
	 * 
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * 设置执行状态
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取具体信息
	 * 
	 * @return
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * 设置具体信息
	 * 
	 * @param detail
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * 获取任务ID
	 * 
	 * @return
	 */
	public String getTaskid() {
		return taskid;
	}

	/**
	 * 设置任务ID
	 * 
	 * @param taskid
	 */
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String toJSON() {
		JSONObject object = new JSONObject();
		object.put("taskid", taskid);
		object.put("taskStatus", status);
		object.put("action", action);
		object.put("detail", detail);

		return object.toString();
	}

	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		object.put("taskid", taskid);
		object.put("taskStatus", status);
		object.put("action", action);
		object.put("detail", detail);

		return object;
	}
}
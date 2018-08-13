package service.cm.core.common.monitor;

import java.io.ByteArrayOutputStream;

import cn.com.bankit.phoenix.commons.serializable.Protocol;
import cn.com.bankit.phoenix.commons.util.ByteUtil;
import cn.com.bankit.phoenix.communication.constant.ContentType;
import cn.com.bankit.phoenix.communication.constant.MessageType;
import cn.com.bankit.phoenix.communication.socket.Router;
import cn.com.bankit.phoenix.communication.socket.SocketMessageDispatcher;
import cn.com.bankit.phoenix.communication.socket.SocketMessageHandler;

/**
 * Monitor Client
 * 
 * @author 江成
 *
 */
public class MonitorClient {

	/**
	 * 默认
	 */
	private static String NAME="default";
	
	/**
	 * socket message dispatcher
	 */
	private SocketMessageDispatcher dispatcher;
	
	/**
	 * 编码
	 */
	private String encoding;
	
	/**
	 * 构造函数
	 */
	public MonitorClient(String address,String encoding){
		Router router=new Router(address,"random");
		this.dispatcher=new SocketMessageDispatcher(router);
		this.encoding=encoding;
	}
	
	/**
	 * 调用
	 * @param name
	 * @param method
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object invoke(String name,String method,Object data) throws Exception{
		
		ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
		//monitor name
		byte[] bytes=name.getBytes(encoding);
		byte[] lenBytes=ByteUtil.int2byteArray(bytes.length);
		byteOut.write(lenBytes);
		byteOut.write(bytes);
		//monitor method
		bytes=method.getBytes(encoding);
		lenBytes=ByteUtil.int2byteArray(bytes.length);
		byteOut.write(lenBytes);
		byteOut.write(bytes);
		
		ClassLoader classLoader=this.getClass().getClassLoader();
		byte protocolMark = Protocol.AVRO.id();
		byte[] content;
		if(data != null){
			content=ObjectParser.encode(protocolMark, data);
		}else{
			content=new byte[0];
		}
		byteOut.write(content);
		//请求
		byte[] reqBytes=byteOut.toByteArray();
		
		String destination=this.dispatcher.getRemoteLogicalAddr();
		//获取message handler
		SocketMessageHandler messageHandler=this.dispatcher.getMessageHandler(NAME);
		//发送请求
		byte[] resBytes=(byte[])messageHandler.syncSend(MessageType.Request, destination,
				"MonitorServer","MonitorAcceptor", reqBytes, ContentType.Binary_Array);
		int offset=0;
		// 协议标识
		protocolMark=Protocol.AVRO.id();
		//参数
		Object resObj = null;
		if(resBytes.length>0){
			// 协议标识
			protocolMark = resBytes[offset];
			offset += 1;
			// 解码请求数据
			resObj = ObjectParser.decode(protocolMark, resBytes, offset, classLoader,this.encoding);
		}
		return resObj;
	}
	
	/**
	 * 调用
	 * @param name
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public Object invoke(String name,String method) throws Exception{
		return this.invoke(name, method, null);
	}
		
}
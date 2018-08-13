package service.cm.core.common.monitor;

import java.io.ByteArrayOutputStream;

import cn.com.bankit.phoenix.commons.serializable.Protocol;
import cn.com.bankit.phoenix.commons.serializable.SerializableUtil;

/**
 * 消息解析器
 * 
 * @author 江成
 *
 */
public class ObjectParser {
	
	/**
	 * 解码请求内容
	 * 
	 * @param protocolMark
	 * @param data
	 * @param className
	 * @param classLoader
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static Object decode(byte protocolMark,byte[] data,int offset,
			ClassLoader classLoader,String encoding) throws Exception {
		// 请求数据
		int len = SerializableUtil.bytesToInt(data, offset);
		offset += 4;
		byte[] content = new byte[len];
		System.arraycopy(data, offset, content, 0, len);
		offset += len;
		
		Object res;
		// 根据协议反序列化
		if (Protocol.AVRO.id() == protocolMark) {
			res = SerializableUtil.avroBytesToObject(content, classLoader);
		} else{
			res=new String(content,encoding);
		}
		return res;
	}

	/**
	 * 编码响应内容
	 * 
	 * @param protocolMark
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static byte[] encode(byte protocolMark, Object bean) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		byte[] content = null;
		if (Protocol.AVRO.id() == protocolMark) {
			content = SerializableUtil.objectToAvroBytes(bean);
		} else {
			String json = SerializableUtil.objectToJson(bean);
			content = SerializableUtil.stringToBytes(json);
		}
	
		// 写入协议标识
		byteOut.write(new byte[] { protocolMark });

		byte[] lenBytes = SerializableUtil.intTobytes(content.length);
		// 写入content长度
		byteOut.write(lenBytes);
		// 写入内容
		byteOut.write(content);

		byte[] res = byteOut.toByteArray();
		return res;
	}
}
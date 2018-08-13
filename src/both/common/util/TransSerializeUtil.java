package both.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import cn.com.bankit.phoenix.commons.serializable.json.JsonSerializableUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TransSerializeUtil {

	public TransSerializeUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String SerializeContextToBase64String(
			Map<String, Object> contextData, Boolean isCompress)
			throws UnsupportedEncodingException, IOException {
		if (contextData == null || contextData.isEmpty())
			return null;

		return SerializeObjectToBase64String(contextData, isCompress);
	}

	public static String SerializeObjectToBase64String(Object contextData,
			Boolean isCompress) throws UnsupportedEncodingException,
			IOException {
		if (contextData == null)
			return null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(contextData);

		oos.close();
		bos.close();

		byte[] serializeData = bos.toByteArray();

		if (isCompress) {
			return compressBytesToBase64String(serializeData);
		} else {
			return new BASE64Encoder().encode(serializeData);
		}
	}

	public static byte[] SerializeContextToByteArray(
			Map<String, Object> contextData, Boolean isCompress)
			throws UnsupportedEncodingException, IOException {
		if (contextData == null || contextData.isEmpty())
			return null;

		return SerializeObjectToByteArray(contextData, isCompress);
	}

	public static byte[] SerializeObjectToByteArray(Object contextData,
			Boolean isCompress) throws UnsupportedEncodingException,
			IOException {
		if (contextData == null)
			return null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(contextData);

		oos.close();
		bos.close();

		byte[] serializeData = bos.toByteArray();

		if (isCompress) {
			return compressBytes(serializeData);
		} else {
			return serializeData;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> DeserializeContextFromBase64String(
			String base64ContextString, Boolean isCompressed)
			throws UnsupportedEncodingException, IOException,
			DataFormatException, ClassNotFoundException {
		return (Map<String, Object>) DeserializeObjectFromBase64String(
				base64ContextString, isCompressed);
	}

	public static Object DeserializeObjectFromBase64String(
			String base64ContextString, Boolean isCompressed)
			throws UnsupportedEncodingException, IOException,
			DataFormatException, ClassNotFoundException {
		if (base64ContextString == null || base64ContextString.isEmpty())
			return null;

		byte[] serializeData;
		if (isCompressed) {
			serializeData = extractBase64StringToBytes(base64ContextString);
		} else {
			serializeData = new BASE64Decoder()
					.decodeBuffer(base64ContextString);
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(serializeData);
		ObjectInputStream ois = new ObjectInputStream(bis);

		Object contextObject = ois.readObject();
		if (contextObject == null)
			return null;

		return contextObject;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> DeserializeContextFromByteArray(
			byte[] contextData, Boolean isCompressed)
			throws UnsupportedEncodingException, IOException,
			DataFormatException, ClassNotFoundException {
		return (Map<String, Object>) DeserializeObjectFromByteArray(
				contextData, isCompressed);
	}

	public static Object DeserializeObjectFromByteArray(byte[] contextData,
			Boolean isCompressed) throws UnsupportedEncodingException,
			IOException, DataFormatException, ClassNotFoundException {
		if (contextData == null || contextData.length == 0)
			return null;

		byte[] serializeData;
		if (isCompressed) {
			serializeData = extractBytes(contextData);
		} else {
			serializeData = contextData;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(serializeData);
		ObjectInputStream ois = new ObjectInputStream(bis);

		Object contextObject = ois.readObject();
		if (contextObject == null)
			return null;

		return contextObject;
	}

	public static String compressBase64String(String data)
			throws UnsupportedEncodingException, IOException {
		byte[] input = new BASE64Decoder().decodeBuffer(data);

		byte[] output = compressBytes(input);

		return new BASE64Encoder().encode(output);
	}

	public static byte[] compressBase64StringToBytes(String data)
			throws UnsupportedEncodingException, IOException {
		byte[] input = new BASE64Decoder().decodeBuffer(data);

		byte[] output = compressBytes(input);

		return output;
	}

	public static String compressBytesToBase64String(byte[] input)
			throws UnsupportedEncodingException, IOException {
		byte[] output = compressBytes(input);

		return new BASE64Encoder().encode(output);
	}

	public static byte[] compressBytes(byte[] input)
			throws UnsupportedEncodingException, IOException {
		Deflater df = new Deflater(); // this function mainly generate the byte
										// code
		df.setLevel(Deflater.BEST_COMPRESSION);
		df.setInput(input);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length); // we
																				// write
																				// the
																				// generated
																				// byte
																				// code
																				// in
																				// this
																				// array
		df.finish();
		byte[] buff = new byte[1024]; // segment segment pop....segment set 1024
		while (!df.finished()) {
			int count = df.deflate(buff); // returns the generated code... index
			baos.write(buff, 0, count); // write 4m 0 to count
		}
		baos.close();
		byte[] output = baos.toByteArray();

		return output;
	}

	public static String extraceBase64String(String data)
			throws UnsupportedEncodingException, IOException,
			DataFormatException {
		byte[] input = new BASE64Decoder().decodeBuffer(data);

		byte[] output = extractBytes(input);

		return new BASE64Encoder().encode(output);
	}

	public static String extractBytesToBase64String(byte[] input)
			throws UnsupportedEncodingException, IOException,
			DataFormatException {
		byte[] output = extractBytes(input);

		return new BASE64Encoder().encode(output);
	}

	public static byte[] extractBase64StringToBytes(String data)
			throws UnsupportedEncodingException, IOException,
			DataFormatException {
		byte[] input = new BASE64Decoder().decodeBuffer(data);

		byte[] output = extractBytes(input);

		return output;
	}

	public static byte[] extractBytes(byte[] input)
			throws UnsupportedEncodingException, IOException,
			DataFormatException {
		Inflater ifl = new Inflater(); // mainly generate the extraction
		ifl.setInput(input);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
		byte[] buff = new byte[1024];
		while (!ifl.finished()) {
			int count = ifl.inflate(buff);
			baos.write(buff, 0, count);
		}
		baos.close();
		byte[] output = baos.toByteArray();

		return output;
	}
	
	/**
	 * 
	 * @param contextbase64str
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getContextMap(String contextbase64str)
			throws Exception {

		if (contextbase64str == null)
			return new HashMap<String, Object>();

		String jsonStr = new String(
				extractBase64StringToBytes(contextbase64str));
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = (Map<String, Object>) JsonSerializableUtil.jsonToObject(jsonStr,
				Map.class);
		//
		// Map contextMap = extractSubMap(jsonMap);
		//
		// LoggerUtil.error("+++++++contextMap+++++++>" +
		// contextMap.toString());
		//
		// if (contextMap == null)
		// contextMap = new HashMap();

		return jsonMap;
	}
}

package service.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;

import cn.com.bankit.phoenix.communication.http.FileItem;
import cn.com.bankit.phoenix.communication.http.HttpMessage;
import cn.com.bankit.phoenix.resource.ResourceManager;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 文件上传服务
 * 
 * @author 江成
 *
 */
public class UploadService extends Service<HttpMessage,HttpMessage>{

	/**
	 * 执行
	 */
	@SuppressWarnings("unchecked")
	public HttpMessage uploadFile(HttpMessage request) throws Exception {
		System.out.println("-------------------> start");
		Map<String,Object> map=(Map<String,Object>)request.getContent();
		for(String key:map.keySet()){
			//获取item
			Object item=map.get(key);
			
			if(item instanceof FileItem){
				FileItem fileItem=(FileItem)item;
				String name=key;
				String fileName=fileItem.getFileName();
				System.out.println("----> name:"+name+" ,fileName:"+fileName);
				if(fileName==null || fileName.length()==0){
					continue;
				}
				InputStream in=null;
				try{
					in=fileItem.getInputStream();
					if(in==null){
						continue;
					}
					ResourceManager resMg=ResourceManager.getInstance();
					String installRoot=resMg.getInstallRoot();
					StringBuilder sb=new StringBuilder();
					sb.append(installRoot);
					sb.append("/workspace/versionpath/");
					sb.append(fileName);
					//写入数据
					resMg.setResourceContent(sb.toString(), in);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(in!=null){
						try{
							in.close();
						}catch(Exception e){
							//do nothing
						}
					}
				}
				
			}else if(item instanceof String[]){
				String name=key;
				Object value=map.get(name);
				System.out.println("----> name:"+name+" ,values:"+Arrays.toString((String[])value));
			}else{
				String name=key;
				Object value=map.get(name);
				System.out.println("----> name:"+name+" ,value:"+value);
			}
		}
	    HttpMessage response=new HttpMessage();
	    response.setContent("上传成功");
		return response;
	}
	
	
	/**
	 * 上传大文件
	 */
	@SuppressWarnings("unchecked")
	public HttpMessage uploadBigFile(HttpMessage request) throws Exception {
		Map<String,Object> map=(Map<String,Object>)request.getContent();
		//记录文件名
		String fileName=(String)map.get("fileName");
		//记录片段开始位置
		long start=Long.parseLong(map.get("start").toString());
		//当前索引
		int index=Integer.parseInt(map.get("index").toString());
		//切片数量
		int count=Integer.parseInt(map.get("count").toString());
		//获取片段数据
		FileItem fileItem=(FileItem)map.get("data");
		
		//定义byte数据流
		ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
		//定义输入流
		InputStream in=null;
		try{
			//获取输入流
			in=fileItem.getInputStream();
			if(in==null){
				 HttpMessage response=new HttpMessage();
				 response.setContent("上传数据无效");
				 return response;
			}
			
			int len=0;
			byte[] buffer=new byte[1024];
			while((len=in.read(buffer))!=-1){
				byteOut.write(buffer, 0, len);
			}
		}finally{
			if(in!=null){
				//关闭输入流
				try{
					in.close();
				}catch(Exception e){
					//do nothing
				}
			}
		}
		
		//获取文件路径
		ResourceManager resMg=ResourceManager.getInstance();
		String installRoot=resMg.getInstallRoot();
		StringBuilder sb=new StringBuilder();
		sb.append(installRoot);
		sb.append("/upload/");
		sb.append(fileName);
		String filePath=sb.toString();
		//定义临时文件名
		String tmpFilePath=filePath+".tmp";
		//定义文件访问器
		RandomAccessFile randomFile=null;
		try{
			//创建文件访问器
			randomFile=new RandomAccessFile(tmpFilePath, "rw");
			randomFile.seek(start);
			randomFile.write(byteOut.toByteArray());
		}finally{
			//关闭文件访问器
			if(randomFile!=null){
				try{
					randomFile.close();
				}catch(Exception e){
					//do nothing
				}
			}
		}
		//传输完成，修改为正常的文件名称
		if(index==count){
			File file=new File(tmpFilePath);
			file.renameTo(new File(filePath));
		}
		System.out.println("写入片段 fileName:"+fileName+", index:"+map.get("index")+", count:"+map.get("count")+", start:"+start+", end:"+map.get("end"));
	    HttpMessage response=new HttpMessage();
	    response.setContent("上传成功");
		return response;
	}


	@Override
	public HttpMessage execute(HttpMessage arg0) throws Exception {
		return null;
	}
}
package both.common.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Vector;

import both.common.util.FormatUtil;
import both.common.util.LoggerUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SFTP客户端
 * 
 * @author 江成
 * 
 */
public class SftpClient implements IFtpClient {

	/**
	 * 会话
	 */
	private Session session;

	/**
	 * 通道
	 */
	private ChannelSftp sftp;

	/**
	 * 连接
	 * 
	 * @param url
	 * @param port
	 * @param userName
	 * @param password
	 * @param timeout
	 *            (单位秒)
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String url, int port, String userName,
			String password, int timeout) throws Exception {
		if (port == -1) {
			port = 22;
		}
		JSch jsch = new JSch();
		try {

			// 用户名的key和value，对应get方法和界面中的值。
			session = jsch.getSession(userName, url, port);
			// 设置密码
			session.setPassword(password);
			// 设置超时
			session.setTimeout(timeout * 1000);
			// Try to ssh from the command line and accept the public key
			session.setConfig("StrictHostKeyChecking", "no");
			// 尝试连接
			session.connect();
		} catch (JSchException e) {
			LoggerUtil.debug(e.getMessage(), e);
			// 断开连接
			disconnect();
			return false;
		}

		try {
			// 建立通道
			this.sftp = (ChannelSftp) session.openChannel("sftp");
			this.sftp.connect();
		} catch (JSchException e) {
			LoggerUtil.debug(e.getMessage(), e);
			// 断开连接
			disconnect();
			return false;
		}
		return true;
	}

	/**
	 * 是否连接中
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (session != null) {
			return session.isConnected();
		}
		return false;
	}

	/**
	 * 断开连接
	 * 
	 * @return
	 */
	public boolean disconnect() throws Exception {
		boolean res = true;
		// 关闭SFTP
		if (this.sftp != null) {
			try {
				this.sftp.quit();
			} catch (Exception e) {
				LoggerUtil.error(e.getMessage());
				res = false;
			}
		}
		// 关闭session
		if (this.session != null) {
			try {
				this.session.disconnect();
			} catch (Exception e) {
				LoggerUtil.error(e.getMessage());
				res = false;
			}
		}
		return res;
	}

	/**
	 * 更改为当前工作目录的父目录
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean changeToParentDirectory() throws Exception {
		this.sftp.cd("..");
		return true;
	}

	/**
	 * 如果不存在文件路径，则创建该目录
	 * 
	 * @param name
	 * @return
	 */
	private boolean makeDirIfNoExist(String name) throws Exception {
		boolean isExist = false;
		@SuppressWarnings("unchecked")
		Vector<LsEntry> items = this.sftp.ls(".");
		for (int i = 0, size = items.size(); i < size; i++) {
			LsEntry entry = items.get(i);
			String fileName = entry.getFilename();
			if (fileName.equals(name)) {
				isExist = true;
				break;
			}
		}

		if (!isExist) {
			// 建立文件夹
			this.sftp.mkdir(name);
		}
		return true;
	}

	/**
	 * 改变工作目录
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public boolean changeWorkingDirectory(String path) throws Exception {
		// 格式化路径
		path = FormatUtil.formatPath(path);
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		if('/'==path.charAt(0)){
			sftp.cd("/");
			path=path.substring(1);
		}

		String name = path;
		int index = path.indexOf("/");
		int fromIndex = 0;
		while (index != -1) {
			name = path.substring(fromIndex, index);
			makeDirIfNoExist(name);
			this.sftp.cd(name);
			// 修改form index
			fromIndex = index + 1;
			index = path.indexOf("/", fromIndex);
		}
		name = path.substring(fromIndex, path.length());
		makeDirIfNoExist(name);

		this.sftp.cd(name);
		return true;
	}

	/**
	 * 上送文件
	 * 
	 * @param fileName
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(String fileName, InputStream input)
			throws Exception {
		this.sftp.put(input, fileName);
		return true;
	}

	/**
	 * 下载文件
	 * 
	 * @param remotePath
	 * @param fileName
	 * @param localPath
	 * @return
	 */
	public boolean downloadFile(String remotePath, String fileName,
			String localPath) throws Exception {
		File localFile = new File(localPath + "/" + fileName);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(localFile));
		try {
			this.sftp.get(fileName, out);
			return true;
		} finally {
			out.close();
		}
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SftpClient sftpClient = new SftpClient();
		//sftpClient.connect("192.168.100.10", 22, "test", "test", 10);
		sftpClient.connect("130.1.107.61", 22, "bip", "bankitbip", 240);

		System.err.println("开始上传");
		long startTime=System.currentTimeMillis();
		File file = new File("test/jdk-6u32-windows-x64.exe");
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			sftpClient.changeWorkingDirectory("/bip/jc");
			sftpClient.uploadFile("jdk-6u32-windows-x64.exe", in);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
			sftpClient.disconnect();
		}
		long endTime=System.currentTimeMillis();
		long time=(endTime-startTime)/1000;
		long size=file.length()/1024;
		long speed=size/time;
		System.err.println("sftp:文件大小："+size+"KB,耗时:"+time+"秒,上传速度为:"+speed);

	}

}

package both.common.ftp;

import both.common.constant.Protocol;

/**
 * FTP工厂
 * 
 * @author 江成
 * 
 */
public class FtpFactory {

	/**
	 * 获取FTP Client
	 * 
	 * @param protocol
	 * @return
	 */
	public static IFtpClient getFtpClient(Protocol protocol) {
		IFtpClient ftpClient = null;
		if (protocol == Protocol.SSH) {
			ftpClient = new SftpClient();
		} else {
			ftpClient = new FtpClient();
		}
		return ftpClient;
	}
}

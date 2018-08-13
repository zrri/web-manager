package both.common.shell;

import both.common.constant.Protocol;
import both.common.shell.ssh.SSH2Connector;
import both.common.shell.telnet.TelnetConnector;


/**
 * connector factory
 * 
 * @author 江成
 * 
 */
public class ConnectorFactory {

	/**
	 * 获取连接器
	 * 
	 * @param protocol
	 * @param netArgs
	 * @return
	 */
	public static IConnector getConnector(Protocol protocol, NetArgs netArgs) {
		IConnector connector=null;
		if(protocol==Protocol.SSH){
			connector=new SSH2Connector();
			connector.init(netArgs);
		}else{
			connector=new TelnetConnector();
			connector.init(netArgs);
		}
		return connector;
	}
}

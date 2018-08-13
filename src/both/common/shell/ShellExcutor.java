package both.common.shell;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import both.common.config.PreferenceUtil;
import both.common.util.LoggerUtil;
import cn.com.bankit.phoenix.commons.thread.PheExecutorFactory;
import cn.com.bankit.phoenix.commons.util.StringUtil;

/**
 * Shell执行器
 * 
 * @author 江成
 * 
 */
public class ShellExcutor {

	/**
	 * 插件ID
	 */
	private static String MARK = "shell";

	/**
	 * 命令间隔
	 */
	private static long commandInterval = 100;
	
	/**
	 * 初始化
	 */
	static{
		// 获取命令间隔
        commandInterval = PreferenceUtil.getLong(MARK, "commandInterval", 100);
	}
	
	/**
	 * 定义线程池
	 */
	private static ExecutorService executor= PheExecutorFactory.newCachedThreadPool();

	/**
	 * 同步发送命令
	 * 
	 * @param script
	 * @param encoding
	 * @param connector
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String execute(String script, final String encoding,
			final IConnector connector, long timeout) throws Exception {
		
		//开始标志
		final boolean[] started = new boolean[]{false};

		// 完成标志
		final boolean[] finish = new boolean[]{false};

		// 接结果数据
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		// 定义线程
		Runnable runner = new Runnable() {

			/**
			 * run
			 */
			public void run() {
				try {
					int len = -1;
					byte[] buffer = new byte[1024];
					//标志开始
					started[0]=true;
					// 获取结果
					while ((len = connector.read(buffer)) != -1) {
						byteOut.write(buffer, 0, len);
					}
					LoggerUtil.debug("执行脚本返回结果：" + byteOut.toString(encoding));
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				} finally {
					finish[0] = true;
				}
			}
		};
		// 启动线程
		executor.execute(runner);

		//等待监听线程启动
		int count=0;
		do{
			Thread.sleep(commandInterval);
			count++;
		}while(!started[0] && count<5);
		
		String[] commands = StringUtil.split(script,"\n");
		for (int i = 0; i < commands.length; i++) {
			String command = commands[i] + "\r\n";
			// 获取发送数据
			byte[] bytes = command.getBytes(encoding);
			// 发送命令
			connector.write(bytes);
			// 等待间隔
			Thread.sleep(commandInterval);
		}

		//标志是否已经进行中断尝试
		boolean interruptTry=false;
		//尝试次数
		int tryTimes = 0;
		while (finish[0] != true) {
			long time = tryTimes * 100;
			if (time > timeout) {
				//中断并退出
				if(!interruptTry){
					LoggerUtil.warn("等待超时，尝试中断处理");
					interruptTry=true;
				    connector.write(new byte[] { 03 });
				}else{
					String msg = "等待返回结果超时,耗时：" + time + "毫秒";
				    throw new TimeoutException(msg);
				}
			}
			// 每次等待100毫秒
			Thread.sleep(100);
			tryTimes++;
		}
		// 获取返回数据
		String s = byteOut.toString(encoding);
		return s;
	}

	/**
	 * 异步发送命令
	 * 
	 * @param script
	 * @param encoding
	 * @param connector
	 * @throws Exception
	 */
	public static void asynExecute(String script, String encoding,
			final IConnector connector) throws Exception {

		// 获取发送数据
		byte[] bytes = script.getBytes(encoding);
		// 发送命令
		connector.write(bytes);
	}
}

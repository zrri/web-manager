package service.cm.services.test;

import java.util.List;

import service.cm.core.common.monitor.MonitorClient;

public class TestClient {

	public static void test(String[] args) throws Exception{
		String encoding = "UTF-8";
		String host="127.0.0.1:8086";
		MonitorClient client=new MonitorClient(host,encoding);
		
		String name="CommunicationServerMonitor";
		String method="doGetAllClientAddress";
		
        @SuppressWarnings("unchecked")
		List<String> resList=(List<String>)client.invoke(name, method, null);
		
		System.out.println("结果数据:");
		for(int i=0,size=resList.size();i<size;i++){
			System.out.println(resList.get(i));
		}
	}
}
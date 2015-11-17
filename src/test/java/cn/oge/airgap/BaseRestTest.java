package cn.oge.airgap;

import java.util.List;

import cn.oge.sci.JAirgapMain;
import cn.oge.sci.StatInfo;

public class BaseRestTest {

	public void dotest(String host, int port, int dubboPort, String[] inkks) {
		dotest(host, port, dubboPort, inkks, null);
	}

	public void dotest(String host, int port, int dubboPort, String[] inkks, List<String> outkks) {
		for (int i = 0; i < 1000; i++) {
			StatInfo result = null;
			if (outkks == null) {
				result = JAirgapMain.calc(inkks, host, port, dubboPort);
			} else {
				result = JAirgapMain.calc(inkks, host, port, dubboPort, outkks);
			}
			if (result != null) {
				System.out.println("good!");
				System.out.println(result.getErrorMsg());
			}

			System.out.println("-------" + (i + 1));
			
			try {
				Thread.sleep(1000 * 3L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

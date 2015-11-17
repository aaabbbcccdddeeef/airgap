package cn.oge.airgap.tzl67;

import org.junit.Test;

import cn.oge.airgap.BaseRestTest;
import cn.oge.airgap.kks.KKS_TZL67_01;
import cn.oge.airgap.kks.KKS_TZL67_02;
import cn.oge.airgap.kks.KKS_XTD_01;

public class RestTest_TZL_67 extends BaseRestTest{

	@Test
	public void 桐子林_1号机_8个方向() {
		String host = "211.154.164.67";
		int port = 5080;
		int dubboPort = 20883;
		dotest(host, port, dubboPort, KKS_TZL67_01.KKS_IN, KKS_TZL67_01.KKS_OUT);
	}

	@Test
	public void 桐子林_2号机_8个方向() {
		String host = "211.154.164.67";
		int port = 5080;
		int dubboPort = 20883;
		dotest(host, port, dubboPort, KKS_TZL67_02.KKS_IN, KKS_TZL67_01.KKS_OUT);
	}

	@Test
	public void 小天都_1号机_4个方向() {
		String host = "10.61.98.79";
		int port = 8082;
		int dubboPort = 20883;
		String[] inkks4 = KKS_XTD_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_TZL67_01.KKS_OUT);
	}


}

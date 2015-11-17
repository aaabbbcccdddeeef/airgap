package cn.oge.airgap;

import java.util.List;

import org.junit.Test;

import cn.oge.airgap.kks.KKS_QJ_01;
import cn.oge.airgap.kks.KKS_TZL67_01;
import cn.oge.airgap.kks.KKS_XTD_01;
import cn.oge.airgap.kks.KKS_XTD_02;
import cn.oge.sci.JAirgapMain;
import cn.oge.sci.StatInfo;

public class RestTest {

	@Test
	public void 桐子林_1号机_8个方向() {
		String host = "211.154.164.67";
		int port = 5080;
		int dubboPort = 20883;
		dotest(host, port, dubboPort, KKS_TZL67_01.KKS_IN, KKS_TZL67_01.KKS_OUT);
	}

	@Test
	public void 桐子林_1号机_4个方向() {
		String host = "211.154.164.67";
		int port = 5080;
		int dubboPort = 20883;
		String[] inkks8 = KKS_TZL67_01.KKS_IN;
		String[] inkks4 = { inkks8[0], inkks8[2], inkks8[4], inkks8[6] };
		dotest(host, port, dubboPort, inkks4, KKS_TZL67_01.KKS_OUT);
	}
	

	@Test
	public void 小天都_1号机_4个方向() {
		String host = "10.61.98.79";
		int port = 8082;
		int dubboPort = 20883;
		String[] inkks4 = KKS_XTD_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_TZL67_01.KKS_OUT);
	}
	
	@Test
	public void 小天都_2号机_4个方向() {
		String host = "10.61.98.79";
		int port = 8082;
		int dubboPort = 20883;
		String[] inkks4 = KKS_XTD_02.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_TZL67_01.KKS_OUT);
	}
	
	@Test
	public void 青居_1号机_4个方向() {
		String host = "10.61.70.80";
		int port = 8082;
		int dubboPort = 20883;
		String[] inkks4 = KKS_QJ_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_TZL67_01.KKS_OUT);
	}

	public static void dotest(String host, int port, int dubboPort, String[] inkks, List<String> outkks) {
		for (int i = 0; i < 1000; i++) {
			StatInfo result = JAirgapMain.calc(inkks, host, port, dubboPort, outkks);
			if (result != null) {
				System.out.println("good!");
			}
			System.out.println("-------" + (i + 1));
		}
	}

}

package cn.oge.airgap.xtd;

import org.junit.Test;

import cn.oge.airgap.BaseRestTest;
import cn.oge.airgap.kks.KKS_XTD_01;
import cn.oge.airgap.kks.KKS_XTD_02;
import cn.oge.airgap.kks.KKS_XTD_03;

public class RestTest_XTD extends BaseRestTest {

	private String host = "10.61.98.79";
	private int port = 8082;
	private int dubboPort = 20883;

	/**
	 * 硬件有问题
	 */
	@Test
	public void 小天都_1号机_4个方向() {
		String[] inkks4 = KKS_XTD_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 小天都_2号机_4个方向() {
		String[] inkks4 = KKS_XTD_02.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 小天都_3号机_4个方向() {
		String[] inkks4 = KKS_XTD_03.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	/**
	 * 硬件有问题
	 */
	@Test
	public void 小天都_1号机_4个方向_写() {
		String[] inkks4 = KKS_XTD_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_XTD_01.KKS_OUT);
	}

	@Test
	public void 小天都_2号机_4个方向_写() {
		String[] inkks4 = KKS_XTD_02.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_XTD_02.KKS_OUT);
	}

	@Test
	public void 小天都_3号机_4个方向_写() {
		String[] inkks4 = KKS_XTD_03.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_XTD_03.KKS_OUT);
	}

}

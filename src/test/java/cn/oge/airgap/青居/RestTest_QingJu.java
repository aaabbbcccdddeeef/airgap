package cn.oge.airgap.青居;

import org.junit.Test;

import cn.oge.airgap.BaseRestTest;
import cn.oge.airgap.kks.KKS_QJ_01;
import cn.oge.airgap.kks.KKS_QJ_02;
import cn.oge.airgap.kks.KKS_QJ_03;
import cn.oge.airgap.kks.KKS_QJ_04;

public class RestTest_QingJu extends BaseRestTest {

	private String host = "10.61.70.80";
	private int port = 8082;
	private int dubboPort = 20883;

	@Test
	public void 青居_1号机_4个方向() {
		String[] inkks4 = KKS_QJ_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 青居_2号机_4个方向() {
		String[] inkks4 = KKS_QJ_02.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 青居_3号机_4个方向() {
		String[] inkks4 = KKS_QJ_03.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 青居_4号机_4个方向() {
		String[] inkks4 = KKS_QJ_04.KKS_IN;
		dotest(host, port, dubboPort, inkks4);
	}

	@Test
	public void 青居_1号机_4个方向_写() {
		String[] inkks4 = KKS_QJ_01.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_QJ_01.KKS_OUT);
	}

	@Test
	public void 青居_2号机_4个方向_写() {
		String[] inkks4 = KKS_QJ_02.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_QJ_02.KKS_OUT);
	}

	@Test
	public void 青居_3号机_4个方向_写() {
		String[] inkks4 = KKS_QJ_03.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_QJ_03.KKS_OUT);
	}

	@Test
	public void 青居_4号机_4个方向_写() {
		String[] inkks4 = KKS_QJ_04.KKS_IN;
		dotest(host, port, dubboPort, inkks4, KKS_QJ_03.KKS_OUT);
	}

}

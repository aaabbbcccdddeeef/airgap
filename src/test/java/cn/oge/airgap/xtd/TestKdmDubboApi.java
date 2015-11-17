package cn.oge.airgap.xtd;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;

import cn.oge.airgap.kks.KKS_XTD_02;
import cn.oge.airgap.kks.KKS_XTD_03;
import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.sci.data.DataLoader;
import cn.oge.sci.data.kdm.KdmDubboApi;
import cn.oge.sci.util.KdmJsonUtils;

public class TestKdmDubboApi {

	private static String dubboUrl = "dubbo://10.61.98.79:20883";

	@Test
	public void test_02() {
		saveToFile(KKS_XTD_02.KKS_IN_STR, 2);
	}

	@Test
	public void test_03() {
		saveToFile(KKS_XTD_03.KKS_IN_STR, 3);
	}

	@Test
	public void test_timestamp_1441487714000() {
		long time = 1441487714000L;
		saveToFile(KKS_XTD_03.KKS_IN_STR, 3, time);
	}

	public static void saveToFile(String kks, int machineNo, Long ltime) {
		Date time = new Date(ltime);
		DataLoader dataLoader = new KdmDubboApi(dubboUrl);
		try {
			List<RTDataSet> snapshot = dataLoader.getRtData(KKS_XTD_02.KKS_IN_STR, time, time);
			System.out.println(JSONArray.fromObject(snapshot));
			KdmJsonUtils.saveToFile(snapshot, "src/test/resources/xtd/airgap_" + machineNo + "_" + ltime + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveToFile(String kks, int machineNo) {
		DataLoader dataLoader = new KdmDubboApi(dubboUrl);
		try {
			List<RTDataSet> snapshot = dataLoader.getRTDataSnapshot(kks);
			System.out.println(JSONArray.fromObject(snapshot));
			KdmJsonUtils.saveToFile(snapshot, "src/test/resources/xtd/snapshot/airgap_0" + machineNo + ".json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

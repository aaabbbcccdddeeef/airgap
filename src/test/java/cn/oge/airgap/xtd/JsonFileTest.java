package cn.oge.airgap.xtd;

import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.oge.airgapcacu.output.AirGapOutput;
import org.oge.common.decompress.model.MasterModel;

import cn.oge.airgap.kks.KKS_TZL67_01;
import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;
import cn.oge.sci.JAirgapMain;
import cn.oge.sci.algorithm.OgeAlgUtils;
import cn.oge.sci.util.KdmJsonUtils;
import cn.oge.sci.util.RestDataloader;

public class JsonFileTest {

	/**
	 * 获取桐子林01号机组的快照数据，存成JSON格式数据
	 */
	@Test
	public void tzl_01_8() {
		String host = "211.154.164.67";
		int port = 5080;
		getSnapshotJsonFile(KKS_TZL67_01.KKS_IN, host, port, "tzl67");
	}

	/**
	 * 获取桐子林01号机组的快照数据，存成JSON格式数据
	 */
	@Test
	public void tzl_01_4() {
		String host = "211.154.164.67";
		int port = 5080;
		String[] inkks8 = KKS_TZL67_01.KKS_IN;
		String[] inkks4 = { inkks8[0], inkks8[2], inkks8[4], inkks8[6] };
		getSnapshotJsonFile(inkks4, host, port, "tzl67");
	}

	public static void getSnapshotJsonFile(String[] kks, String host, int port, String folder) {
		List<RTDataSet> rtdsList = RestDataloader.getSnapshot(kks, host, port);
		Long time = null;
		if (!rtdsList.isEmpty()) {
			List<RTValue> rtvList = rtdsList.get(0).getRTDataValues();
			if (rtvList.isEmpty()) {
				System.out.println("数据有误");
				System.out.println(JSONArray.fromObject(rtdsList));
				return;
			}
			time = rtvList.get(0).getTime();
		}
		KdmJsonUtils.saveToFile(rtdsList, "src/test/resources/" + folder + "/" + time + ".json");
	}

	/**
	 * 小天都数据，4个方向，小天都只有4个方向
	 */
	@Test
	public void test_1439574747000() throws Exception {
		String filepath = "/xtd/1439574747000.json";
		dotest(filepath, true);
	}

	/**
	 * 小天都数据，4个方向，小天都只有4个方向<br>
	 * 错误方向：S-L-N-R
	 */
	@Test
	public void test_1441090167000_error() throws Exception {
		String filepath = "/xtd/xtd-01-error.json";
		dotest(filepath, true);
	}

	/**
	 * 小天都数据，4个方向，小天都只有4个方向<br>
	 * 正确方向：L-N-R-S
	 */
	@Test
	public void test_1441090167000() throws Exception {
		String filepath = "/xtd/xtd-01.json";
		dotest(filepath, true);
	}

	/**
	 * 桐子林数据，4个方向
	 */
	@Test
	public void test_1439901469000() throws Exception {
		String filepath = "/tzl67/1439901469000.json";
		dotest(filepath, true);
	}

	/**
	 * 桐子林数据，8个方向
	 */
	@Test
	public void test_1439903885000() throws Exception {
		String filepath = "/tzl67/1439903885000.json";
		dotest(filepath, false);
	}

	/**
	 * 小天都，snapshot
	 */
	@Test
	public void test_snapshot_02() throws Exception {
		String filepath = "/xtd/snapshot/airgap_02.json";
		dotest(filepath, true);
	}

	/**
	 * 小天都，snapshot
	 */
	@Test
	public void test_snapshot_03() throws Exception {
		String filepath = "/xtd/snapshot/airgap_03.json";
		dotest(filepath, true);
	}

	public static void dotest(String filepath) throws Exception {
		dotest(filepath, false);
	}

	public static void dotest(String filepath, boolean isInkks4) throws Exception {

		String absPath = JsonFileTest.class.getResource(filepath).getPath();
		String json = KdmJsonUtils.readFile(absPath);
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(json);
		MasterModel[] masterModels = OgeAlgUtils.getMasterModels(rtdsList);
		if (isInkks4) {
			JAirgapMain.ajustInKks(4);
		}
		AirGapOutput result = JAirgapMain.cacu(masterModels);
		JAirgapMain.showMessage(result);
	}
}

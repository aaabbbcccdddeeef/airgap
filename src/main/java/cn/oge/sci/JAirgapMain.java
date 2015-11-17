package cn.oge.sci;

import java.util.List;

import net.sf.json.JSONArray;

import org.oge.airgapcacu.core.AirGapCircleAlogrithm;
import org.oge.airgapcacu.core.CacuGap;
import org.oge.airgapcacu.input.AirGapInputOne;
import org.oge.airgapcacu.input.AirGapInputR;
import org.oge.airgapcacu.output.AirGapOutput;
import org.oge.airgapcacu.output.AngleAndFloat;
import org.oge.airgapcacu.output.AngleAndFloatArray;
import org.oge.common.decompress.model.MasterModel;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.sci.algorithm.OgeAlgUtils;
import cn.oge.sci.data.kdm.KdmDubboApi;
import cn.oge.sci.util.Constant;
import cn.oge.sci.util.KdmUtils;
import cn.oge.sci.util.RestDataloader;
import cn.oge.sci.util.RtdsTimeHelper;

public class JAirgapMain {

	private static int KKS_LEN = 8;
	private static int[] KKSAngle = { 0, 45, 90, 135, 180, 225, 270, 315 };

	private static RtdsTimeHelper timeHelper;

	public static StatInfo calc(String[] kks, List<String> kksList) {
		return calc(kks, "211.154.164.67", 5080, 20883, kksList);
	}

	public static void ajustInKks(int num) {
		if (num == 4) {
			KKS_LEN = 4;
			KKSAngle = new int[] { 0, 90, 180, 270 };
		}

		if (num == 8) {
			KKS_LEN = 8;
			KKSAngle = new int[] { 0, 45, 90, 135, 180, 225, 270, 315 };
		}
	}

	public static StatInfo calc(String[] kks, String host, int port, int dubboPort) {
		return calc(kks, host, port, dubboPort, null);
	}

	public static StatInfo calc(String[] kks, String host, int port, int dubboPort, List<String> kksList) {

		if (kks == null || !(kks.length == 8 || kks.length == 4)) {
			return new StatInfo(Constant.KKS_ISNULL);
		}

		if (kks.length == 4) {
			ajustInKks(4);
		}

		try {
			// System.out.println(kksList);
			List<RTDataSet> rtdsList = RestDataloader.getSnapshot(kks, host, port);

			// 时间相关校验处理
			if (timeHelper == null) {
				timeHelper = new RtdsTimeHelper(rtdsList);
			} else {
				timeHelper.check(rtdsList);
			}

			if (!timeHelper.isValid()) {
				int errorNo = timeHelper.getErrorNo();
				if (errorNo == Constant.INVALID_TIME_DIFF) {
					rtdsList = RestDataloader.getRTDataHistory(KdmUtils.arr2str(kks), timeHelper.getMinTime(),
							timeHelper.getMinTime(), host, port);
					timeHelper.check(rtdsList);
				}
			}

			if (!timeHelper.isValid()) {
				return new StatInfo(timeHelper.getErrorNo());
			}

			// 数据排序
			if (kks != null) {
				rtdsList = KdmUtils.sortRtds(rtdsList, kks);
			}

			MasterModel[] masterModels = OgeAlgUtils.getMasterModels(rtdsList);
			AirGapOutput result = cacu(masterModels);
			if (result == null) {
				System.out.println("no airgap result -------->");
				return new StatInfo(Constant.ALG_RESULT_ISNULL);
			}

			// 不进行写库动作
			if (kksList == null) {
				System.out.println(JSONArray.fromObject(result));
				return new StatInfo(Constant.CALC_SUCCESS);
			}

			// 包装结果
			List<RTDataSet> rtDataSetList = PersistBuild.buildRTDataSet(result, kksList, timeHelper.getMinTime());
			// 保存
			boolean bSaveSucc = saveResult(rtDataSetList, host, dubboPort);
			if (!bSaveSucc) {
				return null;
			}

			System.out.println("本次写入数据成功！");
			System.out.println("calc airgap ok -------->");
			showMessage(result);

			return new StatInfo(Constant.WRITE_SUCCESS);
		} catch (Exception e) {
			// TODO log
			e.printStackTrace();
		}
		System.out.println("calc airgap [error] -------->");
		return new StatInfo(Constant.CACL_ERROR);
	}

	public static KdmDubboApi kdmDubboApi;

	public static boolean saveResult(List<RTDataSet> rtDataSetList, String host, int dubboPort) {
		if (kdmDubboApi == null) {
			String dubboUrl = "dubbo://" + host + ":" + dubboPort;
			kdmDubboApi = new KdmDubboApi(dubboUrl);
		}
		// TODO 分析结果是否成功
		kdmDubboApi.writeRtds(rtDataSetList);
		return true;
	}

	private static AirGapCircleAlogrithm airGapCircleAlogrithm = new AirGapCircleAlogrithm();

	public static AirGapOutput cacu(MasterModel[] _masters) throws Exception {

		MasterModel[] master = _masters;
		AirGapInputR one = new AirGapInputR();

		for (int i = 0; i < KKSAngle.length; i++) {
			one.add(new AirGapInputOne(master[i], KKSAngle[i]));
		}

		CacuGap gap = new CacuGap();
		float[][] gapResults = new float[KKS_LEN][];

		int nullCount = 0;
		for (int i = 0; i < KKS_LEN; i++) {
			float[] gapresult = gap.cacu(master[i]); // 返回了 最后的气隙，个数是 磁极个数 。
			gapResults[i] = gapresult;
			if (gapresult == null) {
				nullCount++;
				continue;
			}

			// 排除都是零的情况
			int zoreCount = gapresult.length;
			for (int j = 0; j < gapresult.length; j++) {
				if (gapresult[j] == 0.0) {
					zoreCount--;
				}
			}
			if (zoreCount == 0) {
				nullCount++;
			}
			// 当14个磁极时
			// [
			// [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			// 0.0],
			// [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			// 0.0],
			// [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			// 0.0],
			// [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			// 0.0]
			// ]
		}

		if (nullCount != 0) {
			// 传感器错误
			System.out.println("error-1 " + nullCount + "sensor error");
			return null;
		}

		// 下面根据气隙 计算其他的内容
		AirGapOutput result = airGapCircleAlogrithm.execute(KKSAngle, gapResults);

		if (result == null) {
			System.out.println("error-2 所有传感器错误");
			return null;
		}

		return result;
	}

	public static void showMessage(AirGapOutput result) {

		for (AngleAndFloat dp : result.getAvrGap()) {
			dp.print("每个传感器的平均气隙");
		}
		for (AngleAndFloat dp : result.getDistance()) {
			dp.print("定转子距离偏差");
		}

		// result.getErrorOne(); //异常传感器。

		for (float a1 : result.getLengthAvr()) {
			System.out.print("每个磁极的伸长偏差:" + a1);
		}
		System.out.println("");

		for (AngleAndFloatArray dp : result.getMaxMin()) {
			dp.print("最大最小气隙");
		}
		for (AngleAndFloat dp : result.getRotor()) {
			dp.print("转子不圆度");
		}

		System.out.println("转子不圆度(均值):" + result.getRotorAv());
		System.out.println("定子不圆度:" + result.getStator());

	}

}

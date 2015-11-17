package cn.oge.sci;

import java.util.ArrayList;
import java.util.List;

import org.oge.airgapcacu.output.AirGapOutput;
import org.oge.airgapcacu.output.AngleAndFloat;
import org.oge.airgapcacu.output.AngleAndFloatArray;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;

public class PersistBuild {
	/**
	 * 多值
	 */
	public static List<RTDataSet> buildRTDataSet(AirGapOutput algResult,
			List<String> kksList, Long time) {
		AirGapOutput result = algResult;
		Long sTime = time;
		List<RTDataSet> rtdsList = new ArrayList<RTDataSet>();

		int index = 0;
		/** 每个传感器的平均气隙 */
		AngleAndFloat[] avrGap = result.getAvrGap();
		if (avrGap != null) {
			// 8
			for (int i = 0; i < avrGap.length; i++) {
				rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
						avrGap[i]));
			}
		}

		/** 最大最小气隙 */
		AngleAndFloatArray[] maxMinGap = result.getMaxMin();
		if (maxMinGap != null) {
			// 16
			for (int i = 0; i < maxMinGap.length; i++) {
				AngleAndFloatArray aafa = maxMinGap[i];

				float[] value = aafa.getWave();

				rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
						value[0]));
				rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
						value[1]));
			}
		}

		/** 定转子距离偏差 */
		AngleAndFloat[] distance = result.getDistance();
		if (distance != null) {
			// 8
			for (int i = 0; i < distance.length; i++) {
				rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
						distance[i]));
			}
		}

		/** 定子不圆度 */
		// 1
		rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
				result.getStator()));

		/** 转子不圆度 */
		// 1
		rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
				result.getRotorAv()));

		// result.getErrorOne(); //异常传感器。
		/** 每个磁极的伸长偏差 */
		float[] lengthAvr = result.getLengthAvr();
		if(lengthAvr.length < kksList.size()){
			for (int i = 0; i < lengthAvr.length; i++) {
				// 90
				rtdsList.add(aaf2RTDataSet(kksList.get(index++), sTime,
						lengthAvr[i]));
			}
		}

		return rtdsList;
	}

	private static RTDataSet aaf2RTDataSet(String code, Long time,
			AngleAndFloat aaf) {
		return aaf2RTDataSet(code, time, aaf.getWave());
	}

	private static RTDataSet aaf2RTDataSet(String code, long time, float value) {

		String kksCode = code;
		List<RTValue> rtvList = new ArrayList<RTValue>();
		// String tagId = kksCode;
		rtvList.add(new RTValue(time, value));
		return new RTDataSet(kksCode, rtvList);
	}
}

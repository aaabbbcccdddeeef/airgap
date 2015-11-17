package cn.oge.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestArrayAndList {

	@Test
	public void test() {
		String[] arr = new String[] { "str1", "str2" };
		List<String> list = Arrays.asList(arr);
		for (String item : list) {
			System.out.println(item);
		}

		List<String> list2 = new ArrayList<String>();
		list2.addAll(Arrays.asList(arr));
		for (String item : list2) {
			System.out.println(item);
		}
	}

}

package cn.oge.java;

import org.junit.Test;

public class TestByte {

	@Test
	public void test() {
		System.out.println(Byte.parseByte("127"));
		System.out.println(Byte.parseByte("128",10));
		//System.out.println(Byte.parseByte("242"));
		System.out.println(Byte.parseByte("242",10));
	}

}

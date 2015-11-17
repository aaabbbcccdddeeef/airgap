package cn.oge.java;

public class TestBean {
	public static void main(String[] args) {
		MyBean bean = new MyBean("MyGod");
		String beanName = bean.getName();
		beanName = "Ladeng";
		System.out.println(bean.getName());
		System.out.println(beanName);
	}
}

class MyBean {
	private String name;

	public MyBean(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
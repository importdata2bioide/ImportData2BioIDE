package learnNote;
/**
 * 
 * @Description 各种类型的变量在jvm中的保存位置
 * @author ZXY
 */
public class VariableSave {
	static int k = 100;//类变量（类里面static修饰的变量）保存在“方法区”
	final int h = 200;//常量（在java语言中，定义常量必须加上final关键词）保存在“方法区”；
	int g = 300;//实例变量（类里面的普通变量）保存在“堆”
	
	public int sum() {
		int a = 400;//局部变量（方法里声明的变量）“虚拟机栈”
		int b = 500;//局部变量（方法里声明的变量）“虚拟机栈”
		return a+b;
	}
	
//	结论：
//	方法区和堆是线程共享的；虚拟机栈是线程私有的；
//	因此类变量，常量和实例变量是线程共享的，而局部变量是线程私有的；
//	而线程共享的变量一定会产生线程安全的问题；
//	如何解决线程安全问题呢？可以使用threadlocal
}

package org.yoara.framework.core.util.math;

import java.math.BigDecimal;

/**============================================================<br>
 <p>* @version 1.0.0</p>
 <p>* 数字计算类 </p>
 * ============================================================*/
public class MathUtil {

	/**
	 * 浮点数转换成整数，四舍五入<br/>
	 * 55.51->56 55.490->55
	 */
	public static double formatPrice(double price) {
		return Math.round(price);
	}
	/**
	 *
	 * 功能说明：浮点类型四舍五入
	 * @param doubleMath
	 * @param precision 精度
	 */
	public static double rounding(double doubleMath,int precision){
		BigDecimal b = new BigDecimal(doubleMath);
		BigDecimal bigDecimal = b.setScale(precision, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
	}
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double doubleDiv(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double doubleMul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();

	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double doubleSubtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @return 两个参数的和
	 */
	public static double doubleAdd(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**浮点数以5为单位向上靠<br/>
	 * 50.01->55，50.49->55，55.01->60
	 *
	 * @return 上靠的5的倍数
	 */
	public static long doubleToUpperLong(double d1) {
		int L_10 = (int)d1/10;
		int L_1 = 0;
		double D_1  = d1%10;
		if(D_1==0.0){
			L_1 = 0;
		}else if(D_1<=5.0){
			L_1 = 5;
		}else if(5.0<D_1){
			L_1 = 10;
		}
		return L_10*10+L_1;
	}

	/**差值进位算法<br/>
	 *
	 * 3< x <7，则x进5 <br/>
	 * x < 3，则x进0 <br/>
	 * x > 7，则x进10
	 * **/
	public static long longToFive(long l1) {
		long t = l1%10;
		if(t>=0){
			if(t<3){
				t = 0;
			}else if(3<=t&&t<=7){
				t = 5;
			}else if(7<t){
				t = 10;
			}
		}else{
			if(t>-3){
				t = 0;
			}else if(-3>=t&&t>=-7){
				t = -5;
			}else if(-7>t){
				t = -10;
			}
		}
		return l1/10*10+t;
	}
}

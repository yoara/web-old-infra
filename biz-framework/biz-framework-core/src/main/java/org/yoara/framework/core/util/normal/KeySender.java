package org.yoara.framework.core.util.normal;

import java.awt.*;
import java.awt.event.KeyEvent;

/**============================================================<br>
 <p>* @version 1.0.0</p>
 <p>* 模拟键盘输出至光标处 </p>
 * ============================================================*/
public class KeySender {
	private Robot robot = null;

	public KeySender() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**向光标处发送模拟输出
	 * @param keySqe 输出字符串
	 * @param waitingSendTime 延迟发送时间
	 * @param sendStartIndex 输出的启始位置
	 *  **/
	public void keySend(byte[] keySqe,long waitingSendTime,int sendStartIndex) throws Exception {
		Thread.sleep(waitingSendTime);
		for(int i = sendStartIndex;i<keySqe.length;i++){
			robot.keyPress(keySqe[i]);
		}
		robot.keyPress(KeyEvent.VK_ENTER);
	}
}

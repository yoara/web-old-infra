package org.yoara.framework.component.notify.core.mail;

import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yoara on 2016/7/11.
 */
public class MailSenderPool implements InitializingBean{
    //工厂路由key
    private String senderKey;
    //单个实体对象Bean配置的
    private List<MailSender> mailSenderList;

    private final static Map<String,MailSenderPool>mailSenderFactory = new HashMap<>();

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public void setMailSenderList(List<MailSender> mailSenderList) {
        this.mailSenderList = mailSenderList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        synchronized (mailSenderFactory){
            if(senderKey==null){
                throw new IllegalArgumentException("mail sender [senderKey] cannot be null!");
            }
            if(mailSenderList==null||mailSenderList.size()==0){
                throw new IllegalArgumentException("mail sender:["+senderKey+"] cannot be empty!");
            }
            if(mailSenderFactory.get(senderKey)!=null){
                throw new IllegalArgumentException("mail sender:["+senderKey+"] has been Initialized!");
            }
            mailSenderFactory.put(senderKey,this);
        }
    }

    public static MailSenderPool getMailSenderList(String senderKey) {
        MailSenderPool pool = mailSenderFactory.get(senderKey);
        if(pool==null){
            throw new RuntimeException("no mail sender found");
        }
        return pool;
    }

    public List<MailSender> getMailSenderList() {
        return mailSenderList;
    }
}

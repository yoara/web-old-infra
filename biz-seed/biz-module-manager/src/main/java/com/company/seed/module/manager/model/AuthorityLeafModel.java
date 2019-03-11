package com.company.seed.module.manager.model;

import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationNodeEnums;

import java.io.Serializable;

/**
 * Created by yoara on 2016/4/25.
 * 权限叶子模型
 */
public class AuthorityLeafModel implements Serializable{
    private AuthorityAnnotationEnums definition;
    private String name;
    private String nodeName;
    private AuthorityAnnotationNodeEnums nodeDefinition;
    private int order;
    private boolean isMenu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public AuthorityAnnotationEnums getDefinition() {
        return definition;
    }

    public void setDefinition(AuthorityAnnotationEnums definition) {
        this.definition = definition;
    }

    public AuthorityAnnotationNodeEnums getNodeDefinition() {
        return nodeDefinition;
    }

    public void setNodeDefinition(AuthorityAnnotationNodeEnums nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setIsMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    @Override
    public String toString() {
        return "AuthorityLeafModel{" +
                "definition=" + definition +
                ", name='" + name + '\'' +
                ", nodeDefinition=" + nodeDefinition +
                '}';
    }
}

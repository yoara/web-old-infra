package org.yoara.framework.core.util.normal;

import org.apache.commons.lang.StringUtils;
import org.yoara.framework.core.util.CommonDateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by yoara on 2016/7/12.
 */
public class SQLModelMapHelper {
    //包名
    private String packageName;
    //用户名
    private String username;
    //密码
    private String password;
    //链接地址
    private String url;
    //数据库名
    private String dbName;
    //表名
    private String tableName;
    //字段前缀
    private String columnPrefix;
    //驱动
    private String driver = "com.mysql.jdbc.Driver";
    //对象名，以此生成对象、service、dao、xml的通用名
    private String objectName;
    private String objectNameSmall;
    //模块名
    private String module;
    //通用简单描述前缀
    private String desc;
    //_下划线驼峰处理
    private boolean underLineToCamelCase;

    /**
     * @param username     数据库用户名
     * @param password     数据库密码
     * @param url          数据库连接
     * @param dbName       数据库名
     * @param tableName    表名
     * @param columnPrefix 表字段前缀，如fk，FK等
     * @param packageName  包名
     * @param objectName   Model类名
     * @param module       模块名，用于包名前缀
     * @param desc         描述前缀
     */
    public SQLModelMapHelper(String username,
                             String password, String url, String dbName, String tableName,
                             String columnPrefix, String packageName,
                             String objectName, String module, String desc) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.dbName = dbName;
        this.tableName = tableName;
        this.columnPrefix = columnPrefix;
        this.packageName = packageName;
        this.objectName = objectName;
        this.module = module;
        this.desc = desc;
        objectNameSmall = (objectName.charAt(0) + "").toLowerCase() + objectName.substring(1);
    }

    public void doMapper() throws IOException {
        System.out.println(toString());
        System.out.println("开始获得数据库连接...");
        Connection conn = getJdbcConnect(username, password, url);
        System.out.println("获得数据库连接...DONE");
        System.out.println("开始获得表结构信息...");
        List<SqlModelDef> defs = getAllSchemeInfo(conn);
        System.out.println("获得表结构信息...DONE");


        System.out.println("开始生成model类...");
        //生成对象文件
        makeObject(defs);
        System.out.println("生成model类...DONE");
        System.out.println("");

        System.out.println("开始生成service类...");
        //生成Service
        makeService();
        System.out.println("生成service类...DONE");
        System.out.println("");

        System.out.println("开始生成dao类...");
        //生成Dao
        makeDao();
        System.out.println("生成dao类...DONE");
        System.out.println("");

        System.out.println("开始生成xml文件...");
        //生成xml
        makeMyBatisXml(defs);
        System.out.println("生成xml文件...DONE");
        System.out.println("");

        System.out.println("关闭数据库连接...");
        closeConnect(conn);
        System.out.println("关闭数据库连接...DONE");
        System.out.println("");
        System.out.println("完成操作...");
        System.out.println("请到以下地址复制文件..." + CONF_DIR_PATH);
    }

    private Connection getJdbcConnect(String username, String password, String url) {
        Connection conn = null;
        try {
            // 第一步，加载数据库驱动
            Class.forName(driver);
            // 第二步，取得数据库连接
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private List<SqlModelDef> getAllSchemeInfo(Connection conn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 第三步，创建PreparedStatement
        String sql = String.format("select * from information_schema.columns " +
                "where TABLE_SCHEMA='%s' and table_name='%s'", dbName, tableName);
        List<SqlModelDef> defs = new ArrayList<>();
        try {
            // PreparedStatement使用预编译功能
            // 不会重复的编译相同的sql语句
            // 采用PreparedStatement能够明显的提升性能
            pstmt = conn.prepareStatement(sql);
            // 第四步，返会结果集
            rs = pstmt.executeQuery();
            // COLUMN_NAME, 列名
            // DATA_TYPE,   类型
            // COLUMN_KEY,  主键
            // COLUMN_COMMENT   注释
            while (rs.next()) {
                SqlModelDef def = new SqlModelDef();
                def.setDataType(rs.getString("DATA_TYPE"));
                def.setColumnName(rs.getString("COLUMN_NAME"));
                def.setColumnKey(rs.getString("COLUMN_KEY"));
                def.setColumnComment(rs.getString("COLUMN_COMMENT"));
                defs.add(def);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeJdbcStatement(pstmt, rs);
        return defs;
    }

    private void closeJdbcStatement(PreparedStatement pstmt, ResultSet rs) {
        // 遵循从里到外关闭的原则
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
            }
        }
    }

    private void closeConnect(Connection conn) {
        // 必须关闭Connection
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }


    //生成POJO对象文件
    private void makeObject(List<SqlModelDef> defs) throws IOException {
        StringBuffer all = new StringBuffer();
        all.append(String.format("package %s.model.%s;\n\n", packageName, module));

        StringBuffer importSb = new StringBuffer();
        StringBuffer fieldSb = new StringBuffer("");
        StringBuffer getSetSb = new StringBuffer("");
        for (int i = 1; i < defs.size(); i++) {
            SqlModelDef dbField = defs.get(i);
            String objectField = getObjectString(dbField);
            String fieldType = getType(dbField.getDataType());
            if (fieldType.equals("BigDecimal")) {
                importSb.append("import java.math.BigDecimal;\n");
            } else if (fieldType.equals("Integer")) {
                importSb.append("import java.lang.Integer;\n");
            } else if (fieldType.equals("Long")) {
                importSb.append("import java.lang.Long;\n");
            } else if (fieldType.equals("Date")) {
                importSb.append("import java.util.Date;\n");
            }
            //field
            fieldSb.append(String.format("\t// %s\n", dbField.getColumnComment()));
            fieldSb.append(String.format("\tprivate %s %s;\n", fieldType, objectField));
            //getSet方法
            String _objectField = (objectField.charAt(0) + "").toUpperCase() + objectField.substring(1);

            getSetSb.append(String.format("\tpublic void set%s(%s %s){\n", _objectField, fieldType, objectField));
            getSetSb.append(String.format("\t\tthis.%s = %s;\n\t}\n\n", objectField, objectField));
            getSetSb.append(String.format("\tpublic %s get%s(){\n", fieldType, _objectField));
            getSetSb.append(String.format("\t\treturn %s;\n\t}\n\n", objectField));
        }

        importSb.append("import com.company.model.Entity;\n");
        importSb.append("import java.util.*;\n\n");
        importSb.append(String.format("%spublic class %sModel extends Entity<String> {\n",
                getClassDesc("Model类"), objectName));

        all.append(importSb);
        all.append(fieldSb).append("\n");
        all.append(getSetSb);
        all.append("}\n");
        makeFile(all.toString(), "model");
    }

    private String getClassDesc(String descMore) {
        return String.format("/**\n * %s\n * Created by %s on %s\n */\n",
                desc + descMore, System.getenv("USERNAME"),
                CommonDateUtil.formatNowToyyyy_MM_dd(new Date()), objectName);
    }

    //生成service层
    private void makeService() throws IOException {
        makeCommonInterface("service", "Service");

        //impl
        StringBuffer all = new StringBuffer();
        all.append(String.format("package %s.service.%s.impl;\n\n", packageName, module));

        StringBuffer importSb = new StringBuffer();
        importSb.append(String.format("import %s.model.%s.%sModel;\n", packageName, module, objectName));
        importSb.append(String.format("import %s.service.%s.%sService;\n", packageName, module, objectName));
        importSb.append(String.format("import %s.dao.%s.%sDao;\n", packageName, module, objectName));
        importSb.append("import javax.annotation.Resource;\n");
        importSb.append("import org.springframework.stereotype.Service;\n\n");
        importSb.append("import org.apache.commons.lang3.StringUtils;\n\n");

        importSb.append(String.format("%s\n@Service\npublic class %sServiceImpl implements %sService{\n"
                , getClassDesc("服务层Service"), objectName, objectName));
        importSb.append("\t@Resource\n");
        importSb.append(String.format("\tprivate %sDao %sDao;\n\n", objectName, objectNameSmall));

        importSb.append("\t@Override\n");
        importSb.append(String.format("\tpublic boolean insert%s(%sModel %s){\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\t\treturn %sDao.insert%s(%s);\n\t}\n\n",
                objectNameSmall, objectName, objectNameSmall));

        importSb.append("\t@Override\n");
        importSb.append(String.format("\tpublic boolean update%s(%sModel %s){\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\t\treturn %sDao.update%s(%s);\n\t}\n\n",
                objectNameSmall, objectName, objectNameSmall));

        importSb.append("\t@Override\n");
        importSb.append(String.format("\tpublic boolean save%s(%sModel %s){\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\t\tif(StringUtils.isEmpty(%s.getId())){" +
                        "\n\t\t\treturn %sDao.insert%s(%s);\n\t\t}else{" +
                        "\n\t\t\treturn %sDao.update%s(%s);\n\t\t}\n\t}\n\n",
                objectNameSmall, objectNameSmall, objectName, objectNameSmall,
                objectNameSmall, objectName, objectNameSmall));

        all.append(importSb);
        all.append("}\n");
        makeFile(all.toString(), "serviceImpl");
    }

    //生成dao层
    private void makeDao() throws IOException {
        makeCommonInterface("dao", "Dao");
        //impl
        StringBuffer all = new StringBuffer();
        all.append(String.format("package %s.dao.%s.impl;\n\n", packageName, module));

        StringBuffer importSb = new StringBuffer();
        importSb.append("import com.company.dao.basic.mybatis.BaseDaoImpl;\n");
        importSb.append("import org.springframework.stereotype.Repository;\n");
        importSb.append(String.format("import %s.dao.%s.%sDao;\n", packageName, module, objectName));
        importSb.append(String.format("import %s.model.%s.%sModel;\n", packageName, module, objectName));
        importSb.append(String.format("%s\n@Repository\npublic class %sDaoMysqlImpl extends BaseDaoImpl implements %sDao{\n",
                getClassDesc("持久层Dao"), objectName, objectName));

        importSb.append(String.format("\tprivate String SQL_PREFIX = \"%s.dao.%s.%sDaoMysql.\";\n\n"
                , packageName, module, objectName));

        importSb.append("\t@Override\n");
        importSb.append(String.format("\tpublic boolean insert%s(%sModel %s){\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\t\tinsert(SQL_PREFIX + \"insert%s\", %s);\n\t\treturn true;\n\t}\n\n"
                , objectName, objectNameSmall));

        importSb.append("\t@Override\n");
        importSb.append(String.format("\tpublic boolean update%s(%sModel %s){\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\t\tupdate(SQL_PREFIX + \"update%s\", %s);\n\t\treturn true;\n\t}\n\n"
                , objectName, objectNameSmall));

        all.append(importSb);
        all.append("}\n");
        makeFile(all.toString(), "daoImpl");

    }

    private void makeCommonInterface(String type, String typeBig) throws IOException {
        StringBuffer all = new StringBuffer();

        all.append(String.format("package %s.%s.%s;\n\n", packageName, type, module));

        StringBuffer importSb = new StringBuffer();
        importSb.append(String.format("import %s.model.%s.%sModel;\n\n", packageName, module, objectName));
        importSb.append(String.format("%spublic interface %s%s{\n",
                getClassDesc("服务层Service"), objectName, typeBig));
        importSb.append(String.format("\tboolean insert%s(%sModel %s);\n\n",
                objectName, objectName, objectNameSmall));
        importSb.append(String.format("\tboolean update%s(%sModel %s);\n\n",
                objectName, objectName, objectNameSmall));
        if ("service".equals(type)) {
            importSb.append(String.format("\tboolean save%s(%sModel %s);\n\n",
                    objectName, objectName, objectNameSmall));
        }
        all.append(importSb);
        all.append("}\n");
        makeFile(all.toString(), type);
    }

    //生成xml
    private void makeMyBatisXml(List<SqlModelDef> defs) throws IOException {
        StringBuffer all = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \n" +
                "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n");
        StringBuffer sbMap = new StringBuffer(String.format("<mapper namespace=\"%s.dao.%s.%sDaoMysql\">\n",
                packageName, module, objectName));
        sbMap.append(String.format("\t<resultMap id=\"%sResult\" type=\"%s\">\n",
                (objectName.charAt(0) + "").toLowerCase() + objectName.substring(1)
                , objectName));

        StringBuffer sbInsert = new StringBuffer(
                String.format("\t<insert id=\"insert%s\" parameterType=\"%s\">\n", objectName, objectName));
        sbInsert.append(String.format("\t\tinsert into %s.%s(", dbName, tableName));
        StringBuffer sbInsertValues = new StringBuffer(" values(");

        StringBuffer sbUpdate = new StringBuffer(
                String.format("\t<update id=\"update%s\" parameterType=\"%s\">\n", objectName, objectName));
        sbUpdate.append(String.format("\t\tupdate %s.%s\n\t\t<set>\n", dbName, tableName));
        for (int i = 0; i < defs.size(); i++) {
            SqlModelDef dbField = defs.get(i);
            String objectField = getObjectString(dbField);
            if ("PRI".equals(dbField.getColumnKey())) {
                sbMap.append(String.format("\t\t<id property=\"%s\" column=\"%s\"/>\n", objectField, dbField.getColumnName()));
            } else {
                sbMap.append(String.format("\t\t<result property=\"%s\" column=\"%s\"/>\n", objectField, dbField.getColumnName()));
            }
            sbInsert.append(dbField.getColumnName()).append(",");
            sbInsertValues.append("#{").append(objectField).append("},");
            if (!"PRI".equals(dbField.getColumnKey())) {
                sbUpdate.append(String.format("\t\t\t<if test=\"%s!=null\">%s=#{%s},</if>\n",
                        objectField, dbField.getColumnName(), objectField));
            }
        }
        sbMap.append("\t</resultMap>\n\n");

        sbInsert.setCharAt(sbInsert.length() - 1, ')');
        sbInsertValues.setCharAt(sbInsertValues.length() - 1, ')');
        sbInsert.append("\n\t\t\t").append(sbInsertValues).append("\n");
        sbInsert.append("\t</insert>\n\n");

        sbUpdate.append("\t\t</set>\n\t\twhere fid=#{id}\n");
        sbUpdate.append("\t</update>\n");
        all.append(sbMap).append(sbInsert).append(sbUpdate).append("\n</mapper>");
        makeFile(all.toString(), "xml");
    }

    private String getObjectString(SqlModelDef dbField) {
        String objectField = dbField.getColumnName();
        if (!StringUtils.isEmpty(columnPrefix)) {
            objectField = objectField.substring(columnPrefix.length());
        }
        objectField = (objectField.charAt(0) + "").toLowerCase() + objectField.substring(1);
        if (underLineToCamelCase) {
            String[] underLineWords = objectField.split("_");
            if (underLineWords.length > 1) {
                StringBuffer sbuffer = new StringBuffer();
                sbuffer.append(underLineWords[0]);
                for (int wordIndex = 1; wordIndex < underLineWords.length; wordIndex++) {
                    sbuffer.append((underLineWords[wordIndex].charAt(0) + "").toUpperCase() +
                            underLineWords[wordIndex].substring(1));
                }
                objectField = sbuffer.toString();
            }
        }
        return objectField;
    }

    public final static String CONF_DIR_PATH =
            System.getProperty("user.home") + File.separator + "com.company.autocode";

    //文件生成
    private void makeFile(String content, String type) throws IOException {
        File root = new File(CONF_DIR_PATH);
        if (!root.exists()) {
            root.mkdir();
        }
        switch (type) {
            case "model":
                doMake(content, "model", "Model.java", false);
                break;
            case "service":
                doMake(content, "service", "Service.java", false);
                break;
            case "serviceImpl":
                doMake(content, "service", "ServiceImpl.java", true);
                break;
            case "dao":
                doMake(content, "dao", "Dao.java", false);
                break;
            case "daoImpl":
                doMake(content, "dao", "DaoMysqlImpl.java", true);
                break;
            case "xml":
                doMake(content, "dao", "DaoMysql.xml", false);
                break;
        }
    }

    private void doMake(String content, String dirName, String classPostfix, boolean needImpl) throws IOException {
        String fileDirPath = CONF_DIR_PATH + File.separator + dirName;
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        if (needImpl) {
            fileDirPath = fileDirPath + File.separator + "impl";
            fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
        }
        File file = new File(fileDirPath + File.separator + objectName + classPostfix);
        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8");
        out.write(content);
        out.close();
    }

    //获取类型
    private String getType(String dataType) {
        String type = typeMap.get(dataType.toUpperCase());
        if (type != null) {
            return type;
        }
        return "String";
    }

    private static final Map<String, String> typeMap = new HashMap();

    static {
        typeMap.put("TINYINT", "Integer");
        typeMap.put("SMALLINT", "Integer");
        typeMap.put("MEDIUMINT", "Integer");
        typeMap.put("INT", "Integer");
        typeMap.put("INTEGER", "Integer");
        typeMap.put("BIGINT", "Long");
        typeMap.put("FLOAT", "BigDecimal");
        typeMap.put("DOUBLE", "BigDecimal");
        typeMap.put("DECIMAL", "BigDecimal");

        typeMap.put("DATE", "Date");
        typeMap.put("TIME", "Date");
        typeMap.put("YEAR", "Date");
        typeMap.put("DATETIME", "Date");
        typeMap.put("TIMESTAMP", "Date");

        typeMap.put("CHAR", "String");
        typeMap.put("VARCHAR", "String");
        typeMap.put("TINYBLOB", "String");
        typeMap.put("TINYTEXT", "String");
        typeMap.put("BLOB", "String");
        typeMap.put("TEXT", "String");
        typeMap.put("MEDIUMBLOB", "String");
        typeMap.put("MEDIUMTEXT", "String");
        typeMap.put("LONGBLOB", "String");
        typeMap.put("LONGTEXT", "String");
    }

    //数据结构类
    private class SqlModelDef {
        private String columnName;
        private String dataType;
        private String columnKey;
        private String columnComment;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getColumnKey() {
            return columnKey;
        }

        public void setColumnKey(String columnKey) {
            this.columnKey = columnKey;
        }

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }
    }

    @Override
    public String toString() {
        return "开始生成文件...信息如下：\n" +
                ", 用户名='" + username + "\'\n" +
                ", 密码='" + password + "\'\n" +
                ", 数据库连接='" + url + "\'\n" +
                ", 数据库名='" + dbName + "\'\n" +
                ", 表名='" + tableName + "\'\n" +
                ", 字段前缀='" + columnPrefix + "\'\n" +
                ", 下划线驼峰处理='" + underLineToCamelCase + "\'\n" +
                ", 包名='" + packageName + "\'\n" +
                ", model类名='" + objectName + "\'\n" +
                ", 通用描述='" + desc + "\'\n";
    }

    public void setUnderLineToCamelCase(boolean underLineToCamelCase) {
        this.underLineToCamelCase = underLineToCamelCase;
    }
}

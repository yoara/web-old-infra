package com.company.seed.web.util;

import java.io.*;

/**
 * Created by yoara on 2016/10/24.
 */
public class RenameUtil {
    private static String fileDir = "E:\\biz-seed-master";
    private static String[] oldFilePath = {"biz-","company","com"};
    private static String[] oldFileContext = {"com.company.seed","biz-"};

    /**需修改**/
    private static String newWorkName = "qfang-newhouse";
    private static String[] newFilePath = {"newhouse-","qfang","com"};
    private static String[] newFileContext = {"com.qfang","newhouse-"};


    public static void main(String[] args) throws Exception {
        File rootFileDir = new File(fileDir);

        //创建文件夹
        String newFolderRoot = rootFileDir.getParentFile().getAbsolutePath()+File.separator+newWorkName;
        removeDir(newFolderRoot);
        File newRoot = new File(newFolderRoot);
        newRoot.mkdir();
        iteratorAllFile(rootFileDir,newRoot);

        System.out.println("Done");
    }

    /** 遍历文件夹 **/
    private static void iteratorAllFile(File nowFile,File newFile) throws Exception {
        File[] fs = nowFile.listFiles();
        if(fs!=null&&fs.length>0){
            for(File inFile :fs){
                if(inFile.isDirectory()){
                    File newInFile = new File(newFile.getAbsolutePath()+File.separator+ replacePath(inFile.getName()));
                    newInFile.mkdir();
                    iteratorAllFile(inFile,newInFile);
                }else{
                    File newInFile = new File(newFile.getAbsolutePath()+File.separator+ inFile.getName());
                    newInFile.createNewFile();
                    replaceWord(inFile,newInFile);
                }
            }
        }
    }

    /**包文件夹重命名**/
    private static String replacePath(String name) {
        for(int i=0;i<oldFilePath.length;i++){
            if(name.contains(oldFilePath[i])){
                name = name.replaceAll(oldFilePath[i],newFilePath[i]);
                break;
            }
        }
        return name;
    }

    /** 替换文字 **/
    private static void replaceWord(File inFile,File newInFile) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter(newInFile));

        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            for(int i=0;i<oldFileContext.length;i++){
                tempString = tempString.replaceAll(oldFileContext[i],newFileContext[i]);
            }
            out.write(tempString);
            out.newLine();
        }
        out.close();
    }

    /** 删除文件夹 **/
    public static boolean removeDir(String strDir) {
        File rmDir = new File(strDir);
        if (rmDir.isDirectory() && rmDir.exists()) {
            String[] fileList = rmDir.list();
            for (int i = 0; i < fileList.length; i++) {
                String subFile = strDir + File.separator + fileList[i];
                File tmp = new File(subFile);
                if (tmp.isFile()){
                    tmp.delete();
                }
                if (tmp.isDirectory()){
                    removeDir(subFile);
                }
            }
            rmDir.delete();
        } else {
            return false;
        }
        return true;
    }
}

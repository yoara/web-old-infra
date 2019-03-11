package com.company.seed.basic.web.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.seed.dao.basic.mybatis.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.yoara.framework.component.web.controller.BaseController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yoara on 2016/5/23.
 */
public class ExcelBaseController extends BaseController {
    @Autowired
    private BaseDao baseDao;

    /**
     * 导出excel文件
     * @param key 关联excel
     * @param param 查询参数
     * @param response
     */
    protected void excelExport(String key, Object param, HttpServletResponse response) throws Exception {
        Map<String, Object> excelMap = ExcelUtils.getConfigById(key);
        String fileName = "";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet");
        if (excelMap != null) {
            List<Map<String, Object>> columns = (List<Map<String, Object>>) excelMap.get(ExcelUtils.CONFIG_EXCEL_COLUMNS);
            String sqlMapsId = (String) excelMap.get(ExcelUtils.CONFIG_EXCEL_SQL_MAPS_ID);
            fileName = (String) excelMap.get(ExcelUtils.CONFIG_EXCEL_FILE_NAME);
            List list = baseDao.selectList(sqlMapsId, param);
            JSONArray array = beanToJSON(list);
            createHeader(sheet, columns);
            createBook(workbook, sheet, array, columns);
        }
        exportExcel(workbook, response, fileName);
    }

    /**
     * 转换成JSONARRAY
     */
    protected JSONArray beanToJSON(List<?> list) {
        JSONArray array = new JSONArray();
        for (Object obj : list) {
            array.add(JSONObject.parseObject(JSON.toJSONString(obj)));
        }
        return array;
    }

    /**
     * 构建表头
     */
    private void createHeader(HSSFSheet sheet, List<Map<String, Object>> columns) {
        HSSFRow row = sheet.createRow(0);
        for(int index=0;index<columns.size();index++){
            Map<String, Object> column = columns.get(index);
            HSSFCell cell = row.createCell(index);
            cell.setCellValue((String) column.get(ExcelUtils.CONFIG_COLUMN_HEAD));
            sheet.setColumnWidth(index, column.get("width") == null
                    ? 200 : new BigDecimal((String) column.get("width")).intValue() * 36);
        }
    }

    /**
     * 构建数据表格
     */
    private void createBook(HSSFWorkbook workbook, HSSFSheet sheet,
                            JSONArray array, List<Map<String, Object>> columns) {
        if(array==null){
            return;
        }
        //数据格式化
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < array.size(); i++) {
            JSONObject sqlValue = array.getJSONObject(i);
            HSSFRow row = sheet.createRow(i + 1);
            for(int index=0;index<columns.size();index++){
                Map<String, Object> column = columns.get(index);
                HSSFCell cell = row.createCell(index);
                try {
                    String value = sqlValue.getString((String)column.get(ExcelUtils.CONFIG_COLUMN_KEY));
                    if (value == null || value.equals("") || value.equals("{}")) {
                        value = "";
                    }
                    String dataType = (String) column.get(ExcelUtils.CONFIG_COLUMN_DATA_TYPE);
                    if(StringUtils.isNotEmpty(dataType)){
                        if(ExcelUtils.CONFIG_COLUMN_DATA_TYPE_NUMBER.equals(dataType)){
                            cell.setCellStyle(cellStyle);
                            cell.setCellValue(Double.valueOf(value));
                        }else{
                            cell.setCellValue(value);
                        }
                    }
                } catch (Exception e) {
                    cell.setCellValue("");
                }
            }
        }
    }

    /**
     * 导出excel
     */
    private void exportExcel(HSSFWorkbook workbook, HttpServletResponse response, String fileName) throws Exception {
        String exportFileName = StringUtils.isEmpty(fileName)?"电子表格.xls":fileName + ".xls";
        response.setHeader("Content-disposition", "attachment; filename = " + exportFileName);
        OutputStream stream = response.getOutputStream();
        workbook.write(stream);
        stream.close();
    }
}

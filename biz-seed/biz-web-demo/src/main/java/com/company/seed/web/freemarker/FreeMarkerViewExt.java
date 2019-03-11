//package com.company.seed.web.freemarker;
//
//import freemarker.ext.servlet.FreemarkerServlet;
//import freemarker.ext.servlet.IncludePage;
//import freemarker.template.SimpleHash;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Map;
//
//public class FreeMarkerViewExt extends FreeMarkerView {
//	@Override
//	protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
//		//方便在模板文件中应用${ctx}
//		model.put("ctx", request.getContextPath());
//		//方便在模板文件中应用${base} 默认情况下指向林路径/themes/default
//		model.put("base", request.getContextPath() + "/themes/default");
//		model.put(FreemarkerServlet.KEY_INCLUDE, new IncludePage(request, response));
//		return super.buildTemplateModel(model, request, response);
//	}
//
////   public CityEnum getCity() {
////        String dataSource = ContextHolder.getDataSource();
////        if(dataSource == null) return CityEnum.SHENZHEN;
////        return CityEnum.valueOf(dataSource.toUpperCase());
////    }
//
//}

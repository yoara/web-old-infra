/**
 * Created by Administrator on 2016/8/3.
 */
Package("com.company.manager");
com.company.manager = {
    init: function(){
        this.initFun.initAlias();
        this.initEvent();
        this.initInfo();
    },
    initInfo: function(){
        _cpp._getPosition();
        _cpp._getManager();
        _cpp._getAuthority();
    },
    initEvent: function(){
        $("#position").on("click","input[key=editPosition]", function(){
            $("#result").text("");
            var jli = $(this).parent();
            var data = {
                id:jli.attr("pid"),
                name:jli.find("input:eq(0)").val(),
                status:jli.find("input:eq(1)").is(':checked')
            }
            $.ajax({
                url: ctx + "/security/authority/positionmodify",
                data:data,
                dataType:"json",
                success: function (data) {
                    if(data.status=='C0000'){
                        window.location.reload();
                    }else{
                        $("#result").text(data.message);
                    }
                }
            });
        });
        $("#manager").on("click","input[key=editManager]", function(){
            $("#result").text("");
            var jli = $(this).parent();
            var data = {
                id:jli.attr("mid"),
                name:jli.find("input:eq(0)").val(),
                phoneNumber:jli.find("input:eq(1)").val(),
                status:jli.find("input:eq(2)").is(':checked'),
                positionId:jli.find("select:eq(0)").val(),
            }
            $.ajax({
                url: ctx + "/security/authority/managermodify",
                data:data,
                dataType:"json",
                success: function (data) {
                    if(data.status=='C0000'){
                        window.location.reload();
                    }else{
                        $("#result").text(data.message);
                    }
                }
            });
        });
        $("#authority").on("click","input[key=modifyPa]", function(){
            var check = $(this).is(":checked");
            var jleafli = $(this).parent();
            var jposili = $("#positionPa li.current:first");
            var data = {
                check:check,
                positionId:jposili.attr("pid"),
                authorityId:jleafli.attr("lid")
            }
            $.ajax({
                url: ctx + "/security/authority/modifypa",
                data:data,
                dataType:"json",
                success: function (data) {
                    if(data.status=='C0000'){
                        //window.location.reload();
                    }else{
                        $("#result").text(data.message);
                    }
                }
            });
        });
        $("#positionPa").on("click","a[key=selectPa]",function(){
            $("#authority").find("input[type=checkbox]").removeAttr("checked");

            var paLi = $(this).parent();
            paLi.siblings().removeClass("current");
            paLi.addClass("current");

            var auths = paLi.attr("auth");
            if(auths!=""){
                auths = auths.split(",");
                for(var j=0;j<auths.length;j++){
                    $("#authority").find("li[lid="+auths[j]+"]").find("input:first").prop("checked",true);
                }
            }
        });
        $("#initAuth").on("click",function(){
            $.ajax({
                url: ctx + "/security/authority/initauthority",
                dataType:"json",
                success: function (data) {
                    if(data.status=='C0000'){
                        //window.location.reload();
                    }else{
                        $("#result").text(data.message);
                    }
                }
            });
        });
    },
    initFun: {
        initAlias:function(){
            window._cpp = com.company.manager;
        }
    },
    _getPosition: function(){   //获得岗位信息
        $.ajax({
            url: ctx + "/security/authority/positioninfo",
            dataType:"json",
            async:false,
            success: function (data) {
                if(data.status=='C0000'){
                    for(var i=0;i<data.result.length;i++){
                        var pli = '<li pid="{0}">岗位名：<input value="{1}"/>岗位状态<input type="checkbox" {2}/>'+
                            '<input key="editPosition" type="button" value="编辑"/></li>';
                        pli = pli.replace("{0}",data.result[i].id);
                        pli = pli.replace("{1}",data.result[i].name);
                        pli = pli.replace("{2}",data.result[i].status=="ENABLED"?"checked":"");
                        $("#position").append(pli);
                    }
                }else{
                    $("#result").text(data.message);
                }
            }
        });
    },
    _getManager: function(){  //获得操作人员信息
        $.ajax({
            url: ctx + "/security/authority/managerinfo",
            dataType:"json",
            success: function (data) {
                if(data.status=='C0000'){
                    var positionOption = "";
                    //获得岗位
                    $("#position li:gt(0)").each(function(){
                        var pli = $(this);
                        positionOption+="<option value='{0}'>{1}</option>";
                        positionOption = positionOption.replace("{0}",pli.attr("pid"));
                        positionOption = positionOption.replace("{1}",pli.find("input:eq(0)").val());
                    });
                    $("#manager li:eq(0) select:eq(0)").append(positionOption);
                    for(var i=0;i<data.result.length;i++){
                        var mli = '<li mid={0}>人员名：<input placeholder="默认密码123456" value="{1}"/>' +
                            '人员登录号：<input placeholder="手机号" value="{2}"/>'+
                            '人员状态<input type="checkbox" {3}/>所属岗位：<select key="positionSelect">{4}</select>'+
                            '<input key="editManager" type="button" value="编辑"/></li>';
                        mli = mli.replace("{0}",data.result[i].id);
                        mli = mli.replace("{1}",data.result[i].name);
                        mli = mli.replace("{2}",data.result[i].phoneNumber);
                        mli = mli.replace("{3}",data.result[i].status=="ENABLED"?"checked":"");
                        mli = mli.replace("{4}",positionOption);
                        $("#manager").append(mli);
                        $("#manager li:eq("+(i+1)+")").find("select:eq(0)").val(data.result[i].positionId);
                    }
                }else{
                    $("#result").text(data.message);
                }
            }
        });
    },
    _getAuthority: function(){  //获得权限信息
        $.ajax({
            url: ctx + "/security/authority/authorityinfo",
            dataType:"json",
            success: function (data) {
                if(data.status=='C0000'){
                    //positionPa
                    $("#position li:gt(0)").each(function(){
                        var pli = $(this);
                        var paLi = "<li pid='{0}' auth='{1}'><a href='#' key='selectPa'>{2}<a/></li>";
                        paLi = paLi.replace("{0}",pli.attr("pid"));
                        paLi = paLi.replace("{2}",pli.find("input:eq(0)").val());
                        var auth = '';
                        for(var j=0;j<data.result.listPAndA.length;j++){
                            if(data.result.listPAndA[j].positionId==pli.attr("pid")){
                                auth+=data.result.listPAndA[j].authorityId+",";
                            }
                        }
                        if(auth.length>0){
                            auth = auth.substring(0,auth.length-1);
                        }
                        paLi = paLi.replace("{1}",auth);
                        $("#positionPa").append(paLi);
                    });

                    //authority
                    //遍历权限
                    var currentNode = data.result.listAuth[0].nodeName;
                    var anodeli = "<li>"+currentNode+"<ul>{1}</ul></li>";
                    var aleafLi = "";
                    for(var i=0;i<data.result.listAuth.length;i++){
                        if(currentNode!=data.result.listAuth[i].nodeName){
                            anodeli = anodeli.replace("{1}",aleafLi);
                            $("#authority").append(anodeli);
                            //重置变量
                            currentNode = data.result.listAuth[i].nodeName;
                            anodeli = "<li>{0}<ul>{1}</ul></li>";
                            anodeli = anodeli.replace("{0}",currentNode);
                            aleafLi = "";
                        }
                        aleafLi += "<li lid='"+data.result.listAuth[i].definition+"'>" +
                            "<input key='modifyPa' type='checkbox'/>"
                            +data.result.listAuth[i].name+"</li>";
                    }
                    anodeli = anodeli.replace("{1}",aleafLi);
                    $("#authority").append(anodeli);
                }else{
                    $("#result").text(data.message);
                }
            }
        });
    },
};

$(document).ready(function(){
    com.company.manager.init();
});
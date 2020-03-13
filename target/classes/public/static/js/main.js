function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}
//打开对话框
function openPasswordModifyDialog() {
    $("#dlg").dialog("open").dialog("setTitle","密码修改");
}



//退出
function logout() {
    $.messager.confirm("来自提示","确定退出系统",function (r) {
        $.removeCookie("userIdStr");
        $.removeCookie("userName");
        $.removeCookie("trueName");
        $.messager.alert("系统将在3秒后自动退出","ok")
        setTimeout(function () {
            window.location=ctx+"/index";
        },2000)
    })
}
//修改密码
function modifyPassword() {
    $("#fm").form("submit",{
        url:ctx+"/user/updatePassword",
        onSubmit: function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自CRM面板","密码修改成功,将在5秒后退出","info");
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                setTimeout(function () {
                    window.location.href=ctx+"/index";
                },5000)
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}



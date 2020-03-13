//定义常用变量
function formatterState(value) {
    /*
     *0是未分配
     * 1是已分配
     */
    if(value==0){
        return "未分配"
    }else if(value==1){
        return "已分配"
    }else{
        return "状况位置"
    }
}
//定义常用常量
function formatterDevResult(value) {
    /**
     * 0-未开发
     * 1-开发中
     * 2-开发成功
     * 3-开发失败
     */
    if(value==0){
        return "未开发";
    }else if(value==1){
        return "开发中";
    }else if(value==2){
        return "开发成功";
    }else if(value==3){
        return "开发失败";
    }else {
        return "未知"
    }
}
//定义搜索方法
function searchSaleChance() {
    var customerName=$("#s_customerName").val();
    var createMan=$("#s_createMan").val();
    var state=$("#s_state").combobox("getValue");
    $("#dg").datagrid("load",{
        customerName:customerName,
        createMan:createMan,
        state: state
    });
}
//打开添加弹窗
function openSaleChanceAddDialog() {
    $("#dlg").dialog("open").dialog("setTitle","机会数据添加");
}
//关闭弹窗
function closeSaleChanceDialog() {
    $("#dlg").dialog("close");
}
/**
 * 清空表单信息
 */
function clearFormData() {
    $("#customerName").val("");
    $("#chanceSource").val("");
    $("#linkMan").val("");
    $("#linkPhone").val("");
    $("#cgjl").val("");
    $("#overview").val("");
    $("#assignMan").combobox("setValue","");
}
//保存或更新模板
function saveOrUpdateSaleChance() {
    var url = ctx+"/sale_chance/save";
    if(!(isEmpty($("input[name='id']").val()))){
        url = ctx+"/sale_chance/update";}
    console.log(url);
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data =JSON.parse(data);
            if(data.code==200) {
                closeSaleChanceDialog();
                searchSaleChance();
                clearFormData();
            }
        }
    })
}
//打开修改模板
function openSaleChanceModifyDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的机会数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","机会数据更新");
}
//删除方法
function deleteSaleChance() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的机会数据!","error");
        return;
    }
    $.messager.confirm("来自crm","确定删除选中的记录?",function (r) {
        if(r){var ids= "ids=";
            for(var i=0;i<rows.length;i++){
                if(i<rows.length-1){
                    ids=ids+rows[i].id+"&ids=";
                }else {
                    ids=ids+rows[i].id
                }
            }
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/delete",
                data:ids,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        searchSaleChance();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })
        }
    })
}
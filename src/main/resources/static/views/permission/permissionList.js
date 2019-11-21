var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
	var columns = [
		{
		    field: 'selectItem',
		    radio: true
		},
		{
		    field: 'id',
		    title: 'ID',
		    align: 'center',
		    valign: 'middle',
		    width: '60px'
		},
		{
		    field: 'name',
		    title: '权限名称',
		    align: 'center',
		    valign: 'middle'
		},
		{
		    field: 'icon',
		    title: '图标',
		    align: 'center',
		    valign: 'middle',
		    formatter: function(value, row, index) {
		        return row.icon == null ? '' : '<i class="'+row.icon+' fa-lg"></i>';
		    }
		},
		{
		    field: 'type',
		    title: '类型',
		    align: 'center',
		    valign: 'middle',
		    formatter: function(value, row, index) {
		        if(row.type === 0){
		            return '<span class="label label-primary">目录</span>';
		        }
		        if(row.type === 1){
		            return '<span class="label label-success">菜单</span>';
		        }
		        if(row.type === 2){
		            return '<span class="label label-warning">按钮</span>';
		        }
		    }
		},
		{
		    field: 'sort',
		    title: '排序号',
		    align: 'center',
		    valign: 'middle',
		    sortable: true
		},
		{
		    field: 'url',
		    title: '权限URL',
		    align: 'center',
		    valign: 'middle'
		},
		{
		    field: 'permCode',
		    title: '权限编码',
		    align: 'center',
		    valign: 'middle'
		},
		{
		    field: 'status',
		    title: '状态',
		    align: 'center',
		    valign: 'middle',
		    formatter: function(value, row, index){
		        if (row.status == 0) {
		            return '<span class="label label-danger">禁用</span>';
		        } else if (row.status == 1) {
		            return '<span class="label label-primary">启用</span>';
		        }
		        return "<span>其他</span>";
		    }
		},
		{
		    field: 'gmt_create',
		    title: '创建时间',
		    align: 'center',
		    sortable: true,
		    formatter:function(value, row, index){
		        return formatDateTime(row.gmtCreate);
		    }
		}
    ];
    return columns;
};

$(function () {
   loadPermissionList();
}); 


/**
 * 加载权限列表
 */
function loadPermissionList() {
	var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, "/permission/", colunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pid");
    table.setExpandAll(false);
    table.init();
    Menu.table = table;
}

function getMenuId () {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        parent.layer.msg('请选择一条记录', {time: 1500, icon:5});
        return false;
    } else {
        return selected[0].id;
    }
}

//添加权限
$('#addModal').on('show.bs.modal', function () {
    $("#addForm").data('bootstrapValidator').destroy();
    $('#addForm').data('bootstrapValidator', null);
    addValidator();
});

addValidator();
function addValidator() {
    $('#addForm').bootstrapValidator({
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '权限名称不能为空'
                    }
                }
            },
            icon: {
                validators: {
                    notEmpty: {
                        message: '图标不能为空'
                    },
                    stringLength: {
                        max: 30,
                        message: '图标长度最多15个字符'
                    }
                }
            },
            sort: {
                validators: {
                    notEmpty: {
                        message: '排序不能为空'
                    }
                }
            },
            permCode: {
                validators: {
                    notEmpty: {
                        message: '权限编码不能为空'
                    }
                }
            },
            url: {
                validators: {
                    notEmpty: {
                        message: 'url不能为空'
                    }
                }
            },
            description: {
                validators: {
                    notEmpty: {
                        message: '描述不能为空'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        // Prevent form submission
        e.preventDefault();
        $.ajax({
            url: "/permission/addPermission",
            data:  $("#addForm").serialize(),
            type: "POST",
            dataType: 'json',
            success: function(data){
                $("#addModal").modal('hide');
                if (data.success) {
                    $("#addModal").modal('hide');
                    $("#addForm")[0].reset();
                    $("#addModal .pid").hide();
                    Menu.table.refresh();
                    parent.layer.msg(data.msg, {icon: 1});
                } else {
                    parent.layer.msg(data.msg, {time: 1500, icon:5});
                }
            }
        });
    });
}

//修改权限
$("#updateButton").click(function () {
    var id = getMenuId();
    if (id == false) {
        return false;
    }
    list = [];
    $.ajax({
        async:false,
        url: "/permission/getPermissionById",
        data: {"id": id},
        type: "POST",
        dataType: 'json',
        success: function(data){
            list = data;
        }
    });
    for (var item in list) {
        if (item == 'status' || item == 'type') {
            continue;
        }
        $("#updateModal input[name='"+item+"']").val(list[item]);
    }
    if (list.type == 0) {
        $("#updateModal .pid").hide();
    } else if (list.type == 1) {
        $("#updateModal .pid").show();
        setDomPermistion(0);
    } else if (list.type == 2) {
        $("#updateModal .pid").show();
        setDomPermistion(1);
    }
    $("#updateModal input[name='type'][value="+list.type+"]").prop("checked",true);
    console.log(list.type)
    $("#updateModal input[name='status'][value="+list.status+"]").prop("checked",true);
    $("#updateForm").data('bootstrapValidator').destroy();
    $('#updateForm').data('bootstrapValidator', null);
    updateValidator();
    $("#updateModal").modal("show");
});

updateValidator();
function updateValidator() {
    $('#updateForm').bootstrapValidator({
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '权限名称不能为空'
                    }
                }
            },
            icon: {
                validators: {
                    notEmpty: {
                        message: '图标不能为空'
                    },
                    stringLength: {
                        max: 30,
                        message: '图标长度最多15个字符'
                    }
                }
            },
            sort: {
                validators: {
                    notEmpty: {
                        message: '排序不能为空'
                    }
                }
            },
            permCode: {
                validators: {
                    notEmpty: {
                        message: '权限编码不能为空'
                    }
                }
            },
            url: {
                validators: {
                    notEmpty: {
                        message: 'url不能为空'
                    }
                }
            },
            description: {
                validators: {
                    notEmpty: {
                        message: '描述不能为空'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        $.ajax({
            url: "/permission/updatePermission",
            data:  $("#updateForm").serialize(),
            type: "POST",
            dataType: 'json',
            success: function(data){
                $("#updateModal").modal('hide');
                if (data.success) {
                    $("#updateModal").modal('hide');
                    Menu.table.refresh();
                    parent.layer.msg(data.msg, {icon: 1});
                } else {
                    parent.layer.msg(data.msg, {time: 1500, icon:5});
                }
            }
        });
    });
}

$("#deleteButton").click(function () {
    var id = getMenuId();
    parent.layer.confirm('确定删除选中权限？', {
        btn: ['确定','取消'], //按钮
        shade: false //不显示遮罩
    }, function() {
        $.ajax({
            url: "/permission/deletePermissionRole",
            data:  {"id": id},
            type: "POST",
            dataType: 'json',
            success: function(data){
                if (data.success) {
                    $('#userTableEvents').bootstrapTable('refresh');
                    parent.layer.msg(data.msg, {icon: 1});
                    Menu.table.refresh();
                } else {
                    parent.layer.msg(data.msg, {time: 1500, icon:5});
                }
            }
        });
    });
});

$("#addModal .pid").hide();
$("#addModal #typeRadio input:radio").click(function () {
    if (this.value == 0) {
        $("#addModal .pid").hide();
    } else if (this.value == 1) {
        setDomPermistion(0);
        $("#addModal .pid").show();
    } else if (this.value == 2) {
        setDomPermistion(1);
        $("#addModal .pid").show();
    }
});


$("#updateModal #typeRadio2 input:radio").click(function () {
    if (this.value == 0) {
        $("#updateModal .pid").hide();
    } else if (this.value == 1) {
        setDomPermistion(0);
        $("#updateModal .pid").show();
    } else if (this.value == 2) {
        setDomPermistion(1);
        $("#updateModal .pid").show();
    }
});

function setDomPermistion(type) {
    $("#addModal select, #updateModal select").empty();
    if (type == 0) {
        $.get("/admin/permission/getTypePermission/0", function(result){
            var domOption = "";
            for (var item in result) {
                domOption += "<option value='"+result[item].id+"'>"+result[item].name+"</option>";
            }
            $("#addModal select, #updateModal select").append(domOption);
            $("#updateModal #pid").val(list.pid);
        });
    } else if (type == 1) {
        $.get("/permission/getTypePermission/1", function(result){
            var domOption = "";
            for (var item in result) {
                domOption += "<option value='"+result[item].id+"'>"+result[item].name+"</option>";
            }
            $("#addModal select, #updateModal select").append(domOption);
            $("#updateModal #pid").val(list.pid);
        });
    }
}

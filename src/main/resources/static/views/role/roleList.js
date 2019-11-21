$(function () {
	$("#addUserModal").modal();
	
	$('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
	
	loadRoleList();//加载角色列表
	
	bindAddRoleValidator();//绑定添加角色表单验证
	bindUpdateRoleValidator();//绑定编辑角色表单验证
    bindDeleteRole();//绑定删除角色
    bindAsignPermission();//绑定分配权限
});

/**
 * 加载角色数据
 */
function loadRoleList(){
	 $("#roleTableEvents").bootstrapTable("destroy");//先销毁
	 $("#roleTableEvents").bootstrapTable({
	        url: '/role/',
	        method: 'get',
	        contentType: "application/x-www-form-urlencoded",
	        queryParams: function(params){ //请求服务器时所传的参数
	            return {
	                pageNumber: params.offset,
	                pageSize: params.limit,
	                orderField: params.sort,//排序字段
	         		orderType: params.order//排序方式
	            }
	        },
	        sidePagination:'server',//指定服务器端分页
	        search: false,
	        searchOnEnterKey: true,
	        pagination: true,
	        showRefresh: true,//展示刷新按钮
	        showToggle: true,//展示切换视图男
	        showColumns: true,//展示切换列按钮
	        striped: true,//是否显示行间隔色
	        pagination: true,//启用分页
	        sortable: true,//启用排序
	        sortOrder: "asc",
	        clickToSelect: true,
	        pageNumber:1, //初始化加载第一页，默认第一页
	        pageSize: 10,//每页的记录行数（*）
	        pageList: [1, 5, 10, 25, 50, 100, 'ALL'],//可供选择的每页的行数（*）
	        iconSize: 'outline',
	        toolbar: '#toolbar',
	        icons: {
	            refresh: 'glyphicon-repeat',
	            toggle: 'glyphicon-list-alt',
	            columns: 'glyphicon-list'
	        },
	        columns: [
	            {
	                title:'全选',
	                field:'select',
	                //复选框
	                checkbox:true,
	                width:25,
	                align:'center',
	                valign:'middle'
	            },
	            {
	                field: 'name',
	                title: '角色名',
	                align: 'center',
	                sortable:true
	            },
	            {
	                field: 'description',
	                title: '描述',
	                align: 'center'
	            },
	            {
	                field: 'status',
	                title: '状态',
	                align: 'center',
	                sortable:true,
	                formatter: function(value,row,index){
	                    if (value == 0) {
	                        return '<span class="label label-danger">禁用</span>';
	                    } else if (value == 1) {
	                        return '<span class="label label-primary">启用</span>';
	                    }
	                    else if (value == 5) {
	                        return '<span class="label label-primary">待审核</span>';
	                    }
	                    return "其他";
	                }
	            },
	            {
	                field: 'gmt_create',
	                title: '创建时间',
	                align: 'center',
	                sortable:true,
	                formatter:function(value,row,index){
	                    return formatDateTime(row.gmtCreate);
	                }
	            }
	        ]
	    });
}

/**
 * 绑定添加角色表单验证
 */
function bindAddRoleValidator(){
	addValidator();
	
    $('#addModal').on('show.bs.modal', function () {
    	//清空原本数据
    	$("#addForm")[0].reset();
    	
    	//绑定表单验证
    	$("#addForm").data('bootstrapValidator').destroy();
        $('#addForm').data('bootstrapValidator',null);
        addValidator();
    
    });
	
	function addValidator(){
		$('#addForm').bootstrapValidator({
            fields: {
            	name: {
                    validators: {
                        notEmpty: {
                            message: '角色名不能为空'
                        },
                        stringLength: {
                            max: 15,
                            message: '角色名长度最多15个字符'
                        },
                        remote: {//服务端验证用户名重复
                            url: "/role/checkRoleName",
                            type: "post",
                            data: function(validator){
                            	return {
                            		name : $("#addForm input[name='name']").val()
                            	}
                            },
                            delay : 1000,//间隔1秒
                            message: '用户名已存在'
                        },
                    }
                },
                description: {
                    validators: {
                        notEmpty: {
                            message: '描述不能为空'
                        },
                        stringLength: {
                            max: 30,
                            message: '描述长度最多30个字符'
                        },
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
            loadLayer();
            $.ajax({
                url: "/role/",
                data:  $("#addForm").serializeArray(),
                type: "POST",
                dataType: 'json',
                success: function(data){
                    $("#addModal").modal('hide');
                    $("#addForm")[0].reset();
                    parent.layer.msg(data.msg, {icon: data.code});
                    closeAllLayer();
                    $('#roleTableEvents').bootstrapTable('refresh');
                },
                error: function(){
                	closeAllLayer();
                	parent.layer.msg("请稍候再试");
                }
            });
        });
	}
}

/**
 * 绑定编辑角色表单验证
 */
function bindUpdateRoleValidator(){
	addValidator();
	
	//修改用户
    $("#updateButton").click(function () {
        var list = $('#roleTableEvents').bootstrapTable('getSelections');
        if (list.length <= 0 || list.length > 1) {
            parent.layer.msg('请选择一个要修改的角色', {time: 1500, icon:5});
            return;
        }
        list = list[0];
        for (var item in list) {
            if (item == 'status') {
                continue;
            }
            $("#updateModal input[name='"+item+"']").val(list[item]);
        }
        $("#updateModal input[name='status'][value="+list.status+"]").prop("checked",true);
        $("#updateForm").data('bootstrapValidator').destroy();
        $('#updateForm').data('bootstrapValidator', null);
        addValidator();
        $("#updateModal").modal("show");
    });
	
	function addValidator(){
		//绑定表单验证
	    $('#updateForm').bootstrapValidator({
	        fields: {
	        	name: {
                    validators: {
                        notEmpty: {
                            message: '角色名不能为空'
                        },
                        stringLength: {
                            max: 15,
                            message: '角色名长度最多15个字符'
                        },
                        remote: {//服务端验证用户名重复
                            url: "/role/checkRoleName",
                            type: "post",
                            data: function(validator){
                            	return {
                            		id : $("#updateForm input[name='id']").val(),
                            		name : $("#updateForm input[name='name']").val()
                            	}
                            },
                            delay : 1000,//间隔1秒
                            message: '用户名已存在'
                        },
                    }
                },
                description: {
                    validators: {
                        notEmpty: {
                            message: '描述不能为空'
                        },
                        stringLength: {
                            max: 30,
                            message: '描述长度最多30个字符'
                        },
                    }
                }
	        }
	    }).on('success.form.bv', function(e) {
	        e.preventDefault();
	        loadLayer();
	        $.ajax({
	            url: "/role/",
	            data: $("#updateForm").serializeArray(),
	            type: "PUT",
	            dataType: 'json',
	            success: function(data){
	            	closeAllLayer();
	            	parent.layer.msg(data.msg, {icon: 1});
	                $("#updateModal").modal('hide');
	                $('#roleTableEvents').bootstrapTable('refresh');
	            }
	        });
	    });
	}
}

/**
 * 绑定删除角色
 */
function bindDeleteRole(){
    $("#deleteButton").click(function () {
        var $result = $('#roleTableEvents');
        var list = $result.bootstrapTable('getSelections');
        if (list.length <= 0) {
            parent.layer.msg('请至少选中一个要删除的角色', {time: 1500, icon:5});
            return;
        }
        var id = "";
        for (var item in list) {
            id += list[item].id+",";
        }
        id = id.substring(0,id.length - 1);
        parent.layer.confirm('确定删除选中角色？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	loadLayer();
            $.ajax({
                url: "/role/"+id,
                type: "DELETE",
                dataType: 'json',
                success: function(data){
                	closeAllLayer();
                    parent.layer.msg(data.msg, {icon: data.code});
                    $('#roleTableEvents').bootstrapTable('refresh');
                },
                error: function(){
                	closeAllLayer();
                	layer.msg("请稍候再试");
                }
            });
        });
    });
}

/**
 * 绑定分配权限
 */
function bindAsignPermission(){
	
	 //绑定分配权限按钮事件
	 $("#asignPermissionButton").click(function () {
        var list = $('#roleTableEvents').bootstrapTable('getSelections');
        if (list.length != 1) {
            parent.layer.msg('请选中一个角色', {time: 1500, icon:5});
            return;
        }
        var id = list[0].id; //获取角色id
        
        loadLayer();
        //获取角色权限数据
        $.ajax({
        	url: "/role/"+id+"/permissions",
        	type: "get",
        	success: function(data){
        		loadzTree("permissionTree", "permissionModal", data.data);
        		closeAllLayer();
        	},
        	error: function(){
        		closeAllLayer();
        		layer.alert("获取角色权限数据失败");
        	}
        });
    });
	 
	 
	//绑定分配权限提交事件
    $("#submitButton").click(function () {
    	loadLayer();
    	
    	var list = $('#roleTableEvents').bootstrapTable('getSelections');
        if (list.length != 1) {
            parent.layer.msg('请选中一个角色', {time: 1500, icon:5});
            return;
        }
        var id = list[0].id; //获取角色id
    	var rolePermissiondata = [];
    	var checkedNodes = getzTreeCheckedNodes("permissionTree");
    	
    	if(checkedNodes.length < 1){
    		closeAllLayer();
    		parent.layer.msg('请至少分配一个权限', {time: 1500, icon:5});
            return;
    	}
    	
    	checkedNodes.forEach(function(element, index){
    		rolePermissiondata.push({
    			rid: id,
    			pid: element.id
    		});
    	});
    	
        $.ajax({
            url: "/role/"+id+"/permissions",
            data: JSON.stringify(rolePermissiondata),
            contentType: "application/json; charset=utf-8",
            type: "post",
            dataType: 'json',
            success: function(data){
                closeAllLayer();
                $("#permissionModal").modal('hide');
                parent.layer.msg(data.msg, {icon: data.code});
            },
            error: function(){
            	closeAllLayer();
            	parent.layer.msg("请稍候再试");
            }
        });
    });
}


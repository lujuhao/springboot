$(function () {
	$("#addUserModal").modal();
	
	$('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
	
	bindAddUserValidator();//绑定添加用户表单验证
	bindUpdateUserValidator();//绑定编辑用户表单验证
	
	loadUserList();//加载用户列表
    bindDeleteUser();//绑定删除用户
    bindCheckUser();//绑定审核用户
    bindAsignRole();//绑定分配角色
});

/**
 * 加载用户数据
 */
function loadUserList(){
	 $("#userTableEvents").bootstrapTable("destroy");//先销毁
	 $("#userTableEvents").bootstrapTable({
	        url: '/user/',
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
	                field: 'username',
	                title: '用户名',
	                align: 'center',
	                sortable:true
	            },
	            {
	                field: 'nickname',
	                title: '昵称',
	                align: 'center',
	                sortable:true
	            },
	            {
	                field: 'headImg',
	                title: '头像',
	                align: 'center',
	                formatter: function(value, row, index){//自定义方法，添加操作按钮
	                	return '<img src="'+value+'" class="img-circle" width="50" height="50" alt="用户头像" />'; 
	                } 
	            },
	            {
	                field: 'phone',
	                title: '电话',
	                align: 'center',
	                sortable:true
	            },
	            {
	                field: 'email',
	                title: '邮箱',
	                align: 'center',
	                sortable:true
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
 * 绑定添加用户表单验证
 */
function bindAddUserValidator(){
	addValidator();
	
	//清空原本数据
    $('#addModal').on('show.bs.modal', function () {
        $("#addForm").data('bootstrapValidator').destroy();
        $('#addForm').data('bootstrapValidator', null);
        addValidator();
    });
    
    //绑定表单验证
    function addValidator(){
        $('#addForm').bootstrapValidator({
            fields: {
                username: {
                    validators: {
                        notEmpty: {
                            message: '用户名不能为空'
                        },
                        stringLength: {
                            max: 15,
                            message: '用户名长度最多15个字符'
                        },
                        remote: {//服务端验证用户名重复
                            url: "/user/checkUserName",
                            type: "post",
                            data: function(validator){
                            	return {
                            		userName : $("#addForm input[name='username']").val()
                            	}
                            },
                            delay : 1000,//间隔1秒
                            message: '用户名已存在'
                        },
                    }
                },
                nickname: {
                    validators: {
                        notEmpty: {
                            message: '昵称不能为空'
                        },
                        stringLength: {
                            max: 30,
                            message: '昵称长度最多30个字符'
                        },
                    }
                },
                phone: {
                    validators: {
                        notEmpty: {
                            message: '手机号码不能为空'
                        },
                        regexp: {
                        	regexp :/^1[3456789]\d{9}$/,
                            message: '请输入有效的手机号码'
                        }
                    }
                },
                email: {
                    validators: {
                        notEmpty: {
                            message: '邮箱不能为空'
                        },
                        emailAddress: {
                            message: '请输入有效的邮箱'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        },
                        stringLength: {
                            min: 5,
                            max: 30,
                            message: '密码长度在5到30之间'
                        }
                    }
                },
                confirmPassword: {
                    validators: {
                        notEmpty: {
                            message: '确认密码不能为空'
                        },
                        identical: {
                            field: 'password',
                            message: '两次密码不相同'
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
            loadLayer();
            $.ajax({
                url: "/user/",
                data:  $("#addForm").serialize(),
                type: "POST",
                dataType: 'json',
                success: function(data){
                    $("#addModal").modal('hide');
                    $("#addModal").modal('hide');
                    $("#addForm")[0].reset();
                    closeAllLayer();
                    $('#userTableEvents').bootstrapTable('refresh');
                    parent.layer.msg(data.msg, {icon: data.code});
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
 * 绑定编辑用户表单验证
 */
function bindUpdateUserValidator(){
	addValidator();
	
	//修改用户
    $("#updateButton").click(function () {
        var $result = $('#userTableEvents');
        var list = $result.bootstrapTable('getSelections');
        if (list.length <= 0 || list.length > 1) {
            parent.layer.msg('请选择一个要修改的用户', {time: 1500, icon:5});
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
    
    //绑定表单验证
    function addValidator(){
        $('#updateForm').bootstrapValidator({
            fields: {
                username: {
                    validators: {
                        notEmpty: {
                            message: '用户名不能为空'
                        },
                        stringLength: {
                            max: 15,
                            message: '用户名长度最多15个字符'
                        },
                        remote: {//服务端验证用户名重复
                            url: "/user/checkUserName",
                            type: "post",
                            data: function(validator){
                            	return {
                            		userId : $("#updateForm input[name='id']").val(),
                            		userName : $("#updateForm input[name='username']").val()
                            	}
                            },
                            delay : 1000,//间隔1秒
                            message: '用户名已存在'
                        },
                    }
                },
                nickname: {
                    validators: {
                        notEmpty: {
                            message: '昵称不能为空'
                        },
                        stringLength: {
                            max: 15,
                            message: '昵称长度最多15个字符'
                        }
                    }
                },
                email: {
                    validators: {
                        notEmpty: {
                            message: '邮箱不能为空'
                        },
                        emailAddress: {
                            message: '请输入有效的邮箱'
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
            loadLayer();
            $.ajax({
                url: "/user/",
                data:  $("#updateForm").serialize(),
                type: "PUT",
                dataType: 'json',
                success: function(data){
                    $("#updateModal").modal('hide');
                    $("#updateModal").modal('hide');
                    $('#userTableEvents').bootstrapTable('refresh');
                    closeAllLayer();
                    parent.layer.msg(data.msg, {icon: 1});
                }
            });
        });
    }
}

/**
 * 绑定删除用户
 */
function bindDeleteUser(){
    $("#deleteButton").click(function () {
        var $result = $('#userTableEvents');
        var list = $result.bootstrapTable('getSelections');
        if (list.length <= 0) {
            parent.layer.msg('请至少选中一个要删除的用户', {time: 1500, icon:5});
            return;
        }
        var id = "";
        for (var item in list) {
            id += list[item].id+",";
        }
        id = id.substring(0,id.length - 1);
        parent.layer.confirm('确定删除选中用户？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	loadLayer();
            $.ajax({
                url: "/user/"+id,
                type: "DELETE",
                dataType: 'json',
                success: function(data){
                	$('#userTableEvents').bootstrapTable('refresh');
                	closeAllLayer();
                    parent.layer.msg(data.msg, {icon: data.code});
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
 * 绑定审核用户
 */
function bindCheckUser(){
	 $("#optionUserStatus").click(function () {
	        var $result = $('#userTableEvents');
	        var list = $result.bootstrapTable('getSelections');
	        if (list.length <= 0) {
	            parent.layer.msg('请至少选中一条数据', {time: 1500, icon:5});
	            return;
	        }

	        for (var item in list) {
	            if(list[item].status != 5 ) {
	                parent.layer.msg('请选择待审核用户', {time: 1500, icon:5});
	                return;
	            }
	        }

	        $.ajax({
	            url: "/admin/user/optionUserStatus",
	            data:  JSON.stringify(list),
	            contentType: "application/json; charset=utf-8",
	            type: "POST",
	            dataType: 'json',
	            success: function(data){
	                if (data.success) {
	                    $('#userTableEvents').bootstrapTable('refresh');
	                    parent.layer.msg(data.msg, {icon: 1});
	                } else {
	                    parent.layer.msg(data.msg, {time: 1500, icon:5});
	                }
	            }
	        });
	    });
}

/**
 * 绑定分配角色
 */
function bindAsignRole(){
    $("#distribution").click(function () {
    	
    	//回显用户角色
        $("#distributionModal input[name='roleId']").iCheck('uncheck');
        var $result = $('#userTableEvents');
        var list = $result.bootstrapTable('getSelections');
        if (list.length <= 0 || list.length > 1) {
            parent.layer.msg('请选中一条数据', {time: 1500, icon:5});
            return;
        }
        list = list[0];
        $.ajax({
            url: "/user/"+list.id+"/role",
            contentType: "application/json; charset=utf-8",
            type: "GET",
            dataType: 'json',
            success: function(data){
                if (data.status == 200) {
                    var userRoles = data.data;
                    for (var content in userRoles) {
                        $("#distributionModal input[name='roleId'][value='"+ userRoles[content].id+"']").iCheck('check');
                    }
                }
            }
        });
        $("#distributionModal").modal('show');
    });
    
    //分配用户角色
    $("#distributioneForm").submit(function (e) {
        e.preventDefault();
        // 选中用户
        var $result = $('#userTableEvents');
        var list = $result.bootstrapTable('getSelections');
        list = list[0];

        //选中角色
        var userRoleList = new Array();
        $("#distributionModal input[name='roleId']:checked").each(function(){
        	userRoleList.push({"uid": list.id, "rid": $(this).val()})
        })

        if (userRoleList.length <= 0) {
            parent.layer.msg('请选择角色', {time: 1500, icon:5});
            return;
        }

        $.ajax({
            url: "/user/asignUserRoles",
            data: JSON.stringify(userRoleList),
            contentType: "application/json; charset=utf-8",
            type: "POST",
            dataType: 'json',
            success: function(data){
                closeAllLayer();
                $("#distributionModal").modal('hide');
                parent.layer.msg(data.msg, {icon: data.code});
            },
            error: function(){
            	closeAllLayer();
            	parent.layer.msg("请稍候再试");
            }
        });
    });
}
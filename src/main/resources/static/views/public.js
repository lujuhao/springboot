/**
 * 展示加载layer
 */
function loadLayer(){
	layer.load(0);
}

/**
 * 关闭所有layer
 */
function closeAllLayer(){
	layer.closeAll();
}

/**
 * 在指定div加载zTree数据
 * @param divId zTree容器id
 * @parm modal
 * @param zNodes 节点数据
 */
function loadzTree(divId,modal,zNodes){
	$.fn.zTree.destroy($("#"+divId));
	var zTreeObj;
	var setting = {
		//开启复选框
		check: {
            enable: true,
        },
        //简单数据格式
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "pid",
				rootPId: 0
			}
		}
	};
	zTreeObj = $.fn.zTree.init($("#"+divId), setting, zNodes);
	$("#"+modal).modal('show');
}

/**
 * 获取zTree选中的节点
 * @param divId zTree容器id
 * @return 选中的节点
 */
function getzTreeCheckedNodes(divId){
	var treeObj = $.fn.zTree.getZTreeObj(divId);
	return treeObj.getCheckedNodes(true);
}

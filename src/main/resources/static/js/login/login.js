$(function(){
	 //使登录页面始终处于顶端
	 if(self != top){  
		top.location.href=location.href;  
	 }; 
	 
	 //生成验证码
	 createImgCode();
	 
	 //绑定表单验证
	 validate();
	 
	 //粒子特效
	 initCanvas();
	 
	 $('input[name="rememberMe"]').bootstrapSwitch({
		 	state : true,
	        onColor : "primary",
	        offColor : "danger",
	        onText : "记住密码",
	        offText : "OFF",
	        size : "mini",
	        animate : true,
	        handleWidth :"35",
	 });
});

/**
 * 生成验证码
 */
function createImgCode(){
	$("#imgcode").prop("src","/createImgCode");
	$("#imgcode").click(function(){
		var time=new Date().getTime();
		$(this).prop("src","/createImgCode?time="+time);
	});
	
}

/**
 * 绑定表单验证
 */
function validate(){
	$("input[name='loginName']").focus();
	$("#loginForm").validate({
		   rules:{
			    loginName:{
	            	required:true,
	            	maxlength:16
	            },
			  	password:{
	            	required:true,
	            	maxlength:16
	            },
	            randomCode:{
	            	required:true,
	            	maxlength:4
	            }
	           
			},
			messages:{
				loginName:"请输入用户名",
				password:"请输入密码",
				randomCode:"请输入验证码"
		    },
			submitHandler: function(form){
				login();
			},
			errorPlacement: function(error, element) {//错误显示的位置
				layer.tips(error[0].outerText, element, {
					  tips: [2, '#FF0000'], //1-上，2-右,3-下，4-左.还可配置颜色
					  tipsMore: true//不销毁之前的
				});
			}
	});
}

/**
 * 验证登录
 */
function login(){
	$("#loginButton").text("请稍候");
	$("#loginButton").prop("disabled",true);
	
	$.ajax({
		url:"/auth",
		type:"post",
		data:{
			loginName : $("input[name='loginName']").val(),
			password : $("input[name='password']").val(),
			randomCode : $("input[name='randomCode']").val(),
			rememberMe : $("input[name='rememberMe']").val()
		},
		success:function(data){
			if(data.status == 200){
				location.href=data.msg;
			}else{
				$("#loginButton").text("登录");
				$("#loginButton").prop("disabled",false);
				$(".msg").html("")
				$(".msg").append("<p><bold>"+data.msg+"</bold></p>");
				$("input[name='password']").val("");
			}
		},
		error:function(){
			layer.closeAll();
			layer.alert("系统异常，请稍候再试");
		}
	});
}

/**
 * 粒子特效
 */
function initCanvas(){
	var SEPARATION = 100, AMOUNTX = 50, AMOUNTY = 50;

	var container;
	var camera, scene, renderer;

	var particles, particle, count = 0;

	var mouseX = 0, mouseY = 0;

	var windowHalfX = window.innerWidth / 2;
	var windowHalfY = window.innerHeight / 2;

	init();
	animate();

	function init() {

		container = document.createElement('div');
		$("body").append(container);
		//document.body.appendChild( container );

		camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 1, 10000 );
		camera.position.z = 1000;

		scene = new THREE.Scene();

		particles = new Array();

		var PI2 = Math.PI * 2;
		var material = new THREE.ParticleCanvasMaterial( {

			color: 0xffffff,
			program: function ( context ) {

				context.beginPath();
				context.arc( 0, 0, 1, 0, PI2, true );
				context.fill();

			}

		} );

		var i = 0;

		for ( var ix = 0; ix < AMOUNTX; ix ++ ) {

			for ( var iy = 0; iy < AMOUNTY; iy ++ ) {

				particle = particles[ i ++ ] = new THREE.Particle( material );
				particle.position.x = ix * SEPARATION - ( ( AMOUNTX * SEPARATION ) / 2 );
				particle.position.z = iy * SEPARATION - ( ( AMOUNTY * SEPARATION ) / 2 );
				scene.add( particle );

			}

		}

		renderer = new THREE.CanvasRenderer();
		renderer.setSize( window.innerWidth, window.innerHeight );
		container.appendChild( renderer.domElement );

		document.addEventListener( 'mousemove', onDocumentMouseMove, false );
		document.addEventListener( 'touchstart', onDocumentTouchStart, false );
		document.addEventListener( 'touchmove', onDocumentTouchMove, false );

		//

		window.addEventListener( 'resize', onWindowResize, false );

	}

	function onWindowResize() {

		windowHalfX = window.innerWidth / 2;
		windowHalfY = window.innerHeight / 2;

		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();

		renderer.setSize( window.innerWidth, window.innerHeight );

	}

	//

	function onDocumentMouseMove( event ) {

		mouseX = event.clientX - windowHalfX;
		mouseY = event.clientY - windowHalfY;

	}

	function onDocumentTouchStart( event ) {

		if ( event.touches.length === 1 ) {

			event.preventDefault();

			mouseX = event.touches[ 0 ].pageX - windowHalfX;
			mouseY = event.touches[ 0 ].pageY - windowHalfY;

		}

	}

	function onDocumentTouchMove( event ) {

		if ( event.touches.length === 1 ) {

			event.preventDefault();

			mouseX = event.touches[ 0 ].pageX - windowHalfX;
			mouseY = event.touches[ 0 ].pageY - windowHalfY;

		}

	}

	//

	function animate() {
		requestAnimationFrame( animate );
		render();
	}

	function render() {

		camera.position.x += ( mouseX - camera.position.x ) * .05;
		camera.position.y += ( - mouseY - camera.position.y ) * .05;
		camera.lookAt( scene.position );

		var i = 0;

		for ( var ix = 0; ix < AMOUNTX; ix ++ ) {

			for ( var iy = 0; iy < AMOUNTY; iy ++ ) {

				particle = particles[ i++ ];
				particle.position.y = ( Math.sin( ( ix + count ) * 0.3 ) * 50 ) + ( Math.sin( ( iy + count ) * 0.5 ) * 50 );
				particle.scale.x = particle.scale.y = ( Math.sin( ( ix + count ) * 0.3 ) + 1 ) * 2 + ( Math.sin( ( iy + count ) * 0.5 ) + 1 ) * 2;

			}

		}

		renderer.render( scene, camera );

		count += 0.1;

	}
}
var express=require('express');
var zookeeper=require('node-zookeeper-client');
var httpProxy=require('http-proxy');

var PORT=1234;

var CONNECTION_STRING='127.0.0.1:2181';

var REGISTER_ROOT='/home';

//创建zookeeper的连接
var zk=zookeeper.createClient(CONNECTION_STRING);
zk.connect();

var proxy=httpProxy.createProxyServer();

proxy.on('error',function(err,req,res){
	res.end();
});

var app=express();

app.use(express.static('public'));

app.all('*',function(req,res){

    console.log(req.path);

	if(req.path=='/favicon.ioc'){
		res.end();
		return;
	}

	var serviceName=req.get('Service-Name');
	if(!serviceName){
		res.end();
		return;
	}

	//获取服务路径
	var servicePath=REGISTER_ROOT+'/'+serviceName;

    console.log(servicePath);

	zk.getChildren(servicePath,function(error,addressNotes){
          if(error){
            console.log(error);
          	res.end();
          	return;
          }

          var addressCount=addressNotes.length;

          if(addressCount==0){
          	res.end();
          	return;
          }

          var addressPath=servicePath+'/';
          if(addressCount==1){
          	addressPath=addressPath+addressNotes[0];
          }else{
          	addressPath=addressNotes[parseInt(Math.random()*addressCount)];
          }

          console.log('addressPath is ',addressPath);

          zk.getData(addressPath,function(error,serviceAddress){
          	
          	if(error){
          		console.log(error);
          		res.end();
          		return;
          	}

          	if(!serviceAddress){
          		res.end();
          		return;
          	}

          	console.log('serviceAddress is ',serviceAddress);

          	proxy.web(req,res,{
          		target: 'http://'+serviceAddress
          	});
         });
	});
});

app.listen(PORT,function(){
	console.log('server is running at %d',PORT);
});

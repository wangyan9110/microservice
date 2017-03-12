var fs=require('fs');

fs.readFile('./package.json', 'utf8', function(error, file) { 
    if (error) 
    	throw error; 
    console.log(file);
    console.log('我读完文件了！');
});

console.log('程序执行完了！');
# Q群爆照图片查找

通过 Face++ API 实现在QQ群图片记录里查找爆照图片，**包括被撤销的图片**，学生招新新生群必备良品。  

实际效果不会有漏网鱼，对女性的脸识别还好，但男脸就会有不少表情包混进来，谁让表情包也是人脸。![](image/sad.jpg) 

## 实现
Windows版QQ在收到Q群图片后会将图片存在 `数据目录\Q号\Image\Group\` 里，即使对方撤销也不会被删除。所以遍历该目录下的图片，初步过滤后将剩余文件交给人脸识别API，把带人脸的图片复制到新目录。

### 过滤
1. 文件大小 < 10KB 或 > 1MB
2. gif 后缀
3. 大量白色边框的表情包（未实现）

## 用法
不改动代码的情况下

	java -jar QGroupFace.jar 源图片目录 输出目录 
	java -jar QGroupFace.jar 源图片目录 输出目录 >> log.txt

    如 java -jar QGroupFace.jar E:\Software\QQ_data\10000\Image\Group\Image1 E:\Image1

然后慢慢等待，3-20分钟不等。

## 注意事项
1. 使用前建议***先用QQ自带的消息删除器删除很久前的图片***，且用[批处理文件](release/doFliter.bat)过滤删除一些不太可能是皂片的文件。 
2. 调用API需要 `API Key` 和 `API Secret`，代码里已使用官方的测试Key，好像是体验版，并发限制为1，也可以注册后自己替换，注册版是并发限制3。
3. QQ有些图片是 `.null` 为后缀，实际上是图片，默认当作 `jpg` 处理。
4. QQ有些动图也被保存为 jpg 格式。
5. 由于face++的返回的json项太多固不格式化为对象了，直接正则判断是否成功。

## License
**The MIT License**
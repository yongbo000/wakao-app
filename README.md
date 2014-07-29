哇靠百科 
=========
	关于作者：[yongbo](http://blog.wakao.me), 
	花名：济滇，一个爱好移动App的前端工程师。  
	这是一个完整Android App开发示例，希望可以给初学Android开发的童鞋有所帮助。



那么，这是个什么样的App呢？

> 哇靠百科是一个聚合笑话、文章、美图的娱乐App。  
> 数据来自，糗事百科、我们都爱冷笑话、百思不得姐、头条网、91美图、传送门（微信公众号文章）...  
> App已经在Google Play上线，[线上下载地址点这里](https://play.google.com/store/apps/details?id=me.wakao.app&hl=zh-CN)。  

![app 图片](https://raw.githubusercontent.com/yongbo000/wakao-app/master/QQ20140412-1@2x.png)

## Server端基于NodeJS

~~时间问题，Server端源码暂不开放，在这月底我将整理好Server端代码并开源出来。~~

Server端源码：
[查看server端源码](https://github.com/yongbo000/wakao-server)

**简单Server端介绍**

- 基于以下Node模块开发

	* express
	
	* ejs模版引擎
	
	* mysql
	
- 代码托管于百度BAE3.0（目前已经开始收费），使用了百度提供的一些云服务：

	* mysql云数据库
	
	* image服务
	
			基于image服务我制作了一个图片缩放接口 ：
			http://apitest.wakao.me/zoom?size={图片宽度}&url={图片路径}
			例如访问如下链接地址：http://apitest.wakao.me/zoom?size=200&url=http://bcs.duapp.com/imgs00/20131130/2217/2818-ceda3c727db376bcef7f9abf6fadcce8.jpg
			通过改变size值即可返回按照宽度缩放的图片。
			
	* bcs云存储
			
			我的图片、文件数据全部存放在百度云存储上，目前云存储还没有开始收费


## 运行说明


- 主程序位于包wakao-app下。
- 分别依赖2个开源项目（需将这两个包设置成Library）
	
	* SlideMenu
	
	* Android-ViewPagerIndicator



## 致谢

开源是个很好的习惯，哈哈～  
一路走来，自己也是受很多优秀的开源项目的影响，  
向着他们学习、成长。  
也很想能给后来者留点什么，于是开源了这个小小的App源码。  
App里也引用很多优秀的开源项目里的代码。

**非常感谢以下开源项目**

[开源中国Android开源客服端](https://github.com/oschina/android-app)

[Android-ViewPagerIndicator](https://github.com/JakeWharton/Android-ViewPagerIndicator)

[SlideMenu](https://github.com/TangKe/SlideMenu)


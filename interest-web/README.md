# interest-web

20190110修改
---
前端项目转为vue-cli 3.0

项目介绍
--
vue.js+node.js+webpack构建的前端项目，后端用的是项目[interest-server](https://github.com/smallsnail-wh/interest/tree/master/interest-server)（前后端分离）。

项目展示
--
地址：http://www.lovemtt.com/ 
可使用github登录
（第一次打开可能会有点慢）

项目目录结构
--
```shell
.
├── package.json  项目构建配置
├── vue.config.js  项目构建配置
├── vue.config.js  项目构建配置
└── src
    ├── images  图片文件
    ├── store  状态管理
    ├── styles  样式文件
    ├── App.vue  入口页面
    ├── axios.js  axios配置
    ├── main.js  main.js
    ├── router.js  路由配置
    ├── base.js  公共js
    └── views
        ├── mobile  mobile版
        │   ├── card.vue  帖子页
	│   ├── detail.vue  详情页
	│   ├── home.vue  主页
	│   └── index.vue  head+footer
        ├── sys  后台管理
        │   ├── interest
	│   │   ├── interest-create.vue  兴趣新建
	│   │   ├── interest-delete.vue  兴趣删除
	│   │   ├── interest-edit.vue  兴趣修改
	│   │   └── interest-quill-editor.vue  富文本编辑器组件
	│   ├── banner.vue  轮播管理
	│   ├── base.vue  head+left list
	│   ├── card.vue  帖子管理
	│   ├── email.vue  邮件管理
	│   ├── menu.vue  菜单管理
	│   ├── role.vue  角色管理
	│   ├── user.vue  用户管理
        │   └── welcome.vue  默认页
	├── template  PC版
        │   ├── card.vue  帖子页
	│   ├── detail.vue  详情页
	│   ├── home.vue  主页
	│   └── index.vue  head+footer
        ├── error404.vue  404页面  
        ├── login.vue  PC登录页面
        └── mlogin.vue  mobile登录页面
```

技术栈
--
 - vue2
 - vuex
 - vue-router
 - axios
 - [iview](https://www.iviewui.com/)
 - [vue-quill-editor](https://github.com/surmon-china/vue-quill-editor)
 - webpack

前后端通信：
------
	使用的是axios库。

UI 组件库：
-------
	使用了iview组件库。

安全协议：
-----
	oauth2

前端界面开发
--
注：需要安装nodejs
	

 - 启动：(注意：最好换cnpm，npm我这边启动报404)
	 1. 命令行进入项目文件夹
	 2. 运行npm install（初次启动）
	 3. 运行npm run serve启动前端工程
- 打包命令：
	运行npm run build

打包发布
--
1. 运行npm run build后，得到 dist文件。
2. 使用nginx发布。（[nginx配置参考文件](https://github.com/smallsnail-wh/interest/blob/master/nginx.conf)）
	
Http状态码
--
	在axios.js中拦截异常，并进行处理。
	目前以写的异常处理有：
		1. 401 清除token信息并跳转到登录页面
		2. 403 无权限，跳转到首页
如果你项目启动有错误：
--
1. 项目启动报错，请试一下用管理员权限输入命令。
2. 启动报错：Invalid options in vue.config.js: "publicPath" is not allowed，请把vue.config.js中的publicPath改成baseUrl。
3. install报错，请换成cnpm

下面是主要功能截图
--

**pc首页**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/home%20-%20Home.jpg)
**mobile首页**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/1165609226.jpg)
**pc详情**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/detail%20-%20Home.png)
**mobile详情**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/1014146265.jpg)
**pc帖子详情**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/card%20-%20Home.png)
**mobile帖子详情**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/706938938.jpg)
**pc登录**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/pclogin.png)
**mobile登录**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/369530919.jpg)
**用户管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/user%20-%20Home.png)
**菜单管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/menu%20-%20Home.png)
**角色管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/role%20-%20Home.png)
**轮播管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/banner%20-%20Home.png)
**兴趣管理-新建**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/interest%20-%20Home.png)
**兴趣管理-修改**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/interest%20-%20edit.png)
**兴趣管理-删除**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/interest%20-%20del.png)
**发帖管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/card%20-%20menage.png)
**邮件管理**
![这里写图片描述](https://github.com/smallsnail-wh/images/blob/master/email%20-%20Home.png)

# 这个README.md须在Eclipse里创建<br/><br/>
手机安全卫士<br/><br/>
创建：20140923<br/>
Author：Vigiles<br/><br/>
最低版本：2.3.3 <br/>
目标版本：4.4 <br/>
编译版本：4.4 <br/><br/>
如果在服务器上修改了项目，必须首先在客户端下载，否则就是rejected non fast forward错误

# 第5次提交，主要实现了自动更新的设置<br/>
1）将欢迎界面独有的<br/>
\<activity android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen" \><br/>
放入了<br/>
\<application android:theme="@style/AppTheme" \><br/>
如是，整个应用的任何界面都是新主题<br/><br/>
2）实现了主界面9宫格<br/>
GridView.setAdapter(adapter)通过适配器载入格子<br/>
View.inflate()方法把xml布局转为View控件<br/><br/>
3）自定义滚动条<br/>
\<com.cuiweiyou.mobileguard.ui.FocusedTextView\>使用的是extends了TextView的自定义类，重新3个构造方法，关键是在isFocused()直接返回true<br/>
4）通过自定义控件实现9宫格的“设置中心”界面中的条目<br/>
（1）根据理想的条目样式创建一个布局文件<br/>
（2）创建一个对应的条目类，inflate(context, source, root)得到条目布局转为View，当调用此类时使用的就是这个View<br/>
（3）在类中增加一些增强方法，如控制子控件的方法<br/>
（4）在“设置中心”xml布局里使用这个自定义类<br/>

#第6次提交
实现防盗界面打开验证密码<br/>
采用MD5加密算法

#第7次提交
1.“手机防盗”的第一个界面完成<br/>
2.自定义按钮<br/>
1）/res/下创建drawable目录，在其中创建xml文件，放入所需图片<br/>
\<?xml version="1.0" encoding="utf-8"?>
\<selector xmlns:android="http://schemas.android.com/apk/res/android">
    \<item android:state_pressed="true"
          android:drawable="@drawable/button_pressed" /> <!-- 按下 -->
    \<item android:state_focused="true"
          android:drawable="@drawable/button_focused" /> <!-- 焦点 -->
    \<item android:drawable="@drawable/button_normal" /> <!-- 默认 -->
\</selector><br/>
2）在按钮的background中引用这个风格<br/>
\<Button android:background="@drawable/style_mybutton" <br/>

#第8次提交
1.设置向导流完成，4个界面<br/>
2.第一个界面设置安全号码初步完成<br/>


1、压缩功能依赖java 运行环境
2、dist 目录下会生成debug版本和min版本js/css
3、lib下为压缩依赖的jar
4、压缩js配置文件prspty_filename.txt【filename 根据自己要求填写】。
  可配置相对目录或文件相对目录，分别独占一行
  例：
	..\common
	..\route-tables\route.common.js
  配置文件名称必须以prspty开头，prspty_后的字符即为待生成js文件的名称
  例如：prspty_yufp-custom.txt  将产生yufp-custom-debug.js/css 和yufp-custom-min.js/css
  如果有多个prspty名字开始的txt文件，则会按规则生成多组js/css
5、配置完第4步后运行该bat

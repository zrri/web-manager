@echo off
 set type=
 set total_type=.js,.css
rem 开启变量延迟
setlocal enabledelayedexpansion
rem 说明遍历当前目录下是所有js/css（排除部分特定名称）并执行文件合并，并通过yui compressor 压缩。定义当前目录
set current_dir=%cd%
rem 将目录中build替换为空（目录转移到common 目录）
set work_dir=%current_dir:custom\build=%
rem 定义压缩jar文件路径
set compressjar=.\lib\yufp.build.jar
rem 合并文件用的临时文件
set tmpdir=%current_dir%\tmp
if not exist %current_dir%\dist (
	 md dist
  ) else (
     rd /s/q %current_dir%\dist
     md dist
  )
  rem 计数器
  set  s=1
	rem 循环遍历当前路径下的js	
	for /R %work_dir% %%s in (prspty*.txt) do ( 
        echo ********第!s!个合并开始********
		rem 定义产生的合并js
		set tmp_name=%%~ns
		set tmp_name=!tmp_name:prspty_=!
		set debug_path=%current_dir%\dist\!tmp_name!-debug
		rem 压缩文件
		set min_path=.\dist\!tmp_name!-min
		for %%t in (%total_type%) do ( 
		rem 存在min_path 就删除
		  if exist !min_path!%%t (
				del !min_path!%%t
				echo 删除!min_path!%%t成功
		  )
		  rem 存在debug_path就删除，并创建一个空文档
		  if exist !debug_path!%%t (
			   del !debug_path!%%t
			   echo 删除!debug_path!%%t成功
			   cd.>!debug_path!%%t
			   echo 创建!debug_path!%%t成功
		   )else (
		   rem 没有就创建空文档
			   cd.>!debug_path!%%t
			   echo 创建!debug_path!%%t成功
		   )
		  )
		    set flag=1
           for /f %%i in (%%s) do (
		   rem 有可能进入文件夹后没有找到需要压缩的js/css 所以设置标志位
					 rem 是路径就遍历执行 合并
					   if  "%%~xi"=="" (
					      cd %%i
							for /R . %%y in (*.js,*.css) do (
                                 if %%~xi==.css ( 
								   set type=.css
							   ) else (
								     set type=.js
							   )
								set flag=2							   
								rem 将合成文件复制到 临时文件
								 copy /b !debug_path!!type!	%tmpdir%!type!
								rem 将临时文件和当前文件合并到 合成文件中						 
								 copy /b %tmpdir%!type!+%%y	!debug_path!!type!
							 )
						  cd %current_dir%
					   ) else (
					    set flag=2
							   if %%~xi==.css ( 
								   set type=.css
							   ) else (
								   if %%~xi==.js ( 
								     set type=.js
								   ) else (
								     echo *******************************
									 echo 不支持该文件压缩格式%%i 已退出。
									 echo *******************************
							         goto _exit
								   )
							   )
					        rem 将合成文件复制到 临时文件
							 copy /b !debug_path!!type!	%tmpdir%!type!
							rem 将临时文件和当前文件合并到 合成文件中						 
							 copy /b %tmpdir%!type!+%%i	!debug_path!!type!
					   )     
					)
					rem 计数器+1  				 
					if !flag! equ 1  (
					  echo 未找到需压缩文件
					  goto  _exit
					)	
					echo ********第!s!个合并完成********
					rem 执行yui compressor 压缩 并生成collect-min.js/css 文件 依赖java 环境
					echo ********第!s!个压缩开始********					
						for %%w in (%total_type%) do ( 	
                          rem 删除临时文件
					 if exist %tmpdir%%%w  (
								del %tmpdir%%%w 
							  )		
							rem 获取合并文件的大小  
                          for %%m in ( !debug_path!%%w) do (						    
							 set indexdx=%%~zm							 
							)
							rem 判断文件大小，>=1 就压缩，否则删除							
							if !indexdx! GEQ 1 (
							   java -jar %compressjar% !debug_path!%%w  -o !min_path!%%w	
							) else (
                                 del !debug_path!%%w
                             )							
						)
					echo ********第!s!个压缩完成********	
			        set /a s=!s!+1
		 )
	if %s% equ 1 (echo 未找到prspty_前缀开始txt格式配置文件)
	:_exit
	for %%k in (%total_type%) do ( 
	rem 删除临时文件
	 if exist %tmpdir%%%k  (
				del %tmpdir%%%k 
			  )					
		)
pause 
@echo off
 set type=
 set total_type=.js,.css
rem ���������ӳ�
setlocal enabledelayedexpansion
rem ˵��������ǰĿ¼��������js/css���ų������ض����ƣ���ִ���ļ��ϲ�����ͨ��yui compressor ѹ�������嵱ǰĿ¼
set current_dir=%cd%
rem ��Ŀ¼��build�滻Ϊ�գ�Ŀ¼ת�Ƶ�common Ŀ¼��
set work_dir=%current_dir:custom\build=%
rem ����ѹ��jar�ļ�·��
set compressjar=.\lib\yufp.build.jar
rem �ϲ��ļ��õ���ʱ�ļ�
set tmpdir=%current_dir%\tmp
if not exist %current_dir%\dist (
	 md dist
  ) else (
     rd /s/q %current_dir%\dist
     md dist
  )
  rem ������
  set  s=1
	rem ѭ��������ǰ·���µ�js	
	for /R %work_dir% %%s in (prspty*.txt) do ( 
        echo ********��!s!���ϲ���ʼ********
		rem ��������ĺϲ�js
		set tmp_name=%%~ns
		set tmp_name=!tmp_name:prspty_=!
		set debug_path=%current_dir%\dist\!tmp_name!-debug
		rem ѹ���ļ�
		set min_path=.\dist\!tmp_name!-min
		for %%t in (%total_type%) do ( 
		rem ����min_path ��ɾ��
		  if exist !min_path!%%t (
				del !min_path!%%t
				echo ɾ��!min_path!%%t�ɹ�
		  )
		  rem ����debug_path��ɾ����������һ�����ĵ�
		  if exist !debug_path!%%t (
			   del !debug_path!%%t
			   echo ɾ��!debug_path!%%t�ɹ�
			   cd.>!debug_path!%%t
			   echo ����!debug_path!%%t�ɹ�
		   )else (
		   rem û�оʹ������ĵ�
			   cd.>!debug_path!%%t
			   echo ����!debug_path!%%t�ɹ�
		   )
		  )
		    set flag=1
           for /f %%i in (%%s) do (
		   rem �п��ܽ����ļ��к�û���ҵ���Ҫѹ����js/css �������ñ�־λ
					 rem ��·���ͱ���ִ�� �ϲ�
					   if  "%%~xi"=="" (
					      cd %%i
							for /R . %%y in (*.js,*.css) do (
                                 if %%~xi==.css ( 
								   set type=.css
							   ) else (
								     set type=.js
							   )
								set flag=2							   
								rem ���ϳ��ļ����Ƶ� ��ʱ�ļ�
								 copy /b !debug_path!!type!	%tmpdir%!type!
								rem ����ʱ�ļ��͵�ǰ�ļ��ϲ��� �ϳ��ļ���						 
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
									 echo ��֧�ָ��ļ�ѹ����ʽ%%i ���˳���
									 echo *******************************
							         goto _exit
								   )
							   )
					        rem ���ϳ��ļ����Ƶ� ��ʱ�ļ�
							 copy /b !debug_path!!type!	%tmpdir%!type!
							rem ����ʱ�ļ��͵�ǰ�ļ��ϲ��� �ϳ��ļ���						 
							 copy /b %tmpdir%!type!+%%i	!debug_path!!type!
					   )     
					)
					rem ������+1  				 
					if !flag! equ 1  (
					  echo δ�ҵ���ѹ���ļ�
					  goto  _exit
					)	
					echo ********��!s!���ϲ����********
					rem ִ��yui compressor ѹ�� ������collect-min.js/css �ļ� ����java ����
					echo ********��!s!��ѹ����ʼ********					
						for %%w in (%total_type%) do ( 	
                          rem ɾ����ʱ�ļ�
					 if exist %tmpdir%%%w  (
								del %tmpdir%%%w 
							  )		
							rem ��ȡ�ϲ��ļ��Ĵ�С  
                          for %%m in ( !debug_path!%%w) do (						    
							 set indexdx=%%~zm							 
							)
							rem �ж��ļ���С��>=1 ��ѹ��������ɾ��							
							if !indexdx! GEQ 1 (
							   java -jar %compressjar% !debug_path!%%w  -o !min_path!%%w	
							) else (
                                 del !debug_path!%%w
                             )							
						)
					echo ********��!s!��ѹ�����********	
			        set /a s=!s!+1
		 )
	if %s% equ 1 (echo δ�ҵ�prspty_ǰ׺��ʼtxt��ʽ�����ļ�)
	:_exit
	for %%k in (%total_type%) do ( 
	rem ɾ����ʱ�ļ�
	 if exist %tmpdir%%%k  (
				del %tmpdir%%%k 
			  )					
		)
pause 
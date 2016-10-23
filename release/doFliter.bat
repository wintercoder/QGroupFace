::不是必须 因为程序里也有做过滤
@echo off

set delPath=E:\Software\QQ_data\10000\Image\Group\Image1
echo clean : %delPath%

::<12kb
for /r %delPath% %%i in (*) do @(if %%~zi lss 12288 del "%%i" /f) 
::>1Mb
for /r %delPath% %%i in (*) do @(if %%~zi gtr 1048576 del "%%i" /f) 
::gif
cd /d %delPath%
del *.gif
pause
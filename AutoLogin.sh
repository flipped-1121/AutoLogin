#!/bin/bash

userid="20********" # 填写你的学号
passwd="*******" # 填写你的密码
ISP="******" # 填写你的校园网入口  jzg 	教职工
	         #			        aust 	电信
	         #			        unicom	联通
	         #			        cmcc	移动
# 使用 ping 检查网络连接
if ping -c 1 -W 2 8.8.8.8 >/dev/null 2>&1; then
    echo "Internet is available"
else
    echo "No internet connection"
    curl --max-time 2 --retry 2 --retry-delay 1 -d "callback=dr1003&DDDDD=${userid}@${ISP}&upass=${passwd}&0MKKey=123456" http://10.255.0.19/drcom/login
fi

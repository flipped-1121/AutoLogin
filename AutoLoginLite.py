# -*- coding: utf-8 -*-
"""
   @Author: Kang
   @Version 1.1
   @File: AutoLoginLite.py
   @CreateTime: 2021-09-05 13:22:51
   @Software: PyCharm
"""
from time import sleep

import requests


# 自动登录
def autoLogin():
    url = "http://10.255.0.19/drcom/login"
    try:
        once = requests.get(url=url,
                            params={'callback': 'dr1003', 'DDDDD': "201930****@****", 'upass': "*******",
                                    '0MKKey': 123456})
        sleep(3)
        twice = requests.get(url=url,
                             params={'callback': 'dr1003', 'DDDDD': "201930****@****", 'upass': "*******",
                                     '0MKKey': 123456})
        return "自动登陆成功"
    except:
        return "发生异常"


if __name__ == '__main__':
    autoLogin()

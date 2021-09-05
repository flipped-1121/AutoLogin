# -*- coding: utf-8 -*-
'''
   @Author: Kang
   @Version 1.0 
   @File: AutoLogin.py
   @CreateTime: 2021/9/4 8:49
   @Software: PyCharm
'''
import json
import sys
import requests
import yaml
from win10toast_click import ToastNotifier
from datetime import datetime, timedelta, timezone


# 读取 yml 配置
def getYmlConfig(yaml_file='config.yml'):
    file = open(yaml_file, 'r', encoding="utf-8")
    file_data = file.read()
    file.close()
    config = yaml.load(file_data, Loader=yaml.FullLoader)
    return dict(config)


# 设置全局配置
config = getYmlConfig(yaml_file='config.yml')


# 输出调试信息，并及时刷新缓冲区
def log(content):
    print(getTimeStr() + ' ' + str(content))
    sys.stdout.flush()


# 获取当前 utc 时间，并格式化为北京时间
def getTimeStr():
    utc_dt = datetime.utcnow().replace(tzinfo=timezone.utc)
    bj_dt = utc_dt.astimezone(timezone(timedelta(hours=8)))
    return bj_dt.strftime("%Y-%m-%d %H:%M:%S")


# 发送 Cqhttp 通知
def sendMsgByCqhttp(msg):
    log('正在发送Cqhttp通知。。。')
    url = config['msg']['go-cqhttp']['url']
    token = config['msg']['go-cqhttp']['token']
    qq = config['msg']['go-cqhttp']['qq']
    res = requests.get(url=url,
                       params={'access_token': token, 'user_id': qq, 'message': getTimeStr() + "\n" + str(msg)})
    code = res.json()['status']
    if code == 'ok':
        log('发送Cqhttp通知成功。。。')
    else:
        log('发送Cqhttp通知失败。。。')
        log('Cqhttp返回结果' + code)


# 发送 Windows 通知
def sendMsgByWin(message):
    # initialize
    toaster = ToastNotifier()
    title = config['msg']['win']['title']
    # showcase
    toaster.show_toast(
        title,  # title
        message,  # message
        duration=None,  # for how many seconds toast should be visible; None = leave notification in Notification Center
        threaded=True,
    )


# 综合提交
def infoSubmit(msg):
    if config['msg']['go-cqhttp']['token']: sendMsgByCqhttp(msg)
    if config['msg']['win']['title']: sendMsgByWin(msg)


# 自动登录
def autoLogin():
    url = config['url']['login']
    for user in config['users']:
        userid = user['user']['userid']
        password = user['user']['password']
        ISP = user['user']['ISP']
        key = user['user']['0MKKey']
        values = {"教职工": "@jzg", "电信": "@aust", "联通": "@unicom", "移动": "@cmcc"}
        ISPString = values[ISP]
        try:
            once = requests.get(url=url,
                                params={'callback': 'dr1003', 'DDDDD': str(userid) + ISPString, 'upass': password,
                                        '0MKKey': key})
            twice = requests.get(url=url,
                                 params={'callback': 'dr1003', 'DDDDD': str(userid) + ISPString, 'upass': password,
                                         '0MKKey': key})
            temp = twice.text.replace('dr1003(', '').replace(')', '')
            result = json.loads(temp)['result']
            if result == 1:
                infoSubmit("自动登陆成功🎉🎉🎉")
            else:
                infoSubmit("自动登陆失败😐😐😐")
        except:
            infoSubmit("发生异常")
            # return "发生异常"


if __name__ == '__main__':
    autoLogin()

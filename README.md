<div align="center">
<h1>自动登录安徽理工大学校园网Shell版</h1>
</div>


# 前言
+ 🙄每天打开浏览器，点击`请选择出口`，再进行登录，不胜其烦。
+ 😎没有 Python 没关系，直接 Shell 脚本。
+ 🌟解除宽带速度限制。


# 配置并运行

## 环境配置
### ✅ Windows

#### clone 并进入本项目，下载所需包
```bash
git clone -b shell https://github.com/flipped-1121/AutoLogin.git
cd AutoLogin
```

#### 编辑个人配置

用编辑软件修改bat中的配置参数

|登录入口|对应参数|
|-------|------|
|教职工|@jzg|
|电信|@aust|
|联通|@unicom|
|移动|@cmcc|

#### 运行和调试
🟢运行
```bash
./AutoLogin.bat
```

#### 开机自启
将AutoLoginLite.bat文件放置到`C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Startup`目录下。
<h1>Congratulations🎉</h1>

### ✅ Linux/Unix

#### clone 并进入本项目，下载所需包
```bash
git clone -b shell https://github.com/flipped-1121/AutoLogin.git
cd AutoLogin
```

#### 编辑个人配置

用编辑软件修改shell脚本中的配置参数

|登录入口|对应参数|
|-------|------|
|教职工|@jzg|
|电信|@aust|
|联通|@unicom|
|移动|@cmcc|


#### 运行和调试
🟢授权并运行
```bash
chmod +x ./AutoLogin.sh
./AutoLogin.sh
```

#### 开机自启
利用`crontab`定时任务实现，如果您的系统上crontab命令不可用，请自行搜索相应的安装方法

进入定时任务管理页面
```bash
crontab -e
```

```bash
*/1 * * * * sh 您的绝对路径/AutoLogin.sh
```
<h1>Congratulations🎉</h1>

## 解除宽带限速
经过测试（~~无意间发现~~），当进行两次登录后，即可突破就宽带速度限制。

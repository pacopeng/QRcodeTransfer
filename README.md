# QRcodeTransfer
# 二维码数据传输工具

## 该工具能够通过二维码技术传输远程桌面中的文件到本地。
### 应用场景：
  1. 公司因考虑数据安全会将USB接口，百度网盘，公网邮箱都封锁。
  2. 公司采用远程桌面让开发人员远程开发，但只允许上传文件到远程桌面，想要从远程桌面拷贝文件出来则不允许。
  
## 本工具一共包含5个java文件。具体如何使用，我也不知道。希望有大侠，专家，牛逼人物自行阅读源码了解运行过程。
### 大概操作流程如下：
  1. 服务端（远程桌面上运行），会弹出窗口让用户选择需要传输的文件。
  2. 选择好文件后，服务端程序会将文件转换二维码（每张二维码能存储几K大小的数据）-- 「本人亲测1G大小的文件耗时15～20分钟」
  3. 在大概10秒时间内，在本地端运行客户端程序，该客户端会自动读取固定位置的二维码图片并转换成本地的文件。
  4. 客户端每扫完一张二维码图片，服务端会自动更新二维码图片直到所选择的文件传输完成。
  
 

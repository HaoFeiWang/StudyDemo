# `Android`保活机制
- 提高进程优先级
- 对死亡的进程进行拉活

## 提高进程优先级
- 在屏幕关闭时创建一个1像素的`Activity`处于前台，将进程优先级从`4`提升到`1`。当屏幕开启时，关闭创建的`Activity`,并设置不显示在最近列表中，防止用户感知。
  该方式主要用于防止系统管理工具在检测到锁屏事件后一段时间内（一般为5分钟）会杀死后台进程，已达到省电的目的。该方式在代码中已实现。
- 创建一个`Service`,并将该`Service`设置为前台进程，然后再在该`Service`内部创建一个`InnerService`，用于取消通知栏。该方式在代码中实现。

## 对死亡的进程拉活

- 监听系统广播（网络变化、屏幕亮灭、开机等），在收到广播时拉活死亡的进程，稳定性差，作为备用。
- 在进程死亡时发出一条自定义的广播，然后收到该广播后进行进程的拉活。
- 利用系统`Service`进行拉活（`onStartCommand()`方法的返回值），一旦进程被`Root`权限的管理工具通过`forestop`停止掉，则无法再重启。
- 利用`Native`进程拉活（有待学习）
- 利用`JobScheduler`机制拉活（有待学习）
- 利用账号同步机制拉活（有待学习）

## MediaManager
管理文件流模式的音频录制(MediaRecoder)和音频播放（MediaPlayer）
## AudioRecordManager
管理字节流的音频录制（AudioRecord）
### 参数配置
- `audioSource`：

该参数指的是音频采集的输入源，可选的值以常量的形式定义在 `MediaRecorder.AudioSource` 类中，常用的值包括：`DEFAULT`（默认），`VOICE_RECOGNITION`（用于语音识别，等同于DEFAULT），`MIC`（由手机麦克风输入），`VOICE_COMMUNICATION`（用于VoIP应用）等等。
- `sampleRateInHz` ：

采样率，注意，目前`44100Hz`是唯一可以保证兼容所有Android手机的采样率。
- `channelConfig` ：

通道数的配置，可选的值以常量的形式定义在 `AudioFormat` 类中，常用的是 `CHANNEL_IN_MONO`（单通道），`CHANNEL_IN_STEREO`（双通道）
- `audioFormat` ：

这个参数是用来配置“数据位宽”的，可选的值也是以常量的形式定义在 `AudioFormat` 类中，常用的是 `ENCODING_PCM_16BIT（16bit）`、`ENCODING_PCM_8BIT（8bit）`，注意，16位是可以保证兼容所有Android手机的。
- `bufferSizeInBytes` ：

它配置的是 AudioRecord 内部的音频缓冲区的大小，该缓冲区的值不能低于一帧“音频帧”（Frame）的大小，一帧音频帧的大小计算如下：`int size = 采样率 x 位宽 x 采样时间 x 通道数`。采样时间一般取 `2.5ms~120ms` 之间，由厂商或者具体的应用决定，我们其实可以推断，每一帧的采样时间取得越短，产生的延时就应该会越小，当然，碎片化的数据也就会越多。在Android开发中，`AudioRecord` 类提供了一个帮助你确定这个 `bufferSizeInBytes` 的函数，原型如下：`int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat)`。不同的厂商的底层实现是不一样的，但无外乎就是根据上面的计算公式得到一帧的大小，音频缓冲区的大小则必须是一帧大小的2～N倍。
## AudioTrackManager
管理字节流的音频播放(AudioTrack)
### 参数配置
- `streamType` ：

这个参数代表着当前应用使用的哪一种音频管理策略，当系统有多个进程需要播放音频时，这个管理策略会决定最终的展现效果，该参数的可选的值以常量的形式定义在 AudioManager 类中，主要包括：
  - `STREAM_VOCIE_CALL`：电话声音
  - `STREAM_SYSTEM`：系统声音
  - `STREAM_RING`：铃声
  - `STREAM_MUSCI`：音乐声
  - `STREAM_ALARM`：警告声
  - `STREAM_NOTIFICATION`：通知声
- `sampleRateInHz` ：

应与`AudioRecord`的采样率相同
- `channelConfig` ：

应与`AudioRecord`的声道数相配套，`CHANNEL_OUT_MONO`（单通道）、`CHANNEL_OUT_STEREO`(双通道)
- `audioFormat` ：

应与`AudioRecord`的位宽相同
- `mode` ：

`AudioTrack` 提供了两种播放模式，一种是 `static` 方式，一种是 `streaming` 方式，前者需要一次性将所有的数据都写入播放缓冲区，简单高效，通常用于播放铃声、系统提醒的音频片段; 后者则是按照一定的时间间隔不间断地写入音频数据，理论上它可用于任何音频播放的场景。可选的值以常量的形式定义在`AudioTrack` 类中，一个是 `MODE_STATIC`，另一个是 `MODE_STREAM`，根据具体的应用传入对应的值即可。

[视频地址](http://www.imooc.com/learn/739)

[知乎文章-字节流声音采集](https://zhuanlan.zhihu.com/p/20642437)

[知乎文章-字节流声音播放](https://zhuanlan.zhihu.com/p/20642442)

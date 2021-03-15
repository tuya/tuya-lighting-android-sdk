## 涂鸦智能照明控制Android SDK文档

### **[English](README.md) | [中文版](README_cn.md)**

照明设备较为复杂，同时存在v1和v2新旧两种固件，即使使用了标准指令，也需要开发两套控制逻辑。

因此对照明设备功能进行封装，封装了灯具设备的开关、工作模式切换、亮度控制、冷暖控制、彩光控制和四种情景模式的控制。

#### 功能概述

涂鸦智能照明Android SDK是在TuyaSmartDevice基础上扩展了接入照明设备相关功能的接口封装，加速开发过程。主要包括了以下功能：

* 获取当前设备是几路灯
* 获取灯所有功能点的值
* 开灯或关灯
* 切换工作模式
* 控制灯的亮度
* 控制灯的色温
* 切换场景模式
* 设置彩灯的颜色

#### 快速集成

## 依赖说明

```groovy
implementation 'com.tuya.smart:tuyasmart:3.22.0'
// 控制SDK依赖
implementation 'com.tuya.smart:tuyasmart-centralcontrol:1.0.3'
```

需要注意的是，tuyasmart-centralcontrol使用了kotlin编译，需要引入kotlin库确保其正常使用。

> 项目中已引入kotlin的可忽略下面的配置。

**kotlin接入**

在根目录的build.gradle中引入kotlin插件的依赖：

```groovy
buildscript {
    ext.kotlin_version = '1.3.72'
    dependencies {
    	...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```

在app的build.gradle中引入kotlin插件和kotlin包：

```groovy
apply plugin: 'kotlin-android'
dependencies {
	implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
```



### 设备初始化

首先，创建ITuyaLightDevice对象，灯相关的方法均封装在此方法中。

```java
ITuyaLightDevice lightDevice = new TuyaLightDevice(String devId);
```

**参数说明**

| 参数  | 说明    |
| ----- | :------ |
| devId | 设备 id |

**示例代码**

```java
// 创建lightDevice
ITuyaLightDevice lightDevice = new TuyaLightDevice("vdevo159793004250542");
```

该对象封装了灯的所有dp点，包括控制指令的下发和上报。

### 设备代理监听

**方法说明**

```java
/**
 * 注册监听
 */
void registerLightListener(ILightListener listener);
```

其中，ILightListener回调如下：

```java
public interface ILightListener {
    /**
     * 监听照明设备dp点变化
     *
     * @param dataPoint 该灯具所有dp点的状态
     */
    void onDpUpdate(LightDataPoint dataPoint);

    /**
     * 设备移除
     */
    void onRemoved();

    /**
     * 设备上下线
     */
    void onStatusChanged(boolean online);

    /**
     * 网络状态
     */
    void onNetworkStatusChanged(boolean status);

    /**
     * 设备信息更新例如name之类的
     */
    void onDevInfoUpdate();
}
```

**示例代码**

```java
// 创建lightDevice
ITuyaLightDevice lightDevice = new TuyaLightDevice("vdevo159793004250542");

// 注册监听
lightDevice.registerLightListener(new ILightListener() {
    @Override
    public void onDpUpdate(LightDataPoint dataPoint) { // 返回LightDataPoint，包含灯所有功能点的值
        Log.i("test_light", "onDpUpdate:" + dataPoint);
    }

    @Override
    public void onRemoved() {
        Log.i("test_light", "onRemoved");
    }

    @Override
    public void onStatusChanged(boolean status) {
        Log.i("test_light", "onDpUpdate:" + status);
    }

    @Override
    public void onNetworkStatusChanged(boolean status) {
        Log.i("test_light", "onDpUpdate:" + status);
    }

    @Override
    public void onDevInfoUpdate() {
        Log.i("test_light", "onDevInfoUpdate:");
    }
});
```

**`TuyaSmartLightDataPointModel` 数据模型**

| 字段             | 类型            | 描述                                                         |
| ---------------- | --------------- | ------------------------------------------------------------ |
| powerSwitch      | boolea          | 开关是否打开，YES代表开，NO代表关                            |
| workMode         | LightMode       | 工作模式，MODE_WHITE为白光模式； MODE_COLOUR为彩光模式； MODE_SCENE为情景模式； |
| brightness       | int             | 亮度百分比，从1-100                                          |
| colorTemperature | int             | 色温百分比，从0-100                                          |
| colourHSV        | LightColourData | 颜色值，HSV色彩空间；H为色调，取值范围0-360；S为饱和度，取值范围0-100；V为明度，取值范围1-100 |
| scene            | LightScene      | 彩灯情景，* SCENE_GOODNIGHT为晚安情景； * SCENE_WORK为工作情景； * SCENE_READ为阅读情景； * SCENE_CASUAL为休闲情景； |

**参数说明**

值得说明的是`LightDataPoint`对象，该对象封装了当前设备所有功能点。当功能点发生变化时，将会回调。每次回调的都会是完整的对象。

以下是该对象参数的具体含义：

```java
public class LightDataPoint {
    /**
     * 开关
     */
    public boolean powerSwitch;
    /**
     * 工作模式。
     * <p>
     * MODE_WHITE为白光模式；
     * MODE_COLOUR为彩光模式；
     * MODE_SCENE为情景模式；
     */
    public LightMode workMode;

    /**
     * 亮度百分比，从0到100
     */
    public int brightness;

    /**
     * 色温百分比，从0到100
     */
    public int colorTemperature;

    /**
     * 颜色值，HSV色彩空间.
     * <p>
     * 其中H为色调，取值范围0-360；
     * 其中S为饱和度，取值范围0-100；
     * 其中V为明度，取值范围0-100；
     */
    public LightColourData colorHSV;
    /**
     * 彩灯情景。
     *
     * SCENE_GOODNIGHT为晚安情景；
     * SCENE_WORK为工作情景；
     * SCENE_READ为阅读情景；
     * SCENE_CASUAL为休闲情景；
     */
    public LightScene scene;
}
```

**示例代码**

```
// 开灯
lightDevice.powerSwitch(true, new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Log.i("test_light", "powerSwitch onError:" + code + error);
    }

    @Override
    public void onSuccess() {
        Log.i("test_light", "powerSwitch onSuccess:");
    }
});
// 晚安场景
lightDevice.scene(LightScene.SCENE_GOODNIGHT, new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Log.i("test_light", "scene onError:" + code + error);
    }

    @Override
    public void onSuccess() {
        Log.i("test_light", "scene onSuccess:");
    }
});
// 设置颜色
lightDevice.colorHSV(100, 100, 100, new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Log.i("test_light", "colorHSV onError:" + code + error);
    }
    @Override
    public void onSuccess() {
        Log.i("test_light", "colorHSV onSuccess:");
    }
});
```

### 获取当前灯的类型

灯共分为一路灯（仅有白光）、二路灯（白光+冷暖控制）、三路灯（仅有彩光模式）、四路灯（白光+彩光）、五路灯（白光+彩光+冷暖）。

这5种灯具在功能定义上有所区别，在开发相应的UI和控制时有所区别。

该方法可获取当前灯的类型。

**接口说明**

```java
/**
 * 获取当前是几路灯
 *
 * @return {@link LightType}
 */
LightType lightType();
```

其中LightType中定义的类型有：

```java
/**
 * 白光灯，dpCode：bright_value
 */
TYPE_C,
/**
 * 白光+冷暖，dpCode：bright_value + temp_value
 */
TYPE_CW,
/**
 * RGB，dpCode：colour_data
 */
TYPE_RGB,
/**
 * 白光+RGB，dpCode：bright_value + colour_data
 */
TYPE_RGBC,
/**
 * 白光+冷暖+RGB，dpCode：bright_value + temp_value + colour_data
 */
TYPE_RGBCW
```


### 获取当前设备所有功能的值

打开一个设备面板时，需要获取所有功能点值来展示。可通过此接口获取上面提到的LightDataPoint对象。

```java
/**
 * 获取灯所有功能点的值
 */
LightDataPoint getLightDataPoint();
```

### 开关

控制灯的开关

**接口说明**

```java
/**
 * 开灯 or 关灯
 *
 * @param status         true or false
 * @param resultCallback callback
 */
void powerSwitch(boolean status, IResultCallback resultCallback);
```
**参数说明**

| 字段           | 含义                                                         |
| -------------- | ------------------------------------------------------------ |
| status         | true为开                                                     |
| resultCallback | 仅表示此次发送指令成功or失败，真正控制成功需要识别ILightListener中的dp变化 |

### 切换工作模式
控制工作模式的切换。

**接口说明**

```java
/**
 * 切换工作模式
 *
 * @param mode           工作模式
 * @param resultCallback callback
 */
void workMode(LightMode mode, IResultCallback resultCallback);
```

**参数说明**

| 字段           | 含义                                                         |
| -------------- | ------------------------------------------------------------ |
| mode           | 工作模式，其值有MODE_WHITE（白光）, MODE_COLOUR（彩光）, MODE_SCENE（情景模式） |
| resultCallback | 仅表示此次发送指令成功or失败，真正控制成功需要识别ILightListener中的dp变化 |

**调用示例**

如切换到彩光模式：

```java
lightDevice.workMode(LightMode.MODE_COLOUR, new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Log.i("test_light", "workMode onError:" + code + error);
    }

    @Override
    public void onSuccess() {
        Log.i("test_light", "workMode onSuccess");
    }
});
```

注意：部分灯具必须切换到对应的工作模式才可以控制，比如控制彩光，必须先切换到彩光模式才可以发颜色的值。

### 控制灯的亮度

控制亮度

**接口说明**

```java
/**
 * 亮度控制。
 *
 * @param status         亮度的百分比，取值范围0-100
 * @param resultCallback callback
 */
void brightness(int status, IResultCallback resultCallback);
```

**参数说明**

| 字段           | 含义                                                         |
| -------------- | ------------------------------------------------------------ |
| status         | 亮度的百分比                                                 |
| resultCallback | 仅表示此次发送指令成功or失败，真正控制成功需要识别ILightListener中的dp变化 |
### 控制灯的色温（冷暖）

控制灯的冷暖值

**接口说明**

```java
/**
 * 色温控制
 *
 * @param status         色温的百分比，取值范围0-100
 * @param resultCallback callback
 */
void colorTemperature(int status, IResultCallback resultCallback);
```
**参数说明**

| 字段           | 含义                                                         |
| -------------- | ------------------------------------------------------------ |
| status         | 色温的百分比                                                 |
| resultCallback | 仅表示此次发送指令成功or失败，真正控制成功需要识别ILightListener中的dp变化 |


### 设置彩光的颜色
控制彩色灯的颜色

**接口说明**

```java
/**
 * 设置彩灯的颜色
 *
 * @param hue            色调 （范围：0-360）
 * @param saturation     饱和度（范围：0-100）
 * @param value          明度（范围：0-100）
 * @param resultCallback callback
 */
void colorHSV(int hue, int saturation, int value, IResultCallback resultCallback);
```


### 切换场景模式

切换彩灯的情景模式，目前共有四种模式：

```java
LightScene.SCENE_GOODNIGHT为晚安情景；
LightScene.SCENE_WORK为工作情景；
LightScene.SCENE_READ为阅读情景；
LightScene.SCENE_CASUAL为休闲情景；
```

**方法说明**

```java
/**
 * @param lightScene     {@link LightScene}
 * @param resultCallback callback
 */
void scene(LightScene lightScene, IResultCallback resultCallback);
```

### 附录：灯具所有标准功能点

| code               | 名称                  | 数据类型 | 取值描述                                                     | 说明   |
| ------------------ | --------------------- | -------- | ------------------------------------------------------------ | ------ |
| switch_led         | 开关                  | Boolean  | {}                                                           |        |
| work_mode          | 模式                  | Enum     | {"range":["white","colour","scene","music","scene_1","scene_2","scene_3","scene_4"]} |        |
| bright_value       | 亮度                  | Integer  | {"min":25,"scale":0,"unit":"","max":255,"step":1}            |        |
| bright_value_v2    | 亮度                  | Integer  | {"min":10,"scale":0,"unit":"","max":1000,"step":1}           |        |
| temp_value         | 冷暖                  | Integer  | {"min":0,"scale":0,"unit":"","max":255,"step":1}             |        |
| temp_value_v2      | 冷暖                  | Integer  | {"min":0,"scale":0,"unit":"","max":1000,"step":1}            |        |
| colour_data        | 彩光模式数            | Json     | {}                                                           |        |
| colour_data_v2     | 彩光模式数            | Json     | {}                                                           |        |
| scene_data         | 情景模式数            | Json     | {}                                                           |        |
| scene_data_v2      | 情景模式数            | Json     | {}                                                           |        |
| flash_scene_1      | 柔光模式              | Json     | {}                                                           |        |
| flash_scene_2      | 缤纷模式              | Json     | {}                                                           |        |
| flash_scene_3      | 炫彩模式              | Json     | {}                                                           |        |
| flash_scene_4      | 斑斓模式              | Json     | {}                                                           |        |
| music_data         | 音乐灯模式控制        | Json     | {}                                                           |        |
| control_data       | 调节dp控制            | Json     | {}                                                           |        |
| countdown_1        | 倒计时                | Integer  | {"unit":"","min":0,"max":86400,"scale":0,"step":1}           |        |
| scene_select       | 场景选择              | Enum     | {"range":["1","2","3","4","5"]}                              | 老版本 |
| switch_health_read | 健康阅读开关          | Boolean  | {}                                                           | 老版本 |
| read_time          | 健康阅读-阅读时间设置 | Integer  | {"unit":"minute","min":1,"max":60,"scale":0,"step":1}        | 老版本 |
| rest_time          | 健康阅读-休息时间设置 | Integer  | {"unit":"minute","min":1,"max":60,"scale":0,"step":1}        | 老版本 |

### 灯具v1标准指令集说明
| 功能         | 标准code        | 类型    | 标准值                                                       | 示例                                                         | 备注                                                         |
| ------------ | --------------- | ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 开关         | switch_led      | Boolean | true/false   （打开/关闭）                                   | {"switch_led":true}                                          |                                                              |
| 模式         | work_mode       | Enum    | "white"/"colour"/"scene"/"scene_1"/"scene_2"/"scene_3"/"scene_4" | {"work_mode":"scene"}                                        |                                                              |
| 亮度         | bright_value_v1 | Integer | 25-255                                                       | {"bright_value_v1":100}                                      | 亮度值25-255，对应实际亮度10%-100%，最低亮度显示为10%        |
| 色温         | temp_value_v1   | Integer | 0-255                                                        | {"temp_value_v1":100"}                                       | 色温范围0-100，对应实际色温0%-100%，分别对应最暖和最冷的范围取值，实际的色温值依赖于硬件的灯珠规格，比如2700K-6500K |
| 颜色         | colour_data_v1  | String  | Value: ”00112233334455”（长度为14的字符串）<br>00: R<br>11: G<br>22: B<br>3333: H(色调Hue)<br>44: S(饱和度Saturation)<br>55: V(明度Value)** | {"colour_data_v2":"2700000000ff27"}                          | 颜色按照HSV体系传输，也可以通过算法转换为RGB颜色体系<br>参考网址[https://www.rapidtables.com/convert/color/index.html](https://www.rapidtables.com/convert/color/index.html)<br>可以获得RGB (R,G,B): (HEX)(32,64,C8),(DEC)(50,100,200) |
| **情景**     | scene_data      | String  | Value: "00112233334455"（长度为14的字符串）<br>00: R<br>11: G<br>22: B<br>3333: H(色调Hue)<br>44: S(饱和度Saturation)<br>55: V(明度Value) | {"work_mode":"scene","scene_data":"fffcf70168ffff"}          |                                                              |
| **柔光情景** | flash_scene_1   | String  | Value :"00112233445566"（长度为14的字符串）<br>00: 亮度(brightness)<br>11: 色温(color Temperature)<br>22: 变化频率(frequency)<br>33：变化组数(01)<br>445566：R G B | {"work_mode":"scene_1","flash_scene_1":"ffff320100ff00"}     |                                                              |
| **炫彩情景** | flash_scene_3   | String  | 同上                                                         | {"work_mode":"scene_3","flash_scene_3":"ffff320100ff00"}     |                                                              |
| **缤纷情景** | flash_scene_2   | String  | Value:"00112233445566....445566"（长度为14~44的字符串）<br>00: 亮度(brightness)<br>11: 色温(color Temperature)<br>22: 变化频率(frequency)<br>33：变化组数(01~06)<br>445566：R G B<br>注：变化组数有多少，就有多少个445566,组数最大为6组 | {"work_mode":"scene_2","flash_scene_2":"ffff5003ff000000ff000000ff"} |                                                              |
| **斑斓情景** | flash_scene_4   | String  | 同上                                                         | {"work_mode":"scene_4","flash_scene_4":"ffff0505ff000000ff00ffff00ff00ff0000ff"} |                                                              |

### 灯具v2标准指令集说明

| 功能       | 标准code        | 类型    | 标准值                                                       | 示例                                                         | 备注                                                         |
| ---------- | --------------- | ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 开关       | switch_led      | Boolean | true/false   （打开/关闭）                                   | {"switch_led":true}                                          |                                                              |
| 模式       | work_mode       | Enum    | "white"/"colour"/"scene"/"music"白光模式/彩光模式/场景/音乐灯 | {"work_mode":"scene"}                                        | 白光、彩光、场景、音乐灯的TAB栏，由DP点决定：<br><br>“白光”菜单栏：模式和亮度DP共同决定<br>“彩光”菜单栏：模式和颜色DP共同决定<br>“场景”菜单栏：模式和情境DP共同决定<br>“音乐灯”菜单栏：模式和音乐灯DP共同决定<br>“倒计时”：由倒计时DP点决定<br>“定时”：由云功能的，云端定时决定 |
| 亮度       | bright_value_v2 | Integer | 10 – 1000                                                    | {"bright_value_v2":670}                                      | 亮度值10-1000，对应实际亮度1%-100%，最低亮度显示为1%         |
| 色温       | temp_value_v2   | Integer | 0-1000                                                       | {"temp_value_v2":797"}                                       | 色温范围0-1000，对应实际色温0%-100%，分别对应最暖和最冷的范围取值，实际的色温值依赖于硬件的灯珠规格，比如2700K-6500K |
| 颜色       | colour_data_v2  | String  | 000011112222<br><br>值说明：<br><br>0000：H（色度：0-360，0X0000-0X0168）<br><br>1111：S (饱和：0-1000, 0X0000-0X03E8)<br><br>2222：V (明度：0-1000，0X0000-0X03E8)<br><br>HSV (H,S,V): (HEX)(00DC, 004B,004E),转换为(DEC)为(220度,75%,78%) | {"colour_data_v2":"00DC004B004E"}<br><br>                    | 颜色按照HSV体系传输，也可以通过算法转换为RGB颜色体系<br><br>参考网址[https://www.rapidtables.com/convert/color/index.html](https://www.rapidtables.com/convert/color/index.html)<br><br>可以获得RGB (R,G,B): (HEX)(32,64,C8),(DEC)(50,100,200) |
| 情景       | scene_data_v2   | String  | 0011223344445555666677778888<br><br>00：情景号<br><br>11：单元切换间隔时间（0-100）<br><br>22：单元变化时间（0-100）<br><br>33：单元变化模式（0静态1跳变2渐变）<br><br>4444：H（色度：0-360，0X0000-0X0168）<br><br>5555：S (饱和：0-1000, 0X0000-0X03E8)<br><br>6666：V (明度：0-1000，0X0000-0X03E8)<br><br>7777：白光亮度（0-1000）<br><br>8888：色温值（0-1000）<br><br>注：数字1-8的标号对应有多少单元就有多少组 | {"25":"010b0a02000003e803e8000000000b0a02007603e803e8000000000b0a0200e703e803e800000000"} | 01：情景号01<br><br>0b：单元切换间隔时间（0）<br><br>0a：单元变化时间（10）<br><br>02：单元变化模式:渐变<br><br>0000：H（色度：0X0000）<br><br>03e8：S (饱和：0-1000, 0X0000-0X03E8)<br><br>03e8：V (明度：0-1000，0X0000-0X03E8)<br><br>0000：白光亮度（0-1000）<br><br>0000：色温值（0-1000） |
| 倒计时     | countdown_1     | Integer | **0-86400**<br>**说明：**<br>数据单位秒，对应一分钟取值60，最大设置86400=23小时59分钟<br>0表示关闭 | **{ "**countdown_1**":"120" }**                              |                                                              |
| 音乐       | music_data      | String  | 011112222333344445555<br>说明：<br>0：   变化方式，0表示直接输出，1表示渐变<br><br>1111：H（色度：0-360，0X0000-0X0168）<br><br>2222：S (饱和：0-1000, 0X0000-0X03E8)<br><br>3333：V (明度：0-1000，0X0000-0X03E8)<br><br>4444：白光亮度（0-1000）<br><br>5555：色温值（0-1000） | {"music_data":"1007603e803e800120025"}<br><br>0：   变化方式，0表示直接输出，1表示渐变<br><br>示例说明：<br><br>1：   变化方式， 1表示渐变<br><br>0076：H（色度： 0X0076）<br><br>03e8：S (饱和：0X03e8)<br><br>03e8：：V (明度： 0X03e8)<br><br>0012：亮度（18%）<br><br>0025：色温（37%） | 该功能点和模式功能点一起，决定是否显示音乐灯                 |
| 调节DP控制 | control_data    | String  | 0：   变化方式，0表示直接输出，1表示渐变<br>1111：H（色度：0-360，0 X0000-0X0168 ）<br>2222： S  ( 饱和：0 -1000,  0X0000-0X03E8)<br>3333： V  ( 明度：0-1000，0 X0000-0X03E8)<br>4444：白光亮度（0-1000）<br>5555：色温值（0-1000）<br> | **{** **"**control_data**":"** **10076** **03e803e800120025"** **}**<br>1：   变化方式， 1表示渐变<br>0076 ：H（色度： 0 X0076 ）<br>03e8 ： S  ( 饱和： 0X03e8)<br>03e8 ：： V  ( 明度： 0 X03e8)<br>0012 ：亮度（ 18 %）<br>0025 ：色温（ 37 %） | 该DP用于面板调节过程中实时数据下发                           |
| 情景变化   | scene_data_v2   | String  | 情景的具体格式如下：<br>00    11     22     33   444455556666   77778888<br><br>Tab  time  speed  mode   color(hsv)   white(bright,temper)<br><br>除了Tab之外，其余部分组成基本的情景单元，目前最大可设置8个<br><br>即 00  +（11223344445555666677778888）* 8<br><br>Time与speed的关系：<br><br>渐变与单元切换目前为同时进行，可以达到三种变化状态（分布进行只有两种）<br><br>A:同步完成：<br><br>要实现渐变完成后马上切换下一个单元，发送time = speed即可<br><br>B:渐变后停滞等待<br><br>例如，将实现1s完成渐变，并停滞1s在继续渐变。发送time = 2，speed = 1即可<br><br>C:提前切换<br><br>当渐变时间小于切换时间，会发生在变化之中切换到下一个目标状态开始渐变，实现一种比较随机的变化效果。发送time<speed<br><br>Time（各个情景单元间切换时间）0 – 100（单位：100ms）<br><br>可以实现100ms – 10s的切换间隔，为0时表示直接切换到下一个单元，但从此单元的状态进行过度<br><br>具体如下图：<br><br>以上图为例，当单元B的time为0时，可以相当于变化是A到C的过程，但是在中间节点X处灯光状态切换到B继续变化<br><br>Speed（情景单元从上个状态切换到目标状态的渐变速度）0-100（单位HZ）<br><br>注：目前具体实现依旧使用时间为单位，所以最快为1（10khz），最慢为100（100hz），需不需修改后续商议。<br><br>新版灯光的渐变步进为1000，所以速度 = 时间变化时间/1000<br><br>设变化时间为t<br><br>例如，当speed下发1，s = 0.1s/1000 = 10Khz<br><br>当speed = 0时，当前单元的变化会停滞，直到单元切换时间T到，切换到下一个目标情景单元才会开始从停滞单元变化到新的单元<br><br>Mode为当前情景单元过度到下一个目标情景单元的变化方式，有0，1，2三种方式<br><br>Mode = 0：静态情景，当目标情景的模式为0时，当此变化完成便停止情景变化，成为静态情景。变化速度目前固定为正常白光和彩光的呼吸时间（500ms）。<br><br>Mode = 1：跳变模式，当单元切换时间一到，直接切换为新的情景单元<br><br>Mode = 2：渐变模式，当线程读取到了当前情景单元中数据由上一个情景单元渐变到新的情景<br><br>新版灯的情景较旧版本只能点亮彩灯，现在支持白光灯和彩灯同时点亮的功能 |                                                              |                                                              |






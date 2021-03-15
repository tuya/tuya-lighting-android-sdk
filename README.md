# tuyasmart-lighting-android-sdk

### **[English]([README.md](http://readme.md/)) | [中文版]([README_cn.md](http://readme_cn.md/))**

The lighting equipment is more complicated, and there are both v1 and v2 new and old firmware. Even if standard commands are used, two sets of control logic need to be developed.

Therefore, the functions of the lighting equipment are encapsulated, and the switches, working mode switching, brightness control, cooling and heating control, color light control and the control of the four scene modes of the lighting equipment are encapsulated.

#### Functional Overview

Tuya Smart Lighting Android SDK is based on Tuya SmartDevice, which expands the interface package for accessing lighting equipment related functions to speed up the development process. Mainly includes the following functions:

* Get the current device is a few street lights
* Get the value of all function points of the lamp
* Turn the lights on or off
* Switch working mode
* Control the brightness of the light
* Control the color temperature of the lamp
* Switch scene mode
* Set the color of the lantern

#### Quick integration

# Dependency description

```
implementation 'com.tuya.smart:tuyasmart:3.22.0'
// Control SDK dependencies
implementation 'com.tuya.smart:tuyasmart-centralcontrol:1.0.3'
```

It should be noted that tuyasmart-centralcontrol uses kotlin to compile, and the kotlin library needs to be imported to ensure its normal use.

> If Kotlin has been introduced in the project, the following configuration can be ignored.

**kotlin**

```
buildscript {
    ext.kotlin_version = '1.3.72'
    dependencies {
    	...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```

Introduce the kotlin plugin and kotlin package in the app's build.gradle:

```
apply plugin: 'kotlin-android'
dependencies {
	implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
```

### Device initialization

First, create an ITuyaLightDevice object, and the lamp-related methods are encapsulated in this method.

```
ITuyaLightDevice lightDevice = new TuyaLightDevice(String devId);
```

**Parameter Description**

| parameter | Description |
| --------- | :---------- |
| devId     | Device id   |

**Sample code**

```java
// init lightDevice
ITuyaLightDevice lightDevice = new TuyaLightDevice("vdevo159793004250542");
```

This object encapsulates all dp points of the lamp, including the issuance and reporting of control commands.

### Device agent monitoring

**Method description**

```java
/**
 * register Listener
 */
void registerLightListener(ILightListener listener);
```

Among them, the ILightListener callback is as follows:

```java
public interface ILightListener {
    /**
     * Monitor dp point changes of lighting equipment
     *
     * @param dataPoint The state of all dp points of the fixture
     */
    void onDpUpdate(LightDataPoint dataPoint);

    /**
     * remove device
     */
    void onRemoved();

    /**
     * device ststus
     */
    void onStatusChanged(boolean online);

    /**
     * network status
     */
    void onNetworkStatusChanged(boolean status);

    /**
     * Device information update such as name and the like
     */
    void onDevInfoUpdate();
}
```

**Sample code**

```java
// create lightDevice
ITuyaLightDevice lightDevice = new TuyaLightDevice("vdevo159793004250542");

// register listener
lightDevice.registerLightListener(new ILightListener() {
    @Override
    public void onDpUpdate(LightDataPoint dataPoint) { // return LightDataPoint，Contains the values of all function points of the lamp
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

**`TuyaSmartLightDataPointModel` Data model**

| Field            | Types           | description                                                  |
| ---------------- | --------------- | ------------------------------------------------------------ |
| powerSwitch      | boolea          | Whether the switch is on, YES means on, NO means off         |
| workMode         | LightMode       | Working mode, MODE_WHITE is white light mode; MODE_COLOUR is color light mode; MODE_SCENE is scene mode; |
| brightness       | int             | Brightness percentage，从1-100                               |
| colorTemperature | int             | Color temperature percentage，从0-100                        |
| colourHSV        | LightColourData | Color value, HSV color space; H is hue, value range is 0-360; S is saturation, value range is 0-100; V is lightness, value range is 1-100 |
| scene            | LightScene      | Lantern scene, * SCENE_GOODNIGHT is good night scene; SCENE_WORK is work scene; SCENE_READ is reading scene; SCENE_CASUAL is leisure scene; |

**Parameter Description**

It is worth noting that the `LightDataPoint` object, which encapsulates all the function points of the current device. When the function point changes, it will be called back. Each callback will be a complete object.

The following is the specific meaning of the object parameters:

```java
public class LightDataPoint {
    /**
     * switch
     */
    public boolean powerSwitch;
    /**
     * Operating mode。
     * <p>
     * MODE_WHITE  White light mode；
     * MODE_COLOUR  color light mode；
     * MODE_SCENE Scene mode；
     */
    public LightMode workMode;

    /**
     * Brightness percentage，0~100
     */
    public int brightness;

    /**
     * Color temperature percentage，0~100
     */
    public int colorTemperature;

    /**
     * Color value, HSV color space.
     * <p>
     * H，0-360；
     * S，0-100；
     * V，0-100；
     */
    public LightColourData colorHSV;
    /**
     * Lantern scene
     *
     * SCENE_GOODNIGHT Good night scene；
     * SCENE_WORK Work situation；
     * SCENE_READ Reading situation；
     * SCENE_CASUAL Casual scene；
     */
    public LightScene scene;
}
```

**Sample Code**

```
// Turn on the lights
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
// Good night scene
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
// Set color
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

### Get the current lamp type

The lights are divided into one road light (only white light), two road lights (white light + cooling and heating control), three road lights (only color light mode), four road lights (white light + color light), five lights (white light + color light + cold and warm) .

These 5 kinds of lamps are different in function definition, and they are different in the development of corresponding UI and control.

This method can get the current lamp type.

**Interface Description**

```java
/**
 * Get the current number of lights
 *
 * @return {@link LightType}
 */
LightType lightType();
```

Among them, the types defined in LightType are：

```java
/**
 * White light，dpCode：bright_value
 */
TYPE_C,
/**
 * White light + cold and warm，dpCode：bright_value + temp_value
 */
TYPE_CW,
/**
 * RGB，dpCode：colour_data
 */
TYPE_RGB,
/**
 * White light+RGB，dpCode：bright_value + colour_data
 */
TYPE_RGBC,
/**
 * White light+cold and warm+RGB，dpCode：bright_value + temp_value + colour_data
 */
TYPE_RGBCW
```

### Get the values of all functions of the current device

When opening a device panel, you need to get all the function point values to display. The LightDataPoint object mentioned above can be obtained through this interface.

```java
/**
 * Get the value of all function points of the lamp
 */
LightDataPoint getLightDataPoint();
```

### switch

Control the light switch

**Interface Description**

```java
/**
 * Light on or off
 *
 * @param status         true or false
 * @param resultCallback callback
 */
void powerSwitch(boolean status, IResultCallback resultCallback);
```

**Parameter Description**

| Field          | meaning                                                      |
| -------------- | ------------------------------------------------------------ |
| status         | true is on                                                   |
| resultCallback | It only means that the sending command is successful or failed. The real control success needs to identify the dp change in ILightListener |

### Switch working mode

Control the switching of working modes

**Interface Description**

```java
/**
 * Switch working mode
 *
 * @param mode           Operating mode
 * @param resultCallback callback
 */
void workMode(LightMode mode, IResultCallback resultCallback);
```

**Parameter Description**

| Field          | meaning                                                      |
| -------------- | ------------------------------------------------------------ |
| mode           | Working mode, its values are MODE_WHITE (white light), MODE_COLOUR (color light), MODE_SCENE (scene mode) |
| resultCallback | It only means that the sending command is successful or failed. The real control success needs to identify the dp change in ILightListener |

**Call example**

If switch to IPL mode:

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

Note: Some lamps and lanterns must be switched to the corresponding working mode before they can be controlled.

### Control the brightness of the light

Control brightness

**Interface Description**

```java
/**
 * Brightness control.
 *
 * @param status         Percentage of brightness, value range 0-100
 * @param resultCallback callback
 */
void brightness(int status, IResultCallback resultCallback);
```

**Parameter Description**

| Field          | meaning                                                      |
| -------------- | ------------------------------------------------------------ |
| status         | Percentage of brightness                                     |
| resultCallback | It only means that the sending command is successful or failed. The real control success needs to identify the dp change in ILightListener |

### Control the color temperature of the light (warm and cold)

Control the heating and cooling value of the lamp

**Interface Description**

```java
/**
 * Color temperature control
 *
 * @param status         Percentage of color temperature, value range 0-100
 * @param resultCallback callback
 */
void colorTemperature(int status, IResultCallback resultCallback);
```

**Parameter Description**

| Field          | meaning                                                      |
| -------------- | ------------------------------------------------------------ |
| status         | Percentage of color temperature                              |
| resultCallback | It only means that the sending command is successful or failed. The real control success needs to identify the dp change in ILightListener |


### Set the color of the IPL

Control the color of colored lights

**Interface Description**

```java
/**
 * Set the color of the lantern
 *
 * @param hue            tone （0-360）
 * @param saturation     saturation（0-100）
 * @param value          Lightness（0-100）
 * @param resultCallback callback
 */
void colorHSV(int hue, int saturation, int value, IResultCallback resultCallback);
```

### Switch scene mode

Switch the scene mode of the colorful lights. There are currently four modes:

```java
LightScene.SCENE_GOODNIGHT Good night scene；
LightScene.SCENE_WORK Work situation；
LightScene.SCENE_READ Reading situation；
LightScene.SCENE_CASUAL Casual scene；
```

**Interface Description**

```java
/**
 * @param lightScene     {@link LightScene}
 * @param resultCallback callback
 */
void scene(LightScene lightScene, IResultCallback resultCallback);
```

### Appendix: All standard function points of lamps

| code               | Name                                 | Data type | Value description                                            | Description |
| ------------------ | ------------------------------------ | --------- | ------------------------------------------------------------ | ----------- |
| switch_led         | switch                               | Boolean   | {}                                                           |             |
| work_mode          | mode                                 | Enum      | {"range":["white","colour","scene","music","scene_1","scene_2","scene_3","scene_4"]} |             |
| bright_value       | brightness                           | Integer   | {"min":25,"scale":0,"unit":"","max":255,"step":1}            |             |
| bright_value_v2    | brightness                           | Integer   | {"min":10,"scale":0,"unit":"","max":1000,"step":1}           |             |
| temp_value         | Warm and cold                        | Integer   | {"min":0,"scale":0,"unit":"","max":255,"step":1}             |             |
| temp_value_v2      | Warm and cold                        | Integer   | {"min":0,"scale":0,"unit":"","max":1000,"step":1}            |             |
| colour_data        | Number of IPL Modes                  | Json      | {}                                                           |             |
| colour_data_v2     | Number of IPL Modes                  | Json      | {}                                                           |             |
| scene_data         | Number of scene modes                | Json      | {}                                                           |             |
| scene_data_v2      | Number of scene modes                | Json      | {}                                                           |             |
| flash_scene_1      | Soft light mode                      | Json      | {}                                                           |             |
| flash_scene_2      | Fun mode                             | Json      | {}                                                           |             |
| flash_scene_3      | Colorful mode                        | Json      | {}                                                           |             |
| flash_scene_4      | Gorgeous mode                        | Json      | {}                                                           |             |
| music_data         | Music light mode control             | Json      | {}                                                           |             |
| control_data       | Adjust dp control                    | Json      | {}                                                           |             |
| countdown_1        | Countdown                            | Integer   | {"unit":"","min":0,"max":86400,"scale":0,"step":1}           |             |
| scene_select       | Scene selection                      | Enum      | {"range":["1","2","3","4","5"]}                              | old version |
| switch_health_read | Healthy reading switch               | Boolean   | {}                                                           | old version |
| read_time          | Healthy reading-reading time setting | Integer   | {"unit":"minute","min":1,"max":60,"scale":0,"step":1}        | old version |
| rest_time          | Healthy reading-rest time setting    | Integer   | {"unit":"minute","min":1,"max":60,"scale":0,"step":1}        | old version |

### Lamp v1 standard instruction set description

| Features          | code            | Types   | standard value                                               | Example                                                      | Remarks                                                      |
| ----------------- | --------------- | ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| switch            | switch_led      | Boolean | true/false   （on/off）                                      | {"switch_led":true}                                          |                                                              |
| mode              | work_mode       | Enum    | "white"/"colour"/"scene"/"scene_1"/"scene_2"/"scene_3"/"scene_4" | {"work_mode":"scene"}                                        |                                                              |
| brightness        | bright_value_v1 | Integer | 25-255                                                       | {"bright_value_v1":100}                                      | Brightness value 25-255, corresponding to actual brightness 10%-100%, the lowest brightness display is 10% |
| Color temperature | temp_value_v1   | Integer | 0-255                                                        | {"temp_value_v1":100"}                                       | The color temperature range is 0-100, corresponding to the actual color temperature 0%-100%, corresponding to the warmest and coldest ranges respectively. The actual color temperature value depends on the hardware specifications, such as 2700K-6500K |
| colour            | colour_data_v1  | String  | Value: ”00112233334455”（A string of length 14）<br>00: R<br>11: G<br>22: B<br>3333: H(Hue)<br>44: S(Saturation)<br>55: V(Value)** | {"colour_data_v2":"2700000000ff27"}                          | The color is transmitted according to the HSV system, or it can be converted to the RGB color system through an algorithm.<br/>Refer to the URL [https://www.rapidtables.com/convert/color/index.html](https://www.rapidtables.com/ convert/color/index.html)<br/>You can get RGB (R,G,B): (HEX)(32,64,C8),(DEC)(50,100,200) |
| scene             | scene_data      | String  | Value: "00112233334455"（A string of length 14）<br>00: R<br>11: G<br>22: B<br>3333: H(Hue)<br>44: S(Saturation)<br>55: V(Value) | {"work_mode":"scene","scene_data":"fffcf70168ffff"}          |                                                              |
| Soft light scene  | flash_scene_1   | String  | Value :"00112233445566"（A string of length 14）<br>00: brightness(brightness)<br>11: Color temperature(color Temperature)<br>22: Frequency of change(frequency)<br>33：Change the number of groups(01)<br>445566：R G B | {"work_mode":"scene_1","flash_scene_1":"ffff320100ff00"}     |                                                              |
| Colorful scene    | flash_scene_3   | String  | Same as above                                                | {"work_mode":"scene_3","flash_scene_3":"ffff320100ff00"}     |                                                              |
| Colorful scene    | flash_scene_2   | String  | Value:"00112233445566....445566"（A string with a length of 14~44）<br>00: brightness(brightness)<br>11: Color temperature(color Temperature)<br>22: Frequency of change(frequency)<br>33：Change the number of groups(01~06)<br>445566：R G B<br>Note: as many groups as there are changes, there are as many as 445566, and the maximum number of groups is 6 groups | {"work_mode":"scene_2","flash_scene_2":"ffff5003ff000000ff000000ff"} |                                                              |
| Colorful scene    | flash_scene_4   | String  | Same as above                                                | {"work_mode":"scene_4","flash_scene_4":"ffff0505ff000000ff00ffff00ff00ff0000ff"} |                                                              |

### Lamp v2 standard instruction set description

| Features           | code            | Type    | standard value                                               | Example                                                      | Remarks                                                      |
| ------------------ | --------------- | ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| switch             | switch_led      | Boolean | true/false   （on/off）                                      | {"switch_led":true}                                          |                                                              |
| mode               | work_mode       | Enum    | "white"/"colour"/"scene"/"music"                             | {"work_mode":"scene"}                                        | The TAB column of white light, color light, scene and music light is determined by DP point: <br/><br/>"White light" menu bar: mode and brightness DP are jointly determined<br/>"Color light" menu bar: mode and color DP Joint decision<br/>"Scene" menu bar: Mode and context DP jointly decided<br/>"Music light" menu bar: Mode and music light DP jointly decided<br/>"Countdown": Decided by countdown DP point<br/>" Timing": Determined by cloud function and cloud timing |
| brightness         | bright_value_v2 | Integer | 10 – 1000                                                    | {"bright_value_v2":670}                                      | Brightness value 10-1000, corresponding to actual brightness 1%-100%, the lowest brightness display is 1% |
| Color temperature  | temp_value_v2   | Integer | 0-1000                                                       | {"temp_value_v2":797"}                                       | The color temperature range is 0-1000, corresponding to the actual color temperature 0%-100%, corresponding to the warmest and coldest range values respectively. The actual color temperature value depends on the hardware specifications, such as 2700K-6500K |
| colour             | colour_data_v2  | String  | 000011112222<br><br>Value description：<br><br>0000：H（Chroma：0-360，0X0000-0X0168）<br><br>1111：S (saturation：0-1000, 0X0000-0X03E8)<br><br>2222：V (Lightness：0-1000，0X0000-0X03E8)<br><br>HSV (H,S,V): (HEX)(00DC, 004B,004E),Convert to(DEC):(220,75%,78%) | {"colour_data_v2":"00DC004B004E"}<br><br>                    | The color is transmitted according to the HSV system, and can also be converted to the RGB color system through an algorithm.<br/><br/>Reference URL [https://www.rapidtables.com/convert/color/index.html](https://www.rapidtables .com/convert/color/index.html)<br/><br/>You can get RGB (R,G,B): (HEX)(32,64,C8),(DEC)(50,100,200) |
| scene              | scene_data_v2   | String  | 0011223344445555666677778888<br><br>00：Scene number<br><br>11：Unit switching interval time（0-100）<br><br>22：Unit change time（0-100）<br><br>33：Unit change mode (0 static 1 jump 2 gradual change)<br><br>4444：H（Chroma：0-360，0X0000-0X0168）<br><br>5555：S (saturation：0-1000, 0X0000-0X03E8)<br><br>6666：V (Lightness：0-1000，0X0000-0X03E8)<br><br>7777：White light brightness（0-1000）<br><br>8888：Color temperature（0-1000）<br><br>Note: The numbers 1-8 correspond to as many groups as there are units | {"25":"010b0a02000003e803e8000000000b0a02007603e803e8000000000b0a0200e703e803e800000000"} | 01: Scene number 01<br/><br/>0b: Unit switching interval time (0)<br/><br/>0a: Unit change time (10)<br/><br/>02: Unit change mode: Gradual<br/> <br/>0000: H (Chroma: 0X0000)<br/><br/>03e8: S (Saturation: 0-1000, 0X0000-0X03E8)<br/><br/>03e8: V (Lightness: 0-1000, 0X0000- 0X03E8)<br/><br/>0000: White light brightness (0-1000)<br/><br/>0000: Color temperature value (0-1000) |
| Countdown          | countdown_1     | Integer | **0-86400**<br>**Description：**<br>The data unit is second, which corresponds to a value of 60 for one minute, and the maximum setting is 86400=23 hours and 59 minutes.<br/>0 means off | **{ "**countdown_1**":"120" }**                              |                                                              |
| music              | music_data      | String  | 011112222333344445555<br>Description：<br>0：   Change mode, 0 means direct output, 1 means gradual change<br><br>1111：H（Chroma：0-360，0X0000-0X0168）<br><br>2222：S (saturation：0-1000, 0X0000-0X03E8)<br><br>3333：V (Lightness：0-1000，0X0000-0X03E8)<br><br>4444：White light brightness（0-1000）<br><br>5555：Color temperature（0-1000） | {"music_data":"1007603e803e800120025"}<br><br>0：   Change mode, 0 means direct output, 1 means gradual change<br><br>Example description：<br><br>1：   Change mode, 1 means gradual change<br><br>0076：H（Chroma： 0X0076）<br><br>03e8：S (saturation：0X03e8)<br><br>03e8：：V (Lightness： 0X03e8)<br><br>0012：brightness（18%）<br><br>0025：Color temperature（37%） | This function point and the mode function point together determine whether to display the music light |
| Adjust DP control  | control_data    | String  | 0：  Change mode, 0 means direct output, 1 means gradual change<br>1111：H（Chroma：0-360，0 X0000-0X0168 ）<br>2222： S  ( saturation：0 -1000,  0X0000-0X03E8)<br>3333： V  (Lightness：0-1000，0 X0000-0X03E8)<br>4444：White light brightness（0-1000）<br>5555：Color temperature（0-1000）<br> | **{** **"**control_data**":"** **10076** **03e803e800120025"** **}**<br>1：   Change mode, 1 means gradual change<br>0076 ：H（Chroma： 0 X0076 ）<br>03e8 ： S  ( saturation： 0X03e8)<br>03e8 ：： V  ( Lightness： 0 X03e8)<br>0012 ：brightness（ 18 %）<br>0025 ：Color temperature（ 37 %） | This DP is used for real-time data delivery during panel adjustment |
| Situational change | scene_data_v2   | String  | The specific format of the scenario is as follows: <br/>00 11 22 33 444455556666 77778888<br/><br/>Tab time speed mode color(hsv) white(bright,temper)<br/><br/>Except for Tab, the rest of the composition is basic At present, the maximum number of scene units can be set to 8<br/><br/>that is 00 + (11223344445555666677778888)* 8<br/><br/>The relationship between Time and speed: <br/><br/>The gradual change and unit switching are currently performed simultaneously , Can reach three change states (only two distributions are carried out)<br/><br/>A: Synchronization is completed: <br/><br/>To switch to the next unit immediately after the gradual change is completed, send time = speed<br ><br/>B:Stopping and waiting after gradual change<br/><br/>For example, it will achieve 1s to complete the gradual change, and stagnate for 1s to continue the gradual change. Send time = 2, speed = 1<br/><br/>C: Switch in advance<br/><br/>When the gradual change time is less than the switching time, it will switch to the next target state to start gradual change during the change, achieving one A relatively random effect of change. Send time<speed<br/><br/>Time (switching time between each scene unit) 0-100 (unit: 100ms)<br/><br/>The switching interval of 100ms-10s can be realized, when it is 0, it means to switch directly to the next One unit, but transition from the state of this unit<br/><br/>The specific figure is as follows:<br/><br/>The above figure is an example, when the time of unit B is 0, it can be equivalent to the process of changing from A to C , But at the intermediate node X, the light state switches to B and continues to change<br/><br/>Speed (the gradual speed of the scene unit switching from the last state to the target state) 0-100 (unit HZ)<br/><br/>Note : The current implementation is still using time as a unit, so the fastest is 1 (10khz), and the slowest is 100 (100hz), no need to modify the follow-up negotiation. <br/><br/>The gradual step of the new version of the light is 1000, so speed = time change time/1000<br/><br/>Set the change time to t<br/><br/>For example, when speed is issued 1, s = 0.1s/1000 = 10Khz<br/><br/>When speed = 0, the change of the current unit will be stagnant, until the unit switching time T is reached, switching to the next target scenario unit will start to change from the stagnant unit to the new unit <br/><br/>Mode is the change mode from the current scene unit to the next target scene unit. There are three ways: 0, 1, 2<br/><br/>Mode = 0: static scene, when the target scene mode is At 0 o'clock, when the change is completed, the scene change will stop and become a static scene. The speed of change is currently fixed to the breathing time of normal white light and colored light（500ms）。<br><br>Mode = 1：Jump mode, when the unit switching time comes, directly switch to the new scenario unit<br/><br/>Mode = 2: Gradual mode, when the thread reads the data in the current scenario unit, it changes from the previous scenario unit to the new one Scenes<br/><br/>The scenes of the new version of the lights can only light up the colored lights, and now it supports the function of white light and colored lights at the same time. |                                                              |                                                              |



### 
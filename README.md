# Simple-Analog-Clock-View
Simple customizable analog clock view especially useful for small sizes. Compatible with Sdk v16 and higher.

![](https://media.giphy.com/media/VcxL0025VcDPVpLWsQ/giphy.gif)

## Installation
Add JitPack repository in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```gradle
implementation 'com.github.alperenbabagil:simpleanimationpopup:1.0.3'
```

## Usage
A demo app also included in the project. Basic use cases and implementations showed in it.

Simply add SimpleAnalogClockView to your layout xml like this:

```xml
<com.alperenbabagil.simpleanalogclockviewlibrary.SimpleAnalogClockView
                android:layout_width="100dp"
                android:layout_height="100dp"
                app:hour="4"
                app:minute="20"
                android:layout_margin="10dp"
                />
```

You can set time by code as well like this:

```kotlin
clockview.updateTime(Date())
```
or
```kotlin
clockview.hour=16
clockview.minute=20
```

changes are immediately applied

## Customization
You can customize clock via xml values. Properties are thickness,color and hand lengths.

### Thickness

You can set thickness of hour hand,minute hand and border by changing `hour_hand_thickness`,
`minute_hand_thickness` and `border_thickness` properties in xml. 
Additionally you can set all values via one property: `all_items_thickness`

### Color

You can set color of hour hand,minute hand and border by changing `hour_hand_color`,
`minute_hand_color` and `border_color` properties in xml. 
Additionally you can set all values via one property: `all_items_color`

### Hand length

You can set ratio of hour hand-minute hand length / radius length. 
It can be changed via `hour_hand_r_ratio` and `hour_hand_r_ratio` properties in xml.


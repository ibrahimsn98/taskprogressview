# TaskProgressView

A lightweight calendar task progress view library for Android

[![](https://jitpack.io/v/ibrahimsn98/taskprogressview.svg)](https://jitpack.io/#ibrahimsn98/taskprogressview)
[![](https://androidweekly.net/issues/issue-446/badge)](https://androidweekly.net/issues/issue-446)


## GIF

In-app preview

<img src="https://github.com/ibrahimsn98/taskprogressview/blob/master/art/gif.gif" width="480" />

## XML Attributes

```xml
<me.ibrahimsn.lib.TaskProgressView
    android:id="@+id/taskProgressView"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_marginTop="40dp"
    app:rangeLength="7"
    app:sidePadding="26dp"
    app:taskLineWidth="10dp"
    app:taskLineSpacing="32dp" />
```

## Usage

```kotlin
val taskProgressView = findViewById<TaskProgressView>(R.id.taskProgressView)

// Setup with task list
taskProgressView.setTasks(listOf())

// Focus to any date range by giving head of the range
taskProgressView.focusRange(Calendar.getInstance())
```

## TODO

- [x] Horizontal scrolling feature 
- [ ] Different range types like week, month, year
- [ ] Task description popup


## Setup

> Follow me on Twitter [@ibrahimsn98](https://twitter.com/ibrahimsn98)

Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.ibrahimsn98:taskprogressview:1.0.1'
}
```

## Design Credits

All design and inspiration credits belong to [Mustofa Amar](https://dribbble.com/mustofaamar).


## License
```
MIT License

Copyright (c) 2020 İbrahim Süren

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
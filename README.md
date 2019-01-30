# OpenGE使用文档 1.0
[TOC]
## 1.简介
 **OpenGE**是一个图像绘制的基于安卓api的**Java**库，通过**GView**，**GViewGroup**( ++本版本只是做了简单的封装，待迭代，只提供一个简单的**FrameGLayout**++ )进行扩展绘制，由**GraffitiBoardView**进行管理绘制，**事件分发**等。其交互行为类似安卓中视图绘制机制，**GraffitiBoardView** 相当于**DecorView** ，**GView**相当于**View**而**GViewGroup**相当于**ViewGroup.** 还有一个**ComponentGView**，通常来表示++非几何图形组件++类。如**文字**，**图片**等。其中， 默认目前绘制的形状有: 
- #### 几何图形类型(GView)  
##### 1. 画笔(PenGView)
##### 2. 箭头(ArrowGView)
##### 3. 圈圈(OvalGView)

- #### 非几何图形类型(ComponentGView) 

##### 1.文字(TextGView)
##### 2.图像(ImageCGView)


## 使用部分
### 1.操作模式

使用绘制几何图形类型时候需要切换到**MODE_DRAW**，如果是添加ComponentGView时候需要切换到**MODE_SELECTED**

```java
       
    graffitiBoardView.setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_DRAW);
              //or
    graffitiBoardView.setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_SELECTED);
       
```


# OpenGE使用文档 1.0

[TOC]
## 效果图

![image](https://github.com/Sam474850601/OpenGE/blob/master/photo3.jpg)

## 1.简介
 **OpenGE**是一个图像绘制的基于安卓api的**Java**库，已做到绘制的图像可进行选择删除程度。通过**GView**，**GViewGroup**( ++本版本只是做了简单的封装，待迭代，只提供一个简单的**FrameGLayout**++ )进行扩展绘制，由**GraffitiBoardView**进行管理绘制，**事件分发**等。其交互行为类似安卓中视图绘制机制，**GraffitiBoardView** 相当于**DecorView** ，**GView**相当于**View**而**GViewGroup**相当于**ViewGroup.** 还有一个**ComponentGView**，通常来表示++非几何图形组件++类。如**文字**，**图片**等。其中， 默认目前绘制的形状有: 
- #### 几何图形类型(GView)  
##### 1. 画笔(PenGView)
##### 2. 箭头(ArrowGView)
##### 3. 圈圈(OvalGView)

- #### 非几何图形类型(ComponentGView) 
##### 1. 文字(TextGView)
##### 2. 图像(ImageCGView)

- ### 其他图形自己继承GView或ComponentGView绘制即可

## 类图

![image](https://github.com/Sam474850601/OpenGE/blob/master/classview.png)

## 使用部分
### 1.操作模式

使用绘制几何图形类型时候需要切换到**MODE_DRAW**，如果是添加ComponentGView时候需要切换到**MODE_SELECTED**

```java
       
    graffitiBoardView.setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_DRAW);
              //or
    graffitiBoardView.setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_SELECTED);
       
```

### 添加布局类型，非几何图形类型实例

```
    // 目前只支持这个布局，待扩展
    FrameGLayout frameGLayout = new FrameGLayout()
    //如添加文字
    TextGView text =   new TextGView();
    text.setText("啦啦啦");
    layout.addView(this)
    graffitiBoardView.addComponentGView(frameGLayout); 
   
```

### 添加非几何图形类型实例

```

 graffitiBoardView.setCurrentDrawingGraphical(com.to8to.graphic.engine.gviews.OvalGView::class.java) 
 
```

具体演示详情看demo


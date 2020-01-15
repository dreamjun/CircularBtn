自定义圆形七彩按钮 CircularBtton<br>
======
效果图<br>
---
![](https://github.com/dreamjun/CircularBtn/blob/master/click.png)<br>

注意按钮可以设置可以点击和不能可以点击的效果<br>
----
```Java
setClick(boolean click)
```
效果图<br>
---
![](https://github.com/dreamjun/CircularBtn/blob/master/noclick.png)<br>

自定义点击回调<br>
---
```Java
 public boolean onTouchEvent(MotionEvent event) {
        if (isClick) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    for (Pie pie : mList) {
                        pie.setTouch(false);
                        if (pie.isInRegion(downX, downY)) {
                            pie.setTouch(!pie.isTouch());
                            postInvalidate();
                            if (pieCharViewClick != null) {
                                pieCharViewClick.click(pie);
                            }
                        }
                    }


                    break;
            }

        }
        return true;
    }

可以根据项目自定义  

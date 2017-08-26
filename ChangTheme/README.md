### 问题
- 如何获取插件包中的资源
- 如何捕获需要换肤的控件
- 如何为指定控件的指定属性换成资源包的特定属性

### 类结构
- 属性相关
    - `SkinAttrType`：枚举需要换肤的类型，每个类型都有相应的apply()方法实现最终的资源替换。
    - `SkinAttr` ：将需要换肤的资源名称以及资源类型进行数据封装绑定。
    - `SkinView`：将需要换肤的View以及其需要替换资源的属性进行数据绑定封装。
    - `SkinAttrSupport`：获取当前View所有需要替换资源的属性，并将之封装为`SkinAttr`的集合返回
- 管理相关
    - `ResourceManager`：根据资源名称管理资源文件的获取。
    - `SkinManager`：（单例类）
        - 管理换肤的`ResourceManager`,对于不同的换肤方式返回不同的`ResouceManager`。
        - 配合`PreUtils`进行换肤数据的管理
        - 对`Activity`和`SkinView`进行键值对的数据绑定
        - 换肤逻辑的控制
- `BaseSkinActivity`：所有`Activity`的基类，在其`LayoutInflaterFactory`的`onCreateView()`方法中捕获需要换肤的控件。

### 实现原理
- **获取插件包中的资源**：通过`AssetManager`类的`addAssetPath()`添加插件资源路径，然后通过`new Resources(mAssetManager,superResource.getDisplayMetrics()
                    ,superResource.getConfiguration())`获取到插件包的`Reasource`,然后通过`getDrawable()`、`getColorStateList()`等方法获取插件包中的资源。
- **捕获需要换肤的控件**：给需要换肤的控件的资源名称前加入`前缀`，重写`LayoutInflaterFactory`的`onCreateView()`方法获取所有组件的属性名和属性值，然后进行判断，将需要换肤的控件的资源名称和资源类型加入到`SkinAttr`中，然后再将`View`和`SkinAttr`绑定加入`SkinView`中，然后再将`Activity`和`SkinView`进行数据绑定加入`SkinManager`中，完成换肤控件的捕获。
- **应用内换肤**：通过给一套对应的资源添加`后缀名`

学习自：[Android-实用的App换肤功能](http://www.imooc.com/learn/782)





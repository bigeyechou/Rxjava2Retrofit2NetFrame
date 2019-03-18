### 如果帮到您了能不能给个赞，赞多的话我会抽时间继续给大家更新支持的~
### 项目介绍
    NetWorkFrame：Android网络请求框架
    down下来一定要配上自己的接口名，URLConstant和HttpApi的post/get()中！！！！！
    down下来一定要配上自己的接口名，URLConstant和HttpApi的post/get()中！！！！！
    down下来一定要配上自己的接口名，URLConstant和HttpApi的post/get()中！！！！！

### 软件架构
    基于Retrofit+Rxjava+Okhttp封装的网络请求框架
    使用时请自行修改okhttp的公共参数（HeaderParam）
    1.HttpService请求的接口
    2.HttpMethods类：初始化并配置Retrofit和OkHttp
    3.OnSuccessAndFaultSub类：封装回调,项目中接收的是gzip压缩过的流
    4.各种Api类：根据业务模块划分，实现观察者和被观察者的订阅。
    
### 使用说明
    可直接使用
    集成的接口获取的数据需要在OnSuccessAndFaultSub.class文件的onNext方法看（因为接口只是为了测试Retrofit，框架是根据自己公司的接口规则封装的）
    详情使用介绍请查看简书：https://www.jianshu.com/p/0ad99e598dba  
    喜欢点赞支持一下
    
### 应用实例
    https://github.com/bigeyechou/ChouMediaPlayer 请参考我git上的视频播放项目，集成了此网络请求框架（network），仅供下载参考。

### 版本
**1.1：** 更新了RxJava和RxAndroid的版本（2.0+），语法区别和一些操作符有做改变。


#####接口不得以任何形式转载，项目可随意使用，注明出处。
